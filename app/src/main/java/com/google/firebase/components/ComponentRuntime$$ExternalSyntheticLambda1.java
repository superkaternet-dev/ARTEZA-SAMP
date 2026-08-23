/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.components;

import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentRuntime;
import com.google.firebase.inject.Provider;

public final class ComponentRuntime$$ExternalSyntheticLambda1
implements Provider {
    public final ComponentRuntime f$0;
    public final Component f$1;

    public /* synthetic */ ComponentRuntime$$ExternalSyntheticLambda1(ComponentRuntime componentRuntime, Component component) {
        this.f$0 = componentRuntime;
        this.f$1 = component;
    }

    public final Object get() {
        return this.f$0.lambda$discoverComponents$0$com-google-firebase-components-ComponentRuntime(this.f$1);
    }
}

