/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadServiceProxy;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.message.MessageSnapshot;
import com.liulishuo.filedownloader.message.MessageSnapshotTaker;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileDownloadList {
    private final ArrayList<BaseDownloadTask.IRunningTask> mList = new ArrayList();

    private FileDownloadList() {
    }

    public static FileDownloadList getImpl() {
        return HolderClass.INSTANCE;
    }

    void add(BaseDownloadTask.IRunningTask iRunningTask) {
        if (!iRunningTask.getOrigin().isAttached()) {
            iRunningTask.setAttachKeyDefault();
        }
        if (iRunningTask.getMessageHandler().getMessenger().notifyBegin()) {
            this.addUnchecked(iRunningTask);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void addUnchecked(BaseDownloadTask.IRunningTask iRunningTask) {
        if (iRunningTask.isMarkedAdded2List()) {
            return;
        }
        ArrayList<BaseDownloadTask.IRunningTask> arrayList = this.mList;
        synchronized (arrayList) {
            if (this.mList.contains(iRunningTask)) {
                FileDownloadLog.w(this, "already has %s", iRunningTask);
            } else {
                iRunningTask.markAdded2List();
                this.mList.add(iRunningTask);
                if (FileDownloadLog.NEED_LOG) {
                    FileDownloadLog.v(this, "add list in all %s %d %d", iRunningTask, iRunningTask.getOrigin().getStatus(), this.mList.size());
                }
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    List<BaseDownloadTask.IRunningTask> assembleTasksToStart(int n, FileDownloadListener fileDownloadListener) {
        ArrayList<BaseDownloadTask.IRunningTask> arrayList = new ArrayList<BaseDownloadTask.IRunningTask>();
        ArrayList<BaseDownloadTask.IRunningTask> arrayList2 = this.mList;
        synchronized (arrayList2) {
            Iterator<BaseDownloadTask.IRunningTask> iterator2 = this.mList.iterator();
            while (iterator2.hasNext()) {
                BaseDownloadTask.IRunningTask iRunningTask = iterator2.next();
                if (iRunningTask.getOrigin().getListener() != fileDownloadListener || iRunningTask.getOrigin().isAttached()) continue;
                iRunningTask.setAttachKeyByQueue(n);
                arrayList.add(iRunningTask);
            }
            return arrayList;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    List<BaseDownloadTask.IRunningTask> copy(FileDownloadListener fileDownloadListener) {
        ArrayList<BaseDownloadTask.IRunningTask> arrayList = new ArrayList<BaseDownloadTask.IRunningTask>();
        ArrayList<BaseDownloadTask.IRunningTask> arrayList2 = this.mList;
        synchronized (arrayList2) {
            Iterator<BaseDownloadTask.IRunningTask> iterator2 = this.mList.iterator();
            while (iterator2.hasNext()) {
                BaseDownloadTask.IRunningTask iRunningTask = iterator2.next();
                if (!iRunningTask.is(fileDownloadListener)) continue;
                arrayList.add(iRunningTask);
            }
            return arrayList;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    BaseDownloadTask.IRunningTask[] copy() {
        ArrayList<BaseDownloadTask.IRunningTask> arrayList = this.mList;
        synchronized (arrayList) {
            BaseDownloadTask.IRunningTask[] iRunningTaskArray = new BaseDownloadTask.IRunningTask[this.mList.size()];
            return this.mList.toArray(iRunningTaskArray);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    int count(int n) {
        int n2 = 0;
        ArrayList<BaseDownloadTask.IRunningTask> arrayList = this.mList;
        synchronized (arrayList) {
            Iterator<BaseDownloadTask.IRunningTask> iterator2 = this.mList.iterator();
            while (iterator2.hasNext()) {
                int n3 = n2;
                if (iterator2.next().is(n)) {
                    n3 = n2 + 1;
                }
                n2 = n3;
            }
            return n2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void divertAndIgnoreDuplicate(List<BaseDownloadTask.IRunningTask> list) {
        ArrayList<BaseDownloadTask.IRunningTask> arrayList = this.mList;
        synchronized (arrayList) {
            Iterator<BaseDownloadTask.IRunningTask> iterator2 = this.mList.iterator();
            while (true) {
                if (!iterator2.hasNext()) {
                    this.mList.clear();
                    return;
                }
                BaseDownloadTask.IRunningTask iRunningTask = iterator2.next();
                if (list.contains(iRunningTask)) continue;
                list.add(iRunningTask);
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public BaseDownloadTask.IRunningTask get(int n) {
        ArrayList<BaseDownloadTask.IRunningTask> arrayList = this.mList;
        synchronized (arrayList) {
            BaseDownloadTask.IRunningTask iRunningTask;
            Iterator<BaseDownloadTask.IRunningTask> iterator2 = this.mList.iterator();
            do {
                if (!iterator2.hasNext()) return null;
            } while (!(iRunningTask = iterator2.next()).is(n));
            return iRunningTask;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    List<BaseDownloadTask.IRunningTask> getDownloadingList(int n) {
        ArrayList<BaseDownloadTask.IRunningTask> arrayList = new ArrayList<BaseDownloadTask.IRunningTask>();
        ArrayList<BaseDownloadTask.IRunningTask> arrayList2 = this.mList;
        synchronized (arrayList2) {
            Iterator<BaseDownloadTask.IRunningTask> iterator2 = this.mList.iterator();
            while (iterator2.hasNext()) {
                BaseDownloadTask.IRunningTask iRunningTask = iterator2.next();
                if (!iRunningTask.is(n) || iRunningTask.isOver()) continue;
                arrayList.add(iRunningTask);
            }
            return arrayList;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    List<BaseDownloadTask.IRunningTask> getReceiveServiceTaskList(int n) {
        ArrayList<BaseDownloadTask.IRunningTask> arrayList = new ArrayList<BaseDownloadTask.IRunningTask>();
        ArrayList<BaseDownloadTask.IRunningTask> arrayList2 = this.mList;
        synchronized (arrayList2) {
            Iterator<BaseDownloadTask.IRunningTask> iterator2 = this.mList.iterator();
            while (iterator2.hasNext()) {
                byte by;
                BaseDownloadTask.IRunningTask iRunningTask = iterator2.next();
                if (!iRunningTask.is(n) || iRunningTask.isOver() || (by = iRunningTask.getOrigin().getStatus()) == 0 || by == 10) continue;
                arrayList.add(iRunningTask);
            }
            return arrayList;
        }
    }

    boolean isEmpty() {
        return this.mList.isEmpty();
    }

    boolean isNotContains(BaseDownloadTask.IRunningTask iRunningTask) {
        boolean bl = this.mList.isEmpty() || !this.mList.contains(iRunningTask);
        return bl;
    }

    /*
     * Loose catch block
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public boolean remove(BaseDownloadTask.IRunningTask object, MessageSnapshot messageSnapshot) {
        void var2_3;
        byte by = var2_3.getStatus();
        ArrayList<BaseDownloadTask.IRunningTask> arrayList = this.mList;
        // MONITORENTER : arrayList
        boolean bl = this.mList.remove(object);
        if (bl) {
            if (this.mList.size() == 0 && FileDownloadServiceProxy.getImpl().isRunServiceForeground()) {
                FileDownloader.getImpl().stopForeground(true);
            }
            // MONITOREXIT : arrayList
        }
        if (FileDownloadLog.NEED_LOG && this.mList.size() == 0) {
            FileDownloadLog.v(this, "remove %s left %d %d", object, by, this.mList.size());
        }
        if (!bl) {
            FileDownloadLog.e((Object)this, "remove error, not exist: %s %d", object, by);
            return bl;
        }
        object = object.getMessageHandler().getMessenger();
        switch (by) {
            default: {
                return bl;
            }
            case -1: {
                object.notifyError((MessageSnapshot)var2_3);
                return bl;
            }
            case -2: {
                object.notifyPaused((MessageSnapshot)var2_3);
                return bl;
            }
            case -3: {
                object.notifyBlockComplete(MessageSnapshotTaker.takeBlockCompleted((MessageSnapshot)var2_3));
                return bl;
            }
            case -4: 
        }
        object.notifyWarn((MessageSnapshot)var2_3);
        return bl;
        catch (Throwable throwable) {
            throw throwable;
        }
    }

    int size() {
        return this.mList.size();
    }

    private static final class HolderClass {
        private static final FileDownloadList INSTANCE = new FileDownloadList();

        private HolderClass() {
        }
    }
}

