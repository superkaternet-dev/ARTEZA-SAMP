/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.request.target;

import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.util.Util;

@Deprecated
public abstract class SimpleTarget<Z>
extends BaseTarget<Z> {
    private final int height;
    private final int width;

    public SimpleTarget() {
        this(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public SimpleTarget(int n, int n2) {
        this.width = n;
        this.height = n2;
    }

    @Override
    public final void getSize(SizeReadyCallback object) {
        if (Util.isValidDimensions(this.width, this.height)) {
            object.onSizeReady(this.width, this.height);
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Width and height must both be > 0 or Target#SIZE_ORIGINAL, but given width: ");
        ((StringBuilder)object).append(this.width);
        ((StringBuilder)object).append(" and height: ");
        ((StringBuilder)object).append(this.height);
        ((StringBuilder)object).append(", either provide dimensions in the constructor or call override()");
        throw new IllegalArgumentException(((StringBuilder)object).toString());
    }

    @Override
    public void removeCallback(SizeReadyCallback sizeReadyCallback) {
    }
}

