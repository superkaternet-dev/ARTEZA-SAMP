/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.RemoteException
 */
package com.google.android.gms.common.api.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.common.api.internal.RegistrationMethods;
import com.google.android.gms.common.api.internal.UnregisterListenerMethod;
import com.google.android.gms.tasks.TaskCompletionSource;

final class zacl
extends UnregisterListenerMethod {
    final RegistrationMethods.Builder zaa;

    zacl(RegistrationMethods.Builder builder, ListenerHolder.ListenerKey listenerKey) {
        this.zaa = builder;
        super(listenerKey);
    }

    protected final void unregisterListener(Api.AnyClient anyClient, TaskCompletionSource<Boolean> taskCompletionSource) throws RemoteException {
        RegistrationMethods.Builder.zab(this.zaa).accept(anyClient, taskCompletionSource);
    }
}

