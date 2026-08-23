/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.components;

import com.google.firebase.inject.Deferred;
import com.google.firebase.inject.Provider;
import java.util.Set;

public interface ComponentContainer {
    public <T> T get(Class<T> var1);

    public <T> Deferred<T> getDeferred(Class<T> var1);

    public <T> Provider<T> getProvider(Class<T> var1);

    public <T> Set<T> setOf(Class<T> var1);

    public <T> Provider<Set<T>> setOfProvider(Class<T> var1);
}

