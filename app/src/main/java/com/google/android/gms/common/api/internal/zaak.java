/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.internal.zaaw;

final class zaak
implements Runnable {
    final zaaw zaa;

    zaak(zaaw zaaw2) {
        this.zaa = zaaw2;
    }

    @Override
    public final void run() {
        zaaw zaaw2 = this.zaa;
        zaaw.zaf(zaaw2).cancelAvailabilityErrorNotifications(zaaw.zac(zaaw2));
    }
}

