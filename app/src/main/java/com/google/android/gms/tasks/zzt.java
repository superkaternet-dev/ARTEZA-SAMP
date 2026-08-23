/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.tasks;

import java.util.concurrent.Executor;

final class zzt
implements Executor {
    zzt() {
    }

    @Override
    public final void execute(Runnable runnable) {
        runnable.run();
    }
}

