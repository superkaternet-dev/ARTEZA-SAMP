/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.android;

import com.google.firebase.auth.internal.IdTokenListener;
import com.google.firebase.database.android.AndroidAuthTokenProvider;
import com.google.firebase.database.core.TokenProvider;
import com.google.firebase.internal.InternalTokenResult;
import java.util.concurrent.ExecutorService;

public final class AndroidAuthTokenProvider$$ExternalSyntheticLambda2
implements IdTokenListener {
    public final ExecutorService f$0;
    public final TokenProvider.TokenChangeListener f$1;

    public /* synthetic */ AndroidAuthTokenProvider$$ExternalSyntheticLambda2(ExecutorService executorService, TokenProvider.TokenChangeListener tokenChangeListener) {
        this.f$0 = executorService;
        this.f$1 = tokenChangeListener;
    }

    @Override
    public final void onIdTokenChanged(InternalTokenResult internalTokenResult) {
        AndroidAuthTokenProvider.lambda$addTokenChangeListener$4(this.f$0, this.f$1, internalTokenResult);
    }
}

