/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.widget.ImageView
 */
package com.bumptech.glide.request.target;

import android.graphics.Bitmap;
import android.widget.ImageView;
import com.bumptech.glide.request.target.ImageViewTarget;

public class BitmapImageViewTarget
extends ImageViewTarget<Bitmap> {
    public BitmapImageViewTarget(ImageView imageView) {
        super(imageView);
    }

    @Deprecated
    public BitmapImageViewTarget(ImageView imageView, boolean bl) {
        super(imageView, bl);
    }

    @Override
    protected void setResource(Bitmap bitmap) {
        ((ImageView)this.view).setImageBitmap(bitmap);
    }
}

