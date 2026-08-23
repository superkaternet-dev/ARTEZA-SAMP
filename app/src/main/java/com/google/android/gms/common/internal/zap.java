/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.internal;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.ApiExceptionUtil;
import com.google.android.gms.common.internal.PendingResultUtil;
import com.google.android.gms.common.internal.zas;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.concurrent.TimeUnit;

final class zap
implements PendingResult.StatusListener {
    final PendingResult zaa;
    final TaskCompletionSource zab;
    final PendingResultUtil.ResultConverter zac;
    final zas zad;

    zap(PendingResult pendingResult, TaskCompletionSource taskCompletionSource, PendingResultUtil.ResultConverter resultConverter, zas zas2) {
        this.zaa = pendingResult;
        this.zab = taskCompletionSource;
        this.zac = resultConverter;
        this.zad = zas2;
    }

    @Override
    public final void onComplete(Status status) {
        if (status.isSuccess()) {
            status = this.zaa.await(0L, TimeUnit.MILLISECONDS);
            this.zab.setResult(this.zac.convert(status));
            return;
        }
        this.zab.setException(ApiExceptionUtil.fromStatus(status));
    }
}

