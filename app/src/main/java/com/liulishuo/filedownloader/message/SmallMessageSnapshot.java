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

public abstract class SmallMessageSnapshot
extends MessageSnapshot {
    SmallMessageSnapshot(int n) {
        super(n);
        this.isLargeFile = false;
    }

    SmallMessageSnapshot(Parcel parcel) {
        super(parcel);
    }

    @Override
    public long getLargeSofarBytes() {
        return this.getSmallSofarBytes();
    }

    @Override
    public long getLargeTotalBytes() {
        return this.getSmallTotalBytes();
    }

    public static class CompletedFlowDirectlySnapshot
    extends CompletedSnapshot
    implements IFlowDirectly {
        CompletedFlowDirectlySnapshot(int n, boolean bl, int n2) {
            super(n, bl, n2);
        }

        CompletedFlowDirectlySnapshot(Parcel parcel) {
            super(parcel);
        }
    }

    public static class CompletedSnapshot
    extends SmallMessageSnapshot {
        private final boolean reusedDownloadedFile;
        private final int totalBytes;

        CompletedSnapshot(int n, boolean bl, int n2) {
            super(n);
            this.reusedDownloadedFile = bl;
            this.totalBytes = n2;
        }

        CompletedSnapshot(Parcel parcel) {
            super(parcel);
            boolean bl = parcel.readByte() != 0;
            this.reusedDownloadedFile = bl;
            this.totalBytes = parcel.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public int getSmallTotalBytes() {
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
            parcel.writeInt(this.totalBytes);
        }
    }

    public static class ConnectedMessageSnapshot
    extends SmallMessageSnapshot {
        private final String etag;
        private final String fileName;
        private final boolean resuming;
        private final int totalBytes;

        ConnectedMessageSnapshot(int n, boolean bl, int n2, String string2, String string3) {
            super(n);
            this.resuming = bl;
            this.totalBytes = n2;
            this.etag = string2;
            this.fileName = string3;
        }

        ConnectedMessageSnapshot(Parcel parcel) {
            super(parcel);
            boolean bl = parcel.readByte() != 0;
            this.resuming = bl;
            this.totalBytes = parcel.readInt();
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
        public int getSmallTotalBytes() {
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
            parcel.writeInt(this.totalBytes);
            parcel.writeString(this.etag);
            parcel.writeString(this.fileName);
        }
    }

    public static class ErrorMessageSnapshot
    extends SmallMessageSnapshot {
        private final int sofarBytes;
        private final Throwable throwable;

        ErrorMessageSnapshot(int n, int n2, Throwable throwable) {
            super(n);
            this.sofarBytes = n2;
            this.throwable = throwable;
        }

        ErrorMessageSnapshot(Parcel parcel) {
            super(parcel);
            this.sofarBytes = parcel.readInt();
            this.throwable = (Throwable)parcel.readSerializable();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public int getSmallSofarBytes() {
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
            parcel.writeInt(this.sofarBytes);
            parcel.writeSerializable((Serializable)this.throwable);
        }
    }

    public static class PausedSnapshot
    extends PendingMessageSnapshot {
        PausedSnapshot(int n, int n2, int n3) {
            super(n, n2, n3);
        }

        @Override
        public byte getStatus() {
            return -2;
        }
    }

    public static class PendingMessageSnapshot
    extends SmallMessageSnapshot {
        private final int sofarBytes;
        private final int totalBytes;

        PendingMessageSnapshot(int n, int n2, int n3) {
            super(n);
            this.sofarBytes = n2;
            this.totalBytes = n3;
        }

        PendingMessageSnapshot(Parcel parcel) {
            super(parcel);
            this.sofarBytes = parcel.readInt();
            this.totalBytes = parcel.readInt();
        }

        PendingMessageSnapshot(PendingMessageSnapshot pendingMessageSnapshot) {
            this(pendingMessageSnapshot.getId(), pendingMessageSnapshot.getSmallSofarBytes(), pendingMessageSnapshot.getSmallTotalBytes());
        }

        @Override
        public int getSmallSofarBytes() {
            return this.sofarBytes;
        }

        @Override
        public int getSmallTotalBytes() {
            return this.totalBytes;
        }

        @Override
        public byte getStatus() {
            return 1;
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.sofarBytes);
            parcel.writeInt(this.totalBytes);
        }
    }

    public static class ProgressMessageSnapshot
    extends SmallMessageSnapshot {
        private final int sofarBytes;

        ProgressMessageSnapshot(int n, int n2) {
            super(n);
            this.sofarBytes = n2;
        }

        ProgressMessageSnapshot(Parcel parcel) {
            super(parcel);
            this.sofarBytes = parcel.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public int getSmallSofarBytes() {
            return this.sofarBytes;
        }

        @Override
        public byte getStatus() {
            return 3;
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.sofarBytes);
        }
    }

    public static class RetryMessageSnapshot
    extends ErrorMessageSnapshot {
        private final int retryingTimes;

        RetryMessageSnapshot(int n, int n2, Throwable throwable, int n3) {
            super(n, n2, throwable);
            this.retryingTimes = n3;
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
        WarnFlowDirectlySnapshot(int n, int n2, int n3) {
            super(n, n2, n3);
        }

        WarnFlowDirectlySnapshot(Parcel parcel) {
            super(parcel);
        }
    }

    public static class WarnMessageSnapshot
    extends PendingMessageSnapshot
    implements MessageSnapshot.IWarnMessageSnapshot {
        WarnMessageSnapshot(int n, int n2, int n3) {
            super(n, n2, n3);
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

