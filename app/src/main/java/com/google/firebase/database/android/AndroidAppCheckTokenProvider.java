/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.android;

import com.google.firebase.appcheck.AppCheckTokenResult;
import com.google.firebase.appcheck.interop.InternalAppCheckTokenProvider;
import com.google.firebase.database.android.AndroidAppCheckTokenProvider$$ExternalSyntheticLambda0;
import com.google.firebase.database.android.AndroidAppCheckTokenProvider$$ExternalSyntheticLambda1;
import com.google.firebase.database.android.AndroidAppCheckTokenProvider$$ExternalSyntheticLambda2;
import com.google.firebase.database.android.AndroidAppCheckTokenProvider$$ExternalSyntheticLambda3;
import com.google.firebase.database.android.AndroidAppCheckTokenProvider$$ExternalSyntheticLambda4;
import com.google.firebase.database.android.AndroidAppCheckTokenProvider$$ExternalSyntheticLambda5;
import com.google.firebase.database.core.TokenProvider;
import com.google.firebase.inject.Deferred;
import com.google.firebase.inject.Provider;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class AndroidAppCheckTokenProvider
implements TokenProvider {
    private final Deferred<InternalAppCheckTokenProvider> deferredAppCheckProvider;
    private final AtomicReference<InternalAppCheckTokenProvider> internalAppCheck;

    public AndroidAppCheckTokenProvider(Deferred<InternalAppCheckTokenProvider> deferred) {
        this.deferredAppCheckProvider = deferred;
        this.internalAppCheck = new AtomicReference();
        deferred.whenAvailable(new AndroidAppCheckTokenProvider$$ExternalSyntheticLambda3(this));
    }

    static /* synthetic */ void lambda$addTokenChangeListener$3(TokenProvider.TokenChangeListener tokenChangeListener, AppCheckTokenResult appCheckTokenResult) {
        tokenChangeListener.onTokenChange(appCheckTokenResult.getToken());
    }

    static /* synthetic */ void lambda$addTokenChangeListener$4(ExecutorService executorService, TokenProvider.TokenChangeListener tokenChangeListener, AppCheckTokenResult appCheckTokenResult) {
        executorService.execute(new AndroidAppCheckTokenProvider$$ExternalSyntheticLambda5(tokenChangeListener, appCheckTokenResult));
    }

    static /* synthetic */ void lambda$addTokenChangeListener$5(ExecutorService executorService, TokenProvider.TokenChangeListener tokenChangeListener, Provider provider) {
        ((InternalAppCheckTokenProvider)provider.get()).addAppCheckTokenListener(new AndroidAppCheckTokenProvider$$ExternalSyntheticLambda2(executorService, tokenChangeListener));
    }

    static /* synthetic */ void lambda$getToken$1(TokenProvider.GetTokenCompletionListener getTokenCompletionListener, AppCheckTokenResult appCheckTokenResult) {
        getTokenCompletionListener.onSuccess(appCheckTokenResult.getToken());
    }

    static /* synthetic */ void lambda$getToken$2(TokenProvider.GetTokenCompletionListener getTokenCompletionListener, Exception exception) {
        getTokenCompletionListener.onError(exception.getMessage());
    }

    @Override
    public void addTokenChangeListener(ExecutorService executorService, TokenProvider.TokenChangeListener tokenChangeListener) {
        this.deferredAppCheckProvider.whenAvailable(new AndroidAppCheckTokenProvider$$ExternalSyntheticLambda4(executorService, tokenChangeListener));
    }

    @Override
    public void getToken(boolean bl, TokenProvider.GetTokenCompletionListener getTokenCompletionListener) {
        InternalAppCheckTokenProvider internalAppCheckTokenProvider = this.internalAppCheck.get();
        if (internalAppCheckTokenProvider != null) {
            internalAppCheckTokenProvider.getToken(bl).addOnSuccessListener(new AndroidAppCheckTokenProvider$$ExternalSyntheticLambda1(getTokenCompletionListener)).addOnFailureListener(new AndroidAppCheckTokenProvider$$ExternalSyntheticLambda0(getTokenCompletionListener));
        } else {
            getTokenCompletionListener.onSuccess(null);
        }
    }

    public /* synthetic */ void lambda$new$0$com-google-firebase-database-android-AndroidAppCheckTokenProvider(Provider provider) {
        this.internalAppCheck.set((InternalAppCheckTokenProvider)provider.get());
    }

    @Override
    public void removeTokenChangeListener(TokenProvider.TokenChangeListener tokenChangeListener) {
    }
}

