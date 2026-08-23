/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.motion.utils;

import androidx.constraintlayout.motion.utils.CurveFit;

public class LinearCurveFit
extends CurveFit {
    private static final String TAG = "LinearCurveFit";
    private double[] mT;
    private double mTotalLength;
    private double[][] mY;

    public LinearCurveFit(double[] dArray, double[][] dArray2) {
        block2: {
            this.mTotalLength = Double.NaN;
            int n = dArray.length;
            n = dArray2[0].length;
            this.mT = dArray;
            this.mY = dArray2;
            if (n <= 2) break block2;
            double d = 0.0;
            double d2 = 0.0;
            double d3 = 0.0;
            for (int i = 0; i < dArray.length; ++i) {
                double d4 = dArray2[i][0];
                double d5 = dArray2[i][0];
                if (i > 0) {
                    d += Math.hypot(d4 - d2, d5 - d3);
                }
                d2 = d4;
                d3 = d5;
            }
            this.mTotalLength = 0.0;
        }
    }

    private double getLength2D(double d) {
        if (Double.isNaN(this.mTotalLength)) {
            return 0.0;
        }
        Object object = this.mT;
        int n = ((double[])object).length;
        if (d <= object[0]) {
            return 0.0;
        }
        if (d >= object[n - 1]) {
            return this.mTotalLength;
        }
        double d2 = 0.0;
        double d3 = 0.0;
        double d4 = 0.0;
        for (int i = 0; i < n - 1; ++i) {
            object = this.mY;
            void var13_10 = object[i][0];
            void var11_9 = object[i][1];
            d4 = i > 0 ? d2 + Math.hypot((double)(var13_10 - d3), (double)(var11_9 - d4)) : d2;
            d3 = var13_10;
            void var9_8 = var11_9;
            object = this.mT;
            if (d == object[i]) {
                return d4;
            }
            if (d < object[i + 1]) {
                d2 = object[i + 1];
                d3 = object[i];
                d = (d - object[i]) / (d2 - d3);
                object = this.mY;
                d3 = object[i][0];
                d2 = object[i + 1][0];
                return d4 + Math.hypot((double)(var11_9 - ((1.0 - d) * object[i][1] + object[i + 1][1] * d)), (double)(var13_10 - ((1.0 - d) * d3 + d2 * d)));
            }
            d2 = d4;
            d4 = var9_8;
        }
        return 0.0;
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
            double d2 = object[i + 1];
            double d3 = object[i];
            d = (d - object[i]) / (d2 - d3);
            object = this.mY;
            return (1.0 - d) * object[i][n] + object[i + 1][n] * d;
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
            double d2 = object[i + 1];
            double d3 = object[i];
            d = (d - object[i]) / (d2 - d3);
            for (n3 = 0; n3 < n2; ++n3) {
                object = this.mY;
                dArray[n3] = (1.0 - d) * object[i][n3] + object[i + 1][n3] * d;
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
            double d2 = object[i + 1];
            double d3 = object[i];
            d = (d - object[i]) / (d2 - d3);
            for (n3 = 0; n3 < n2; ++n3) {
                object = this.mY;
                fArray[n3] = (float)((1.0 - d) * object[i][n3] + object[i + 1][n3] * d);
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
            d = (d - object[i]) / d2;
            object = this.mY;
            d = object[i][n];
            return (double)((object[i + 1][n] - d) / d2);
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
            d = (d - object[i]) / d2;
            for (n = 0; n < n2; ++n) {
                object = this.mY;
                d = object[i][n];
                dArray[n] = (object[i + 1][n] - d) / d2;
            }
            break;
        }
    }

    @Override
    public double[] getTimePoints() {
        return this.mT;
    }
}

