/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.internal.zaaa;

final class zav
implements Runnable {
    final zaaa zaa;

    zav(zaaa zaaa2) {
        this.zaa = zaaa2;
    }

    @Override
    public final void run() {
        zaaa.zaj(this.zaa).lock();
        try {
            zaaa.zap(this.zaa);
            return;
        }
        finally {
            zaaa.zaj(this.zaa).unlock();
        }
    }
}

