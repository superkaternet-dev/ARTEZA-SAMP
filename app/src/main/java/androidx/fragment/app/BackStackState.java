/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.text.TextUtils
 *  android.util.Log
 */
package androidx.fragment.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import androidx.fragment.app.BackStackRecord;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import java.util.ArrayList;

final class BackStackState
implements Parcelable {
    public static final Parcelable.Creator<BackStackState> CREATOR = new Parcelable.Creator<BackStackState>(){

        public BackStackState createFromParcel(Parcel parcel) {
            return new BackStackState(parcel);
        }

        public BackStackState[] newArray(int n) {
            return new BackStackState[n];
        }
    };
    private static final String TAG = "FragmentManager";
    final int mBreadCrumbShortTitleRes;
    final CharSequence mBreadCrumbShortTitleText;
    final int mBreadCrumbTitleRes;
    final CharSequence mBreadCrumbTitleText;
    final int[] mCurrentMaxLifecycleStates;
    final ArrayList<String> mFragmentWhos;
    final int mIndex;
    final String mName;
    final int[] mOldMaxLifecycleStates;
    final int[] mOps;
    final boolean mReorderingAllowed;
    final ArrayList<String> mSharedElementSourceNames;
    final ArrayList<String> mSharedElementTargetNames;
    final int mTransition;

    public BackStackState(Parcel parcel) {
        this.mOps = parcel.createIntArray();
        this.mFragmentWhos = parcel.createStringArrayList();
        this.mOldMaxLifecycleStates = parcel.createIntArray();
        this.mCurrentMaxLifecycleStates = parcel.createIntArray();
        this.mTransition = parcel.readInt();
        this.mName = parcel.readString();
        this.mIndex = parcel.readInt();
        this.mBreadCrumbTitleRes = parcel.readInt();
        this.mBreadCrumbTitleText = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.mBreadCrumbShortTitleRes = parcel.readInt();
        this.mBreadCrumbShortTitleText = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.mSharedElementSourceNames = parcel.createStringArrayList();
        this.mSharedElementTargetNames = parcel.createStringArrayList();
        boolean bl = parcel.readInt() != 0;
        this.mReorderingAllowed = bl;
    }

    public BackStackState(BackStackRecord object) {
        int n = ((BackStackRecord)object).mOps.size();
        this.mOps = new int[n * 5];
        if (((BackStackRecord)object).mAddToBackStack) {
            this.mFragmentWhos = new ArrayList(n);
            this.mOldMaxLifecycleStates = new int[n];
            this.mCurrentMaxLifecycleStates = new int[n];
            int n2 = 0;
            int n3 = 0;
            while (n3 < n) {
                FragmentTransaction.Op op = (FragmentTransaction.Op)((BackStackRecord)object).mOps.get(n3);
                Object object2 = this.mOps;
                int n4 = n2 + 1;
                object2[n2] = op.mCmd;
                ArrayList<String> arrayList = this.mFragmentWhos;
                object2 = op.mFragment != null ? (Object)op.mFragment.mWho : null;
                arrayList.add((String)object2);
                object2 = this.mOps;
                n2 = n4 + 1;
                object2[n4] = op.mEnterAnim;
                object2 = this.mOps;
                n4 = n2 + 1;
                object2[n2] = op.mExitAnim;
                object2 = this.mOps;
                n2 = n4 + 1;
                object2[n4] = op.mPopEnterAnim;
                this.mOps[n2] = op.mPopExitAnim;
                this.mOldMaxLifecycleStates[n3] = op.mOldMaxState.ordinal();
                this.mCurrentMaxLifecycleStates[n3] = op.mCurrentMaxState.ordinal();
                ++n3;
                ++n2;
            }
            this.mTransition = ((BackStackRecord)object).mTransition;
            this.mName = ((BackStackRecord)object).mName;
            this.mIndex = ((BackStackRecord)object).mIndex;
            this.mBreadCrumbTitleRes = ((BackStackRecord)object).mBreadCrumbTitleRes;
            this.mBreadCrumbTitleText = ((BackStackRecord)object).mBreadCrumbTitleText;
            this.mBreadCrumbShortTitleRes = ((BackStackRecord)object).mBreadCrumbShortTitleRes;
            this.mBreadCrumbShortTitleText = ((BackStackRecord)object).mBreadCrumbShortTitleText;
            this.mSharedElementSourceNames = ((BackStackRecord)object).mSharedElementSourceNames;
            this.mSharedElementTargetNames = ((BackStackRecord)object).mSharedElementTargetNames;
            this.mReorderingAllowed = ((BackStackRecord)object).mReorderingAllowed;
            return;
        }
        object = new IllegalStateException("Not on back stack");
        throw object;
    }

    public int describeContents() {
        return 0;
    }

    public BackStackRecord instantiate(FragmentManager fragmentManager) {
        BackStackRecord backStackRecord = new BackStackRecord(fragmentManager);
        int n = 0;
        for (int i = 0; i < this.mOps.length; ++i) {
            FragmentTransaction.Op op = new FragmentTransaction.Op();
            Object object = this.mOps;
            int n2 = i + 1;
            op.mCmd = object[i];
            if (FragmentManager.isLoggingEnabled(2)) {
                object = new StringBuilder();
                object.append("Instantiate ");
                object.append(backStackRecord);
                object.append(" op #");
                object.append(n);
                object.append(" base fragment #");
                object.append(this.mOps[n2]);
                Log.v((String)TAG, (String)object.toString());
            }
            op.mFragment = (object = (Object)this.mFragmentWhos.get(n)) != null ? fragmentManager.findActiveFragment((String)object) : null;
            op.mOldMaxState = Lifecycle.State.values()[this.mOldMaxLifecycleStates[n]];
            op.mCurrentMaxState = Lifecycle.State.values()[this.mCurrentMaxLifecycleStates[n]];
            object = this.mOps;
            i = n2 + 1;
            op.mEnterAnim = (int)object[n2];
            object = this.mOps;
            n2 = i + 1;
            op.mExitAnim = object[i];
            object = this.mOps;
            i = n2 + 1;
            op.mPopEnterAnim = object[n2];
            op.mPopExitAnim = this.mOps[i];
            backStackRecord.mEnterAnim = op.mEnterAnim;
            backStackRecord.mExitAnim = op.mExitAnim;
            backStackRecord.mPopEnterAnim = op.mPopEnterAnim;
            backStackRecord.mPopExitAnim = op.mPopExitAnim;
            backStackRecord.addOp(op);
            ++n;
        }
        backStackRecord.mTransition = this.mTransition;
        backStackRecord.mName = this.mName;
        backStackRecord.mIndex = this.mIndex;
        backStackRecord.mAddToBackStack = true;
        backStackRecord.mBreadCrumbTitleRes = this.mBreadCrumbTitleRes;
        backStackRecord.mBreadCrumbTitleText = this.mBreadCrumbTitleText;
        backStackRecord.mBreadCrumbShortTitleRes = this.mBreadCrumbShortTitleRes;
        backStackRecord.mBreadCrumbShortTitleText = this.mBreadCrumbShortTitleText;
        backStackRecord.mSharedElementSourceNames = this.mSharedElementSourceNames;
        backStackRecord.mSharedElementTargetNames = this.mSharedElementTargetNames;
        backStackRecord.mReorderingAllowed = this.mReorderingAllowed;
        backStackRecord.bumpBackStackNesting(1);
        return backStackRecord;
    }

    public void writeToParcel(Parcel parcel, int n) {
        parcel.writeIntArray(this.mOps);
        parcel.writeStringList(this.mFragmentWhos);
        parcel.writeIntArray(this.mOldMaxLifecycleStates);
        parcel.writeIntArray(this.mCurrentMaxLifecycleStates);
        parcel.writeInt(this.mTransition);
        parcel.writeString(this.mName);
        parcel.writeInt(this.mIndex);
        parcel.writeInt(this.mBreadCrumbTitleRes);
        TextUtils.writeToParcel((CharSequence)this.mBreadCrumbTitleText, (Parcel)parcel, (int)0);
        parcel.writeInt(this.mBreadCrumbShortTitleRes);
        TextUtils.writeToParcel((CharSequence)this.mBreadCrumbShortTitleText, (Parcel)parcel, (int)0);
        parcel.writeStringList(this.mSharedElementSourceNames);
        parcel.writeStringList(this.mSharedElementTargetNames);
        parcel.writeInt(this.mReorderingAllowed ? 1 : 0);
    }
}

