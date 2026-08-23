/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.InternalHelpers;
import com.google.firebase.database.core.EventRegistration;
import com.google.firebase.database.core.Repo;
import com.google.firebase.database.core.view.Change;
import com.google.firebase.database.core.view.DataEvent;
import com.google.firebase.database.core.view.Event;
import com.google.firebase.database.core.view.QuerySpec;

public class ChildEventRegistration
extends EventRegistration {
    private final ChildEventListener eventListener;
    private final Repo repo;
    private final QuerySpec spec;

    public ChildEventRegistration(Repo repo, ChildEventListener childEventListener, QuerySpec querySpec) {
        this.repo = repo;
        this.eventListener = childEventListener;
        this.spec = querySpec;
    }

    @Override
    public EventRegistration clone(QuerySpec querySpec) {
        return new ChildEventRegistration(this.repo, this.eventListener, querySpec);
    }

    @Override
    public DataEvent createEvent(Change change, QuerySpec object) {
        DataSnapshot dataSnapshot = InternalHelpers.createDataSnapshot(InternalHelpers.createReference(this.repo, ((QuerySpec)object).getPath().child(change.getChildKey())), change.getIndexedNode());
        object = change.getPrevName() != null ? change.getPrevName().asString() : null;
        return new DataEvent(change.getEventType(), this, dataSnapshot, (String)object);
    }

    public boolean equals(Object object) {
        boolean bl = object instanceof ChildEventRegistration && ((ChildEventRegistration)object).eventListener.equals(this.eventListener) && ((ChildEventRegistration)object).repo.equals(this.repo) && ((ChildEventRegistration)object).spec.equals(this.spec);
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
        switch (1.$SwitchMap$com$google$firebase$database$core$view$Event$EventType[dataEvent.getEventType().ordinal()]) {
            default: {
                break;
            }
            case 4: {
                this.eventListener.onChildRemoved(dataEvent.getSnapshot());
                break;
            }
            case 3: {
                this.eventListener.onChildMoved(dataEvent.getSnapshot(), dataEvent.getPreviousName());
                break;
            }
            case 2: {
                this.eventListener.onChildChanged(dataEvent.getSnapshot(), dataEvent.getPreviousName());
                break;
            }
            case 1: {
                this.eventListener.onChildAdded(dataEvent.getSnapshot(), dataEvent.getPreviousName());
            }
        }
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
        boolean bl = eventRegistration instanceof ChildEventRegistration && ((ChildEventRegistration)eventRegistration).eventListener.equals(this.eventListener);
        return bl;
    }

    @Override
    public boolean respondsTo(Event.EventType eventType) {
        boolean bl = eventType != Event.EventType.VALUE;
        return bl;
    }

    public String toString() {
        return "ChildEventRegistration";
    }
}

