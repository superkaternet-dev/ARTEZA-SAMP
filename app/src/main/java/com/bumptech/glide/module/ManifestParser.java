/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.util.Log
 */
package com.bumptech.glide.module;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.bumptech.glide.module.GlideModule;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public final class ManifestParser {
    private static final String GLIDE_MODULE_VALUE = "GlideModule";
    private static final String TAG = "ManifestParser";
    private final Context context;

    public ManifestParser(Context context) {
        this.context = context;
    }

    private static GlideModule parseModule(String string2) {
        StringBuilder stringBuilder;
        Class<?> clazz;
        try {
            clazz = Class.forName(string2);
            stringBuilder = null;
            string2 = null;
        }
        catch (ClassNotFoundException classNotFoundException) {
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Unable to find GlideModule implementation", classNotFoundException);
            throw illegalArgumentException;
        }
        try {
            Object obj = clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            string2 = obj;
        }
        catch (InvocationTargetException invocationTargetException) {
            ManifestParser.throwInstantiateGlideModuleException(clazz, invocationTargetException);
            string2 = stringBuilder;
        }
        catch (NoSuchMethodException noSuchMethodException) {
            ManifestParser.throwInstantiateGlideModuleException(clazz, noSuchMethodException);
        }
        catch (IllegalAccessException illegalAccessException) {
            ManifestParser.throwInstantiateGlideModuleException(clazz, illegalAccessException);
        }
        catch (InstantiationException instantiationException) {
            ManifestParser.throwInstantiateGlideModuleException(clazz, instantiationException);
        }
        if (string2 instanceof GlideModule) {
            return (GlideModule)((Object)string2);
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("Expected instanceof GlideModule, but found: ");
        stringBuilder.append((Object)string2);
        throw new RuntimeException(stringBuilder.toString());
    }

    private static void throwInstantiateGlideModuleException(Class<?> clazz, Exception exception) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to instantiate GlideModule implementation for ");
        stringBuilder.append(clazz);
        throw new RuntimeException(stringBuilder.toString(), exception);
    }

    public List<GlideModule> parse() {
        ApplicationInfo applicationInfo;
        ArrayList<GlideModule> arrayList;
        block8: {
            if (Log.isLoggable((String)TAG, (int)3)) {
                Log.d((String)TAG, (String)"Loading Glide modules");
            }
            arrayList = new ArrayList<GlideModule>();
            applicationInfo = this.context.getPackageManager().getApplicationInfo(this.context.getPackageName(), 128);
            if (applicationInfo.metaData != null) break block8;
            if (Log.isLoggable((String)TAG, (int)3)) {
                Log.d((String)TAG, (String)"Got null app info metadata");
            }
            return arrayList;
        }
        try {
            StringBuilder stringBuilder;
            if (Log.isLoggable((String)TAG, (int)2)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Got app info metadata: ");
                stringBuilder.append(applicationInfo.metaData);
                Log.v((String)TAG, (String)stringBuilder.toString());
            }
            for (String string2 : applicationInfo.metaData.keySet()) {
                if (!GLIDE_MODULE_VALUE.equals(applicationInfo.metaData.get(string2))) continue;
                arrayList.add(ManifestParser.parseModule(string2));
                if (!Log.isLoggable((String)TAG, (int)3)) continue;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Loaded Glide module: ");
                stringBuilder.append(string2);
                Log.d((String)TAG, (String)stringBuilder.toString());
            }
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            RuntimeException runtimeException = new RuntimeException("Unable to find metadata to parse GlideModules", nameNotFoundException);
            throw runtimeException;
        }
        if (Log.isLoggable((String)TAG, (int)3)) {
            Log.d((String)TAG, (String)"Finished loading Glide modules");
        }
        return arrayList;
    }
}

