/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.components;

import com.google.firebase.components.OptionalProvider$$ExternalSyntheticLambda0;
import com.google.firebase.components.OptionalProvider$$ExternalSyntheticLambda1;
import com.google.firebase.components.OptionalProvider$$ExternalSyntheticLambda2;
import com.google.firebase.inject.Deferred;
import com.google.firebase.inject.Provider;

class OptionalProvider<T>
implements Provider<T>,
Deferred<T> {
    private static final Provider<Object> EMPTY_PROVIDER;
    private static final Deferred.DeferredHandler<Object> NOOP_HANDLER;
    private volatile Provider<T> delegate;
    private Deferred.DeferredHandler<T> handler;

    static {
        NOOP_HANDLER = OptionalProvider$$ExternalSyntheticLambda1.INSTANCE;
        EMPTY_PROVIDER = OptionalProvider$$ExternalSyntheticLambda2.INSTANCE;
    }

    private OptionalProvider(Deferred.DeferredHandler<T> deferredHandler, Provider<T> provider) {
        this.handler = deferredHandler;
        this.delegate = provider;
    }

    static <T> OptionalProvider<T> empty() {
        return new OptionalProvider<Object>(NOOP_HANDLER, EMPTY_PROVIDER);
    }

    static /* synthetic */ void lambda$static$0(Provider provider) {
    }

    static /* synthetic */ Object lambda$static$1() {
        return null;
    }

    static /* synthetic */ void lambda$whenAvailable$2(Deferred.DeferredHandler deferredHandler, Deferred.DeferredHandler deferredHandler2, Provider provider) {
        deferredHandler.handle(provider);
        deferredHandler2.handle(provider);
    }

    static <T> OptionalProvider<T> of(Provider<T> provider) {
        return new OptionalProvider<T>(null, provider);
    }

    @Override
    public T get() {
        return this.delegate.get();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void set(Provider<T> provider) {
        if (this.delegate == EMPTY_PROVIDER) {
            Deferred.DeferredHandler<T> deferredHandler;
            synchronized (this) {
                deferredHandler = this.handler;
                this.handler = null;
                this.delegate = provider;
            }
            deferredHandler.handle(provider);
            return;
        }
        throw new IllegalStateException("provide() can be called only once.");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public void whenAvailable(Deferred.DeferredHandler<T> deferredHandler) {
        Provider<T> provider = this.delegate;
        Provider<Object> provider2 = EMPTY_PROVIDER;
        if (provider != provider2) {
            deferredHandler.handle(provider);
            return;
        }
        provider = null;
        // MONITORENTER : this
        Provider<T> provider3 = this.delegate;
        if (provider3 != provider2) {
            provider = provider3;
        } else {
            OptionalProvider$$ExternalSyntheticLambda0 optionalProvider$$ExternalSyntheticLambda0;
            provider2 = this.handler;
            this.handler = optionalProvider$$ExternalSyntheticLambda0 = new OptionalProvider$$ExternalSyntheticLambda0((Deferred.DeferredHandler)((Object)provider2), deferredHandler);
        }
        // MONITOREXIT : this
        if (provider == null) return;
        deferredHandler.handle(provider3);
    }
}

