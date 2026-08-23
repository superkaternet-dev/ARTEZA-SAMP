/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.internal.zabq;

final class zabm
implements Runnable {
    final zabq zaa;

    zabm(zabq zabq2) {
        this.zaa = zabq2;
    }

    @Override
    public final void run() {
        zabq.zaj(this.zaa);
    }
}

