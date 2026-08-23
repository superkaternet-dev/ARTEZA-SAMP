/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import kotlin.Metadata;

@Metadata(bv={1, 0, 3}, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0082\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006\u00a8\u0006\u0007"}, d2={"Lkotlin/collections/State;", "", "(Ljava/lang/String;I)V", "Ready", "NotReady", "Done", "Failed", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
final class State
extends Enum<State> {
    private static final State[] $VALUES;
    public static final /* enum */ State Done;
    public static final /* enum */ State Failed;
    public static final /* enum */ State NotReady;
    public static final /* enum */ State Ready;

    static {
        State state;
        State state2;
        State state3;
        State state4;
        Ready = state4 = new State();
        NotReady = state3 = new State();
        Done = state2 = new State();
        Failed = state = new State();
        $VALUES = new State[]{state4, state3, state2, state};
    }

    public static State valueOf(String string2) {
        return Enum.valueOf(State.class, string2);
    }

    public static State[] values() {
        return (State[])$VALUES.clone();
    }
}

