/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Function;
import kotlin.KotlinNothingValueException;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.KotlinReflectionNotSupportedError;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function10;
import kotlin.jvm.functions.Function11;
import kotlin.jvm.functions.Function12;
import kotlin.jvm.functions.Function13;
import kotlin.jvm.functions.Function14;
import kotlin.jvm.functions.Function15;
import kotlin.jvm.functions.Function16;
import kotlin.jvm.functions.Function17;
import kotlin.jvm.functions.Function18;
import kotlin.jvm.functions.Function19;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function20;
import kotlin.jvm.functions.Function21;
import kotlin.jvm.functions.Function22;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.functions.Function6;
import kotlin.jvm.functions.Function7;
import kotlin.jvm.functions.Function8;
import kotlin.jvm.functions.Function9;
import kotlin.jvm.internal.ClassBasedDeclarationContainer;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.reflect.KCallable;
import kotlin.reflect.KClass;
import kotlin.reflect.KFunction;
import kotlin.reflect.KType;
import kotlin.reflect.KTypeParameter;
import kotlin.reflect.KVisibility;
import kotlin.text.StringsKt;

@Metadata(bv={1, 0, 3}, d1={"\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u001b\n\u0002\b\u0003\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0014\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u0000 M2\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003:\u0001MB\u0011\u0012\n\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005\u00a2\u0006\u0002\u0010\u0006J\u0013\u0010D\u001a\u00020\u00122\b\u0010E\u001a\u0004\u0018\u00010\u0002H\u0096\u0002J\b\u0010F\u001a\u00020GH\u0002J\b\u0010H\u001a\u00020IH\u0016J\u0012\u0010J\u001a\u00020\u00122\b\u0010K\u001a\u0004\u0018\u00010\u0002H\u0017J\b\u0010L\u001a\u00020/H\u0016R\u001a\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR \u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00020\u000e0\r8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\u00128VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u0011\u0010\u0015R\u001a\u0010\u0016\u001a\u00020\u00128VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b\u0017\u0010\u0014\u001a\u0004\b\u0016\u0010\u0015R\u001a\u0010\u0018\u001a\u00020\u00128VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b\u0019\u0010\u0014\u001a\u0004\b\u0018\u0010\u0015R\u001a\u0010\u001a\u001a\u00020\u00128VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b\u001b\u0010\u0014\u001a\u0004\b\u001a\u0010\u0015R\u001a\u0010\u001c\u001a\u00020\u00128VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b\u001d\u0010\u0014\u001a\u0004\b\u001c\u0010\u0015R\u001a\u0010\u001e\u001a\u00020\u00128VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b\u001f\u0010\u0014\u001a\u0004\b\u001e\u0010\u0015R\u001a\u0010 \u001a\u00020\u00128VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b!\u0010\u0014\u001a\u0004\b \u0010\u0015R\u001a\u0010\"\u001a\u00020\u00128VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b#\u0010\u0014\u001a\u0004\b\"\u0010\u0015R\u0018\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010%R\u001e\u0010&\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030'0\r8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b(\u0010\u0010R\u001e\u0010)\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00010\r8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b*\u0010\u0010R\u0016\u0010+\u001a\u0004\u0018\u00010\u00028VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b,\u0010-R\u0016\u0010.\u001a\u0004\u0018\u00010/8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b0\u00101R(\u00102\u001a\u0010\u0012\f\u0012\n\u0012\u0006\b\u0001\u0012\u00020\u00020\u00010\b8VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b3\u0010\u0014\u001a\u0004\b4\u0010\u000bR\u0016\u00105\u001a\u0004\u0018\u00010/8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b6\u00101R \u00107\u001a\b\u0012\u0004\u0012\u0002080\b8VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b9\u0010\u0014\u001a\u0004\b:\u0010\u000bR \u0010;\u001a\b\u0012\u0004\u0012\u00020<0\b8VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b=\u0010\u0014\u001a\u0004\b>\u0010\u000bR\u001c\u0010?\u001a\u0004\u0018\u00010@8VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\bA\u0010\u0014\u001a\u0004\bB\u0010C\u00a8\u0006N"}, d2={"Lkotlin/jvm/internal/ClassReference;", "Lkotlin/reflect/KClass;", "", "Lkotlin/jvm/internal/ClassBasedDeclarationContainer;", "jClass", "Ljava/lang/Class;", "(Ljava/lang/Class;)V", "annotations", "", "", "getAnnotations", "()Ljava/util/List;", "constructors", "", "Lkotlin/reflect/KFunction;", "getConstructors", "()Ljava/util/Collection;", "isAbstract", "", "isAbstract$annotations", "()V", "()Z", "isCompanion", "isCompanion$annotations", "isData", "isData$annotations", "isFinal", "isFinal$annotations", "isFun", "isFun$annotations", "isInner", "isInner$annotations", "isOpen", "isOpen$annotations", "isSealed", "isSealed$annotations", "getJClass", "()Ljava/lang/Class;", "members", "Lkotlin/reflect/KCallable;", "getMembers", "nestedClasses", "getNestedClasses", "objectInstance", "getObjectInstance", "()Ljava/lang/Object;", "qualifiedName", "", "getQualifiedName", "()Ljava/lang/String;", "sealedSubclasses", "getSealedSubclasses$annotations", "getSealedSubclasses", "simpleName", "getSimpleName", "supertypes", "Lkotlin/reflect/KType;", "getSupertypes$annotations", "getSupertypes", "typeParameters", "Lkotlin/reflect/KTypeParameter;", "getTypeParameters$annotations", "getTypeParameters", "visibility", "Lkotlin/reflect/KVisibility;", "getVisibility$annotations", "getVisibility", "()Lkotlin/reflect/KVisibility;", "equals", "other", "error", "", "hashCode", "", "isInstance", "value", "toString", "Companion", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class ClassReference
implements KClass<Object>,
ClassBasedDeclarationContainer {
    public static final Companion Companion = new Companion(null);
    private static final Map<Class<? extends Function<?>>, Integer> FUNCTION_CLASSES;
    private static final HashMap<String, String> classFqNames;
    private static final HashMap<String, String> primitiveFqNames;
    private static final HashMap<String, String> primitiveWrapperFqNames;
    private static final Map<String, String> simpleNames;
    private final Class<?> jClass;

    static {
        Iterator iterator2 = CollectionsKt.listOf(Function0.class, Function1.class, Function2.class, Function3.class, Function4.class, Function5.class, Function6.class, Function7.class, Function8.class, Function9.class, Function10.class, Function11.class, Function12.class, Function13.class, Function14.class, Function15.class, Function16.class, Function17.class, Function18.class, Function19.class, Function20.class, Function21.class, Function22.class);
        Object object2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterator2, 10));
        int n = 0;
        iterator2 = iterator2.iterator();
        while (iterator2.hasNext()) {
            Object object4 = iterator2.next();
            if (n < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            object2.add(TuplesKt.to((Class)object4, n));
            ++n;
        }
        object2 = (List)object2;
        FUNCTION_CLASSES = MapsKt.toMap((Iterable)object2);
        iterator2 = new HashMap<String, String>();
        ((HashMap)((Object)iterator2)).put("boolean", "kotlin.Boolean");
        ((HashMap)((Object)iterator2)).put("char", "kotlin.Char");
        ((HashMap)((Object)iterator2)).put("byte", "kotlin.Byte");
        ((HashMap)((Object)iterator2)).put("short", "kotlin.Short");
        ((HashMap)((Object)iterator2)).put("int", "kotlin.Int");
        ((HashMap)((Object)iterator2)).put("float", "kotlin.Float");
        ((HashMap)((Object)iterator2)).put("long", "kotlin.Long");
        ((HashMap)((Object)iterator2)).put("double", "kotlin.Double");
        primitiveFqNames = iterator2;
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("java.lang.Boolean", "kotlin.Boolean");
        hashMap.put("java.lang.Character", "kotlin.Char");
        hashMap.put("java.lang.Byte", "kotlin.Byte");
        hashMap.put("java.lang.Short", "kotlin.Short");
        hashMap.put("java.lang.Integer", "kotlin.Int");
        hashMap.put("java.lang.Float", "kotlin.Float");
        hashMap.put("java.lang.Long", "kotlin.Long");
        hashMap.put("java.lang.Double", "kotlin.Double");
        primitiveWrapperFqNames = hashMap;
        object2 = new HashMap();
        ((HashMap)object2).put("java.lang.Object", "kotlin.Any");
        ((HashMap)object2).put("java.lang.String", "kotlin.String");
        ((HashMap)object2).put("java.lang.CharSequence", "kotlin.CharSequence");
        ((HashMap)object2).put("java.lang.Throwable", "kotlin.Throwable");
        ((HashMap)object2).put("java.lang.Cloneable", "kotlin.Cloneable");
        ((HashMap)object2).put("java.lang.Number", "kotlin.Number");
        ((HashMap)object2).put("java.lang.Comparable", "kotlin.Comparable");
        ((HashMap)object2).put("java.lang.Enum", "kotlin.Enum");
        ((HashMap)object2).put("java.lang.annotation.Annotation", "kotlin.Annotation");
        ((HashMap)object2).put("java.lang.Iterable", "kotlin.collections.Iterable");
        ((HashMap)object2).put("java.util.Iterator", "kotlin.collections.Iterator");
        ((HashMap)object2).put("java.util.Collection", "kotlin.collections.Collection");
        ((HashMap)object2).put("java.util.List", "kotlin.collections.List");
        ((HashMap)object2).put("java.util.Set", "kotlin.collections.Set");
        ((HashMap)object2).put("java.util.ListIterator", "kotlin.collections.ListIterator");
        ((HashMap)object2).put("java.util.Map", "kotlin.collections.Map");
        ((HashMap)object2).put("java.util.Map$Entry", "kotlin.collections.Map.Entry");
        ((HashMap)object2).put("kotlin.jvm.internal.StringCompanionObject", "kotlin.String.Companion");
        ((HashMap)object2).put("kotlin.jvm.internal.EnumCompanionObject", "kotlin.Enum.Companion");
        ((HashMap)object2).putAll((Map)((Object)iterator2));
        ((HashMap)object2).putAll((Map)hashMap);
        iterator2 = ((HashMap)((Object)iterator2)).values();
        Intrinsics.checkNotNullExpressionValue(iterator2, "primitiveFqNames.values");
        for (Object object : (Iterable)((Object)iterator2)) {
            Map map = (Map)object2;
            object = (String)object;
            CharSequence charSequence = new StringBuilder();
            charSequence.append("kotlin.jvm.internal.");
            Intrinsics.checkNotNullExpressionValue(object, "kotlinName");
            charSequence.append(StringsKt.substringAfterLast$default((String)object, '.', null, 2, null));
            charSequence.append("CompanionObject");
            charSequence = charSequence.toString();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((String)object);
            stringBuilder.append(".Companion");
            object = TuplesKt.to(charSequence, stringBuilder.toString());
            map.put(((Pair)object).getFirst(), ((Pair)object).getSecond());
        }
        iterator2 = (Map)object2;
        for (Map.Entry<Class<Function<?>>, Integer> entry : FUNCTION_CLASSES.entrySet()) {
            Object object;
            object = entry.getKey();
            n = ((Number)entry.getValue()).intValue();
            object = ((Class)object).getName();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("kotlin.Function");
            stringBuilder.append(n);
            ((HashMap)object2).put(object, stringBuilder.toString());
        }
        classFqNames = object2;
        iterator2 = (Map)object2;
        object2 = new LinkedHashMap(MapsKt.mapCapacity(iterator2.size()));
        for (Object t : (Iterable)iterator2.entrySet()) {
            object2.put(((Map.Entry)t).getKey(), StringsKt.substringAfterLast$default((String)((Map.Entry)t).getValue(), '.', null, 2, null));
        }
        simpleNames = object2;
    }

    public ClassReference(Class<?> clazz) {
        Intrinsics.checkNotNullParameter(clazz, "jClass");
        this.jClass = clazz;
    }

    public static final /* synthetic */ Map access$getSimpleNames$cp() {
        return simpleNames;
    }

    private final Void error() {
        throw (Throwable)new KotlinReflectionNotSupportedError();
    }

    public static /* synthetic */ void getSealedSubclasses$annotations() {
    }

    public static /* synthetic */ void getSupertypes$annotations() {
    }

    public static /* synthetic */ void getTypeParameters$annotations() {
    }

    public static /* synthetic */ void getVisibility$annotations() {
    }

    public static /* synthetic */ void isAbstract$annotations() {
    }

    public static /* synthetic */ void isCompanion$annotations() {
    }

    public static /* synthetic */ void isData$annotations() {
    }

    public static /* synthetic */ void isFinal$annotations() {
    }

    public static /* synthetic */ void isFun$annotations() {
    }

    public static /* synthetic */ void isInner$annotations() {
    }

    public static /* synthetic */ void isOpen$annotations() {
    }

    public static /* synthetic */ void isSealed$annotations() {
    }

    @Override
    public boolean equals(Object object) {
        boolean bl = object instanceof ClassReference && Intrinsics.areEqual(JvmClassMappingKt.getJavaObjectType(this), JvmClassMappingKt.getJavaObjectType((KClass)object));
        return bl;
    }

    @Override
    public List<Annotation> getAnnotations() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @Override
    public Collection<KFunction<Object>> getConstructors() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @Override
    public Class<?> getJClass() {
        return this.jClass;
    }

    @Override
    public Collection<KCallable<?>> getMembers() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @Override
    public Collection<KClass<?>> getNestedClasses() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @Override
    public Object getObjectInstance() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @Override
    public String getQualifiedName() {
        return Companion.getClassQualifiedName(this.getJClass());
    }

    @Override
    public List<KClass<? extends Object>> getSealedSubclasses() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @Override
    public String getSimpleName() {
        return Companion.getClassSimpleName(this.getJClass());
    }

    @Override
    public List<KType> getSupertypes() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @Override
    public List<KTypeParameter> getTypeParameters() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @Override
    public KVisibility getVisibility() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @Override
    public int hashCode() {
        return JvmClassMappingKt.getJavaObjectType(this).hashCode();
    }

    @Override
    public boolean isAbstract() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @Override
    public boolean isCompanion() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @Override
    public boolean isData() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @Override
    public boolean isFinal() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @Override
    public boolean isFun() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @Override
    public boolean isInner() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @Override
    public boolean isInstance(Object object) {
        return Companion.isInstance(object, this.getJClass());
    }

    @Override
    public boolean isOpen() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @Override
    public boolean isSealed() {
        this.error();
        throw new KotlinNothingValueException();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getJClass().toString());
        stringBuilder.append(" (Kotlin reflection is not available)");
        return stringBuilder.toString();
    }

    @Metadata(bv={1, 0, 3}, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0014\u0010\u000f\u001a\u0004\u0018\u00010\n2\n\u0010\u0010\u001a\u0006\u0012\u0002\b\u00030\u0005J\u0014\u0010\u0011\u001a\u0004\u0018\u00010\n2\n\u0010\u0010\u001a\u0006\u0012\u0002\b\u00030\u0005J\u001c\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u00012\n\u0010\u0010\u001a\u0006\u0012\u0002\b\u00030\u0005R&\u0010\u0003\u001a\u001a\u0012\u0010\u0012\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030\u00060\u0005\u0012\u0004\u0012\u00020\u00070\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R*\u0010\b\u001a\u001e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\tj\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n`\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R*\u0010\f\u001a\u001e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\tj\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n`\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R*\u0010\r\u001a\u001e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\tj\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n`\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2={"Lkotlin/jvm/internal/ClassReference$Companion;", "", "()V", "FUNCTION_CLASSES", "", "Ljava/lang/Class;", "Lkotlin/Function;", "", "classFqNames", "Ljava/util/HashMap;", "", "Lkotlin/collections/HashMap;", "primitiveFqNames", "primitiveWrapperFqNames", "simpleNames", "getClassQualifiedName", "jClass", "getClassSimpleName", "isInstance", "", "value", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final String getClassQualifiedName(Class<?> object) {
            Intrinsics.checkNotNullParameter(object, "jClass");
            boolean bl = ((Class)object).isAnonymousClass();
            String string2 = null;
            String string3 = null;
            if (bl) {
                object = string2;
            } else if (((Class)object).isLocalClass()) {
                object = string2;
            } else if (((Class)object).isArray()) {
                object = ((Class)object).getComponentType();
                Intrinsics.checkNotNullExpressionValue(object, "componentType");
                if (((Class)object).isPrimitive()) {
                    string2 = (String)classFqNames.get(((Class)object).getName());
                    object = string3;
                    if (string2 != null) {
                        object = new StringBuilder();
                        ((StringBuilder)object).append(string2);
                        ((StringBuilder)object).append("Array");
                        object = ((StringBuilder)object).toString();
                    }
                } else {
                    object = string3;
                }
                if (object == null) {
                    object = "kotlin.Array";
                }
            } else {
                string3 = (String)classFqNames.get(((Class)object).getName());
                object = string3 != null ? string3 : ((Class)object).getCanonicalName();
            }
            return object;
        }

        /*
         * Unable to fully structure code
         */
        public final String getClassSimpleName(Class<?> var1_1) {
            block12: {
                block13: {
                    block11: {
                        Intrinsics.checkNotNullParameter(var1_1, "jClass");
                        var2_2 = var1_1.isAnonymousClass();
                        var3_3 = "Array";
                        var4_4 = null;
                        if (!var2_2) break block11;
                        var1_1 = null;
                        break block12;
                    }
                    if (!var1_1.isLocalClass()) break block13;
                    var4_4 = var1_1.getSimpleName();
                    var5_5 = var1_1.getEnclosingMethod();
                    if (var5_5 == null) ** GOTO lbl-1000
                    Intrinsics.checkNotNullExpressionValue(var4_4, "name");
                    var3_3 = new StringBuilder();
                    var3_3.append(var5_5.getName());
                    var3_3.append("$");
                    var3_3 = StringsKt.substringAfter$default(var4_4, var3_3.toString(), null, 2, null);
                    if (var3_3 != null) {
                        var1_1 = var3_3;
                    } else if ((var1_1 = var1_1.getEnclosingConstructor()) != null) {
                        Intrinsics.checkNotNullExpressionValue(var4_4, "name");
                        var3_3 = new StringBuilder();
                        var3_3.append(var1_1.getName());
                        var3_3.append("$");
                        var1_1 = StringsKt.substringAfter$default(var4_4, var3_3.toString(), null, 2, null);
                    } else {
                        var1_1 = null;
                    }
                    if (var1_1 == null) {
                        Intrinsics.checkNotNullExpressionValue(var4_4, "name");
                        var1_1 = StringsKt.substringAfter$default(var4_4, '$', null, 2, null);
                    }
                    break block12;
                }
                if (var1_1.isArray()) {
                    var1_1 = var1_1.getComponentType();
                    Intrinsics.checkNotNullExpressionValue(var1_1, "componentType");
                    if (var1_1.isPrimitive()) {
                        var5_6 = (String)ClassReference.access$getSimpleNames$cp().get(var1_1.getName());
                        var1_1 = var4_4;
                        if (var5_6 != null) {
                            var1_1 = new StringBuilder();
                            var1_1.append(var5_6);
                            var1_1.append("Array");
                            var1_1 = var1_1.toString();
                        }
                    } else {
                        var1_1 = var4_4;
                    }
                    if (var1_1 == null) {
                        var1_1 = var3_3;
                    }
                } else {
                    var3_3 = (String)ClassReference.access$getSimpleNames$cp().get(var1_1.getName());
                    var1_1 = var3_3 != null ? var3_3 : var1_1.getSimpleName();
                }
            }
            return var1_1;
        }

        public final boolean isInstance(Object object, Class<?> clazz) {
            Intrinsics.checkNotNullParameter(clazz, "jClass");
            Object object2 = FUNCTION_CLASSES;
            if (object2 != null) {
                if ((object2 = (Integer)object2.get(clazz)) != null) {
                    return TypeIntrinsics.isFunctionOfArity(object, ((Number)object2).intValue());
                }
                if (clazz.isPrimitive()) {
                    clazz = JvmClassMappingKt.getJavaObjectType(JvmClassMappingKt.getKotlinClass(clazz));
                }
                return clazz.isInstance(object);
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.Map<K, V>");
        }
    }
}

