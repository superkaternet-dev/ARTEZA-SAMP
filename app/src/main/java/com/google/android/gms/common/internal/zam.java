/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.RemoteException
 */
package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.common.internal.zax;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.base.zaa;
import com.google.android.gms.internal.base.zac;

public final class zam
extends zaa
implements IInterface {
    zam(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.common.internal.ISignInButtonCreator");
    }

    public final IObjectWrapper zae(IObjectWrapper iObjectWrapper, zax object) throws RemoteException {
        Parcel parcel = this.zaa();
        zac.zad(parcel, iObjectWrapper);
        zac.zac(parcel, (Parcelable)object);
        iObjectWrapper = this.zab(2, parcel);
        object = IObjectWrapper.Stub.asInterface(iObjectWrapper.readStrongBinder());
        iObjectWrapper.recycle();
        return object;
    }
}

