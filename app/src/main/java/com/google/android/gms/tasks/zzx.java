/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.tasks;

import com.google.android.gms.internal.tasks.zza;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.zzb;

public final class zzx
implements OnCompleteListener {
    public final zza zza;
    public final TaskCompletionSource zzb;
    public final zzb zzc;

    public /* synthetic */ zzx(zza zza2, TaskCompletionSource taskCompletionSource, zzb zzb2) {
        this.zza = zza2;
        this.zzb = taskCompletionSource;
        this.zzc = zzb2;
    }

    public final void onComplete(Task object) {
        zza zza2 = this.zza;
        TaskCompletionSource taskCompletionSource = this.zzb;
        zzb zzb2 = this.zzc;
        zza2.removeCallbacksAndMessages(null);
        if (((Task)object).isSuccessful()) {
            taskCompletionSource.trySetResult(((Task)object).getResult());
            return;
        }
        if (((Task)object).isCanceled()) {
            zzb2.zza();
            return;
        }
        object = ((Task)object).getException();
        object.getClass();
        taskCompletionSource.trySetException((Exception)object);
    }
}

