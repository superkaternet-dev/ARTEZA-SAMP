/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.Options;
import okio.Sink;
import okio.Source;
import okio.Timeout;
import okio.Util;

final class RealBufferedSource
implements BufferedSource {
    public final Buffer buffer = new Buffer();
    boolean closed;
    public final Source source;

    RealBufferedSource(Source source) {
        if (source != null) {
            this.source = source;
            return;
        }
        throw new IllegalArgumentException("source == null");
    }

    @Override
    public Buffer buffer() {
        return this.buffer;
    }

    @Override
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        this.closed = true;
        this.source.close();
        this.buffer.clear();
    }

    @Override
    public boolean exhausted() throws IOException {
        if (!this.closed) {
            boolean bl = this.buffer.exhausted() && this.source.read(this.buffer, 8192L) == -1L;
            return bl;
        }
        throw new IllegalStateException("closed");
    }

    @Override
    public long indexOf(byte by) throws IOException {
        return this.indexOf(by, 0L);
    }

    @Override
    public long indexOf(byte by, long l) throws IOException {
        if (!this.closed) {
            while (true) {
                long l2;
                if ((l2 = this.buffer.indexOf(by, l)) != -1L) {
                    return l2;
                }
                l2 = this.buffer.size;
                if (this.source.read(this.buffer, 8192L) == -1L) {
                    return -1L;
                }
                l = Math.max(l, l2);
            }
        }
        IllegalStateException illegalStateException = new IllegalStateException("closed");
        throw illegalStateException;
    }

    @Override
    public long indexOf(ByteString byteString) throws IOException {
        return this.indexOf(byteString, 0L);
    }

    @Override
    public long indexOf(ByteString serializable, long l) throws IOException {
        if (!this.closed) {
            while (true) {
                long l2;
                if ((l2 = this.buffer.indexOf((ByteString)serializable, l)) != -1L) {
                    return l2;
                }
                l2 = this.buffer.size;
                if (this.source.read(this.buffer, 8192L) == -1L) {
                    return -1L;
                }
                l = Math.max(l, l2 - (long)((ByteString)serializable).size() + 1L);
            }
        }
        serializable = new IllegalStateException("closed");
        throw serializable;
    }

    @Override
    public long indexOfElement(ByteString byteString) throws IOException {
        return this.indexOfElement(byteString, 0L);
    }

    @Override
    public long indexOfElement(ByteString serializable, long l) throws IOException {
        if (!this.closed) {
            while (true) {
                long l2;
                if ((l2 = this.buffer.indexOfElement((ByteString)serializable, l)) != -1L) {
                    return l2;
                }
                l2 = this.buffer.size;
                if (this.source.read(this.buffer, 8192L) == -1L) {
                    return -1L;
                }
                l = Math.max(l, l2);
            }
        }
        serializable = new IllegalStateException("closed");
        throw serializable;
    }

    @Override
    public InputStream inputStream() {
        return new InputStream(this){
            final RealBufferedSource this$0;
            {
                this.this$0 = realBufferedSource;
            }

            @Override
            public int available() throws IOException {
                if (!this.this$0.closed) {
                    return (int)Math.min(this.this$0.buffer.size, Integer.MAX_VALUE);
                }
                throw new IOException("closed");
            }

            @Override
            public void close() throws IOException {
                this.this$0.close();
            }

            @Override
            public int read() throws IOException {
                if (!this.this$0.closed) {
                    if (this.this$0.buffer.size == 0L && this.this$0.source.read(this.this$0.buffer, 8192L) == -1L) {
                        return -1;
                    }
                    return this.this$0.buffer.readByte() & 0xFF;
                }
                throw new IOException("closed");
            }

            @Override
            public int read(byte[] byArray, int n, int n2) throws IOException {
                if (!this.this$0.closed) {
                    Util.checkOffsetAndCount(byArray.length, n, n2);
                    if (this.this$0.buffer.size == 0L && this.this$0.source.read(this.this$0.buffer, 8192L) == -1L) {
                        return -1;
                    }
                    return this.this$0.buffer.read(byArray, n, n2);
                }
                throw new IOException("closed");
            }

            public String toString() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.this$0);
                stringBuilder.append(".inputStream()");
                return stringBuilder.toString();
            }
        };
    }

    @Override
    public int read(byte[] byArray) throws IOException {
        return this.read(byArray, 0, byArray.length);
    }

    @Override
    public int read(byte[] byArray, int n, int n2) throws IOException {
        Util.checkOffsetAndCount(byArray.length, n, n2);
        if (this.buffer.size == 0L && this.source.read(this.buffer, 8192L) == -1L) {
            return -1;
        }
        n2 = (int)Math.min((long)n2, this.buffer.size);
        return this.buffer.read(byArray, n, n2);
    }

    @Override
    public long read(Buffer object, long l) throws IOException {
        if (object != null) {
            if (l >= 0L) {
                if (!this.closed) {
                    if (this.buffer.size == 0L && this.source.read(this.buffer, 8192L) == -1L) {
                        return -1L;
                    }
                    l = Math.min(l, this.buffer.size);
                    return this.buffer.read((Buffer)object, l);
                }
                throw new IllegalStateException("closed");
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("byteCount < 0: ");
            ((StringBuilder)object).append(l);
            throw new IllegalArgumentException(((StringBuilder)object).toString());
        }
        throw new IllegalArgumentException("sink == null");
    }

    @Override
    public long readAll(Sink object) throws IOException {
        if (object != null) {
            long l;
            long l2 = 0L;
            while (this.source.read(this.buffer, 8192L) != -1L) {
                long l3 = this.buffer.completeSegmentByteCount();
                l = l2;
                if (l3 > 0L) {
                    l = l2 + l3;
                    object.write(this.buffer, l3);
                }
                l2 = l;
            }
            l = l2;
            if (this.buffer.size() > 0L) {
                l = l2 + this.buffer.size();
                Buffer buffer = this.buffer;
                object.write(buffer, buffer.size());
            }
            return l;
        }
        object = new IllegalArgumentException("sink == null");
        throw object;
    }

    @Override
    public byte readByte() throws IOException {
        this.require(1L);
        return this.buffer.readByte();
    }

    @Override
    public byte[] readByteArray() throws IOException {
        this.buffer.writeAll(this.source);
        return this.buffer.readByteArray();
    }

    @Override
    public byte[] readByteArray(long l) throws IOException {
        this.require(l);
        return this.buffer.readByteArray(l);
    }

    @Override
    public ByteString readByteString() throws IOException {
        this.buffer.writeAll(this.source);
        return this.buffer.readByteString();
    }

    @Override
    public ByteString readByteString(long l) throws IOException {
        this.require(l);
        return this.buffer.readByteString(l);
    }

    @Override
    public long readDecimalLong() throws IOException {
        this.require(1L);
        int n = 0;
        while (this.request(n + 1)) {
            byte by = this.buffer.getByte(n);
            if (by >= 48 && by <= 57 || n == 0 && by == 45) {
                ++n;
                continue;
            }
            if (n != 0) break;
            throw new NumberFormatException(String.format("Expected leading [0-9] or '-' character but was %#x", by));
        }
        return this.buffer.readDecimalLong();
    }

    @Override
    public void readFully(Buffer buffer, long l) throws IOException {
        try {
            this.require(l);
        }
        catch (EOFException eOFException) {
            buffer.writeAll(this.buffer);
            throw eOFException;
        }
        this.buffer.readFully(buffer, l);
    }

    @Override
    public void readFully(byte[] byArray) throws IOException {
        try {
            this.require(byArray.length);
        }
        catch (EOFException eOFException) {
            int n = 0;
            while (this.buffer.size > 0L) {
                Buffer buffer = this.buffer;
                int n2 = buffer.read(byArray, n, (int)buffer.size);
                if (n2 != -1) {
                    n += n2;
                    continue;
                }
                throw new AssertionError();
            }
            throw eOFException;
        }
        this.buffer.readFully(byArray);
    }

    @Override
    public long readHexadecimalUnsignedLong() throws IOException {
        this.require(1L);
        int n = 0;
        while (this.request(n + 1)) {
            byte by = this.buffer.getByte(n);
            if (by >= 48 && by <= 57 || by >= 97 && by <= 102 || by >= 65 && by <= 70) {
                ++n;
                continue;
            }
            if (n != 0) break;
            throw new NumberFormatException(String.format("Expected leading [0-9a-fA-F] character but was %#x", by));
        }
        return this.buffer.readHexadecimalUnsignedLong();
    }

    @Override
    public int readInt() throws IOException {
        this.require(4L);
        return this.buffer.readInt();
    }

    @Override
    public int readIntLe() throws IOException {
        this.require(4L);
        return this.buffer.readIntLe();
    }

    @Override
    public long readLong() throws IOException {
        this.require(8L);
        return this.buffer.readLong();
    }

    @Override
    public long readLongLe() throws IOException {
        this.require(8L);
        return this.buffer.readLongLe();
    }

    @Override
    public short readShort() throws IOException {
        this.require(2L);
        return this.buffer.readShort();
    }

    @Override
    public short readShortLe() throws IOException {
        this.require(2L);
        return this.buffer.readShortLe();
    }

    @Override
    public String readString(long l, Charset charset) throws IOException {
        this.require(l);
        if (charset != null) {
            return this.buffer.readString(l, charset);
        }
        throw new IllegalArgumentException("charset == null");
    }

    @Override
    public String readString(Charset charset) throws IOException {
        if (charset != null) {
            this.buffer.writeAll(this.source);
            return this.buffer.readString(charset);
        }
        throw new IllegalArgumentException("charset == null");
    }

    @Override
    public String readUtf8() throws IOException {
        this.buffer.writeAll(this.source);
        return this.buffer.readUtf8();
    }

    @Override
    public String readUtf8(long l) throws IOException {
        this.require(l);
        return this.buffer.readUtf8(l);
    }

    @Override
    public int readUtf8CodePoint() throws IOException {
        this.require(1L);
        byte by = this.buffer.getByte(0L);
        if ((by & 0xE0) == 192) {
            this.require(2L);
        } else if ((by & 0xF0) == 224) {
            this.require(3L);
        } else if ((by & 0xF8) == 240) {
            this.require(4L);
        }
        return this.buffer.readUtf8CodePoint();
    }

    @Override
    public String readUtf8Line() throws IOException {
        long l = this.indexOf((byte)10);
        if (l == -1L) {
            String string2 = this.buffer.size != 0L ? this.readUtf8(this.buffer.size) : null;
            return string2;
        }
        return this.buffer.readUtf8Line(l);
    }

    @Override
    public String readUtf8LineStrict() throws IOException {
        long l = this.indexOf((byte)10);
        if (l != -1L) {
            return this.buffer.readUtf8Line(l);
        }
        Buffer buffer = new Buffer();
        Object object = this.buffer;
        ((Buffer)object).copyTo(buffer, 0L, Math.min(32L, ((Buffer)object).size()));
        object = new StringBuilder();
        ((StringBuilder)object).append("\\n not found: size=");
        ((StringBuilder)object).append(this.buffer.size());
        ((StringBuilder)object).append(" content=");
        ((StringBuilder)object).append(buffer.readByteString().hex());
        ((StringBuilder)object).append("\u2026");
        throw new EOFException(((StringBuilder)object).toString());
    }

    @Override
    public boolean request(long l) throws IOException {
        if (l >= 0L) {
            if (!this.closed) {
                while (this.buffer.size < l) {
                    if (this.source.read(this.buffer, 8192L) != -1L) continue;
                    return false;
                }
                return true;
            }
            throw new IllegalStateException("closed");
        }
        Serializable serializable = new StringBuilder();
        serializable.append("byteCount < 0: ");
        serializable.append(l);
        serializable = new IllegalArgumentException(serializable.toString());
        throw serializable;
    }

    @Override
    public void require(long l) throws IOException {
        if (this.request(l)) {
            return;
        }
        throw new EOFException();
    }

    @Override
    public int select(Options object) throws IOException {
        if (!this.closed) {
            do {
                int n;
                if ((n = this.buffer.selectPrefix((Options)object)) == -1) {
                    return -1;
                }
                int n2 = ((Options)object).byteStrings[n].size();
                if ((long)n2 > this.buffer.size) continue;
                this.buffer.skip(n2);
                return n;
            } while (this.source.read(this.buffer, 8192L) != -1L);
            return -1;
        }
        object = new IllegalStateException("closed");
        throw object;
    }

    @Override
    public void skip(long l) throws IOException {
        if (!this.closed) {
            while (l > 0L) {
                if (this.buffer.size == 0L && this.source.read(this.buffer, 8192L) == -1L) {
                    throw new EOFException();
                }
                long l2 = Math.min(l, this.buffer.size());
                this.buffer.skip(l2);
                l -= l2;
            }
            return;
        }
        IllegalStateException illegalStateException = new IllegalStateException("closed");
        throw illegalStateException;
    }

    @Override
    public Timeout timeout() {
        return this.source.timeout();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("buffer(");
        stringBuilder.append(this.source);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}

