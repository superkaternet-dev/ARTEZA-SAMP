/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.tasks;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.zzn;

final class zzm
implements Runnable {
    final Task zza;
    final zzn zzb;

    zzm(zzn zzn2, Task task) {
        this.zzb = zzn2;
        this.zza = task;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final void run() {
        Object object = zzn.zzb(this.zzb);
        synchronized (object) {
            zzn zzn2 = this.zzb;
            if (zzn.zza(zzn2) != null) {
                zzn.zza(zzn2).onSuccess(this.zza.getResult());
            }
            return;
        }
    }
}

