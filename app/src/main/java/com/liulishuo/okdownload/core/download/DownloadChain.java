/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.download;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.breakpoint.DownloadStore;
import com.liulishuo.okdownload.core.connection.DownloadConnection;
import com.liulishuo.okdownload.core.dispatcher.CallbackDispatcher;
import com.liulishuo.okdownload.core.download.DownloadCache;
import com.liulishuo.okdownload.core.exception.InterruptException;
import com.liulishuo.okdownload.core.file.MultiPointOutputStream;
import com.liulishuo.okdownload.core.interceptor.BreakpointInterceptor;
import com.liulishuo.okdownload.core.interceptor.FetchDataInterceptor;
import com.liulishuo.okdownload.core.interceptor.Interceptor;
import com.liulishuo.okdownload.core.interceptor.RetryInterceptor;
import com.liulishuo.okdownload.core.interceptor.connect.CallServerInterceptor;
import com.liulishuo.okdownload.core.interceptor.connect.HeaderInterceptor;
import com.liulishuo.okdownload.core.interceptor.connect.RedirectInterceptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class DownloadChain
implements Runnable {
    private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), Util.threadFactory("OkDownload Cancel Block", false));
    private static final String TAG = "DownloadChain";
    private final int blockIndex;
    private final DownloadCache cache;
    private final CallbackDispatcher callbackDispatcher;
    int connectIndex = 0;
    final List<Interceptor.Connect> connectInterceptorList = new ArrayList<Interceptor.Connect>();
    private volatile DownloadConnection connection;
    volatile Thread currentThread;
    int fetchIndex = 0;
    final List<Interceptor.Fetch> fetchInterceptorList = new ArrayList<Interceptor.Fetch>();
    final AtomicBoolean finished = new AtomicBoolean(false);
    private final BreakpointInfo info;
    long noCallbackIncreaseBytes;
    private final Runnable releaseConnectionRunnable = new Runnable(this){
        final DownloadChain this$0;
        {
            this.this$0 = downloadChain;
        }

        @Override
        public void run() {
            this.this$0.releaseConnection();
        }
    };
    private long responseContentLength;
    private final DownloadStore store;
    private final DownloadTask task;

    private DownloadChain(int n, DownloadTask downloadTask, BreakpointInfo breakpointInfo, DownloadCache downloadCache, DownloadStore downloadStore) {
        this.blockIndex = n;
        this.task = downloadTask;
        this.cache = downloadCache;
        this.info = breakpointInfo;
        this.store = downloadStore;
        this.callbackDispatcher = OkDownload.with().callbackDispatcher();
    }

    static DownloadChain createChain(int n, DownloadTask downloadTask, BreakpointInfo breakpointInfo, DownloadCache downloadCache, DownloadStore downloadStore) {
        return new DownloadChain(n, downloadTask, breakpointInfo, downloadCache, downloadStore);
    }

    public void cancel() {
        if (!this.finished.get() && this.currentThread != null) {
            this.currentThread.interrupt();
            return;
        }
    }

    public void flushNoCallbackIncreaseBytes() {
        if (this.noCallbackIncreaseBytes == 0L) {
            return;
        }
        this.callbackDispatcher.dispatch().fetchProgress(this.task, this.blockIndex, this.noCallbackIncreaseBytes);
        this.noCallbackIncreaseBytes = 0L;
    }

    public int getBlockIndex() {
        return this.blockIndex;
    }

    public DownloadCache getCache() {
        return this.cache;
    }

    public DownloadConnection getConnection() {
        synchronized (this) {
            DownloadConnection downloadConnection = this.connection;
            return downloadConnection;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public DownloadConnection getConnectionOrCreate() throws IOException {
        synchronized (this) {
            if (this.cache.isInterrupt()) {
                throw InterruptException.SIGNAL;
            }
            if (this.connection != null) return this.connection;
            Object object = this.cache.getRedirectLocation();
            if (object == null) {
                object = this.info.getUrl();
            }
            this.connection = OkDownload.with().connectionFactory().create((String)object);
            return this.connection;
        }
    }

    public DownloadStore getDownloadStore() {
        return this.store;
    }

    public BreakpointInfo getInfo() {
        return this.info;
    }

    public MultiPointOutputStream getOutputStream() {
        return this.cache.getOutputStream();
    }

    public long getResponseContentLength() {
        return this.responseContentLength;
    }

    public DownloadTask getTask() {
        return this.task;
    }

    public void increaseCallbackBytes(long l) {
        this.noCallbackIncreaseBytes += l;
    }

    boolean isFinished() {
        return this.finished.get();
    }

    public long loopFetch() throws IOException {
        if (this.fetchIndex == this.fetchInterceptorList.size()) {
            --this.fetchIndex;
        }
        return this.processFetch();
    }

    public DownloadConnection.Connected processConnect() throws IOException {
        if (!this.cache.isInterrupt()) {
            List<Interceptor.Connect> list = this.connectInterceptorList;
            int n = this.connectIndex;
            this.connectIndex = n + 1;
            return list.get(n).interceptConnect(this);
        }
        throw InterruptException.SIGNAL;
    }

    public long processFetch() throws IOException {
        if (!this.cache.isInterrupt()) {
            List<Interceptor.Fetch> list = this.fetchInterceptorList;
            int n = this.fetchIndex;
            this.fetchIndex = n + 1;
            return list.get(n).interceptFetch(this);
        }
        throw InterruptException.SIGNAL;
    }

    public void releaseConnection() {
        synchronized (this) {
            if (this.connection != null) {
                this.connection.release();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("release connection ");
                stringBuilder.append(this.connection);
                stringBuilder.append(" task[");
                stringBuilder.append(this.task.getId());
                stringBuilder.append("] block[");
                stringBuilder.append(this.blockIndex);
                stringBuilder.append("]");
                Util.d(TAG, stringBuilder.toString());
            }
            this.connection = null;
            return;
        }
    }

    void releaseConnectionAsync() {
        EXECUTOR.execute(this.releaseConnectionRunnable);
    }

    public void resetConnectForRetry() {
        this.connectIndex = 1;
        this.releaseConnection();
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public void run() {
        if (!this.isFinished()) {
            this.currentThread = Thread.currentThread();
            try {
                this.start();
lbl5:
                // 2 sources

                while (true) {
                    this.finished.set(true);
                    this.releaseConnectionAsync();
                    break;
                }
            }
            catch (Throwable var1_1) {
                this.finished.set(true);
                this.releaseConnectionAsync();
                throw var1_1;
            }
            catch (IOException var1_2) {
                ** continue;
            }
            return;
        }
        var1_3 = new IllegalAccessError("The chain has been finished!");
        throw var1_3;
    }

    public void setConnection(DownloadConnection downloadConnection) {
        synchronized (this) {
            this.connection = downloadConnection;
            return;
        }
    }

    public void setRedirectLocation(String string2) {
        this.cache.setRedirectLocation(string2);
    }

    public void setResponseContentLength(long l) {
        this.responseContentLength = l;
    }

    void start() throws IOException {
        CallbackDispatcher callbackDispatcher = OkDownload.with().callbackDispatcher();
        RetryInterceptor retryInterceptor = new RetryInterceptor();
        BreakpointInterceptor breakpointInterceptor = new BreakpointInterceptor();
        this.connectInterceptorList.add(retryInterceptor);
        this.connectInterceptorList.add(breakpointInterceptor);
        this.connectInterceptorList.add(new RedirectInterceptor());
        this.connectInterceptorList.add(new HeaderInterceptor());
        this.connectInterceptorList.add(new CallServerInterceptor());
        this.connectIndex = 0;
        Object object = this.processConnect();
        if (!this.cache.isInterrupt()) {
            callbackDispatcher.dispatch().fetchStart(this.task, this.blockIndex, this.getResponseContentLength());
            object = new FetchDataInterceptor(this.blockIndex, object.getInputStream(), this.getOutputStream(), this.task);
            this.fetchInterceptorList.add(retryInterceptor);
            this.fetchInterceptorList.add(breakpointInterceptor);
            this.fetchInterceptorList.add((Interceptor.Fetch)object);
            this.fetchIndex = 0;
            long l = this.processFetch();
            callbackDispatcher.dispatch().fetchEnd(this.task, this.blockIndex, l);
            return;
        }
        throw InterruptException.SIGNAL;
    }
}

