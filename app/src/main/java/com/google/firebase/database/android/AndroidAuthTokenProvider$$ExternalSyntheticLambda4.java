/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.android;

import com.google.firebase.database.android.AndroidAuthTokenProvider;
import com.google.firebase.database.core.TokenProvider;
import com.google.firebase.inject.Deferred;
import com.google.firebase.inject.Provider;
import java.util.concurrent.ExecutorService;

public final class AndroidAuthTokenProvider$$ExternalSyntheticLambda4
implements Deferred.DeferredHandler {
    public final ExecutorService f$0;
    public final TokenProvider.TokenChangeListener f$1;

    public /* synthetic */ AndroidAuthTokenProvider$$ExternalSyntheticLambda4(ExecutorService executorService, TokenProvider.TokenChangeListener tokenChangeListener) {
        this.f$0 = executorService;
        this.f$1 = tokenChangeListener;
    }

    public final void handle(Provider provider) {
        AndroidAuthTokenProvider.lambda$addTokenChangeListener$5(this.f$0, this.f$1, provider);
    }
}

