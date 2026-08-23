/*
 * Decompiled with CFR 0.152.
 */
package kotlin.sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;

@Metadata(bv={1, 0, 3}, d1={"\u0000$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010(\n\u0000\b\u0002\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003B+\u0012\u000e\u0010\u0004\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00000\u0005\u0012\u0014\u0010\u0006\u001a\u0010\u0012\u0004\u0012\u00028\u0000\u0012\u0006\u0012\u0004\u0018\u00018\u00000\u0007\u00a2\u0006\u0002\u0010\bJ\u000f\u0010\t\u001a\b\u0012\u0004\u0012\u00028\u00000\nH\u0096\u0002R\u0016\u0010\u0004\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00000\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0006\u001a\u0010\u0012\u0004\u0012\u00028\u0000\u0012\u0006\u0012\u0004\u0018\u00018\u00000\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2={"Lkotlin/sequences/GeneratorSequence;", "T", "", "Lkotlin/sequences/Sequence;", "getInitialValue", "Lkotlin/Function0;", "getNextValue", "Lkotlin/Function1;", "(Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;)V", "iterator", "", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
final class GeneratorSequence<T>
implements Sequence<T> {
    private final Function0<T> getInitialValue;
    private final Function1<T, T> getNextValue;

    public GeneratorSequence(Function0<? extends T> function0, Function1<? super T, ? extends T> function1) {
        Intrinsics.checkNotNullParameter(function0, "getInitialValue");
        Intrinsics.checkNotNullParameter(function1, "getNextValue");
        this.getInitialValue = function0;
        this.getNextValue = function1;
    }

    public static final /* synthetic */ Function0 access$getGetInitialValue$p(GeneratorSequence generatorSequence) {
        return generatorSequence.getInitialValue;
    }

    public static final /* synthetic */ Function1 access$getGetNextValue$p(GeneratorSequence generatorSequence) {
        return generatorSequence.getNextValue;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>(this){
            private T nextItem;
            private int nextState;
            final GeneratorSequence this$0;
            {
                this.this$0 = generatorSequence;
                this.nextState = -2;
            }

            private final void calcNext() {
                Function1<P1, R> function1;
                if (this.nextState == -2) {
                    function1 = GeneratorSequence.access$getGetInitialValue$p(this.this$0).invoke();
                } else {
                    function1 = GeneratorSequence.access$getGetNextValue$p(this.this$0);
                    T t = this.nextItem;
                    Intrinsics.checkNotNull(t);
                    function1 = function1.invoke(t);
                }
                this.nextItem = function1;
                int n = function1 == null ? 0 : 1;
                this.nextState = n;
            }

            public final T getNextItem() {
                return this.nextItem;
            }

            public final int getNextState() {
                return this.nextState;
            }

            public boolean hasNext() {
                if (this.nextState < 0) {
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
                if (this.nextState < 0) {
                    this.calcNext();
                }
                if (this.nextState != 0) {
                    T t = this.nextItem;
                    if (t != null) {
                        this.nextState = -1;
                        return t;
                    }
                    throw new NullPointerException("null cannot be cast to non-null type T");
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

