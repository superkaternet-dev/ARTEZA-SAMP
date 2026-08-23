/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.android;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.android.AndroidAuthTokenProvider;
import com.google.firebase.database.core.TokenProvider;

public final class AndroidAuthTokenProvider$$ExternalSyntheticLambda1
implements OnSuccessListener {
    public final TokenProvider.GetTokenCompletionListener f$0;

    public /* synthetic */ AndroidAuthTokenProvider$$ExternalSyntheticLambda1(TokenProvider.GetTokenCompletionListener getTokenCompletionListener) {
        this.f$0 = getTokenCompletionListener;
    }

    public final void onSuccess(Object object) {
        AndroidAuthTokenProvider.lambda$getToken$1(this.f$0, (GetTokenResult)object);
    }
}

