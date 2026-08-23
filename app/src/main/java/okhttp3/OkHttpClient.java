/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.Address;
import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CertificatePinner;
import okhttp3.ConnectionPool;
import okhttp3.ConnectionSpec;
import okhttp3.CookieJar;
import okhttp3.Dispatcher;
import okhttp3.Dns;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.RealCall;
import okhttp3.Request;
import okhttp3.internal.Internal;
import okhttp3.internal.InternalCache;
import okhttp3.internal.Platform;
import okhttp3.internal.RouteDatabase;
import okhttp3.internal.Util;
import okhttp3.internal.http.StreamAllocation;
import okhttp3.internal.io.RealConnection;
import okhttp3.internal.tls.CertificateChainCleaner;
import okhttp3.internal.tls.OkHostnameVerifier;

public class OkHttpClient
implements Cloneable,
Call.Factory {
    private static final List<ConnectionSpec> DEFAULT_CONNECTION_SPECS;
    private static final List<Protocol> DEFAULT_PROTOCOLS;
    final Authenticator authenticator;
    final Cache cache;
    final CertificateChainCleaner certificateChainCleaner;
    final CertificatePinner certificatePinner;
    final int connectTimeout;
    final ConnectionPool connectionPool;
    final List<ConnectionSpec> connectionSpecs;
    final CookieJar cookieJar;
    final Dispatcher dispatcher;
    final Dns dns;
    final boolean followRedirects;
    final boolean followSslRedirects;
    final HostnameVerifier hostnameVerifier;
    final List<Interceptor> interceptors;
    final InternalCache internalCache;
    final List<Interceptor> networkInterceptors;
    final List<Protocol> protocols;
    final Proxy proxy;
    final Authenticator proxyAuthenticator;
    final ProxySelector proxySelector;
    final int readTimeout;
    final boolean retryOnConnectionFailure;
    final SocketFactory socketFactory;
    final SSLSocketFactory sslSocketFactory;
    final int writeTimeout;

    static {
        DEFAULT_PROTOCOLS = Util.immutableList(Protocol.HTTP_2, Protocol.SPDY_3, Protocol.HTTP_1_1);
        ArrayList<ConnectionSpec> arrayList = new ArrayList<ConnectionSpec>(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS));
        if (Platform.get().isCleartextTrafficPermitted()) {
            arrayList.add(ConnectionSpec.CLEARTEXT);
        }
        DEFAULT_CONNECTION_SPECS = Util.immutableList(arrayList);
        Internal.instance = new Internal(){

            @Override
            public void addLenient(Headers.Builder builder, String string2) {
                builder.addLenient(string2);
            }

            @Override
            public void addLenient(Headers.Builder builder, String string2, String string3) {
                builder.addLenient(string2, string3);
            }

            @Override
            public void apply(ConnectionSpec connectionSpec, SSLSocket sSLSocket, boolean bl) {
                connectionSpec.apply(sSLSocket, bl);
            }

            @Override
            public StreamAllocation callEngineGetStreamAllocation(Call call) {
                return ((RealCall)call).engine.streamAllocation;
            }

            @Override
            public void callEnqueue(Call call, Callback callback, boolean bl) {
                ((RealCall)call).enqueue(callback, bl);
            }

            @Override
            public boolean connectionBecameIdle(ConnectionPool connectionPool, RealConnection realConnection) {
                return connectionPool.connectionBecameIdle(realConnection);
            }

            @Override
            public RealConnection get(ConnectionPool connectionPool, Address address, StreamAllocation streamAllocation) {
                return connectionPool.get(address, streamAllocation);
            }

            @Override
            public HttpUrl getHttpUrlChecked(String string2) throws MalformedURLException, UnknownHostException {
                return HttpUrl.getChecked(string2);
            }

            @Override
            public InternalCache internalCache(OkHttpClient okHttpClient) {
                return okHttpClient.internalCache();
            }

            @Override
            public void put(ConnectionPool connectionPool, RealConnection realConnection) {
                connectionPool.put(realConnection);
            }

            @Override
            public RouteDatabase routeDatabase(ConnectionPool connectionPool) {
                return connectionPool.routeDatabase;
            }

            @Override
            public void setCache(Builder builder, InternalCache internalCache) {
                builder.setInternalCache(internalCache);
            }
        };
    }

    public OkHttpClient() {
        this(new Builder());
    }

    private OkHttpClient(Builder builder) {
        this.dispatcher = builder.dispatcher;
        this.proxy = builder.proxy;
        this.protocols = builder.protocols;
        Object object = builder.connectionSpecs;
        this.connectionSpecs = object;
        this.interceptors = Util.immutableList(builder.interceptors);
        this.networkInterceptors = Util.immutableList(builder.networkInterceptors);
        this.proxySelector = builder.proxySelector;
        this.cookieJar = builder.cookieJar;
        this.cache = builder.cache;
        this.internalCache = builder.internalCache;
        this.socketFactory = builder.socketFactory;
        boolean bl = false;
        object = object.iterator();
        while (object.hasNext()) {
            ConnectionSpec connectionSpec = (ConnectionSpec)object.next();
            if (!bl && !connectionSpec.isTls()) {
                bl = false;
                continue;
            }
            bl = true;
        }
        if (builder.sslSocketFactory == null && bl) {
            object = this.systemDefaultTrustManager();
            this.sslSocketFactory = this.systemDefaultSslSocketFactory((X509TrustManager)object);
            this.certificateChainCleaner = CertificateChainCleaner.get((X509TrustManager)object);
        } else {
            this.sslSocketFactory = builder.sslSocketFactory;
            this.certificateChainCleaner = builder.certificateChainCleaner;
        }
        this.hostnameVerifier = builder.hostnameVerifier;
        this.certificatePinner = builder.certificatePinner.withCertificateChainCleaner(this.certificateChainCleaner);
        this.proxyAuthenticator = builder.proxyAuthenticator;
        this.authenticator = builder.authenticator;
        this.connectionPool = builder.connectionPool;
        this.dns = builder.dns;
        this.followSslRedirects = builder.followSslRedirects;
        this.followRedirects = builder.followRedirects;
        this.retryOnConnectionFailure = builder.retryOnConnectionFailure;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;
    }

    private SSLSocketFactory systemDefaultSslSocketFactory(X509TrustManager object) {
        try {
            SSLContext sSLContext = SSLContext.getInstance("TLS");
            sSLContext.init(null, new TrustManager[]{object}, null);
            object = sSLContext.getSocketFactory();
            return object;
        }
        catch (GeneralSecurityException generalSecurityException) {
            throw new AssertionError();
        }
    }

    private X509TrustManager systemDefaultTrustManager() {
        try {
            Object object = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            ((TrustManagerFactory)object).init((KeyStore)null);
            Object[] objectArray = ((TrustManagerFactory)object).getTrustManagers();
            if (objectArray.length == 1 && objectArray[0] instanceof X509TrustManager) {
                return (X509TrustManager)objectArray[0];
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unexpected default trust managers:");
            stringBuilder.append(Arrays.toString(objectArray));
            object = new IllegalStateException(stringBuilder.toString());
            throw object;
        }
        catch (GeneralSecurityException generalSecurityException) {
            throw new AssertionError();
        }
    }

    public Authenticator authenticator() {
        return this.authenticator;
    }

    public Cache cache() {
        return this.cache;
    }

    public CertificatePinner certificatePinner() {
        return this.certificatePinner;
    }

    public int connectTimeoutMillis() {
        return this.connectTimeout;
    }

    public ConnectionPool connectionPool() {
        return this.connectionPool;
    }

    public List<ConnectionSpec> connectionSpecs() {
        return this.connectionSpecs;
    }

    public CookieJar cookieJar() {
        return this.cookieJar;
    }

    public Dispatcher dispatcher() {
        return this.dispatcher;
    }

    public Dns dns() {
        return this.dns;
    }

    public boolean followRedirects() {
        return this.followRedirects;
    }

    public boolean followSslRedirects() {
        return this.followSslRedirects;
    }

    public HostnameVerifier hostnameVerifier() {
        return this.hostnameVerifier;
    }

    public List<Interceptor> interceptors() {
        return this.interceptors;
    }

    InternalCache internalCache() {
        Object object = this.cache;
        object = object != null ? ((Cache)object).internalCache : this.internalCache;
        return object;
    }

    public List<Interceptor> networkInterceptors() {
        return this.networkInterceptors;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    @Override
    public Call newCall(Request request) {
        return new RealCall(this, request);
    }

    public List<Protocol> protocols() {
        return this.protocols;
    }

    public Proxy proxy() {
        return this.proxy;
    }

    public Authenticator proxyAuthenticator() {
        return this.proxyAuthenticator;
    }

    public ProxySelector proxySelector() {
        return this.proxySelector;
    }

    public int readTimeoutMillis() {
        return this.readTimeout;
    }

    public boolean retryOnConnectionFailure() {
        return this.retryOnConnectionFailure;
    }

    public SocketFactory socketFactory() {
        return this.socketFactory;
    }

    public SSLSocketFactory sslSocketFactory() {
        return this.sslSocketFactory;
    }

    public int writeTimeoutMillis() {
        return this.writeTimeout;
    }

    public static final class Builder {
        Authenticator authenticator;
        Cache cache;
        CertificateChainCleaner certificateChainCleaner;
        CertificatePinner certificatePinner;
        int connectTimeout;
        ConnectionPool connectionPool;
        List<ConnectionSpec> connectionSpecs;
        CookieJar cookieJar;
        Dispatcher dispatcher;
        Dns dns;
        boolean followRedirects;
        boolean followSslRedirects;
        HostnameVerifier hostnameVerifier;
        final List<Interceptor> interceptors;
        InternalCache internalCache;
        final List<Interceptor> networkInterceptors;
        List<Protocol> protocols;
        Proxy proxy;
        Authenticator proxyAuthenticator;
        ProxySelector proxySelector;
        int readTimeout;
        boolean retryOnConnectionFailure;
        SocketFactory socketFactory;
        SSLSocketFactory sslSocketFactory;
        int writeTimeout;

        public Builder() {
            this.interceptors = new ArrayList<Interceptor>();
            this.networkInterceptors = new ArrayList<Interceptor>();
            this.dispatcher = new Dispatcher();
            this.protocols = DEFAULT_PROTOCOLS;
            this.connectionSpecs = DEFAULT_CONNECTION_SPECS;
            this.proxySelector = ProxySelector.getDefault();
            this.cookieJar = CookieJar.NO_COOKIES;
            this.socketFactory = SocketFactory.getDefault();
            this.hostnameVerifier = OkHostnameVerifier.INSTANCE;
            this.certificatePinner = CertificatePinner.DEFAULT;
            this.proxyAuthenticator = Authenticator.NONE;
            this.authenticator = Authenticator.NONE;
            this.connectionPool = new ConnectionPool();
            this.dns = Dns.SYSTEM;
            this.followSslRedirects = true;
            this.followRedirects = true;
            this.retryOnConnectionFailure = true;
            this.connectTimeout = 10000;
            this.readTimeout = 10000;
            this.writeTimeout = 10000;
        }

        Builder(OkHttpClient okHttpClient) {
            ArrayList<Interceptor> arrayList = new ArrayList<Interceptor>();
            this.interceptors = arrayList;
            ArrayList<Interceptor> arrayList2 = new ArrayList<Interceptor>();
            this.networkInterceptors = arrayList2;
            this.dispatcher = okHttpClient.dispatcher;
            this.proxy = okHttpClient.proxy;
            this.protocols = okHttpClient.protocols;
            this.connectionSpecs = okHttpClient.connectionSpecs;
            arrayList.addAll(okHttpClient.interceptors);
            arrayList2.addAll(okHttpClient.networkInterceptors);
            this.proxySelector = okHttpClient.proxySelector;
            this.cookieJar = okHttpClient.cookieJar;
            this.internalCache = okHttpClient.internalCache;
            this.cache = okHttpClient.cache;
            this.socketFactory = okHttpClient.socketFactory;
            this.sslSocketFactory = okHttpClient.sslSocketFactory;
            this.certificateChainCleaner = okHttpClient.certificateChainCleaner;
            this.hostnameVerifier = okHttpClient.hostnameVerifier;
            this.certificatePinner = okHttpClient.certificatePinner;
            this.proxyAuthenticator = okHttpClient.proxyAuthenticator;
            this.authenticator = okHttpClient.authenticator;
            this.connectionPool = okHttpClient.connectionPool;
            this.dns = okHttpClient.dns;
            this.followSslRedirects = okHttpClient.followSslRedirects;
            this.followRedirects = okHttpClient.followRedirects;
            this.retryOnConnectionFailure = okHttpClient.retryOnConnectionFailure;
            this.connectTimeout = okHttpClient.connectTimeout;
            this.readTimeout = okHttpClient.readTimeout;
            this.writeTimeout = okHttpClient.writeTimeout;
        }

        public Builder addInterceptor(Interceptor interceptor) {
            this.interceptors.add(interceptor);
            return this;
        }

        public Builder addNetworkInterceptor(Interceptor interceptor) {
            this.networkInterceptors.add(interceptor);
            return this;
        }

        public Builder authenticator(Authenticator authenticator) {
            if (authenticator != null) {
                this.authenticator = authenticator;
                return this;
            }
            throw new NullPointerException("authenticator == null");
        }

        public OkHttpClient build() {
            return new OkHttpClient(this);
        }

        public Builder cache(Cache cache) {
            this.cache = cache;
            this.internalCache = null;
            return this;
        }

        public Builder certificatePinner(CertificatePinner certificatePinner) {
            if (certificatePinner != null) {
                this.certificatePinner = certificatePinner;
                return this;
            }
            throw new NullPointerException("certificatePinner == null");
        }

        public Builder connectTimeout(long l, TimeUnit timeUnit) {
            if (l >= 0L) {
                if (timeUnit != null) {
                    long l2 = timeUnit.toMillis(l);
                    if (l2 <= Integer.MAX_VALUE) {
                        if (l2 == 0L && l > 0L) {
                            throw new IllegalArgumentException("Timeout too small.");
                        }
                        this.connectTimeout = (int)l2;
                        return this;
                    }
                    throw new IllegalArgumentException("Timeout too large.");
                }
                throw new NullPointerException("unit == null");
            }
            throw new IllegalArgumentException("timeout < 0");
        }

        public Builder connectionPool(ConnectionPool connectionPool) {
            if (connectionPool != null) {
                this.connectionPool = connectionPool;
                return this;
            }
            throw new NullPointerException("connectionPool == null");
        }

        public Builder connectionSpecs(List<ConnectionSpec> list) {
            this.connectionSpecs = Util.immutableList(list);
            return this;
        }

        public Builder cookieJar(CookieJar cookieJar) {
            if (cookieJar != null) {
                this.cookieJar = cookieJar;
                return this;
            }
            throw new NullPointerException("cookieJar == null");
        }

        public Builder dispatcher(Dispatcher dispatcher) {
            if (dispatcher != null) {
                this.dispatcher = dispatcher;
                return this;
            }
            throw new IllegalArgumentException("dispatcher == null");
        }

        public Builder dns(Dns dns) {
            if (dns != null) {
                this.dns = dns;
                return this;
            }
            throw new NullPointerException("dns == null");
        }

        public Builder followRedirects(boolean bl) {
            this.followRedirects = bl;
            return this;
        }

        public Builder followSslRedirects(boolean bl) {
            this.followSslRedirects = bl;
            return this;
        }

        public Builder hostnameVerifier(HostnameVerifier hostnameVerifier) {
            if (hostnameVerifier != null) {
                this.hostnameVerifier = hostnameVerifier;
                return this;
            }
            throw new NullPointerException("hostnameVerifier == null");
        }

        public List<Interceptor> interceptors() {
            return this.interceptors;
        }

        public List<Interceptor> networkInterceptors() {
            return this.networkInterceptors;
        }

        public Builder protocols(List<Protocol> list) {
            if ((list = Util.immutableList(list)).contains((Object)Protocol.HTTP_1_1)) {
                if (!list.contains((Object)Protocol.HTTP_1_0)) {
                    if (!list.contains(null)) {
                        this.protocols = Util.immutableList(list);
                        return this;
                    }
                    throw new IllegalArgumentException("protocols must not contain null");
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("protocols must not contain http/1.0: ");
                stringBuilder.append(list);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("protocols doesn't contain http/1.1: ");
            stringBuilder.append(list);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public Builder proxy(Proxy proxy) {
            this.proxy = proxy;
            return this;
        }

        public Builder proxyAuthenticator(Authenticator authenticator) {
            if (authenticator != null) {
                this.proxyAuthenticator = authenticator;
                return this;
            }
            throw new NullPointerException("proxyAuthenticator == null");
        }

        public Builder proxySelector(ProxySelector proxySelector) {
            this.proxySelector = proxySelector;
            return this;
        }

        public Builder readTimeout(long l, TimeUnit timeUnit) {
            if (l >= 0L) {
                if (timeUnit != null) {
                    long l2 = timeUnit.toMillis(l);
                    if (l2 <= Integer.MAX_VALUE) {
                        if (l2 == 0L && l > 0L) {
                            throw new IllegalArgumentException("Timeout too small.");
                        }
                        this.readTimeout = (int)l2;
                        return this;
                    }
                    throw new IllegalArgumentException("Timeout too large.");
                }
                throw new NullPointerException("unit == null");
            }
            throw new IllegalArgumentException("timeout < 0");
        }

        public Builder retryOnConnectionFailure(boolean bl) {
            this.retryOnConnectionFailure = bl;
            return this;
        }

        void setInternalCache(InternalCache internalCache) {
            this.internalCache = internalCache;
            this.cache = null;
        }

        public Builder socketFactory(SocketFactory socketFactory) {
            if (socketFactory != null) {
                this.socketFactory = socketFactory;
                return this;
            }
            throw new NullPointerException("socketFactory == null");
        }

        public Builder sslSocketFactory(SSLSocketFactory sSLSocketFactory) {
            if (sSLSocketFactory != null) {
                Object object = Platform.get().trustManager(sSLSocketFactory);
                if (object != null) {
                    this.sslSocketFactory = sSLSocketFactory;
                    this.certificateChainCleaner = CertificateChainCleaner.get((X509TrustManager)object);
                    return this;
                }
                object = new StringBuilder();
                ((StringBuilder)object).append("Unable to extract the trust manager on ");
                ((StringBuilder)object).append(Platform.get());
                ((StringBuilder)object).append(", sslSocketFactory is ");
                ((StringBuilder)object).append(sSLSocketFactory.getClass());
                throw new IllegalStateException(((StringBuilder)object).toString());
            }
            throw new NullPointerException("sslSocketFactory == null");
        }

        public Builder sslSocketFactory(SSLSocketFactory sSLSocketFactory, X509TrustManager x509TrustManager) {
            if (sSLSocketFactory != null) {
                if (x509TrustManager != null) {
                    this.sslSocketFactory = sSLSocketFactory;
                    this.certificateChainCleaner = CertificateChainCleaner.get(x509TrustManager);
                    return this;
                }
                throw new NullPointerException("trustManager == null");
            }
            throw new NullPointerException("sslSocketFactory == null");
        }

        public Builder writeTimeout(long l, TimeUnit timeUnit) {
            if (l >= 0L) {
                if (timeUnit != null) {
                    long l2 = timeUnit.toMillis(l);
                    if (l2 <= Integer.MAX_VALUE) {
                        if (l2 == 0L && l > 0L) {
                            throw new IllegalArgumentException("Timeout too small.");
                        }
                        this.writeTimeout = (int)l2;
                        return this;
                    }
                    throw new IllegalArgumentException("Timeout too large.");
                }
                throw new NullPointerException("unit == null");
            }
            throw new IllegalArgumentException("timeout < 0");
        }
    }
}

