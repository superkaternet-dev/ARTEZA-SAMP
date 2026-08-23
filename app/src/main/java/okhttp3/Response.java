/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import okhttp3.CacheControl;
import okhttp3.Challenge;
import okhttp3.Handshake;
import okhttp3.Headers;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.internal.http.OkHeaders;
import okio.Buffer;
import okio.BufferedSource;

public final class Response
implements Closeable {
    private final ResponseBody body;
    private volatile CacheControl cacheControl;
    private final Response cacheResponse;
    private final int code;
    private final Handshake handshake;
    private final Headers headers;
    private final String message;
    private final Response networkResponse;
    private final Response priorResponse;
    private final Protocol protocol;
    private final long receivedResponseAtMillis;
    private final Request request;
    private final long sentRequestAtMillis;

    private Response(Builder builder) {
        this.request = builder.request;
        this.protocol = builder.protocol;
        this.code = builder.code;
        this.message = builder.message;
        this.handshake = builder.handshake;
        this.headers = builder.headers.build();
        this.body = builder.body;
        this.networkResponse = builder.networkResponse;
        this.cacheResponse = builder.cacheResponse;
        this.priorResponse = builder.priorResponse;
        this.sentRequestAtMillis = builder.sentRequestAtMillis;
        this.receivedResponseAtMillis = builder.receivedResponseAtMillis;
    }

    public ResponseBody body() {
        return this.body;
    }

    public CacheControl cacheControl() {
        CacheControl cacheControl = this.cacheControl;
        if (cacheControl == null) {
            this.cacheControl = cacheControl = CacheControl.parse(this.headers);
        }
        return cacheControl;
    }

    public Response cacheResponse() {
        return this.cacheResponse;
    }

    public List<Challenge> challenges() {
        block4: {
            String string2;
            block3: {
                int n;
                block2: {
                    n = this.code;
                    if (n != 401) break block2;
                    string2 = "WWW-Authenticate";
                    break block3;
                }
                if (n != 407) break block4;
                string2 = "Proxy-Authenticate";
            }
            return OkHeaders.parseChallenges(this.headers(), string2);
        }
        return Collections.emptyList();
    }

    @Override
    public void close() {
        this.body.close();
    }

    public int code() {
        return this.code;
    }

    public Handshake handshake() {
        return this.handshake;
    }

    public String header(String string2) {
        return this.header(string2, null);
    }

    public String header(String string2, String string3) {
        if ((string2 = this.headers.get(string2)) == null) {
            string2 = string3;
        }
        return string2;
    }

    public List<String> headers(String string2) {
        return this.headers.values(string2);
    }

    public Headers headers() {
        return this.headers;
    }

    public boolean isRedirect() {
        switch (this.code) {
            default: {
                return false;
            }
            case 300: 
            case 301: 
            case 302: 
            case 303: 
            case 307: 
            case 308: 
        }
        return true;
    }

    public boolean isSuccessful() {
        int n = this.code;
        boolean bl = n >= 200 && n < 300;
        return bl;
    }

    public String message() {
        return this.message;
    }

    public Response networkResponse() {
        return this.networkResponse;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public ResponseBody peekBody(long l) throws IOException {
        Object object = this.body.source();
        object.request(l);
        Object object2 = object.buffer().clone();
        if (((Buffer)object2).size() > l) {
            object = new Buffer();
            ((Buffer)object).write((Buffer)object2, l);
            ((Buffer)object2).clear();
        } else {
            object = object2;
        }
        return ResponseBody.create(this.body.contentType(), ((Buffer)object).size(), (BufferedSource)object);
    }

    public Response priorResponse() {
        return this.priorResponse;
    }

    public Protocol protocol() {
        return this.protocol;
    }

    public long receivedResponseAtMillis() {
        return this.receivedResponseAtMillis;
    }

    public Request request() {
        return this.request;
    }

    public long sentRequestAtMillis() {
        return this.sentRequestAtMillis;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Response{protocol=");
        stringBuilder.append((Object)this.protocol);
        stringBuilder.append(", code=");
        stringBuilder.append(this.code);
        stringBuilder.append(", message=");
        stringBuilder.append(this.message);
        stringBuilder.append(", url=");
        stringBuilder.append(this.request.url());
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public static class Builder {
        private ResponseBody body;
        private Response cacheResponse;
        private int code = -1;
        private Handshake handshake;
        private Headers.Builder headers;
        private String message;
        private Response networkResponse;
        private Response priorResponse;
        private Protocol protocol;
        private long receivedResponseAtMillis;
        private Request request;
        private long sentRequestAtMillis;

        public Builder() {
            this.headers = new Headers.Builder();
        }

        private Builder(Response response) {
            this.request = response.request;
            this.protocol = response.protocol;
            this.code = response.code;
            this.message = response.message;
            this.handshake = response.handshake;
            this.headers = response.headers.newBuilder();
            this.body = response.body;
            this.networkResponse = response.networkResponse;
            this.cacheResponse = response.cacheResponse;
            this.priorResponse = response.priorResponse;
            this.sentRequestAtMillis = response.sentRequestAtMillis;
            this.receivedResponseAtMillis = response.receivedResponseAtMillis;
        }

        private void checkPriorResponse(Response response) {
            if (response.body == null) {
                return;
            }
            throw new IllegalArgumentException("priorResponse.body != null");
        }

        private void checkSupportResponse(String string2, Response object) {
            if (((Response)object).body == null) {
                if (((Response)object).networkResponse == null) {
                    if (((Response)object).cacheResponse == null) {
                        if (((Response)object).priorResponse == null) {
                            return;
                        }
                        object = new StringBuilder();
                        ((StringBuilder)object).append(string2);
                        ((StringBuilder)object).append(".priorResponse != null");
                        throw new IllegalArgumentException(((StringBuilder)object).toString());
                    }
                    object = new StringBuilder();
                    ((StringBuilder)object).append(string2);
                    ((StringBuilder)object).append(".cacheResponse != null");
                    throw new IllegalArgumentException(((StringBuilder)object).toString());
                }
                object = new StringBuilder();
                ((StringBuilder)object).append(string2);
                ((StringBuilder)object).append(".networkResponse != null");
                throw new IllegalArgumentException(((StringBuilder)object).toString());
            }
            object = new StringBuilder();
            ((StringBuilder)object).append(string2);
            ((StringBuilder)object).append(".body != null");
            throw new IllegalArgumentException(((StringBuilder)object).toString());
        }

        public Builder addHeader(String string2, String string3) {
            this.headers.add(string2, string3);
            return this;
        }

        public Builder body(ResponseBody responseBody) {
            this.body = responseBody;
            return this;
        }

        public Response build() {
            if (this.request != null) {
                if (this.protocol != null) {
                    if (this.code >= 0) {
                        return new Response(this);
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("code < 0: ");
                    stringBuilder.append(this.code);
                    throw new IllegalStateException(stringBuilder.toString());
                }
                throw new IllegalStateException("protocol == null");
            }
            throw new IllegalStateException("request == null");
        }

        public Builder cacheResponse(Response response) {
            if (response != null) {
                this.checkSupportResponse("cacheResponse", response);
            }
            this.cacheResponse = response;
            return this;
        }

        public Builder code(int n) {
            this.code = n;
            return this;
        }

        public Builder handshake(Handshake handshake) {
            this.handshake = handshake;
            return this;
        }

        public Builder header(String string2, String string3) {
            this.headers.set(string2, string3);
            return this;
        }

        public Builder headers(Headers headers) {
            this.headers = headers.newBuilder();
            return this;
        }

        public Builder message(String string2) {
            this.message = string2;
            return this;
        }

        public Builder networkResponse(Response response) {
            if (response != null) {
                this.checkSupportResponse("networkResponse", response);
            }
            this.networkResponse = response;
            return this;
        }

        public Builder priorResponse(Response response) {
            if (response != null) {
                this.checkPriorResponse(response);
            }
            this.priorResponse = response;
            return this;
        }

        public Builder protocol(Protocol protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder receivedResponseAtMillis(long l) {
            this.receivedResponseAtMillis = l;
            return this;
        }

        public Builder removeHeader(String string2) {
            this.headers.removeAll(string2);
            return this;
        }

        public Builder request(Request request) {
            this.request = request;
            return this;
        }

        public Builder sentRequestAtMillis(long l) {
            this.sentRequestAtMillis = l;
            return this;
        }
    }
}

