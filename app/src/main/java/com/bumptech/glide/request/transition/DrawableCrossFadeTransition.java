/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.ColorDrawable
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.TransitionDrawable
 */
package com.bumptech.glide.request.transition;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import com.bumptech.glide.request.transition.Transition;

public class DrawableCrossFadeTransition
implements Transition<Drawable> {
    private final int duration;
    private final boolean isCrossFadeEnabled;

    public DrawableCrossFadeTransition(int n, boolean bl) {
        this.duration = n;
        this.isCrossFadeEnabled = bl;
    }

    @Override
    public boolean transition(Drawable drawable2, Transition.ViewAdapter viewAdapter) {
        Drawable drawable3;
        Drawable drawable4 = drawable3 = viewAdapter.getCurrentDrawable();
        if (drawable3 == null) {
            drawable4 = new ColorDrawable(0);
        }
        drawable2 = new TransitionDrawable(new Drawable[]{drawable4, drawable2});
        drawable2.setCrossFadeEnabled(this.isCrossFadeEnabled);
        drawable2.startTransition(this.duration);
        viewAdapter.setDrawable(drawable2);
        return true;
    }
}

