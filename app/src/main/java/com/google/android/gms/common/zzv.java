/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common;

import com.google.android.gms.common.zzu;
import com.google.android.gms.common.zzw;
import java.util.concurrent.Callable;

final class zzv
extends zzw {
    private final Callable<String> zzd;

    /* synthetic */ zzv(Callable callable, zzu zzu2) {
        super(false, null, null);
        this.zzd = callable;
    }

    @Override
    final String zza() {
        try {
            String string2 = this.zzd.call();
            return string2;
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}

