/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.ServiceConnection
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.Message
 */
package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.android.gms.common.internal.GmsClientSupervisor;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.zzn;
import com.google.android.gms.common.internal.zzo;
import com.google.android.gms.common.internal.zzq;
import com.google.android.gms.common.stats.ConnectionTracker;
import com.google.android.gms.internal.common.zzi;
import java.util.HashMap;
import java.util.concurrent.Executor;

final class zzr
extends GmsClientSupervisor {
    private final HashMap<zzn, zzo> zzb = new HashMap();
    private final Context zzc;
    private volatile Handler zzd;
    private final zzq zze;
    private final ConnectionTracker zzf;
    private final long zzg;
    private final long zzh;

    zzr(Context context, Looper looper) {
        zzq zzq2;
        this.zze = zzq2 = new zzq(this, null);
        this.zzc = context.getApplicationContext();
        this.zzd = new zzi(looper, zzq2);
        this.zzf = ConnectionTracker.getInstance();
        this.zzg = 5000L;
        this.zzh = 300000L;
    }

    static /* bridge */ /* synthetic */ long zzd(zzr zzr2) {
        return zzr2.zzh;
    }

    static /* bridge */ /* synthetic */ Context zze(zzr zzr2) {
        return zzr2.zzc;
    }

    static /* bridge */ /* synthetic */ Handler zzf(zzr zzr2) {
        return zzr2.zzd;
    }

    static /* bridge */ /* synthetic */ ConnectionTracker zzg(zzr zzr2) {
        return zzr2.zzf;
    }

    static /* bridge */ /* synthetic */ HashMap zzh(zzr zzr2) {
        return zzr2.zzb;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected final void zza(zzn object, ServiceConnection object2, String charSequence) {
        Preconditions.checkNotNull(object2, "ServiceConnection must not be null");
        HashMap<zzn, zzo> hashMap = this.zzb;
        synchronized (hashMap) {
            zzo zzo2 = this.zzb.get(object);
            if (zzo2 == null) {
                charSequence = object.toString();
                int n = ((String)charSequence).length();
                object = new StringBuilder(n + 50);
                ((StringBuilder)object).append("Nonexistent connection status for service config: ");
                ((StringBuilder)object).append((String)charSequence);
                object2 = new IllegalStateException(((StringBuilder)object).toString());
                throw object2;
            }
            if (!zzo2.zzh((ServiceConnection)object2)) {
                object = object.toString();
                int n = ((String)object).length();
                charSequence = new StringBuilder(n + 76);
                ((StringBuilder)charSequence).append("Trying to unbind a GmsServiceConnection  that was not bound before.  config=");
                ((StringBuilder)charSequence).append((String)object);
                object2 = new IllegalStateException(((StringBuilder)charSequence).toString());
                throw object2;
            }
            zzo2.zzf((ServiceConnection)object2, (String)charSequence);
            if (zzo2.zzi()) {
                object = this.zzd.obtainMessage(0, object);
                this.zzd.sendMessageDelayed((Message)object, this.zzg);
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected final boolean zzc(zzn object, ServiceConnection object2, String charSequence, Executor executor) {
        Preconditions.checkNotNull(object2, "ServiceConnection must not be null");
        HashMap<zzn, zzo> hashMap = this.zzb;
        synchronized (hashMap) {
            zzo zzo2 = this.zzb.get(object);
            if (zzo2 == null) {
                zzo2 = new zzo(this, (zzn)object);
                zzo2.zzd((ServiceConnection)object2, (ServiceConnection)object2, (String)charSequence);
                zzo2.zze((String)charSequence, executor);
                this.zzb.put((zzn)object, zzo2);
                object = zzo2;
                return ((zzo)object).zzj();
            } else {
                this.zzd.removeMessages(0, object);
                if (zzo2.zzh((ServiceConnection)object2)) {
                    object = object.toString();
                    int n = ((String)object).length();
                    charSequence = new StringBuilder(n + 81);
                    ((StringBuilder)charSequence).append("Trying to bind a GmsServiceConnection that was already connected before.  config=");
                    ((StringBuilder)charSequence).append((String)object);
                    object2 = new IllegalStateException(((StringBuilder)charSequence).toString());
                    throw object2;
                }
                zzo2.zzd((ServiceConnection)object2, (ServiceConnection)object2, (String)charSequence);
                switch (zzo2.zza()) {
                    default: {
                        object = zzo2;
                        return ((zzo)object).zzj();
                    }
                    case 2: {
                        zzo2.zze((String)charSequence, executor);
                        object = zzo2;
                        return ((zzo)object).zzj();
                    }
                    case 1: {
                        object2.onServiceConnected(zzo2.zzb(), zzo2.zzc());
                        object = zzo2;
                    }
                }
            }
            return ((zzo)object).zzj();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    final void zzi(Looper looper) {
        HashMap<zzn, zzo> hashMap = this.zzb;
        synchronized (hashMap) {
            zzi zzi2 = new zzi(looper, this.zze);
            this.zzd = zzi2;
            return;
        }
    }
}

