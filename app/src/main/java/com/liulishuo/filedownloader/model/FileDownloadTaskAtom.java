/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.liulishuo.filedownloader.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

public class FileDownloadTaskAtom
implements Parcelable {
    public static final Parcelable.Creator<FileDownloadTaskAtom> CREATOR = new Parcelable.Creator<FileDownloadTaskAtom>(){

        public FileDownloadTaskAtom createFromParcel(Parcel parcel) {
            return new FileDownloadTaskAtom(parcel);
        }

        public FileDownloadTaskAtom[] newArray(int n) {
            return new FileDownloadTaskAtom[n];
        }
    };
    private int id;
    private String path;
    private long totalBytes;
    private String url;

    protected FileDownloadTaskAtom(Parcel parcel) {
        this.url = parcel.readString();
        this.path = parcel.readString();
        this.totalBytes = parcel.readLong();
    }

    public FileDownloadTaskAtom(String string2, String string3, long l) {
        this.setUrl(string2);
        this.setPath(string3);
        this.setTotalBytes(l);
    }

    public int describeContents() {
        return 0;
    }

    public int getId() {
        int n = this.id;
        if (n != 0) {
            return n;
        }
        this.id = n = FileDownloadUtils.generateId(this.getUrl(), this.getPath());
        return n;
    }

    public String getPath() {
        return this.path;
    }

    public long getTotalBytes() {
        return this.totalBytes;
    }

    public String getUrl() {
        return this.url;
    }

    public void setPath(String string2) {
        this.path = string2;
    }

    public void setTotalBytes(long l) {
        this.totalBytes = l;
    }

    public void setUrl(String string2) {
        this.url = string2;
    }

    public void writeToParcel(Parcel parcel, int n) {
        parcel.writeString(this.url);
        parcel.writeString(this.path);
        parcel.writeLong(this.totalBytes);
    }
}

