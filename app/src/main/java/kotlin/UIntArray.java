/*
 * Decompiled with CFR 0.152.
 */
package kotlin;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.UInt;
import kotlin.collections.ArraysKt;
import kotlin.collections.UIntIterator;
import kotlin.jvm.internal.CollectionToArray;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(bv={1, 0, 3}, d1={"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0015\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0000\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0087@\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u00012B\u0014\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0005\u0010\u0006B\u0014\b\u0001\u0012\u0006\u0010\u0007\u001a\u00020\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0005\u0010\tJ\u001b\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0002H\u0096\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0011\u0010\u0012J \u0010\u0013\u001a\u00020\u000f2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\u0016\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0015\u0010\u0016J\u001a\u0010\u0017\u001a\u00020\u000f2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u00d6\u0003\u00a2\u0006\u0004\b\u001a\u0010\u001bJ\u001e\u0010\u001c\u001a\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u0004H\u0086\u0002\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001e\u0010\u001fJ\u0010\u0010 \u001a\u00020\u0004H\u00d6\u0001\u00a2\u0006\u0004\b!\u0010\u000bJ\u000f\u0010\"\u001a\u00020\u000fH\u0016\u00a2\u0006\u0004\b#\u0010$J\u0010\u0010%\u001a\u00020&H\u0096\u0002\u00a2\u0006\u0004\b'\u0010(J#\u0010)\u001a\u00020*2\u0006\u0010\u001d\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0002H\u0086\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b,\u0010-J\u0010\u0010.\u001a\u00020/H\u00d6\u0001\u00a2\u0006\u0004\b0\u00101R\u0014\u0010\u0003\u001a\u00020\u00048VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\u0007\u001a\u00020\b8\u0000X\u0081\u0004\u00a2\u0006\b\n\u0000\u0012\u0004\b\f\u0010\r\u00f8\u0001\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!\u00a8\u00063"}, d2={"Lkotlin/UIntArray;", "", "Lkotlin/UInt;", "size", "", "constructor-impl", "(I)[I", "storage", "", "([I)[I", "getSize-impl", "([I)I", "getStorage$annotations", "()V", "contains", "", "element", "contains-WZ4Q5Ns", "([II)Z", "containsAll", "elements", "containsAll-impl", "([ILjava/util/Collection;)Z", "equals", "other", "", "equals-impl", "([ILjava/lang/Object;)Z", "get", "index", "get-pVg5ArA", "([II)I", "hashCode", "hashCode-impl", "isEmpty", "isEmpty-impl", "([I)Z", "iterator", "Lkotlin/collections/UIntIterator;", "iterator-impl", "([I)Lkotlin/collections/UIntIterator;", "set", "", "value", "set-VXSXFK8", "([III)V", "toString", "", "toString-impl", "([I)Ljava/lang/String;", "Iterator", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class UIntArray
implements Collection<UInt>,
KMappedMarker {
    private final int[] storage;

    private /* synthetic */ UIntArray(int[] nArray) {
        Intrinsics.checkNotNullParameter(nArray, "storage");
        this.storage = nArray;
    }

    public static final /* synthetic */ UIntArray box-impl(int[] nArray) {
        Intrinsics.checkNotNullParameter(nArray, "v");
        return new UIntArray(nArray);
    }

    public static int[] constructor-impl(int n) {
        return UIntArray.constructor-impl(new int[n]);
    }

    public static int[] constructor-impl(int[] nArray) {
        Intrinsics.checkNotNullParameter(nArray, "storage");
        return nArray;
    }

    public static boolean contains-WZ4Q5Ns(int[] nArray, int n) {
        return ArraysKt.contains(nArray, n);
    }

    public static boolean containsAll-impl(int[] nArray, Collection<UInt> object) {
        boolean bl;
        block3: {
            Intrinsics.checkNotNullParameter(object, "elements");
            object = (Iterable)object;
            boolean bl2 = object.isEmpty();
            bl = false;
            if (bl2) {
                bl = true;
            } else {
                java.util.Iterator<UInt> iterator2 = object.iterator();
                while (iterator2.hasNext()) {
                    object = iterator2.next();
                    boolean bl3 = object instanceof UInt && ArraysKt.contains(nArray, ((UInt)object).unbox-impl());
                    if (bl3) continue;
                    break block3;
                }
                bl = true;
            }
        }
        return bl;
    }

    public static boolean equals-impl(int[] nArray, Object object) {
        return object instanceof UIntArray && Intrinsics.areEqual(nArray, ((UIntArray)object).unbox-impl());
    }

    public static final boolean equals-impl0(int[] nArray, int[] nArray2) {
        return Intrinsics.areEqual(nArray, nArray2);
    }

    public static final int get-pVg5ArA(int[] nArray, int n) {
        return UInt.constructor-impl(nArray[n]);
    }

    public static int getSize-impl(int[] nArray) {
        return nArray.length;
    }

    public static /* synthetic */ void getStorage$annotations() {
    }

    public static int hashCode-impl(int[] nArray) {
        int n = nArray != null ? Arrays.hashCode(nArray) : 0;
        return n;
    }

    public static boolean isEmpty-impl(int[] nArray) {
        boolean bl = nArray.length == 0;
        return bl;
    }

    public static UIntIterator iterator-impl(int[] nArray) {
        return new Iterator(nArray);
    }

    public static final void set-VXSXFK8(int[] nArray, int n, int n2) {
        nArray[n] = n2;
    }

    public static String toString-impl(int[] nArray) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UIntArray(storage=");
        stringBuilder.append(Arrays.toString(nArray));
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    @Override
    public /* synthetic */ boolean add(Object object) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean add-WZ4Q5Ns(int n) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public boolean addAll(Collection<? extends UInt> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean contains-WZ4Q5Ns(int n) {
        return UIntArray.contains-WZ4Q5Ns(this.storage, n);
    }

    @Override
    public boolean containsAll(Collection<? extends Object> collection) {
        return UIntArray.containsAll-impl(this.storage, collection);
    }

    @Override
    public boolean equals(Object object) {
        return UIntArray.equals-impl(this.storage, object);
    }

    public int getSize() {
        return UIntArray.getSize-impl(this.storage);
    }

    @Override
    public int hashCode() {
        return UIntArray.hashCode-impl(this.storage);
    }

    @Override
    public boolean isEmpty() {
        return UIntArray.isEmpty-impl(this.storage);
    }

    public UIntIterator iterator() {
        return UIntArray.iterator-impl(this.storage);
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
        return UIntArray.toString-impl(this.storage);
    }

    public final /* synthetic */ int[] unbox-impl() {
        return this.storage;
    }

    /*
     * Illegal identifiers - consider using --renameillegalidents true
     */
    @Metadata(bv={1, 0, 3}, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\bH\u0096\u0002J\u0015\u0010\t\u001a\u00020\nH\u0016\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u000b\u0010\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!\u00a8\u0006\r"}, d2={"Lkotlin/UIntArray$Iterator;", "Lkotlin/collections/UIntIterator;", "array", "", "([I)V", "index", "", "hasNext", "", "nextUInt", "Lkotlin/UInt;", "nextUInt-pVg5ArA", "()I", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
    private static final class Iterator
    extends UIntIterator {
        private final int[] array;
        private int index;

        public Iterator(int[] nArray) {
            Intrinsics.checkNotNullParameter(nArray, "array");
            this.array = nArray;
        }

        @Override
        public boolean hasNext() {
            boolean bl = this.index < this.array.length;
            return bl;
        }

        @Override
        public int nextUInt-pVg5ArA() {
            int n = this.index;
            int[] nArray = this.array;
            if (n < nArray.length) {
                this.index = n + 1;
                return UInt.constructor-impl(nArray[n]);
            }
            throw (Throwable)new NoSuchElementException(String.valueOf(this.index));
        }
    }
}

