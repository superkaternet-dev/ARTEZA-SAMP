/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 */
package com.bumptech.glide.load.resource.drawable;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.drawable.DrawableResource;

final class NonOwnedDrawableResource
extends DrawableResource<Drawable> {
    private NonOwnedDrawableResource(Drawable drawable2) {
        super(drawable2);
    }

    static Resource<Drawable> newInstance(Drawable object) {
        object = object != null ? new NonOwnedDrawableResource((Drawable)object) : null;
        return object;
    }

    @Override
    public Class<Drawable> getResourceClass() {
        return this.drawable.getClass();
    }

    @Override
    public int getSize() {
        return Math.max(1, this.drawable.getIntrinsicWidth() * this.drawable.getIntrinsicHeight() * 4);
    }

    @Override
    public void recycle() {
    }
}

