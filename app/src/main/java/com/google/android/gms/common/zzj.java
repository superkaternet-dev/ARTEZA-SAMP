/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common;

import com.google.android.gms.common.zzi;
import java.util.Arrays;

final class zzj
extends zzi {
    private final byte[] zza;

    zzj(byte[] byArray) {
        super(Arrays.copyOfRange(byArray, 0, 25));
        this.zza = byArray;
    }

    @Override
    final byte[] zzf() {
        return this.zza;
    }
}

