/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.tasks;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.zze;
import com.google.android.gms.tasks.zzq;
import com.google.android.gms.tasks.zzw;
import java.util.concurrent.Executor;

final class zzf<TResult, TContinuationResult>
implements OnSuccessListener<TContinuationResult>,
OnFailureListener,
OnCanceledListener,
zzq {
    private final Executor zza;
    private final Continuation<TResult, Task<TContinuationResult>> zzb;
    private final zzw<TContinuationResult> zzc;

    public zzf(Executor executor, Continuation<TResult, Task<TContinuationResult>> continuation, zzw<TContinuationResult> zzw2) {
        this.zza = executor;
        this.zzb = continuation;
        this.zzc = zzw2;
    }

    static /* bridge */ /* synthetic */ Continuation zza(zzf zzf2) {
        return zzf2.zzb;
    }

    static /* bridge */ /* synthetic */ zzw zzb(zzf zzf2) {
        return zzf2.zzc;
    }

    @Override
    public final void onCanceled() {
        this.zzc.zzc();
    }

    @Override
    public final void onFailure(Exception exception) {
        this.zzc.zza(exception);
    }

    @Override
    public final void onSuccess(TContinuationResult TContinuationResult) {
        this.zzc.zzb(TContinuationResult);
    }

    @Override
    public final void zzc() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void zzd(Task<TResult> task) {
        this.zza.execute(new zze(this, task));
    }
}

