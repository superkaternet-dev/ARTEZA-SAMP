/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.AppComponentFactory
 *  android.app.Application
 *  android.app.Service
 *  android.content.BroadcastReceiver
 *  android.content.ContentProvider
 *  android.content.Intent
 */
package androidx.core.app;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Intent;
import androidx.core.app.CoreComponentFactory;
import java.lang.reflect.InvocationTargetException;

public class AppComponentFactory
extends android.app.AppComponentFactory {
    public final Activity instantiateActivity(ClassLoader classLoader, String string2, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateActivityCompat(classLoader, string2, intent));
    }

    /*
     * WARNING - void declaration
     */
    public Activity instantiateActivityCompat(ClassLoader classLoader, String string2, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        void var1_4;
        try {
            classLoader = Class.forName(string2, false, classLoader).asSubclass(Activity.class).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            return classLoader;
        }
        catch (NoSuchMethodException noSuchMethodException) {
        }
        catch (InvocationTargetException invocationTargetException) {
            // empty catch block
        }
        throw new RuntimeException("Couldn't call constructor", (Throwable)var1_4);
    }

    public final Application instantiateApplication(ClassLoader classLoader, String string2) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateApplicationCompat(classLoader, string2));
    }

    /*
     * WARNING - void declaration
     */
    public Application instantiateApplicationCompat(ClassLoader classLoader, String string2) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        void var1_4;
        try {
            classLoader = Class.forName(string2, false, classLoader).asSubclass(Application.class).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            return classLoader;
        }
        catch (NoSuchMethodException noSuchMethodException) {
        }
        catch (InvocationTargetException invocationTargetException) {
            // empty catch block
        }
        throw new RuntimeException("Couldn't call constructor", (Throwable)var1_4);
    }

    public final ContentProvider instantiateProvider(ClassLoader classLoader, String string2) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateProviderCompat(classLoader, string2));
    }

    /*
     * WARNING - void declaration
     */
    public ContentProvider instantiateProviderCompat(ClassLoader classLoader, String string2) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        void var1_4;
        try {
            classLoader = Class.forName(string2, false, classLoader).asSubclass(ContentProvider.class).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            return classLoader;
        }
        catch (NoSuchMethodException noSuchMethodException) {
        }
        catch (InvocationTargetException invocationTargetException) {
            // empty catch block
        }
        throw new RuntimeException("Couldn't call constructor", (Throwable)var1_4);
    }

    public final BroadcastReceiver instantiateReceiver(ClassLoader classLoader, String string2, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateReceiverCompat(classLoader, string2, intent));
    }

    /*
     * WARNING - void declaration
     */
    public BroadcastReceiver instantiateReceiverCompat(ClassLoader classLoader, String string2, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        void var1_4;
        try {
            classLoader = Class.forName(string2, false, classLoader).asSubclass(BroadcastReceiver.class).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            return classLoader;
        }
        catch (NoSuchMethodException noSuchMethodException) {
        }
        catch (InvocationTargetException invocationTargetException) {
            // empty catch block
        }
        throw new RuntimeException("Couldn't call constructor", (Throwable)var1_4);
    }

    public final Service instantiateService(ClassLoader classLoader, String string2, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateServiceCompat(classLoader, string2, intent));
    }

    /*
     * WARNING - void declaration
     */
    public Service instantiateServiceCompat(ClassLoader classLoader, String string2, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        void var1_4;
        try {
            classLoader = Class.forName(string2, false, classLoader).asSubclass(Service.class).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            return classLoader;
        }
        catch (NoSuchMethodException noSuchMethodException) {
        }
        catch (InvocationTargetException invocationTargetException) {
            // empty catch block
        }
        throw new RuntimeException("Couldn't call constructor", (Throwable)var1_4);
    }
}

