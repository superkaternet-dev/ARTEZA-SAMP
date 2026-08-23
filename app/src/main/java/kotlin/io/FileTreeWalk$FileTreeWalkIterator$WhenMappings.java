/*
 * Decompiled with CFR 0.152.
 */
package kotlin.io;

import kotlin.Metadata;
import kotlin.io.FileWalkDirection;

@Metadata(bv={1, 0, 3}, k=3, mv={1, 4, 1})
public final class FileTreeWalk$FileTreeWalkIterator$WhenMappings {
    public static final int[] $EnumSwitchMapping$0;

    static {
        int[] nArray = new int[FileWalkDirection.values().length];
        $EnumSwitchMapping$0 = nArray;
        nArray[FileWalkDirection.TOP_DOWN.ordinal()] = 1;
        nArray[FileWalkDirection.BOTTOM_UP.ordinal()] = 2;
    }
}

