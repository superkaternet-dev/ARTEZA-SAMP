/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultTransform;
import com.google.android.gms.common.api.TransformedResult;
import java.util.concurrent.TimeUnit;

public abstract class PendingResultFacade<A extends Result, B extends Result>
extends PendingResult<B> {
    @Override
    public final void addStatusListener(PendingResult.StatusListener statusListener) {
        throw null;
    }

    @Override
    public final B await() {
        throw null;
    }

    @Override
    public final B await(long l, TimeUnit timeUnit) {
        throw null;
    }

    @Override
    public final void cancel() {
        throw null;
    }

    @Override
    public final boolean isCanceled() {
        throw null;
    }

    @Override
    public final void setResultCallback(ResultCallback<? super B> resultCallback) {
        throw null;
    }

    @Override
    public final void setResultCallback(ResultCallback<? super B> resultCallback, long l, TimeUnit timeUnit) {
        throw null;
    }

    @Override
    public final <S extends Result> TransformedResult<S> then(ResultTransform<? super B, ? extends S> resultTransform) {
        throw null;
    }
}

