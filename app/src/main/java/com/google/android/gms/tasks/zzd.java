/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.tasks;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.zzc;
import com.google.android.gms.tasks.zzq;
import com.google.android.gms.tasks.zzw;
import java.util.concurrent.Executor;

final class zzd<TResult, TContinuationResult>
implements zzq<TResult> {
    private final Executor zza;
    private final Continuation<TResult, TContinuationResult> zzb;
    private final zzw<TContinuationResult> zzc;

    public zzd(Executor executor, Continuation<TResult, TContinuationResult> continuation, zzw<TContinuationResult> zzw2) {
        this.zza = executor;
        this.zzb = continuation;
        this.zzc = zzw2;
    }

    static /* bridge */ /* synthetic */ Continuation zza(zzd zzd2) {
        return zzd2.zzb;
    }

    static /* bridge */ /* synthetic */ zzw zzb(zzd zzd2) {
        return zzd2.zzc;
    }

    @Override
    public final void zzc() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void zzd(Task<TResult> task) {
        this.zza.execute(new zzc(this, task));
    }
}

