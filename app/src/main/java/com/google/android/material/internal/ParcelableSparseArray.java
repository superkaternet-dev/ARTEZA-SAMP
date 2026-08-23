/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.os.Parcelable$Creator
 *  android.util.SparseArray
 */
package com.google.android.material.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

public class ParcelableSparseArray
extends SparseArray<Parcelable>
implements Parcelable {
    public static final Parcelable.Creator<ParcelableSparseArray> CREATOR = new Parcelable.ClassLoaderCreator<ParcelableSparseArray>(){

        public ParcelableSparseArray createFromParcel(Parcel parcel) {
            return new ParcelableSparseArray(parcel, null);
        }

        public ParcelableSparseArray createFromParcel(Parcel parcel, ClassLoader classLoader) {
            return new ParcelableSparseArray(parcel, classLoader);
        }

        public ParcelableSparseArray[] newArray(int n) {
            return new ParcelableSparseArray[n];
        }
    };

    public ParcelableSparseArray() {
    }

    public ParcelableSparseArray(Parcel parcelableArray, ClassLoader classLoader) {
        int n = parcelableArray.readInt();
        int[] nArray = new int[n];
        parcelableArray.readIntArray(nArray);
        parcelableArray = parcelableArray.readParcelableArray(classLoader);
        for (int i = 0; i < n; ++i) {
            this.put(nArray[i], parcelableArray[i]);
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int n) {
        int n2 = this.size();
        int[] nArray = new int[n2];
        Parcelable[] parcelableArray = new Parcelable[n2];
        for (int i = 0; i < n2; ++i) {
            nArray[i] = this.keyAt(i);
            parcelableArray[i] = (Parcelable)this.valueAt(i);
        }
        parcel.writeInt(n2);
        parcel.writeIntArray(nArray);
        parcel.writeParcelableArray(parcelableArray, n);
    }
}

