/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import java.lang.annotation.Annotation;
import java.util.List;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeReference$WhenMappings;
import kotlin.reflect.KClass;
import kotlin.reflect.KClassifier;
import kotlin.reflect.KType;
import kotlin.reflect.KTypeProjection;

@Metadata(bv={1, 0, 3}, d1={"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u001b\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B#\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\b\u0010\u0017\u001a\u00020\u0013H\u0002J\u0013\u0010\u0018\u001a\u00020\b2\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0096\u0002J\b\u0010\u001b\u001a\u00020\u001cH\u0016J\b\u0010\u001d\u001a\u00020\u0013H\u0016J\f\u0010\u0017\u001a\u00020\u0013*\u00020\u0006H\u0002R\u001a\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u00058VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\rR\u001a\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\rR\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0014\u0010\u0007\u001a\u00020\bX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\u0011R\u001c\u0010\u0012\u001a\u00020\u0013*\u0006\u0012\u0002\b\u00030\u00148BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0015\u0010\u0016\u00a8\u0006\u001e"}, d2={"Lkotlin/jvm/internal/TypeReference;", "Lkotlin/reflect/KType;", "classifier", "Lkotlin/reflect/KClassifier;", "arguments", "", "Lkotlin/reflect/KTypeProjection;", "isMarkedNullable", "", "(Lkotlin/reflect/KClassifier;Ljava/util/List;Z)V", "annotations", "", "getAnnotations", "()Ljava/util/List;", "getArguments", "getClassifier", "()Lkotlin/reflect/KClassifier;", "()Z", "arrayClassName", "", "Ljava/lang/Class;", "getArrayClassName", "(Ljava/lang/Class;)Ljava/lang/String;", "asString", "equals", "other", "", "hashCode", "", "toString", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class TypeReference
implements KType {
    private final List<KTypeProjection> arguments;
    private final KClassifier classifier;
    private final boolean isMarkedNullable;

    public TypeReference(KClassifier kClassifier, List<KTypeProjection> list, boolean bl) {
        Intrinsics.checkNotNullParameter(kClassifier, "classifier");
        Intrinsics.checkNotNullParameter(list, "arguments");
        this.classifier = kClassifier;
        this.arguments = list;
        this.isMarkedNullable = bl;
    }

    public static final /* synthetic */ String access$asString(TypeReference typeReference, KTypeProjection kTypeProjection) {
        return typeReference.asString(kTypeProjection);
    }

    private final String asString() {
        Object object = this.getClassifier();
        boolean bl = object instanceof KClass;
        String string2 = null;
        if (!bl) {
            object = null;
        }
        Object object2 = (KClass)object;
        object = string2;
        if (object2 != null) {
            object = JvmClassMappingKt.getJavaClass(object2);
        }
        object = object == null ? this.getClassifier().toString() : (((Class)object).isArray() ? this.getArrayClassName((Class<?>)object) : ((Class)object).getName());
        bl = this.getArguments().isEmpty();
        object2 = "";
        string2 = bl ? "" : CollectionsKt.joinToString$default(this.getArguments(), ", ", "<", ">", 0, null, new Function1<KTypeProjection, CharSequence>(this){
            final TypeReference this$0;
            {
                this.this$0 = typeReference;
                super(1);
            }

            public final CharSequence invoke(KTypeProjection kTypeProjection) {
                Intrinsics.checkNotNullParameter(kTypeProjection, "it");
                return TypeReference.access$asString(this.this$0, kTypeProjection);
            }
        }, 24, null);
        if (this.isMarkedNullable()) {
            object2 = "?";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append((String)object);
        stringBuilder.append(string2);
        stringBuilder.append((String)object2);
        return stringBuilder.toString();
    }

    private final String asString(KTypeProjection object) {
        block9: {
            if (((KTypeProjection)object).getVariance() == null) {
                return "*";
            }
            KType kType = ((KTypeProjection)object).getType();
            Object object2 = kType;
            if (!(kType instanceof TypeReference)) {
                object2 = null;
            }
            if ((object2 = (TypeReference)object2) == null || (object2 = super.asString()) == null) {
                object2 = String.valueOf(((KTypeProjection)object).getType());
            }
            object = ((KTypeProjection)object).getVariance();
            if (object != null) {
                switch (TypeReference$WhenMappings.$EnumSwitchMapping$0[((Enum)object).ordinal()]) {
                    default: {
                        break block9;
                    }
                    case 3: {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("out ");
                        ((StringBuilder)object).append((String)object2);
                        object = ((StringBuilder)object).toString();
                        break;
                    }
                    case 2: {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("in ");
                        ((StringBuilder)object).append((String)object2);
                        object = ((StringBuilder)object).toString();
                        break;
                    }
                    case 1: {
                        object = object2;
                    }
                }
                return object;
            }
        }
        throw new NoWhenBranchMatchedException();
    }

    private final String getArrayClassName(Class<?> object) {
        object = Intrinsics.areEqual(object, boolean[].class) ? "kotlin.BooleanArray" : (Intrinsics.areEqual(object, char[].class) ? "kotlin.CharArray" : (Intrinsics.areEqual(object, byte[].class) ? "kotlin.ByteArray" : (Intrinsics.areEqual(object, short[].class) ? "kotlin.ShortArray" : (Intrinsics.areEqual(object, int[].class) ? "kotlin.IntArray" : (Intrinsics.areEqual(object, float[].class) ? "kotlin.FloatArray" : (Intrinsics.areEqual(object, long[].class) ? "kotlin.LongArray" : (Intrinsics.areEqual(object, double[].class) ? "kotlin.DoubleArray" : "kotlin.Array")))))));
        return object;
    }

    public boolean equals(Object object) {
        boolean bl = object instanceof TypeReference && Intrinsics.areEqual(this.getClassifier(), ((TypeReference)object).getClassifier()) && Intrinsics.areEqual(this.getArguments(), ((TypeReference)object).getArguments()) && this.isMarkedNullable() == ((TypeReference)object).isMarkedNullable();
        return bl;
    }

    @Override
    public List<Annotation> getAnnotations() {
        return CollectionsKt.emptyList();
    }

    @Override
    public List<KTypeProjection> getArguments() {
        return this.arguments;
    }

    @Override
    public KClassifier getClassifier() {
        return this.classifier;
    }

    public int hashCode() {
        return (this.getClassifier().hashCode() * 31 + ((Object)this.getArguments()).hashCode()) * 31 + ((Object)this.isMarkedNullable()).hashCode();
    }

    @Override
    public boolean isMarkedNullable() {
        return this.isMarkedNullable;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.asString());
        stringBuilder.append(" (Kotlin reflection is not available)");
        return stringBuilder.toString();
    }
}

