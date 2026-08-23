/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 */
package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream;
import com.bumptech.glide.util.ExceptionPassthroughInputStream;
import com.bumptech.glide.util.MarkEnforcingInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamBitmapDecoder
implements ResourceDecoder<InputStream, Bitmap> {
    private final ArrayPool byteArrayPool;
    private final Downsampler downsampler;

    public StreamBitmapDecoder(Downsampler downsampler, ArrayPool arrayPool) {
        this.downsampler = downsampler;
        this.byteArrayPool = arrayPool;
    }

    @Override
    public Resource<Bitmap> decode(InputStream inputStream, int n, int n2, Options object) throws IOException {
        boolean bl;
        if (inputStream instanceof RecyclableBufferedInputStream) {
            inputStream = (RecyclableBufferedInputStream)inputStream;
            bl = false;
        } else {
            inputStream = new RecyclableBufferedInputStream(inputStream, this.byteArrayPool);
            bl = true;
        }
        ExceptionPassthroughInputStream exceptionPassthroughInputStream = ExceptionPassthroughInputStream.obtain(inputStream);
        MarkEnforcingInputStream markEnforcingInputStream = new MarkEnforcingInputStream(exceptionPassthroughInputStream);
        UntrustedCallbacks untrustedCallbacks = new UntrustedCallbacks((RecyclableBufferedInputStream)inputStream, exceptionPassthroughInputStream);
        try {
            object = this.downsampler.decode(markEnforcingInputStream, n, n2, (Options)object, (Downsampler.DecodeCallbacks)untrustedCallbacks);
            return object;
        }
        finally {
            exceptionPassthroughInputStream.release();
            if (bl) {
                ((RecyclableBufferedInputStream)inputStream).release();
            }
        }
    }

    @Override
    public boolean handles(InputStream inputStream, Options options) {
        return this.downsampler.handles(inputStream);
    }

    static class UntrustedCallbacks
    implements Downsampler.DecodeCallbacks {
        private final RecyclableBufferedInputStream bufferedStream;
        private final ExceptionPassthroughInputStream exceptionStream;

        UntrustedCallbacks(RecyclableBufferedInputStream recyclableBufferedInputStream, ExceptionPassthroughInputStream exceptionPassthroughInputStream) {
            this.bufferedStream = recyclableBufferedInputStream;
            this.exceptionStream = exceptionPassthroughInputStream;
        }

        @Override
        public void onDecodeComplete(BitmapPool bitmapPool, Bitmap bitmap) throws IOException {
            IOException iOException = this.exceptionStream.getException();
            if (iOException != null) {
                if (bitmap != null) {
                    bitmapPool.put(bitmap);
                }
                throw iOException;
            }
        }

        @Override
        public void onObtainBounds() {
            this.bufferedStream.fixMarkLimit();
        }
    }
}

