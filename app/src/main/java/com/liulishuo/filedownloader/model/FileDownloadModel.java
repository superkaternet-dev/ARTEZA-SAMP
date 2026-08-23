/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.liulishuo.filedownloader.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FileDownloadModel
implements Parcelable {
    public static final String CONNECTION_COUNT = "connectionCount";
    public static final Parcelable.Creator<FileDownloadModel> CREATOR = new Parcelable.Creator<FileDownloadModel>(){

        public FileDownloadModel createFromParcel(Parcel parcel) {
            return new FileDownloadModel(parcel);
        }

        public FileDownloadModel[] newArray(int n) {
            return new FileDownloadModel[n];
        }
    };
    public static final int DEFAULT_CALLBACK_PROGRESS_TIMES = 100;
    public static final String ERR_MSG = "errMsg";
    public static final String ETAG = "etag";
    public static final String FILENAME = "filename";
    public static final String ID = "_id";
    public static final String PATH = "path";
    public static final String PATH_AS_DIRECTORY = "pathAsDirectory";
    public static final String SOFAR = "sofar";
    public static final String STATUS = "status";
    public static final String TOTAL = "total";
    public static final int TOTAL_VALUE_IN_CHUNKED_RESOURCE = -1;
    public static final String URL = "url";
    private int connectionCount;
    private String eTag;
    private String errMsg;
    private String filename;
    private int id;
    private boolean isLargeFile;
    private String path;
    private boolean pathAsDirectory;
    private final AtomicLong soFar;
    private final AtomicInteger status;
    private long total;
    private String url;

    public FileDownloadModel() {
        this.soFar = new AtomicLong();
        this.status = new AtomicInteger();
    }

    protected FileDownloadModel(Parcel parcel) {
        this.id = parcel.readInt();
        this.url = parcel.readString();
        this.path = parcel.readString();
        byte by = parcel.readByte();
        boolean bl = true;
        boolean bl2 = by != 0;
        this.pathAsDirectory = bl2;
        this.filename = parcel.readString();
        this.status = new AtomicInteger(parcel.readByte());
        this.soFar = new AtomicLong(parcel.readLong());
        this.total = parcel.readLong();
        this.errMsg = parcel.readString();
        this.eTag = parcel.readString();
        this.connectionCount = parcel.readInt();
        bl2 = parcel.readByte() != 0 ? bl : false;
        this.isLargeFile = bl2;
    }

    public void deleteTargetFile() {
        Object object = this.getTargetFilePath();
        if (object != null && ((File)(object = new File((String)object))).exists()) {
            ((File)object).delete();
        }
    }

    public void deleteTaskFiles() {
        this.deleteTempFile();
        this.deleteTargetFile();
    }

    public void deleteTempFile() {
        Object object = this.getTempFilePath();
        if (object != null && ((File)(object = new File((String)object))).exists()) {
            ((File)object).delete();
        }
    }

    public int describeContents() {
        return 0;
    }

    public int getConnectionCount() {
        return this.connectionCount;
    }

    public String getETag() {
        return this.eTag;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public String getFilename() {
        return this.filename;
    }

    public int getId() {
        return this.id;
    }

    public String getPath() {
        return this.path;
    }

    public long getSoFar() {
        return this.soFar.get();
    }

    public byte getStatus() {
        return (byte)this.status.get();
    }

    public String getTargetFilePath() {
        return FileDownloadUtils.getTargetFilePath(this.getPath(), this.isPathAsDirectory(), this.getFilename());
    }

    public String getTempFilePath() {
        if (this.getTargetFilePath() == null) {
            return null;
        }
        return FileDownloadUtils.getTempPath(this.getTargetFilePath());
    }

    public long getTotal() {
        return this.total;
    }

    public String getUrl() {
        return this.url;
    }

    public void increaseSoFar(long l) {
        this.soFar.addAndGet(l);
    }

    public boolean isChunked() {
        boolean bl = this.total == -1L;
        return bl;
    }

    public boolean isLargeFile() {
        return this.isLargeFile;
    }

    public boolean isPathAsDirectory() {
        return this.pathAsDirectory;
    }

    public void resetConnectionCount() {
        this.connectionCount = 1;
    }

    public void setConnectionCount(int n) {
        this.connectionCount = n;
    }

    public void setETag(String string2) {
        this.eTag = string2;
    }

    public void setErrMsg(String string2) {
        this.errMsg = string2;
    }

    public void setFilename(String string2) {
        this.filename = string2;
    }

    public void setId(int n) {
        this.id = n;
    }

    public void setPath(String string2, boolean bl) {
        this.path = string2;
        this.pathAsDirectory = bl;
    }

    public void setSoFar(long l) {
        this.soFar.set(l);
    }

    public void setStatus(byte by) {
        this.status.set(by);
    }

    public void setTotal(long l) {
        boolean bl = l > Integer.MAX_VALUE;
        this.isLargeFile = bl;
        this.total = l;
    }

    public void setUrl(String string2) {
        this.url = string2;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, Integer.valueOf(this.getId()));
        contentValues.put(URL, this.getUrl());
        contentValues.put(PATH, this.getPath());
        contentValues.put(STATUS, Byte.valueOf(this.getStatus()));
        contentValues.put(SOFAR, Long.valueOf(this.getSoFar()));
        contentValues.put(TOTAL, Long.valueOf(this.getTotal()));
        contentValues.put(ERR_MSG, this.getErrMsg());
        contentValues.put(ETAG, this.getETag());
        contentValues.put(CONNECTION_COUNT, Integer.valueOf(this.getConnectionCount()));
        contentValues.put(PATH_AS_DIRECTORY, Boolean.valueOf(this.isPathAsDirectory()));
        if (this.isPathAsDirectory() && this.getFilename() != null) {
            contentValues.put(FILENAME, this.getFilename());
        }
        return contentValues;
    }

    public String toString() {
        return FileDownloadUtils.formatString("id[%d], url[%s], path[%s], status[%d], sofar[%s], total[%d], etag[%s], %s", this.id, this.url, this.path, this.status.get(), this.soFar, this.total, this.eTag, super.toString());
    }

    public void writeToParcel(Parcel parcel, int n) {
        parcel.writeInt(this.id);
        parcel.writeString(this.url);
        parcel.writeString(this.path);
        parcel.writeByte((byte)(this.pathAsDirectory ? 1 : 0));
        parcel.writeString(this.filename);
        parcel.writeByte((byte)this.status.get());
        parcel.writeLong(this.soFar.get());
        parcel.writeLong(this.total);
        parcel.writeString(this.errMsg);
        parcel.writeString(this.eTag);
        parcel.writeInt(this.connectionCount);
        parcel.writeByte((byte)(this.isLargeFile ? 1 : 0));
    }
}

