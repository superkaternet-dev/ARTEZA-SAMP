/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 */
package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

public class BitmapPoolAdapter
implements BitmapPool {
    @Override
    public void clearMemory() {
    }

    @Override
    public Bitmap get(int n, int n2, Bitmap.Config config) {
        return Bitmap.createBitmap((int)n, (int)n2, (Bitmap.Config)config);
    }

    @Override
    public Bitmap getDirty(int n, int n2, Bitmap.Config config) {
        return this.get(n, n2, config);
    }

    @Override
    public long getMaxSize() {
        return 0L;
    }

    @Override
    public void put(Bitmap bitmap) {
        bitmap.recycle();
    }

    @Override
    public void setSizeMultiplier(float f) {
    }

    @Override
    public void trimMemory(int n) {
    }
}

