/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.listener;

import com.liulishuo.okdownload.DownloadListener;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.liulishuo.okdownload.core.listener.assist.Listener1Assist;
import com.liulishuo.okdownload.core.listener.assist.ListenerAssist;
import java.util.List;
import java.util.Map;

public abstract class DownloadListener1
implements DownloadListener,
Listener1Assist.Listener1Callback,
ListenerAssist {
    final Listener1Assist assist;

    public DownloadListener1() {
        this(new Listener1Assist());
    }

    DownloadListener1(Listener1Assist listener1Assist) {
        this.assist = listener1Assist;
        listener1Assist.setCallback(this);
    }

    @Override
    public void connectEnd(DownloadTask downloadTask, int n, int n2, Map<String, List<String>> map) {
        this.assist.connectEnd(downloadTask);
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
        this.assist.downloadFromBeginning(downloadTask, breakpointInfo, resumeFailedCause);
    }

    @Override
    public void downloadFromBreakpoint(DownloadTask downloadTask, BreakpointInfo breakpointInfo) {
        this.assist.downloadFromBreakpoint(downloadTask, breakpointInfo);
    }

    @Override
    public void fetchEnd(DownloadTask downloadTask, int n, long l) {
    }

    @Override
    public void fetchProgress(DownloadTask downloadTask, int n, long l) {
        this.assist.fetchProgress(downloadTask, l);
    }

    @Override
    public void fetchStart(DownloadTask downloadTask, int n, long l) {
    }

    @Override
    public boolean isAlwaysRecoverAssistModel() {
        return this.assist.isAlwaysRecoverAssistModel();
    }

    @Override
    public void setAlwaysRecoverAssistModel(boolean bl) {
        this.assist.setAlwaysRecoverAssistModel(bl);
    }

    @Override
    public void setAlwaysRecoverAssistModelIfNotSet(boolean bl) {
        this.assist.setAlwaysRecoverAssistModelIfNotSet(bl);
    }

    @Override
    public final void taskEnd(DownloadTask downloadTask, EndCause endCause, Exception exception) {
        this.assist.taskEnd(downloadTask, endCause, exception);
    }

    @Override
    public final void taskStart(DownloadTask downloadTask) {
        this.assist.taskStart(downloadTask);
    }
}

