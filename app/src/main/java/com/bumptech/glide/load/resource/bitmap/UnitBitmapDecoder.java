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
import com.bumptech.glide.util.Util;

public final class UnitBitmapDecoder
implements ResourceDecoder<Bitmap, Bitmap> {
    @Override
    public Resource<Bitmap> decode(Bitmap bitmap, int n, int n2, Options options) {
        return new NonOwnedBitmapResource(bitmap);
    }

    @Override
    public boolean handles(Bitmap bitmap, Options options) {
        return true;
    }

    private static final class NonOwnedBitmapResource
    implements Resource<Bitmap> {
        private final Bitmap bitmap;

        NonOwnedBitmapResource(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        public Bitmap get() {
            return this.bitmap;
        }

        @Override
        public Class<Bitmap> getResourceClass() {
            return Bitmap.class;
        }

        @Override
        public int getSize() {
            return Util.getBitmapByteSize(this.bitmap);
        }

        @Override
        public void recycle() {
        }
    }
}

