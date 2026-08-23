/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.android.gms.tasks;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.zzi;
import com.google.android.gms.tasks.zzq;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

final class zzj<TResult>
implements zzq<TResult> {
    private final Executor zza;
    private final Object zzb = new Object();
    @Nullable
    private OnCompleteListener<TResult> zzc;

    public zzj(Executor executor, OnCompleteListener<TResult> onCompleteListener) {
        this.zza = executor;
        this.zzc = onCompleteListener;
    }

    static /* bridge */ /* synthetic */ OnCompleteListener zza(zzj zzj2) {
        return zzj2.zzc;
    }

    static /* bridge */ /* synthetic */ Object zzb(zzj zzj2) {
        return zzj2.zzb;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final void zzc() {
        Object object = this.zzb;
        synchronized (object) {
            this.zzc = null;
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final void zzd(Task<TResult> task) {
        Object object = this.zzb;
        synchronized (object) {
            if (this.zzc == null) {
                return;
            }
        }
        this.zza.execute(new zzi(this, task));
    }
}

