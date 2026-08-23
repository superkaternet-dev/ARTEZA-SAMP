/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.animation.Interpolator
 */
package androidx.interpolator.view.animation;

import android.view.animation.Interpolator;

abstract class LookupTableInterpolator
implements Interpolator {
    private final float mStepSize;
    private final float[] mValues;

    protected LookupTableInterpolator(float[] fArray) {
        this.mValues = fArray;
        this.mStepSize = 1.0f / (float)(fArray.length - 1);
    }

    public float getInterpolation(float f) {
        if (f >= 1.0f) {
            return 1.0f;
        }
        if (f <= 0.0f) {
            return 0.0f;
        }
        float[] fArray = this.mValues;
        int n = Math.min((int)((float)(fArray.length - 1) * f), fArray.length - 2);
        float f2 = n;
        float f3 = this.mStepSize;
        f = (f - f2 * f3) / f3;
        fArray = this.mValues;
        return fArray[n] + (fArray[n + 1] - fArray[n]) * f;
    }
}

