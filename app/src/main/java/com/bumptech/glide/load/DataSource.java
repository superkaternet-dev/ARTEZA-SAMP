/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load;

public final class DataSource
extends Enum<DataSource> {
    private static final DataSource[] $VALUES;
    public static final /* enum */ DataSource DATA_DISK_CACHE;
    public static final /* enum */ DataSource LOCAL;
    public static final /* enum */ DataSource MEMORY_CACHE;
    public static final /* enum */ DataSource REMOTE;
    public static final /* enum */ DataSource RESOURCE_DISK_CACHE;

    static {
        DataSource dataSource;
        DataSource dataSource2;
        DataSource dataSource3;
        DataSource dataSource4;
        DataSource dataSource5;
        LOCAL = dataSource5 = new DataSource();
        REMOTE = dataSource4 = new DataSource();
        DATA_DISK_CACHE = dataSource3 = new DataSource();
        RESOURCE_DISK_CACHE = dataSource2 = new DataSource();
        MEMORY_CACHE = dataSource = new DataSource();
        $VALUES = new DataSource[]{dataSource5, dataSource4, dataSource3, dataSource2, dataSource};
    }

    public static DataSource valueOf(String string2) {
        return Enum.valueOf(DataSource.class, string2);
    }

    public static DataSource[] values() {
        return (DataSource[])$VALUES.clone();
    }
}

