/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common;

import com.google.android.gms.common.zzi;
import com.google.android.gms.common.zzm;
import java.util.concurrent.Callable;

public final class zzd
implements Callable {
    public final boolean zza;
    public final String zzb;
    public final zzi zzc;

    public /* synthetic */ zzd(boolean bl, String string2, zzi zzi2) {
        this.zza = bl;
        this.zzb = string2;
        this.zzc = zzi2;
    }

    public final Object call() {
        return zzm.zzc(this.zza, this.zzb, this.zzc);
    }
}

