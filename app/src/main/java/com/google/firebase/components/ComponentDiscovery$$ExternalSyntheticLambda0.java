/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.components;

import com.google.firebase.components.ComponentDiscovery;
import com.google.firebase.inject.Provider;

public final class ComponentDiscovery$$ExternalSyntheticLambda0
implements Provider {
    public final String f$0;

    public /* synthetic */ ComponentDiscovery$$ExternalSyntheticLambda0(String string2) {
        this.f$0 = string2;
    }

    public final Object get() {
        return ComponentDiscovery.lambda$discoverLazy$0(this.f$0);
    }
}

