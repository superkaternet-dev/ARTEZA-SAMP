/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArrayAsCollection;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.collections.EmptyList;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import kotlin.ranges.IntRange;

@Metadata(bv={1, 0, 3}, d1={"\u0000\u0088\u0001\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000f\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\u001aC\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\u0004\b\u0000\u0010\u00072\u0006\u0010\f\u001a\u00020\u00062!\u0010\r\u001a\u001d\u0012\u0013\u0012\u00110\u0006\u00a2\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0011\u0012\u0004\u0012\u0002H\u00070\u000eH\u0087\b\u00f8\u0001\u0000\u001aC\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0013\"\u0004\b\u0000\u0010\u00072\u0006\u0010\f\u001a\u00020\u00062!\u0010\r\u001a\u001d\u0012\u0013\u0012\u00110\u0006\u00a2\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0011\u0012\u0004\u0012\u0002H\u00070\u000eH\u0087\b\u00f8\u0001\u0000\u001a\u001f\u0010\u0014\u001a\u0012\u0012\u0004\u0012\u0002H\u00070\u0015j\b\u0012\u0004\u0012\u0002H\u0007`\u0016\"\u0004\b\u0000\u0010\u0007H\u0087\b\u001a5\u0010\u0014\u001a\u0012\u0012\u0004\u0012\u0002H\u00070\u0015j\b\u0012\u0004\u0012\u0002H\u0007`\u0016\"\u0004\b\u0000\u0010\u00072\u0012\u0010\u0017\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00070\u0018\"\u0002H\u0007\u00a2\u0006\u0002\u0010\u0019\u001aN\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u001b0\b\"\u0004\b\u0000\u0010\u001b2\u0006\u0010\u001c\u001a\u00020\u00062\u001f\b\u0001\u0010\u001d\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u001b0\u0013\u0012\u0004\u0012\u00020\u001e0\u000e\u00a2\u0006\u0002\b\u001fH\u0087\b\u00f8\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0002 \u0001\u001aF\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u001b0\b\"\u0004\b\u0000\u0010\u001b2\u001f\b\u0001\u0010\u001d\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u001b0\u0013\u0012\u0004\u0012\u00020\u001e0\u000e\u00a2\u0006\u0002\b\u001fH\u0087\b\u00f8\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u001a\u0012\u0010 \u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\u0004\b\u0000\u0010\u0007\u001a\u0015\u0010!\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\u0004\b\u0000\u0010\u0007H\u0087\b\u001a+\u0010!\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\u0004\b\u0000\u0010\u00072\u0012\u0010\u0017\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00070\u0018\"\u0002H\u0007\u00a2\u0006\u0002\u0010\"\u001a%\u0010#\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\b\b\u0000\u0010\u0007*\u00020$2\b\u0010%\u001a\u0004\u0018\u0001H\u0007\u00a2\u0006\u0002\u0010&\u001a3\u0010#\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\b\b\u0000\u0010\u0007*\u00020$2\u0016\u0010\u0017\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u0001H\u00070\u0018\"\u0004\u0018\u0001H\u0007\u00a2\u0006\u0002\u0010\"\u001a\u0015\u0010'\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0013\"\u0004\b\u0000\u0010\u0007H\u0087\b\u001a+\u0010'\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0013\"\u0004\b\u0000\u0010\u00072\u0012\u0010\u0017\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00070\u0018\"\u0002H\u0007\u00a2\u0006\u0002\u0010\"\u001a%\u0010(\u001a\u00020\u001e2\u0006\u0010\f\u001a\u00020\u00062\u0006\u0010)\u001a\u00020\u00062\u0006\u0010*\u001a\u00020\u0006H\u0002\u00a2\u0006\u0002\b+\u001a\b\u0010,\u001a\u00020\u001eH\u0001\u001a\b\u0010-\u001a\u00020\u001eH\u0001\u001a%\u0010.\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0002\"\u0004\b\u0000\u0010\u0007*\n\u0012\u0006\b\u0001\u0012\u0002H\u00070\u0018H\u0000\u00a2\u0006\u0002\u0010/\u001aS\u00100\u001a\u00020\u0006\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\b2\u0006\u0010%\u001a\u0002H\u00072\u001a\u00101\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u000702j\n\u0012\u0006\b\u0000\u0012\u0002H\u0007`32\b\b\u0002\u0010)\u001a\u00020\u00062\b\b\u0002\u0010*\u001a\u00020\u0006\u00a2\u0006\u0002\u00104\u001a>\u00100\u001a\u00020\u0006\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\b2\b\b\u0002\u0010)\u001a\u00020\u00062\b\b\u0002\u0010*\u001a\u00020\u00062\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u0002H\u0007\u0012\u0004\u0012\u00020\u00060\u000e\u001aE\u00100\u001a\u00020\u0006\"\u000e\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u000706*\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00070\b2\b\u0010%\u001a\u0004\u0018\u0001H\u00072\b\b\u0002\u0010)\u001a\u00020\u00062\b\b\u0002\u0010*\u001a\u00020\u0006\u00a2\u0006\u0002\u00107\u001ag\u00108\u001a\u00020\u0006\"\u0004\b\u0000\u0010\u0007\"\u000e\b\u0001\u00109*\b\u0012\u0004\u0012\u0002H906*\b\u0012\u0004\u0012\u0002H\u00070\b2\b\u0010:\u001a\u0004\u0018\u0001H92\b\b\u0002\u0010)\u001a\u00020\u00062\b\b\u0002\u0010*\u001a\u00020\u00062\u0016\b\u0004\u0010;\u001a\u0010\u0012\u0004\u0012\u0002H\u0007\u0012\u0006\u0012\u0004\u0018\u0001H90\u000eH\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010<\u001a,\u0010=\u001a\u00020>\"\t\b\u0000\u0010\u0007\u00a2\u0006\u0002\b?*\b\u0012\u0004\u0012\u0002H\u00070\u00022\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0002H\u0087\b\u001a;\u0010@\u001a\u0002HA\"\u0010\b\u0000\u0010B*\u0006\u0012\u0002\b\u00030\u0002*\u0002HA\"\u0004\b\u0001\u0010A*\u0002HB2\f\u0010C\u001a\b\u0012\u0004\u0012\u0002HA0DH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010E\u001a\u0019\u0010F\u001a\u00020>\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u0002H\u0087\b\u001a,\u0010G\u001a\u00020>\"\u0004\b\u0000\u0010\u0007*\n\u0012\u0004\u0012\u0002H\u0007\u0018\u00010\u0002H\u0087\b\u0082\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000\u001a\u001e\u0010H\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\bH\u0000\u001a!\u0010I\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0002\"\u0004\b\u0000\u0010\u0007*\n\u0012\u0004\u0012\u0002H\u0007\u0018\u00010\u0002H\u0087\b\u001a!\u0010I\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\u0004\b\u0000\u0010\u0007*\n\u0012\u0004\u0012\u0002H\u0007\u0018\u00010\bH\u0087\b\u001a&\u0010J\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070K2\u0006\u0010L\u001a\u00020MH\u0007\"\u0019\u0010\u0000\u001a\u00020\u0001*\u0006\u0012\u0002\b\u00030\u00028F\u00a2\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"!\u0010\u0005\u001a\u00020\u0006\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\b8F\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\n\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006N"}, d2={"indices", "Lkotlin/ranges/IntRange;", "", "getIndices", "(Ljava/util/Collection;)Lkotlin/ranges/IntRange;", "lastIndex", "", "T", "", "getLastIndex", "(Ljava/util/List;)I", "List", "size", "init", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "index", "MutableList", "", "arrayListOf", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "elements", "", "([Ljava/lang/Object;)Ljava/util/ArrayList;", "buildList", "E", "capacity", "builderAction", "", "Lkotlin/ExtensionFunctionType;", "emptyList", "listOf", "([Ljava/lang/Object;)Ljava/util/List;", "listOfNotNull", "", "element", "(Ljava/lang/Object;)Ljava/util/List;", "mutableListOf", "rangeCheck", "fromIndex", "toIndex", "rangeCheck$CollectionsKt__CollectionsKt", "throwCountOverflow", "throwIndexOverflow", "asCollection", "([Ljava/lang/Object;)Ljava/util/Collection;", "binarySearch", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "(Ljava/util/List;Ljava/lang/Object;Ljava/util/Comparator;II)I", "comparison", "", "(Ljava/util/List;Ljava/lang/Comparable;II)I", "binarySearchBy", "K", "key", "selector", "(Ljava/util/List;Ljava/lang/Comparable;IILkotlin/jvm/functions/Function1;)I", "containsAll", "", "Lkotlin/internal/OnlyInputTypes;", "ifEmpty", "R", "C", "defaultValue", "Lkotlin/Function0;", "(Ljava/util/Collection;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "isNotEmpty", "isNullOrEmpty", "optimizeReadOnlyList", "orEmpty", "shuffled", "", "random", "Lkotlin/random/Random;", "kotlin-stdlib"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/collections/CollectionsKt")
class CollectionsKt__CollectionsKt
extends CollectionsKt__CollectionsJVMKt {
    private static final <T> List<T> List(int n, Function1<? super Integer, ? extends T> function1) {
        ArrayList<T> arrayList = new ArrayList<T>(n);
        for (int i = 0; i < n; ++i) {
            arrayList.add(function1.invoke(i));
        }
        return arrayList;
    }

    private static final <T> List<T> MutableList(int n, Function1<? super Integer, ? extends T> function1) {
        ArrayList<T> arrayList = new ArrayList<T>(n);
        for (int i = 0; i < n; ++i) {
            arrayList.add(function1.invoke(i));
        }
        return arrayList;
    }

    private static final <T> ArrayList<T> arrayListOf() {
        return new ArrayList();
    }

    public static final <T> ArrayList<T> arrayListOf(T ... object) {
        Intrinsics.checkNotNullParameter(object, "elements");
        object = ((T[])object).length == 0 ? new ArrayList() : new ArrayList((Collection)new ArrayAsCollection<T>((T[])object, true));
        return object;
    }

    public static final <T> Collection<T> asCollection(T[] TArray) {
        Intrinsics.checkNotNullParameter(TArray, "$this$asCollection");
        return new ArrayAsCollection<T>(TArray, false);
    }

    public static final <T> int binarySearch(List<? extends T> list, int n, int n2, Function1<? super T, Integer> function1) {
        Intrinsics.checkNotNullParameter(list, "$this$binarySearch");
        Intrinsics.checkNotNullParameter(function1, "comparison");
        CollectionsKt__CollectionsKt.rangeCheck$CollectionsKt__CollectionsKt(list.size(), n, n2);
        int n3 = n;
        n = n2 - 1;
        n2 = n3;
        while (n2 <= n) {
            n3 = n2 + n >>> 1;
            int n4 = ((Number)function1.invoke(list.get(n3))).intValue();
            if (n4 < 0) {
                n2 = n3 + 1;
                continue;
            }
            if (n4 > 0) {
                n = n3 - 1;
                continue;
            }
            return n3;
        }
        return -(n2 + 1);
    }

    public static final <T extends Comparable<? super T>> int binarySearch(List<? extends T> list, T t, int n, int n2) {
        Intrinsics.checkNotNullParameter(list, "$this$binarySearch");
        CollectionsKt__CollectionsKt.rangeCheck$CollectionsKt__CollectionsKt(list.size(), n, n2);
        int n3 = n;
        n = n2 - 1;
        n2 = n3;
        while (n2 <= n) {
            n3 = n2 + n >>> 1;
            int n4 = ComparisonsKt.compareValues((Comparable)list.get(n3), t);
            if (n4 < 0) {
                n2 = n3 + 1;
                continue;
            }
            if (n4 > 0) {
                n = n3 - 1;
                continue;
            }
            return n3;
        }
        return -(n2 + 1);
    }

    public static final <T> int binarySearch(List<? extends T> list, T t, Comparator<? super T> comparator, int n, int n2) {
        Intrinsics.checkNotNullParameter(list, "$this$binarySearch");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        CollectionsKt__CollectionsKt.rangeCheck$CollectionsKt__CollectionsKt(list.size(), n, n2);
        int n3 = n;
        n = n2 - 1;
        n2 = n3;
        while (n2 <= n) {
            int n4 = n2 + n >>> 1;
            n3 = comparator.compare(list.get(n4), t);
            if (n3 < 0) {
                n2 = n4 + 1;
                continue;
            }
            if (n3 > 0) {
                n = n4 - 1;
                continue;
            }
            return n4;
        }
        return -(n2 + 1);
    }

    public static /* synthetic */ int binarySearch$default(List list, int n, int n2, Function1 function1, int n3, Object object) {
        if ((n3 & 1) != 0) {
            n = 0;
        }
        if ((n3 & 2) != 0) {
            n2 = list.size();
        }
        return CollectionsKt.binarySearch(list, n, n2, function1);
    }

    public static /* synthetic */ int binarySearch$default(List list, Comparable comparable, int n, int n2, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n = 0;
        }
        if ((n3 & 4) != 0) {
            n2 = list.size();
        }
        return CollectionsKt.binarySearch(list, comparable, n, n2);
    }

    public static /* synthetic */ int binarySearch$default(List list, Object object, Comparator comparator, int n, int n2, int n3, Object object2) {
        if ((n3 & 4) != 0) {
            n = 0;
        }
        if ((n3 & 8) != 0) {
            n2 = list.size();
        }
        return CollectionsKt.binarySearch(list, object, comparator, n, n2);
    }

    public static final <T, K extends Comparable<? super K>> int binarySearchBy(List<? extends T> list, K k, int n, int n2, Function1<? super T, ? extends K> function1) {
        Intrinsics.checkNotNullParameter(list, "$this$binarySearchBy");
        Intrinsics.checkNotNullParameter(function1, "selector");
        return CollectionsKt.binarySearch(list, n, n2, new Function1<T, Integer>(function1, k){
            final Comparable $key;
            final Function1 $selector;
            {
                this.$selector = function1;
                this.$key = comparable;
                super(1);
            }

            public final int invoke(T t) {
                return ComparisonsKt.compareValues((Comparable)this.$selector.invoke(t), this.$key);
            }
        });
    }

    public static /* synthetic */ int binarySearchBy$default(List list, Comparable comparable, int n, int n2, Function1 function1, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n = 0;
        }
        if ((n3 & 4) != 0) {
            n2 = list.size();
        }
        Intrinsics.checkNotNullParameter(list, "$this$binarySearchBy");
        Intrinsics.checkNotNullParameter(function1, "selector");
        return CollectionsKt.binarySearch(list, n, n2, new /* invalid duplicate definition of identical inner class */);
    }

    private static final <E> List<E> buildList(int n, Function1<? super List<E>, Unit> function1) {
        List list = CollectionsKt.createListBuilder(n);
        function1.invoke(list);
        return CollectionsKt.build(list);
    }

    private static final <E> List<E> buildList(Function1<? super List<E>, Unit> function1) {
        List list = CollectionsKt.createListBuilder();
        function1.invoke(list);
        return CollectionsKt.build(list);
    }

    private static final <T> boolean containsAll(Collection<? extends T> collection, Collection<? extends T> collection2) {
        return collection.containsAll(collection2);
    }

    public static final <T> List<T> emptyList() {
        return EmptyList.INSTANCE;
    }

    public static final IntRange getIndices(Collection<?> collection) {
        Intrinsics.checkNotNullParameter(collection, "$this$indices");
        return new IntRange(0, collection.size() - 1);
    }

    public static final <T> int getLastIndex(List<? extends T> list) {
        Intrinsics.checkNotNullParameter(list, "$this$lastIndex");
        return list.size() - 1;
    }

    private static final <C extends Collection<?> & R, R> R ifEmpty(C object, Function0<? extends R> function0) {
        block0: {
            if (!object.isEmpty()) break block0;
            object = function0.invoke();
        }
        return object;
    }

    private static final <T> boolean isNotEmpty(Collection<? extends T> collection) {
        return collection.isEmpty() ^ true;
    }

    private static final <T> boolean isNullOrEmpty(Collection<? extends T> collection) {
        boolean bl = collection == null || collection.isEmpty();
        return bl;
    }

    private static final <T> List<T> listOf() {
        return CollectionsKt.emptyList();
    }

    public static final <T> List<T> listOf(T ... object) {
        Intrinsics.checkNotNullParameter(object, "elements");
        object = ((T[])object).length > 0 ? ArraysKt.asList(object) : CollectionsKt.emptyList();
        return object;
    }

    public static final <T> List<T> listOfNotNull(T object) {
        object = object != null ? CollectionsKt.listOf(object) : CollectionsKt.emptyList();
        return object;
    }

    public static final <T> List<T> listOfNotNull(T ... TArray) {
        Intrinsics.checkNotNullParameter(TArray, "elements");
        return ArraysKt.filterNotNull(TArray);
    }

    private static final <T> List<T> mutableListOf() {
        return new ArrayList();
    }

    public static final <T> List<T> mutableListOf(T ... object) {
        Intrinsics.checkNotNullParameter(object, "elements");
        object = ((T[])object).length == 0 ? new ArrayList() : new ArrayList((Collection)new ArrayAsCollection<T>((T[])object, true));
        return (List)object;
    }

    public static final <T> List<T> optimizeReadOnlyList(List<? extends T> list) {
        Intrinsics.checkNotNullParameter(list, "$this$optimizeReadOnlyList");
        switch (list.size()) {
            default: {
                break;
            }
            case 1: {
                list = CollectionsKt.listOf(list.get(0));
                break;
            }
            case 0: {
                list = CollectionsKt.emptyList();
            }
        }
        return list;
    }

    private static final <T> Collection<T> orEmpty(Collection<? extends T> collection) {
        if (collection == null) {
            collection = CollectionsKt.emptyList();
        }
        return collection;
    }

    private static final <T> List<T> orEmpty(List<? extends T> list) {
        if (list == null) {
            list = CollectionsKt.emptyList();
        }
        return list;
    }

    private static final void rangeCheck$CollectionsKt__CollectionsKt(int n, int n2, int n3) {
        if (n2 <= n3) {
            if (n2 >= 0) {
                if (n3 <= n) {
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("toIndex (");
                stringBuilder.append(n3);
                stringBuilder.append(") is greater than size (");
                stringBuilder.append(n);
                stringBuilder.append(").");
                throw (Throwable)new IndexOutOfBoundsException(stringBuilder.toString());
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("fromIndex (");
            stringBuilder.append(n2);
            stringBuilder.append(") is less than zero.");
            throw (Throwable)new IndexOutOfBoundsException(stringBuilder.toString());
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("fromIndex (");
        stringBuilder.append(n2);
        stringBuilder.append(") is greater than toIndex (");
        stringBuilder.append(n3);
        stringBuilder.append(").");
        throw (Throwable)new IllegalArgumentException(stringBuilder.toString());
    }

    public static final <T> List<T> shuffled(Iterable<? extends T> iterable, Random random) {
        Intrinsics.checkNotNullParameter(iterable, "$this$shuffled");
        Intrinsics.checkNotNullParameter(random, "random");
        iterable = CollectionsKt.toMutableList(iterable);
        CollectionsKt.shuffle(iterable, random);
        return iterable;
    }

    public static final void throwCountOverflow() {
        throw (Throwable)new ArithmeticException("Count overflow has happened.");
    }

    public static final void throwIndexOverflow() {
        throw (Throwable)new ArithmeticException("Index overflow has happened.");
    }
}

