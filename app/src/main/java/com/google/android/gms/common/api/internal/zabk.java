/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 */
package com.google.android.gms.common.api.internal;

import android.os.Handler;
import java.util.concurrent.Executor;

public final class zabk
implements Executor {
    public final Handler zaa;

    public /* synthetic */ zabk(Handler handler) {
        this.zaa = handler;
    }

    @Override
    public final void execute(Runnable runnable) {
        this.zaa.post(runnable);
    }
}

