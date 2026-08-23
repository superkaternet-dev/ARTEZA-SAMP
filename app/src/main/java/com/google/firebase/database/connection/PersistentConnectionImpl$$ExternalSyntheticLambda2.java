/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.connection;

import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.connection.PersistentConnectionImpl;
import java.util.Map;

public final class PersistentConnectionImpl$$ExternalSyntheticLambda2
implements PersistentConnectionImpl.ConnectionRequestCallback {
    public final PersistentConnectionImpl f$0;
    public final PersistentConnectionImpl.QuerySpec f$1;
    public final TaskCompletionSource f$2;

    public /* synthetic */ PersistentConnectionImpl$$ExternalSyntheticLambda2(PersistentConnectionImpl persistentConnectionImpl, PersistentConnectionImpl.QuerySpec querySpec, TaskCompletionSource taskCompletionSource) {
        this.f$0 = persistentConnectionImpl;
        this.f$1 = querySpec;
        this.f$2 = taskCompletionSource;
    }

    public final void onResponse(Map map) {
        this.f$0.lambda$get$0$com-google-firebase-database-connection-PersistentConnectionImpl(this.f$1, this.f$2, map);
    }
}

