/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.connection;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.connection.PersistentConnectionImpl;

public final class PersistentConnectionImpl$$ExternalSyntheticLambda0
implements OnFailureListener {
    public final PersistentConnectionImpl f$0;
    public final long f$1;

    public /* synthetic */ PersistentConnectionImpl$$ExternalSyntheticLambda0(PersistentConnectionImpl persistentConnectionImpl, long l) {
        this.f$0 = persistentConnectionImpl;
        this.f$1 = l;
    }

    @Override
    public final void onFailure(Exception exception) {
        this.f$0.lambda$tryScheduleReconnect$2$com-google-firebase-database-connection-PersistentConnectionImpl(this.f$1, exception);
    }
}

