/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.os.IBinder
 *  android.os.RemoteException
 */
package com.liulishuo.filedownloader;

import android.app.Notification;
import android.os.IBinder;
import android.os.RemoteException;
import com.liulishuo.filedownloader.i.IFileDownloadIPCCallback;
import com.liulishuo.filedownloader.i.IFileDownloadIPCService;
import com.liulishuo.filedownloader.message.MessageSnapshot;
import com.liulishuo.filedownloader.message.MessageSnapshotFlow;
import com.liulishuo.filedownloader.model.FileDownloadHeader;
import com.liulishuo.filedownloader.services.BaseFileServiceUIGuard;
import com.liulishuo.filedownloader.services.FileDownloadService;
import com.liulishuo.filedownloader.util.DownloadServiceNotConnectedHelper;

class FileDownloadServiceUIGuard
extends BaseFileServiceUIGuard<FileDownloadServiceCallback, IFileDownloadIPCService> {
    FileDownloadServiceUIGuard() {
        super(FileDownloadService.SeparateProcessService.class);
    }

    @Override
    protected IFileDownloadIPCService asInterface(IBinder iBinder) {
        return IFileDownloadIPCService.Stub.asInterface(iBinder);
    }

    @Override
    public void clearAllTaskData() {
        if (!this.isConnected()) {
            DownloadServiceNotConnectedHelper.clearAllTaskData();
            return;
        }
        try {
            ((IFileDownloadIPCService)this.getService()).clearAllTaskData();
        }
        catch (RemoteException remoteException) {
            remoteException.printStackTrace();
        }
    }

    @Override
    public boolean clearTaskData(int n) {
        if (!this.isConnected()) {
            return DownloadServiceNotConnectedHelper.clearTaskData(n);
        }
        try {
            boolean bl = ((IFileDownloadIPCService)this.getService()).clearTaskData(n);
            return bl;
        }
        catch (RemoteException remoteException) {
            remoteException.printStackTrace();
            return false;
        }
    }

    @Override
    protected FileDownloadServiceCallback createCallback() {
        return new FileDownloadServiceCallback();
    }

    @Override
    public long getSofar(int n) {
        long l;
        if (!this.isConnected()) {
            return DownloadServiceNotConnectedHelper.getSofar(n);
        }
        long l2 = 0L;
        try {
            l = ((IFileDownloadIPCService)this.getService()).getSofar(n);
        }
        catch (RemoteException remoteException) {
            remoteException.printStackTrace();
            l = l2;
        }
        return l;
    }

    @Override
    public byte getStatus(int n) {
        byte by;
        if (!this.isConnected()) {
            return DownloadServiceNotConnectedHelper.getStatus(n);
        }
        byte by2 = 0;
        try {
            by = ((IFileDownloadIPCService)this.getService()).getStatus(n);
        }
        catch (RemoteException remoteException) {
            remoteException.printStackTrace();
            by = by2;
        }
        return by;
    }

    @Override
    public long getTotal(int n) {
        if (!this.isConnected()) {
            return DownloadServiceNotConnectedHelper.getTotal(n);
        }
        long l = 0L;
        try {
            long l2;
            l = l2 = ((IFileDownloadIPCService)this.getService()).getTotal(n);
        }
        catch (RemoteException remoteException) {
            remoteException.printStackTrace();
        }
        return l;
    }

    @Override
    public boolean isDownloading(String string2, String string3) {
        if (!this.isConnected()) {
            return DownloadServiceNotConnectedHelper.isDownloading(string2, string3);
        }
        try {
            boolean bl = ((IFileDownloadIPCService)this.getService()).checkDownloading(string2, string3);
            return bl;
        }
        catch (RemoteException remoteException) {
            remoteException.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isIdle() {
        if (!this.isConnected()) {
            return DownloadServiceNotConnectedHelper.isIdle();
        }
        try {
            ((IFileDownloadIPCService)this.getService()).isIdle();
        }
        catch (RemoteException remoteException) {
            remoteException.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean pause(int n) {
        if (!this.isConnected()) {
            return DownloadServiceNotConnectedHelper.pause(n);
        }
        try {
            boolean bl = ((IFileDownloadIPCService)this.getService()).pause(n);
            return bl;
        }
        catch (RemoteException remoteException) {
            remoteException.printStackTrace();
            return false;
        }
    }

    @Override
    public void pauseAllTasks() {
        if (!this.isConnected()) {
            DownloadServiceNotConnectedHelper.pauseAllTasks();
            return;
        }
        try {
            ((IFileDownloadIPCService)this.getService()).pauseAllTasks();
        }
        catch (RemoteException remoteException) {
            remoteException.printStackTrace();
        }
    }

    @Override
    protected void registerCallback(IFileDownloadIPCService iFileDownloadIPCService, FileDownloadServiceCallback fileDownloadServiceCallback) throws RemoteException {
        iFileDownloadIPCService.registerCallback(fileDownloadServiceCallback);
    }

    @Override
    public boolean setMaxNetworkThreadCount(int n) {
        if (!this.isConnected()) {
            return DownloadServiceNotConnectedHelper.setMaxNetworkThreadCount(n);
        }
        try {
            boolean bl = ((IFileDownloadIPCService)this.getService()).setMaxNetworkThreadCount(n);
            return bl;
        }
        catch (RemoteException remoteException) {
            remoteException.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean start(String string2, String string3, boolean bl, int n, int n2, int n3, boolean bl2, FileDownloadHeader fileDownloadHeader, boolean bl3) {
        if (!this.isConnected()) {
            return DownloadServiceNotConnectedHelper.start(string2, string3, bl);
        }
        try {
            ((IFileDownloadIPCService)this.getService()).start(string2, string3, bl, n, n2, n3, bl2, fileDownloadHeader, bl3);
            return true;
        }
        catch (RemoteException remoteException) {
            remoteException.printStackTrace();
            return false;
        }
    }

    @Override
    public void startForeground(int n, Notification notification) {
        if (!this.isConnected()) {
            DownloadServiceNotConnectedHelper.startForeground(n, notification);
            return;
        }
        try {
            ((IFileDownloadIPCService)this.getService()).startForeground(n, notification);
        }
        catch (RemoteException remoteException) {
            remoteException.printStackTrace();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void stopForeground(boolean bl) {
        Throwable throwable2;
        block5: {
            if (!this.isConnected()) {
                DownloadServiceNotConnectedHelper.stopForeground(bl);
                return;
            }
            try {
                try {
                    ((IFileDownloadIPCService)this.getService()).stopForeground(bl);
                }
                catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
            }
            catch (Throwable throwable2) {
                break block5;
            }
            this.runServiceForeground = false;
            return;
        }
        this.runServiceForeground = false;
        throw throwable2;
    }

    @Override
    protected void unregisterCallback(IFileDownloadIPCService iFileDownloadIPCService, FileDownloadServiceCallback fileDownloadServiceCallback) throws RemoteException {
        iFileDownloadIPCService.unregisterCallback(fileDownloadServiceCallback);
    }

    protected static class FileDownloadServiceCallback
    extends IFileDownloadIPCCallback.Stub {
        protected FileDownloadServiceCallback() {
        }

        @Override
        public void callback(MessageSnapshot messageSnapshot) throws RemoteException {
            MessageSnapshotFlow.getImpl().inflow(messageSnapshot);
        }
    }
}

