/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.android;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.internal.InternalAuthProvider;
import com.google.firebase.database.android.AndroidAuthTokenProvider$$ExternalSyntheticLambda0;
import com.google.firebase.database.android.AndroidAuthTokenProvider$$ExternalSyntheticLambda1;
import com.google.firebase.database.android.AndroidAuthTokenProvider$$ExternalSyntheticLambda2;
import com.google.firebase.database.android.AndroidAuthTokenProvider$$ExternalSyntheticLambda3;
import com.google.firebase.database.android.AndroidAuthTokenProvider$$ExternalSyntheticLambda4;
import com.google.firebase.database.android.AndroidAuthTokenProvider$$ExternalSyntheticLambda5;
import com.google.firebase.database.core.TokenProvider;
import com.google.firebase.inject.Deferred;
import com.google.firebase.inject.Provider;
import com.google.firebase.internal.InternalTokenResult;
import com.google.firebase.internal.api.FirebaseNoSignedInUserException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class AndroidAuthTokenProvider
implements TokenProvider {
    private final Deferred<InternalAuthProvider> deferredAuthProvider;
    private final AtomicReference<InternalAuthProvider> internalAuth;

    public AndroidAuthTokenProvider(Deferred<InternalAuthProvider> deferred) {
        this.deferredAuthProvider = deferred;
        this.internalAuth = new AtomicReference();
        deferred.whenAvailable(new AndroidAuthTokenProvider$$ExternalSyntheticLambda3(this));
    }

    private static boolean isUnauthenticatedUsage(Exception exception) {
        boolean bl = exception instanceof FirebaseApiNotAvailableException || exception instanceof FirebaseNoSignedInUserException;
        return bl;
    }

    static /* synthetic */ void lambda$addTokenChangeListener$3(TokenProvider.TokenChangeListener tokenChangeListener, InternalTokenResult internalTokenResult) {
        tokenChangeListener.onTokenChange(internalTokenResult.getToken());
    }

    static /* synthetic */ void lambda$addTokenChangeListener$4(ExecutorService executorService, TokenProvider.TokenChangeListener tokenChangeListener, InternalTokenResult internalTokenResult) {
        executorService.execute(new AndroidAuthTokenProvider$$ExternalSyntheticLambda5(tokenChangeListener, internalTokenResult));
    }

    static /* synthetic */ void lambda$addTokenChangeListener$5(ExecutorService executorService, TokenProvider.TokenChangeListener tokenChangeListener, Provider provider) {
        ((InternalAuthProvider)provider.get()).addIdTokenListener(new AndroidAuthTokenProvider$$ExternalSyntheticLambda2(executorService, tokenChangeListener));
    }

    static /* synthetic */ void lambda$getToken$1(TokenProvider.GetTokenCompletionListener getTokenCompletionListener, GetTokenResult getTokenResult) {
        getTokenCompletionListener.onSuccess(getTokenResult.getToken());
    }

    static /* synthetic */ void lambda$getToken$2(TokenProvider.GetTokenCompletionListener getTokenCompletionListener, Exception exception) {
        if (AndroidAuthTokenProvider.isUnauthenticatedUsage(exception)) {
            getTokenCompletionListener.onSuccess(null);
        } else {
            getTokenCompletionListener.onError(exception.getMessage());
        }
    }

    @Override
    public void addTokenChangeListener(ExecutorService executorService, TokenProvider.TokenChangeListener tokenChangeListener) {
        this.deferredAuthProvider.whenAvailable(new AndroidAuthTokenProvider$$ExternalSyntheticLambda4(executorService, tokenChangeListener));
    }

    @Override
    public void getToken(boolean bl, TokenProvider.GetTokenCompletionListener getTokenCompletionListener) {
        InternalAuthProvider internalAuthProvider = this.internalAuth.get();
        if (internalAuthProvider != null) {
            internalAuthProvider.getAccessToken(bl).addOnSuccessListener(new AndroidAuthTokenProvider$$ExternalSyntheticLambda1(getTokenCompletionListener)).addOnFailureListener(new AndroidAuthTokenProvider$$ExternalSyntheticLambda0(getTokenCompletionListener));
        } else {
            getTokenCompletionListener.onSuccess(null);
        }
    }

    public /* synthetic */ void lambda$new$0$com-google-firebase-database-android-AndroidAuthTokenProvider(Provider provider) {
        this.internalAuth.set((InternalAuthProvider)provider.get());
    }

    @Override
    public void removeTokenChangeListener(TokenProvider.TokenChangeListener tokenChangeListener) {
    }
}

