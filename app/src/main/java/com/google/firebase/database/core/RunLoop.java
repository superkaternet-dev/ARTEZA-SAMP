/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import java.util.concurrent.ScheduledFuture;

public interface RunLoop {
    public void restart();

    public ScheduledFuture schedule(Runnable var1, long var2);

    public void scheduleNow(Runnable var1);

    public void shutdown();
}

