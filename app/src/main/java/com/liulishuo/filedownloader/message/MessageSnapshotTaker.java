/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.message;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.download.DownloadStatusCallback;
import com.liulishuo.filedownloader.message.BlockCompleteMessage;
import com.liulishuo.filedownloader.message.LargeMessageSnapshot;
import com.liulishuo.filedownloader.message.MessageSnapshot;
import com.liulishuo.filedownloader.message.SmallMessageSnapshot;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.io.File;

public class MessageSnapshotTaker {
    public static MessageSnapshot catchCanReusedOldFile(int n, File file, boolean bl) {
        long l = file.length();
        if (l > Integer.MAX_VALUE) {
            if (bl) {
                return new LargeMessageSnapshot.CompletedFlowDirectlySnapshot(n, true, l);
            }
            return new LargeMessageSnapshot.CompletedSnapshot(n, true, l);
        }
        if (bl) {
            return new SmallMessageSnapshot.CompletedFlowDirectlySnapshot(n, true, (int)l);
        }
        return new SmallMessageSnapshot.CompletedSnapshot(n, true, (int)l);
    }

    public static MessageSnapshot catchException(int n, long l, Throwable throwable) {
        if (l > Integer.MAX_VALUE) {
            return new LargeMessageSnapshot.ErrorMessageSnapshot(n, l, throwable);
        }
        return new SmallMessageSnapshot.ErrorMessageSnapshot(n, (int)l, throwable);
    }

    public static MessageSnapshot catchPause(BaseDownloadTask baseDownloadTask) {
        if (baseDownloadTask.isLargeFile()) {
            return new LargeMessageSnapshot.PausedSnapshot(baseDownloadTask.getId(), baseDownloadTask.getLargeFileSoFarBytes(), baseDownloadTask.getLargeFileTotalBytes());
        }
        return new SmallMessageSnapshot.PausedSnapshot(baseDownloadTask.getId(), baseDownloadTask.getSmallFileSoFarBytes(), baseDownloadTask.getSmallFileTotalBytes());
    }

    public static MessageSnapshot catchWarn(int n, long l, long l2, boolean bl) {
        if (l2 > Integer.MAX_VALUE) {
            if (bl) {
                return new LargeMessageSnapshot.WarnFlowDirectlySnapshot(n, l, l2);
            }
            return new LargeMessageSnapshot.WarnMessageSnapshot(n, l, l2);
        }
        if (bl) {
            return new SmallMessageSnapshot.WarnFlowDirectlySnapshot(n, (int)l, (int)l2);
        }
        return new SmallMessageSnapshot.WarnMessageSnapshot(n, (int)l, (int)l2);
    }

    public static MessageSnapshot take(byte by, FileDownloadModel fileDownloadModel) {
        return MessageSnapshotTaker.take(by, fileDownloadModel, null);
    }

    public static MessageSnapshot take(byte by, FileDownloadModel object, DownloadStatusCallback.ProcessParams object2) {
        int n;
        block9: {
            block10: {
                n = ((FileDownloadModel)object).getId();
                if (by == -4) break block9;
                switch (by) {
                    default: {
                        String string2 = FileDownloadUtils.formatString("it can't takes a snapshot for the task(%s) when its status is %d,", object, by);
                        FileDownloadLog.w(MessageSnapshotTaker.class, "it can't takes a snapshot for the task(%s) when its status is %d,", object, by);
                        object2 = ((DownloadStatusCallback.ProcessParams)object2).getException() != null ? new IllegalStateException(string2, ((DownloadStatusCallback.ProcessParams)object2).getException()) : new IllegalStateException(string2);
                    }
                    case 6: {
                        object = new MessageSnapshot.StartedMessageSnapshot(n);
                        break block10;
                    }
                    case 5: {
                        object = ((FileDownloadModel)object).isLargeFile() ? new LargeMessageSnapshot.RetryMessageSnapshot(n, ((FileDownloadModel)object).getSoFar(), ((DownloadStatusCallback.ProcessParams)object2).getException(), ((DownloadStatusCallback.ProcessParams)object2).getRetryingTimes()) : new SmallMessageSnapshot.RetryMessageSnapshot(n, (int)((FileDownloadModel)object).getSoFar(), ((DownloadStatusCallback.ProcessParams)object2).getException(), ((DownloadStatusCallback.ProcessParams)object2).getRetryingTimes());
                        break block10;
                    }
                    case 3: {
                        object = ((FileDownloadModel)object).isLargeFile() ? new LargeMessageSnapshot.ProgressMessageSnapshot(n, ((FileDownloadModel)object).getSoFar()) : new SmallMessageSnapshot.ProgressMessageSnapshot(n, (int)((FileDownloadModel)object).getSoFar());
                        break block10;
                    }
                    case 2: {
                        String string3 = ((FileDownloadModel)object).isPathAsDirectory() ? ((FileDownloadModel)object).getFilename() : null;
                        object = ((FileDownloadModel)object).isLargeFile() ? new LargeMessageSnapshot.ConnectedMessageSnapshot(n, ((DownloadStatusCallback.ProcessParams)object2).isResuming(), ((FileDownloadModel)object).getTotal(), ((FileDownloadModel)object).getETag(), string3) : new SmallMessageSnapshot.ConnectedMessageSnapshot(n, ((DownloadStatusCallback.ProcessParams)object2).isResuming(), (int)((FileDownloadModel)object).getTotal(), ((FileDownloadModel)object).getETag(), string3);
                        break block10;
                    }
                    case 1: {
                        object = ((FileDownloadModel)object).isLargeFile() ? new LargeMessageSnapshot.PendingMessageSnapshot(n, ((FileDownloadModel)object).getSoFar(), ((FileDownloadModel)object).getTotal()) : new SmallMessageSnapshot.PendingMessageSnapshot(n, (int)((FileDownloadModel)object).getSoFar(), (int)((FileDownloadModel)object).getTotal());
                        break block10;
                    }
                    case -1: {
                        object = ((FileDownloadModel)object).isLargeFile() ? new LargeMessageSnapshot.ErrorMessageSnapshot(n, ((FileDownloadModel)object).getSoFar(), ((DownloadStatusCallback.ProcessParams)object2).getException()) : new SmallMessageSnapshot.ErrorMessageSnapshot(n, (int)((FileDownloadModel)object).getSoFar(), ((DownloadStatusCallback.ProcessParams)object2).getException());
                        break block10;
                    }
                    case -3: {
                        object = ((FileDownloadModel)object).isLargeFile() ? new LargeMessageSnapshot.CompletedSnapshot(n, false, ((FileDownloadModel)object).getTotal()) : new SmallMessageSnapshot.CompletedSnapshot(n, false, (int)((FileDownloadModel)object).getTotal());
                        break block10;
                    }
                }
                object = ((FileDownloadModel)object).isLargeFile() ? new LargeMessageSnapshot.ErrorMessageSnapshot(n, ((FileDownloadModel)object).getSoFar(), (Throwable)object2) : new SmallMessageSnapshot.ErrorMessageSnapshot(n, (int)((FileDownloadModel)object).getSoFar(), (Throwable)object2);
            }
            return object;
        }
        throw new IllegalStateException(FileDownloadUtils.formatString("please use #catchWarn instead %d", n));
    }

    public static MessageSnapshot takeBlockCompleted(MessageSnapshot messageSnapshot) {
        if (messageSnapshot.getStatus() == -3) {
            return new BlockCompleteMessage.BlockCompleteMessageImpl(messageSnapshot);
        }
        throw new IllegalStateException(FileDownloadUtils.formatString("take block completed snapshot, must has already be completed. %d %d", messageSnapshot.getId(), messageSnapshot.getStatus()));
    }
}

