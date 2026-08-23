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

@Metadata(bv={1, 0, 3}, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010(\n\u0002\b\u0002\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003B#\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\bJ\u0016\u0010\f\u001a\b\u0012\u0004\u0012\u00028\u00000\u00022\u0006\u0010\r\u001a\u00020\u0006H\u0016J\u000f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00028\u00000\u000fH\u0096\u0002J\u0016\u0010\u0010\u001a\b\u0012\u0004\u0012\u00028\u00000\u00022\u0006\u0010\r\u001a\u00020\u0006H\u0016R\u0014\u0010\t\u001a\u00020\u00068BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2={"Lkotlin/sequences/SubSequence;", "T", "Lkotlin/sequences/Sequence;", "Lkotlin/sequences/DropTakeSequence;", "sequence", "startIndex", "", "endIndex", "(Lkotlin/sequences/Sequence;II)V", "count", "getCount", "()I", "drop", "n", "iterator", "", "take", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class SubSequence<T>
implements Sequence<T>,
DropTakeSequence<T> {
    private final int endIndex;
    private final Sequence<T> sequence;
    private final int startIndex;

    public SubSequence(Sequence<? extends T> object, int n, int n2) {
        Intrinsics.checkNotNullParameter(object, "sequence");
        this.sequence = object;
        this.startIndex = n;
        this.endIndex = n2;
        boolean bl = true;
        boolean bl2 = n >= 0;
        if (bl2) {
            bl2 = n2 >= 0;
            if (bl2) {
                bl2 = n2 >= n ? bl : false;
                if (bl2) {
                    return;
                }
                object = new StringBuilder();
                ((StringBuilder)object).append("endIndex should be not less than startIndex, but was ");
                ((StringBuilder)object).append(n2);
                ((StringBuilder)object).append(" < ");
                ((StringBuilder)object).append(n);
                throw (Throwable)new IllegalArgumentException(((StringBuilder)object).toString().toString());
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("endIndex should be non-negative, but is ");
            ((StringBuilder)object).append(n2);
            throw (Throwable)new IllegalArgumentException(((StringBuilder)object).toString().toString());
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("startIndex should be non-negative, but is ");
        ((StringBuilder)object).append(n);
        throw (Throwable)new IllegalArgumentException(((StringBuilder)object).toString().toString());
    }

    public static final /* synthetic */ int access$getEndIndex$p(SubSequence subSequence) {
        return subSequence.endIndex;
    }

    public static final /* synthetic */ Sequence access$getSequence$p(SubSequence subSequence) {
        return subSequence.sequence;
    }

    public static final /* synthetic */ int access$getStartIndex$p(SubSequence subSequence) {
        return subSequence.startIndex;
    }

    private final int getCount() {
        return this.endIndex - this.startIndex;
    }

    @Override
    public Sequence<T> drop(int n) {
        Sequence sequence = n >= this.getCount() ? SequencesKt.emptySequence() : (Sequence)new SubSequence<T>(this.sequence, this.startIndex + n, this.endIndex);
        return sequence;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>(this){
            private final Iterator<T> iterator;
            private int position;
            final SubSequence this$0;
            {
                this.this$0 = subSequence;
                this.iterator = SubSequence.access$getSequence$p(subSequence).iterator();
            }

            private final void drop() {
                while (this.position < SubSequence.access$getStartIndex$p(this.this$0) && this.iterator.hasNext()) {
                    this.iterator.next();
                    ++this.position;
                }
            }

            public final Iterator<T> getIterator() {
                return this.iterator;
            }

            public final int getPosition() {
                return this.position;
            }

            public boolean hasNext() {
                this.drop();
                boolean bl = this.position < SubSequence.access$getEndIndex$p(this.this$0) && this.iterator.hasNext();
                return bl;
            }

            public T next() {
                this.drop();
                if (this.position < SubSequence.access$getEndIndex$p(this.this$0)) {
                    ++this.position;
                    return this.iterator.next();
                }
                throw (Throwable)new NoSuchElementException();
            }

            public void remove() {
                throw new UnsupportedOperationException("Operation is not supported for read-only collection");
            }

            public final void setPosition(int n) {
                this.position = n;
            }
        };
    }

    @Override
    public Sequence<T> take(int n) {
        Sequence sequence;
        if (n >= this.getCount()) {
            sequence = this;
        } else {
            sequence = this.sequence;
            int n2 = this.startIndex;
            sequence = new SubSequence<T>(sequence, n2, n2 + n);
        }
        return sequence;
    }
}

