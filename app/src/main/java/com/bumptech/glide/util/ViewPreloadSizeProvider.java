/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 *  android.view.View
 */
package com.bumptech.glide.util;

import android.graphics.drawable.Drawable;
import android.view.View;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;
import java.util.Arrays;

public class ViewPreloadSizeProvider<T>
implements ListPreloader.PreloadSizeProvider<T>,
SizeReadyCallback {
    private int[] size;
    private SizeViewTarget viewTarget;

    public ViewPreloadSizeProvider() {
    }

    public ViewPreloadSizeProvider(View object) {
        object = new SizeViewTarget((View)object);
        this.viewTarget = object;
        ((CustomViewTarget)object).getSize(this);
    }

    @Override
    public int[] getPreloadSize(T object, int n, int n2) {
        object = this.size;
        if (object == null) {
            return null;
        }
        return Arrays.copyOf(object, ((T)object).length);
    }

    @Override
    public void onSizeReady(int n, int n2) {
        this.size = new int[]{n, n2};
        this.viewTarget = null;
    }

    public void setView(View object) {
        if (this.size == null && this.viewTarget == null) {
            object = new SizeViewTarget((View)object);
            this.viewTarget = object;
            ((CustomViewTarget)object).getSize(this);
            return;
        }
    }

    static final class SizeViewTarget
    extends CustomViewTarget<View, Object> {
        SizeViewTarget(View view) {
            super(view);
        }

        @Override
        public void onLoadFailed(Drawable drawable2) {
        }

        @Override
        protected void onResourceCleared(Drawable drawable2) {
        }

        @Override
        public void onResourceReady(Object object, Transition<? super Object> transition) {
        }
    }
}

