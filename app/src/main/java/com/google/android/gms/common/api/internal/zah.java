/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.RemoteException
 */
package com.google.android.gms.common.api.internal;

import android.os.RemoteException;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.common.api.internal.zabq;
import com.google.android.gms.common.api.internal.zaci;
import com.google.android.gms.common.api.internal.zad;
import com.google.android.gms.tasks.TaskCompletionSource;

public final class zah
extends zad<Boolean> {
    public final ListenerHolder.ListenerKey<?> zab;

    public zah(ListenerHolder.ListenerKey<?> listenerKey, TaskCompletionSource<Boolean> taskCompletionSource) {
        super(4, taskCompletionSource);
        this.zab = listenerKey;
    }

    @Override
    public final boolean zaa(zabq<?> object) {
        return (object = ((zabq)object).zah().get(this.zab)) != null && ((zaci)object).zaa.zab();
    }

    @Override
    public final Feature[] zab(zabq<?> object) {
        if ((object = ((zabq)object).zah().get(this.zab)) == null) {
            return null;
        }
        return ((zaci)object).zaa.getRequiredFeatures();
    }

    @Override
    public final void zac(zabq<?> zabq2) throws RemoteException {
        zaci zaci2 = zabq2.zah().remove(this.zab);
        if (zaci2 != null) {
            zaci2.zab.unregisterListener(zabq2.zaf(), this.zaa);
            zaci2.zaa.clearListener();
            return;
        }
        this.zaa.trySetResult(false);
    }
}

