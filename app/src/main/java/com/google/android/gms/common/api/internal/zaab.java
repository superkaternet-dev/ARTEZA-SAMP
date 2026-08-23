/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BasePendingResult;
import com.google.android.gms.common.api.internal.zaad;

final class zaab
implements PendingResult.StatusListener {
    final BasePendingResult zaa;
    final zaad zab;

    zaab(zaad zaad2, BasePendingResult basePendingResult) {
        this.zab = zaad2;
        this.zaa = basePendingResult;
    }

    @Override
    public final void onComplete(Status status) {
        zaad.zaa(this.zab).remove(this.zaa);
    }
}

