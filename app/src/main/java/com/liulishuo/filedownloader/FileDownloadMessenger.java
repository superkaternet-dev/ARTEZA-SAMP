/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadLargeFileListener;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadMessageStation;
import com.liulishuo.filedownloader.FileDownloadMonitor;
import com.liulishuo.filedownloader.IFileDownloadMessenger;
import com.liulishuo.filedownloader.message.BlockCompleteMessage;
import com.liulishuo.filedownloader.message.MessageSnapshot;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

class FileDownloadMessenger
implements IFileDownloadMessenger {
    private boolean mIsDiscard = false;
    private BaseDownloadTask.LifeCycleCallback mLifeCycleCallback;
    private BaseDownloadTask.IRunningTask mTask;
    private Queue<MessageSnapshot> parcelQueue;

    FileDownloadMessenger(BaseDownloadTask.IRunningTask iRunningTask, BaseDownloadTask.LifeCycleCallback lifeCycleCallback) {
        this.init(iRunningTask, lifeCycleCallback);
    }

    private void init(BaseDownloadTask.IRunningTask iRunningTask, BaseDownloadTask.LifeCycleCallback lifeCycleCallback) {
        this.mTask = iRunningTask;
        this.mLifeCycleCallback = lifeCycleCallback;
        this.parcelQueue = new LinkedBlockingQueue<MessageSnapshot>();
    }

    private void inspectAndHandleOverStatus(int n) {
        if (FileDownloadStatus.isOver(n)) {
            if (!this.parcelQueue.isEmpty()) {
                MessageSnapshot messageSnapshot = this.parcelQueue.peek();
                FileDownloadLog.w(this, "the messenger[%s](with id[%d]) has already accomplished all his job, but there still are some messages in parcel queue[%d] queue-top-status[%d]", this, messageSnapshot.getId(), this.parcelQueue.size(), messageSnapshot.getStatus());
            }
            this.mTask = null;
        }
    }

    private void process(MessageSnapshot messageSnapshot) {
        BaseDownloadTask.IRunningTask iRunningTask = this.mTask;
        if (iRunningTask == null) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "occur this case, it would be the host task of this messenger has been over(paused/warn/completed/error) on the other thread before receiving the snapshot(id[%d], status[%d])", messageSnapshot.getId(), messageSnapshot.getStatus());
            }
            return;
        }
        if (!this.mIsDiscard && iRunningTask.getOrigin().getListener() != null) {
            this.parcelQueue.offer(messageSnapshot);
            FileDownloadMessageStation.getImpl().requestEnqueue(this);
        } else {
            if ((FileDownloadMonitor.isValid() || this.mTask.isContainFinishListener()) && messageSnapshot.getStatus() == 4) {
                this.mLifeCycleCallback.onOver();
            }
            this.inspectAndHandleOverStatus(messageSnapshot.getStatus());
        }
    }

    @Override
    public void discard() {
        this.mIsDiscard = true;
    }

    @Override
    public boolean handoverDirectly() {
        return this.mTask.getOrigin().isSyncCallback();
    }

    @Override
    public void handoverMessage() {
        if (this.mIsDiscard) {
            return;
        }
        MessageSnapshot messageSnapshot = this.parcelQueue.poll();
        byte by = messageSnapshot.getStatus();
        Object object = this.mTask;
        if (object != null) {
            BaseDownloadTask baseDownloadTask = object.getOrigin();
            FileDownloadListener fileDownloadListener = baseDownloadTask.getListener();
            object = object.getMessageHandler();
            this.inspectAndHandleOverStatus(by);
            if (fileDownloadListener != null && !fileDownloadListener.isInvalid()) {
                if (by == 4) {
                    try {
                        fileDownloadListener.blockComplete(baseDownloadTask);
                        this.notifyCompleted(((BlockCompleteMessage)((Object)messageSnapshot)).transmitToCompleted());
                    }
                    catch (Throwable throwable) {
                        this.notifyError(object.prepareErrorMessage(throwable));
                    }
                } else {
                    object = fileDownloadListener instanceof FileDownloadLargeFileListener ? (FileDownloadLargeFileListener)fileDownloadListener : null;
                    switch (by) {
                        default: {
                            break;
                        }
                        case 6: {
                            fileDownloadListener.started(baseDownloadTask);
                            break;
                        }
                        case 5: {
                            if (object != null) {
                                ((FileDownloadLargeFileListener)object).retry(baseDownloadTask, messageSnapshot.getThrowable(), messageSnapshot.getRetryingTimes(), messageSnapshot.getLargeSofarBytes());
                                break;
                            }
                            fileDownloadListener.retry(baseDownloadTask, messageSnapshot.getThrowable(), messageSnapshot.getRetryingTimes(), messageSnapshot.getSmallSofarBytes());
                            break;
                        }
                        case 3: {
                            if (object != null) {
                                ((FileDownloadLargeFileListener)object).progress(baseDownloadTask, messageSnapshot.getLargeSofarBytes(), baseDownloadTask.getLargeFileTotalBytes());
                                break;
                            }
                            fileDownloadListener.progress(baseDownloadTask, messageSnapshot.getSmallSofarBytes(), baseDownloadTask.getSmallFileTotalBytes());
                            break;
                        }
                        case 2: {
                            if (object != null) {
                                ((FileDownloadLargeFileListener)object).connected(baseDownloadTask, messageSnapshot.getEtag(), messageSnapshot.isResuming(), baseDownloadTask.getLargeFileSoFarBytes(), messageSnapshot.getLargeTotalBytes());
                                break;
                            }
                            fileDownloadListener.connected(baseDownloadTask, messageSnapshot.getEtag(), messageSnapshot.isResuming(), baseDownloadTask.getSmallFileSoFarBytes(), messageSnapshot.getSmallTotalBytes());
                            break;
                        }
                        case 1: {
                            if (object != null) {
                                ((FileDownloadLargeFileListener)object).pending(baseDownloadTask, messageSnapshot.getLargeSofarBytes(), messageSnapshot.getLargeTotalBytes());
                                break;
                            }
                            fileDownloadListener.pending(baseDownloadTask, messageSnapshot.getSmallSofarBytes(), messageSnapshot.getSmallTotalBytes());
                            break;
                        }
                        case -1: {
                            fileDownloadListener.error(baseDownloadTask, messageSnapshot.getThrowable());
                            break;
                        }
                        case -2: {
                            if (object != null) {
                                ((FileDownloadLargeFileListener)object).paused(baseDownloadTask, messageSnapshot.getLargeSofarBytes(), messageSnapshot.getLargeTotalBytes());
                                break;
                            }
                            fileDownloadListener.paused(baseDownloadTask, messageSnapshot.getSmallSofarBytes(), messageSnapshot.getSmallTotalBytes());
                            break;
                        }
                        case -3: {
                            fileDownloadListener.completed(baseDownloadTask);
                            break;
                        }
                        case -4: {
                            fileDownloadListener.warn(baseDownloadTask);
                        }
                    }
                }
                return;
            }
            return;
        }
        throw new IllegalArgumentException(FileDownloadUtils.formatString("can't handover the message, no master to receive this message(status[%d]) size[%d]", by, this.parcelQueue.size()));
    }

    @Override
    public boolean isBlockingCompleted() {
        boolean bl = this.parcelQueue.peek().getStatus() == 4;
        return bl;
    }

    @Override
    public boolean notifyBegin() {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "notify begin %s", this.mTask);
        }
        if (this.mTask == null) {
            FileDownloadLog.w(this, "can't begin the task, the holder fo the messenger is nil, %d", this.parcelQueue.size());
            return false;
        }
        this.mLifeCycleCallback.onBegin();
        return true;
    }

    @Override
    public void notifyBlockComplete(MessageSnapshot messageSnapshot) {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "notify block completed %s %s", this.mTask, Thread.currentThread().getName());
        }
        this.mLifeCycleCallback.onIng();
        this.process(messageSnapshot);
    }

    @Override
    public void notifyCompleted(MessageSnapshot messageSnapshot) {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "notify completed %s", this.mTask);
        }
        this.mLifeCycleCallback.onOver();
        this.process(messageSnapshot);
    }

    @Override
    public void notifyConnected(MessageSnapshot messageSnapshot) {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "notify connected %s", this.mTask);
        }
        this.mLifeCycleCallback.onIng();
        this.process(messageSnapshot);
    }

    @Override
    public void notifyError(MessageSnapshot messageSnapshot) {
        if (FileDownloadLog.NEED_LOG) {
            BaseDownloadTask.IRunningTask iRunningTask = this.mTask;
            FileDownloadLog.d(this, "notify error %s %s", iRunningTask, iRunningTask.getOrigin().getErrorCause());
        }
        this.mLifeCycleCallback.onOver();
        this.process(messageSnapshot);
    }

    @Override
    public void notifyPaused(MessageSnapshot messageSnapshot) {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "notify paused %s", this.mTask);
        }
        this.mLifeCycleCallback.onOver();
        this.process(messageSnapshot);
    }

    @Override
    public void notifyPending(MessageSnapshot messageSnapshot) {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "notify pending %s", this.mTask);
        }
        this.mLifeCycleCallback.onIng();
        this.process(messageSnapshot);
    }

    @Override
    public void notifyProgress(MessageSnapshot messageSnapshot) {
        BaseDownloadTask baseDownloadTask = this.mTask.getOrigin();
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "notify progress %s %d %d", baseDownloadTask, baseDownloadTask.getLargeFileSoFarBytes(), baseDownloadTask.getLargeFileTotalBytes());
        }
        if (baseDownloadTask.getCallbackProgressTimes() <= 0) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "notify progress but client not request notify %s", this.mTask);
            }
            return;
        }
        this.mLifeCycleCallback.onIng();
        this.process(messageSnapshot);
    }

    @Override
    public void notifyRetry(MessageSnapshot messageSnapshot) {
        if (FileDownloadLog.NEED_LOG) {
            BaseDownloadTask baseDownloadTask = this.mTask.getOrigin();
            FileDownloadLog.d(this, "notify retry %s %d %d %s", this.mTask, baseDownloadTask.getAutoRetryTimes(), baseDownloadTask.getRetryingTimes(), baseDownloadTask.getErrorCause());
        }
        this.mLifeCycleCallback.onIng();
        this.process(messageSnapshot);
    }

    @Override
    public void notifyStarted(MessageSnapshot messageSnapshot) {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "notify started %s", this.mTask);
        }
        this.mLifeCycleCallback.onIng();
        this.process(messageSnapshot);
    }

    @Override
    public void notifyWarn(MessageSnapshot messageSnapshot) {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "notify warn %s", this.mTask);
        }
        this.mLifeCycleCallback.onOver();
        this.process(messageSnapshot);
    }

    @Override
    public void reAppointment(BaseDownloadTask.IRunningTask iRunningTask, BaseDownloadTask.LifeCycleCallback lifeCycleCallback) {
        if (this.mTask == null) {
            this.init(iRunningTask, lifeCycleCallback);
            return;
        }
        throw new IllegalStateException(FileDownloadUtils.formatString("the messenger is working, can't re-appointment for %s", iRunningTask));
    }

    public String toString() {
        BaseDownloadTask.IRunningTask iRunningTask = this.mTask;
        int n = iRunningTask == null ? -1 : iRunningTask.getOrigin().getId();
        return FileDownloadUtils.formatString("%d:%s", n, super.toString());
    }
}

