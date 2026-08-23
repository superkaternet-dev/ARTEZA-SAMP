/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadConnectListener;
import com.liulishuo.filedownloader.FileDownloadList;
import com.liulishuo.filedownloader.FileDownloadServiceProxy;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.ILostServiceConnectedHandler;
import com.liulishuo.filedownloader.IQueuesHandler;
import com.liulishuo.filedownloader.event.DownloadServiceConnectChangedEvent;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LostServiceConnectedHandler
extends FileDownloadConnectListener
implements ILostServiceConnectedHandler {
    private final ArrayList<BaseDownloadTask.IRunningTask> mWaitingList = new ArrayList();

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void connected() {
        IQueuesHandler iQueuesHandler = FileDownloader.getImpl().getQueuesHandler();
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "The downloader service is connected.", new Object[0]);
        }
        ArrayList<BaseDownloadTask.IRunningTask> arrayList = this.mWaitingList;
        synchronized (arrayList) {
            Object object = (List)this.mWaitingList.clone();
            try {
                this.mWaitingList.clear();
                ArrayList<Integer> arrayList2 = new ArrayList<Integer>(iQueuesHandler.serialQueueSize());
                Iterator iterator2 = object.iterator();
                while (true) {
                    if (!iterator2.hasNext()) {
                        iQueuesHandler.unFreezeSerialQueues(arrayList2);
                        return;
                    }
                    object = (BaseDownloadTask.IRunningTask)iterator2.next();
                    int n = object.getAttachKey();
                    if (iQueuesHandler.contain(n)) {
                        object.getOrigin().asInQueueTask().enqueue();
                        if (arrayList2.contains(n)) continue;
                        arrayList2.add(n);
                        continue;
                    }
                    object.startTaskByRescue();
                }
            }
            catch (Throwable throwable) {}
            throw throwable;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void disconnected() {
        if (this.getConnectStatus() != DownloadServiceConnectChangedEvent.ConnectStatus.lost) {
            if (FileDownloadList.getImpl().size() <= 0) return;
            FileDownloadLog.w(this, "file download service has be unbound but the size of active tasks are not empty %d ", FileDownloadList.getImpl().size());
            return;
        }
        IQueuesHandler iQueuesHandler = FileDownloader.getImpl().getQueuesHandler();
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "lost the connection to the file download service, and current active task size is %d", FileDownloadList.getImpl().size());
        }
        if (FileDownloadList.getImpl().size() <= 0) return;
        ArrayList<BaseDownloadTask.IRunningTask> arrayList = this.mWaitingList;
        synchronized (arrayList) {
            FileDownloadList.getImpl().divertAndIgnoreDuplicate(this.mWaitingList);
            Iterator<BaseDownloadTask.IRunningTask> iterator2 = this.mWaitingList.iterator();
            while (iterator2.hasNext()) {
                iterator2.next().free();
            }
            iQueuesHandler.freezeAllSerialQueues();
        }
        try {
            FileDownloader.getImpl().bindService();
            return;
        }
        catch (IllegalStateException illegalStateException) {
            FileDownloadLog.w(this, "restart service failed, you may need to restart downloading manually when the app comes back to foreground", new Object[0]);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean dispatchTaskStart(BaseDownloadTask.IRunningTask iRunningTask) {
        if (!FileDownloader.getImpl().isServiceConnected()) {
            ArrayList<BaseDownloadTask.IRunningTask> arrayList = this.mWaitingList;
            synchronized (arrayList) {
                if (!FileDownloader.getImpl().isServiceConnected()) {
                    if (FileDownloadLog.NEED_LOG) {
                        FileDownloadLog.d(this, "Waiting for connecting with the downloader service... %d", iRunningTask.getOrigin().getId());
                    }
                    FileDownloadServiceProxy.getImpl().bindStartByContext(FileDownloadHelper.getAppContext());
                    if (!this.mWaitingList.contains(iRunningTask)) {
                        iRunningTask.free();
                        this.mWaitingList.add(iRunningTask);
                    }
                    return true;
                }
            }
        }
        this.taskWorkFine(iRunningTask);
        return false;
    }

    @Override
    public boolean isInWaitingList(BaseDownloadTask.IRunningTask iRunningTask) {
        boolean bl = !this.mWaitingList.isEmpty() && this.mWaitingList.contains(iRunningTask);
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void taskWorkFine(BaseDownloadTask.IRunningTask iRunningTask) {
        if (this.mWaitingList.isEmpty()) return;
        ArrayList<BaseDownloadTask.IRunningTask> arrayList = this.mWaitingList;
        synchronized (arrayList) {
            this.mWaitingList.remove(iRunningTask);
            return;
        }
    }
}

