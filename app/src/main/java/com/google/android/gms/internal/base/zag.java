/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$ConstantState
 */
package com.google.android.gms.internal.base;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import com.google.android.gms.internal.base.zaf;

final class zag
extends Drawable {
    private static final zag zaa = new zag();
    private static final zaf zab = new zaf(null);

    private zag() {
    }

    static /* bridge */ /* synthetic */ zag zaa() {
        return zaa;
    }

    public final void draw(Canvas canvas) {
    }

    public final Drawable.ConstantState getConstantState() {
        return zab;
    }

    public final int getOpacity() {
        return -2;
    }

    public final void setAlpha(int n) {
    }

    public final void setColorFilter(ColorFilter colorFilter) {
    }
}

