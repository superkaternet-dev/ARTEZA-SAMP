/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.download;

import com.liulishuo.filedownloader.database.FileDownloadDatabase;
import com.liulishuo.filedownloader.download.ConnectTask;
import com.liulishuo.filedownloader.download.ConnectionProfile;
import com.liulishuo.filedownloader.download.CustomComponentHolder;
import com.liulishuo.filedownloader.download.FetchDataTask;
import com.liulishuo.filedownloader.download.ProcessCallback;
import com.liulishuo.filedownloader.model.ConnectionModel;
import com.liulishuo.filedownloader.model.FileDownloadHeader;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

public class DownloadRunnable
implements Runnable {
    private final ProcessCallback callback;
    private final ConnectTask connectTask;
    final int connectionIndex;
    private final int downloadId;
    private FetchDataTask fetchDataTask;
    private final boolean isWifiRequired;
    private final String path;
    private volatile boolean paused;

    private DownloadRunnable(int n, int n2, ConnectTask connectTask, ProcessCallback processCallback, boolean bl, String string2) {
        this.downloadId = n;
        this.connectionIndex = n2;
        this.paused = false;
        this.callback = processCallback;
        this.path = string2;
        this.connectTask = connectTask;
        this.isWifiRequired = bl;
    }

    private long getDownloadedOffset() {
        FileDownloadDatabase object2 = CustomComponentHolder.getImpl().getDatabaseInstance();
        if (this.connectionIndex >= 0) {
            for (ConnectionModel connectionModel : object2.findConnectionModel(this.downloadId)) {
                if (connectionModel.getIndex() != this.connectionIndex) continue;
                return connectionModel.getCurrentOffset();
            }
        } else {
            FileDownloadModel fileDownloadModel = object2.find(this.downloadId);
            if (fileDownloadModel != null) {
                return fileDownloadModel.getSoFar();
            }
        }
        return 0L;
    }

    public void discard() {
        this.pause();
    }

    public void pause() {
        this.paused = true;
        FetchDataTask fetchDataTask = this.fetchDataTask;
        if (fetchDataTask != null) {
            fetchDataTask.pause();
        }
    }

    /*
     * Exception decompiling
     */
    @Override
    public void run() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Back jump on a try block [egrp 16[TRYBLOCK] [80 : 941->955)] java.lang.Throwable
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
        private ProcessCallback callback;
        private final ConnectTask.Builder connectTaskBuilder = new ConnectTask.Builder();
        private Integer connectionIndex;
        private Boolean isWifiRequired;
        private String path;

        public DownloadRunnable build() {
            if (this.callback != null && this.path != null && this.isWifiRequired != null && this.connectionIndex != null) {
                ConnectTask connectTask = this.connectTaskBuilder.build();
                return new DownloadRunnable(connectTask.downloadId, this.connectionIndex, connectTask, this.callback, this.isWifiRequired, this.path);
            }
            throw new IllegalArgumentException(FileDownloadUtils.formatString("%s %s %B", this.callback, this.path, this.isWifiRequired));
        }

        DownloadRunnable buildForTest(ConnectTask connectTask) {
            return new DownloadRunnable(connectTask.downloadId, 0, connectTask, this.callback, false, "");
        }

        public Builder setCallback(ProcessCallback processCallback) {
            this.callback = processCallback;
            return this;
        }

        public Builder setConnectionIndex(Integer n) {
            this.connectionIndex = n;
            return this;
        }

        public Builder setConnectionModel(ConnectionProfile connectionProfile) {
            this.connectTaskBuilder.setConnectionProfile(connectionProfile);
            return this;
        }

        public Builder setEtag(String string2) {
            this.connectTaskBuilder.setEtag(string2);
            return this;
        }

        public Builder setHeader(FileDownloadHeader fileDownloadHeader) {
            this.connectTaskBuilder.setHeader(fileDownloadHeader);
            return this;
        }

        public Builder setId(int n) {
            this.connectTaskBuilder.setDownloadId(n);
            return this;
        }

        public Builder setPath(String string2) {
            this.path = string2;
            return this;
        }

        public Builder setUrl(String string2) {
            this.connectTaskBuilder.setUrl(string2);
            return this;
        }

        public Builder setWifiRequired(boolean bl) {
            this.isWifiRequired = bl;
            return this;
        }
    }
}

