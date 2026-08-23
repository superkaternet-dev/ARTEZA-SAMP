/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.spi.AbstractInterruptibleChannel;
import java.util.concurrent.atomic.AtomicReference;

public final class ByteBufferUtil {
    private static final AtomicReference<byte[]> BUFFER_REF = new AtomicReference();
    private static final int BUFFER_SIZE = 16384;

    private ByteBufferUtil() {
    }

    public static ByteBuffer fromFile(File object) throws IOException {
        AbstractInterruptibleChannel abstractInterruptibleChannel;
        Object object2;
        AbstractInterruptibleChannel abstractInterruptibleChannel2;
        Object object3;
        block22: {
            block23: {
                block24: {
                    long l;
                    object3 = null;
                    abstractInterruptibleChannel2 = null;
                    object2 = object3;
                    abstractInterruptibleChannel = abstractInterruptibleChannel2;
                    try {
                        l = ((File)object).length();
                        if (l > Integer.MAX_VALUE) break block22;
                        if (l == 0L) break block23;
                        object2 = object3;
                        abstractInterruptibleChannel = abstractInterruptibleChannel2;
                        object2 = object3;
                        abstractInterruptibleChannel = abstractInterruptibleChannel2;
                    }
                    catch (Throwable throwable) {
                        if (abstractInterruptibleChannel != null) {
                            try {
                                abstractInterruptibleChannel.close();
                            }
                            catch (IOException iOException) {
                                // empty catch block
                            }
                        }
                        if (object2 != null) {
                            try {
                                ((RandomAccessFile)object2).close();
                            }
                            catch (IOException iOException) {
                                // empty catch block
                            }
                        }
                        throw throwable;
                    }
                    RandomAccessFile randomAccessFile = new RandomAccessFile((File)object, "r");
                    object2 = object = randomAccessFile;
                    abstractInterruptibleChannel = abstractInterruptibleChannel2;
                    abstractInterruptibleChannel2 = ((RandomAccessFile)object).getChannel();
                    object2 = object;
                    abstractInterruptibleChannel = abstractInterruptibleChannel2;
                    object3 = ((FileChannel)abstractInterruptibleChannel2).map(FileChannel.MapMode.READ_ONLY, 0L, l).load();
                    if (abstractInterruptibleChannel2 == null) break block24;
                    try {
                        abstractInterruptibleChannel2.close();
                    }
                    catch (IOException iOException) {
                        // empty catch block
                    }
                }
                try {
                    ((RandomAccessFile)object).close();
                }
                catch (IOException iOException) {
                    // empty catch block
                }
                return object3;
            }
            object2 = object3;
            abstractInterruptibleChannel = abstractInterruptibleChannel2;
            object2 = object3;
            abstractInterruptibleChannel = abstractInterruptibleChannel2;
            object = new IOException("File unsuitable for memory mapping");
            object2 = object3;
            abstractInterruptibleChannel = abstractInterruptibleChannel2;
            throw object;
        }
        object2 = object3;
        abstractInterruptibleChannel = abstractInterruptibleChannel2;
        object2 = object3;
        abstractInterruptibleChannel = abstractInterruptibleChannel2;
        object = new IOException("File too large to map into memory");
        object2 = object3;
        abstractInterruptibleChannel = abstractInterruptibleChannel2;
        throw object;
    }

    public static ByteBuffer fromStream(InputStream object) throws IOException {
        int n;
        byte[] byArray;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(16384);
        byte[] byArray2 = byArray = (byte[])BUFFER_REF.getAndSet(null);
        if (byArray == null) {
            byArray2 = new byte[16384];
        }
        while ((n = ((InputStream)object).read(byArray2)) >= 0) {
            byteArrayOutputStream.write(byArray2, 0, n);
        }
        BUFFER_REF.set(byArray2);
        object = byteArrayOutputStream.toByteArray();
        return ByteBufferUtil.rewind(ByteBuffer.allocateDirect(((Object)object).length).put((byte[])object));
    }

    private static SafeArray getSafeArray(ByteBuffer byteBuffer) {
        if (!byteBuffer.isReadOnly() && byteBuffer.hasArray()) {
            return new SafeArray(byteBuffer.array(), byteBuffer.arrayOffset(), byteBuffer.limit());
        }
        return null;
    }

    public static ByteBuffer rewind(ByteBuffer byteBuffer) {
        return (ByteBuffer)byteBuffer.position(0);
    }

    public static byte[] toBytes(ByteBuffer object) {
        Object object2 = ByteBufferUtil.getSafeArray((ByteBuffer)object);
        if (object2 != null && ((SafeArray)object2).offset == 0 && ((SafeArray)object2).limit == ((SafeArray)object2).data.length) {
            object = ((ByteBuffer)object).array();
        } else {
            object2 = ((ByteBuffer)object).asReadOnlyBuffer();
            object = new byte[((Buffer)object2).limit()];
            ByteBufferUtil.rewind((ByteBuffer)object2);
            ((ByteBuffer)object2).get((byte[])object);
        }
        return object;
    }

    public static void toFile(ByteBuffer byteBuffer, File object) throws IOException {
        block17: {
            ByteBufferUtil.rewind(byteBuffer);
            RandomAccessFile randomAccessFile = null;
            AbstractInterruptibleChannel abstractInterruptibleChannel = null;
            Object object2 = randomAccessFile;
            AbstractInterruptibleChannel abstractInterruptibleChannel2 = abstractInterruptibleChannel;
            object2 = randomAccessFile;
            abstractInterruptibleChannel2 = abstractInterruptibleChannel;
            try {
                RandomAccessFile randomAccessFile2 = new RandomAccessFile((File)object, "rw");
                object2 = object = randomAccessFile2;
                abstractInterruptibleChannel2 = abstractInterruptibleChannel;
            }
            catch (Throwable throwable) {
                block18: {
                    if (abstractInterruptibleChannel2 != null) {
                        try {
                            abstractInterruptibleChannel2.close();
                        }
                        catch (IOException iOException) {
                            // empty catch block
                        }
                    }
                    if (object2 == null) break block18;
                    try {
                        ((RandomAccessFile)object2).close();
                    }
                    catch (IOException iOException) {}
                }
                throw throwable;
            }
            abstractInterruptibleChannel = ((RandomAccessFile)object).getChannel();
            object2 = object;
            abstractInterruptibleChannel2 = abstractInterruptibleChannel;
            ((FileChannel)abstractInterruptibleChannel).write(byteBuffer);
            object2 = object;
            abstractInterruptibleChannel2 = abstractInterruptibleChannel;
            ((FileChannel)abstractInterruptibleChannel).force(false);
            object2 = object;
            abstractInterruptibleChannel2 = abstractInterruptibleChannel;
            abstractInterruptibleChannel.close();
            object2 = object;
            abstractInterruptibleChannel2 = abstractInterruptibleChannel;
            ((RandomAccessFile)object).close();
            if (abstractInterruptibleChannel == null) break block17;
            try {
                abstractInterruptibleChannel.close();
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        try {
            ((RandomAccessFile)object).close();
        }
        catch (IOException iOException) {}
    }

    public static InputStream toStream(ByteBuffer byteBuffer) {
        return new ByteBufferStream(byteBuffer);
    }

    public static void toStream(ByteBuffer byteBuffer, OutputStream outputStream) throws IOException {
        Object object = ByteBufferUtil.getSafeArray(byteBuffer);
        if (object != null) {
            outputStream.write(((SafeArray)object).data, ((SafeArray)object).offset, ((SafeArray)object).offset + ((SafeArray)object).limit);
        } else {
            byte[] byArray = BUFFER_REF.getAndSet(null);
            object = byArray;
            if (byArray == null) {
                object = new byte[16384];
            }
            while (byteBuffer.remaining() > 0) {
                int n = Math.min(byteBuffer.remaining(), ((Object)object).length);
                byteBuffer.get((byte[])object, 0, n);
                outputStream.write((byte[])object, 0, n);
            }
            BUFFER_REF.set((byte[])object);
        }
    }

    private static class ByteBufferStream
    extends InputStream {
        private static final int UNSET = -1;
        private final ByteBuffer byteBuffer;
        private int markPos = -1;

        ByteBufferStream(ByteBuffer byteBuffer) {
            this.byteBuffer = byteBuffer;
        }

        @Override
        public int available() {
            return this.byteBuffer.remaining();
        }

        @Override
        public void mark(int n) {
            synchronized (this) {
                this.markPos = this.byteBuffer.position();
                return;
            }
        }

        @Override
        public boolean markSupported() {
            return true;
        }

        @Override
        public int read() {
            if (!this.byteBuffer.hasRemaining()) {
                return -1;
            }
            return this.byteBuffer.get() & 0xFF;
        }

        @Override
        public int read(byte[] byArray, int n, int n2) {
            if (!this.byteBuffer.hasRemaining()) {
                return -1;
            }
            n2 = Math.min(n2, this.available());
            this.byteBuffer.get(byArray, n, n2);
            return n2;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void reset() throws IOException {
            synchronized (this) {
                int n = this.markPos;
                if (n != -1) {
                    this.byteBuffer.position(n);
                    return;
                }
                IOException iOException = new IOException("Cannot reset to unset mark position");
                throw iOException;
            }
        }

        @Override
        public long skip(long l) {
            if (!this.byteBuffer.hasRemaining()) {
                return -1L;
            }
            l = Math.min(l, (long)this.available());
            ByteBuffer byteBuffer = this.byteBuffer;
            byteBuffer.position((int)((long)byteBuffer.position() + l));
            return l;
        }
    }

    static final class SafeArray {
        final byte[] data;
        final int limit;
        final int offset;

        SafeArray(byte[] byArray, int n, int n2) {
            this.data = byArray;
            this.offset = n;
            this.limit = n2;
        }
    }
}

