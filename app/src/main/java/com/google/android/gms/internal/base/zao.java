/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.internal.base;

import com.google.android.gms.internal.base.zam;
import com.google.android.gms.internal.base.zan;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

final class zao
implements zam {
    private zao() {
    }

    /* synthetic */ zao(zan zan2) {
    }

    @Override
    public final ExecutorService zaa(ThreadFactory threadFactory, int n) {
        return this.zac(1, threadFactory, 1);
    }

    @Override
    public final ExecutorService zab(int n, int n2) {
        return this.zac(4, Executors.defaultThreadFactory(), 2);
    }

    @Override
    public final ExecutorService zac(int n, ThreadFactory object, int n2) {
        object = new ThreadPoolExecutor(n, n, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), (ThreadFactory)object);
        ((ThreadPoolExecutor)object).allowCoreThreadTimeOut(true);
        return Executors.unconfigurableExecutorService((ExecutorService)object);
    }
}

