/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.reflect;

import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

public class TypeToken<T> {
    final int hashCode;
    final Class<? super T> rawType;
    final Type type;

    protected TypeToken() {
        Type type;
        this.type = type = TypeToken.getSuperclassTypeParameter(this.getClass());
        this.rawType = $Gson$Types.getRawType(type);
        this.hashCode = type.hashCode();
    }

    TypeToken(Type type) {
        this.type = type = $Gson$Types.canonicalize($Gson$Preconditions.checkNotNull(type));
        this.rawType = $Gson$Types.getRawType(type);
        this.hashCode = type.hashCode();
    }

    private static AssertionError buildUnexpectedTypeError(Type type, Class<?> ... classArray) {
        StringBuilder stringBuilder = new StringBuilder("Unexpected type. Expected one of: ");
        int n = classArray.length;
        for (int i = 0; i < n; ++i) {
            stringBuilder.append(classArray[i].getName());
            stringBuilder.append(", ");
        }
        stringBuilder.append("but got: ");
        stringBuilder.append(type.getClass().getName());
        stringBuilder.append(", for type token: ");
        stringBuilder.append(type.toString());
        stringBuilder.append('.');
        return new AssertionError((Object)stringBuilder.toString());
    }

    public static <T> TypeToken<T> get(Class<T> clazz) {
        return new TypeToken<T>(clazz);
    }

    public static TypeToken<?> get(Type type) {
        return new TypeToken(type);
    }

    static Type getSuperclassTypeParameter(Class<?> type) {
        if (!((type = type.getGenericSuperclass()) instanceof Class)) {
            return $Gson$Types.canonicalize(((ParameterizedType)type).getActualTypeArguments()[0]);
        }
        throw new RuntimeException("Missing type parameter.");
    }

    private static boolean isAssignableFrom(Type clazz, GenericArrayType type) {
        Type type2 = type.getGenericComponentType();
        if (type2 instanceof ParameterizedType) {
            type = clazz;
            if (clazz instanceof GenericArrayType) {
                type = ((GenericArrayType)((Object)clazz)).getGenericComponentType();
            } else if (clazz instanceof Class) {
                clazz = clazz;
                while (clazz.isArray()) {
                    clazz = clazz.getComponentType();
                }
                type = clazz;
            }
            return TypeToken.isAssignableFrom(type, (ParameterizedType)type2, new HashMap<String, Type>());
        }
        return true;
    }

    private static boolean isAssignableFrom(Type object, ParameterizedType parameterizedType, Map<String, Type> map) {
        int n;
        int n2 = 0;
        if (object == null) {
            return false;
        }
        if (parameterizedType.equals(object)) {
            return true;
        }
        Class<?> clazz = $Gson$Types.getRawType((Type)object);
        ParameterizedType parameterizedType2 = null;
        if (object instanceof ParameterizedType) {
            parameterizedType2 = (ParameterizedType)object;
        }
        if (parameterizedType2 != null) {
            Type[] typeArray = parameterizedType2.getActualTypeArguments();
            TypeVariable<Class<?>>[] typeVariableArray = clazz.getTypeParameters();
            for (n = 0; n < typeArray.length; ++n) {
                object = typeArray[n];
                TypeVariable<Class<?>> typeVariable = typeVariableArray[n];
                while (object instanceof TypeVariable) {
                    object = map.get(((TypeVariable)object).getName());
                }
                map.put(typeVariable.getName(), (Type)object);
            }
            if (TypeToken.typeEquals(parameterizedType2, parameterizedType, map)) {
                return true;
            }
        }
        object = clazz.getGenericInterfaces();
        int n3 = ((Type[])object).length;
        for (n = n2; n < n3; ++n) {
            if (!TypeToken.isAssignableFrom(object[n], parameterizedType, new HashMap<String, Type>(map))) continue;
            return true;
        }
        return TypeToken.isAssignableFrom(clazz.getGenericSuperclass(), parameterizedType, new HashMap<String, Type>(map));
    }

    private static boolean matches(Type type, Type type2, Map<String, Type> map) {
        boolean bl = type2.equals(type) || type instanceof TypeVariable && type2.equals(map.get(((TypeVariable)type).getName()));
        return bl;
    }

    private static boolean typeEquals(ParameterizedType typeArray, ParameterizedType typeArray2, Map<String, Type> map) {
        if (typeArray.getRawType().equals(typeArray2.getRawType())) {
            typeArray = typeArray.getActualTypeArguments();
            typeArray2 = typeArray2.getActualTypeArguments();
            for (int i = 0; i < typeArray.length; ++i) {
                if (TypeToken.matches(typeArray[i], typeArray2[i], map)) continue;
                return false;
            }
            return true;
        }
        return false;
    }

    public final boolean equals(Object object) {
        boolean bl = object instanceof TypeToken && $Gson$Types.equals(this.type, ((TypeToken)object).type);
        return bl;
    }

    public final Class<? super T> getRawType() {
        return this.rawType;
    }

    public final Type getType() {
        return this.type;
    }

    public final int hashCode() {
        return this.hashCode;
    }

    @Deprecated
    public boolean isAssignableFrom(TypeToken<?> typeToken) {
        return this.isAssignableFrom(typeToken.getType());
    }

    @Deprecated
    public boolean isAssignableFrom(Class<?> clazz) {
        return this.isAssignableFrom((Type)clazz);
    }

    @Deprecated
    public boolean isAssignableFrom(Type type) {
        boolean bl = false;
        if (type == null) {
            return false;
        }
        if (this.type.equals(type)) {
            return true;
        }
        Type type2 = this.type;
        if (type2 instanceof Class) {
            return this.rawType.isAssignableFrom($Gson$Types.getRawType(type));
        }
        if (type2 instanceof ParameterizedType) {
            return TypeToken.isAssignableFrom(type, (ParameterizedType)type2, new HashMap<String, Type>());
        }
        if (type2 instanceof GenericArrayType) {
            if (this.rawType.isAssignableFrom($Gson$Types.getRawType(type)) && TypeToken.isAssignableFrom(type, (GenericArrayType)this.type)) {
                bl = true;
            }
            return bl;
        }
        throw TypeToken.buildUnexpectedTypeError(type2, Class.class, ParameterizedType.class, GenericArrayType.class);
    }

    public final String toString() {
        return $Gson$Types.typeToString(this.type);
    }
}

