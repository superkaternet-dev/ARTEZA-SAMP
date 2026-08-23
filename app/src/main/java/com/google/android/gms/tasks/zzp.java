/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.tasks;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.zzo;
import com.google.android.gms.tasks.zzq;
import com.google.android.gms.tasks.zzw;
import java.util.concurrent.Executor;

final class zzp<TResult, TContinuationResult>
implements OnSuccessListener<TContinuationResult>,
OnFailureListener,
OnCanceledListener,
zzq {
    private final Executor zza;
    private final SuccessContinuation<TResult, TContinuationResult> zzb;
    private final zzw<TContinuationResult> zzc;

    public zzp(Executor executor, SuccessContinuation<TResult, TContinuationResult> successContinuation, zzw<TContinuationResult> zzw2) {
        this.zza = executor;
        this.zzb = successContinuation;
        this.zzc = zzw2;
    }

    static /* bridge */ /* synthetic */ SuccessContinuation zza(zzp zzp2) {
        return zzp2.zzb;
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
        this.zza.execute(new zzo(this, task));
    }
}

