/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.internal.common;

import com.google.android.gms.internal.common.zzo;
import com.google.android.gms.internal.common.zzs;
import com.google.android.gms.internal.common.zzu;
import com.google.android.gms.internal.common.zzw;
import com.google.android.gms.internal.common.zzx;

final class zzt
extends zzw {
    final zzu zza;

    zzt(zzu zzu2, zzx zzx2, CharSequence charSequence) {
        this.zza = zzu2;
        super(zzx2, charSequence);
    }

    @Override
    final int zzc(int n) {
        return n + 1;
    }

    @Override
    final int zzd(int n) {
        int n2;
        block2: {
            zzo zzo2 = this.zza.zza;
            CharSequence charSequence = this.zzb;
            int n3 = charSequence.length();
            zzs.zzb(n, n3, "index");
            while (n < n3) {
                n2 = n;
                if (!zzo2.zza(charSequence.charAt(n))) {
                    ++n;
                    continue;
                }
                break block2;
            }
            n2 = -1;
        }
        return n2;
    }
}

