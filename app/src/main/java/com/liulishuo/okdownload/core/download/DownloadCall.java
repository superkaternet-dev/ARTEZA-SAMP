/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.SystemClock
 */
package com.liulishuo.okdownload.core.download;

import android.os.SystemClock;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.core.NamedRunnable;
import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.breakpoint.BlockInfo;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.breakpoint.DownloadStore;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.liulishuo.okdownload.core.download.BreakpointLocalCheck;
import com.liulishuo.okdownload.core.download.BreakpointRemoteCheck;
import com.liulishuo.okdownload.core.download.DownloadCache;
import com.liulishuo.okdownload.core.download.DownloadChain;
import com.liulishuo.okdownload.core.file.ProcessFileStrategy;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DownloadCall
extends NamedRunnable
implements Comparable<DownloadCall> {
    private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), Util.threadFactory("OkDownload Block", false));
    static final int MAX_COUNT_RETRY_FOR_PRECONDITION_FAILED = 1;
    private static final String TAG = "DownloadCall";
    public final boolean asyncExecuted;
    private final ArrayList<DownloadChain> blockChainList;
    volatile DownloadCache cache;
    volatile boolean canceled;
    private volatile Thread currentThread;
    volatile boolean finishing;
    private final DownloadStore store;
    public final DownloadTask task;

    private DownloadCall(DownloadTask downloadTask, boolean bl, DownloadStore downloadStore) {
        this(downloadTask, bl, new ArrayList<DownloadChain>(), downloadStore);
    }

    DownloadCall(DownloadTask downloadTask, boolean bl, ArrayList<DownloadChain> arrayList, DownloadStore downloadStore) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("download call: ");
        stringBuilder.append(downloadTask.getId());
        super(stringBuilder.toString());
        this.task = downloadTask;
        this.asyncExecuted = bl;
        this.blockChainList = arrayList;
        this.store = downloadStore;
    }

    public static DownloadCall create(DownloadTask downloadTask, boolean bl, DownloadStore downloadStore) {
        return new DownloadCall(downloadTask, bl, downloadStore);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void inspectTaskEnd(DownloadCache downloadCache, EndCause endCause, Exception exception) {
        if (endCause == EndCause.CANCELED) {
            throw new IllegalAccessError("can't recognize cancelled on here");
        }
        synchronized (this) {
            if (this.canceled) {
                return;
            }
            this.finishing = true;
        }
        this.store.onTaskEnd(this.task.getId(), endCause, exception);
        if (endCause == EndCause.COMPLETED) {
            OkDownload.with().processFileStrategy().completeProcessStream(downloadCache.getOutputStream(), this.task);
        }
        OkDownload.with().callbackDispatcher().dispatch().taskEnd(this.task, endCause, exception);
    }

    private void inspectTaskStart() {
        this.store.onTaskStart(this.task.getId());
        OkDownload.with().callbackDispatcher().dispatch().taskStart(this.task);
    }

    void assembleBlockAndCallbackFromBeginning(BreakpointInfo breakpointInfo, BreakpointRemoteCheck breakpointRemoteCheck, ResumeFailedCause resumeFailedCause) {
        Util.assembleBlock(this.task, breakpointInfo, breakpointRemoteCheck.getInstanceLength(), breakpointRemoteCheck.isAcceptRange());
        OkDownload.with().callbackDispatcher().dispatch().downloadFromBeginning(this.task, breakpointInfo, resumeFailedCause);
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean cancel() {
        synchronized (this) {
            if (this.canceled) {
                return false;
            }
            if (this.finishing) {
                return false;
            }
            this.canceled = true;
            {
                catch (Throwable throwable) {}
                {
                    throw throwable;
                }
            }
        }
        long l = SystemClock.uptimeMillis();
        OkDownload.with().downloadDispatcher().flyingCanceled(this);
        Object object = this.cache;
        if (object != null) {
            ((DownloadCache)object).setUserCanceled();
        }
        Object object2 = (List)this.blockChainList.clone();
        Iterator iterator2 = object2.iterator();
        while (iterator2.hasNext()) {
            ((DownloadChain)iterator2.next()).cancel();
        }
        if (object2.isEmpty() && this.currentThread != null) {
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("interrupt thread with cancel operation because of chains are not running ");
            ((StringBuilder)object2).append(this.task.getId());
            Util.d(TAG, ((StringBuilder)object2).toString());
            this.currentThread.interrupt();
        }
        if (object != null) {
            ((DownloadCache)object).getOutputStream().cancelAsync();
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("cancel task ");
        ((StringBuilder)object).append(this.task.getId());
        ((StringBuilder)object).append(" consume: ");
        ((StringBuilder)object).append(SystemClock.uptimeMillis() - l);
        ((StringBuilder)object).append("ms");
        Util.d(TAG, ((StringBuilder)object).toString());
        return true;
    }

    @Override
    public int compareTo(DownloadCall downloadCall) {
        return downloadCall.getPriority() - this.getPriority();
    }

    DownloadCache createCache(BreakpointInfo breakpointInfo) {
        return new DownloadCache(OkDownload.with().processFileStrategy().createProcessStream(this.task, breakpointInfo, this.store));
    }

    BreakpointLocalCheck createLocalCheck(BreakpointInfo breakpointInfo, long l) {
        return new BreakpointLocalCheck(this.task, breakpointInfo, l);
    }

    BreakpointRemoteCheck createRemoteCheck(BreakpointInfo breakpointInfo) {
        return new BreakpointRemoteCheck(this.task, breakpointInfo);
    }

    public boolean equalsTask(DownloadTask downloadTask) {
        return this.task.equals(downloadTask);
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void execute() throws InterruptedException {
        void var5_19;
        Object object;
        Object object2;
        block20: {
            block21: {
                boolean bl;
                this.currentThread = Thread.currentThread();
                int n = 0;
                object2 = OkDownload.with();
                object = ((OkDownload)object2).processFileStrategy();
                this.inspectTaskStart();
                do {
                    int n2;
                    block19: {
                        BreakpointRemoteCheck breakpointRemoteCheck;
                        DownloadCache downloadCache;
                        void var5_7;
                        if (this.task.getUrl().length() <= 0) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("unexpected url: ");
                            stringBuilder.append(this.task.getUrl());
                            this.cache = new DownloadCache.PreError(new IOException(stringBuilder.toString()));
                            break;
                        }
                        if (this.canceled) break;
                        try {
                            BreakpointInfo breakpointInfo = this.store.get(this.task.getId());
                            if (breakpointInfo == null) {
                                BreakpointInfo breakpointInfo2 = this.store.createAndInsert(this.task);
                            }
                            this.setInfoToTask((BreakpointInfo)var5_7);
                            if (this.canceled) break;
                            this.cache = downloadCache = this.createCache((BreakpointInfo)var5_7);
                            breakpointRemoteCheck = this.createRemoteCheck((BreakpointInfo)var5_7);
                        }
                        catch (IOException iOException) {
                            this.cache = new DownloadCache.PreError(iOException);
                            break;
                        }
                        try {
                            breakpointRemoteCheck.check();
                            ((ProcessFileStrategy)object).getFileLock().waitForRelease(this.task.getFile().getAbsolutePath());
                            OkDownload.with().downloadStrategy().inspectAnotherSameInfo(this.task, (BreakpointInfo)var5_7, breakpointRemoteCheck.getInstanceLength());
                        }
                        catch (IOException iOException) {
                            downloadCache.catchException(iOException);
                            break;
                        }
                        try {
                            StringBuilder stringBuilder;
                            boolean bl2 = breakpointRemoteCheck.isResumable();
                            if (bl2) {
                                BreakpointLocalCheck breakpointLocalCheck = this.createLocalCheck((BreakpointInfo)var5_7, breakpointRemoteCheck.getInstanceLength());
                                breakpointLocalCheck.check();
                                if (breakpointLocalCheck.isDirty()) {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("breakpoint invalid: download from beginning because of local check is dirty ");
                                    stringBuilder.append(this.task.getId());
                                    stringBuilder.append(" ");
                                    stringBuilder.append(breakpointLocalCheck);
                                    Util.d(TAG, stringBuilder.toString());
                                    ((ProcessFileStrategy)object).discardProcess(this.task);
                                    this.assembleBlockAndCallbackFromBeginning((BreakpointInfo)var5_7, breakpointRemoteCheck, breakpointLocalCheck.getCauseOrThrow());
                                } else {
                                    ((OkDownload)object2).callbackDispatcher().dispatch().downloadFromBreakpoint(this.task, (BreakpointInfo)var5_7);
                                }
                            } else {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("breakpoint invalid: download from beginning because of remote check not resumable ");
                                stringBuilder.append(this.task.getId());
                                stringBuilder.append(" ");
                                stringBuilder.append(breakpointRemoteCheck);
                                Util.d(TAG, stringBuilder.toString());
                                ((ProcessFileStrategy)object).discardProcess(this.task);
                                this.assembleBlockAndCallbackFromBeginning((BreakpointInfo)var5_7, breakpointRemoteCheck, breakpointRemoteCheck.getCauseOrThrow());
                            }
                            this.start(downloadCache, (BreakpointInfo)var5_7);
                            if (this.canceled) break;
                            n2 = n;
                            if (!downloadCache.isPreconditionFailed()) break block19;
                            n2 = n + 1;
                            if (n >= 1) break block19;
                        }
                        catch (IOException iOException) {
                            downloadCache.setUnknownError(iOException);
                            break;
                        }
                        this.store.remove(this.task.getId());
                        bl = true;
                        n = n2;
                        continue;
                    }
                    n = n2;
                    bl = false;
                } while (bl);
                this.finishing = true;
                this.blockChainList.clear();
                object = this.cache;
                if (this.canceled) return;
                if (object == null) {
                    return;
                }
                object2 = null;
                if (((DownloadCache)object).isServerCanceled() || ((DownloadCache)object).isUnknownError() || ((DownloadCache)object).isPreconditionFailed()) break block21;
                if (((DownloadCache)object).isFileBusyAfterRun()) {
                    EndCause endCause = EndCause.FILE_BUSY;
                    break block20;
                } else if (((DownloadCache)object).isPreAllocateFailed()) {
                    EndCause endCause = EndCause.PRE_ALLOCATE_FAILED;
                    object2 = ((DownloadCache)object).getRealCause();
                    break block20;
                } else {
                    EndCause endCause = EndCause.COMPLETED;
                }
                break block20;
            }
            EndCause endCause = EndCause.ERROR;
            object2 = ((DownloadCache)object).getRealCause();
        }
        this.inspectTaskEnd((DownloadCache)object, (EndCause)var5_19, (Exception)object2);
    }

    @Override
    protected void finished() {
        OkDownload.with().downloadDispatcher().finish(this);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("call is finished ");
        stringBuilder.append(this.task.getId());
        Util.d(TAG, stringBuilder.toString());
    }

    public File getFile() {
        return this.task.getFile();
    }

    int getPriority() {
        return this.task.getPriority();
    }

    @Override
    protected void interrupted(InterruptedException interruptedException) {
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    public boolean isFinishing() {
        return this.finishing;
    }

    void setInfoToTask(BreakpointInfo breakpointInfo) {
        DownloadTask.TaskHideWrapper.setBreakpointInfo(this.task, breakpointInfo);
    }

    void start(DownloadCache downloadCache, BreakpointInfo breakpointInfo) throws InterruptedException {
        int n = breakpointInfo.getBlockCount();
        ArrayList<DownloadChain> arrayList = new ArrayList<DownloadChain>(breakpointInfo.getBlockCount());
        for (int i = 0; i < n; ++i) {
            BlockInfo blockInfo = breakpointInfo.getBlock(i);
            if (Util.isCorrectFull(blockInfo.getCurrentOffset(), blockInfo.getContentLength())) continue;
            Util.resetBlockIfDirty(blockInfo);
            arrayList.add(DownloadChain.createChain(i, this.task, breakpointInfo, downloadCache, this.store));
        }
        if (this.canceled) {
            return;
        }
        this.startBlocks(arrayList);
    }

    void startBlocks(List<DownloadChain> list) throws InterruptedException {
        Future future;
        Object object = new ArrayList(list.size());
        try {
            Iterator<DownloadChain> iterator2 = list.iterator();
            while (iterator2.hasNext()) {
                ((ArrayList)object).add(this.submitChain(iterator2.next()));
            }
            this.blockChainList.addAll(list);
            iterator2 = ((ArrayList)object).iterator();
            while (iterator2.hasNext()) {
                future = (Future)((Object)iterator2.next());
                boolean bl = future.isDone();
                if (bl) continue;
            }
        }
        catch (Throwable throwable) {
            try {
                object = ((ArrayList)object).iterator();
                while (object.hasNext()) {
                    ((Future)object.next()).cancel(true);
                }
                throw throwable;
            }
            catch (Throwable throwable2) {
                this.blockChainList.removeAll(list);
                throw throwable2;
            }
        }
        {
            try {
                future.get();
                continue;
            }
            catch (ExecutionException executionException) {
                continue;
            }
            catch (CancellationException cancellationException) {
                continue;
            }
        }
        this.blockChainList.removeAll(list);
    }

    Future<?> submitChain(DownloadChain downloadChain) {
        return EXECUTOR.submit(downloadChain);
    }
}

