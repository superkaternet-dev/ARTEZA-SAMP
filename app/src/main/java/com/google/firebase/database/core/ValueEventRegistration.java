/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.InternalHelpers;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.EventRegistration;
import com.google.firebase.database.core.Repo;
import com.google.firebase.database.core.view.Change;
import com.google.firebase.database.core.view.DataEvent;
import com.google.firebase.database.core.view.Event;
import com.google.firebase.database.core.view.QuerySpec;

public class ValueEventRegistration
extends EventRegistration {
    private final ValueEventListener eventListener;
    private final Repo repo;
    private final QuerySpec spec;

    public ValueEventRegistration(Repo repo, ValueEventListener valueEventListener, QuerySpec querySpec) {
        this.repo = repo;
        this.eventListener = valueEventListener;
        this.spec = querySpec;
    }

    @Override
    public EventRegistration clone(QuerySpec querySpec) {
        return new ValueEventRegistration(this.repo, this.eventListener, querySpec);
    }

    @Override
    public DataEvent createEvent(Change object, QuerySpec querySpec) {
        object = InternalHelpers.createDataSnapshot(InternalHelpers.createReference(this.repo, querySpec.getPath()), ((Change)object).getIndexedNode());
        return new DataEvent(Event.EventType.VALUE, this, (DataSnapshot)object, null);
    }

    public boolean equals(Object object) {
        boolean bl = object instanceof ValueEventRegistration && ((ValueEventRegistration)object).eventListener.equals(this.eventListener) && ((ValueEventRegistration)object).repo.equals(this.repo) && ((ValueEventRegistration)object).spec.equals(this.spec);
        return bl;
    }

    @Override
    public void fireCancelEvent(DatabaseError databaseError) {
        this.eventListener.onCancelled(databaseError);
    }

    @Override
    public void fireEvent(DataEvent dataEvent) {
        if (this.isZombied()) {
            return;
        }
        this.eventListener.onDataChange(dataEvent.getSnapshot());
    }

    @Override
    public QuerySpec getQuerySpec() {
        return this.spec;
    }

    @Override
    Repo getRepo() {
        return this.repo;
    }

    public int hashCode() {
        return (this.eventListener.hashCode() * 31 + this.repo.hashCode()) * 31 + this.spec.hashCode();
    }

    @Override
    public boolean isSameListener(EventRegistration eventRegistration) {
        boolean bl = eventRegistration instanceof ValueEventRegistration && ((ValueEventRegistration)eventRegistration).eventListener.equals(this.eventListener);
        return bl;
    }

    @Override
    public boolean respondsTo(Event.EventType eventType) {
        boolean bl = eventType == Event.EventType.VALUE;
        return bl;
    }

    public String toString() {
        return "ValueEventRegistration";
    }
}

