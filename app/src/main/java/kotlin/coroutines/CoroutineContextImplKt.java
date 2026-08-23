/*
 * Decompiled with CFR 0.152.
 */
package kotlin.coroutines;

import kotlin.Metadata;
import kotlin.coroutines.AbstractCoroutineContextKey;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv={1, 0, 3}, d1={"\u0000\u0018\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a+\u0010\u0000\u001a\u0004\u0018\u0001H\u0001\"\b\b\u0000\u0010\u0001*\u00020\u0002*\u00020\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0004H\u0007\u00a2\u0006\u0002\u0010\u0005\u001a\u0018\u0010\u0006\u001a\u00020\u0007*\u00020\u00022\n\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u0004H\u0007\u00a8\u0006\b"}, d2={"getPolymorphicElement", "E", "Lkotlin/coroutines/CoroutineContext$Element;", "key", "Lkotlin/coroutines/CoroutineContext$Key;", "(Lkotlin/coroutines/CoroutineContext$Element;Lkotlin/coroutines/CoroutineContext$Key;)Lkotlin/coroutines/CoroutineContext$Element;", "minusPolymorphicKey", "Lkotlin/coroutines/CoroutineContext;", "kotlin-stdlib"}, k=2, mv={1, 4, 1})
public final class CoroutineContextImplKt {
    public static final <E extends CoroutineContext.Element> E getPolymorphicElement(CoroutineContext.Element element, CoroutineContext.Key<E> key) {
        Intrinsics.checkNotNullParameter(element, "$this$getPolymorphicElement");
        Intrinsics.checkNotNullParameter(key, "key");
        boolean bl = key instanceof AbstractCoroutineContextKey;
        CoroutineContext.Element element2 = null;
        CoroutineContext.Element element3 = null;
        if (bl) {
            element2 = element3;
            if (((AbstractCoroutineContextKey)key).isSubKey$kotlin_stdlib(element.getKey()) && !((element2 = ((AbstractCoroutineContextKey)key).tryCast$kotlin_stdlib(element)) instanceof CoroutineContext.Element)) {
                element2 = element3;
            }
            return (E)element2;
        }
        if (element.getKey() == key) {
            element2 = element;
        }
        return (E)element2;
    }

    public static final CoroutineContext minusPolymorphicKey(CoroutineContext.Element coroutineContext, CoroutineContext.Key<?> key) {
        Intrinsics.checkNotNullParameter(coroutineContext, "$this$minusPolymorphicKey");
        Intrinsics.checkNotNullParameter(key, "key");
        if (key instanceof AbstractCoroutineContextKey) {
            coroutineContext = ((AbstractCoroutineContextKey)key).isSubKey$kotlin_stdlib(coroutineContext.getKey()) && ((AbstractCoroutineContextKey)key).tryCast$kotlin_stdlib((CoroutineContext.Element)coroutineContext) != null ? (CoroutineContext)EmptyCoroutineContext.INSTANCE : (CoroutineContext)coroutineContext;
            return coroutineContext;
        }
        coroutineContext = coroutineContext.getKey() == key ? (CoroutineContext)EmptyCoroutineContext.INSTANCE : (CoroutineContext)coroutineContext;
        return coroutineContext;
    }
}

