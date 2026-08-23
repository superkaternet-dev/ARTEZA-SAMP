/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.connection;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.connection.PersistentConnectionImpl;

public final class PersistentConnectionImpl$$ExternalSyntheticLambda1
implements OnSuccessListener {
    public final PersistentConnectionImpl f$0;
    public final long f$1;
    public final Task f$2;
    public final Task f$3;

    public /* synthetic */ PersistentConnectionImpl$$ExternalSyntheticLambda1(PersistentConnectionImpl persistentConnectionImpl, long l, Task task, Task task2) {
        this.f$0 = persistentConnectionImpl;
        this.f$1 = l;
        this.f$2 = task;
        this.f$3 = task2;
    }

    public final void onSuccess(Object object) {
        this.f$0.lambda$tryScheduleReconnect$1$com-google-firebase-database-connection-PersistentConnectionImpl(this.f$1, this.f$2, this.f$3, (Void)object);
    }
}

