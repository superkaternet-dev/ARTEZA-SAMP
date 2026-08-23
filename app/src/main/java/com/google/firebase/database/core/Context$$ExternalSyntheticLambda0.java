/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import com.google.firebase.database.connection.ConnectionTokenProvider;
import com.google.firebase.database.core.Context;
import com.google.firebase.database.core.TokenProvider;
import java.util.concurrent.ScheduledExecutorService;

public final class Context$$ExternalSyntheticLambda0
implements ConnectionTokenProvider {
    public final TokenProvider f$0;
    public final ScheduledExecutorService f$1;

    public /* synthetic */ Context$$ExternalSyntheticLambda0(TokenProvider tokenProvider, ScheduledExecutorService scheduledExecutorService) {
        this.f$0 = tokenProvider;
        this.f$1 = scheduledExecutorService;
    }

    @Override
    public final void getToken(boolean bl, ConnectionTokenProvider.GetTokenCallback getTokenCallback) {
        Context.lambda$wrapTokenProvider$0(this.f$0, this.f$1, bl, getTokenCallback);
    }
}

