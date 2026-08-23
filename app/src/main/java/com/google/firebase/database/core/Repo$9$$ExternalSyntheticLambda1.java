/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.core.Repo;

public final class Repo$9$$ExternalSyntheticLambda1
implements Runnable {
    public final TaskCompletionSource f$0;
    public final DataSnapshot f$1;

    public /* synthetic */ Repo$9$$ExternalSyntheticLambda1(TaskCompletionSource taskCompletionSource, DataSnapshot dataSnapshot) {
        this.f$0 = taskCompletionSource;
        this.f$1 = dataSnapshot;
    }

    @Override
    public final void run() {
        Repo.9.lambda$run$0(this.f$0, this.f$1);
    }
}

