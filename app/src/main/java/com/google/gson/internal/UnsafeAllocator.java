/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal;

import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public abstract class UnsafeAllocator {
    private static void assertInstantiable(Class<?> clazz) {
        int n = clazz.getModifiers();
        if (!Modifier.isInterface(n)) {
            if (!Modifier.isAbstract(n)) {
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Abstract class can't be instantiated! Class name: ");
            stringBuilder.append(clazz.getName());
            throw new UnsupportedOperationException(stringBuilder.toString());
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Interface can't be instantiated! Interface name: ");
        stringBuilder.append(clazz.getName());
        throw new UnsupportedOperationException(stringBuilder.toString());
    }

    public static UnsafeAllocator create() {
        try {
            Object object = Class.forName("sun.misc.Unsafe");
            Object object2 = ((Class)object).getDeclaredField("theUnsafe");
            ((Field)object2).setAccessible(true);
            object2 = ((Field)object2).get(null);
            object = new UnsafeAllocator(((Class)object).getMethod("allocateInstance", Class.class), object2){
                final Method val$allocateInstance;
                final Object val$unsafe;
                {
                    this.val$allocateInstance = method;
                    this.val$unsafe = object;
                }

                @Override
                public <T> T newInstance(Class<T> clazz) throws Exception {
                    UnsafeAllocator.assertInstantiable(clazz);
                    return (T)this.val$allocateInstance.invoke(this.val$unsafe, clazz);
                }
            };
            return object;
        }
        catch (Exception exception) {
            try {
                Object object = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", Class.class);
                ((Method)object).setAccessible(true);
                int n = (Integer)((Method)object).invoke(null, Object.class);
                object = ObjectStreamClass.class.getDeclaredMethod("newInstance", Class.class, Integer.TYPE);
                ((Method)object).setAccessible(true);
                object = new UnsafeAllocator((Method)object, n){
                    final int val$constructorId;
                    final Method val$newInstance;
                    {
                        this.val$newInstance = method;
                        this.val$constructorId = n;
                    }

                    @Override
                    public <T> T newInstance(Class<T> clazz) throws Exception {
                        UnsafeAllocator.assertInstantiable(clazz);
                        return (T)this.val$newInstance.invoke(null, clazz, this.val$constructorId);
                    }
                };
                return object;
            }
            catch (Exception exception2) {
                try {
                    Object object = ObjectInputStream.class.getDeclaredMethod("newInstance", Class.class, Class.class);
                    ((Method)object).setAccessible(true);
                    object = new UnsafeAllocator((Method)object){
                        final Method val$newInstance;
                        {
                            this.val$newInstance = method;
                        }

                        @Override
                        public <T> T newInstance(Class<T> clazz) throws Exception {
                            UnsafeAllocator.assertInstantiable(clazz);
                            return (T)this.val$newInstance.invoke(null, clazz, Object.class);
                        }
                    };
                    return object;
                }
                catch (Exception exception3) {
                    return new UnsafeAllocator(){

                        @Override
                        public <T> T newInstance(Class<T> clazz) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Cannot allocate ");
                            stringBuilder.append(clazz);
                            throw new UnsupportedOperationException(stringBuilder.toString());
                        }
                    };
                }
            }
        }
    }

    public abstract <T> T newInstance(Class<T> var1) throws Exception;
}

