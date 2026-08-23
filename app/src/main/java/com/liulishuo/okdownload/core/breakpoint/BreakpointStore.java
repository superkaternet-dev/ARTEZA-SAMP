/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.breakpoint;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import java.io.IOException;

public interface BreakpointStore {
    public BreakpointInfo createAndInsert(DownloadTask var1) throws IOException;

    public BreakpointInfo findAnotherInfoFromCompare(DownloadTask var1, BreakpointInfo var2);

    public int findOrCreateId(DownloadTask var1);

    public BreakpointInfo get(int var1);

    public String getResponseFilename(String var1);

    public boolean isOnlyMemoryCache();

    public void remove(int var1);

    public boolean update(BreakpointInfo var1) throws IOException;
}

