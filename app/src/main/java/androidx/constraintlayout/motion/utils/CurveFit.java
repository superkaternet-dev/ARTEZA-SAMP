/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.motion.utils;

import androidx.constraintlayout.motion.utils.ArcCurveFit;
import androidx.constraintlayout.motion.utils.LinearCurveFit;
import androidx.constraintlayout.motion.utils.MonotonicCurveFit;

public abstract class CurveFit {
    public static final int CONSTANT = 2;
    public static final int LINEAR = 1;
    public static final int SPLINE = 0;

    public static CurveFit get(int n, double[] dArray, double[][] dArray2) {
        if (dArray.length == 1) {
            n = 2;
        }
        switch (n) {
            default: {
                return new LinearCurveFit(dArray, dArray2);
            }
            case 2: {
                return new Constant(dArray[0], dArray2[0]);
            }
            case 0: 
        }
        return new MonotonicCurveFit(dArray, dArray2);
    }

    public static CurveFit getArc(int[] nArray, double[] dArray, double[][] dArray2) {
        return new ArcCurveFit(nArray, dArray, dArray2);
    }

    public abstract double getPos(double var1, int var3);

    public abstract void getPos(double var1, double[] var3);

    public abstract void getPos(double var1, float[] var3);

    public abstract double getSlope(double var1, int var3);

    public abstract void getSlope(double var1, double[] var3);

    public abstract double[] getTimePoints();

    static class Constant
    extends CurveFit {
        double mTime;
        double[] mValue;

        Constant(double d, double[] dArray) {
            this.mTime = d;
            this.mValue = dArray;
        }

        @Override
        public double getPos(double d, int n) {
            return this.mValue[n];
        }

        @Override
        public void getPos(double d, double[] dArray) {
            double[] dArray2 = this.mValue;
            System.arraycopy(dArray2, 0, dArray, 0, dArray2.length);
        }

        @Override
        public void getPos(double d, float[] fArray) {
            double[] dArray;
            for (int i = 0; i < (dArray = this.mValue).length; ++i) {
                fArray[i] = (float)dArray[i];
            }
        }

        @Override
        public double getSlope(double d, int n) {
            return 0.0;
        }

        @Override
        public void getSlope(double d, double[] dArray) {
            for (int i = 0; i < this.mValue.length; ++i) {
                dArray[i] = 0.0;
            }
        }

        @Override
        public double[] getTimePoints() {
            return new double[]{this.mTime};
        }
    }
}

