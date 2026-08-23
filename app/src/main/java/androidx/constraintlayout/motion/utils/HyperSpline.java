/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.motion.utils;

public class HyperSpline {
    double[][] mCtl;
    Cubic[][] mCurve;
    double[] mCurveLength;
    int mDimensionality;
    int mPoints;
    double mTotalLength;

    public HyperSpline() {
    }

    public HyperSpline(double[][] dArray) {
        this.setup(dArray);
    }

    static Cubic[] calcNaturalCubic(int n, double[] dArray) {
        Object[] objectArray = new double[n];
        double[] dArray2 = new double[n];
        double[] dArray3 = new double[n];
        int n2 = n - 1;
        objectArray[0] = 0.5;
        for (n = 1; n < n2; ++n) {
            objectArray[n] = 1.0 / (4.0 - objectArray[n - 1]);
        }
        objectArray[n2] = 1.0 / (2.0 - objectArray[n2 - 1]);
        dArray2[0] = (dArray[1] - dArray[0]) * 3.0 * objectArray[0];
        for (n = 1; n < n2; ++n) {
            dArray2[n] = ((dArray[n + 1] - dArray[n - 1]) * 3.0 - dArray2[n - 1]) * objectArray[n];
        }
        dArray2[n2] = ((dArray[n2] - dArray[n2 - 1]) * 3.0 - dArray2[n2 - 1]) * objectArray[n2];
        dArray3[n2] = dArray2[n2];
        for (n = n2 - 1; n >= 0; --n) {
            dArray3[n] = dArray2[n] - objectArray[n] * dArray3[n + 1];
        }
        objectArray = new Cubic[n2];
        for (n = 0; n < n2; ++n) {
            objectArray[n] = (double)new Cubic((float)dArray[n], dArray3[n], (dArray[n + 1] - dArray[n]) * 3.0 - dArray3[n] * 2.0 - dArray3[n + 1], (dArray[n] - dArray[n + 1]) * 2.0 + dArray3[n] + dArray3[n + 1]);
        }
        return objectArray;
    }

    public double approxLength(Cubic[] cubicArray) {
        double d;
        double d2;
        double d3;
        double d4 = 0.0;
        int n = cubicArray.length;
        double[] dArray = new double[cubicArray.length];
        for (d3 = 0.0; d3 < 1.0; d3 += 0.1) {
            d2 = 0.0;
            for (n = 0; n < cubicArray.length; ++n) {
                double d5;
                d = dArray[n];
                dArray[n] = d5 = cubicArray[n].eval(d3);
                d2 += (d -= d5) * d;
            }
            d = d4;
            if (d3 > 0.0) {
                d = d4 + Math.sqrt(d2);
            }
            d4 = d;
        }
        d3 = 0.0;
        for (n = 0; n < cubicArray.length; ++n) {
            d2 = dArray[n];
            dArray[n] = d = cubicArray[n].eval(1.0);
            d3 += (d2 -= d) * d2;
        }
        return d4 + Math.sqrt(d3);
    }

    public double getPos(double d, int n) {
        int n2;
        double[] dArray;
        d = this.mTotalLength * d;
        for (n2 = 0; n2 < (dArray = this.mCurveLength).length - 1 && dArray[n2] < d; d -= dArray[n2], ++n2) {
        }
        return this.mCurve[n][n2].eval(d / dArray[n2]);
    }

    public void getPos(double d, double[] dArray) {
        int n;
        double[] dArray2;
        d = this.mTotalLength * d;
        for (n = 0; n < (dArray2 = this.mCurveLength).length - 1 && dArray2[n] < d; d -= dArray2[n], ++n) {
        }
        for (int i = 0; i < dArray.length; ++i) {
            dArray[i] = this.mCurve[i][n].eval(d / this.mCurveLength[n]);
        }
    }

    public void getPos(double d, float[] fArray) {
        int n;
        double[] dArray;
        d = this.mTotalLength * d;
        for (n = 0; n < (dArray = this.mCurveLength).length - 1 && dArray[n] < d; d -= dArray[n], ++n) {
        }
        for (int i = 0; i < fArray.length; ++i) {
            fArray[i] = (float)this.mCurve[i][n].eval(d / this.mCurveLength[n]);
        }
    }

    public void getVelocity(double d, double[] dArray) {
        int n;
        double[] dArray2;
        d = this.mTotalLength * d;
        for (n = 0; n < (dArray2 = this.mCurveLength).length - 1 && dArray2[n] < d; d -= dArray2[n], ++n) {
        }
        for (int i = 0; i < dArray.length; ++i) {
            dArray[i] = this.mCurve[i][n].vel(d / this.mCurveLength[n]);
        }
    }

    public void setup(double[][] object) {
        Object object2;
        int n;
        int n2;
        this.mDimensionality = n2 = object[0].length;
        this.mPoints = n = ((double[][])object).length;
        this.mCtl = new double[n2][n];
        this.mCurve = new Cubic[this.mDimensionality][];
        for (n2 = 0; n2 < this.mDimensionality; ++n2) {
            for (n = 0; n < this.mPoints; ++n) {
                this.mCtl[n2][n] = object[n][n2];
            }
        }
        for (n2 = 0; n2 < (n = this.mDimensionality); ++n2) {
            object = this.mCurve;
            object2 = this.mCtl;
            object[n2] = (double[])HyperSpline.calcNaturalCubic(object2[n2].length, object2[n2]);
        }
        this.mCurveLength = new double[this.mPoints - 1];
        this.mTotalLength = 0.0;
        object2 = new Cubic[n];
        for (n2 = 0; n2 < this.mCurveLength.length; ++n2) {
            for (n = 0; n < this.mDimensionality; ++n) {
                object2[n] = (double[])this.mCurve[n][n2];
            }
            double d = this.mTotalLength;
            object = this.mCurveLength;
            double d2 = this.approxLength((Cubic[])object2);
            object[n2] = (double[])d2;
            this.mTotalLength = d + d2;
        }
    }

    public static class Cubic {
        public static final double HALF = 0.5;
        public static final double THIRD = 0.3333333333333333;
        double mA;
        double mB;
        double mC;
        double mD;

        public Cubic(double d, double d2, double d3, double d4) {
            this.mA = d;
            this.mB = d2;
            this.mC = d3;
            this.mD = d4;
        }

        public double eval(double d) {
            return ((this.mD * d + this.mC) * d + this.mB) * d + this.mA;
        }

        public double vel(double d) {
            return (this.mD * 0.3333333333333333 * d + this.mC * 0.5) * d + this.mB;
        }
    }
}

