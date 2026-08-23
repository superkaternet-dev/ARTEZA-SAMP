/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.content.res.Resources$NotFoundException
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.view.View
 *  android.widget.CompoundButton
 */
package androidx.appcompat.widget;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import androidx.appcompat.R;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.DrawableUtils;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.CompoundButtonCompat;

class AppCompatCompoundButtonHelper {
    private ColorStateList mButtonTintList = null;
    private PorterDuff.Mode mButtonTintMode = null;
    private boolean mHasButtonTint = false;
    private boolean mHasButtonTintMode = false;
    private boolean mSkipNextApply;
    private final CompoundButton mView;

    AppCompatCompoundButtonHelper(CompoundButton compoundButton) {
        this.mView = compoundButton;
    }

    void applyButtonTint() {
        Drawable drawable2 = CompoundButtonCompat.getButtonDrawable(this.mView);
        if (drawable2 != null && (this.mHasButtonTint || this.mHasButtonTintMode)) {
            drawable2 = DrawableCompat.wrap(drawable2).mutate();
            if (this.mHasButtonTint) {
                DrawableCompat.setTintList(drawable2, this.mButtonTintList);
            }
            if (this.mHasButtonTintMode) {
                DrawableCompat.setTintMode(drawable2, this.mButtonTintMode);
            }
            if (drawable2.isStateful()) {
                drawable2.setState(this.mView.getDrawableState());
            }
            this.mView.setButtonDrawable(drawable2);
        }
    }

    int getCompoundPaddingLeft(int n) {
        int n2 = n;
        if (Build.VERSION.SDK_INT < 17) {
            Drawable drawable2 = CompoundButtonCompat.getButtonDrawable(this.mView);
            n2 = n;
            if (drawable2 != null) {
                n2 = n + drawable2.getIntrinsicWidth();
            }
        }
        return n2;
    }

    ColorStateList getSupportButtonTintList() {
        return this.mButtonTintList;
    }

    PorterDuff.Mode getSupportButtonTintMode() {
        return this.mButtonTintMode;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void loadFromAttributes(AttributeSet attributeSet, int n) {
        int n2;
        TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), attributeSet, R.styleable.CompoundButton, n, 0);
        CompoundButton compoundButton = this.mView;
        ViewCompat.saveAttributeDataForStyleable((View)compoundButton, compoundButton.getContext(), R.styleable.CompoundButton, attributeSet, tintTypedArray.getWrappedTypeArray(), n, 0);
        n = n2 = 0;
        try {
            if (tintTypedArray.hasValue(R.styleable.CompoundButton_buttonCompat)) {
                int n3 = tintTypedArray.getResourceId(R.styleable.CompoundButton_buttonCompat, 0);
                n = n2;
                if (n3 != 0) {
                    try {
                        attributeSet = this.mView;
                        attributeSet.setButtonDrawable(AppCompatResources.getDrawable(attributeSet.getContext(), n3));
                        n = 1;
                    }
                    catch (Resources.NotFoundException notFoundException) {
                        n = n2;
                    }
                }
            }
            if (n == 0 && tintTypedArray.hasValue(R.styleable.CompoundButton_android_button) && (n = tintTypedArray.getResourceId(R.styleable.CompoundButton_android_button, 0)) != 0) {
                attributeSet = this.mView;
                attributeSet.setButtonDrawable(AppCompatResources.getDrawable(attributeSet.getContext(), n));
            }
            if (tintTypedArray.hasValue(R.styleable.CompoundButton_buttonTint)) {
                CompoundButtonCompat.setButtonTintList(this.mView, tintTypedArray.getColorStateList(R.styleable.CompoundButton_buttonTint));
            }
            if (!tintTypedArray.hasValue(R.styleable.CompoundButton_buttonTintMode)) return;
            CompoundButtonCompat.setButtonTintMode(this.mView, DrawableUtils.parseTintMode(tintTypedArray.getInt(R.styleable.CompoundButton_buttonTintMode, -1), null));
            return;
        }
        finally {
            tintTypedArray.recycle();
        }
    }

    void onSetButtonDrawable() {
        if (this.mSkipNextApply) {
            this.mSkipNextApply = false;
            return;
        }
        this.mSkipNextApply = true;
        this.applyButtonTint();
    }

    void setSupportButtonTintList(ColorStateList colorStateList) {
        this.mButtonTintList = colorStateList;
        this.mHasButtonTint = true;
        this.applyButtonTint();
    }

    void setSupportButtonTintMode(PorterDuff.Mode mode) {
        this.mButtonTintMode = mode;
        this.mHasButtonTintMode = true;
        this.applyButtonTint();
    }
}

