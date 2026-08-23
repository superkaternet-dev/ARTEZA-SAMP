/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.auth.internal;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.internal.IdTokenListener;
import com.google.firebase.internal.InternalTokenProvider;

public interface InternalAuthProvider
extends InternalTokenProvider {
    public void addIdTokenListener(IdTokenListener var1);

    @Override
    public Task<GetTokenResult> getAccessToken(boolean var1);

    @Override
    public String getUid();

    public void removeIdTokenListener(IdTokenListener var1);
}

