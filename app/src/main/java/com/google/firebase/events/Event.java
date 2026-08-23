/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.events;

import com.google.firebase.components.Preconditions;

public class Event<T> {
    private final T payload;
    private final Class<T> type;

    public Event(Class<T> clazz, T t) {
        this.type = Preconditions.checkNotNull(clazz);
        this.payload = Preconditions.checkNotNull(t);
    }

    public T getPayload() {
        return this.payload;
    }

    public Class<T> getType() {
        return this.type;
    }

    public String toString() {
        return String.format("Event{type: %s, payload: %s}", this.type, this.payload);
    }
}

