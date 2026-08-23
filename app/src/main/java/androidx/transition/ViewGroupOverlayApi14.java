/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.View
 *  android.view.ViewGroup
 */
package androidx.transition;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.transition.ViewGroupOverlayImpl;
import androidx.transition.ViewOverlayApi14;

class ViewGroupOverlayApi14
extends ViewOverlayApi14
implements ViewGroupOverlayImpl {
    ViewGroupOverlayApi14(Context context, ViewGroup viewGroup, View view) {
        super(context, viewGroup, view);
    }

    static ViewGroupOverlayApi14 createFrom(ViewGroup viewGroup) {
        return (ViewGroupOverlayApi14)ViewOverlayApi14.createFrom((View)viewGroup);
    }

    @Override
    public void add(View view) {
        this.mOverlayViewGroup.add(view);
    }

    @Override
    public void remove(View view) {
        this.mOverlayViewGroup.remove(view);
    }
}

