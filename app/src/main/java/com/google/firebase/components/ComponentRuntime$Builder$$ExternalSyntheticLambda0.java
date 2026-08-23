/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.components;

import com.google.firebase.components.ComponentRegistrar;
import com.google.firebase.components.ComponentRuntime;
import com.google.firebase.inject.Provider;

public final class ComponentRuntime$Builder$$ExternalSyntheticLambda0
implements Provider {
    public final ComponentRegistrar f$0;

    public /* synthetic */ ComponentRuntime$Builder$$ExternalSyntheticLambda0(ComponentRegistrar componentRegistrar) {
        this.f$0 = componentRegistrar;
    }

    public final Object get() {
        return ComponentRuntime.Builder.lambda$addComponentRegistrar$0(this.f$0);
    }
}

