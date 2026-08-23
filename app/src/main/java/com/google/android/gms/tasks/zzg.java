/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.tasks;

import com.google.android.gms.tasks.zzh;

final class zzg
implements Runnable {
    final zzh zza;

    zzg(zzh zzh2) {
        this.zza = zzh2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final void run() {
        Object object = zzh.zzb(this.zza);
        synchronized (object) {
            zzh zzh2 = this.zza;
            if (zzh.zza(zzh2) != null) {
                zzh.zza(zzh2).onCanceled();
            }
            return;
        }
    }
}

