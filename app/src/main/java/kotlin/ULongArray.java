/*
 * Decompiled with CFR 0.152.
 */
package kotlin;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.ULong;
import kotlin.collections.ArraysKt;
import kotlin.collections.ULongIterator;
import kotlin.jvm.internal.CollectionToArray;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(bv={1, 0, 3}, d1={"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0016\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0000\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0087@\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u00012B\u0014\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0005\u0010\u0006B\u0014\b\u0001\u0012\u0006\u0010\u0007\u001a\u00020\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0005\u0010\tJ\u001b\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0002H\u0096\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0011\u0010\u0012J \u0010\u0013\u001a\u00020\u000f2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\u0016\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0015\u0010\u0016J\u001a\u0010\u0017\u001a\u00020\u000f2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u00d6\u0003\u00a2\u0006\u0004\b\u001a\u0010\u001bJ\u001e\u0010\u001c\u001a\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u0004H\u0086\u0002\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001e\u0010\u001fJ\u0010\u0010 \u001a\u00020\u0004H\u00d6\u0001\u00a2\u0006\u0004\b!\u0010\u000bJ\u000f\u0010\"\u001a\u00020\u000fH\u0016\u00a2\u0006\u0004\b#\u0010$J\u0010\u0010%\u001a\u00020&H\u0096\u0002\u00a2\u0006\u0004\b'\u0010(J#\u0010)\u001a\u00020*2\u0006\u0010\u001d\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0002H\u0086\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b,\u0010-J\u0010\u0010.\u001a\u00020/H\u00d6\u0001\u00a2\u0006\u0004\b0\u00101R\u0014\u0010\u0003\u001a\u00020\u00048VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\u0007\u001a\u00020\b8\u0000X\u0081\u0004\u00a2\u0006\b\n\u0000\u0012\u0004\b\f\u0010\r\u00f8\u0001\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!\u00a8\u00063"}, d2={"Lkotlin/ULongArray;", "", "Lkotlin/ULong;", "size", "", "constructor-impl", "(I)[J", "storage", "", "([J)[J", "getSize-impl", "([J)I", "getStorage$annotations", "()V", "contains", "", "element", "contains-VKZWuLQ", "([JJ)Z", "containsAll", "elements", "containsAll-impl", "([JLjava/util/Collection;)Z", "equals", "other", "", "equals-impl", "([JLjava/lang/Object;)Z", "get", "index", "get-s-VKNKU", "([JI)J", "hashCode", "hashCode-impl", "isEmpty", "isEmpty-impl", "([J)Z", "iterator", "Lkotlin/collections/ULongIterator;", "iterator-impl", "([J)Lkotlin/collections/ULongIterator;", "set", "", "value", "set-k8EXiF4", "([JIJ)V", "toString", "", "toString-impl", "([J)Ljava/lang/String;", "Iterator", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class ULongArray
implements Collection<ULong>,
KMappedMarker {
    private final long[] storage;

    private /* synthetic */ ULongArray(long[] lArray) {
        Intrinsics.checkNotNullParameter(lArray, "storage");
        this.storage = lArray;
    }

    public static final /* synthetic */ ULongArray box-impl(long[] lArray) {
        Intrinsics.checkNotNullParameter(lArray, "v");
        return new ULongArray(lArray);
    }

    public static long[] constructor-impl(int n) {
        return ULongArray.constructor-impl(new long[n]);
    }

    public static long[] constructor-impl(long[] lArray) {
        Intrinsics.checkNotNullParameter(lArray, "storage");
        return lArray;
    }

    public static boolean contains-VKZWuLQ(long[] lArray, long l) {
        return ArraysKt.contains(lArray, l);
    }

    public static boolean containsAll-impl(long[] lArray, Collection<ULong> object) {
        boolean bl;
        block3: {
            Intrinsics.checkNotNullParameter(object, "elements");
            object = (Iterable)object;
            boolean bl2 = object.isEmpty();
            bl = false;
            if (bl2) {
                bl = true;
            } else {
                java.util.Iterator<ULong> iterator2 = object.iterator();
                while (iterator2.hasNext()) {
                    object = iterator2.next();
                    boolean bl3 = object instanceof ULong && ArraysKt.contains(lArray, ((ULong)object).unbox-impl());
                    if (bl3) continue;
                    break block3;
                }
                bl = true;
            }
        }
        return bl;
    }

    public static boolean equals-impl(long[] lArray, Object object) {
        return object instanceof ULongArray && Intrinsics.areEqual(lArray, ((ULongArray)object).unbox-impl());
    }

    public static final boolean equals-impl0(long[] lArray, long[] lArray2) {
        return Intrinsics.areEqual(lArray, lArray2);
    }

    public static final long get-s-VKNKU(long[] lArray, int n) {
        return ULong.constructor-impl(lArray[n]);
    }

    public static int getSize-impl(long[] lArray) {
        return lArray.length;
    }

    public static /* synthetic */ void getStorage$annotations() {
    }

    public static int hashCode-impl(long[] lArray) {
        int n = lArray != null ? Arrays.hashCode(lArray) : 0;
        return n;
    }

    public static boolean isEmpty-impl(long[] lArray) {
        boolean bl = lArray.length == 0;
        return bl;
    }

    public static ULongIterator iterator-impl(long[] lArray) {
        return new Iterator(lArray);
    }

    public static final void set-k8EXiF4(long[] lArray, int n, long l) {
        lArray[n] = l;
    }

    public static String toString-impl(long[] lArray) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ULongArray(storage=");
        stringBuilder.append(Arrays.toString(lArray));
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    @Override
    public /* synthetic */ boolean add(Object object) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean add-VKZWuLQ(long l) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public boolean addAll(Collection<? extends ULong> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean contains-VKZWuLQ(long l) {
        return ULongArray.contains-VKZWuLQ(this.storage, l);
    }

    @Override
    public boolean containsAll(Collection<? extends Object> collection) {
        return ULongArray.containsAll-impl(this.storage, collection);
    }

    @Override
    public boolean equals(Object object) {
        return ULongArray.equals-impl(this.storage, object);
    }

    public int getSize() {
        return ULongArray.getSize-impl(this.storage);
    }

    @Override
    public int hashCode() {
        return ULongArray.hashCode-impl(this.storage);
    }

    @Override
    public boolean isEmpty() {
        return ULongArray.isEmpty-impl(this.storage);
    }

    public ULongIterator iterator() {
        return ULongArray.iterator-impl(this.storage);
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
        return ULongArray.toString-impl(this.storage);
    }

    public final /* synthetic */ long[] unbox-impl() {
        return this.storage;
    }

    /*
     * Illegal identifiers - consider using --renameillegalidents true
     */
    @Metadata(bv={1, 0, 3}, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0016\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\bH\u0096\u0002J\u0015\u0010\t\u001a\u00020\nH\u0016\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u000b\u0010\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!\u00a8\u0006\r"}, d2={"Lkotlin/ULongArray$Iterator;", "Lkotlin/collections/ULongIterator;", "array", "", "([J)V", "index", "", "hasNext", "", "nextULong", "Lkotlin/ULong;", "nextULong-s-VKNKU", "()J", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
    private static final class Iterator
    extends ULongIterator {
        private final long[] array;
        private int index;

        public Iterator(long[] lArray) {
            Intrinsics.checkNotNullParameter(lArray, "array");
            this.array = lArray;
        }

        @Override
        public boolean hasNext() {
            boolean bl = this.index < this.array.length;
            return bl;
        }

        @Override
        public long nextULong-s-VKNKU() {
            int n = this.index;
            long[] lArray = this.array;
            if (n < lArray.length) {
                this.index = n + 1;
                return ULong.constructor-impl(lArray[n]);
            }
            throw (Throwable)new NoSuchElementException(String.valueOf(this.index));
        }
    }
}

