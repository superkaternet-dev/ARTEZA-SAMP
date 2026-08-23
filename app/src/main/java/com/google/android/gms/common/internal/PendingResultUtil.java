/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.internal;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.internal.zao;
import com.google.android.gms.common.internal.zap;
import com.google.android.gms.common.internal.zaq;
import com.google.android.gms.common.internal.zar;
import com.google.android.gms.common.internal.zas;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

public class PendingResultUtil {
    private static final zas zaa = new zao();

    public static <R extends Result, T extends Response<R>> Task<T> toResponseTask(PendingResult<R> pendingResult, T t) {
        return PendingResultUtil.toTask(pendingResult, new zaq(t));
    }

    public static <R extends Result, T> Task<T> toTask(PendingResult<R> pendingResult, ResultConverter<R, T> resultConverter) {
        zas zas2 = zaa;
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        pendingResult.addStatusListener(new zap(pendingResult, taskCompletionSource, resultConverter, zas2));
        return taskCompletionSource.getTask();
    }

    public static <R extends Result> Task<Void> toVoidTask(PendingResult<R> pendingResult) {
        return PendingResultUtil.toTask(pendingResult, new zar());
    }

    public static interface ResultConverter<R extends Result, T> {
        public T convert(R var1);
    }
}

