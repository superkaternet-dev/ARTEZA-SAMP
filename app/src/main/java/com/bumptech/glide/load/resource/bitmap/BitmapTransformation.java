/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Bitmap
 */
package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.util.Util;

public abstract class BitmapTransformation
implements Transformation<Bitmap> {
    protected abstract Bitmap transform(BitmapPool var1, Bitmap var2, int var3, int var4);

    @Override
    public final Resource<Bitmap> transform(Context object, Resource<Bitmap> resource, int n, int n2) {
        if (Util.isValidDimensions(n, n2)) {
            Bitmap bitmap;
            object = Glide.get((Context)object).getBitmapPool();
            Bitmap bitmap2 = resource.get();
            if (n == Integer.MIN_VALUE) {
                n = bitmap2.getWidth();
            }
            if (n2 == Integer.MIN_VALUE) {
                n2 = bitmap2.getHeight();
            }
            object = bitmap2.equals(bitmap = this.transform((BitmapPool)object, bitmap2, n, n2)) ? resource : BitmapResource.obtain(bitmap, (BitmapPool)object);
            return object;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Cannot apply transformation on width: ");
        ((StringBuilder)object).append(n);
        ((StringBuilder)object).append(" or height: ");
        ((StringBuilder)object).append(n2);
        ((StringBuilder)object).append(" less than or equal to zero and not Target.SIZE_ORIGINAL");
        throw new IllegalArgumentException(((StringBuilder)object).toString());
    }
}

