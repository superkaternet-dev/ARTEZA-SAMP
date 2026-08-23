/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package com.google.android.gms.dynamite;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.common.zza;
import com.google.android.gms.internal.common.zzc;

public final class zzq
extends zza
implements IInterface {
    zzq(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.dynamite.IDynamiteLoader");
    }

    public final int zze() throws RemoteException {
        Parcel parcel = this.zzB(6, this.zza());
        int n = parcel.readInt();
        parcel.recycle();
        return n;
    }

    public final int zzf(IObjectWrapper iObjectWrapper, String string2, boolean bl) throws RemoteException {
        Parcel parcel = this.zza();
        zzc.zze(parcel, iObjectWrapper);
        parcel.writeString(string2);
        zzc.zzb(parcel, bl);
        iObjectWrapper = this.zzB(3, parcel);
        int n = iObjectWrapper.readInt();
        iObjectWrapper.recycle();
        return n;
    }

    public final int zzg(IObjectWrapper iObjectWrapper, String string2, boolean bl) throws RemoteException {
        Parcel parcel = this.zza();
        zzc.zze(parcel, iObjectWrapper);
        parcel.writeString(string2);
        zzc.zzb(parcel, bl);
        iObjectWrapper = this.zzB(5, parcel);
        int n = iObjectWrapper.readInt();
        iObjectWrapper.recycle();
        return n;
    }

    public final IObjectWrapper zzh(IObjectWrapper iObjectWrapper, String string2, int n) throws RemoteException {
        Parcel parcel = this.zza();
        zzc.zze(parcel, iObjectWrapper);
        parcel.writeString(string2);
        parcel.writeInt(n);
        string2 = this.zzB(2, parcel);
        iObjectWrapper = IObjectWrapper.Stub.asInterface(string2.readStrongBinder());
        string2.recycle();
        return iObjectWrapper;
    }

    public final IObjectWrapper zzi(IObjectWrapper iObjectWrapper, String string2, int n, IObjectWrapper iObjectWrapper2) throws RemoteException {
        Parcel parcel = this.zza();
        zzc.zze(parcel, iObjectWrapper);
        parcel.writeString(string2);
        parcel.writeInt(n);
        zzc.zze(parcel, iObjectWrapper2);
        string2 = this.zzB(8, parcel);
        iObjectWrapper = IObjectWrapper.Stub.asInterface(string2.readStrongBinder());
        string2.recycle();
        return iObjectWrapper;
    }

    public final IObjectWrapper zzj(IObjectWrapper iObjectWrapper, String string2, int n) throws RemoteException {
        Parcel parcel = this.zza();
        zzc.zze(parcel, iObjectWrapper);
        parcel.writeString(string2);
        parcel.writeInt(n);
        string2 = this.zzB(4, parcel);
        iObjectWrapper = IObjectWrapper.Stub.asInterface(string2.readStrongBinder());
        string2.recycle();
        return iObjectWrapper;
    }

    public final IObjectWrapper zzk(IObjectWrapper iObjectWrapper, String string2, boolean bl, long l) throws RemoteException {
        Parcel parcel = this.zza();
        zzc.zze(parcel, iObjectWrapper);
        parcel.writeString(string2);
        zzc.zzb(parcel, bl);
        parcel.writeLong(l);
        string2 = this.zzB(7, parcel);
        iObjectWrapper = IObjectWrapper.Stub.asInterface(string2.readStrongBinder());
        string2.recycle();
        return iObjectWrapper;
    }
}

