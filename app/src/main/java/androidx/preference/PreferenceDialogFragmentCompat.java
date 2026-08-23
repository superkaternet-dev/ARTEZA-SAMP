/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Dialog
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnClickListener
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.Canvas
 *  android.graphics.drawable.BitmapDrawable
 *  android.graphics.drawable.Drawable
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.text.TextUtils
 *  android.view.View
 *  android.widget.TextView
 */
package androidx.preference;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.DialogPreference;

public abstract class PreferenceDialogFragmentCompat
extends DialogFragment
implements DialogInterface.OnClickListener {
    protected static final String ARG_KEY = "key";
    private static final String SAVE_STATE_ICON = "PreferenceDialogFragment.icon";
    private static final String SAVE_STATE_LAYOUT = "PreferenceDialogFragment.layout";
    private static final String SAVE_STATE_MESSAGE = "PreferenceDialogFragment.message";
    private static final String SAVE_STATE_NEGATIVE_TEXT = "PreferenceDialogFragment.negativeText";
    private static final String SAVE_STATE_POSITIVE_TEXT = "PreferenceDialogFragment.positiveText";
    private static final String SAVE_STATE_TITLE = "PreferenceDialogFragment.title";
    private BitmapDrawable mDialogIcon;
    private int mDialogLayoutRes;
    private CharSequence mDialogMessage;
    private CharSequence mDialogTitle;
    private CharSequence mNegativeButtonText;
    private CharSequence mPositiveButtonText;
    private DialogPreference mPreference;
    private int mWhichButtonClicked;

    private void requestInputMethod(Dialog dialog) {
        dialog.getWindow().setSoftInputMode(5);
    }

    public DialogPreference getPreference() {
        if (this.mPreference == null) {
            String string2 = this.getArguments().getString(ARG_KEY);
            this.mPreference = (DialogPreference)((DialogPreference.TargetFragment)((Object)this.getTargetFragment())).findPreference(string2);
        }
        return this.mPreference;
    }

    protected boolean needInputMethod() {
        return false;
    }

    protected void onBindDialogView(View object) {
        View view = object.findViewById(16908299);
        if (view != null) {
            object = this.mDialogMessage;
            int n = 8;
            if (!TextUtils.isEmpty((CharSequence)object)) {
                if (view instanceof TextView) {
                    ((TextView)view).setText((CharSequence)object);
                }
                n = 0;
            }
            if (view.getVisibility() != n) {
                view.setVisibility(n);
            }
        }
    }

    public void onClick(DialogInterface dialogInterface, int n) {
        this.mWhichButtonClicked = n;
    }

    @Override
    public void onCreate(Bundle object) {
        super.onCreate((Bundle)object);
        Object object2 = this.getTargetFragment();
        if (object2 instanceof DialogPreference.TargetFragment) {
            object2 = (DialogPreference.TargetFragment)object2;
            String string2 = this.getArguments().getString(ARG_KEY);
            if (object == null) {
                object = (DialogPreference)object2.findPreference(string2);
                this.mPreference = object;
                this.mDialogTitle = ((DialogPreference)object).getDialogTitle();
                this.mPositiveButtonText = this.mPreference.getPositiveButtonText();
                this.mNegativeButtonText = this.mPreference.getNegativeButtonText();
                this.mDialogMessage = this.mPreference.getDialogMessage();
                this.mDialogLayoutRes = this.mPreference.getDialogLayoutResource();
                object2 = this.mPreference.getDialogIcon();
                if (object2 != null && !(object2 instanceof BitmapDrawable)) {
                    object = Bitmap.createBitmap((int)object2.getIntrinsicWidth(), (int)object2.getIntrinsicHeight(), (Bitmap.Config)Bitmap.Config.ARGB_8888);
                    string2 = new Canvas((Bitmap)object);
                    object2.setBounds(0, 0, string2.getWidth(), string2.getHeight());
                    object2.draw((Canvas)string2);
                    this.mDialogIcon = new BitmapDrawable(this.getResources(), (Bitmap)object);
                } else {
                    this.mDialogIcon = (BitmapDrawable)object2;
                }
            } else {
                this.mDialogTitle = object.getCharSequence(SAVE_STATE_TITLE);
                this.mPositiveButtonText = object.getCharSequence(SAVE_STATE_POSITIVE_TEXT);
                this.mNegativeButtonText = object.getCharSequence(SAVE_STATE_NEGATIVE_TEXT);
                this.mDialogMessage = object.getCharSequence(SAVE_STATE_MESSAGE);
                this.mDialogLayoutRes = object.getInt(SAVE_STATE_LAYOUT, 0);
                if ((object = (Bitmap)object.getParcelable(SAVE_STATE_ICON)) != null) {
                    this.mDialogIcon = new BitmapDrawable(this.getResources(), (Bitmap)object);
                }
            }
            return;
        }
        throw new IllegalStateException("Target fragment must implement TargetFragment interface");
    }

    @Override
    public Dialog onCreateDialog(Bundle object) {
        FragmentActivity fragmentActivity = this.getActivity();
        this.mWhichButtonClicked = -2;
        object = new AlertDialog.Builder((Context)fragmentActivity).setTitle(this.mDialogTitle).setIcon((Drawable)this.mDialogIcon).setPositiveButton(this.mPositiveButtonText, (DialogInterface.OnClickListener)this).setNegativeButton(this.mNegativeButtonText, (DialogInterface.OnClickListener)this);
        if ((fragmentActivity = this.onCreateDialogView((Context)fragmentActivity)) != null) {
            this.onBindDialogView((View)fragmentActivity);
            ((AlertDialog.Builder)object).setView((View)fragmentActivity);
        } else {
            ((AlertDialog.Builder)object).setMessage(this.mDialogMessage);
        }
        this.onPrepareDialogBuilder((AlertDialog.Builder)object);
        object = ((AlertDialog.Builder)object).create();
        if (this.needInputMethod()) {
            this.requestInputMethod((Dialog)object);
        }
        return object;
    }

    protected View onCreateDialogView(Context context) {
        int n = this.mDialogLayoutRes;
        if (n == 0) {
            return null;
        }
        return this.getLayoutInflater().inflate(n, null);
    }

    public abstract void onDialogClosed(boolean var1);

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        boolean bl = this.mWhichButtonClicked == -1;
        this.onDialogClosed(bl);
    }

    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putCharSequence(SAVE_STATE_TITLE, this.mDialogTitle);
        bundle.putCharSequence(SAVE_STATE_POSITIVE_TEXT, this.mPositiveButtonText);
        bundle.putCharSequence(SAVE_STATE_NEGATIVE_TEXT, this.mNegativeButtonText);
        bundle.putCharSequence(SAVE_STATE_MESSAGE, this.mDialogMessage);
        bundle.putInt(SAVE_STATE_LAYOUT, this.mDialogLayoutRes);
        BitmapDrawable bitmapDrawable = this.mDialogIcon;
        if (bitmapDrawable != null) {
            bundle.putParcelable(SAVE_STATE_ICON, (Parcelable)bitmapDrawable.getBitmap());
        }
    }
}

