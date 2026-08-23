/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.material.shape;

import com.google.android.material.shape.EdgeTreatment;
import com.google.android.material.shape.ShapePath;

public class TriangleEdgeTreatment
extends EdgeTreatment {
    private final boolean inside;
    private final float size;

    public TriangleEdgeTreatment(float f, boolean bl) {
        this.size = f;
        this.inside = bl;
    }

    @Override
    public void getEdgePath(float f, float f2, ShapePath shapePath) {
        shapePath.lineTo(f / 2.0f - this.size * f2, 0.0f);
        float f3 = f / 2.0f;
        float f4 = this.inside ? this.size : -this.size;
        shapePath.lineTo(f3, f4 * f2);
        shapePath.lineTo(f / 2.0f + this.size * f2, 0.0f);
        shapePath.lineTo(f, 0.0f);
    }
}

