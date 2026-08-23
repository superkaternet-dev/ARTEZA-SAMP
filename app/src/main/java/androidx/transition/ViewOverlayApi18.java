/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 *  android.view.View
 *  android.view.ViewOverlay
 */
package androidx.transition;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewOverlay;
import androidx.transition.ViewOverlayImpl;

class ViewOverlayApi18
implements ViewOverlayImpl {
    private final ViewOverlay mViewOverlay;

    ViewOverlayApi18(View view) {
        this.mViewOverlay = view.getOverlay();
    }

    @Override
    public void add(Drawable drawable2) {
        this.mViewOverlay.add(drawable2);
    }

    @Override
    public void clear() {
        this.mViewOverlay.clear();
    }

    @Override
    public void remove(Drawable drawable2) {
        this.mViewOverlay.remove(drawable2);
    }
}

