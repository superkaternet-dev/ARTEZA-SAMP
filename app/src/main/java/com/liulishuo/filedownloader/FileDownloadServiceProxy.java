/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.content.Context
 */
package com.liulishuo.filedownloader;

import android.app.Notification;
import android.content.Context;
import com.liulishuo.filedownloader.FileDownloadServiceSharedTransmit;
import com.liulishuo.filedownloader.FileDownloadServiceUIGuard;
import com.liulishuo.filedownloader.IFileDownloadServiceProxy;
import com.liulishuo.filedownloader.model.FileDownloadHeader;
import com.liulishuo.filedownloader.services.FDServiceSharedHandler;
import com.liulishuo.filedownloader.util.FileDownloadProperties;

public class FileDownloadServiceProxy
implements IFileDownloadServiceProxy {
    private final IFileDownloadServiceProxy handler;

    private FileDownloadServiceProxy() {
        IFileDownloadServiceProxy iFileDownloadServiceProxy = FileDownloadProperties.getImpl().processNonSeparate ? new FileDownloadServiceSharedTransmit() : new FileDownloadServiceUIGuard();
        this.handler = iFileDownloadServiceProxy;
    }

    public static FDServiceSharedHandler.FileDownloadServiceSharedConnection getConnectionListener() {
        if (FileDownloadServiceProxy.getImpl().handler instanceof FileDownloadServiceSharedTransmit) {
            return (FDServiceSharedHandler.FileDownloadServiceSharedConnection)((Object)FileDownloadServiceProxy.getImpl().handler);
        }
        return null;
    }

    public static FileDownloadServiceProxy getImpl() {
        return HolderClass.INSTANCE;
    }

    @Override
    public void bindStartByContext(Context context) {
        this.handler.bindStartByContext(context);
    }

    @Override
    public void bindStartByContext(Context context, Runnable runnable) {
        this.handler.bindStartByContext(context, runnable);
    }

    @Override
    public void clearAllTaskData() {
        this.handler.clearAllTaskData();
    }

    @Override
    public boolean clearTaskData(int n) {
        return this.handler.clearTaskData(n);
    }

    @Override
    public long getSofar(int n) {
        return this.handler.getSofar(n);
    }

    @Override
    public byte getStatus(int n) {
        return this.handler.getStatus(n);
    }

    @Override
    public long getTotal(int n) {
        return this.handler.getTotal(n);
    }

    @Override
    public boolean isConnected() {
        return this.handler.isConnected();
    }

    @Override
    public boolean isDownloading(String string2, String string3) {
        return this.handler.isDownloading(string2, string3);
    }

    @Override
    public boolean isIdle() {
        return this.handler.isIdle();
    }

    @Override
    public boolean isRunServiceForeground() {
        return this.handler.isRunServiceForeground();
    }

    @Override
    public boolean pause(int n) {
        return this.handler.pause(n);
    }

    @Override
    public void pauseAllTasks() {
        this.handler.pauseAllTasks();
    }

    @Override
    public boolean setMaxNetworkThreadCount(int n) {
        return this.handler.setMaxNetworkThreadCount(n);
    }

    @Override
    public boolean start(String string2, String string3, boolean bl, int n, int n2, int n3, boolean bl2, FileDownloadHeader fileDownloadHeader, boolean bl3) {
        return this.handler.start(string2, string3, bl, n, n2, n3, bl2, fileDownloadHeader, bl3);
    }

    @Override
    public void startForeground(int n, Notification notification) {
        this.handler.startForeground(n, notification);
    }

    @Override
    public void stopForeground(boolean bl) {
        this.handler.stopForeground(bl);
    }

    @Override
    public void unbindByContext(Context context) {
        this.handler.unbindByContext(context);
    }

    private static final class HolderClass {
        private static final FileDownloadServiceProxy INSTANCE = new FileDownloadServiceProxy();

        private HolderClass() {
        }
    }
}

