/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.view.View$BaseSavedState
 */
package com.smarteist.autoimageslider.IndicatorView.draw.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

public class PositionSavedState
extends View.BaseSavedState {
    public static final Parcelable.Creator<PositionSavedState> CREATOR = new Parcelable.Creator<PositionSavedState>(){

        public PositionSavedState createFromParcel(Parcel parcel) {
            return new PositionSavedState(parcel);
        }

        public PositionSavedState[] newArray(int n) {
            return new PositionSavedState[n];
        }
    };
    private int lastSelectedPosition;
    private int selectedPosition;
    private int selectingPosition;

    private PositionSavedState(Parcel parcel) {
        super(parcel);
        this.selectedPosition = parcel.readInt();
        this.selectingPosition = parcel.readInt();
        this.lastSelectedPosition = parcel.readInt();
    }

    public PositionSavedState(Parcelable parcelable) {
        super(parcelable);
    }

    public int getLastSelectedPosition() {
        return this.lastSelectedPosition;
    }

    public int getSelectedPosition() {
        return this.selectedPosition;
    }

    public int getSelectingPosition() {
        return this.selectingPosition;
    }

    public void setLastSelectedPosition(int n) {
        this.lastSelectedPosition = n;
    }

    public void setSelectedPosition(int n) {
        this.selectedPosition = n;
    }

    public void setSelectingPosition(int n) {
        this.selectingPosition = n;
    }

    public void writeToParcel(Parcel parcel, int n) {
        super.writeToParcel(parcel, n);
        parcel.writeInt(this.selectedPosition);
        parcel.writeInt(this.selectingPosition);
        parcel.writeInt(this.lastSelectedPosition);
    }
}

