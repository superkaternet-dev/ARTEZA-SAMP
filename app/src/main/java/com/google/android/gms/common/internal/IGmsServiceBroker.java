/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Binder
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package com.google.android.gms.common.internal;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.internal.GetServiceRequest;
import com.google.android.gms.common.internal.IGmsCallbacks;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.zzaa;
import com.google.android.gms.common.internal.zzaj;

public interface IGmsServiceBroker
extends IInterface {
    public void getService(IGmsCallbacks var1, GetServiceRequest var2) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IGmsServiceBroker {
        public Stub() {
            this.attachInterface(this, "com.google.android.gms.common.internal.IGmsServiceBroker");
        }

        public IBinder asBinder() {
            return this;
        }

        public final boolean onTransact(int n, Parcel object, Parcel parcel, int n2) throws RemoteException {
            Object object2;
            if (n > 0xFFFFFF) {
                return super.onTransact(n, object, parcel, n2);
            }
            object.enforceInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
            IBinder iBinder = object.readStrongBinder();
            Object var6_6 = null;
            object2 = iBinder == null ? null : ((object2 = iBinder.queryLocalInterface("com.google.android.gms.common.internal.IGmsCallbacks")) instanceof IGmsCallbacks ? (IGmsCallbacks)object2 : new zzaa(iBinder));
            if (n == 46) {
                object = object.readInt() != 0 ? (GetServiceRequest)GetServiceRequest.CREATOR.createFromParcel(object) : var6_6;
                this.getService((IGmsCallbacks)object2, (GetServiceRequest)object);
                Preconditions.checkNotNull(parcel);
                parcel.writeNoException();
                return true;
            }
            if (n == 47) {
                if (object.readInt() != 0) {
                    object = (zzaj)zzaj.CREATOR.createFromParcel(object);
                }
                throw new UnsupportedOperationException();
            }
            object.readInt();
            if (n != 4) {
                object.readString();
                switch (n) {
                    default: {
                        break;
                    }
                    case 34: {
                        object.readString();
                        break;
                    }
                    case 20: 
                    case 30: {
                        object.createStringArray();
                        object.readString();
                        if (object.readInt() == 0) break;
                        object = (Bundle)Bundle.CREATOR.createFromParcel(object);
                        break;
                    }
                    case 19: {
                        object.readStrongBinder();
                        if (object.readInt() == 0) break;
                        object = (Bundle)Bundle.CREATOR.createFromParcel(object);
                        break;
                    }
                    case 10: {
                        object.readString();
                        object.createStringArray();
                        break;
                    }
                    case 9: {
                        object.readString();
                        object.createStringArray();
                        object.readString();
                        object.readStrongBinder();
                        object.readString();
                        if (object.readInt() == 0) break;
                        object = (Bundle)Bundle.CREATOR.createFromParcel(object);
                        break;
                    }
                    case 2: 
                    case 5: 
                    case 6: 
                    case 7: 
                    case 8: 
                    case 11: 
                    case 12: 
                    case 13: 
                    case 14: 
                    case 15: 
                    case 16: 
                    case 17: 
                    case 18: 
                    case 23: 
                    case 25: 
                    case 27: 
                    case 37: 
                    case 38: 
                    case 41: 
                    case 43: {
                        if (object.readInt() == 0) break;
                        object = (Bundle)Bundle.CREATOR.createFromParcel(object);
                        break;
                    }
                    case 1: {
                        object.readString();
                        object.createStringArray();
                        object.readString();
                        if (object.readInt() == 0) break;
                        object = (Bundle)Bundle.CREATOR.createFromParcel(object);
                    }
                }
            }
            throw new UnsupportedOperationException();
        }
    }
}

