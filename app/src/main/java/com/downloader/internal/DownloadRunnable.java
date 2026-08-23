/*
 * Decompiled with CFR 0.152.
 */
package com.downloader.internal;

import com.downloader.Error;
import com.downloader.Priority;
import com.downloader.Response;
import com.downloader.Status;
import com.downloader.internal.DownloadTask;
import com.downloader.request.DownloadRequest;

public class DownloadRunnable
implements Runnable {
    public final Priority priority;
    public final DownloadRequest request;
    public final int sequence;

    DownloadRunnable(DownloadRequest downloadRequest) {
        this.request = downloadRequest;
        this.priority = downloadRequest.getPriority();
        this.sequence = downloadRequest.getSequenceNumber();
    }

    @Override
    public void run() {
        this.request.setStatus(Status.RUNNING);
        Response response = DownloadTask.create(this.request).run();
        if (response.isSuccessful()) {
            this.request.deliverSuccess();
        } else if (response.isPaused()) {
            this.request.deliverPauseEvent();
        } else if (response.getError() != null) {
            this.request.deliverError(response.getError());
        } else if (!response.isCancelled()) {
            this.request.deliverError(new Error());
        }
    }
}

