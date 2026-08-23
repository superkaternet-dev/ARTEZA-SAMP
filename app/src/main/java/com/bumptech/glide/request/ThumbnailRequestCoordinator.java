/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.request;

import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestCoordinator;

public class ThumbnailRequestCoordinator
implements RequestCoordinator,
Request {
    private volatile Request full;
    private RequestCoordinator.RequestState fullState = RequestCoordinator.RequestState.CLEARED;
    private boolean isRunningDuringBegin;
    private final RequestCoordinator parent;
    private final Object requestLock;
    private volatile Request thumb;
    private RequestCoordinator.RequestState thumbState = RequestCoordinator.RequestState.CLEARED;

    public ThumbnailRequestCoordinator(Object object, RequestCoordinator requestCoordinator) {
        this.requestLock = object;
        this.parent = requestCoordinator;
    }

    private boolean parentCanNotifyCleared() {
        RequestCoordinator requestCoordinator = this.parent;
        boolean bl = requestCoordinator == null || requestCoordinator.canNotifyCleared(this);
        return bl;
    }

    private boolean parentCanNotifyStatusChanged() {
        RequestCoordinator requestCoordinator = this.parent;
        boolean bl = requestCoordinator == null || requestCoordinator.canNotifyStatusChanged(this);
        return bl;
    }

    private boolean parentCanSetImage() {
        RequestCoordinator requestCoordinator = this.parent;
        boolean bl = requestCoordinator == null || requestCoordinator.canSetImage(this);
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void begin() {
        Object object = this.requestLock;
        synchronized (object) {
            this.isRunningDuringBegin = true;
            try {
                if (this.fullState != RequestCoordinator.RequestState.SUCCESS && this.thumbState != RequestCoordinator.RequestState.RUNNING) {
                    this.thumbState = RequestCoordinator.RequestState.RUNNING;
                    this.thumb.begin();
                }
                if (this.isRunningDuringBegin && this.fullState != RequestCoordinator.RequestState.RUNNING) {
                    this.fullState = RequestCoordinator.RequestState.RUNNING;
                    this.full.begin();
                }
                return;
            }
            finally {
                this.isRunningDuringBegin = false;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean canNotifyCleared(Request request) {
        Object object = this.requestLock;
        synchronized (object) {
            if (!this.parentCanNotifyCleared()) return false;
            if (!request.equals(this.full)) return false;
            if (this.fullState == RequestCoordinator.RequestState.PAUSED) return false;
            return true;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean canNotifyStatusChanged(Request request) {
        Object object = this.requestLock;
        synchronized (object) {
            if (!this.parentCanNotifyStatusChanged()) return false;
            if (!request.equals(this.full)) return false;
            if (this.isAnyResourceSet()) return false;
            return true;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean canSetImage(Request request) {
        Object object = this.requestLock;
        synchronized (object) {
            if (!this.parentCanSetImage()) return false;
            if (request.equals(this.full)) return true;
            if (this.fullState == RequestCoordinator.RequestState.SUCCESS) return false;
            return true;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void clear() {
        Object object = this.requestLock;
        synchronized (object) {
            this.isRunningDuringBegin = false;
            this.fullState = RequestCoordinator.RequestState.CLEARED;
            this.thumbState = RequestCoordinator.RequestState.CLEARED;
            this.thumb.clear();
            this.full.clear();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public RequestCoordinator getRoot() {
        Object object = this.requestLock;
        synchronized (object) {
            RequestCoordinator requestCoordinator = this.parent;
            if (requestCoordinator == null) return this;
            return requestCoordinator.getRoot();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean isAnyResourceSet() {
        Object object = this.requestLock;
        synchronized (object) {
            if (this.thumb.isAnyResourceSet()) return true;
            if (!this.full.isAnyResourceSet()) return false;
            return true;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean isCleared() {
        Object object = this.requestLock;
        synchronized (object) {
            if (this.fullState != RequestCoordinator.RequestState.CLEARED) return false;
            return true;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean isComplete() {
        Object object = this.requestLock;
        synchronized (object) {
            if (this.fullState != RequestCoordinator.RequestState.SUCCESS) return false;
            return true;
        }
    }

    @Override
    public boolean isEquivalentTo(Request request) {
        boolean bl = request instanceof ThumbnailRequestCoordinator;
        boolean bl2 = false;
        if (bl) {
            request = (ThumbnailRequestCoordinator)request;
            if ((this.full == null ? ((ThumbnailRequestCoordinator)request).full == null : this.full.isEquivalentTo(((ThumbnailRequestCoordinator)request).full)) && (this.thumb == null ? ((ThumbnailRequestCoordinator)request).thumb == null : this.thumb.isEquivalentTo(((ThumbnailRequestCoordinator)request).thumb))) {
                bl2 = true;
            }
            return bl2;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean isRunning() {
        Object object = this.requestLock;
        synchronized (object) {
            if (this.fullState != RequestCoordinator.RequestState.RUNNING) return false;
            return true;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void onRequestFailed(Request object) {
        Object object2 = this.requestLock;
        synchronized (object2) {
            if (!object.equals(this.full)) {
                this.thumbState = RequestCoordinator.RequestState.FAILED;
                return;
            }
            this.fullState = RequestCoordinator.RequestState.FAILED;
            object = this.parent;
            if (object != null) {
                object.onRequestFailed(this);
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void onRequestSuccess(Request object) {
        Object object2 = this.requestLock;
        synchronized (object2) {
            if (object.equals(this.thumb)) {
                this.thumbState = RequestCoordinator.RequestState.SUCCESS;
                return;
            }
            this.fullState = RequestCoordinator.RequestState.SUCCESS;
            object = this.parent;
            if (object != null) {
                object.onRequestSuccess(this);
            }
            if (!this.thumbState.isComplete()) {
                this.thumb.clear();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void pause() {
        Object object = this.requestLock;
        synchronized (object) {
            if (!this.thumbState.isComplete()) {
                this.thumbState = RequestCoordinator.RequestState.PAUSED;
                this.thumb.pause();
            }
            if (!this.fullState.isComplete()) {
                this.fullState = RequestCoordinator.RequestState.PAUSED;
                this.full.pause();
            }
            return;
        }
    }

    public void setRequests(Request request, Request request2) {
        this.full = request;
        this.thumb = request2;
    }
}

