/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 *  android.os.Looper
 */
package com.google.android.gms.common.util.concurrent;

import android.os.Handler;
import android.os.Looper;
import com.google.android.gms.internal.common.zzi;
import java.util.concurrent.Executor;

public class HandlerExecutor
implements Executor {
    private final Handler zza;

    public HandlerExecutor(Looper looper) {
        this.zza = new zzi(looper);
    }

    @Override
    public final void execute(Runnable runnable) {
        this.zza.post(runnable);
    }
}

