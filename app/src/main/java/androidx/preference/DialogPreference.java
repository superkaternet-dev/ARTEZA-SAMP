/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 */
package androidx.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.Preference;
import androidx.preference.R;

public abstract class DialogPreference
extends Preference {
    private Drawable mDialogIcon;
    private int mDialogLayoutResId;
    private CharSequence mDialogMessage;
    private CharSequence mDialogTitle;
    private CharSequence mNegativeButtonText;
    private CharSequence mPositiveButtonText;

    public DialogPreference(Context context) {
        this(context, null);
    }

    public DialogPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, TypedArrayUtils.getAttr(context, R.attr.dialogPreferenceStyle, 0x1010091));
    }

    public DialogPreference(Context context, AttributeSet attributeSet, int n) {
        this(context, attributeSet, n, 0);
    }

    public DialogPreference(Context context, AttributeSet object, int n, int n2) {
        super(context, (AttributeSet)object, n, n2);
        context = context.obtainStyledAttributes(object, R.styleable.DialogPreference, n, n2);
        object = TypedArrayUtils.getString((TypedArray)context, R.styleable.DialogPreference_dialogTitle, R.styleable.DialogPreference_android_dialogTitle);
        this.mDialogTitle = object;
        if (object == null) {
            this.mDialogTitle = this.getTitle();
        }
        this.mDialogMessage = TypedArrayUtils.getString((TypedArray)context, R.styleable.DialogPreference_dialogMessage, R.styleable.DialogPreference_android_dialogMessage);
        this.mDialogIcon = TypedArrayUtils.getDrawable((TypedArray)context, R.styleable.DialogPreference_dialogIcon, R.styleable.DialogPreference_android_dialogIcon);
        this.mPositiveButtonText = TypedArrayUtils.getString((TypedArray)context, R.styleable.DialogPreference_positiveButtonText, R.styleable.DialogPreference_android_positiveButtonText);
        this.mNegativeButtonText = TypedArrayUtils.getString((TypedArray)context, R.styleable.DialogPreference_negativeButtonText, R.styleable.DialogPreference_android_negativeButtonText);
        this.mDialogLayoutResId = TypedArrayUtils.getResourceId((TypedArray)context, R.styleable.DialogPreference_dialogLayout, R.styleable.DialogPreference_android_dialogLayout, 0);
        context.recycle();
    }

    public Drawable getDialogIcon() {
        return this.mDialogIcon;
    }

    public int getDialogLayoutResource() {
        return this.mDialogLayoutResId;
    }

    public CharSequence getDialogMessage() {
        return this.mDialogMessage;
    }

    public CharSequence getDialogTitle() {
        return this.mDialogTitle;
    }

    public CharSequence getNegativeButtonText() {
        return this.mNegativeButtonText;
    }

    public CharSequence getPositiveButtonText() {
        return this.mPositiveButtonText;
    }

    @Override
    protected void onClick() {
        this.getPreferenceManager().showDialog(this);
    }

    public void setDialogIcon(int n) {
        this.mDialogIcon = AppCompatResources.getDrawable(this.getContext(), n);
    }

    public void setDialogIcon(Drawable drawable2) {
        this.mDialogIcon = drawable2;
    }

    public void setDialogLayoutResource(int n) {
        this.mDialogLayoutResId = n;
    }

    public void setDialogMessage(int n) {
        this.setDialogMessage(this.getContext().getString(n));
    }

    public void setDialogMessage(CharSequence charSequence) {
        this.mDialogMessage = charSequence;
    }

    public void setDialogTitle(int n) {
        this.setDialogTitle(this.getContext().getString(n));
    }

    public void setDialogTitle(CharSequence charSequence) {
        this.mDialogTitle = charSequence;
    }

    public void setNegativeButtonText(int n) {
        this.setNegativeButtonText(this.getContext().getString(n));
    }

    public void setNegativeButtonText(CharSequence charSequence) {
        this.mNegativeButtonText = charSequence;
    }

    public void setPositiveButtonText(int n) {
        this.setPositiveButtonText(this.getContext().getString(n));
    }

    public void setPositiveButtonText(CharSequence charSequence) {
        this.mPositiveButtonText = charSequence;
    }

    public static interface TargetFragment {
        public <T extends Preference> T findPreference(CharSequence var1);
    }
}

