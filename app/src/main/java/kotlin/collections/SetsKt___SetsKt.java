/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;

@Metadata(bv={1, 0, 3}, d1={"\u0000\u001c\n\u0000\n\u0002\u0010\"\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a,\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0086\u0002\u00a2\u0006\u0002\u0010\u0004\u001a4\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u000e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0006H\u0086\u0002\u00a2\u0006\u0002\u0010\u0007\u001a-\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0086\u0002\u001a-\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\tH\u0086\u0002\u001a,\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0087\b\u00a2\u0006\u0002\u0010\u0004\u001a,\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0086\u0002\u00a2\u0006\u0002\u0010\u0004\u001a4\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u000e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0006H\u0086\u0002\u00a2\u0006\u0002\u0010\u0007\u001a-\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0086\u0002\u001a-\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\tH\u0086\u0002\u001a,\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0087\b\u00a2\u0006\u0002\u0010\u0004\u00a8\u0006\r"}, d2={"minus", "", "T", "element", "(Ljava/util/Set;Ljava/lang/Object;)Ljava/util/Set;", "elements", "", "(Ljava/util/Set;[Ljava/lang/Object;)Ljava/util/Set;", "", "Lkotlin/sequences/Sequence;", "minusElement", "plus", "plusElement", "kotlin-stdlib"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/collections/SetsKt")
class SetsKt___SetsKt
extends SetsKt__SetsKt {
    public static final <T> Set<T> minus(Set<? extends T> collection, Iterable<? extends T> iterable) {
        Intrinsics.checkNotNullParameter(collection, "$this$minus");
        Intrinsics.checkNotNullParameter(iterable, "elements");
        iterable = CollectionsKt.convertToSetForSetOperationWith(iterable, (Iterable)collection);
        if (iterable.isEmpty()) {
            return CollectionsKt.toSet((Iterable)collection);
        }
        if (iterable instanceof Set) {
            Object object = collection;
            collection = new LinkedHashSet();
            object = object.iterator();
            while (object.hasNext()) {
                Object e = object.next();
                if (iterable.contains(e)) continue;
                collection.add(e);
            }
            return collection;
        }
        collection = new LinkedHashSet<T>((Collection)collection);
        ((AbstractSet)collection).removeAll((Collection<?>)iterable);
        return collection;
    }

    public static final <T> Set<T> minus(Set<? extends T> object, T t) {
        Intrinsics.checkNotNullParameter(object, "$this$minus");
        LinkedHashSet linkedHashSet = new LinkedHashSet(MapsKt.mapCapacity(object.size()));
        boolean bl = false;
        for (Object e : (Iterable)object) {
            boolean bl2;
            boolean bl3;
            if (!bl && Intrinsics.areEqual(e, t)) {
                bl3 = true;
                bl2 = false;
            } else {
                bl2 = true;
                bl3 = bl;
            }
            bl = bl3;
            if (!bl2) continue;
            ((Collection)linkedHashSet).add(e);
            bl = bl3;
        }
        return (Set)((Collection)linkedHashSet);
    }

    public static final <T> Set<T> minus(Set<? extends T> set, Sequence<? extends T> sequence) {
        Intrinsics.checkNotNullParameter(set, "$this$minus");
        Intrinsics.checkNotNullParameter(sequence, "elements");
        set = new LinkedHashSet<T>((Collection)set);
        CollectionsKt.removeAll((Collection)set, sequence);
        return set;
    }

    public static final <T> Set<T> minus(Set<? extends T> set, T[] TArray) {
        Intrinsics.checkNotNullParameter(set, "$this$minus");
        Intrinsics.checkNotNullParameter(TArray, "elements");
        set = new LinkedHashSet<T>((Collection)set);
        CollectionsKt.removeAll((Collection)set, TArray);
        return set;
    }

    private static final <T> Set<T> minusElement(Set<? extends T> set, T t) {
        return SetsKt.minus(set, t);
    }

    public static final <T> Set<T> plus(Set<? extends T> set, Iterable<? extends T> iterable) {
        int n;
        Intrinsics.checkNotNullParameter(set, "$this$plus");
        Intrinsics.checkNotNullParameter(iterable, "elements");
        Serializable serializable = CollectionsKt.collectionSizeOrNull(iterable);
        if (serializable != null) {
            n = ((Number)serializable).intValue();
            n = set.size() + n;
        } else {
            n = set.size() * 2;
        }
        serializable = new LinkedHashSet(MapsKt.mapCapacity(n));
        ((AbstractCollection)((Object)serializable)).addAll((Collection)set);
        CollectionsKt.addAll((Collection)((Object)serializable), iterable);
        return (Set)((Object)serializable);
    }

    public static final <T> Set<T> plus(Set<? extends T> set, T t) {
        Intrinsics.checkNotNullParameter(set, "$this$plus");
        LinkedHashSet<T> linkedHashSet = new LinkedHashSet<T>(MapsKt.mapCapacity(set.size() + 1));
        linkedHashSet.addAll((Collection)set);
        linkedHashSet.add(t);
        return linkedHashSet;
    }

    public static final <T> Set<T> plus(Set<? extends T> set, Sequence<? extends T> sequence) {
        Intrinsics.checkNotNullParameter(set, "$this$plus");
        Intrinsics.checkNotNullParameter(sequence, "elements");
        LinkedHashSet linkedHashSet = new LinkedHashSet(MapsKt.mapCapacity(set.size() * 2));
        linkedHashSet.addAll(set);
        CollectionsKt.addAll((Collection)linkedHashSet, sequence);
        return linkedHashSet;
    }

    public static final <T> Set<T> plus(Set<? extends T> set, T[] TArray) {
        Intrinsics.checkNotNullParameter(set, "$this$plus");
        Intrinsics.checkNotNullParameter(TArray, "elements");
        LinkedHashSet linkedHashSet = new LinkedHashSet(MapsKt.mapCapacity(set.size() + TArray.length));
        linkedHashSet.addAll(set);
        CollectionsKt.addAll((Collection)linkedHashSet, TArray);
        return linkedHashSet;
    }

    private static final <T> Set<T> plusElement(Set<? extends T> set, T t) {
        return SetsKt.plus(set, t);
    }
}

