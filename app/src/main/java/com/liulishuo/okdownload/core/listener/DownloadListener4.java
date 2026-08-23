/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.listener;

import com.liulishuo.okdownload.DownloadListener;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.liulishuo.okdownload.core.listener.assist.Listener4Assist;
import com.liulishuo.okdownload.core.listener.assist.ListenerAssist;
import com.liulishuo.okdownload.core.listener.assist.ListenerModelHandler;
import java.util.List;
import java.util.Map;

public abstract class DownloadListener4
implements DownloadListener,
Listener4Assist.Listener4Callback,
ListenerAssist {
    final Listener4Assist assist;

    public DownloadListener4() {
        this(new Listener4Assist<Listener4Assist.Listener4Model>(new Listener4ModelCreator()));
    }

    DownloadListener4(Listener4Assist listener4Assist) {
        this.assist = listener4Assist;
        listener4Assist.setCallback(this);
    }

    @Override
    public void connectTrialEnd(DownloadTask downloadTask, int n, Map<String, List<String>> map) {
    }

    @Override
    public void connectTrialStart(DownloadTask downloadTask, Map<String, List<String>> map) {
    }

    @Override
    public final void downloadFromBeginning(DownloadTask downloadTask, BreakpointInfo breakpointInfo, ResumeFailedCause resumeFailedCause) {
        this.assist.infoReady(downloadTask, breakpointInfo, false);
    }

    @Override
    public final void downloadFromBreakpoint(DownloadTask downloadTask, BreakpointInfo breakpointInfo) {
        this.assist.infoReady(downloadTask, breakpointInfo, true);
    }

    @Override
    public void fetchEnd(DownloadTask downloadTask, int n, long l) {
        this.assist.fetchEnd(downloadTask, n);
    }

    @Override
    public final void fetchProgress(DownloadTask downloadTask, int n, long l) {
        this.assist.fetchProgress(downloadTask, n, l);
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

    public void setAssistExtend(Listener4Assist.AssistExtend assistExtend) {
        this.assist.setAssistExtend(assistExtend);
    }

    @Override
    public final void taskEnd(DownloadTask downloadTask, EndCause endCause, Exception exception) {
        this.assist.taskEnd(downloadTask, endCause, exception);
    }

    static class Listener4ModelCreator
    implements ListenerModelHandler.ModelCreator<Listener4Assist.Listener4Model> {
        Listener4ModelCreator() {
        }

        @Override
        public Listener4Assist.Listener4Model create(int n) {
            return new Listener4Assist.Listener4Model(n);
        }
    }
}

