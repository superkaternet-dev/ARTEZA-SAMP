/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.inject;

import com.google.firebase.inject.Provider;

public interface Deferred<T> {
    public void whenAvailable(DeferredHandler<T> var1);

    public static interface DeferredHandler<T> {
        public void handle(Provider<T> var1);
    }
}

