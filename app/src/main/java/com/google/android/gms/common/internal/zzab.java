/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.internal.IGmsCallbacks;
import com.google.android.gms.common.internal.zzj;
import com.google.android.gms.internal.common.zzb;
import com.google.android.gms.internal.common.zzc;

public abstract class zzab
extends zzb
implements IGmsCallbacks {
    public zzab() {
        super("com.google.android.gms.common.internal.IGmsCallbacks");
    }

    @Override
    protected final boolean zza(int n, Parcel parcel, Parcel parcel2, int n2) throws RemoteException {
        switch (n) {
            default: {
                return false;
            }
            case 3: {
                this.zzc(parcel.readInt(), parcel.readStrongBinder(), zzc.zza(parcel, zzj.CREATOR));
                break;
            }
            case 2: {
                this.zzb(parcel.readInt(), (Bundle)zzc.zza(parcel, Bundle.CREATOR));
                break;
            }
            case 1: {
                this.onPostInitComplete(parcel.readInt(), parcel.readStrongBinder(), (Bundle)zzc.zza(parcel, Bundle.CREATOR));
            }
        }
        parcel2.writeNoException();
        return true;
    }
}

