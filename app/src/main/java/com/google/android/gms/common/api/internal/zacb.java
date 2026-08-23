/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.internal.ListenerHolder;

public final class zacb
implements Runnable {
    public final ListenerHolder zaa;
    public final ListenerHolder.Notifier zab;

    public /* synthetic */ zacb(ListenerHolder listenerHolder, ListenerHolder.Notifier notifier) {
        this.zaa = listenerHolder;
        this.zab = notifier;
    }

    @Override
    public final void run() {
        this.zaa.zaa(this.zab);
    }
}

