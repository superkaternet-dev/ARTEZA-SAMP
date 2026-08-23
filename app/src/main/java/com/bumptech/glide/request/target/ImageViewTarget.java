/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Animatable
 *  android.graphics.drawable.Drawable
 *  android.widget.ImageView
 */
package com.bumptech.glide.request.target;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;

public abstract class ImageViewTarget<Z>
extends ViewTarget<ImageView, Z>
implements Transition.ViewAdapter {
    private Animatable animatable;

    public ImageViewTarget(ImageView imageView) {
        super(imageView);
    }

    @Deprecated
    public ImageViewTarget(ImageView imageView, boolean bl) {
        super(imageView, bl);
    }

    private void maybeUpdateAnimatable(Z object) {
        if (object instanceof Animatable) {
            object = (Animatable)object;
            this.animatable = object;
            object.start();
        } else {
            this.animatable = null;
        }
    }

    private void setResourceInternal(Z z) {
        this.setResource(z);
        this.maybeUpdateAnimatable(z);
    }

    @Override
    public Drawable getCurrentDrawable() {
        return ((ImageView)this.view).getDrawable();
    }

    @Override
    public void onLoadCleared(Drawable drawable2) {
        super.onLoadCleared(drawable2);
        Animatable animatable = this.animatable;
        if (animatable != null) {
            animatable.stop();
        }
        this.setResourceInternal(null);
        this.setDrawable(drawable2);
    }

    @Override
    public void onLoadFailed(Drawable drawable2) {
        super.onLoadFailed(drawable2);
        this.setResourceInternal(null);
        this.setDrawable(drawable2);
    }

    @Override
    public void onLoadStarted(Drawable drawable2) {
        super.onLoadStarted(drawable2);
        this.setResourceInternal(null);
        this.setDrawable(drawable2);
    }

    @Override
    public void onResourceReady(Z z, Transition<? super Z> transition) {
        if (transition != null && transition.transition(z, this)) {
            this.maybeUpdateAnimatable(z);
        } else {
            this.setResourceInternal(z);
        }
    }

    @Override
    public void onStart() {
        Animatable animatable = this.animatable;
        if (animatable != null) {
            animatable.start();
        }
    }

    @Override
    public void onStop() {
        Animatable animatable = this.animatable;
        if (animatable != null) {
            animatable.stop();
        }
    }

    @Override
    public void setDrawable(Drawable drawable2) {
        ((ImageView)this.view).setImageDrawable(drawable2);
    }

    protected abstract void setResource(Z var1);
}

