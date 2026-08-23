/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.ServiceConnection
 *  android.os.IBinder
 */
package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.common.internal.IGmsServiceBroker;
import com.google.android.gms.common.internal.zzac;

public final class zze
implements ServiceConnection {
    final BaseGmsClient zza;
    private final int zzb;

    public zze(BaseGmsClient baseGmsClient, int n) {
        this.zza = baseGmsClient;
        this.zzb = n;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void onServiceConnected(ComponentName object, IBinder iBinder) {
        object = this.zza;
        if (iBinder == null) {
            BaseGmsClient.zzk((BaseGmsClient)object, 16);
            return;
        }
        Object object2 = BaseGmsClient.zzd((BaseGmsClient)object);
        synchronized (object2) {
            BaseGmsClient baseGmsClient = this.zza;
            object = iBinder.queryLocalInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
            object = object != null && object instanceof IGmsServiceBroker ? (IGmsServiceBroker)object : new zzac(iBinder);
            BaseGmsClient.zzh(baseGmsClient, (IGmsServiceBroker)object);
        }
        this.zza.zzl(0, null, this.zzb);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void onServiceDisconnected(ComponentName object) {
        object = BaseGmsClient.zzd(this.zza);
        synchronized (object) {
            BaseGmsClient.zzh(this.zza, null);
        }
        object = this.zza.zzb;
        object.sendMessage(object.obtainMessage(6, this.zzb, 1));
    }
}

