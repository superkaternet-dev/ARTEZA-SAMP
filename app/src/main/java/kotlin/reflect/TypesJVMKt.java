/*
 * Decompiled with CFR 0.152.
 */
package kotlin.reflect;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.KTypeBase;
import kotlin.reflect.GenericArrayTypeImpl;
import kotlin.reflect.KClass;
import kotlin.reflect.KType;
import kotlin.reflect.KTypeParameter;
import kotlin.reflect.KTypeProjection;
import kotlin.reflect.KVariance;
import kotlin.reflect.ParameterizedTypeImpl;
import kotlin.reflect.TypeVariableImpl;
import kotlin.reflect.TypesJVMKt;
import kotlin.reflect.TypesJVMKt$WhenMappings;
import kotlin.reflect.WildcardTypeImpl;
import kotlin.sequences.SequencesKt;
import kotlin.text.StringsKt;

@Metadata(bv={1, 0, 3}, d1={"\u00000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\u001a\"\u0010\n\u001a\u00020\u00012\n\u0010\u000b\u001a\u0006\u0012\u0002\b\u00030\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00070\u000eH\u0003\u001a\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0001H\u0002\u001a\u0016\u0010\u0012\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0013\u001a\u00020\u0014H\u0003\"\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00028FX\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006\"\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00078BX\u0083\u0004\u00a2\u0006\f\u0012\u0004\b\u0003\u0010\b\u001a\u0004\b\u0005\u0010\t\u00a8\u0006\u0015"}, d2={"javaType", "Ljava/lang/reflect/Type;", "Lkotlin/reflect/KType;", "getJavaType$annotations", "(Lkotlin/reflect/KType;)V", "getJavaType", "(Lkotlin/reflect/KType;)Ljava/lang/reflect/Type;", "Lkotlin/reflect/KTypeProjection;", "(Lkotlin/reflect/KTypeProjection;)V", "(Lkotlin/reflect/KTypeProjection;)Ljava/lang/reflect/Type;", "createPossiblyInnerType", "jClass", "Ljava/lang/Class;", "arguments", "", "typeToString", "", "type", "computeJavaType", "forceWrapper", "", "kotlin-stdlib"}, k=2, mv={1, 4, 1})
public final class TypesJVMKt {
    public static final /* synthetic */ Type access$computeJavaType(KType kType, boolean bl) {
        return TypesJVMKt.computeJavaType(kType, bl);
    }

    public static final /* synthetic */ String access$typeToString(Type type) {
        return TypesJVMKt.typeToString(type);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static final Type computeJavaType(KType object, boolean bl) {
        Object object2 = object.getClassifier();
        if (object2 instanceof KTypeParameter) {
            return new TypeVariableImpl((KTypeParameter)object2);
        }
        if (object2 instanceof KClass) {
            object2 = (KClass)object2;
            object2 = bl ? JvmClassMappingKt.getJavaObjectType(object2) : JvmClassMappingKt.getJavaClass(object2);
            List<KTypeProjection> list = object.getArguments();
            if (list.isEmpty()) {
                return (Type)object2;
            }
            if (!((Class)object2).isArray()) return TypesJVMKt.createPossiblyInnerType(object2, list);
            Object object3 = ((Class)object2).getComponentType();
            Intrinsics.checkNotNullExpressionValue(object3, "jClass.componentType");
            if (((Class)object3).isPrimitive()) {
                return (Type)object2;
            }
            object3 = CollectionsKt.singleOrNull(list);
            if (object3 != null) {
                object = ((KTypeProjection)object3).component1();
                object3 = ((KTypeProjection)object3).component2();
                if (object == null) return (Type)object2;
                switch (TypesJVMKt$WhenMappings.$EnumSwitchMapping$0[((Enum)object).ordinal()]) {
                    default: {
                        throw new NoWhenBranchMatchedException();
                    }
                    case 2: 
                    case 3: {
                        Intrinsics.checkNotNull(object3);
                        object = TypesJVMKt.computeJavaType$default((KType)object3, false, 1, null);
                        if (!(object instanceof Class)) return new GenericArrayTypeImpl((Type)object);
                        return (Type)object2;
                    }
                    case 1: {
                        return (Type)object2;
                    }
                }
            }
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("kotlin.Array must have exactly one type argument: ");
            ((StringBuilder)object2).append(object);
            throw (Throwable)new IllegalArgumentException(((StringBuilder)object2).toString());
        }
        object2 = new StringBuilder();
        ((StringBuilder)object2).append("Unsupported type classifier: ");
        ((StringBuilder)object2).append(object);
        throw (Throwable)new UnsupportedOperationException(((StringBuilder)object2).toString());
    }

    static /* synthetic */ Type computeJavaType$default(KType kType, boolean bl, int n, Object object) {
        if ((n & 1) != 0) {
            bl = false;
        }
        return TypesJVMKt.computeJavaType(kType, bl);
    }

    private static final Type createPossiblyInnerType(Class<?> clazz, List<KTypeProjection> collection) {
        Class<?> clazz2 = clazz.getDeclaringClass();
        if (clazz2 != null) {
            if (Modifier.isStatic(clazz.getModifiers())) {
                clazz2 = clazz2;
                Object object = collection;
                collection = new ArrayList(CollectionsKt.collectionSizeOrDefault(object, 10));
                object = object.iterator();
                while (object.hasNext()) {
                    collection.add((KTypeProjection)((Object)TypesJVMKt.getJavaType((KTypeProjection)object.next())));
                }
                collection = (List)collection;
                return new ParameterizedTypeImpl(clazz, clazz2, (List<? extends Type>)collection);
            }
            int n = clazz.getTypeParameters().length;
            clazz2 = TypesJVMKt.createPossiblyInnerType(clazz2, collection.subList(n, collection.size()));
            Object object = collection.subList(0, n);
            collection = new ArrayList(CollectionsKt.collectionSizeOrDefault(object, 10));
            object = object.iterator();
            while (object.hasNext()) {
                collection.add((KTypeProjection)((Object)TypesJVMKt.getJavaType((KTypeProjection)object.next())));
            }
            collection = (List)collection;
            return new ParameterizedTypeImpl(clazz, clazz2, (List<? extends Type>)collection);
        }
        clazz2 = collection;
        collection = new ArrayList(CollectionsKt.collectionSizeOrDefault(clazz2, 10));
        clazz2 = clazz2.iterator();
        while (clazz2.hasNext()) {
            collection.add((KTypeProjection)((Object)TypesJVMKt.getJavaType((KTypeProjection)clazz2.next())));
        }
        collection = (List)collection;
        return new ParameterizedTypeImpl(clazz, null, (List<? extends Type>)collection);
    }

    public static final Type getJavaType(KType kType) {
        Type type;
        Intrinsics.checkNotNullParameter(kType, "$this$javaType");
        if (kType instanceof KTypeBase && (type = ((KTypeBase)kType).getJavaType()) != null) {
            return type;
        }
        return TypesJVMKt.computeJavaType$default(kType, false, 1, null);
    }

    private static final Type getJavaType(KTypeProjection object) {
        KVariance kVariance = ((KTypeProjection)object).getVariance();
        if (kVariance != null) {
            object = ((KTypeProjection)object).getType();
            Intrinsics.checkNotNull(object);
            switch (TypesJVMKt$WhenMappings.$EnumSwitchMapping$1[kVariance.ordinal()]) {
                default: {
                    throw new NoWhenBranchMatchedException();
                }
                case 3: {
                    object = new WildcardTypeImpl(TypesJVMKt.computeJavaType((KType)object, true), null);
                    break;
                }
                case 2: {
                    object = new WildcardTypeImpl(null, TypesJVMKt.computeJavaType((KType)object, true));
                    break;
                }
                case 1: {
                    object = TypesJVMKt.computeJavaType((KType)object, true);
                }
            }
            return object;
        }
        return WildcardTypeImpl.Companion.getSTAR();
    }

    public static /* synthetic */ void getJavaType$annotations(KType kType) {
    }

    private static /* synthetic */ void getJavaType$annotations(KTypeProjection kTypeProjection) {
    }

    private static final String typeToString(Type object) {
        if (object instanceof Class) {
            if (((Class)object).isArray()) {
                object = SequencesKt.generateSequence(object, (Function1)typeToString.unwrap.1.INSTANCE);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(((Class)SequencesKt.last(object)).getName());
                stringBuilder.append(StringsKt.repeat("[]", SequencesKt.count(object)));
                object = stringBuilder.toString();
            } else {
                object = ((Class)object).getName();
            }
            Intrinsics.checkNotNullExpressionValue(object, "if (type.isArray) {\n    \u2026\n        } else type.name");
        } else {
            object = object.toString();
        }
        return object;
    }
}

