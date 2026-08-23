/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  javax.annotation.Nullable
 */
package com.google.android.gms.common;

import android.util.Log;
import javax.annotation.Nullable;

class zzw {
    private static final zzw zzd = new zzw(true, null, null);
    final boolean zza;
    @Nullable
    final String zzb;
    @Nullable
    final Throwable zzc;

    zzw(boolean bl, @Nullable String string2, @Nullable Throwable throwable) {
        this.zza = bl;
        this.zzb = string2;
        this.zzc = throwable;
    }

    static zzw zzb() {
        return zzd;
    }

    static zzw zzc(String string2) {
        return new zzw(false, string2, null);
    }

    static zzw zzd(String string2, Throwable throwable) {
        return new zzw(false, string2, throwable);
    }

    @Nullable
    String zza() {
        return this.zzb;
    }

    final void zze() {
        if (!this.zza && Log.isLoggable((String)"GoogleCertificatesRslt", (int)3)) {
            if (this.zzc != null) {
                Log.d((String)"GoogleCertificatesRslt", (String)this.zza(), (Throwable)this.zzc);
                return;
            }
            Log.d((String)"GoogleCertificatesRslt", (String)this.zza());
        }
    }
}

