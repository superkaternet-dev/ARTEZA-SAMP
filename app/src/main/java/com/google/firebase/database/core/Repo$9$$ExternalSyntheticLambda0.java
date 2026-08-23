/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import com.google.firebase.database.core.Repo;

public final class Repo$9$$ExternalSyntheticLambda0
implements OnCompleteListener {
    public final Repo.9 f$0;
    public final TaskCompletionSource f$1;
    public final DataSnapshot f$2;
    public final Query f$3;

    public /* synthetic */ Repo$9$$ExternalSyntheticLambda0(Repo.9 var1_1, TaskCompletionSource taskCompletionSource, DataSnapshot dataSnapshot, Query query) {
        this.f$0 = var1_1;
        this.f$1 = taskCompletionSource;
        this.f$2 = dataSnapshot;
        this.f$3 = query;
    }

    public final void onComplete(Task task) {
        this.f$0.lambda$run$1$com-google-firebase-database-core-Repo$9(this.f$1, this.f$2, this.f$3, task);
    }
}

