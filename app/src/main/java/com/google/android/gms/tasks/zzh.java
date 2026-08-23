/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.android.gms.tasks;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.zzg;
import com.google.android.gms.tasks.zzq;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

final class zzh<TResult>
implements zzq<TResult> {
    private final Executor zza;
    private final Object zzb = new Object();
    @Nullable
    private OnCanceledListener zzc;

    public zzh(Executor executor, OnCanceledListener onCanceledListener) {
        this.zza = executor;
        this.zzc = onCanceledListener;
    }

    static /* bridge */ /* synthetic */ OnCanceledListener zza(zzh zzh2) {
        return zzh2.zzc;
    }

    static /* bridge */ /* synthetic */ Object zzb(zzh zzh2) {
        return zzh2.zzb;
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
        if (!task.isCanceled()) {
            return;
        }
        Object object = this.zzb;
        synchronized (object) {
            if (this.zzc == null) {
                return;
            }
        }
        this.zza.execute(new zzg(this));
    }
}

