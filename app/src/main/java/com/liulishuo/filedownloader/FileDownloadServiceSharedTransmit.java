/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Build$VERSION
 */
package com.liulishuo.filedownloader;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.liulishuo.filedownloader.FileDownloadEventPool;
import com.liulishuo.filedownloader.IFileDownloadServiceProxy;
import com.liulishuo.filedownloader.event.DownloadServiceConnectChangedEvent;
import com.liulishuo.filedownloader.model.FileDownloadHeader;
import com.liulishuo.filedownloader.services.FDServiceSharedHandler;
import com.liulishuo.filedownloader.services.FileDownloadService;
import com.liulishuo.filedownloader.util.DownloadServiceNotConnectedHelper;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.util.ArrayList;
import java.util.List;

class FileDownloadServiceSharedTransmit
implements IFileDownloadServiceProxy,
FDServiceSharedHandler.FileDownloadServiceSharedConnection {
    private static final Class<?> SERVICE_CLASS = FileDownloadService.SharedMainProcessService.class;
    private final ArrayList<Runnable> connectedRunnableList = new ArrayList();
    private FDServiceSharedHandler handler;
    private boolean runServiceForeground = false;

    FileDownloadServiceSharedTransmit() {
    }

    @Override
    public void bindStartByContext(Context context) {
        this.bindStartByContext(context, null);
    }

    @Override
    public void bindStartByContext(Context context, Runnable runnable) {
        boolean bl;
        if (runnable != null && !this.connectedRunnableList.contains(runnable)) {
            this.connectedRunnableList.add(runnable);
        }
        runnable = new Intent(context, SERVICE_CLASS);
        this.runServiceForeground = bl = FileDownloadUtils.needMakeServiceForeground(context);
        runnable.putExtra("is_foreground", bl);
        if (this.runServiceForeground) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "start foreground service", new Object[0]);
            }
            if (Build.VERSION.SDK_INT >= 26) {
                context.startForegroundService((Intent)runnable);
            }
        } else {
            context.startService((Intent)runnable);
        }
    }

    @Override
    public void clearAllTaskData() {
        if (!this.isConnected()) {
            DownloadServiceNotConnectedHelper.clearAllTaskData();
            return;
        }
        this.handler.clearAllTaskData();
    }

    @Override
    public boolean clearTaskData(int n) {
        if (!this.isConnected()) {
            return DownloadServiceNotConnectedHelper.clearTaskData(n);
        }
        return this.handler.clearTaskData(n);
    }

    @Override
    public long getSofar(int n) {
        if (!this.isConnected()) {
            return DownloadServiceNotConnectedHelper.getSofar(n);
        }
        return this.handler.getSofar(n);
    }

    @Override
    public byte getStatus(int n) {
        if (!this.isConnected()) {
            return DownloadServiceNotConnectedHelper.getStatus(n);
        }
        return this.handler.getStatus(n);
    }

    @Override
    public long getTotal(int n) {
        if (!this.isConnected()) {
            return DownloadServiceNotConnectedHelper.getTotal(n);
        }
        return this.handler.getTotal(n);
    }

    @Override
    public boolean isConnected() {
        boolean bl = this.handler != null;
        return bl;
    }

    @Override
    public boolean isDownloading(String string2, String string3) {
        if (!this.isConnected()) {
            return DownloadServiceNotConnectedHelper.isDownloading(string2, string3);
        }
        return this.handler.checkDownloading(string2, string3);
    }

    @Override
    public boolean isIdle() {
        if (!this.isConnected()) {
            return DownloadServiceNotConnectedHelper.isIdle();
        }
        return this.handler.isIdle();
    }

    @Override
    public boolean isRunServiceForeground() {
        return this.runServiceForeground;
    }

    @Override
    public void onConnected(FDServiceSharedHandler iterator2) {
        this.handler = iterator2;
        iterator2 = (List)this.connectedRunnableList.clone();
        this.connectedRunnableList.clear();
        iterator2 = iterator2.iterator();
        while (iterator2.hasNext()) {
            ((Runnable)iterator2.next()).run();
        }
        FileDownloadEventPool.getImpl().asyncPublishInNewThread(new DownloadServiceConnectChangedEvent(DownloadServiceConnectChangedEvent.ConnectStatus.connected, SERVICE_CLASS));
    }

    @Override
    public void onDisconnected() {
        this.handler = null;
        FileDownloadEventPool.getImpl().asyncPublishInNewThread(new DownloadServiceConnectChangedEvent(DownloadServiceConnectChangedEvent.ConnectStatus.disconnected, SERVICE_CLASS));
    }

    @Override
    public boolean pause(int n) {
        if (!this.isConnected()) {
            return DownloadServiceNotConnectedHelper.pause(n);
        }
        return this.handler.pause(n);
    }

    @Override
    public void pauseAllTasks() {
        if (!this.isConnected()) {
            DownloadServiceNotConnectedHelper.pauseAllTasks();
            return;
        }
        this.handler.pauseAllTasks();
    }

    @Override
    public boolean setMaxNetworkThreadCount(int n) {
        if (!this.isConnected()) {
            return DownloadServiceNotConnectedHelper.setMaxNetworkThreadCount(n);
        }
        return this.handler.setMaxNetworkThreadCount(n);
    }

    @Override
    public boolean start(String string2, String string3, boolean bl, int n, int n2, int n3, boolean bl2, FileDownloadHeader fileDownloadHeader, boolean bl3) {
        if (!this.isConnected()) {
            return DownloadServiceNotConnectedHelper.start(string2, string3, bl);
        }
        this.handler.start(string2, string3, bl, n, n2, n3, bl2, fileDownloadHeader, bl3);
        return true;
    }

    @Override
    public void startForeground(int n, Notification notification) {
        if (!this.isConnected()) {
            DownloadServiceNotConnectedHelper.startForeground(n, notification);
            return;
        }
        this.handler.startForeground(n, notification);
    }

    @Override
    public void stopForeground(boolean bl) {
        if (!this.isConnected()) {
            DownloadServiceNotConnectedHelper.stopForeground(bl);
            return;
        }
        this.handler.stopForeground(bl);
        this.runServiceForeground = false;
    }

    @Override
    public void unbindByContext(Context context) {
        context.stopService(new Intent(context, SERVICE_CLASS));
        this.handler = null;
    }
}

