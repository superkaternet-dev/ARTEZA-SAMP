/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import okhttp3.MediaType;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.ByteString;
import okio.Okio;
import okio.Source;

public abstract class RequestBody {
    public static RequestBody create(MediaType mediaType, File file) {
        if (file != null) {
            return new RequestBody(mediaType, file){
                final MediaType val$contentType;
                final File val$file;
                {
                    this.val$contentType = mediaType;
                    this.val$file = file;
                }

                @Override
                public long contentLength() {
                    return this.val$file.length();
                }

                @Override
                public MediaType contentType() {
                    return this.val$contentType;
                }

                @Override
                public void writeTo(BufferedSink bufferedSink) throws IOException {
                    Source source;
                    Source source2 = null;
                    try {
                        source2 = source = Okio.source(this.val$file);
                    }
                    catch (Throwable throwable) {
                        Util.closeQuietly(source2);
                        throw throwable;
                    }
                    bufferedSink.writeAll(source);
                    Util.closeQuietly(source);
                }
            };
        }
        throw new NullPointerException("content == null");
    }

    public static RequestBody create(MediaType mediaType, String string2) {
        Charset charset = Util.UTF_8;
        Object object = mediaType;
        if (mediaType != null) {
            Charset charset2;
            charset = charset2 = mediaType.charset();
            object = mediaType;
            if (charset2 == null) {
                charset = Util.UTF_8;
                object = new StringBuilder();
                ((StringBuilder)object).append(mediaType);
                ((StringBuilder)object).append("; charset=utf-8");
                object = MediaType.parse(((StringBuilder)object).toString());
            }
        }
        return RequestBody.create((MediaType)object, string2.getBytes(charset));
    }

    public static RequestBody create(MediaType mediaType, ByteString byteString) {
        return new RequestBody(mediaType, byteString){
            final ByteString val$content;
            final MediaType val$contentType;
            {
                this.val$contentType = mediaType;
                this.val$content = byteString;
            }

            @Override
            public long contentLength() throws IOException {
                return this.val$content.size();
            }

            @Override
            public MediaType contentType() {
                return this.val$contentType;
            }

            @Override
            public void writeTo(BufferedSink bufferedSink) throws IOException {
                bufferedSink.write(this.val$content);
            }
        };
    }

    public static RequestBody create(MediaType mediaType, byte[] byArray) {
        return RequestBody.create(mediaType, byArray, 0, byArray.length);
    }

    public static RequestBody create(MediaType mediaType, byte[] byArray, int n, int n2) {
        if (byArray != null) {
            Util.checkOffsetAndCount(byArray.length, n, n2);
            return new RequestBody(mediaType, n2, byArray, n){
                final int val$byteCount;
                final byte[] val$content;
                final MediaType val$contentType;
                final int val$offset;
                {
                    this.val$contentType = mediaType;
                    this.val$byteCount = n;
                    this.val$content = byArray;
                    this.val$offset = n2;
                }

                @Override
                public long contentLength() {
                    return this.val$byteCount;
                }

                @Override
                public MediaType contentType() {
                    return this.val$contentType;
                }

                @Override
                public void writeTo(BufferedSink bufferedSink) throws IOException {
                    bufferedSink.write(this.val$content, this.val$offset, this.val$byteCount);
                }
            };
        }
        throw new NullPointerException("content == null");
    }

    public long contentLength() throws IOException {
        return -1L;
    }

    public abstract MediaType contentType();

    public abstract void writeTo(BufferedSink var1) throws IOException;
}

