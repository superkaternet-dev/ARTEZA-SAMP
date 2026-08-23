/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.internal.zzaf;
import com.google.android.gms.common.zzn;
import com.google.android.gms.common.zzq;
import com.google.android.gms.common.zzs;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.common.zza;
import com.google.android.gms.internal.common.zzc;

public final class zzad
extends zza
implements zzaf {
    zzad(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.common.internal.IGoogleCertificatesApi");
    }

    @Override
    public final zzq zze(zzn abstractSafeParcelable) throws RemoteException {
        Parcel parcel = this.zza();
        zzc.zzc(parcel, abstractSafeParcelable);
        parcel = this.zzB(6, parcel);
        abstractSafeParcelable = zzc.zza(parcel, zzq.CREATOR);
        parcel.recycle();
        return abstractSafeParcelable;
    }

    @Override
    public final boolean zzf(zzs zzs2, IObjectWrapper iObjectWrapper) throws RemoteException {
        Parcel parcel = this.zza();
        zzc.zzc(parcel, zzs2);
        zzc.zze(parcel, iObjectWrapper);
        zzs2 = this.zzB(5, parcel);
        boolean bl = zzc.zzf((Parcel)zzs2);
        zzs2.recycle();
        return bl;
    }

    @Override
    public final boolean zzg() throws RemoteException {
        Parcel parcel = this.zzB(7, this.zza());
        boolean bl = zzc.zzf(parcel);
        parcel.recycle();
        return bl;
    }
}

