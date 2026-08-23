/*
 * Decompiled with CFR 0.152.
 */
package kotlin;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.UShort;
import kotlin.collections.ArraysKt;
import kotlin.collections.UShortIterator;
import kotlin.jvm.internal.CollectionToArray;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(bv={1, 0, 3}, d1={"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0017\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0000\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0087@\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u00012B\u0014\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0005\u0010\u0006B\u0014\b\u0001\u0012\u0006\u0010\u0007\u001a\u00020\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0005\u0010\tJ\u001b\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0002H\u0096\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0011\u0010\u0012J \u0010\u0013\u001a\u00020\u000f2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\u0016\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0015\u0010\u0016J\u001a\u0010\u0017\u001a\u00020\u000f2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u00d6\u0003\u00a2\u0006\u0004\b\u001a\u0010\u001bJ\u001e\u0010\u001c\u001a\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u0004H\u0086\u0002\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001e\u0010\u001fJ\u0010\u0010 \u001a\u00020\u0004H\u00d6\u0001\u00a2\u0006\u0004\b!\u0010\u000bJ\u000f\u0010\"\u001a\u00020\u000fH\u0016\u00a2\u0006\u0004\b#\u0010$J\u0010\u0010%\u001a\u00020&H\u0096\u0002\u00a2\u0006\u0004\b'\u0010(J#\u0010)\u001a\u00020*2\u0006\u0010\u001d\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0002H\u0086\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b,\u0010-J\u0010\u0010.\u001a\u00020/H\u00d6\u0001\u00a2\u0006\u0004\b0\u00101R\u0014\u0010\u0003\u001a\u00020\u00048VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\u0007\u001a\u00020\b8\u0000X\u0081\u0004\u00a2\u0006\b\n\u0000\u0012\u0004\b\f\u0010\r\u00f8\u0001\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!\u00a8\u00063"}, d2={"Lkotlin/UShortArray;", "", "Lkotlin/UShort;", "size", "", "constructor-impl", "(I)[S", "storage", "", "([S)[S", "getSize-impl", "([S)I", "getStorage$annotations", "()V", "contains", "", "element", "contains-xj2QHRw", "([SS)Z", "containsAll", "elements", "containsAll-impl", "([SLjava/util/Collection;)Z", "equals", "other", "", "equals-impl", "([SLjava/lang/Object;)Z", "get", "index", "get-Mh2AYeg", "([SI)S", "hashCode", "hashCode-impl", "isEmpty", "isEmpty-impl", "([S)Z", "iterator", "Lkotlin/collections/UShortIterator;", "iterator-impl", "([S)Lkotlin/collections/UShortIterator;", "set", "", "value", "set-01HTLdE", "([SIS)V", "toString", "", "toString-impl", "([S)Ljava/lang/String;", "Iterator", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class UShortArray
implements Collection<UShort>,
KMappedMarker {
    private final short[] storage;

    private /* synthetic */ UShortArray(short[] sArray) {
        Intrinsics.checkNotNullParameter(sArray, "storage");
        this.storage = sArray;
    }

    public static final /* synthetic */ UShortArray box-impl(short[] sArray) {
        Intrinsics.checkNotNullParameter(sArray, "v");
        return new UShortArray(sArray);
    }

    public static short[] constructor-impl(int n) {
        return UShortArray.constructor-impl(new short[n]);
    }

    public static short[] constructor-impl(short[] sArray) {
        Intrinsics.checkNotNullParameter(sArray, "storage");
        return sArray;
    }

    public static boolean contains-xj2QHRw(short[] sArray, short s) {
        return ArraysKt.contains(sArray, s);
    }

    public static boolean containsAll-impl(short[] sArray, Collection<UShort> object) {
        boolean bl;
        block3: {
            Intrinsics.checkNotNullParameter(object, "elements");
            object = (Iterable)object;
            boolean bl2 = object.isEmpty();
            bl = false;
            if (bl2) {
                bl = true;
            } else {
                object = object.iterator();
                while (object.hasNext()) {
                    Object e = object.next();
                    boolean bl3 = e instanceof UShort && ArraysKt.contains(sArray, ((UShort)e).unbox-impl());
                    if (bl3) continue;
                    break block3;
                }
                bl = true;
            }
        }
        return bl;
    }

    public static boolean equals-impl(short[] sArray, Object object) {
        return object instanceof UShortArray && Intrinsics.areEqual(sArray, ((UShortArray)object).unbox-impl());
    }

    public static final boolean equals-impl0(short[] sArray, short[] sArray2) {
        return Intrinsics.areEqual(sArray, sArray2);
    }

    public static final short get-Mh2AYeg(short[] sArray, int n) {
        return UShort.constructor-impl(sArray[n]);
    }

    public static int getSize-impl(short[] sArray) {
        return sArray.length;
    }

    public static /* synthetic */ void getStorage$annotations() {
    }

    public static int hashCode-impl(short[] sArray) {
        int n = sArray != null ? Arrays.hashCode(sArray) : 0;
        return n;
    }

    public static boolean isEmpty-impl(short[] sArray) {
        boolean bl = sArray.length == 0;
        return bl;
    }

    public static UShortIterator iterator-impl(short[] sArray) {
        return new Iterator(sArray);
    }

    public static final void set-01HTLdE(short[] sArray, int n, short s) {
        sArray[n] = s;
    }

    public static String toString-impl(short[] sArray) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UShortArray(storage=");
        stringBuilder.append(Arrays.toString(sArray));
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    @Override
    public /* synthetic */ boolean add(Object object) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean add-xj2QHRw(short s) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public boolean addAll(Collection<? extends UShort> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean contains-xj2QHRw(short s) {
        return UShortArray.contains-xj2QHRw(this.storage, s);
    }

    @Override
    public boolean containsAll(Collection<? extends Object> collection) {
        return UShortArray.containsAll-impl(this.storage, collection);
    }

    @Override
    public boolean equals(Object object) {
        return UShortArray.equals-impl(this.storage, object);
    }

    public int getSize() {
        return UShortArray.getSize-impl(this.storage);
    }

    @Override
    public int hashCode() {
        return UShortArray.hashCode-impl(this.storage);
    }

    @Override
    public boolean isEmpty() {
        return UShortArray.isEmpty-impl(this.storage);
    }

    public UShortIterator iterator() {
        return UShortArray.iterator-impl(this.storage);
    }

    @Override
    public boolean remove(Object object) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public boolean removeAll(Collection<? extends Object> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public boolean retainAll(Collection<? extends Object> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public Object[] toArray() {
        return CollectionToArray.toArray(this);
    }

    @Override
    public <T> T[] toArray(T[] TArray) {
        return CollectionToArray.toArray(this, TArray);
    }

    public String toString() {
        return UShortArray.toString-impl(this.storage);
    }

    public final /* synthetic */ short[] unbox-impl() {
        return this.storage;
    }

    /*
     * Illegal identifiers - consider using --renameillegalidents true
     */
    @Metadata(bv={1, 0, 3}, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0017\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\bH\u0096\u0002J\u0015\u0010\t\u001a\u00020\nH\u0016\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u000b\u0010\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!\u00a8\u0006\r"}, d2={"Lkotlin/UShortArray$Iterator;", "Lkotlin/collections/UShortIterator;", "array", "", "([S)V", "index", "", "hasNext", "", "nextUShort", "Lkotlin/UShort;", "nextUShort-Mh2AYeg", "()S", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
    private static final class Iterator
    extends UShortIterator {
        private final short[] array;
        private int index;

        public Iterator(short[] sArray) {
            Intrinsics.checkNotNullParameter(sArray, "array");
            this.array = sArray;
        }

        @Override
        public boolean hasNext() {
            boolean bl = this.index < this.array.length;
            return bl;
        }

        @Override
        public short nextUShort-Mh2AYeg() {
            int n = this.index;
            short[] sArray = this.array;
            if (n < sArray.length) {
                this.index = n + 1;
                return UShort.constructor-impl(sArray[n]);
            }
            throw (Throwable)new NoSuchElementException(String.valueOf(this.index));
        }
    }
}

