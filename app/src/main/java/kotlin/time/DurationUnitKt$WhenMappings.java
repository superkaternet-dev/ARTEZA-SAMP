/*
 * Decompiled with CFR 0.152.
 */
package kotlin.time;

import java.util.concurrent.TimeUnit;
import kotlin.Metadata;

@Metadata(bv={1, 0, 3}, k=3, mv={1, 4, 1})
public final class DurationUnitKt$WhenMappings {
    public static final int[] $EnumSwitchMapping$0;

    static {
        int[] nArray = new int[TimeUnit.values().length];
        $EnumSwitchMapping$0 = nArray;
        nArray[TimeUnit.NANOSECONDS.ordinal()] = 1;
        nArray[TimeUnit.MICROSECONDS.ordinal()] = 2;
        nArray[TimeUnit.MILLISECONDS.ordinal()] = 3;
        nArray[TimeUnit.SECONDS.ordinal()] = 4;
        nArray[TimeUnit.MINUTES.ordinal()] = 5;
        nArray[TimeUnit.HOURS.ordinal()] = 6;
        nArray[TimeUnit.DAYS.ordinal()] = 7;
    }
}

