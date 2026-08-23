/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.dynamic;

import com.google.android.gms.dynamic.DeferredLifecycleHelper;
import com.google.android.gms.dynamic.LifecycleDelegate;
import com.google.android.gms.dynamic.zah;

final class zaf
implements zah {
    final DeferredLifecycleHelper zaa;

    zaf(DeferredLifecycleHelper deferredLifecycleHelper) {
        this.zaa = deferredLifecycleHelper;
    }

    @Override
    public final int zaa() {
        return 4;
    }

    @Override
    public final void zab(LifecycleDelegate lifecycleDelegate) {
        DeferredLifecycleHelper.zaa(this.zaa).onStart();
    }
}

