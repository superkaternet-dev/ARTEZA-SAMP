/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.components;

import com.google.firebase.inject.Provider;

public class Lazy<T>
implements Provider<T> {
    private static final Object UNINITIALIZED = new Object();
    private volatile Object instance = UNINITIALIZED;
    private volatile Provider<T> provider;

    public Lazy(Provider<T> provider) {
        this.provider = provider;
    }

    Lazy(T t) {
        this.instance = t;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public T get() {
        Object object = this.instance;
        Object object2 = UNINITIALIZED;
        Object object3 = object;
        if (object != object2) return (T)object3;
        synchronized (this) {
            object3 = object = this.instance;
            if (object != object2) return (T)object3;
            this.instance = object3 = this.provider.get();
            this.provider = null;
            return (T)object3;
        }
    }

    boolean isInitialized() {
        boolean bl = this.instance != UNINITIALIZED;
        return bl;
    }
}

