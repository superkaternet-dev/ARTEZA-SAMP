/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.os.Binder
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package android.support.v4.app;

import android.app.Notification;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INotificationSideChannel
extends IInterface {
    public void cancel(String var1, int var2, String var3) throws RemoteException;

    public void cancelAll(String var1) throws RemoteException;

    public void notify(String var1, int var2, String var3, Notification var4) throws RemoteException;

    public static class Default
    implements INotificationSideChannel {
        public IBinder asBinder() {
            return null;
        }

        @Override
        public void cancel(String string2, int n, String string3) throws RemoteException {
        }

        @Override
        public void cancelAll(String string2) throws RemoteException {
        }

        @Override
        public void notify(String string2, int n, String string3, Notification notification) throws RemoteException {
        }
    }

    public static abstract class Stub
    extends Binder
    implements INotificationSideChannel {
        private static final String DESCRIPTOR = "android.support.v4.app.INotificationSideChannel";
        static final int TRANSACTION_cancel = 2;
        static final int TRANSACTION_cancelAll = 3;
        static final int TRANSACTION_notify = 1;

        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        public static INotificationSideChannel asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (iInterface != null && iInterface instanceof INotificationSideChannel) {
                return (INotificationSideChannel)iInterface;
            }
            return new Proxy(iBinder);
        }

        public static INotificationSideChannel getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public static boolean setDefaultImpl(INotificationSideChannel iNotificationSideChannel) {
            if (Proxy.sDefaultImpl == null && iNotificationSideChannel != null) {
                Proxy.sDefaultImpl = iNotificationSideChannel;
                return true;
            }
            return false;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int n, Parcel object, Parcel object2, int n2) throws RemoteException {
            switch (n) {
                default: {
                    return super.onTransact(n, object, object2, n2);
                }
                case 1598968902: {
                    object2.writeString(DESCRIPTOR);
                    return true;
                }
                case 3: {
                    object.enforceInterface(DESCRIPTOR);
                    this.cancelAll(object.readString());
                    return true;
                }
                case 2: {
                    object.enforceInterface(DESCRIPTOR);
                    this.cancel(object.readString(), object.readInt(), object.readString());
                    return true;
                }
                case 1: 
            }
            object.enforceInterface(DESCRIPTOR);
            String string2 = object.readString();
            n = object.readInt();
            object2 = object.readString();
            object = object.readInt() != 0 ? (Notification)Notification.CREATOR.createFromParcel(object) : null;
            this.notify(string2, n, (String)object2, (Notification)object);
            return true;
        }

        private static class Proxy
        implements INotificationSideChannel {
            public static INotificationSideChannel sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override
            public void cancel(String string2, int n, String string3) throws RemoteException {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string2);
                    parcel.writeInt(n);
                    parcel.writeString(string3);
                    if (!this.mRemote.transact(2, parcel, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cancel(string2, n, string3);
                        return;
                    }
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }

            @Override
            public void cancelAll(String string2) throws RemoteException {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string2);
                    if (!this.mRemote.transact(3, parcel, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cancelAll(string2);
                        return;
                    }
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override
            public void notify(String string2, int n, String string3, Notification notification) throws RemoteException {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string2);
                    parcel.writeInt(n);
                    parcel.writeString(string3);
                    if (notification != null) {
                        parcel.writeInt(1);
                        notification.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    if (!this.mRemote.transact(1, parcel, null, 1) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notify(string2, n, string3, notification);
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

