/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.net.URL;
import java.util.List;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;
import okhttp3.internal.http.HttpMethod;

public final class Request {
    private final RequestBody body;
    private volatile CacheControl cacheControl;
    private final Headers headers;
    private final String method;
    private final Object tag;
    private final HttpUrl url;

    private Request(Builder object) {
        this.url = ((Builder)object).url;
        this.method = ((Builder)object).method;
        this.headers = ((Builder)object).headers.build();
        this.body = ((Builder)object).body;
        object = ((Builder)object).tag != null ? ((Builder)object).tag : this;
        this.tag = object;
    }

    public RequestBody body() {
        return this.body;
    }

    public CacheControl cacheControl() {
        CacheControl cacheControl = this.cacheControl;
        if (cacheControl == null) {
            this.cacheControl = cacheControl = CacheControl.parse(this.headers);
        }
        return cacheControl;
    }

    public String header(String string2) {
        return this.headers.get(string2);
    }

    public List<String> headers(String string2) {
        return this.headers.values(string2);
    }

    public Headers headers() {
        return this.headers;
    }

    public boolean isHttps() {
        return this.url.isHttps();
    }

    public String method() {
        return this.method;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public Object tag() {
        return this.tag;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Request{method=");
        stringBuilder.append(this.method);
        stringBuilder.append(", url=");
        stringBuilder.append(this.url);
        stringBuilder.append(", tag=");
        Object object = this.tag;
        if (object == this) {
            object = null;
        }
        stringBuilder.append(object);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public HttpUrl url() {
        return this.url;
    }

    public static class Builder {
        private RequestBody body;
        private Headers.Builder headers;
        private String method;
        private Object tag;
        private HttpUrl url;

        public Builder() {
            this.method = "GET";
            this.headers = new Headers.Builder();
        }

        private Builder(Request request) {
            this.url = request.url;
            this.method = request.method;
            this.body = request.body;
            this.tag = request.tag;
            this.headers = request.headers.newBuilder();
        }

        public Builder addHeader(String string2, String string3) {
            this.headers.add(string2, string3);
            return this;
        }

        public Request build() {
            if (this.url != null) {
                return new Request(this);
            }
            throw new IllegalStateException("url == null");
        }

        public Builder cacheControl(CacheControl object) {
            if (((String)(object = ((CacheControl)object).toString())).isEmpty()) {
                return this.removeHeader("Cache-Control");
            }
            return this.header("Cache-Control", (String)object);
        }

        public Builder delete() {
            return this.delete(RequestBody.create(null, new byte[0]));
        }

        public Builder delete(RequestBody requestBody) {
            return this.method("DELETE", requestBody);
        }

        public Builder get() {
            return this.method("GET", null);
        }

        public Builder head() {
            return this.method("HEAD", null);
        }

        public Builder header(String string2, String string3) {
            this.headers.set(string2, string3);
            return this;
        }

        public Builder headers(Headers headers) {
            this.headers = headers.newBuilder();
            return this;
        }

        public Builder method(String string2, RequestBody object) {
            if (string2 != null) {
                if (string2.length() != 0) {
                    if (object != null && !HttpMethod.permitsRequestBody(string2)) {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("method ");
                        ((StringBuilder)object).append(string2);
                        ((StringBuilder)object).append(" must not have a request body.");
                        throw new IllegalArgumentException(((StringBuilder)object).toString());
                    }
                    if (object == null && HttpMethod.requiresRequestBody(string2)) {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("method ");
                        ((StringBuilder)object).append(string2);
                        ((StringBuilder)object).append(" must have a request body.");
                        throw new IllegalArgumentException(((StringBuilder)object).toString());
                    }
                    this.method = string2;
                    this.body = object;
                    return this;
                }
                throw new IllegalArgumentException("method.length() == 0");
            }
            throw new NullPointerException("method == null");
        }

        public Builder patch(RequestBody requestBody) {
            return this.method("PATCH", requestBody);
        }

        public Builder post(RequestBody requestBody) {
            return this.method("POST", requestBody);
        }

        public Builder put(RequestBody requestBody) {
            return this.method("PUT", requestBody);
        }

        public Builder removeHeader(String string2) {
            this.headers.removeAll(string2);
            return this;
        }

        public Builder tag(Object object) {
            this.tag = object;
            return this;
        }

        public Builder url(String object) {
            if (object != null) {
                CharSequence charSequence;
                if (((String)object).regionMatches(true, 0, "ws:", 0, 3)) {
                    charSequence = new StringBuilder();
                    ((StringBuilder)charSequence).append("http:");
                    ((StringBuilder)charSequence).append(((String)object).substring(3));
                    charSequence = ((StringBuilder)charSequence).toString();
                } else {
                    charSequence = object;
                    if (((String)object).regionMatches(true, 0, "wss:", 0, 4)) {
                        charSequence = new StringBuilder();
                        ((StringBuilder)charSequence).append("https:");
                        ((StringBuilder)charSequence).append(((String)object).substring(4));
                        charSequence = ((StringBuilder)charSequence).toString();
                    }
                }
                object = HttpUrl.parse((String)charSequence);
                if (object != null) {
                    return this.url((HttpUrl)object);
                }
                object = new StringBuilder();
                ((StringBuilder)object).append("unexpected url: ");
                ((StringBuilder)object).append((String)charSequence);
                throw new IllegalArgumentException(((StringBuilder)object).toString());
            }
            throw new NullPointerException("url == null");
        }

        public Builder url(URL uRL) {
            if (uRL != null) {
                Object object = HttpUrl.get(uRL);
                if (object != null) {
                    return this.url((HttpUrl)object);
                }
                object = new StringBuilder();
                ((StringBuilder)object).append("unexpected url: ");
                ((StringBuilder)object).append(uRL);
                throw new IllegalArgumentException(((StringBuilder)object).toString());
            }
            throw new NullPointerException("url == null");
        }

        public Builder url(HttpUrl httpUrl) {
            if (httpUrl != null) {
                this.url = httpUrl;
                return this;
            }
            throw new NullPointerException("url == null");
        }
    }
}

