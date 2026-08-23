/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.SystemClock
 */
package com.liulishuo.filedownloader.download;

import android.os.SystemClock;
import com.liulishuo.filedownloader.connection.FileDownloadConnection;
import com.liulishuo.filedownloader.database.FileDownloadDatabase;
import com.liulishuo.filedownloader.download.ConnectionProfile;
import com.liulishuo.filedownloader.download.CustomComponentHolder;
import com.liulishuo.filedownloader.download.DownloadRunnable;
import com.liulishuo.filedownloader.download.ProcessCallback;
import com.liulishuo.filedownloader.exception.FileDownloadGiveUpRetryException;
import com.liulishuo.filedownloader.stream.FileDownloadOutputStream;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.io.IOException;

public class FetchDataTask {
    static final int BUFFER_SIZE = 4096;
    private final ProcessCallback callback;
    private final FileDownloadConnection connection;
    private final int connectionIndex;
    private final long contentLength;
    long currentOffset;
    private final FileDownloadDatabase database;
    private final int downloadId;
    private final long endOffset;
    private final DownloadRunnable hostRunnable;
    private final boolean isWifiRequired;
    private volatile long lastSyncBytes = 0L;
    private volatile long lastSyncTimestamp = 0L;
    private FileDownloadOutputStream outputStream;
    private final String path;
    private volatile boolean paused;
    private final long startOffset;

    private FetchDataTask(FileDownloadConnection fileDownloadConnection, ConnectionProfile connectionProfile, DownloadRunnable downloadRunnable, int n, int n2, boolean bl, ProcessCallback processCallback, String string2) {
        this.callback = processCallback;
        this.path = string2;
        this.connection = fileDownloadConnection;
        this.isWifiRequired = bl;
        this.hostRunnable = downloadRunnable;
        this.connectionIndex = n2;
        this.downloadId = n;
        this.database = CustomComponentHolder.getImpl().getDatabaseInstance();
        this.startOffset = connectionProfile.startOffset;
        this.endOffset = connectionProfile.endOffset;
        this.currentOffset = connectionProfile.currentOffset;
        this.contentLength = connectionProfile.contentLength;
    }

    private void checkAndSync() {
        long l = SystemClock.elapsedRealtime();
        if (FileDownloadUtils.isNeedSync(this.currentOffset - this.lastSyncBytes, l - this.lastSyncTimestamp)) {
            this.sync();
            this.lastSyncBytes = this.currentOffset;
            this.lastSyncTimestamp = l;
        }
    }

    private void sync() {
        boolean bl;
        long l = SystemClock.uptimeMillis();
        try {
            this.outputStream.flushAndSync();
            bl = true;
        }
        catch (IOException iOException) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "Because of the system cannot guarantee that all the buffers have been synchronized with physical media, or write to filefailed, we just not flushAndSync process to database too %s", iOException);
            }
            bl = false;
        }
        if (bl) {
            int n = this.connectionIndex;
            bl = n >= 0;
            if (bl) {
                this.database.updateConnectionModel(this.downloadId, n, this.currentOffset);
            } else {
                this.callback.syncProgressFromCache();
            }
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "require flushAndSync id[%d] index[%d] offset[%d], consume[%d]", this.downloadId, this.connectionIndex, this.currentOffset, SystemClock.uptimeMillis() - l);
            }
        }
    }

    public void pause() {
        this.paused = true;
    }

    /*
     * Exception decompiling
     */
    public void run() throws IOException, IllegalAccessException, IllegalArgumentException, FileDownloadGiveUpRetryException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Back jump on a try block [egrp 7[TRYBLOCK] [7 : 343->354)] java.lang.Throwable
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.insertExceptionBlocks(Op02WithProcessedDataAndRefs.java:2283)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:415)
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

    public static class Builder {
        ProcessCallback callback;
        FileDownloadConnection connection;
        Integer connectionIndex;
        ConnectionProfile connectionProfile;
        Integer downloadId;
        DownloadRunnable downloadRunnable;
        Boolean isWifiRequired;
        String path;

        public FetchDataTask build() throws IllegalArgumentException {
            Integer n;
            ConnectionProfile connectionProfile;
            FileDownloadConnection fileDownloadConnection;
            if (this.isWifiRequired != null && (fileDownloadConnection = this.connection) != null && (connectionProfile = this.connectionProfile) != null && this.callback != null && this.path != null && (n = this.downloadId) != null && this.connectionIndex != null) {
                return new FetchDataTask(fileDownloadConnection, connectionProfile, this.downloadRunnable, n, this.connectionIndex, this.isWifiRequired, this.callback, this.path);
            }
            throw new IllegalArgumentException();
        }

        public Builder setCallback(ProcessCallback processCallback) {
            this.callback = processCallback;
            return this;
        }

        public Builder setConnection(FileDownloadConnection fileDownloadConnection) {
            this.connection = fileDownloadConnection;
            return this;
        }

        public Builder setConnectionIndex(int n) {
            this.connectionIndex = n;
            return this;
        }

        public Builder setConnectionProfile(ConnectionProfile connectionProfile) {
            this.connectionProfile = connectionProfile;
            return this;
        }

        public Builder setDownloadId(int n) {
            this.downloadId = n;
            return this;
        }

        public Builder setHost(DownloadRunnable downloadRunnable) {
            this.downloadRunnable = downloadRunnable;
            return this;
        }

        public Builder setPath(String string2) {
            this.path = string2;
            return this;
        }

        public Builder setWifiRequired(boolean bl) {
            this.isWifiRequired = bl;
            return this;
        }
    }
}

