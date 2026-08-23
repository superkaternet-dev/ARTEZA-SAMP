/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.ImageView
 */
package com.bumptech.glide.request.target;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.request.target.FixedSizeDrawable;
import com.bumptech.glide.request.target.ImageViewTarget;

public abstract class ThumbnailImageViewTarget<T>
extends ImageViewTarget<T> {
    public ThumbnailImageViewTarget(ImageView imageView) {
        super(imageView);
    }

    @Deprecated
    public ThumbnailImageViewTarget(ImageView imageView, boolean bl) {
        super(imageView, bl);
    }

    protected abstract Drawable getDrawable(T var1);

    @Override
    protected void setResource(T object) {
        ViewGroup.LayoutParams layoutParams = ((ImageView)this.view).getLayoutParams();
        Drawable drawable2 = this.getDrawable(object);
        object = drawable2;
        if (layoutParams != null) {
            object = drawable2;
            if (layoutParams.width > 0) {
                object = drawable2;
                if (layoutParams.height > 0) {
                    object = new FixedSizeDrawable(drawable2, layoutParams.width, layoutParams.height);
                }
            }
        }
        ((ImageView)this.view).setImageDrawable(object);
    }
}

