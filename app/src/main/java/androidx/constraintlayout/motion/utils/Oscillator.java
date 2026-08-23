/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.motion.utils;

import java.util.Arrays;

public class Oscillator {
    public static final int BOUNCE = 6;
    public static final int COS_WAVE = 5;
    public static final int REVERSE_SAW_WAVE = 4;
    public static final int SAW_WAVE = 3;
    public static final int SIN_WAVE = 0;
    public static final int SQUARE_WAVE = 1;
    public static String TAG = "Oscillator";
    public static final int TRIANGLE_WAVE = 2;
    double PI2;
    double[] mArea;
    private boolean mNormalized = false;
    float[] mPeriod = new float[0];
    double[] mPosition = new double[0];
    int mType;

    public Oscillator() {
        this.PI2 = Math.PI * 2;
    }

    public void addPoint(double d, float f) {
        int n;
        int n2 = this.mPeriod.length + 1;
        int n3 = n = Arrays.binarySearch(this.mPosition, d);
        if (n < 0) {
            n3 = -n - 1;
        }
        this.mPosition = Arrays.copyOf(this.mPosition, n2);
        this.mPeriod = Arrays.copyOf(this.mPeriod, n2);
        this.mArea = new double[n2];
        double[] dArray = this.mPosition;
        System.arraycopy(dArray, n3, dArray, n3 + 1, n2 - n3 - 1);
        this.mPosition[n3] = d;
        this.mPeriod[n3] = f;
        this.mNormalized = false;
    }

    double getDP(double d) {
        if (d <= 0.0) {
            d = 1.0E-5;
        } else if (d >= 1.0) {
            d = 0.999999;
        }
        int n = Arrays.binarySearch(this.mPosition, d);
        double d2 = 0.0;
        if (n > 0) {
            return 0.0;
        }
        if (n != 0) {
            n = -n - 1;
            float[] fArray = this.mPeriod;
            double d3 = fArray[n] - fArray[n - 1];
            double[] dArray = this.mPosition;
            d2 = dArray[n];
            double d4 = dArray[n - 1];
            Double.isNaN(d3);
            d3 /= d2 - d4;
            d2 = fArray[n - 1];
            d4 = dArray[n - 1];
            Double.isNaN(d2);
            d2 = d3 * d + (d2 - d4 * d3);
        }
        return d2;
    }

    double getP(double d) {
        if (d < 0.0) {
            d = 0.0;
        } else if (d > 1.0) {
            d = 1.0;
        }
        int n = Arrays.binarySearch(this.mPosition, d);
        double d2 = 0.0;
        if (n > 0) {
            d = 1.0;
        } else if (n != 0) {
            n = -n - 1;
            float[] fArray = this.mPeriod;
            d2 = fArray[n] - fArray[n - 1];
            double[] dArray = this.mPosition;
            double d3 = dArray[n];
            double d4 = dArray[n - 1];
            Double.isNaN(d2);
            d3 = d2 / (d3 - d4);
            d2 = this.mArea[n - 1];
            double d5 = fArray[n - 1];
            d4 = dArray[n - 1];
            Double.isNaN(d5);
            d = d2 + (d5 - d4 * d3) * (d - dArray[n - 1]) + (d * d - dArray[n - 1] * dArray[n - 1]) * d3 / 2.0;
        } else {
            d = d2;
        }
        return d;
    }

    public double getSlope(double d) {
        switch (this.mType) {
            default: {
                return this.PI2 * this.getDP(d) * Math.cos(this.PI2 * this.getP(d));
            }
            case 6: {
                return this.getDP(d) * 4.0 * ((this.getP(d) * 4.0 + 2.0) % 4.0 - 2.0);
            }
            case 5: {
                return -this.PI2 * this.getDP(d) * Math.sin(this.PI2 * this.getP(d));
            }
            case 4: {
                return -this.getDP(d) * 2.0;
            }
            case 3: {
                return this.getDP(d) * 2.0;
            }
            case 2: {
                return this.getDP(d) * 4.0 * Math.signum((this.getP(d) * 4.0 + 3.0) % 4.0 - 2.0);
            }
            case 1: 
        }
        return 0.0;
    }

    public double getValue(double d) {
        switch (this.mType) {
            default: {
                return Math.sin(this.PI2 * this.getP(d));
            }
            case 6: {
                d = 1.0 - Math.abs(this.getP(d) * 4.0 % 4.0 - 2.0);
                return 1.0 - d * d;
            }
            case 5: {
                return Math.cos(this.PI2 * this.getP(d));
            }
            case 4: {
                return 1.0 - (this.getP(d) * 2.0 + 1.0) % 2.0;
            }
            case 3: {
                return (this.getP(d) * 2.0 + 1.0) % 2.0 - 1.0;
            }
            case 2: {
                return 1.0 - Math.abs((this.getP(d) * 4.0 + 1.0) % 4.0 - 2.0);
            }
            case 1: 
        }
        return Math.signum(0.5 - this.getP(d) % 1.0);
    }

    public void normalize() {
        double d;
        float f;
        double d2;
        Object[] objectArray;
        int n;
        double d3 = 0.0;
        double d4 = 0.0;
        for (n = 0; n < (objectArray = this.mPeriod).length; ++n) {
            d2 = objectArray[n];
            Double.isNaN(d2);
            d4 += d2;
        }
        for (n = 1; n < (objectArray = this.mPeriod).length; ++n) {
            f = (objectArray[n - 1] + objectArray[n]) / 2.0f;
            objectArray = this.mPosition;
            d = objectArray[n];
            d2 = objectArray[n - 1];
            double d5 = f;
            Double.isNaN(d5);
            d3 += d5 * (d - d2);
        }
        for (n = 0; n < (objectArray = this.mPeriod).length; ++n) {
            d2 = objectArray[n];
            d = d4 / d3;
            Double.isNaN(d2);
            objectArray[n] = (float)(d2 * d);
        }
        this.mArea[0] = 0.0;
        for (n = 1; n < (objectArray = this.mPeriod).length; ++n) {
            f = (objectArray[n - 1] + objectArray[n]) / 2.0f;
            objectArray = this.mPosition;
            d2 = objectArray[n];
            d = objectArray[n - 1];
            objectArray = this.mArea;
            d4 = objectArray[n - 1];
            d3 = f;
            Double.isNaN(d3);
            objectArray[n] = (float)(d4 + d3 * (d2 - d));
        }
        this.mNormalized = true;
    }

    public void setType(int n) {
        this.mType = n;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("pos =");
        stringBuilder.append(Arrays.toString(this.mPosition));
        stringBuilder.append(" period=");
        stringBuilder.append(Arrays.toString(this.mPeriod));
        return stringBuilder.toString();
    }
}

