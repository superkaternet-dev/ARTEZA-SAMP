/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.cause;

public final class EndCause
extends Enum<EndCause> {
    private static final EndCause[] $VALUES;
    public static final /* enum */ EndCause CANCELED;
    public static final /* enum */ EndCause COMPLETED;
    public static final /* enum */ EndCause ERROR;
    public static final /* enum */ EndCause FILE_BUSY;
    public static final /* enum */ EndCause PRE_ALLOCATE_FAILED;
    public static final /* enum */ EndCause SAME_TASK_BUSY;

    static {
        EndCause endCause;
        EndCause endCause2;
        EndCause endCause3;
        EndCause endCause4;
        EndCause endCause5;
        EndCause endCause6;
        COMPLETED = endCause6 = new EndCause();
        ERROR = endCause5 = new EndCause();
        CANCELED = endCause4 = new EndCause();
        FILE_BUSY = endCause3 = new EndCause();
        SAME_TASK_BUSY = endCause2 = new EndCause();
        PRE_ALLOCATE_FAILED = endCause = new EndCause();
        $VALUES = new EndCause[]{endCause6, endCause5, endCause4, endCause3, endCause2, endCause};
    }

    public static EndCause valueOf(String string2) {
        return Enum.valueOf(EndCause.class, string2);
    }

    public static EndCause[] values() {
        return (EndCause[])$VALUES.clone();
    }
}

