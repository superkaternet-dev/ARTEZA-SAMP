/*
 * Decompiled with CFR 0.152.
 */
package kotlin.coroutines.intrinsics;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.jvm.internal.BaseContinuationImpl;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.coroutines.jvm.internal.RestrictedContinuationImpl;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;

@Metadata(bv={1, 0, 3}, d1={"\u0000.\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\u001aF\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001\"\u0004\b\u0000\u0010\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u00012\u001c\b\u0004\u0010\u0005\u001a\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006H\u0083\b\u00a2\u0006\u0002\b\b\u001aD\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001\"\u0004\b\u0000\u0010\u0003*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u00062\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\n\u001a]\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001\"\u0004\b\u0000\u0010\u000b\"\u0004\b\u0001\u0010\u0003*#\b\u0001\u0012\u0004\u0012\u0002H\u000b\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\f\u00a2\u0006\u0002\b\r2\u0006\u0010\u000e\u001a\u0002H\u000b2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000f\u001a\u001e\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0007\u001aA\u0010\u0011\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0003*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u00062\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012\u001aZ\u0010\u0011\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u000b\"\u0004\b\u0001\u0010\u0003*#\b\u0001\u0012\u0004\u0012\u0002H\u000b\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\f\u00a2\u0006\u0002\b\r2\u0006\u0010\u000e\u001a\u0002H\u000b2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013\u001an\u0010\u0011\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u000b\"\u0004\b\u0001\u0010\u0014\"\u0004\b\u0002\u0010\u0003*)\b\u0001\u0012\u0004\u0012\u0002H\u000b\u0012\u0004\u0012\u0002H\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0015\u00a2\u0006\u0002\b\r2\u0006\u0010\u000e\u001a\u0002H\u000b2\u0006\u0010\u0016\u001a\u0002H\u00142\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0081\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0017\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0018"}, d2={"createCoroutineFromSuspendFunction", "Lkotlin/coroutines/Continuation;", "", "T", "completion", "block", "Lkotlin/Function1;", "", "createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt", "createCoroutineUnintercepted", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;", "R", "Lkotlin/Function2;", "Lkotlin/ExtensionFunctionType;", "receiver", "(Lkotlin/jvm/functions/Function2;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;", "intercepted", "startCoroutineUninterceptedOrReturn", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "(Lkotlin/jvm/functions/Function2;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "P", "Lkotlin/Function3;", "param", "(Lkotlin/jvm/functions/Function3;Ljava/lang/Object;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlin-stdlib"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/coroutines/intrinsics/IntrinsicsKt")
class IntrinsicsKt__IntrinsicsJvmKt {
    private static final <T> Continuation<Unit> createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt(Continuation<? super T> continuation, Function1<? super Continuation<? super T>, ? extends Object> function1) {
        block5: {
            block4: {
                CoroutineContext coroutineContext;
                block2: {
                    block3: {
                        coroutineContext = continuation.getContext();
                        if (coroutineContext != EmptyCoroutineContext.INSTANCE) break block2;
                        if (continuation == null) break block3;
                        continuation = new RestrictedContinuationImpl(function1, continuation, continuation){
                            final Function1 $block;
                            final Continuation $completion;
                            private int label;
                            {
                                this.$block = function1;
                                this.$completion = continuation;
                                super(continuation2);
                            }

                            protected Object invokeSuspend(Object object) {
                                switch (this.label) {
                                    default: {
                                        throw (Throwable)new IllegalStateException("This coroutine had already completed".toString());
                                    }
                                    case 1: {
                                        this.label = 2;
                                        ResultKt.throwOnFailure(object);
                                        break;
                                    }
                                    case 0: {
                                        this.label = 1;
                                        ResultKt.throwOnFailure(object);
                                        object = this.$block.invoke(this);
                                    }
                                }
                                return object;
                            }
                        };
                        break block4;
                    }
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
                }
                if (continuation == null) break block5;
                continuation = new ContinuationImpl(function1, continuation, coroutineContext, continuation, coroutineContext){
                    final Function1 $block;
                    final Continuation $completion;
                    final CoroutineContext $context;
                    private int label;
                    {
                        this.$block = function1;
                        this.$completion = continuation;
                        this.$context = coroutineContext;
                        super(continuation2, coroutineContext2);
                    }

                    protected Object invokeSuspend(Object object) {
                        switch (this.label) {
                            default: {
                                throw (Throwable)new IllegalStateException("This coroutine had already completed".toString());
                            }
                            case 1: {
                                this.label = 2;
                                ResultKt.throwOnFailure(object);
                                break;
                            }
                            case 0: {
                                this.label = 1;
                                ResultKt.throwOnFailure(object);
                                object = this.$block.invoke(this);
                            }
                        }
                        return object;
                    }
                };
            }
            return continuation;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
    }

    public static final <T> Continuation<Unit> createCoroutineUnintercepted(Function1<? super Continuation<? super T>, ? extends Object> object, Continuation<? super T> object2) {
        block6: {
            block3: {
                Continuation<T> continuation;
                block4: {
                    block5: {
                        block2: {
                            Intrinsics.checkNotNullParameter(object, "$this$createCoroutineUnintercepted");
                            Intrinsics.checkNotNullParameter(object2, "completion");
                            continuation = DebugProbesKt.probeCoroutineCreated(object2);
                            if (!(object instanceof BaseContinuationImpl)) break block2;
                            object = ((BaseContinuationImpl)object).create(continuation);
                            break block3;
                        }
                        object2 = continuation.getContext();
                        if (object2 != EmptyCoroutineContext.INSTANCE) break block4;
                        if (continuation == null) break block5;
                        object = new RestrictedContinuationImpl(continuation, continuation, (Function1)object){
                            final Continuation $completion;
                            final Function1 $this_createCoroutineUnintercepted$inlined;
                            private int label;
                            {
                                this.$completion = continuation;
                                this.$this_createCoroutineUnintercepted$inlined = function1;
                                super(continuation2);
                            }

                            protected Object invokeSuspend(Object object) {
                                block4: {
                                    switch (this.label) {
                                        default: {
                                            throw (Throwable)new IllegalStateException("This coroutine had already completed".toString());
                                        }
                                        case 1: {
                                            this.label = 2;
                                            ResultKt.throwOnFailure(object);
                                            break;
                                        }
                                        case 0: {
                                            this.label = 1;
                                            ResultKt.throwOnFailure(object);
                                            object = this;
                                            Function1 function1 = this.$this_createCoroutineUnintercepted$inlined;
                                            if (function1 == null) break block4;
                                            object = ((Function1)TypeIntrinsics.beforeCheckcastToFunctionOfArity(function1, 1)).invoke(object);
                                        }
                                    }
                                    return object;
                                }
                                throw new NullPointerException("null cannot be cast to non-null type (kotlin.coroutines.Continuation<T>) -> kotlin.Any?");
                            }
                        };
                        break block3;
                    }
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
                }
                if (continuation == null) break block6;
                object = new ContinuationImpl(continuation, (CoroutineContext)object2, continuation, (CoroutineContext)object2, (Function1)object){
                    final Continuation $completion;
                    final CoroutineContext $context;
                    final Function1 $this_createCoroutineUnintercepted$inlined;
                    private int label;
                    {
                        this.$completion = continuation;
                        this.$context = coroutineContext;
                        this.$this_createCoroutineUnintercepted$inlined = function1;
                        super(continuation2, coroutineContext2);
                    }

                    protected Object invokeSuspend(Object object) {
                        block4: {
                            switch (this.label) {
                                default: {
                                    throw (Throwable)new IllegalStateException("This coroutine had already completed".toString());
                                }
                                case 1: {
                                    this.label = 2;
                                    ResultKt.throwOnFailure(object);
                                    break;
                                }
                                case 0: {
                                    this.label = 1;
                                    ResultKt.throwOnFailure(object);
                                    object = this;
                                    Function1 function1 = this.$this_createCoroutineUnintercepted$inlined;
                                    if (function1 == null) break block4;
                                    object = ((Function1)TypeIntrinsics.beforeCheckcastToFunctionOfArity(function1, 1)).invoke(object);
                                }
                            }
                            return object;
                        }
                        throw new NullPointerException("null cannot be cast to non-null type (kotlin.coroutines.Continuation<T>) -> kotlin.Any?");
                    }
                };
            }
            return object;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
    }

    public static final <R, T> Continuation<Unit> createCoroutineUnintercepted(Function2<? super R, ? super Continuation<? super T>, ? extends Object> object, R r, Continuation<? super T> continuation) {
        block6: {
            block3: {
                CoroutineContext coroutineContext;
                block4: {
                    block5: {
                        block2: {
                            Intrinsics.checkNotNullParameter(object, "$this$createCoroutineUnintercepted");
                            Intrinsics.checkNotNullParameter(continuation, "completion");
                            continuation = DebugProbesKt.probeCoroutineCreated(continuation);
                            if (!(object instanceof BaseContinuationImpl)) break block2;
                            object = ((BaseContinuationImpl)object).create(r, continuation);
                            break block3;
                        }
                        coroutineContext = continuation.getContext();
                        if (coroutineContext != EmptyCoroutineContext.INSTANCE) break block4;
                        if (continuation == null) break block5;
                        object = new RestrictedContinuationImpl(continuation, continuation, (Function2)object, r){
                            final Continuation $completion;
                            final Object $receiver$inlined;
                            final Function2 $this_createCoroutineUnintercepted$inlined;
                            private int label;
                            {
                                this.$completion = continuation;
                                this.$this_createCoroutineUnintercepted$inlined = function2;
                                this.$receiver$inlined = object;
                                super(continuation2);
                            }

                            protected Object invokeSuspend(Object object) {
                                block4: {
                                    switch (this.label) {
                                        default: {
                                            throw (Throwable)new IllegalStateException("This coroutine had already completed".toString());
                                        }
                                        case 1: {
                                            this.label = 2;
                                            ResultKt.throwOnFailure(object);
                                            break;
                                        }
                                        case 0: {
                                            this.label = 1;
                                            ResultKt.throwOnFailure(object);
                                            Continuation continuation = this;
                                            object = this.$this_createCoroutineUnintercepted$inlined;
                                            if (object == null) break block4;
                                            object = ((Function2)TypeIntrinsics.beforeCheckcastToFunctionOfArity(object, 2)).invoke(this.$receiver$inlined, continuation);
                                        }
                                    }
                                    return object;
                                }
                                throw new NullPointerException("null cannot be cast to non-null type (R, kotlin.coroutines.Continuation<T>) -> kotlin.Any?");
                            }
                        };
                        break block3;
                    }
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
                }
                if (continuation == null) break block6;
                object = new ContinuationImpl(continuation, coroutineContext, continuation, coroutineContext, (Function2)object, r){
                    final Continuation $completion;
                    final CoroutineContext $context;
                    final Object $receiver$inlined;
                    final Function2 $this_createCoroutineUnintercepted$inlined;
                    private int label;
                    {
                        this.$completion = continuation;
                        this.$context = coroutineContext;
                        this.$this_createCoroutineUnintercepted$inlined = function2;
                        this.$receiver$inlined = object;
                        super(continuation2, coroutineContext2);
                    }

                    protected Object invokeSuspend(Object object) {
                        block4: {
                            switch (this.label) {
                                default: {
                                    throw (Throwable)new IllegalStateException("This coroutine had already completed".toString());
                                }
                                case 1: {
                                    this.label = 2;
                                    ResultKt.throwOnFailure(object);
                                    break;
                                }
                                case 0: {
                                    this.label = 1;
                                    ResultKt.throwOnFailure(object);
                                    Continuation continuation = this;
                                    object = this.$this_createCoroutineUnintercepted$inlined;
                                    if (object == null) break block4;
                                    object = ((Function2)TypeIntrinsics.beforeCheckcastToFunctionOfArity(object, 2)).invoke(this.$receiver$inlined, continuation);
                                }
                            }
                            return object;
                        }
                        throw new NullPointerException("null cannot be cast to non-null type (R, kotlin.coroutines.Continuation<T>) -> kotlin.Any?");
                    }
                };
            }
            return object;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.coroutines.Continuation<kotlin.Any?>");
    }

    public static final <T> Continuation<T> intercepted(Continuation<? super T> continuationImpl) {
        block0: {
            Intrinsics.checkNotNullParameter(continuationImpl, "$this$intercepted");
            Continuation<Object> continuation = !(continuationImpl instanceof ContinuationImpl) ? null : continuationImpl;
            continuation = continuation;
            if (continuation == null || (continuation = continuation.intercepted()) == null) break block0;
            continuationImpl = continuation;
        }
        return continuationImpl;
    }

    private static final <T> Object startCoroutineUninterceptedOrReturn(Function1<? super Continuation<? super T>, ? extends Object> function1, Continuation<? super T> continuation) {
        if (function1 != null) {
            return ((Function1)TypeIntrinsics.beforeCheckcastToFunctionOfArity(function1, 1)).invoke(continuation);
        }
        throw new NullPointerException("null cannot be cast to non-null type (kotlin.coroutines.Continuation<T>) -> kotlin.Any?");
    }

    private static final <R, T> Object startCoroutineUninterceptedOrReturn(Function2<? super R, ? super Continuation<? super T>, ? extends Object> function2, R r, Continuation<? super T> continuation) {
        if (function2 != null) {
            return ((Function2)TypeIntrinsics.beforeCheckcastToFunctionOfArity(function2, 2)).invoke(r, continuation);
        }
        throw new NullPointerException("null cannot be cast to non-null type (R, kotlin.coroutines.Continuation<T>) -> kotlin.Any?");
    }

    private static final <R, P, T> Object startCoroutineUninterceptedOrReturn(Function3<? super R, ? super P, ? super Continuation<? super T>, ? extends Object> function3, R r, P p, Continuation<? super T> continuation) {
        if (function3 != null) {
            return ((Function3)TypeIntrinsics.beforeCheckcastToFunctionOfArity(function3, 3)).invoke(r, p, continuation);
        }
        throw new NullPointerException("null cannot be cast to non-null type (R, P, kotlin.coroutines.Continuation<T>) -> kotlin.Any?");
    }
}

