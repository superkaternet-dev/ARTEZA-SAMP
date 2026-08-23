/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections.builders;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import kotlin.Metadata;
import kotlin.collections.AbstractList;
import kotlin.collections.AbstractMutableList;
import kotlin.collections.ArrayDeque;
import kotlin.collections.ArraysKt;
import kotlin.collections.builders.ListBuilderKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMutableList;
import kotlin.jvm.internal.markers.KMutableListIterator;

@Metadata(bv={1, 0, 3}, d1={"\u0000j\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u001e\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\b\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0010)\n\u0002\b\u0002\n\u0002\u0010+\n\u0002\b\u0010\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00060\u0003j\u0002`\u00042\b\u0012\u0004\u0012\u0002H\u00010\u0005:\u0001LB\u0007\b\u0016\u00a2\u0006\u0002\u0010\u0006B\u000f\b\u0016\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tBM\b\u0002\u0012\f\u0010\n\u001a\b\u0012\u0004\u0012\u00028\u00000\u000b\u0012\u0006\u0010\f\u001a\u00020\b\u0012\u0006\u0010\r\u001a\u00020\b\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u000e\u0010\u0010\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000\u0012\u000e\u0010\u0011\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000\u00a2\u0006\u0002\u0010\u0012J\u0015\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0018\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u0010\u0019J\u001d\u0010\u0017\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u0010\u001cJ\u001e\u0010\u001d\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\b2\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00028\u00000\u001fH\u0016J\u0016\u0010\u001d\u001a\u00020\u000f2\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00028\u00000\u001fH\u0016J&\u0010 \u001a\u00020\u001a2\u0006\u0010!\u001a\u00020\b2\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00028\u00000\u001f2\u0006\u0010\"\u001a\u00020\bH\u0002J\u001d\u0010#\u001a\u00020\u001a2\u0006\u0010!\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00028\u0000H\u0002\u00a2\u0006\u0002\u0010\u001cJ\f\u0010$\u001a\b\u0012\u0004\u0012\u00028\u00000%J\b\u0010&\u001a\u00020\u001aH\u0002J\b\u0010'\u001a\u00020\u001aH\u0016J\u0014\u0010(\u001a\u00020\u000f2\n\u0010)\u001a\u0006\u0012\u0002\b\u00030%H\u0002J\u0010\u0010*\u001a\u00020\u001a2\u0006\u0010+\u001a\u00020\bH\u0002J\u0010\u0010,\u001a\u00020\u001a2\u0006\u0010\"\u001a\u00020\bH\u0002J\u0013\u0010-\u001a\u00020\u000f2\b\u0010)\u001a\u0004\u0018\u00010.H\u0096\u0002J\u0016\u0010/\u001a\u00028\u00002\u0006\u0010\u001b\u001a\u00020\bH\u0096\u0002\u00a2\u0006\u0002\u00100J\b\u00101\u001a\u00020\bH\u0016J\u0015\u00102\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u00103J\u0018\u00104\u001a\u00020\u001a2\u0006\u0010!\u001a\u00020\b2\u0006\u0010\"\u001a\u00020\bH\u0002J\b\u00105\u001a\u00020\u000fH\u0016J\u000f\u00106\u001a\b\u0012\u0004\u0012\u00028\u000007H\u0096\u0002J\u0015\u00108\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u00103J\u000e\u00109\u001a\b\u0012\u0004\u0012\u00028\u00000:H\u0016J\u0016\u00109\u001a\b\u0012\u0004\u0012\u00028\u00000:2\u0006\u0010\u001b\u001a\u00020\bH\u0016J\u0015\u0010;\u001a\u00020\u000f2\u0006\u0010\u0018\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u0010\u0019J\u0016\u0010<\u001a\u00020\u000f2\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00028\u00000\u001fH\u0016J\u0015\u0010=\u001a\u00028\u00002\u0006\u0010\u001b\u001a\u00020\bH\u0016\u00a2\u0006\u0002\u00100J\u0015\u0010>\u001a\u00028\u00002\u0006\u0010!\u001a\u00020\bH\u0002\u00a2\u0006\u0002\u00100J\u0018\u0010?\u001a\u00020\u001a2\u0006\u0010@\u001a\u00020\b2\u0006\u0010A\u001a\u00020\bH\u0002J\u0016\u0010B\u001a\u00020\u000f2\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00028\u00000\u001fH\u0016J.\u0010C\u001a\u00020\b2\u0006\u0010@\u001a\u00020\b2\u0006\u0010A\u001a\u00020\b2\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00028\u00000\u001f2\u0006\u0010D\u001a\u00020\u000fH\u0002J\u001e\u0010E\u001a\u00028\u00002\u0006\u0010\u001b\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00028\u0000H\u0096\u0002\u00a2\u0006\u0002\u0010FJ\u001e\u0010G\u001a\b\u0012\u0004\u0012\u00028\u00000\u00022\u0006\u0010H\u001a\u00020\b2\u0006\u0010I\u001a\u00020\bH\u0016J\b\u0010J\u001a\u00020KH\u0016R\u0016\u0010\n\u001a\b\u0012\u0004\u0012\u00028\u00000\u000bX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0013R\u0016\u0010\u0010\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0011\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\u00020\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0015\u0010\u0016\u00a8\u0006M"}, d2={"Lkotlin/collections/builders/ListBuilder;", "E", "", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "Lkotlin/collections/AbstractMutableList;", "()V", "initialCapacity", "", "(I)V", "array", "", "offset", "length", "isReadOnly", "", "backing", "root", "([Ljava/lang/Object;IIZLkotlin/collections/builders/ListBuilder;Lkotlin/collections/builders/ListBuilder;)V", "[Ljava/lang/Object;", "size", "getSize", "()I", "add", "element", "(Ljava/lang/Object;)Z", "", "index", "(ILjava/lang/Object;)V", "addAll", "elements", "", "addAllInternal", "i", "n", "addAtInternal", "build", "", "checkIsMutable", "clear", "contentEquals", "other", "ensureCapacity", "minCapacity", "ensureExtraCapacity", "equals", "", "get", "(I)Ljava/lang/Object;", "hashCode", "indexOf", "(Ljava/lang/Object;)I", "insertAtInternal", "isEmpty", "iterator", "", "lastIndexOf", "listIterator", "", "remove", "removeAll", "removeAt", "removeAtInternal", "removeRangeInternal", "rangeOffset", "rangeLength", "retainAll", "retainOrRemoveAllInternal", "retain", "set", "(ILjava/lang/Object;)Ljava/lang/Object;", "subList", "fromIndex", "toIndex", "toString", "", "Itr", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class ListBuilder<E>
extends AbstractMutableList<E>
implements List<E>,
RandomAccess,
KMutableList {
    private E[] array;
    private final ListBuilder<E> backing;
    private boolean isReadOnly;
    private int length;
    private int offset;
    private final ListBuilder<E> root;

    public ListBuilder() {
        this(10);
    }

    public ListBuilder(int n) {
        this(ListBuilderKt.arrayOfUninitializedElements(n), 0, 0, false, null, null);
    }

    private ListBuilder(E[] EArray, int n, int n2, boolean bl, ListBuilder<E> listBuilder, ListBuilder<E> listBuilder2) {
        this.array = EArray;
        this.offset = n;
        this.length = n2;
        this.isReadOnly = bl;
        this.backing = listBuilder;
        this.root = listBuilder2;
    }

    public static final /* synthetic */ void access$setArray$p(ListBuilder listBuilder, Object[] objectArray) {
        listBuilder.array = objectArray;
    }

    public static final /* synthetic */ void access$setLength$p(ListBuilder listBuilder, int n) {
        listBuilder.length = n;
    }

    public static final /* synthetic */ void access$setOffset$p(ListBuilder listBuilder, int n) {
        listBuilder.offset = n;
    }

    private final void addAllInternal(int n, Collection<? extends E> object, int n2) {
        ListBuilder<E> listBuilder = this.backing;
        if (listBuilder != null) {
            super.addAllInternal(n, (Collection<E>)object, n2);
            this.array = this.backing.array;
            this.length += n2;
        } else {
            this.insertAtInternal(n, n2);
            object = object.iterator();
            for (int i = 0; i < n2; ++i) {
                this.array[n + i] = object.next();
            }
        }
    }

    private final void addAtInternal(int n, E e) {
        ListBuilder<E> listBuilder = this.backing;
        if (listBuilder != null) {
            super.addAtInternal(n, e);
            this.array = this.backing.array;
            ++this.length;
        } else {
            this.insertAtInternal(n, 1);
            this.array[n] = e;
        }
    }

    private final void checkIsMutable() {
        ListBuilder<E> listBuilder;
        if (!(this.isReadOnly || (listBuilder = this.root) != null && listBuilder.isReadOnly)) {
            return;
        }
        throw (Throwable)new UnsupportedOperationException();
    }

    private final boolean contentEquals(List<?> list) {
        return ListBuilderKt.access$subarrayContentEquals(this.array, this.offset, this.length, list);
    }

    private final void ensureCapacity(int n) {
        if (this.backing == null) {
            if (n > this.array.length) {
                n = ArrayDeque.Companion.newCapacity$kotlin_stdlib(this.array.length, n);
                this.array = ListBuilderKt.copyOfUninitializedElements(this.array, n);
            }
            return;
        }
        throw (Throwable)new IllegalStateException();
    }

    private final void ensureExtraCapacity(int n) {
        this.ensureCapacity(this.length + n);
    }

    private final void insertAtInternal(int n, int n2) {
        this.ensureExtraCapacity(n2);
        E[] EArray = this.array;
        ArraysKt.copyInto(EArray, EArray, n + n2, n, this.offset + this.length);
        this.length += n2;
    }

    private final E removeAtInternal(int n) {
        ListBuilder<E> listBuilder = this.backing;
        if (listBuilder != null) {
            listBuilder = super.removeAtInternal(n);
            --this.length;
            return (E)listBuilder;
        }
        E[] EArray = this.array;
        listBuilder = EArray[n];
        ArraysKt.copyInto(EArray, EArray, n, n + 1, this.offset + this.length);
        ListBuilderKt.resetAt(this.array, this.offset + this.length - 1);
        --this.length;
        return (E)listBuilder;
    }

    private final void removeRangeInternal(int n, int n2) {
        E[] EArray = this.backing;
        if (EArray != null) {
            super.removeRangeInternal(n, n2);
        } else {
            EArray = this.array;
            ArraysKt.copyInto(EArray, EArray, n, n + n2, this.length);
            EArray = this.array;
            n = this.length;
            ListBuilderKt.resetRange(EArray, n - n2, n);
        }
        this.length -= n2;
    }

    private final int retainOrRemoveAllInternal(int n, int n2, Collection<? extends E> EArray, boolean bl) {
        int n3;
        E[] EArray2 = this.backing;
        if (EArray2 != null) {
            n = super.retainOrRemoveAllInternal(n, n2, (Collection<? extends E>)EArray, bl);
            this.length -= n;
            return n;
        }
        int n4 = 0;
        int n5 = 0;
        while (n4 < n2) {
            if (EArray.contains(this.array[n + n4]) == bl) {
                EArray2 = this.array;
                EArray2[n5 + n] = EArray2[n4 + n];
                n3 = n5 + 1;
                n5 = n4 + 1;
            } else {
                n3 = n5;
                n5 = ++n4;
            }
            n4 = n5;
            n5 = n3;
        }
        n3 = n2 - n5;
        EArray = this.array;
        ArraysKt.copyInto(EArray, EArray, n + n5, n + n2, this.length);
        EArray = this.array;
        n = this.length;
        ListBuilderKt.resetRange(EArray, n - n3, n);
        this.length -= n3;
        return n3;
    }

    @Override
    public void add(int n, E e) {
        this.checkIsMutable();
        AbstractList.Companion.checkPositionIndex$kotlin_stdlib(n, this.length);
        this.addAtInternal(this.offset + n, e);
    }

    @Override
    public boolean add(E e) {
        this.checkIsMutable();
        this.addAtInternal(this.offset + this.length, e);
        return true;
    }

    @Override
    public boolean addAll(int n, Collection<? extends E> collection) {
        Intrinsics.checkNotNullParameter(collection, "elements");
        this.checkIsMutable();
        AbstractList.Companion.checkPositionIndex$kotlin_stdlib(n, this.length);
        int n2 = collection.size();
        this.addAllInternal(this.offset + n, collection, n2);
        boolean bl = n2 > 0;
        return bl;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        Intrinsics.checkNotNullParameter(collection, "elements");
        this.checkIsMutable();
        int n = collection.size();
        this.addAllInternal(this.offset + this.length, collection, n);
        boolean bl = n > 0;
        return bl;
    }

    public final List<E> build() {
        if (this.backing == null) {
            this.checkIsMutable();
            this.isReadOnly = true;
            return this;
        }
        throw (Throwable)new IllegalStateException();
    }

    @Override
    public void clear() {
        this.checkIsMutable();
        this.removeRangeInternal(this.offset, this.length);
    }

    @Override
    public boolean equals(Object object) {
        boolean bl = object == this || object instanceof List && this.contentEquals((List)object);
        return bl;
    }

    @Override
    public E get(int n) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(n, this.length);
        return this.array[this.offset + n];
    }

    @Override
    public int getSize() {
        return this.length;
    }

    @Override
    public int hashCode() {
        return ListBuilderKt.access$subarrayContentHashCode(this.array, this.offset, this.length);
    }

    @Override
    public int indexOf(Object object) {
        for (int i = 0; i < this.length; ++i) {
            if (!Intrinsics.areEqual(this.array[this.offset + i], object)) continue;
            return i;
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        boolean bl = this.length == 0;
        return bl;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr(this, 0);
    }

    @Override
    public int lastIndexOf(Object object) {
        for (int i = this.length - 1; i >= 0; --i) {
            if (!Intrinsics.areEqual(this.array[this.offset + i], object)) continue;
            return i;
        }
        return -1;
    }

    @Override
    public ListIterator<E> listIterator() {
        return new Itr(this, 0);
    }

    @Override
    public ListIterator<E> listIterator(int n) {
        AbstractList.Companion.checkPositionIndex$kotlin_stdlib(n, this.length);
        return new Itr(this, n);
    }

    @Override
    public boolean remove(Object object) {
        this.checkIsMutable();
        int n = this.indexOf(object);
        if (n >= 0) {
            this.remove(n);
        }
        boolean bl = n >= 0;
        return bl;
    }

    @Override
    public boolean removeAll(Collection<? extends Object> collection) {
        Intrinsics.checkNotNullParameter(collection, "elements");
        this.checkIsMutable();
        int n = this.offset;
        int n2 = this.length;
        boolean bl = false;
        if (this.retainOrRemoveAllInternal(n, n2, collection, false) > 0) {
            bl = true;
        }
        return bl;
    }

    @Override
    public E removeAt(int n) {
        this.checkIsMutable();
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(n, this.length);
        return this.removeAtInternal(this.offset + n);
    }

    @Override
    public boolean retainAll(Collection<? extends Object> collection) {
        Intrinsics.checkNotNullParameter(collection, "elements");
        this.checkIsMutable();
        int n = this.offset;
        int n2 = this.length;
        boolean bl = true;
        if (this.retainOrRemoveAllInternal(n, n2, collection, true) <= 0) {
            bl = false;
        }
        return bl;
    }

    @Override
    public E set(int n, E e) {
        this.checkIsMutable();
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(n, this.length);
        E[] EArray = this.array;
        int n2 = this.offset;
        E e2 = EArray[n2 + n];
        EArray[n2 + n] = e;
        return e2;
    }

    @Override
    public List<E> subList(int n, int n2) {
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(n, n2, this.length);
        E[] EArray = this.array;
        int n3 = this.offset;
        boolean bl = this.isReadOnly;
        ListBuilder listBuilder = this.root;
        if (listBuilder == null) {
            listBuilder = this;
        }
        return new ListBuilder<E>(EArray, n3 + n, n2 - n, bl, this, listBuilder);
    }

    @Override
    public String toString() {
        return ListBuilderKt.access$subarrayContentToString(this.array, this.offset, this.length);
    }

    @Metadata(bv={1, 0, 3}, d1={"\u0000*\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010+\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\t\b\u0002\u0018\u0000*\u0004\b\u0001\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u001d\b\u0016\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00010\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u0015\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00028\u0001H\u0016\u00a2\u0006\u0002\u0010\fJ\t\u0010\r\u001a\u00020\u000eH\u0096\u0002J\b\u0010\u000f\u001a\u00020\u000eH\u0016J\u000e\u0010\u0010\u001a\u00028\u0001H\u0096\u0002\u00a2\u0006\u0002\u0010\u0011J\b\u0010\u0012\u001a\u00020\u0006H\u0016J\r\u0010\u0013\u001a\u00028\u0001H\u0016\u00a2\u0006\u0002\u0010\u0011J\b\u0010\u0014\u001a\u00020\u0006H\u0016J\b\u0010\u0015\u001a\u00020\nH\u0016J\u0015\u0010\u0016\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00028\u0001H\u0016\u00a2\u0006\u0002\u0010\fR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00010\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2={"Lkotlin/collections/builders/ListBuilder$Itr;", "E", "", "list", "Lkotlin/collections/builders/ListBuilder;", "index", "", "(Lkotlin/collections/builders/ListBuilder;I)V", "lastIndex", "add", "", "element", "(Ljava/lang/Object;)V", "hasNext", "", "hasPrevious", "next", "()Ljava/lang/Object;", "nextIndex", "previous", "previousIndex", "remove", "set", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
    private static final class Itr<E>
    implements ListIterator<E>,
    KMutableListIterator {
        private int index;
        private int lastIndex;
        private final ListBuilder<E> list;

        public Itr(ListBuilder<E> listBuilder, int n) {
            Intrinsics.checkNotNullParameter(listBuilder, "list");
            this.list = listBuilder;
            this.index = n;
            this.lastIndex = -1;
        }

        @Override
        public void add(E e) {
            ListBuilder<E> listBuilder = this.list;
            int n = this.index;
            this.index = n + 1;
            listBuilder.add(n, e);
            this.lastIndex = -1;
        }

        @Override
        public boolean hasNext() {
            boolean bl = this.index < ((ListBuilder)this.list).length;
            return bl;
        }

        @Override
        public boolean hasPrevious() {
            boolean bl = this.index > 0;
            return bl;
        }

        @Override
        public E next() {
            if (this.index < ((ListBuilder)this.list).length) {
                int n = this.index;
                this.index = n + 1;
                this.lastIndex = n;
                return (E)((ListBuilder)this.list).array[((ListBuilder)this.list).offset + this.lastIndex];
            }
            throw (Throwable)new NoSuchElementException();
        }

        @Override
        public int nextIndex() {
            return this.index;
        }

        @Override
        public E previous() {
            int n = this.index;
            if (n > 0) {
                this.index = --n;
                this.lastIndex = n;
                return (E)((ListBuilder)this.list).array[((ListBuilder)this.list).offset + this.lastIndex];
            }
            throw (Throwable)new NoSuchElementException();
        }

        @Override
        public int previousIndex() {
            return this.index - 1;
        }

        @Override
        public void remove() {
            int n = this.lastIndex;
            boolean bl = n != -1;
            if (bl) {
                this.list.remove(n);
                this.index = this.lastIndex;
                this.lastIndex = -1;
                return;
            }
            throw (Throwable)new IllegalStateException("Call next() or previous() before removing element from the iterator.".toString());
        }

        @Override
        public void set(E e) {
            int n = this.lastIndex;
            boolean bl = n != -1;
            if (bl) {
                this.list.set(n, e);
                return;
            }
            throw (Throwable)new IllegalStateException("Call next() or previous() before replacing element from the iterator.".toString());
        }
    }
}

