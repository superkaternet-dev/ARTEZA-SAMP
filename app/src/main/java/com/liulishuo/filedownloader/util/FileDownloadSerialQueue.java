/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 *  android.os.Handler$Callback
 *  android.os.HandlerThread
 *  android.os.Message
 */
package com.liulishuo.filedownloader.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FileDownloadSerialQueue {
    public static final int ID_INVALID = 0;
    private static final int WHAT_NEXT = 1;
    final SerialFinishCallback finishCallback;
    private final Handler mHandler;
    private final HandlerThread mHandlerThread;
    private final BlockingQueue<BaseDownloadTask> mTasks;
    private final Object operationLock = new Object();
    volatile boolean paused = false;
    private final List<BaseDownloadTask> pausedList;
    volatile BaseDownloadTask workingTask;

    public FileDownloadSerialQueue() {
        HandlerThread handlerThread;
        this.mTasks = new LinkedBlockingQueue<BaseDownloadTask>();
        this.pausedList = new ArrayList<BaseDownloadTask>();
        this.mHandlerThread = handlerThread = new HandlerThread(FileDownloadUtils.getThreadPoolName("SerialDownloadManager"));
        handlerThread.start();
        this.mHandler = new Handler(handlerThread.getLooper(), (Handler.Callback)new SerialLoop(this));
        this.finishCallback = new SerialFinishCallback(new WeakReference<FileDownloadSerialQueue>(this));
        this.sendNext();
    }

    private void sendNext() {
        this.mHandler.sendEmptyMessage(1);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void enqueue(BaseDownloadTask baseDownloadTask) {
        SerialFinishCallback serialFinishCallback = this.finishCallback;
        synchronized (serialFinishCallback) {
            if (this.paused) {
                this.pausedList.add(baseDownloadTask);
                return;
            }
            try {
                this.mTasks.put(baseDownloadTask);
            }
            catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            return;
        }
    }

    public int getWaitingTaskCount() {
        return this.mTasks.size() + this.pausedList.size();
    }

    public int getWorkingTaskId() {
        int n = this.workingTask != null ? this.workingTask.getId() : 0;
        return n;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void pause() {
        SerialFinishCallback serialFinishCallback = this.finishCallback;
        synchronized (serialFinishCallback) {
            if (this.paused) {
                FileDownloadLog.w(this, "require pause this queue(remain %d), but it has already been paused", this.mTasks.size());
                return;
            }
            this.paused = true;
            this.mTasks.drainTo(this.pausedList);
            if (this.workingTask != null) {
                this.workingTask.removeFinishListener(this.finishCallback);
                this.workingTask.pause();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void resume() {
        SerialFinishCallback serialFinishCallback = this.finishCallback;
        synchronized (serialFinishCallback) {
            if (!this.paused) {
                FileDownloadLog.w(this, "require resume this queue(remain %d), but it is still running", this.mTasks.size());
                return;
            }
            this.paused = false;
            this.mTasks.addAll(this.pausedList);
            this.pausedList.clear();
            if (this.workingTask == null) {
                this.sendNext();
            } else {
                this.workingTask.addFinishListener(this.finishCallback);
                this.workingTask.start();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public List<BaseDownloadTask> shutdown() {
        SerialFinishCallback serialFinishCallback = this.finishCallback;
        synchronized (serialFinishCallback) {
            if (this.workingTask != null) {
                this.pause();
            }
            ArrayList<BaseDownloadTask> arrayList = new ArrayList<BaseDownloadTask>(this.pausedList);
            this.pausedList.clear();
            this.mHandler.removeMessages(1);
            this.mHandlerThread.interrupt();
            this.mHandlerThread.quit();
            return arrayList;
        }
    }

    private static class SerialFinishCallback
    implements BaseDownloadTask.FinishListener {
        private final WeakReference<FileDownloadSerialQueue> mQueueWeakReference;

        SerialFinishCallback(WeakReference<FileDownloadSerialQueue> weakReference) {
            this.mQueueWeakReference = weakReference;
        }

        @Override
        public void over(BaseDownloadTask object) {
            synchronized (this) {
                block8: {
                    block7: {
                        block6: {
                            object.removeFinishListener(this);
                            object = this.mQueueWeakReference;
                            if (object != null) break block6;
                            return;
                        }
                        object = (FileDownloadSerialQueue)((Reference)object).get();
                        if (object != null) break block7;
                        return;
                    }
                    ((FileDownloadSerialQueue)object).workingTask = null;
                    boolean bl = ((FileDownloadSerialQueue)object).paused;
                    if (!bl) break block8;
                    return;
                }
                ((FileDownloadSerialQueue)object).sendNext();
                return;
            }
        }
    }

    private class SerialLoop
    implements Handler.Callback {
        final FileDownloadSerialQueue this$0;

        private SerialLoop(FileDownloadSerialQueue fileDownloadSerialQueue) {
            this.this$0 = fileDownloadSerialQueue;
        }

        public boolean handleMessage(Message object) {
            switch (object.what) {
                default: {
                    break;
                }
                case 1: {
                    try {
                        if (this.this$0.paused) break;
                        object = this.this$0;
                        object.workingTask = (BaseDownloadTask)((FileDownloadSerialQueue)object).mTasks.take();
                        this.this$0.workingTask.addFinishListener(this.this$0.finishCallback).start();
                        break;
                    }
                    catch (InterruptedException interruptedException) {
                        // empty catch block
                    }
                }
            }
            return false;
        }
    }
}

