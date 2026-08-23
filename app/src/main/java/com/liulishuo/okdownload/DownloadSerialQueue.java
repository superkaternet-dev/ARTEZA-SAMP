/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload;

import com.liulishuo.okdownload.DownloadListener;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.listener.DownloadListener2;
import com.liulishuo.okdownload.core.listener.DownloadListenerBunch;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DownloadSerialQueue
extends DownloadListener2
implements Runnable {
    static final int ID_INVALID = 0;
    private static final Executor SERIAL_EXECUTOR = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 30L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), Util.threadFactory("OkDownload DynamicSerial", false));
    private static final String TAG = "DownloadSerialQueue";
    DownloadListenerBunch listenerBunch;
    volatile boolean looping = false;
    volatile boolean paused = false;
    volatile DownloadTask runningTask;
    volatile boolean shutedDown = false;
    private final ArrayList<DownloadTask> taskList;

    public DownloadSerialQueue() {
        this(null);
    }

    public DownloadSerialQueue(DownloadListener downloadListener) {
        this(downloadListener, new ArrayList<DownloadTask>());
    }

    DownloadSerialQueue(DownloadListener downloadListener, ArrayList<DownloadTask> arrayList) {
        this.listenerBunch = new DownloadListenerBunch.Builder().append(this).append(downloadListener).build();
        this.taskList = arrayList;
    }

    public void enqueue(DownloadTask downloadTask) {
        synchronized (this) {
            this.taskList.add(downloadTask);
            Collections.sort(this.taskList);
            if (!this.paused && !this.looping) {
                this.looping = true;
                this.startNewLooper();
            }
            return;
        }
    }

    public int getWaitingTaskCount() {
        return this.taskList.size();
    }

    public int getWorkingTaskId() {
        int n = this.runningTask != null ? this.runningTask.getId() : 0;
        return n;
    }

    public void pause() {
        synchronized (this) {
            if (this.paused) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("require pause this queue(remain ");
                stringBuilder.append(this.taskList.size());
                stringBuilder.append("), but");
                stringBuilder.append("it has already been paused");
                Util.w(TAG, stringBuilder.toString());
                return;
            }
            this.paused = true;
            if (this.runningTask != null) {
                this.runningTask.cancel();
                this.taskList.add(0, this.runningTask);
                this.runningTask = null;
            }
            return;
        }
    }

    public void resume() {
        synchronized (this) {
            if (!this.paused) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("require resume this queue(remain ");
                stringBuilder.append(this.taskList.size());
                stringBuilder.append("), but it is");
                stringBuilder.append(" still running");
                Util.w(TAG, stringBuilder.toString());
                return;
            }
            this.paused = false;
            if (!this.taskList.isEmpty() && !this.looping) {
                this.looping = true;
                this.startNewLooper();
            }
            return;
        }
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void run() {
        while (!this.shutedDown) {
            synchronized (this) {
                block8: {
                    try {
                        if (this.taskList.isEmpty() || this.paused) break block8;
                        DownloadTask downloadTask = this.taskList.remove(0);
                        // MONITOREXIT @DISABLED, blocks:[0, 3, 6] lbl6 : MonitorExitStatement: MONITOREXIT : this
                        downloadTask.execute(this.listenerBunch);
                        continue;
                    }
                    catch (Throwable throwable) {
                        while (true) {
                            void var1_3;
                            try {}
                            catch (Throwable throwable2) {
                                continue;
                            }
                            throw var1_3;
                        }
                    }
                }
                this.runningTask = null;
                this.looping = false;
                break;
            }
        }
    }

    public void setListener(DownloadListener downloadListener) {
        this.listenerBunch = new DownloadListenerBunch.Builder().append(this).append(downloadListener).build();
    }

    public DownloadTask[] shutdown() {
        synchronized (this) {
            this.shutedDown = true;
            if (this.runningTask != null) {
                this.runningTask.cancel();
            }
            DownloadTask[] downloadTaskArray = new DownloadTask[this.taskList.size()];
            this.taskList.toArray(downloadTaskArray);
            this.taskList.clear();
            return downloadTaskArray;
        }
    }

    void startNewLooper() {
        SERIAL_EXECUTOR.execute(this);
    }

    @Override
    public void taskEnd(DownloadTask downloadTask, EndCause endCause, Exception exception) {
        synchronized (this) {
            if (endCause != EndCause.CANCELED && downloadTask == this.runningTask) {
                this.runningTask = null;
            }
            return;
        }
    }

    @Override
    public void taskStart(DownloadTask downloadTask) {
        this.runningTask = downloadTask;
    }
}

