/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.RemoteException
 */
package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.common.internal.zzv;
import com.google.android.gms.internal.common.zzb;
import com.google.android.gms.internal.common.zzc;

public interface IAccountAccessor
extends IInterface {
    public Account zzb() throws RemoteException;

    public static abstract class Stub
    extends zzb
    implements IAccountAccessor {
        public Stub() {
            super("com.google.android.gms.common.internal.IAccountAccessor");
        }

        public static IAccountAccessor asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.IAccountAccessor");
            if (iInterface instanceof IAccountAccessor) {
                return (IAccountAccessor)iInterface;
            }
            return new zzv(iBinder);
        }

        @Override
        protected final boolean zza(int n, Parcel parcel, Parcel parcel2, int n2) throws RemoteException {
            if (n == 2) {
                parcel = this.zzb();
                parcel2.writeNoException();
                zzc.zzd(parcel2, (Parcelable)parcel);
                return true;
            }
            return false;
        }
    }
}

