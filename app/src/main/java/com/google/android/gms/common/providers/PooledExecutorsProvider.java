/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.providers;

import com.google.android.gms.common.providers.zza;
import java.util.concurrent.ScheduledExecutorService;

@Deprecated
public class PooledExecutorsProvider {
    private static PooledExecutorFactory zza;

    private PooledExecutorsProvider() {
    }

    @Deprecated
    public static PooledExecutorFactory getInstance() {
        synchronized (PooledExecutorsProvider.class) {
            PooledExecutorFactory pooledExecutorFactory;
            if (zza == null) {
                pooledExecutorFactory = new zza();
                zza = pooledExecutorFactory;
            }
            pooledExecutorFactory = zza;
            return pooledExecutorFactory;
        }
    }

    public static interface PooledExecutorFactory {
        @Deprecated
        public ScheduledExecutorService newSingleThreadScheduledExecutor();
    }
}

