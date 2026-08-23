/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.PendingIntent
 *  android.os.Looper
 *  android.os.Message
 *  android.util.Log
 */
package com.google.android.gms.common.internal;

import android.app.PendingIntent;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.common.internal.zzc;
import com.google.android.gms.internal.common.zzi;

final class zzb
extends zzi {
    final BaseGmsClient zza;

    public zzb(BaseGmsClient baseGmsClient, Looper looper) {
        this.zza = baseGmsClient;
        super(looper);
    }

    private static final void zza(Message object) {
        object = (zzc)((Message)object).obj;
        ((zzc)object).zzc();
        ((zzc)object).zzg();
    }

    private static final boolean zzb(Message message) {
        return message.what == 2 || message.what == 1 || message.what == 7;
        {
        }
    }

    public final void handleMessage(Message object) {
        if (this.zza.zzd.get() != object.arg1) {
            if (zzb.zzb(object)) {
                zzb.zza(object);
            }
            return;
        }
        if (object.what != 1 && object.what != 7 && (object.what != 4 || this.zza.enableLocalFallback()) && object.what != 5 || this.zza.isConnecting()) {
            int n = object.what;
            Object object2 = null;
            if (n == 4) {
                BaseGmsClient.zzg(this.zza, new ConnectionResult(object.arg2));
                if (BaseGmsClient.zzo(this.zza) && !BaseGmsClient.zzm((BaseGmsClient)(object = this.zza))) {
                    BaseGmsClient.zzi((BaseGmsClient)object, 3, null);
                    return;
                }
                object = this.zza;
                object = BaseGmsClient.zza((BaseGmsClient)object) != null ? BaseGmsClient.zza((BaseGmsClient)object) : new ConnectionResult(8);
                this.zza.zzc.onReportServiceBinding((ConnectionResult)object);
                this.zza.onConnectionFailed((ConnectionResult)object);
                return;
            }
            if (object.what == 5) {
                object = this.zza;
                object = BaseGmsClient.zza((BaseGmsClient)object) != null ? BaseGmsClient.zza((BaseGmsClient)object) : new ConnectionResult(8);
                this.zza.zzc.onReportServiceBinding((ConnectionResult)object);
                this.zza.onConnectionFailed((ConnectionResult)object);
                return;
            }
            if (object.what == 3) {
                if (object.obj instanceof PendingIntent) {
                    object2 = (PendingIntent)object.obj;
                }
                object = new ConnectionResult(object.arg2, (PendingIntent)object2);
                this.zza.zzc.onReportServiceBinding((ConnectionResult)object);
                this.zza.onConnectionFailed((ConnectionResult)object);
                return;
            }
            if (object.what == 6) {
                BaseGmsClient.zzi(this.zza, 5, null);
                object2 = this.zza;
                if (BaseGmsClient.zzb((BaseGmsClient)object2) != null) {
                    BaseGmsClient.zzb((BaseGmsClient)object2).onConnectionSuspended(object.arg2);
                }
                this.zza.onConnectionSuspended(object.arg2);
                BaseGmsClient.zzn(this.zza, 5, 1, null);
                return;
            }
            if (object.what == 2 && !this.zza.isConnected()) {
                zzb.zza(object);
                return;
            }
            if (zzb.zzb(object)) {
                ((zzc)object.obj).zze();
                return;
            }
            n = object.what;
            object2 = new StringBuilder(45);
            ((StringBuilder)object2).append("Don't know how to handle message: ");
            ((StringBuilder)object2).append(n);
            object = new Exception();
            Log.wtf((String)"GmsClient", (String)((StringBuilder)object2).toString(), (Throwable)object);
            return;
        }
        zzb.zza(object);
    }
}

