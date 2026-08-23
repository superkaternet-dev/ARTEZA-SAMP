/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 */
package com.bumptech.glide.request.target;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.Util;

public abstract class CustomTarget<T>
implements Target<T> {
    private final int height;
    private Request request;
    private final int width;

    public CustomTarget() {
        this(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public CustomTarget(int n, int n2) {
        if (Util.isValidDimensions(n, n2)) {
            this.width = n;
            this.height = n2;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Width and height must both be > 0 or Target#SIZE_ORIGINAL, but given width: ");
        stringBuilder.append(n);
        stringBuilder.append(" and height: ");
        stringBuilder.append(n2);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    @Override
    public final Request getRequest() {
        return this.request;
    }

    @Override
    public final void getSize(SizeReadyCallback sizeReadyCallback) {
        sizeReadyCallback.onSizeReady(this.width, this.height);
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onLoadFailed(Drawable drawable2) {
    }

    @Override
    public void onLoadStarted(Drawable drawable2) {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public final void removeCallback(SizeReadyCallback sizeReadyCallback) {
    }

    @Override
    public final void setRequest(Request request) {
        this.request = request;
    }
}

