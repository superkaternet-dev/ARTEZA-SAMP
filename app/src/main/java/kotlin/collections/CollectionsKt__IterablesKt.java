/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv={1, 0, 3}, d1={"\u0000:\n\u0000\n\u0002\u0010\u001c\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u001e\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a.\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0014\b\u0004\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00050\u0004H\u0087\b\u00f8\u0001\u0000\u001a \u0010\u0006\u001a\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\b\u001a\u00020\u0007H\u0001\u001a\u001f\u0010\t\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001H\u0001\u00a2\u0006\u0002\u0010\n\u001a\u001e\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\f\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001H\u0000\u001a,\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\u00020\f\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001H\u0000\u001a\"\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0010\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00010\u0001\u001a\u001d\u0010\u0011\u001a\u00020\u0012\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\fH\u0002\u00a2\u0006\u0002\b\u0013\u001a@\u0010\u0014\u001a\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00160\u00100\u0015\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0016*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00160\u00150\u0001\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006\u0017"}, d2={"Iterable", "", "T", "iterator", "Lkotlin/Function0;", "", "collectionSizeOrDefault", "", "default", "collectionSizeOrNull", "(Ljava/lang/Iterable;)Ljava/lang/Integer;", "convertToSetForSetOperation", "", "convertToSetForSetOperationWith", "source", "flatten", "", "safeToConvertToSet", "", "safeToConvertToSet$CollectionsKt__IterablesKt", "unzip", "Lkotlin/Pair;", "R", "kotlin-stdlib"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/collections/CollectionsKt")
class CollectionsKt__IterablesKt
extends CollectionsKt__CollectionsKt {
    private static final <T> Iterable<T> Iterable(Function0<? extends Iterator<? extends T>> function0) {
        return new Iterable<T>(function0){
            final Function0 $iterator;
            {
                this.$iterator = function0;
            }

            public Iterator<T> iterator() {
                return (Iterator)this.$iterator.invoke();
            }
        };
    }

    public static final <T> int collectionSizeOrDefault(Iterable<? extends T> iterable, int n) {
        block0: {
            Intrinsics.checkNotNullParameter(iterable, "$this$collectionSizeOrDefault");
            if (!(iterable instanceof Collection)) break block0;
            n = ((Collection)iterable).size();
        }
        return n;
    }

    public static final <T> Integer collectionSizeOrNull(Iterable<? extends T> object) {
        Intrinsics.checkNotNullParameter(object, "$this$collectionSizeOrNull");
        object = object instanceof Collection ? Integer.valueOf(((Collection)object).size()) : null;
        return object;
    }

    public static final <T> Collection<T> convertToSetForSetOperation(Iterable<? extends T> collection) {
        Intrinsics.checkNotNullParameter(collection, "$this$convertToSetForSetOperation");
        if (collection instanceof Set) {
            collection = collection;
        } else if (collection instanceof Collection) {
            if (CollectionsKt__IterablesKt.safeToConvertToSet$CollectionsKt__IterablesKt(collection)) {
                collection = CollectionsKt.toHashSet(collection);
            }
        } else {
            collection = CollectionsKt.toHashSet(collection);
        }
        return collection;
    }

    public static final <T> Collection<T> convertToSetForSetOperationWith(Iterable<? extends T> collection, Iterable<? extends T> iterable) {
        Intrinsics.checkNotNullParameter(collection, "$this$convertToSetForSetOperationWith");
        Intrinsics.checkNotNullParameter(iterable, "source");
        if (collection instanceof Set) {
            collection = collection;
        } else if (collection instanceof Collection) {
            if (!(iterable instanceof Collection) || ((Collection)iterable).size() >= 2) {
                if (CollectionsKt__IterablesKt.safeToConvertToSet$CollectionsKt__IterablesKt(collection)) {
                    collection = CollectionsKt.toHashSet(collection);
                }
            }
        } else {
            collection = CollectionsKt.toHashSet(collection);
        }
        return collection;
    }

    public static final <T> List<T> flatten(Iterable<? extends Iterable<? extends T>> iterable2) {
        Intrinsics.checkNotNullParameter(iterable2, "$this$flatten");
        ArrayList arrayList = new ArrayList();
        for (Iterable<T> iterable : iterable2) {
            CollectionsKt.addAll((Collection)arrayList, iterable);
        }
        return arrayList;
    }

    private static final <T> boolean safeToConvertToSet$CollectionsKt__IterablesKt(Collection<? extends T> collection) {
        boolean bl = collection.size() > 2 && collection instanceof ArrayList;
        return bl;
    }

    public static final <T, R> Pair<List<T>, List<R>> unzip(Iterable<? extends Pair<? extends T, ? extends R>> object) {
        Intrinsics.checkNotNullParameter(object, "$this$unzip");
        int n = CollectionsKt.collectionSizeOrDefault(object, 10);
        ArrayList arrayList = new ArrayList(n);
        ArrayList arrayList2 = new ArrayList(n);
        Iterator<Pair<T, R>> iterator2 = object.iterator();
        while (iterator2.hasNext()) {
            object = iterator2.next();
            arrayList.add(((Pair)object).getFirst());
            arrayList2.add(((Pair)object).getSecond());
        }
        return TuplesKt.to(arrayList, arrayList2);
    }
}

