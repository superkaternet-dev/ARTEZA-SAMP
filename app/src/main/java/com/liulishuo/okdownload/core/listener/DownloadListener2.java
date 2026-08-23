/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.listener;

import com.liulishuo.okdownload.DownloadListener;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import java.util.List;
import java.util.Map;

public abstract class DownloadListener2
implements DownloadListener {
    @Override
    public void connectEnd(DownloadTask downloadTask, int n, int n2, Map<String, List<String>> map) {
    }

    @Override
    public void connectStart(DownloadTask downloadTask, int n, Map<String, List<String>> map) {
    }

    @Override
    public void connectTrialEnd(DownloadTask downloadTask, int n, Map<String, List<String>> map) {
    }

    @Override
    public void connectTrialStart(DownloadTask downloadTask, Map<String, List<String>> map) {
    }

    @Override
    public void downloadFromBeginning(DownloadTask downloadTask, BreakpointInfo breakpointInfo, ResumeFailedCause resumeFailedCause) {
    }

    @Override
    public void downloadFromBreakpoint(DownloadTask downloadTask, BreakpointInfo breakpointInfo) {
    }

    @Override
    public void fetchEnd(DownloadTask downloadTask, int n, long l) {
    }

    @Override
    public void fetchProgress(DownloadTask downloadTask, int n, long l) {
    }

    @Override
    public void fetchStart(DownloadTask downloadTask, int n, long l) {
    }
}

