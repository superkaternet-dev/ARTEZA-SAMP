/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.util.Log
 */
package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.zzab;
import com.google.android.gms.common.internal.zzj;

public final class zzd
extends zzab {
    private BaseGmsClient zza;
    private final int zzb;

    public zzd(BaseGmsClient baseGmsClient, int n) {
        this.zza = baseGmsClient;
        this.zzb = n;
    }

    @Override
    public final void onPostInitComplete(int n, IBinder iBinder, Bundle bundle) {
        Preconditions.checkNotNull(this.zza, "onPostInitComplete can be called only once per call to getRemoteService");
        this.zza.onPostInitHandler(n, iBinder, bundle, this.zzb);
        this.zza = null;
    }

    @Override
    public final void zzb(int n, Bundle bundle) {
        Log.wtf((String)"GmsClient", (String)"received deprecated onAccountValidationComplete callback, ignoring", (Throwable)new Exception());
    }

    @Override
    public final void zzc(int n, IBinder iBinder, zzj zzj2) {
        BaseGmsClient baseGmsClient = this.zza;
        Preconditions.checkNotNull(baseGmsClient, "onPostInitCompleteWithConnectionInfo can be called only once per call togetRemoteService");
        Preconditions.checkNotNull(zzj2);
        BaseGmsClient.zzj(baseGmsClient, zzj2);
        this.onPostInitComplete(n, iBinder, zzj2.zza);
    }
}

