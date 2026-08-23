/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.breakpoint;

import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.breakpoint.BreakpointStore;
import com.liulishuo.okdownload.core.cause.EndCause;
import java.io.IOException;

public interface DownloadStore
extends BreakpointStore {
    public BreakpointInfo getAfterCompleted(int var1);

    public void onSyncToFilesystemSuccess(BreakpointInfo var1, int var2, long var3) throws IOException;

    public void onTaskEnd(int var1, EndCause var2, Exception var3);

    public void onTaskStart(int var1);
}

