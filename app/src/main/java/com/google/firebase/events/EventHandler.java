/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.events;

import com.google.firebase.events.Event;

public interface EventHandler<T> {
    public void handle(Event<T> var1);
}

