/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  android.util.SparseArray
 */
package com.liulishuo.filedownloader;

import android.text.TextUtils;
import android.util.SparseArray;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.DownloadTaskHunter;
import com.liulishuo.filedownloader.FileDownloadList;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.ITaskHunter;
import com.liulishuo.filedownloader.model.FileDownloadHeader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.io.File;
import java.util.ArrayList;

public class DownloadTask
implements BaseDownloadTask,
BaseDownloadTask.IRunningTask,
DownloadTaskHunter.ICaptureTask {
    public static final int DEFAULT_CALLBACK_PROGRESS_MIN_INTERVAL_MILLIS = 10;
    private final Object headerCreateLock = new Object();
    volatile int mAttachKey = 0;
    private int mAutoRetryTimes = 0;
    private int mCallbackProgressMinIntervalMillis = 10;
    private int mCallbackProgressTimes = 100;
    private String mFilename;
    private ArrayList<BaseDownloadTask.FinishListener> mFinishListenerList;
    private FileDownloadHeader mHeader;
    private final ITaskHunter mHunter;
    private int mId;
    private boolean mIsForceReDownload = false;
    private boolean mIsInQueueTask = false;
    private volatile boolean mIsMarkedAdded2List = false;
    private boolean mIsWifiRequired = false;
    private SparseArray<Object> mKeyedTags;
    private FileDownloadListener mListener;
    private final ITaskHunter.IMessageHandler mMessageHandler;
    private String mPath;
    private boolean mPathAsDirectory;
    private final Object mPauseLock;
    private boolean mSyncCallback = false;
    private Object mTag;
    private final String mUrl;

    DownloadTask(String object) {
        this.mUrl = object;
        this.mPauseLock = object = new Object();
        this.mHunter = object = new DownloadTaskHunter(this, object);
        this.mMessageHandler = object;
    }

    static /* synthetic */ boolean access$102(DownloadTask downloadTask, boolean bl) {
        downloadTask.mIsInQueueTask = bl;
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void checkAndCreateHeader() {
        if (this.mHeader != null) return;
        Object object = this.headerCreateLock;
        synchronized (object) {
            FileDownloadHeader fileDownloadHeader;
            if (this.mHeader != null) return;
            this.mHeader = fileDownloadHeader = new FileDownloadHeader();
            return;
        }
    }

    private int startTaskUnchecked() {
        if (this.isUsing()) {
            if (this.isRunning()) {
                throw new IllegalStateException(FileDownloadUtils.formatString("This task is running %d, if you want to start the same task, please create a new one by FileDownloader.create", this.getId()));
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("This task is dirty to restart, If you want to reuse this task, please invoke #reuse method manually and retry to restart again.");
            stringBuilder.append(this.mHunter.toString());
            throw new IllegalStateException(stringBuilder.toString());
        }
        if (!this.isAttached()) {
            this.setAttachKeyDefault();
        }
        this.mHunter.intoLaunchPool();
        return this.getId();
    }

    @Override
    public BaseDownloadTask addFinishListener(BaseDownloadTask.FinishListener finishListener) {
        if (this.mFinishListenerList == null) {
            this.mFinishListenerList = new ArrayList();
        }
        if (!this.mFinishListenerList.contains(finishListener)) {
            this.mFinishListenerList.add(finishListener);
        }
        return this;
    }

    @Override
    public BaseDownloadTask addHeader(String string2) {
        this.checkAndCreateHeader();
        this.mHeader.add(string2);
        return this;
    }

    @Override
    public BaseDownloadTask addHeader(String string2, String string3) {
        this.checkAndCreateHeader();
        this.mHeader.add(string2, string3);
        return this;
    }

    @Override
    public BaseDownloadTask.InQueueTask asInQueueTask() {
        return new InQueueTaskImpl(this);
    }

    @Override
    public boolean cancel() {
        return this.pause();
    }

    @Override
    public void free() {
        this.mHunter.free();
        if (FileDownloadList.getImpl().isNotContains(this)) {
            this.mIsMarkedAdded2List = false;
        }
    }

    @Override
    public int getAttachKey() {
        return this.mAttachKey;
    }

    @Override
    public int getAutoRetryTimes() {
        return this.mAutoRetryTimes;
    }

    @Override
    public int getCallbackProgressMinInterval() {
        return this.mCallbackProgressMinIntervalMillis;
    }

    @Override
    public int getCallbackProgressTimes() {
        return this.mCallbackProgressTimes;
    }

    @Override
    public int getDownloadId() {
        return this.getId();
    }

    @Override
    public Throwable getErrorCause() {
        return this.mHunter.getErrorCause();
    }

    @Override
    public String getEtag() {
        return this.mHunter.getEtag();
    }

    @Override
    public Throwable getEx() {
        return this.getErrorCause();
    }

    @Override
    public String getFilename() {
        return this.mFilename;
    }

    @Override
    public ArrayList<BaseDownloadTask.FinishListener> getFinishListenerList() {
        return this.mFinishListenerList;
    }

    @Override
    public FileDownloadHeader getHeader() {
        return this.mHeader;
    }

    @Override
    public int getId() {
        int n = this.mId;
        if (n != 0) {
            return n;
        }
        if (!TextUtils.isEmpty((CharSequence)this.mPath) && !TextUtils.isEmpty((CharSequence)this.mUrl)) {
            this.mId = n = FileDownloadUtils.generateId(this.mUrl, this.mPath, this.mPathAsDirectory);
            return n;
        }
        return 0;
    }

    @Override
    public long getLargeFileSoFarBytes() {
        return this.mHunter.getSofarBytes();
    }

    @Override
    public long getLargeFileTotalBytes() {
        return this.mHunter.getTotalBytes();
    }

    @Override
    public FileDownloadListener getListener() {
        return this.mListener;
    }

    @Override
    public ITaskHunter.IMessageHandler getMessageHandler() {
        return this.mMessageHandler;
    }

    @Override
    public BaseDownloadTask getOrigin() {
        return this;
    }

    @Override
    public String getPath() {
        return this.mPath;
    }

    @Override
    public Object getPauseLock() {
        return this.mPauseLock;
    }

    @Override
    public int getRetryingTimes() {
        return this.mHunter.getRetryingTimes();
    }

    @Override
    public BaseDownloadTask.IRunningTask getRunningTask() {
        return this;
    }

    @Override
    public int getSmallFileSoFarBytes() {
        if (this.mHunter.getSofarBytes() > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int)this.mHunter.getSofarBytes();
    }

    @Override
    public int getSmallFileTotalBytes() {
        if (this.mHunter.getTotalBytes() > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int)this.mHunter.getTotalBytes();
    }

    @Override
    public int getSoFarBytes() {
        return this.getSmallFileSoFarBytes();
    }

    @Override
    public int getSpeed() {
        return this.mHunter.getSpeed();
    }

    @Override
    public byte getStatus() {
        return this.mHunter.getStatus();
    }

    @Override
    public Object getTag() {
        return this.mTag;
    }

    @Override
    public Object getTag(int n) {
        Object object = this.mKeyedTags;
        object = object == null ? null : object.get(n);
        return object;
    }

    @Override
    public String getTargetFilePath() {
        return FileDownloadUtils.getTargetFilePath(this.getPath(), this.isPathAsDirectory(), this.getFilename());
    }

    @Override
    public int getTotalBytes() {
        return this.getSmallFileTotalBytes();
    }

    @Override
    public String getUrl() {
        return this.mUrl;
    }

    @Override
    public boolean is(int n) {
        boolean bl = this.getId() == n;
        return bl;
    }

    @Override
    public boolean is(FileDownloadListener fileDownloadListener) {
        boolean bl = this.getListener() == fileDownloadListener;
        return bl;
    }

    @Override
    public boolean isAttached() {
        boolean bl = this.mAttachKey != 0;
        return bl;
    }

    @Override
    public boolean isContainFinishListener() {
        ArrayList<BaseDownloadTask.FinishListener> arrayList = this.mFinishListenerList;
        boolean bl = arrayList != null && arrayList.size() > 0;
        return bl;
    }

    @Override
    public boolean isContinue() {
        return this.isResuming();
    }

    @Override
    public boolean isForceReDownload() {
        return this.mIsForceReDownload;
    }

    @Override
    public boolean isLargeFile() {
        return this.mHunter.isLargeFile();
    }

    @Override
    public boolean isMarkedAdded2List() {
        return this.mIsMarkedAdded2List;
    }

    @Override
    public boolean isOver() {
        return FileDownloadStatus.isOver(this.getStatus());
    }

    @Override
    public boolean isPathAsDirectory() {
        return this.mPathAsDirectory;
    }

    @Override
    public boolean isResuming() {
        return this.mHunter.isResuming();
    }

    @Override
    public boolean isReusedOldFile() {
        return this.mHunter.isReusedOldFile();
    }

    @Override
    public boolean isRunning() {
        if (FileDownloader.getImpl().getLostConnectedHandler().isInWaitingList(this)) {
            return true;
        }
        return FileDownloadStatus.isIng(this.getStatus());
    }

    @Override
    public boolean isSyncCallback() {
        return this.mSyncCallback;
    }

    @Override
    public boolean isUsing() {
        boolean bl = this.mHunter.getStatus() != 0;
        return bl;
    }

    @Override
    public boolean isWifiRequired() {
        return this.mIsWifiRequired;
    }

    @Override
    public void markAdded2List() {
        this.mIsMarkedAdded2List = true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean pause() {
        Object object = this.mPauseLock;
        synchronized (object) {
            return this.mHunter.pause();
        }
    }

    @Override
    public int ready() {
        return this.asInQueueTask().enqueue();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public BaseDownloadTask removeAllHeaders(String string2) {
        if (this.mHeader == null) {
            Object object = this.headerCreateLock;
            synchronized (object) {
                if (this.mHeader == null) {
                    return this;
                }
            }
        }
        this.mHeader.removeAll(string2);
        return this;
    }

    @Override
    public boolean removeFinishListener(BaseDownloadTask.FinishListener finishListener) {
        ArrayList<BaseDownloadTask.FinishListener> arrayList = this.mFinishListenerList;
        boolean bl = arrayList != null && arrayList.remove(finishListener);
        return bl;
    }

    @Override
    public boolean reuse() {
        if (this.isRunning()) {
            FileDownloadLog.w(this, "This task[%d] is running, if you want start the same task, please create a new one by FileDownloader#create", this.getId());
            return false;
        }
        this.mAttachKey = 0;
        this.mIsInQueueTask = false;
        this.mIsMarkedAdded2List = false;
        this.mHunter.reset();
        return true;
    }

    @Override
    public void setAttachKeyByQueue(int n) {
        this.mAttachKey = n;
    }

    @Override
    public void setAttachKeyDefault() {
        int n = this.getListener() != null ? this.getListener().hashCode() : this.hashCode();
        this.mAttachKey = n;
    }

    @Override
    public BaseDownloadTask setAutoRetryTimes(int n) {
        this.mAutoRetryTimes = n;
        return this;
    }

    @Override
    public BaseDownloadTask setCallbackProgressIgnored() {
        return this.setCallbackProgressTimes(-1);
    }

    @Override
    public BaseDownloadTask setCallbackProgressMinInterval(int n) {
        this.mCallbackProgressMinIntervalMillis = n;
        return this;
    }

    @Override
    public BaseDownloadTask setCallbackProgressTimes(int n) {
        this.mCallbackProgressTimes = n;
        return this;
    }

    @Override
    public void setFileName(String string2) {
        this.mFilename = string2;
    }

    @Override
    public BaseDownloadTask setFinishListener(BaseDownloadTask.FinishListener finishListener) {
        this.addFinishListener(finishListener);
        return this;
    }

    @Override
    public BaseDownloadTask setForceReDownload(boolean bl) {
        this.mIsForceReDownload = bl;
        return this;
    }

    @Override
    public BaseDownloadTask setListener(FileDownloadListener fileDownloadListener) {
        this.mListener = fileDownloadListener;
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "setListener %s", fileDownloadListener);
        }
        return this;
    }

    @Override
    public BaseDownloadTask setMinIntervalUpdateSpeed(int n) {
        this.mHunter.setMinIntervalUpdateSpeed(n);
        return this;
    }

    @Override
    public BaseDownloadTask setPath(String string2) {
        return this.setPath(string2, false);
    }

    @Override
    public BaseDownloadTask setPath(String string2, boolean bl) {
        this.mPath = string2;
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "setPath %s", string2);
        }
        this.mPathAsDirectory = bl;
        this.mFilename = bl ? null : new File(string2).getName();
        return this;
    }

    @Override
    public BaseDownloadTask setSyncCallback(boolean bl) {
        this.mSyncCallback = bl;
        return this;
    }

    @Override
    public BaseDownloadTask setTag(int n, Object object) {
        if (this.mKeyedTags == null) {
            this.mKeyedTags = new SparseArray(2);
        }
        this.mKeyedTags.put(n, object);
        return this;
    }

    @Override
    public BaseDownloadTask setTag(Object object) {
        this.mTag = object;
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "setTag %s", object);
        }
        return this;
    }

    @Override
    public BaseDownloadTask setWifiRequired(boolean bl) {
        this.mIsWifiRequired = bl;
        return this;
    }

    @Override
    public int start() {
        if (!this.mIsInQueueTask) {
            return this.startTaskUnchecked();
        }
        throw new IllegalStateException("If you start the task manually, it means this task doesn't belong to a queue, so you must not invoke BaseDownloadTask#ready() or InQueueTask#enqueue() before you start() this method. For detail: If this task doesn't belong to a queue, what is just an isolated task, you just need to invoke BaseDownloadTask#start() to start this task, that's all. In other words, If this task doesn't belong to a queue, you must not invoke BaseDownloadTask#ready() method or InQueueTask#enqueue() method before invoke BaseDownloadTask#start(), If you do that and if there is the same listener object to start a queue in another thread, this task may be assembled by the queue, in that case, when you invoke BaseDownloadTask#start() manually to start this task or this task is started by the queue, there is an exception buried in there, because this task object is started two times without declare BaseDownloadTask#reuse() : 1. you invoke BaseDownloadTask#start() manually;  2. the queue start this task automatically.");
    }

    @Override
    public void startTaskByQueue() {
        this.startTaskUnchecked();
    }

    @Override
    public void startTaskByRescue() {
        this.startTaskUnchecked();
    }

    public String toString() {
        return FileDownloadUtils.formatString("%d@%s", this.getId(), super.toString());
    }

    private static final class InQueueTaskImpl
    implements BaseDownloadTask.InQueueTask {
        private final DownloadTask mTask;

        private InQueueTaskImpl(DownloadTask downloadTask) {
            this.mTask = downloadTask;
            DownloadTask.access$102(downloadTask, true);
        }

        @Override
        public int enqueue() {
            int n = this.mTask.getId();
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "add the task[%d] to the queue", n);
            }
            FileDownloadList.getImpl().addUnchecked(this.mTask);
            return n;
        }
    }
}

