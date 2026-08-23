/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.RippleDrawable
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.view.View
 *  android.widget.ImageView
 */
package androidx.appcompat.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.R;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.appcompat.widget.DrawableUtils;
import androidx.appcompat.widget.TintInfo;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.view.ViewCompat;
import androidx.core.widget.ImageViewCompat;

public class AppCompatImageHelper {
    private TintInfo mImageTint;
    private TintInfo mInternalImageTint;
    private TintInfo mTmpInfo;
    private final ImageView mView;

    public AppCompatImageHelper(ImageView imageView) {
        this.mView = imageView;
    }

    private boolean applyFrameworkTintUsingColorFilter(Drawable drawable2) {
        if (this.mTmpInfo == null) {
            this.mTmpInfo = new TintInfo();
        }
        TintInfo tintInfo = this.mTmpInfo;
        tintInfo.clear();
        ColorStateList colorStateList = ImageViewCompat.getImageTintList(this.mView);
        if (colorStateList != null) {
            tintInfo.mHasTintList = true;
            tintInfo.mTintList = colorStateList;
        }
        if ((colorStateList = ImageViewCompat.getImageTintMode(this.mView)) != null) {
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
            if (this.mInternalImageTint == null) {
                bl = false;
            }
            return bl;
        }
        return n == 21;
    }

    void applySupportImageTint() {
        Drawable drawable2 = this.mView.getDrawable();
        if (drawable2 != null) {
            DrawableUtils.fixDrawable(drawable2);
        }
        if (drawable2 != null) {
            if (this.shouldApplyFrameworkTintUsingColorFilter() && this.applyFrameworkTintUsingColorFilter(drawable2)) {
                return;
            }
            TintInfo tintInfo = this.mImageTint;
            if (tintInfo != null) {
                AppCompatDrawableManager.tintDrawable(drawable2, tintInfo, this.mView.getDrawableState());
            } else {
                tintInfo = this.mInternalImageTint;
                if (tintInfo != null) {
                    AppCompatDrawableManager.tintDrawable(drawable2, tintInfo, this.mView.getDrawableState());
                }
            }
        }
    }

    ColorStateList getSupportImageTintList() {
        TintInfo tintInfo = this.mImageTint;
        tintInfo = tintInfo != null ? tintInfo.mTintList : null;
        return tintInfo;
    }

    PorterDuff.Mode getSupportImageTintMode() {
        TintInfo tintInfo = this.mImageTint;
        tintInfo = tintInfo != null ? tintInfo.mTintMode : null;
        return tintInfo;
    }

    boolean hasOverlappingRendering() {
        Drawable drawable2 = this.mView.getBackground();
        return Build.VERSION.SDK_INT < 21 || !(drawable2 instanceof RippleDrawable);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void loadFromAttributes(AttributeSet attributeSet, int n) {
        TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), attributeSet, R.styleable.AppCompatImageView, n, 0);
        ImageView imageView = this.mView;
        ViewCompat.saveAttributeDataForStyleable((View)imageView, imageView.getContext(), R.styleable.AppCompatImageView, attributeSet, tintTypedArray.getWrappedTypeArray(), n, 0);
        try {
            imageView = this.mView.getDrawable();
            attributeSet = imageView;
            if (imageView == null) {
                n = tintTypedArray.getResourceId(R.styleable.AppCompatImageView_srcCompat, -1);
                attributeSet = imageView;
                if (n != -1) {
                    imageView = AppCompatResources.getDrawable(this.mView.getContext(), n);
                    attributeSet = imageView;
                    if (imageView != null) {
                        this.mView.setImageDrawable((Drawable)imageView);
                        attributeSet = imageView;
                    }
                }
            }
            if (attributeSet != null) {
                DrawableUtils.fixDrawable((Drawable)attributeSet);
            }
            if (tintTypedArray.hasValue(R.styleable.AppCompatImageView_tint)) {
                ImageViewCompat.setImageTintList(this.mView, tintTypedArray.getColorStateList(R.styleable.AppCompatImageView_tint));
            }
            if (!tintTypedArray.hasValue(R.styleable.AppCompatImageView_tintMode)) return;
            ImageViewCompat.setImageTintMode(this.mView, DrawableUtils.parseTintMode(tintTypedArray.getInt(R.styleable.AppCompatImageView_tintMode, -1), null));
            return;
        }
        finally {
            tintTypedArray.recycle();
        }
    }

    public void setImageResource(int n) {
        if (n != 0) {
            Drawable drawable2 = AppCompatResources.getDrawable(this.mView.getContext(), n);
            if (drawable2 != null) {
                DrawableUtils.fixDrawable(drawable2);
            }
            this.mView.setImageDrawable(drawable2);
        } else {
            this.mView.setImageDrawable(null);
        }
        this.applySupportImageTint();
    }

    void setInternalImageTint(ColorStateList colorStateList) {
        if (colorStateList != null) {
            if (this.mInternalImageTint == null) {
                this.mInternalImageTint = new TintInfo();
            }
            this.mInternalImageTint.mTintList = colorStateList;
            this.mInternalImageTint.mHasTintList = true;
        } else {
            this.mInternalImageTint = null;
        }
        this.applySupportImageTint();
    }

    void setSupportImageTintList(ColorStateList colorStateList) {
        if (this.mImageTint == null) {
            this.mImageTint = new TintInfo();
        }
        this.mImageTint.mTintList = colorStateList;
        this.mImageTint.mHasTintList = true;
        this.applySupportImageTint();
    }

    void setSupportImageTintMode(PorterDuff.Mode mode) {
        if (this.mImageTint == null) {
            this.mImageTint = new TintInfo();
        }
        this.mImageTint.mTintMode = mode;
        this.mImageTint.mHasTintMode = true;
        this.applySupportImageTint();
    }
}

