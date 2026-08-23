/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.android.gms.tasks;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.zzm;
import com.google.android.gms.tasks.zzq;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

final class zzn<TResult>
implements zzq<TResult> {
    private final Executor zza;
    private final Object zzb = new Object();
    @Nullable
    private OnSuccessListener<? super TResult> zzc;

    public zzn(Executor executor, OnSuccessListener<? super TResult> onSuccessListener) {
        this.zza = executor;
        this.zzc = onSuccessListener;
    }

    static /* bridge */ /* synthetic */ OnSuccessListener zza(zzn zzn2) {
        return zzn2.zzc;
    }

    static /* bridge */ /* synthetic */ Object zzb(zzn zzn2) {
        return zzn2.zzb;
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
        if (!task.isSuccessful()) {
            return;
        }
        Object object = this.zzb;
        synchronized (object) {
            if (this.zzc == null) {
                return;
            }
        }
        this.zza.execute(new zzm(this, task));
    }
}

