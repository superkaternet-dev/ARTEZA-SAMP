/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.android;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.android.AndroidAuthTokenProvider;
import com.google.firebase.database.core.TokenProvider;

public final class AndroidAuthTokenProvider$$ExternalSyntheticLambda0
implements OnFailureListener {
    public final TokenProvider.GetTokenCompletionListener f$0;

    public /* synthetic */ AndroidAuthTokenProvider$$ExternalSyntheticLambda0(TokenProvider.GetTokenCompletionListener getTokenCompletionListener) {
        this.f$0 = getTokenCompletionListener;
    }

    @Override
    public final void onFailure(Exception exception) {
        AndroidAuthTokenProvider.lambda$getToken$2(this.f$0, exception);
    }
}

