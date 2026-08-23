/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.os.Parcelable$Creator
 */
package com.google.android.material.stateful;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.collection.SimpleArrayMap;
import androidx.customview.view.AbsSavedState;

public class ExtendableSavedState
extends AbsSavedState {
    public static final Parcelable.Creator<ExtendableSavedState> CREATOR = new Parcelable.ClassLoaderCreator<ExtendableSavedState>(){

        public ExtendableSavedState createFromParcel(Parcel parcel) {
            return new ExtendableSavedState(parcel, null);
        }

        public ExtendableSavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
            return new ExtendableSavedState(parcel, classLoader);
        }

        public ExtendableSavedState[] newArray(int n) {
            return new ExtendableSavedState[n];
        }
    };
    public final SimpleArrayMap<String, Bundle> extendableStates;

    private ExtendableSavedState(Parcel parcel, ClassLoader objectArray) {
        super(parcel, (ClassLoader)objectArray);
        int n = parcel.readInt();
        String[] stringArray = new String[n];
        parcel.readStringArray(stringArray);
        objectArray = new Bundle[n];
        parcel.readTypedArray(objectArray, Bundle.CREATOR);
        this.extendableStates = new SimpleArrayMap(n);
        for (int i = 0; i < n; ++i) {
            this.extendableStates.put(stringArray[i], (Bundle)objectArray[i]);
        }
    }

    public ExtendableSavedState(Parcelable parcelable) {
        super(parcelable);
        this.extendableStates = new SimpleArrayMap();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ExtendableSavedState{");
        stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
        stringBuilder.append(" states=");
        stringBuilder.append(this.extendableStates);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int n) {
        super.writeToParcel(parcel, n);
        int n2 = this.extendableStates.size();
        parcel.writeInt(n2);
        String[] stringArray = new String[n2];
        Bundle[] bundleArray = new Bundle[n2];
        for (n = 0; n < n2; ++n) {
            stringArray[n] = this.extendableStates.keyAt(n);
            bundleArray[n] = this.extendableStates.valueAt(n);
        }
        parcel.writeStringArray(stringArray);
        parcel.writeTypedArray((Parcelable[])bundleArray, 0);
    }
}

