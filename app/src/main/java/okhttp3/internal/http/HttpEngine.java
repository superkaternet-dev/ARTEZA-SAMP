/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.http;

import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.Address;
import okhttp3.CertificatePinner;
import okhttp3.Connection;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Route;
import okhttp3.internal.Internal;
import okhttp3.internal.InternalCache;
import okhttp3.internal.Util;
import okhttp3.internal.Version;
import okhttp3.internal.http.CacheRequest;
import okhttp3.internal.http.CacheStrategy;
import okhttp3.internal.http.HttpMethod;
import okhttp3.internal.http.HttpStream;
import okhttp3.internal.http.OkHeaders;
import okhttp3.internal.http.RealResponseBody;
import okhttp3.internal.http.RequestException;
import okhttp3.internal.http.RetryableSink;
import okhttp3.internal.http.RouteException;
import okhttp3.internal.http.StreamAllocation;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;
import okio.Sink;
import okio.Source;
import okio.Timeout;

public final class HttpEngine {
    private static final ResponseBody EMPTY_BODY = new ResponseBody(){

        @Override
        public long contentLength() {
            return 0L;
        }

        @Override
        public MediaType contentType() {
            return null;
        }

        @Override
        public BufferedSource source() {
            return new Buffer();
        }
    };
    public static final int MAX_FOLLOW_UPS = 20;
    public final boolean bufferRequestBody;
    private BufferedSink bufferedRequestBody;
    private Response cacheResponse;
    private CacheStrategy cacheStrategy;
    private final boolean callerWritesRequestBody;
    final OkHttpClient client;
    private final boolean forWebSocket;
    private HttpStream httpStream;
    private Request networkRequest;
    private final Response priorResponse;
    private Sink requestBodyOut;
    long sentRequestMillis = -1L;
    private CacheRequest storeRequest;
    public final StreamAllocation streamAllocation;
    private boolean transparentGzip;
    private final Request userRequest;
    private Response userResponse;

    public HttpEngine(OkHttpClient object, Request request, boolean bl, boolean bl2, boolean bl3, StreamAllocation streamAllocation, RetryableSink retryableSink, Response response) {
        this.client = object;
        this.userRequest = request;
        this.bufferRequestBody = bl;
        this.callerWritesRequestBody = bl2;
        this.forWebSocket = bl3;
        object = streamAllocation != null ? streamAllocation : new StreamAllocation(((OkHttpClient)object).connectionPool(), HttpEngine.createAddress((OkHttpClient)object, request));
        this.streamAllocation = object;
        this.requestBodyOut = retryableSink;
        this.priorResponse = response;
    }

    static /* synthetic */ Request access$102(HttpEngine httpEngine, Request request) {
        httpEngine.networkRequest = request;
        return request;
    }

    private Response cacheWritingResponse(CacheRequest object, Response response) throws IOException {
        if (object == null) {
            return response;
        }
        Sink sink = object.body();
        if (sink == null) {
            return response;
        }
        object = new Source(this, response.body().source(), (CacheRequest)object, Okio.buffer(sink)){
            boolean cacheRequestClosed;
            final HttpEngine this$0;
            final BufferedSink val$cacheBody;
            final CacheRequest val$cacheRequest;
            final BufferedSource val$source;
            {
                this.this$0 = httpEngine;
                this.val$source = bufferedSource;
                this.val$cacheRequest = cacheRequest;
                this.val$cacheBody = bufferedSink;
            }

            @Override
            public void close() throws IOException {
                if (!this.cacheRequestClosed && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
                    this.cacheRequestClosed = true;
                    this.val$cacheRequest.abort();
                }
                this.val$source.close();
            }

            @Override
            public long read(Buffer buffer, long l) throws IOException {
                block3: {
                    block4: {
                        try {
                            l = this.val$source.read(buffer, l);
                            if (l != -1L) break block3;
                            if (this.cacheRequestClosed) break block4;
                            this.cacheRequestClosed = true;
                        }
                        catch (IOException iOException) {
                            if (!this.cacheRequestClosed) {
                                this.cacheRequestClosed = true;
                                this.val$cacheRequest.abort();
                            }
                            throw iOException;
                        }
                        this.val$cacheBody.close();
                    }
                    return -1L;
                }
                buffer.copyTo(this.val$cacheBody.buffer(), buffer.size() - l, l);
                this.val$cacheBody.emitCompleteSegments();
                return l;
            }

            @Override
            public Timeout timeout() {
                return this.val$source.timeout();
            }
        };
        return response.newBuilder().body(new RealResponseBody(response.headers(), Okio.buffer((Source)object))).build();
    }

    private static Headers combine(Headers object, Headers headers) throws IOException {
        int n;
        Headers.Builder builder = new Headers.Builder();
        int n2 = ((Headers)object).size();
        for (n = 0; n < n2; ++n) {
            String string2 = ((Headers)object).name(n);
            String string3 = ((Headers)object).value(n);
            if ("Warning".equalsIgnoreCase(string2) && string3.startsWith("1") || OkHeaders.isEndToEnd(string2) && headers.get(string2) != null) continue;
            builder.add(string2, string3);
        }
        n2 = headers.size();
        for (n = 0; n < n2; ++n) {
            object = headers.name(n);
            if ("Content-Length".equalsIgnoreCase((String)object) || !OkHeaders.isEndToEnd((String)object)) continue;
            builder.add((String)object, headers.value(n));
        }
        return builder.build();
    }

    private HttpStream connect() throws RouteException, RequestException, IOException {
        boolean bl = this.networkRequest.method().equals("GET");
        return this.streamAllocation.newStream(this.client.connectTimeoutMillis(), this.client.readTimeoutMillis(), this.client.writeTimeoutMillis(), this.client.retryOnConnectionFailure(), bl ^ true);
    }

    private String cookieHeader(List<Cookie> list) {
        StringBuilder stringBuilder = new StringBuilder();
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            if (i > 0) {
                stringBuilder.append("; ");
            }
            Cookie cookie = list.get(i);
            stringBuilder.append(cookie.name());
            stringBuilder.append('=');
            stringBuilder.append(cookie.value());
        }
        return stringBuilder.toString();
    }

    private static Address createAddress(OkHttpClient okHttpClient, Request request) {
        SSLSocketFactory sSLSocketFactory = null;
        HostnameVerifier hostnameVerifier = null;
        CertificatePinner certificatePinner = null;
        if (request.isHttps()) {
            sSLSocketFactory = okHttpClient.sslSocketFactory();
            hostnameVerifier = okHttpClient.hostnameVerifier();
            certificatePinner = okHttpClient.certificatePinner();
        }
        return new Address(request.url().host(), request.url().port(), okHttpClient.dns(), okHttpClient.socketFactory(), sSLSocketFactory, hostnameVerifier, certificatePinner, okHttpClient.proxyAuthenticator(), okHttpClient.proxy(), okHttpClient.protocols(), okHttpClient.connectionSpecs(), okHttpClient.proxySelector());
    }

    public static boolean hasBody(Response response) {
        if (response.request().method().equals("HEAD")) {
            return false;
        }
        int n = response.code();
        if ((n < 100 || n >= 200) && n != 204 && n != 304) {
            return true;
        }
        return OkHeaders.contentLength(response) != -1L || "chunked".equalsIgnoreCase(response.header("Transfer-Encoding"));
        {
        }
    }

    private boolean isRecoverable(IOException iOException, boolean bl) {
        boolean bl2 = iOException instanceof ProtocolException;
        boolean bl3 = false;
        if (bl2) {
            return false;
        }
        if (iOException instanceof InterruptedIOException) {
            bl2 = bl3;
            if (iOException instanceof SocketTimeoutException) {
                bl2 = bl3;
                if (bl) {
                    bl2 = true;
                }
            }
            return bl2;
        }
        if (iOException instanceof SSLHandshakeException && iOException.getCause() instanceof CertificateException) {
            return false;
        }
        return !(iOException instanceof SSLPeerUnverifiedException);
    }

    private void maybeCache() throws IOException {
        InternalCache internalCache = Internal.instance.internalCache(this.client);
        if (internalCache == null) {
            return;
        }
        if (!CacheStrategy.isCacheable(this.userResponse, this.networkRequest)) {
            if (HttpMethod.invalidatesCache(this.networkRequest.method())) {
                try {
                    internalCache.remove(this.networkRequest);
                }
                catch (IOException iOException) {
                    // empty catch block
                }
            }
            return;
        }
        this.storeRequest = internalCache.put(this.userResponse);
    }

    private Request networkRequest(Request request) throws IOException {
        List<Cookie> list;
        Request.Builder builder = request.newBuilder();
        if (request.header("Host") == null) {
            builder.header("Host", Util.hostHeader(request.url(), false));
        }
        if (request.header("Connection") == null) {
            builder.header("Connection", "Keep-Alive");
        }
        if (request.header("Accept-Encoding") == null) {
            this.transparentGzip = true;
            builder.header("Accept-Encoding", "gzip");
        }
        if (!(list = this.client.cookieJar().loadForRequest(request.url())).isEmpty()) {
            builder.header("Cookie", this.cookieHeader(list));
        }
        if (request.header("User-Agent") == null) {
            builder.header("User-Agent", Version.userAgent());
        }
        return builder.build();
    }

    private Response readNetworkResponse() throws IOException {
        Response response;
        this.httpStream.finishRequest();
        Response response2 = response = this.httpStream.readResponseHeaders().request(this.networkRequest).handshake(this.streamAllocation.connection().handshake()).sentRequestAtMillis(this.sentRequestMillis).receivedResponseAtMillis(System.currentTimeMillis()).build();
        if (!this.forWebSocket) {
            response2 = response.newBuilder().body(this.httpStream.openResponseBody(response)).build();
        }
        if ("close".equalsIgnoreCase(response2.request().header("Connection")) || "close".equalsIgnoreCase(response2.header("Connection"))) {
            this.streamAllocation.noNewStreams();
        }
        return response2;
    }

    private static Response stripBody(Response response) {
        block0: {
            if (response == null || response.body() == null) break block0;
            response = response.newBuilder().body(null).build();
        }
        return response;
    }

    private Response unzip(Response response) throws IOException {
        if (this.transparentGzip && "gzip".equalsIgnoreCase(this.userResponse.header("Content-Encoding"))) {
            if (response.body() == null) {
                return response;
            }
            GzipSource gzipSource = new GzipSource(response.body().source());
            Headers headers = response.headers().newBuilder().removeAll("Content-Encoding").removeAll("Content-Length").build();
            return response.newBuilder().headers(headers).body(new RealResponseBody(headers, Okio.buffer(gzipSource))).build();
        }
        return response;
    }

    private static boolean validate(Response object, Response object2) {
        if (((Response)object2).code() == 304) {
            return true;
        }
        return (object = ((Response)object).headers().getDate("Last-Modified")) != null && (object2 = ((Response)object2).headers().getDate("Last-Modified")) != null && ((Date)object2).getTime() < ((Date)object).getTime();
    }

    private boolean writeRequestHeadersEagerly() {
        boolean bl = this.callerWritesRequestBody && this.permitsRequestBody(this.networkRequest) && this.requestBodyOut == null;
        return bl;
    }

    public void cancel() {
        this.streamAllocation.cancel();
    }

    public StreamAllocation close() {
        Closeable closeable = this.bufferedRequestBody;
        if (closeable != null) {
            Util.closeQuietly(closeable);
        } else {
            closeable = this.requestBodyOut;
            if (closeable != null) {
                Util.closeQuietly(closeable);
            }
        }
        closeable = this.userResponse;
        if (closeable != null) {
            Util.closeQuietly(((Response)closeable).body());
        } else {
            this.streamAllocation.streamFailed(null);
        }
        return this.streamAllocation;
    }

    public Request followUpRequest() throws IOException {
        if (this.userResponse != null) {
            Object object = this.streamAllocation.connection();
            object = object != null ? object.route() : null;
            int n = this.userResponse.code();
            Object object2 = this.userRequest.method();
            switch (n) {
                default: {
                    return null;
                }
                case 408: {
                    object = this.requestBodyOut;
                    n = object != null && !(object instanceof RetryableSink) ? 0 : 1;
                    if (this.callerWritesRequestBody && n == 0) {
                        return null;
                    }
                    return this.userRequest;
                }
                case 407: {
                    object2 = object != null ? ((Route)object).proxy() : this.client.proxy();
                    if (((Proxy)object2).type() == Proxy.Type.HTTP) {
                        return this.client.proxyAuthenticator().authenticate((Route)object, this.userResponse);
                    }
                    throw new ProtocolException("Received HTTP_PROXY_AUTH (407) code while not using proxy");
                }
                case 401: {
                    return this.client.authenticator().authenticate((Route)object, this.userResponse);
                }
                case 307: 
                case 308: {
                    if (((String)object2).equals("GET") || ((String)object2).equals("HEAD")) break;
                    return null;
                }
                case 300: 
                case 301: 
                case 302: 
                case 303: 
            }
            if (!this.client.followRedirects()) {
                return null;
            }
            object = this.userResponse.header("Location");
            if (object == null) {
                return null;
            }
            object = this.userRequest.url().resolve((String)object);
            if (object == null) {
                return null;
            }
            if (!((HttpUrl)object).scheme().equals(this.userRequest.url().scheme()) && !this.client.followSslRedirects()) {
                return null;
            }
            Request.Builder builder = this.userRequest.newBuilder();
            if (HttpMethod.permitsRequestBody((String)object2)) {
                if (HttpMethod.redirectsToGet((String)object2)) {
                    builder.method("GET", null);
                } else {
                    builder.method((String)object2, null);
                }
                builder.removeHeader("Transfer-Encoding");
                builder.removeHeader("Content-Length");
                builder.removeHeader("Content-Type");
            }
            if (!this.sameConnection((HttpUrl)object)) {
                builder.removeHeader("Authorization");
            }
            return builder.url((HttpUrl)object).build();
        }
        throw new IllegalStateException();
    }

    public BufferedSink getBufferedRequestBody() {
        Sink sink = this.bufferedRequestBody;
        if (sink != null) {
            return sink;
        }
        sink = this.getRequestBody();
        if (sink != null) {
            this.bufferedRequestBody = sink = Okio.buffer(sink);
        } else {
            sink = null;
        }
        return sink;
    }

    public Connection getConnection() {
        return this.streamAllocation.connection();
    }

    public Request getRequest() {
        return this.userRequest;
    }

    public Sink getRequestBody() {
        if (this.cacheStrategy != null) {
            return this.requestBodyOut;
        }
        throw new IllegalStateException();
    }

    public Response getResponse() {
        Response response = this.userResponse;
        if (response != null) {
            return response;
        }
        throw new IllegalStateException();
    }

    public boolean hasResponse() {
        boolean bl = this.userResponse != null;
        return bl;
    }

    boolean permitsRequestBody(Request request) {
        return HttpMethod.permitsRequestBody(request.method());
    }

    public void readResponse() throws IOException {
        Closeable closeable;
        if (this.userResponse != null) {
            return;
        }
        Object object = this.networkRequest;
        if (object == null && this.cacheResponse == null) {
            throw new IllegalStateException("call sendRequest() first!");
        }
        if (object == null) {
            return;
        }
        if (this.forWebSocket) {
            this.httpStream.writeRequestHeaders((Request)object);
            object = this.readNetworkResponse();
        } else if (!this.callerWritesRequestBody) {
            object = new NetworkInterceptorChain(this, 0, (Request)object, this.streamAllocation.connection()).proceed(this.networkRequest);
        } else {
            object = this.bufferedRequestBody;
            if (object != null && object.buffer().size() > 0L) {
                this.bufferedRequestBody.emit();
            }
            if (this.sentRequestMillis == -1L) {
                if (OkHeaders.contentLength(this.networkRequest) == -1L && (object = this.requestBodyOut) instanceof RetryableSink) {
                    long l = ((RetryableSink)object).contentLength();
                    this.networkRequest = this.networkRequest.newBuilder().header("Content-Length", Long.toString(l)).build();
                }
                this.httpStream.writeRequestHeaders(this.networkRequest);
            }
            if ((closeable = this.requestBodyOut) != null) {
                object = this.bufferedRequestBody;
                if (object != null) {
                    object.close();
                } else {
                    closeable.close();
                }
                object = this.requestBodyOut;
                if (object instanceof RetryableSink) {
                    this.httpStream.writeRequestBody((RetryableSink)object);
                }
            }
            object = this.readNetworkResponse();
        }
        this.receiveHeaders(((Response)object).headers());
        closeable = this.cacheResponse;
        if (closeable != null) {
            if (HttpEngine.validate((Response)closeable, (Response)object)) {
                this.userResponse = this.cacheResponse.newBuilder().request(this.userRequest).priorResponse(HttpEngine.stripBody(this.priorResponse)).headers(HttpEngine.combine(this.cacheResponse.headers(), ((Response)object).headers())).cacheResponse(HttpEngine.stripBody(this.cacheResponse)).networkResponse(HttpEngine.stripBody((Response)object)).build();
                ((Response)object).body().close();
                this.releaseStreamAllocation();
                object = Internal.instance.internalCache(this.client);
                object.trackConditionalCacheHit();
                object.update(this.cacheResponse, this.userResponse);
                this.userResponse = this.unzip(this.userResponse);
                return;
            }
            Util.closeQuietly(this.cacheResponse.body());
        }
        this.userResponse = object = ((Response)object).newBuilder().request(this.userRequest).priorResponse(HttpEngine.stripBody(this.priorResponse)).cacheResponse(HttpEngine.stripBody(this.cacheResponse)).networkResponse(HttpEngine.stripBody((Response)object)).build();
        if (HttpEngine.hasBody((Response)object)) {
            this.maybeCache();
            this.userResponse = this.unzip(this.cacheWritingResponse(this.storeRequest, this.userResponse));
        }
    }

    public void receiveHeaders(Headers list) throws IOException {
        if (this.client.cookieJar() == CookieJar.NO_COOKIES) {
            return;
        }
        list = Cookie.parseAll(this.userRequest.url(), (Headers)((Object)list));
        if (list.isEmpty()) {
            return;
        }
        this.client.cookieJar().saveFromResponse(this.userRequest.url(), list);
    }

    public HttpEngine recover(IOException iOException, boolean bl) {
        return this.recover(iOException, bl, this.requestBodyOut);
    }

    public HttpEngine recover(IOException object, boolean bl, Sink sink) {
        this.streamAllocation.streamFailed((IOException)object);
        if (!this.client.retryOnConnectionFailure()) {
            return null;
        }
        if (sink != null && !(sink instanceof RetryableSink)) {
            return null;
        }
        if (!this.isRecoverable((IOException)object, bl)) {
            return null;
        }
        if (!this.streamAllocation.hasMoreRoutes()) {
            return null;
        }
        object = this.close();
        return new HttpEngine(this.client, this.userRequest, this.bufferRequestBody, this.callerWritesRequestBody, this.forWebSocket, (StreamAllocation)object, (RetryableSink)sink, this.priorResponse);
    }

    public void releaseStreamAllocation() throws IOException {
        this.streamAllocation.release();
    }

    public boolean sameConnection(HttpUrl httpUrl) {
        HttpUrl httpUrl2 = this.userRequest.url();
        boolean bl = httpUrl2.host().equals(httpUrl.host()) && httpUrl2.port() == httpUrl.port() && httpUrl2.scheme().equals(httpUrl.scheme());
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void sendRequest() throws RequestException, RouteException, IOException {
        CacheStrategy cacheStrategy;
        if (this.cacheStrategy != null) {
            return;
        }
        if (this.httpStream != null) throw new IllegalStateException();
        Object object = this.networkRequest(this.userRequest);
        Object object2 = Internal.instance.internalCache(this.client);
        Response response = object2 != null ? object2.get((Request)object) : null;
        this.cacheStrategy = cacheStrategy = new CacheStrategy.Factory(System.currentTimeMillis(), (Request)object, response).get();
        this.networkRequest = cacheStrategy.networkRequest;
        this.cacheResponse = this.cacheStrategy.cacheResponse;
        if (object2 != null) {
            object2.trackResponse(this.cacheStrategy);
        }
        if (response != null && this.cacheResponse == null) {
            Util.closeQuietly(response.body());
        }
        if ((object2 = this.networkRequest) == null && this.cacheResponse == null) {
            this.userResponse = new Response.Builder().request(this.userRequest).priorResponse(HttpEngine.stripBody(this.priorResponse)).protocol(Protocol.HTTP_1_1).code(504).message("Unsatisfiable Request (only-if-cached)").body(EMPTY_BODY).sentRequestAtMillis(this.sentRequestMillis).receivedResponseAtMillis(System.currentTimeMillis()).build();
            return;
        }
        if (object2 == null) {
            this.userResponse = response = this.cacheResponse.newBuilder().request(this.userRequest).priorResponse(HttpEngine.stripBody(this.priorResponse)).cacheResponse(HttpEngine.stripBody(this.cacheResponse)).build();
            this.userResponse = this.unzip(response);
            return;
        }
        try {
            this.httpStream = object2 = this.connect();
            object2.setHttpEngine(this);
            if (!this.writeRequestHeadersEagerly()) return;
            long l = OkHeaders.contentLength((Request)object);
            if (!this.bufferRequestBody) {
                this.httpStream.writeRequestHeaders(this.networkRequest);
                this.requestBodyOut = this.httpStream.createRequestBody(this.networkRequest, l);
                return;
            }
            if (l > Integer.MAX_VALUE) {
                object = new IllegalStateException("Use setFixedLengthStreamingMode() or setChunkedStreamingMode() for requests larger than 2 GiB.");
                throw object;
            }
            if (l != -1L) {
                this.httpStream.writeRequestHeaders(this.networkRequest);
                this.requestBodyOut = object = new RetryableSink((int)l);
                return;
            }
            this.requestBodyOut = object = new RetryableSink();
            return;
        }
        catch (Throwable throwable) {
            if (response == null) throw throwable;
            Util.closeQuietly(response.body());
            throw throwable;
        }
    }

    public void writingRequestHeaders() {
        if (this.sentRequestMillis == -1L) {
            this.sentRequestMillis = System.currentTimeMillis();
            return;
        }
        throw new IllegalStateException();
    }

    class NetworkInterceptorChain
    implements Interceptor.Chain {
        private int calls;
        private final Connection connection;
        private final int index;
        private final Request request;
        final HttpEngine this$0;

        NetworkInterceptorChain(HttpEngine httpEngine, int n, Request request, Connection connection) {
            this.this$0 = httpEngine;
            this.index = n;
            this.request = request;
            this.connection = connection;
        }

        @Override
        public Connection connection() {
            return this.connection;
        }

        @Override
        public Response proceed(Request object) throws IOException {
            int n;
            Object object2;
            Object object3;
            ++this.calls;
            if (this.index > 0) {
                object3 = this.this$0.client.networkInterceptors().get(this.index - 1);
                object2 = this.connection().route().address();
                if (((Request)object).url().host().equals(((Address)object2).url().host()) && ((Request)object).url().port() == ((Address)object2).url().port()) {
                    if (this.calls > 1) {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("network interceptor ");
                        ((StringBuilder)object).append(object3);
                        ((StringBuilder)object).append(" must call proceed() exactly once");
                        throw new IllegalStateException(((StringBuilder)object).toString());
                    }
                } else {
                    object = new StringBuilder();
                    ((StringBuilder)object).append("network interceptor ");
                    ((StringBuilder)object).append(object3);
                    ((StringBuilder)object).append(" must retain the same host and port");
                    throw new IllegalStateException(((StringBuilder)object).toString());
                }
            }
            if (this.index < this.this$0.client.networkInterceptors().size()) {
                object2 = new NetworkInterceptorChain(this.this$0, this.index + 1, (Request)object, this.connection);
                object = this.this$0.client.networkInterceptors().get(this.index);
                object3 = object.intercept((Interceptor.Chain)object2);
                if (((NetworkInterceptorChain)object2).calls == 1) {
                    if (object3 != null) {
                        return object3;
                    }
                    object3 = new StringBuilder();
                    ((StringBuilder)object3).append("network interceptor ");
                    ((StringBuilder)object3).append(object);
                    ((StringBuilder)object3).append(" returned null");
                    throw new NullPointerException(((StringBuilder)object3).toString());
                }
                object3 = new StringBuilder();
                ((StringBuilder)object3).append("network interceptor ");
                ((StringBuilder)object3).append(object);
                ((StringBuilder)object3).append(" must call proceed() exactly once");
                throw new IllegalStateException(((StringBuilder)object3).toString());
            }
            this.this$0.httpStream.writeRequestHeaders((Request)object);
            HttpEngine.access$102(this.this$0, (Request)object);
            if (this.this$0.permitsRequestBody((Request)object) && ((Request)object).body() != null) {
                object3 = Okio.buffer(this.this$0.httpStream.createRequestBody((Request)object, ((Request)object).body().contentLength()));
                ((Request)object).body().writeTo((BufferedSink)object3);
                object3.close();
            }
            if ((n = ((Response)(object3 = this.this$0.readNetworkResponse())).code()) != 204 && n != 205 || ((Response)object3).body().contentLength() <= 0L) {
                return object3;
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("HTTP ");
            ((StringBuilder)object).append(n);
            ((StringBuilder)object).append(" had non-zero Content-Length: ");
            ((StringBuilder)object).append(((Response)object3).body().contentLength());
            throw new ProtocolException(((StringBuilder)object).toString());
        }

        @Override
        public Request request() {
            return this.request;
        }
    }
}

