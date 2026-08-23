/*
 * Decompiled with CFR 0.152.
 */
package kotlin.reflect;

import kotlin.Metadata;
import kotlin.reflect.KVariance;

@Metadata(bv={1, 0, 3}, k=3, mv={1, 4, 1})
public final class TypesJVMKt$WhenMappings {
    public static final int[] $EnumSwitchMapping$0;
    public static final int[] $EnumSwitchMapping$1;

    static {
        int[] nArray = new int[KVariance.values().length];
        $EnumSwitchMapping$0 = nArray;
        nArray[KVariance.IN.ordinal()] = 1;
        nArray[KVariance.INVARIANT.ordinal()] = 2;
        nArray[KVariance.OUT.ordinal()] = 3;
        nArray = new int[KVariance.values().length];
        $EnumSwitchMapping$1 = nArray;
        nArray[KVariance.INVARIANT.ordinal()] = 1;
        nArray[KVariance.IN.ordinal()] = 2;
        nArray[KVariance.OUT.ordinal()] = 3;
    }
}

