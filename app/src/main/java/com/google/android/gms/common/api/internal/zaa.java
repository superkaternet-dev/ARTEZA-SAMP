/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 */
package com.google.android.gms.common.api.internal;

import android.app.Activity;
import com.google.android.gms.common.api.internal.LifecycleCallback;
import com.google.android.gms.common.api.internal.LifecycleFragment;
import java.util.ArrayList;
import java.util.List;

final class zaa
extends LifecycleCallback {
    private List<Runnable> zaa = new ArrayList<Runnable>();

    private zaa(LifecycleFragment lifecycleFragment) {
        super(lifecycleFragment);
        this.mLifecycleFragment.addCallback("LifecycleObserverOnStop", this);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static /* bridge */ /* synthetic */ zaa zaa(Activity activity) {
        synchronized (activity) {
            LifecycleFragment lifecycleFragment = com.google.android.gms.common.api.internal.zaa.getFragment(activity);
            zaa zaa2 = lifecycleFragment.getCallbackOrNull("LifecycleObserverOnStop", zaa.class);
            if (zaa2 != null) return zaa2;
            return new zaa(lifecycleFragment);
        }
    }

    static /* bridge */ /* synthetic */ void zab(zaa zaa2, Runnable runnable) {
        zaa2.zac(runnable);
    }

    private final void zac(Runnable runnable) {
        synchronized (this) {
            this.zaa.add(runnable);
            return;
        }
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final void onStop() {
        Object object;
        synchronized (this) {
            List<Runnable> list = this.zaa;
            object = new ArrayList();
            this.zaa = object;
            // MONITOREXIT @DISABLED, blocks:[0, 2] lbl6 : MonitorExitStatement: MONITOREXIT : this
            object = list.iterator();
            {
                catch (Throwable throwable) {}
                {
                    throw throwable;
                }
            }
        }
        while (object.hasNext()) {
            ((Runnable)object.next()).run();
        }
    }
}

