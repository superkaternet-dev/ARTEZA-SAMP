/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.http;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import okhttp3.Headers;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okhttp3.internal.framed.ErrorCode;
import okhttp3.internal.framed.FramedConnection;
import okhttp3.internal.framed.FramedStream;
import okhttp3.internal.framed.Header;
import okhttp3.internal.http.HttpEngine;
import okhttp3.internal.http.HttpStream;
import okhttp3.internal.http.RealResponseBody;
import okhttp3.internal.http.RequestLine;
import okhttp3.internal.http.RetryableSink;
import okhttp3.internal.http.StatusLine;
import okhttp3.internal.http.StreamAllocation;
import okio.ByteString;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

public final class Http2xStream
implements HttpStream {
    private static final ByteString CONNECTION;
    private static final ByteString ENCODING;
    private static final ByteString HOST;
    private static final List<ByteString> HTTP_2_SKIPPED_REQUEST_HEADERS;
    private static final List<ByteString> HTTP_2_SKIPPED_RESPONSE_HEADERS;
    private static final ByteString KEEP_ALIVE;
    private static final ByteString PROXY_CONNECTION;
    private static final List<ByteString> SPDY_3_SKIPPED_REQUEST_HEADERS;
    private static final List<ByteString> SPDY_3_SKIPPED_RESPONSE_HEADERS;
    private static final ByteString TE;
    private static final ByteString TRANSFER_ENCODING;
    private static final ByteString UPGRADE;
    private final FramedConnection framedConnection;
    private HttpEngine httpEngine;
    private FramedStream stream;
    private final StreamAllocation streamAllocation;

    static {
        ByteString byteString;
        ByteString byteString2;
        ByteString byteString3;
        ByteString byteString4;
        ByteString byteString5;
        ByteString byteString6;
        ByteString byteString7;
        ByteString byteString8;
        CONNECTION = byteString8 = ByteString.encodeUtf8("connection");
        HOST = byteString7 = ByteString.encodeUtf8("host");
        KEEP_ALIVE = byteString6 = ByteString.encodeUtf8("keep-alive");
        PROXY_CONNECTION = byteString5 = ByteString.encodeUtf8("proxy-connection");
        TRANSFER_ENCODING = byteString4 = ByteString.encodeUtf8("transfer-encoding");
        TE = byteString3 = ByteString.encodeUtf8("te");
        ENCODING = byteString2 = ByteString.encodeUtf8("encoding");
        UPGRADE = byteString = ByteString.encodeUtf8("upgrade");
        SPDY_3_SKIPPED_REQUEST_HEADERS = Util.immutableList(byteString8, byteString7, byteString6, byteString5, byteString4, Header.TARGET_METHOD, Header.TARGET_PATH, Header.TARGET_SCHEME, Header.TARGET_AUTHORITY, Header.TARGET_HOST, Header.VERSION);
        SPDY_3_SKIPPED_RESPONSE_HEADERS = Util.immutableList(byteString8, byteString7, byteString6, byteString5, byteString4);
        HTTP_2_SKIPPED_REQUEST_HEADERS = Util.immutableList(byteString8, byteString7, byteString6, byteString5, byteString3, byteString4, byteString2, byteString, Header.TARGET_METHOD, Header.TARGET_PATH, Header.TARGET_SCHEME, Header.TARGET_AUTHORITY, Header.TARGET_HOST, Header.VERSION);
        HTTP_2_SKIPPED_RESPONSE_HEADERS = Util.immutableList(byteString8, byteString7, byteString6, byteString5, byteString3, byteString4, byteString2, byteString);
    }

    public Http2xStream(StreamAllocation streamAllocation, FramedConnection framedConnection) {
        this.streamAllocation = streamAllocation;
        this.framedConnection = framedConnection;
    }

    public static List<Header> http2HeadersList(Request object) {
        Headers headers = ((Request)object).headers();
        ArrayList<Header> arrayList = new ArrayList<Header>(headers.size() + 4);
        arrayList.add(new Header(Header.TARGET_METHOD, ((Request)object).method()));
        arrayList.add(new Header(Header.TARGET_PATH, RequestLine.requestPath(((Request)object).url())));
        arrayList.add(new Header(Header.TARGET_AUTHORITY, Util.hostHeader(((Request)object).url(), false)));
        arrayList.add(new Header(Header.TARGET_SCHEME, ((Request)object).url().scheme()));
        int n = headers.size();
        for (int i = 0; i < n; ++i) {
            object = ByteString.encodeUtf8(headers.name(i).toLowerCase(Locale.US));
            if (HTTP_2_SKIPPED_REQUEST_HEADERS.contains(object)) continue;
            arrayList.add(new Header((ByteString)object, headers.value(i)));
        }
        return arrayList;
    }

    private static String joinOnNull(String charSequence, String string2) {
        charSequence = new StringBuilder((String)charSequence);
        ((StringBuilder)charSequence).append('\u0000');
        ((StringBuilder)charSequence).append(string2);
        return ((StringBuilder)charSequence).toString();
    }

    public static Response.Builder readHttp2HeadersList(List<Header> object) throws IOException {
        String string2 = null;
        Headers.Builder builder = new Headers.Builder();
        int n = object.size();
        for (int i = 0; i < n; ++i) {
            String string3;
            ByteString byteString = object.get((int)i).name;
            String string4 = ((Header)object.get((int)i)).value.utf8();
            if (byteString.equals(Header.RESPONSE_STATUS)) {
                string3 = string4;
            } else {
                string3 = string2;
                if (!HTTP_2_SKIPPED_RESPONSE_HEADERS.contains(byteString)) {
                    builder.add(byteString.utf8(), string4);
                    string3 = string2;
                }
            }
            string2 = string3;
        }
        if (string2 != null) {
            object = new StringBuilder();
            ((StringBuilder)object).append("HTTP/1.1 ");
            ((StringBuilder)object).append(string2);
            object = StatusLine.parse(((StringBuilder)object).toString());
            return new Response.Builder().protocol(Protocol.HTTP_2).code(((StatusLine)object).code).message(((StatusLine)object).message).headers(builder.build());
        }
        object = new ProtocolException("Expected ':status' header not present");
        throw object;
    }

    public static Response.Builder readSpdy3HeadersList(List<Header> object) throws IOException {
        String string2 = null;
        String string3 = "HTTP/1.1";
        Headers.Builder builder = new Headers.Builder();
        int n = object.size();
        for (int i = 0; i < n; ++i) {
            ByteString byteString = object.get((int)i).name;
            String string4 = ((Header)object.get((int)i)).value.utf8();
            int n2 = 0;
            while (n2 < string4.length()) {
                String string5;
                String string6;
                int n3;
                int n4 = n3 = string4.indexOf(0, n2);
                if (n3 == -1) {
                    n4 = string4.length();
                }
                String string7 = string4.substring(n2, n4);
                if (byteString.equals(Header.RESPONSE_STATUS)) {
                    string6 = string7;
                    string5 = string3;
                } else if (byteString.equals(Header.VERSION)) {
                    string6 = string2;
                    string5 = string7;
                } else {
                    string6 = string2;
                    string5 = string3;
                    if (!SPDY_3_SKIPPED_RESPONSE_HEADERS.contains(byteString)) {
                        builder.add(byteString.utf8(), string7);
                        string5 = string3;
                        string6 = string2;
                    }
                }
                n2 = n4 + 1;
                string2 = string6;
                string3 = string5;
            }
        }
        if (string2 != null) {
            object = new StringBuilder();
            ((StringBuilder)object).append(string3);
            ((StringBuilder)object).append(" ");
            ((StringBuilder)object).append(string2);
            object = StatusLine.parse(((StringBuilder)object).toString());
            return new Response.Builder().protocol(Protocol.SPDY_3).code(((StatusLine)object).code).message(((StatusLine)object).message).headers(builder.build());
        }
        object = new ProtocolException("Expected ':status' header not present");
        throw object;
    }

    public static List<Header> spdy3HeadersList(Request object) {
        Headers headers = ((Request)object).headers();
        ArrayList<Header> arrayList = new ArrayList<Header>(headers.size() + 5);
        arrayList.add(new Header(Header.TARGET_METHOD, ((Request)object).method()));
        arrayList.add(new Header(Header.TARGET_PATH, RequestLine.requestPath(((Request)object).url())));
        arrayList.add(new Header(Header.VERSION, "HTTP/1.1"));
        arrayList.add(new Header(Header.TARGET_HOST, Util.hostHeader(((Request)object).url(), false)));
        arrayList.add(new Header(Header.TARGET_SCHEME, ((Request)object).url().scheme()));
        object = new LinkedHashSet();
        int n = headers.size();
        block0: for (int i = 0; i < n; ++i) {
            ByteString byteString = ByteString.encodeUtf8(headers.name(i).toLowerCase(Locale.US));
            if (SPDY_3_SKIPPED_REQUEST_HEADERS.contains(byteString)) continue;
            String string2 = headers.value(i);
            if (object.add(byteString)) {
                arrayList.add(new Header(byteString, string2));
                continue;
            }
            for (int j = 0; j < arrayList.size(); ++j) {
                if (!((Header)arrayList.get((int)j)).name.equals(byteString)) continue;
                arrayList.set(j, new Header(byteString, Http2xStream.joinOnNull(((Header)arrayList.get((int)j)).value.utf8(), string2)));
                continue block0;
            }
        }
        return arrayList;
    }

    @Override
    public void cancel() {
        FramedStream framedStream = this.stream;
        if (framedStream != null) {
            framedStream.closeLater(ErrorCode.CANCEL);
        }
    }

    @Override
    public Sink createRequestBody(Request request, long l) throws IOException {
        return this.stream.getSink();
    }

    @Override
    public void finishRequest() throws IOException {
        this.stream.getSink().close();
    }

    @Override
    public ResponseBody openResponseBody(Response response) throws IOException {
        StreamFinishingSource streamFinishingSource = new StreamFinishingSource(this, this.stream.getSource());
        return new RealResponseBody(response.headers(), Okio.buffer(streamFinishingSource));
    }

    @Override
    public Response.Builder readResponseHeaders() throws IOException {
        Response.Builder builder = this.framedConnection.getProtocol() == Protocol.HTTP_2 ? Http2xStream.readHttp2HeadersList(this.stream.getResponseHeaders()) : Http2xStream.readSpdy3HeadersList(this.stream.getResponseHeaders());
        return builder;
    }

    @Override
    public void setHttpEngine(HttpEngine httpEngine) {
        this.httpEngine = httpEngine;
    }

    @Override
    public void writeRequestBody(RetryableSink retryableSink) throws IOException {
        retryableSink.writeToSocket(this.stream.getSink());
    }

    @Override
    public void writeRequestHeaders(Request list) throws IOException {
        if (this.stream != null) {
            return;
        }
        this.httpEngine.writingRequestHeaders();
        boolean bl = this.httpEngine.permitsRequestBody((Request)((Object)list));
        list = this.framedConnection.getProtocol() == Protocol.HTTP_2 ? Http2xStream.http2HeadersList((Request)((Object)list)) : Http2xStream.spdy3HeadersList((Request)((Object)list));
        this.stream = list = this.framedConnection.newStream(list, bl, true);
        ((FramedStream)((Object)list)).readTimeout().timeout(this.httpEngine.client.readTimeoutMillis(), TimeUnit.MILLISECONDS);
        this.stream.writeTimeout().timeout(this.httpEngine.client.writeTimeoutMillis(), TimeUnit.MILLISECONDS);
    }

    class StreamFinishingSource
    extends ForwardingSource {
        final Http2xStream this$0;

        public StreamFinishingSource(Http2xStream http2xStream, Source source) {
            this.this$0 = http2xStream;
            super(source);
        }

        @Override
        public void close() throws IOException {
            this.this$0.streamAllocation.streamFinished(false, this.this$0);
            super.close();
        }
    }
}

