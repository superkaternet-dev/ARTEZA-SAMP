/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.download;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.liulishuo.okdownload.core.download.ConnectTrial;
import com.liulishuo.okdownload.core.download.DownloadStrategy;
import com.liulishuo.okdownload.core.exception.FileBusyAfterRunException;
import com.liulishuo.okdownload.core.exception.ServerCanceledException;
import java.io.IOException;

public class BreakpointRemoteCheck {
    private boolean acceptRange;
    ResumeFailedCause failedCause;
    private final BreakpointInfo info;
    private long instanceLength;
    private boolean resumable;
    private final DownloadTask task;

    public BreakpointRemoteCheck(DownloadTask downloadTask, BreakpointInfo breakpointInfo) {
        this.task = downloadTask;
        this.info = breakpointInfo;
    }

    public void check() throws IOException {
        DownloadStrategy downloadStrategy = OkDownload.with().downloadStrategy();
        ConnectTrial connectTrial = this.createConnectTrial();
        connectTrial.executeTrial();
        boolean bl = connectTrial.isAcceptRange();
        boolean bl2 = connectTrial.isChunked();
        long l = connectTrial.getInstanceLength();
        Object object = connectTrial.getResponseEtag();
        String string2 = connectTrial.getResponseFilename();
        int n = connectTrial.getResponseCode();
        downloadStrategy.validFilenameFromResponse(string2, this.task, this.info);
        this.info.setChunked(bl2);
        this.info.setEtag((String)object);
        if (!OkDownload.with().downloadDispatcher().isFileConflictAfterRun(this.task)) {
            long l2 = this.info.getTotalOffset();
            boolean bl3 = true;
            bl2 = l2 != 0L;
            object = downloadStrategy.getPreconditionFailedCause(n, bl2, this.info, (String)object);
            bl2 = object == null;
            this.resumable = bl2;
            this.failedCause = object;
            this.instanceLength = l;
            this.acceptRange = bl;
            if (!this.isTrialSpecialPass(n, l, bl2) && downloadStrategy.isServerCanceled(n, bl2 = this.info.getTotalOffset() != 0L ? bl3 : false)) {
                throw new ServerCanceledException(n, this.info.getTotalOffset());
            }
            return;
        }
        throw FileBusyAfterRunException.SIGNAL;
    }

    ConnectTrial createConnectTrial() {
        return new ConnectTrial(this.task, this.info);
    }

    public ResumeFailedCause getCause() {
        return this.failedCause;
    }

    public ResumeFailedCause getCauseOrThrow() {
        Object object = this.failedCause;
        if (object != null) {
            return object;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("No cause find with resumable: ");
        ((StringBuilder)object).append(this.resumable);
        throw new IllegalStateException(((StringBuilder)object).toString());
    }

    public long getInstanceLength() {
        return this.instanceLength;
    }

    public boolean isAcceptRange() {
        return this.acceptRange;
    }

    public boolean isResumable() {
        return this.resumable;
    }

    boolean isTrialSpecialPass(int n, long l, boolean bl) {
        return n == 416 && l >= 0L && bl;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("acceptRange[");
        stringBuilder.append(this.acceptRange);
        stringBuilder.append("] ");
        stringBuilder.append("resumable[");
        stringBuilder.append(this.resumable);
        stringBuilder.append("] ");
        stringBuilder.append("failedCause[");
        stringBuilder.append((Object)this.failedCause);
        stringBuilder.append("] ");
        stringBuilder.append("instanceLength[");
        stringBuilder.append(this.instanceLength);
        stringBuilder.append("] ");
        stringBuilder.append(super.toString());
        return stringBuilder.toString();
    }
}

