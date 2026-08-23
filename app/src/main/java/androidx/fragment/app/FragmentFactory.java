/*
 * Decompiled with CFR 0.152.
 */
package androidx.fragment.app;

import androidx.collection.SimpleArrayMap;
import androidx.fragment.app.Fragment;
import java.lang.reflect.InvocationTargetException;

public class FragmentFactory {
    private static final SimpleArrayMap<ClassLoader, SimpleArrayMap<String, Class<?>>> sClassCacheMap = new SimpleArrayMap();

    static boolean isFragmentClass(ClassLoader classLoader, String string2) {
        try {
            boolean bl = Fragment.class.isAssignableFrom(FragmentFactory.loadClass(classLoader, string2));
            return bl;
        }
        catch (ClassNotFoundException classNotFoundException) {
            return false;
        }
    }

    private static Class<?> loadClass(ClassLoader classLoader, String string2) throws ClassNotFoundException {
        Object object = sClassCacheMap;
        Class<?> clazz = ((SimpleArrayMap)object).get(classLoader);
        SimpleArrayMap<String, Class<Object>> simpleArrayMap = clazz;
        if (clazz == null) {
            simpleArrayMap = new SimpleArrayMap();
            ((SimpleArrayMap)object).put((ClassLoader)classLoader, simpleArrayMap);
        }
        object = simpleArrayMap.get(string2);
        clazz = object;
        if (object == null) {
            clazz = Class.forName(string2, false, classLoader);
            simpleArrayMap.put(string2, clazz);
        }
        return clazz;
    }

    public static Class<? extends Fragment> loadFragmentClass(ClassLoader object, String string2) {
        try {
            object = FragmentFactory.loadClass((ClassLoader)object, string2);
            return object;
        }
        catch (ClassCastException classCastException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to instantiate fragment ");
            stringBuilder.append(string2);
            stringBuilder.append(": make sure class is a valid subclass of Fragment");
            throw new Fragment.InstantiationException(stringBuilder.toString(), classCastException);
        }
        catch (ClassNotFoundException classNotFoundException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to instantiate fragment ");
            stringBuilder.append(string2);
            stringBuilder.append(": make sure class name exists");
            throw new Fragment.InstantiationException(stringBuilder.toString(), classNotFoundException);
        }
    }

    public Fragment instantiate(ClassLoader object, String string2) {
        try {
            object = FragmentFactory.loadFragmentClass((ClassLoader)object, string2).getConstructor(new Class[0]).newInstance(new Object[0]);
            return object;
        }
        catch (InvocationTargetException invocationTargetException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to instantiate fragment ");
            stringBuilder.append(string2);
            stringBuilder.append(": calling Fragment constructor caused an exception");
            throw new Fragment.InstantiationException(stringBuilder.toString(), invocationTargetException);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            object = new StringBuilder();
            ((StringBuilder)object).append("Unable to instantiate fragment ");
            ((StringBuilder)object).append(string2);
            ((StringBuilder)object).append(": could not find Fragment constructor");
            throw new Fragment.InstantiationException(((StringBuilder)object).toString(), noSuchMethodException);
        }
        catch (IllegalAccessException illegalAccessException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to instantiate fragment ");
            stringBuilder.append(string2);
            stringBuilder.append(": make sure class name exists, is public, and has an empty constructor that is public");
            throw new Fragment.InstantiationException(stringBuilder.toString(), illegalAccessException);
        }
        catch (InstantiationException instantiationException) {
            object = new StringBuilder();
            ((StringBuilder)object).append("Unable to instantiate fragment ");
            ((StringBuilder)object).append(string2);
            ((StringBuilder)object).append(": make sure class name exists, is public, and has an empty constructor that is public");
            throw new Fragment.InstantiationException(((StringBuilder)object).toString(), instantiationException);
        }
    }
}

