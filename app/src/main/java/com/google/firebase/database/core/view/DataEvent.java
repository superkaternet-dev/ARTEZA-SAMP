/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.view;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.core.EventRegistration;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.view.Event;

public class DataEvent
implements Event {
    private final EventRegistration eventRegistration;
    private final Event.EventType eventType;
    private final String prevName;
    private final DataSnapshot snapshot;

    public DataEvent(Event.EventType eventType, EventRegistration eventRegistration, DataSnapshot dataSnapshot, String string2) {
        this.eventType = eventType;
        this.eventRegistration = eventRegistration;
        this.snapshot = dataSnapshot;
        this.prevName = string2;
    }

    @Override
    public void fire() {
        this.eventRegistration.fireEvent(this);
    }

    public Event.EventType getEventType() {
        return this.eventType;
    }

    @Override
    public Path getPath() {
        Path path = this.snapshot.getRef().getPath();
        if (this.eventType == Event.EventType.VALUE) {
            return path;
        }
        return path.getParent();
    }

    public String getPreviousName() {
        return this.prevName;
    }

    public DataSnapshot getSnapshot() {
        return this.snapshot;
    }

    @Override
    public String toString() {
        if (this.eventType == Event.EventType.VALUE) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.getPath());
            stringBuilder.append(": ");
            stringBuilder.append((Object)this.eventType);
            stringBuilder.append(": ");
            stringBuilder.append(this.snapshot.getValue(true));
            return stringBuilder.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getPath());
        stringBuilder.append(": ");
        stringBuilder.append((Object)this.eventType);
        stringBuilder.append(": { ");
        stringBuilder.append(this.snapshot.getKey());
        stringBuilder.append(": ");
        stringBuilder.append(this.snapshot.getValue(true));
        stringBuilder.append(" }");
        return stringBuilder.toString();
    }
}

