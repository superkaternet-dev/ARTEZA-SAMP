/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.view.View
 */
package androidx.appcompat.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.R;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.appcompat.widget.DrawableUtils;
import androidx.appcompat.widget.TintInfo;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.view.ViewCompat;

class AppCompatBackgroundHelper {
    private int mBackgroundResId = -1;
    private TintInfo mBackgroundTint;
    private final AppCompatDrawableManager mDrawableManager;
    private TintInfo mInternalBackgroundTint;
    private TintInfo mTmpInfo;
    private final View mView;

    AppCompatBackgroundHelper(View view) {
        this.mView = view;
        this.mDrawableManager = AppCompatDrawableManager.get();
    }

    private boolean applyFrameworkTintUsingColorFilter(Drawable drawable2) {
        if (this.mTmpInfo == null) {
            this.mTmpInfo = new TintInfo();
        }
        TintInfo tintInfo = this.mTmpInfo;
        tintInfo.clear();
        ColorStateList colorStateList = ViewCompat.getBackgroundTintList(this.mView);
        if (colorStateList != null) {
            tintInfo.mHasTintList = true;
            tintInfo.mTintList = colorStateList;
        }
        if ((colorStateList = ViewCompat.getBackgroundTintMode(this.mView)) != null) {
            tintInfo.mHasTintMode = true;
            tintInfo.mTintMode = colorStateList;
        }
        if (!tintInfo.mHasTintList && !tintInfo.mHasTintMode) {
            return false;
        }
        AppCompatDrawableManager.tintDrawable(drawable2, tintInfo, this.mView.getDrawableState());
        return true;
    }

    private boolean shouldApplyFrameworkTintUsingColorFilter() {
        int n = Build.VERSION.SDK_INT;
        boolean bl = true;
        if (n > 21) {
            if (this.mInternalBackgroundTint == null) {
                bl = false;
            }
            return bl;
        }
        return n == 21;
    }

    void applySupportBackgroundTint() {
        Drawable drawable2 = this.mView.getBackground();
        if (drawable2 != null) {
            if (this.shouldApplyFrameworkTintUsingColorFilter() && this.applyFrameworkTintUsingColorFilter(drawable2)) {
                return;
            }
            TintInfo tintInfo = this.mBackgroundTint;
            if (tintInfo != null) {
                AppCompatDrawableManager.tintDrawable(drawable2, tintInfo, this.mView.getDrawableState());
            } else {
                tintInfo = this.mInternalBackgroundTint;
                if (tintInfo != null) {
                    AppCompatDrawableManager.tintDrawable(drawable2, tintInfo, this.mView.getDrawableState());
                }
            }
        }
    }

    ColorStateList getSupportBackgroundTintList() {
        TintInfo tintInfo = this.mBackgroundTint;
        tintInfo = tintInfo != null ? tintInfo.mTintList : null;
        return tintInfo;
    }

    PorterDuff.Mode getSupportBackgroundTintMode() {
        TintInfo tintInfo = this.mBackgroundTint;
        tintInfo = tintInfo != null ? tintInfo.mTintMode : null;
        return tintInfo;
    }

    void loadFromAttributes(AttributeSet attributeSet, int n) {
        TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), attributeSet, R.styleable.ViewBackgroundHelper, n, 0);
        View view = this.mView;
        ViewCompat.saveAttributeDataForStyleable(view, view.getContext(), R.styleable.ViewBackgroundHelper, attributeSet, tintTypedArray.getWrappedTypeArray(), n, 0);
        try {
            if (tintTypedArray.hasValue(R.styleable.ViewBackgroundHelper_android_background)) {
                this.mBackgroundResId = tintTypedArray.getResourceId(R.styleable.ViewBackgroundHelper_android_background, -1);
                attributeSet = this.mDrawableManager.getTintList(this.mView.getContext(), this.mBackgroundResId);
                if (attributeSet != null) {
                    this.setInternalBackgroundTint((ColorStateList)attributeSet);
                }
            }
            if (tintTypedArray.hasValue(R.styleable.ViewBackgroundHelper_backgroundTint)) {
                ViewCompat.setBackgroundTintList(this.mView, tintTypedArray.getColorStateList(R.styleable.ViewBackgroundHelper_backgroundTint));
            }
            if (tintTypedArray.hasValue(R.styleable.ViewBackgroundHelper_backgroundTintMode)) {
                ViewCompat.setBackgroundTintMode(this.mView, DrawableUtils.parseTintMode(tintTypedArray.getInt(R.styleable.ViewBackgroundHelper_backgroundTintMode, -1), null));
            }
            return;
        }
        finally {
            tintTypedArray.recycle();
        }
    }

    void onSetBackgroundDrawable(Drawable drawable2) {
        this.mBackgroundResId = -1;
        this.setInternalBackgroundTint(null);
        this.applySupportBackgroundTint();
    }

    void onSetBackgroundResource(int n) {
        this.mBackgroundResId = n;
        AppCompatDrawableManager appCompatDrawableManager = this.mDrawableManager;
        appCompatDrawableManager = appCompatDrawableManager != null ? appCompatDrawableManager.getTintList(this.mView.getContext(), n) : null;
        this.setInternalBackgroundTint((ColorStateList)appCompatDrawableManager);
        this.applySupportBackgroundTint();
    }

    void setInternalBackgroundTint(ColorStateList colorStateList) {
        if (colorStateList != null) {
            if (this.mInternalBackgroundTint == null) {
                this.mInternalBackgroundTint = new TintInfo();
            }
            this.mInternalBackgroundTint.mTintList = colorStateList;
            this.mInternalBackgroundTint.mHasTintList = true;
        } else {
            this.mInternalBackgroundTint = null;
        }
        this.applySupportBackgroundTint();
    }

    void setSupportBackgroundTintList(ColorStateList colorStateList) {
        if (this.mBackgroundTint == null) {
            this.mBackgroundTint = new TintInfo();
        }
        this.mBackgroundTint.mTintList = colorStateList;
        this.mBackgroundTint.mHasTintList = true;
        this.applySupportBackgroundTint();
    }

    void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        if (this.mBackgroundTint == null) {
            this.mBackgroundTint = new TintInfo();
        }
        this.mBackgroundTint.mTintMode = mode;
        this.mBackgroundTint.mHasTintMode = true;
        this.applySupportBackgroundTint();
    }
}

