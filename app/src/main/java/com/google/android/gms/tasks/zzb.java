/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.tasks;

import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.gms.tasks.zza;
import com.google.android.gms.tasks.zzw;

final class zzb
extends CancellationToken {
    private final zzw<Void> zza = new zzw();

    zzb() {
    }

    @Override
    public final boolean isCancellationRequested() {
        return this.zza.isComplete();
    }

    @Override
    public final CancellationToken onCanceledRequested(OnTokenCanceledListener object) {
        zzw<Void> zzw2 = this.zza;
        object = new zza(this, (OnTokenCanceledListener)object);
        zzw2.addOnSuccessListener(TaskExecutors.MAIN_THREAD, (OnSuccessListener<Void>)object);
        return this;
    }

    public final void zza() {
        this.zza.zze(null);
    }
}

