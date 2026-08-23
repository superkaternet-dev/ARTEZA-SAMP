/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.tasks;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.zzj;

final class zzi
implements Runnable {
    final Task zza;
    final zzj zzb;

    zzi(zzj zzj2, Task task) {
        this.zzb = zzj2;
        this.zza = task;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final void run() {
        Object object = zzj.zzb(this.zzb);
        synchronized (object) {
            zzj zzj2 = this.zzb;
            if (zzj.zza(zzj2) != null) {
                zzj.zza(zzj2).onComplete(this.zza);
            }
            return;
        }
    }
}

