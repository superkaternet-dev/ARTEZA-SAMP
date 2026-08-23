/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.util.Log
 */
package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import com.google.android.gms.common.wrappers.PackageManagerWrapper;
import com.google.android.gms.common.wrappers.Wrappers;

public final class zzag {
    private static final Object zza = new Object();
    private static boolean zzb;
    private static String zzc;
    private static int zzd;

    public static int zza(Context context) {
        zzag.zzc(context);
        return zzd;
    }

    public static String zzb(Context context) {
        zzag.zzc(context);
        return zzc;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static void zzc(Context object) {
        Object object2 = zza;
        synchronized (object2) {
            block6: {
                if (zzb) {
                    return;
                }
                zzb = true;
                String string2 = object.getPackageName();
                object = Wrappers.packageManager((Context)object);
                object = ((PackageManagerWrapper)object).getApplicationInfo((String)string2, (int)128).metaData;
                if (object != null) break block6;
                return;
            }
            try {
                zzc = object.getString("com.google.app.id");
                zzd = object.getInt("com.google.android.gms.version");
            }
            catch (PackageManager.NameNotFoundException nameNotFoundException) {
                Log.wtf((String)"MetadataValueReader", (String)"This should never happen.", (Throwable)nameNotFoundException);
            }
            return;
        }
    }
}

