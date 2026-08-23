/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.tasks;

import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.zzs;
import com.google.android.gms.tasks.zzw;

public class TaskCompletionSource<TResult> {
    private final zzw<TResult> zza = new zzw();

    public TaskCompletionSource() {
    }

    public TaskCompletionSource(CancellationToken cancellationToken) {
        cancellationToken.onCanceledRequested(new zzs(this));
    }

    static /* bridge */ /* synthetic */ zzw zza(TaskCompletionSource taskCompletionSource) {
        return taskCompletionSource.zza;
    }

    public Task<TResult> getTask() {
        return this.zza;
    }

    public void setException(Exception exception) {
        this.zza.zza(exception);
    }

    public void setResult(TResult TResult) {
        this.zza.zzb(TResult);
    }

    public boolean trySetException(Exception exception) {
        return this.zza.zzd(exception);
    }

    public boolean trySetResult(TResult TResult) {
        return this.zza.zze(TResult);
    }
}

