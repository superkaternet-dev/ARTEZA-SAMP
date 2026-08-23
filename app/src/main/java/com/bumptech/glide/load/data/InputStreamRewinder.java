/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.data;

import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class InputStreamRewinder
implements DataRewinder<InputStream> {
    private static final int MARK_READ_LIMIT = 0x500000;
    private final RecyclableBufferedInputStream bufferedStream;

    public InputStreamRewinder(InputStream inputStream, ArrayPool arrayPool) {
        inputStream = new RecyclableBufferedInputStream(inputStream, arrayPool);
        this.bufferedStream = inputStream;
        ((RecyclableBufferedInputStream)inputStream).mark(0x500000);
    }

    @Override
    public void cleanup() {
        this.bufferedStream.release();
    }

    public void fixMarkLimits() {
        this.bufferedStream.fixMarkLimit();
    }

    @Override
    public InputStream rewindAndGet() throws IOException {
        this.bufferedStream.reset();
        return this.bufferedStream;
    }

    public static final class Factory
    implements DataRewinder.Factory<InputStream> {
        private final ArrayPool byteArrayPool;

        public Factory(ArrayPool arrayPool) {
            this.byteArrayPool = arrayPool;
        }

        @Override
        public DataRewinder<InputStream> build(InputStream inputStream) {
            return new InputStreamRewinder(inputStream, this.byteArrayPool);
        }

        @Override
        public Class<InputStream> getDataClass() {
            return InputStream.class;
        }
    }
}

