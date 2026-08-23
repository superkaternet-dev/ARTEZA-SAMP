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
import com.google.android.gms.common.api.internal.TaskApiCall;
import com.google.android.gms.tasks.TaskCompletionSource;

final class zacv
extends TaskApiCall {
    final TaskApiCall.Builder zaa;

    zacv(TaskApiCall.Builder builder, Feature[] featureArray, boolean bl, int n) {
        this.zaa = builder;
        super(featureArray, bl, n);
    }

    protected final void doExecute(Api.AnyClient anyClient, TaskCompletionSource taskCompletionSource) throws RemoteException {
        TaskApiCall.Builder.zaa(this.zaa).accept(anyClient, taskCompletionSource);
    }
}

