/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.connection;

import com.google.firebase.database.connection.PersistentConnectionImpl;

public final class PersistentConnectionImpl$$ExternalSyntheticLambda4
implements Runnable {
    public final PersistentConnectionImpl f$0;
    public final boolean f$1;
    public final boolean f$2;

    public /* synthetic */ PersistentConnectionImpl$$ExternalSyntheticLambda4(PersistentConnectionImpl persistentConnectionImpl, boolean bl, boolean bl2) {
        this.f$0 = persistentConnectionImpl;
        this.f$1 = bl;
        this.f$2 = bl2;
    }

    @Override
    public final void run() {
        this.f$0.lambda$tryScheduleReconnect$3$com-google-firebase-database-connection-PersistentConnectionImpl(this.f$1, this.f$2);
    }
}

