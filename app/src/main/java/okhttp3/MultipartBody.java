/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;

public final class MultipartBody
extends RequestBody {
    public static final MediaType ALTERNATIVE;
    private static final byte[] COLONSPACE;
    private static final byte[] CRLF;
    private static final byte[] DASHDASH;
    public static final MediaType DIGEST;
    public static final MediaType FORM;
    public static final MediaType MIXED;
    public static final MediaType PARALLEL;
    private final ByteString boundary;
    private long contentLength = -1L;
    private final MediaType contentType;
    private final MediaType originalType;
    private final List<Part> parts;

    static {
        MIXED = MediaType.parse("multipart/mixed");
        ALTERNATIVE = MediaType.parse("multipart/alternative");
        DIGEST = MediaType.parse("multipart/digest");
        PARALLEL = MediaType.parse("multipart/parallel");
        FORM = MediaType.parse("multipart/form-data");
        COLONSPACE = new byte[]{58, 32};
        CRLF = new byte[]{13, 10};
        DASHDASH = new byte[]{45, 45};
    }

    MultipartBody(ByteString byteString, MediaType mediaType, List<Part> list) {
        this.boundary = byteString;
        this.originalType = mediaType;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(mediaType);
        stringBuilder.append("; boundary=");
        stringBuilder.append(byteString.utf8());
        this.contentType = MediaType.parse(stringBuilder.toString());
        this.parts = Util.immutableList(list);
    }

    static StringBuilder appendQuotedString(StringBuilder stringBuilder, String string2) {
        stringBuilder.append('\"');
        int n = string2.length();
        block5: for (int i = 0; i < n; ++i) {
            char c = string2.charAt(i);
            switch (c) {
                default: {
                    stringBuilder.append(c);
                    continue block5;
                }
                case '\"': {
                    stringBuilder.append("%22");
                    continue block5;
                }
                case '\r': {
                    stringBuilder.append("%0D");
                    continue block5;
                }
                case '\n': {
                    stringBuilder.append("%0A");
                }
            }
        }
        stringBuilder.append('\"');
        return stringBuilder;
    }

    private long writeOrCountBytes(BufferedSink bufferedSink, boolean bl) throws IOException {
        long l;
        BufferedSink bufferedSink2;
        long l2 = 0L;
        Object object = null;
        if (bl) {
            bufferedSink = bufferedSink2 = new Buffer();
        } else {
            bufferedSink2 = bufferedSink;
            bufferedSink = object;
        }
        int n = this.parts.size();
        for (int i = 0; i < n; ++i) {
            object = this.parts.get(i);
            Object object2 = ((Part)object).headers;
            object = ((Part)object).body;
            bufferedSink2.write(DASHDASH);
            bufferedSink2.write(this.boundary);
            bufferedSink2.write(CRLF);
            if (object2 != null) {
                int n2 = ((Headers)object2).size();
                for (int j = 0; j < n2; ++j) {
                    bufferedSink2.writeUtf8(((Headers)object2).name(j)).write(COLONSPACE).writeUtf8(((Headers)object2).value(j)).write(CRLF);
                }
            }
            if ((object2 = ((RequestBody)object).contentType()) != null) {
                bufferedSink2.writeUtf8("Content-Type: ").writeUtf8(((MediaType)object2).toString()).write(CRLF);
            }
            if ((l = ((RequestBody)object).contentLength()) != -1L) {
                bufferedSink2.writeUtf8("Content-Length: ").writeDecimalLong(l).write(CRLF);
            } else if (bl) {
                ((Buffer)bufferedSink).clear();
                return -1L;
            }
            object2 = CRLF;
            bufferedSink2.write((byte[])object2);
            if (bl) {
                l2 += l;
            } else {
                ((RequestBody)object).writeTo(bufferedSink2);
            }
            bufferedSink2.write((byte[])object2);
        }
        object = DASHDASH;
        bufferedSink2.write((byte[])object);
        bufferedSink2.write(this.boundary);
        bufferedSink2.write((byte[])object);
        bufferedSink2.write(CRLF);
        l = l2;
        if (bl) {
            l = l2 + ((Buffer)bufferedSink).size();
            ((Buffer)bufferedSink).clear();
        }
        return l;
    }

    public String boundary() {
        return this.boundary.utf8();
    }

    @Override
    public long contentLength() throws IOException {
        long l = this.contentLength;
        if (l != -1L) {
            return l;
        }
        this.contentLength = l = this.writeOrCountBytes(null, true);
        return l;
    }

    @Override
    public MediaType contentType() {
        return this.contentType;
    }

    public Part part(int n) {
        return this.parts.get(n);
    }

    public List<Part> parts() {
        return this.parts;
    }

    public int size() {
        return this.parts.size();
    }

    public MediaType type() {
        return this.originalType;
    }

    @Override
    public void writeTo(BufferedSink bufferedSink) throws IOException {
        this.writeOrCountBytes(bufferedSink, false);
    }

    public static final class Builder {
        private final ByteString boundary;
        private final List<Part> parts;
        private MediaType type = MIXED;

        public Builder() {
            this(UUID.randomUUID().toString());
        }

        public Builder(String string2) {
            this.parts = new ArrayList<Part>();
            this.boundary = ByteString.encodeUtf8(string2);
        }

        public Builder addFormDataPart(String string2, String string3) {
            return this.addPart(Part.createFormData(string2, string3));
        }

        public Builder addFormDataPart(String string2, String string3, RequestBody requestBody) {
            return this.addPart(Part.createFormData(string2, string3, requestBody));
        }

        public Builder addPart(Headers headers, RequestBody requestBody) {
            return this.addPart(Part.create(headers, requestBody));
        }

        public Builder addPart(Part part) {
            if (part != null) {
                this.parts.add(part);
                return this;
            }
            throw new NullPointerException("part == null");
        }

        public Builder addPart(RequestBody requestBody) {
            return this.addPart(Part.create(requestBody));
        }

        public MultipartBody build() {
            if (!this.parts.isEmpty()) {
                return new MultipartBody(this.boundary, this.type, this.parts);
            }
            throw new IllegalStateException("Multipart body must have at least one part.");
        }

        public Builder setType(MediaType mediaType) {
            if (mediaType != null) {
                if (mediaType.type().equals("multipart")) {
                    this.type = mediaType;
                    return this;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("multipart != ");
                stringBuilder.append(mediaType);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            throw new NullPointerException("type == null");
        }
    }

    public static final class Part {
        private final RequestBody body;
        private final Headers headers;

        private Part(Headers headers, RequestBody requestBody) {
            this.headers = headers;
            this.body = requestBody;
        }

        public static Part create(Headers headers, RequestBody requestBody) {
            if (requestBody != null) {
                if (headers != null && headers.get("Content-Type") != null) {
                    throw new IllegalArgumentException("Unexpected header: Content-Type");
                }
                if (headers != null && headers.get("Content-Length") != null) {
                    throw new IllegalArgumentException("Unexpected header: Content-Length");
                }
                return new Part(headers, requestBody);
            }
            throw new NullPointerException("body == null");
        }

        public static Part create(RequestBody requestBody) {
            return Part.create(null, requestBody);
        }

        public static Part createFormData(String string2, String string3) {
            return Part.createFormData(string2, null, RequestBody.create(null, string3));
        }

        public static Part createFormData(String string2, String string3, RequestBody requestBody) {
            if (string2 != null) {
                StringBuilder stringBuilder = new StringBuilder("form-data; name=");
                MultipartBody.appendQuotedString(stringBuilder, string2);
                if (string3 != null) {
                    stringBuilder.append("; filename=");
                    MultipartBody.appendQuotedString(stringBuilder, string3);
                }
                return Part.create(Headers.of("Content-Disposition", stringBuilder.toString()), requestBody);
            }
            throw new NullPointerException("name == null");
        }
    }
}

