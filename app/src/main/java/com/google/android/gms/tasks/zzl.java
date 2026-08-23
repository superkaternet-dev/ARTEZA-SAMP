/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.android.gms.tasks;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.zzk;
import com.google.android.gms.tasks.zzq;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

final class zzl<TResult>
implements zzq<TResult> {
    private final Executor zza;
    private final Object zzb = new Object();
    @Nullable
    private OnFailureListener zzc;

    public zzl(Executor executor, OnFailureListener onFailureListener) {
        this.zza = executor;
        this.zzc = onFailureListener;
    }

    static /* bridge */ /* synthetic */ OnFailureListener zza(zzl zzl2) {
        return zzl2.zzc;
    }

    static /* bridge */ /* synthetic */ Object zzb(zzl zzl2) {
        return zzl2.zzb;
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
        if (!task.isSuccessful() && !task.isCanceled()) {
            Object object = this.zzb;
            synchronized (object) {
                if (this.zzc == null) {
                    return;
                }
            }
            this.zza.execute(new zzk(this, task));
            return;
        }
    }
}

