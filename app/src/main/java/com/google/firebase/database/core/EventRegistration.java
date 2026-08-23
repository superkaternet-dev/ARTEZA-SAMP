/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.core.EventRegistrationZombieListener;
import com.google.firebase.database.core.Repo;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.core.view.Change;
import com.google.firebase.database.core.view.DataEvent;
import com.google.firebase.database.core.view.Event;
import com.google.firebase.database.core.view.QuerySpec;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class EventRegistration {
    private boolean isUserInitiated = false;
    private EventRegistrationZombieListener listener;
    private AtomicBoolean zombied = new AtomicBoolean(false);

    public abstract EventRegistration clone(QuerySpec var1);

    public abstract DataEvent createEvent(Change var1, QuerySpec var2);

    public abstract void fireCancelEvent(DatabaseError var1);

    public abstract void fireEvent(DataEvent var1);

    public abstract QuerySpec getQuerySpec();

    Repo getRepo() {
        return null;
    }

    public abstract boolean isSameListener(EventRegistration var1);

    public boolean isUserInitiated() {
        return this.isUserInitiated;
    }

    public boolean isZombied() {
        return this.zombied.get();
    }

    public abstract boolean respondsTo(Event.EventType var1);

    public void setIsUserInitiated(boolean bl) {
        this.isUserInitiated = bl;
    }

    public void setOnZombied(EventRegistrationZombieListener eventRegistrationZombieListener) {
        boolean bl = this.isZombied();
        boolean bl2 = true;
        Utilities.hardAssert(bl ^ true);
        if (this.listener != null) {
            bl2 = false;
        }
        Utilities.hardAssert(bl2);
        this.listener = eventRegistrationZombieListener;
    }

    public void zombify() {
        EventRegistrationZombieListener eventRegistrationZombieListener;
        if (this.zombied.compareAndSet(false, true) && (eventRegistrationZombieListener = this.listener) != null) {
            eventRegistrationZombieListener.onZombied(this);
            this.listener = null;
        }
    }
}

