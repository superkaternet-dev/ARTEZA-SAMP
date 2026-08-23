/*
 * Decompiled with CFR 0.152.
 */
package com.downloader.internal;

import com.downloader.Status;
import com.downloader.core.Core;
import com.downloader.internal.DownloadRunnable;
import com.downloader.request.DownloadRequest;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DownloadRequestQueue {
    private static DownloadRequestQueue instance;
    private final Map<Integer, DownloadRequest> currentRequestMap = new ConcurrentHashMap<Integer, DownloadRequest>();
    private final AtomicInteger sequenceGenerator = new AtomicInteger();

    private DownloadRequestQueue() {
    }

    private void cancelAndRemoveFromMap(DownloadRequest downloadRequest) {
        if (downloadRequest != null) {
            downloadRequest.cancel();
            this.currentRequestMap.remove(downloadRequest.getDownloadId());
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static DownloadRequestQueue getInstance() {
        if (instance != null) return instance;
        synchronized (DownloadRequestQueue.class) {
            DownloadRequestQueue downloadRequestQueue;
            if (instance != null) return instance;
            instance = downloadRequestQueue = new DownloadRequestQueue();
            return instance;
        }
    }

    private int getSequenceNumber() {
        return this.sequenceGenerator.incrementAndGet();
    }

    public static void initialize() {
        DownloadRequestQueue.getInstance();
    }

    public void addRequest(DownloadRequest downloadRequest) {
        this.currentRequestMap.put(downloadRequest.getDownloadId(), downloadRequest);
        downloadRequest.setStatus(Status.QUEUED);
        downloadRequest.setSequenceNumber(this.getSequenceNumber());
        downloadRequest.setFuture(Core.getInstance().getExecutorSupplier().forDownloadTasks().submit(new DownloadRunnable(downloadRequest)));
    }

    public void cancel(int n) {
        this.cancelAndRemoveFromMap(this.currentRequestMap.get(n));
    }

    public void cancel(Object object) {
        Iterator<Map.Entry<Integer, DownloadRequest>> iterator2 = this.currentRequestMap.entrySet().iterator();
        while (iterator2.hasNext()) {
            DownloadRequest downloadRequest = iterator2.next().getValue();
            if (downloadRequest.getTag() instanceof String && object instanceof String) {
                if (!((String)downloadRequest.getTag()).equals((String)object)) continue;
                this.cancelAndRemoveFromMap(downloadRequest);
                continue;
            }
            if (!downloadRequest.getTag().equals(object)) continue;
            this.cancelAndRemoveFromMap(downloadRequest);
        }
    }

    public void cancelAll() {
        Iterator<Map.Entry<Integer, DownloadRequest>> iterator2 = this.currentRequestMap.entrySet().iterator();
        while (iterator2.hasNext()) {
            this.cancelAndRemoveFromMap(iterator2.next().getValue());
        }
    }

    public void finish(DownloadRequest downloadRequest) {
        this.currentRequestMap.remove(downloadRequest.getDownloadId());
    }

    public Status getStatus(int n) {
        DownloadRequest downloadRequest = this.currentRequestMap.get(n);
        if (downloadRequest != null) {
            return downloadRequest.getStatus();
        }
        return Status.UNKNOWN;
    }

    public void pause(int n) {
        DownloadRequest downloadRequest = this.currentRequestMap.get(n);
        if (downloadRequest != null) {
            downloadRequest.setStatus(Status.PAUSED);
        }
    }

    public void resume(int n) {
        DownloadRequest downloadRequest = this.currentRequestMap.get(n);
        if (downloadRequest != null) {
            downloadRequest.setStatus(Status.QUEUED);
            downloadRequest.setFuture(Core.getInstance().getExecutorSupplier().forDownloadTasks().submit(new DownloadRunnable(downloadRequest)));
        }
    }
}

