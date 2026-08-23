/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.google.android.gms.common.wrappers;

import android.content.Context;
import com.google.android.gms.common.util.PlatformVersion;

public class InstantApps {
    private static Context zza;
    private static Boolean zzb;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static boolean isInstantApp(Context context) {
        synchronized (InstantApps.class) {
            Boolean bl;
            Context context2 = context.getApplicationContext();
            Context context3 = zza;
            if (context3 != null && (bl = zzb) != null && context3 == context2) {
                return bl;
            }
            zzb = null;
            if (PlatformVersion.isAtLeastO()) {
                zzb = context2.getPackageManager().isInstantApp();
            } else {
                try {
                    context.getClassLoader().loadClass("com.google.android.instantapps.supervisor.InstantAppsRuntime");
                    zzb = true;
                }
                catch (ClassNotFoundException classNotFoundException) {
                    zzb = false;
                }
            }
            zza = context2;
            return zzb;
        }
    }
}

