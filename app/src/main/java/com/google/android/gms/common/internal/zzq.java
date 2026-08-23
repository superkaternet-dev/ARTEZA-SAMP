/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.os.Handler$Callback
 *  android.os.Message
 *  android.util.Log
 */
package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.zzn;
import com.google.android.gms.common.internal.zzo;
import com.google.android.gms.common.internal.zzp;
import com.google.android.gms.common.internal.zzr;
import java.util.HashMap;

final class zzq
implements Handler.Callback {
    final zzr zza;

    /* synthetic */ zzq(zzr zzr2, zzp zzp2) {
        this.zza = zzr2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final boolean handleMessage(Message object) {
        switch (((Message)object).what) {
            default: {
                return false;
            }
            case 1: {
                HashMap hashMap = zzr.zzh(this.zza);
                synchronized (hashMap) {
                    zzn zzn2 = (zzn)((Message)object).obj;
                    zzo zzo2 = (zzo)zzr.zzh(this.zza).get(zzn2);
                    if (zzo2 != null && zzo2.zza() == 3) {
                        Object object2 = String.valueOf(zzn2);
                        int n = String.valueOf(object2).length();
                        object = new StringBuilder(n + 47);
                        ((StringBuilder)object).append("Timeout waiting for ServiceConnection callback ");
                        ((StringBuilder)object).append((String)object2);
                        object2 = new Exception();
                        Log.e((String)"GmsClientSupervisor", (String)((StringBuilder)object).toString(), (Throwable)object2);
                        object = object2 = zzo2.zzb();
                        if (object2 == null) {
                            object = zzn2.zzb();
                        }
                        object2 = object;
                        if (object == null) {
                            object = zzn2.zzd();
                            Preconditions.checkNotNull(object);
                            object2 = new ComponentName((String)object, "unknown");
                        }
                        zzo2.onServiceDisconnected((ComponentName)object2);
                    }
                    return true;
                }
            }
            case 0: 
        }
        HashMap hashMap = zzr.zzh(this.zza);
        synchronized (hashMap) {
            zzn zzn3 = (zzn)((Message)object).obj;
            object = (zzo)zzr.zzh(this.zza).get(zzn3);
            if (object != null && ((zzo)object).zzi()) {
                if (((zzo)object).zzj()) {
                    ((zzo)object).zzg("GmsClientSupervisor");
                }
                zzr.zzh(this.zza).remove(zzn3);
            }
            return true;
        }
    }
}

