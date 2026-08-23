/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.components;

import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentContainer;
import com.google.firebase.components.ComponentFactory;

public final class Component$$ExternalSyntheticLambda2
implements ComponentFactory {
    public final Object f$0;

    public /* synthetic */ Component$$ExternalSyntheticLambda2(Object object) {
        this.f$0 = object;
    }

    public final Object create(ComponentContainer componentContainer) {
        return Component.lambda$of$1(this.f$0, componentContainer);
    }
}

