/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.components;

import com.google.firebase.components.ComponentRuntime;
import com.google.firebase.components.OptionalProvider;
import com.google.firebase.inject.Provider;

public final class ComponentRuntime$$ExternalSyntheticLambda4
implements Runnable {
    public final OptionalProvider f$0;
    public final Provider f$1;

    public /* synthetic */ ComponentRuntime$$ExternalSyntheticLambda4(OptionalProvider optionalProvider, Provider provider) {
        this.f$0 = optionalProvider;
        this.f$1 = provider;
    }

    @Override
    public final void run() {
        ComponentRuntime.lambda$processInstanceComponents$2(this.f$0, this.f$1);
    }
}

