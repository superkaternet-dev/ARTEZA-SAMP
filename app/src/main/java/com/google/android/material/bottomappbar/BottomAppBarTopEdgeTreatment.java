/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.material.bottomappbar;

import com.google.android.material.shape.EdgeTreatment;
import com.google.android.material.shape.ShapePath;

public class BottomAppBarTopEdgeTreatment
extends EdgeTreatment {
    private static final int ANGLE_LEFT = 180;
    private static final int ANGLE_UP = 270;
    private static final int ARC_HALF = 180;
    private static final int ARC_QUARTER = 90;
    private float cradleVerticalOffset;
    private float fabDiameter;
    private float fabMargin;
    private float horizontalOffset;
    private float roundedCornerRadius;

    public BottomAppBarTopEdgeTreatment(float f, float f2, float f3) {
        this.fabMargin = f;
        this.roundedCornerRadius = f2;
        this.cradleVerticalOffset = f3;
        if (!(f3 < 0.0f)) {
            this.horizontalOffset = 0.0f;
            return;
        }
        throw new IllegalArgumentException("cradleVerticalOffset must be positive.");
    }

    float getCradleVerticalOffset() {
        return this.cradleVerticalOffset;
    }

    @Override
    public void getEdgePath(float f, float f2, ShapePath shapePath) {
        float f3 = this.fabDiameter;
        if (f3 == 0.0f) {
            shapePath.lineTo(f, 0.0f);
            return;
        }
        f3 = (this.fabMargin * 2.0f + f3) / 2.0f;
        float f4 = f2 * this.roundedCornerRadius;
        float f5 = f / 2.0f + this.horizontalOffset;
        if ((f2 = this.cradleVerticalOffset * f2 + (1.0f - f2) * f3) / f3 >= 1.0f) {
            shapePath.lineTo(f, 0.0f);
            return;
        }
        float f6 = f3 + f4;
        float f7 = f2 + f4;
        float f8 = (float)Math.sqrt(f6 * f6 - f7 * f7);
        float f9 = f5 - f8;
        f6 = f5 + f8;
        f8 = (float)Math.toDegrees(Math.atan(f8 / f7));
        f7 = 90.0f - f8;
        shapePath.lineTo(f9 - f4, 0.0f);
        shapePath.addArc(f9 - f4, 0.0f, f9 + f4, f4 * 2.0f, 270.0f, f8);
        shapePath.addArc(f5 - f3, -f3 - f2, f5 + f3, f3 - f2, 180.0f - f7, f7 * 2.0f - 180.0f);
        shapePath.addArc(f6 - f4, 0.0f, f6 + f4, f4 * 2.0f, 270.0f - f8, f8);
        shapePath.lineTo(f, 0.0f);
    }

    float getFabCradleMargin() {
        return this.fabMargin;
    }

    float getFabCradleRoundedCornerRadius() {
        return this.roundedCornerRadius;
    }

    float getFabDiameter() {
        return this.fabDiameter;
    }

    float getHorizontalOffset() {
        return this.horizontalOffset;
    }

    void setCradleVerticalOffset(float f) {
        this.cradleVerticalOffset = f;
    }

    void setFabCradleMargin(float f) {
        this.fabMargin = f;
    }

    void setFabCradleRoundedCornerRadius(float f) {
        this.roundedCornerRadius = f;
    }

    void setFabDiameter(float f) {
        this.fabDiameter = f;
    }

    void setHorizontalOffset(float f) {
        this.horizontalOffset = f;
    }
}

