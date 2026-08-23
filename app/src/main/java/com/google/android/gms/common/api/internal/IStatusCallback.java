/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package com.google.android.gms.common.api.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.zaby;
import com.google.android.gms.internal.base.zab;
import com.google.android.gms.internal.base.zac;

public interface IStatusCallback
extends IInterface {
    public void onResult(Status var1) throws RemoteException;

    public static abstract class Stub
    extends zab
    implements IStatusCallback {
        public Stub() {
            super("com.google.android.gms.common.api.internal.IStatusCallback");
        }

        public static IStatusCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterface = iBinder.queryLocalInterface("com.google.android.gms.common.api.internal.IStatusCallback");
            if (iInterface instanceof IStatusCallback) {
                return (IStatusCallback)iInterface;
            }
            return new zaby(iBinder);
        }

        @Override
        protected final boolean zaa(int n, Parcel parcel, Parcel parcel2, int n2) throws RemoteException {
            if (n == 1) {
                this.onResult(zac.zaa(parcel, Status.CREATOR));
                return true;
            }
            return false;
        }
    }
}

