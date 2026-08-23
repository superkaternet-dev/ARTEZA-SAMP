/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.TypeEvaluator
 */
package androidx.transition;

import android.animation.TypeEvaluator;

class FloatArrayEvaluator
implements TypeEvaluator<float[]> {
    private float[] mArray;

    FloatArrayEvaluator(float[] fArray) {
        this.mArray = fArray;
    }

    public float[] evaluate(float f, float[] fArray, float[] fArray2) {
        float[] fArray3;
        float[] fArray4 = fArray3 = this.mArray;
        if (fArray3 == null) {
            fArray4 = new float[fArray.length];
        }
        for (int i = 0; i < fArray4.length; ++i) {
            float f2 = fArray[i];
            fArray4[i] = (fArray2[i] - f2) * f + f2;
        }
        return fArray4;
    }
}

