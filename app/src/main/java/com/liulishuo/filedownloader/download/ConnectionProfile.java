/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.download;

import com.liulishuo.filedownloader.connection.FileDownloadConnection;
import com.liulishuo.filedownloader.util.FileDownloadProperties;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.net.ProtocolException;

public class ConnectionProfile {
    static final int RANGE_INFINITE = -1;
    final long contentLength;
    final long currentOffset;
    final long endOffset;
    private final boolean isForceNoRange;
    private final boolean isTrialConnect;
    final long startOffset;

    private ConnectionProfile() {
        this.startOffset = 0L;
        this.currentOffset = 0L;
        this.endOffset = 0L;
        this.contentLength = 0L;
        this.isForceNoRange = false;
        this.isTrialConnect = true;
    }

    private ConnectionProfile(long l, long l2, long l3, long l4) {
        this(l, l2, l3, l4, false);
    }

    private ConnectionProfile(long l, long l2, long l3, long l4, boolean bl) {
        if (l == 0L && l3 == 0L || !bl) {
            this.startOffset = l;
            this.currentOffset = l2;
            this.endOffset = l3;
            this.contentLength = l4;
            this.isForceNoRange = bl;
            this.isTrialConnect = false;
            return;
        }
        throw new IllegalArgumentException();
    }

    public void processProfile(FileDownloadConnection fileDownloadConnection) throws ProtocolException {
        if (this.isForceNoRange) {
            return;
        }
        if (this.isTrialConnect && FileDownloadProperties.getImpl().trialConnectionHeadMethod) {
            fileDownloadConnection.setRequestMethod("HEAD");
        }
        String string2 = this.endOffset == -1L ? FileDownloadUtils.formatString("bytes=%d-", this.currentOffset) : FileDownloadUtils.formatString("bytes=%d-%d", this.currentOffset, this.endOffset);
        fileDownloadConnection.addHeader("Range", string2);
    }

    public String toString() {
        return FileDownloadUtils.formatString("range[%d, %d) current offset[%d]", this.startOffset, this.endOffset, this.currentOffset);
    }

    public static class ConnectionProfileBuild {
        public static ConnectionProfile buildBeginToEndConnectionProfile(long l) {
            return new ConnectionProfile(0L, 0L, -1L, l);
        }

        public static ConnectionProfile buildConnectionProfile(long l, long l2, long l3, long l4) {
            return new ConnectionProfile(l, l2, l3, l4);
        }

        public static ConnectionProfile buildToEndConnectionProfile(long l, long l2, long l3) {
            return new ConnectionProfile(l, l2, -1L, l3);
        }

        public static ConnectionProfile buildTrialConnectionProfile() {
            return new ConnectionProfile();
        }

        public static ConnectionProfile buildTrialConnectionProfileNoRange() {
            return new ConnectionProfile(0L, 0L, 0L, 0L, true);
        }
    }
}

