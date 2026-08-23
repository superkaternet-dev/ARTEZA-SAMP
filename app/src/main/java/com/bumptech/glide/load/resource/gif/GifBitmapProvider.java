/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 */
package com.bumptech.glide.load.resource.gif;

import android.graphics.Bitmap;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

public final class GifBitmapProvider
implements GifDecoder.BitmapProvider {
    private final ArrayPool arrayPool;
    private final BitmapPool bitmapPool;

    public GifBitmapProvider(BitmapPool bitmapPool) {
        this(bitmapPool, null);
    }

    public GifBitmapProvider(BitmapPool bitmapPool, ArrayPool arrayPool) {
        this.bitmapPool = bitmapPool;
        this.arrayPool = arrayPool;
    }

    @Override
    public Bitmap obtain(int n, int n2, Bitmap.Config config) {
        return this.bitmapPool.getDirty(n, n2, config);
    }

    @Override
    public byte[] obtainByteArray(int n) {
        ArrayPool arrayPool = this.arrayPool;
        if (arrayPool == null) {
            return new byte[n];
        }
        return arrayPool.get(n, byte[].class);
    }

    @Override
    public int[] obtainIntArray(int n) {
        ArrayPool arrayPool = this.arrayPool;
        if (arrayPool == null) {
            return new int[n];
        }
        return arrayPool.get(n, int[].class);
    }

    @Override
    public void release(Bitmap bitmap) {
        this.bitmapPool.put(bitmap);
    }

    @Override
    public void release(byte[] byArray) {
        ArrayPool arrayPool = this.arrayPool;
        if (arrayPool == null) {
            return;
        }
        arrayPool.put(byArray);
    }

    @Override
    public void release(int[] nArray) {
        ArrayPool arrayPool = this.arrayPool;
        if (arrayPool == null) {
            return;
        }
        arrayPool.put(nArray);
    }
}

