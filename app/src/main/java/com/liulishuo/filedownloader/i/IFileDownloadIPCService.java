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
package com.liulishuo.filedownloader.i;

import android.app.Notification;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.liulishuo.filedownloader.i.IFileDownloadIPCCallback;
import com.liulishuo.filedownloader.model.FileDownloadHeader;

public interface IFileDownloadIPCService
extends IInterface {
    public boolean checkDownloading(String var1, String var2) throws RemoteException;

    public void clearAllTaskData() throws RemoteException;

    public boolean clearTaskData(int var1) throws RemoteException;

    public long getSofar(int var1) throws RemoteException;

    public byte getStatus(int var1) throws RemoteException;

    public long getTotal(int var1) throws RemoteException;

    public boolean isIdle() throws RemoteException;

    public boolean pause(int var1) throws RemoteException;

    public void pauseAllTasks() throws RemoteException;

    public void registerCallback(IFileDownloadIPCCallback var1) throws RemoteException;

    public boolean setMaxNetworkThreadCount(int var1) throws RemoteException;

    public void start(String var1, String var2, boolean var3, int var4, int var5, int var6, boolean var7, FileDownloadHeader var8, boolean var9) throws RemoteException;

    public void startForeground(int var1, Notification var2) throws RemoteException;

    public void stopForeground(boolean var1) throws RemoteException;

    public void unregisterCallback(IFileDownloadIPCCallback var1) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IFileDownloadIPCService {
        private static final String DESCRIPTOR = "com.liulishuo.filedownloader.i.IFileDownloadIPCService";
        static final int TRANSACTION_checkDownloading = 3;
        static final int TRANSACTION_clearAllTaskData = 15;
        static final int TRANSACTION_clearTaskData = 14;
        static final int TRANSACTION_getSofar = 8;
        static final int TRANSACTION_getStatus = 10;
        static final int TRANSACTION_getTotal = 9;
        static final int TRANSACTION_isIdle = 11;
        static final int TRANSACTION_pause = 5;
        static final int TRANSACTION_pauseAllTasks = 6;
        static final int TRANSACTION_registerCallback = 1;
        static final int TRANSACTION_setMaxNetworkThreadCount = 7;
        static final int TRANSACTION_start = 4;
        static final int TRANSACTION_startForeground = 12;
        static final int TRANSACTION_stopForeground = 13;
        static final int TRANSACTION_unregisterCallback = 2;

        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        public static IFileDownloadIPCService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (iInterface != null && iInterface instanceof IFileDownloadIPCService) {
                return (IFileDownloadIPCService)iInterface;
            }
            return new Proxy(iBinder);
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int n, Parcel object, Parcel parcel, int n2) throws RemoteException {
            boolean bl = false;
            switch (n) {
                default: {
                    return super.onTransact(n, object, parcel, n2);
                }
                case 1598968902: {
                    parcel.writeString(DESCRIPTOR);
                    return true;
                }
                case 15: {
                    object.enforceInterface(DESCRIPTOR);
                    this.clearAllTaskData();
                    parcel.writeNoException();
                    return true;
                }
                case 14: {
                    object.enforceInterface(DESCRIPTOR);
                    n = this.clearTaskData(object.readInt()) ? 1 : 0;
                    parcel.writeNoException();
                    parcel.writeInt(n);
                    return true;
                }
                case 13: {
                    object.enforceInterface(DESCRIPTOR);
                    if (object.readInt() != 0) {
                        bl = true;
                    }
                    this.stopForeground(bl);
                    return true;
                }
                case 12: {
                    object.enforceInterface(DESCRIPTOR);
                    n = object.readInt();
                    object = object.readInt() != 0 ? (Notification)Notification.CREATOR.createFromParcel(object) : null;
                    this.startForeground(n, (Notification)object);
                    return true;
                }
                case 11: {
                    object.enforceInterface(DESCRIPTOR);
                    n = this.isIdle() ? 1 : 0;
                    parcel.writeNoException();
                    parcel.writeInt(n);
                    return true;
                }
                case 10: {
                    object.enforceInterface(DESCRIPTOR);
                    byte by = this.getStatus(object.readInt());
                    parcel.writeNoException();
                    parcel.writeByte(by);
                    return true;
                }
                case 9: {
                    object.enforceInterface(DESCRIPTOR);
                    long l = this.getTotal(object.readInt());
                    parcel.writeNoException();
                    parcel.writeLong(l);
                    return true;
                }
                case 8: {
                    object.enforceInterface(DESCRIPTOR);
                    long l = this.getSofar(object.readInt());
                    parcel.writeNoException();
                    parcel.writeLong(l);
                    return true;
                }
                case 7: {
                    object.enforceInterface(DESCRIPTOR);
                    n = this.setMaxNetworkThreadCount(object.readInt()) ? 1 : 0;
                    parcel.writeNoException();
                    parcel.writeInt(n);
                    return true;
                }
                case 6: {
                    object.enforceInterface(DESCRIPTOR);
                    this.pauseAllTasks();
                    parcel.writeNoException();
                    return true;
                }
                case 5: {
                    object.enforceInterface(DESCRIPTOR);
                    n = this.pause(object.readInt()) ? 1 : 0;
                    parcel.writeNoException();
                    parcel.writeInt(n);
                    return true;
                }
                case 4: {
                    object.enforceInterface(DESCRIPTOR);
                    String string2 = object.readString();
                    String string3 = object.readString();
                    bl = object.readInt() != 0;
                    int n3 = object.readInt();
                    n = object.readInt();
                    n2 = object.readInt();
                    boolean bl2 = object.readInt() != 0;
                    FileDownloadHeader fileDownloadHeader = object.readInt() != 0 ? (FileDownloadHeader)FileDownloadHeader.CREATOR.createFromParcel(object) : null;
                    boolean bl3 = object.readInt() != 0;
                    this.start(string2, string3, bl, n3, n, n2, bl2, fileDownloadHeader, bl3);
                    parcel.writeNoException();
                    return true;
                }
                case 3: {
                    object.enforceInterface(DESCRIPTOR);
                    n = this.checkDownloading(object.readString(), object.readString()) ? 1 : 0;
                    parcel.writeNoException();
                    parcel.writeInt(n);
                    return true;
                }
                case 2: {
                    object.enforceInterface(DESCRIPTOR);
                    this.unregisterCallback(IFileDownloadIPCCallback.Stub.asInterface(object.readStrongBinder()));
                    return true;
                }
                case 1: 
            }
            object.enforceInterface(DESCRIPTOR);
            this.registerCallback(IFileDownloadIPCCallback.Stub.asInterface(object.readStrongBinder()));
            return true;
        }

        private static class Proxy
        implements IFileDownloadIPCService {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override
            public boolean checkDownloading(String string2, String string3) throws RemoteException {
                boolean bl;
                Parcel parcel;
                Parcel parcel2;
                block2: {
                    parcel2 = Parcel.obtain();
                    parcel = Parcel.obtain();
                    bl = false;
                    try {
                        parcel2.writeInterfaceToken(Stub.DESCRIPTOR);
                        parcel2.writeString(string2);
                        parcel2.writeString(string3);
                        this.mRemote.transact(3, parcel2, parcel, 0);
                        parcel.readException();
                        int n = parcel.readInt();
                        if (n == 0) break block2;
                        bl = true;
                    }
                    catch (Throwable throwable) {
                        parcel.recycle();
                        parcel2.recycle();
                        throw throwable;
                    }
                }
                parcel.recycle();
                parcel2.recycle();
                return bl;
            }

            @Override
            public void clearAllTaskData() throws RemoteException {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(15, parcel, parcel2, 0);
                    parcel2.readException();
                    return;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            @Override
            public boolean clearTaskData(int n) throws RemoteException {
                boolean bl;
                Parcel parcel;
                Parcel parcel2;
                block2: {
                    parcel2 = Parcel.obtain();
                    parcel = Parcel.obtain();
                    bl = false;
                    try {
                        parcel2.writeInterfaceToken(Stub.DESCRIPTOR);
                        parcel2.writeInt(n);
                        this.mRemote.transact(14, parcel2, parcel, 0);
                        parcel.readException();
                        n = parcel.readInt();
                        if (n == 0) break block2;
                        bl = true;
                    }
                    catch (Throwable throwable) {
                        parcel.recycle();
                        parcel2.recycle();
                        throw throwable;
                    }
                }
                parcel.recycle();
                parcel2.recycle();
                return bl;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override
            public long getSofar(int n) throws RemoteException {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeInt(n);
                    this.mRemote.transact(8, parcel, parcel2, 0);
                    parcel2.readException();
                    long l = parcel2.readLong();
                    return l;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            @Override
            public byte getStatus(int n) throws RemoteException {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeInt(n);
                    this.mRemote.transact(10, parcel, parcel2, 0);
                    parcel2.readException();
                    byte by = parcel2.readByte();
                    return by;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            @Override
            public long getTotal(int n) throws RemoteException {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeInt(n);
                    this.mRemote.transact(9, parcel, parcel2, 0);
                    parcel2.readException();
                    long l = parcel2.readLong();
                    return l;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            @Override
            public boolean isIdle() throws RemoteException {
                boolean bl;
                Parcel parcel;
                Parcel parcel2;
                block2: {
                    parcel2 = Parcel.obtain();
                    parcel = Parcel.obtain();
                    bl = false;
                    try {
                        parcel2.writeInterfaceToken(Stub.DESCRIPTOR);
                        this.mRemote.transact(11, parcel2, parcel, 0);
                        parcel.readException();
                        int n = parcel.readInt();
                        if (n == 0) break block2;
                        bl = true;
                    }
                    catch (Throwable throwable) {
                        parcel.recycle();
                        parcel2.recycle();
                        throw throwable;
                    }
                }
                parcel.recycle();
                parcel2.recycle();
                return bl;
            }

            @Override
            public boolean pause(int n) throws RemoteException {
                boolean bl;
                Parcel parcel;
                Parcel parcel2;
                block2: {
                    parcel2 = Parcel.obtain();
                    parcel = Parcel.obtain();
                    bl = false;
                    try {
                        parcel2.writeInterfaceToken(Stub.DESCRIPTOR);
                        parcel2.writeInt(n);
                        this.mRemote.transact(5, parcel2, parcel, 0);
                        parcel.readException();
                        n = parcel.readInt();
                        if (n == 0) break block2;
                        bl = true;
                    }
                    catch (Throwable throwable) {
                        parcel.recycle();
                        parcel2.recycle();
                        throw throwable;
                    }
                }
                parcel.recycle();
                parcel2.recycle();
                return bl;
            }

            @Override
            public void pauseAllTasks() throws RemoteException {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, parcel, parcel2, 0);
                    parcel2.readException();
                    return;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void registerCallback(IFileDownloadIPCCallback iFileDownloadIPCCallback) throws RemoteException {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    iFileDownloadIPCCallback = iFileDownloadIPCCallback != null ? iFileDownloadIPCCallback.asBinder() : null;
                    parcel.writeStrongBinder((IBinder)iFileDownloadIPCCallback);
                    this.mRemote.transact(1, parcel, null, 1);
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }

            @Override
            public boolean setMaxNetworkThreadCount(int n) throws RemoteException {
                boolean bl;
                Parcel parcel;
                Parcel parcel2;
                block2: {
                    parcel2 = Parcel.obtain();
                    parcel = Parcel.obtain();
                    bl = false;
                    try {
                        parcel2.writeInterfaceToken(Stub.DESCRIPTOR);
                        parcel2.writeInt(n);
                        this.mRemote.transact(7, parcel2, parcel, 0);
                        parcel.readException();
                        n = parcel.readInt();
                        if (n == 0) break block2;
                        bl = true;
                    }
                    catch (Throwable throwable) {
                        parcel.recycle();
                        parcel2.recycle();
                        throw throwable;
                    }
                }
                parcel.recycle();
                parcel2.recycle();
                return bl;
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void start(String string2, String string3, boolean bl, int n, int n2, int n3, boolean bl2, FileDownloadHeader fileDownloadHeader, boolean bl3) throws RemoteException {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string2);
                    parcel.writeString(string3);
                    int n4 = 1;
                    int n5 = bl ? 1 : 0;
                    parcel.writeInt(n5);
                    parcel.writeInt(n);
                    parcel.writeInt(n2);
                    parcel.writeInt(n3);
                    n = bl2 ? 1 : 0;
                    parcel.writeInt(n);
                    if (fileDownloadHeader != null) {
                        parcel.writeInt(1);
                        fileDownloadHeader.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    n = bl3 ? n4 : 0;
                    parcel.writeInt(n);
                    this.mRemote.transact(4, parcel, parcel2, 0);
                    parcel2.readException();
                    return;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            @Override
            public void startForeground(int n, Notification notification) throws RemoteException {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeInt(n);
                    if (notification != null) {
                        parcel.writeInt(1);
                        notification.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    this.mRemote.transact(12, parcel, null, 1);
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }

            @Override
            public void stopForeground(boolean bl) throws RemoteException {
                Parcel parcel = Parcel.obtain();
                parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                int n = bl ? 1 : 0;
                try {
                    parcel.writeInt(n);
                    this.mRemote.transact(13, parcel, null, 1);
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void unregisterCallback(IFileDownloadIPCCallback iFileDownloadIPCCallback) throws RemoteException {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    iFileDownloadIPCCallback = iFileDownloadIPCCallback != null ? iFileDownloadIPCCallback.asBinder() : null;
                    parcel.writeStrongBinder((IBinder)iFileDownloadIPCCallback);
                    this.mRemote.transact(2, parcel, null, 1);
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }
        }
    }
}

