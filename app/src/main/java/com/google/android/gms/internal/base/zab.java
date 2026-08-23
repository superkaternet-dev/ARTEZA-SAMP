/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Binder
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package com.google.android.gms.internal.base;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public class zab
extends Binder
implements IInterface {
    protected zab(String string2) {
        this.attachInterface(this, string2);
    }

    public final IBinder asBinder() {
        return this;
    }

    public final boolean onTransact(int n, Parcel parcel, Parcel parcel2, int n2) throws RemoteException {
        if (n > 0xFFFFFF) {
            if (super.onTransact(n, parcel, parcel2, n2)) {
                return true;
            }
        } else {
            parcel.enforceInterface(this.getInterfaceDescriptor());
        }
        return this.zaa(n, parcel, parcel2, n2);
    }

    protected boolean zaa(int n, Parcel parcel, Parcel parcel2, int n2) throws RemoteException {
        throw null;
    }
}

