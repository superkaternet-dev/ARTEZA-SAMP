/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package com.google.android.gms.signin.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.internal.IAccountAccessor;
import com.google.android.gms.internal.base.zaa;
import com.google.android.gms.internal.base.zac;
import com.google.android.gms.signin.internal.zae;
import com.google.android.gms.signin.internal.zai;

public final class zaf
extends zaa
implements IInterface {
    zaf(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.signin.internal.ISignInService");
    }

    public final void zae(int n) throws RemoteException {
        Parcel parcel = this.zaa();
        parcel.writeInt(n);
        this.zac(7, parcel);
    }

    public final void zaf(IAccountAccessor iAccountAccessor, int n, boolean bl) throws RemoteException {
        Parcel parcel = this.zaa();
        zac.zad(parcel, iAccountAccessor);
        parcel.writeInt(n);
        zac.zab(parcel, bl);
        this.zac(9, parcel);
    }

    public final void zag(zai zai2, zae zae2) throws RemoteException {
        Parcel parcel = this.zaa();
        zac.zac(parcel, zai2);
        zac.zad(parcel, zae2);
        this.zac(12, parcel);
    }
}

