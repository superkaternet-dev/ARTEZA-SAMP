/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.graphics.drawable.BitmapDrawable
 *  android.graphics.drawable.Drawable
 */
package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.DrawableToBitmapConverter;
import com.bumptech.glide.load.resource.bitmap.LazyBitmapDrawableResource;
import java.security.MessageDigest;

public class DrawableTransformation
implements Transformation<Drawable> {
    private final boolean isRequired;
    private final Transformation<Bitmap> wrapped;

    public DrawableTransformation(Transformation<Bitmap> transformation, boolean bl) {
        this.wrapped = transformation;
        this.isRequired = bl;
    }

    private Resource<Drawable> newDrawableResource(Context context, Resource<Bitmap> resource) {
        return LazyBitmapDrawableResource.obtain(context.getResources(), resource);
    }

    public Transformation<BitmapDrawable> asBitmapDrawable() {
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DrawableTransformation) {
            object = (DrawableTransformation)object;
            return this.wrapped.equals(((DrawableTransformation)object).wrapped);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.wrapped.hashCode();
    }

    @Override
    public Resource<Drawable> transform(Context object, Resource<Drawable> resource, int n, int n2) {
        Object object2 = Glide.get((Context)object).getBitmapPool();
        Object object3 = resource.get();
        if ((object2 = DrawableToBitmapConverter.convert((BitmapPool)object2, (Drawable)object3, n, n2)) == null) {
            if (!this.isRequired) {
                return resource;
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Unable to convert ");
            ((StringBuilder)object).append(object3);
            ((StringBuilder)object).append(" to a Bitmap");
            throw new IllegalArgumentException(((StringBuilder)object).toString());
        }
        object3 = this.wrapped.transform((Context)object, (Resource<Bitmap>)object2, n, n2);
        if (object3.equals(object2)) {
            object3.recycle();
            return resource;
        }
        return this.newDrawableResource((Context)object, (Resource<Bitmap>)object3);
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        this.wrapped.updateDiskCacheKey(messageDigest);
    }
}

