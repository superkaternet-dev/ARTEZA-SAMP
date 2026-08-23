/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 */
package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import com.google.android.gms.common.api.internal.LifecycleCallback;
import com.google.android.gms.common.api.internal.zzd;

final class zzc
implements Runnable {
    final LifecycleCallback zza;
    final String zzb;
    final zzd zzc;

    zzc(zzd zzd2, LifecycleCallback lifecycleCallback, String string2) {
        this.zzc = zzd2;
        this.zza = lifecycleCallback;
        this.zzb = string2;
    }

    @Override
    public final void run() {
        zzd zzd2 = this.zzc;
        if (zzd.zza(zzd2) > 0) {
            LifecycleCallback lifecycleCallback = this.zza;
            zzd2 = zzd.zzb(zzd2) != null ? zzd.zzb(zzd2).getBundle(this.zzb) : null;
            lifecycleCallback.onCreate((Bundle)zzd2);
        }
        if (zzd.zza(this.zzc) >= 2) {
            this.zza.onStart();
        }
        if (zzd.zza(this.zzc) >= 3) {
            this.zza.onResume();
        }
        if (zzd.zza(this.zzc) >= 4) {
            this.zza.onStop();
        }
        if (zzd.zza(this.zzc) >= 5) {
            this.zza.onDestroy();
        }
    }
}

