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

public final class zzr
extends zza
implements IInterface {
    zzr(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.dynamite.IDynamiteLoaderV2");
    }

    public final IObjectWrapper zze(IObjectWrapper iObjectWrapper, String object, int n, IObjectWrapper iObjectWrapper2) throws RemoteException {
        Parcel parcel = this.zza();
        zzc.zze(parcel, iObjectWrapper);
        parcel.writeString((String)object);
        parcel.writeInt(n);
        zzc.zze(parcel, iObjectWrapper2);
        iObjectWrapper = this.zzB(2, parcel);
        object = IObjectWrapper.Stub.asInterface(iObjectWrapper.readStrongBinder());
        iObjectWrapper.recycle();
        return object;
    }

    public final IObjectWrapper zzf(IObjectWrapper iObjectWrapper, String string2, int n, IObjectWrapper iObjectWrapper2) throws RemoteException {
        Parcel parcel = this.zza();
        zzc.zze(parcel, iObjectWrapper);
        parcel.writeString(string2);
        parcel.writeInt(n);
        zzc.zze(parcel, iObjectWrapper2);
        string2 = this.zzB(3, parcel);
        iObjectWrapper = IObjectWrapper.Stub.asInterface(string2.readStrongBinder());
        string2.recycle();
        return iObjectWrapper;
    }
}

