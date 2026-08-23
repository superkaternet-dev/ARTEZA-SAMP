/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;

public interface DownloadMonitor {
    public void taskDownloadFromBeginning(DownloadTask var1, BreakpointInfo var2, ResumeFailedCause var3);

    public void taskDownloadFromBreakpoint(DownloadTask var1, BreakpointInfo var2);

    public void taskEnd(DownloadTask var1, EndCause var2, Exception var3);

    public void taskStart(DownloadTask var1);
}

