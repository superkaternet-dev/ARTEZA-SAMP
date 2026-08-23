/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.material.shape;

import com.google.android.material.shape.CornerTreatment;
import com.google.android.material.shape.ShapePath;

public class CutCornerTreatment
extends CornerTreatment {
    private final float size;

    public CutCornerTreatment(float f) {
        this.size = f;
    }

    @Override
    public void getCornerPath(float f, float f2, ShapePath shapePath) {
        shapePath.reset(0.0f, this.size * f2);
        double d = Math.sin(f);
        double d2 = this.size;
        Double.isNaN(d2);
        double d3 = f2;
        Double.isNaN(d3);
        float f3 = (float)(d * d2 * d3);
        d3 = Math.cos(f);
        d = this.size;
        Double.isNaN(d);
        d2 = f2;
        Double.isNaN(d2);
        shapePath.lineTo(f3, (float)(d3 * d * d2));
    }
}

