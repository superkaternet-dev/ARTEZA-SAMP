/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.components;

import com.google.firebase.components.EventBus;
import com.google.firebase.events.Event;
import java.util.Map;

public final class EventBus$$ExternalSyntheticLambda0
implements Runnable {
    public final Map.Entry f$0;
    public final Event f$1;

    public /* synthetic */ EventBus$$ExternalSyntheticLambda0(Map.Entry entry, Event event) {
        this.f$0 = entry;
        this.f$1 = event;
    }

    @Override
    public final void run() {
        EventBus.lambda$publish$0(this.f$0, this.f$1);
    }
}

