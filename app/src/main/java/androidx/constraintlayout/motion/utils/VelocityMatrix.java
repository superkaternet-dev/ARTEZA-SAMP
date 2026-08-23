/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.motion.utils;

import androidx.constraintlayout.motion.widget.KeyCycleOscillator;
import androidx.constraintlayout.motion.widget.SplineSet;

public class VelocityMatrix {
    private static String TAG = "VelocityMatrix";
    float mDRotate;
    float mDScaleX;
    float mDScaleY;
    float mDTranslateX;
    float mDTranslateY;
    float mRotate;

    public void applyTransform(float f, float f2, int n, int n2, float[] fArray) {
        float f3 = fArray[0];
        float f4 = fArray[1];
        f = (f - 0.5f) * 2.0f;
        float f5 = (f2 - 0.5f) * 2.0f;
        float f6 = this.mDTranslateX;
        float f7 = this.mDTranslateY;
        float f8 = this.mDScaleX;
        f2 = this.mDScaleY;
        float f9 = (float)Math.toRadians(this.mRotate);
        float f10 = (float)Math.toRadians(this.mDRotate);
        double d = (float)(-n) * f;
        double d2 = Math.sin(f9);
        Double.isNaN(d);
        double d3 = (float)n2 * f5;
        double d4 = Math.cos(f9);
        Double.isNaN(d3);
        float f11 = (float)(d * d2 - d3 * d4);
        d = (float)n * f;
        d3 = Math.cos(f9);
        Double.isNaN(d);
        d2 = (float)n2 * f5;
        d4 = Math.sin(f9);
        Double.isNaN(d2);
        f9 = (float)(d * d3 - d2 * d4);
        fArray[0] = f3 + f6 + f8 * f + f11 * f10;
        fArray[1] = f4 + f7 + f2 * f5 + f9 * f10;
    }

    public void clear() {
        this.mDRotate = 0.0f;
        this.mDTranslateY = 0.0f;
        this.mDTranslateX = 0.0f;
        this.mDScaleY = 0.0f;
        this.mDScaleX = 0.0f;
    }

    public void setRotationVelocity(KeyCycleOscillator keyCycleOscillator, float f) {
        if (keyCycleOscillator != null) {
            this.mDRotate = keyCycleOscillator.getSlope(f);
        }
    }

    public void setRotationVelocity(SplineSet splineSet, float f) {
        if (splineSet != null) {
            this.mDRotate = splineSet.getSlope(f);
            this.mRotate = splineSet.get(f);
        }
    }

    public void setScaleVelocity(KeyCycleOscillator keyCycleOscillator, KeyCycleOscillator keyCycleOscillator2, float f) {
        if (keyCycleOscillator == null && keyCycleOscillator2 == null) {
            return;
        }
        if (keyCycleOscillator == null) {
            this.mDScaleX = keyCycleOscillator.getSlope(f);
        }
        if (keyCycleOscillator2 == null) {
            this.mDScaleY = keyCycleOscillator2.getSlope(f);
        }
    }

    public void setScaleVelocity(SplineSet splineSet, SplineSet splineSet2, float f) {
        if (splineSet != null) {
            this.mDScaleX = splineSet.getSlope(f);
        }
        if (splineSet2 != null) {
            this.mDScaleY = splineSet2.getSlope(f);
        }
    }

    public void setTranslationVelocity(KeyCycleOscillator keyCycleOscillator, KeyCycleOscillator keyCycleOscillator2, float f) {
        if (keyCycleOscillator != null) {
            this.mDTranslateX = keyCycleOscillator.getSlope(f);
        }
        if (keyCycleOscillator2 != null) {
            this.mDTranslateY = keyCycleOscillator2.getSlope(f);
        }
    }

    public void setTranslationVelocity(SplineSet splineSet, SplineSet splineSet2, float f) {
        if (splineSet != null) {
            this.mDTranslateX = splineSet.getSlope(f);
        }
        if (splineSet2 != null) {
            this.mDTranslateY = splineSet2.getSlope(f);
        }
    }
}

