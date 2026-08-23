/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.heartbeatinfo;

import com.google.firebase.heartbeatinfo.DefaultHeartBeatController;
import java.util.concurrent.ThreadFactory;

public final class DefaultHeartBeatController$$ExternalSyntheticLambda4
implements ThreadFactory {
    public static final DefaultHeartBeatController$$ExternalSyntheticLambda4 INSTANCE = new DefaultHeartBeatController$$ExternalSyntheticLambda4();

    private /* synthetic */ DefaultHeartBeatController$$ExternalSyntheticLambda4() {
    }

    @Override
    public final Thread newThread(Runnable runnable) {
        return DefaultHeartBeatController.lambda$static$0(runnable);
    }
}

