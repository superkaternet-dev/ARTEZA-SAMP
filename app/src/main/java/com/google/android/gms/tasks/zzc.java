/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.tasks;

import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.zzd;

final class zzc
implements Runnable {
    final Task zza;
    final zzd zzb;

    zzc(zzd zzd2, Task task) {
        this.zzb = zzd2;
        this.zza = task;
    }

    @Override
    public final void run() {
        Object TContinuationResult;
        if (this.zza.isCanceled()) {
            zzd.zzb(this.zzb).zzc();
            return;
        }
        try {
            TContinuationResult = zzd.zza(this.zzb).then(this.zza);
        }
        catch (Exception exception) {
            zzd.zzb(this.zzb).zza(exception);
            return;
        }
        catch (RuntimeExecutionException runtimeExecutionException) {
            if (runtimeExecutionException.getCause() instanceof Exception) {
                zzd.zzb(this.zzb).zza((Exception)runtimeExecutionException.getCause());
                return;
            }
            zzd.zzb(this.zzb).zza(runtimeExecutionException);
            return;
        }
        zzd.zzb(this.zzb).zzb(TContinuationResult);
        return;
    }
}

