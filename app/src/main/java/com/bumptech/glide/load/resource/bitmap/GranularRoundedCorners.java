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

public final class GranularRoundedCorners
extends BitmapTransformation {
    private static final String ID = "com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners";
    private static final byte[] ID_BYTES = "com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners".getBytes(CHARSET);
    private final float bottomLeft;
    private final float bottomRight;
    private final float topLeft;
    private final float topRight;

    public GranularRoundedCorners(float f, float f2, float f3, float f4) {
        this.topLeft = f;
        this.topRight = f2;
        this.bottomRight = f3;
        this.bottomLeft = f4;
    }

    @Override
    public boolean equals(Object object) {
        boolean bl = object instanceof GranularRoundedCorners;
        boolean bl2 = false;
        if (bl) {
            object = (GranularRoundedCorners)object;
            bl = bl2;
            if (this.topLeft == ((GranularRoundedCorners)object).topLeft) {
                bl = bl2;
                if (this.topRight == ((GranularRoundedCorners)object).topRight) {
                    bl = bl2;
                    if (this.bottomRight == ((GranularRoundedCorners)object).bottomRight) {
                        bl = bl2;
                        if (this.bottomLeft == ((GranularRoundedCorners)object).bottomLeft) {
                            bl = true;
                        }
                    }
                }
            }
            return bl;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int n = Util.hashCode(ID.hashCode(), Util.hashCode(this.topLeft));
        n = Util.hashCode(this.topRight, n);
        n = Util.hashCode(this.bottomRight, n);
        return Util.hashCode(this.bottomLeft, n);
    }

    @Override
    protected Bitmap transform(BitmapPool bitmapPool, Bitmap bitmap, int n, int n2) {
        return TransformationUtils.roundedCorners(bitmapPool, bitmap, this.topLeft, this.topRight, this.bottomRight, this.bottomLeft);
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
        messageDigest.update(ByteBuffer.allocate(16).putFloat(this.topLeft).putFloat(this.topRight).putFloat(this.bottomRight).putFloat(this.bottomLeft).array());
    }
}

