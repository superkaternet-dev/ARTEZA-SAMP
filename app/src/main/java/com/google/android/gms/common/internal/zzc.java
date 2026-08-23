/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.google.android.gms.common.internal;

import android.util.Log;
import com.google.android.gms.common.internal.BaseGmsClient;
import java.util.ArrayList;

public abstract class zzc<TListener> {
    private TListener zza;
    private boolean zzb;
    final BaseGmsClient zzd;

    /*
     * Ignored method signature, as it can't be verified against descriptor
     */
    public zzc(BaseGmsClient baseGmsClient, Object object) {
        this.zzd = baseGmsClient;
        this.zza = object;
        this.zzb = false;
    }

    protected abstract void zza(TListener var1);

    protected abstract void zzc();

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public final void zze() {
        // MONITORENTER : this
        TListener TListener = this.zza;
        if (this.zzb) {
            String string2 = this.toString();
            int n = string2.length();
            StringBuilder stringBuilder = new StringBuilder(n + 47);
            stringBuilder.append("Callback proxy ");
            stringBuilder.append(string2);
            stringBuilder.append(" being reused. This is not safe.");
            Log.w((String)"GmsClient", (String)stringBuilder.toString());
        }
        // MONITOREXIT : this
        if (TListener != null) {
            this.zza(TListener);
        }
        // MONITORENTER : this
        this.zzb = true;
        // MONITOREXIT : this
        this.zzg();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void zzf() {
        synchronized (this) {
            this.zza = null;
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void zzg() {
        this.zzf();
        ArrayList arrayList = BaseGmsClient.zzf(this.zzd);
        synchronized (arrayList) {
            BaseGmsClient.zzf(this.zzd).remove(this);
            return;
        }
    }
}

