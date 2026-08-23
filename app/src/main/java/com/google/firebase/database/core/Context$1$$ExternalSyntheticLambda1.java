/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import com.google.firebase.database.connection.ConnectionTokenProvider;
import com.google.firebase.database.core.Context;

public final class Context$1$$ExternalSyntheticLambda1
implements Runnable {
    public final ConnectionTokenProvider.GetTokenCallback f$0;
    public final String f$1;

    public /* synthetic */ Context$1$$ExternalSyntheticLambda1(ConnectionTokenProvider.GetTokenCallback getTokenCallback, String string2) {
        this.f$0 = getTokenCallback;
        this.f$1 = string2;
    }

    @Override
    public final void run() {
        Context.1.lambda$onSuccess$0(this.f$0, this.f$1);
    }
}

