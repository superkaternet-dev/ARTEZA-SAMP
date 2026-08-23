/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.content.Intent
 *  android.os.IBinder
 */
package com.liulishuo.filedownloader.services;

import android.app.Notification;
import android.content.Intent;
import android.os.IBinder;
import com.liulishuo.filedownloader.FileDownloadServiceProxy;
import com.liulishuo.filedownloader.i.IFileDownloadIPCCallback;
import com.liulishuo.filedownloader.i.IFileDownloadIPCService;
import com.liulishuo.filedownloader.model.FileDownloadHeader;
import com.liulishuo.filedownloader.services.FileDownloadManager;
import com.liulishuo.filedownloader.services.FileDownloadService;
import com.liulishuo.filedownloader.services.IFileDownloadServiceHandler;
import java.lang.ref.WeakReference;

public class FDServiceSharedHandler
extends IFileDownloadIPCService.Stub
implements IFileDownloadServiceHandler {
    private final FileDownloadManager downloadManager;
    private final WeakReference<FileDownloadService> wService;

    FDServiceSharedHandler(WeakReference<FileDownloadService> weakReference, FileDownloadManager fileDownloadManager) {
        this.wService = weakReference;
        this.downloadManager = fileDownloadManager;
    }

    @Override
    public boolean checkDownloading(String string2, String string3) {
        return this.downloadManager.isDownloading(string2, string3);
    }

    @Override
    public void clearAllTaskData() {
        this.downloadManager.clearAllTaskData();
    }

    @Override
    public boolean clearTaskData(int n) {
        return this.downloadManager.clearTaskData(n);
    }

    @Override
    public long getSofar(int n) {
        return this.downloadManager.getSoFar(n);
    }

    @Override
    public byte getStatus(int n) {
        return this.downloadManager.getStatus(n);
    }

    @Override
    public long getTotal(int n) {
        return this.downloadManager.getTotal(n);
    }

    @Override
    public boolean isIdle() {
        return this.downloadManager.isIdle();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        FileDownloadServiceProxy.getConnectionListener().onDisconnected();
    }

    @Override
    public void onStartCommand(Intent intent, int n, int n2) {
        FileDownloadServiceProxy.getConnectionListener().onConnected(this);
    }

    @Override
    public boolean pause(int n) {
        return this.downloadManager.pause(n);
    }

    @Override
    public void pauseAllTasks() {
        this.downloadManager.pauseAll();
    }

    @Override
    public void registerCallback(IFileDownloadIPCCallback iFileDownloadIPCCallback) {
    }

    @Override
    public boolean setMaxNetworkThreadCount(int n) {
        return this.downloadManager.setMaxNetworkThreadCount(n);
    }

    @Override
    public void start(String string2, String string3, boolean bl, int n, int n2, int n3, boolean bl2, FileDownloadHeader fileDownloadHeader, boolean bl3) {
        this.downloadManager.start(string2, string3, bl, n, n2, n3, bl2, fileDownloadHeader, bl3);
    }

    @Override
    public void startForeground(int n, Notification notification) {
        WeakReference<FileDownloadService> weakReference = this.wService;
        if (weakReference != null && weakReference.get() != null) {
            ((FileDownloadService)((Object)this.wService.get())).startForeground(n, notification);
        }
    }

    @Override
    public void stopForeground(boolean bl) {
        WeakReference<FileDownloadService> weakReference = this.wService;
        if (weakReference != null && weakReference.get() != null) {
            ((FileDownloadService)((Object)this.wService.get())).stopForeground(bl);
        }
    }

    @Override
    public void unregisterCallback(IFileDownloadIPCCallback iFileDownloadIPCCallback) {
    }

    public static interface FileDownloadServiceSharedConnection {
        public void onConnected(FDServiceSharedHandler var1);

        public void onDisconnected();
    }
}

