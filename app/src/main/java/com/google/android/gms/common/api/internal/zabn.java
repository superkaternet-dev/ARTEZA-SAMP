/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.internal.zabq;

final class zabn
implements Runnable {
    final int zaa;
    final zabq zab;

    zabn(zabq zabq2, int n) {
        this.zab = zabq2;
        this.zaa = n;
    }

    @Override
    public final void run() {
        zabq.zak(this.zab, this.zaa);
    }
}

