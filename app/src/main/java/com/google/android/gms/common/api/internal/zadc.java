/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BasePendingResult;
import com.google.android.gms.common.api.internal.zadb;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public final class zadc {
    public static final Status zaa = new Status(8, "The connection to Google Play services was lost");
    final Set<BasePendingResult<?>> zab = Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap()));
    private final zadb zac = new zadb(this);

    final void zaa(BasePendingResult<? extends Result> basePendingResult) {
        this.zab.add(basePendingResult);
        basePendingResult.zan(this.zac);
    }

    public final void zab() {
        BasePendingResult[] basePendingResultArray = this.zab;
        for (BasePendingResult basePendingResult : basePendingResultArray.toArray(new BasePendingResult[0])) {
            basePendingResult.zan(null);
            if (!basePendingResult.zam()) continue;
            this.zab.remove(basePendingResult);
        }
    }
}

