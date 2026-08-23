/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.DownloadSpeedMonitor;
import com.liulishuo.filedownloader.FileDownloadList;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadMessenger;
import com.liulishuo.filedownloader.FileDownloadMonitor;
import com.liulishuo.filedownloader.FileDownloadServiceProxy;
import com.liulishuo.filedownloader.FileDownloadTaskLauncher;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.IDownloadSpeed;
import com.liulishuo.filedownloader.IFileDownloadMessenger;
import com.liulishuo.filedownloader.ILostServiceConnectedHandler;
import com.liulishuo.filedownloader.ITaskHunter;
import com.liulishuo.filedownloader.message.MessageSnapshot;
import com.liulishuo.filedownloader.message.MessageSnapshotTaker;
import com.liulishuo.filedownloader.model.FileDownloadHeader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;

public class DownloadTaskHunter
implements ITaskHunter,
ITaskHunter.IStarter,
ITaskHunter.IMessageHandler,
BaseDownloadTask.LifeCycleCallback {
    private String mEtag;
    private boolean mIsLargeFile;
    private boolean mIsResuming;
    private boolean mIsReusedOldFile = false;
    private IFileDownloadMessenger mMessenger;
    private final Object mPauseLock;
    private int mRetryingTimes;
    private long mSoFarBytes;
    private final IDownloadSpeed.Lookup mSpeedLookup;
    private final IDownloadSpeed.Monitor mSpeedMonitor;
    private volatile byte mStatus = 0;
    private final ICaptureTask mTask;
    private Throwable mThrowable = null;
    private long mTotalBytes;

    DownloadTaskHunter(ICaptureTask iCaptureTask, Object object) {
        this.mPauseLock = object;
        this.mTask = iCaptureTask;
        this.mSpeedMonitor = object = new DownloadSpeedMonitor();
        this.mSpeedLookup = object;
        this.mMessenger = new FileDownloadMessenger(iCaptureTask.getRunningTask(), this);
    }

    private int getId() {
        return this.mTask.getRunningTask().getOrigin().getId();
    }

    private void prepare() throws IOException {
        BaseDownloadTask baseDownloadTask;
        block8: {
            Object object;
            block7: {
                block6: {
                    baseDownloadTask = this.mTask.getRunningTask().getOrigin();
                    if (baseDownloadTask.getPath() == null) {
                        baseDownloadTask.setPath(FileDownloadUtils.getDefaultSaveFilePath(baseDownloadTask.getUrl()));
                        if (FileDownloadLog.NEED_LOG) {
                            FileDownloadLog.d(this, "save Path is null to %s", baseDownloadTask.getPath());
                        }
                    }
                    if (!baseDownloadTask.isPathAsDirectory()) break block6;
                    object = new File(baseDownloadTask.getPath());
                    break block7;
                }
                object = FileDownloadUtils.getParent(baseDownloadTask.getPath());
                if (object == null) break block8;
                object = new File((String)object);
            }
            if (!(((File)object).exists() || ((File)object).mkdirs() || ((File)object).exists())) {
                throw new IOException(FileDownloadUtils.formatString("Create parent directory failed, please make sure you have permission to create file or directory on the path: %s", ((File)object).getAbsolutePath()));
            }
            return;
        }
        throw new InvalidParameterException(FileDownloadUtils.formatString("the provided mPath[%s] is invalid, can't find its directory", baseDownloadTask.getPath()));
    }

    private void update(MessageSnapshot messageSnapshot) {
        byte by;
        BaseDownloadTask baseDownloadTask = this.mTask.getRunningTask().getOrigin();
        this.mStatus = by = messageSnapshot.getStatus();
        this.mIsLargeFile = messageSnapshot.isLargeFile();
        switch (by) {
            default: {
                break;
            }
            case 6: {
                this.mMessenger.notifyStarted(messageSnapshot);
                break;
            }
            case 5: {
                this.mSoFarBytes = messageSnapshot.getLargeSofarBytes();
                this.mThrowable = messageSnapshot.getThrowable();
                this.mRetryingTimes = messageSnapshot.getRetryingTimes();
                this.mSpeedMonitor.reset();
                this.mMessenger.notifyRetry(messageSnapshot);
                break;
            }
            case 3: {
                this.mSoFarBytes = messageSnapshot.getLargeSofarBytes();
                this.mSpeedMonitor.update(messageSnapshot.getLargeSofarBytes());
                this.mMessenger.notifyProgress(messageSnapshot);
                break;
            }
            case 2: {
                this.mTotalBytes = messageSnapshot.getLargeTotalBytes();
                this.mIsResuming = messageSnapshot.isResuming();
                this.mEtag = messageSnapshot.getEtag();
                String string2 = messageSnapshot.getFileName();
                if (string2 != null) {
                    if (baseDownloadTask.getFilename() != null) {
                        FileDownloadLog.w(this, "already has mFilename[%s], but assign mFilename[%s] again", baseDownloadTask.getFilename(), string2);
                    }
                    this.mTask.setFileName(string2);
                }
                this.mSpeedMonitor.start(this.mSoFarBytes);
                this.mMessenger.notifyConnected(messageSnapshot);
                break;
            }
            case 1: {
                this.mSoFarBytes = messageSnapshot.getLargeSofarBytes();
                this.mTotalBytes = messageSnapshot.getLargeTotalBytes();
                this.mMessenger.notifyPending(messageSnapshot);
                break;
            }
            case -1: {
                this.mThrowable = messageSnapshot.getThrowable();
                this.mSoFarBytes = messageSnapshot.getLargeSofarBytes();
                FileDownloadList.getImpl().remove(this.mTask.getRunningTask(), messageSnapshot);
                break;
            }
            case -2: {
                break;
            }
            case -3: {
                this.mIsReusedOldFile = messageSnapshot.isReusedDownloadedFile();
                this.mSoFarBytes = messageSnapshot.getLargeTotalBytes();
                this.mTotalBytes = messageSnapshot.getLargeTotalBytes();
                FileDownloadList.getImpl().remove(this.mTask.getRunningTask(), messageSnapshot);
                break;
            }
            case -4: {
                this.mSpeedMonitor.reset();
                int n = FileDownloadList.getImpl().count(baseDownloadTask.getId());
                int n2 = n <= 1 && baseDownloadTask.isPathAsDirectory() ? FileDownloadList.getImpl().count(FileDownloadUtils.generateId(baseDownloadTask.getUrl(), baseDownloadTask.getTargetFilePath())) : 0;
                if (n + n2 <= 1) {
                    n2 = FileDownloadServiceProxy.getImpl().getStatus(baseDownloadTask.getId());
                    FileDownloadLog.w(this, "warn, but no mListener to receive, switch to pending %d %d", baseDownloadTask.getId(), n2);
                    if (FileDownloadStatus.isIng(n2)) {
                        long l;
                        this.mStatus = 1;
                        this.mTotalBytes = messageSnapshot.getLargeTotalBytes();
                        this.mSoFarBytes = l = messageSnapshot.getLargeSofarBytes();
                        this.mSpeedMonitor.start(l);
                        this.mMessenger.notifyPending(((MessageSnapshot.IWarnMessageSnapshot)((Object)messageSnapshot)).turnToPending());
                        break;
                    }
                }
                FileDownloadList.getImpl().remove(this.mTask.getRunningTask(), messageSnapshot);
            }
        }
    }

    @Override
    public boolean equalListener(FileDownloadListener fileDownloadListener) {
        boolean bl = this.mTask.getRunningTask().getOrigin().getListener() == fileDownloadListener;
        return bl;
    }

    @Override
    public void free() {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "free the task %d, when the status is %d", this.getId(), this.mStatus);
        }
        this.mStatus = 0;
    }

    @Override
    public Throwable getErrorCause() {
        return this.mThrowable;
    }

    @Override
    public String getEtag() {
        return this.mEtag;
    }

    @Override
    public IFileDownloadMessenger getMessenger() {
        return this.mMessenger;
    }

    @Override
    public int getRetryingTimes() {
        return this.mRetryingTimes;
    }

    @Override
    public long getSofarBytes() {
        return this.mSoFarBytes;
    }

    @Override
    public int getSpeed() {
        return this.mSpeedLookup.getSpeed();
    }

    @Override
    public byte getStatus() {
        return this.mStatus;
    }

    @Override
    public long getTotalBytes() {
        return this.mTotalBytes;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void intoLaunchPool() {
        Object object = this.mPauseLock;
        synchronized (object) {
            if (this.mStatus != 0) {
                FileDownloadLog.w(this, "High concurrent cause, this task %d will not input to launch pool, because of the status isn't idle : %d", this.getId(), this.mStatus);
                return;
            }
            this.mStatus = (byte)10;
        }
        object = this.mTask.getRunningTask();
        BaseDownloadTask baseDownloadTask = object.getOrigin();
        if (FileDownloadMonitor.isValid()) {
            FileDownloadMonitor.getMonitor().onRequestStart(baseDownloadTask);
        }
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.v(this, "call start Url[%s], Path[%s] Listener[%s], Tag[%s]", baseDownloadTask.getUrl(), baseDownloadTask.getPath(), baseDownloadTask.getListener(), baseDownloadTask.getTag());
        }
        boolean bl = true;
        try {
            this.prepare();
        }
        catch (Throwable throwable) {
            bl = false;
            FileDownloadList.getImpl().add((BaseDownloadTask.IRunningTask)object);
            FileDownloadList.getImpl().remove((BaseDownloadTask.IRunningTask)object, this.prepareErrorMessage(throwable));
        }
        if (bl) {
            FileDownloadTaskLauncher.getImpl().launch(this);
        }
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.v(this, "the task[%d] has been into the launch pool.", this.getId());
        }
    }

    @Override
    public boolean isLargeFile() {
        return this.mIsLargeFile;
    }

    @Override
    public boolean isResuming() {
        return this.mIsResuming;
    }

    @Override
    public boolean isReusedOldFile() {
        return this.mIsReusedOldFile;
    }

    @Override
    public void onBegin() {
        if (FileDownloadMonitor.isValid()) {
            FileDownloadMonitor.getMonitor().onTaskBegin(this.mTask.getRunningTask().getOrigin());
        }
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.v(this, "filedownloader:lifecycle:start %s by %d ", this.toString(), this.getStatus());
        }
    }

    @Override
    public void onIng() {
        if (FileDownloadMonitor.isValid() && this.getStatus() == 6) {
            FileDownloadMonitor.getMonitor().onTaskStarted(this.mTask.getRunningTask().getOrigin());
        }
    }

    @Override
    public void onOver() {
        BaseDownloadTask baseDownloadTask = this.mTask.getRunningTask().getOrigin();
        if (FileDownloadMonitor.isValid()) {
            FileDownloadMonitor.getMonitor().onTaskOver(baseDownloadTask);
        }
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.v(this, "filedownloader:lifecycle:over %s by %d ", this.toString(), this.getStatus());
        }
        this.mSpeedMonitor.end(this.mSoFarBytes);
        if (this.mTask.getFinishListenerList() != null) {
            ArrayList arrayList = (ArrayList)this.mTask.getFinishListenerList().clone();
            int n = arrayList.size();
            for (int i = 0; i < n; ++i) {
                ((BaseDownloadTask.FinishListener)arrayList.get(i)).over(baseDownloadTask);
            }
        }
        FileDownloader.getImpl().getLostConnectedHandler().taskWorkFine(this.mTask.getRunningTask());
    }

    @Override
    public boolean pause() {
        if (FileDownloadStatus.isOver(this.getStatus())) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "High concurrent cause, Already is over, can't pause again, %d %d", this.getStatus(), this.mTask.getRunningTask().getOrigin().getId());
            }
            return false;
        }
        this.mStatus = (byte)-2;
        BaseDownloadTask.IRunningTask iRunningTask = this.mTask.getRunningTask();
        BaseDownloadTask baseDownloadTask = iRunningTask.getOrigin();
        FileDownloadTaskLauncher.getImpl().expire(this);
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.v(this, "the task[%d] has been expired from the launch pool.", this.getId());
        }
        if (!FileDownloader.getImpl().isServiceConnected()) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "request pause the task[%d] to the download service, but the download service isn't connected yet.", baseDownloadTask.getId());
            }
        } else {
            FileDownloadServiceProxy.getImpl().pause(baseDownloadTask.getId());
        }
        FileDownloadList.getImpl().add(iRunningTask);
        FileDownloadList.getImpl().remove(iRunningTask, MessageSnapshotTaker.catchPause(baseDownloadTask));
        FileDownloader.getImpl().getLostConnectedHandler().taskWorkFine(iRunningTask);
        return true;
    }

    @Override
    public MessageSnapshot prepareErrorMessage(Throwable throwable) {
        this.mStatus = (byte)-1;
        this.mThrowable = throwable;
        return MessageSnapshotTaker.catchException(this.getId(), this.getSofarBytes(), throwable);
    }

    @Override
    public void reset() {
        this.mThrowable = null;
        this.mEtag = null;
        this.mIsResuming = false;
        this.mRetryingTimes = 0;
        this.mIsReusedOldFile = false;
        this.mIsLargeFile = false;
        this.mSoFarBytes = 0L;
        this.mTotalBytes = 0L;
        this.mSpeedMonitor.reset();
        if (FileDownloadStatus.isOver(this.mStatus)) {
            this.mMessenger.discard();
            this.mMessenger = new FileDownloadMessenger(this.mTask.getRunningTask(), this);
        } else {
            this.mMessenger.reAppointment(this.mTask.getRunningTask(), this);
        }
        this.mStatus = 0;
    }

    @Override
    public void setMinIntervalUpdateSpeed(int n) {
        this.mSpeedLookup.setMinIntervalUpdateSpeed(n);
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void start() {
        Object object;
        if (this.mStatus != 10) {
            FileDownloadLog.w(this, "High concurrent cause, this task %d will not start, because the of status isn't toLaunchPool: %d", this.getId(), this.mStatus);
            return;
        }
        BaseDownloadTask.IRunningTask iRunningTask = this.mTask.getRunningTask();
        BaseDownloadTask baseDownloadTask = iRunningTask.getOrigin();
        ILostServiceConnectedHandler iLostServiceConnectedHandler = FileDownloader.getImpl().getLostConnectedHandler();
        try {
            if (iLostServiceConnectedHandler.dispatchTaskStart(iRunningTask)) {
                return;
            }
            object = this.mPauseLock;
            synchronized (object) {
                if (this.mStatus == 10) break block13;
            }
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            FileDownloadList.getImpl().remove(iRunningTask, this.prepareErrorMessage(throwable));
            return;
        }
        {
            block13: {
                FileDownloadLog.w(this, "High concurrent cause, this task %d will not start, the status can't assign to toFileDownloadService, because the status isn't toLaunchPool: %d", this.getId(), this.mStatus);
                return;
            }
            this.mStatus = (byte)11;
        }
        {
            FileDownloadList.getImpl().add(iRunningTask);
            if (FileDownloadHelper.inspectAndInflowDownloaded(baseDownloadTask.getId(), baseDownloadTask.getTargetFilePath(), baseDownloadTask.isForceReDownload(), true)) {
                return;
            }
            boolean bl = FileDownloadServiceProxy.getImpl().start(baseDownloadTask.getUrl(), baseDownloadTask.getPath(), baseDownloadTask.isPathAsDirectory(), baseDownloadTask.getCallbackProgressTimes(), baseDownloadTask.getCallbackProgressMinInterval(), baseDownloadTask.getAutoRetryTimes(), baseDownloadTask.isForceReDownload(), this.mTask.getHeader(), baseDownloadTask.isWifiRequired());
            if (this.mStatus == -2) {
                FileDownloadLog.w(this, "High concurrent cause, this task %d will be paused,because of the status is paused, so the pause action must be applied", this.getId());
                if (!bl) return;
                FileDownloadServiceProxy.getImpl().pause(this.getId());
                return;
            }
            if (bl) {
                iLostServiceConnectedHandler.taskWorkFine(iRunningTask);
                return;
            }
            if (iLostServiceConnectedHandler.dispatchTaskStart(iRunningTask)) return;
            object = new RuntimeException("Occur Unknown Error, when request to start maybe some problem in binder, maybe the process was killed in unexpected.");
            object = this.prepareErrorMessage((Throwable)object);
            if (FileDownloadList.getImpl().isNotContains(iRunningTask)) {
                iLostServiceConnectedHandler.taskWorkFine(iRunningTask);
                FileDownloadList.getImpl().add(iRunningTask);
            }
            FileDownloadList.getImpl().remove(iRunningTask, (MessageSnapshot)object);
            return;
        }
    }

    @Override
    public boolean updateKeepAhead(MessageSnapshot messageSnapshot) {
        if (!FileDownloadStatus.isKeepAhead(this.getStatus(), messageSnapshot.getStatus())) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "can't update mStatus change by keep ahead, %d, but the current mStatus is %d, %d", this.mStatus, this.getStatus(), this.getId());
            }
            return false;
        }
        this.update(messageSnapshot);
        return true;
    }

    @Override
    public boolean updateKeepFlow(MessageSnapshot messageSnapshot) {
        byte by = this.getStatus();
        byte by2 = messageSnapshot.getStatus();
        if (-2 == by && FileDownloadStatus.isIng(by2)) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "High concurrent cause, callback pending, but has already be paused %d", this.getId());
            }
            return true;
        }
        if (!FileDownloadStatus.isKeepFlow(by, by2)) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "can't update mStatus change by keep flow, %d, but the current mStatus is %d, %d", this.mStatus, this.getStatus(), this.getId());
            }
            return false;
        }
        this.update(messageSnapshot);
        return true;
    }

    @Override
    public boolean updateMoreLikelyCompleted(MessageSnapshot messageSnapshot) {
        if (!FileDownloadStatus.isMoreLikelyCompleted(this.mTask.getRunningTask().getOrigin())) {
            return false;
        }
        this.update(messageSnapshot);
        return true;
    }

    @Override
    public boolean updateSameFilePathTaskRunning(MessageSnapshot messageSnapshot) {
        if (!this.mTask.getRunningTask().getOrigin().isPathAsDirectory()) {
            return false;
        }
        if (messageSnapshot.getStatus() == -4 && this.getStatus() == 2) {
            this.update(messageSnapshot);
            return true;
        }
        return false;
    }

    static interface ICaptureTask {
        public ArrayList<BaseDownloadTask.FinishListener> getFinishListenerList();

        public FileDownloadHeader getHeader();

        public BaseDownloadTask.IRunningTask getRunningTask();

        public void setFileName(String var1);
    }
}

