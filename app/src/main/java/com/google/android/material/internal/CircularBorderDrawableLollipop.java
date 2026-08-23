/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Outline
 */
package com.google.android.material.internal;

import android.graphics.Outline;
import com.google.android.material.internal.CircularBorderDrawable;

public class CircularBorderDrawableLollipop
extends CircularBorderDrawable {
    public void getOutline(Outline outline) {
        this.copyBounds(this.rect);
        outline.setOval(this.rect);
    }
}

