/*
 * Decompiled with CFR 0.152.
 */
package kotlin.sequences;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.sequences.SequencesKt__SequencesKt;

@Metadata(bv={1, 0, 3}, d1={"\u0000D\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001f\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a(\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u0006\u0012\u0002\b\u00030\u00012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004\u001aA\u0010\u0005\u001a\u0002H\u0006\"\u0010\b\u0000\u0010\u0006*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0007\"\u0004\b\u0001\u0010\u0002*\u0006\u0012\u0002\b\u00030\u00012\u0006\u0010\b\u001a\u0002H\u00062\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004\u00a2\u0006\u0002\u0010\t\u001a5\u0010\n\u001a\u00020\u000b\"\u0004\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0\u00012\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\f\u0012\u0004\u0012\u00020\u000b0\u000eH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\b\u000f\u001a5\u0010\n\u001a\u00020\u0010\"\u0004\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0\u00012\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\f\u0012\u0004\u0012\u00020\u00100\u000eH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\b\u0011\u001a&\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\f0\u0013\"\u000e\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0\u0014*\b\u0012\u0004\u0012\u0002H\f0\u0001\u001a8\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\f0\u0013\"\u0004\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0\u00012\u001a\u0010\u0015\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\f0\u0016j\n\u0012\u0006\b\u0000\u0012\u0002H\f`\u0017\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006\u0018"}, d2={"filterIsInstance", "Lkotlin/sequences/Sequence;", "R", "klass", "Ljava/lang/Class;", "filterIsInstanceTo", "C", "", "destination", "(Lkotlin/sequences/Sequence;Ljava/util/Collection;Ljava/lang/Class;)Ljava/util/Collection;", "sumOf", "Ljava/math/BigDecimal;", "T", "selector", "Lkotlin/Function1;", "sumOfBigDecimal", "Ljava/math/BigInteger;", "sumOfBigInteger", "toSortedSet", "Ljava/util/SortedSet;", "", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "kotlin-stdlib"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/sequences/SequencesKt")
class SequencesKt___SequencesJvmKt
extends SequencesKt__SequencesKt {
    public static final <R> Sequence<R> filterIsInstance(Sequence<?> sequence, Class<R> clazz) {
        Intrinsics.checkNotNullParameter(sequence, "$this$filterIsInstance");
        Intrinsics.checkNotNullParameter(clazz, "klass");
        sequence = SequencesKt.filter(sequence, (Function1)new Function1<Object, Boolean>(clazz){
            final Class $klass;
            {
                this.$klass = clazz;
                super(1);
            }

            public final boolean invoke(Object object) {
                return this.$klass.isInstance(object);
            }
        });
        if (sequence != null) {
            return sequence;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.sequences.Sequence<R>");
    }

    public static final <C extends Collection<? super R>, R> C filterIsInstanceTo(Sequence<?> object, C c, Class<R> clazz) {
        Intrinsics.checkNotNullParameter(object, "$this$filterIsInstanceTo");
        Intrinsics.checkNotNullParameter(c, "destination");
        Intrinsics.checkNotNullParameter(clazz, "klass");
        object = object.iterator();
        while (object.hasNext()) {
            Object e = object.next();
            if (!clazz.isInstance(e)) continue;
            c.add(e);
        }
        return c;
    }

    private static final <T> BigDecimal sumOfBigDecimal(Sequence<? extends T> object, Function1<? super T, ? extends BigDecimal> function1) {
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

    private static final <T> BigInteger sumOfBigInteger(Sequence<? extends T> object, Function1<? super T, ? extends BigInteger> function1) {
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

    public static final <T extends Comparable<? super T>> SortedSet<T> toSortedSet(Sequence<? extends T> sequence) {
        Intrinsics.checkNotNullParameter(sequence, "$this$toSortedSet");
        return (SortedSet)SequencesKt.toCollection(sequence, (Collection)new TreeSet());
    }

    public static final <T> SortedSet<T> toSortedSet(Sequence<? extends T> sequence, Comparator<? super T> comparator) {
        Intrinsics.checkNotNullParameter(sequence, "$this$toSortedSet");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return (SortedSet)SequencesKt.toCollection(sequence, (Collection)new TreeSet<T>(comparator));
    }
}

