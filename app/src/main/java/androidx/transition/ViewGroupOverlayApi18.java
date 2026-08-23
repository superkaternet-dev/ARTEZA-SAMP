/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroupOverlay
 */
package androidx.transition;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import androidx.transition.ViewGroupOverlayImpl;

class ViewGroupOverlayApi18
implements ViewGroupOverlayImpl {
    private final ViewGroupOverlay mViewGroupOverlay;

    ViewGroupOverlayApi18(ViewGroup viewGroup) {
        this.mViewGroupOverlay = viewGroup.getOverlay();
    }

    @Override
    public void add(Drawable drawable2) {
        this.mViewGroupOverlay.add(drawable2);
    }

    @Override
    public void add(View view) {
        this.mViewGroupOverlay.add(view);
    }

    @Override
    public void clear() {
        this.mViewGroupOverlay.clear();
    }

    @Override
    public void remove(Drawable drawable2) {
        this.mViewGroupOverlay.remove(drawable2);
    }

    @Override
    public void remove(View view) {
        this.mViewGroupOverlay.remove(view);
    }
}

