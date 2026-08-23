/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.components;

import com.google.firebase.components.ComponentRuntime;
import com.google.firebase.components.LazySet;
import com.google.firebase.inject.Provider;

public final class ComponentRuntime$$ExternalSyntheticLambda3
implements Runnable {
    public final LazySet f$0;
    public final Provider f$1;

    public /* synthetic */ ComponentRuntime$$ExternalSyntheticLambda3(LazySet lazySet, Provider provider) {
        this.f$0 = lazySet;
        this.f$1 = provider;
    }

    @Override
    public final void run() {
        ComponentRuntime.lambda$processSetComponents$3(this.f$0, this.f$1);
    }
}

