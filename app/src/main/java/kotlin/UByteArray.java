/*
 * Decompiled with CFR 0.152.
 */
package kotlin;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.collections.ArraysKt;
import kotlin.collections.UByteIterator;
import kotlin.jvm.internal.CollectionToArray;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(bv={1, 0, 3}, d1={"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0000\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0087@\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u00012B\u0014\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0005\u0010\u0006B\u0014\b\u0001\u0012\u0006\u0010\u0007\u001a\u00020\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0005\u0010\tJ\u001b\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0002H\u0096\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0011\u0010\u0012J \u0010\u0013\u001a\u00020\u000f2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\u0016\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0015\u0010\u0016J\u001a\u0010\u0017\u001a\u00020\u000f2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u00d6\u0003\u00a2\u0006\u0004\b\u001a\u0010\u001bJ\u001e\u0010\u001c\u001a\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u0004H\u0086\u0002\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001e\u0010\u001fJ\u0010\u0010 \u001a\u00020\u0004H\u00d6\u0001\u00a2\u0006\u0004\b!\u0010\u000bJ\u000f\u0010\"\u001a\u00020\u000fH\u0016\u00a2\u0006\u0004\b#\u0010$J\u0010\u0010%\u001a\u00020&H\u0096\u0002\u00a2\u0006\u0004\b'\u0010(J#\u0010)\u001a\u00020*2\u0006\u0010\u001d\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0002H\u0086\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b,\u0010-J\u0010\u0010.\u001a\u00020/H\u00d6\u0001\u00a2\u0006\u0004\b0\u00101R\u0014\u0010\u0003\u001a\u00020\u00048VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\u0007\u001a\u00020\b8\u0000X\u0081\u0004\u00a2\u0006\b\n\u0000\u0012\u0004\b\f\u0010\r\u00f8\u0001\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!\u00a8\u00063"}, d2={"Lkotlin/UByteArray;", "", "Lkotlin/UByte;", "size", "", "constructor-impl", "(I)[B", "storage", "", "([B)[B", "getSize-impl", "([B)I", "getStorage$annotations", "()V", "contains", "", "element", "contains-7apg3OU", "([BB)Z", "containsAll", "elements", "containsAll-impl", "([BLjava/util/Collection;)Z", "equals", "other", "", "equals-impl", "([BLjava/lang/Object;)Z", "get", "index", "get-w2LRezQ", "([BI)B", "hashCode", "hashCode-impl", "isEmpty", "isEmpty-impl", "([B)Z", "iterator", "Lkotlin/collections/UByteIterator;", "iterator-impl", "([B)Lkotlin/collections/UByteIterator;", "set", "", "value", "set-VurrAj0", "([BIB)V", "toString", "", "toString-impl", "([B)Ljava/lang/String;", "Iterator", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class UByteArray
implements Collection<UByte>,
KMappedMarker {
    private final byte[] storage;

    private /* synthetic */ UByteArray(byte[] byArray) {
        Intrinsics.checkNotNullParameter(byArray, "storage");
        this.storage = byArray;
    }

    public static final /* synthetic */ UByteArray box-impl(byte[] byArray) {
        Intrinsics.checkNotNullParameter(byArray, "v");
        return new UByteArray(byArray);
    }

    public static byte[] constructor-impl(int n) {
        return UByteArray.constructor-impl(new byte[n]);
    }

    public static byte[] constructor-impl(byte[] byArray) {
        Intrinsics.checkNotNullParameter(byArray, "storage");
        return byArray;
    }

    public static boolean contains-7apg3OU(byte[] byArray, byte by) {
        return ArraysKt.contains(byArray, by);
    }

    public static boolean containsAll-impl(byte[] byArray, Collection<UByte> object) {
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
                    boolean bl3 = e instanceof UByte && ArraysKt.contains(byArray, ((UByte)e).unbox-impl());
                    if (bl3) continue;
                    break block3;
                }
                bl = true;
            }
        }
        return bl;
    }

    public static boolean equals-impl(byte[] byArray, Object object) {
        return object instanceof UByteArray && Intrinsics.areEqual(byArray, ((UByteArray)object).unbox-impl());
    }

    public static final boolean equals-impl0(byte[] byArray, byte[] byArray2) {
        return Intrinsics.areEqual(byArray, byArray2);
    }

    public static final byte get-w2LRezQ(byte[] byArray, int n) {
        return UByte.constructor-impl(byArray[n]);
    }

    public static int getSize-impl(byte[] byArray) {
        return byArray.length;
    }

    public static /* synthetic */ void getStorage$annotations() {
    }

    public static int hashCode-impl(byte[] byArray) {
        int n = byArray != null ? Arrays.hashCode(byArray) : 0;
        return n;
    }

    public static boolean isEmpty-impl(byte[] byArray) {
        boolean bl = byArray.length == 0;
        return bl;
    }

    public static UByteIterator iterator-impl(byte[] byArray) {
        return new Iterator(byArray);
    }

    public static final void set-VurrAj0(byte[] byArray, int n, byte by) {
        byArray[n] = by;
    }

    public static String toString-impl(byte[] byArray) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UByteArray(storage=");
        stringBuilder.append(Arrays.toString(byArray));
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    @Override
    public /* synthetic */ boolean add(Object object) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean add-7apg3OU(byte by) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public boolean addAll(Collection<? extends UByte> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean contains-7apg3OU(byte by) {
        return UByteArray.contains-7apg3OU(this.storage, by);
    }

    @Override
    public boolean containsAll(Collection<? extends Object> collection) {
        return UByteArray.containsAll-impl(this.storage, collection);
    }

    @Override
    public boolean equals(Object object) {
        return UByteArray.equals-impl(this.storage, object);
    }

    public int getSize() {
        return UByteArray.getSize-impl(this.storage);
    }

    @Override
    public int hashCode() {
        return UByteArray.hashCode-impl(this.storage);
    }

    @Override
    public boolean isEmpty() {
        return UByteArray.isEmpty-impl(this.storage);
    }

    public UByteIterator iterator() {
        return UByteArray.iterator-impl(this.storage);
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
        return UByteArray.toString-impl(this.storage);
    }

    public final /* synthetic */ byte[] unbox-impl() {
        return this.storage;
    }

    /*
     * Illegal identifiers - consider using --renameillegalidents true
     */
    @Metadata(bv={1, 0, 3}, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\bH\u0096\u0002J\u0015\u0010\t\u001a\u00020\nH\u0016\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u000b\u0010\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!\u00a8\u0006\r"}, d2={"Lkotlin/UByteArray$Iterator;", "Lkotlin/collections/UByteIterator;", "array", "", "([B)V", "index", "", "hasNext", "", "nextUByte", "Lkotlin/UByte;", "nextUByte-w2LRezQ", "()B", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
    private static final class Iterator
    extends UByteIterator {
        private final byte[] array;
        private int index;

        public Iterator(byte[] byArray) {
            Intrinsics.checkNotNullParameter(byArray, "array");
            this.array = byArray;
        }

        @Override
        public boolean hasNext() {
            boolean bl = this.index < this.array.length;
            return bl;
        }

        @Override
        public byte nextUByte-w2LRezQ() {
            int n = this.index;
            byte[] byArray = this.array;
            if (n < byArray.length) {
                this.index = n + 1;
                return UByte.constructor-impl(byArray[n]);
            }
            throw (Throwable)new NoSuchElementException(String.valueOf(this.index));
        }
    }
}

