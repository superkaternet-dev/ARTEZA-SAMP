/*
 * Decompiled with CFR 0.152.
 */
package retrofit2;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.NoSuchElementException;
import okhttp3.ResponseBody;
import okio.Buffer;

final class Utils {
    static final Type[] EMPTY_TYPE_ARRAY = new Type[0];

    private Utils() {
    }

    static ResponseBody buffer(ResponseBody responseBody) throws IOException {
        Buffer buffer = new Buffer();
        responseBody.source().readAll(buffer);
        return ResponseBody.create(responseBody.contentType(), responseBody.contentLength(), buffer);
    }

    static <T> T checkNotNull(T t, String string2) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException(string2);
    }

    static void checkNotPrimitive(Type type) {
        if (type instanceof Class && ((Class)type).isPrimitive()) {
            throw new IllegalArgumentException();
        }
    }

    private static Class<?> declaringClassOf(TypeVariable<?> type) {
        type = (type = type.getGenericDeclaration()) instanceof Class ? (Class)type : null;
        return type;
    }

    private static boolean equal(Object object, Object object2) {
        boolean bl = object == object2 || object != null && object.equals(object2);
        return bl;
    }

    static boolean equals(Type type, Type type2) {
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
            if (!(Utils.equal(type.getOwnerType(), type2.getOwnerType()) && type.getRawType().equals(type2.getRawType()) && Arrays.equals(type.getActualTypeArguments(), type2.getActualTypeArguments()))) {
                bl3 = false;
            }
            return bl3;
        }
        if (type instanceof GenericArrayType) {
            if (!(type2 instanceof GenericArrayType)) {
                return false;
            }
            type = (GenericArrayType)type;
            type2 = (GenericArrayType)type2;
            return Utils.equals(type.getGenericComponentType(), type2.getGenericComponentType());
        }
        if (type instanceof WildcardType) {
            if (!(type2 instanceof WildcardType)) {
                return false;
            }
            type = (WildcardType)type;
            type2 = (WildcardType)type2;
            bl3 = Arrays.equals(type.getUpperBounds(), type2.getUpperBounds()) && Arrays.equals(type.getLowerBounds(), type2.getLowerBounds()) ? bl : false;
            return bl3;
        }
        if (type instanceof TypeVariable) {
            if (!(type2 instanceof TypeVariable)) {
                return false;
            }
            type = (TypeVariable)type;
            type2 = (TypeVariable)type2;
            bl3 = type.getGenericDeclaration() == type2.getGenericDeclaration() && type.getName().equals(type2.getName()) ? bl2 : false;
            return bl3;
        }
        return false;
    }

    static Type getCallResponseType(Type type) {
        if (type instanceof ParameterizedType) {
            return Utils.getParameterUpperBound(0, (ParameterizedType)type);
        }
        throw new IllegalArgumentException("Call return type must be parameterized as Call<Foo> or Call<? extends Foo>");
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
                return Utils.getGenericSupertype(((Class)object2).getGenericInterfaces()[i], object[i], clazz);
            }
        }
        if (!((Class)object2).isInterface()) {
            while (object2 != Object.class) {
                object = ((Class)object2).getSuperclass();
                if (object == clazz) {
                    return ((Class)object2).getGenericSuperclass();
                }
                if (clazz.isAssignableFrom((Class<?>)object)) {
                    return Utils.getGenericSupertype(((Class)object2).getGenericSuperclass(), object, clazz);
                }
                object2 = object;
            }
        }
        return clazz;
    }

    static Type getParameterUpperBound(int n, ParameterizedType type) {
        Type[] typeArray = type.getActualTypeArguments();
        if (n >= 0 && n < typeArray.length) {
            type = typeArray[n];
            if (type instanceof WildcardType) {
                return ((WildcardType)type).getUpperBounds()[0];
            }
            return type;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Index ");
        stringBuilder.append(n);
        stringBuilder.append(" not in range [0,");
        stringBuilder.append(typeArray.length);
        stringBuilder.append(") for ");
        stringBuilder.append(type);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    static Class<?> getRawType(Type type) {
        if (type != null) {
            if (type instanceof Class) {
                return (Class)type;
            }
            if (type instanceof ParameterizedType) {
                if ((type = ((ParameterizedType)type).getRawType()) instanceof Class) {
                    return (Class)type;
                }
                throw new IllegalArgumentException();
            }
            if (type instanceof GenericArrayType) {
                return Array.newInstance(Utils.getRawType(((GenericArrayType)type).getGenericComponentType()), 0).getClass();
            }
            if (type instanceof TypeVariable) {
                return Object.class;
            }
            if (type instanceof WildcardType) {
                return Utils.getRawType(((WildcardType)type).getUpperBounds()[0]);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Expected a Class, ParameterizedType, or GenericArrayType, but <");
            stringBuilder.append(type);
            stringBuilder.append("> is of type ");
            stringBuilder.append(type.getClass().getName());
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        throw new NullPointerException("type == null");
    }

    static Type getSupertype(Type type, Class<?> clazz, Class<?> clazz2) {
        if (clazz2.isAssignableFrom(clazz)) {
            return Utils.resolve(type, clazz, Utils.getGenericSupertype(type, clazz, clazz2));
        }
        throw new IllegalArgumentException();
    }

    static boolean hasUnresolvableType(Type object) {
        if (object instanceof Class) {
            return false;
        }
        if (object instanceof ParameterizedType) {
            object = ((ParameterizedType)object).getActualTypeArguments();
            int n = ((Type[])object).length;
            for (int i = 0; i < n; ++i) {
                if (!Utils.hasUnresolvableType(object[i])) continue;
                return true;
            }
            return false;
        }
        if (object instanceof GenericArrayType) {
            return Utils.hasUnresolvableType(((GenericArrayType)object).getGenericComponentType());
        }
        if (object instanceof TypeVariable) {
            return true;
        }
        if (object instanceof WildcardType) {
            return true;
        }
        String string2 = object == null ? "null" : object.getClass().getName();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Expected a Class, ParameterizedType, or GenericArrayType, but <");
        stringBuilder.append(object);
        stringBuilder.append("> is of type ");
        stringBuilder.append(string2);
        object = new IllegalArgumentException(stringBuilder.toString());
        throw object;
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

    static boolean isAnnotationPresent(Annotation[] annotationArray, Class<? extends Annotation> clazz) {
        int n = annotationArray.length;
        for (int i = 0; i < n; ++i) {
            if (!clazz.isInstance(annotationArray[i])) continue;
            return true;
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    static Type resolve(Type object, Class<?> typeArray, Type typeArray2) {
        Object object2;
        while (typeArray2 instanceof TypeVariable) {
            object2 = (TypeVariable)typeArray2;
            if ((typeArray2 = Utils.resolveTypeVariable((Type)object, typeArray, object2)) != object2) continue;
            return typeArray2;
        }
        if (typeArray2 instanceof Class && ((Class)typeArray2).isArray()) {
            object2 = ((Class)(typeArray2 = (Class)typeArray2)).getComponentType();
            if (object2 != (object = Utils.resolve((Type)object, typeArray, (Type)object2))) return new GenericArrayTypeImpl((Type)object);
            return typeArray2;
        }
        if (typeArray2 instanceof GenericArrayType) {
            object2 = (typeArray2 = (GenericArrayType)typeArray2).getGenericComponentType();
            if (object2 != (object = Utils.resolve((Type)object, typeArray, (Type)object2))) return new GenericArrayTypeImpl((Type)object);
            return typeArray2;
        }
        boolean bl = typeArray2 instanceof ParameterizedType;
        boolean bl2 = true;
        if (bl) {
            ParameterizedType parameterizedType = (ParameterizedType)typeArray2;
            Type type = Utils.resolve((Type)object, typeArray, (Type)(typeArray2 = parameterizedType.getOwnerType()));
            if (type == typeArray2) {
                bl2 = false;
            }
            typeArray2 = parameterizedType.getActualTypeArguments();
            int n = ((Object)typeArray2).length;
            for (int i = 0; i < n; ++i) {
                Type type2 = Utils.resolve((Type)object, typeArray, (Type)typeArray2[i]);
                boolean bl3 = bl2;
                object2 = typeArray2;
                if (type2 != typeArray2[i]) {
                    bl3 = bl2;
                    object2 = typeArray2;
                    if (!bl2) {
                        object2 = (Type[])typeArray2.clone();
                        bl3 = true;
                    }
                    object2[i] = type2;
                }
                bl2 = bl3;
                typeArray2 = object2;
            }
            if (!bl2) return parameterizedType;
            return new ParameterizedTypeImpl(type, parameterizedType.getRawType(), (Type[])typeArray2);
        }
        if (!(typeArray2 instanceof WildcardType)) return typeArray2;
        typeArray2 = (WildcardType)typeArray2;
        Object object3 = typeArray2.getLowerBounds();
        object2 = typeArray2.getUpperBounds();
        if (((Type[])object3).length == 1) {
            if ((object = Utils.resolve((Type)object, typeArray, object3[0])) == object3[0]) return typeArray2;
            return new WildcardTypeImpl(new Type[]{Object.class}, new Type[]{object});
        }
        if (((Type[])object2).length != 1) return typeArray2;
        object3 = object2[0];
        object = Utils.resolve((Type)object, typeArray, (Type)object3);
        if (object == object2[0]) return typeArray2;
        typeArray = EMPTY_TYPE_ARRAY;
        return new WildcardTypeImpl(new Type[]{object}, typeArray);
    }

    private static Type resolveTypeVariable(Type type, Class<?> clazz, TypeVariable<?> typeVariable) {
        Class<?> clazz2 = Utils.declaringClassOf(typeVariable);
        if (clazz2 == null) {
            return typeVariable;
        }
        if ((type = Utils.getGenericSupertype(type, clazz, clazz2)) instanceof ParameterizedType) {
            int n = Utils.indexOf(clazz2.getTypeParameters(), typeVariable);
            return ((ParameterizedType)type).getActualTypeArguments()[n];
        }
        return typeVariable;
    }

    static String typeToString(Type object) {
        object = object instanceof Class ? ((Class)object).getName() : object.toString();
        return object;
    }

    static <T> void validateServiceInterface(Class<T> clazz) {
        if (clazz.isInterface()) {
            if (clazz.getInterfaces().length <= 0) {
                return;
            }
            throw new IllegalArgumentException("API interfaces must not extend other interfaces.");
        }
        throw new IllegalArgumentException("API declarations must be interfaces.");
    }

    private static final class GenericArrayTypeImpl
    implements GenericArrayType {
        private final Type componentType;

        public GenericArrayTypeImpl(Type type) {
            this.componentType = type;
        }

        public boolean equals(Object object) {
            boolean bl = object instanceof GenericArrayType && Utils.equals(this, (GenericArrayType)object);
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
            stringBuilder.append(Utils.typeToString(this.componentType));
            stringBuilder.append("[]");
            return stringBuilder.toString();
        }
    }

    private static final class ParameterizedTypeImpl
    implements ParameterizedType {
        private final Type ownerType;
        private final Type rawType;
        private final Type[] typeArguments;

        public ParameterizedTypeImpl(Type typeArray, Type type, Type ... typeArray2) {
            int n;
            int n2;
            boolean bl = type instanceof Class;
            int n3 = 0;
            if (bl) {
                n2 = 1;
                n = typeArray == null ? 1 : 0;
                if (((Class)type).getEnclosingClass() != null) {
                    n2 = 0;
                }
                if (n != n2) {
                    throw new IllegalArgumentException();
                }
            }
            this.ownerType = typeArray;
            this.rawType = type;
            typeArray = (Type[])typeArray2.clone();
            this.typeArguments = typeArray;
            n2 = typeArray.length;
            for (n = n3; n < n2; ++n) {
                type = typeArray[n];
                if (type != null) {
                    Utils.checkNotPrimitive(type);
                    continue;
                }
                throw new NullPointerException();
            }
        }

        public boolean equals(Object object) {
            boolean bl = object instanceof ParameterizedType && Utils.equals(this, (ParameterizedType)object);
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
            return Arrays.hashCode(this.typeArguments) ^ this.rawType.hashCode() ^ Utils.hashCodeOrZero(this.ownerType);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder((this.typeArguments.length + 1) * 30);
            stringBuilder.append(Utils.typeToString(this.rawType));
            if (this.typeArguments.length == 0) {
                return stringBuilder.toString();
            }
            stringBuilder.append("<");
            stringBuilder.append(Utils.typeToString(this.typeArguments[0]));
            for (int i = 1; i < this.typeArguments.length; ++i) {
                stringBuilder.append(", ");
                stringBuilder.append(Utils.typeToString(this.typeArguments[i]));
            }
            stringBuilder.append(">");
            return stringBuilder.toString();
        }
    }

    private static final class WildcardTypeImpl
    implements WildcardType {
        private final Type lowerBound;
        private final Type upperBound;

        public WildcardTypeImpl(Type[] typeArray, Type[] typeArray2) {
            block2: {
                block3: {
                    block8: {
                        block7: {
                            block4: {
                                block5: {
                                    block6: {
                                        if (typeArray2.length > 1) break block2;
                                        if (typeArray.length != 1) break block3;
                                        if (typeArray2.length != 1) break block4;
                                        if (typeArray2[0] == null) break block5;
                                        Utils.checkNotPrimitive(typeArray2[0]);
                                        if (typeArray[0] != Object.class) break block6;
                                        this.lowerBound = typeArray2[0];
                                        this.upperBound = Object.class;
                                        break block7;
                                    }
                                    throw new IllegalArgumentException();
                                }
                                throw new NullPointerException();
                            }
                            if (typeArray[0] == null) break block8;
                            Utils.checkNotPrimitive(typeArray[0]);
                            this.lowerBound = null;
                            this.upperBound = typeArray[0];
                        }
                        return;
                    }
                    throw new NullPointerException();
                }
                throw new IllegalArgumentException();
            }
            throw new IllegalArgumentException();
        }

        public boolean equals(Object object) {
            boolean bl = object instanceof WildcardType && Utils.equals(this, (WildcardType)object);
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
                stringBuilder.append(Utils.typeToString(this.lowerBound));
                return stringBuilder.toString();
            }
            if (this.upperBound == Object.class) {
                return "?";
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("? extends ");
            stringBuilder.append(Utils.typeToString(this.upperBound));
            return stringBuilder.toString();
        }
    }
}

