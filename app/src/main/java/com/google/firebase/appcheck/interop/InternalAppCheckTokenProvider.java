/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.appcheck.interop;

import com.google.android.gms.tasks.Task;
import com.google.firebase.appcheck.AppCheckTokenResult;
import com.google.firebase.appcheck.interop.AppCheckTokenListener;

public interface InternalAppCheckTokenProvider {
    public void addAppCheckTokenListener(AppCheckTokenListener var1);

    public Task<AppCheckTokenResult> getToken(boolean var1);

    public void removeAppCheckTokenListener(AppCheckTokenListener var1);
}

