/*
 * Decompiled with CFR 0.152.
 */
package kotlin.time;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.time.Duration;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(bv={1, 0, 3}, d1={"\u0000*\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002B\u0018\u0012\u0006\u0010\u0003\u001a\u00028\u0000\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\r\u001a\u00028\u0000H\u00c6\u0003\u00a2\u0006\u0002\u0010\u000bJ\u0016\u0010\u000e\u001a\u00020\u0005H\u00c6\u0003\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u000f\u0010\bJ-\u0010\u0010\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\b\b\u0002\u0010\u0003\u001a\u00028\u00002\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0011\u0010\u0012J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0002H\u00d6\u0003J\t\u0010\u0016\u001a\u00020\u0017H\u00d6\u0001J\t\u0010\u0018\u001a\u00020\u0019H\u00d6\u0001R\u0019\u0010\u0004\u001a\u00020\u0005\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\n\n\u0002\u0010\t\u001a\u0004\b\u0007\u0010\bR\u0013\u0010\u0003\u001a\u00028\u0000\u00a2\u0006\n\n\u0002\u0010\f\u001a\u0004\b\n\u0010\u000b\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!\u00a8\u0006\u001a"}, d2={"Lkotlin/time/TimedValue;", "T", "", "value", "duration", "Lkotlin/time/Duration;", "(Ljava/lang/Object;DLkotlin/jvm/internal/DefaultConstructorMarker;)V", "getDuration-UwyO8pc", "()D", "D", "getValue", "()Ljava/lang/Object;", "Ljava/lang/Object;", "component1", "component2", "component2-UwyO8pc", "copy", "copy-RFiDyg4", "(Ljava/lang/Object;D)Lkotlin/time/TimedValue;", "equals", "", "other", "hashCode", "", "toString", "", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class TimedValue<T> {
    private final double duration;
    private final T value;

    private TimedValue(T t, double d) {
        this.value = t;
        this.duration = d;
    }

    public /* synthetic */ TimedValue(Object object, double d, DefaultConstructorMarker defaultConstructorMarker) {
        this(object, d);
    }

    public static /* synthetic */ TimedValue copy-RFiDyg4$default(TimedValue timedValue, Object object, double d, int n, Object object2) {
        if ((n & 1) != 0) {
            object = timedValue.value;
        }
        if ((n & 2) != 0) {
            d = timedValue.duration;
        }
        return timedValue.copy-RFiDyg4(object, d);
    }

    public final T component1() {
        return this.value;
    }

    public final double component2-UwyO8pc() {
        return this.duration;
    }

    public final TimedValue<T> copy-RFiDyg4(T t, double d) {
        return new TimedValue<T>(t, d);
    }

    public boolean equals(Object object) {
        block2: {
            block3: {
                if (this == object) break block2;
                if (!(object instanceof TimedValue)) break block3;
                object = (TimedValue)object;
                if (Intrinsics.areEqual(this.value, ((TimedValue)object).value) && Double.compare(this.duration, ((TimedValue)object).duration) == 0) break block2;
            }
            return false;
        }
        return true;
    }

    public final double getDuration-UwyO8pc() {
        return this.duration;
    }

    public final T getValue() {
        return this.value;
    }

    public int hashCode() {
        T t = this.value;
        int n = t != null ? t.hashCode() : 0;
        long l = Double.doubleToLongBits(this.duration);
        return n * 31 + (int)(l ^ l >>> 32);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("TimedValue(value=");
        stringBuilder.append(this.value);
        stringBuilder.append(", duration=");
        stringBuilder.append(Duration.toString-impl(this.duration));
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}

