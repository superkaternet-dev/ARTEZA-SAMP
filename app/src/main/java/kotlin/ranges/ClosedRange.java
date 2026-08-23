/*
 * Decompiled with CFR 0.152.
 */
package kotlin.ranges;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv={1, 0, 3}, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000f\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0004\bf\u0018\u0000*\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00020\u0003J\u0016\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00028\u0000H\u0096\u0002\u00a2\u0006\u0002\u0010\fJ\b\u0010\r\u001a\u00020\nH\u0016R\u0012\u0010\u0004\u001a\u00028\u0000X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0012\u0010\u0007\u001a\u00028\u0000X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\u0006\u00a8\u0006\u000e"}, d2={"Lkotlin/ranges/ClosedRange;", "T", "", "", "endInclusive", "getEndInclusive", "()Ljava/lang/Comparable;", "start", "getStart", "contains", "", "value", "(Ljava/lang/Comparable;)Z", "isEmpty", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public interface ClosedRange<T extends Comparable<? super T>> {
    public boolean contains(T var1);

    public T getEndInclusive();

    public T getStart();

    public boolean isEmpty();

    @Metadata(bv={1, 0, 3}, k=3, mv={1, 4, 1})
    public static final class DefaultImpls {
        public static <T extends Comparable<? super T>> boolean contains(ClosedRange<T> closedRange, T object) {
            Intrinsics.checkNotNullParameter(object, "value");
            boolean bl = object.compareTo(closedRange.getStart()) >= 0 && object.compareTo(closedRange.getEndInclusive()) <= 0;
            return bl;
        }

        public static <T extends Comparable<? super T>> boolean isEmpty(ClosedRange<T> closedRange) {
            boolean bl = closedRange.getStart().compareTo(closedRange.getEndInclusive()) > 0;
            return bl;
        }
    }
}

