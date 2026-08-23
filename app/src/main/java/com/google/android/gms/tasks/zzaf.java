/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.tasks;

import com.google.android.gms.tasks.zzae;
import com.google.android.gms.tasks.zzw;
import java.util.concurrent.ExecutionException;

final class zzaf
implements zzae {
    private final Object zza = new Object();
    private final int zzb;
    private final zzw<Void> zzc;
    private int zzd;
    private int zze;
    private int zzf;
    private Exception zzg;
    private boolean zzh;

    public zzaf(int n, zzw<Void> zzw2) {
        this.zzb = n;
        this.zzc = zzw2;
    }

    private final void zza() {
        if (this.zzd + this.zze + this.zzf == this.zzb) {
            if (this.zzg != null) {
                zzw<Void> zzw2 = this.zzc;
                int n = this.zze;
                int n2 = this.zzb;
                StringBuilder stringBuilder = new StringBuilder(54);
                stringBuilder.append(n);
                stringBuilder.append(" out of ");
                stringBuilder.append(n2);
                stringBuilder.append(" underlying tasks failed");
                zzw2.zza(new ExecutionException(stringBuilder.toString(), this.zzg));
                return;
            }
            if (this.zzh) {
                this.zzc.zzc();
                return;
            }
            this.zzc.zzb(null);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final void onCanceled() {
        Object object = this.zza;
        synchronized (object) {
            ++this.zzf;
            this.zzh = true;
            this.zza();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final void onFailure(Exception exception) {
        Object object = this.zza;
        synchronized (object) {
            ++this.zze;
            this.zzg = exception;
            this.zza();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final void onSuccess(Object object) {
        Object object2 = this.zza;
        synchronized (object2) {
            ++this.zzd;
            this.zza();
            return;
        }
    }
}

