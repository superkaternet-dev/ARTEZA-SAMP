/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.breakpoint.BreakpointStore;
import com.liulishuo.okdownload.core.dispatcher.DownloadDispatcher;
import java.io.File;

public class StatusUtil {
    static DownloadTask createFinder(String string2, String string3, String string4) {
        return new DownloadTask.Builder(string2, string3, string4).build();
    }

    public static BreakpointInfo getCurrentInfo(DownloadTask object) {
        BreakpointStore breakpointStore = OkDownload.with().breakpointStore();
        object = breakpointStore.get(breakpointStore.findOrCreateId((DownloadTask)object));
        object = object == null ? null : ((BreakpointInfo)object).copy();
        return object;
    }

    public static BreakpointInfo getCurrentInfo(String string2, String string3, String string4) {
        return StatusUtil.getCurrentInfo(StatusUtil.createFinder(string2, string3, string4));
    }

    public static Status getStatus(DownloadTask downloadTask) {
        Status status = StatusUtil.isCompletedOrUnknown(downloadTask);
        if (status == Status.COMPLETED) {
            return Status.COMPLETED;
        }
        DownloadDispatcher downloadDispatcher = OkDownload.with().downloadDispatcher();
        if (downloadDispatcher.isPending(downloadTask)) {
            return Status.PENDING;
        }
        if (downloadDispatcher.isRunning(downloadTask)) {
            return Status.RUNNING;
        }
        return status;
    }

    public static Status getStatus(String string2, String string3, String string4) {
        return StatusUtil.getStatus(StatusUtil.createFinder(string2, string3, string4));
    }

    public static boolean isCompleted(DownloadTask downloadTask) {
        boolean bl = StatusUtil.isCompletedOrUnknown(downloadTask) == Status.COMPLETED;
        return bl;
    }

    public static boolean isCompleted(String string2, String string3, String string4) {
        return StatusUtil.isCompleted(StatusUtil.createFinder(string2, string3, string4));
    }

    public static Status isCompletedOrUnknown(DownloadTask object) {
        BreakpointStore breakpointStore = OkDownload.with().breakpointStore();
        BreakpointInfo breakpointInfo = breakpointStore.get(((DownloadTask)object).getId());
        String string2 = ((DownloadTask)object).getFilename();
        File file = ((DownloadTask)object).getParentFile();
        File file2 = ((DownloadTask)object).getFile();
        if (breakpointInfo != null) {
            if (!breakpointInfo.isChunked() && breakpointInfo.getTotalLength() <= 0L) {
                return Status.UNKNOWN;
            }
            if (file2 != null && file2.equals(breakpointInfo.getFile()) && file2.exists() && breakpointInfo.getTotalOffset() == breakpointInfo.getTotalLength()) {
                return Status.COMPLETED;
            }
            if (string2 == null && breakpointInfo.getFile() != null && breakpointInfo.getFile().exists()) {
                return Status.IDLE;
            }
            if (file2 != null && file2.equals(breakpointInfo.getFile()) && file2.exists()) {
                return Status.IDLE;
            }
        } else {
            if (breakpointStore.isOnlyMemoryCache()) {
                return Status.UNKNOWN;
            }
            if (file2 != null && file2.exists()) {
                return Status.COMPLETED;
            }
            if ((object = breakpointStore.getResponseFilename(((DownloadTask)object).getUrl())) != null && new File(file, (String)object).exists()) {
                return Status.COMPLETED;
            }
        }
        return Status.UNKNOWN;
    }

    public static boolean isSameTaskPendingOrRunning(DownloadTask downloadTask) {
        boolean bl = OkDownload.with().downloadDispatcher().findSameTask(downloadTask) != null;
        return bl;
    }

    public static final class Status
    extends Enum<Status> {
        private static final Status[] $VALUES;
        public static final /* enum */ Status COMPLETED;
        public static final /* enum */ Status IDLE;
        public static final /* enum */ Status PENDING;
        public static final /* enum */ Status RUNNING;
        public static final /* enum */ Status UNKNOWN;

        static {
            Status status;
            Status status2;
            Status status3;
            Status status4;
            Status status5;
            PENDING = status5 = new Status();
            RUNNING = status4 = new Status();
            COMPLETED = status3 = new Status();
            IDLE = status2 = new Status();
            UNKNOWN = status = new Status();
            $VALUES = new Status[]{status5, status4, status3, status2, status};
        }

        public static Status valueOf(String string2) {
            return Enum.valueOf(Status.class, string2);
        }

        public static Status[] values() {
            return (Status[])$VALUES.clone();
        }
    }
}

