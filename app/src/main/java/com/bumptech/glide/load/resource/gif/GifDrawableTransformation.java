/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Bitmap
 */
package com.bumptech.glide.load.resource.gif;

import android.content.Context;
import android.graphics.Bitmap;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.util.Preconditions;
import java.security.MessageDigest;

public class GifDrawableTransformation
implements Transformation<GifDrawable> {
    private final Transformation<Bitmap> wrapped;

    public GifDrawableTransformation(Transformation<Bitmap> transformation) {
        this.wrapped = Preconditions.checkNotNull(transformation);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof GifDrawableTransformation) {
            object = (GifDrawableTransformation)object;
            return this.wrapped.equals(((GifDrawableTransformation)object).wrapped);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.wrapped.hashCode();
    }

    @Override
    public Resource<GifDrawable> transform(Context object, Resource<GifDrawable> resource, int n, int n2) {
        GifDrawable gifDrawable = resource.get();
        Object object2 = Glide.get((Context)object).getBitmapPool();
        object2 = new BitmapResource(gifDrawable.getFirstFrame(), (BitmapPool)object2);
        if (!object2.equals(object = this.wrapped.transform((Context)object, (Resource<Bitmap>)object2, n, n2))) {
            object2.recycle();
        }
        object = (Bitmap)object.get();
        gifDrawable.setFrameTransformation(this.wrapped, (Bitmap)object);
        return resource;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        this.wrapped.updateDiskCacheKey(messageDigest);
    }
}

