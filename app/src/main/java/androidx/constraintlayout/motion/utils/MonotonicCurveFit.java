/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.motion.utils;

import androidx.constraintlayout.motion.utils.CurveFit;

public class MonotonicCurveFit
extends CurveFit {
    private static final String TAG = "MonotonicCurveFit";
    private double[] mT;
    private double[][] mTangent;
    private double[][] mY;

    public MonotonicCurveFit(double[] dArray, double[][] dArray2) {
        double d;
        double d2;
        int n;
        int n2;
        int n3 = dArray.length;
        int n4 = dArray2[0].length;
        double[][] dArray3 = new double[n3 - 1][n4];
        double[][] dArray4 = new double[n3][n4];
        for (n2 = 0; n2 < n4; ++n2) {
            for (n = 0; n < n3 - 1; ++n) {
                d2 = dArray[n + 1];
                d = dArray[n];
                dArray3[n][n2] = (dArray2[n + 1][n2] - dArray2[n][n2]) / (d2 - d);
                dArray4[n][n2] = n == 0 ? dArray3[n][n2] : (dArray3[n - 1][n2] + dArray3[n][n2]) * 0.5;
            }
            dArray4[n3 - 1][n2] = dArray3[n3 - 2][n2];
        }
        for (n2 = 0; n2 < n3 - 1; ++n2) {
            for (n = 0; n < n4; ++n) {
                if (dArray3[n2][n] == 0.0) {
                    dArray4[n2][n] = 0.0;
                    dArray4[n2 + 1][n] = 0.0;
                    continue;
                }
                d = dArray4[n2][n] / dArray3[n2][n];
                d2 = dArray4[n2 + 1][n] / dArray3[n2][n];
                double d3 = Math.hypot(d, d2);
                if (!(d3 > 9.0)) continue;
                d3 = 3.0 / d3;
                dArray4[n2][n] = d3 * d * dArray3[n2][n];
                dArray4[n2 + 1][n] = d3 * d2 * dArray3[n2][n];
            }
        }
        this.mT = dArray;
        this.mY = dArray2;
        this.mTangent = dArray4;
    }

    private static double diff(double d, double d2, double d3, double d4, double d5, double d6) {
        double d7 = d2 * d2;
        return -6.0 * d7 * d4 + d2 * 6.0 * d4 + d7 * 6.0 * d3 - 6.0 * d2 * d3 + d * 3.0 * d6 * d7 + 3.0 * d * d5 * d7 - 2.0 * d * d6 * d2 - 4.0 * d * d5 * d2 + d * d5;
    }

    private static double interpolate(double d, double d2, double d3, double d4, double d5, double d6) {
        double d7 = d2 * d2;
        double d8 = d7 * d2;
        return -2.0 * d8 * d4 + d7 * 3.0 * d4 + d8 * 2.0 * d3 - 3.0 * d7 * d3 + d3 + d * d6 * d8 + d * d5 * d8 - d * d6 * d7 - d * 2.0 * d5 * d7 + d * d5 * d2;
    }

    @Override
    public double getPos(double d, int n) {
        Object object = this.mT;
        int n2 = ((double[])object).length;
        if (d <= object[0]) {
            return this.mY[0][n];
        }
        if (d >= object[n2 - 1]) {
            return this.mY[n2 - 1][n];
        }
        for (int i = 0; i < n2 - 1; ++i) {
            object = this.mT;
            if (d == object[i]) {
                return this.mY[i][n];
            }
            if (!(d < object[i + 1])) continue;
            double d2 = object[i + 1] - object[i];
            double d3 = (d - object[i]) / d2;
            object = this.mY;
            void var8_8 = object[i][n];
            d = object[i + 1][n];
            object = this.mTangent;
            return MonotonicCurveFit.interpolate(d2, d3, (double)var8_8, d, (double)object[i][n], (double)object[i + 1][n]);
        }
        return 0.0;
    }

    @Override
    public void getPos(double d, double[] dArray) {
        Object object = this.mT;
        int n = ((double[])object).length;
        int n2 = this.mY[0].length;
        if (d <= object[0]) {
            for (int i = 0; i < n2; ++i) {
                dArray[i] = this.mY[0][i];
            }
            return;
        }
        if (d >= object[n - 1]) {
            for (int i = 0; i < n2; ++i) {
                dArray[i] = this.mY[n - 1][i];
            }
            return;
        }
        for (int i = 0; i < n - 1; ++i) {
            int n3;
            if (d == this.mT[i]) {
                for (n3 = 0; n3 < n2; ++n3) {
                    dArray[n3] = this.mY[i][n3];
                }
            }
            if (!(d < (object = this.mT)[i + 1])) continue;
            double d2 = object[i + 1] - object[i];
            double d3 = (d - object[i]) / d2;
            for (n3 = 0; n3 < n2; ++n3) {
                object = this.mY;
                void var8_12 = object[i][n3];
                d = object[i + 1][n3];
                object = this.mTangent;
                dArray[n3] = MonotonicCurveFit.interpolate(d2, d3, (double)var8_12, d, (double)object[i][n3], (double)object[i + 1][n3]);
            }
            return;
        }
    }

    @Override
    public void getPos(double d, float[] fArray) {
        Object object = this.mT;
        int n = ((double[])object).length;
        int n2 = this.mY[0].length;
        if (d <= object[0]) {
            for (int i = 0; i < n2; ++i) {
                fArray[i] = (float)this.mY[0][i];
            }
            return;
        }
        if (d >= object[n - 1]) {
            for (int i = 0; i < n2; ++i) {
                fArray[i] = (float)this.mY[n - 1][i];
            }
            return;
        }
        for (int i = 0; i < n - 1; ++i) {
            int n3;
            if (d == this.mT[i]) {
                for (n3 = 0; n3 < n2; ++n3) {
                    fArray[n3] = (float)this.mY[i][n3];
                }
            }
            if (!(d < (object = this.mT)[i + 1])) continue;
            double d2 = object[i + 1] - object[i];
            double d3 = (d - object[i]) / d2;
            for (n3 = 0; n3 < n2; ++n3) {
                object = this.mY;
                void var8_12 = object[i][n3];
                d = object[i + 1][n3];
                object = this.mTangent;
                fArray[n3] = (float)MonotonicCurveFit.interpolate(d2, d3, (double)var8_12, d, (double)object[i][n3], (double)object[i + 1][n3]);
            }
            return;
        }
    }

    @Override
    public double getSlope(double d, int n) {
        Object object = this.mT;
        int n2 = ((double[])object).length;
        if (d < object[0]) {
            d = object[0];
        } else if (d >= object[n2 - 1]) {
            d = object[n2 - 1];
        }
        for (int i = 0; i < n2 - 1; ++i) {
            object = this.mT;
            if (!(d <= object[i + 1])) continue;
            double d2 = object[i + 1] - object[i];
            double d3 = (d - object[i]) / d2;
            object = this.mY;
            d = object[i][n];
            void var8_8 = object[i + 1][n];
            object = this.mTangent;
            return MonotonicCurveFit.diff(d2, d3, d, (double)var8_8, (double)object[i][n], (double)object[i + 1][n]) / d2;
        }
        return 0.0;
    }

    @Override
    public void getSlope(double d, double[] dArray) {
        Object object = this.mT;
        int n = ((double[])object).length;
        int n2 = this.mY[0].length;
        if (d <= object[0]) {
            d = object[0];
        } else if (d >= object[n - 1]) {
            d = object[n - 1];
        }
        for (int i = 0; i < n - 1; ++i) {
            object = this.mT;
            if (!(d <= object[i + 1])) continue;
            double d2 = object[i + 1] - object[i];
            double d3 = (d - object[i]) / d2;
            for (n = 0; n < n2; ++n) {
                object = this.mY;
                void var8_9 = object[i][n];
                d = object[i + 1][n];
                object = this.mTangent;
                dArray[n] = MonotonicCurveFit.diff(d2, d3, (double)var8_9, d, (double)object[i][n], (double)object[i + 1][n]) / d2;
            }
            break;
        }
    }

    @Override
    public double[] getTimePoints() {
        return this.mT;
    }
}

