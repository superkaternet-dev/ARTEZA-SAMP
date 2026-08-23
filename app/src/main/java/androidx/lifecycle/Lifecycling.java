/*
 * Decompiled with CFR 0.152.
 */
package androidx.lifecycle;

import androidx.lifecycle.ClassesInfoCache;
import androidx.lifecycle.CompositeGeneratedAdaptersObserver;
import androidx.lifecycle.FullLifecycleObserver;
import androidx.lifecycle.FullLifecycleObserverAdapter;
import androidx.lifecycle.GeneratedAdapter;
import androidx.lifecycle.GenericLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ReflectiveGenericLifecycleObserver;
import androidx.lifecycle.SingleGeneratedAdapterObserver;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lifecycling {
    private static final int GENERATED_CALLBACK = 2;
    private static final int REFLECTIVE_CALLBACK = 1;
    private static Map<Class<?>, Integer> sCallbackCache = new HashMap();
    private static Map<Class<?>, List<Constructor<? extends GeneratedAdapter>>> sClassToAdapters = new HashMap();

    private Lifecycling() {
    }

    private static GeneratedAdapter createGeneratedAdapter(Constructor<? extends GeneratedAdapter> object, Object object2) {
        try {
            object = ((Constructor)object).newInstance(object2);
            return object;
        }
        catch (InvocationTargetException invocationTargetException) {
            throw new RuntimeException(invocationTargetException);
        }
        catch (InstantiationException instantiationException) {
            throw new RuntimeException(instantiationException);
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new RuntimeException(illegalAccessException);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static Constructor<? extends GeneratedAdapter> generatedConstructor(Class<?> genericDeclaration) {
        try {
            Object object = ((Class)genericDeclaration).getPackage();
            String string2 = ((Class)genericDeclaration).getCanonicalName();
            object = object != null ? ((Package)object).getName() : "";
            if (!((String)object).isEmpty()) {
                string2 = string2.substring(((String)object).length() + 1);
            }
            string2 = Lifecycling.getAdapterName(string2);
            if (((String)object).isEmpty()) {
                object = string2;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append((String)object);
                stringBuilder.append(".");
                stringBuilder.append(string2);
                object = stringBuilder.toString();
            }
            genericDeclaration = Class.forName((String)object).getDeclaredConstructor(new Class[]{genericDeclaration});
            if (!((AccessibleObject)((Object)genericDeclaration)).isAccessible()) {
                ((Constructor)genericDeclaration).setAccessible(true);
            }
            return genericDeclaration;
        }
        catch (NoSuchMethodException noSuchMethodException) {
            throw new RuntimeException(noSuchMethodException);
        }
        catch (ClassNotFoundException classNotFoundException) {
            return null;
        }
    }

    public static String getAdapterName(String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2.replace(".", "_"));
        stringBuilder.append("_LifecycleAdapter");
        return stringBuilder.toString();
    }

    @Deprecated
    static GenericLifecycleObserver getCallback(Object object) {
        return new GenericLifecycleObserver(Lifecycling.lifecycleEventObserver(object)){
            final LifecycleEventObserver val$observer;
            {
                this.val$observer = lifecycleEventObserver;
            }

            @Override
            public void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
                this.val$observer.onStateChanged(lifecycleOwner, event);
            }
        };
    }

    private static int getObserverConstructorType(Class<?> clazz) {
        Integer n = sCallbackCache.get(clazz);
        if (n != null) {
            return n;
        }
        int n2 = Lifecycling.resolveObserverCallbackType(clazz);
        sCallbackCache.put(clazz, n2);
        return n2;
    }

    private static boolean isLifecycleParent(Class<?> clazz) {
        boolean bl = clazz != null && LifecycleObserver.class.isAssignableFrom(clazz);
        return bl;
    }

    static LifecycleEventObserver lifecycleEventObserver(Object object) {
        boolean bl = object instanceof LifecycleEventObserver;
        boolean bl2 = object instanceof FullLifecycleObserver;
        if (bl && bl2) {
            return new FullLifecycleObserverAdapter((FullLifecycleObserver)object, (LifecycleEventObserver)object);
        }
        if (bl2) {
            return new FullLifecycleObserverAdapter((FullLifecycleObserver)object, null);
        }
        if (bl) {
            return (LifecycleEventObserver)object;
        }
        Class<?> clazz = object.getClass();
        if (Lifecycling.getObserverConstructorType(clazz) == 2) {
            if ((clazz = sClassToAdapters.get(clazz)).size() == 1) {
                return new SingleGeneratedAdapterObserver(Lifecycling.createGeneratedAdapter((Constructor)clazz.get(0), object));
            }
            GeneratedAdapter[] generatedAdapterArray = new GeneratedAdapter[clazz.size()];
            for (int i = 0; i < clazz.size(); ++i) {
                generatedAdapterArray[i] = Lifecycling.createGeneratedAdapter((Constructor)clazz.get(i), object);
            }
            return new CompositeGeneratedAdaptersObserver(generatedAdapterArray);
        }
        return new ReflectiveGenericLifecycleObserver(object);
    }

    private static int resolveObserverCallbackType(Class<?> clazz) {
        if (clazz.getCanonicalName() == null) {
            return 1;
        }
        Object object = Lifecycling.generatedConstructor(clazz);
        if (object != null) {
            sClassToAdapters.put(clazz, Collections.singletonList(object));
            return 2;
        }
        if (ClassesInfoCache.sInstance.hasLifecycleMethods(clazz)) {
            return 1;
        }
        object = clazz.getSuperclass();
        Object object2 = null;
        if (Lifecycling.isLifecycleParent(object)) {
            if (Lifecycling.getObserverConstructorType(object) == 1) {
                return 1;
            }
            object2 = new ArrayList(sClassToAdapters.get(object));
        }
        for (Class<?> clazz2 : clazz.getInterfaces()) {
            if (!Lifecycling.isLifecycleParent(clazz2)) {
                object = object2;
            } else {
                if (Lifecycling.getObserverConstructorType(clazz2) == 1) {
                    return 1;
                }
                object = object2;
                if (object2 == null) {
                    object = new ArrayList();
                }
                object.addAll((Collection)sClassToAdapters.get(clazz2));
            }
            object2 = object;
        }
        if (object2 != null) {
            sClassToAdapters.put(clazz, (List<Constructor<GeneratedAdapter>>)object2);
            return 2;
        }
        return 1;
    }
}

