/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.internal.ActivityLifecycleObserver;
import com.google.android.gms.common.api.internal.zaa;
import java.lang.ref.WeakReference;

public final class zab
extends ActivityLifecycleObserver {
    private final WeakReference<zaa> zaa;

    zab(zaa zaa2) {
        this.zaa = new WeakReference<zaa>(zaa2);
    }

    @Override
    public final ActivityLifecycleObserver onStopCallOnce(Runnable runnable) {
        zaa zaa2 = (zaa)this.zaa.get();
        if (zaa2 != null) {
            com.google.android.gms.common.api.internal.zaa.zab(zaa2, runnable);
            return this;
        }
        throw new IllegalStateException("The target activity has already been GC'd");
    }
}

