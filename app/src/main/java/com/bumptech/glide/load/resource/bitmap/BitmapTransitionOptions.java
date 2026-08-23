/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.drawable.Drawable
 */
package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.request.transition.BitmapTransitionFactory;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.bumptech.glide.request.transition.TransitionFactory;

public final class BitmapTransitionOptions
extends TransitionOptions<BitmapTransitionOptions, Bitmap> {
    public static BitmapTransitionOptions with(TransitionFactory<Bitmap> transitionFactory) {
        return (BitmapTransitionOptions)new BitmapTransitionOptions().transition(transitionFactory);
    }

    public static BitmapTransitionOptions withCrossFade() {
        return new BitmapTransitionOptions().crossFade();
    }

    public static BitmapTransitionOptions withCrossFade(int n) {
        return new BitmapTransitionOptions().crossFade(n);
    }

    public static BitmapTransitionOptions withCrossFade(DrawableCrossFadeFactory.Builder builder) {
        return new BitmapTransitionOptions().crossFade(builder);
    }

    public static BitmapTransitionOptions withCrossFade(DrawableCrossFadeFactory drawableCrossFadeFactory) {
        return new BitmapTransitionOptions().crossFade(drawableCrossFadeFactory);
    }

    public static BitmapTransitionOptions withWrapped(TransitionFactory<Drawable> transitionFactory) {
        return new BitmapTransitionOptions().transitionUsing(transitionFactory);
    }

    public BitmapTransitionOptions crossFade() {
        return this.crossFade(new DrawableCrossFadeFactory.Builder());
    }

    public BitmapTransitionOptions crossFade(int n) {
        return this.crossFade(new DrawableCrossFadeFactory.Builder(n));
    }

    public BitmapTransitionOptions crossFade(DrawableCrossFadeFactory.Builder builder) {
        return this.transitionUsing(builder.build());
    }

    public BitmapTransitionOptions crossFade(DrawableCrossFadeFactory drawableCrossFadeFactory) {
        return this.transitionUsing(drawableCrossFadeFactory);
    }

    public BitmapTransitionOptions transitionUsing(TransitionFactory<Drawable> transitionFactory) {
        return (BitmapTransitionOptions)this.transition(new BitmapTransitionFactory(transitionFactory));
    }
}

