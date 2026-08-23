/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.CheckForNull
 */
package com.google.android.gms.internal.common;

import com.google.android.gms.internal.common.zzag;
import com.google.android.gms.internal.common.zzs;
import javax.annotation.CheckForNull;

final class zzaf
extends zzag {
    final transient int zza;
    final transient int zzb;
    final zzag zzc;

    zzaf(zzag zzag2, int n, int n2) {
        this.zzc = zzag2;
        this.zza = n;
        this.zzb = n2;
    }

    @Override
    public final Object get(int n) {
        zzs.zza(n, this.zzb, "index");
        return this.zzc.get(n + this.zza);
    }

    @Override
    public final int size() {
        return this.zzb;
    }

    @Override
    final int zzb() {
        return this.zzc.zzc() + this.zza + this.zzb;
    }

    @Override
    final int zzc() {
        return this.zzc.zzc() + this.zza;
    }

    @Override
    final boolean zzf() {
        return true;
    }

    @Override
    @CheckForNull
    final Object[] zzg() {
        return this.zzc.zzg();
    }

    public final zzag zzh(int n, int n2) {
        zzs.zzc(n, n2, this.zzb);
        zzag zzag2 = this.zzc;
        int n3 = this.zza;
        return zzag2.zzh(n + n3, n2 + n3);
    }
}

