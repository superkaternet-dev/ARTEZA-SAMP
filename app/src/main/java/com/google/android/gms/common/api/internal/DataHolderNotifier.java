/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.common.data.DataHolder;

public abstract class DataHolderNotifier<L>
implements ListenerHolder.Notifier<L> {
    private final DataHolder zaa;

    protected DataHolderNotifier(DataHolder dataHolder) {
        this.zaa = dataHolder;
    }

    @Override
    public final void notifyListener(L l) {
        this.notifyListener(l, this.zaa);
    }

    protected abstract void notifyListener(L var1, DataHolder var2);

    @Override
    public void onNotifyListenerFailed() {
        DataHolder dataHolder = this.zaa;
        if (dataHolder != null) {
            dataHolder.close();
        }
    }
}

