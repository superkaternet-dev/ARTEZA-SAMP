/*
 * Decompiled with CFR 0.152.
 */
package kotlin.time;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.time.TimeMark;
import kotlin.time.TimeSource;
import kotlin.time.TimedValue;

@Metadata(bv={1, 0, 3}, d1={"\u0000\"\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a/\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u0087\b\u00f8\u0001\u0000\u00f8\u0001\u0001\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u00a2\u0006\u0002\u0010\u0005\u001a3\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\b2\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002H\b0\u0003H\u0087\b\u00f8\u0001\u0001\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u001a3\u0010\u0000\u001a\u00020\u0001*\u00020\t2\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u0087\b\u00f8\u0001\u0000\u00f8\u0001\u0001\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u00a2\u0006\u0002\u0010\n\u001a7\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\b*\u00020\t2\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002H\b0\u0003H\u0087\b\u00f8\u0001\u0001\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u0082\u0002\u000b\n\u0002\b\u0019\n\u0005\b\u009920\u0001\u00a8\u0006\u000b"}, d2={"measureTime", "Lkotlin/time/Duration;", "block", "Lkotlin/Function0;", "", "(Lkotlin/jvm/functions/Function0;)D", "measureTimedValue", "Lkotlin/time/TimedValue;", "T", "Lkotlin/time/TimeSource;", "(Lkotlin/time/TimeSource;Lkotlin/jvm/functions/Function0;)D", "kotlin-stdlib"}, k=2, mv={1, 4, 1})
public final class MeasureTimeKt {
    public static final double measureTime(Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(function0, "block");
        Object object = TimeSource.Monotonic.INSTANCE;
        object = object.markNow();
        function0.invoke();
        return ((TimeMark)object).elapsedNow-UwyO8pc();
    }

    public static final double measureTime(TimeSource object, Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(object, "$this$measureTime");
        Intrinsics.checkNotNullParameter(function0, "block");
        object = object.markNow();
        function0.invoke();
        return ((TimeMark)object).elapsedNow-UwyO8pc();
    }

    public static final <T> TimedValue<T> measureTimedValue(Function0<? extends T> function0) {
        Intrinsics.checkNotNullParameter(function0, "block");
        Object object = TimeSource.Monotonic.INSTANCE;
        object = object.markNow();
        return new TimedValue(function0.invoke(), ((TimeMark)object).elapsedNow-UwyO8pc(), null);
    }

    public static final <T> TimedValue<T> measureTimedValue(TimeSource object, Function0<? extends T> function0) {
        Intrinsics.checkNotNullParameter(object, "$this$measureTimedValue");
        Intrinsics.checkNotNullParameter(function0, "block");
        object = object.markNow();
        return new TimedValue(function0.invoke(), ((TimeMark)object).elapsedNow-UwyO8pc(), null);
    }
}

