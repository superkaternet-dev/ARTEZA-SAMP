/*
 * Decompiled with CFR 0.152.
 */
package kotlin.math;

import kotlin.Metadata;
import kotlin.math.Constants;
import kotlin.math.MathKt;
import kotlin.math.MathKt__MathHKt;

@Metadata(bv={1, 0, 3}, d1={"\u0000\"\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b7\u001a\u0011\u0010\u0016\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010\u0016\u001a\u00020\t2\u0006\u0010\u0018\u001a\u00020\tH\u0087\b\u001a\u0011\u0010\u0016\u001a\u00020\f2\u0006\u0010\u0018\u001a\u00020\fH\u0087\b\u001a\u0011\u0010\u0019\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010\u0019\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0010\u0010\u001a\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0007\u001a\u0011\u0010\u001a\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010\u001b\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010\u001b\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0010\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0007\u001a\u0011\u0010\u001c\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010\u001d\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010\u001d\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0019\u0010\u001e\u001a\u00020\u00012\u0006\u0010\u001f\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0019\u0010\u001e\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0010\u0010 \u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0007\u001a\u0011\u0010 \u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010!\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010!\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010\"\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010\"\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010#\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010#\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010$\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010$\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010%\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010%\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010&\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010&\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0019\u0010'\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u00012\u0006\u0010\u001f\u001a\u00020\u0001H\u0087\b\u001a\u0019\u0010'\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010(\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010(\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010)\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010)\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0018\u0010*\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u00012\u0006\u0010+\u001a\u00020\u0001H\u0007\u001a\u0018\u0010*\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u00062\u0006\u0010+\u001a\u00020\u0006H\u0007\u001a\u0011\u0010,\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010,\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0010\u0010-\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0007\u001a\u0010\u0010-\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0007\u001a\u0019\u0010.\u001a\u00020\u00012\u0006\u0010/\u001a\u00020\u00012\u0006\u00100\u001a\u00020\u0001H\u0087\b\u001a\u0019\u0010.\u001a\u00020\u00062\u0006\u0010/\u001a\u00020\u00062\u0006\u00100\u001a\u00020\u0006H\u0087\b\u001a\u0019\u0010.\u001a\u00020\t2\u0006\u0010/\u001a\u00020\t2\u0006\u00100\u001a\u00020\tH\u0087\b\u001a\u0019\u0010.\u001a\u00020\f2\u0006\u0010/\u001a\u00020\f2\u0006\u00100\u001a\u00020\fH\u0087\b\u001a\u0019\u00101\u001a\u00020\u00012\u0006\u0010/\u001a\u00020\u00012\u0006\u00100\u001a\u00020\u0001H\u0087\b\u001a\u0019\u00101\u001a\u00020\u00062\u0006\u0010/\u001a\u00020\u00062\u0006\u00100\u001a\u00020\u0006H\u0087\b\u001a\u0019\u00101\u001a\u00020\t2\u0006\u0010/\u001a\u00020\t2\u0006\u00100\u001a\u00020\tH\u0087\b\u001a\u0019\u00101\u001a\u00020\f2\u0006\u0010/\u001a\u00020\f2\u0006\u00100\u001a\u00020\fH\u0087\b\u001a\u0011\u00102\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u00102\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u00103\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u00103\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u00104\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u00104\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u00105\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u00105\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u00106\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u00106\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u00107\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u00107\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0010\u00108\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0007\u001a\u0010\u00108\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0007\u001a\u0015\u00109\u001a\u00020\u0001*\u00020\u00012\u0006\u0010:\u001a\u00020\u0001H\u0087\b\u001a\u0015\u00109\u001a\u00020\u0006*\u00020\u00062\u0006\u0010:\u001a\u00020\u0006H\u0087\b\u001a\r\u0010;\u001a\u00020\u0001*\u00020\u0001H\u0087\b\u001a\r\u0010;\u001a\u00020\u0006*\u00020\u0006H\u0087\b\u001a\u0015\u0010<\u001a\u00020\u0001*\u00020\u00012\u0006\u0010=\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010<\u001a\u00020\u0006*\u00020\u00062\u0006\u0010=\u001a\u00020\u0006H\u0087\b\u001a\r\u0010>\u001a\u00020\u0001*\u00020\u0001H\u0087\b\u001a\r\u0010>\u001a\u00020\u0006*\u00020\u0006H\u0087\b\u001a\u0015\u0010?\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010?\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0018\u001a\u00020\tH\u0087\b\u001a\u0015\u0010?\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0015\u0010?\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u0018\u001a\u00020\tH\u0087\b\u001a\f\u0010@\u001a\u00020\t*\u00020\u0001H\u0007\u001a\f\u0010@\u001a\u00020\t*\u00020\u0006H\u0007\u001a\f\u0010A\u001a\u00020\f*\u00020\u0001H\u0007\u001a\f\u0010A\u001a\u00020\f*\u00020\u0006H\u0007\u001a\u0015\u0010B\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010B\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u000f\u001a\u00020\tH\u0087\b\u001a\u0015\u0010B\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u0006H\u0087\b\u001a\u0015\u0010B\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u000f\u001a\u00020\tH\u0087\b\"\u001f\u0010\u0000\u001a\u00020\u0001*\u00020\u00018\u00c6\u0002X\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u0002\u0010\u0003\u001a\u0004\b\u0004\u0010\u0005\"\u001f\u0010\u0000\u001a\u00020\u0006*\u00020\u00068\u00c6\u0002X\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u0002\u0010\u0007\u001a\u0004\b\u0004\u0010\b\"\u001f\u0010\u0000\u001a\u00020\t*\u00020\t8\u00c6\u0002X\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u0002\u0010\n\u001a\u0004\b\u0004\u0010\u000b\"\u001f\u0010\u0000\u001a\u00020\f*\u00020\f8\u00c6\u0002X\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u0002\u0010\r\u001a\u0004\b\u0004\u0010\u000e\"\u001f\u0010\u000f\u001a\u00020\u0001*\u00020\u00018\u00c6\u0002X\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u0010\u0010\u0003\u001a\u0004\b\u0011\u0010\u0005\"\u001f\u0010\u000f\u001a\u00020\u0006*\u00020\u00068\u00c6\u0002X\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u0010\u0010\u0007\u001a\u0004\b\u0011\u0010\b\"\u001e\u0010\u000f\u001a\u00020\t*\u00020\t8FX\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u0010\u0010\n\u001a\u0004\b\u0011\u0010\u000b\"\u001e\u0010\u000f\u001a\u00020\t*\u00020\f8FX\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u0010\u0010\r\u001a\u0004\b\u0011\u0010\u0012\"\u001f\u0010\u0013\u001a\u00020\u0001*\u00020\u00018\u00c6\u0002X\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u0014\u0010\u0003\u001a\u0004\b\u0015\u0010\u0005\"\u001f\u0010\u0013\u001a\u00020\u0006*\u00020\u00068\u00c6\u0002X\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u0014\u0010\u0007\u001a\u0004\b\u0015\u0010\b\u00a8\u0006C"}, d2={"absoluteValue", "", "getAbsoluteValue$annotations", "(D)V", "getAbsoluteValue", "(D)D", "", "(F)V", "(F)F", "", "(I)V", "(I)I", "", "(J)V", "(J)J", "sign", "getSign$annotations", "getSign", "(J)I", "ulp", "getUlp$annotations", "getUlp", "abs", "x", "n", "acos", "acosh", "asin", "asinh", "atan", "atan2", "y", "atanh", "ceil", "cos", "cosh", "exp", "expm1", "floor", "hypot", "ln", "ln1p", "log", "base", "log10", "log2", "max", "a", "b", "min", "round", "sin", "sinh", "sqrt", "tan", "tanh", "truncate", "IEEErem", "divisor", "nextDown", "nextTowards", "to", "nextUp", "pow", "roundToInt", "roundToLong", "withSign", "kotlin-stdlib"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/math/MathKt")
class MathKt__MathJVMKt
extends MathKt__MathHKt {
    private static final double IEEErem(double d, double d2) {
        return Math.IEEEremainder(d, d2);
    }

    private static final float IEEErem(float f, float f2) {
        return (float)Math.IEEEremainder(f, f2);
    }

    private static final double abs(double d) {
        return Math.abs(d);
    }

    private static final float abs(float f) {
        return Math.abs(f);
    }

    private static final int abs(int n) {
        return Math.abs(n);
    }

    private static final long abs(long l) {
        return Math.abs(l);
    }

    private static final double acos(double d) {
        return Math.acos(d);
    }

    private static final float acos(float f) {
        return (float)Math.acos(f);
    }

    public static final double acosh(double d) {
        double d2 = 1.0;
        if (d < d2) {
            d = Double.NaN;
        } else if (d > Constants.upper_taylor_2_bound) {
            d = Math.log(d) + Constants.LN2;
        } else {
            Double.isNaN(d2);
            if (d - d2 >= Constants.taylor_n_bound) {
                Double.isNaN(d2);
                d = Math.log(Math.sqrt(d * d - d2) + d);
            } else {
                double d3;
                Double.isNaN(d2);
                d2 = d = (d3 = Math.sqrt(d - d2));
                if (d3 >= Constants.taylor_2_bound) {
                    d2 = 12;
                    Double.isNaN(d2);
                    d2 = d - d3 * d3 * d3 / d2;
                }
                d = Math.sqrt(2.0) * d2;
            }
        }
        return d;
    }

    private static final float acosh(float f) {
        return (float)MathKt.acosh((double)f);
    }

    private static final double asin(double d) {
        return Math.asin(d);
    }

    private static final float asin(float f) {
        return (float)Math.asin(f);
    }

    public static final double asinh(double d) {
        if (d >= Constants.taylor_n_bound) {
            if (d > Constants.upper_taylor_n_bound) {
                if (d > Constants.upper_taylor_2_bound) {
                    d = Math.log(d) + Constants.LN2;
                } else {
                    double d2 = 2;
                    Double.isNaN(d2);
                    double d3 = 1.0;
                    Double.isNaN(d2);
                    Double.isNaN(d3);
                    d = Math.log(d * d2 + d3 / (d2 * d));
                }
            } else {
                double d4 = 1.0;
                Double.isNaN(d4);
                d = Math.log(Math.sqrt(d * d + d4) + d);
            }
        } else if (d <= -Constants.taylor_n_bound) {
            d = -MathKt.asinh(-d);
        } else {
            double d5;
            double d6 = d5 = d;
            if (Math.abs(d) >= Constants.taylor_2_bound) {
                d6 = 6;
                Double.isNaN(d6);
                d6 = d5 - d * d * d / d6;
            }
            d = d6;
        }
        return d;
    }

    private static final float asinh(float f) {
        return (float)MathKt.asinh((double)f);
    }

    private static final double atan(double d) {
        return Math.atan(d);
    }

    private static final float atan(float f) {
        return (float)Math.atan(f);
    }

    private static final double atan2(double d, double d2) {
        return Math.atan2(d, d2);
    }

    private static final float atan2(float f, float f2) {
        return (float)Math.atan2(f, f2);
    }

    public static final double atanh(double d) {
        if (Math.abs(d) < Constants.taylor_n_bound) {
            double d2;
            double d3 = d2 = d;
            if (Math.abs(d) > Constants.taylor_2_bound) {
                d3 = 3;
                Double.isNaN(d3);
                d3 = d2 + d * d * d / d3;
            }
            return d3;
        }
        double d4 = 1.0;
        Double.isNaN(d4);
        Double.isNaN(d4);
        d = Math.log((d4 + d) / (d4 - d));
        d4 = 2;
        Double.isNaN(d4);
        return d / d4;
    }

    private static final float atanh(float f) {
        return (float)MathKt.atanh((double)f);
    }

    private static final double ceil(double d) {
        return Math.ceil(d);
    }

    private static final float ceil(float f) {
        return (float)Math.ceil(f);
    }

    private static final double cos(double d) {
        return Math.cos(d);
    }

    private static final float cos(float f) {
        return (float)Math.cos(f);
    }

    private static final double cosh(double d) {
        return Math.cosh(d);
    }

    private static final float cosh(float f) {
        return (float)Math.cosh(f);
    }

    private static final double exp(double d) {
        return Math.exp(d);
    }

    private static final float exp(float f) {
        return (float)Math.exp(f);
    }

    private static final double expm1(double d) {
        return Math.expm1(d);
    }

    private static final float expm1(float f) {
        return (float)Math.expm1(f);
    }

    private static final double floor(double d) {
        return Math.floor(d);
    }

    private static final float floor(float f) {
        return (float)Math.floor(f);
    }

    private static final double getAbsoluteValue(double d) {
        return Math.abs(d);
    }

    private static final float getAbsoluteValue(float f) {
        return Math.abs(f);
    }

    private static final int getAbsoluteValue(int n) {
        return Math.abs(n);
    }

    private static final long getAbsoluteValue(long l) {
        return Math.abs(l);
    }

    public static /* synthetic */ void getAbsoluteValue$annotations(double d) {
    }

    public static /* synthetic */ void getAbsoluteValue$annotations(float f) {
    }

    public static /* synthetic */ void getAbsoluteValue$annotations(int n) {
    }

    public static /* synthetic */ void getAbsoluteValue$annotations(long l) {
    }

    private static final double getSign(double d) {
        return Math.signum(d);
    }

    private static final float getSign(float f) {
        return Math.signum(f);
    }

    public static final int getSign(int n) {
        n = n < 0 ? -1 : (n > 0 ? 1 : 0);
        return n;
    }

    public static final int getSign(long l) {
        int n = l < 0L ? -1 : (l > 0L ? 1 : 0);
        return n;
    }

    public static /* synthetic */ void getSign$annotations(double d) {
    }

    public static /* synthetic */ void getSign$annotations(float f) {
    }

    public static /* synthetic */ void getSign$annotations(int n) {
    }

    public static /* synthetic */ void getSign$annotations(long l) {
    }

    private static final double getUlp(double d) {
        return Math.ulp(d);
    }

    private static final float getUlp(float f) {
        return Math.ulp(f);
    }

    public static /* synthetic */ void getUlp$annotations(double d) {
    }

    public static /* synthetic */ void getUlp$annotations(float f) {
    }

    private static final double hypot(double d, double d2) {
        return Math.hypot(d, d2);
    }

    private static final float hypot(float f, float f2) {
        return (float)Math.hypot(f, f2);
    }

    private static final double ln(double d) {
        return Math.log(d);
    }

    private static final float ln(float f) {
        return (float)Math.log(f);
    }

    private static final double ln1p(double d) {
        return Math.log1p(d);
    }

    private static final float ln1p(float f) {
        return (float)Math.log1p(f);
    }

    public static final double log(double d, double d2) {
        if (!(d2 <= 0.0) && d2 != 1.0) {
            return Math.log(d) / Math.log(d2);
        }
        return Double.NaN;
    }

    public static final float log(float f, float f2) {
        if (!(f2 <= 0.0f) && f2 != 1.0f) {
            return (float)(Math.log(f) / Math.log(f2));
        }
        return Float.NaN;
    }

    private static final double log10(double d) {
        return Math.log10(d);
    }

    private static final float log10(float f) {
        return (float)Math.log10(f);
    }

    public static final double log2(double d) {
        return Math.log(d) / Constants.LN2;
    }

    public static final float log2(float f) {
        return (float)(Math.log(f) / Constants.LN2);
    }

    private static final double max(double d, double d2) {
        return Math.max(d, d2);
    }

    private static final float max(float f, float f2) {
        return Math.max(f, f2);
    }

    private static final int max(int n, int n2) {
        return Math.max(n, n2);
    }

    private static final long max(long l, long l2) {
        return Math.max(l, l2);
    }

    private static final double min(double d, double d2) {
        return Math.min(d, d2);
    }

    private static final float min(float f, float f2) {
        return Math.min(f, f2);
    }

    private static final int min(int n, int n2) {
        return Math.min(n, n2);
    }

    private static final long min(long l, long l2) {
        return Math.min(l, l2);
    }

    private static final double nextDown(double d) {
        return Math.nextAfter(d, Double.NEGATIVE_INFINITY);
    }

    private static final float nextDown(float f) {
        return Math.nextAfter(f, Double.NEGATIVE_INFINITY);
    }

    private static final double nextTowards(double d, double d2) {
        return Math.nextAfter(d, d2);
    }

    private static final float nextTowards(float f, float f2) {
        return Math.nextAfter(f, (double)f2);
    }

    private static final double nextUp(double d) {
        return Math.nextUp(d);
    }

    private static final float nextUp(float f) {
        return Math.nextUp(f);
    }

    private static final double pow(double d, double d2) {
        return Math.pow(d, d2);
    }

    private static final double pow(double d, int n) {
        return Math.pow(d, n);
    }

    private static final float pow(float f, float f2) {
        return (float)Math.pow(f, f2);
    }

    private static final float pow(float f, int n) {
        return (float)Math.pow(f, n);
    }

    private static final double round(double d) {
        return Math.rint(d);
    }

    private static final float round(float f) {
        return (float)Math.rint(f);
    }

    public static final int roundToInt(double d) {
        if (!Double.isNaN(d)) {
            int n = Integer.MAX_VALUE;
            if (!(d > (double)Integer.MAX_VALUE)) {
                n = d < (double)Integer.MIN_VALUE ? Integer.MIN_VALUE : (int)Math.round(d);
            }
            return n;
        }
        throw (Throwable)new IllegalArgumentException("Cannot round NaN value.");
    }

    public static final int roundToInt(float f) {
        if (!Float.isNaN(f)) {
            return Math.round(f);
        }
        throw (Throwable)new IllegalArgumentException("Cannot round NaN value.");
    }

    public static final long roundToLong(double d) {
        if (!Double.isNaN(d)) {
            return Math.round(d);
        }
        throw (Throwable)new IllegalArgumentException("Cannot round NaN value.");
    }

    public static final long roundToLong(float f) {
        return MathKt.roundToLong((double)f);
    }

    private static final double sign(double d) {
        return Math.signum(d);
    }

    private static final float sign(float f) {
        return Math.signum(f);
    }

    private static final double sin(double d) {
        return Math.sin(d);
    }

    private static final float sin(float f) {
        return (float)Math.sin(f);
    }

    private static final double sinh(double d) {
        return Math.sinh(d);
    }

    private static final float sinh(float f) {
        return (float)Math.sinh(f);
    }

    private static final double sqrt(double d) {
        return Math.sqrt(d);
    }

    private static final float sqrt(float f) {
        return (float)Math.sqrt(f);
    }

    private static final double tan(double d) {
        return Math.tan(d);
    }

    private static final float tan(float f) {
        return (float)Math.tan(f);
    }

    private static final double tanh(double d) {
        return Math.tanh(d);
    }

    private static final float tanh(float f) {
        return (float)Math.tanh(f);
    }

    public static final double truncate(double d) {
        block0: {
            if (Double.isNaN(d) || Double.isInfinite(d)) break block0;
            d = d > 0.0 ? Math.floor(d) : Math.ceil(d);
        }
        return d;
    }

    public static final float truncate(float f) {
        block0: {
            if (Float.isNaN(f) || Float.isInfinite(f)) break block0;
            f = f > 0.0f ? (float)Math.floor(f) : (float)Math.ceil(f);
        }
        return f;
    }

    private static final double withSign(double d, double d2) {
        return Math.copySign(d, d2);
    }

    private static final double withSign(double d, int n) {
        return Math.copySign(d, (double)n);
    }

    private static final float withSign(float f, float f2) {
        return Math.copySign(f, f2);
    }

    private static final float withSign(float f, int n) {
        return Math.copySign(f, n);
    }
}

