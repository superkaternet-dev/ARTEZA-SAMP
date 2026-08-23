/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.internal.zaad;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

final class zaac
implements OnCompleteListener {
    final TaskCompletionSource zaa;
    final zaad zab;

    zaac(zaad zaad2, TaskCompletionSource taskCompletionSource) {
        this.zab = zaad2;
        this.zaa = taskCompletionSource;
    }

    public final void onComplete(Task task) {
        zaad.zab(this.zab).remove(this.zaa);
    }
}

