/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.liulishuo.filedownloader.message;

import android.os.Parcel;
import android.os.Parcelable;
import com.liulishuo.filedownloader.message.IMessageSnapshot;
import com.liulishuo.filedownloader.message.LargeMessageSnapshot;
import com.liulishuo.filedownloader.message.SmallMessageSnapshot;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

public abstract class MessageSnapshot
implements IMessageSnapshot,
Parcelable {
    public static final Parcelable.Creator<MessageSnapshot> CREATOR = new Parcelable.Creator<MessageSnapshot>(){

        public MessageSnapshot createFromParcel(Parcel object) {
            byte by = object.readByte();
            boolean bl = true;
            if (by != 1) {
                bl = false;
            }
            by = object.readByte();
            switch (by) {
                default: {
                    object = null;
                    break;
                }
                case 6: {
                    object = new StartedMessageSnapshot((Parcel)object);
                    break;
                }
                case 5: {
                    if (bl) {
                        object = new LargeMessageSnapshot.RetryMessageSnapshot((Parcel)object);
                        break;
                    }
                    object = new SmallMessageSnapshot.RetryMessageSnapshot((Parcel)object);
                    break;
                }
                case 3: {
                    if (bl) {
                        object = new LargeMessageSnapshot.ProgressMessageSnapshot((Parcel)object);
                        break;
                    }
                    object = new SmallMessageSnapshot.ProgressMessageSnapshot((Parcel)object);
                    break;
                }
                case 2: {
                    if (bl) {
                        object = new LargeMessageSnapshot.ConnectedMessageSnapshot((Parcel)object);
                        break;
                    }
                    object = new SmallMessageSnapshot.ConnectedMessageSnapshot((Parcel)object);
                    break;
                }
                case 1: {
                    if (bl) {
                        object = new LargeMessageSnapshot.PendingMessageSnapshot((Parcel)object);
                        break;
                    }
                    object = new SmallMessageSnapshot.PendingMessageSnapshot((Parcel)object);
                    break;
                }
                case -1: {
                    if (bl) {
                        object = new LargeMessageSnapshot.ErrorMessageSnapshot((Parcel)object);
                        break;
                    }
                    object = new SmallMessageSnapshot.ErrorMessageSnapshot((Parcel)object);
                    break;
                }
                case -3: {
                    if (bl) {
                        object = new LargeMessageSnapshot.CompletedSnapshot((Parcel)object);
                        break;
                    }
                    object = new SmallMessageSnapshot.CompletedSnapshot((Parcel)object);
                    break;
                }
                case -4: {
                    object = bl ? new LargeMessageSnapshot.WarnMessageSnapshot((Parcel)object) : new SmallMessageSnapshot.WarnMessageSnapshot((Parcel)object);
                }
            }
            if (object != null) {
                ((MessageSnapshot)object).isLargeFile = bl;
                return object;
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Can't restore the snapshot because unknown status: ");
            ((StringBuilder)object).append(by);
            throw new IllegalStateException(((StringBuilder)object).toString());
        }

        public MessageSnapshot[] newArray(int n) {
            return new MessageSnapshot[n];
        }
    };
    private final int id;
    protected boolean isLargeFile;

    MessageSnapshot(int n) {
        this.id = n;
    }

    MessageSnapshot(Parcel parcel) {
        this.id = parcel.readInt();
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public String getEtag() {
        throw new NoFieldException("getEtag", this);
    }

    @Override
    public String getFileName() {
        throw new NoFieldException("getFileName", this);
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public long getLargeSofarBytes() {
        throw new NoFieldException("getLargeSofarBytes", this);
    }

    @Override
    public long getLargeTotalBytes() {
        throw new NoFieldException("getLargeTotalBytes", this);
    }

    @Override
    public int getRetryingTimes() {
        throw new NoFieldException("getRetryingTimes", this);
    }

    @Override
    public int getSmallSofarBytes() {
        throw new NoFieldException("getSmallSofarBytes", this);
    }

    @Override
    public int getSmallTotalBytes() {
        throw new NoFieldException("getSmallTotalBytes", this);
    }

    @Override
    public Throwable getThrowable() {
        throw new NoFieldException("getThrowable", this);
    }

    @Override
    public boolean isLargeFile() {
        return this.isLargeFile;
    }

    @Override
    public boolean isResuming() {
        throw new NoFieldException("isResuming", this);
    }

    @Override
    public boolean isReusedDownloadedFile() {
        throw new NoFieldException("isReusedDownloadedFile", this);
    }

    public void writeToParcel(Parcel parcel, int n) {
        parcel.writeByte((byte)(this.isLargeFile ? 1 : 0));
        parcel.writeByte(this.getStatus());
        parcel.writeInt(this.id);
    }

    public static interface IWarnMessageSnapshot {
        public MessageSnapshot turnToPending();
    }

    public static class NoFieldException
    extends IllegalStateException {
        NoFieldException(String string2, MessageSnapshot messageSnapshot) {
            super(FileDownloadUtils.formatString("There isn't a field for '%s' in this message %d %d %s", string2, messageSnapshot.getId(), messageSnapshot.getStatus(), messageSnapshot.getClass().getName()));
        }
    }

    public static class StartedMessageSnapshot
    extends MessageSnapshot {
        StartedMessageSnapshot(int n) {
            super(n);
        }

        StartedMessageSnapshot(Parcel parcel) {
            super(parcel);
        }

        @Override
        public byte getStatus() {
            return 6;
        }
    }
}

