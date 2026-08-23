/*
 * Decompiled with CFR 0.152.
 */
package com.downloader;

import com.downloader.Error;

public class Response {
    private Error error;
    private boolean isCancelled;
    private boolean isPaused;
    private boolean isSuccessful;

    public Error getError() {
        return this.error;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public boolean isPaused() {
        return this.isPaused;
    }

    public boolean isSuccessful() {
        return this.isSuccessful;
    }

    public void setCancelled(boolean bl) {
        this.isCancelled = bl;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public void setPaused(boolean bl) {
        this.isPaused = bl;
    }

    public void setSuccessful(boolean bl) {
        this.isSuccessful = bl;
    }
}

