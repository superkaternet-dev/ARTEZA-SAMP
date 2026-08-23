/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 */
package com.bumptech.glide.load.resource.drawable;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.drawable.NonOwnedDrawableResource;

public class UnitDrawableDecoder
implements ResourceDecoder<Drawable, Drawable> {
    @Override
    public Resource<Drawable> decode(Drawable drawable2, int n, int n2, Options options) {
        return NonOwnedDrawableResource.newInstance(drawable2);
    }

    @Override
    public boolean handles(Drawable drawable2, Options options) {
        return true;
    }
}

