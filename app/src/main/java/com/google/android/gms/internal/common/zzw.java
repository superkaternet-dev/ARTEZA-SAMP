/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.internal.common;

import com.google.android.gms.internal.common.zzj;
import com.google.android.gms.internal.common.zzo;
import com.google.android.gms.internal.common.zzx;

abstract class zzw
extends zzj<String> {
    final CharSequence zzb;
    final zzo zzc;
    final boolean zzd;
    int zze = 0;
    int zzf;

    protected zzw(zzx zzx2, CharSequence charSequence) {
        this.zzc = zzx.zza(zzx2);
        this.zzd = zzx.zzg(zzx2);
        this.zzf = Integer.MAX_VALUE;
        this.zzb = charSequence;
    }

    abstract int zzc(int var1);

    abstract int zzd(int var1);
}

