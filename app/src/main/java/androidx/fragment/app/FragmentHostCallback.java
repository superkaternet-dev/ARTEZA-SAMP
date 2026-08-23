/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentSender
 *  android.content.IntentSender$SendIntentException
 *  android.os.Bundle
 *  android.os.Handler
 *  android.view.LayoutInflater
 *  android.view.View
 */
package androidx.fragment.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Preconditions;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentManagerImpl;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public abstract class FragmentHostCallback<E>
extends FragmentContainer {
    private final Activity mActivity;
    private final Context mContext;
    final FragmentManager mFragmentManager;
    private final Handler mHandler;
    private final int mWindowAnimations;

    FragmentHostCallback(Activity activity, Context context, Handler handler, int n) {
        this.mFragmentManager = new FragmentManagerImpl();
        this.mActivity = activity;
        this.mContext = Preconditions.checkNotNull(context, "context == null");
        this.mHandler = Preconditions.checkNotNull(handler, "handler == null");
        this.mWindowAnimations = n;
    }

    public FragmentHostCallback(Context context, Handler handler, int n) {
        Activity activity = context instanceof Activity ? (Activity)context : null;
        this(activity, context, handler, n);
    }

    FragmentHostCallback(FragmentActivity fragmentActivity) {
        this(fragmentActivity, (Context)fragmentActivity, new Handler(), 0);
    }

    Activity getActivity() {
        return this.mActivity;
    }

    Context getContext() {
        return this.mContext;
    }

    Handler getHandler() {
        return this.mHandler;
    }

    public void onDump(String string2, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] stringArray) {
    }

    @Override
    public View onFindViewById(int n) {
        return null;
    }

    public abstract E onGetHost();

    public LayoutInflater onGetLayoutInflater() {
        return LayoutInflater.from((Context)this.mContext);
    }

    public int onGetWindowAnimations() {
        return this.mWindowAnimations;
    }

    @Override
    public boolean onHasView() {
        return true;
    }

    public boolean onHasWindowAnimations() {
        return true;
    }

    @Deprecated
    public void onRequestPermissionsFromFragment(Fragment fragment, String[] stringArray, int n) {
    }

    public boolean onShouldSaveFragmentState(Fragment fragment) {
        return true;
    }

    public boolean onShouldShowRequestPermissionRationale(String string2) {
        return false;
    }

    public void onStartActivityFromFragment(Fragment fragment, Intent intent, int n) {
        this.onStartActivityFromFragment(fragment, intent, n, null);
    }

    public void onStartActivityFromFragment(Fragment fragment, Intent intent, int n, Bundle bundle) {
        if (n == -1) {
            ContextCompat.startActivity(this.mContext, intent, bundle);
            return;
        }
        throw new IllegalStateException("Starting activity with a requestCode requires a FragmentActivity host");
    }

    @Deprecated
    public void onStartIntentSenderFromFragment(Fragment fragment, IntentSender intentSender, int n, Intent intent, int n2, int n3, int n4, Bundle bundle) throws IntentSender.SendIntentException {
        if (n == -1) {
            ActivityCompat.startIntentSenderForResult(this.mActivity, intentSender, n, intent, n2, n3, n4, bundle);
            return;
        }
        throw new IllegalStateException("Starting intent sender with a requestCode requires a FragmentActivity host");
    }

    public void onSupportInvalidateOptionsMenu() {
    }
}

