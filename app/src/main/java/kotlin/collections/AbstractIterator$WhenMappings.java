/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import kotlin.Metadata;
import kotlin.collections.State;

@Metadata(bv={1, 0, 3}, k=3, mv={1, 4, 1})
public final class AbstractIterator$WhenMappings {
    public static final int[] $EnumSwitchMapping$0;

    static {
        int[] nArray = new int[State.values().length];
        $EnumSwitchMapping$0 = nArray;
        nArray[State.Done.ordinal()] = 1;
        nArray[State.Ready.ordinal()] = 2;
    }
}

