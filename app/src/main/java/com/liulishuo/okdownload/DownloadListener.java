/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import java.util.List;
import java.util.Map;

public interface DownloadListener {
    public void connectEnd(DownloadTask var1, int var2, int var3, Map<String, List<String>> var4);

    public void connectStart(DownloadTask var1, int var2, Map<String, List<String>> var3);

    public void connectTrialEnd(DownloadTask var1, int var2, Map<String, List<String>> var3);

    public void connectTrialStart(DownloadTask var1, Map<String, List<String>> var2);

    public void downloadFromBeginning(DownloadTask var1, BreakpointInfo var2, ResumeFailedCause var3);

    public void downloadFromBreakpoint(DownloadTask var1, BreakpointInfo var2);

    public void fetchEnd(DownloadTask var1, int var2, long var3);

    public void fetchProgress(DownloadTask var1, int var2, long var3);

    public void fetchStart(DownloadTask var1, int var2, long var3);

    public void taskEnd(DownloadTask var1, EndCause var2, Exception var3);

    public void taskStart(DownloadTask var1);
}

