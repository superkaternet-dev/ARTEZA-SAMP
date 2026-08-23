/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.motion.utils;

import androidx.constraintlayout.motion.utils.CurveFit;
import java.util.Arrays;

class ArcCurveFit
extends CurveFit {
    public static final int ARC_START_FLIP = 3;
    public static final int ARC_START_HORIZONTAL = 2;
    public static final int ARC_START_LINEAR = 0;
    public static final int ARC_START_VERTICAL = 1;
    private static final int START_HORIZONTAL = 2;
    private static final int START_LINEAR = 3;
    private static final int START_VERTICAL = 1;
    Arc[] mArcs;
    private final double[] mTime;

    public ArcCurveFit(int[] nArray, double[] dArray, double[][] dArray2) {
        Arc[] arcArray;
        this.mTime = dArray;
        this.mArcs = new Arc[dArray.length - 1];
        int n = 1;
        int n2 = 1;
        for (int i = 0; i < (arcArray = this.mArcs).length; ++i) {
            int n3 = nArray[i];
            int n4 = 2;
            switch (n3) {
                default: {
                    break;
                }
                case 3: {
                    n = n2 == 1 ? n4 : 1;
                    n2 = n;
                    break;
                }
                case 2: {
                    n = 2;
                    n2 = 2;
                    break;
                }
                case 1: {
                    n = 1;
                    n2 = 1;
                    break;
                }
                case 0: {
                    n = 3;
                }
            }
            arcArray[i] = new Arc(n, dArray[i], dArray[i + 1], dArray2[i][0], dArray2[i][1], dArray2[i + 1][0], dArray2[i + 1][1]);
        }
    }

    @Override
    public double getPos(double d, int n) {
        Arc[] arcArray;
        double d2;
        if (d < this.mArcs[0].mTime1) {
            d2 = this.mArcs[0].mTime1;
        } else {
            arcArray = this.mArcs;
            d2 = d;
            if (d > arcArray[arcArray.length - 1].mTime2) {
                arcArray = this.mArcs;
                d2 = arcArray[arcArray.length - 1].mTime2;
            }
        }
        for (int i = 0; i < (arcArray = this.mArcs).length; ++i) {
            if (!(d2 <= arcArray[i].mTime2)) continue;
            if (this.mArcs[i].linear) {
                if (n == 0) {
                    return this.mArcs[i].getLinearX(d2);
                }
                return this.mArcs[i].getLinearY(d2);
            }
            this.mArcs[i].setPoint(d2);
            if (n == 0) {
                return this.mArcs[i].getX();
            }
            return this.mArcs[i].getY();
        }
        return Double.NaN;
    }

    @Override
    public void getPos(double d, double[] dArray) {
        double d2 = d;
        if (d < this.mArcs[0].mTime1) {
            d2 = this.mArcs[0].mTime1;
        }
        Arc[] arcArray = this.mArcs;
        d = d2;
        if (d2 > arcArray[arcArray.length - 1].mTime2) {
            arcArray = this.mArcs;
            d = arcArray[arcArray.length - 1].mTime2;
        }
        for (int i = 0; i < (arcArray = this.mArcs).length; ++i) {
            if (!(d <= arcArray[i].mTime2)) continue;
            if (this.mArcs[i].linear) {
                dArray[0] = this.mArcs[i].getLinearX(d);
                dArray[1] = this.mArcs[i].getLinearY(d);
                return;
            }
            this.mArcs[i].setPoint(d);
            dArray[0] = this.mArcs[i].getX();
            dArray[1] = this.mArcs[i].getY();
            return;
        }
    }

    @Override
    public void getPos(double d, float[] fArray) {
        Arc[] arcArray;
        double d2;
        if (d < this.mArcs[0].mTime1) {
            d2 = this.mArcs[0].mTime1;
        } else {
            arcArray = this.mArcs;
            d2 = d;
            if (d > arcArray[arcArray.length - 1].mTime2) {
                arcArray = this.mArcs;
                d2 = arcArray[arcArray.length - 1].mTime2;
            }
        }
        for (int i = 0; i < (arcArray = this.mArcs).length; ++i) {
            if (!(d2 <= arcArray[i].mTime2)) continue;
            if (this.mArcs[i].linear) {
                fArray[0] = (float)this.mArcs[i].getLinearX(d2);
                fArray[1] = (float)this.mArcs[i].getLinearY(d2);
                return;
            }
            this.mArcs[i].setPoint(d2);
            fArray[0] = (float)this.mArcs[i].getX();
            fArray[1] = (float)this.mArcs[i].getY();
            return;
        }
    }

    @Override
    public double getSlope(double d, int n) {
        double d2 = d;
        if (d < this.mArcs[0].mTime1) {
            d2 = this.mArcs[0].mTime1;
        }
        Arc[] arcArray = this.mArcs;
        d = d2;
        if (d2 > arcArray[arcArray.length - 1].mTime2) {
            arcArray = this.mArcs;
            d = arcArray[arcArray.length - 1].mTime2;
        }
        for (int i = 0; i < (arcArray = this.mArcs).length; ++i) {
            if (!(d <= arcArray[i].mTime2)) continue;
            if (this.mArcs[i].linear) {
                if (n == 0) {
                    return this.mArcs[i].getLinearDX(d);
                }
                return this.mArcs[i].getLinearDY(d);
            }
            this.mArcs[i].setPoint(d);
            if (n == 0) {
                return this.mArcs[i].getDX();
            }
            return this.mArcs[i].getDY();
        }
        return Double.NaN;
    }

    @Override
    public void getSlope(double d, double[] dArray) {
        Arc[] arcArray;
        double d2;
        if (d < this.mArcs[0].mTime1) {
            d2 = this.mArcs[0].mTime1;
        } else {
            arcArray = this.mArcs;
            d2 = d;
            if (d > arcArray[arcArray.length - 1].mTime2) {
                arcArray = this.mArcs;
                d2 = arcArray[arcArray.length - 1].mTime2;
            }
        }
        for (int i = 0; i < (arcArray = this.mArcs).length; ++i) {
            if (!(d2 <= arcArray[i].mTime2)) continue;
            if (this.mArcs[i].linear) {
                dArray[0] = this.mArcs[i].getLinearDX(d2);
                dArray[1] = this.mArcs[i].getLinearDY(d2);
                return;
            }
            this.mArcs[i].setPoint(d2);
            dArray[0] = this.mArcs[i].getDX();
            dArray[1] = this.mArcs[i].getDY();
            return;
        }
    }

    @Override
    public double[] getTimePoints() {
        return this.mTime;
    }

    private static class Arc {
        private static final double EPSILON = 0.001;
        private static final String TAG = "Arc";
        private static double[] ourPercent = new double[91];
        boolean linear;
        double mArcDistance;
        double mArcVelocity;
        double mEllipseA;
        double mEllipseB;
        double mEllipseCenterX;
        double mEllipseCenterY;
        double[] mLut;
        double mOneOverDeltaTime;
        double mTime1;
        double mTime2;
        double mTmpCosAngle;
        double mTmpSinAngle;
        boolean mVertical;
        double mX1;
        double mX2;
        double mY1;
        double mY2;

        Arc(int n, double d, double d2, double d3, double d4, double d5, double d6) {
            boolean bl = false;
            this.linear = false;
            int n2 = 1;
            if (n == 1) {
                bl = true;
            }
            this.mVertical = bl;
            this.mTime1 = d;
            this.mTime2 = d2;
            this.mOneOverDeltaTime = 1.0 / (d2 - d);
            if (3 == n) {
                this.linear = true;
            }
            d2 = d5 - d3;
            d = d6 - d4;
            if (!(this.linear || Math.abs(d2) < 0.001 || Math.abs(d) < 0.001)) {
                this.mLut = new double[101];
                bl = this.mVertical;
                n = n2;
                if (bl) {
                    n = -1;
                }
                double d7 = n;
                Double.isNaN(d7);
                this.mEllipseA = d7 * d2;
                n = bl ? 1 : -1;
                d2 = n;
                Double.isNaN(d2);
                this.mEllipseB = d2 * d;
                d = bl ? d5 : d3;
                this.mEllipseCenterX = d;
                d = bl ? d4 : d6;
                this.mEllipseCenterY = d;
                this.buildTable(d3, d4, d5, d6);
                this.mArcVelocity = this.mArcDistance * this.mOneOverDeltaTime;
                return;
            }
            this.linear = true;
            this.mX1 = d3;
            this.mX2 = d5;
            this.mY1 = d4;
            this.mY2 = d6;
            this.mArcDistance = d3 = Math.hypot(d, d2);
            this.mArcVelocity = d3 * this.mOneOverDeltaTime;
            d4 = this.mTime2;
            d3 = this.mTime1;
            this.mEllipseCenterX = d2 / (d4 - d3);
            this.mEllipseCenterY = d / (d4 - d3);
        }

        private void buildTable(double d, double d2, double d3, double d4) {
            double[] dArray;
            int n;
            d3 -= d;
            d2 -= d4;
            d4 = 0.0;
            double d5 = 0.0;
            d = 0.0;
            for (n = 0; n < (dArray = ourPercent).length; ++n) {
                double d6 = n;
                Double.isNaN(d6);
                double d7 = dArray.length - 1;
                Double.isNaN(d7);
                d7 = Math.toRadians(d6 * 90.0 / d7);
                d6 = Math.sin(d7);
                d7 = Math.cos(d7);
                d6 = d3 * d6;
                d7 = d2 * d7;
                if (n > 0) {
                    Arc.ourPercent[n] = d = Math.hypot(d6 - d4, d7 - d5) + d;
                }
                d4 = d6;
                d5 = d7;
            }
            this.mArcDistance = d;
            for (n = 0; n < (dArray = ourPercent).length; ++n) {
                dArray[n] = dArray[n] / d;
            }
            for (n = 0; n < (dArray = this.mLut).length; ++n) {
                d2 = n;
                d = dArray.length - 1;
                Double.isNaN(d2);
                Double.isNaN(d);
                int n2 = Arrays.binarySearch(ourPercent, d2 /= d);
                if (n2 >= 0) {
                    this.mLut[n] = n2 / (ourPercent.length - 1);
                    continue;
                }
                if (n2 == -1) {
                    this.mLut[n] = 0.0;
                    continue;
                }
                int n3 = -n2 - 2;
                n2 = -n2;
                d = n3;
                dArray = ourPercent;
                d2 = (d2 - dArray[n3]) / (dArray[n2 - 1] - dArray[n3]);
                Double.isNaN(d);
                d3 = dArray.length - 1;
                Double.isNaN(d3);
                this.mLut[n] = d = (d + d2) / d3;
            }
        }

        double getDX() {
            double d = this.mEllipseA * this.mTmpCosAngle;
            double d2 = -this.mEllipseB;
            double d3 = this.mTmpSinAngle;
            d3 = this.mArcVelocity / Math.hypot(d, d2 * d3);
            d = this.mVertical ? -d * d3 : (d *= d3);
            return d;
        }

        double getDY() {
            double d = this.mEllipseA;
            double d2 = this.mTmpCosAngle;
            double d3 = -this.mEllipseB * this.mTmpSinAngle;
            d = this.mArcVelocity / Math.hypot(d * d2, d3);
            d3 = this.mVertical ? -d3 * d : (d3 *= d);
            return d3;
        }

        public double getLinearDX(double d) {
            return this.mEllipseCenterX;
        }

        public double getLinearDY(double d) {
            return this.mEllipseCenterY;
        }

        public double getLinearX(double d) {
            double d2 = this.mTime1;
            double d3 = this.mOneOverDeltaTime;
            double d4 = this.mX1;
            return d4 + (this.mX2 - d4) * ((d - d2) * d3);
        }

        public double getLinearY(double d) {
            double d2 = this.mTime1;
            double d3 = this.mOneOverDeltaTime;
            double d4 = this.mY1;
            return d4 + (this.mY2 - d4) * ((d - d2) * d3);
        }

        double getX() {
            return this.mEllipseCenterX + this.mEllipseA * this.mTmpSinAngle;
        }

        double getY() {
            return this.mEllipseCenterY + this.mEllipseB * this.mTmpCosAngle;
        }

        double lookup(double d) {
            if (d <= 0.0) {
                return 0.0;
            }
            if (d >= 1.0) {
                return 1.0;
            }
            double[] dArray = this.mLut;
            double d2 = dArray.length - 1;
            Double.isNaN(d2);
            d = d2 * d;
            int n = (int)d;
            d2 = (int)d;
            Double.isNaN(d2);
            return dArray[n] + (dArray[n + 1] - dArray[n]) * (d - d2);
        }

        void setPoint(double d) {
            d = this.mVertical ? this.mTime2 - d : (d -= this.mTime1);
            d = this.lookup(d * this.mOneOverDeltaTime) * 1.5707963267948966;
            this.mTmpSinAngle = Math.sin(d);
            this.mTmpCosAngle = Math.cos(d);
        }
    }
}

