/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.android;

import com.google.firebase.appcheck.AppCheckTokenResult;
import com.google.firebase.appcheck.interop.AppCheckTokenListener;
import com.google.firebase.database.android.AndroidAppCheckTokenProvider;
import com.google.firebase.database.core.TokenProvider;
import java.util.concurrent.ExecutorService;

public final class AndroidAppCheckTokenProvider$$ExternalSyntheticLambda2
implements AppCheckTokenListener {
    public final ExecutorService f$0;
    public final TokenProvider.TokenChangeListener f$1;

    public /* synthetic */ AndroidAppCheckTokenProvider$$ExternalSyntheticLambda2(ExecutorService executorService, TokenProvider.TokenChangeListener tokenChangeListener) {
        this.f$0 = executorService;
        this.f$1 = tokenChangeListener;
    }

    @Override
    public final void onAppCheckTokenChanged(AppCheckTokenResult appCheckTokenResult) {
        AndroidAppCheckTokenProvider.lambda$addTokenChangeListener$4(this.f$0, this.f$1, appCheckTokenResult);
    }
}

