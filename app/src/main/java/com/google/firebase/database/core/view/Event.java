/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.view;

import com.google.firebase.database.core.Path;

public interface Event {
    public void fire();

    public Path getPath();

    public String toString();

    public static final class EventType
    extends Enum<EventType> {
        private static final EventType[] $VALUES;
        public static final /* enum */ EventType CHILD_ADDED;
        public static final /* enum */ EventType CHILD_CHANGED;
        public static final /* enum */ EventType CHILD_MOVED;
        public static final /* enum */ EventType CHILD_REMOVED;
        public static final /* enum */ EventType VALUE;

        static {
            EventType eventType;
            EventType eventType2;
            EventType eventType3;
            EventType eventType4;
            EventType eventType5;
            CHILD_REMOVED = eventType5 = new EventType();
            CHILD_ADDED = eventType4 = new EventType();
            CHILD_MOVED = eventType3 = new EventType();
            CHILD_CHANGED = eventType2 = new EventType();
            VALUE = eventType = new EventType();
            $VALUES = new EventType[]{eventType5, eventType4, eventType3, eventType2, eventType};
        }

        public static EventType valueOf(String string2) {
            return Enum.valueOf(EventType.class, string2);
        }

        public static EventType[] values() {
            return (EventType[])$VALUES.clone();
        }
    }
}

