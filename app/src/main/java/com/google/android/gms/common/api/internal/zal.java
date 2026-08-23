/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import androidx.collection.ArrayMap;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.AvailabilityException;
import com.google.android.gms.common.api.HasApiKey;
import com.google.android.gms.common.api.internal.ApiKey;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class zal {
    private final ArrayMap<ApiKey<?>, ConnectionResult> zaa;
    private final ArrayMap<ApiKey<?>, String> zab = new ArrayMap();
    private final TaskCompletionSource<Map<ApiKey<?>, String>> zac = new TaskCompletionSource();
    private int zad;
    private boolean zae = false;

    public zal(Iterable<? extends HasApiKey<?>> object) {
        this.zaa = new ArrayMap();
        Iterator<HasApiKey<?>> iterator2 = object.iterator();
        while (iterator2.hasNext()) {
            object = iterator2.next();
            this.zaa.put(object.getApiKey(), null);
        }
        this.zad = this.zaa.keySet().size();
    }

    public final Task<Map<ApiKey<?>, String>> zaa() {
        return this.zac.getTask();
    }

    public final Set<ApiKey<?>> zab() {
        return this.zaa.keySet();
    }

    public final void zac(ApiKey<?> object, ConnectionResult connectionResult, String string2) {
        this.zaa.put((ApiKey<?>)object, connectionResult);
        this.zab.put((ApiKey<?>)object, string2);
        --this.zad;
        if (!connectionResult.isSuccess()) {
            this.zae = true;
        }
        if (this.zad == 0) {
            if (this.zae) {
                object = new AvailabilityException(this.zaa);
                this.zac.setException((Exception)object);
                return;
            }
            this.zac.setResult(this.zab);
        }
    }
}

