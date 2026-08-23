/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package androidx.activity.result;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

public final class ActivityResult
implements Parcelable {
    public static final Parcelable.Creator<ActivityResult> CREATOR = new Parcelable.Creator<ActivityResult>(){

        public ActivityResult createFromParcel(Parcel parcel) {
            return new ActivityResult(parcel);
        }

        public ActivityResult[] newArray(int n) {
            return new ActivityResult[n];
        }
    };
    private final Intent mData;
    private final int mResultCode;

    public ActivityResult(int n, Intent intent) {
        this.mResultCode = n;
        this.mData = intent;
    }

    ActivityResult(Parcel object) {
        this.mResultCode = object.readInt();
        object = object.readInt() == 0 ? null : (Intent)Intent.CREATOR.createFromParcel(object);
        this.mData = object;
    }

    public static String resultCodeToString(int n) {
        switch (n) {
            default: {
                return String.valueOf(n);
            }
            case 0: {
                return "RESULT_CANCELED";
            }
            case -1: 
        }
        return "RESULT_OK";
    }

    public int describeContents() {
        return 0;
    }

    public Intent getData() {
        return this.mData;
    }

    public int getResultCode() {
        return this.mResultCode;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ActivityResult{resultCode=");
        stringBuilder.append(ActivityResult.resultCodeToString(this.mResultCode));
        stringBuilder.append(", data=");
        stringBuilder.append(this.mData);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int n) {
        parcel.writeInt(this.mResultCode);
        int n2 = this.mData == null ? 0 : 1;
        parcel.writeInt(n2);
        Intent intent = this.mData;
        if (intent != null) {
            intent.writeToParcel(parcel, n);
        }
    }
}

