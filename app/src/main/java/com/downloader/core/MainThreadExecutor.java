/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 *  android.os.Looper
 */
package com.downloader.core;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Executor;

public class MainThreadExecutor
implements Executor {
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(Runnable runnable) {
        this.handler.post(runnable);
    }
}

