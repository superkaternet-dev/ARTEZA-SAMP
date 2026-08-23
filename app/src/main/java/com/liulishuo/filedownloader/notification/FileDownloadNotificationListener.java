/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.notification;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadList;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.notification.BaseNotificationItem;
import com.liulishuo.filedownloader.notification.FileDownloadNotificationHelper;

public abstract class FileDownloadNotificationListener
extends FileDownloadListener {
    private final FileDownloadNotificationHelper helper;

    public FileDownloadNotificationListener(FileDownloadNotificationHelper fileDownloadNotificationHelper) {
        if (fileDownloadNotificationHelper != null) {
            this.helper = fileDownloadNotificationHelper;
            return;
        }
        throw new IllegalArgumentException("helper must not be null!");
    }

    public void addNotificationItem(int n) {
        if (n == 0) {
            return;
        }
        BaseDownloadTask.IRunningTask iRunningTask = FileDownloadList.getImpl().get(n);
        if (iRunningTask != null) {
            this.addNotificationItem(iRunningTask.getOrigin());
        }
    }

    public void addNotificationItem(BaseDownloadTask object) {
        if (this.disableNotification((BaseDownloadTask)object)) {
            return;
        }
        if ((object = this.create((BaseDownloadTask)object)) != null) {
            this.helper.add(object);
        }
    }

    @Override
    protected void blockComplete(BaseDownloadTask baseDownloadTask) {
    }

    @Override
    protected void completed(BaseDownloadTask baseDownloadTask) {
        this.destroyNotification(baseDownloadTask);
    }

    protected abstract BaseNotificationItem create(BaseDownloadTask var1);

    public void destroyNotification(BaseDownloadTask baseDownloadTask) {
        if (this.disableNotification(baseDownloadTask)) {
            return;
        }
        this.helper.showIndeterminate(baseDownloadTask.getId(), baseDownloadTask.getStatus());
        Object t = this.helper.remove(baseDownloadTask.getId());
        if (!this.interceptCancel(baseDownloadTask, (BaseNotificationItem)t) && t != null) {
            ((BaseNotificationItem)t).cancel();
        }
    }

    protected boolean disableNotification(BaseDownloadTask baseDownloadTask) {
        return false;
    }

    @Override
    protected void error(BaseDownloadTask baseDownloadTask, Throwable throwable) {
        this.destroyNotification(baseDownloadTask);
    }

    public FileDownloadNotificationHelper getHelper() {
        return this.helper;
    }

    protected boolean interceptCancel(BaseDownloadTask baseDownloadTask, BaseNotificationItem baseNotificationItem) {
        return false;
    }

    @Override
    protected void paused(BaseDownloadTask baseDownloadTask, int n, int n2) {
        this.destroyNotification(baseDownloadTask);
    }

    @Override
    protected void pending(BaseDownloadTask baseDownloadTask, int n, int n2) {
        this.addNotificationItem(baseDownloadTask);
        this.showIndeterminate(baseDownloadTask);
    }

    @Override
    protected void progress(BaseDownloadTask baseDownloadTask, int n, int n2) {
        this.showProgress(baseDownloadTask, n, n2);
    }

    @Override
    protected void retry(BaseDownloadTask baseDownloadTask, Throwable throwable, int n, int n2) {
        super.retry(baseDownloadTask, throwable, n, n2);
        this.showIndeterminate(baseDownloadTask);
    }

    public void showIndeterminate(BaseDownloadTask baseDownloadTask) {
        if (this.disableNotification(baseDownloadTask)) {
            return;
        }
        this.helper.showIndeterminate(baseDownloadTask.getId(), baseDownloadTask.getStatus());
    }

    public void showProgress(BaseDownloadTask baseDownloadTask, int n, int n2) {
        if (this.disableNotification(baseDownloadTask)) {
            return;
        }
        this.helper.showProgress(baseDownloadTask.getId(), baseDownloadTask.getSmallFileSoFarBytes(), baseDownloadTask.getSmallFileTotalBytes());
    }

    @Override
    protected void started(BaseDownloadTask baseDownloadTask) {
        super.started(baseDownloadTask);
        this.showIndeterminate(baseDownloadTask);
    }

    @Override
    protected void warn(BaseDownloadTask baseDownloadTask) {
    }
}

