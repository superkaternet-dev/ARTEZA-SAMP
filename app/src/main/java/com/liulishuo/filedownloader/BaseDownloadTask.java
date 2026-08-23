/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader;

import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.ITaskHunter;

public interface BaseDownloadTask {
    public static final int DEFAULT_CALLBACK_PROGRESS_MIN_INTERVAL_MILLIS = 10;

    public BaseDownloadTask addFinishListener(FinishListener var1);

    public BaseDownloadTask addHeader(String var1);

    public BaseDownloadTask addHeader(String var1, String var2);

    public InQueueTask asInQueueTask();

    public boolean cancel();

    public int getAutoRetryTimes();

    public int getCallbackProgressMinInterval();

    public int getCallbackProgressTimes();

    public int getDownloadId();

    public Throwable getErrorCause();

    public String getEtag();

    public Throwable getEx();

    public String getFilename();

    public int getId();

    public long getLargeFileSoFarBytes();

    public long getLargeFileTotalBytes();

    public FileDownloadListener getListener();

    public String getPath();

    public int getRetryingTimes();

    public int getSmallFileSoFarBytes();

    public int getSmallFileTotalBytes();

    public int getSoFarBytes();

    public int getSpeed();

    public byte getStatus();

    public Object getTag();

    public Object getTag(int var1);

    public String getTargetFilePath();

    public int getTotalBytes();

    public String getUrl();

    public boolean isAttached();

    public boolean isContinue();

    public boolean isForceReDownload();

    public boolean isLargeFile();

    public boolean isPathAsDirectory();

    public boolean isResuming();

    public boolean isReusedOldFile();

    public boolean isRunning();

    public boolean isSyncCallback();

    public boolean isUsing();

    public boolean isWifiRequired();

    public boolean pause();

    public int ready();

    public BaseDownloadTask removeAllHeaders(String var1);

    public boolean removeFinishListener(FinishListener var1);

    public boolean reuse();

    public BaseDownloadTask setAutoRetryTimes(int var1);

    public BaseDownloadTask setCallbackProgressIgnored();

    public BaseDownloadTask setCallbackProgressMinInterval(int var1);

    public BaseDownloadTask setCallbackProgressTimes(int var1);

    public BaseDownloadTask setFinishListener(FinishListener var1);

    public BaseDownloadTask setForceReDownload(boolean var1);

    public BaseDownloadTask setListener(FileDownloadListener var1);

    public BaseDownloadTask setMinIntervalUpdateSpeed(int var1);

    public BaseDownloadTask setPath(String var1);

    public BaseDownloadTask setPath(String var1, boolean var2);

    public BaseDownloadTask setSyncCallback(boolean var1);

    public BaseDownloadTask setTag(int var1, Object var2);

    public BaseDownloadTask setTag(Object var1);

    public BaseDownloadTask setWifiRequired(boolean var1);

    public int start();

    public static interface FinishListener {
        public void over(BaseDownloadTask var1);
    }

    public static interface IRunningTask {
        public void free();

        public int getAttachKey();

        public ITaskHunter.IMessageHandler getMessageHandler();

        public BaseDownloadTask getOrigin();

        public Object getPauseLock();

        public boolean is(int var1);

        public boolean is(FileDownloadListener var1);

        public boolean isContainFinishListener();

        public boolean isMarkedAdded2List();

        public boolean isOver();

        public void markAdded2List();

        public void setAttachKeyByQueue(int var1);

        public void setAttachKeyDefault();

        public void startTaskByQueue();

        public void startTaskByRescue();
    }

    public static interface InQueueTask {
        public int enqueue();
    }

    public static interface LifeCycleCallback {
        public void onBegin();

        public void onIng();

        public void onOver();
    }
}

