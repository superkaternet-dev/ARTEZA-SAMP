/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Service
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.content.pm.ServiceInfo
 *  android.os.Bundle
 *  android.util.Log
 */
package com.google.firebase.components;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.util.Log;
import com.google.firebase.components.ComponentDiscovery$$ExternalSyntheticLambda0;
import com.google.firebase.components.ComponentRegistrar;
import com.google.firebase.components.InvalidRegistrarException;
import com.google.firebase.inject.Provider;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class ComponentDiscovery<T> {
    private static final String COMPONENT_KEY_PREFIX = "com.google.firebase.components:";
    private static final String COMPONENT_SENTINEL_VALUE = "com.google.firebase.components.ComponentRegistrar";
    static final String TAG = "ComponentDiscovery";
    private final T context;
    private final RegistrarNameRetriever<T> retriever;

    ComponentDiscovery(T t, RegistrarNameRetriever<T> registrarNameRetriever) {
        this.context = t;
        this.retriever = registrarNameRetriever;
    }

    public static ComponentDiscovery<Context> forContext(Context context, Class<? extends Service> clazz) {
        return new ComponentDiscovery<Context>(context, new MetadataRegistrarNameRetriever(clazz));
    }

    private static ComponentRegistrar instantiate(String string2) {
        try {
            Serializable serializable = Class.forName(string2);
            if (ComponentRegistrar.class.isAssignableFrom((Class<?>)serializable)) {
                return (ComponentRegistrar)serializable.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            }
            serializable = new Serializable(String.format("Class %s is not an instance of %s", string2, COMPONENT_SENTINEL_VALUE));
            throw serializable;
        }
        catch (InvocationTargetException invocationTargetException) {
            throw new InvalidRegistrarException(String.format("Could not instantiate %s", string2), invocationTargetException);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            throw new InvalidRegistrarException(String.format("Could not instantiate %s", string2), noSuchMethodException);
        }
        catch (InstantiationException instantiationException) {
            throw new InvalidRegistrarException(String.format("Could not instantiate %s.", string2), instantiationException);
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new InvalidRegistrarException(String.format("Could not instantiate %s.", string2), illegalAccessException);
        }
        catch (ClassNotFoundException classNotFoundException) {
            Log.w((String)TAG, (String)String.format("Class %s is not an found.", string2));
            return null;
        }
    }

    static /* synthetic */ ComponentRegistrar lambda$discoverLazy$0(String string2) {
        return ComponentDiscovery.instantiate(string2);
    }

    @Deprecated
    public List<ComponentRegistrar> discover() {
        ArrayList<ComponentRegistrar> arrayList = new ArrayList<ComponentRegistrar>();
        for (String invalidRegistrarException : this.retriever.retrieve(this.context)) {
            ComponentRegistrar componentRegistrar = ComponentDiscovery.instantiate(invalidRegistrarException);
            if (componentRegistrar == null) continue;
            try {
                arrayList.add(componentRegistrar);
            }
            catch (InvalidRegistrarException invalidRegistrarException2) {
                Log.w((String)TAG, (String)"Invalid component registrar.", (Throwable)invalidRegistrarException2);
            }
        }
        return arrayList;
    }

    public List<Provider<ComponentRegistrar>> discoverLazy() {
        ArrayList<Provider<ComponentRegistrar>> arrayList = new ArrayList<Provider<ComponentRegistrar>>();
        Iterator<String> iterator2 = this.retriever.retrieve(this.context).iterator();
        while (iterator2.hasNext()) {
            arrayList.add(new ComponentDiscovery$$ExternalSyntheticLambda0(iterator2.next()));
        }
        return arrayList;
    }

    private static class MetadataRegistrarNameRetriever
    implements RegistrarNameRetriever<Context> {
        private final Class<? extends Service> discoveryService;

        private MetadataRegistrarNameRetriever(Class<? extends Service> clazz) {
            this.discoveryService = clazz;
        }

        private Bundle getMetadata(Context object) {
            block7: {
                PackageManager packageManager;
                block6: {
                    try {
                        packageManager = object.getPackageManager();
                        if (packageManager != null) break block6;
                    }
                    catch (PackageManager.NameNotFoundException nameNotFoundException) {
                        Log.w((String)ComponentDiscovery.TAG, (String)"Application info not found.");
                        return null;
                    }
                    Log.w((String)ComponentDiscovery.TAG, (String)"Context has no PackageManager.");
                    return null;
                }
                ComponentName componentName = new ComponentName((Context)object, this.discoveryService);
                object = packageManager.getServiceInfo(componentName, 128);
                if (object != null) break block7;
                object = new StringBuilder();
                ((StringBuilder)object).append(this.discoveryService);
                ((StringBuilder)object).append(" has no service info.");
                Log.w((String)ComponentDiscovery.TAG, (String)((StringBuilder)object).toString());
                return null;
            }
            object = ((ServiceInfo)object).metaData;
            return object;
        }

        @Override
        public List<String> retrieve(Context context) {
            if ((context = this.getMetadata(context)) == null) {
                Log.w((String)ComponentDiscovery.TAG, (String)"Could not retrieve metadata, returning empty list of registrars.");
                return Collections.emptyList();
            }
            ArrayList<String> arrayList = new ArrayList<String>();
            for (String string2 : context.keySet()) {
                if (!ComponentDiscovery.COMPONENT_SENTINEL_VALUE.equals(context.get(string2)) || !string2.startsWith(ComponentDiscovery.COMPONENT_KEY_PREFIX)) continue;
                arrayList.add(string2.substring(ComponentDiscovery.COMPONENT_KEY_PREFIX.length()));
            }
            return arrayList;
        }
    }

    static interface RegistrarNameRetriever<T> {
        public List<String> retrieve(T var1);
    }
}

