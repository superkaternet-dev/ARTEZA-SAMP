/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.net.Uri
 */
package com.liulishuo.okdownload.core.download;

import android.net.Uri;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import java.io.File;

public class BreakpointLocalCheck {
    private boolean dirty;
    boolean fileExist;
    private final BreakpointInfo info;
    boolean infoRight;
    boolean outputStreamSupport;
    private final long responseInstanceLength;
    private final DownloadTask task;

    public BreakpointLocalCheck(DownloadTask downloadTask, BreakpointInfo breakpointInfo, long l) {
        this.task = downloadTask;
        this.info = breakpointInfo;
        this.responseInstanceLength = l;
    }

    public void check() {
        boolean bl;
        this.fileExist = this.isFileExistToResume();
        this.infoRight = this.isInfoRightToResume();
        this.outputStreamSupport = bl = this.isOutputStreamSupportResume();
        bl = !(this.infoRight && this.fileExist && bl);
        this.dirty = bl;
    }

    public ResumeFailedCause getCauseOrThrow() {
        if (!this.infoRight) {
            return ResumeFailedCause.INFO_DIRTY;
        }
        if (!this.fileExist) {
            return ResumeFailedCause.FILE_NOT_EXIST;
        }
        if (!this.outputStreamSupport) {
            return ResumeFailedCause.OUTPUT_STREAM_NOT_SUPPORT;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("No cause find with dirty: ");
        stringBuilder.append(this.dirty);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public boolean isFileExistToResume() {
        Object object = this.task.getUri();
        boolean bl = Util.isUriContentScheme((Uri)object);
        boolean bl2 = true;
        boolean bl3 = true;
        if (bl) {
            if (Util.getSizeFromContentUri((Uri)object) <= 0L) {
                bl3 = false;
            }
            return bl3;
        }
        object = this.task.getFile();
        bl3 = object != null && ((File)object).exists() ? bl2 : false;
        return bl3;
    }

    public boolean isInfoRightToResume() {
        int n = this.info.getBlockCount();
        if (n <= 0) {
            return false;
        }
        if (this.info.isChunked()) {
            return false;
        }
        if (this.info.getFile() == null) {
            return false;
        }
        File file = this.task.getFile();
        if (!this.info.getFile().equals(file)) {
            return false;
        }
        if (this.info.getFile().length() > this.info.getTotalLength()) {
            return false;
        }
        if (this.responseInstanceLength > 0L && this.info.getTotalLength() != this.responseInstanceLength) {
            return false;
        }
        for (int i = 0; i < n; ++i) {
            if (this.info.getBlock(i).getContentLength() > 0L) continue;
            return false;
        }
        return true;
    }

    public boolean isOutputStreamSupportResume() {
        if (OkDownload.with().outputStreamFactory().supportSeek()) {
            return true;
        }
        if (this.info.getBlockCount() != 1) {
            return false;
        }
        return !OkDownload.with().processFileStrategy().isPreAllocateLength(this.task);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("fileExist[");
        stringBuilder.append(this.fileExist);
        stringBuilder.append("] ");
        stringBuilder.append("infoRight[");
        stringBuilder.append(this.infoRight);
        stringBuilder.append("] ");
        stringBuilder.append("outputStreamSupport[");
        stringBuilder.append(this.outputStreamSupport);
        stringBuilder.append("] ");
        stringBuilder.append(super.toString());
        return stringBuilder.toString();
    }
}

