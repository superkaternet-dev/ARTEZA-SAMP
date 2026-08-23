/*
 * Decompiled with CFR 0.152.
 */
package kotlin.sequences;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
import kotlin.sequences.SequenceScope;

@Metadata(bv={1, 0, 3}, d1={"\u0000B\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\b\u0012\u0004\u0012\u00020\u00050\u0004B\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u0016\u001a\u00020\u0017H\u0002J\t\u0010\u0018\u001a\u00020\u0019H\u0096\u0002J\u000e\u0010\u001a\u001a\u00028\u0000H\u0096\u0002\u00a2\u0006\u0002\u0010\u001bJ\r\u0010\u001c\u001a\u00028\u0000H\u0002\u00a2\u0006\u0002\u0010\u001bJ\u001e\u0010\u001d\u001a\u00020\u00052\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00050\u001fH\u0016\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010 J\u0019\u0010!\u001a\u00020\u00052\u0006\u0010\"\u001a\u00028\u0000H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010#J\u001f\u0010$\u001a\u00020\u00052\f\u0010%\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010&R\u0014\u0010\u0007\u001a\u00020\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0016\u0010\u000b\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\"\u0010\f\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u0012\u0010\u0011\u001a\u0004\u0018\u00018\u0000X\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0012R\u0012\u0010\u0013\u001a\u00060\u0014j\u0002`\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006'"}, d2={"Lkotlin/sequences/SequenceBuilderIterator;", "T", "Lkotlin/sequences/SequenceScope;", "", "Lkotlin/coroutines/Continuation;", "", "()V", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "nextIterator", "nextStep", "getNextStep", "()Lkotlin/coroutines/Continuation;", "setNextStep", "(Lkotlin/coroutines/Continuation;)V", "nextValue", "Ljava/lang/Object;", "state", "", "Lkotlin/sequences/State;", "exceptionalState", "", "hasNext", "", "next", "()Ljava/lang/Object;", "nextNotReady", "resumeWith", "result", "Lkotlin/Result;", "(Ljava/lang/Object;)V", "yield", "value", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "yieldAll", "iterator", "(Ljava/util/Iterator;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
final class SequenceBuilderIterator<T>
extends SequenceScope<T>
implements Iterator<T>,
Continuation<Unit>,
KMappedMarker {
    private Iterator<? extends T> nextIterator;
    private Continuation<? super Unit> nextStep;
    private T nextValue;
    private int state;

    private final Throwable exceptionalState() {
        Serializable serializable;
        switch (this.state) {
            default: {
                serializable = new StringBuilder();
                ((StringBuilder)serializable).append("Unexpected state of the iterator: ");
                ((StringBuilder)serializable).append(this.state);
                serializable = new IllegalStateException(((StringBuilder)serializable).toString());
                break;
            }
            case 5: {
                serializable = new IllegalStateException("Iterator has failed.");
                break;
            }
            case 4: {
                serializable = new NoSuchElementException();
            }
        }
        return serializable;
    }

    private final T nextNotReady() {
        if (this.hasNext()) {
            return this.next();
        }
        throw (Throwable)new NoSuchElementException();
    }

    @Override
    public CoroutineContext getContext() {
        return EmptyCoroutineContext.INSTANCE;
    }

    public final Continuation<Unit> getNextStep() {
        return this.nextStep;
    }

    @Override
    public boolean hasNext() {
        while (true) {
            Object object;
            switch (this.state) {
                default: {
                    throw this.exceptionalState();
                }
                case 4: {
                    return false;
                }
                case 2: 
                case 3: {
                    return true;
                }
                case 1: {
                    object = this.nextIterator;
                    Intrinsics.checkNotNull(object);
                    if (object.hasNext()) {
                        this.state = 2;
                        return true;
                    }
                    this.nextIterator = null;
                }
                case 0: 
            }
            this.state = 5;
            object = this.nextStep;
            Intrinsics.checkNotNull(object);
            this.nextStep = null;
            Unit unit = Unit.INSTANCE;
            Result.Companion companion = Result.Companion;
            object.resumeWith(Result.constructor-impl(unit));
        }
    }

    @Override
    public T next() {
        switch (this.state) {
            default: {
                throw this.exceptionalState();
            }
            case 3: {
                this.state = 0;
                T t = this.nextValue;
                this.nextValue = null;
                return t;
            }
            case 2: {
                this.state = 1;
                Iterator<T> iterator2 = this.nextIterator;
                Intrinsics.checkNotNull(iterator2);
                return iterator2.next();
            }
            case 0: 
            case 1: 
        }
        return this.nextNotReady();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public void resumeWith(Object object) {
        ResultKt.throwOnFailure(object);
        this.state = 4;
    }

    public final void setNextStep(Continuation<? super Unit> continuation) {
        this.nextStep = continuation;
    }

    @Override
    public Object yield(T object, Continuation<? super Unit> continuation) {
        this.nextValue = object;
        this.state = 3;
        this.nextStep = continuation;
        object = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (object == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        if (object == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            return object;
        }
        return Unit.INSTANCE;
    }

    @Override
    public Object yieldAll(Iterator<? extends T> object, Continuation<? super Unit> continuation) {
        if (!object.hasNext()) {
            return Unit.INSTANCE;
        }
        this.nextIterator = object;
        this.state = 2;
        this.nextStep = continuation;
        object = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (object == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        if (object == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            return object;
        }
        return Unit.INSTANCE;
    }
}

