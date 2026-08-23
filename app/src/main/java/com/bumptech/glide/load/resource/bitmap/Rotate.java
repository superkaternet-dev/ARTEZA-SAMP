/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 */
package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.util.Util;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class Rotate
extends BitmapTransformation {
    private static final String ID = "com.bumptech.glide.load.resource.bitmap.Rotate";
    private static final byte[] ID_BYTES = "com.bumptech.glide.load.resource.bitmap.Rotate".getBytes(CHARSET);
    private final int degreesToRotate;

    public Rotate(int n) {
        this.degreesToRotate = n;
    }

    @Override
    public boolean equals(Object object) {
        boolean bl = object instanceof Rotate;
        boolean bl2 = false;
        if (bl) {
            object = (Rotate)object;
            if (this.degreesToRotate == ((Rotate)object).degreesToRotate) {
                bl2 = true;
            }
            return bl2;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Util.hashCode(ID.hashCode(), Util.hashCode(this.degreesToRotate));
    }

    @Override
    protected Bitmap transform(BitmapPool bitmapPool, Bitmap bitmap, int n, int n2) {
        return TransformationUtils.rotateImage(bitmap, this.degreesToRotate);
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
        messageDigest.update(ByteBuffer.allocate(4).putInt(this.degreesToRotate).array());
    }
}

