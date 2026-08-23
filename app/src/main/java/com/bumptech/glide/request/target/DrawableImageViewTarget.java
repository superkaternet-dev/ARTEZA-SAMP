/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 *  android.widget.ImageView
 */
package com.bumptech.glide.request.target;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.bumptech.glide.request.target.ImageViewTarget;

public class DrawableImageViewTarget
extends ImageViewTarget<Drawable> {
    public DrawableImageViewTarget(ImageView imageView) {
        super(imageView);
    }

    @Deprecated
    public DrawableImageViewTarget(ImageView imageView, boolean bl) {
        super(imageView, bl);
    }

    @Override
    protected void setResource(Drawable drawable2) {
        ((ImageView)this.view).setImageDrawable(drawable2);
    }
}

