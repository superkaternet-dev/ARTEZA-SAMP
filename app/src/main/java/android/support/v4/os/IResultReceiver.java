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
package android.support.v4.os;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IResultReceiver
extends IInterface {
    public void send(int var1, Bundle var2) throws RemoteException;

    public static class Default
    implements IResultReceiver {
        public IBinder asBinder() {
            return null;
        }

        @Override
        public void send(int n, Bundle bundle) throws RemoteException {
        }
    }

    public static abstract class Stub
    extends Binder
    implements IResultReceiver {
        private static final String DESCRIPTOR = "android.support.v4.os.IResultReceiver";
        static final int TRANSACTION_send = 1;

        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        public static IResultReceiver asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (iInterface != null && iInterface instanceof IResultReceiver) {
                return (IResultReceiver)iInterface;
            }
            return new Proxy(iBinder);
        }

        public static IResultReceiver getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public static boolean setDefaultImpl(IResultReceiver iResultReceiver) {
            if (Proxy.sDefaultImpl == null && iResultReceiver != null) {
                Proxy.sDefaultImpl = iResultReceiver;
                return true;
            }
            return false;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int n, Parcel object, Parcel parcel, int n2) throws RemoteException {
            switch (n) {
                default: {
                    return super.onTransact(n, object, parcel, n2);
                }
                case 1598968902: {
                    parcel.writeString(DESCRIPTOR);
                    return true;
                }
                case 1: 
            }
            object.enforceInterface(DESCRIPTOR);
            n = object.readInt();
            object = object.readInt() != 0 ? (Bundle)Bundle.CREATOR.createFromParcel(object) : null;
            this.send(n, (Bundle)object);
            return true;
        }

        private static class Proxy
        implements IResultReceiver {
            public static IResultReceiver sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override
            public void send(int n, Bundle bundle) throws RemoteException {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeInt(n);
                    if (bundle != null) {
                        parcel.writeInt(1);
                        bundle.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    if (!this.mRemote.transact(1, parcel, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().send(n, bundle);
                        return;
                    }
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }
        }
    }
}

