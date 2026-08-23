/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public final class ConstructorConstructor {
    private final Map<Type, InstanceCreator<?>> instanceCreators;

    public ConstructorConstructor(Map<Type, InstanceCreator<?>> map) {
        this.instanceCreators = map;
    }

    private <T> ObjectConstructor<T> newDefaultConstructor(Class<? super T> object) {
        try {
            object = ((Class)object).getDeclaredConstructor(new Class[0]);
            if (!((AccessibleObject)object).isAccessible()) {
                ((Constructor)object).setAccessible(true);
            }
            object = new ObjectConstructor<T>(this, (Constructor)object){
                final ConstructorConstructor this$0;
                final Constructor val$constructor;
                {
                    this.this$0 = constructorConstructor;
                    this.val$constructor = constructor;
                }

                @Override
                public T construct() {
                    Object t;
                    try {
                        t = this.val$constructor.newInstance(null);
                    }
                    catch (IllegalAccessException illegalAccessException) {
                        throw new AssertionError((Object)illegalAccessException);
                    }
                    catch (InvocationTargetException invocationTargetException) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Failed to invoke ");
                        stringBuilder.append(this.val$constructor);
                        stringBuilder.append(" with no args");
                        throw new RuntimeException(stringBuilder.toString(), invocationTargetException.getTargetException());
                    }
                    catch (InstantiationException instantiationException) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Failed to invoke ");
                        stringBuilder.append(this.val$constructor);
                        stringBuilder.append(" with no args");
                        throw new RuntimeException(stringBuilder.toString(), instantiationException);
                    }
                    return t;
                }
            };
            return object;
        }
        catch (NoSuchMethodException noSuchMethodException) {
            return null;
        }
    }

    private <T> ObjectConstructor<T> newDefaultImplementationConstructor(Type type, Class<? super T> clazz) {
        if (Collection.class.isAssignableFrom(clazz)) {
            if (SortedSet.class.isAssignableFrom(clazz)) {
                return new ObjectConstructor<T>(this){
                    final ConstructorConstructor this$0;
                    {
                        this.this$0 = constructorConstructor;
                    }

                    @Override
                    public T construct() {
                        return new TreeSet();
                    }
                };
            }
            if (EnumSet.class.isAssignableFrom(clazz)) {
                return new ObjectConstructor<T>(this, type){
                    final ConstructorConstructor this$0;
                    final Type val$type;
                    {
                        this.this$0 = constructorConstructor;
                        this.val$type = type;
                    }

                    @Override
                    public T construct() {
                        Object object = this.val$type;
                        if (object instanceof ParameterizedType) {
                            if ((object = ((ParameterizedType)object).getActualTypeArguments()[0]) instanceof Class) {
                                return EnumSet.noneOf((Class)object);
                            }
                            object = new StringBuilder();
                            ((StringBuilder)object).append("Invalid EnumSet type: ");
                            ((StringBuilder)object).append(this.val$type.toString());
                            throw new JsonIOException(((StringBuilder)object).toString());
                        }
                        object = new StringBuilder();
                        ((StringBuilder)object).append("Invalid EnumSet type: ");
                        ((StringBuilder)object).append(this.val$type.toString());
                        throw new JsonIOException(((StringBuilder)object).toString());
                    }
                };
            }
            if (Set.class.isAssignableFrom(clazz)) {
                return new ObjectConstructor<T>(this){
                    final ConstructorConstructor this$0;
                    {
                        this.this$0 = constructorConstructor;
                    }

                    @Override
                    public T construct() {
                        return new LinkedHashSet();
                    }
                };
            }
            if (Queue.class.isAssignableFrom(clazz)) {
                return new ObjectConstructor<T>(this){
                    final ConstructorConstructor this$0;
                    {
                        this.this$0 = constructorConstructor;
                    }

                    @Override
                    public T construct() {
                        return new ArrayDeque();
                    }
                };
            }
            return new ObjectConstructor<T>(this){
                final ConstructorConstructor this$0;
                {
                    this.this$0 = constructorConstructor;
                }

                @Override
                public T construct() {
                    return new ArrayList();
                }
            };
        }
        if (Map.class.isAssignableFrom(clazz)) {
            if (ConcurrentNavigableMap.class.isAssignableFrom(clazz)) {
                return new ObjectConstructor<T>(this){
                    final ConstructorConstructor this$0;
                    {
                        this.this$0 = constructorConstructor;
                    }

                    @Override
                    public T construct() {
                        return new ConcurrentSkipListMap();
                    }
                };
            }
            if (ConcurrentMap.class.isAssignableFrom(clazz)) {
                return new ObjectConstructor<T>(this){
                    final ConstructorConstructor this$0;
                    {
                        this.this$0 = constructorConstructor;
                    }

                    @Override
                    public T construct() {
                        return new ConcurrentHashMap();
                    }
                };
            }
            if (SortedMap.class.isAssignableFrom(clazz)) {
                return new ObjectConstructor<T>(this){
                    final ConstructorConstructor this$0;
                    {
                        this.this$0 = constructorConstructor;
                    }

                    @Override
                    public T construct() {
                        return new TreeMap();
                    }
                };
            }
            if (type instanceof ParameterizedType && !String.class.isAssignableFrom(TypeToken.get(((ParameterizedType)type).getActualTypeArguments()[0]).getRawType())) {
                return new ObjectConstructor<T>(this){
                    final ConstructorConstructor this$0;
                    {
                        this.this$0 = constructorConstructor;
                    }

                    @Override
                    public T construct() {
                        return new LinkedHashMap();
                    }
                };
            }
            return new ObjectConstructor<T>(this){
                final ConstructorConstructor this$0;
                {
                    this.this$0 = constructorConstructor;
                }

                @Override
                public T construct() {
                    return new LinkedTreeMap();
                }
            };
        }
        return null;
    }

    private <T> ObjectConstructor<T> newUnsafeAllocator(Type type, Class<? super T> clazz) {
        return new ObjectConstructor<T>(this, clazz, type){
            final ConstructorConstructor this$0;
            private final UnsafeAllocator unsafeAllocator;
            final Class val$rawType;
            final Type val$type;
            {
                this.this$0 = constructorConstructor;
                this.val$rawType = clazz;
                this.val$type = type;
                this.unsafeAllocator = UnsafeAllocator.create();
            }

            @Override
            public T construct() {
                Object t;
                try {
                    t = this.unsafeAllocator.newInstance(this.val$rawType);
                }
                catch (Exception exception) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unable to invoke no-args constructor for ");
                    stringBuilder.append(this.val$type);
                    stringBuilder.append(". ");
                    stringBuilder.append("Register an InstanceCreator with Gson for this type may fix this problem.");
                    throw new RuntimeException(stringBuilder.toString(), exception);
                }
                return t;
            }
        };
    }

    public <T> ObjectConstructor<T> get(TypeToken<T> object) {
        Type type = ((TypeToken)object).getType();
        object = ((TypeToken)object).getRawType();
        ObjectConstructor<T> objectConstructor = this.instanceCreators.get(type);
        if (objectConstructor != null) {
            return new ObjectConstructor<T>(this, (InstanceCreator)objectConstructor, type){
                final ConstructorConstructor this$0;
                final Type val$type;
                final InstanceCreator val$typeCreator;
                {
                    this.this$0 = constructorConstructor;
                    this.val$typeCreator = instanceCreator;
                    this.val$type = type;
                }

                @Override
                public T construct() {
                    return this.val$typeCreator.createInstance(this.val$type);
                }
            };
        }
        objectConstructor = this.instanceCreators.get(object);
        if (objectConstructor != null) {
            return new ObjectConstructor<T>(this, (InstanceCreator)objectConstructor, type){
                final ConstructorConstructor this$0;
                final InstanceCreator val$rawTypeCreator;
                final Type val$type;
                {
                    this.this$0 = constructorConstructor;
                    this.val$rawTypeCreator = instanceCreator;
                    this.val$type = type;
                }

                @Override
                public T construct() {
                    return this.val$rawTypeCreator.createInstance(this.val$type);
                }
            };
        }
        objectConstructor = this.newDefaultConstructor((Class<? super T>)object);
        if (objectConstructor != null) {
            return objectConstructor;
        }
        objectConstructor = this.newDefaultImplementationConstructor(type, (Class<? super T>)object);
        if (objectConstructor != null) {
            return objectConstructor;
        }
        return this.newUnsafeAllocator(type, (Class<? super T>)object);
    }

    public String toString() {
        return this.instanceCreators.toString();
    }
}

