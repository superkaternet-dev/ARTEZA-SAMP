/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 */
package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import com.google.android.gms.common.api.internal.LifecycleCallback;
import com.google.android.gms.common.api.internal.zzb;

final class zza
implements Runnable {
    final LifecycleCallback zza;
    final String zzb;
    final zzb zzc;

    zza(zzb zzb2, LifecycleCallback lifecycleCallback, String string2) {
        this.zzc = zzb2;
        this.zza = lifecycleCallback;
        this.zzb = string2;
    }

    @Override
    public final void run() {
        zzb zzb2 = this.zzc;
        if (com.google.android.gms.common.api.internal.zzb.zza(zzb2) > 0) {
            LifecycleCallback lifecycleCallback = this.zza;
            zzb2 = com.google.android.gms.common.api.internal.zzb.zzb(zzb2) != null ? com.google.android.gms.common.api.internal.zzb.zzb(zzb2).getBundle(this.zzb) : null;
            lifecycleCallback.onCreate((Bundle)zzb2);
        }
        if (com.google.android.gms.common.api.internal.zzb.zza(this.zzc) >= 2) {
            this.zza.onStart();
        }
        if (com.google.android.gms.common.api.internal.zzb.zza(this.zzc) >= 3) {
            this.zza.onResume();
        }
        if (com.google.android.gms.common.api.internal.zzb.zza(this.zzc) >= 4) {
            this.zza.onStop();
        }
        if (com.google.android.gms.common.api.internal.zzb.zza(this.zzc) >= 5) {
            this.zza.onDestroy();
        }
    }
}

