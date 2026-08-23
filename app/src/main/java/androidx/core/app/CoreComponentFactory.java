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
import android.app.AppComponentFactory;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Intent;

public class CoreComponentFactory
extends AppComponentFactory {
    static <T> T checkCompatWrapper(T t) {
        Object object;
        if (t instanceof CompatWrapped && (object = ((CompatWrapped)t).getWrapper()) != null) {
            return (T)object;
        }
        return t;
    }

    public Activity instantiateActivity(ClassLoader classLoader, String string2, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(super.instantiateActivity(classLoader, string2, intent));
    }

    public Application instantiateApplication(ClassLoader classLoader, String string2) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(super.instantiateApplication(classLoader, string2));
    }

    public ContentProvider instantiateProvider(ClassLoader classLoader, String string2) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(super.instantiateProvider(classLoader, string2));
    }

    public BroadcastReceiver instantiateReceiver(ClassLoader classLoader, String string2, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(super.instantiateReceiver(classLoader, string2, intent));
    }

    public Service instantiateService(ClassLoader classLoader, String string2, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(super.instantiateService(classLoader, string2, intent));
    }

    public static interface CompatWrapped {
        public Object getWrapper();
    }
}

