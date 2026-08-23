/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.download;

import com.liulishuo.filedownloader.IThreadPoolMonitor;
import com.liulishuo.filedownloader.connection.FileDownloadConnection;
import com.liulishuo.filedownloader.database.FileDownloadDatabase;
import com.liulishuo.filedownloader.download.ConnectTask;
import com.liulishuo.filedownloader.download.ConnectionProfile;
import com.liulishuo.filedownloader.download.CustomComponentHolder;
import com.liulishuo.filedownloader.download.DownloadRunnable;
import com.liulishuo.filedownloader.download.DownloadStatusCallback;
import com.liulishuo.filedownloader.download.ProcessCallback;
import com.liulishuo.filedownloader.exception.FileDownloadGiveUpRetryException;
import com.liulishuo.filedownloader.exception.FileDownloadHttpException;
import com.liulishuo.filedownloader.exception.FileDownloadNetworkPolicyException;
import com.liulishuo.filedownloader.exception.FileDownloadOutOfSpaceException;
import com.liulishuo.filedownloader.exception.FileDownloadSecurityException;
import com.liulishuo.filedownloader.model.ConnectionModel;
import com.liulishuo.filedownloader.model.FileDownloadHeader;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.liulishuo.filedownloader.stream.FileDownloadOutputStream;
import com.liulishuo.filedownloader.util.FileDownloadExecutors;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadProperties;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

public class DownloadLaunchRunnable
implements Runnable,
ProcessCallback {
    private static final ThreadPoolExecutor DOWNLOAD_EXECUTOR = FileDownloadExecutors.newFixedThreadPool("ConnectionBlock");
    private static final int HTTP_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
    private static final int TOTAL_VALUE_IN_CHUNKED_RESOURCE = -1;
    private boolean acceptPartial;
    private final AtomicBoolean alive;
    private final FileDownloadDatabase database;
    private final int defaultConnectionCount;
    private final ArrayList<DownloadRunnable> downloadRunnableList = new ArrayList(5);
    private volatile boolean error;
    private volatile Exception errorException;
    private boolean isChunked;
    private final boolean isForceReDownload;
    private boolean isNeedForceDiscardRange = false;
    private boolean isResumeAvailableOnDB;
    private boolean isSingleConnection;
    private boolean isTriedFixRangeNotSatisfiable = false;
    private final boolean isWifiRequired;
    private long lastCallbackBytes = 0L;
    private long lastCallbackTimestamp = 0L;
    private long lastUpdateBytes = 0L;
    private long lastUpdateTimestamp = 0L;
    private final FileDownloadModel model;
    private volatile boolean paused = false;
    private String redirectedUrl;
    private DownloadRunnable singleDownloadRunnable;
    private final DownloadStatusCallback statusCallback;
    private final boolean supportSeek;
    private final IThreadPoolMonitor threadPoolMonitor;
    private final FileDownloadHeader userRequestHeader;
    int validRetryTimes;

    private DownloadLaunchRunnable(DownloadStatusCallback downloadStatusCallback, FileDownloadModel fileDownloadModel, FileDownloadHeader fileDownloadHeader, IThreadPoolMonitor iThreadPoolMonitor, int n, int n2, boolean bl, boolean bl2, int n3) {
        this.defaultConnectionCount = 5;
        this.alive = new AtomicBoolean(true);
        this.model = fileDownloadModel;
        this.userRequestHeader = fileDownloadHeader;
        this.isForceReDownload = bl;
        this.isWifiRequired = bl2;
        this.database = CustomComponentHolder.getImpl().getDatabaseInstance();
        this.supportSeek = CustomComponentHolder.getImpl().isSupportSeek();
        this.threadPoolMonitor = iThreadPoolMonitor;
        this.validRetryTimes = n3;
        this.statusCallback = downloadStatusCallback;
    }

    private DownloadLaunchRunnable(FileDownloadModel fileDownloadModel, FileDownloadHeader fileDownloadHeader, IThreadPoolMonitor iThreadPoolMonitor, int n, int n2, boolean bl, boolean bl2, int n3) {
        this.defaultConnectionCount = 5;
        this.alive = new AtomicBoolean(true);
        this.model = fileDownloadModel;
        this.userRequestHeader = fileDownloadHeader;
        this.isForceReDownload = bl;
        this.isWifiRequired = bl2;
        this.database = CustomComponentHolder.getImpl().getDatabaseInstance();
        this.supportSeek = CustomComponentHolder.getImpl().isSupportSeek();
        this.threadPoolMonitor = iThreadPoolMonitor;
        this.validRetryTimes = n3;
        this.statusCallback = new DownloadStatusCallback(fileDownloadModel, n3, n, n2);
    }

    private int calcConnectionCount(long l) {
        if (this.isMultiConnectionAvailable()) {
            if (this.isResumeAvailableOnDB) {
                return this.model.getConnectionCount();
            }
            return CustomComponentHolder.getImpl().determineConnectionCount(this.model.getId(), this.model.getUrl(), this.model.getPath(), l);
        }
        return 1;
    }

    private void checkupAfterGetFilename() throws RetryDirectly, DiscardSafely {
        int n = this.model.getId();
        if (this.model.isPathAsDirectory()) {
            Object object = this.model.getTargetFilePath();
            int n2 = FileDownloadUtils.generateId(this.model.getUrl(), (String)object);
            if (!FileDownloadHelper.inspectAndInflowDownloaded(n, (String)object, this.isForceReDownload, false)) {
                FileDownloadModel fileDownloadModel = this.database.find(n2);
                if (fileDownloadModel != null) {
                    if (!FileDownloadHelper.inspectAndInflowDownloading(n, fileDownloadModel, this.threadPoolMonitor, false)) {
                        Object object2 = this.database.findConnectionModel(n2);
                        this.database.remove(n2);
                        this.database.removeConnections(n2);
                        FileDownloadUtils.deleteTargetFile(this.model.getTargetFilePath());
                        if (FileDownloadUtils.isBreakpointAvailable(n2, fileDownloadModel)) {
                            this.model.setSoFar(fileDownloadModel.getSoFar());
                            this.model.setTotal(fileDownloadModel.getTotal());
                            this.model.setETag(fileDownloadModel.getETag());
                            this.model.setConnectionCount(fileDownloadModel.getConnectionCount());
                            this.database.update(this.model);
                            if (object2 != null) {
                                object = object2.iterator();
                                while (object.hasNext()) {
                                    object2 = (ConnectionModel)object.next();
                                    ((ConnectionModel)object2).setId(n);
                                    this.database.insertConnectionModel((ConnectionModel)object2);
                                }
                            }
                            throw new RetryDirectly(this);
                        }
                    } else {
                        this.database.remove(n);
                        this.database.removeConnections(n);
                        throw new DiscardSafely(this);
                    }
                }
                if (FileDownloadHelper.inspectAndInflowConflictPath(n, this.model.getSoFar(), this.model.getTempFilePath(), (String)object, this.threadPoolMonitor)) {
                    this.database.remove(n);
                    this.database.removeConnections(n);
                    throw new DiscardSafely(this);
                }
            } else {
                this.database.remove(n);
                this.database.removeConnections(n);
                throw new DiscardSafely(this);
            }
        }
    }

    private void checkupBeforeConnect() throws FileDownloadGiveUpRetryException {
        if (this.isWifiRequired && !FileDownloadUtils.checkPermission("android.permission.ACCESS_NETWORK_STATE")) {
            throw new FileDownloadGiveUpRetryException(FileDownloadUtils.formatString("Task[%d] can't start the download runnable, because this task require wifi, but user application nor current process has %s, so we can't check whether the network type connection.", this.model.getId(), "android.permission.ACCESS_NETWORK_STATE"));
        }
        if (this.isWifiRequired && FileDownloadUtils.isNetworkNotOnWifiType()) {
            throw new FileDownloadNetworkPolicyException();
        }
    }

    static DownloadLaunchRunnable createForTest(DownloadStatusCallback downloadStatusCallback, FileDownloadModel fileDownloadModel, FileDownloadHeader fileDownloadHeader, IThreadPoolMonitor iThreadPoolMonitor, int n, int n2, boolean bl, boolean bl2, int n3) {
        return new DownloadLaunchRunnable(downloadStatusCallback, fileDownloadModel, fileDownloadHeader, iThreadPoolMonitor, n, n2, bl, bl2, n3);
    }

    private int determineConnectionCount() {
        return 5;
    }

    /*
     * WARNING - void declaration
     */
    private void fetchWithMultipleConnection(List<ConnectionModel> object4, long l) throws InterruptedException {
        void var2_8;
        int n = this.model.getId();
        Object object = this.model.getETag();
        Object object2 = this.redirectedUrl;
        if (object2 == null) {
            object2 = this.model.getUrl();
        }
        String string2 = this.model.getTempFilePath();
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "fetch data with multiple connection(count: [%d]) for task[%d] totalLength[%d]", object4.size(), n, (long)var2_8);
        }
        long l2 = 0L;
        boolean bl = this.isResumeAvailableOnDB;
        Iterator iterator2 = object4.iterator();
        String string3 = object;
        while (iterator2.hasNext()) {
            ConnectionModel connectionModel = (ConnectionModel)iterator2.next();
            long l3 = connectionModel.getEndOffset() == -1L ? var2_8 - connectionModel.getCurrentOffset() : connectionModel.getEndOffset() - connectionModel.getCurrentOffset() + 1L;
            l2 += connectionModel.getCurrentOffset() - connectionModel.getStartOffset();
            if (l3 == 0L) {
                if (!FileDownloadLog.NEED_LOG) continue;
                FileDownloadLog.d(this, "pass connection[%d-%d], because it has been completed", connectionModel.getId(), connectionModel.getIndex());
                continue;
            }
            object = new DownloadRunnable.Builder();
            ConnectionProfile connectionProfile = ConnectionProfile.ConnectionProfileBuild.buildConnectionProfile(connectionModel.getStartOffset(), connectionModel.getCurrentOffset(), connectionModel.getEndOffset(), l3);
            DownloadRunnable.Builder builder = ((DownloadRunnable.Builder)object).setId(n).setConnectionIndex(connectionModel.getIndex()).setCallback(this).setUrl((String)object2);
            object = bl ? string3 : null;
            object = builder.setEtag((String)object).setHeader(this.userRequestHeader).setWifiRequired(this.isWifiRequired).setConnectionModel(connectionProfile).setPath(string2).build();
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "enable multiple connection: %s", connectionModel);
            }
            if (object != null) {
                this.downloadRunnableList.add((DownloadRunnable)object);
                continue;
            }
            throw new IllegalArgumentException("the download runnable must not be null!");
        }
        if (l2 != this.model.getSoFar()) {
            FileDownloadLog.w(this, "correct the sofar[%d] from connection table[%d]", this.model.getSoFar(), l2);
            this.model.setSoFar(l2);
        }
        object2 = new ArrayList(this.downloadRunnableList.size());
        for (DownloadRunnable downloadRunnable : this.downloadRunnableList) {
            if (this.paused) {
                downloadRunnable.pause();
                continue;
            }
            object2.add(Executors.callable(downloadRunnable));
        }
        if (this.paused) {
            this.model.setStatus((byte)-2);
            return;
        }
        List list = DOWNLOAD_EXECUTOR.invokeAll(object2);
        if (FileDownloadLog.NEED_LOG) {
            for (Future future : list) {
                FileDownloadLog.d(this, "finish sub-task for [%d] %B %B", n, future.isDone(), future.isCancelled());
            }
        }
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void handlePreAllocate(long l, String object) throws IOException, IllegalAccessException {
        block11: {
            FileDownloadOutputStream fileDownloadOutputStream = null;
            FileDownloadOutputStream fileDownloadOutputStream2 = null;
            if (l != -1L) {
                void var3_5;
                block10: {
                    long l3;
                    long l4;
                    try {
                        fileDownloadOutputStream2 = fileDownloadOutputStream = FileDownloadUtils.createOutputStream(this.model.getTempFilePath());
                    }
                    catch (Throwable throwable) {
                        fileDownloadOutputStream = fileDownloadOutputStream2;
                        break block10;
                    }
                    try {
                        File file = new File((String)object);
                        l4 = file.length();
                        l3 = l - l4;
                    }
                    catch (Throwable throwable) {
                        break block10;
                    }
                    {
                        long l2 = FileDownloadUtils.getFreeSpaceBytes((String)object);
                        if (l2 < l3) {
                            object = new FileDownloadOutOfSpaceException(l2, l3, l4);
                            throw object;
                        }
                        object = fileDownloadOutputStream;
                        if (!FileDownloadProperties.getImpl().fileNonPreAllocation) {
                            fileDownloadOutputStream.setLength(l);
                            object = fileDownloadOutputStream;
                        }
                        break block11;
                    }
                }
                if (fileDownloadOutputStream != null) {
                    fileDownloadOutputStream.close();
                }
                throw var3_5;
            }
            object = fileDownloadOutputStream;
        }
        if (object != null) {
            object.close();
        }
    }

    /*
     * Unable to fully structure code
     */
    private void handleTrialConnectResult(Map<String, List<String>> var1_1, ConnectTask var2_2, FileDownloadConnection var3_3) throws IOException, RetryDirectly, IllegalArgumentException, FileDownloadSecurityException {
        block11: {
            block14: {
                block13: {
                    block12: {
                        block10: {
                            var7_4 = this.model.getId();
                            var6_5 = var3_3.getResponseCode();
                            this.acceptPartial = FileDownloadUtils.isAcceptRange(var6_5, var3_3);
                            var5_6 = var6_5 == 200 || var6_5 == 201 || var6_5 == 0;
                            var9_7 = FileDownloadUtils.findInstanceLengthForTrial(var3_3);
                            var12_8 = this.model.getETag();
                            var11_9 = FileDownloadUtils.findEtag(var7_4, var3_3);
                            if (var6_5 != 412) break block10;
                            var4_10 = true;
                            break block11;
                        }
                        if (var12_8 == null || var12_8.equals(var11_9) || !var5_6 && !this.acceptPartial) break block12;
                        var4_10 = true;
                        break block11;
                    }
                    if (var6_5 != 201 || !var2_2.isRangeNotFromBeginning()) break block13;
                    var4_10 = true;
                    break block11;
                }
                if (var6_5 != 416) ** GOTO lbl-1000
                if (!this.acceptPartial || var9_7 < 0L) break block14;
                FileDownloadLog.w(this, "get 416 but the Content-Range is returned, no need to retry", new Object[0]);
                ** GOTO lbl-1000
            }
            if (this.model.getSoFar() > 0L) {
                FileDownloadLog.w(this, "get 416, precondition failed and just retry", new Object[0]);
                var4_10 = true;
            } else if (!this.isNeedForceDiscardRange) {
                this.isNeedForceDiscardRange = true;
                FileDownloadLog.w(this, "get 416, precondition failed and need to retry with discarding range", new Object[0]);
                var4_10 = true;
            } else lbl-1000:
            // 3 sources

            {
                var4_10 = false;
            }
        }
        if (var4_10) {
            if (this.isResumeAvailableOnDB) {
                FileDownloadLog.w(this, "there is precondition failed on this request[%d] with old etag[%s]\u3001new etag[%s]\u3001response code is %d", new Object[]{var7_4, var12_8, var11_9, var6_5});
            }
            this.database.removeConnections(this.model.getId());
            FileDownloadUtils.deleteTaskFiles(this.model.getTargetFilePath(), this.model.getTempFilePath());
            this.isResumeAvailableOnDB = false;
            if (var12_8 != null && var12_8.equals(var11_9)) {
                FileDownloadLog.w(this, "the old etag[%s] is the same to the new etag[%s], but the response status code is %d not Partial(206), so wo have to start this task from very beginning for task[%d]!", new Object[]{var12_8, var11_9, var6_5, var7_4});
                var1_1 = null;
            } else {
                var1_1 = var11_9;
            }
            this.model.setSoFar(0L);
            this.model.setTotal(0L);
            this.model.setETag((String)var1_1);
            this.model.resetConnectionCount();
            this.database.updateOldEtagOverdue(var7_4, this.model.getETag(), this.model.getSoFar(), this.model.getTotal(), this.model.getConnectionCount());
            throw new RetryDirectly(this);
        }
        this.redirectedUrl = var2_2.getFinalRedirectedUrl();
        if (!this.acceptPartial && !var5_6) {
            throw new FileDownloadHttpException(var6_5, (Map<String, List<String>>)var1_1, var3_3.getResponseHeaderFields());
        }
        var1_1 = null;
        if (this.model.isPathAsDirectory()) {
            var1_1 = FileDownloadUtils.findFilename(var3_3, this.model.getUrl());
        }
        var8_11 = var9_7 == -1L;
        this.isChunked = var8_11;
        var2_2 = this.statusCallback;
        var8_11 = this.isResumeAvailableOnDB != false && this.acceptPartial != false;
        var2_2.onConnected(var8_11, var9_7, var11_9, (String)var1_1);
    }

    private boolean isMultiConnectionAvailable() {
        boolean bl = this.isResumeAvailableOnDB;
        boolean bl2 = false;
        if (bl && this.model.getConnectionCount() <= 1) {
            return false;
        }
        bl = bl2;
        if (this.acceptPartial) {
            bl = bl2;
            if (this.supportSeek) {
                bl = bl2;
                if (!this.isChunked) {
                    bl = true;
                }
            }
        }
        return bl;
    }

    private void realDownloadWithMultiConnectionFromBeginning(long l, int n) throws InterruptedException {
        long l2 = 0L;
        long l3 = l / (long)n;
        int n2 = this.model.getId();
        ArrayList<ConnectionModel> arrayList = new ArrayList<ConnectionModel>();
        for (int i = 0; i < n; ++i) {
            long l4 = i == n - 1 ? -1L : l2 + l3 - 1L;
            ConnectionModel connectionModel = new ConnectionModel();
            connectionModel.setId(n2);
            connectionModel.setIndex(i);
            connectionModel.setStartOffset(l2);
            connectionModel.setCurrentOffset(l2);
            connectionModel.setEndOffset(l4);
            arrayList.add(connectionModel);
            this.database.insertConnectionModel(connectionModel);
            l2 += l3;
        }
        this.model.setConnectionCount(n);
        this.database.updateConnectionCount(n2, n);
        this.fetchWithMultipleConnection(arrayList, l);
    }

    private void realDownloadWithMultiConnectionFromResume(int n, List<ConnectionModel> list) throws InterruptedException {
        if (n > 1 && list.size() == n) {
            this.fetchWithMultipleConnection(list, this.model.getTotal());
            return;
        }
        throw new IllegalArgumentException();
    }

    private void realDownloadWithSingleConnection(long l) throws IOException, IllegalAccessException {
        ConnectionProfile connectionProfile;
        if (!this.acceptPartial) {
            this.model.setSoFar(0L);
            connectionProfile = ConnectionProfile.ConnectionProfileBuild.buildBeginToEndConnectionProfile(l);
        } else {
            connectionProfile = ConnectionProfile.ConnectionProfileBuild.buildToEndConnectionProfile(this.model.getSoFar(), this.model.getSoFar(), l - this.model.getSoFar());
        }
        this.singleDownloadRunnable = new DownloadRunnable.Builder().setId(this.model.getId()).setConnectionIndex(-1).setCallback(this).setUrl(this.model.getUrl()).setEtag(this.model.getETag()).setHeader(this.userRequestHeader).setWifiRequired(this.isWifiRequired).setConnectionModel(connectionProfile).setPath(this.model.getTempFilePath()).build();
        this.model.setConnectionCount(1);
        this.database.updateConnectionCount(this.model.getId(), 1);
        if (this.paused) {
            this.model.setStatus((byte)-2);
            this.singleDownloadRunnable.pause();
        } else {
            this.singleDownloadRunnable.run();
        }
    }

    private void trialConnect() throws IOException, RetryDirectly, IllegalAccessException, FileDownloadSecurityException {
        Object object;
        Object object2;
        FileDownloadConnection fileDownloadConnection;
        block12: {
            block11: {
                fileDownloadConnection = null;
                object2 = fileDownloadConnection;
                try {
                    if (!this.isNeedForceDiscardRange) break block11;
                    object2 = fileDownloadConnection;
                }
                catch (Throwable throwable) {
                    if (object2 != null) {
                        object2.ending();
                    }
                    throw throwable;
                }
                object = ConnectionProfile.ConnectionProfileBuild.buildTrialConnectionProfileNoRange();
                break block12;
            }
            object2 = fileDownloadConnection;
            object = ConnectionProfile.ConnectionProfileBuild.buildTrialConnectionProfile();
        }
        object2 = fileDownloadConnection;
        object2 = fileDownloadConnection;
        Object object3 = new ConnectTask.Builder();
        object2 = fileDownloadConnection;
        object3 = ((ConnectTask.Builder)object3).setDownloadId(this.model.getId()).setUrl(this.model.getUrl()).setEtag(this.model.getETag()).setHeader(this.userRequestHeader).setConnectionProfile((ConnectionProfile)object).build();
        object2 = fileDownloadConnection;
        object2 = object = ((ConnectTask)object3).connect();
        this.handleTrialConnectResult(((ConnectTask)object3).getRequestHeader(), (ConnectTask)object3, (FileDownloadConnection)object);
        if (object != null) {
            object.ending();
        }
    }

    public int getId() {
        return this.model.getId();
    }

    public String getTempFilePath() {
        return this.model.getTempFilePath();
    }

    void inspectTaskModelResumeAvailableOnDB(List<ConnectionModel> list) {
        int n = this.model.getConnectionCount();
        String string2 = this.model.getTempFilePath();
        String string3 = this.model.getTargetFilePath();
        boolean bl = false;
        boolean bl2 = n > 1;
        long l = this.isNeedForceDiscardRange ? 0L : (bl2 && !this.supportSeek ? 0L : (FileDownloadUtils.isBreakpointAvailable(this.model.getId(), this.model) ? (!this.supportSeek ? new File(string2).length() : (bl2 ? (n != list.size() ? 0L : ConnectionModel.getTotalOffset(list)) : this.model.getSoFar())) : 0L));
        this.model.setSoFar(l);
        if (l > 0L) {
            bl = true;
        }
        this.isResumeAvailableOnDB = bl;
        if (!bl) {
            this.database.removeConnections(this.model.getId());
            FileDownloadUtils.deleteTaskFiles(string3, string2);
        }
    }

    public boolean isAlive() {
        boolean bl = this.alive.get() || this.statusCallback.isAlive();
        return bl;
    }

    @Override
    public boolean isRetry(Exception exception) {
        boolean bl = exception instanceof FileDownloadHttpException;
        boolean bl2 = true;
        if (bl) {
            int n = ((FileDownloadHttpException)exception).getCode();
            if (this.isSingleConnection && n == 416 && !this.isTriedFixRangeNotSatisfiable) {
                FileDownloadUtils.deleteTaskFiles(this.model.getTargetFilePath(), this.model.getTempFilePath());
                this.isTriedFixRangeNotSatisfiable = true;
                return true;
            }
        }
        if (this.validRetryTimes <= 0 || exception instanceof FileDownloadGiveUpRetryException) {
            bl2 = false;
        }
        return bl2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void onCompleted(DownloadRunnable downloadRunnable, long l, long l2) {
        if (this.paused) {
            if (!FileDownloadLog.NEED_LOG) return;
            FileDownloadLog.d(this, "the task[%d] has already been paused, so pass the completed callback", this.model.getId());
            return;
        }
        int n = downloadRunnable.connectionIndex;
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "the connection has been completed(%d): [%d, %d)  %d", n, l, l2, this.model.getTotal());
        }
        if (this.isSingleConnection) {
            if (l == 0L) return;
            if (l2 == this.model.getTotal()) return;
            FileDownloadLog.e((Object)this, "the single task not completed corrected(%d, %d != %d) for task(%d)", l, l2, this.model.getTotal(), this.model.getId());
            return;
        }
        ArrayList<DownloadRunnable> arrayList = this.downloadRunnableList;
        synchronized (arrayList) {
            this.downloadRunnableList.remove(downloadRunnable);
            return;
        }
    }

    @Override
    public void onError(Exception object) {
        this.error = true;
        this.errorException = object;
        if (this.paused) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "the task[%d] has already been paused, so pass the error callback", this.model.getId());
            }
            return;
        }
        for (DownloadRunnable downloadRunnable : (ArrayList)this.downloadRunnableList.clone()) {
            if (downloadRunnable == null) continue;
            downloadRunnable.discard();
        }
    }

    @Override
    public void onProgress(long l) {
        if (this.paused) {
            return;
        }
        this.statusCallback.onProgress(l);
    }

    @Override
    public void onRetry(Exception exception) {
        int n;
        if (this.paused) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "the task[%d] has already been paused, so pass the retry callback", this.model.getId());
            }
            return;
        }
        int n2 = this.validRetryTimes;
        this.validRetryTimes = n = n2 - 1;
        if (n2 < 0) {
            FileDownloadLog.e((Object)this, "valid retry times is less than 0(%d) for download task(%d)", n, this.model.getId());
        }
        this.statusCallback.onRetry(exception, this.validRetryTimes);
    }

    public void pause() {
        this.paused = true;
        DownloadRunnable downloadRunnable2 = this.singleDownloadRunnable;
        if (downloadRunnable2 != null) {
            downloadRunnable2.pause();
        }
        for (DownloadRunnable downloadRunnable2 : (ArrayList)this.downloadRunnableList.clone()) {
            if (downloadRunnable2 == null) continue;
            downloadRunnable2.pause();
        }
    }

    public void pending() {
        this.inspectTaskModelResumeAvailableOnDB(this.database.findConnectionModel(this.model.getId()));
        this.statusCallback.onPending();
    }

    /*
     * Exception decompiling
     */
    @Override
    public void run() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [35[UNCONDITIONALDOLOOP]], but top level block is 15[TRYBLOCK]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    @Override
    public void syncProgressFromCache() {
        this.database.updateProgress(this.model.getId(), this.model.getSoFar());
    }

    public static class Builder {
        private Integer callbackProgressMaxCount;
        private FileDownloadHeader header;
        private Boolean isForceReDownload;
        private Boolean isWifiRequired;
        private Integer maxRetryTimes;
        private Integer minIntervalMillis;
        private FileDownloadModel model;
        private IThreadPoolMonitor threadPoolMonitor;

        public DownloadLaunchRunnable build() {
            if (this.model != null && this.threadPoolMonitor != null && this.minIntervalMillis != null && this.callbackProgressMaxCount != null && this.isForceReDownload != null && this.isWifiRequired != null && this.maxRetryTimes != null) {
                return new DownloadLaunchRunnable(this.model, this.header, this.threadPoolMonitor, this.minIntervalMillis, (int)this.callbackProgressMaxCount, this.isForceReDownload, (boolean)this.isWifiRequired, this.maxRetryTimes);
            }
            throw new IllegalArgumentException();
        }

        public Builder setCallbackProgressMaxCount(Integer n) {
            this.callbackProgressMaxCount = n;
            return this;
        }

        public Builder setForceReDownload(Boolean bl) {
            this.isForceReDownload = bl;
            return this;
        }

        public Builder setHeader(FileDownloadHeader fileDownloadHeader) {
            this.header = fileDownloadHeader;
            return this;
        }

        public Builder setMaxRetryTimes(Integer n) {
            this.maxRetryTimes = n;
            return this;
        }

        public Builder setMinIntervalMillis(Integer n) {
            this.minIntervalMillis = n;
            return this;
        }

        public Builder setModel(FileDownloadModel fileDownloadModel) {
            this.model = fileDownloadModel;
            return this;
        }

        public Builder setThreadPoolMonitor(IThreadPoolMonitor iThreadPoolMonitor) {
            this.threadPoolMonitor = iThreadPoolMonitor;
            return this;
        }

        public Builder setWifiRequired(Boolean bl) {
            this.isWifiRequired = bl;
            return this;
        }
    }

    class DiscardSafely
    extends Throwable {
        final DownloadLaunchRunnable this$0;

        DiscardSafely(DownloadLaunchRunnable downloadLaunchRunnable) {
            this.this$0 = downloadLaunchRunnable;
        }
    }

    class RetryDirectly
    extends Throwable {
        final DownloadLaunchRunnable this$0;

        RetryDirectly(DownloadLaunchRunnable downloadLaunchRunnable) {
            this.this$0 = downloadLaunchRunnable;
        }
    }
}

