/*
 * Decompiled with CFR 0.152.
 */
package kotlin;

import kotlin.LazyThreadSafetyMode;
import kotlin.Metadata;

@Metadata(bv={1, 0, 3}, k=3, mv={1, 4, 1})
public final class LazyKt$WhenMappings {
    public static final int[] $EnumSwitchMapping$0;

    static {
        int[] nArray = new int[LazyThreadSafetyMode.values().length];
        $EnumSwitchMapping$0 = nArray;
        nArray[LazyThreadSafetyMode.SYNCHRONIZED.ordinal()] = 1;
        nArray[LazyThreadSafetyMode.PUBLICATION.ordinal()] = 2;
        nArray[LazyThreadSafetyMode.NONE.ordinal()] = 3;
    }
}

