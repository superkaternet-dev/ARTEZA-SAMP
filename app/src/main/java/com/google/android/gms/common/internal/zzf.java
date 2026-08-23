/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.RemoteException
 *  android.util.Log
 */
package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.zza;

public final class zzf
extends zza {
    public final IBinder zze;
    final BaseGmsClient zzf;

    public zzf(BaseGmsClient baseGmsClient, int n, IBinder iBinder, Bundle bundle) {
        this.zzf = baseGmsClient;
        super(baseGmsClient, n, bundle);
        this.zze = iBinder;
    }

    @Override
    protected final void zzb(ConnectionResult connectionResult) {
        if (BaseGmsClient.zzc(this.zzf) != null) {
            BaseGmsClient.zzc(this.zzf).onConnectionFailed(connectionResult);
        }
        this.zzf.onConnectionFailed(connectionResult);
    }

    @Override
    protected final boolean zzd() {
        Object object;
        block4: {
            String string2;
            String string3;
            try {
                object = this.zze;
                Preconditions.checkNotNull(object);
                string3 = object.getInterfaceDescriptor();
                if (this.zzf.getServiceDescriptor().equals(string3)) break block4;
                string2 = this.zzf.getServiceDescriptor();
                object = new StringBuilder(String.valueOf(string2).length() + 34 + String.valueOf(string3).length());
                object.append("service descriptor mismatch: ");
            }
            catch (RemoteException remoteException) {
                Log.w((String)"GmsClient", (String)"service probably died");
                return false;
            }
            object.append(string2);
            object.append(" vs. ");
            object.append(string3);
            Log.w((String)"GmsClient", (String)object.toString());
            return false;
        }
        object = this.zzf.createServiceInterface(this.zze);
        if (object != null && (BaseGmsClient.zzn(this.zzf, 2, 4, (IInterface)object) || BaseGmsClient.zzn(this.zzf, 3, 4, (IInterface)object))) {
            BaseGmsClient.zzg(this.zzf, null);
            object = this.zzf.getConnectionHint();
            BaseGmsClient baseGmsClient = this.zzf;
            if (BaseGmsClient.zzb(baseGmsClient) != null) {
                BaseGmsClient.zzb(baseGmsClient).onConnected((Bundle)object);
            }
            return true;
        }
        return false;
    }
}

