/*
 * Decompiled with CFR 0.152.
 */
package kotlin.time;

import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import kotlin.time.DurationKt;
import kotlin.time.DurationUnitKt;
import kotlin.time.FormatToDecimalsKt;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(bv={1, 0, 3}, d1={"\u0000^\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b&\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0012\b\u0087@\u0018\u0000 v2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001vB\u0014\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u001b\u0010%\u001a\u00020\t2\u0006\u0010&\u001a\u00020\u0000H\u0096\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b'\u0010(J\u001e\u0010)\u001a\u00020\u00002\u0006\u0010*\u001a\u00020\u0003H\u0086\u0002\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b+\u0010,J\u001e\u0010)\u001a\u00020\u00002\u0006\u0010*\u001a\u00020\tH\u0086\u0002\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b+\u0010-J\u001b\u0010)\u001a\u00020\u00032\u0006\u0010&\u001a\u00020\u0000H\u0086\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b.\u0010,J\u001a\u0010/\u001a\u0002002\b\u0010&\u001a\u0004\u0018\u000101H\u00d6\u0003\u00a2\u0006\u0004\b2\u00103J\u0010\u00104\u001a\u00020\tH\u00d6\u0001\u00a2\u0006\u0004\b5\u0010\rJ\r\u00106\u001a\u000200\u00a2\u0006\u0004\b7\u00108J\r\u00109\u001a\u000200\u00a2\u0006\u0004\b:\u00108J\r\u0010;\u001a\u000200\u00a2\u0006\u0004\b<\u00108J\r\u0010=\u001a\u000200\u00a2\u0006\u0004\b>\u00108J\u001b\u0010?\u001a\u00020\u00002\u0006\u0010&\u001a\u00020\u0000H\u0086\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b@\u0010,J\u001b\u0010A\u001a\u00020\u00002\u0006\u0010&\u001a\u00020\u0000H\u0086\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\bB\u0010,J\u0017\u0010C\u001a\u00020\t2\u0006\u0010\u0002\u001a\u00020\u0003H\u0002\u00a2\u0006\u0004\bD\u0010(J\u001e\u0010E\u001a\u00020\u00002\u0006\u0010*\u001a\u00020\u0003H\u0086\u0002\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bF\u0010,J\u001e\u0010E\u001a\u00020\u00002\u0006\u0010*\u001a\u00020\tH\u0086\u0002\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bF\u0010-J\u009d\u0001\u0010G\u001a\u0002HH\"\u0004\b\u0000\u0010H2u\u0010I\u001aq\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\bK\u0012\b\bL\u0012\u0004\b\b(M\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\bK\u0012\b\bL\u0012\u0004\b\b(N\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\bK\u0012\b\bL\u0012\u0004\b\b(O\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\bK\u0012\b\bL\u0012\u0004\b\b(P\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\bK\u0012\b\bL\u0012\u0004\b\b(Q\u0012\u0004\u0012\u0002HH0JH\u0086\b\u00f8\u0001\u0002\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u00a2\u0006\u0004\bR\u0010SJ\u0088\u0001\u0010G\u001a\u0002HH\"\u0004\b\u0000\u0010H2`\u0010I\u001a\\\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\bK\u0012\b\bL\u0012\u0004\b\b(N\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\bK\u0012\b\bL\u0012\u0004\b\b(O\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\bK\u0012\b\bL\u0012\u0004\b\b(P\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\bK\u0012\b\bL\u0012\u0004\b\b(Q\u0012\u0004\u0012\u0002HH0TH\u0086\b\u00f8\u0001\u0002\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u00a2\u0006\u0004\bR\u0010UJs\u0010G\u001a\u0002HH\"\u0004\b\u0000\u0010H2K\u0010I\u001aG\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\bK\u0012\b\bL\u0012\u0004\b\b(O\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\bK\u0012\b\bL\u0012\u0004\b\b(P\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\bK\u0012\b\bL\u0012\u0004\b\b(Q\u0012\u0004\u0012\u0002HH0VH\u0086\b\u00f8\u0001\u0002\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u00a2\u0006\u0004\bR\u0010WJ^\u0010G\u001a\u0002HH\"\u0004\b\u0000\u0010H26\u0010I\u001a2\u0012\u0013\u0012\u00110Y\u00a2\u0006\f\bK\u0012\b\bL\u0012\u0004\b\b(P\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\bK\u0012\b\bL\u0012\u0004\b\b(Q\u0012\u0004\u0012\u0002HH0XH\u0086\b\u00f8\u0001\u0002\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u00a2\u0006\u0004\bR\u0010ZJ\u0019\u0010[\u001a\u00020\u00032\n\u0010\\\u001a\u00060]j\u0002`^\u00a2\u0006\u0004\b_\u0010`J\u0019\u0010a\u001a\u00020\t2\n\u0010\\\u001a\u00060]j\u0002`^\u00a2\u0006\u0004\bb\u0010cJ\r\u0010d\u001a\u00020e\u00a2\u0006\u0004\bf\u0010gJ\u0019\u0010h\u001a\u00020Y2\n\u0010\\\u001a\u00060]j\u0002`^\u00a2\u0006\u0004\bi\u0010jJ\r\u0010k\u001a\u00020Y\u00a2\u0006\u0004\bl\u0010mJ\r\u0010n\u001a\u00020Y\u00a2\u0006\u0004\bo\u0010mJ\u000f\u0010p\u001a\u00020eH\u0016\u00a2\u0006\u0004\bq\u0010gJ#\u0010p\u001a\u00020e2\n\u0010\\\u001a\u00060]j\u0002`^2\b\b\u0002\u0010r\u001a\u00020\t\u00a2\u0006\u0004\bq\u0010sJ\u0016\u0010t\u001a\u00020\u0000H\u0086\u0002\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bu\u0010\u0005R\u0017\u0010\u0006\u001a\u00020\u00008F\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\u0005R\u001a\u0010\b\u001a\u00020\t8@X\u0081\u0004\u00a2\u0006\f\u0012\u0004\b\n\u0010\u000b\u001a\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0005R\u0011\u0010\u0010\u001a\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0011\u0010\u0005R\u0011\u0010\u0012\u001a\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0013\u0010\u0005R\u0011\u0010\u0014\u001a\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0015\u0010\u0005R\u0011\u0010\u0016\u001a\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0017\u0010\u0005R\u0011\u0010\u0018\u001a\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0019\u0010\u0005R\u0011\u0010\u001a\u001a\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u001b\u0010\u0005R\u001a\u0010\u001c\u001a\u00020\t8@X\u0081\u0004\u00a2\u0006\f\u0012\u0004\b\u001d\u0010\u000b\u001a\u0004\b\u001e\u0010\rR\u001a\u0010\u001f\u001a\u00020\t8@X\u0081\u0004\u00a2\u0006\f\u0012\u0004\b \u0010\u000b\u001a\u0004\b!\u0010\rR\u001a\u0010\"\u001a\u00020\t8@X\u0081\u0004\u00a2\u0006\f\u0012\u0004\b#\u0010\u000b\u001a\u0004\b$\u0010\rR\u000e\u0010\u0002\u001a\u00020\u0003X\u0080\u0004\u00a2\u0006\u0002\n\u0000\u00f8\u0001\u0000\u0082\u0002\u000f\n\u0002\b\u0019\n\u0002\b!\n\u0005\b\u009920\u0001\u00a8\u0006w"}, d2={"Lkotlin/time/Duration;", "", "value", "", "constructor-impl", "(D)D", "absoluteValue", "getAbsoluteValue-UwyO8pc", "hoursComponent", "", "getHoursComponent$annotations", "()V", "getHoursComponent-impl", "(D)I", "inDays", "getInDays-impl", "inHours", "getInHours-impl", "inMicroseconds", "getInMicroseconds-impl", "inMilliseconds", "getInMilliseconds-impl", "inMinutes", "getInMinutes-impl", "inNanoseconds", "getInNanoseconds-impl", "inSeconds", "getInSeconds-impl", "minutesComponent", "getMinutesComponent$annotations", "getMinutesComponent-impl", "nanosecondsComponent", "getNanosecondsComponent$annotations", "getNanosecondsComponent-impl", "secondsComponent", "getSecondsComponent$annotations", "getSecondsComponent-impl", "compareTo", "other", "compareTo-LRDsOJo", "(DD)I", "div", "scale", "div-UwyO8pc", "(DD)D", "(DI)D", "div-LRDsOJo", "equals", "", "", "equals-impl", "(DLjava/lang/Object;)Z", "hashCode", "hashCode-impl", "isFinite", "isFinite-impl", "(D)Z", "isInfinite", "isInfinite-impl", "isNegative", "isNegative-impl", "isPositive", "isPositive-impl", "minus", "minus-LRDsOJo", "plus", "plus-LRDsOJo", "precision", "precision-impl", "times", "times-UwyO8pc", "toComponents", "T", "action", "Lkotlin/Function5;", "Lkotlin/ParameterName;", "name", "days", "hours", "minutes", "seconds", "nanoseconds", "toComponents-impl", "(DLkotlin/jvm/functions/Function5;)Ljava/lang/Object;", "Lkotlin/Function4;", "(DLkotlin/jvm/functions/Function4;)Ljava/lang/Object;", "Lkotlin/Function3;", "(DLkotlin/jvm/functions/Function3;)Ljava/lang/Object;", "Lkotlin/Function2;", "", "(DLkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "toDouble", "unit", "Ljava/util/concurrent/TimeUnit;", "Lkotlin/time/DurationUnit;", "toDouble-impl", "(DLjava/util/concurrent/TimeUnit;)D", "toInt", "toInt-impl", "(DLjava/util/concurrent/TimeUnit;)I", "toIsoString", "", "toIsoString-impl", "(D)Ljava/lang/String;", "toLong", "toLong-impl", "(DLjava/util/concurrent/TimeUnit;)J", "toLongMilliseconds", "toLongMilliseconds-impl", "(D)J", "toLongNanoseconds", "toLongNanoseconds-impl", "toString", "toString-impl", "decimals", "(DLjava/util/concurrent/TimeUnit;I)Ljava/lang/String;", "unaryMinus", "unaryMinus-UwyO8pc", "Companion", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class Duration
implements Comparable<Duration> {
    public static final Companion Companion = new Companion(null);
    private static final double INFINITE;
    private static final double ZERO;
    private final double value;

    static {
        ZERO = Duration.constructor-impl(0.0);
        INFINITE = Duration.constructor-impl(Double.POSITIVE_INFINITY);
    }

    private /* synthetic */ Duration(double d) {
        this.value = d;
    }

    public static final /* synthetic */ Duration box-impl(double d) {
        return new Duration(d);
    }

    public static int compareTo-LRDsOJo(double d, double d2) {
        return Double.compare(d, d2);
    }

    public static double constructor-impl(double d) {
        return d;
    }

    public static final double div-LRDsOJo(double d, double d2) {
        return d / d2;
    }

    public static final double div-UwyO8pc(double d, double d2) {
        return Duration.constructor-impl(d / d2);
    }

    public static final double div-UwyO8pc(double d, int n) {
        double d2 = n;
        Double.isNaN(d2);
        return Duration.constructor-impl(d / d2);
    }

    public static boolean equals-impl(double d, Object object) {
        return object instanceof Duration && Double.compare(d, ((Duration)object).unbox-impl()) == 0;
    }

    public static final boolean equals-impl0(double d, double d2) {
        boolean bl = Double.compare(d, d2) == 0;
        return bl;
    }

    public static final double getAbsoluteValue-UwyO8pc(double d) {
        block0: {
            if (!Duration.isNegative-impl(d)) break block0;
            d = Duration.unaryMinus-UwyO8pc(d);
        }
        return d;
    }

    public static /* synthetic */ void getHoursComponent$annotations() {
    }

    public static final int getHoursComponent-impl(double d) {
        double d2 = Duration.getInHours-impl(d);
        d = 24;
        Double.isNaN(d);
        return (int)(d2 % d);
    }

    public static final double getInDays-impl(double d) {
        return Duration.toDouble-impl(d, TimeUnit.DAYS);
    }

    public static final double getInHours-impl(double d) {
        return Duration.toDouble-impl(d, TimeUnit.HOURS);
    }

    public static final double getInMicroseconds-impl(double d) {
        return Duration.toDouble-impl(d, TimeUnit.MICROSECONDS);
    }

    public static final double getInMilliseconds-impl(double d) {
        return Duration.toDouble-impl(d, TimeUnit.MILLISECONDS);
    }

    public static final double getInMinutes-impl(double d) {
        return Duration.toDouble-impl(d, TimeUnit.MINUTES);
    }

    public static final double getInNanoseconds-impl(double d) {
        return Duration.toDouble-impl(d, TimeUnit.NANOSECONDS);
    }

    public static final double getInSeconds-impl(double d) {
        return Duration.toDouble-impl(d, TimeUnit.SECONDS);
    }

    public static /* synthetic */ void getMinutesComponent$annotations() {
    }

    public static final int getMinutesComponent-impl(double d) {
        double d2 = Duration.getInMinutes-impl(d);
        d = 60;
        Double.isNaN(d);
        return (int)(d2 % d);
    }

    public static /* synthetic */ void getNanosecondsComponent$annotations() {
    }

    public static final int getNanosecondsComponent-impl(double d) {
        return (int)(Duration.getInNanoseconds-impl(d) % 1.0E9);
    }

    public static /* synthetic */ void getSecondsComponent$annotations() {
    }

    public static final int getSecondsComponent-impl(double d) {
        d = Duration.getInSeconds-impl(d);
        double d2 = 60;
        Double.isNaN(d2);
        return (int)(d % d2);
    }

    public static int hashCode-impl(double d) {
        long l = Double.doubleToLongBits(d);
        return (int)(l ^ l >>> 32);
    }

    public static final boolean isFinite-impl(double d) {
        boolean bl = !Double.isInfinite(d) && !Double.isNaN(d);
        return bl;
    }

    public static final boolean isInfinite-impl(double d) {
        return Double.isInfinite(d);
    }

    public static final boolean isNegative-impl(double d) {
        boolean bl = false;
        if (d < 0.0) {
            bl = true;
        }
        return bl;
    }

    public static final boolean isPositive-impl(double d) {
        boolean bl = false;
        if (d > 0.0) {
            bl = true;
        }
        return bl;
    }

    public static final double minus-LRDsOJo(double d, double d2) {
        return Duration.constructor-impl(d - d2);
    }

    public static final double plus-LRDsOJo(double d, double d2) {
        return Duration.constructor-impl(d + d2);
    }

    private static final int precision-impl(double d, double d2) {
        int n = 1;
        if (d2 < 1.0) {
            n = 3;
        } else if (d2 < (double)10) {
            n = 2;
        } else if (!(d2 < (double)100)) {
            n = 0;
        }
        return n;
    }

    public static final double times-UwyO8pc(double d, double d2) {
        return Duration.constructor-impl(d * d2);
    }

    public static final double times-UwyO8pc(double d, int n) {
        double d2 = n;
        Double.isNaN(d2);
        return Duration.constructor-impl(d2 * d);
    }

    public static final <T> T toComponents-impl(double d, Function2<? super Long, ? super Integer, ? extends T> function2) {
        Intrinsics.checkNotNullParameter(function2, "action");
        return function2.invoke((long)Duration.getInSeconds-impl(d), Duration.getNanosecondsComponent-impl(d));
    }

    public static final <T> T toComponents-impl(double d, Function3<? super Integer, ? super Integer, ? super Integer, ? extends T> function3) {
        Intrinsics.checkNotNullParameter(function3, "action");
        return function3.invoke((int)Duration.getInMinutes-impl(d), Duration.getSecondsComponent-impl(d), Duration.getNanosecondsComponent-impl(d));
    }

    public static final <T> T toComponents-impl(double d, Function4<? super Integer, ? super Integer, ? super Integer, ? super Integer, ? extends T> function4) {
        Intrinsics.checkNotNullParameter(function4, "action");
        return function4.invoke((int)Duration.getInHours-impl(d), Duration.getMinutesComponent-impl(d), Duration.getSecondsComponent-impl(d), Duration.getNanosecondsComponent-impl(d));
    }

    public static final <T> T toComponents-impl(double d, Function5<? super Integer, ? super Integer, ? super Integer, ? super Integer, ? super Integer, ? extends T> function5) {
        Intrinsics.checkNotNullParameter(function5, "action");
        return function5.invoke((int)Duration.getInDays-impl(d), Duration.getHoursComponent-impl(d), Duration.getMinutesComponent-impl(d), Duration.getSecondsComponent-impl(d), Duration.getNanosecondsComponent-impl(d));
    }

    public static final double toDouble-impl(double d, TimeUnit timeUnit) {
        Intrinsics.checkNotNullParameter((Object)timeUnit, "unit");
        return DurationUnitKt.convertDurationUnit(d, DurationKt.access$getStorageUnit$p(), timeUnit);
    }

    public static final int toInt-impl(double d, TimeUnit timeUnit) {
        Intrinsics.checkNotNullParameter((Object)timeUnit, "unit");
        return (int)Duration.toDouble-impl(d, timeUnit);
    }

    public static final String toIsoString-impl(double d) {
        String string2;
        StringBuilder stringBuilder = new StringBuilder();
        if (Duration.isNegative-impl(d)) {
            stringBuilder.append('-');
        }
        stringBuilder.append("PT");
        d = Duration.getAbsoluteValue-UwyO8pc(d);
        int n = (int)Duration.getInHours-impl(d);
        int n2 = Duration.getMinutesComponent-impl(d);
        int n3 = Duration.getSecondsComponent-impl(d);
        int n4 = Duration.getNanosecondsComponent-impl(d);
        boolean bl = true;
        boolean bl2 = n != 0;
        boolean bl3 = n3 != 0 || n4 != 0;
        boolean bl4 = bl;
        if (n2 == 0) {
            bl4 = bl3 && bl2 ? bl : false;
        }
        if (bl2) {
            stringBuilder.append(n);
            stringBuilder.append('H');
        }
        if (bl4) {
            stringBuilder.append(n2);
            stringBuilder.append('M');
        }
        if (bl3 || !bl2 && !bl4) {
            stringBuilder.append(n3);
            if (n4 != 0) {
                stringBuilder.append('.');
                string2 = StringsKt.padStart(String.valueOf(n4), 9, '0');
                if (n4 % 1000000 == 0) {
                    stringBuilder.append(string2, 0, 3);
                    Intrinsics.checkNotNullExpressionValue(stringBuilder, "this.append(value, startIndex, endIndex)");
                } else if (n4 % 1000 == 0) {
                    stringBuilder.append(string2, 0, 6);
                    Intrinsics.checkNotNullExpressionValue(stringBuilder, "this.append(value, startIndex, endIndex)");
                } else {
                    stringBuilder.append(string2);
                }
            }
            stringBuilder.append('S');
        }
        string2 = stringBuilder.toString();
        Intrinsics.checkNotNullExpressionValue(string2, "StringBuilder().apply(builderAction).toString()");
        return string2;
    }

    public static final long toLong-impl(double d, TimeUnit timeUnit) {
        Intrinsics.checkNotNullParameter((Object)timeUnit, "unit");
        return (long)Duration.toDouble-impl(d, timeUnit);
    }

    public static final long toLongMilliseconds-impl(double d) {
        return Duration.toLong-impl(d, TimeUnit.MILLISECONDS);
    }

    public static final long toLongNanoseconds-impl(double d) {
        return Duration.toLong-impl(d, TimeUnit.NANOSECONDS);
    }

    public static String toString-impl(double d) {
        Object object;
        if (Duration.isInfinite-impl(d)) {
            object = String.valueOf(d);
        } else if (d == 0.0) {
            object = "0s";
        } else {
            double d2 = Duration.getInNanoseconds-impl(Duration.getAbsoluteValue-UwyO8pc(d));
            boolean bl = false;
            int n = 0;
            if (d2 < 1.0E-6) {
                object = TimeUnit.SECONDS;
                bl = true;
            } else if (d2 < 1.0) {
                object = TimeUnit.NANOSECONDS;
                n = 7;
            } else if (d2 < 1000.0) {
                object = TimeUnit.NANOSECONDS;
            } else if (d2 < 1000000.0) {
                object = TimeUnit.MICROSECONDS;
            } else if (d2 < 1.0E9) {
                object = TimeUnit.MILLISECONDS;
            } else if (d2 < 1.0E12) {
                object = TimeUnit.SECONDS;
            } else if (d2 < 6.0E13) {
                object = TimeUnit.MINUTES;
            } else if (d2 < 3.6E15) {
                object = TimeUnit.HOURS;
            } else if (d2 < 8.64E20) {
                object = TimeUnit.DAYS;
            } else {
                object = TimeUnit.DAYS;
                bl = true;
            }
            d2 = Duration.toDouble-impl(d, (TimeUnit)((Object)object));
            StringBuilder stringBuilder = new StringBuilder();
            String string2 = bl ? FormatToDecimalsKt.formatScientific(d2) : (n > 0 ? FormatToDecimalsKt.formatUpToDecimals(d2, n) : FormatToDecimalsKt.formatToExactDecimals(d2, Duration.precision-impl(d, Math.abs(d2))));
            stringBuilder.append(string2);
            stringBuilder.append(DurationUnitKt.shortName((TimeUnit)((Object)object)));
            object = stringBuilder.toString();
        }
        return object;
    }

    public static final String toString-impl(double d, TimeUnit object, int n) {
        Intrinsics.checkNotNullParameter(object, "unit");
        boolean bl = n >= 0;
        if (bl) {
            if (Duration.isInfinite-impl(d)) {
                return String.valueOf(d);
            }
            d = Duration.toDouble-impl(d, (TimeUnit)((Object)object));
            StringBuilder stringBuilder = new StringBuilder();
            String string2 = Math.abs(d) < 1.0E14 ? FormatToDecimalsKt.formatToExactDecimals(d, RangesKt.coerceAtMost(n, 12)) : FormatToDecimalsKt.formatScientific(d);
            stringBuilder.append(string2);
            stringBuilder.append(DurationUnitKt.shortName((TimeUnit)((Object)object)));
            return stringBuilder.toString();
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("decimals must be not negative, but was ");
        ((StringBuilder)object).append(n);
        throw (Throwable)new IllegalArgumentException(((StringBuilder)object).toString().toString());
    }

    public static /* synthetic */ String toString-impl$default(double d, TimeUnit timeUnit, int n, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = 0;
        }
        return Duration.toString-impl(d, timeUnit, n);
    }

    public static final double unaryMinus-UwyO8pc(double d) {
        return Duration.constructor-impl(-d);
    }

    public int compareTo-LRDsOJo(double d) {
        return Duration.compareTo-LRDsOJo(this.value, d);
    }

    public boolean equals(Object object) {
        return Duration.equals-impl(this.value, object);
    }

    public int hashCode() {
        return Duration.hashCode-impl(this.value);
    }

    public String toString() {
        return Duration.toString-impl(this.value);
    }

    public final /* synthetic */ double unbox-impl() {
        return this.value;
    }

    /*
     * Illegal identifiers - consider using --renameillegalidents true
     */
    @Metadata(bv={1, 0, 3}, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J&\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\n\u0010\r\u001a\u00060\u000ej\u0002`\u000f2\n\u0010\u0010\u001a\u00060\u000ej\u0002`\u000fR\u0019\u0010\u0003\u001a\u00020\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\u0005\u0010\u0006R\u0019\u0010\b\u001a\u00020\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\t\u0010\u0006\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!\u00a8\u0006\u0011"}, d2={"Lkotlin/time/Duration$Companion;", "", "()V", "INFINITE", "Lkotlin/time/Duration;", "getINFINITE-UwyO8pc", "()D", "D", "ZERO", "getZERO-UwyO8pc", "convert", "", "value", "sourceUnit", "Ljava/util/concurrent/TimeUnit;", "Lkotlin/time/DurationUnit;", "targetUnit", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final double convert(double d, TimeUnit timeUnit, TimeUnit timeUnit2) {
            Intrinsics.checkNotNullParameter((Object)timeUnit, "sourceUnit");
            Intrinsics.checkNotNullParameter((Object)timeUnit2, "targetUnit");
            return DurationUnitKt.convertDurationUnit(d, timeUnit, timeUnit2);
        }

        public final double getINFINITE-UwyO8pc() {
            return INFINITE;
        }

        public final double getZERO-UwyO8pc() {
            return ZERO;
        }
    }
}

