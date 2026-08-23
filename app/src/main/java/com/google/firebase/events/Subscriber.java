/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.events;

import com.google.firebase.events.EventHandler;
import java.util.concurrent.Executor;

public interface Subscriber {
    public <T> void subscribe(Class<T> var1, EventHandler<? super T> var2);

    public <T> void subscribe(Class<T> var1, Executor var2, EventHandler<? super T> var3);

    public <T> void unsubscribe(Class<T> var1, EventHandler<? super T> var2);
}

