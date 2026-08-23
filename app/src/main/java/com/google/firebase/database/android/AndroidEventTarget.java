/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 *  android.os.Looper
 */
package com.google.firebase.database.android;

import android.os.Handler;
import android.os.Looper;
import com.google.firebase.database.core.EventTarget;

public class AndroidEventTarget
implements EventTarget {
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void postEvent(Runnable runnable) {
        this.handler.post(runnable);
    }

    @Override
    public void restart() {
    }

    @Override
    public void shutdown() {
    }
}

