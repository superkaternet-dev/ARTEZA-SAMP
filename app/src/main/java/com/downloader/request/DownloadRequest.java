/*
 * Decompiled with CFR 0.152.
 */
package com.downloader.request;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.Priority;
import com.downloader.Response;
import com.downloader.Status;
import com.downloader.core.Core;
import com.downloader.internal.ComponentHolder;
import com.downloader.internal.DownloadRequestQueue;
import com.downloader.internal.SynchronousCall;
import com.downloader.request.DownloadRequestBuilder;
import com.downloader.utils.Utils;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

public class DownloadRequest {
    private int connectTimeout;
    private String dirPath;
    private int downloadId;
    private long downloadedBytes;
    private String fileName;
    private Future future;
    private HashMap<String, List<String>> headerMap;
    private OnCancelListener onCancelListener;
    private OnDownloadListener onDownloadListener;
    private OnPauseListener onPauseListener;
    private OnProgressListener onProgressListener;
    private OnStartOrResumeListener onStartOrResumeListener;
    private Priority priority;
    private int readTimeout;
    private int sequenceNumber;
    private Status status;
    private Object tag;
    private long totalBytes;
    private String url;
    private String userAgent;

    DownloadRequest(DownloadRequestBuilder downloadRequestBuilder) {
        this.url = downloadRequestBuilder.url;
        this.dirPath = downloadRequestBuilder.dirPath;
        this.fileName = downloadRequestBuilder.fileName;
        this.headerMap = downloadRequestBuilder.headerMap;
        this.priority = downloadRequestBuilder.priority;
        this.tag = downloadRequestBuilder.tag;
        int n = downloadRequestBuilder.readTimeout != 0 ? downloadRequestBuilder.readTimeout : this.getReadTimeoutFromConfig();
        this.readTimeout = n;
        n = downloadRequestBuilder.connectTimeout != 0 ? downloadRequestBuilder.connectTimeout : this.getConnectTimeoutFromConfig();
        this.connectTimeout = n;
        this.userAgent = downloadRequestBuilder.userAgent;
    }

    private void deliverCancelEvent() {
        Core.getInstance().getExecutorSupplier().forMainThreadTasks().execute(new Runnable(this){
            final DownloadRequest this$0;
            {
                this.this$0 = downloadRequest;
            }

            @Override
            public void run() {
                if (this.this$0.onCancelListener != null) {
                    this.this$0.onCancelListener.onCancel();
                }
            }
        });
    }

    private void destroy() {
        this.onProgressListener = null;
        this.onDownloadListener = null;
        this.onStartOrResumeListener = null;
        this.onPauseListener = null;
        this.onCancelListener = null;
    }

    private void finish() {
        this.destroy();
        DownloadRequestQueue.getInstance().finish(this);
    }

    private int getConnectTimeoutFromConfig() {
        return ComponentHolder.getInstance().getConnectTimeout();
    }

    private int getReadTimeoutFromConfig() {
        return ComponentHolder.getInstance().getReadTimeout();
    }

    public void cancel() {
        this.status = Status.CANCELLED;
        Future future = this.future;
        if (future != null) {
            future.cancel(true);
        }
        this.deliverCancelEvent();
        Utils.deleteTempFileAndDatabaseEntryInBackground(Utils.getTempPath(this.dirPath, this.fileName), this.downloadId);
    }

    public void deliverError(Error error) {
        if (this.status != Status.CANCELLED) {
            this.setStatus(Status.FAILED);
            Core.getInstance().getExecutorSupplier().forMainThreadTasks().execute(new Runnable(this, error){
                final DownloadRequest this$0;
                final Error val$error;
                {
                    this.this$0 = downloadRequest;
                    this.val$error = error;
                }

                @Override
                public void run() {
                    if (this.this$0.onDownloadListener != null) {
                        this.this$0.onDownloadListener.onError(this.val$error);
                    }
                    this.this$0.finish();
                }
            });
        }
    }

    public void deliverPauseEvent() {
        if (this.status != Status.CANCELLED) {
            Core.getInstance().getExecutorSupplier().forMainThreadTasks().execute(new Runnable(this){
                final DownloadRequest this$0;
                {
                    this.this$0 = downloadRequest;
                }

                @Override
                public void run() {
                    if (this.this$0.onPauseListener != null) {
                        this.this$0.onPauseListener.onPause();
                    }
                }
            });
        }
    }

    public void deliverStartEvent() {
        if (this.status != Status.CANCELLED) {
            Core.getInstance().getExecutorSupplier().forMainThreadTasks().execute(new Runnable(this){
                final DownloadRequest this$0;
                {
                    this.this$0 = downloadRequest;
                }

                @Override
                public void run() {
                    if (this.this$0.onStartOrResumeListener != null) {
                        this.this$0.onStartOrResumeListener.onStartOrResume();
                    }
                }
            });
        }
    }

    public void deliverSuccess() {
        if (this.status != Status.CANCELLED) {
            this.setStatus(Status.COMPLETED);
            Core.getInstance().getExecutorSupplier().forMainThreadTasks().execute(new Runnable(this){
                final DownloadRequest this$0;
                {
                    this.this$0 = downloadRequest;
                }

                @Override
                public void run() {
                    if (this.this$0.onDownloadListener != null) {
                        this.this$0.onDownloadListener.onDownloadComplete();
                    }
                    this.this$0.finish();
                }
            });
        }
    }

    public Response executeSync() {
        this.downloadId = Utils.getUniqueId(this.url, this.dirPath, this.fileName);
        return new SynchronousCall(this).execute();
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public String getDirPath() {
        return this.dirPath;
    }

    public int getDownloadId() {
        return this.downloadId;
    }

    public long getDownloadedBytes() {
        return this.downloadedBytes;
    }

    public String getFileName() {
        return this.fileName;
    }

    public Future getFuture() {
        return this.future;
    }

    public HashMap<String, List<String>> getHeaders() {
        return this.headerMap;
    }

    public OnProgressListener getOnProgressListener() {
        return this.onProgressListener;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public int getSequenceNumber() {
        return this.sequenceNumber;
    }

    public Status getStatus() {
        return this.status;
    }

    public Object getTag() {
        return this.tag;
    }

    public long getTotalBytes() {
        return this.totalBytes;
    }

    public String getUrl() {
        return this.url;
    }

    public String getUserAgent() {
        if (this.userAgent == null) {
            this.userAgent = ComponentHolder.getInstance().getUserAgent();
        }
        return this.userAgent;
    }

    public void setConnectTimeout(int n) {
        this.connectTimeout = n;
    }

    public void setDirPath(String string2) {
        this.dirPath = string2;
    }

    public void setDownloadId(int n) {
        this.downloadId = n;
    }

    public void setDownloadedBytes(long l) {
        this.downloadedBytes = l;
    }

    public void setFileName(String string2) {
        this.fileName = string2;
    }

    public void setFuture(Future future) {
        this.future = future;
    }

    public DownloadRequest setOnCancelListener(OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
        return this;
    }

    public DownloadRequest setOnPauseListener(OnPauseListener onPauseListener) {
        this.onPauseListener = onPauseListener;
        return this;
    }

    public DownloadRequest setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
        return this;
    }

    public DownloadRequest setOnStartOrResumeListener(OnStartOrResumeListener onStartOrResumeListener) {
        this.onStartOrResumeListener = onStartOrResumeListener;
        return this;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setReadTimeout(int n) {
        this.readTimeout = n;
    }

    public void setSequenceNumber(int n) {
        this.sequenceNumber = n;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setTag(Object object) {
        this.tag = object;
    }

    public void setTotalBytes(long l) {
        this.totalBytes = l;
    }

    public void setUrl(String string2) {
        this.url = string2;
    }

    public void setUserAgent(String string2) {
        this.userAgent = string2;
    }

    public int start(OnDownloadListener onDownloadListener) {
        this.onDownloadListener = onDownloadListener;
        this.downloadId = Utils.getUniqueId(this.url, this.dirPath, this.fileName);
        DownloadRequestQueue.getInstance().addRequest(this);
        return this.downloadId;
    }
}

