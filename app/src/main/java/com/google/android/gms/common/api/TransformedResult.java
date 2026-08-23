/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.ResultTransform;

public abstract class TransformedResult<R extends Result> {
    public abstract void andFinally(ResultCallbacks<? super R> var1);

    public abstract <S extends Result> TransformedResult<S> then(ResultTransform<? super R, ? extends S> var1);
}

