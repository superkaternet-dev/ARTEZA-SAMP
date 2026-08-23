/*
 * Decompiled with CFR 0.152.
 */
package kotlin.sequences;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.FlatteningSequence;
import kotlin.sequences.Sequence;

@Metadata(bv={1, 0, 3}, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010(\n\u0002\b\u0002\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u00022\b\u0012\u0004\u0012\u0002H\u00020\u0003B'\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003\u0012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0006\u00a2\u0006\u0002\u0010\u0007J3\u0010\b\u001a\b\u0012\u0004\u0012\u0002H\t0\u0003\"\u0004\b\u0002\u0010\t2\u0018\u0010\n\u001a\u0014\u0012\u0004\u0012\u00028\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\t0\u000b0\u0006H\u0000\u00a2\u0006\u0002\b\fJ\u000f\u0010\n\u001a\b\u0012\u0004\u0012\u00028\u00010\u000bH\u0096\u0002R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2={"Lkotlin/sequences/TransformingSequence;", "T", "R", "Lkotlin/sequences/Sequence;", "sequence", "transformer", "Lkotlin/Function1;", "(Lkotlin/sequences/Sequence;Lkotlin/jvm/functions/Function1;)V", "flatten", "E", "iterator", "", "flatten$kotlin_stdlib", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class TransformingSequence<T, R>
implements Sequence<R> {
    private final Sequence<T> sequence;
    private final Function1<T, R> transformer;

    public TransformingSequence(Sequence<? extends T> sequence, Function1<? super T, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(sequence, "sequence");
        Intrinsics.checkNotNullParameter(function1, "transformer");
        this.sequence = sequence;
        this.transformer = function1;
    }

    public static final /* synthetic */ Sequence access$getSequence$p(TransformingSequence transformingSequence) {
        return transformingSequence.sequence;
    }

    public static final /* synthetic */ Function1 access$getTransformer$p(TransformingSequence transformingSequence) {
        return transformingSequence.transformer;
    }

    public final <E> Sequence<E> flatten$kotlin_stdlib(Function1<? super R, ? extends Iterator<? extends E>> function1) {
        Intrinsics.checkNotNullParameter(function1, "iterator");
        return new FlatteningSequence(this.sequence, this.transformer, function1);
    }

    @Override
    public Iterator<R> iterator() {
        return new Iterator<R>(this){
            private final Iterator<T> iterator;
            final TransformingSequence this$0;
            {
                this.this$0 = transformingSequence;
                this.iterator = TransformingSequence.access$getSequence$p(transformingSequence).iterator();
            }

            public final Iterator<T> getIterator() {
                return this.iterator;
            }

            public boolean hasNext() {
                return this.iterator.hasNext();
            }

            public R next() {
                return TransformingSequence.access$getTransformer$p(this.this$0).invoke(this.iterator.next());
            }

            public void remove() {
                throw new UnsupportedOperationException("Operation is not supported for read-only collection");
            }
        };
    }
}

