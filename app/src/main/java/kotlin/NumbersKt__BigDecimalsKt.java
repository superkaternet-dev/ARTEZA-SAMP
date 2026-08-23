/*
 * Decompiled with CFR 0.152.
 */
package kotlin;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv={1, 0, 3}, d1={"\u0000$\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\b\u0002\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0002\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\u0004\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0005\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0007\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\b\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\t\u001a\u00020\u0001*\u00020\nH\u0087\b\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0087\b\u001a\r\u0010\t\u001a\u00020\u0001*\u00020\rH\u0087\b\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\r2\u0006\u0010\u000b\u001a\u00020\fH\u0087\b\u001a\r\u0010\t\u001a\u00020\u0001*\u00020\u000eH\u0087\b\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\u000e2\u0006\u0010\u000b\u001a\u00020\fH\u0087\b\u001a\r\u0010\t\u001a\u00020\u0001*\u00020\u000fH\u0087\b\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\u000f2\u0006\u0010\u000b\u001a\u00020\fH\u0087\b\u001a\r\u0010\u0010\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u00a8\u0006\u0011"}, d2={"dec", "Ljava/math/BigDecimal;", "div", "other", "inc", "minus", "plus", "rem", "times", "toBigDecimal", "", "mathContext", "Ljava/math/MathContext;", "", "", "", "unaryMinus", "kotlin-stdlib"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/NumbersKt")
class NumbersKt__BigDecimalsKt {
    private static final BigDecimal dec(BigDecimal bigDecimal) {
        Intrinsics.checkNotNullParameter(bigDecimal, "$this$dec");
        bigDecimal = bigDecimal.subtract(BigDecimal.ONE);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "this.subtract(BigDecimal.ONE)");
        return bigDecimal;
    }

    private static final BigDecimal div(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
        Intrinsics.checkNotNullParameter(bigDecimal, "$this$div");
        bigDecimal = bigDecimal.divide(bigDecimal2, RoundingMode.HALF_EVEN);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "this.divide(other, RoundingMode.HALF_EVEN)");
        return bigDecimal;
    }

    private static final BigDecimal inc(BigDecimal bigDecimal) {
        Intrinsics.checkNotNullParameter(bigDecimal, "$this$inc");
        bigDecimal = bigDecimal.add(BigDecimal.ONE);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "this.add(BigDecimal.ONE)");
        return bigDecimal;
    }

    private static final BigDecimal minus(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
        Intrinsics.checkNotNullParameter(bigDecimal, "$this$minus");
        bigDecimal = bigDecimal.subtract(bigDecimal2);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "this.subtract(other)");
        return bigDecimal;
    }

    private static final BigDecimal plus(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
        Intrinsics.checkNotNullParameter(bigDecimal, "$this$plus");
        bigDecimal = bigDecimal.add(bigDecimal2);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "this.add(other)");
        return bigDecimal;
    }

    private static final BigDecimal rem(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
        Intrinsics.checkNotNullParameter(bigDecimal, "$this$rem");
        bigDecimal = bigDecimal.remainder(bigDecimal2);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "this.remainder(other)");
        return bigDecimal;
    }

    private static final BigDecimal times(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
        Intrinsics.checkNotNullParameter(bigDecimal, "$this$times");
        bigDecimal = bigDecimal.multiply(bigDecimal2);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "this.multiply(other)");
        return bigDecimal;
    }

    private static final BigDecimal toBigDecimal(double d) {
        return new BigDecimal(String.valueOf(d));
    }

    private static final BigDecimal toBigDecimal(double d, MathContext mathContext) {
        return new BigDecimal(String.valueOf(d), mathContext);
    }

    private static final BigDecimal toBigDecimal(float f) {
        return new BigDecimal(String.valueOf(f));
    }

    private static final BigDecimal toBigDecimal(float f, MathContext mathContext) {
        return new BigDecimal(String.valueOf(f), mathContext);
    }

    private static final BigDecimal toBigDecimal(int n) {
        BigDecimal bigDecimal = BigDecimal.valueOf(n);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "BigDecimal.valueOf(this.toLong())");
        return bigDecimal;
    }

    private static final BigDecimal toBigDecimal(int n, MathContext mathContext) {
        return new BigDecimal(n, mathContext);
    }

    private static final BigDecimal toBigDecimal(long l) {
        BigDecimal bigDecimal = BigDecimal.valueOf(l);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "BigDecimal.valueOf(this)");
        return bigDecimal;
    }

    private static final BigDecimal toBigDecimal(long l, MathContext mathContext) {
        return new BigDecimal(l, mathContext);
    }

    private static final BigDecimal unaryMinus(BigDecimal bigDecimal) {
        Intrinsics.checkNotNullParameter(bigDecimal, "$this$unaryMinus");
        bigDecimal = bigDecimal.negate();
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "this.negate()");
        return bigDecimal;
    }
}

