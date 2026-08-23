/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.view;

import com.google.firebase.database.core.Context;
import com.google.firebase.database.core.EventTarget;
import com.google.firebase.database.core.view.Event;
import com.google.firebase.database.logging.LogWrapper;
import java.util.ArrayList;
import java.util.List;

public class EventRaiser {
    private final EventTarget eventTarget;
    private final LogWrapper logger;

    public EventRaiser(Context context) {
        this.eventTarget = context.getEventTarget();
        this.logger = context.getLogger("EventRaiser");
    }

    public void raiseEvents(List<? extends Event> list) {
        if (this.logger.logsDebug()) {
            LogWrapper logWrapper = this.logger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Raising ");
            stringBuilder.append(list.size());
            stringBuilder.append(" event(s)");
            logWrapper.debug(stringBuilder.toString(), new Object[0]);
        }
        list = new ArrayList<Event>(list);
        this.eventTarget.postEvent(new Runnable(this, (ArrayList)list){
            final EventRaiser this$0;
            final ArrayList val$eventsClone;
            {
                this.this$0 = eventRaiser;
                this.val$eventsClone = arrayList;
            }

            @Override
            public void run() {
                for (Event event : this.val$eventsClone) {
                    if (this.this$0.logger.logsDebug()) {
                        LogWrapper logWrapper = this.this$0.logger;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Raising ");
                        stringBuilder.append(event.toString());
                        logWrapper.debug(stringBuilder.toString(), new Object[0]);
                    }
                    event.fire();
                }
            }
        });
    }
}

