/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.Dialog
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnCancelListener
 *  android.content.DialogInterface$OnDismissListener
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Looper
 *  android.util.Log
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.Window
 */
package androidx.fragment.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewTreeLifecycleOwner;
import androidx.lifecycle.ViewTreeViewModelStoreOwner;
import androidx.savedstate.ViewTreeSavedStateRegistryOwner;

public class DialogFragment
extends Fragment
implements DialogInterface.OnCancelListener,
DialogInterface.OnDismissListener {
    private static final String SAVED_BACK_STACK_ID = "android:backStackId";
    private static final String SAVED_CANCELABLE = "android:cancelable";
    private static final String SAVED_DIALOG_STATE_TAG = "android:savedDialogState";
    private static final String SAVED_INTERNAL_DIALOG_SHOWING = "android:dialogShowing";
    private static final String SAVED_SHOWS_DIALOG = "android:showsDialog";
    private static final String SAVED_STYLE = "android:style";
    private static final String SAVED_THEME = "android:theme";
    public static final int STYLE_NORMAL = 0;
    public static final int STYLE_NO_FRAME = 2;
    public static final int STYLE_NO_INPUT = 3;
    public static final int STYLE_NO_TITLE = 1;
    private int mBackStackId = -1;
    private boolean mCancelable = true;
    private boolean mCreatingDialog;
    private Dialog mDialog;
    private boolean mDialogCreated = false;
    private Runnable mDismissRunnable = new Runnable(this){
        final DialogFragment this$0;
        {
            this.this$0 = dialogFragment;
        }

        @Override
        public void run() {
            this.this$0.mOnDismissListener.onDismiss((DialogInterface)this.this$0.mDialog);
        }
    };
    private boolean mDismissed;
    private Handler mHandler;
    private Observer<LifecycleOwner> mObserver;
    private DialogInterface.OnCancelListener mOnCancelListener = new DialogInterface.OnCancelListener(this){
        final DialogFragment this$0;
        {
            this.this$0 = dialogFragment;
        }

        public void onCancel(DialogInterface object) {
            if (this.this$0.mDialog != null) {
                object = this.this$0;
                ((DialogFragment)object).onCancel((DialogInterface)((DialogFragment)object).mDialog);
            }
        }
    };
    private DialogInterface.OnDismissListener mOnDismissListener = new DialogInterface.OnDismissListener(this){
        final DialogFragment this$0;
        {
            this.this$0 = dialogFragment;
        }

        public void onDismiss(DialogInterface object) {
            if (this.this$0.mDialog != null) {
                object = this.this$0;
                ((DialogFragment)object).onDismiss((DialogInterface)((DialogFragment)object).mDialog);
            }
        }
    };
    private boolean mShownByMe;
    private boolean mShowsDialog = true;
    private int mStyle = 0;
    private int mTheme = 0;
    private boolean mViewDestroyed;

    public DialogFragment() {
        this.mObserver = new Observer<LifecycleOwner>(this){
            final DialogFragment this$0;
            {
                this.this$0 = dialogFragment;
            }

            @Override
            public void onChanged(LifecycleOwner object) {
                if (object != null && this.this$0.mShowsDialog) {
                    View view = this.this$0.requireView();
                    if (view.getParent() == null) {
                        if (this.this$0.mDialog != null) {
                            if (FragmentManager.isLoggingEnabled(3)) {
                                object = new StringBuilder();
                                ((StringBuilder)object).append("DialogFragment ");
                                ((StringBuilder)object).append(this);
                                ((StringBuilder)object).append(" setting the content view on ");
                                ((StringBuilder)object).append(this.this$0.mDialog);
                                Log.d((String)"FragmentManager", (String)((StringBuilder)object).toString());
                            }
                            this.this$0.mDialog.setContentView(view);
                        }
                    } else {
                        throw new IllegalStateException("DialogFragment can not be attached to a container view");
                    }
                }
            }
        };
    }

    public DialogFragment(int n) {
        super(n);
        this.mObserver = new /* invalid duplicate definition of identical inner class */;
    }

    private void dismissInternal(boolean bl, boolean bl2) {
        if (this.mDismissed) {
            return;
        }
        this.mDismissed = true;
        this.mShownByMe = false;
        Object object = this.mDialog;
        if (object != null) {
            object.setOnDismissListener(null);
            this.mDialog.dismiss();
            if (!bl2) {
                if (Looper.myLooper() == this.mHandler.getLooper()) {
                    this.onDismiss((DialogInterface)this.mDialog);
                } else {
                    this.mHandler.post(this.mDismissRunnable);
                }
            }
        }
        this.mViewDestroyed = true;
        if (this.mBackStackId >= 0) {
            this.getParentFragmentManager().popBackStack(this.mBackStackId, 1);
            this.mBackStackId = -1;
        } else {
            object = this.getParentFragmentManager().beginTransaction();
            ((FragmentTransaction)object).remove(this);
            if (bl) {
                ((FragmentTransaction)object).commitAllowingStateLoss();
            } else {
                ((FragmentTransaction)object).commit();
            }
        }
    }

    private void prepareDialog(Bundle bundle) {
        if (!this.mShowsDialog) {
            return;
        }
        if (!this.mDialogCreated) {
            try {
                this.mCreatingDialog = true;
                bundle = this.onCreateDialog(bundle);
                this.mDialog = bundle;
                if (this.mShowsDialog) {
                    this.setupDialog((Dialog)bundle, this.mStyle);
                    bundle = this.getContext();
                    if (bundle instanceof Activity) {
                        this.mDialog.setOwnerActivity((Activity)bundle);
                    }
                    this.mDialog.setCancelable(this.mCancelable);
                    this.mDialog.setOnCancelListener(this.mOnCancelListener);
                    this.mDialog.setOnDismissListener(this.mOnDismissListener);
                    this.mDialogCreated = true;
                } else {
                    this.mDialog = null;
                }
            }
            finally {
                this.mCreatingDialog = false;
            }
        }
    }

    @Override
    FragmentContainer createFragmentContainer() {
        return new FragmentContainer(this, super.createFragmentContainer()){
            final DialogFragment this$0;
            final FragmentContainer val$fragmentContainer;
            {
                this.this$0 = dialogFragment;
                this.val$fragmentContainer = fragmentContainer;
            }

            @Override
            public View onFindViewById(int n) {
                View view = this.this$0.onFindViewById(n);
                if (view != null) {
                    return view;
                }
                if (this.val$fragmentContainer.onHasView()) {
                    return this.val$fragmentContainer.onFindViewById(n);
                }
                return null;
            }

            @Override
            public boolean onHasView() {
                boolean bl = this.this$0.onHasView() || this.val$fragmentContainer.onHasView();
                return bl;
            }
        };
    }

    public void dismiss() {
        this.dismissInternal(false, false);
    }

    public void dismissAllowingStateLoss() {
        this.dismissInternal(true, false);
    }

    public Dialog getDialog() {
        return this.mDialog;
    }

    public boolean getShowsDialog() {
        return this.mShowsDialog;
    }

    public int getTheme() {
        return this.mTheme;
    }

    public boolean isCancelable() {
        return this.mCancelable;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.getViewLifecycleOwnerLiveData().observeForever(this.mObserver);
        if (!this.mShownByMe) {
            this.mDismissed = false;
        }
    }

    public void onCancel(DialogInterface dialogInterface) {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mHandler = new Handler();
        boolean bl = this.mContainerId == 0;
        this.mShowsDialog = bl;
        if (bundle != null) {
            this.mStyle = bundle.getInt(SAVED_STYLE, 0);
            this.mTheme = bundle.getInt(SAVED_THEME, 0);
            this.mCancelable = bundle.getBoolean(SAVED_CANCELABLE, true);
            this.mShowsDialog = bundle.getBoolean(SAVED_SHOWS_DIALOG, this.mShowsDialog);
            this.mBackStackId = bundle.getInt(SAVED_BACK_STACK_ID, -1);
        }
    }

    public Dialog onCreateDialog(Bundle object) {
        if (FragmentManager.isLoggingEnabled(3)) {
            object = new StringBuilder();
            ((StringBuilder)object).append("onCreateDialog called for DialogFragment ");
            ((StringBuilder)object).append(this);
            Log.d((String)"FragmentManager", (String)((StringBuilder)object).toString());
        }
        return new Dialog(this.requireContext(), this.getTheme());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Dialog dialog = this.mDialog;
        if (dialog != null) {
            this.mViewDestroyed = true;
            dialog.setOnDismissListener(null);
            this.mDialog.dismiss();
            if (!this.mDismissed) {
                this.onDismiss((DialogInterface)this.mDialog);
            }
            this.mDialog = null;
            this.mDialogCreated = false;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (!this.mShownByMe && !this.mDismissed) {
            this.mDismissed = true;
        }
        this.getViewLifecycleOwnerLiveData().removeObserver(this.mObserver);
    }

    public void onDismiss(DialogInterface object) {
        if (!this.mViewDestroyed) {
            if (FragmentManager.isLoggingEnabled(3)) {
                object = new StringBuilder();
                ((StringBuilder)object).append("onDismiss called for DialogFragment ");
                ((StringBuilder)object).append(this);
                Log.d((String)"FragmentManager", (String)((StringBuilder)object).toString());
            }
            this.dismissInternal(true, true);
        }
    }

    View onFindViewById(int n) {
        Dialog dialog = this.mDialog;
        if (dialog != null) {
            return dialog.findViewById(n);
        }
        return null;
    }

    @Override
    public LayoutInflater onGetLayoutInflater(Bundle object) {
        LayoutInflater layoutInflater = super.onGetLayoutInflater((Bundle)object);
        if (this.mShowsDialog && !this.mCreatingDialog) {
            this.prepareDialog((Bundle)object);
            if (FragmentManager.isLoggingEnabled(2)) {
                object = new StringBuilder();
                ((StringBuilder)object).append("get layout inflater for DialogFragment ");
                ((StringBuilder)object).append(this);
                ((StringBuilder)object).append(" from dialog context");
                Log.d((String)"FragmentManager", (String)((StringBuilder)object).toString());
            }
            Dialog dialog = this.mDialog;
            object = layoutInflater;
            if (dialog != null) {
                object = layoutInflater.cloneInContext(dialog.getContext());
            }
            return object;
        }
        if (FragmentManager.isLoggingEnabled(2)) {
            object = new StringBuilder();
            ((StringBuilder)object).append("getting layout inflater for DialogFragment ");
            ((StringBuilder)object).append(this);
            object = ((StringBuilder)object).toString();
            if (!this.mShowsDialog) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("mShowsDialog = false: ");
                stringBuilder.append((String)object);
                Log.d((String)"FragmentManager", (String)stringBuilder.toString());
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("mCreatingDialog = true: ");
                stringBuilder.append((String)object);
                Log.d((String)"FragmentManager", (String)stringBuilder.toString());
            }
        }
        return layoutInflater;
    }

    boolean onHasView() {
        return this.mDialogCreated;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        boolean bl;
        int n;
        super.onSaveInstanceState(bundle);
        Dialog dialog = this.mDialog;
        if (dialog != null) {
            dialog = dialog.onSaveInstanceState();
            dialog.putBoolean(SAVED_INTERNAL_DIALOG_SHOWING, false);
            bundle.putBundle(SAVED_DIALOG_STATE_TAG, (Bundle)dialog);
        }
        if ((n = this.mStyle) != 0) {
            bundle.putInt(SAVED_STYLE, n);
        }
        if ((n = this.mTheme) != 0) {
            bundle.putInt(SAVED_THEME, n);
        }
        if (!(bl = this.mCancelable)) {
            bundle.putBoolean(SAVED_CANCELABLE, bl);
        }
        if (!(bl = this.mShowsDialog)) {
            bundle.putBoolean(SAVED_SHOWS_DIALOG, bl);
        }
        if ((n = this.mBackStackId) != -1) {
            bundle.putInt(SAVED_BACK_STACK_ID, n);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = this.mDialog;
        if (dialog != null) {
            this.mViewDestroyed = false;
            dialog.show();
            dialog = this.mDialog.getWindow().getDecorView();
            ViewTreeLifecycleOwner.set((View)dialog, this);
            ViewTreeViewModelStoreOwner.set((View)dialog, this);
            ViewTreeSavedStateRegistryOwner.set((View)dialog, this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Dialog dialog = this.mDialog;
        if (dialog != null) {
            dialog.hide();
        }
    }

    @Override
    public void onViewStateRestored(Bundle bundle) {
        super.onViewStateRestored(bundle);
        if (this.mDialog != null && bundle != null && (bundle = bundle.getBundle(SAVED_DIALOG_STATE_TAG)) != null) {
            this.mDialog.onRestoreInstanceState(bundle);
        }
    }

    @Override
    void performCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        super.performCreateView(layoutInflater, viewGroup, bundle);
        if (this.mView == null && this.mDialog != null && bundle != null && (layoutInflater = bundle.getBundle(SAVED_DIALOG_STATE_TAG)) != null) {
            this.mDialog.onRestoreInstanceState((Bundle)layoutInflater);
        }
    }

    public final Dialog requireDialog() {
        Object object = this.getDialog();
        if (object != null) {
            return object;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("DialogFragment ");
        ((StringBuilder)object).append(this);
        ((StringBuilder)object).append(" does not have a Dialog.");
        throw new IllegalStateException(((StringBuilder)object).toString());
    }

    public void setCancelable(boolean bl) {
        this.mCancelable = bl;
        Dialog dialog = this.mDialog;
        if (dialog != null) {
            dialog.setCancelable(bl);
        }
    }

    public void setShowsDialog(boolean bl) {
        this.mShowsDialog = bl;
    }

    public void setStyle(int n, int n2) {
        if (FragmentManager.isLoggingEnabled(2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Setting style and theme for DialogFragment ");
            stringBuilder.append(this);
            stringBuilder.append(" to ");
            stringBuilder.append(n);
            stringBuilder.append(", ");
            stringBuilder.append(n2);
            Log.d((String)"FragmentManager", (String)stringBuilder.toString());
        }
        this.mStyle = n;
        if (n == 2 || n == 3) {
            this.mTheme = 16973913;
        }
        if (n2 != 0) {
            this.mTheme = n2;
        }
    }

    public void setupDialog(Dialog dialog, int n) {
        switch (n) {
            default: {
                break;
            }
            case 3: {
                Window window = dialog.getWindow();
                if (window != null) {
                    window.addFlags(24);
                }
            }
            case 1: 
            case 2: {
                dialog.requestWindowFeature(1);
            }
        }
    }

    public int show(FragmentTransaction fragmentTransaction, String string2) {
        int n;
        this.mDismissed = false;
        this.mShownByMe = true;
        fragmentTransaction.add(this, string2);
        this.mViewDestroyed = false;
        this.mBackStackId = n = fragmentTransaction.commit();
        return n;
    }

    public void show(FragmentManager object, String string2) {
        this.mDismissed = false;
        this.mShownByMe = true;
        object = ((FragmentManager)object).beginTransaction();
        ((FragmentTransaction)object).add(this, string2);
        ((FragmentTransaction)object).commit();
    }

    public void showNow(FragmentManager object, String string2) {
        this.mDismissed = false;
        this.mShownByMe = true;
        object = ((FragmentManager)object).beginTransaction();
        ((FragmentTransaction)object).add(this, string2);
        ((FragmentTransaction)object).commitNow();
    }
}

