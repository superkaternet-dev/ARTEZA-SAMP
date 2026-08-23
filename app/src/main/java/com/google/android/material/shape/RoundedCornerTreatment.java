/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.material.shape;

import com.google.android.material.shape.CornerTreatment;
import com.google.android.material.shape.ShapePath;

public class RoundedCornerTreatment
extends CornerTreatment {
    private final float radius;

    public RoundedCornerTreatment(float f) {
        this.radius = f;
    }

    @Override
    public void getCornerPath(float f, float f2, ShapePath shapePath) {
        shapePath.reset(0.0f, this.radius * f2);
        float f3 = this.radius;
        shapePath.addArc(0.0f, 0.0f, f3 * 2.0f * f2, f3 * 2.0f * f2, f + 180.0f, 90.0f);
    }
}

