/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package androidx.fragment.app;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.fragment.app.Fragment;

final class FragmentState
implements Parcelable {
    public static final Parcelable.Creator<FragmentState> CREATOR = new Parcelable.Creator<FragmentState>(){

        public FragmentState createFromParcel(Parcel parcel) {
            return new FragmentState(parcel);
        }

        public FragmentState[] newArray(int n) {
            return new FragmentState[n];
        }
    };
    final Bundle mArguments;
    final String mClassName;
    final int mContainerId;
    final boolean mDetached;
    final int mFragmentId;
    final boolean mFromLayout;
    final boolean mHidden;
    final int mMaxLifecycleState;
    final boolean mRemoving;
    final boolean mRetainInstance;
    Bundle mSavedFragmentState;
    final String mTag;
    final String mWho;

    FragmentState(Parcel parcel) {
        this.mClassName = parcel.readString();
        this.mWho = parcel.readString();
        int n = parcel.readInt();
        boolean bl = true;
        boolean bl2 = n != 0;
        this.mFromLayout = bl2;
        this.mFragmentId = parcel.readInt();
        this.mContainerId = parcel.readInt();
        this.mTag = parcel.readString();
        bl2 = parcel.readInt() != 0;
        this.mRetainInstance = bl2;
        bl2 = parcel.readInt() != 0;
        this.mRemoving = bl2;
        bl2 = parcel.readInt() != 0;
        this.mDetached = bl2;
        this.mArguments = parcel.readBundle();
        bl2 = parcel.readInt() != 0 ? bl : false;
        this.mHidden = bl2;
        this.mSavedFragmentState = parcel.readBundle();
        this.mMaxLifecycleState = parcel.readInt();
    }

    FragmentState(Fragment fragment) {
        this.mClassName = fragment.getClass().getName();
        this.mWho = fragment.mWho;
        this.mFromLayout = fragment.mFromLayout;
        this.mFragmentId = fragment.mFragmentId;
        this.mContainerId = fragment.mContainerId;
        this.mTag = fragment.mTag;
        this.mRetainInstance = fragment.mRetainInstance;
        this.mRemoving = fragment.mRemoving;
        this.mDetached = fragment.mDetached;
        this.mArguments = fragment.mArguments;
        this.mHidden = fragment.mHidden;
        this.mMaxLifecycleState = fragment.mMaxState.ordinal();
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        String string2;
        StringBuilder stringBuilder = new StringBuilder(128);
        stringBuilder.append("FragmentState{");
        stringBuilder.append(this.mClassName);
        stringBuilder.append(" (");
        stringBuilder.append(this.mWho);
        stringBuilder.append(")}:");
        if (this.mFromLayout) {
            stringBuilder.append(" fromLayout");
        }
        if (this.mContainerId != 0) {
            stringBuilder.append(" id=0x");
            stringBuilder.append(Integer.toHexString(this.mContainerId));
        }
        if ((string2 = this.mTag) != null && !string2.isEmpty()) {
            stringBuilder.append(" tag=");
            stringBuilder.append(this.mTag);
        }
        if (this.mRetainInstance) {
            stringBuilder.append(" retainInstance");
        }
        if (this.mRemoving) {
            stringBuilder.append(" removing");
        }
        if (this.mDetached) {
            stringBuilder.append(" detached");
        }
        if (this.mHidden) {
            stringBuilder.append(" hidden");
        }
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int n) {
        parcel.writeString(this.mClassName);
        parcel.writeString(this.mWho);
        parcel.writeInt(this.mFromLayout ? 1 : 0);
        parcel.writeInt(this.mFragmentId);
        parcel.writeInt(this.mContainerId);
        parcel.writeString(this.mTag);
        parcel.writeInt(this.mRetainInstance ? 1 : 0);
        parcel.writeInt(this.mRemoving ? 1 : 0);
        parcel.writeInt(this.mDetached ? 1 : 0);
        parcel.writeBundle(this.mArguments);
        parcel.writeInt(this.mHidden ? 1 : 0);
        parcel.writeBundle(this.mSavedFragmentState);
        parcel.writeInt(this.mMaxLifecycleState);
    }
}

