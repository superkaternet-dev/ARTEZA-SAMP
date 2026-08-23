/*
 * Decompiled with CFR 0.152.
 */
package androidx.lifecycle;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class ClassesInfoCache {
    private static final int CALL_TYPE_NO_ARG = 0;
    private static final int CALL_TYPE_PROVIDER = 1;
    private static final int CALL_TYPE_PROVIDER_WITH_EVENT = 2;
    static ClassesInfoCache sInstance = new ClassesInfoCache();
    private final Map<Class<?>, CallbackInfo> mCallbackMap = new HashMap();
    private final Map<Class<?>, Boolean> mHasLifecycleMethods = new HashMap();

    ClassesInfoCache() {
    }

    /*
     * WARNING - void declaration
     */
    private CallbackInfo createInfo(Class<?> clazz, Method[] object) {
        void var2_4;
        int n;
        CallbackInfo callbackInfo;
        Class<?> clazz2 = clazz.getSuperclass();
        HashMap<MethodReference, Lifecycle.Event> hashMap = new HashMap<MethodReference, Lifecycle.Event>();
        if (clazz2 != null && (callbackInfo = this.getInfo(clazz2)) != null) {
            hashMap.putAll(callbackInfo.mHandlerToEvent);
        }
        Class<?>[] classArray = clazz.getInterfaces();
        int n2 = classArray.length;
        for (n = 0; n < n2; ++n) {
            for (Map.Entry<MethodReference, Lifecycle.Event> entry : this.getInfo(classArray[n]).mHandlerToEvent.entrySet()) {
                this.verifyAndPutHandler(hashMap, entry.getKey(), entry.getValue(), clazz);
            }
        }
        if (object == null) {
            Method[] methodArray = this.getDeclaredMethods(clazz);
        }
        boolean bl = false;
        for (void var8_11 : var2_4) {
            OnLifecycleEvent onLifecycleEvent = var8_11.getAnnotation(OnLifecycleEvent.class);
            if (onLifecycleEvent == null) continue;
            bl = true;
            Class<?>[] classArray2 = var8_11.getParameterTypes();
            n = 0;
            if (classArray2.length > 0) {
                n = 1;
                if (!classArray2[0].isAssignableFrom(LifecycleOwner.class)) {
                    throw new IllegalArgumentException("invalid parameter type. Must be one and instanceof LifecycleOwner");
                }
            }
            Lifecycle.Event event = onLifecycleEvent.value();
            if (classArray2.length > 1) {
                n = 2;
                if (classArray2[1].isAssignableFrom(Lifecycle.Event.class)) {
                    if (event != Lifecycle.Event.ON_ANY) {
                        throw new IllegalArgumentException("Second arg is supported only for ON_ANY value");
                    }
                } else {
                    throw new IllegalArgumentException("invalid parameter type. second arg must be an event");
                }
            }
            if (classArray2.length <= 2) {
                this.verifyAndPutHandler(hashMap, new MethodReference(n, (Method)var8_11), event, clazz);
                continue;
            }
            throw new IllegalArgumentException("cannot have more than 2 params");
        }
        CallbackInfo callbackInfo2 = new CallbackInfo(hashMap);
        this.mCallbackMap.put(clazz, callbackInfo2);
        this.mHasLifecycleMethods.put(clazz, bl);
        return callbackInfo2;
    }

    private Method[] getDeclaredMethods(Class<?> methodArray) {
        try {
            methodArray = methodArray.getDeclaredMethods();
            return methodArray;
        }
        catch (NoClassDefFoundError noClassDefFoundError) {
            throw new IllegalArgumentException("The observer class has some methods that use newer APIs which are not available in the current OS version. Lifecycles cannot access even other methods so you should make sure that your observer classes only access framework classes that are available in your min API level OR use lifecycle:compiler annotation processor.", noClassDefFoundError);
        }
    }

    private void verifyAndPutHandler(Map<MethodReference, Lifecycle.Event> object, MethodReference object2, Lifecycle.Event event, Class<?> clazz) {
        Lifecycle.Event event2 = object.get(object2);
        if (event2 != null && event != event2) {
            object = ((MethodReference)object2).mMethod;
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("Method ");
            ((StringBuilder)object2).append(((Method)object).getName());
            ((StringBuilder)object2).append(" in ");
            ((StringBuilder)object2).append(clazz.getName());
            ((StringBuilder)object2).append(" already declared with different @OnLifecycleEvent value: previous value ");
            ((StringBuilder)object2).append((Object)event2);
            ((StringBuilder)object2).append(", new value ");
            ((StringBuilder)object2).append((Object)event);
            throw new IllegalArgumentException(((StringBuilder)object2).toString());
        }
        if (event2 == null) {
            object.put((MethodReference)object2, (Lifecycle.Event)event);
        }
    }

    CallbackInfo getInfo(Class<?> clazz) {
        CallbackInfo callbackInfo = this.mCallbackMap.get(clazz);
        if (callbackInfo != null) {
            return callbackInfo;
        }
        return this.createInfo(clazz, null);
    }

    boolean hasLifecycleMethods(Class<?> clazz) {
        Method[] methodArray = this.mHasLifecycleMethods.get(clazz);
        if (methodArray != null) {
            return methodArray.booleanValue();
        }
        methodArray = this.getDeclaredMethods(clazz);
        int n = methodArray.length;
        for (int i = 0; i < n; ++i) {
            if (methodArray[i].getAnnotation(OnLifecycleEvent.class) == null) continue;
            this.createInfo(clazz, methodArray);
            return true;
        }
        this.mHasLifecycleMethods.put(clazz, false);
        return false;
    }

    static class CallbackInfo {
        final Map<Lifecycle.Event, List<MethodReference>> mEventToHandlers;
        final Map<MethodReference, Lifecycle.Event> mHandlerToEvent;

        CallbackInfo(Map<MethodReference, Lifecycle.Event> arrayList) {
            this.mHandlerToEvent = arrayList;
            this.mEventToHandlers = new HashMap<Lifecycle.Event, List<MethodReference>>();
            for (Map.Entry<MethodReference, Lifecycle.Event> entry : arrayList.entrySet()) {
                Lifecycle.Event event = entry.getValue();
                List<MethodReference> list = this.mEventToHandlers.get((Object)event);
                arrayList = list;
                if (list == null) {
                    arrayList = new ArrayList<MethodReference>();
                    this.mEventToHandlers.put(event, arrayList);
                }
                arrayList.add(entry.getKey());
            }
        }

        private static void invokeMethodsForEvent(List<MethodReference> list, LifecycleOwner lifecycleOwner, Lifecycle.Event event, Object object) {
            if (list != null) {
                for (int i = list.size() - 1; i >= 0; --i) {
                    list.get(i).invokeCallback(lifecycleOwner, event, object);
                }
            }
        }

        void invokeCallbacks(LifecycleOwner lifecycleOwner, Lifecycle.Event event, Object object) {
            CallbackInfo.invokeMethodsForEvent(this.mEventToHandlers.get((Object)event), lifecycleOwner, event, object);
            CallbackInfo.invokeMethodsForEvent(this.mEventToHandlers.get((Object)Lifecycle.Event.ON_ANY), lifecycleOwner, event, object);
        }
    }

    static final class MethodReference {
        final int mCallType;
        final Method mMethod;

        MethodReference(int n, Method method) {
            this.mCallType = n;
            this.mMethod = method;
            method.setAccessible(true);
        }

        public boolean equals(Object object) {
            boolean bl = true;
            if (this == object) {
                return true;
            }
            if (!(object instanceof MethodReference)) {
                return false;
            }
            object = (MethodReference)object;
            if (this.mCallType != ((MethodReference)object).mCallType || !this.mMethod.getName().equals(((MethodReference)object).mMethod.getName())) {
                bl = false;
            }
            return bl;
        }

        public int hashCode() {
            return this.mCallType * 31 + this.mMethod.getName().hashCode();
        }

        void invokeCallback(LifecycleOwner lifecycleOwner, Lifecycle.Event event, Object object) {
            try {
                switch (this.mCallType) {
                    default: {
                        break;
                    }
                    case 2: {
                        this.mMethod.invoke(object, new Object[]{lifecycleOwner, event});
                        break;
                    }
                    case 1: {
                        this.mMethod.invoke(object, lifecycleOwner);
                        break;
                    }
                    case 0: {
                        this.mMethod.invoke(object, new Object[0]);
                    }
                }
                return;
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new RuntimeException(illegalAccessException);
            }
            catch (InvocationTargetException invocationTargetException) {
                throw new RuntimeException("Failed to call observer method", invocationTargetException.getCause());
            }
        }
    }
}

