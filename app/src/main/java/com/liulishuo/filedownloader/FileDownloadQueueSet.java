/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import java.util.ArrayList;
import java.util.List;

public class FileDownloadQueueSet {
    private Integer autoRetryTimes;
    private Integer callbackProgressMinIntervalMillis;
    private Integer callbackProgressTimes;
    private String directory;
    private Boolean isForceReDownload;
    private boolean isSerial;
    private Boolean isWifiRequired;
    private Boolean syncCallback;
    private Object tag;
    private FileDownloadListener target;
    private List<BaseDownloadTask.FinishListener> taskFinishListenerList;
    private BaseDownloadTask[] tasks;

    public FileDownloadQueueSet(FileDownloadListener fileDownloadListener) {
        if (fileDownloadListener != null) {
            this.target = fileDownloadListener;
            return;
        }
        throw new IllegalArgumentException("create FileDownloadQueueSet must with valid target!");
    }

    public FileDownloadQueueSet addTaskFinishListener(BaseDownloadTask.FinishListener finishListener) {
        if (this.taskFinishListenerList == null) {
            this.taskFinishListenerList = new ArrayList<BaseDownloadTask.FinishListener>();
        }
        this.taskFinishListenerList.add(finishListener);
        return this;
    }

    public FileDownloadQueueSet disableCallbackProgressTimes() {
        return this.setCallbackProgressTimes(0);
    }

    public FileDownloadQueueSet downloadSequentially(List<BaseDownloadTask> list) {
        this.isSerial = true;
        BaseDownloadTask[] baseDownloadTaskArray = new BaseDownloadTask[list.size()];
        this.tasks = baseDownloadTaskArray;
        list.toArray(baseDownloadTaskArray);
        return this;
    }

    public FileDownloadQueueSet downloadSequentially(BaseDownloadTask ... baseDownloadTaskArray) {
        this.isSerial = true;
        this.tasks = baseDownloadTaskArray;
        return this;
    }

    public FileDownloadQueueSet downloadTogether(List<BaseDownloadTask> list) {
        this.isSerial = false;
        BaseDownloadTask[] baseDownloadTaskArray = new BaseDownloadTask[list.size()];
        this.tasks = baseDownloadTaskArray;
        list.toArray(baseDownloadTaskArray);
        return this;
    }

    public FileDownloadQueueSet downloadTogether(BaseDownloadTask ... baseDownloadTaskArray) {
        this.isSerial = false;
        this.tasks = baseDownloadTaskArray;
        return this;
    }

    public FileDownloadQueueSet ignoreEachTaskInternalProgress() {
        this.setCallbackProgressTimes(-1);
        return this;
    }

    public void reuseAndStart() {
        BaseDownloadTask[] baseDownloadTaskArray = this.tasks;
        int n = baseDownloadTaskArray.length;
        for (int i = 0; i < n; ++i) {
            baseDownloadTaskArray[i].reuse();
        }
        this.start();
    }

    public FileDownloadQueueSet setAutoRetryTimes(int n) {
        this.autoRetryTimes = n;
        return this;
    }

    public FileDownloadQueueSet setCallbackProgressMinInterval(int n) {
        this.callbackProgressMinIntervalMillis = n;
        return this;
    }

    public FileDownloadQueueSet setCallbackProgressTimes(int n) {
        this.callbackProgressTimes = n;
        return this;
    }

    public FileDownloadQueueSet setDirectory(String string2) {
        this.directory = string2;
        return this;
    }

    public FileDownloadQueueSet setForceReDownload(boolean bl) {
        this.isForceReDownload = bl;
        return this;
    }

    public FileDownloadQueueSet setSyncCallback(boolean bl) {
        this.syncCallback = bl;
        return this;
    }

    public FileDownloadQueueSet setTag(Object object) {
        this.tag = object;
        return this;
    }

    public FileDownloadQueueSet setWifiRequired(boolean bl) {
        this.isWifiRequired = bl;
        return this;
    }

    public void start() {
        for (BaseDownloadTask baseDownloadTask : this.tasks) {
            baseDownloadTask.setListener(this.target);
            Object object = this.autoRetryTimes;
            if (object != null) {
                baseDownloadTask.setAutoRetryTimes((Integer)object);
            }
            if ((object = this.syncCallback) != null) {
                baseDownloadTask.setSyncCallback((Boolean)object);
            }
            if ((object = this.isForceReDownload) != null) {
                baseDownloadTask.setForceReDownload((Boolean)object);
            }
            if ((object = this.callbackProgressTimes) != null) {
                baseDownloadTask.setCallbackProgressTimes((Integer)object);
            }
            if ((object = this.callbackProgressMinIntervalMillis) != null) {
                baseDownloadTask.setCallbackProgressMinInterval((Integer)object);
            }
            if ((object = this.tag) != null) {
                baseDownloadTask.setTag(object);
            }
            if ((object = this.taskFinishListenerList) != null) {
                object = object.iterator();
                while (object.hasNext()) {
                    baseDownloadTask.addFinishListener((BaseDownloadTask.FinishListener)object.next());
                }
            }
            if ((object = this.directory) != null) {
                baseDownloadTask.setPath((String)object, true);
            }
            if ((object = this.isWifiRequired) != null) {
                baseDownloadTask.setWifiRequired((Boolean)object);
            }
            baseDownloadTask.asInQueueTask().enqueue();
        }
        FileDownloader.getImpl().start(this.target, this.isSerial);
    }
}

