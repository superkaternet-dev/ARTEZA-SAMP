/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import okhttp3.MediaType;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSource;

public abstract class ResponseBody
implements Closeable {
    private Reader reader;

    private Charset charset() {
        Charset charset;
        MediaType mediaType = this.contentType();
        Charset charset2 = charset = Util.UTF_8;
        if (mediaType != null) {
            charset2 = mediaType.charset(charset);
        }
        return charset2;
    }

    public static ResponseBody create(MediaType mediaType, long l, BufferedSource bufferedSource) {
        if (bufferedSource != null) {
            return new ResponseBody(mediaType, l, bufferedSource){
                final BufferedSource val$content;
                final long val$contentLength;
                final MediaType val$contentType;
                {
                    this.val$contentType = mediaType;
                    this.val$contentLength = l;
                    this.val$content = bufferedSource;
                }

                @Override
                public long contentLength() {
                    return this.val$contentLength;
                }

                @Override
                public MediaType contentType() {
                    return this.val$contentType;
                }

                @Override
                public BufferedSource source() {
                    return this.val$content;
                }
            };
        }
        throw new NullPointerException("source == null");
    }

    public static ResponseBody create(MediaType object, String string2) {
        Charset charset = Util.UTF_8;
        Object object2 = object;
        if (object != null) {
            Charset charset2;
            charset = charset2 = ((MediaType)object).charset();
            object2 = object;
            if (charset2 == null) {
                charset = Util.UTF_8;
                object2 = new StringBuilder();
                ((StringBuilder)object2).append(object);
                ((StringBuilder)object2).append("; charset=utf-8");
                object2 = MediaType.parse(((StringBuilder)object2).toString());
            }
        }
        object = new Buffer().writeString(string2, charset);
        return ResponseBody.create((MediaType)object2, ((Buffer)object).size(), (BufferedSource)object);
    }

    public static ResponseBody create(MediaType mediaType, byte[] byArray) {
        Buffer buffer = new Buffer().write(byArray);
        return ResponseBody.create(mediaType, byArray.length, buffer);
    }

    public final InputStream byteStream() {
        return this.source().inputStream();
    }

    public final byte[] bytes() throws IOException {
        long l = this.contentLength();
        if (l <= Integer.MAX_VALUE) {
            BufferedSource bufferedSource = this.source();
            byte[] byArray = bufferedSource.readByteArray();
            if (l != -1L && l != (long)byArray.length) {
                throw new IOException("Content-Length and stream length disagree");
            }
            return byArray;
            finally {
                Util.closeQuietly(bufferedSource);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot buffer entire body for content length: ");
        stringBuilder.append(l);
        throw new IOException(stringBuilder.toString());
    }

    public final Reader charStream() {
        Reader reader = this.reader;
        if (reader == null) {
            this.reader = reader = new InputStreamReader(this.byteStream(), this.charset());
        }
        return reader;
    }

    @Override
    public void close() {
        Util.closeQuietly(this.source());
    }

    public abstract long contentLength();

    public abstract MediaType contentType();

    public abstract BufferedSource source();

    public final String string() throws IOException {
        return new String(this.bytes(), this.charset().name());
    }
}

