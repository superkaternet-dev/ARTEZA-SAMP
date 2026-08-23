/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.connection;

import com.liulishuo.filedownloader.util.FileDownloadHelper;

public class DefaultConnectionCountAdapter
implements FileDownloadHelper.ConnectionCountAdapter {
    private static final long FOUR_CONNECTION_UPPER_LIMIT = 0x6400000L;
    private static final long ONE_CONNECTION_UPPER_LIMIT = 0x100000L;
    private static final long THREE_CONNECTION_UPPER_LIMIT = 0x3200000L;
    private static final long TWO_CONNECTION_UPPER_LIMIT = 0x500000L;

    @Override
    public int determineConnectionCount(int n, String string2, String string3, long l) {
        if (l < 0x100000L) {
            return 1;
        }
        if (l < 0x500000L) {
            return 2;
        }
        if (l < 0x3200000L) {
            return 3;
        }
        if (l < 0x6400000L) {
            return 4;
        }
        return 5;
    }
}

