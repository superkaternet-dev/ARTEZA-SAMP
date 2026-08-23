/*
 * Decompiled with CFR 0.152.
 */
package kotlin.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.ParameterizedTypeImpl;
import kotlin.reflect.TypeImpl;
import kotlin.reflect.TypesJVMKt;

@Metadata(bv={1, 0, 3}, d1={"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u00012\u00020\u0002B)\u0012\n\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u0004\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00060\b\u00a2\u0006\u0002\u0010\tJ\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096\u0002J\u0013\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00060\nH\u0016\u00a2\u0006\u0002\u0010\u0011J\n\u0010\u0012\u001a\u0004\u0018\u00010\u0006H\u0016J\b\u0010\u0013\u001a\u00020\u0006H\u0016J\b\u0010\u0014\u001a\u00020\u0015H\u0016J\b\u0010\u0016\u001a\u00020\u0017H\u0016J\b\u0010\u0018\u001a\u00020\u0015H\u0016R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00060\nX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u000b\u00a8\u0006\u0019"}, d2={"Lkotlin/reflect/ParameterizedTypeImpl;", "Ljava/lang/reflect/ParameterizedType;", "Lkotlin/reflect/TypeImpl;", "rawType", "Ljava/lang/Class;", "ownerType", "Ljava/lang/reflect/Type;", "typeArguments", "", "(Ljava/lang/Class;Ljava/lang/reflect/Type;Ljava/util/List;)V", "", "[Ljava/lang/reflect/Type;", "equals", "", "other", "", "getActualTypeArguments", "()[Ljava/lang/reflect/Type;", "getOwnerType", "getRawType", "getTypeName", "", "hashCode", "", "toString", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
final class ParameterizedTypeImpl
implements ParameterizedType,
TypeImpl {
    private final Type ownerType;
    private final Class<?> rawType;
    private final Type[] typeArguments;

    public ParameterizedTypeImpl(Class<?> typeArray, Type type, List<? extends Type> list) {
        Intrinsics.checkNotNullParameter(typeArray, "rawType");
        Intrinsics.checkNotNullParameter(list, "typeArguments");
        this.rawType = typeArray;
        this.ownerType = type;
        typeArray = ((Collection)list).toArray(new Type[0]);
        if (typeArray != null) {
            this.typeArguments = typeArray;
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
    }

    public boolean equals(Object object) {
        boolean bl = object instanceof ParameterizedType && Intrinsics.areEqual(this.rawType, ((ParameterizedType)object).getRawType()) && Intrinsics.areEqual(this.ownerType, ((ParameterizedType)object).getOwnerType()) && Arrays.equals(this.getActualTypeArguments(), ((ParameterizedType)object).getActualTypeArguments());
        return bl;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return this.typeArguments;
    }

    @Override
    public Type getOwnerType() {
        return this.ownerType;
    }

    @Override
    public Type getRawType() {
        return this.rawType;
    }

    @Override
    public String getTypeName() {
        CharSequence charSequence = new StringBuilder();
        Object[] objectArray = this.ownerType;
        if (objectArray != null) {
            charSequence.append(TypesJVMKt.access$typeToString((Type)objectArray));
            charSequence.append("$");
            charSequence.append(this.rawType.getSimpleName());
        } else {
            charSequence.append(TypesJVMKt.access$typeToString(this.rawType));
        }
        objectArray = this.typeArguments;
        boolean bl = objectArray.length == 0;
        if (bl ^ true) {
            ArraysKt.joinTo$default(objectArray, (Appendable)((Object)charSequence), null, (CharSequence)"<", (CharSequence)">", 0, null, (Function1)getTypeName.1.1.INSTANCE, 50, null);
        }
        charSequence = charSequence.toString();
        Intrinsics.checkNotNullExpressionValue(charSequence, "StringBuilder().apply(builderAction).toString()");
        return charSequence;
    }

    public int hashCode() {
        int n = this.rawType.hashCode();
        Type type = this.ownerType;
        int n2 = type != null ? type.hashCode() : 0;
        return n ^ n2 ^ Arrays.hashCode(this.getActualTypeArguments());
    }

    public String toString() {
        return this.getTypeName();
    }
}

