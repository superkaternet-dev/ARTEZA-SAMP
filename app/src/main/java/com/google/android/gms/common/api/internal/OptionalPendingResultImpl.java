/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultTransform;
import com.google.android.gms.common.api.TransformedResult;
import com.google.android.gms.common.api.internal.BasePendingResult;
import java.util.concurrent.TimeUnit;

public final class OptionalPendingResultImpl<R extends Result>
extends OptionalPendingResult<R> {
    private final BasePendingResult<R> zaa;

    public OptionalPendingResultImpl(PendingResult<R> pendingResult) {
        this.zaa = (BasePendingResult)pendingResult;
    }

    @Override
    public final void addStatusListener(PendingResult.StatusListener statusListener) {
        ((PendingResult)this.zaa).addStatusListener(statusListener);
    }

    @Override
    public final R await() {
        return this.zaa.await();
    }

    @Override
    public final R await(long l, TimeUnit timeUnit) {
        return this.zaa.await(l, timeUnit);
    }

    @Override
    public final void cancel() {
        this.zaa.cancel();
    }

    @Override
    public final R get() {
        if (this.zaa.isReady()) {
            TimeUnit timeUnit = TimeUnit.MILLISECONDS;
            return this.zaa.await(0L, timeUnit);
        }
        throw new IllegalStateException("Result is not available. Check that isDone() returns true before calling get().");
    }

    @Override
    public final boolean isCanceled() {
        return this.zaa.isCanceled();
    }

    @Override
    public final boolean isDone() {
        return this.zaa.isReady();
    }

    @Override
    public final void setResultCallback(ResultCallback<? super R> resultCallback) {
        this.zaa.setResultCallback(resultCallback);
    }

    @Override
    public final void setResultCallback(ResultCallback<? super R> resultCallback, long l, TimeUnit timeUnit) {
        this.zaa.setResultCallback(resultCallback, l, timeUnit);
    }

    @Override
    public final <S extends Result> TransformedResult<S> then(ResultTransform<? super R, ? extends S> resultTransform) {
        return ((PendingResult)this.zaa).then(resultTransform);
    }
}

