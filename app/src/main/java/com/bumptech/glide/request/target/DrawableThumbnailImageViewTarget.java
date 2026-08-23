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
import com.bumptech.glide.request.target.ThumbnailImageViewTarget;

public class DrawableThumbnailImageViewTarget
extends ThumbnailImageViewTarget<Drawable> {
    public DrawableThumbnailImageViewTarget(ImageView imageView) {
        super(imageView);
    }

    @Deprecated
    public DrawableThumbnailImageViewTarget(ImageView imageView, boolean bl) {
        super(imageView, bl);
    }

    @Override
    protected Drawable getDrawable(Drawable drawable2) {
        return drawable2;
    }
}

