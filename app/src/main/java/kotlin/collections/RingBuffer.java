/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import java.util.Arrays;
import java.util.Iterator;
import java.util.RandomAccess;
import kotlin.Metadata;
import kotlin.collections.AbstractIterator;
import kotlin.collections.AbstractList;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

@Metadata(bv={1, 0, 3}, d1={"\u0000>\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010(\n\u0002\b\b\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00060\u0003j\u0002`\u0004B\u000f\b\u0016\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007B\u001d\u0012\u000e\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\t\u0012\u0006\u0010\u000b\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\fJ\u0013\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u0016J\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\u0006\u0010\u0018\u001a\u00020\u0006J\u0016\u0010\u0019\u001a\u00028\u00002\u0006\u0010\u001a\u001a\u00020\u0006H\u0096\u0002\u00a2\u0006\u0002\u0010\u001bJ\u0006\u0010\u001c\u001a\u00020\u001dJ\u000f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00028\u00000\u001fH\u0096\u0002J\u000e\u0010 \u001a\u00020\u00142\u0006\u0010!\u001a\u00020\u0006J\u0015\u0010\"\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\tH\u0014\u00a2\u0006\u0002\u0010#J'\u0010\"\u001a\b\u0012\u0004\u0012\u0002H\u00010\t\"\u0004\b\u0001\u0010\u00012\f\u0010$\u001a\b\u0012\u0004\u0012\u0002H\u00010\tH\u0014\u00a2\u0006\u0002\u0010%J\u0015\u0010&\u001a\u00020\u0006*\u00020\u00062\u0006\u0010!\u001a\u00020\u0006H\u0082\bR\u0018\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\tX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\rR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u0006@RX\u0096\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006'"}, d2={"Lkotlin/collections/RingBuffer;", "T", "Lkotlin/collections/AbstractList;", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "capacity", "", "(I)V", "buffer", "", "", "filledSize", "([Ljava/lang/Object;I)V", "[Ljava/lang/Object;", "<set-?>", "size", "getSize", "()I", "startIndex", "add", "", "element", "(Ljava/lang/Object;)V", "expanded", "maxCapacity", "get", "index", "(I)Ljava/lang/Object;", "isFull", "", "iterator", "", "removeFirst", "n", "toArray", "()[Ljava/lang/Object;", "array", "([Ljava/lang/Object;)[Ljava/lang/Object;", "forward", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
final class RingBuffer<T>
extends AbstractList<T>
implements RandomAccess {
    private final Object[] buffer;
    private final int capacity;
    private int size;
    private int startIndex;

    public RingBuffer(int n) {
        this(new Object[n], 0);
    }

    public RingBuffer(Object[] object, int n) {
        Intrinsics.checkNotNullParameter(object, "buffer");
        this.buffer = object;
        boolean bl = true;
        boolean bl2 = n >= 0;
        if (bl2) {
            bl2 = n <= ((Object[])object).length ? bl : false;
            if (bl2) {
                this.capacity = ((Object)object).length;
                this.size = n;
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ring buffer filled size: ");
            stringBuilder.append(n);
            stringBuilder.append(" cannot be larger than the buffer size: ");
            stringBuilder.append(((Object)object).length);
            throw (Throwable)new IllegalArgumentException(stringBuilder.toString().toString());
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("ring buffer filled size should not be negative but it is ");
        ((StringBuilder)object).append(n);
        throw (Throwable)new IllegalArgumentException(((StringBuilder)object).toString().toString());
    }

    public static final /* synthetic */ int access$forward(RingBuffer ringBuffer, int n, int n2) {
        return ringBuffer.forward(n, n2);
    }

    public static final /* synthetic */ Object[] access$getBuffer$p(RingBuffer ringBuffer) {
        return ringBuffer.buffer;
    }

    public static final /* synthetic */ int access$getSize$p(RingBuffer ringBuffer) {
        return ringBuffer.size();
    }

    public static final /* synthetic */ int access$getStartIndex$p(RingBuffer ringBuffer) {
        return ringBuffer.startIndex;
    }

    public static final /* synthetic */ void access$setSize$p(RingBuffer ringBuffer, int n) {
        ringBuffer.size = n;
    }

    public static final /* synthetic */ void access$setStartIndex$p(RingBuffer ringBuffer, int n) {
        ringBuffer.startIndex = n;
    }

    private final int forward(int n, int n2) {
        return (n + n2) % this.capacity;
    }

    public final void add(T t) {
        if (!this.isFull()) {
            this.buffer[(this.startIndex + this.size()) % ((RingBuffer)this).capacity] = t;
            this.size = this.size() + 1;
            return;
        }
        throw (Throwable)new IllegalStateException("ring buffer is full");
    }

    public final RingBuffer<T> expanded(int n) {
        Object[] objectArray;
        int n2 = this.capacity;
        n = RangesKt.coerceAtMost(n2 + (n2 >> 1) + 1, n);
        if (this.startIndex == 0) {
            objectArray = Arrays.copyOf(this.buffer, n);
            Intrinsics.checkNotNullExpressionValue(objectArray, "java.util.Arrays.copyOf(this, newSize)");
        } else {
            objectArray = this.toArray(new Object[n]);
        }
        return new RingBuffer<T>(objectArray, this.size());
    }

    @Override
    public T get(int n) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(n, this.size());
        return (T)this.buffer[(this.startIndex + n) % this.capacity];
    }

    @Override
    public int getSize() {
        return this.size;
    }

    public final boolean isFull() {
        boolean bl = this.size() == this.capacity;
        return bl;
    }

    @Override
    public Iterator<T> iterator() {
        return new AbstractIterator<T>(this){
            private int count;
            private int index;
            final RingBuffer this$0;
            {
                this.this$0 = ringBuffer;
                this.count = ringBuffer.size();
                this.index = RingBuffer.access$getStartIndex$p(ringBuffer);
            }

            protected void computeNext() {
                if (this.count == 0) {
                    this.done();
                } else {
                    this.setNext(RingBuffer.access$getBuffer$p(this.this$0)[this.index]);
                    RingBuffer ringBuffer = this.this$0;
                    this.index = (this.index + 1) % RingBuffer.access$getCapacity$p(ringBuffer);
                    --this.count;
                }
            }
        };
    }

    public final void removeFirst(int n) {
        int n2 = 1;
        int n3 = n >= 0 ? 1 : 0;
        if (n3 != 0) {
            n3 = n <= this.size() ? n2 : 0;
            if (n3 != 0) {
                if (n > 0) {
                    n3 = this.startIndex;
                    n2 = (n3 + n) % this.capacity;
                    if (n3 > n2) {
                        ArraysKt.fill(this.buffer, null, n3, this.capacity);
                        ArraysKt.fill(this.buffer, null, 0, n2);
                    } else {
                        ArraysKt.fill(this.buffer, null, n3, n2);
                    }
                    this.startIndex = n2;
                    this.size = this.size() - n;
                }
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("n shouldn't be greater than the buffer size: n = ");
            stringBuilder.append(n);
            stringBuilder.append(", size = ");
            stringBuilder.append(this.size());
            throw (Throwable)new IllegalArgumentException(stringBuilder.toString().toString());
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("n shouldn't be negative but it is ");
        stringBuilder.append(n);
        throw (Throwable)new IllegalArgumentException(stringBuilder.toString().toString());
    }

    @Override
    public Object[] toArray() {
        return this.toArray(new Object[this.size()]);
    }

    @Override
    public <T> T[] toArray(T[] object) {
        int n;
        Intrinsics.checkNotNullParameter(object, "array");
        if (((T[])object).length < this.size()) {
            object = Arrays.copyOf(object, this.size());
            Intrinsics.checkNotNullExpressionValue(object, "java.util.Arrays.copyOf(this, newSize)");
        }
        int n2 = this.size();
        int n3 = 0;
        for (n = this.startIndex; n3 < n2 && n < this.capacity; ++n3, ++n) {
            object[n3] = this.buffer[n];
        }
        n = 0;
        while (n3 < n2) {
            object[n3] = this.buffer[n];
            ++n3;
            ++n;
        }
        if (((T[])object).length > this.size()) {
            object[this.size()] = null;
        }
        if (object != null) {
            return object;
        }
        object = new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
        throw object;
    }
}

