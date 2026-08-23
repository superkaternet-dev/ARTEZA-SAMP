/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.content.Intent
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.RemoteCallbackList
 *  android.os.RemoteException
 */
package com.liulishuo.filedownloader.services;

import android.app.Notification;
import android.content.Intent;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import com.liulishuo.filedownloader.i.IFileDownloadIPCCallback;
import com.liulishuo.filedownloader.i.IFileDownloadIPCService;
import com.liulishuo.filedownloader.message.MessageSnapshot;
import com.liulishuo.filedownloader.message.MessageSnapshotFlow;
import com.liulishuo.filedownloader.model.FileDownloadHeader;
import com.liulishuo.filedownloader.services.FileDownloadManager;
import com.liulishuo.filedownloader.services.FileDownloadService;
import com.liulishuo.filedownloader.services.IFileDownloadServiceHandler;
import java.lang.ref.WeakReference;

public class FDServiceSeparateHandler
extends IFileDownloadIPCService.Stub
implements MessageSnapshotFlow.MessageReceiver,
IFileDownloadServiceHandler {
    private final RemoteCallbackList<IFileDownloadIPCCallback> callbackList = new RemoteCallbackList();
    private final FileDownloadManager downloadManager;
    private final WeakReference<FileDownloadService> wService;

    FDServiceSeparateHandler(WeakReference<FileDownloadService> weakReference, FileDownloadManager fileDownloadManager) {
        this.wService = weakReference;
        this.downloadManager = fileDownloadManager;
        MessageSnapshotFlow.getImpl().setReceiver(this);
    }

    /*
     * Exception decompiling
     */
    private int callback(MessageSnapshot var1_1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [11[FORLOOP]], but top level block is 4[TRYBLOCK]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    @Override
    public boolean checkDownloading(String string2, String string3) throws RemoteException {
        return this.downloadManager.isDownloading(string2, string3);
    }

    @Override
    public void clearAllTaskData() throws RemoteException {
        this.downloadManager.clearAllTaskData();
    }

    @Override
    public boolean clearTaskData(int n) throws RemoteException {
        return this.downloadManager.clearTaskData(n);
    }

    @Override
    public long getSofar(int n) throws RemoteException {
        return this.downloadManager.getSoFar(n);
    }

    @Override
    public byte getStatus(int n) throws RemoteException {
        return this.downloadManager.getStatus(n);
    }

    @Override
    public long getTotal(int n) throws RemoteException {
        return this.downloadManager.getTotal(n);
    }

    @Override
    public boolean isIdle() throws RemoteException {
        return this.downloadManager.isIdle();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return this;
    }

    @Override
    public void onDestroy() {
        MessageSnapshotFlow.getImpl().setReceiver(null);
    }

    @Override
    public void onStartCommand(Intent intent, int n, int n2) {
    }

    @Override
    public boolean pause(int n) throws RemoteException {
        return this.downloadManager.pause(n);
    }

    @Override
    public void pauseAllTasks() throws RemoteException {
        this.downloadManager.pauseAll();
    }

    @Override
    public void receive(MessageSnapshot messageSnapshot) {
        this.callback(messageSnapshot);
    }

    @Override
    public void registerCallback(IFileDownloadIPCCallback iFileDownloadIPCCallback) throws RemoteException {
        this.callbackList.register((IInterface)iFileDownloadIPCCallback);
    }

    @Override
    public boolean setMaxNetworkThreadCount(int n) throws RemoteException {
        return this.downloadManager.setMaxNetworkThreadCount(n);
    }

    @Override
    public void start(String string2, String string3, boolean bl, int n, int n2, int n3, boolean bl2, FileDownloadHeader fileDownloadHeader, boolean bl3) throws RemoteException {
        this.downloadManager.start(string2, string3, bl, n, n2, n3, bl2, fileDownloadHeader, bl3);
    }

    @Override
    public void startForeground(int n, Notification notification) throws RemoteException {
        WeakReference<FileDownloadService> weakReference = this.wService;
        if (weakReference != null && weakReference.get() != null) {
            ((FileDownloadService)((Object)this.wService.get())).startForeground(n, notification);
        }
    }

    @Override
    public void stopForeground(boolean bl) throws RemoteException {
        WeakReference<FileDownloadService> weakReference = this.wService;
        if (weakReference != null && weakReference.get() != null) {
            ((FileDownloadService)((Object)this.wService.get())).stopForeground(bl);
        }
    }

    @Override
    public void unregisterCallback(IFileDownloadIPCCallback iFileDownloadIPCCallback) throws RemoteException {
        this.callbackList.unregister((IInterface)iFileDownloadIPCCallback);
    }
}

