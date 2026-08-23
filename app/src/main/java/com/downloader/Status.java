/*
 * Decompiled with CFR 0.152.
 */
package com.downloader;

public final class Status
extends Enum<Status> {
    private static final Status[] $VALUES;
    public static final /* enum */ Status CANCELLED;
    public static final /* enum */ Status COMPLETED;
    public static final /* enum */ Status FAILED;
    public static final /* enum */ Status PAUSED;
    public static final /* enum */ Status QUEUED;
    public static final /* enum */ Status RUNNING;
    public static final /* enum */ Status UNKNOWN;

    static {
        Status status;
        Status status2;
        Status status3;
        Status status4;
        Status status5;
        Status status6;
        Status status7;
        QUEUED = status7 = new Status();
        RUNNING = status6 = new Status();
        PAUSED = status5 = new Status();
        COMPLETED = status4 = new Status();
        CANCELLED = status3 = new Status();
        FAILED = status2 = new Status();
        UNKNOWN = status = new Status();
        $VALUES = new Status[]{status7, status6, status5, status4, status3, status2, status};
    }

    public static Status valueOf(String string2) {
        return Enum.valueOf(Status.class, string2);
    }

    public static Status[] values() {
        return (Status[])$VALUES.clone();
    }
}

