/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.tasks;

import com.google.android.gms.tasks.Task;

public interface Continuation<TResult, TContinuationResult> {
    public TContinuationResult then(Task<TResult> var1) throws Exception;
}

