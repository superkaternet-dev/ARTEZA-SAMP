/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.internal;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.GetTokenResult;

public interface InternalTokenProvider {
    public Task<GetTokenResult> getAccessToken(boolean var1);

    public String getUid();
}

