/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.collections.ReversedList;
import kotlin.collections.ReversedListReadOnly;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;

@Metadata(bv={1, 0, 3}, d1={"\u0000\u0018\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001\u001a#\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0007\u00a2\u0006\u0002\b\u0004\u001a\u001d\u0010\u0005\u001a\u00020\u0006*\u0006\u0012\u0002\b\u00030\u00012\u0006\u0010\u0007\u001a\u00020\u0006H\u0002\u00a2\u0006\u0002\b\b\u001a\u001d\u0010\t\u001a\u00020\u0006*\u0006\u0012\u0002\b\u00030\u00012\u0006\u0010\u0007\u001a\u00020\u0006H\u0002\u00a2\u0006\u0002\b\n\u00a8\u0006\u000b"}, d2={"asReversed", "", "T", "", "asReversedMutable", "reverseElementIndex", "", "index", "reverseElementIndex$CollectionsKt__ReversedViewsKt", "reversePositionIndex", "reversePositionIndex$CollectionsKt__ReversedViewsKt", "kotlin-stdlib"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/collections/CollectionsKt")
class CollectionsKt__ReversedViewsKt
extends CollectionsKt__MutableCollectionsKt {
    public static final /* synthetic */ int access$reverseElementIndex(List list, int n) {
        return CollectionsKt__ReversedViewsKt.reverseElementIndex$CollectionsKt__ReversedViewsKt(list, n);
    }

    public static final /* synthetic */ int access$reversePositionIndex(List list, int n) {
        return CollectionsKt__ReversedViewsKt.reversePositionIndex$CollectionsKt__ReversedViewsKt(list, n);
    }

    public static final <T> List<T> asReversed(List<? extends T> list) {
        Intrinsics.checkNotNullParameter(list, "$this$asReversed");
        return new ReversedListReadOnly<T>(list);
    }

    public static final <T> List<T> asReversedMutable(List<T> list) {
        Intrinsics.checkNotNullParameter(list, "$this$asReversed");
        return new ReversedList<T>(list);
    }

    private static final int reverseElementIndex$CollectionsKt__ReversedViewsKt(List<?> list, int n) {
        int n2 = CollectionsKt.getLastIndex(list);
        if (n >= 0 && n2 >= n) {
            return CollectionsKt.getLastIndex(list) - n;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Element index ");
        stringBuilder.append(n);
        stringBuilder.append(" must be in range [");
        stringBuilder.append(new IntRange(0, CollectionsKt.getLastIndex(list)));
        stringBuilder.append("].");
        throw (Throwable)new IndexOutOfBoundsException(stringBuilder.toString());
    }

    private static final int reversePositionIndex$CollectionsKt__ReversedViewsKt(List<?> list, int n) {
        int n2 = list.size();
        if (n >= 0 && n2 >= n) {
            return list.size() - n;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Position index ");
        stringBuilder.append(n);
        stringBuilder.append(" must be in range [");
        stringBuilder.append(new IntRange(0, list.size()));
        stringBuilder.append("].");
        throw (Throwable)new IndexOutOfBoundsException(stringBuilder.toString());
    }
}

