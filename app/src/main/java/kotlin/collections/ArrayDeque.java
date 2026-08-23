/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.AbstractList;
import kotlin.collections.AbstractMutableList;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

@Metadata(bv={1, 0, 3}, d1={"\u0000L\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u001e\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0014\b\u0007\u0018\u0000 I*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002:\u0001IB\u000f\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005B\u0007\b\u0016\u00a2\u0006\u0002\u0010\u0006B\u0015\b\u0016\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\b\u00a2\u0006\u0002\u0010\tJ\u0015\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u0010\u0016J\u001d\u0010\u0013\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u0010\u0019J\u001e\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u0018\u001a\u00020\u00042\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH\u0016J\u0016\u0010\u001a\u001a\u00020\u00142\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH\u0016J\u0013\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u0015\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u001cJ\u0013\u0010\u001d\u001a\u00020\u00172\u0006\u0010\u0015\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u001cJ\b\u0010\u001e\u001a\u00020\u0017H\u0016J\u0016\u0010\u001f\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00028\u0000H\u0096\u0002\u00a2\u0006\u0002\u0010\u0016J\u001e\u0010 \u001a\u00020\u00172\u0006\u0010!\u001a\u00020\u00042\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH\u0002J\u0010\u0010\"\u001a\u00020\u00172\u0006\u0010#\u001a\u00020\u0004H\u0002J\u0010\u0010$\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004H\u0002J\u0010\u0010%\u001a\u00020\u00172\u0006\u0010&\u001a\u00020\u0004H\u0002J\u001d\u0010'\u001a\u00020\u00142\u0012\u0010(\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00140)H\u0082\bJ\u000b\u0010*\u001a\u00028\u0000\u00a2\u0006\u0002\u0010+J\r\u0010,\u001a\u0004\u0018\u00018\u0000\u00a2\u0006\u0002\u0010+J\u0016\u0010-\u001a\u00028\u00002\u0006\u0010\u0018\u001a\u00020\u0004H\u0096\u0002\u00a2\u0006\u0002\u0010.J\u0010\u0010/\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004H\u0002J\u0015\u00100\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u00101J\u0016\u00102\u001a\u00028\u00002\u0006\u0010!\u001a\u00020\u0004H\u0083\b\u00a2\u0006\u0002\u0010.J\u0011\u0010!\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004H\u0083\bJM\u00103\u001a\u00020\u00172>\u00104\u001a:\u0012\u0013\u0012\u00110\u0004\u00a2\u0006\f\b6\u0012\b\b7\u0012\u0004\b\b(\u000e\u0012\u001b\u0012\u0019\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000b\u00a2\u0006\f\b6\u0012\b\b7\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\u001705H\u0000\u00a2\u0006\u0002\b8J\b\u00109\u001a\u00020\u0014H\u0016J\u000b\u0010:\u001a\u00028\u0000\u00a2\u0006\u0002\u0010+J\u0015\u0010;\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u00101J\r\u0010<\u001a\u0004\u0018\u00018\u0000\u00a2\u0006\u0002\u0010+J\u0010\u0010=\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004H\u0002J\u0010\u0010>\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004H\u0002J\u0015\u0010?\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u0010\u0016J\u0016\u0010@\u001a\u00020\u00142\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH\u0016J\u0015\u0010A\u001a\u00028\u00002\u0006\u0010\u0018\u001a\u00020\u0004H\u0016\u00a2\u0006\u0002\u0010.J\u000b\u0010B\u001a\u00028\u0000\u00a2\u0006\u0002\u0010+J\r\u0010C\u001a\u0004\u0018\u00018\u0000\u00a2\u0006\u0002\u0010+J\u000b\u0010D\u001a\u00028\u0000\u00a2\u0006\u0002\u0010+J\r\u0010E\u001a\u0004\u0018\u00018\u0000\u00a2\u0006\u0002\u0010+J\u0016\u0010F\u001a\u00020\u00142\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH\u0016J\u001e\u0010G\u001a\u00028\u00002\u0006\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00028\u0000H\u0096\u0002\u00a2\u0006\u0002\u0010HR\u0018\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000bX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\rR\u000e\u0010\u000e\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u0004@RX\u0096\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012\u00a8\u0006J"}, d2={"Lkotlin/collections/ArrayDeque;", "E", "Lkotlin/collections/AbstractMutableList;", "initialCapacity", "", "(I)V", "()V", "elements", "", "(Ljava/util/Collection;)V", "elementData", "", "", "[Ljava/lang/Object;", "head", "<set-?>", "size", "getSize", "()I", "add", "", "element", "(Ljava/lang/Object;)Z", "", "index", "(ILjava/lang/Object;)V", "addAll", "addFirst", "(Ljava/lang/Object;)V", "addLast", "clear", "contains", "copyCollectionElements", "internalIndex", "copyElements", "newCapacity", "decremented", "ensureCapacity", "minCapacity", "filterInPlace", "predicate", "Lkotlin/Function1;", "first", "()Ljava/lang/Object;", "firstOrNull", "get", "(I)Ljava/lang/Object;", "incremented", "indexOf", "(Ljava/lang/Object;)I", "internalGet", "internalStructure", "structure", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "internalStructure$kotlin_stdlib", "isEmpty", "last", "lastIndexOf", "lastOrNull", "negativeMod", "positiveMod", "remove", "removeAll", "removeAt", "removeFirst", "removeFirstOrNull", "removeLast", "removeLastOrNull", "retainAll", "set", "(ILjava/lang/Object;)Ljava/lang/Object;", "Companion", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class ArrayDeque<E>
extends AbstractMutableList<E> {
    public static final Companion Companion = new Companion(null);
    private static final int defaultMinCapacity = 10;
    private static final Object[] emptyElementData = new Object[0];
    private static final int maxArraySize = 0x7FFFFFF7;
    private Object[] elementData;
    private int head;
    private int size;

    public ArrayDeque() {
        this.elementData = emptyElementData;
    }

    public ArrayDeque(int n) {
        block4: {
            Object[] objectArray;
            block3: {
                block2: {
                    if (n != 0) break block2;
                    objectArray = emptyElementData;
                    break block3;
                }
                if (n <= 0) break block4;
                objectArray = new Object[n];
            }
            this.elementData = objectArray;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Illegal Capacity: ");
        stringBuilder.append(n);
        throw (Throwable)new IllegalArgumentException(stringBuilder.toString());
    }

    public ArrayDeque(Collection<? extends E> objectArray) {
        Intrinsics.checkNotNullParameter(objectArray, "elements");
        boolean bl = false;
        objectArray = objectArray.toArray(new Object[0]);
        if (objectArray != null) {
            this.elementData = objectArray;
            this.size = objectArray.length;
            if (objectArray.length == 0) {
                bl = true;
            }
            if (bl) {
                this.elementData = emptyElementData;
            }
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
    }

    public static final /* synthetic */ int access$getSize$p(ArrayDeque arrayDeque) {
        return arrayDeque.size();
    }

    public static final /* synthetic */ void access$setElementData$p(ArrayDeque arrayDeque, Object[] objectArray) {
        arrayDeque.elementData = objectArray;
    }

    public static final /* synthetic */ void access$setHead$p(ArrayDeque arrayDeque, int n) {
        arrayDeque.head = n;
    }

    private final void copyCollectionElements(int n, Collection<? extends E> collection) {
        Iterator<E> iterator2 = collection.iterator();
        int n2 = this.elementData.length;
        while (n < n2 && iterator2.hasNext()) {
            this.elementData[n] = iterator2.next();
            ++n;
        }
        n2 = this.head;
        for (n = 0; n < n2 && iterator2.hasNext(); ++n) {
            this.elementData[n] = iterator2.next();
        }
        this.size = this.size() + collection.size();
    }

    private final void copyElements(int n) {
        Object[] objectArray = new Object[n];
        Object[] objectArray2 = this.elementData;
        ArraysKt.copyInto(objectArray2, objectArray, 0, this.head, objectArray2.length);
        objectArray2 = this.elementData;
        n = objectArray2.length;
        int n2 = this.head;
        ArraysKt.copyInto(objectArray2, objectArray, n - n2, 0, n2);
        this.head = 0;
        this.elementData = objectArray;
    }

    private final int decremented(int n) {
        n = n == 0 ? ArraysKt.getLastIndex(this.elementData) : --n;
        return n;
    }

    private final void ensureCapacity(int n) {
        if (n >= 0) {
            Object[] objectArray = this.elementData;
            if (n <= objectArray.length) {
                return;
            }
            if (objectArray == emptyElementData) {
                this.elementData = new Object[RangesKt.coerceAtLeast(n, 10)];
                return;
            }
            this.copyElements(Companion.newCapacity$kotlin_stdlib(objectArray.length, n));
            return;
        }
        throw (Throwable)new IllegalStateException("Deque is too big.");
    }

    private final boolean filterInPlace(Function1<? super E, Boolean> function1) {
        int n;
        boolean bl = this.isEmpty();
        int n2 = 0;
        if (!bl && (n = this.elementData.length == 0 ? 1 : 0) == 0) {
            n = this.size();
            int n3 = this.positiveMod(this.head + n);
            int n4 = this.head;
            boolean bl2 = false;
            bl = false;
            if (this.head < n3) {
                n2 = this.head;
                n = n4;
                for (n4 = n2; n4 < n3; ++n4) {
                    Object object = this.elementData[n4];
                    if (function1.invoke(object).booleanValue()) {
                        ((ArrayDeque)this).elementData[n] = object;
                        ++n;
                        continue;
                    }
                    bl = true;
                }
                ArraysKt.fill(this.elementData, null, n, n3);
                n2 = n;
                bl2 = bl;
            } else {
                Object object;
                int n5 = this.elementData.length;
                bl = bl2;
                for (n = this.head; n < n5; ++n) {
                    object = this.elementData[n];
                    ((ArrayDeque)this).elementData[n] = null;
                    if (function1.invoke(object).booleanValue()) {
                        ((ArrayDeque)this).elementData[n4] = object;
                        ++n4;
                        continue;
                    }
                    bl = true;
                }
                n = this.positiveMod(n4);
                n4 = n2;
                while (true) {
                    n2 = n;
                    bl2 = bl;
                    if (n4 >= n3) break;
                    object = this.elementData[n4];
                    ((ArrayDeque)this).elementData[n4] = null;
                    if (function1.invoke(object).booleanValue()) {
                        ((ArrayDeque)this).elementData[n] = object;
                        n = this.incremented(n);
                    } else {
                        bl = true;
                    }
                    ++n4;
                }
            }
            if (bl2) {
                this.size = this.negativeMod(n2 - this.head);
            }
            return bl2;
        }
        return false;
    }

    private final int incremented(int n) {
        n = n == ArraysKt.getLastIndex(this.elementData) ? 0 : ++n;
        return n;
    }

    private final E internalGet(int n) {
        return (E)this.elementData[n];
    }

    private final int internalIndex(int n) {
        return this.positiveMod(this.head + n);
    }

    private final int negativeMod(int n) {
        block0: {
            if (n >= 0) break block0;
            n = this.elementData.length + n;
        }
        return n;
    }

    private final int positiveMod(int n) {
        block0: {
            Object[] objectArray = this.elementData;
            if (n < objectArray.length) break block0;
            n -= objectArray.length;
        }
        return n;
    }

    @Override
    public void add(int n, E e) {
        AbstractList.Companion.checkPositionIndex$kotlin_stdlib(n, this.size());
        if (n == this.size()) {
            this.addLast(e);
            return;
        }
        if (n == 0) {
            this.addFirst(e);
            return;
        }
        this.ensureCapacity(this.size() + 1);
        int n2 = this.positiveMod(this.head + n);
        if (n < this.size() + 1 >> 1) {
            n2 = this.decremented(n2);
            n = this.decremented(this.head);
            int n3 = this.head;
            if (n2 >= n3) {
                Object[] objectArray = this.elementData;
                objectArray[n] = objectArray[n3];
                ArraysKt.copyInto(objectArray, objectArray, n3, n3 + 1, n2 + 1);
            } else {
                Object[] objectArray = this.elementData;
                ArraysKt.copyInto(objectArray, objectArray, n3 - 1, n3, objectArray.length);
                objectArray = this.elementData;
                objectArray[objectArray.length - 1] = objectArray[0];
                ArraysKt.copyInto(objectArray, objectArray, 0, 1, n2 + 1);
            }
            this.elementData[n2] = e;
            this.head = n;
        } else {
            n = this.size();
            n = this.positiveMod(this.head + n);
            if (n2 < n) {
                Object[] objectArray = this.elementData;
                ArraysKt.copyInto(objectArray, objectArray, n2 + 1, n2, n);
            } else {
                Object[] objectArray = this.elementData;
                ArraysKt.copyInto(objectArray, objectArray, 1, 0, n);
                objectArray = this.elementData;
                objectArray[0] = objectArray[objectArray.length - 1];
                ArraysKt.copyInto(objectArray, objectArray, n2 + 1, n2, objectArray.length - 1);
            }
            this.elementData[n2] = e;
        }
        this.size = this.size() + 1;
    }

    @Override
    public boolean add(E e) {
        this.addLast(e);
        return true;
    }

    @Override
    public boolean addAll(int n, Collection<? extends E> collection) {
        Intrinsics.checkNotNullParameter(collection, "elements");
        AbstractList.Companion.checkPositionIndex$kotlin_stdlib(n, this.size());
        if (collection.isEmpty()) {
            return false;
        }
        if (n == this.size()) {
            return this.addAll(collection);
        }
        this.ensureCapacity(this.size() + collection.size());
        int n2 = this.size();
        int n3 = this.positiveMod(this.head + n2);
        n2 = this.positiveMod(this.head + n);
        int n4 = collection.size();
        if (n < this.size() + 1 >> 1) {
            n3 = this.head;
            n = n3 - n4;
            if (n2 >= n3) {
                if (n >= 0) {
                    Object[] objectArray = this.elementData;
                    ArraysKt.copyInto(objectArray, objectArray, n, n3, n2);
                } else {
                    Object[] objectArray = this.elementData;
                    int n5 = objectArray.length - (n += objectArray.length);
                    if (n5 >= n2 - n3) {
                        ArraysKt.copyInto(objectArray, objectArray, n, n3, n2);
                    } else {
                        ArraysKt.copyInto(objectArray, objectArray, n, n3, n3 + n5);
                        objectArray = this.elementData;
                        ArraysKt.copyInto(objectArray, objectArray, 0, this.head + n5, n2);
                    }
                }
            } else {
                Object[] objectArray = this.elementData;
                ArraysKt.copyInto(objectArray, objectArray, n, n3, objectArray.length);
                if (n4 >= n2) {
                    objectArray = this.elementData;
                    ArraysKt.copyInto(objectArray, objectArray, objectArray.length - n4, 0, n2);
                } else {
                    objectArray = this.elementData;
                    ArraysKt.copyInto(objectArray, objectArray, objectArray.length - n4, 0, n4);
                    objectArray = this.elementData;
                    ArraysKt.copyInto(objectArray, objectArray, 0, n4, n2);
                }
            }
            this.head = n;
            this.copyCollectionElements(this.negativeMod(n2 - n4), collection);
        } else {
            n = n2 + n4;
            if (n2 < n3) {
                Object[] objectArray = this.elementData;
                if (n3 + n4 <= objectArray.length) {
                    ArraysKt.copyInto(objectArray, objectArray, n, n2, n3);
                } else if (n >= objectArray.length) {
                    ArraysKt.copyInto(objectArray, objectArray, n - objectArray.length, n2, n3);
                } else {
                    n4 = n3 + n4 - objectArray.length;
                    ArraysKt.copyInto(objectArray, objectArray, 0, n3 - n4, n3);
                    objectArray = this.elementData;
                    ArraysKt.copyInto(objectArray, objectArray, n, n2, n3 - n4);
                }
            } else {
                Object[] objectArray = this.elementData;
                ArraysKt.copyInto(objectArray, objectArray, n4, 0, n3);
                objectArray = this.elementData;
                if (n >= objectArray.length) {
                    ArraysKt.copyInto(objectArray, objectArray, n - objectArray.length, n2, objectArray.length);
                } else {
                    ArraysKt.copyInto(objectArray, objectArray, 0, objectArray.length - n4, objectArray.length);
                    objectArray = this.elementData;
                    ArraysKt.copyInto(objectArray, objectArray, n, n2, objectArray.length - n4);
                }
            }
            this.copyCollectionElements(n2, collection);
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        Intrinsics.checkNotNullParameter(collection, "elements");
        if (collection.isEmpty()) {
            return false;
        }
        this.ensureCapacity(this.size() + collection.size());
        int n = this.size();
        this.copyCollectionElements(this.positiveMod(this.head + n), collection);
        return true;
    }

    @Override
    public final void addFirst(E e) {
        int n;
        this.ensureCapacity(this.size() + 1);
        this.head = n = this.decremented(this.head);
        this.elementData[n] = e;
        this.size = this.size() + 1;
    }

    @Override
    public final void addLast(E e) {
        this.ensureCapacity(this.size() + 1);
        Object[] objectArray = this.elementData;
        int n = this.size();
        objectArray[((ArrayDeque)this).positiveMod((int)(((ArrayDeque)this).head + n))] = e;
        this.size = this.size() + 1;
    }

    @Override
    public void clear() {
        int n = this.size();
        int n2 = this.head;
        n = this.positiveMod(this.head + n);
        if (n2 < n) {
            ArraysKt.fill(this.elementData, null, n2, n);
        } else if (this.isEmpty() ^ true) {
            Object[] objectArray = this.elementData;
            ArraysKt.fill(objectArray, null, this.head, objectArray.length);
            ArraysKt.fill(this.elementData, null, 0, n);
        }
        this.head = 0;
        this.size = 0;
    }

    @Override
    public boolean contains(Object object) {
        boolean bl = this.indexOf(object) != -1;
        return bl;
    }

    public final E first() {
        if (!this.isEmpty()) {
            int n = this.head;
            return (E)this.elementData[n];
        }
        throw (Throwable)new NoSuchElementException("ArrayDeque is empty.");
    }

    public final E firstOrNull() {
        Object object;
        if (this.isEmpty()) {
            object = null;
        } else {
            int n = this.head;
            object = this.elementData[n];
        }
        return (E)object;
    }

    @Override
    public E get(int n) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(n, this.size());
        n = this.positiveMod(this.head + n);
        return (E)this.elementData[n];
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public int indexOf(Object object) {
        block6: {
            int n;
            int n2;
            block5: {
                n2 = this.size();
                n2 = this.head;
                n = this.positiveMod(this.head + n2);
                if (n2 >= n) break block5;
                while (n2 < n) {
                    if (Intrinsics.areEqual(object, this.elementData[n2])) {
                        return n2 - this.head;
                    }
                    ++n2;
                }
                break block6;
            }
            if (n2 < n) break block6;
            int n3 = this.elementData.length;
            while (n2 < n3) {
                if (Intrinsics.areEqual(object, this.elementData[n2])) {
                    return n2 - this.head;
                }
                ++n2;
            }
            for (n2 = 0; n2 < n; ++n2) {
                if (!Intrinsics.areEqual(object, this.elementData[n2])) continue;
                return this.elementData.length + n2 - this.head;
            }
        }
        return -1;
    }

    public final void internalStructure$kotlin_stdlib(Function2<? super Integer, ? super Object[], Unit> function2) {
        Intrinsics.checkNotNullParameter(function2, "structure");
        int n = this.size();
        n = this.positiveMod(this.head + n);
        if (this.isEmpty()) {
            function2.invoke((Integer)this.head, (Object[])new Object[0]);
            return;
        }
        Object[] objectArray = new Object[this.size()];
        int n2 = this.head;
        if (n2 < n) {
            ArraysKt.copyInto$default(this.elementData, objectArray, 0, n2, n, 2, null);
            function2.invoke((Integer)this.head, (Object[])objectArray);
        } else {
            ArraysKt.copyInto$default(this.elementData, objectArray, 0, n2, 0, 10, null);
            Object[] objectArray2 = this.elementData;
            ArraysKt.copyInto(objectArray2, objectArray, objectArray2.length - this.head, 0, n);
            function2.invoke((Integer)(this.head - this.elementData.length), (Object[])objectArray);
        }
    }

    @Override
    public boolean isEmpty() {
        boolean bl = this.size() == 0;
        return bl;
    }

    public final E last() {
        if (!this.isEmpty()) {
            int n = CollectionsKt.getLastIndex(this);
            n = this.positiveMod(this.head + n);
            return (E)this.elementData[n];
        }
        throw (Throwable)new NoSuchElementException("ArrayDeque is empty.");
    }

    @Override
    public int lastIndexOf(Object object) {
        int n = this.size();
        int n2 = this.head;
        n = this.positiveMod(this.head + n);
        if (n2 < n) {
            if (--n >= n2) {
                while (true) {
                    if (Intrinsics.areEqual(object, this.elementData[n])) {
                        return n - this.head;
                    }
                    if (n != n2) {
                        --n;
                        continue;
                    }
                    break;
                }
            }
        } else if (n2 > n) {
            --n;
            while (n >= 0) {
                if (Intrinsics.areEqual(object, this.elementData[n])) {
                    return this.elementData.length + n - this.head;
                }
                --n;
            }
            n = ArraysKt.getLastIndex(this.elementData);
            if (n >= (n2 = this.head)) {
                while (true) {
                    if (Intrinsics.areEqual(object, this.elementData[n])) {
                        return n - this.head;
                    }
                    if (n == n2) break;
                    --n;
                }
            }
        }
        return -1;
    }

    public final E lastOrNull() {
        Object object;
        if (this.isEmpty()) {
            object = null;
        } else {
            int n = CollectionsKt.getLastIndex(this);
            n = this.positiveMod(this.head + n);
            object = this.elementData[n];
        }
        return (E)object;
    }

    @Override
    public boolean remove(Object object) {
        int n = this.indexOf(object);
        if (n == -1) {
            return false;
        }
        this.remove(n);
        return true;
    }

    @Override
    public boolean removeAll(Collection<? extends Object> collection) {
        boolean bl;
        block9: {
            int n;
            Intrinsics.checkNotNullParameter(collection, "elements");
            boolean bl2 = this.isEmpty();
            bl = false;
            int n2 = 0;
            if (bl2 || (n = this.elementData.length == 0 ? 1 : 0) != 0) break block9;
            n = this.size();
            int n3 = this.positiveMod(this.head + n);
            int n4 = this.head;
            bl2 = false;
            bl = false;
            if (this.head < n3) {
                n2 = this.head;
                n = n4;
                for (n4 = n2; n4 < n3; ++n4) {
                    Object object = this.elementData[n4];
                    if (collection.contains(object) ^ true) {
                        ((ArrayDeque)this).elementData[n] = object;
                        ++n;
                        continue;
                    }
                    bl = true;
                }
                ArraysKt.fill(this.elementData, null, n, n3);
            } else {
                Object object;
                int n5 = this.elementData.length;
                bl = bl2;
                for (n = this.head; n < n5; ++n) {
                    object = this.elementData[n];
                    ((ArrayDeque)this).elementData[n] = null;
                    if (collection.contains(object) ^ true) {
                        ((ArrayDeque)this).elementData[n4] = object;
                        ++n4;
                        continue;
                    }
                    bl = true;
                }
                n = this.positiveMod(n4);
                for (n4 = n2; n4 < n3; ++n4) {
                    object = this.elementData[n4];
                    ((ArrayDeque)this).elementData[n4] = null;
                    if (collection.contains(object) ^ true) {
                        ((ArrayDeque)this).elementData[n] = object;
                        n = this.incremented(n);
                        continue;
                    }
                    bl = true;
                }
            }
            if (bl) {
                this.size = this.negativeMod(n - this.head);
            }
        }
        return bl;
    }

    @Override
    public E removeAt(int n) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(n, this.size());
        if (n == CollectionsKt.getLastIndex(this)) {
            return this.removeLast();
        }
        if (n == 0) {
            return this.removeFirst();
        }
        int n2 = this.positiveMod(this.head + n);
        Object object = this.elementData[n2];
        if (n < this.size() >> 1) {
            Object[] objectArray;
            n = this.head;
            if (n2 >= n) {
                objectArray = this.elementData;
                ArraysKt.copyInto(objectArray, objectArray, n + 1, n, n2);
            } else {
                objectArray = this.elementData;
                ArraysKt.copyInto(objectArray, objectArray, 1, 0, n2);
                objectArray = this.elementData;
                objectArray[0] = objectArray[objectArray.length - 1];
                n = this.head;
                ArraysKt.copyInto(objectArray, objectArray, n + 1, n, objectArray.length - 1);
            }
            objectArray = this.elementData;
            n = this.head;
            objectArray[n] = null;
            this.head = this.incremented(n);
        } else {
            n = CollectionsKt.getLastIndex(this);
            n = this.positiveMod(this.head + n);
            if (n2 <= n) {
                Object[] objectArray = this.elementData;
                ArraysKt.copyInto(objectArray, objectArray, n2, n2 + 1, n + 1);
            } else {
                Object[] objectArray = this.elementData;
                ArraysKt.copyInto(objectArray, objectArray, n2, n2 + 1, objectArray.length);
                objectArray = this.elementData;
                objectArray[objectArray.length - 1] = objectArray[0];
                ArraysKt.copyInto(objectArray, objectArray, 0, 1, n + 1);
            }
            this.elementData[n] = null;
        }
        this.size = this.size() - 1;
        return (E)object;
    }

    @Override
    public final E removeFirst() {
        if (!this.isEmpty()) {
            int n = this.head;
            Object object = this.elementData[n];
            Object[] objectArray = this.elementData;
            n = this.head;
            objectArray[n] = null;
            this.head = this.incremented(n);
            this.size = this.size() - 1;
            return (E)object;
        }
        throw (Throwable)new NoSuchElementException("ArrayDeque is empty.");
    }

    public final E removeFirstOrNull() {
        E e = this.isEmpty() ? null : (E)this.removeFirst();
        return e;
    }

    @Override
    public final E removeLast() {
        if (!this.isEmpty()) {
            int n = CollectionsKt.getLastIndex(this);
            n = this.positiveMod(this.head + n);
            Object object = this.elementData[n];
            this.elementData[n] = null;
            this.size = this.size() - 1;
            return (E)object;
        }
        throw (Throwable)new NoSuchElementException("ArrayDeque is empty.");
    }

    public final E removeLastOrNull() {
        E e = this.isEmpty() ? null : (E)this.removeLast();
        return e;
    }

    @Override
    public boolean retainAll(Collection<? extends Object> collection) {
        boolean bl;
        block9: {
            int n;
            Intrinsics.checkNotNullParameter(collection, "elements");
            boolean bl2 = this.isEmpty();
            bl = false;
            int n2 = 0;
            if (bl2 || (n = this.elementData.length == 0 ? 1 : 0) != 0) break block9;
            n = this.size();
            int n3 = this.positiveMod(this.head + n);
            int n4 = this.head;
            bl2 = false;
            bl = false;
            if (this.head < n3) {
                n2 = this.head;
                n = n4;
                for (n4 = n2; n4 < n3; ++n4) {
                    Object object = this.elementData[n4];
                    if (collection.contains(object)) {
                        ((ArrayDeque)this).elementData[n] = object;
                        ++n;
                        continue;
                    }
                    bl = true;
                }
                ArraysKt.fill(this.elementData, null, n, n3);
            } else {
                Object object;
                int n5 = this.elementData.length;
                bl = bl2;
                for (n = this.head; n < n5; ++n) {
                    object = this.elementData[n];
                    ((ArrayDeque)this).elementData[n] = null;
                    if (collection.contains(object)) {
                        ((ArrayDeque)this).elementData[n4] = object;
                        ++n4;
                        continue;
                    }
                    bl = true;
                }
                n = this.positiveMod(n4);
                for (n4 = n2; n4 < n3; ++n4) {
                    object = this.elementData[n4];
                    ((ArrayDeque)this).elementData[n4] = null;
                    if (collection.contains(object)) {
                        ((ArrayDeque)this).elementData[n] = object;
                        n = this.incremented(n);
                        continue;
                    }
                    bl = true;
                }
            }
            if (bl) {
                this.size = this.negativeMod(n - this.head);
            }
        }
        return bl;
    }

    @Override
    public E set(int n, E e) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(n, this.size());
        n = this.positiveMod(this.head + n);
        Object object = this.elementData[n];
        this.elementData[n] = e;
        return (E)object;
    }

    @Metadata(bv={1, 0, 3}, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0007\b\u0080\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001d\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004H\u0000\u00a2\u0006\u0002\b\fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0018\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0006X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0007R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2={"Lkotlin/collections/ArrayDeque$Companion;", "", "()V", "defaultMinCapacity", "", "emptyElementData", "", "[Ljava/lang/Object;", "maxArraySize", "newCapacity", "oldCapacity", "minCapacity", "newCapacity$kotlin_stdlib", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final int newCapacity$kotlin_stdlib(int n, int n2) {
            int n3;
            n = n3 = (n >> 1) + n;
            if (n3 - n2 < 0) {
                n = n2;
            }
            int n4 = 0x7FFFFFF7;
            n3 = n;
            if (n - 0x7FFFFFF7 > 0) {
                n = n4;
                if (n2 > 0x7FFFFFF7) {
                    n = Integer.MAX_VALUE;
                }
                n3 = n;
            }
            return n3;
        }
    }
}

