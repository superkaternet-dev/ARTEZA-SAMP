/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.internal.BasePendingResult;
import com.google.android.gms.common.api.internal.zar;

final class zas {
    final BasePendingResult zaa;

    /* synthetic */ zas(BasePendingResult basePendingResult, zar zar2) {
        this.zaa = basePendingResult;
    }

    protected final void finalize() throws Throwable {
        BasePendingResult.zal(BasePendingResult.zaj(this.zaa));
        super.finalize();
    }
}

