/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.components;

import com.google.firebase.inject.Provider;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class LazySet<T>
implements Provider<Set<T>> {
    private volatile Set<T> actualSet = null;
    private volatile Set<Provider<T>> providers = Collections.newSetFromMap(new ConcurrentHashMap());

    LazySet(Collection<Provider<T>> collection) {
        this.providers.addAll(collection);
    }

    static LazySet<?> fromCollection(Collection<Provider<?>> collection) {
        return new LazySet((Set)collection);
    }

    private void updateSet() {
        synchronized (this) {
            try {
                for (Provider<T> provider : this.providers) {
                    this.actualSet.add(provider.get());
                }
                this.providers = null;
                return;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
        }
    }

    void add(Provider<T> provider) {
        synchronized (this) {
            if (this.actualSet == null) {
                this.providers.add(provider);
            } else {
                this.actualSet.add(provider.get());
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public Set<T> get() {
        if (this.actualSet != null) return Collections.unmodifiableSet(this.actualSet);
        synchronized (this) {
            if (this.actualSet != null) return Collections.unmodifiableSet(this.actualSet);
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            this.actualSet = Collections.newSetFromMap(concurrentHashMap);
            this.updateSet();
            return Collections.unmodifiableSet(this.actualSet);
        }
    }
}

