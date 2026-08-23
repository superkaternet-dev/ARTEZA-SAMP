/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.view;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.core.EventRegistration;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.view.Event;

public class CancelEvent
implements Event {
    private final DatabaseError error;
    private final EventRegistration eventRegistration;
    private final Path path;

    public CancelEvent(EventRegistration eventRegistration, DatabaseError databaseError, Path path) {
        this.eventRegistration = eventRegistration;
        this.path = path;
        this.error = databaseError;
    }

    @Override
    public void fire() {
        this.eventRegistration.fireCancelEvent(this.error);
    }

    @Override
    public Path getPath() {
        return this.path;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getPath());
        stringBuilder.append(":CANCEL");
        return stringBuilder.toString();
    }
}

