/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.TypeEvaluator
 *  android.graphics.Matrix
 */
package com.google.android.material.animation;

import android.animation.TypeEvaluator;
import android.graphics.Matrix;

public class MatrixEvaluator
implements TypeEvaluator<Matrix> {
    private final float[] tempEndValues;
    private final Matrix tempMatrix;
    private final float[] tempStartValues = new float[9];

    public MatrixEvaluator() {
        this.tempEndValues = new float[9];
        this.tempMatrix = new Matrix();
    }

    public Matrix evaluate(float f, Matrix object, Matrix object2) {
        object.getValues(this.tempStartValues);
        object2.getValues(this.tempEndValues);
        for (int i = 0; i < 9; ++i) {
            object2 = this.tempEndValues;
            Matrix matrix = object2[i];
            object = this.tempStartValues;
            Matrix matrix2 = object[i];
            object2[i] = object[i] + f * (matrix - matrix2);
        }
        this.tempMatrix.setValues(this.tempEndValues);
        return this.tempMatrix;
    }
}

