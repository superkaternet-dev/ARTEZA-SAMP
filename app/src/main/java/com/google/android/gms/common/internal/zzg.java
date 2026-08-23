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
import com.google.android.gms.common.internal.zza;

public final class zzg
extends zza {
    final BaseGmsClient zze;

    public zzg(BaseGmsClient baseGmsClient, int n, Bundle bundle) {
        this.zze = baseGmsClient;
        super(baseGmsClient, n, null);
    }

    @Override
    protected final void zzb(ConnectionResult connectionResult) {
        if (this.zze.enableLocalFallback() && BaseGmsClient.zzo(this.zze)) {
            BaseGmsClient.zzk(this.zze, 16);
            return;
        }
        this.zze.zzc.onReportServiceBinding(connectionResult);
        this.zze.onConnectionFailed(connectionResult);
    }

    @Override
    protected final boolean zzd() {
        this.zze.zzc.onReportServiceBinding(ConnectionResult.RESULT_SUCCESS);
        return true;
    }
}

