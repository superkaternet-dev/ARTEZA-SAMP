/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 */
package com.liulishuo.filedownloader.message;

import android.os.Parcel;
import com.liulishuo.filedownloader.message.IFlowDirectly;
import com.liulishuo.filedownloader.message.MessageSnapshot;
import java.io.Serializable;

public abstract class LargeMessageSnapshot
extends MessageSnapshot {
    LargeMessageSnapshot(int n) {
        super(n);
        this.isLargeFile = true;
    }

    LargeMessageSnapshot(Parcel parcel) {
        super(parcel);
    }

    @Override
    public int getSmallSofarBytes() {
        if (this.getLargeSofarBytes() > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int)this.getLargeSofarBytes();
    }

    @Override
    public int getSmallTotalBytes() {
        if (this.getLargeTotalBytes() > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int)this.getLargeTotalBytes();
    }

    public static class CompletedFlowDirectlySnapshot
    extends CompletedSnapshot
    implements IFlowDirectly {
        CompletedFlowDirectlySnapshot(int n, boolean bl, long l) {
            super(n, bl, l);
        }

        CompletedFlowDirectlySnapshot(Parcel parcel) {
            super(parcel);
        }
    }

    public static class CompletedSnapshot
    extends LargeMessageSnapshot {
        private final boolean reusedDownloadedFile;
        private final long totalBytes;

        CompletedSnapshot(int n, boolean bl, long l) {
            super(n);
            this.reusedDownloadedFile = bl;
            this.totalBytes = l;
        }

        CompletedSnapshot(Parcel parcel) {
            super(parcel);
            boolean bl = parcel.readByte() != 0;
            this.reusedDownloadedFile = bl;
            this.totalBytes = parcel.readLong();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public long getLargeTotalBytes() {
            return this.totalBytes;
        }

        @Override
        public byte getStatus() {
            return -3;
        }

        @Override
        public boolean isReusedDownloadedFile() {
            return this.reusedDownloadedFile;
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeByte((byte)(this.reusedDownloadedFile ? 1 : 0));
            parcel.writeLong(this.totalBytes);
        }
    }

    public static class ConnectedMessageSnapshot
    extends LargeMessageSnapshot {
        private final String etag;
        private final String fileName;
        private final boolean resuming;
        private final long totalBytes;

        ConnectedMessageSnapshot(int n, boolean bl, long l, String string2, String string3) {
            super(n);
            this.resuming = bl;
            this.totalBytes = l;
            this.etag = string2;
            this.fileName = string3;
        }

        ConnectedMessageSnapshot(Parcel parcel) {
            super(parcel);
            boolean bl = parcel.readByte() != 0;
            this.resuming = bl;
            this.totalBytes = parcel.readLong();
            this.etag = parcel.readString();
            this.fileName = parcel.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public String getEtag() {
            return this.etag;
        }

        @Override
        public String getFileName() {
            return this.fileName;
        }

        @Override
        public long getLargeTotalBytes() {
            return this.totalBytes;
        }

        @Override
        public byte getStatus() {
            return 2;
        }

        @Override
        public boolean isResuming() {
            return this.resuming;
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeByte((byte)(this.resuming ? 1 : 0));
            parcel.writeLong(this.totalBytes);
            parcel.writeString(this.etag);
            parcel.writeString(this.fileName);
        }
    }

    public static class ErrorMessageSnapshot
    extends LargeMessageSnapshot {
        private final long sofarBytes;
        private final Throwable throwable;

        ErrorMessageSnapshot(int n, long l, Throwable throwable) {
            super(n);
            this.sofarBytes = l;
            this.throwable = throwable;
        }

        ErrorMessageSnapshot(Parcel parcel) {
            super(parcel);
            this.sofarBytes = parcel.readLong();
            this.throwable = (Throwable)parcel.readSerializable();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public long getLargeSofarBytes() {
            return this.sofarBytes;
        }

        @Override
        public byte getStatus() {
            return -1;
        }

        @Override
        public Throwable getThrowable() {
            return this.throwable;
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeLong(this.sofarBytes);
            parcel.writeSerializable((Serializable)this.throwable);
        }
    }

    public static class PausedSnapshot
    extends PendingMessageSnapshot {
        PausedSnapshot(int n, long l, long l2) {
            super(n, l, l2);
        }

        @Override
        public byte getStatus() {
            return -2;
        }
    }

    public static class PendingMessageSnapshot
    extends LargeMessageSnapshot {
        private final long sofarBytes;
        private final long totalBytes;

        PendingMessageSnapshot(int n, long l, long l2) {
            super(n);
            this.sofarBytes = l;
            this.totalBytes = l2;
        }

        PendingMessageSnapshot(Parcel parcel) {
            super(parcel);
            this.sofarBytes = parcel.readLong();
            this.totalBytes = parcel.readLong();
        }

        PendingMessageSnapshot(PendingMessageSnapshot pendingMessageSnapshot) {
            this(pendingMessageSnapshot.getId(), pendingMessageSnapshot.getLargeSofarBytes(), pendingMessageSnapshot.getLargeTotalBytes());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public long getLargeSofarBytes() {
            return this.sofarBytes;
        }

        @Override
        public long getLargeTotalBytes() {
            return this.totalBytes;
        }

        @Override
        public byte getStatus() {
            return 1;
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeLong(this.sofarBytes);
            parcel.writeLong(this.totalBytes);
        }
    }

    public static class ProgressMessageSnapshot
    extends LargeMessageSnapshot {
        private final long sofarBytes;

        ProgressMessageSnapshot(int n, long l) {
            super(n);
            this.sofarBytes = l;
        }

        ProgressMessageSnapshot(Parcel parcel) {
            super(parcel);
            this.sofarBytes = parcel.readLong();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public long getLargeSofarBytes() {
            return this.sofarBytes;
        }

        @Override
        public byte getStatus() {
            return 3;
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeLong(this.sofarBytes);
        }
    }

    public static class RetryMessageSnapshot
    extends ErrorMessageSnapshot {
        private final int retryingTimes;

        RetryMessageSnapshot(int n, long l, Throwable throwable, int n2) {
            super(n, l, throwable);
            this.retryingTimes = n2;
        }

        RetryMessageSnapshot(Parcel parcel) {
            super(parcel);
            this.retryingTimes = parcel.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public int getRetryingTimes() {
            return this.retryingTimes;
        }

        @Override
        public byte getStatus() {
            return 5;
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.retryingTimes);
        }
    }

    public static class WarnFlowDirectlySnapshot
    extends WarnMessageSnapshot
    implements IFlowDirectly {
        WarnFlowDirectlySnapshot(int n, long l, long l2) {
            super(n, l, l2);
        }

        WarnFlowDirectlySnapshot(Parcel parcel) {
            super(parcel);
        }
    }

    public static class WarnMessageSnapshot
    extends PendingMessageSnapshot
    implements MessageSnapshot.IWarnMessageSnapshot {
        WarnMessageSnapshot(int n, long l, long l2) {
            super(n, l, l2);
        }

        WarnMessageSnapshot(Parcel parcel) {
            super(parcel);
        }

        @Override
        public byte getStatus() {
            return -4;
        }

        @Override
        public MessageSnapshot turnToPending() {
            return new PendingMessageSnapshot(this);
        }
    }
}

