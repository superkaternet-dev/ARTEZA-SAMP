/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.CollectionsKt__ReversedViewsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv={1, 0, 3}, d1={"\u0000R\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u001c\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001f\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a(\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u0006\u0012\u0002\b\u00030\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005\u001aA\u0010\u0006\u001a\u0002H\u0007\"\u0010\b\u0000\u0010\u0007*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\b\"\u0004\b\u0001\u0010\u0002*\u0006\u0012\u0002\b\u00030\u00032\u0006\u0010\t\u001a\u0002H\u00072\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005\u00a2\u0006\u0002\u0010\n\u001a\u0016\u0010\u000b\u001a\u00020\f\"\u0004\b\u0000\u0010\r*\b\u0012\u0004\u0012\u0002H\r0\u000e\u001a5\u0010\u000f\u001a\u00020\u0010\"\u0004\b\u0000\u0010\r*\b\u0012\u0004\u0012\u0002H\r0\u00032\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u00020\u00100\u0012H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\b\u0013\u001a5\u0010\u000f\u001a\u00020\u0014\"\u0004\b\u0000\u0010\r*\b\u0012\u0004\u0012\u0002H\r0\u00032\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u00020\u00140\u0012H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\b\u0015\u001a&\u0010\u0016\u001a\b\u0012\u0004\u0012\u0002H\r0\u0017\"\u000e\b\u0000\u0010\r*\b\u0012\u0004\u0012\u0002H\r0\u0018*\b\u0012\u0004\u0012\u0002H\r0\u0003\u001a8\u0010\u0016\u001a\b\u0012\u0004\u0012\u0002H\r0\u0017\"\u0004\b\u0000\u0010\r*\b\u0012\u0004\u0012\u0002H\r0\u00032\u001a\u0010\u0019\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\r0\u001aj\n\u0012\u0006\b\u0000\u0012\u0002H\r`\u001b\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006\u001c"}, d2={"filterIsInstance", "", "R", "", "klass", "Ljava/lang/Class;", "filterIsInstanceTo", "C", "", "destination", "(Ljava/lang/Iterable;Ljava/util/Collection;Ljava/lang/Class;)Ljava/util/Collection;", "reverse", "", "T", "", "sumOf", "Ljava/math/BigDecimal;", "selector", "Lkotlin/Function1;", "sumOfBigDecimal", "Ljava/math/BigInteger;", "sumOfBigInteger", "toSortedSet", "Ljava/util/SortedSet;", "", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "kotlin-stdlib"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/collections/CollectionsKt")
class CollectionsKt___CollectionsJvmKt
extends CollectionsKt__ReversedViewsKt {
    public static final <R> List<R> filterIsInstance(Iterable<?> iterable, Class<R> clazz) {
        Intrinsics.checkNotNullParameter(iterable, "$this$filterIsInstance");
        Intrinsics.checkNotNullParameter(clazz, "klass");
        return (List)CollectionsKt.filterIsInstanceTo(iterable, (Collection)new ArrayList(), clazz);
    }

    public static final <C extends Collection<? super R>, R> C filterIsInstanceTo(Iterable<?> iterable2, C c, Class<R> clazz) {
        Intrinsics.checkNotNullParameter(iterable2, "$this$filterIsInstanceTo");
        Intrinsics.checkNotNullParameter(c, "destination");
        Intrinsics.checkNotNullParameter(clazz, "klass");
        for (Iterable<?> iterable2 : iterable2) {
            if (!clazz.isInstance(iterable2)) continue;
            c.add(iterable2);
        }
        return c;
    }

    public static final <T> void reverse(List<T> list) {
        Intrinsics.checkNotNullParameter(list, "$this$reverse");
        Collections.reverse(list);
    }

    private static final <T> BigDecimal sumOfBigDecimal(Iterable<? extends T> object, Function1<? super T, ? extends BigDecimal> function1) {
        BigDecimal bigDecimal = BigDecimal.valueOf(0);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "BigDecimal.valueOf(this.toLong())");
        Iterator<T> iterator2 = object.iterator();
        object = bigDecimal;
        while (iterator2.hasNext()) {
            object = ((BigDecimal)object).add(function1.invoke(iterator2.next()));
            Intrinsics.checkNotNullExpressionValue(object, "this.add(other)");
        }
        return object;
    }

    private static final <T> BigInteger sumOfBigInteger(Iterable<? extends T> object, Function1<? super T, ? extends BigInteger> function1) {
        BigInteger bigInteger = BigInteger.valueOf(0);
        Intrinsics.checkNotNullExpressionValue(bigInteger, "BigInteger.valueOf(this.toLong())");
        Iterator<T> iterator2 = object.iterator();
        object = bigInteger;
        while (iterator2.hasNext()) {
            object = ((BigInteger)object).add(function1.invoke(iterator2.next()));
            Intrinsics.checkNotNullExpressionValue(object, "this.add(other)");
        }
        return object;
    }

    public static final <T extends Comparable<? super T>> SortedSet<T> toSortedSet(Iterable<? extends T> iterable) {
        Intrinsics.checkNotNullParameter(iterable, "$this$toSortedSet");
        return (SortedSet)CollectionsKt.toCollection(iterable, (Collection)new TreeSet());
    }

    public static final <T> SortedSet<T> toSortedSet(Iterable<? extends T> iterable, Comparator<? super T> comparator) {
        Intrinsics.checkNotNullParameter(iterable, "$this$toSortedSet");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return (SortedSet)CollectionsKt.toCollection(iterable, (Collection)new TreeSet<T>(comparator));
    }
}

