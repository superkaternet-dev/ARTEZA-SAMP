/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.drawable.Drawable
 */
package com.bumptech.glide.request.transition;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.request.transition.BitmapContainerTransitionFactory;
import com.bumptech.glide.request.transition.TransitionFactory;

public class BitmapTransitionFactory
extends BitmapContainerTransitionFactory<Bitmap> {
    public BitmapTransitionFactory(TransitionFactory<Drawable> transitionFactory) {
        super(transitionFactory);
    }

    @Override
    protected Bitmap getBitmap(Bitmap bitmap) {
        return bitmap;
    }
}

