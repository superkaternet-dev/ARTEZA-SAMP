/*
 * Decompiled with CFR 0.152.
 */
package kotlin.sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.DropTakeSequence;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.sequences.SubSequence;

@Metadata(bv={1, 0, 3}, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010(\n\u0002\b\u0002\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003B\u001b\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u0016\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\u00022\u0006\u0010\t\u001a\u00020\u0006H\u0016J\u000f\u0010\n\u001a\b\u0012\u0004\u0012\u00028\u00000\u000bH\u0096\u0002J\u0016\u0010\f\u001a\b\u0012\u0004\u0012\u00028\u00000\u00022\u0006\u0010\t\u001a\u00020\u0006H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2={"Lkotlin/sequences/TakeSequence;", "T", "Lkotlin/sequences/Sequence;", "Lkotlin/sequences/DropTakeSequence;", "sequence", "count", "", "(Lkotlin/sequences/Sequence;I)V", "drop", "n", "iterator", "", "take", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class TakeSequence<T>
implements Sequence<T>,
DropTakeSequence<T> {
    private final int count;
    private final Sequence<T> sequence;

    public TakeSequence(Sequence<? extends T> object, int n) {
        Intrinsics.checkNotNullParameter(object, "sequence");
        this.sequence = object;
        this.count = n;
        boolean bl = n >= 0;
        if (bl) {
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("count must be non-negative, but was ");
        ((StringBuilder)object).append(n);
        ((StringBuilder)object).append('.');
        throw (Throwable)new IllegalArgumentException(((StringBuilder)object).toString().toString());
    }

    public static final /* synthetic */ int access$getCount$p(TakeSequence takeSequence) {
        return takeSequence.count;
    }

    public static final /* synthetic */ Sequence access$getSequence$p(TakeSequence takeSequence) {
        return takeSequence.sequence;
    }

    @Override
    public Sequence<T> drop(int n) {
        int n2 = this.count;
        Sequence sequence = n >= n2 ? SequencesKt.emptySequence() : (Sequence)new SubSequence<T>(this.sequence, n, n2);
        return sequence;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>(this){
            private final Iterator<T> iterator;
            private int left;
            final TakeSequence this$0;
            {
                this.this$0 = takeSequence;
                this.left = TakeSequence.access$getCount$p(takeSequence);
                this.iterator = TakeSequence.access$getSequence$p(takeSequence).iterator();
            }

            public final Iterator<T> getIterator() {
                return this.iterator;
            }

            public final int getLeft() {
                return this.left;
            }

            public boolean hasNext() {
                boolean bl = this.left > 0 && this.iterator.hasNext();
                return bl;
            }

            public T next() {
                int n = this.left;
                if (n != 0) {
                    this.left = n - 1;
                    return this.iterator.next();
                }
                throw (Throwable)new NoSuchElementException();
            }

            public void remove() {
                throw new UnsupportedOperationException("Operation is not supported for read-only collection");
            }

            public final void setLeft(int n) {
                this.left = n;
            }
        };
    }

    @Override
    public Sequence<T> take(int n) {
        Sequence sequence = n >= this.count ? (Sequence)this : (Sequence)new TakeSequence<T>(this.sequence, n);
        return sequence;
    }
}

