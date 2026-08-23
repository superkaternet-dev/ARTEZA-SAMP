/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 */
package com.bumptech.glide.request.target;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.manager.LifecycleListener;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;

public interface Target<R>
extends LifecycleListener {
    public static final int SIZE_ORIGINAL = Integer.MIN_VALUE;

    public Request getRequest();

    public void getSize(SizeReadyCallback var1);

    public void onLoadCleared(Drawable var1);

    public void onLoadFailed(Drawable var1);

    public void onLoadStarted(Drawable var1);

    public void onResourceReady(R var1, Transition<? super R> var2);

    public void removeCallback(SizeReadyCallback var1);

    public void setRequest(Request var1);
}

