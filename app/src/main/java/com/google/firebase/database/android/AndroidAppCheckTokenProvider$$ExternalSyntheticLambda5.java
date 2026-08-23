/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.android;

import com.google.firebase.appcheck.AppCheckTokenResult;
import com.google.firebase.database.android.AndroidAppCheckTokenProvider;
import com.google.firebase.database.core.TokenProvider;

public final class AndroidAppCheckTokenProvider$$ExternalSyntheticLambda5
implements Runnable {
    public final TokenProvider.TokenChangeListener f$0;
    public final AppCheckTokenResult f$1;

    public /* synthetic */ AndroidAppCheckTokenProvider$$ExternalSyntheticLambda5(TokenProvider.TokenChangeListener tokenChangeListener, AppCheckTokenResult appCheckTokenResult) {
        this.f$0 = tokenChangeListener;
        this.f$1 = appCheckTokenResult;
    }

    @Override
    public final void run() {
        AndroidAppCheckTokenProvider.lambda$addTokenChangeListener$3(this.f$0, this.f$1);
    }
}

