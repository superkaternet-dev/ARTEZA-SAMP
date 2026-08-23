/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal;

import com.google.gson.internal.$Gson$Preconditions;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;

public final class $Gson$Types {
    static final Type[] EMPTY_TYPE_ARRAY = new Type[0];

    private $Gson$Types() {
        throw new UnsupportedOperationException();
    }

    public static GenericArrayType arrayOf(Type type) {
        return new GenericArrayTypeImpl(type);
    }

    public static Type canonicalize(Type type) {
        if (type instanceof Class) {
            if (((Class)(type = (Class)type)).isArray()) {
                type = new GenericArrayTypeImpl($Gson$Types.canonicalize(((Class)type).getComponentType()));
            }
            return type;
        }
        if (type instanceof ParameterizedType) {
            type = (ParameterizedType)type;
            return new ParameterizedTypeImpl(type.getOwnerType(), type.getRawType(), type.getActualTypeArguments());
        }
        if (type instanceof GenericArrayType) {
            return new GenericArrayTypeImpl(((GenericArrayType)type).getGenericComponentType());
        }
        if (type instanceof WildcardType) {
            type = (WildcardType)type;
            return new WildcardTypeImpl(type.getUpperBounds(), type.getLowerBounds());
        }
        return type;
    }

    static void checkNotPrimitive(Type type) {
        boolean bl = !(type instanceof Class) || !((Class)type).isPrimitive();
        $Gson$Preconditions.checkArgument(bl);
    }

    private static Class<?> declaringClassOf(TypeVariable<?> type) {
        type = (type = type.getGenericDeclaration()) instanceof Class ? (Class)type : null;
        return type;
    }

    static boolean equal(Object object, Object object2) {
        boolean bl = object == object2 || object != null && object.equals(object2);
        return bl;
    }

    public static boolean equals(Type type, Type type2) {
        boolean bl = true;
        boolean bl2 = true;
        boolean bl3 = true;
        if (type == type2) {
            return true;
        }
        if (type instanceof Class) {
            return type.equals(type2);
        }
        if (type instanceof ParameterizedType) {
            if (!(type2 instanceof ParameterizedType)) {
                return false;
            }
            type = (ParameterizedType)type;
            type2 = (ParameterizedType)type2;
            bl = $Gson$Types.equal(type.getOwnerType(), type2.getOwnerType()) && type.getRawType().equals(type2.getRawType()) && Arrays.equals(type.getActualTypeArguments(), type2.getActualTypeArguments()) ? bl3 : false;
            return bl;
        }
        if (type instanceof GenericArrayType) {
            if (!(type2 instanceof GenericArrayType)) {
                return false;
            }
            type = (GenericArrayType)type;
            type2 = (GenericArrayType)type2;
            return $Gson$Types.equals(type.getGenericComponentType(), type2.getGenericComponentType());
        }
        if (type instanceof WildcardType) {
            if (!(type2 instanceof WildcardType)) {
                return false;
            }
            type = (WildcardType)type;
            type2 = (WildcardType)type2;
            if (!Arrays.equals(type.getUpperBounds(), type2.getUpperBounds()) || !Arrays.equals(type.getLowerBounds(), type2.getLowerBounds())) {
                bl = false;
            }
            return bl;
        }
        if (type instanceof TypeVariable) {
            if (!(type2 instanceof TypeVariable)) {
                return false;
            }
            type = (TypeVariable)type;
            type2 = (TypeVariable)type2;
            bl = type.getGenericDeclaration() == type2.getGenericDeclaration() && type.getName().equals(type2.getName()) ? bl2 : false;
            return bl;
        }
        return false;
    }

    public static Type getArrayComponentType(Type type) {
        type = type instanceof GenericArrayType ? ((GenericArrayType)type).getGenericComponentType() : ((Class)type).getComponentType();
        return type;
    }

    public static Type getCollectionElementType(Type type, Class<?> type2) {
        type = type2 = $Gson$Types.getSupertype(type, type2, Collection.class);
        if (type2 instanceof WildcardType) {
            type = ((WildcardType)type2).getUpperBounds()[0];
        }
        if (type instanceof ParameterizedType) {
            return ((ParameterizedType)type).getActualTypeArguments()[0];
        }
        return Object.class;
    }

    static Type getGenericSupertype(Type object, Class<?> object2, Class<?> clazz) {
        if (clazz == object2) {
            return object;
        }
        if (clazz.isInterface()) {
            object = ((Class)object2).getInterfaces();
            int n = ((Class<?>[])object).length;
            for (int i = 0; i < n; ++i) {
                if (object[i] == clazz) {
                    return ((Class)object2).getGenericInterfaces()[i];
                }
                if (!clazz.isAssignableFrom(object[i])) continue;
                return $Gson$Types.getGenericSupertype(((Class)object2).getGenericInterfaces()[i], object[i], clazz);
            }
        }
        if (!((Class)object2).isInterface()) {
            while (object2 != Object.class) {
                object = ((Class)object2).getSuperclass();
                if (object == clazz) {
                    return ((Class)object2).getGenericSuperclass();
                }
                if (clazz.isAssignableFrom((Class<?>)object)) {
                    return $Gson$Types.getGenericSupertype(((Class)object2).getGenericSuperclass(), object, clazz);
                }
                object2 = object;
            }
        }
        return clazz;
    }

    public static Type[] getMapKeyAndValueTypes(Type type, Class<?> clazz) {
        if (type == Properties.class) {
            return new Type[]{String.class, String.class};
        }
        if ((type = $Gson$Types.getSupertype(type, clazz, Map.class)) instanceof ParameterizedType) {
            return ((ParameterizedType)type).getActualTypeArguments();
        }
        return new Type[]{Object.class, Object.class};
    }

    public static Class<?> getRawType(Type type) {
        if (type instanceof Class) {
            return (Class)type;
        }
        if (type instanceof ParameterizedType) {
            type = ((ParameterizedType)type).getRawType();
            $Gson$Preconditions.checkArgument(type instanceof Class);
            return (Class)type;
        }
        if (type instanceof GenericArrayType) {
            return Array.newInstance($Gson$Types.getRawType(((GenericArrayType)type).getGenericComponentType()), 0).getClass();
        }
        if (type instanceof TypeVariable) {
            return Object.class;
        }
        if (type instanceof WildcardType) {
            return $Gson$Types.getRawType(((WildcardType)type).getUpperBounds()[0]);
        }
        String string2 = type == null ? "null" : type.getClass().getName();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Expected a Class, ParameterizedType, or GenericArrayType, but <");
        stringBuilder.append(type);
        stringBuilder.append("> is of type ");
        stringBuilder.append(string2);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    static Type getSupertype(Type type, Class<?> clazz, Class<?> clazz2) {
        $Gson$Preconditions.checkArgument(clazz2.isAssignableFrom(clazz));
        return $Gson$Types.resolve(type, clazz, $Gson$Types.getGenericSupertype(type, clazz, clazz2));
    }

    static int hashCodeOrZero(Object object) {
        int n = object != null ? object.hashCode() : 0;
        return n;
    }

    private static int indexOf(Object[] object, Object object2) {
        for (int i = 0; i < ((Object[])object).length; ++i) {
            if (!object2.equals(object[i])) continue;
            return i;
        }
        object = new NoSuchElementException();
        throw object;
    }

    public static ParameterizedType newParameterizedTypeWithOwner(Type type, Type type2, Type ... typeArray) {
        return new ParameterizedTypeImpl(type, type2, typeArray);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static Type resolve(Type object, Class<?> clazz, Type typeArray) {
        Object object2;
        while (typeArray instanceof TypeVariable) {
            object2 = (TypeVariable)typeArray;
            if ((typeArray = $Gson$Types.resolveTypeVariable((Type)object, clazz, object2)) != object2) continue;
            return typeArray;
        }
        if (typeArray instanceof Class && ((Class)typeArray).isArray()) {
            object2 = ((Class)(typeArray = (Class)typeArray)).getComponentType();
            if (object2 != (object = $Gson$Types.resolve((Type)object, clazz, (Type)object2))) return $Gson$Types.arrayOf((Type)object);
            return typeArray;
        }
        if (typeArray instanceof GenericArrayType) {
            object2 = (typeArray = (GenericArrayType)typeArray).getGenericComponentType();
            if (object2 != (object = $Gson$Types.resolve((Type)object, clazz, (Type)object2))) return $Gson$Types.arrayOf((Type)object);
            return typeArray;
        }
        boolean bl = typeArray instanceof ParameterizedType;
        boolean bl2 = true;
        if (bl) {
            ParameterizedType parameterizedType = (ParameterizedType)typeArray;
            Type type = $Gson$Types.resolve((Type)object, clazz, (Type)(typeArray = parameterizedType.getOwnerType()));
            if (type == typeArray) {
                bl2 = false;
            }
            typeArray = parameterizedType.getActualTypeArguments();
            int n = ((Object)typeArray).length;
            for (int i = 0; i < n; ++i) {
                Type type2 = $Gson$Types.resolve((Type)object, clazz, (Type)typeArray[i]);
                boolean bl3 = bl2;
                object2 = typeArray;
                if (type2 != typeArray[i]) {
                    bl3 = bl2;
                    object2 = typeArray;
                    if (!bl2) {
                        object2 = (Type[])typeArray.clone();
                        bl3 = true;
                    }
                    object2[i] = type2;
                }
                bl2 = bl3;
                typeArray = object2;
            }
            if (!bl2) return parameterizedType;
            return $Gson$Types.newParameterizedTypeWithOwner(type, parameterizedType.getRawType(), (Type[])typeArray);
        }
        if (!(typeArray instanceof WildcardType)) return typeArray;
        typeArray = (WildcardType)typeArray;
        Object object3 = typeArray.getLowerBounds();
        object2 = typeArray.getUpperBounds();
        if (((Type[])object3).length == 1) {
            if ((object = $Gson$Types.resolve((Type)object, clazz, object3[0])) == object3[0]) return typeArray;
            return $Gson$Types.supertypeOf((Type)object);
        }
        if (((Type[])object2).length != 1) return typeArray;
        object3 = object2[0];
        object = $Gson$Types.resolve((Type)object, clazz, (Type)object3);
        if (object == object2[0]) return typeArray;
        return $Gson$Types.subtypeOf((Type)object);
    }

    static Type resolveTypeVariable(Type type, Class<?> clazz, TypeVariable<?> typeVariable) {
        Class<?> clazz2 = $Gson$Types.declaringClassOf(typeVariable);
        if (clazz2 == null) {
            return typeVariable;
        }
        if ((type = $Gson$Types.getGenericSupertype(type, clazz, clazz2)) instanceof ParameterizedType) {
            int n = $Gson$Types.indexOf(clazz2.getTypeParameters(), typeVariable);
            return ((ParameterizedType)type).getActualTypeArguments()[n];
        }
        return typeVariable;
    }

    public static WildcardType subtypeOf(Type type) {
        Type[] typeArray = EMPTY_TYPE_ARRAY;
        return new WildcardTypeImpl(new Type[]{type}, typeArray);
    }

    public static WildcardType supertypeOf(Type type) {
        return new WildcardTypeImpl(new Type[]{Object.class}, new Type[]{type});
    }

    public static String typeToString(Type object) {
        object = object instanceof Class ? ((Class)object).getName() : object.toString();
        return object;
    }

    private static final class GenericArrayTypeImpl
    implements GenericArrayType,
    Serializable {
        private static final long serialVersionUID = 0L;
        private final Type componentType;

        public GenericArrayTypeImpl(Type type) {
            this.componentType = $Gson$Types.canonicalize(type);
        }

        public boolean equals(Object object) {
            boolean bl = object instanceof GenericArrayType && $Gson$Types.equals(this, (GenericArrayType)object);
            return bl;
        }

        @Override
        public Type getGenericComponentType() {
            return this.componentType;
        }

        public int hashCode() {
            return this.componentType.hashCode();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append($Gson$Types.typeToString(this.componentType));
            stringBuilder.append("[]");
            return stringBuilder.toString();
        }
    }

    private static final class ParameterizedTypeImpl
    implements ParameterizedType,
    Serializable {
        private static final long serialVersionUID = 0L;
        private final Type ownerType;
        private final Type rawType;
        private final Type[] typeArguments;

        public ParameterizedTypeImpl(Type object, Type type, Type ... typeArray) {
            int n;
            if (type instanceof Class) {
                Class clazz = (Class)type;
                boolean bl = Modifier.isStatic(clazz.getModifiers());
                boolean bl2 = false;
                n = !bl && clazz.getEnclosingClass() != null ? 0 : 1;
                if (object != null || n != 0) {
                    bl2 = true;
                }
                $Gson$Preconditions.checkArgument(bl2);
            }
            object = object == null ? null : $Gson$Types.canonicalize((Type)object);
            this.ownerType = object;
            this.rawType = $Gson$Types.canonicalize(type);
            this.typeArguments = (Type[])typeArray.clone();
            for (n = 0; n < ((Type[])(object = this.typeArguments)).length; ++n) {
                $Gson$Preconditions.checkNotNull(object[n]);
                $Gson$Types.checkNotPrimitive(this.typeArguments[n]);
                object = this.typeArguments;
                object[n] = $Gson$Types.canonicalize(object[n]);
            }
        }

        public boolean equals(Object object) {
            boolean bl = object instanceof ParameterizedType && $Gson$Types.equals(this, (ParameterizedType)object);
            return bl;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return (Type[])this.typeArguments.clone();
        }

        @Override
        public Type getOwnerType() {
            return this.ownerType;
        }

        @Override
        public Type getRawType() {
            return this.rawType;
        }

        public int hashCode() {
            return Arrays.hashCode(this.typeArguments) ^ this.rawType.hashCode() ^ $Gson$Types.hashCodeOrZero(this.ownerType);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder((this.typeArguments.length + 1) * 30);
            stringBuilder.append($Gson$Types.typeToString(this.rawType));
            if (this.typeArguments.length == 0) {
                return stringBuilder.toString();
            }
            stringBuilder.append("<");
            stringBuilder.append($Gson$Types.typeToString(this.typeArguments[0]));
            for (int i = 1; i < this.typeArguments.length; ++i) {
                stringBuilder.append(", ");
                stringBuilder.append($Gson$Types.typeToString(this.typeArguments[i]));
            }
            stringBuilder.append(">");
            return stringBuilder.toString();
        }
    }

    private static final class WildcardTypeImpl
    implements WildcardType,
    Serializable {
        private static final long serialVersionUID = 0L;
        private final Type lowerBound;
        private final Type upperBound;

        public WildcardTypeImpl(Type[] typeArray, Type[] typeArray2) {
            int n = typeArray2.length;
            boolean bl = true;
            boolean bl2 = n <= 1;
            $Gson$Preconditions.checkArgument(bl2);
            bl2 = typeArray.length == 1;
            $Gson$Preconditions.checkArgument(bl2);
            if (typeArray2.length == 1) {
                $Gson$Preconditions.checkNotNull(typeArray2[0]);
                $Gson$Types.checkNotPrimitive(typeArray2[0]);
                bl2 = typeArray[0] == Object.class ? bl : false;
                $Gson$Preconditions.checkArgument(bl2);
                this.lowerBound = $Gson$Types.canonicalize(typeArray2[0]);
                this.upperBound = Object.class;
            } else {
                $Gson$Preconditions.checkNotNull(typeArray[0]);
                $Gson$Types.checkNotPrimitive(typeArray[0]);
                this.lowerBound = null;
                this.upperBound = $Gson$Types.canonicalize(typeArray[0]);
            }
        }

        public boolean equals(Object object) {
            boolean bl = object instanceof WildcardType && $Gson$Types.equals(this, (WildcardType)object);
            return bl;
        }

        @Override
        public Type[] getLowerBounds() {
            Type type = this.lowerBound;
            Type[] typeArray = type != null ? new Type[]{type} : EMPTY_TYPE_ARRAY;
            return typeArray;
        }

        @Override
        public Type[] getUpperBounds() {
            return new Type[]{this.upperBound};
        }

        public int hashCode() {
            Type type = this.lowerBound;
            int n = type != null ? type.hashCode() + 31 : 1;
            return n ^ this.upperBound.hashCode() + 31;
        }

        public String toString() {
            if (this.lowerBound != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("? super ");
                stringBuilder.append($Gson$Types.typeToString(this.lowerBound));
                return stringBuilder.toString();
            }
            if (this.upperBound == Object.class) {
                return "?";
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("? extends ");
            stringBuilder.append($Gson$Types.typeToString(this.upperBound));
            return stringBuilder.toString();
        }
    }
}

