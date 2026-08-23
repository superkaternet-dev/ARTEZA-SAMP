/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.resource.bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RecyclableBufferedInputStream
extends FilterInputStream {
    private volatile byte[] buf;
    private final ArrayPool byteArrayPool;
    private int count;
    private int marklimit;
    private int markpos = -1;
    private int pos;

    public RecyclableBufferedInputStream(InputStream inputStream, ArrayPool arrayPool) {
        this(inputStream, arrayPool, 65536);
    }

    RecyclableBufferedInputStream(InputStream inputStream, ArrayPool arrayPool, int n) {
        super(inputStream);
        this.byteArrayPool = arrayPool;
        this.buf = arrayPool.get(n, byte[].class);
    }

    /*
     * Unable to fully structure code
     */
    private int fillbuf(InputStream var1_1, byte[] var2_2) throws IOException {
        var4_3 = this.markpos;
        if (var4_3 != -1 && (var3_4 = this.pos) - var4_3 < (var5_5 = this.marklimit)) {
            if (var4_3 == 0 && var5_5 > var2_2.length && this.count == var2_2.length) {
                var3_4 = var4_3 = var2_2.length * 2;
                if (var4_3 > var5_5) {
                    var3_4 = this.marklimit;
                }
                var6_6 = this.byteArrayPool.get(var3_4, byte[].class);
                System.arraycopy(var2_2, 0, var6_6, 0, var2_2.length);
                this.buf = var6_6;
                this.byteArrayPool.put(var2_2);
                while (true) {
                    var2_2 = var6_6;
                    break;
                }
            } else {
                var6_6 = var2_2;
                if (var4_3 <= 0) ** continue;
                System.arraycopy(var2_2, var4_3, var2_2, 0, var2_2.length - var4_3);
            }
            this.pos = var3_4 = this.pos - this.markpos;
            this.markpos = 0;
            this.count = 0;
            var4_3 = var1_1.read(var2_2, var3_4, var2_2.length - var3_4);
            var3_4 = this.pos;
            if (var4_3 > 0) {
                var3_4 += var4_3;
            }
            this.count = var3_4;
            return var4_3;
        }
        var3_4 = var1_1.read(var2_2);
        if (var3_4 > 0) {
            this.markpos = -1;
            this.pos = 0;
            this.count = var3_4;
        }
        return var3_4;
    }

    private static IOException streamClosed() throws IOException {
        throw new IOException("BufferedInputStream is closed");
    }

    @Override
    public int available() throws IOException {
        synchronized (this) {
            block5: {
                InputStream inputStream = this.in;
                if (this.buf == null || inputStream == null) break block5;
                int n = this.count;
                int n2 = this.pos;
                int n3 = inputStream.available();
                return n - n2 + n3;
            }
            throw RecyclableBufferedInputStream.streamClosed();
        }
    }

    @Override
    public void close() throws IOException {
        if (this.buf != null) {
            this.byteArrayPool.put(this.buf);
            this.buf = null;
        }
        InputStream inputStream = this.in;
        this.in = null;
        if (inputStream != null) {
            inputStream.close();
        }
    }

    public void fixMarkLimit() {
        synchronized (this) {
            this.marklimit = this.buf.length;
            return;
        }
    }

    @Override
    public void mark(int n) {
        synchronized (this) {
            this.marklimit = Math.max(this.marklimit, n);
            this.markpos = this.pos;
            return;
        }
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int read() throws IOException {
        synchronized (this) {
            byte[] byArray = this.buf;
            Object object = this.in;
            if (byArray != null && object != null) {
                int n;
                if (this.pos >= this.count && (n = this.fillbuf((InputStream)object, byArray)) == -1) {
                    return -1;
                }
                object = byArray;
                if (byArray != this.buf && (object = (Object)this.buf) == null) {
                    throw RecyclableBufferedInputStream.streamClosed();
                }
                int n2 = this.count;
                int n3 = this.pos;
                if (n2 - n3 > 0) {
                    this.pos = n3 + 1;
                    Object object2 = object[n3];
                    return object2 & 0xFF;
                }
                return -1;
            }
            throw RecyclableBufferedInputStream.streamClosed();
        }
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public int read(byte[] var1_1, int var2_3, int var3_4) throws IOException {
        synchronized (this) {
            block20: {
                block24: {
                    block21: {
                        block23: {
                            block22: {
                                block19: {
                                    var8_5 = this.buf;
                                    if (var8_5 == null) ** GOTO lbl90
                                    if (var3_4 != 0) break block19;
                                    return 0;
                                }
                                var10_6 = this.in;
                                if (var10_6 == null) break block20;
                                var5_7 = this.pos;
                                var4_8 = this.count;
                                if (var5_7 >= var4_8) break block21;
                                if (var4_8 - var5_7 >= var3_4) {
                                    var4_8 = var3_4;
                                    break block22;
                                }
                                var4_8 -= var5_7;
                            }
                            System.arraycopy(var8_5, var5_7, var1_1, var2_3, var4_8);
                            this.pos += var4_8;
                            if (var4_8 == var3_4) break block23;
                            var5_7 = var10_6.available();
                            if (var5_7 == 0) break block23;
                            var5_7 = var2_3 + var4_8;
                            var2_3 = var3_4 - var4_8;
                            var4_8 = var5_7;
                            break block24;
                        }
                        return var4_8;
                    }
                    var5_7 = var3_4;
                    var4_8 = var2_3;
                    var2_3 = var5_7;
                }
                while (true) {
                    block31: {
                        block26: {
                            block30: {
                                block28: {
                                    block29: {
                                        block25: {
                                            block27: {
                                                var6_9 = this.markpos;
                                                var5_7 = -1;
                                                if (var6_9 != -1) break block25;
                                                if (var2_3 < var8_5.length) break block25;
                                                var6_9 = var7_10 = var10_6.read(var1_1, var4_8, var2_3);
                                                if (var7_10 != -1) break block26;
                                                if (var2_3 == var3_4) break block27;
                                                var5_7 = var3_4 - var2_3;
                                            }
                                            return var5_7;
                                        }
                                        var6_9 = this.fillbuf(var10_6, var8_5);
                                        if (var6_9 != -1) break block28;
                                        if (var2_3 == var3_4) break block29;
                                        var5_7 = var3_4 - var2_3;
                                    }
                                    return var5_7;
                                }
                                var9_11 = var8_5;
                                if (var8_5 == this.buf || (var9_11 = this.buf) != null) ** GOTO lbl65
                                throw RecyclableBufferedInputStream.streamClosed();
lbl65:
                                // 1 sources

                                var5_7 = this.count;
                                var6_9 = this.pos;
                                if (var5_7 - var6_9 >= var2_3) {
                                    var5_7 = var2_3;
                                    break block30;
                                }
                                var5_7 -= var6_9;
                            }
                            System.arraycopy(var9_11, var6_9, var1_1, var4_8, var5_7);
                            this.pos += var5_7;
                            var6_9 = var5_7;
                            var8_5 = var9_11;
                        }
                        if ((var2_3 -= var6_9) == 0) {
                            return var3_4;
                        }
                        var5_7 = var10_6.available();
                        if (var5_7 != 0) break block31;
                        return var3_4 - var2_3;
                    }
                    var4_8 += var6_9;
                }
            }
            try {
                throw RecyclableBufferedInputStream.streamClosed();
lbl90:
                // 1 sources

                throw RecyclableBufferedInputStream.streamClosed();
            }
            catch (Throwable var1_2) {
                throw var1_2;
            }
        }
    }

    public void release() {
        synchronized (this) {
            if (this.buf != null) {
                this.byteArrayPool.put(this.buf);
                this.buf = null;
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void reset() throws IOException {
        synchronized (this) {
            if (this.buf == null) {
                IOException iOException = new IOException("Stream is closed");
                throw iOException;
            }
            int n = this.markpos;
            if (-1 != n) {
                this.pos = n;
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Mark has been invalidated, pos: ");
            stringBuilder.append(this.pos);
            stringBuilder.append(" markLimit: ");
            stringBuilder.append(this.marklimit);
            InvalidMarkException invalidMarkException = new InvalidMarkException(stringBuilder.toString());
            throw invalidMarkException;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public long skip(long l) throws IOException {
        synchronized (this) {
            if (l < 1L) {
                return 0L;
            }
            byte[] byArray = this.buf;
            if (byArray == null) {
                throw RecyclableBufferedInputStream.streamClosed();
            }
            InputStream inputStream = this.in;
            if (inputStream == null) {
                throw RecyclableBufferedInputStream.streamClosed();
            }
            int n = this.count;
            int n2 = this.pos;
            if ((long)(n - n2) >= l) {
                this.pos = (int)((long)n2 + l);
                return l;
            }
            long l2 = (long)n - (long)n2;
            this.pos = n;
            if (this.markpos != -1 && l <= (long)this.marklimit) {
                n = this.fillbuf(inputStream, byArray);
                if (n == -1) {
                    return l2;
                }
                n2 = this.count;
                n = this.pos;
                if ((long)(n2 - n) >= l - l2) {
                    this.pos = (int)((long)n + l - l2);
                    return l;
                }
                long l3 = n2;
                l = n;
                this.pos = n2;
                return l3 + l2 - l;
            }
            if ((l = inputStream.skip(l - l2)) > 0L) {
                this.markpos = -1;
            }
            return l2 + l;
        }
    }

    static class InvalidMarkException
    extends IOException {
        private static final long serialVersionUID = -4338378848813561757L;

        InvalidMarkException(String string2) {
            super(string2);
        }
    }
}

