/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common;

import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.zzz;
import com.google.android.gms.internal.common.zzag;
import java.util.List;

final class zzx {
    private String zza = null;
    private long zzb = -1L;
    private zzag<byte[]> zzc = zzag.zzl();
    private zzag<byte[]> zzd = zzag.zzl();

    zzx() {
    }

    final zzx zza(long l) {
        this.zzb = l;
        return this;
    }

    final zzx zzb(List<byte[]> list) {
        Preconditions.checkNotNull(list);
        this.zzd = zzag.zzk(list);
        return this;
    }

    final zzx zzc(List<byte[]> list) {
        Preconditions.checkNotNull(list);
        this.zzc = zzag.zzk(list);
        return this;
    }

    final zzx zzd(String string2) {
        this.zza = string2;
        return this;
    }

    final zzz zze() {
        if (this.zza != null) {
            if (this.zzb >= 0L) {
                if (this.zzc.isEmpty() && this.zzd.isEmpty()) {
                    throw new IllegalStateException("Either orderedTestCerts or orderedProdCerts must have at least one cert");
                }
                return new zzz(this.zza, this.zzb, this.zzc, this.zzd, null);
            }
            throw new IllegalStateException("minimumStampedVersionNumber must be greater than or equal to 0");
        }
        throw new IllegalStateException("packageName must be defined");
    }
}

