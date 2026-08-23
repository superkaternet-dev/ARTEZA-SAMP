/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.google.firebase.heartbeatinfo;

import android.content.Context;
import com.google.firebase.heartbeatinfo.DefaultHeartBeatController;
import com.google.firebase.inject.Provider;

public final class DefaultHeartBeatController$$ExternalSyntheticLambda1
implements Provider {
    public final Context f$0;
    public final String f$1;

    public /* synthetic */ DefaultHeartBeatController$$ExternalSyntheticLambda1(Context context, String string2) {
        this.f$0 = context;
        this.f$1 = string2;
    }

    public final Object get() {
        return DefaultHeartBeatController.lambda$new$3(this.f$0, this.f$1);
    }
}

