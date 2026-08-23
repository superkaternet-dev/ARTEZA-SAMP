/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.ServiceConnection
 *  android.os.HandlerThread
 *  android.os.Looper
 */
package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.HandlerThread;
import android.os.Looper;
import com.google.android.gms.common.internal.zzn;
import com.google.android.gms.common.internal.zzr;
import java.util.concurrent.Executor;

public abstract class GmsClientSupervisor {
    static HandlerThread zza;
    private static int zzb;
    private static final Object zzc;
    private static zzr zzd;
    private static boolean zze;

    static {
        zzb = 4225;
        zzc = new Object();
        zze = false;
    }

    public static int getDefaultBindFlags() {
        return zzb;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static GmsClientSupervisor getInstance(Context context) {
        Object object = zzc;
        synchronized (object) {
            if (zzd == null) {
                zzr zzr2;
                Context context2 = context.getApplicationContext();
                context = zze ? GmsClientSupervisor.getOrStartHandlerThread().getLooper() : context.getMainLooper();
                zzd = zzr2 = new zzr(context2, (Looper)context);
            }
            return zzd;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static HandlerThread getOrStartHandlerThread() {
        Object object = zzc;
        synchronized (object) {
            HandlerThread handlerThread = zza;
            if (handlerThread != null) {
                return handlerThread;
            }
            zza = handlerThread = new HandlerThread("GoogleApiHandler", 9);
            handlerThread.start();
            return zza;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void setUseHandlerThreadForCallbacks() {
        Object object = zzc;
        synchronized (object) {
            zzr zzr2 = zzd;
            if (zzr2 != null && !zze) {
                zzr2.zzi(GmsClientSupervisor.getOrStartHandlerThread().getLooper());
            }
            zze = true;
            return;
        }
    }

    public boolean bindService(ComponentName componentName, ServiceConnection serviceConnection, String string2) {
        return this.zzc(new zzn(componentName, GmsClientSupervisor.getDefaultBindFlags()), serviceConnection, string2, null);
    }

    public boolean bindService(String string2, ServiceConnection serviceConnection, String string3) {
        return this.zzc(new zzn(string2, GmsClientSupervisor.getDefaultBindFlags(), false), serviceConnection, string3, null);
    }

    public void unbindService(ComponentName componentName, ServiceConnection serviceConnection, String string2) {
        this.zza(new zzn(componentName, GmsClientSupervisor.getDefaultBindFlags()), serviceConnection, string2);
    }

    public void unbindService(String string2, ServiceConnection serviceConnection, String string3) {
        this.zza(new zzn(string2, GmsClientSupervisor.getDefaultBindFlags(), false), serviceConnection, string3);
    }

    protected abstract void zza(zzn var1, ServiceConnection var2, String var3);

    public final void zzb(String string2, String string3, int n, ServiceConnection serviceConnection, String string4, boolean bl) {
        this.zza(new zzn(string2, string3, n, bl), serviceConnection, string4);
    }

    protected abstract boolean zzc(zzn var1, ServiceConnection var2, String var3, Executor var4);
}

