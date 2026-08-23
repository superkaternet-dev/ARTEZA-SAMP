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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileDownloadHeader
implements Parcelable {
    public static final Parcelable.Creator<FileDownloadHeader> CREATOR = new Parcelable.Creator<FileDownloadHeader>(){

        public FileDownloadHeader createFromParcel(Parcel parcel) {
            return new FileDownloadHeader(parcel);
        }

        public FileDownloadHeader[] newArray(int n) {
            return new FileDownloadHeader[n];
        }
    };
    private HashMap<String, List<String>> mHeaderMap;

    public FileDownloadHeader() {
    }

    protected FileDownloadHeader(Parcel parcel) {
        this.mHeaderMap = parcel.readHashMap(String.class.getClassLoader());
    }

    public void add(String stringArray) {
        stringArray = stringArray.split(":");
        this.add(stringArray[0].trim(), stringArray[1].trim());
    }

    public void add(String string2, String string3) {
        if (string2 != null) {
            if (!string2.isEmpty()) {
                if (string3 != null) {
                    List<String> list;
                    if (this.mHeaderMap == null) {
                        this.mHeaderMap = new HashMap();
                    }
                    List<String> list2 = list = this.mHeaderMap.get(string2);
                    if (list == null) {
                        list2 = new ArrayList<String>();
                        this.mHeaderMap.put(string2, list2);
                    }
                    if (!list2.contains(string3)) {
                        list2.add(string3);
                    }
                    return;
                }
                throw new NullPointerException("value == null");
            }
            throw new IllegalArgumentException("name is empty");
        }
        throw new NullPointerException("name == null");
    }

    public int describeContents() {
        return 0;
    }

    public HashMap<String, List<String>> getHeaders() {
        return this.mHeaderMap;
    }

    public void removeAll(String string2) {
        HashMap<String, List<String>> hashMap = this.mHeaderMap;
        if (hashMap == null) {
            return;
        }
        hashMap.remove(string2);
    }

    public String toString() {
        return this.mHeaderMap.toString();
    }

    public void writeToParcel(Parcel parcel, int n) {
        parcel.writeMap(this.mHeaderMap);
    }
}

