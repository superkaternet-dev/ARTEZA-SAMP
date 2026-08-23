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

@Metadata(bv={1, 0, 3}, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010(\n\u0002\b\u0002\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u0002*\u0004\b\u0002\u0010\u00032\b\u0012\u0004\u0012\u0002H\u00030\u0004BA\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0007\u0012\u0018\u0010\b\u001a\u0014\u0012\u0004\u0012\u00028\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00020\t0\u0007\u00a2\u0006\u0002\u0010\nJ\u000f\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00020\tH\u0096\u0002R \u0010\b\u001a\u0014\u0012\u0004\u0012\u00028\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00020\t0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2={"Lkotlin/sequences/FlatteningSequence;", "T", "R", "E", "Lkotlin/sequences/Sequence;", "sequence", "transformer", "Lkotlin/Function1;", "iterator", "", "(Lkotlin/sequences/Sequence;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class FlatteningSequence<T, R, E>
implements Sequence<E> {
    private final Function1<R, Iterator<E>> iterator;
    private final Sequence<T> sequence;
    private final Function1<T, R> transformer;

    public FlatteningSequence(Sequence<? extends T> sequence, Function1<? super T, ? extends R> function1, Function1<? super R, ? extends Iterator<? extends E>> function12) {
        Intrinsics.checkNotNullParameter(sequence, "sequence");
        Intrinsics.checkNotNullParameter(function1, "transformer");
        Intrinsics.checkNotNullParameter(function12, "iterator");
        this.sequence = sequence;
        this.transformer = function1;
        this.iterator = function12;
    }

    public static final /* synthetic */ Function1 access$getIterator$p(FlatteningSequence flatteningSequence) {
        return flatteningSequence.iterator;
    }

    public static final /* synthetic */ Sequence access$getSequence$p(FlatteningSequence flatteningSequence) {
        return flatteningSequence.sequence;
    }

    public static final /* synthetic */ Function1 access$getTransformer$p(FlatteningSequence flatteningSequence) {
        return flatteningSequence.transformer;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>(this){
            private Iterator<? extends E> itemIterator;
            private final Iterator<T> iterator;
            final FlatteningSequence this$0;
            {
                this.this$0 = flatteningSequence;
                this.iterator = FlatteningSequence.access$getSequence$p(flatteningSequence).iterator();
            }

            private final boolean ensureItemIterator() {
                Iterator<E> iterator2 = this.itemIterator;
                if (iterator2 != null && !iterator2.hasNext()) {
                    this.itemIterator = null;
                }
                while (this.itemIterator == null) {
                    if (!this.iterator.hasNext()) {
                        return false;
                    }
                    iterator2 = this.iterator.next();
                    iterator2 = (Iterator<E>)FlatteningSequence.access$getIterator$p(this.this$0).invoke(FlatteningSequence.access$getTransformer$p(this.this$0).invoke(iterator2));
                    if (!iterator2.hasNext()) continue;
                    this.itemIterator = iterator2;
                    return true;
                }
                return true;
            }

            public final Iterator<E> getItemIterator() {
                return this.itemIterator;
            }

            public final Iterator<T> getIterator() {
                return this.iterator;
            }

            public boolean hasNext() {
                return this.ensureItemIterator();
            }

            public E next() {
                if (this.ensureItemIterator()) {
                    Iterator<E> iterator2 = this.itemIterator;
                    Intrinsics.checkNotNull(iterator2);
                    return iterator2.next();
                }
                throw (Throwable)new NoSuchElementException();
            }

            public void remove() {
                throw new UnsupportedOperationException("Operation is not supported for read-only collection");
            }

            public final void setItemIterator(Iterator<? extends E> iterator2) {
                this.itemIterator = iterator2;
            }
        };
    }
}

