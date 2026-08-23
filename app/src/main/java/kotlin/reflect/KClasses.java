/*
 * Decompiled with CFR 0.152.
 */
package kotlin.reflect;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KClass;

@Metadata(bv={1, 0, 3}, d1={"\u0000\u0010\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a+\u0010\u0000\u001a\u0002H\u0001\"\b\b\u0000\u0010\u0001*\u00020\u0002*\b\u0012\u0004\u0012\u0002H\u00010\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0002H\u0007\u00a2\u0006\u0002\u0010\u0005\u001a-\u0010\u0006\u001a\u0004\u0018\u0001H\u0001\"\b\b\u0000\u0010\u0001*\u00020\u0002*\b\u0012\u0004\u0012\u0002H\u00010\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0002H\u0007\u00a2\u0006\u0002\u0010\u0005\u00a8\u0006\u0007"}, d2={"cast", "T", "", "Lkotlin/reflect/KClass;", "value", "(Lkotlin/reflect/KClass;Ljava/lang/Object;)Ljava/lang/Object;", "safeCast", "kotlin-stdlib"}, k=2, mv={1, 4, 1})
public final class KClasses {
    public static final <T> T cast(KClass<T> kClass, Object object) {
        Intrinsics.checkNotNullParameter(kClass, "$this$cast");
        if (kClass.isInstance(object)) {
            if (object != null) {
                return (T)object;
            }
            throw new NullPointerException("null cannot be cast to non-null type T");
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Value cannot be cast to ");
        ((StringBuilder)object).append(kClass.getQualifiedName());
        throw (Throwable)new ClassCastException(((StringBuilder)object).toString());
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final <T> T safeCast(KClass<T> object, Object object2) {
        Intrinsics.checkNotNullParameter(object, "$this$safeCast");
        if (object.isInstance(object2)) {
            if (object2 == null) throw new NullPointerException("null cannot be cast to non-null type T");
            object = object2;
            return (T)object;
        } else {
            object = null;
        }
        return (T)object;
    }
}

