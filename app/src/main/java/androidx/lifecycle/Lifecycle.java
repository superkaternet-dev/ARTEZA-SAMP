/*
 * Decompiled with CFR 0.152.
 */
package androidx.lifecycle;

import androidx.lifecycle.LifecycleObserver;
import java.util.concurrent.atomic.AtomicReference;

public abstract class Lifecycle {
    AtomicReference<Object> mInternalScopeRef = new AtomicReference();

    public abstract void addObserver(LifecycleObserver var1);

    public abstract State getCurrentState();

    public abstract void removeObserver(LifecycleObserver var1);

    public static final class Event
    extends Enum<Event> {
        private static final Event[] $VALUES;
        public static final /* enum */ Event ON_ANY;
        public static final /* enum */ Event ON_CREATE;
        public static final /* enum */ Event ON_DESTROY;
        public static final /* enum */ Event ON_PAUSE;
        public static final /* enum */ Event ON_RESUME;
        public static final /* enum */ Event ON_START;
        public static final /* enum */ Event ON_STOP;

        static {
            Event event;
            Event event2;
            Event event3;
            Event event4;
            Event event5;
            Event event6;
            Event event7;
            ON_CREATE = event7 = new Event();
            ON_START = event6 = new Event();
            ON_RESUME = event5 = new Event();
            ON_PAUSE = event4 = new Event();
            ON_STOP = event3 = new Event();
            ON_DESTROY = event2 = new Event();
            ON_ANY = event = new Event();
            $VALUES = new Event[]{event7, event6, event5, event4, event3, event2, event};
        }

        public static Event downFrom(State state) {
            switch (1.$SwitchMap$androidx$lifecycle$Lifecycle$State[state.ordinal()]) {
                default: {
                    return null;
                }
                case 3: {
                    return ON_PAUSE;
                }
                case 2: {
                    return ON_STOP;
                }
                case 1: 
            }
            return ON_DESTROY;
        }

        public static Event downTo(State state) {
            switch (1.$SwitchMap$androidx$lifecycle$Lifecycle$State[state.ordinal()]) {
                default: {
                    return null;
                }
                case 4: {
                    return ON_DESTROY;
                }
                case 2: {
                    return ON_PAUSE;
                }
                case 1: 
            }
            return ON_STOP;
        }

        public static Event upFrom(State state) {
            switch (1.$SwitchMap$androidx$lifecycle$Lifecycle$State[state.ordinal()]) {
                default: {
                    return null;
                }
                case 5: {
                    return ON_CREATE;
                }
                case 2: {
                    return ON_RESUME;
                }
                case 1: 
            }
            return ON_START;
        }

        public static Event upTo(State state) {
            switch (1.$SwitchMap$androidx$lifecycle$Lifecycle$State[state.ordinal()]) {
                default: {
                    return null;
                }
                case 3: {
                    return ON_RESUME;
                }
                case 2: {
                    return ON_START;
                }
                case 1: 
            }
            return ON_CREATE;
        }

        public static Event valueOf(String string2) {
            return Enum.valueOf(Event.class, string2);
        }

        public static Event[] values() {
            return (Event[])$VALUES.clone();
        }

        public State getTargetState() {
            switch (1.$SwitchMap$androidx$lifecycle$Lifecycle$Event[this.ordinal()]) {
                default: {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append((Object)this);
                    stringBuilder.append(" has no target state");
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
                case 6: {
                    return State.DESTROYED;
                }
                case 5: {
                    return State.RESUMED;
                }
                case 3: 
                case 4: {
                    return State.STARTED;
                }
                case 1: 
                case 2: 
            }
            return State.CREATED;
        }
    }

    public static final class State
    extends Enum<State> {
        private static final State[] $VALUES;
        public static final /* enum */ State CREATED;
        public static final /* enum */ State DESTROYED;
        public static final /* enum */ State INITIALIZED;
        public static final /* enum */ State RESUMED;
        public static final /* enum */ State STARTED;

        static {
            State state;
            State state2;
            State state3;
            State state4;
            State state5;
            DESTROYED = state5 = new State();
            INITIALIZED = state4 = new State();
            CREATED = state3 = new State();
            STARTED = state2 = new State();
            RESUMED = state = new State();
            $VALUES = new State[]{state5, state4, state3, state2, state};
        }

        public static State valueOf(String string2) {
            return Enum.valueOf(State.class, string2);
        }

        public static State[] values() {
            return (State[])$VALUES.clone();
        }

        public boolean isAtLeast(State state) {
            boolean bl = this.compareTo(state) >= 0;
            return bl;
        }
    }
}

