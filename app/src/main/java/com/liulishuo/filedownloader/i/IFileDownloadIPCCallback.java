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
package com.liulishuo.filedownloader.i;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.liulishuo.filedownloader.message.MessageSnapshot;

public interface IFileDownloadIPCCallback
extends IInterface {
    public void callback(MessageSnapshot var1) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IFileDownloadIPCCallback {
        private static final String DESCRIPTOR = "com.liulishuo.filedownloader.i.IFileDownloadIPCCallback";
        static final int TRANSACTION_callback = 1;

        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        public static IFileDownloadIPCCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (iInterface != null && iInterface instanceof IFileDownloadIPCCallback) {
                return (IFileDownloadIPCCallback)iInterface;
            }
            return new Proxy(iBinder);
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
            object = object.readInt() != 0 ? (MessageSnapshot)MessageSnapshot.CREATOR.createFromParcel(object) : null;
            this.callback((MessageSnapshot)object);
            return true;
        }

        private static class Proxy
        implements IFileDownloadIPCCallback {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override
            public void callback(MessageSnapshot messageSnapshot) throws RemoteException {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (messageSnapshot != null) {
                        parcel.writeInt(1);
                        messageSnapshot.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    this.mRemote.transact(1, parcel, null, 1);
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }
        }
    }
}

