/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.tasks;

import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.zzl;

final class zzk
implements Runnable {
    final Task zza;
    final zzl zzb;

    zzk(zzl zzl2, Task task) {
        this.zzb = zzl2;
        this.zza = task;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final void run() {
        Object object = zzl.zzb(this.zzb);
        synchronized (object) {
            zzl zzl2 = this.zzb;
            if (zzl.zza(zzl2) != null) {
                zzl.zza(zzl2).onFailure(Preconditions.checkNotNull(this.zza.getException()));
            }
            return;
        }
    }
}

