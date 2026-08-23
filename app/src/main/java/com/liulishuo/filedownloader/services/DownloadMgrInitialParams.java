/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.services;

import com.liulishuo.filedownloader.connection.DefaultConnectionCountAdapter;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.database.FileDownloadDatabase;
import com.liulishuo.filedownloader.database.RemitDatabase;
import com.liulishuo.filedownloader.services.DefaultIdGenerator;
import com.liulishuo.filedownloader.services.ForegroundServiceConfig;
import com.liulishuo.filedownloader.stream.FileDownloadRandomAccessFile;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadProperties;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

public class DownloadMgrInitialParams {
    private final InitCustomMaker mMaker;

    public DownloadMgrInitialParams() {
        this.mMaker = null;
    }

    public DownloadMgrInitialParams(InitCustomMaker initCustomMaker) {
        this.mMaker = initCustomMaker;
    }

    private FileDownloadHelper.ConnectionCountAdapter createDefaultConnectionCountAdapter() {
        return new DefaultConnectionCountAdapter();
    }

    private FileDownloadHelper.ConnectionCreator createDefaultConnectionCreator() {
        return new FileDownloadUrlConnection.Creator();
    }

    private FileDownloadDatabase createDefaultDatabase() {
        return new RemitDatabase();
    }

    private ForegroundServiceConfig createDefaultForegroundServiceConfig() {
        return new ForegroundServiceConfig.Builder().needRecreateChannelId(true).build();
    }

    private FileDownloadHelper.IdGenerator createDefaultIdGenerator() {
        return new DefaultIdGenerator();
    }

    private FileDownloadHelper.OutputStreamCreator createDefaultOutputStreamCreator() {
        return new FileDownloadRandomAccessFile.Creator();
    }

    private int getDefaultMaxNetworkThreadCount() {
        return FileDownloadProperties.getImpl().downloadMaxNetworkThreadCount;
    }

    public FileDownloadHelper.ConnectionCountAdapter createConnectionCountAdapter() {
        Object object = this.mMaker;
        if (object == null) {
            return this.createDefaultConnectionCountAdapter();
        }
        object = ((InitCustomMaker)object).mConnectionCountAdapter;
        if (object != null) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "initial FileDownloader manager with the customize connection count adapter: %s", object);
            }
            return object;
        }
        return this.createDefaultConnectionCountAdapter();
    }

    public FileDownloadHelper.ConnectionCreator createConnectionCreator() {
        Object object = this.mMaker;
        if (object == null) {
            return this.createDefaultConnectionCreator();
        }
        object = ((InitCustomMaker)object).mConnectionCreator;
        if (object != null) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "initial FileDownloader manager with the customize connection creator: %s", object);
            }
            return object;
        }
        return this.createDefaultConnectionCreator();
    }

    public FileDownloadDatabase createDatabase() {
        Object object = this.mMaker;
        if (object != null && ((InitCustomMaker)object).mDatabaseCustomMaker != null) {
            object = this.mMaker.mDatabaseCustomMaker.customMake();
            if (object != null) {
                if (FileDownloadLog.NEED_LOG) {
                    FileDownloadLog.d(this, "initial FileDownloader manager with the customize database: %s", object);
                }
                return object;
            }
            return this.createDefaultDatabase();
        }
        return this.createDefaultDatabase();
    }

    public ForegroundServiceConfig createForegroundServiceConfig() {
        Object object = this.mMaker;
        if (object == null) {
            return this.createDefaultForegroundServiceConfig();
        }
        object = ((InitCustomMaker)object).mForegroundServiceConfig;
        if (object != null) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "initial FileDownloader manager with the customize foreground service config: %s", object);
            }
            return object;
        }
        return this.createDefaultForegroundServiceConfig();
    }

    public FileDownloadHelper.IdGenerator createIdGenerator() {
        Object object = this.mMaker;
        if (object == null) {
            return this.createDefaultIdGenerator();
        }
        object = ((InitCustomMaker)object).mIdGenerator;
        if (object != null) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "initial FileDownloader manager with the customize id generator: %s", object);
            }
            return object;
        }
        return this.createDefaultIdGenerator();
    }

    public FileDownloadHelper.OutputStreamCreator createOutputStreamCreator() {
        Object object = this.mMaker;
        if (object == null) {
            return this.createDefaultOutputStreamCreator();
        }
        object = ((InitCustomMaker)object).mOutputStreamCreator;
        if (object != null) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "initial FileDownloader manager with the customize output stream: %s", object);
            }
            return object;
        }
        return this.createDefaultOutputStreamCreator();
    }

    public int getMaxNetworkThreadCount() {
        Object object = this.mMaker;
        if (object == null) {
            return this.getDefaultMaxNetworkThreadCount();
        }
        object = ((InitCustomMaker)object).mMaxNetworkThreadCount;
        if (object != null) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "initial FileDownloader manager with the customize maxNetworkThreadCount: %d", object);
            }
            return FileDownloadProperties.getValidNetworkThreadCount((Integer)object);
        }
        return this.getDefaultMaxNetworkThreadCount();
    }

    public static class InitCustomMaker {
        FileDownloadHelper.ConnectionCountAdapter mConnectionCountAdapter;
        FileDownloadHelper.ConnectionCreator mConnectionCreator;
        FileDownloadHelper.DatabaseCustomMaker mDatabaseCustomMaker;
        ForegroundServiceConfig mForegroundServiceConfig;
        FileDownloadHelper.IdGenerator mIdGenerator;
        Integer mMaxNetworkThreadCount;
        FileDownloadHelper.OutputStreamCreator mOutputStreamCreator;

        public void commit() {
        }

        public InitCustomMaker connectionCountAdapter(FileDownloadHelper.ConnectionCountAdapter connectionCountAdapter) {
            this.mConnectionCountAdapter = connectionCountAdapter;
            return this;
        }

        public InitCustomMaker connectionCreator(FileDownloadHelper.ConnectionCreator connectionCreator) {
            this.mConnectionCreator = connectionCreator;
            return this;
        }

        public InitCustomMaker database(FileDownloadHelper.DatabaseCustomMaker databaseCustomMaker) {
            this.mDatabaseCustomMaker = databaseCustomMaker;
            return this;
        }

        public InitCustomMaker foregroundServiceConfig(ForegroundServiceConfig foregroundServiceConfig) {
            this.mForegroundServiceConfig = foregroundServiceConfig;
            return this;
        }

        public InitCustomMaker idGenerator(FileDownloadHelper.IdGenerator idGenerator) {
            this.mIdGenerator = idGenerator;
            return this;
        }

        public InitCustomMaker maxNetworkThreadCount(int n) {
            if (n > 0) {
                this.mMaxNetworkThreadCount = n;
            }
            return this;
        }

        public InitCustomMaker outputStreamCreator(FileDownloadHelper.OutputStreamCreator outputStreamCreator) {
            this.mOutputStreamCreator = outputStreamCreator;
            if (outputStreamCreator != null && !outputStreamCreator.supportSeek() && !FileDownloadProperties.getImpl().fileNonPreAllocation) {
                throw new IllegalArgumentException("Since the provided FileDownloadOutputStream does not support the seek function, if FileDownloader pre-allocates file size at the beginning of the download, it will can not be resumed from the breakpoint. If you need to ensure that the resumption is available, please add and set the value of 'file.non-pre-allocation' field to 'true' in the 'filedownloader.properties' file which is in your application assets folder manually for resolving this problem.");
            }
            return this;
        }

        public String toString() {
            return FileDownloadUtils.formatString("component: database[%s], maxNetworkCount[%s], outputStream[%s], connection[%s], connectionCountAdapter[%s]", this.mDatabaseCustomMaker, this.mMaxNetworkThreadCount, this.mOutputStreamCreator, this.mConnectionCreator, this.mConnectionCountAdapter);
        }
    }
}

