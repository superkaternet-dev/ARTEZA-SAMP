/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.connection;

import com.google.firebase.database.connection.PersistentConnectionImpl;
import java.util.Map;

public final class PersistentConnectionImpl$$ExternalSyntheticLambda3
implements PersistentConnectionImpl.ConnectionRequestCallback {
    public final PersistentConnectionImpl f$0;
    public final boolean f$1;

    public /* synthetic */ PersistentConnectionImpl$$ExternalSyntheticLambda3(PersistentConnectionImpl persistentConnectionImpl, boolean bl) {
        this.f$0 = persistentConnectionImpl;
        this.f$1 = bl;
    }

    public final void onResponse(Map map) {
        this.f$0.lambda$sendAppCheckTokenHelper$4$com-google-firebase-database-connection-PersistentConnectionImpl(this.f$1, map);
    }
}

