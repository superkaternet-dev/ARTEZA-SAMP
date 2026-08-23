/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BasePendingResult;
import com.google.android.gms.common.api.internal.GoogleApiManager;
import com.google.android.gms.common.api.internal.zaab;
import com.google.android.gms.common.api.internal.zaac;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public final class zaad {
    private final Map<BasePendingResult<?>, Boolean> zaa = Collections.synchronizedMap(new WeakHashMap());
    private final Map<TaskCompletionSource<?>, Boolean> zab = Collections.synchronizedMap(new WeakHashMap());

    static /* bridge */ /* synthetic */ Map zaa(zaad zaad2) {
        return zaad2.zaa;
    }

    static /* bridge */ /* synthetic */ Map zab(zaad zaad2) {
        return zaad2.zab;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private final void zah(boolean bl, Status status) {
        Object object = this.zaa;
        // MONITORENTER : object
        Object object22 = new HashMap(this.zaa);
        // MONITOREXIT : object
        Iterator iterator2 = this.zab;
        object = new HashMap(this.zab);
        for (Object object22 : object22.entrySet()) {
            if (!bl && !((Boolean)object22.getValue()).booleanValue()) continue;
            ((BasePendingResult)object22.getKey()).forceFailureUnlessReady(status);
        }
        iterator2 = object.entrySet().iterator();
        while (iterator2.hasNext()) {
            object = iterator2.next();
            if (!bl && !((Boolean)object.getValue()).booleanValue()) continue;
            ((TaskCompletionSource)object.getKey()).trySetException(new ApiException(status));
        }
        return;
        finally {
            // MONITORENTER : iterator2
        }
    }

    final void zac(BasePendingResult<? extends Result> basePendingResult, boolean bl) {
        this.zaa.put(basePendingResult, bl);
        ((PendingResult)basePendingResult).addStatusListener(new zaab(this, basePendingResult));
    }

    final <TResult> void zad(TaskCompletionSource<TResult> taskCompletionSource, boolean bl) {
        this.zab.put(taskCompletionSource, bl);
        taskCompletionSource.getTask().addOnCompleteListener(new zaac(this, taskCompletionSource));
    }

    final void zae(int n, String string2) {
        StringBuilder stringBuilder = new StringBuilder("The connection to Google Play services was lost");
        if (n == 1) {
            stringBuilder.append(" due to service disconnection.");
        } else if (n == 3) {
            stringBuilder.append(" due to dead object exception.");
        }
        if (string2 != null) {
            stringBuilder.append(" Last reason for disconnect: ");
            stringBuilder.append(string2);
        }
        this.zah(true, new Status(20, stringBuilder.toString()));
    }

    public final void zaf() {
        this.zah(false, GoogleApiManager.zaa);
    }

    final boolean zag() {
        return !this.zaa.isEmpty() || !this.zab.isEmpty();
        {
        }
    }
}

