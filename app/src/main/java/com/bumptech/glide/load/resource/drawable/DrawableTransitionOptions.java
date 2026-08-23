/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 */
package com.bumptech.glide.load.resource.drawable;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.bumptech.glide.request.transition.TransitionFactory;

public final class DrawableTransitionOptions
extends TransitionOptions<DrawableTransitionOptions, Drawable> {
    public static DrawableTransitionOptions with(TransitionFactory<Drawable> transitionFactory) {
        return (DrawableTransitionOptions)new DrawableTransitionOptions().transition(transitionFactory);
    }

    public static DrawableTransitionOptions withCrossFade() {
        return new DrawableTransitionOptions().crossFade();
    }

    public static DrawableTransitionOptions withCrossFade(int n) {
        return new DrawableTransitionOptions().crossFade(n);
    }

    public static DrawableTransitionOptions withCrossFade(DrawableCrossFadeFactory.Builder builder) {
        return new DrawableTransitionOptions().crossFade(builder);
    }

    public static DrawableTransitionOptions withCrossFade(DrawableCrossFadeFactory drawableCrossFadeFactory) {
        return new DrawableTransitionOptions().crossFade(drawableCrossFadeFactory);
    }

    public DrawableTransitionOptions crossFade() {
        return this.crossFade(new DrawableCrossFadeFactory.Builder());
    }

    public DrawableTransitionOptions crossFade(int n) {
        return this.crossFade(new DrawableCrossFadeFactory.Builder(n));
    }

    public DrawableTransitionOptions crossFade(DrawableCrossFadeFactory.Builder builder) {
        return this.crossFade(builder.build());
    }

    public DrawableTransitionOptions crossFade(DrawableCrossFadeFactory drawableCrossFadeFactory) {
        return (DrawableTransitionOptions)this.transition(drawableCrossFadeFactory);
    }
}

