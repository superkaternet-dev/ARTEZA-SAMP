/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.PendingIntent
 *  android.content.Intent
 *  android.content.IntentSender
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package androidx.activity.result;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Parcel;
import android.os.Parcelable;

public final class IntentSenderRequest
implements Parcelable {
    public static final Parcelable.Creator<IntentSenderRequest> CREATOR = new Parcelable.Creator<IntentSenderRequest>(){

        public IntentSenderRequest createFromParcel(Parcel parcel) {
            return new IntentSenderRequest(parcel);
        }

        public IntentSenderRequest[] newArray(int n) {
            return new IntentSenderRequest[n];
        }
    };
    private final Intent mFillInIntent;
    private final int mFlagsMask;
    private final int mFlagsValues;
    private final IntentSender mIntentSender;

    IntentSenderRequest(IntentSender intentSender, Intent intent, int n, int n2) {
        this.mIntentSender = intentSender;
        this.mFillInIntent = intent;
        this.mFlagsMask = n;
        this.mFlagsValues = n2;
    }

    IntentSenderRequest(Parcel parcel) {
        this.mIntentSender = (IntentSender)parcel.readParcelable(IntentSender.class.getClassLoader());
        this.mFillInIntent = (Intent)parcel.readParcelable(Intent.class.getClassLoader());
        this.mFlagsMask = parcel.readInt();
        this.mFlagsValues = parcel.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public Intent getFillInIntent() {
        return this.mFillInIntent;
    }

    public int getFlagsMask() {
        return this.mFlagsMask;
    }

    public int getFlagsValues() {
        return this.mFlagsValues;
    }

    public IntentSender getIntentSender() {
        return this.mIntentSender;
    }

    public void writeToParcel(Parcel parcel, int n) {
        parcel.writeParcelable((Parcelable)this.mIntentSender, n);
        parcel.writeParcelable((Parcelable)this.mFillInIntent, n);
        parcel.writeInt(this.mFlagsMask);
        parcel.writeInt(this.mFlagsValues);
    }

    public static final class Builder {
        private Intent mFillInIntent;
        private int mFlagsMask;
        private int mFlagsValues;
        private IntentSender mIntentSender;

        public Builder(PendingIntent pendingIntent) {
            this(pendingIntent.getIntentSender());
        }

        public Builder(IntentSender intentSender) {
            this.mIntentSender = intentSender;
        }

        public IntentSenderRequest build() {
            return new IntentSenderRequest(this.mIntentSender, this.mFillInIntent, this.mFlagsMask, this.mFlagsValues);
        }

        public Builder setFillInIntent(Intent intent) {
            this.mFillInIntent = intent;
            return this;
        }

        public Builder setFlags(int n, int n2) {
            this.mFlagsValues = n;
            this.mFlagsMask = n2;
            return this;
        }
    }
}

