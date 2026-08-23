/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.drawable.BitmapDrawable
 *  android.graphics.drawable.Drawable
 *  android.widget.ImageView
 */
package com.bumptech.glide.request.target;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.bumptech.glide.request.target.ThumbnailImageViewTarget;

public class BitmapThumbnailImageViewTarget
extends ThumbnailImageViewTarget<Bitmap> {
    public BitmapThumbnailImageViewTarget(ImageView imageView) {
        super(imageView);
    }

    @Deprecated
    public BitmapThumbnailImageViewTarget(ImageView imageView, boolean bl) {
        super(imageView, bl);
    }

    @Override
    protected Drawable getDrawable(Bitmap bitmap) {
        return new BitmapDrawable(((ImageView)this.view).getResources(), bitmap);
    }
}

