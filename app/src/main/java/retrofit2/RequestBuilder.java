/*
 * Decompiled with CFR 0.152.
 */
package retrofit2;

import java.io.IOException;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;

final class RequestBuilder {
    private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final String PATH_SEGMENT_ALWAYS_ENCODE_SET = " \"<>^`{}|\\?#";
    private final HttpUrl baseUrl;
    private RequestBody body;
    private MediaType contentType;
    private FormBody.Builder formBuilder;
    private final boolean hasBody;
    private final String method;
    private MultipartBody.Builder multipartBuilder;
    private String relativeUrl;
    private final Request.Builder requestBuilder;
    private HttpUrl.Builder urlBuilder;

    RequestBuilder(String object, HttpUrl httpUrl, String string2, Headers headers, MediaType mediaType, boolean bl, boolean bl2, boolean bl3) {
        this.method = object;
        this.baseUrl = httpUrl;
        this.relativeUrl = string2;
        this.requestBuilder = object = new Request.Builder();
        this.contentType = mediaType;
        this.hasBody = bl;
        if (headers != null) {
            ((Request.Builder)object).headers(headers);
        }
        if (bl2) {
            this.formBuilder = new FormBody.Builder();
        } else if (bl3) {
            this.multipartBuilder = object = new MultipartBody.Builder();
            ((MultipartBody.Builder)object).setType(MultipartBody.FORM);
        }
    }

    private static String canonicalizeForPath(String string2, boolean bl) {
        int n;
        int n2 = string2.length();
        for (int i = 0; i < n2; i += Character.charCount(n)) {
            n = string2.codePointAt(i);
            if (n >= 32 && n < 127 && PATH_SEGMENT_ALWAYS_ENCODE_SET.indexOf(n) == -1 && (bl || n != 47 && n != 37)) {
                continue;
            }
            Buffer buffer = new Buffer();
            buffer.writeUtf8(string2, 0, i);
            RequestBuilder.canonicalizeForPath(buffer, string2, i, n2, bl);
            return buffer.readUtf8();
        }
        return string2;
    }

    private static void canonicalizeForPath(Buffer buffer, String string2, int n, int n2, boolean bl) {
        Object object = null;
        while (n < n2) {
            int n3 = string2.codePointAt(n);
            if (!bl || n3 != 9 && n3 != 10 && n3 != 12 && n3 != 13) {
                if (n3 >= 32 && n3 < 127 && PATH_SEGMENT_ALWAYS_ENCODE_SET.indexOf(n3) == -1 && (bl || n3 != 47 && n3 != 37)) {
                    buffer.writeUtf8CodePoint(n3);
                } else {
                    Buffer buffer2 = object;
                    if (object == null) {
                        buffer2 = new Buffer();
                    }
                    buffer2.writeUtf8CodePoint(n3);
                    while (true) {
                        object = buffer2;
                        if (buffer2.exhausted()) break;
                        int n4 = buffer2.readByte() & 0xFF;
                        buffer.writeByte(37);
                        object = HEX_DIGITS;
                        buffer.writeByte((int)object[n4 >> 4 & 0xF]);
                        buffer.writeByte((int)object[n4 & 0xF]);
                    }
                }
            }
            n += Character.charCount(n3);
        }
    }

    void addFormField(String string2, String string3, boolean bl) {
        if (bl) {
            this.formBuilder.addEncoded(string2, string3);
        } else {
            this.formBuilder.add(string2, string3);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    void addHeader(String object, String string2) {
        if (!"Content-Type".equalsIgnoreCase((String)object)) {
            this.requestBuilder.addHeader((String)object, string2);
            return;
        }
        object = MediaType.parse(string2);
        if (object != null) {
            this.contentType = object;
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Malformed content type: ");
        ((StringBuilder)object).append(string2);
        throw new IllegalArgumentException(((StringBuilder)object).toString());
    }

    void addPart(Headers headers, RequestBody requestBody) {
        this.multipartBuilder.addPart(headers, requestBody);
    }

    void addPart(MultipartBody.Part part) {
        this.multipartBuilder.addPart(part);
    }

    void addPathParam(String string2, String string3, boolean bl) {
        String string4 = this.relativeUrl;
        if (string4 != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{");
            stringBuilder.append(string2);
            stringBuilder.append("}");
            this.relativeUrl = string4.replace(stringBuilder.toString(), RequestBuilder.canonicalizeForPath(string3, bl));
            return;
        }
        throw new AssertionError();
    }

    void addQueryParam(String charSequence, String string2, boolean bl) {
        Object object = this.relativeUrl;
        if (object != null) {
            this.urlBuilder = object = this.baseUrl.newBuilder((String)object);
            if (object != null) {
                this.relativeUrl = null;
            } else {
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append("Malformed URL. Base: ");
                ((StringBuilder)charSequence).append(this.baseUrl);
                ((StringBuilder)charSequence).append(", Relative: ");
                ((StringBuilder)charSequence).append(this.relativeUrl);
                throw new IllegalArgumentException(((StringBuilder)charSequence).toString());
            }
        }
        if (bl) {
            this.urlBuilder.addEncodedQueryParameter((String)charSequence, string2);
        } else {
            this.urlBuilder.addQueryParameter((String)charSequence, string2);
        }
    }

    Request build() {
        Object object;
        block14: {
            Object object2;
            HttpUrl httpUrl;
            block13: {
                block12: {
                    object = this.urlBuilder;
                    if (object == null) break block12;
                    httpUrl = ((HttpUrl.Builder)object).build();
                    break block13;
                }
                httpUrl = this.baseUrl.resolve(this.relativeUrl);
                if (httpUrl == null) break block14;
            }
            object = object2 = this.body;
            if (object2 == null) {
                object = this.formBuilder;
                if (object != null) {
                    object = ((FormBody.Builder)object).build();
                } else {
                    object = this.multipartBuilder;
                    if (object != null) {
                        object = ((MultipartBody.Builder)object).build();
                    } else {
                        object = object2;
                        if (this.hasBody) {
                            object = RequestBody.create(null, new byte[0]);
                        }
                    }
                }
            }
            MediaType mediaType = this.contentType;
            object2 = object;
            if (mediaType != null) {
                if (object != null) {
                    object2 = new ContentTypeOverridingRequestBody((RequestBody)object, mediaType);
                } else {
                    this.requestBuilder.addHeader("Content-Type", mediaType.toString());
                    object2 = object;
                }
            }
            return this.requestBuilder.url(httpUrl).method(this.method, (RequestBody)object2).build();
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Malformed URL. Base: ");
        ((StringBuilder)object).append(this.baseUrl);
        ((StringBuilder)object).append(", Relative: ");
        ((StringBuilder)object).append(this.relativeUrl);
        throw new IllegalArgumentException(((StringBuilder)object).toString());
    }

    void setBody(RequestBody requestBody) {
        this.body = requestBody;
    }

    void setRelativeUrl(Object object) {
        if (object != null) {
            this.relativeUrl = object.toString();
            return;
        }
        throw new NullPointerException("@Url parameter is null.");
    }

    private static class ContentTypeOverridingRequestBody
    extends RequestBody {
        private final MediaType contentType;
        private final RequestBody delegate;

        ContentTypeOverridingRequestBody(RequestBody requestBody, MediaType mediaType) {
            this.delegate = requestBody;
            this.contentType = mediaType;
        }

        @Override
        public long contentLength() throws IOException {
            return this.delegate.contentLength();
        }

        @Override
        public MediaType contentType() {
            return this.contentType;
        }

        @Override
        public void writeTo(BufferedSink bufferedSink) throws IOException {
            this.delegate.writeTo(bufferedSink);
        }
    }
}

