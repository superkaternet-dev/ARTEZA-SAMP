/*
 * Decompiled with CFR 0.152.
 */
package kotlin;

import kotlin.DeepRecursiveFunction;
import kotlin.DeepRecursiveKt;
import kotlin.DeepRecursiveScope;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(bv={1, 0, 3}, d1={"\u0000B\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0003\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u00022\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u00032\b\u0012\u0004\u0012\u0002H\u00020\u0004BK\u00129\u0010\u0005\u001a5\b\u0001\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0003\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006\u00a2\u0006\u0002\b\b\u0012\u0006\u0010\t\u001a\u00028\u0000\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ\u0019\u0010\u0015\u001a\u00028\u00012\u0006\u0010\t\u001a\u00028\u0000H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0016Jc\u0010\u0017\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u000429\u0010\u0018\u001a5\b\u0001\u0012\f\u0012\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u0003\u0012\u0006\u0012\u0004\u0018\u00010\u0007\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006\u00a2\u0006\u0002\b\b2\u000e\u0010\u000b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0004H\u0002\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0019J\u001e\u0010\u001a\u001a\u00020\u001b2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00028\u00010\u0013H\u0016\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001cJ\u000b\u0010\u001d\u001a\u00028\u0001\u00a2\u0006\u0002\u0010\u001eJ5\u0010\u0015\u001a\u0002H\u001f\"\u0004\b\u0002\u0010 \"\u0004\b\u0003\u0010\u001f*\u000e\u0012\u0004\u0012\u0002H \u0012\u0004\u0012\u0002H\u001f0!2\u0006\u0010\t\u001a\u0002H H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\"R\u0018\u0010\u000b\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0007\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u00020\r8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fRF\u0010\u0010\u001a5\b\u0001\u0012\f\u0012\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u0003\u0012\u0006\u0012\u0004\u0018\u00010\u0007\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006\u00a2\u0006\u0002\b\bX\u0082\u000e\u00f8\u0001\u0000\u00a2\u0006\u0004\n\u0002\u0010\u0011R\u001e\u0010\u0012\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0013X\u0082\u000e\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\n\u0002\u0010\u0014R\u0010\u0010\t\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!\u00a8\u0006#"}, d2={"Lkotlin/DeepRecursiveScopeImpl;", "T", "R", "Lkotlin/DeepRecursiveScope;", "Lkotlin/coroutines/Continuation;", "block", "Lkotlin/Function3;", "", "Lkotlin/ExtensionFunctionType;", "value", "(Lkotlin/jvm/functions/Function3;Ljava/lang/Object;)V", "cont", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "function", "Lkotlin/jvm/functions/Function3;", "result", "Lkotlin/Result;", "Ljava/lang/Object;", "callRecursive", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "crossFunctionCompletion", "currentFunction", "(Lkotlin/jvm/functions/Function3;Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;", "resumeWith", "", "(Ljava/lang/Object;)V", "runCallLoop", "()Ljava/lang/Object;", "S", "U", "Lkotlin/DeepRecursiveFunction;", "(Lkotlin/DeepRecursiveFunction;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
final class DeepRecursiveScopeImpl<T, R>
extends DeepRecursiveScope<T, R>
implements Continuation<R> {
    private Continuation<Object> cont;
    private Function3<? super DeepRecursiveScope<?, ?>, Object, ? super Continuation<Object>, ? extends Object> function;
    private Object result;
    private Object value;

    public DeepRecursiveScopeImpl(Function3<? super DeepRecursiveScope<T, R>, ? super T, ? super Continuation<? super R>, ? extends Object> function3, T t) {
        Intrinsics.checkNotNullParameter(function3, "block");
        super(null);
        this.function = function3;
        this.value = t;
        this.cont = this;
        this.result = DeepRecursiveKt.access$getUNDEFINED_RESULT$p();
    }

    public static final /* synthetic */ Continuation access$getCont$p(DeepRecursiveScopeImpl deepRecursiveScopeImpl) {
        return deepRecursiveScopeImpl.cont;
    }

    public static final /* synthetic */ Function3 access$getFunction$p(DeepRecursiveScopeImpl deepRecursiveScopeImpl) {
        return deepRecursiveScopeImpl.function;
    }

    public static final /* synthetic */ Object access$getResult$p-d1pmJ48(DeepRecursiveScopeImpl deepRecursiveScopeImpl) {
        return deepRecursiveScopeImpl.result;
    }

    public static final /* synthetic */ void access$setCont$p(DeepRecursiveScopeImpl deepRecursiveScopeImpl, Continuation continuation) {
        deepRecursiveScopeImpl.cont = continuation;
    }

    public static final /* synthetic */ void access$setFunction$p(DeepRecursiveScopeImpl deepRecursiveScopeImpl, Function3 function3) {
        deepRecursiveScopeImpl.function = function3;
    }

    public static final /* synthetic */ void access$setResult$p(DeepRecursiveScopeImpl deepRecursiveScopeImpl, Object object) {
        deepRecursiveScopeImpl.result = object;
    }

    private final Continuation<Object> crossFunctionCompletion(Function3<? super DeepRecursiveScope<?, ?>, Object, ? super Continuation<Object>, ? extends Object> function3, Continuation<Object> continuation) {
        return new Continuation<Object>((CoroutineContext)EmptyCoroutineContext.INSTANCE, this, function3, continuation){
            final Continuation $cont$inlined;
            final CoroutineContext $context;
            final Function3 $currentFunction$inlined;
            final DeepRecursiveScopeImpl this$0;
            {
                this.$context = coroutineContext;
                this.this$0 = deepRecursiveScopeImpl;
                this.$currentFunction$inlined = function3;
                this.$cont$inlined = continuation;
            }

            public CoroutineContext getContext() {
                return this.$context;
            }

            public void resumeWith(Object object) {
                DeepRecursiveScopeImpl.access$setFunction$p(this.this$0, this.$currentFunction$inlined);
                DeepRecursiveScopeImpl.access$setCont$p(this.this$0, this.$cont$inlined);
                DeepRecursiveScopeImpl.access$setResult$p(this.this$0, object);
            }
        };
    }

    @Override
    public Object callRecursive(T object, Continuation<? super R> continuation) {
        if (continuation != null) {
            this.cont = continuation;
            this.value = object;
            object = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            if (object == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                DebugProbesKt.probeCoroutineSuspended(continuation);
            }
            return object;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
    }

    @Override
    public <U, S> Object callRecursive(DeepRecursiveFunction<U, S> function3, U u, Continuation<? super S> continuation) {
        block4: {
            block8: {
                block7: {
                    block5: {
                        block6: {
                            Function3<DeepRecursiveScope<U, S>, U, Continuation<S>, Object> function32 = ((DeepRecursiveFunction)((Object)function3)).getBlock$kotlin_stdlib();
                            if (function32 == null) break block4;
                            DeepRecursiveScopeImpl deepRecursiveScopeImpl = this;
                            function3 = deepRecursiveScopeImpl.function;
                            if (function32 == function3) break block5;
                            deepRecursiveScopeImpl.function = function32;
                            if (continuation == null) break block6;
                            deepRecursiveScopeImpl.cont = deepRecursiveScopeImpl.crossFunctionCompletion(function3, continuation);
                            break block7;
                        }
                        throw new NullPointerException("null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
                    }
                    if (continuation == null) break block8;
                    deepRecursiveScopeImpl.cont = continuation;
                }
                deepRecursiveScopeImpl.value = u;
                function3 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                if (function3 == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                    DebugProbesKt.probeCoroutineSuspended(continuation);
                }
                return function3;
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.DeepRecursiveFunctionBlock /* = suspend kotlin.DeepRecursiveScope<*, *>.(kotlin.Any?) -> kotlin.Any? */");
    }

    @Override
    public CoroutineContext getContext() {
        return EmptyCoroutineContext.INSTANCE;
    }

    @Override
    public void resumeWith(Object object) {
        this.cont = null;
        this.result = object;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final R runCallLoop() {
        while (true) {
            Object object = this.result;
            Continuation<Object> continuation = this.cont;
            if (continuation == null) {
                ResultKt.throwOnFailure(object);
                return (R)object;
            }
            if (Result.equals-impl0(DeepRecursiveKt.access$getUNDEFINED_RESULT$p(), object)) {
                block6: {
                    object = this.function;
                    Object object2 = this.value;
                    if (object == null) break block6;
                    if ((object = ((Function3)TypeIntrinsics.beforeCheckcastToFunctionOfArity(object, 3)).invoke(this, object2, continuation)) == IntrinsicsKt.getCOROUTINE_SUSPENDED()) continue;
                    object2 = Result.Companion;
                    continuation.resumeWith(Result.constructor-impl(object));
                    continue;
                }
                try {
                    object = new NullPointerException("null cannot be cast to non-null type (R, P, kotlin.coroutines.Continuation<T>) -> kotlin.Any?");
                    throw object;
                }
                catch (Throwable throwable) {
                    object = Result.Companion;
                    continuation.resumeWith(Result.constructor-impl(ResultKt.createFailure(throwable)));
                }
                continue;
            }
            this.result = DeepRecursiveKt.access$getUNDEFINED_RESULT$p();
            continuation.resumeWith(object);
        }
    }
}

