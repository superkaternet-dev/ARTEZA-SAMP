/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.google.android.gms.dynamite;

import android.content.Context;
import com.google.android.gms.dynamite.DynamiteModule;

final class zzo
implements DynamiteModule.VersionPolicy.IVersions {
    private final int zza;

    public zzo(int n, int n2) {
        this.zza = n;
    }

    @Override
    public final int zza(Context context, String string2) {
        return this.zza;
    }

    @Override
    public final int zzb(Context context, String string2, boolean bl) {
        return 0;
    }
}

