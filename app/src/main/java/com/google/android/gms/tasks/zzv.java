/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 */
package com.google.android.gms.tasks;

import android.app.Activity;
import com.google.android.gms.common.api.internal.LifecycleCallback;
import com.google.android.gms.common.api.internal.LifecycleFragment;
import com.google.android.gms.tasks.zzq;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class zzv
extends LifecycleCallback {
    private final List<WeakReference<zzq<?>>> zza = new ArrayList();

    private zzv(LifecycleFragment lifecycleFragment) {
        super(lifecycleFragment);
        this.mLifecycleFragment.addCallback("TaskOnStopCallback", this);
    }

    public static zzv zza(Activity object) {
        LifecycleFragment lifecycleFragment = zzv.getFragment(object);
        zzv zzv2 = lifecycleFragment.getCallbackOrNull("TaskOnStopCallback", zzv.class);
        object = zzv2;
        if (zzv2 == null) {
            object = new zzv(lifecycleFragment);
        }
        return object;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final void onStop() {
        List<WeakReference<zzq<?>>> list = this.zza;
        synchronized (list) {
            Iterator<WeakReference<zzq<?>>> iterator2 = this.zza.iterator();
            while (true) {
                if (!iterator2.hasNext()) {
                    this.zza.clear();
                    return;
                }
                zzq zzq2 = (zzq)iterator2.next().get();
                if (zzq2 == null) continue;
                zzq2.zzc();
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final <T> void zzb(zzq<T> zzq2) {
        List<WeakReference<zzq<?>>> list = this.zza;
        synchronized (list) {
            List<WeakReference<zzq<?>>> list2 = this.zza;
            WeakReference<zzq<T>> weakReference = new WeakReference<zzq<T>>(zzq2);
            list2.add(weakReference);
            return;
        }
    }
}

