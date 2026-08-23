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
import com.google.android.gms.common.internal.GetServiceRequest;
import com.google.android.gms.common.internal.IGmsCallbacks;
import com.google.android.gms.common.internal.IGmsServiceBroker;
import com.google.android.gms.common.internal.zzm;

final class zzac
implements IGmsServiceBroker {
    private final IBinder zza;

    zzac(IBinder iBinder) {
        this.zza = iBinder;
    }

    public final IBinder asBinder() {
        return this.zza;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final void getService(IGmsCallbacks iGmsCallbacks, GetServiceRequest getServiceRequest) throws RemoteException {
        Parcel parcel = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
            parcel.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
            iGmsCallbacks = iGmsCallbacks != null ? iGmsCallbacks.asBinder() : null;
            parcel.writeStrongBinder((IBinder)iGmsCallbacks);
            if (getServiceRequest != null) {
                parcel.writeInt(1);
                zzm.zza(getServiceRequest, parcel, 0);
            } else {
                parcel.writeInt(0);
            }
            this.zza.transact(46, parcel, parcel2, 0);
            parcel2.readException();
            return;
        }
        finally {
            parcel2.recycle();
            parcel.recycle();
        }
    }
}

