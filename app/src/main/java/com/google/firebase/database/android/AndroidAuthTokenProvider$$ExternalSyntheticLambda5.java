/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.android;

import com.google.firebase.database.android.AndroidAuthTokenProvider;
import com.google.firebase.database.core.TokenProvider;
import com.google.firebase.internal.InternalTokenResult;

public final class AndroidAuthTokenProvider$$ExternalSyntheticLambda5
implements Runnable {
    public final TokenProvider.TokenChangeListener f$0;
    public final InternalTokenResult f$1;

    public /* synthetic */ AndroidAuthTokenProvider$$ExternalSyntheticLambda5(TokenProvider.TokenChangeListener tokenChangeListener, InternalTokenResult internalTokenResult) {
        this.f$0 = tokenChangeListener;
        this.f$1 = internalTokenResult;
    }

    @Override
    public final void run() {
        AndroidAuthTokenProvider.lambda$addTokenChangeListener$3(this.f$0, this.f$1);
    }
}

