/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.RemoteException
 */
package com.google.android.gms.common.api.internal;

import android.os.RemoteException;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.tasks.TaskCompletionSource;

public abstract class RegisterListenerMethod<A extends Api.AnyClient, L> {
    private final ListenerHolder<L> zaa;
    private final Feature[] zab;
    private final boolean zac;
    private final int zad;

    protected RegisterListenerMethod(ListenerHolder<L> listenerHolder) {
        this(listenerHolder, null, false, 0);
    }

    protected RegisterListenerMethod(ListenerHolder<L> listenerHolder, Feature[] featureArray, boolean bl) {
        this(listenerHolder, featureArray, bl, 0);
    }

    protected RegisterListenerMethod(ListenerHolder<L> listenerHolder, Feature[] featureArray, boolean bl, int n) {
        this.zaa = listenerHolder;
        this.zab = featureArray;
        this.zac = bl;
        this.zad = n;
    }

    public void clearListener() {
        this.zaa.clear();
    }

    public ListenerHolder.ListenerKey<L> getListenerKey() {
        return this.zaa.getListenerKey();
    }

    public Feature[] getRequiredFeatures() {
        return this.zab;
    }

    protected abstract void registerListener(A var1, TaskCompletionSource<Void> var2) throws RemoteException;

    public final int zaa() {
        return this.zad;
    }

    public final boolean zab() {
        return this.zac;
    }
}

