/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.internal.zzz;
import com.google.android.gms.internal.common.zzb;
import com.google.android.gms.internal.common.zzc;

public abstract class zzy
extends zzb
implements zzz {
    public zzy() {
        super("com.google.android.gms.common.internal.ICertData");
    }

    public static zzz zzg(IBinder iBinder) {
        IInterface iInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.ICertData");
        if (iInterface instanceof zzz) {
            return (zzz)iInterface;
        }
        return new zzx(iBinder);
    }

    @Override
    protected final boolean zza(int n, Parcel object, Parcel parcel, int n2) throws RemoteException {
        switch (n) {
            default: {
                return false;
            }
            case 2: {
                n = this.zzc();
                parcel.writeNoException();
                parcel.writeInt(n);
                break;
            }
            case 1: {
                object = this.zzd();
                parcel.writeNoException();
                zzc.zze(parcel, (IInterface)object);
            }
        }
        return true;
    }
}

