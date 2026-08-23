/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections.builders;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.collections.builders.ListBuilderKt;
import kotlin.collections.builders.MapBuilderEntries;
import kotlin.collections.builders.MapBuilderKeys;
import kotlin.collections.builders.MapBuilderValues;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMutableIterator;
import kotlin.jvm.internal.markers.KMutableMap;
import kotlin.ranges.RangesKt;

@Metadata(bv={1, 0, 3}, d1={"\u0000\u00a0\u0001\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u0015\n\u0002\b\b\n\u0002\u0010#\n\u0002\u0010'\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u001f\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010$\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u001e\n\u0002\b\u0003\n\u0002\u0010&\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0018\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\b\u0000\u0018\u0000 v*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u00022\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u0003:\u0006vwxyz{B\u0007\b\u0016\u00a2\u0006\u0002\u0010\u0004B\u000f\b\u0016\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007BE\b\u0002\u0012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\t\u0012\u000e\u0010\n\u001a\n\u0012\u0004\u0012\u00028\u0001\u0018\u00010\t\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\f\u0012\u0006\u0010\u000e\u001a\u00020\u0006\u0012\u0006\u0010\u000f\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0010J\u0017\u0010.\u001a\u00020\u00062\u0006\u0010/\u001a\u00028\u0000H\u0000\u00a2\u0006\u0004\b0\u00101J\u0013\u00102\u001a\b\u0012\u0004\u0012\u00028\u00010\tH\u0002\u00a2\u0006\u0002\u00103J\u0012\u00104\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u000105J\r\u00106\u001a\u000207H\u0000\u00a2\u0006\u0002\b8J\b\u00109\u001a\u000207H\u0016J\b\u0010:\u001a\u000207H\u0002J\u0019\u0010;\u001a\u00020\u001f2\n\u0010<\u001a\u0006\u0012\u0002\b\u00030=H\u0000\u00a2\u0006\u0002\b>J!\u0010?\u001a\u00020\u001f2\u0012\u0010@\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010AH\u0000\u00a2\u0006\u0002\bBJ\u0015\u0010C\u001a\u00020\u001f2\u0006\u0010/\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u0010DJ\u0015\u0010E\u001a\u00020\u001f2\u0006\u0010F\u001a\u00028\u0001H\u0016\u00a2\u0006\u0002\u0010DJ\u0018\u0010G\u001a\u00020\u001f2\u000e\u0010H\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u000305H\u0002J\u0010\u0010I\u001a\u0002072\u0006\u0010\u0011\u001a\u00020\u0006H\u0002J\u0010\u0010J\u001a\u0002072\u0006\u0010K\u001a\u00020\u0006H\u0002J\u0019\u0010L\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010MH\u0000\u00a2\u0006\u0002\bNJ\u0013\u0010O\u001a\u00020\u001f2\b\u0010H\u001a\u0004\u0018\u00010PH\u0096\u0002J\u0015\u0010Q\u001a\u00020\u00062\u0006\u0010/\u001a\u00028\u0000H\u0002\u00a2\u0006\u0002\u00101J\u0015\u0010R\u001a\u00020\u00062\u0006\u0010F\u001a\u00028\u0001H\u0002\u00a2\u0006\u0002\u00101J\u0018\u0010S\u001a\u0004\u0018\u00018\u00012\u0006\u0010/\u001a\u00028\u0000H\u0096\u0002\u00a2\u0006\u0002\u0010TJ\u0015\u0010U\u001a\u00020\u00062\u0006\u0010/\u001a\u00028\u0000H\u0002\u00a2\u0006\u0002\u00101J\b\u0010V\u001a\u00020\u0006H\u0016J\b\u0010W\u001a\u00020\u001fH\u0016J\u0019\u0010X\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010YH\u0000\u00a2\u0006\u0002\bZJ\u001f\u0010[\u001a\u0004\u0018\u00018\u00012\u0006\u0010/\u001a\u00028\u00002\u0006\u0010F\u001a\u00028\u0001H\u0016\u00a2\u0006\u0002\u0010\\J\u001e\u0010]\u001a\u0002072\u0014\u0010^\u001a\u0010\u0012\u0006\b\u0001\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u000105H\u0016J\"\u0010_\u001a\u00020\u001f2\u0018\u0010^\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010A0=H\u0002J\u001c\u0010`\u001a\u00020\u001f2\u0012\u0010@\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010AH\u0002J\u0010\u0010a\u001a\u00020\u001f2\u0006\u0010b\u001a\u00020\u0006H\u0002J\u0010\u0010c\u001a\u0002072\u0006\u0010d\u001a\u00020\u0006H\u0002J\u0017\u0010e\u001a\u0004\u0018\u00018\u00012\u0006\u0010/\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u0010TJ!\u0010f\u001a\u00020\u001f2\u0012\u0010@\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010AH\u0000\u00a2\u0006\u0002\bgJ\u0010\u0010h\u001a\u0002072\u0006\u0010i\u001a\u00020\u0006H\u0002J\u0017\u0010j\u001a\u00020\u00062\u0006\u0010/\u001a\u00028\u0000H\u0000\u00a2\u0006\u0004\bk\u00101J\u0010\u0010l\u001a\u0002072\u0006\u0010m\u001a\u00020\u0006H\u0002J\u0017\u0010n\u001a\u00020\u001f2\u0006\u0010o\u001a\u00028\u0001H\u0000\u00a2\u0006\u0004\bp\u0010DJ\b\u0010q\u001a\u00020rH\u0016J\u0019\u0010s\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010tH\u0000\u00a2\u0006\u0002\buR\u0014\u0010\u0011\u001a\u00020\u00068BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R&\u0010\u0014\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u00160\u00158VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0017\u0010\u0018R\u001c\u0010\u0019\u001a\u0010\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u0001\u0018\u00010\u001aX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001c\u001a\u00020\u00068BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001d\u0010\u0013R\u000e\u0010\u001e\u001a\u00020\u001fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010 \u001a\b\u0012\u0004\u0012\u00028\u00000\u00158VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b!\u0010\u0018R\u0016\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\tX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\"R\u0016\u0010#\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010$X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010&\u001a\u00020\u00062\u0006\u0010%\u001a\u00020\u0006@RX\u0096\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b'\u0010\u0013R\u001a\u0010(\u001a\b\u0012\u0004\u0012\u00028\u00010)8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b*\u0010+R\u0018\u0010\n\u001a\n\u0012\u0004\u0012\u00028\u0001\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\"R\u0016\u0010,\u001a\n\u0012\u0004\u0012\u00028\u0001\u0018\u00010-X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006|"}, d2={"Lkotlin/collections/builders/MapBuilder;", "K", "V", "", "()V", "initialCapacity", "", "(I)V", "keysArray", "", "valuesArray", "presenceArray", "", "hashArray", "maxProbeDistance", "length", "([Ljava/lang/Object;[Ljava/lang/Object;[I[III)V", "capacity", "getCapacity", "()I", "entries", "", "", "getEntries", "()Ljava/util/Set;", "entriesView", "Lkotlin/collections/builders/MapBuilderEntries;", "hashShift", "hashSize", "getHashSize", "isReadOnly", "", "keys", "getKeys", "[Ljava/lang/Object;", "keysView", "Lkotlin/collections/builders/MapBuilderKeys;", "<set-?>", "size", "getSize", "values", "", "getValues", "()Ljava/util/Collection;", "valuesView", "Lkotlin/collections/builders/MapBuilderValues;", "addKey", "key", "addKey$kotlin_stdlib", "(Ljava/lang/Object;)I", "allocateValuesArray", "()[Ljava/lang/Object;", "build", "", "checkIsMutable", "", "checkIsMutable$kotlin_stdlib", "clear", "compact", "containsAllEntries", "m", "", "containsAllEntries$kotlin_stdlib", "containsEntry", "entry", "", "containsEntry$kotlin_stdlib", "containsKey", "(Ljava/lang/Object;)Z", "containsValue", "value", "contentEquals", "other", "ensureCapacity", "ensureExtraCapacity", "n", "entriesIterator", "Lkotlin/collections/builders/MapBuilder$EntriesItr;", "entriesIterator$kotlin_stdlib", "equals", "", "findKey", "findValue", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", "hash", "hashCode", "isEmpty", "keysIterator", "Lkotlin/collections/builders/MapBuilder$KeysItr;", "keysIterator$kotlin_stdlib", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "putAll", "from", "putAllEntries", "putEntry", "putRehash", "i", "rehash", "newHashSize", "remove", "removeEntry", "removeEntry$kotlin_stdlib", "removeHashAt", "removedHash", "removeKey", "removeKey$kotlin_stdlib", "removeKeyAt", "index", "removeValue", "element", "removeValue$kotlin_stdlib", "toString", "", "valuesIterator", "Lkotlin/collections/builders/MapBuilder$ValuesItr;", "valuesIterator$kotlin_stdlib", "Companion", "EntriesItr", "EntryRef", "Itr", "KeysItr", "ValuesItr", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class MapBuilder<K, V>
implements Map<K, V>,
KMutableMap {
    private static final Companion Companion = new Companion(null);
    @Deprecated
    private static final int INITIAL_CAPACITY = 8;
    @Deprecated
    private static final int INITIAL_MAX_PROBE_DISTANCE = 2;
    @Deprecated
    private static final int MAGIC = -1640531527;
    @Deprecated
    private static final int TOMBSTONE = -1;
    private MapBuilderEntries<K, V> entriesView;
    private int[] hashArray;
    private int hashShift;
    private boolean isReadOnly;
    private K[] keysArray;
    private MapBuilderKeys<K> keysView;
    private int length;
    private int maxProbeDistance;
    private int[] presenceArray;
    private int size;
    private V[] valuesArray;
    private MapBuilderValues<V> valuesView;

    public MapBuilder() {
        this(8);
    }

    public MapBuilder(int n) {
        E[] EArray = ListBuilderKt.arrayOfUninitializedElements(n);
        int[] nArray = new int[n];
        int[] nArray2 = new int[MapBuilder.Companion.computeHashSize(n)];
        this(EArray, null, nArray, nArray2, 2, 0);
    }

    private MapBuilder(K[] KArray, V[] VArray, int[] nArray, int[] nArray2, int n, int n2) {
        this.keysArray = KArray;
        this.valuesArray = VArray;
        this.presenceArray = nArray;
        this.hashArray = nArray2;
        this.maxProbeDistance = n;
        this.length = n2;
        this.hashShift = MapBuilder.Companion.computeShift(this.getHashSize());
    }

    public static final /* synthetic */ void access$setKeysArray$p(MapBuilder mapBuilder, Object[] objectArray) {
        mapBuilder.keysArray = objectArray;
    }

    public static final /* synthetic */ void access$setLength$p(MapBuilder mapBuilder, int n) {
        mapBuilder.length = n;
    }

    public static final /* synthetic */ void access$setPresenceArray$p(MapBuilder mapBuilder, int[] nArray) {
        mapBuilder.presenceArray = nArray;
    }

    public static final /* synthetic */ void access$setValuesArray$p(MapBuilder mapBuilder, Object[] objectArray) {
        mapBuilder.valuesArray = objectArray;
    }

    private final V[] allocateValuesArray() {
        Object[] objectArray = this.valuesArray;
        if (objectArray != null) {
            return objectArray;
        }
        this.valuesArray = objectArray = ListBuilderKt.arrayOfUninitializedElements(this.getCapacity());
        return objectArray;
    }

    private final void compact() {
        int n;
        int n2 = 0;
        V[] VArray = this.valuesArray;
        for (int i = 0; i < (n = this.length); ++i) {
            n = n2;
            if (this.presenceArray[i] >= 0) {
                K[] KArray = this.keysArray;
                KArray[n2] = KArray[i];
                if (VArray != null) {
                    VArray[n2] = VArray[i];
                }
                n = n2 + 1;
            }
            n2 = n;
        }
        ListBuilderKt.resetRange(this.keysArray, n2, n);
        if (VArray != null) {
            ListBuilderKt.resetRange(VArray, n2, this.length);
        }
        this.length = n2;
    }

    private final boolean contentEquals(Map<?, ?> map) {
        boolean bl = this.size() == map.size() && this.containsAllEntries$kotlin_stdlib((Collection)map.entrySet());
        return bl;
    }

    private final void ensureCapacity(int n) {
        if (n > this.getCapacity()) {
            int n2;
            int n3 = n2 = this.getCapacity() * 3 / 2;
            if (n > n2) {
                n3 = n;
            }
            this.keysArray = ListBuilderKt.copyOfUninitializedElements(this.keysArray, n3);
            Object object = this.valuesArray;
            object = object != null ? ListBuilderKt.copyOfUninitializedElements(object, n3) : null;
            this.valuesArray = object;
            object = Arrays.copyOf(this.presenceArray, n3);
            Intrinsics.checkNotNullExpressionValue(object, "java.util.Arrays.copyOf(this, newSize)");
            this.presenceArray = (int[])object;
            n = MapBuilder.Companion.computeHashSize(n3);
            if (n > this.getHashSize()) {
                this.rehash(n);
            }
        } else if (this.length + n - this.size() > this.getCapacity()) {
            this.rehash(this.getHashSize());
        }
    }

    private final void ensureExtraCapacity(int n) {
        this.ensureCapacity(this.length + n);
    }

    private final int findKey(K k) {
        int n = this.hash(k);
        int n2 = this.maxProbeDistance;
        int n3;
        while ((n3 = this.hashArray[n]) != 0) {
            if (n3 > 0 && Intrinsics.areEqual(this.keysArray[n3 - 1], k)) {
                return n3 - 1;
            }
            n3 = n2 - 1;
            if (n3 < 0) {
                return -1;
            }
            n2 = n == 0 ? this.getHashSize() - 1 : n - 1;
            n = n2;
            n2 = n3;
        }
        return -1;
    }

    private final int findValue(V v) {
        int n = this.length;
        while (--n >= 0) {
            if (this.presenceArray[n] < 0) continue;
            V[] VArray = this.valuesArray;
            Intrinsics.checkNotNull(VArray);
            if (!Intrinsics.areEqual(VArray[n], v)) continue;
            return n;
        }
        return -1;
    }

    private final int getCapacity() {
        return this.keysArray.length;
    }

    private final int getHashSize() {
        return this.hashArray.length;
    }

    private final int hash(K k) {
        int n = k != null ? k.hashCode() : 0;
        return n * -1640531527 >>> this.hashShift;
    }

    private final boolean putAllEntries(Collection<? extends Map.Entry<? extends K, ? extends V>> object) {
        if (object.isEmpty()) {
            return false;
        }
        this.ensureExtraCapacity(object.size());
        object = object.iterator();
        boolean bl = false;
        while (object.hasNext()) {
            if (!this.putEntry((Map.Entry)object.next())) continue;
            bl = true;
        }
        return bl;
    }

    private final boolean putEntry(Map.Entry<? extends K, ? extends V> entry) {
        int n = this.addKey$kotlin_stdlib(entry.getKey());
        V[] VArray = this.allocateValuesArray();
        if (n >= 0) {
            VArray[n] = entry.getValue();
            return true;
        }
        V v = VArray[-n - 1];
        if (Intrinsics.areEqual(entry.getValue(), v) ^ true) {
            VArray[-n - 1] = entry.getValue();
            return true;
        }
        return false;
    }

    private final boolean putRehash(int n) {
        int n2 = this.hash(this.keysArray[n]);
        int n3 = this.maxProbeDistance;
        while (true) {
            int[] nArray;
            if ((nArray = this.hashArray)[n2] == 0) {
                nArray[n2] = n + 1;
                this.presenceArray[n] = n2;
                return true;
            }
            int n4 = n3 - 1;
            if (n4 < 0) {
                return false;
            }
            n3 = n2 == 0 ? this.getHashSize() - 1 : n2 - 1;
            n2 = n3;
            n3 = n4;
        }
    }

    private final void rehash(int n) {
        if (this.length > this.size()) {
            this.compact();
        }
        if (n != this.getHashSize()) {
            this.hashArray = new int[n];
            this.hashShift = MapBuilder.Companion.computeShift(n);
        } else {
            ArraysKt.fill(this.hashArray, 0, 0, this.getHashSize());
        }
        for (n = 0; n < this.length; ++n) {
            if (this.putRehash(n)) {
                continue;
            }
            throw (Throwable)new IllegalStateException("This cannot happen with fixed magic multiplier and grow-only hash array. Have object hashCodes changed?");
        }
    }

    private final void removeHashAt(int n) {
        int n2 = n;
        int n3 = 0;
        int n4 = RangesKt.coerceAtMost(this.maxProbeDistance * 2, this.getHashSize() / 2);
        int n5 = n;
        n = n2;
        while (true) {
            n = n == 0 ? this.getHashSize() - 1 : --n;
            int n6 = n3 + 1;
            if (n6 > this.maxProbeDistance) {
                this.hashArray[n5] = 0;
                return;
            }
            int[] nArray = this.hashArray;
            int n7 = nArray[n];
            if (n7 == 0) {
                nArray[n5] = 0;
                return;
            }
            if (n7 < 0) {
                nArray[n5] = -1;
                n2 = n;
                n3 = 0;
            } else {
                n2 = n5;
                n3 = n6;
                if ((this.hash(this.keysArray[n7 - 1]) - n & this.getHashSize() - 1) >= n6) {
                    this.hashArray[n5] = n7;
                    this.presenceArray[n7 - 1] = n5;
                    n2 = n;
                    n3 = 0;
                }
            }
            if (--n4 < 0) {
                this.hashArray[n2] = -1;
                return;
            }
            n5 = n2;
        }
    }

    private final void removeKeyAt(int n) {
        ListBuilderKt.resetAt(this.keysArray, n);
        this.removeHashAt(this.presenceArray[n]);
        this.presenceArray[n] = -1;
        this.size = this.size() - 1;
    }

    public final int addKey$kotlin_stdlib(K k) {
        this.checkIsMutable$kotlin_stdlib();
        block0: while (true) {
            int n = this.hash(k);
            int n2 = RangesKt.coerceAtMost(this.maxProbeDistance * 2, this.getHashSize() / 2);
            int n3 = 0;
            while (true) {
                int n4;
                if ((n4 = this.hashArray[n]) <= 0) {
                    if (this.length >= this.getCapacity()) {
                        this.ensureExtraCapacity(1);
                        continue block0;
                    }
                    n4 = this.length;
                    this.length = n4 + 1;
                    this.keysArray[n4] = k;
                    this.presenceArray[n4] = n;
                    this.hashArray[n] = n4 + 1;
                    this.size = this.size() + 1;
                    if (n3 > this.maxProbeDistance) {
                        this.maxProbeDistance = n3;
                    }
                    return n4;
                }
                if (Intrinsics.areEqual(this.keysArray[n4 - 1], k)) {
                    return -n4;
                }
                n4 = n3 + 1;
                if (n4 > n2) {
                    this.rehash(this.getHashSize() * 2);
                    continue block0;
                }
                n3 = n == 0 ? this.getHashSize() - 1 : n - 1;
                n = n3;
                n3 = n4;
            }
            break;
        }
    }

    public final Map<K, V> build() {
        this.checkIsMutable$kotlin_stdlib();
        this.isReadOnly = true;
        return this;
    }

    public final void checkIsMutable$kotlin_stdlib() {
        if (!this.isReadOnly) {
            return;
        }
        throw (Throwable)new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        Object[] objectArray;
        this.checkIsMutable$kotlin_stdlib();
        int n = this.length - 1;
        if (n >= 0) {
            int n2 = 0;
            while (true) {
                int n3;
                if ((n3 = (objectArray = this.presenceArray)[n2]) >= 0) {
                    this.hashArray[n3] = 0;
                    objectArray[n2] = -1;
                }
                if (n2 == n) break;
                ++n2;
            }
        }
        ListBuilderKt.resetRange(this.keysArray, 0, this.length);
        objectArray = this.valuesArray;
        if (objectArray != null) {
            ListBuilderKt.resetRange(objectArray, 0, this.length);
        }
        this.size = 0;
        this.length = 0;
    }

    public final boolean containsAllEntries$kotlin_stdlib(Collection<?> collection2) {
        Intrinsics.checkNotNullParameter(collection2, "m");
        for (Collection<?> collection2 : collection2) {
            if (collection2 != null) {
                try {
                    boolean bl = this.containsEntry$kotlin_stdlib((Map.Entry)((Object)collection2));
                    if (bl) continue;
                }
                catch (ClassCastException classCastException) {
                    return false;
                }
            }
            return false;
        }
        return true;
    }

    public final boolean containsEntry$kotlin_stdlib(Map.Entry<? extends K, ? extends V> entry) {
        Intrinsics.checkNotNullParameter(entry, "entry");
        int n = this.findKey(entry.getKey());
        if (n < 0) {
            return false;
        }
        V[] VArray = this.valuesArray;
        Intrinsics.checkNotNull(VArray);
        return Intrinsics.areEqual(VArray[n], entry.getValue());
    }

    @Override
    public boolean containsKey(Object object) {
        boolean bl = this.findKey(object) >= 0;
        return bl;
    }

    @Override
    public boolean containsValue(Object object) {
        boolean bl = this.findValue(object) >= 0;
        return bl;
    }

    public final EntriesItr<K, V> entriesIterator$kotlin_stdlib() {
        return new EntriesItr(this);
    }

    @Override
    public boolean equals(Object object) {
        boolean bl = object == this || object instanceof Map && this.contentEquals((Map)object);
        return bl;
    }

    @Override
    public V get(Object VArray) {
        int n = this.findKey(VArray);
        if (n < 0) {
            return null;
        }
        VArray = this.valuesArray;
        Intrinsics.checkNotNull(VArray);
        return VArray[n];
    }

    public Set<Map.Entry<K, V>> getEntries() {
        MapBuilderEntries<K, V> mapBuilderEntries = this.entriesView;
        if (mapBuilderEntries == null) {
            this.entriesView = mapBuilderEntries = new MapBuilderEntries(this);
            return mapBuilderEntries;
        }
        return mapBuilderEntries;
    }

    public Set<K> getKeys() {
        Set<Object> set = this.keysView;
        if (set == null) {
            this.keysView = set = new MapBuilderKeys(this);
            set = set;
        } else {
            set = set;
        }
        return set;
    }

    public int getSize() {
        return this.size;
    }

    public Collection<V> getValues() {
        Collection<V> collection = this.valuesView;
        if (collection == null) {
            this.valuesView = collection = new MapBuilderValues(this);
            collection = collection;
        } else {
            collection = collection;
        }
        return collection;
    }

    @Override
    public int hashCode() {
        int n = 0;
        EntriesItr<K, V> entriesItr = this.entriesIterator$kotlin_stdlib();
        while (entriesItr.hasNext()) {
            n += entriesItr.nextHashCode$kotlin_stdlib();
        }
        return n;
    }

    @Override
    public boolean isEmpty() {
        boolean bl = this.size() == 0;
        return bl;
    }

    public final KeysItr<K, V> keysIterator$kotlin_stdlib() {
        return new KeysItr(this);
    }

    @Override
    public V put(K object, V v) {
        this.checkIsMutable$kotlin_stdlib();
        int n = this.addKey$kotlin_stdlib(object);
        object = this.allocateValuesArray();
        if (n < 0) {
            K k = object[-n - 1];
            object[-n - 1] = v;
            return (V)k;
        }
        object[n] = v;
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        Intrinsics.checkNotNullParameter(map, "from");
        this.checkIsMutable$kotlin_stdlib();
        this.putAllEntries((Collection)map.entrySet());
    }

    @Override
    public V remove(Object VArray) {
        int n = this.removeKey$kotlin_stdlib(VArray);
        if (n < 0) {
            return null;
        }
        VArray = this.valuesArray;
        Intrinsics.checkNotNull(VArray);
        V v = VArray[n];
        ListBuilderKt.resetAt(VArray, n);
        return v;
    }

    public final boolean removeEntry$kotlin_stdlib(Map.Entry<? extends K, ? extends V> entry) {
        Intrinsics.checkNotNullParameter(entry, "entry");
        this.checkIsMutable$kotlin_stdlib();
        int n = this.findKey(entry.getKey());
        if (n < 0) {
            return false;
        }
        V[] VArray = this.valuesArray;
        Intrinsics.checkNotNull(VArray);
        if (Intrinsics.areEqual(VArray[n], entry.getValue()) ^ true) {
            return false;
        }
        this.removeKeyAt(n);
        return true;
    }

    public final int removeKey$kotlin_stdlib(K k) {
        this.checkIsMutable$kotlin_stdlib();
        int n = this.findKey(k);
        if (n < 0) {
            return -1;
        }
        this.removeKeyAt(n);
        return n;
    }

    public final boolean removeValue$kotlin_stdlib(V v) {
        this.checkIsMutable$kotlin_stdlib();
        int n = this.findValue(v);
        if (n < 0) {
            return false;
        }
        this.removeKeyAt(n);
        return true;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(this.size() * 3 + 2);
        stringBuilder.append("{");
        int n = 0;
        Object object = this.entriesIterator$kotlin_stdlib();
        while (((Itr)object).hasNext()) {
            if (n > 0) {
                stringBuilder.append(", ");
            }
            ((EntriesItr)object).nextAppendString(stringBuilder);
            ++n;
        }
        stringBuilder.append("}");
        object = stringBuilder.toString();
        Intrinsics.checkNotNullExpressionValue(object, "sb.toString()");
        return object;
    }

    public final ValuesItr<K, V> valuesIterator$kotlin_stdlib() {
        return new ValuesItr(this);
    }

    @Metadata(bv={1, 0, 3}, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004H\u0002J\u0010\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2={"Lkotlin/collections/builders/MapBuilder$Companion;", "", "()V", "INITIAL_CAPACITY", "", "INITIAL_MAX_PROBE_DISTANCE", "MAGIC", "TOMBSTONE", "computeHashSize", "capacity", "computeShift", "hashSize", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
    private static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private final int computeHashSize(int n) {
            return Integer.highestOneBit(RangesKt.coerceAtLeast(n, 1) * 3);
        }

        private final int computeShift(int n) {
            return Integer.numberOfLeadingZeros(n) + 1;
        }
    }

    @Metadata(bv={1, 0, 3}, d1={"\u0000<\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010)\n\u0002\u0010'\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0000\u0018\u0000*\u0004\b\u0002\u0010\u0001*\u0004\b\u0003\u0010\u00022\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u00032\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u00050\u0004B\u0019\u0012\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00028\u0002\u0012\u0004\u0012\u00028\u00030\u0007\u00a2\u0006\u0002\u0010\bJ\u0015\u0010\t\u001a\u000e\u0012\u0004\u0012\u00028\u0002\u0012\u0004\u0012\u00028\u00030\nH\u0096\u0002J\u0012\u0010\u000b\u001a\u00020\f2\n\u0010\r\u001a\u00060\u000ej\u0002`\u000fJ\r\u0010\u0010\u001a\u00020\u0011H\u0000\u00a2\u0006\u0002\b\u0012\u00a8\u0006\u0013"}, d2={"Lkotlin/collections/builders/MapBuilder$EntriesItr;", "K", "V", "Lkotlin/collections/builders/MapBuilder$Itr;", "", "", "map", "Lkotlin/collections/builders/MapBuilder;", "(Lkotlin/collections/builders/MapBuilder;)V", "next", "Lkotlin/collections/builders/MapBuilder$EntryRef;", "nextAppendString", "", "sb", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "nextHashCode", "", "nextHashCode$kotlin_stdlib", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
    public static final class EntriesItr<K, V>
    extends Itr<K, V>
    implements Iterator<Map.Entry<K, V>>,
    KMutableIterator {
        public EntriesItr(MapBuilder<K, V> mapBuilder) {
            Intrinsics.checkNotNullParameter(mapBuilder, "map");
            super(mapBuilder);
        }

        @Override
        public EntryRef<K, V> next() {
            if (this.getIndex$kotlin_stdlib() < this.getMap$kotlin_stdlib().length) {
                int n = this.getIndex$kotlin_stdlib();
                this.setIndex$kotlin_stdlib(n + 1);
                this.setLastIndex$kotlin_stdlib(n);
                EntryRef entryRef = new EntryRef(this.getMap$kotlin_stdlib(), this.getLastIndex$kotlin_stdlib());
                this.initNext$kotlin_stdlib();
                return entryRef;
            }
            throw (Throwable)new NoSuchElementException();
        }

        public final void nextAppendString(StringBuilder stringBuilder) {
            Intrinsics.checkNotNullParameter(stringBuilder, "sb");
            if (this.getIndex$kotlin_stdlib() < this.getMap$kotlin_stdlib().length) {
                int n = this.getIndex$kotlin_stdlib();
                this.setIndex$kotlin_stdlib(n + 1);
                this.setLastIndex$kotlin_stdlib(n);
                Object object = this.getMap$kotlin_stdlib().keysArray[this.getLastIndex$kotlin_stdlib()];
                if (Intrinsics.areEqual(object, this.getMap$kotlin_stdlib())) {
                    stringBuilder.append("(this Map)");
                } else {
                    stringBuilder.append(object);
                }
                stringBuilder.append('=');
                object = this.getMap$kotlin_stdlib().valuesArray;
                Intrinsics.checkNotNull(object);
                object = object[this.getLastIndex$kotlin_stdlib()];
                if (Intrinsics.areEqual(object, this.getMap$kotlin_stdlib())) {
                    stringBuilder.append("(this Map)");
                } else {
                    stringBuilder.append(object);
                }
                this.initNext$kotlin_stdlib();
                return;
            }
            throw (Throwable)new NoSuchElementException();
        }

        public final int nextHashCode$kotlin_stdlib() {
            if (this.getIndex$kotlin_stdlib() < this.getMap$kotlin_stdlib().length) {
                int n = this.getIndex$kotlin_stdlib();
                this.setIndex$kotlin_stdlib(n + 1);
                this.setLastIndex$kotlin_stdlib(n);
                Object object = this.getMap$kotlin_stdlib().keysArray[this.getLastIndex$kotlin_stdlib()];
                int n2 = 0;
                n = object != null ? object.hashCode() : 0;
                object = this.getMap$kotlin_stdlib().valuesArray;
                Intrinsics.checkNotNull(object);
                object = object[this.getLastIndex$kotlin_stdlib()];
                if (object != null) {
                    n2 = object.hashCode();
                }
                this.initNext$kotlin_stdlib();
                return n ^ n2;
            }
            throw (Throwable)new NoSuchElementException();
        }
    }

    @Metadata(bv={1, 0, 3}, d1={"\u00000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010'\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\b\u0000\u0018\u0000*\u0004\b\u0002\u0010\u0001*\u0004\b\u0003\u0010\u00022\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u0003B!\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0002\u0012\u0004\u0012\u00028\u00030\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0096\u0002J\b\u0010\u0012\u001a\u00020\u0007H\u0016J\u0015\u0010\u0013\u001a\u00028\u00032\u0006\u0010\u0014\u001a\u00028\u0003H\u0016\u00a2\u0006\u0002\u0010\u0015J\b\u0010\u0016\u001a\u00020\u0017H\u0016R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00028\u00028VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0002\u0012\u0004\u0012\u00028\u00030\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u00028\u00038VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u000b\u00a8\u0006\u0018"}, d2={"Lkotlin/collections/builders/MapBuilder$EntryRef;", "K", "V", "", "map", "Lkotlin/collections/builders/MapBuilder;", "index", "", "(Lkotlin/collections/builders/MapBuilder;I)V", "key", "getKey", "()Ljava/lang/Object;", "value", "getValue", "equals", "", "other", "", "hashCode", "setValue", "newValue", "(Ljava/lang/Object;)Ljava/lang/Object;", "toString", "", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
    public static final class EntryRef<K, V>
    implements Map.Entry<K, V>,
    KMutableMap.Entry {
        private final int index;
        private final MapBuilder<K, V> map;

        public EntryRef(MapBuilder<K, V> mapBuilder, int n) {
            Intrinsics.checkNotNullParameter(mapBuilder, "map");
            this.map = mapBuilder;
            this.index = n;
        }

        @Override
        public boolean equals(Object object) {
            boolean bl = object instanceof Map.Entry && Intrinsics.areEqual(((Map.Entry)object).getKey(), this.getKey()) && Intrinsics.areEqual(((Map.Entry)object).getValue(), this.getValue());
            return bl;
        }

        @Override
        public K getKey() {
            return (K)((MapBuilder)this.map).keysArray[this.index];
        }

        @Override
        public V getValue() {
            Object[] objectArray = ((MapBuilder)this.map).valuesArray;
            Intrinsics.checkNotNull(objectArray);
            return (V)objectArray[this.index];
        }

        @Override
        public int hashCode() {
            Object object = this.getKey();
            int n = 0;
            int n2 = object != null ? object.hashCode() : 0;
            object = this.getValue();
            if (object != null) {
                n = object.hashCode();
            }
            return n2 ^ n;
        }

        @Override
        public V setValue(V v) {
            this.map.checkIsMutable$kotlin_stdlib();
            Object[] objectArray = ((MapBuilder)this.map).allocateValuesArray();
            int n = this.index;
            Object object = objectArray[n];
            objectArray[n] = v;
            return (V)object;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.getKey());
            stringBuilder.append('=');
            stringBuilder.append(this.getValue());
            return stringBuilder.toString();
        }
    }

    @Metadata(bv={1, 0, 3}, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0010\u0018\u0000*\u0004\b\u0002\u0010\u0001*\u0004\b\u0003\u0010\u00022\u00020\u0003B\u0019\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0002\u0012\u0004\u0012\u00028\u00030\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u0012\u001a\u00020\u0013J\r\u0010\u0014\u001a\u00020\u0015H\u0000\u00a2\u0006\u0002\b\u0016J\u0006\u0010\u0017\u001a\u00020\u0015R\u001a\u0010\u0007\u001a\u00020\bX\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u00020\bX\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\n\"\u0004\b\u000f\u0010\fR \u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0002\u0012\u0004\u0012\u00028\u00030\u0005X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006\u0018"}, d2={"Lkotlin/collections/builders/MapBuilder$Itr;", "K", "V", "", "map", "Lkotlin/collections/builders/MapBuilder;", "(Lkotlin/collections/builders/MapBuilder;)V", "index", "", "getIndex$kotlin_stdlib", "()I", "setIndex$kotlin_stdlib", "(I)V", "lastIndex", "getLastIndex$kotlin_stdlib", "setLastIndex$kotlin_stdlib", "getMap$kotlin_stdlib", "()Lkotlin/collections/builders/MapBuilder;", "hasNext", "", "initNext", "", "initNext$kotlin_stdlib", "remove", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
    public static class Itr<K, V> {
        private int index;
        private int lastIndex;
        private final MapBuilder<K, V> map;

        public Itr(MapBuilder<K, V> mapBuilder) {
            Intrinsics.checkNotNullParameter(mapBuilder, "map");
            this.map = mapBuilder;
            this.lastIndex = -1;
            this.initNext$kotlin_stdlib();
        }

        public final int getIndex$kotlin_stdlib() {
            return this.index;
        }

        public final int getLastIndex$kotlin_stdlib() {
            return this.lastIndex;
        }

        public final MapBuilder<K, V> getMap$kotlin_stdlib() {
            return this.map;
        }

        public final boolean hasNext() {
            boolean bl = this.index < ((MapBuilder)this.map).length;
            return bl;
        }

        public final void initNext$kotlin_stdlib() {
            int n;
            int[] nArray;
            while (this.index < ((MapBuilder)this.map).length && (nArray = ((MapBuilder)this.map).presenceArray)[n = this.index] < 0) {
                this.index = n + 1;
            }
        }

        public final void remove() {
            this.map.checkIsMutable$kotlin_stdlib();
            ((MapBuilder)this.map).removeKeyAt(this.lastIndex);
            this.lastIndex = -1;
        }

        public final void setIndex$kotlin_stdlib(int n) {
            this.index = n;
        }

        public final void setLastIndex$kotlin_stdlib(int n) {
            this.lastIndex = n;
        }
    }

    @Metadata(bv={1, 0, 3}, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010)\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0000\u0018\u0000*\u0004\b\u0002\u0010\u0001*\u0004\b\u0003\u0010\u00022\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u00032\b\u0012\u0004\u0012\u0002H\u00010\u0004B\u0019\u0012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00028\u0002\u0012\u0004\u0012\u00028\u00030\u0006\u00a2\u0006\u0002\u0010\u0007J\u000e\u0010\b\u001a\u00028\u0002H\u0096\u0002\u00a2\u0006\u0002\u0010\t\u00a8\u0006\n"}, d2={"Lkotlin/collections/builders/MapBuilder$KeysItr;", "K", "V", "Lkotlin/collections/builders/MapBuilder$Itr;", "", "map", "Lkotlin/collections/builders/MapBuilder;", "(Lkotlin/collections/builders/MapBuilder;)V", "next", "()Ljava/lang/Object;", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
    public static final class KeysItr<K, V>
    extends Itr<K, V>
    implements Iterator<K>,
    KMutableIterator {
        public KeysItr(MapBuilder<K, V> mapBuilder) {
            Intrinsics.checkNotNullParameter(mapBuilder, "map");
            super(mapBuilder);
        }

        @Override
        public K next() {
            if (this.getIndex$kotlin_stdlib() < this.getMap$kotlin_stdlib().length) {
                int n = this.getIndex$kotlin_stdlib();
                this.setIndex$kotlin_stdlib(n + 1);
                this.setLastIndex$kotlin_stdlib(n);
                Object object = this.getMap$kotlin_stdlib().keysArray[this.getLastIndex$kotlin_stdlib()];
                this.initNext$kotlin_stdlib();
                return (K)object;
            }
            throw (Throwable)new NoSuchElementException();
        }
    }

    @Metadata(bv={1, 0, 3}, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010)\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0000\u0018\u0000*\u0004\b\u0002\u0010\u0001*\u0004\b\u0003\u0010\u00022\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u00032\b\u0012\u0004\u0012\u0002H\u00020\u0004B\u0019\u0012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00028\u0002\u0012\u0004\u0012\u00028\u00030\u0006\u00a2\u0006\u0002\u0010\u0007J\u000e\u0010\b\u001a\u00028\u0003H\u0096\u0002\u00a2\u0006\u0002\u0010\t\u00a8\u0006\n"}, d2={"Lkotlin/collections/builders/MapBuilder$ValuesItr;", "K", "V", "Lkotlin/collections/builders/MapBuilder$Itr;", "", "map", "Lkotlin/collections/builders/MapBuilder;", "(Lkotlin/collections/builders/MapBuilder;)V", "next", "()Ljava/lang/Object;", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
    public static final class ValuesItr<K, V>
    extends Itr<K, V>
    implements Iterator<V>,
    KMutableIterator {
        public ValuesItr(MapBuilder<K, V> mapBuilder) {
            Intrinsics.checkNotNullParameter(mapBuilder, "map");
            super(mapBuilder);
        }

        @Override
        public V next() {
            if (this.getIndex$kotlin_stdlib() < this.getMap$kotlin_stdlib().length) {
                int n = this.getIndex$kotlin_stdlib();
                this.setIndex$kotlin_stdlib(n + 1);
                this.setLastIndex$kotlin_stdlib(n);
                Object object = this.getMap$kotlin_stdlib().valuesArray;
                Intrinsics.checkNotNull(object);
                object = object[this.getLastIndex$kotlin_stdlib()];
                this.initNext$kotlin_stdlib();
                return (V)object;
            }
            throw (Throwable)new NoSuchElementException();
        }
    }
}

