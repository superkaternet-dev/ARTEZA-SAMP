/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.util.Log
 */
package com.bumptech.glide.signature;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.signature.ObjectKey;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class ApplicationVersionSignature {
    private static final ConcurrentMap<String, Key> PACKAGE_NAME_TO_KEY = new ConcurrentHashMap<String, Key>();
    private static final String TAG = "AppVersionSignature";

    private ApplicationVersionSignature() {
    }

    private static PackageInfo getPackageInfo(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo;
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Cannot resolve info for");
            stringBuilder.append(context.getPackageName());
            Log.e((String)TAG, (String)stringBuilder.toString(), (Throwable)nameNotFoundException);
            return null;
        }
    }

    private static String getVersionCode(PackageInfo object) {
        object = object != null ? String.valueOf(object.versionCode) : UUID.randomUUID().toString();
        return object;
    }

    public static Key obtain(Context object) {
        Key key;
        String string2 = object.getPackageName();
        ConcurrentMap<String, Key> concurrentMap = PACKAGE_NAME_TO_KEY;
        Key key2 = key = (Key)concurrentMap.get(string2);
        if (key == null) {
            object = ApplicationVersionSignature.obtainVersionSignature(object);
            key2 = key = concurrentMap.putIfAbsent(string2, (Key)object);
            if (key == null) {
                key2 = object;
            }
        }
        return key2;
    }

    private static Key obtainVersionSignature(Context context) {
        return new ObjectKey(ApplicationVersionSignature.getVersionCode(ApplicationVersionSignature.getPackageInfo(context)));
    }

    static void reset() {
        PACKAGE_NAME_TO_KEY.clear();
    }
}

