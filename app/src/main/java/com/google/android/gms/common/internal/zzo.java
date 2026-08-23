/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.ServiceConnection
 *  android.os.IBinder
 *  android.os.Message
 */
package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import com.google.android.gms.common.internal.zzn;
import com.google.android.gms.common.internal.zzr;
import com.google.android.gms.common.internal.zzs;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executor;

final class zzo
implements ServiceConnection,
zzs {
    final zzr zza;
    private final Map<ServiceConnection, ServiceConnection> zzb;
    private int zzc;
    private boolean zzd;
    private IBinder zze;
    private final zzn zzf;
    private ComponentName zzg;

    public zzo(zzr zzr2, zzn zzn2) {
        this.zza = zzr2;
        this.zzf = zzn2;
        this.zzb = new HashMap<ServiceConnection, ServiceConnection>();
        this.zzc = 2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        HashMap hashMap = zzr.zzh(this.zza);
        synchronized (hashMap) {
            zzr.zzf(this.zza).removeMessages(1, (Object)this.zzf);
            this.zze = iBinder;
            this.zzg = componentName;
            Iterator<ServiceConnection> iterator2 = this.zzb.values().iterator();
            while (true) {
                if (!iterator2.hasNext()) {
                    this.zzc = 1;
                    return;
                }
                iterator2.next().onServiceConnected(componentName, iBinder);
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void onServiceDisconnected(ComponentName componentName) {
        HashMap hashMap = zzr.zzh(this.zza);
        synchronized (hashMap) {
            zzr.zzf(this.zza).removeMessages(1, (Object)this.zzf);
            this.zze = null;
            this.zzg = componentName;
            Iterator<ServiceConnection> iterator2 = this.zzb.values().iterator();
            while (true) {
                if (!iterator2.hasNext()) {
                    this.zzc = 2;
                    return;
                }
                iterator2.next().onServiceDisconnected(componentName);
            }
        }
    }

    public final int zza() {
        return this.zzc;
    }

    public final ComponentName zzb() {
        return this.zzg;
    }

    public final IBinder zzc() {
        return this.zze;
    }

    public final void zzd(ServiceConnection serviceConnection, ServiceConnection serviceConnection2, String string2) {
        this.zzb.put(serviceConnection, serviceConnection2);
    }

    public final void zze(String object, Executor executor) {
        boolean bl;
        this.zzc = 3;
        zzr zzr2 = this.zza;
        this.zzd = bl = zzr.zzg(zzr2).zza(zzr.zze(zzr2), (String)object, this.zzf.zzc(zzr.zze(zzr2)), this, this.zzf.zza(), executor);
        if (bl) {
            object = zzr.zzf(this.zza).obtainMessage(1, (Object)this.zzf);
            zzr.zzf(this.zza).sendMessageDelayed((Message)object, zzr.zzd(this.zza));
            return;
        }
        this.zzc = 2;
        try {
            object = this.zza;
            zzr.zzg((zzr)object).unbindService(zzr.zze((zzr)object), this);
            return;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            return;
        }
    }

    public final void zzf(ServiceConnection serviceConnection, String string2) {
        this.zzb.remove(serviceConnection);
    }

    public final void zzg(String object) {
        zzr.zzf(this.zza).removeMessages(1, (Object)this.zzf);
        object = this.zza;
        zzr.zzg((zzr)object).unbindService(zzr.zze((zzr)object), this);
        this.zzd = false;
        this.zzc = 2;
    }

    public final boolean zzh(ServiceConnection serviceConnection) {
        return this.zzb.containsKey(serviceConnection);
    }

    public final boolean zzi() {
        return this.zzb.isEmpty();
    }

    public final boolean zzj() {
        return this.zzd;
    }
}

