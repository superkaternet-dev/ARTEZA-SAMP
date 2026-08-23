/*
 * Decompiled with CFR 0.152.
 */
package kotlin.sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;

@Metadata(bv={1, 0, 3}, d1={"\u0000 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010(\n\u0000\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B'\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\u0002\u0010\u0007J\u000f\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\tH\u0096\u0002R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2={"Lkotlin/sequences/TakeWhileSequence;", "T", "Lkotlin/sequences/Sequence;", "sequence", "predicate", "Lkotlin/Function1;", "", "(Lkotlin/sequences/Sequence;Lkotlin/jvm/functions/Function1;)V", "iterator", "", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class TakeWhileSequence<T>
implements Sequence<T> {
    private final Function1<T, Boolean> predicate;
    private final Sequence<T> sequence;

    public TakeWhileSequence(Sequence<? extends T> sequence, Function1<? super T, Boolean> function1) {
        Intrinsics.checkNotNullParameter(sequence, "sequence");
        Intrinsics.checkNotNullParameter(function1, "predicate");
        this.sequence = sequence;
        this.predicate = function1;
    }

    public static final /* synthetic */ Function1 access$getPredicate$p(TakeWhileSequence takeWhileSequence) {
        return takeWhileSequence.predicate;
    }

    public static final /* synthetic */ Sequence access$getSequence$p(TakeWhileSequence takeWhileSequence) {
        return takeWhileSequence.sequence;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>(this){
            private final Iterator<T> iterator;
            private T nextItem;
            private int nextState;
            final TakeWhileSequence this$0;
            {
                this.this$0 = takeWhileSequence;
                this.iterator = TakeWhileSequence.access$getSequence$p(takeWhileSequence).iterator();
                this.nextState = -1;
            }

            private final void calcNext() {
                if (this.iterator.hasNext()) {
                    T t = this.iterator.next();
                    if (((Boolean)TakeWhileSequence.access$getPredicate$p(this.this$0).invoke(t)).booleanValue()) {
                        this.nextState = 1;
                        this.nextItem = t;
                        return;
                    }
                }
                this.nextState = 0;
            }

            public final Iterator<T> getIterator() {
                return this.iterator;
            }

            public final T getNextItem() {
                return this.nextItem;
            }

            public final int getNextState() {
                return this.nextState;
            }

            public boolean hasNext() {
                if (this.nextState == -1) {
                    this.calcNext();
                }
                int n = this.nextState;
                boolean bl = true;
                if (n != 1) {
                    bl = false;
                }
                return bl;
            }

            public T next() {
                if (this.nextState == -1) {
                    this.calcNext();
                }
                if (this.nextState != 0) {
                    T t = this.nextItem;
                    this.nextItem = null;
                    this.nextState = -1;
                    return t;
                }
                throw (Throwable)new NoSuchElementException();
            }

            public void remove() {
                throw new UnsupportedOperationException("Operation is not supported for read-only collection");
            }

            public final void setNextItem(T t) {
                this.nextItem = t;
            }

            public final void setNextState(int n) {
                this.nextState = n;
            }
        };
    }
}

