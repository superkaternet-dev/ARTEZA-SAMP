/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.google.android.gms.common.wrappers;

import android.content.Context;
import com.google.android.gms.common.wrappers.PackageManagerWrapper;

public class Wrappers {
    private static Wrappers zza = new Wrappers();
    private PackageManagerWrapper zzb = null;

    public static PackageManagerWrapper packageManager(Context context) {
        return zza.zza(context);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final PackageManagerWrapper zza(Context object) {
        synchronized (this) {
            PackageManagerWrapper packageManagerWrapper;
            if (this.zzb != null) return this.zzb;
            Object object2 = object;
            if (object.getApplicationContext() != null) {
                object2 = object.getApplicationContext();
            }
            this.zzb = packageManagerWrapper = new PackageManagerWrapper((Context)object2);
            return this.zzb;
        }
    }
}

