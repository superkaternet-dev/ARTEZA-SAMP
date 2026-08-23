/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common;

import com.google.android.gms.common.zzm;
import com.google.android.gms.common.zzx;
import com.google.android.gms.common.zzz;
import com.google.android.gms.internal.common.zzag;

public class GmsSignatureVerifier {
    private static final zzz zza;
    private static final zzz zzb;

    static {
        zzx zzx2 = new zzx();
        zzx2.zzd("com.google.android.gms");
        zzx2.zza(204200000L);
        zzx2.zzc(zzag.zzn(zzm.zzd.zzf(), zzm.zzb.zzf()));
        zzx2.zzb(zzag.zzn(zzm.zzc.zzf(), zzm.zza.zzf()));
        zza = zzx2.zze();
        zzx2 = new zzx();
        zzx2.zzd("com.android.vending");
        zzx2.zza(82240000L);
        zzx2.zzc(zzag.zzm(zzm.zzd.zzf()));
        zzx2.zzb(zzag.zzm(zzm.zzc.zzf()));
        zzb = zzx2.zze();
    }
}

