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
import com.google.android.gms.common.internal.zzz;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.common.zza;

public final class zzx
extends zza
implements zzz {
    zzx(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.common.internal.ICertData");
    }

    @Override
    public final int zzc() throws RemoteException {
        Parcel parcel = this.zzB(2, this.zza());
        int n = parcel.readInt();
        parcel.recycle();
        return n;
    }

    @Override
    public final IObjectWrapper zzd() throws RemoteException {
        Parcel parcel = this.zzB(1, this.zza());
        IObjectWrapper iObjectWrapper = IObjectWrapper.Stub.asInterface(parcel.readStrongBinder());
        parcel.recycle();
        return iObjectWrapper;
    }
}

