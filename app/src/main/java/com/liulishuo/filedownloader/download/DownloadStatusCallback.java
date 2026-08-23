/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.database.sqlite.SQLiteFullException
 *  android.os.Build$VERSION
 *  android.os.Handler
 *  android.os.Handler$Callback
 *  android.os.HandlerThread
 *  android.os.Message
 *  android.os.SystemClock
 */
package com.liulishuo.filedownloader.download;

import android.database.sqlite.SQLiteFullException;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import com.liulishuo.filedownloader.database.FileDownloadDatabase;
import com.liulishuo.filedownloader.download.CustomComponentHolder;
import com.liulishuo.filedownloader.exception.FileDownloadGiveUpRetryException;
import com.liulishuo.filedownloader.exception.FileDownloadOutOfSpaceException;
import com.liulishuo.filedownloader.message.MessageSnapshotFlow;
import com.liulishuo.filedownloader.message.MessageSnapshotTaker;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.liulishuo.filedownloader.services.FileDownloadBroadcastHandler;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadProperties;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

public class DownloadStatusCallback
implements Handler.Callback {
    private static final String ALREADY_DEAD_MESSAGE = "require callback %d but the host thread of the flow has already dead, what is occurred because of there are several reason can final this flow on different thread.";
    private static final int CALLBACK_SAFE_MIN_INTERVAL_BYTES = 1;
    private static final int CALLBACK_SAFE_MIN_INTERVAL_MILLIS = 5;
    private static final int NO_ANY_PROGRESS_CALLBACK = -1;
    private final AtomicLong callbackIncreaseBuffer = new AtomicLong();
    private long callbackMinIntervalBytes;
    private final int callbackProgressMaxCount;
    private final int callbackProgressMinInterval;
    private final FileDownloadDatabase database;
    private Handler handler;
    private HandlerThread handlerThread;
    private volatile boolean handlingMessage = false;
    private final AtomicBoolean isFirstCallback;
    private volatile long lastCallbackTimestamp = 0L;
    private final int maxRetryTimes;
    private final FileDownloadModel model;
    private final AtomicBoolean needCallbackProgressToUser = new AtomicBoolean(false);
    private final AtomicBoolean needSetProcess = new AtomicBoolean(false);
    private volatile Thread parkThread;
    private final ProcessParams processParams;

    DownloadStatusCallback(FileDownloadModel fileDownloadModel, int n, int n2, int n3) {
        this.isFirstCallback = new AtomicBoolean(true);
        this.model = fileDownloadModel;
        this.database = CustomComponentHolder.getImpl().getDatabaseInstance();
        int n4 = 5;
        if (n2 < 5) {
            n2 = n4;
        }
        this.callbackProgressMinInterval = n2;
        this.callbackProgressMaxCount = n3;
        this.processParams = new ProcessParams();
        this.maxRetryTimes = n;
    }

    private static long calculateCallbackMinIntervalBytes(long l, long l2) {
        block2: {
            if (l2 <= 0L) {
                return -1L;
            }
            long l3 = 1L;
            if (l == -1L) {
                return 1L;
            }
            if ((l /= l2) > 0L) break block2;
            l = l3;
        }
        return l;
    }

    private Exception exFiltrate(Exception exception) {
        block2: {
            long l;
            long l2;
            Object object = this.model.getTempFilePath();
            if (!this.model.isChunked() && !FileDownloadProperties.getImpl().fileNonPreAllocation || !(exception instanceof IOException) || !new File((String)object).exists() || (l2 = FileDownloadUtils.getFreeSpaceBytes((String)object)) > 4096L) break block2;
            if (!((File)(object = new File((String)object))).exists()) {
                FileDownloadLog.e((Object)this, exception, "Exception with: free space isn't enough, and the target file not exist.", new Object[0]);
                l = 0L;
            } else {
                l = ((File)object).length();
            }
            exception = Build.VERSION.SDK_INT >= 9 ? new FileDownloadOutOfSpaceException(l2, 4096L, l, exception) : new FileDownloadOutOfSpaceException(l2, 4096L, l);
        }
        return exception;
    }

    private void handleCompleted() throws IOException {
        this.renameTempFile();
        this.model.setStatus((byte)-3);
        this.database.updateCompleted(this.model.getId(), this.model.getTotal());
        this.database.removeConnections(this.model.getId());
        this.onStatusChanged((byte)-3);
        if (FileDownloadProperties.getImpl().broadcastCompleted) {
            FileDownloadBroadcastHandler.sendCompletedBroadcast(this.model);
        }
    }

    private void handleError(Exception exception) {
        Exception exception2 = this.exFiltrate(exception);
        if (exception2 instanceof SQLiteFullException) {
            this.handleSQLiteFullException((SQLiteFullException)((Object)exception2));
            exception = exception2;
        } else {
            try {
                this.model.setStatus((byte)-1);
                this.model.setErrMsg(exception.toString());
                this.database.updateError(this.model.getId(), exception2, this.model.getSoFar());
                exception = exception2;
            }
            catch (SQLiteFullException sQLiteFullException) {
                this.handleSQLiteFullException(sQLiteFullException);
            }
        }
        this.processParams.setException(exception);
        this.onStatusChanged((byte)-1);
    }

    private void handlePaused() {
        this.model.setStatus((byte)-2);
        this.database.updatePause(this.model.getId(), this.model.getSoFar());
        this.onStatusChanged((byte)-2);
    }

    private void handleProgress() {
        if (this.model.getSoFar() == this.model.getTotal()) {
            this.database.updateProgress(this.model.getId(), this.model.getSoFar());
            return;
        }
        if (this.needSetProcess.compareAndSet(true, false)) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.i(this, "handleProgress update model's status with progress", new Object[0]);
            }
            this.model.setStatus((byte)3);
        }
        if (this.needCallbackProgressToUser.compareAndSet(true, false)) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.i(this, "handleProgress notify user progress status", new Object[0]);
            }
            this.onStatusChanged((byte)3);
        }
    }

    private void handleRetry(Exception exception, int n) {
        exception = this.exFiltrate(exception);
        this.processParams.setException(exception);
        this.processParams.setRetryingTimes(this.maxRetryTimes - n);
        this.model.setStatus((byte)5);
        this.model.setErrMsg(exception.toString());
        this.database.updateRetry(this.model.getId(), exception);
        this.onStatusChanged((byte)5);
    }

    private void handleSQLiteFullException(SQLiteFullException sQLiteFullException) {
        int n = this.model.getId();
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "the data of the task[%d] is dirty, because the SQLite full exception[%s], so remove it from the database directly.", n, sQLiteFullException.toString());
        }
        this.model.setErrMsg(sQLiteFullException.toString());
        this.model.setStatus((byte)-1);
        this.database.remove(n);
        this.database.removeConnections(n);
    }

    private void inspectNeedCallbackToUser(long l) {
        boolean bl;
        if (this.isFirstCallback.compareAndSet(true, false)) {
            bl = true;
        } else {
            long l2 = this.lastCallbackTimestamp;
            bl = this.callbackMinIntervalBytes != -1L && this.callbackIncreaseBuffer.get() >= this.callbackMinIntervalBytes && l - l2 >= (long)this.callbackProgressMinInterval;
        }
        if (bl && this.needCallbackProgressToUser.compareAndSet(false, true)) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.i(this, "inspectNeedCallbackToUser need callback to user", new Object[0]);
            }
            this.lastCallbackTimestamp = l;
            this.callbackIncreaseBuffer.set(0L);
        }
    }

    private boolean interceptBeforeCompleted() {
        if (this.model.isChunked()) {
            FileDownloadModel fileDownloadModel = this.model;
            fileDownloadModel.setTotal(fileDownloadModel.getSoFar());
        } else if (this.model.getSoFar() != this.model.getTotal()) {
            this.onErrorDirectly(new FileDownloadGiveUpRetryException(FileDownloadUtils.formatString("sofar[%d] not equal total[%d]", this.model.getSoFar(), this.model.getTotal())));
            return true;
        }
        return false;
    }

    private void onStatusChanged(byte by) {
        if (by == -2) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "High concurrent cause, Already paused and we don't need to call-back to Task in here, %d", this.model.getId());
            }
            return;
        }
        MessageSnapshotFlow.getImpl().inflow(MessageSnapshotTaker.take(by, this.model, this.processParams));
    }

    private void renameTempFile() throws IOException {
        Serializable serializable;
        boolean bl;
        boolean bl2;
        String string2;
        String string3;
        block17: {
            block18: {
                File file;
                block15: {
                    long l;
                    block16: {
                        string3 = this.model.getTempFilePath();
                        string2 = this.model.getTargetFilePath();
                        file = new File(string3);
                        bl = bl2 = true;
                        bl = bl2;
                        try {
                            serializable = new File(string2);
                            bl = bl2;
                        }
                        catch (Throwable throwable) {
                            if (bl && file.exists() && !file.delete()) {
                                FileDownloadLog.w(this, "delete the temp file(%s) failed, on completed downloading.", string3);
                            }
                            throw throwable;
                        }
                        if (!serializable.exists()) break block15;
                        bl = bl2;
                        l = serializable.length();
                        bl = bl2;
                        if (!serializable.delete()) break block16;
                        bl = bl2;
                        FileDownloadLog.w(this, "The target file([%s], [%d]) will be replaced with the new downloaded file[%d]", string2, l, file.length());
                        break block15;
                    }
                    bl = bl2;
                    bl = bl2;
                    serializable = new IOException(FileDownloadUtils.formatString("Can't delete the old file([%s], [%d]), so can't replace it with the new downloaded one.", string2, l));
                    bl = bl2;
                    throw serializable;
                }
                bl = bl2;
                boolean bl3 = file.renameTo((File)serializable);
                bl2 = bl3 ^ true;
                if (bl2) break block17;
                if (!bl2 || !file.exists() || file.delete()) break block18;
                FileDownloadLog.w(this, "delete the temp file(%s) failed, on completed downloading.", string3);
            }
            return;
        }
        bl = bl2;
        bl = bl2;
        serializable = new IOException(FileDownloadUtils.formatString("Can't rename the  temp downloaded file(%s) to the target file(%s)", string3, string2));
        bl = bl2;
        throw serializable;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void sendMessage(Message message) {
        synchronized (this) {
            if (!this.handlerThread.isAlive()) {
                if (!FileDownloadLog.NEED_LOG) return;
                FileDownloadLog.d(this, ALREADY_DEAD_MESSAGE, message.what);
                return;
            }
            try {
                this.handler.sendMessage(message);
                return;
            }
            catch (IllegalStateException illegalStateException) {
                if (this.handlerThread.isAlive()) throw illegalStateException;
                if (!FileDownloadLog.NEED_LOG) return;
                FileDownloadLog.d(this, ALREADY_DEAD_MESSAGE, message.what);
                return;
            }
        }
    }

    void discardAllMessage() {
        Handler handler = this.handler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            this.handlerThread.quit();
            this.parkThread = Thread.currentThread();
            while (this.handlingMessage) {
                LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(100L));
            }
            this.parkThread = null;
        }
    }

    /*
     * Exception decompiling
     */
    public boolean handleMessage(Message var1_1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [3[CASE]], but top level block is 0[TRYBLOCK]
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

    public boolean isAlive() {
        HandlerThread handlerThread = this.handlerThread;
        boolean bl = handlerThread != null && handlerThread.isAlive();
        return bl;
    }

    void onCompletedDirectly() throws IOException {
        if (this.interceptBeforeCompleted()) {
            return;
        }
        this.handleCompleted();
    }

    void onConnected(boolean bl, long l, String string2, String string3) throws IllegalArgumentException {
        String string4 = this.model.getETag();
        if (string4 != null && !string4.equals(string2)) {
            throw new IllegalArgumentException(FileDownloadUtils.formatString("callback onConnected must with precondition succeed, but the etag is changes(%s != %s)", string2, string4));
        }
        this.processParams.setResuming(bl);
        this.model.setStatus((byte)2);
        this.model.setTotal(l);
        this.model.setETag(string2);
        this.model.setFilename(string3);
        this.database.updateConnected(this.model.getId(), l, string2, string3);
        this.onStatusChanged((byte)2);
        this.callbackMinIntervalBytes = DownloadStatusCallback.calculateCallbackMinIntervalBytes(l, this.callbackProgressMaxCount);
        this.needSetProcess.compareAndSet(false, true);
    }

    void onErrorDirectly(Exception exception) {
        this.handleError(exception);
    }

    void onMultiConnection() {
        HandlerThread handlerThread;
        this.handlerThread = handlerThread = new HandlerThread("source-status-callback");
        handlerThread.start();
        this.handler = new Handler(this.handlerThread.getLooper(), (Handler.Callback)this);
    }

    void onPausedDirectly() {
        this.handlePaused();
    }

    public void onPending() {
        this.model.setStatus((byte)1);
        this.database.updatePending(this.model.getId());
        this.onStatusChanged((byte)1);
    }

    void onProgress(long l) {
        this.callbackIncreaseBuffer.addAndGet(l);
        this.model.increaseSoFar(l);
        this.inspectNeedCallbackToUser(SystemClock.elapsedRealtime());
        if (this.handler == null) {
            this.handleProgress();
        } else if (this.needCallbackProgressToUser.get()) {
            this.sendMessage(this.handler.obtainMessage(3));
        }
    }

    void onRetry(Exception exception, int n) {
        this.callbackIncreaseBuffer.set(0L);
        Handler handler = this.handler;
        if (handler == null) {
            this.handleRetry(exception, n);
        } else {
            this.sendMessage(handler.obtainMessage(5, n, 0, (Object)exception));
        }
    }

    void onStartThread() {
        this.model.setStatus((byte)6);
        this.onStatusChanged((byte)6);
        this.database.onTaskStart(this.model.getId());
    }

    public static class ProcessParams {
        private Exception exception;
        private boolean isResuming;
        private int retryingTimes;

        public Exception getException() {
            return this.exception;
        }

        public int getRetryingTimes() {
            return this.retryingTimes;
        }

        public boolean isResuming() {
            return this.isResuming;
        }

        void setException(Exception exception) {
            this.exception = exception;
        }

        void setResuming(boolean bl) {
            this.isResuming = bl;
        }

        void setRetryingTimes(int n) {
            this.retryingTimes = n;
        }
    }
}

