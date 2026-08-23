/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 */
package com.google.android.gms.common.internal;

import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.common.internal.zzc;

abstract class zza
extends zzc<Boolean> {
    public final int zza;
    public final Bundle zzb;
    final BaseGmsClient zzc;

    protected zza(BaseGmsClient baseGmsClient, int n, Bundle bundle) {
        this.zzc = baseGmsClient;
        super(baseGmsClient, true);
        this.zza = n;
        this.zzb = bundle;
    }

    protected abstract void zzb(ConnectionResult var1);

    @Override
    protected final void zzc() {
    }

    protected abstract boolean zzd();
}

