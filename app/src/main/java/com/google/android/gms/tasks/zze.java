/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.tasks;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.gms.tasks.zzf;

final class zze
implements Runnable {
    final Task zza;
    final zzf zzb;

    zze(zzf zzf2, Task task) {
        this.zzb = zzf2;
        this.zza = task;
    }

    @Override
    public final void run() {
        Task task;
        block4: {
            try {
                task = (Task)zzf.zza(this.zzb).then(this.zza);
                if (task != null) break block4;
            }
            catch (Exception exception) {
                zzf.zzb(this.zzb).zza(exception);
                return;
            }
            catch (RuntimeExecutionException runtimeExecutionException) {
                if (runtimeExecutionException.getCause() instanceof Exception) {
                    zzf.zzb(this.zzb).zza((Exception)runtimeExecutionException.getCause());
                    return;
                }
                zzf.zzb(this.zzb).zza(runtimeExecutionException);
                return;
            }
            this.zzb.onFailure(new NullPointerException("Continuation returned null"));
            return;
        }
        task.addOnSuccessListener(TaskExecutors.zza, this.zzb);
        task.addOnFailureListener(TaskExecutors.zza, (OnFailureListener)this.zzb);
        task.addOnCanceledListener(TaskExecutors.zza, (OnCanceledListener)this.zzb);
        return;
    }
}

