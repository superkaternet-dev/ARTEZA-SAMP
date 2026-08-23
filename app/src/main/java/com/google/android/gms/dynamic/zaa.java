/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.dynamic;

import com.google.android.gms.dynamic.DeferredLifecycleHelper;
import com.google.android.gms.dynamic.LifecycleDelegate;
import com.google.android.gms.dynamic.OnDelegateCreatedListener;
import com.google.android.gms.dynamic.zah;

final class zaa
implements OnDelegateCreatedListener {
    final DeferredLifecycleHelper zaa;

    zaa(DeferredLifecycleHelper deferredLifecycleHelper) {
        this.zaa = deferredLifecycleHelper;
    }

    public final void onDelegateCreated(LifecycleDelegate object) {
        DeferredLifecycleHelper.zac(this.zaa, (LifecycleDelegate)object);
        object = DeferredLifecycleHelper.zab(this.zaa).iterator();
        while (object.hasNext()) {
            ((zah)object.next()).zab(DeferredLifecycleHelper.zaa(this.zaa));
        }
        DeferredLifecycleHelper.zab(this.zaa).clear();
        DeferredLifecycleHelper.zad(this.zaa, null);
    }
}

