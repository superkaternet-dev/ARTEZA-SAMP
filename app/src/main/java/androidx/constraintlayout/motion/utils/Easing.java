/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package androidx.constraintlayout.motion.utils;

import android.util.Log;
import java.util.Arrays;

public class Easing {
    private static final String ACCELERATE = "cubic(0.4, 0.05, 0.8, 0.7)";
    private static final String ACCELERATE_NAME = "accelerate";
    private static final String DECELERATE = "cubic(0.0, 0.0, 0.2, 0.95)";
    private static final String DECELERATE_NAME = "decelerate";
    private static final String LINEAR = "cubic(1, 1, 0, 0)";
    private static final String LINEAR_NAME = "linear";
    public static String[] NAMED_EASING;
    private static final String STANDARD = "cubic(0.4, 0.0, 0.2, 1)";
    private static final String STANDARD_NAME = "standard";
    static Easing sDefault;
    String str = "identity";

    static {
        sDefault = new Easing();
        NAMED_EASING = new String[]{STANDARD_NAME, ACCELERATE_NAME, DECELERATE_NAME, LINEAR_NAME};
    }

    /*
     * Enabled aggressive block sorting
     */
    public static Easing getInterpolator(String charSequence) {
        if (charSequence == null) {
            return null;
        }
        if (((String)charSequence).startsWith("cubic")) {
            return new CubicEasing((String)charSequence);
        }
        int n = -1;
        switch (((String)charSequence).hashCode()) {
            case 1312628413: {
                if (!((String)charSequence).equals(STANDARD_NAME)) break;
                n = 0;
                break;
            }
            case -1102672091: {
                if (!((String)charSequence).equals(LINEAR_NAME)) break;
                n = 3;
                break;
            }
            case -1263948740: {
                if (!((String)charSequence).equals(DECELERATE_NAME)) break;
                n = 2;
                break;
            }
            case -1354466595: {
                if (!((String)charSequence).equals(ACCELERATE_NAME)) break;
                n = 1;
            }
        }
        switch (n) {
            default: {
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append("transitionEasing syntax error syntax:transitionEasing=\"cubic(1.0,0.5,0.0,0.6)\" or ");
                ((StringBuilder)charSequence).append(Arrays.toString(NAMED_EASING));
                Log.e((String)"ConstraintSet", (String)((StringBuilder)charSequence).toString());
                return sDefault;
            }
            case 3: {
                return new CubicEasing(LINEAR);
            }
            case 2: {
                return new CubicEasing(DECELERATE);
            }
            case 1: {
                return new CubicEasing(ACCELERATE);
            }
            case 0: 
        }
        return new CubicEasing(STANDARD);
    }

    public double get(double d) {
        return d;
    }

    public double getDiff(double d) {
        return 1.0;
    }

    public String toString() {
        return this.str;
    }

    static class CubicEasing
    extends Easing {
        private static double d_error;
        private static double error;
        double x1;
        double x2;
        double y1;
        double y2;

        static {
            error = 0.01;
            d_error = 1.0E-4;
        }

        public CubicEasing(double d, double d2, double d3, double d4) {
            this.setup(d, d2, d3, d4);
        }

        CubicEasing(String string2) {
            this.str = string2;
            int n = string2.indexOf(40);
            int n2 = string2.indexOf(44, n);
            this.x1 = Double.parseDouble(string2.substring(n + 1, n2).trim());
            n = string2.indexOf(44, n2 + 1);
            this.y1 = Double.parseDouble(string2.substring(n2 + 1, n).trim());
            n2 = string2.indexOf(44, n + 1);
            this.x2 = Double.parseDouble(string2.substring(n + 1, n2).trim());
            this.y2 = Double.parseDouble(string2.substring(n2 + 1, string2.indexOf(41, n2 + 1)).trim());
        }

        private double getDiffX(double d) {
            double d2 = 1.0 - d;
            double d3 = this.x1;
            double d4 = this.x2;
            return d2 * 3.0 * d2 * d3 + 6.0 * d2 * d * (d4 - d3) + 3.0 * d * d * (1.0 - d4);
        }

        private double getDiffY(double d) {
            double d2 = 1.0 - d;
            double d3 = this.y1;
            double d4 = this.y2;
            return d2 * 3.0 * d2 * d3 + 6.0 * d2 * d * (d4 - d3) + 3.0 * d * d * (1.0 - d4);
        }

        private double getX(double d) {
            double d2 = 1.0 - d;
            return this.x1 * (d2 * 3.0 * d2 * d) + this.x2 * (3.0 * d2 * d * d) + d * d * d;
        }

        private double getY(double d) {
            double d2 = 1.0 - d;
            return this.y1 * (d2 * 3.0 * d2 * d) + this.y2 * (3.0 * d2 * d * d) + d * d * d;
        }

        @Override
        public double get(double d) {
            double d2;
            if (d <= 0.0) {
                return 0.0;
            }
            if (d >= 1.0) {
                return 1.0;
            }
            double d3 = 0.5;
            double d4 = 0.5;
            while (d4 > error) {
                d2 = this.getX(d3);
                d4 *= 0.5;
                if (d2 < d) {
                    d3 += d4;
                    continue;
                }
                d3 -= d4;
            }
            double d5 = this.getX(d3 - d4);
            double d6 = this.getX(d3 + d4);
            d2 = this.getY(d3 - d4);
            return (this.getY(d3 + d4) - d2) * (d - d5) / (d6 - d5) + d2;
        }

        @Override
        public double getDiff(double d) {
            double d2;
            double d3;
            double d4 = 0.5;
            double d5 = 0.5;
            while (d5 > d_error) {
                d3 = this.getX(d4);
                d2 = d5 * 0.5;
                d5 = d3 < d ? d4 + d2 : d4 - d2;
                d4 = d5;
                d5 = d2;
            }
            d = this.getX(d4 - d5);
            d3 = this.getX(d4 + d5);
            d2 = this.getY(d4 - d5);
            return (this.getY(d4 + d5) - d2) / (d3 - d);
        }

        void setup(double d, double d2, double d3, double d4) {
            this.x1 = d;
            this.y1 = d2;
            this.x2 = d3;
            this.y2 = d4;
        }
    }
}

