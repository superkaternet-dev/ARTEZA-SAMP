/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.tasks;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.zzq;
import java.util.ArrayDeque;
import java.util.Queue;

final class zzr<TResult> {
    private final Object zza = new Object();
    private Queue<zzq<TResult>> zzb;
    private boolean zzc;

    zzr() {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void zza(zzq<TResult> zzq2) {
        Object object = this.zza;
        synchronized (object) {
            if (this.zzb == null) {
                ArrayDeque<zzq<TResult>> arrayDeque = new ArrayDeque<zzq<TResult>>();
                this.zzb = arrayDeque;
            }
            this.zzb.add(zzq2);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void zzb(Task<TResult> task) {
        Object object;
        block10: {
            object = this.zza;
            synchronized (object) {
                if (this.zzb != null && !this.zzc) {
                    this.zzc = true;
                    break block10;
                }
                return;
            }
        }
        while (true) {
            zzq<TResult> zzq2;
            object = this.zza;
            synchronized (object) {
                zzq2 = this.zzb.poll();
                if (zzq2 == null) {
                    this.zzc = false;
                    return;
                }
            }
            zzq2.zzd(task);
        }
    }
}

