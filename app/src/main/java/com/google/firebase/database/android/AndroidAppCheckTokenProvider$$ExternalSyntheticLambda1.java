/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.android;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.appcheck.AppCheckTokenResult;
import com.google.firebase.database.android.AndroidAppCheckTokenProvider;
import com.google.firebase.database.core.TokenProvider;

public final class AndroidAppCheckTokenProvider$$ExternalSyntheticLambda1
implements OnSuccessListener {
    public final TokenProvider.GetTokenCompletionListener f$0;

    public /* synthetic */ AndroidAppCheckTokenProvider$$ExternalSyntheticLambda1(TokenProvider.GetTokenCompletionListener getTokenCompletionListener) {
        this.f$0 = getTokenCompletionListener;
    }

    public final void onSuccess(Object object) {
        AndroidAppCheckTokenProvider.lambda$getToken$1(this.f$0, (AppCheckTokenResult)object);
    }
}

