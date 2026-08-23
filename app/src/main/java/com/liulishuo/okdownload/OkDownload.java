/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.liulishuo.okdownload;

import android.content.Context;
import com.liulishuo.okdownload.DownloadMonitor;
import com.liulishuo.okdownload.OkDownloadProvider;
import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.breakpoint.BreakpointStore;
import com.liulishuo.okdownload.core.breakpoint.DownloadStore;
import com.liulishuo.okdownload.core.connection.DownloadConnection;
import com.liulishuo.okdownload.core.dispatcher.CallbackDispatcher;
import com.liulishuo.okdownload.core.dispatcher.DownloadDispatcher;
import com.liulishuo.okdownload.core.download.DownloadStrategy;
import com.liulishuo.okdownload.core.file.DownloadOutputStream;
import com.liulishuo.okdownload.core.file.DownloadUriOutputStream;
import com.liulishuo.okdownload.core.file.ProcessFileStrategy;

public class OkDownload {
    static volatile OkDownload singleton;
    private final BreakpointStore breakpointStore;
    private final CallbackDispatcher callbackDispatcher;
    private final DownloadConnection.Factory connectionFactory;
    private final Context context;
    private final DownloadDispatcher downloadDispatcher;
    private final DownloadStrategy downloadStrategy;
    DownloadMonitor monitor;
    private final DownloadOutputStream.Factory outputStreamFactory;
    private final ProcessFileStrategy processFileStrategy;

    OkDownload(Context context, DownloadDispatcher downloadDispatcher, CallbackDispatcher callbackDispatcher, DownloadStore downloadStore, DownloadConnection.Factory factory, DownloadOutputStream.Factory factory2, ProcessFileStrategy processFileStrategy, DownloadStrategy downloadStrategy) {
        this.context = context;
        this.downloadDispatcher = downloadDispatcher;
        this.callbackDispatcher = callbackDispatcher;
        this.breakpointStore = downloadStore;
        this.connectionFactory = factory;
        this.outputStreamFactory = factory2;
        this.processFileStrategy = processFileStrategy;
        this.downloadStrategy = downloadStrategy;
        downloadDispatcher.setDownloadStore(Util.createRemitDatabase(downloadStore));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void setSingletonInstance(OkDownload object) {
        if (singleton != null) {
            throw new IllegalArgumentException("OkDownload must be null.");
        }
        synchronized (OkDownload.class) {
            if (singleton == null) {
                singleton = object;
                return;
            }
            object = new IllegalArgumentException("OkDownload must be null.");
            throw object;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static OkDownload with() {
        if (singleton != null) return singleton;
        synchronized (OkDownload.class) {
            if (singleton != null) return singleton;
            if (OkDownloadProvider.context != null) {
                Builder builder = new Builder(OkDownloadProvider.context);
                singleton = builder.build();
                return singleton;
            }
            IllegalStateException illegalStateException = new IllegalStateException("context == null");
            throw illegalStateException;
        }
    }

    public BreakpointStore breakpointStore() {
        return this.breakpointStore;
    }

    public CallbackDispatcher callbackDispatcher() {
        return this.callbackDispatcher;
    }

    public DownloadConnection.Factory connectionFactory() {
        return this.connectionFactory;
    }

    public Context context() {
        return this.context;
    }

    public DownloadDispatcher downloadDispatcher() {
        return this.downloadDispatcher;
    }

    public DownloadStrategy downloadStrategy() {
        return this.downloadStrategy;
    }

    public DownloadMonitor getMonitor() {
        return this.monitor;
    }

    public DownloadOutputStream.Factory outputStreamFactory() {
        return this.outputStreamFactory;
    }

    public ProcessFileStrategy processFileStrategy() {
        return this.processFileStrategy;
    }

    public void setMonitor(DownloadMonitor downloadMonitor) {
        this.monitor = downloadMonitor;
    }

    public static class Builder {
        private CallbackDispatcher callbackDispatcher;
        private DownloadConnection.Factory connectionFactory;
        private final Context context;
        private DownloadDispatcher downloadDispatcher;
        private DownloadStore downloadStore;
        private DownloadStrategy downloadStrategy;
        private DownloadMonitor monitor;
        private DownloadOutputStream.Factory outputStreamFactory;
        private ProcessFileStrategy processFileStrategy;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
        }

        public OkDownload build() {
            if (this.downloadDispatcher == null) {
                this.downloadDispatcher = new DownloadDispatcher();
            }
            if (this.callbackDispatcher == null) {
                this.callbackDispatcher = new CallbackDispatcher();
            }
            if (this.downloadStore == null) {
                this.downloadStore = Util.createDefaultDatabase(this.context);
            }
            if (this.connectionFactory == null) {
                this.connectionFactory = Util.createDefaultConnectionFactory();
            }
            if (this.outputStreamFactory == null) {
                this.outputStreamFactory = new DownloadUriOutputStream.Factory();
            }
            if (this.processFileStrategy == null) {
                this.processFileStrategy = new ProcessFileStrategy();
            }
            if (this.downloadStrategy == null) {
                this.downloadStrategy = new DownloadStrategy();
            }
            OkDownload okDownload = new OkDownload(this.context, this.downloadDispatcher, this.callbackDispatcher, this.downloadStore, this.connectionFactory, this.outputStreamFactory, this.processFileStrategy, this.downloadStrategy);
            okDownload.setMonitor(this.monitor);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("downloadStore[");
            stringBuilder.append(this.downloadStore);
            stringBuilder.append("] connectionFactory[");
            stringBuilder.append(this.connectionFactory);
            Util.d("OkDownload", stringBuilder.toString());
            return okDownload;
        }

        public Builder callbackDispatcher(CallbackDispatcher callbackDispatcher) {
            this.callbackDispatcher = callbackDispatcher;
            return this;
        }

        public Builder connectionFactory(DownloadConnection.Factory factory) {
            this.connectionFactory = factory;
            return this;
        }

        public Builder downloadDispatcher(DownloadDispatcher downloadDispatcher) {
            this.downloadDispatcher = downloadDispatcher;
            return this;
        }

        public Builder downloadStore(DownloadStore downloadStore) {
            this.downloadStore = downloadStore;
            return this;
        }

        public Builder downloadStrategy(DownloadStrategy downloadStrategy) {
            this.downloadStrategy = downloadStrategy;
            return this;
        }

        public Builder monitor(DownloadMonitor downloadMonitor) {
            this.monitor = downloadMonitor;
            return this;
        }

        public Builder outputStreamFactory(DownloadOutputStream.Factory factory) {
            this.outputStreamFactory = factory;
            return this;
        }

        public Builder processFileStrategy(ProcessFileStrategy processFileStrategy) {
            this.processFileStrategy = processFileStrategy;
            return this;
        }
    }
}

