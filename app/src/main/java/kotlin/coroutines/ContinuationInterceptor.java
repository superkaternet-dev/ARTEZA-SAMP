/*
 * Decompiled with CFR 0.152.
 */
package kotlin.coroutines;

import kotlin.Metadata;
import kotlin.coroutines.AbstractCoroutineContextKey;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv={1, 0, 3}, d1={"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\bg\u0018\u0000 \u000f2\u00020\u0001:\u0001\u000fJ(\u0010\u0002\u001a\u0004\u0018\u0001H\u0003\"\b\b\u0000\u0010\u0003*\u00020\u00012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0005H\u0096\u0002\u00a2\u0006\u0002\u0010\u0006J\"\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\t0\b\"\u0004\b\u0000\u0010\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\t0\bH&J\u0014\u0010\u000b\u001a\u00020\f2\n\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005H\u0016J\u0014\u0010\r\u001a\u00020\u000e2\n\u0010\n\u001a\u0006\u0012\u0002\b\u00030\bH\u0016\u00a8\u0006\u0010"}, d2={"Lkotlin/coroutines/ContinuationInterceptor;", "Lkotlin/coroutines/CoroutineContext$Element;", "get", "E", "key", "Lkotlin/coroutines/CoroutineContext$Key;", "(Lkotlin/coroutines/CoroutineContext$Key;)Lkotlin/coroutines/CoroutineContext$Element;", "interceptContinuation", "Lkotlin/coroutines/Continuation;", "T", "continuation", "minusKey", "Lkotlin/coroutines/CoroutineContext;", "releaseInterceptedContinuation", "", "Key", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public interface ContinuationInterceptor
extends CoroutineContext.Element {
    public static final Key Key = kotlin.coroutines.ContinuationInterceptor$Key.$$INSTANCE;

    @Override
    public <E extends CoroutineContext.Element> E get(CoroutineContext.Key<E> var1);

    public <T> Continuation<T> interceptContinuation(Continuation<? super T> var1);

    @Override
    public CoroutineContext minusKey(CoroutineContext.Key<?> var1);

    public void releaseInterceptedContinuation(Continuation<?> var1);

    @Metadata(bv={1, 0, 3}, k=3, mv={1, 4, 1})
    public static final class DefaultImpls {
        public static <R> R fold(ContinuationInterceptor continuationInterceptor, R r, Function2<? super R, ? super CoroutineContext.Element, ? extends R> function2) {
            Intrinsics.checkNotNullParameter(function2, "operation");
            return CoroutineContext.Element.DefaultImpls.fold(continuationInterceptor, r, function2);
        }

        public static <E extends CoroutineContext.Element> E get(ContinuationInterceptor continuationInterceptor, CoroutineContext.Key<E> key) {
            Intrinsics.checkNotNullParameter(key, "key");
            boolean bl = key instanceof AbstractCoroutineContextKey;
            CoroutineContext.Element element = null;
            CoroutineContext.Element element2 = null;
            if (bl) {
                element = element2;
                if (((AbstractCoroutineContextKey)key).isSubKey$kotlin_stdlib(continuationInterceptor.getKey()) && !((element = ((AbstractCoroutineContextKey)key).tryCast$kotlin_stdlib(continuationInterceptor)) instanceof CoroutineContext.Element)) {
                    element = element2;
                }
                return (E)element;
            }
            if (Key == key) {
                if (continuationInterceptor != null) {
                    element = continuationInterceptor;
                } else {
                    throw new NullPointerException("null cannot be cast to non-null type E");
                }
            }
            return (E)element;
        }

        public static CoroutineContext minusKey(ContinuationInterceptor coroutineContext, CoroutineContext.Key<?> key) {
            Intrinsics.checkNotNullParameter(key, "key");
            if (key instanceof AbstractCoroutineContextKey) {
                coroutineContext = ((AbstractCoroutineContextKey)key).isSubKey$kotlin_stdlib(coroutineContext.getKey()) && ((AbstractCoroutineContextKey)key).tryCast$kotlin_stdlib((CoroutineContext.Element)coroutineContext) != null ? (CoroutineContext)EmptyCoroutineContext.INSTANCE : (CoroutineContext)coroutineContext;
                return coroutineContext;
            }
            coroutineContext = Key == key ? (CoroutineContext)EmptyCoroutineContext.INSTANCE : (CoroutineContext)coroutineContext;
            return coroutineContext;
        }

        public static CoroutineContext plus(ContinuationInterceptor continuationInterceptor, CoroutineContext coroutineContext) {
            Intrinsics.checkNotNullParameter(coroutineContext, "context");
            return CoroutineContext.Element.DefaultImpls.plus(continuationInterceptor, coroutineContext);
        }

        public static void releaseInterceptedContinuation(ContinuationInterceptor continuationInterceptor, Continuation<?> continuation) {
            Intrinsics.checkNotNullParameter(continuation, "continuation");
        }
    }

    @Metadata(bv={1, 0, 3}, d1={"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2={"Lkotlin/coroutines/ContinuationInterceptor$Key;", "Lkotlin/coroutines/CoroutineContext$Key;", "Lkotlin/coroutines/ContinuationInterceptor;", "()V", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
    public static final class Key
    implements CoroutineContext.Key<ContinuationInterceptor> {
        static final Key $$INSTANCE = new Key();

        private Key() {
        }
    }
}

