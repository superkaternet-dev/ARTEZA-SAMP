/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;
import okio.Sink;
import okio.Source;
import okio.Timeout;
import okio.Util;

final class RealBufferedSink
implements BufferedSink {
    public final Buffer buffer = new Buffer();
    boolean closed;
    public final Sink sink;

    RealBufferedSink(Sink sink) {
        if (sink != null) {
            this.sink = sink;
            return;
        }
        throw new IllegalArgumentException("sink == null");
    }

    @Override
    public Buffer buffer() {
        return this.buffer;
    }

    @Override
    public void close() throws IOException {
        Object object;
        block7: {
            if (this.closed) {
                return;
            }
            Throwable throwable = null;
            try {
                if (this.buffer.size > 0L) {
                    Sink sink = this.sink;
                    object = this.buffer;
                    sink.write((Buffer)object, ((Buffer)object).size);
                }
            }
            catch (Throwable throwable2) {
                // empty catch block
            }
            try {
                this.sink.close();
                object = throwable;
            }
            catch (Throwable throwable3) {
                object = throwable;
                if (throwable != null) break block7;
                object = throwable3;
            }
        }
        this.closed = true;
        if (object != null) {
            Util.sneakyRethrow((Throwable)object);
        }
    }

    @Override
    public BufferedSink emit() throws IOException {
        if (!this.closed) {
            long l = this.buffer.size();
            if (l > 0L) {
                this.sink.write(this.buffer, l);
            }
            return this;
        }
        throw new IllegalStateException("closed");
    }

    @Override
    public BufferedSink emitCompleteSegments() throws IOException {
        if (!this.closed) {
            long l = this.buffer.completeSegmentByteCount();
            if (l > 0L) {
                this.sink.write(this.buffer, l);
            }
            return this;
        }
        throw new IllegalStateException("closed");
    }

    @Override
    public void flush() throws IOException {
        if (!this.closed) {
            if (this.buffer.size > 0L) {
                Sink sink = this.sink;
                Buffer buffer = this.buffer;
                sink.write(buffer, buffer.size);
            }
            this.sink.flush();
            return;
        }
        throw new IllegalStateException("closed");
    }

    @Override
    public OutputStream outputStream() {
        return new OutputStream(this){
            final RealBufferedSink this$0;
            {
                this.this$0 = realBufferedSink;
            }

            @Override
            public void close() throws IOException {
                this.this$0.close();
            }

            @Override
            public void flush() throws IOException {
                if (!this.this$0.closed) {
                    this.this$0.flush();
                }
            }

            public String toString() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.this$0);
                stringBuilder.append(".outputStream()");
                return stringBuilder.toString();
            }

            @Override
            public void write(int n) throws IOException {
                if (!this.this$0.closed) {
                    this.this$0.buffer.writeByte((byte)n);
                    this.this$0.emitCompleteSegments();
                    return;
                }
                throw new IOException("closed");
            }

            @Override
            public void write(byte[] byArray, int n, int n2) throws IOException {
                if (!this.this$0.closed) {
                    this.this$0.buffer.write(byArray, n, n2);
                    this.this$0.emitCompleteSegments();
                    return;
                }
                throw new IOException("closed");
            }
        };
    }

    @Override
    public Timeout timeout() {
        return this.sink.timeout();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("buffer(");
        stringBuilder.append(this.sink);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    @Override
    public BufferedSink write(ByteString byteString) throws IOException {
        if (!this.closed) {
            this.buffer.write(byteString);
            return this.emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    @Override
    public BufferedSink write(Source source, long l) throws IOException {
        while (l > 0L) {
            long l2 = source.read(this.buffer, l);
            if (l2 != -1L) {
                l -= l2;
                this.emitCompleteSegments();
                continue;
            }
            throw new EOFException();
        }
        return this;
    }

    @Override
    public BufferedSink write(byte[] byArray) throws IOException {
        if (!this.closed) {
            this.buffer.write(byArray);
            return this.emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    @Override
    public BufferedSink write(byte[] byArray, int n, int n2) throws IOException {
        if (!this.closed) {
            this.buffer.write(byArray, n, n2);
            return this.emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    @Override
    public void write(Buffer buffer, long l) throws IOException {
        if (!this.closed) {
            this.buffer.write(buffer, l);
            this.emitCompleteSegments();
            return;
        }
        throw new IllegalStateException("closed");
    }

    @Override
    public long writeAll(Source object) throws IOException {
        if (object != null) {
            long l;
            long l2 = 0L;
            while ((l = object.read(this.buffer, 8192L)) != -1L) {
                l2 += l;
                this.emitCompleteSegments();
            }
            return l2;
        }
        object = new IllegalArgumentException("source == null");
        throw object;
    }

    @Override
    public BufferedSink writeByte(int n) throws IOException {
        if (!this.closed) {
            this.buffer.writeByte(n);
            return this.emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    @Override
    public BufferedSink writeDecimalLong(long l) throws IOException {
        if (!this.closed) {
            this.buffer.writeDecimalLong(l);
            return this.emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    @Override
    public BufferedSink writeHexadecimalUnsignedLong(long l) throws IOException {
        if (!this.closed) {
            this.buffer.writeHexadecimalUnsignedLong(l);
            return this.emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    @Override
    public BufferedSink writeInt(int n) throws IOException {
        if (!this.closed) {
            this.buffer.writeInt(n);
            return this.emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    @Override
    public BufferedSink writeIntLe(int n) throws IOException {
        if (!this.closed) {
            this.buffer.writeIntLe(n);
            return this.emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    @Override
    public BufferedSink writeLong(long l) throws IOException {
        if (!this.closed) {
            this.buffer.writeLong(l);
            return this.emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    @Override
    public BufferedSink writeLongLe(long l) throws IOException {
        if (!this.closed) {
            this.buffer.writeLongLe(l);
            return this.emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    @Override
    public BufferedSink writeShort(int n) throws IOException {
        if (!this.closed) {
            this.buffer.writeShort(n);
            return this.emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    @Override
    public BufferedSink writeShortLe(int n) throws IOException {
        if (!this.closed) {
            this.buffer.writeShortLe(n);
            return this.emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    @Override
    public BufferedSink writeString(String string2, int n, int n2, Charset charset) throws IOException {
        if (!this.closed) {
            this.buffer.writeString(string2, n, n2, charset);
            return this.emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    @Override
    public BufferedSink writeString(String string2, Charset charset) throws IOException {
        if (!this.closed) {
            this.buffer.writeString(string2, charset);
            return this.emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    @Override
    public BufferedSink writeUtf8(String string2) throws IOException {
        if (!this.closed) {
            this.buffer.writeUtf8(string2);
            return this.emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    @Override
    public BufferedSink writeUtf8(String string2, int n, int n2) throws IOException {
        if (!this.closed) {
            this.buffer.writeUtf8(string2, n, n2);
            return this.emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }

    @Override
    public BufferedSink writeUtf8CodePoint(int n) throws IOException {
        if (!this.closed) {
            this.buffer.writeUtf8CodePoint(n);
            return this.emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }
}

