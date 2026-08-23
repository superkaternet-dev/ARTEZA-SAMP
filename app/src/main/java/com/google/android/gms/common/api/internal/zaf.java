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

public final class zaf
extends zad<Void> {
    public final zaci zab;

    public zaf(zaci zaci2, TaskCompletionSource<Void> taskCompletionSource) {
        super(3, taskCompletionSource);
        this.zab = zaci2;
    }

    @Override
    public final boolean zaa(zabq<?> zabq2) {
        return this.zab.zaa.zab();
    }

    @Override
    public final Feature[] zab(zabq<?> zabq2) {
        return this.zab.zaa.getRequiredFeatures();
    }

    @Override
    public final void zac(zabq<?> zabq2) throws RemoteException {
        this.zab.zaa.registerListener(zabq2.zaf(), this.zaa);
        ListenerHolder.ListenerKey<?> listenerKey = this.zab.zaa.getListenerKey();
        if (listenerKey != null) {
            zabq2.zah().put(listenerKey, this.zab);
        }
    }
}

