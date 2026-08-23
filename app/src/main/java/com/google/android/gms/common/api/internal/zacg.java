/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 */
package com.google.android.gms.common.api.internal;

import android.os.IBinder;
import com.google.android.gms.common.api.internal.NonGmsServiceBrokerClient;

public final class zacg
implements Runnable {
    public final NonGmsServiceBrokerClient zaa;
    public final IBinder zab;

    public /* synthetic */ zacg(NonGmsServiceBrokerClient nonGmsServiceBrokerClient, IBinder iBinder) {
        this.zaa = nonGmsServiceBrokerClient;
        this.zab = iBinder;
    }

    @Override
    public final void run() {
        this.zaa.zaa(this.zab);
    }
}

