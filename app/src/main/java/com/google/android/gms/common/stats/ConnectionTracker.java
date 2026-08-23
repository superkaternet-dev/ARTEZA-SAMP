/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.ServiceConnection
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.util.Log
 *  javax.annotation.Nullable
 */
package com.google.android.gms.common.stats;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.zzs;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.android.gms.common.wrappers.Wrappers;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

public class ConnectionTracker {
    private static final Object zzb = new Object();
    @Nullable
    private static volatile ConnectionTracker zzc;
    public ConcurrentHashMap<ServiceConnection, ServiceConnection> zza = new ConcurrentHashMap();

    private ConnectionTracker() {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static ConnectionTracker getInstance() {
        Object object;
        if (zzc == null) {
            object = zzb;
            synchronized (object) {
                if (zzc == null) {
                    ConnectionTracker connectionTracker;
                    zzc = connectionTracker = new ConnectionTracker();
                }
            }
        }
        object = zzc;
        Preconditions.checkNotNull(object);
        return object;
    }

    private static void zzb(Context context, ServiceConnection serviceConnection) {
        try {
            context.unbindService(serviceConnection);
            return;
        }
        catch (NoSuchElementException noSuchElementException) {
        }
        catch (IllegalStateException illegalStateException) {
        }
        catch (IllegalArgumentException illegalArgumentException) {
            // empty catch block
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final boolean zzc(Context context, String string2, Intent intent, ServiceConnection serviceConnection, int n, boolean bl, @Nullable Executor executor) {
        block8: {
            ComponentName componentName = intent.getComponent();
            if (componentName != null) {
                String string3 = componentName.getPackageName();
                "com.google.android.gms".equals(string3);
                try {
                    int n2 = Wrappers.packageManager((Context)context).getApplicationInfo((String)string3, (int)0).flags;
                    if ((n2 & 0x200000) == 0) break block8;
                }
                catch (PackageManager.NameNotFoundException nameNotFoundException) {
                    // empty catch block
                    break block8;
                }
                Log.w((String)"ConnectionTracker", (String)"Attempted to bind to a service in a STOPPED package.");
                return false;
            }
        }
        if (!ConnectionTracker.zzd(serviceConnection)) {
            return ConnectionTracker.zze(context, intent, serviceConnection, n, executor);
        }
        ServiceConnection serviceConnection2 = this.zza.putIfAbsent(serviceConnection, serviceConnection);
        if (serviceConnection2 != null && serviceConnection != serviceConnection2) {
            Log.w((String)"ConnectionTracker", (String)String.format("Duplicate binding with the same ServiceConnection: %s, %s, %s.", serviceConnection, string2, intent.getAction()));
        }
        try {
            bl = ConnectionTracker.zze(context, intent, serviceConnection, n, executor);
            if (bl) {
                return bl;
            }
            this.zza.remove(serviceConnection, serviceConnection);
            return false;
        }
        catch (Throwable throwable) {
            this.zza.remove(serviceConnection, serviceConnection);
            throw throwable;
        }
    }

    private static boolean zzd(ServiceConnection serviceConnection) {
        return !(serviceConnection instanceof zzs);
    }

    private static final boolean zze(Context context, Intent intent, ServiceConnection serviceConnection, int n, @Nullable Executor executor) {
        if (PlatformVersion.isAtLeastQ() && executor != null) {
            return context.bindService(intent, n, executor, serviceConnection);
        }
        return context.bindService(intent, serviceConnection, n);
    }

    public boolean bindService(Context context, Intent intent, ServiceConnection serviceConnection, int n) {
        return this.zzc(context, context.getClass().getName(), intent, serviceConnection, n, true, null);
    }

    public void unbindService(Context context, ServiceConnection serviceConnection) {
        if (ConnectionTracker.zzd(serviceConnection) && this.zza.containsKey(serviceConnection)) {
            try {
                ConnectionTracker.zzb(context, this.zza.get(serviceConnection));
                return;
            }
            finally {
                this.zza.remove(serviceConnection);
            }
        }
        ConnectionTracker.zzb(context, serviceConnection);
    }

    public void unbindServiceSafe(Context context, ServiceConnection serviceConnection) {
        try {
            this.unbindService(context, serviceConnection);
            return;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            return;
        }
    }

    public final boolean zza(Context context, String string2, Intent intent, ServiceConnection serviceConnection, int n, @Nullable Executor executor) {
        return this.zzc(context, string2, intent, serviceConnection, n, true, executor);
    }
}

