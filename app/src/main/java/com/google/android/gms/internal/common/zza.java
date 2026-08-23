/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package com.google.android.gms.internal.common;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public class zza
implements IInterface {
    private final IBinder zza;
    private final String zzb;

    protected zza(IBinder iBinder, String string2) {
        this.zza = iBinder;
        this.zzb = string2;
    }

    public final IBinder asBinder() {
        return this.zza;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected final Parcel zzB(int n, Parcel parcel) throws RemoteException {
        Throwable throwable2222222;
        block4: {
            Parcel parcel2 = Parcel.obtain();
            this.zza.transact(n, parcel, parcel2, 0);
            parcel2.readException();
            {
                catch (Throwable throwable2222222) {
                    break block4;
                }
                catch (RuntimeException runtimeException) {}
                {
                    parcel2.recycle();
                    throw runtimeException;
                }
            }
            parcel.recycle();
            return parcel2;
        }
        parcel.recycle();
        throw throwable2222222;
    }

    protected final void zzC(int n, Parcel parcel) throws RemoteException {
        Parcel parcel2 = Parcel.obtain();
        try {
            this.zza.transact(1, parcel, parcel2, 0);
            parcel2.readException();
            return;
        }
        finally {
            parcel.recycle();
            parcel2.recycle();
        }
    }

    protected final void zzD(int n, Parcel parcel) throws RemoteException {
        try {
            this.zza.transact(2, parcel, null, 1);
            return;
        }
        finally {
            parcel.recycle();
        }
    }

    protected final Parcel zza() {
        Parcel parcel = Parcel.obtain();
        parcel.writeInterfaceToken(this.zzb);
        return parcel;
    }
}

