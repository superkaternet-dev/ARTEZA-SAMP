/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.drawable.Drawable
 *  android.widget.ImageView
 */
package com.bumptech.glide.request.target;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.ViewTarget;

public class ImageViewTargetFactory {
    public <Z> ViewTarget<ImageView, Z> buildTarget(ImageView object, Class<Z> clazz) {
        if (Bitmap.class.equals(clazz)) {
            return new BitmapImageViewTarget((ImageView)object);
        }
        if (Drawable.class.isAssignableFrom(clazz)) {
            return new DrawableImageViewTarget((ImageView)object);
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Unhandled class: ");
        ((StringBuilder)object).append(clazz);
        ((StringBuilder)object).append(", try .as*(Class).transcode(ResourceTranscoder)");
        throw new IllegalArgumentException(((StringBuilder)object).toString());
    }
}

