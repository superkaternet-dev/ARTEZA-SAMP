/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.request;

import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestCoordinator;

public final class ErrorRequestCoordinator
implements RequestCoordinator,
Request {
    private volatile Request error;
    private RequestCoordinator.RequestState errorState;
    private final RequestCoordinator parent;
    private volatile Request primary;
    private RequestCoordinator.RequestState primaryState = RequestCoordinator.RequestState.CLEARED;
    private final Object requestLock;

    public ErrorRequestCoordinator(Object object, RequestCoordinator requestCoordinator) {
        this.errorState = RequestCoordinator.RequestState.CLEARED;
        this.requestLock = object;
        this.parent = requestCoordinator;
    }

    private boolean isValidRequest(Request request) {
        boolean bl = request.equals(this.primary) || this.primaryState == RequestCoordinator.RequestState.FAILED && request.equals(this.error);
        return bl;
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
            if (this.primaryState != RequestCoordinator.RequestState.RUNNING) {
                this.primaryState = RequestCoordinator.RequestState.RUNNING;
                this.primary.begin();
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
    public boolean canNotifyCleared(Request request) {
        Object object = this.requestLock;
        synchronized (object) {
            if (!this.parentCanNotifyCleared()) return false;
            if (!this.isValidRequest(request)) return false;
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
            if (!this.isValidRequest(request)) return false;
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
            if (!this.isValidRequest(request)) return false;
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
            this.primaryState = RequestCoordinator.RequestState.CLEARED;
            this.primary.clear();
            if (this.errorState != RequestCoordinator.RequestState.CLEARED) {
                this.errorState = RequestCoordinator.RequestState.CLEARED;
                this.error.clear();
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
            if (this.primary.isAnyResourceSet()) return true;
            if (!this.error.isAnyResourceSet()) return false;
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
            if (this.primaryState != RequestCoordinator.RequestState.CLEARED) return false;
            if (this.errorState != RequestCoordinator.RequestState.CLEARED) return false;
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
            if (this.primaryState == RequestCoordinator.RequestState.SUCCESS) return true;
            if (this.errorState != RequestCoordinator.RequestState.SUCCESS) return false;
            return true;
        }
    }

    @Override
    public boolean isEquivalentTo(Request request) {
        boolean bl = request instanceof ErrorRequestCoordinator;
        boolean bl2 = false;
        if (bl) {
            request = (ErrorRequestCoordinator)request;
            bl = bl2;
            if (this.primary.isEquivalentTo(((ErrorRequestCoordinator)request).primary)) {
                bl = bl2;
                if (this.error.isEquivalentTo(((ErrorRequestCoordinator)request).error)) {
                    bl = true;
                }
            }
            return bl;
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
            if (this.primaryState == RequestCoordinator.RequestState.RUNNING) return true;
            if (this.errorState != RequestCoordinator.RequestState.RUNNING) return false;
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
            if (!object.equals(this.error)) {
                this.primaryState = RequestCoordinator.RequestState.FAILED;
                if (this.errorState != RequestCoordinator.RequestState.RUNNING) {
                    this.errorState = RequestCoordinator.RequestState.RUNNING;
                    this.error.begin();
                }
                return;
            }
            this.errorState = RequestCoordinator.RequestState.FAILED;
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
            if (object.equals(this.primary)) {
                this.primaryState = RequestCoordinator.RequestState.SUCCESS;
            } else if (object.equals(this.error)) {
                this.errorState = RequestCoordinator.RequestState.SUCCESS;
            }
            object = this.parent;
            if (object != null) {
                object.onRequestSuccess(this);
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
            if (this.primaryState == RequestCoordinator.RequestState.RUNNING) {
                this.primaryState = RequestCoordinator.RequestState.PAUSED;
                this.primary.pause();
            }
            if (this.errorState == RequestCoordinator.RequestState.RUNNING) {
                this.errorState = RequestCoordinator.RequestState.PAUSED;
                this.error.pause();
            }
            return;
        }
    }

    public void setRequests(Request request, Request request2) {
        this.primary = request;
        this.error = request2;
    }
}

