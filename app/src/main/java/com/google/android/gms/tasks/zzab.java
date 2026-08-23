/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.tasks;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import java.util.Collection;
import java.util.List;

final class zzab
implements Continuation<Void, Task<List<Task<?>>>> {
    final Collection zza;

    zzab(Collection collection) {
        this.zza = collection;
    }
}

