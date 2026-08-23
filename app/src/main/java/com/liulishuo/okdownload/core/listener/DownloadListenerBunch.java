/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.listener;

import com.liulishuo.okdownload.DownloadListener;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DownloadListenerBunch
implements DownloadListener {
    final DownloadListener[] listenerList;

    DownloadListenerBunch(DownloadListener[] downloadListenerArray) {
        this.listenerList = downloadListenerArray;
    }

    @Override
    public void connectEnd(DownloadTask downloadTask, int n, int n2, Map<String, List<String>> map) {
        DownloadListener[] downloadListenerArray = this.listenerList;
        int n3 = downloadListenerArray.length;
        for (int i = 0; i < n3; ++i) {
            downloadListenerArray[i].connectEnd(downloadTask, n, n2, map);
        }
    }

    @Override
    public void connectStart(DownloadTask downloadTask, int n, Map<String, List<String>> map) {
        DownloadListener[] downloadListenerArray = this.listenerList;
        int n2 = downloadListenerArray.length;
        for (int i = 0; i < n2; ++i) {
            downloadListenerArray[i].connectStart(downloadTask, n, map);
        }
    }

    @Override
    public void connectTrialEnd(DownloadTask downloadTask, int n, Map<String, List<String>> map) {
        DownloadListener[] downloadListenerArray = this.listenerList;
        int n2 = downloadListenerArray.length;
        for (int i = 0; i < n2; ++i) {
            downloadListenerArray[i].connectTrialEnd(downloadTask, n, map);
        }
    }

    @Override
    public void connectTrialStart(DownloadTask downloadTask, Map<String, List<String>> map) {
        DownloadListener[] downloadListenerArray = this.listenerList;
        int n = downloadListenerArray.length;
        for (int i = 0; i < n; ++i) {
            downloadListenerArray[i].connectTrialStart(downloadTask, map);
        }
    }

    public boolean contain(DownloadListener downloadListener) {
        DownloadListener[] downloadListenerArray = this.listenerList;
        int n = downloadListenerArray.length;
        for (int i = 0; i < n; ++i) {
            if (downloadListenerArray[i] != downloadListener) continue;
            return true;
        }
        return false;
    }

    @Override
    public void downloadFromBeginning(DownloadTask downloadTask, BreakpointInfo breakpointInfo, ResumeFailedCause resumeFailedCause) {
        DownloadListener[] downloadListenerArray = this.listenerList;
        int n = downloadListenerArray.length;
        for (int i = 0; i < n; ++i) {
            downloadListenerArray[i].downloadFromBeginning(downloadTask, breakpointInfo, resumeFailedCause);
        }
    }

    @Override
    public void downloadFromBreakpoint(DownloadTask downloadTask, BreakpointInfo breakpointInfo) {
        DownloadListener[] downloadListenerArray = this.listenerList;
        int n = downloadListenerArray.length;
        for (int i = 0; i < n; ++i) {
            downloadListenerArray[i].downloadFromBreakpoint(downloadTask, breakpointInfo);
        }
    }

    @Override
    public void fetchEnd(DownloadTask downloadTask, int n, long l) {
        DownloadListener[] downloadListenerArray = this.listenerList;
        int n2 = downloadListenerArray.length;
        for (int i = 0; i < n2; ++i) {
            downloadListenerArray[i].fetchEnd(downloadTask, n, l);
        }
    }

    @Override
    public void fetchProgress(DownloadTask downloadTask, int n, long l) {
        DownloadListener[] downloadListenerArray = this.listenerList;
        int n2 = downloadListenerArray.length;
        for (int i = 0; i < n2; ++i) {
            downloadListenerArray[i].fetchProgress(downloadTask, n, l);
        }
    }

    @Override
    public void fetchStart(DownloadTask downloadTask, int n, long l) {
        DownloadListener[] downloadListenerArray = this.listenerList;
        int n2 = downloadListenerArray.length;
        for (int i = 0; i < n2; ++i) {
            downloadListenerArray[i].fetchStart(downloadTask, n, l);
        }
    }

    public int indexOf(DownloadListener downloadListener) {
        DownloadListener[] downloadListenerArray;
        for (int i = 0; i < (downloadListenerArray = this.listenerList).length; ++i) {
            if (downloadListenerArray[i] != downloadListener) continue;
            return i;
        }
        return -1;
    }

    @Override
    public void taskEnd(DownloadTask downloadTask, EndCause endCause, Exception exception) {
        DownloadListener[] downloadListenerArray = this.listenerList;
        int n = downloadListenerArray.length;
        for (int i = 0; i < n; ++i) {
            downloadListenerArray[i].taskEnd(downloadTask, endCause, exception);
        }
    }

    @Override
    public void taskStart(DownloadTask downloadTask) {
        DownloadListener[] downloadListenerArray = this.listenerList;
        int n = downloadListenerArray.length;
        for (int i = 0; i < n; ++i) {
            downloadListenerArray[i].taskStart(downloadTask);
        }
    }

    public static class Builder {
        private List<DownloadListener> listenerList = new ArrayList<DownloadListener>();

        public Builder append(DownloadListener downloadListener) {
            if (downloadListener != null && !this.listenerList.contains(downloadListener)) {
                this.listenerList.add(downloadListener);
            }
            return this;
        }

        public DownloadListenerBunch build() {
            List<DownloadListener> list = this.listenerList;
            return new DownloadListenerBunch(list.toArray(new DownloadListener[list.size()]));
        }

        public boolean remove(DownloadListener downloadListener) {
            return this.listenerList.remove(downloadListener);
        }
    }
}

