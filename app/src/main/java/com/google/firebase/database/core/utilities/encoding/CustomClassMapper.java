/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.google.firebase.database.core.utilities.encoding;

import android.util.Log;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.core.utilities.Utilities;
import java.io.Serializable;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CustomClassMapper {
    private static final String LOG_TAG = "ClassMapper";
    private static final ConcurrentMap<Class<?>, BeanMapper<?>> mappers = new ConcurrentHashMap();

    private static <T> T convertBean(Object object, Class<T> clazz) {
        Object object2 = CustomClassMapper.loadOrCreateBeanMapperForClass(clazz);
        if (object instanceof Map) {
            return ((BeanMapper)object2).deserialize(CustomClassMapper.expectMap(object));
        }
        object2 = new StringBuilder();
        ((StringBuilder)object2).append("Can't convert object of type ");
        ((StringBuilder)object2).append(object.getClass().getName());
        ((StringBuilder)object2).append(" to type ");
        ((StringBuilder)object2).append(clazz.getName());
        throw new DatabaseException(((StringBuilder)object2).toString());
    }

    private static Boolean convertBoolean(Object object) {
        if (object instanceof Boolean) {
            return (Boolean)object;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Failed to convert value of type ");
        stringBuilder.append(object.getClass().getName());
        stringBuilder.append(" to boolean");
        throw new DatabaseException(stringBuilder.toString());
    }

    private static Double convertDouble(Object object) {
        if (object instanceof Integer) {
            return ((Integer)object).doubleValue();
        }
        if (object instanceof Long) {
            Comparable<Long> comparable = (Long)object;
            Double d = ((Long)object).doubleValue();
            if (d.longValue() == ((Long)comparable).longValue()) {
                return d;
            }
            comparable = new StringBuilder();
            ((StringBuilder)comparable).append("Loss of precision while converting number to double: ");
            ((StringBuilder)comparable).append(object);
            ((StringBuilder)comparable).append(". Did you mean to use a 64-bit long instead?");
            throw new DatabaseException(((StringBuilder)comparable).toString());
        }
        if (object instanceof Double) {
            return (Double)object;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Failed to convert a value of type ");
        stringBuilder.append(object.getClass().getName());
        stringBuilder.append(" to double");
        throw new DatabaseException(stringBuilder.toString());
    }

    private static Integer convertInteger(Object object) {
        if (object instanceof Integer) {
            return (Integer)object;
        }
        if (!(object instanceof Long) && !(object instanceof Double)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to convert a value of type ");
            stringBuilder.append(object.getClass().getName());
            stringBuilder.append(" to int");
            throw new DatabaseException(stringBuilder.toString());
        }
        double d = ((Number)object).doubleValue();
        if (d >= -2.147483648E9 && d <= 2.147483647E9) {
            return ((Number)object).intValue();
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Numeric value out of 32-bit integer range: ");
        ((StringBuilder)object).append(d);
        ((StringBuilder)object).append(". Did you mean to use a long or double instead of an int?");
        throw new DatabaseException(((StringBuilder)object).toString());
    }

    private static Long convertLong(Object object) {
        if (object instanceof Integer) {
            return ((Integer)object).longValue();
        }
        if (object instanceof Long) {
            return (Long)object;
        }
        if (object instanceof Double) {
            Double d = (Double)object;
            if (d >= -9.223372036854776E18 && d <= 9.223372036854776E18) {
                return d.longValue();
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Numeric value out of 64-bit long range: ");
            ((StringBuilder)object).append(d);
            ((StringBuilder)object).append(". Did you mean to use a double instead of a long?");
            throw new DatabaseException(((StringBuilder)object).toString());
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Failed to convert a value of type ");
        stringBuilder.append(object.getClass().getName());
        stringBuilder.append(" to long");
        throw new DatabaseException(stringBuilder.toString());
    }

    private static String convertString(Object object) {
        if (object instanceof String) {
            return (String)object;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Failed to convert value of type ");
        stringBuilder.append(object.getClass().getName());
        stringBuilder.append(" to String");
        throw new DatabaseException(stringBuilder.toString());
    }

    public static <T> T convertToCustomClass(Object object, GenericTypeIndicator<T> object2) {
        if ((object2 = object2.getClass().getGenericSuperclass()) instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType)object2;
            if (parameterizedType.getRawType().equals(GenericTypeIndicator.class)) {
                return CustomClassMapper.deserializeToType(object, parameterizedType.getActualTypeArguments()[0]);
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Not a direct subclass of GenericTypeIndicator: ");
            ((StringBuilder)object).append(object2);
            throw new DatabaseException(((StringBuilder)object).toString());
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Not a direct subclass of GenericTypeIndicator: ");
        ((StringBuilder)object).append(object2);
        throw new DatabaseException(((StringBuilder)object).toString());
    }

    public static <T> T convertToCustomClass(Object object, Class<T> clazz) {
        return CustomClassMapper.deserializeToClass(object, clazz);
    }

    public static Object convertToPlainJavaTypes(Object object) {
        return CustomClassMapper.serialize(object);
    }

    public static Map<String, Object> convertToPlainJavaTypes(Map<String, Object> object) {
        object = CustomClassMapper.serialize(object);
        Utilities.hardAssert(object instanceof Map);
        return (Map)object;
    }

    private static <T> T deserializeToClass(Object object, Class<T> clazz) {
        if (object == null) {
            return null;
        }
        if (!(clazz.isPrimitive() || Number.class.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz))) {
            if (String.class.isAssignableFrom(clazz)) {
                return (T)CustomClassMapper.convertString(object);
            }
            if (!clazz.isArray()) {
                if (clazz.getTypeParameters().length <= 0) {
                    if (clazz.equals(Object.class)) {
                        return (T)object;
                    }
                    if (clazz.isEnum()) {
                        return CustomClassMapper.deserializeToEnum(object, clazz);
                    }
                    return CustomClassMapper.convertBean(object, clazz);
                }
                object = new StringBuilder();
                ((StringBuilder)object).append("Class ");
                ((StringBuilder)object).append(clazz.getName());
                ((StringBuilder)object).append(" has generic type parameters, please use GenericTypeIndicator instead");
                throw new DatabaseException(((StringBuilder)object).toString());
            }
            throw new DatabaseException("Converting to Arrays is not supported, please use Listsinstead");
        }
        return CustomClassMapper.deserializeToPrimitive(object, clazz);
    }

    private static <T> T deserializeToEnum(Object object, Class<T> clazz) {
        if (object instanceof String) {
            T t;
            object = (String)object;
            try {
                t = Enum.valueOf(clazz, (String)object);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Could not find enum value of ");
                stringBuilder.append(clazz.getName());
                stringBuilder.append(" for value \"");
                stringBuilder.append((String)object);
                stringBuilder.append("\"");
                throw new DatabaseException(stringBuilder.toString());
            }
            return t;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Expected a String while deserializing to enum ");
        stringBuilder.append(clazz);
        stringBuilder.append(" but got a ");
        stringBuilder.append(object.getClass());
        throw new DatabaseException(stringBuilder.toString());
    }

    private static <T> T deserializeToParameterizedType(Object serializable, ParameterizedType typeArray) {
        Class object2 = (Class)typeArray.getRawType();
        if (List.class.isAssignableFrom(object2)) {
            typeArray = typeArray.getActualTypeArguments()[0];
            if (serializable instanceof List) {
                List list = (List)((Object)serializable);
                serializable = new ArrayList(list.size());
                Iterator iterator2 = list.iterator();
                while (iterator2.hasNext()) {
                    serializable.add(CustomClassMapper.deserializeToType(iterator2.next(), (Type)typeArray));
                }
                return (T)serializable;
            }
            typeArray = new StringBuilder();
            ((StringBuilder)typeArray).append("Expected a List while deserializing, but got a ");
            ((StringBuilder)typeArray).append(serializable.getClass());
            throw new DatabaseException(((StringBuilder)typeArray).toString());
        }
        if (Map.class.isAssignableFrom(object2)) {
            Type type = typeArray.getActualTypeArguments()[0];
            typeArray = typeArray.getActualTypeArguments()[1];
            if (type.equals(String.class)) {
                Map<String, Object> map = CustomClassMapper.expectMap(serializable);
                serializable = new HashMap();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    ((HashMap)serializable).put(entry.getKey(), CustomClassMapper.deserializeToType(entry.getValue(), (Type)typeArray));
                }
                return (T)serializable;
            }
            serializable = new StringBuilder();
            ((StringBuilder)serializable).append("Only Maps with string keys are supported, but found Map with key type ");
            ((StringBuilder)serializable).append(type);
            throw new DatabaseException(((StringBuilder)serializable).toString());
        }
        if (!Collection.class.isAssignableFrom(object2)) {
            serializable = CustomClassMapper.expectMap(serializable);
            BeanMapper beanMapper = CustomClassMapper.loadOrCreateBeanMapperForClass(object2);
            HashMap hashMap = new HashMap();
            TypeVariable<Class<T>>[] typeVariableArray = beanMapper.clazz.getTypeParameters();
            if (((Object)(typeArray = typeArray.getActualTypeArguments())).length == typeVariableArray.length) {
                for (int i = 0; i < typeVariableArray.length; ++i) {
                    hashMap.put(typeVariableArray[i], (Type)typeArray[i]);
                }
                return beanMapper.deserialize((Map<String, Object>)((Object)serializable), hashMap);
            }
            throw new IllegalStateException("Mismatched lengths for type variables and actual types");
        }
        serializable = new DatabaseException("Collections are not supported, please use Lists instead");
        throw serializable;
    }

    private static <T> T deserializeToPrimitive(Object object, Class<T> clazz) {
        if (!Integer.class.isAssignableFrom(clazz) && !Integer.TYPE.isAssignableFrom(clazz)) {
            if (!Boolean.class.isAssignableFrom(clazz) && !Boolean.TYPE.isAssignableFrom(clazz)) {
                if (!Double.class.isAssignableFrom(clazz) && !Double.TYPE.isAssignableFrom(clazz)) {
                    if (!Long.class.isAssignableFrom(clazz) && !Long.TYPE.isAssignableFrom(clazz)) {
                        if (!Float.class.isAssignableFrom(clazz) && !Float.TYPE.isAssignableFrom(clazz)) {
                            throw new DatabaseException(String.format("Deserializing values to %s is not supported", clazz.getSimpleName()));
                        }
                        return (T)Float.valueOf(CustomClassMapper.convertDouble(object).floatValue());
                    }
                    return (T)CustomClassMapper.convertLong(object);
                }
                return (T)CustomClassMapper.convertDouble(object);
            }
            return (T)CustomClassMapper.convertBoolean(object);
        }
        return (T)CustomClassMapper.convertInteger(object);
    }

    private static <T> T deserializeToType(Object object, Type type) {
        if (object == null) {
            return null;
        }
        if (type instanceof ParameterizedType) {
            return CustomClassMapper.deserializeToParameterizedType(object, (ParameterizedType)type);
        }
        if (type instanceof Class) {
            return CustomClassMapper.deserializeToClass(object, (Class)type);
        }
        boolean bl = type instanceof WildcardType;
        boolean bl2 = true;
        boolean bl3 = true;
        if (bl) {
            if (((WildcardType)type).getLowerBounds().length <= 0) {
                Type[] typeArray = ((WildcardType)type).getUpperBounds();
                if (typeArray.length <= 0) {
                    bl3 = false;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Wildcard type ");
                stringBuilder.append(type);
                stringBuilder.append(" is not upper bounded.");
                Utilities.hardAssert(bl3, stringBuilder.toString());
                return CustomClassMapper.deserializeToType(object, typeArray[0]);
            }
            throw new DatabaseException("Generic lower-bounded wildcard types are not supported");
        }
        if (type instanceof TypeVariable) {
            Type[] typeArray = ((TypeVariable)type).getBounds();
            bl3 = typeArray.length > 0 ? bl2 : false;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Wildcard type ");
            stringBuilder.append(type);
            stringBuilder.append(" is not upper bounded.");
            Utilities.hardAssert(bl3, stringBuilder.toString());
            return CustomClassMapper.deserializeToType(object, typeArray[0]);
        }
        if (type instanceof GenericArrayType) {
            throw new DatabaseException("Generic Arrays are not supported, please use Lists instead");
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Unknown type encountered: ");
        ((StringBuilder)object).append(type);
        throw new IllegalStateException(((StringBuilder)object).toString());
    }

    private static Map<String, Object> expectMap(Object object) {
        if (object instanceof Map) {
            return (Map)object;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Expected a Map while deserializing, but got a ");
        stringBuilder.append(object.getClass());
        throw new DatabaseException(stringBuilder.toString());
    }

    private static <T> BeanMapper<T> loadOrCreateBeanMapperForClass(Class<T> clazz) {
        BeanMapper<T> beanMapper;
        ConcurrentMap<Class<?>, BeanMapper<?>> concurrentMap = mappers;
        BeanMapper<T> beanMapper2 = beanMapper = (BeanMapper<T>)concurrentMap.get(clazz);
        if (beanMapper == null) {
            beanMapper2 = new BeanMapper<T>(clazz);
            concurrentMap.put(clazz, beanMapper2);
        }
        return beanMapper2;
    }

    private static <T> Object serialize(T object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Number) {
            if (!(object instanceof Float) && !(object instanceof Double)) {
                if (!(object instanceof Long) && !(object instanceof Integer)) {
                    throw new DatabaseException(String.format("Numbers of type %s are not supported, please use an int, long, float or double", object.getClass().getSimpleName()));
                }
                return object;
            }
            double d = ((Number)object).doubleValue();
            if (d <= 9.223372036854776E18 && d >= -9.223372036854776E18 && Math.floor(d) == d) {
                return ((Number)object).longValue();
            }
            return d;
        }
        if (object instanceof String) {
            return object;
        }
        if (object instanceof Boolean) {
            return object;
        }
        if (!(object instanceof Character)) {
            if (object instanceof Map) {
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                for (Map.Entry entry : ((Map)object).entrySet()) {
                    Object k = entry.getKey();
                    if (k instanceof String) {
                        hashMap.put((String)k, CustomClassMapper.serialize(entry.getValue()));
                        continue;
                    }
                    throw new DatabaseException("Maps with non-string keys are not supported");
                }
                return hashMap;
            }
            if (object instanceof Collection) {
                if (object instanceof List) {
                    Object object2 = (List)object;
                    object = new ArrayList(object2.size());
                    object2 = object2.iterator();
                    while (object2.hasNext()) {
                        object.add(CustomClassMapper.serialize(object2.next()));
                    }
                    return object;
                }
                throw new DatabaseException("Serializing Collections is not supported, please use Lists instead");
            }
            if (!object.getClass().isArray()) {
                if (object instanceof Enum) {
                    return ((Enum)object).name();
                }
                return CustomClassMapper.loadOrCreateBeanMapperForClass(object.getClass()).serialize(object);
            }
            throw new DatabaseException("Serializing Arrays is not supported, please use Lists instead");
        }
        object = new DatabaseException("Characters are not supported, please use Strings");
        throw object;
    }

    private static class BeanMapper<T> {
        private final Class<T> clazz;
        private final Constructor<T> constructor;
        private final Map<String, Field> fields;
        private final Map<String, Method> getters;
        private final Map<String, String> properties;
        private final Map<String, Method> setters;
        private final boolean throwOnUnknownProperties;
        private final boolean warnOnUnknownProperties;

        /*
         * WARNING - void declaration
         */
        public BeanMapper(Class<T> serializable) {
            Class clazz;
            void var4_5;
            this.clazz = serializable;
            this.throwOnUnknownProperties = ((Class)serializable).isAnnotationPresent(ThrowOnExtraProperties.class);
            this.warnOnUnknownProperties = ((Class)serializable).isAnnotationPresent(IgnoreExtraProperties.class) ^ true;
            this.properties = new HashMap<String, String>();
            this.setters = new HashMap<String, Method>();
            this.getters = new HashMap<String, Method>();
            this.fields = new HashMap<String, Field>();
            try {
                Constructor object22 = ((Class)serializable).getDeclaredConstructor(new Class[0]);
                object22.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Object var4_4 = null;
            }
            this.constructor = var4_5;
            for (Method method : ((Class)serializable).getMethods()) {
                if (!BeanMapper.shouldIncludeGetter(method)) continue;
                String string2 = BeanMapper.propertyName(method);
                this.addProperty(string2);
                method.setAccessible(true);
                if (!this.getters.containsKey(string2)) {
                    this.getters.put(string2, method);
                    continue;
                }
                serializable = new StringBuilder();
                ((StringBuilder)serializable).append("Found conflicting getters for name: ");
                ((StringBuilder)serializable).append(method.getName());
                throw new DatabaseException(((StringBuilder)serializable).toString());
            }
            for (Field field : ((Class)serializable).getFields()) {
                if (!BeanMapper.shouldIncludeField(field)) continue;
                this.addProperty(BeanMapper.propertyName(field));
            }
            Serializable serializable2 = serializable;
            do {
                void var4_11;
                for (Method method : var4_11.getDeclaredMethods()) {
                    String string3;
                    String string4;
                    if (!BeanMapper.shouldIncludeSetter(method) || (string4 = this.properties.get((string3 = BeanMapper.propertyName(method)).toLowerCase(Locale.US))) == null) continue;
                    if (string4.equals(string3)) {
                        Method method2 = this.setters.get(string3);
                        if (method2 == null) {
                            method.setAccessible(true);
                            this.setters.put(string3, method);
                            continue;
                        }
                        if (BeanMapper.isSetterOverride(method, method2)) continue;
                        serializable = new StringBuilder();
                        ((StringBuilder)serializable).append("Found a conflicting setters with name: ");
                        ((StringBuilder)serializable).append(method.getName());
                        ((StringBuilder)serializable).append(" (conflicts with ");
                        ((StringBuilder)serializable).append(method2.getName());
                        ((StringBuilder)serializable).append(" defined on ");
                        ((StringBuilder)serializable).append(method2.getDeclaringClass().getName());
                        ((StringBuilder)serializable).append(")");
                        throw new DatabaseException(((StringBuilder)serializable).toString());
                    }
                    serializable = new StringBuilder();
                    ((StringBuilder)serializable).append("Found setter with invalid case-sensitive name: ");
                    ((StringBuilder)serializable).append(method.getName());
                    throw new DatabaseException(((StringBuilder)serializable).toString());
                }
                for (AccessibleObject accessibleObject : var4_11.getDeclaredFields()) {
                    String string5 = BeanMapper.propertyName((Field)accessibleObject);
                    if (!this.properties.containsKey(string5.toLowerCase(Locale.US)) || this.fields.containsKey(string5)) continue;
                    ((Field)accessibleObject).setAccessible(true);
                    this.fields.put(string5, (Field)accessibleObject);
                }
                clazz = var4_11.getSuperclass();
                if (clazz == null) break;
                Class clazz2 = clazz;
            } while (!clazz.equals(Object.class));
            if (!this.properties.isEmpty()) {
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("No properties to serialize found on class ");
            stringBuilder.append(((Class)serializable).getName());
            serializable = new DatabaseException(stringBuilder.toString());
            throw serializable;
        }

        private void addProperty(String string2) {
            CharSequence charSequence = this.properties.put(string2.toLowerCase(Locale.US), string2);
            if (charSequence != null && !string2.equals(charSequence)) {
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append("Found two getters or fields with conflicting case sensitivity for property: ");
                ((StringBuilder)charSequence).append(string2.toLowerCase(Locale.US));
                throw new DatabaseException(((StringBuilder)charSequence).toString());
            }
        }

        private static String annotatedName(AccessibleObject accessibleObject) {
            if (accessibleObject.isAnnotationPresent(PropertyName.class)) {
                return accessibleObject.getAnnotation(PropertyName.class).value();
            }
            return null;
        }

        private static boolean isSetterOverride(Method method, Method method2) {
            Utilities.hardAssert(method.getDeclaringClass().isAssignableFrom(method2.getDeclaringClass()), "Expected override from a base class");
            Utilities.hardAssert(method.getReturnType().equals(Void.TYPE), "Expected void return type");
            Utilities.hardAssert(method2.getReturnType().equals(Void.TYPE), "Expected void return type");
            Class<?>[] classArray = method.getParameterTypes();
            Class<?>[] classArray2 = method2.getParameterTypes();
            int n = classArray.length;
            boolean bl = false;
            boolean bl2 = n == 1;
            Utilities.hardAssert(bl2, "Expected exactly one parameter");
            bl2 = classArray2.length == 1;
            Utilities.hardAssert(bl2, "Expected exactly one parameter");
            bl2 = method.getName().equals(method2.getName()) && classArray[0].equals(classArray2[0]) ? true : bl;
            return bl2;
        }

        private static String propertyName(Field object) {
            String string2 = BeanMapper.annotatedName((AccessibleObject)object);
            object = string2 != null ? string2 : ((Field)object).getName();
            return object;
        }

        private static String propertyName(Method object) {
            String string2 = BeanMapper.annotatedName((AccessibleObject)object);
            object = string2 != null ? string2 : BeanMapper.serializedName(((Method)object).getName());
            return object;
        }

        private Type resolveType(Type type, Map<TypeVariable<Class<T>>, Type> object) {
            if (type instanceof TypeVariable) {
                if ((object = object.get(type)) != null) {
                    return object;
                }
                object = new StringBuilder();
                ((StringBuilder)object).append("Could not resolve type ");
                ((StringBuilder)object).append(type);
                throw new IllegalStateException(((StringBuilder)object).toString());
            }
            return type;
        }

        private static String serializedName(String object) {
            int n;
            String[] stringArray = new String[3];
            stringArray[0] = "get";
            stringArray[1] = "set";
            stringArray[2] = "is";
            CharSequence charSequence = null;
            int n2 = stringArray.length;
            for (n = 0; n < n2; ++n) {
                String string2 = stringArray[n];
                if (!((String)object).startsWith(string2)) continue;
                charSequence = string2;
            }
            if (charSequence != null) {
                object = ((String)object).substring(((String)charSequence).length()).toCharArray();
                for (n = 0; n < ((Object)object).length && Character.isUpperCase((char)object[n]); ++n) {
                    object[n] = Character.toLowerCase((char)object[n]);
                }
                return new String((char[])object);
            }
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append("Unknown Bean prefix for method: ");
            ((StringBuilder)charSequence).append((String)object);
            object = new IllegalArgumentException(((StringBuilder)charSequence).toString());
            throw object;
        }

        private static boolean shouldIncludeField(Field field) {
            if (field.getDeclaringClass().equals(Object.class)) {
                return false;
            }
            if (!Modifier.isPublic(field.getModifiers())) {
                return false;
            }
            if (Modifier.isStatic(field.getModifiers())) {
                return false;
            }
            if (Modifier.isTransient(field.getModifiers())) {
                return false;
            }
            return !field.isAnnotationPresent(Exclude.class);
        }

        private static boolean shouldIncludeGetter(Method method) {
            if (!method.getName().startsWith("get") && !method.getName().startsWith("is")) {
                return false;
            }
            if (method.getDeclaringClass().equals(Object.class)) {
                return false;
            }
            if (!Modifier.isPublic(method.getModifiers())) {
                return false;
            }
            if (Modifier.isStatic(method.getModifiers())) {
                return false;
            }
            if (method.getReturnType().equals(Void.TYPE)) {
                return false;
            }
            if (method.getParameterTypes().length != 0) {
                return false;
            }
            return !method.isAnnotationPresent(Exclude.class);
        }

        private static boolean shouldIncludeSetter(Method method) {
            if (!method.getName().startsWith("set")) {
                return false;
            }
            if (method.getDeclaringClass().equals(Object.class)) {
                return false;
            }
            if (Modifier.isStatic(method.getModifiers())) {
                return false;
            }
            if (!method.getReturnType().equals(Void.TYPE)) {
                return false;
            }
            if (method.getParameterTypes().length != 1) {
                return false;
            }
            return !method.isAnnotationPresent(Exclude.class);
        }

        public T deserialize(Map<String, Object> map) {
            return this.deserialize(map, Collections.<TypeVariable<Class<T>>, Type>emptyMap());
        }

        public T deserialize(Map<String, Object> entry2, Map<TypeVariable<Class<T>>, Type> map) {
            Executable executable = this.constructor;
            if (executable != null) {
                T t;
                try {
                    t = executable.newInstance(new Object[0]);
                }
                catch (InvocationTargetException invocationTargetException) {
                    throw new RuntimeException(invocationTargetException);
                }
                catch (IllegalAccessException illegalAccessException) {
                    throw new RuntimeException(illegalAccessException);
                }
                catch (InstantiationException instantiationException) {
                    throw new RuntimeException(instantiationException);
                }
                for (Map.Entry entry2 : entry2.entrySet()) {
                    Object object = (Type[])entry2.getKey();
                    if (this.setters.containsKey(object)) {
                        executable = this.setters.get(object);
                        if (((Type[])(object = ((Method)executable).getGenericParameterTypes())).length == 1) {
                            object = this.resolveType(object[0], map);
                            entry2 = CustomClassMapper.deserializeToType(entry2.getValue(), (Type)object);
                            try {
                                ((Method)executable).invoke(t, entry2);
                                continue;
                            }
                            catch (InvocationTargetException invocationTargetException) {
                                throw new RuntimeException(invocationTargetException);
                            }
                            catch (IllegalAccessException illegalAccessException) {
                                throw new RuntimeException(illegalAccessException);
                            }
                        }
                        throw new IllegalStateException("Setter does not have exactly one parameter");
                    }
                    if (this.fields.containsKey(object)) {
                        executable = this.fields.get(object);
                        object = this.resolveType(((Field)((Object)executable)).getGenericType(), map);
                        entry2 = CustomClassMapper.deserializeToType(entry2.getValue(), (Type)object);
                        try {
                            ((Field)((Object)executable)).set(t, entry2);
                            continue;
                        }
                        catch (IllegalAccessException illegalAccessException) {
                            throw new RuntimeException(illegalAccessException);
                        }
                    }
                    entry2 = new StringBuilder();
                    ((StringBuilder)((Object)entry2)).append("No setter/field for ");
                    ((StringBuilder)((Object)entry2)).append((String)object);
                    ((StringBuilder)((Object)entry2)).append(" found on class ");
                    ((StringBuilder)((Object)entry2)).append(this.clazz.getName());
                    entry2 = executable = ((StringBuilder)((Object)entry2)).toString();
                    if (this.properties.containsKey(((String)object).toLowerCase(Locale.US))) {
                        entry2 = new StringBuilder();
                        ((StringBuilder)((Object)entry2)).append((String)((Object)executable));
                        ((StringBuilder)((Object)entry2)).append(" (fields/setters are case sensitive!)");
                        entry2 = ((StringBuilder)((Object)entry2)).toString();
                    }
                    if (!this.throwOnUnknownProperties) {
                        if (!this.warnOnUnknownProperties) continue;
                        Log.w((String)CustomClassMapper.LOG_TAG, (String)((Object)entry2));
                        continue;
                    }
                    throw new DatabaseException((String)((Object)entry2));
                }
                return t;
            }
            entry2 = new StringBuilder();
            ((StringBuilder)((Object)entry2)).append("Class ");
            ((StringBuilder)((Object)entry2)).append(this.clazz.getName());
            ((StringBuilder)((Object)entry2)).append(" does not define a no-argument constructor. If you are using ProGuard, make sure these constructors are not stripped.");
            entry2 = new DatabaseException(((StringBuilder)((Object)entry2)).toString());
            throw entry2;
        }

        public Map<String, Object> serialize(T object) {
            block10: {
                if (!this.clazz.isAssignableFrom(object.getClass())) break block10;
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                for (String string2 : this.properties.values()) {
                    block11: {
                        Object object2;
                        if (this.getters.containsKey(string2)) {
                            object2 = this.getters.get(string2);
                            try {
                                object2 = ((Method)object2).invoke(object, new Object[0]);
                            }
                            catch (InvocationTargetException invocationTargetException) {
                                throw new RuntimeException(invocationTargetException);
                            }
                            catch (IllegalAccessException illegalAccessException) {
                                throw new RuntimeException(illegalAccessException);
                            }
                        }
                        object2 = this.fields.get(string2);
                        if (object2 == null) break block11;
                        try {
                            object2 = ((Field)object2).get(object);
                        }
                        catch (IllegalAccessException illegalAccessException) {
                            throw new RuntimeException(illegalAccessException);
                        }
                        hashMap.put(string2, CustomClassMapper.serialize(object2));
                        continue;
                    }
                    object = new StringBuilder();
                    ((StringBuilder)object).append("Bean property without field or getter:");
                    ((StringBuilder)object).append(string2);
                    throw new IllegalStateException(((StringBuilder)object).toString());
                }
                return hashMap;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Can't serialize object of class ");
            stringBuilder.append(object.getClass());
            stringBuilder.append(" with BeanMapper for class ");
            stringBuilder.append(this.clazz);
            object = new IllegalArgumentException(stringBuilder.toString());
            throw object;
        }
    }
}

