/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.components;

import com.google.firebase.components.ComponentContainer;
import java.util.Set;

abstract class AbstractComponentContainer
implements ComponentContainer {
    AbstractComponentContainer() {
    }

    @Override
    public <T> T get(Class<T> object) {
        if ((object = this.getProvider(object)) == null) {
            return null;
        }
        return object.get();
    }

    @Override
    public <T> Set<T> setOf(Class<T> clazz) {
        return this.setOfProvider(clazz).get();
    }
}

