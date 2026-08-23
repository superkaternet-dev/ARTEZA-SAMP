/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.tasks;

import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.concurrent.TimeoutException;

public final class zzy
implements Runnable {
    public final TaskCompletionSource zza;

    public /* synthetic */ zzy(TaskCompletionSource taskCompletionSource) {
        this.zza = taskCompletionSource;
    }

    @Override
    public final void run() {
        this.zza.trySetException(new TimeoutException());
    }
}

