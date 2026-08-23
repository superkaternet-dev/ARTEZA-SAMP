/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.RemoteException
 */
package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.common.internal.IGmsCallbacks;
import com.google.android.gms.common.internal.zzj;
import com.google.android.gms.internal.common.zza;
import com.google.android.gms.internal.common.zzc;

public final class zzaa
extends zza
implements IGmsCallbacks {
    zzaa(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.common.internal.IGmsCallbacks");
    }

    @Override
    public final void onPostInitComplete(int n, IBinder iBinder, Bundle bundle) throws RemoteException {
        Parcel parcel = this.zza();
        parcel.writeInt(n);
        parcel.writeStrongBinder(iBinder);
        zzc.zzc(parcel, (Parcelable)bundle);
        this.zzC(1, parcel);
    }

    @Override
    public final void zzb(int n, Bundle bundle) throws RemoteException {
        throw null;
    }

    @Override
    public final void zzc(int n, IBinder iBinder, zzj zzj2) throws RemoteException {
        throw null;
    }
}

