/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.graphics.Canvas
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$Callback
 *  android.util.AttributeSet
 *  android.view.View
 *  android.widget.ProgressBar
 *  android.widget.SeekBar
 */
package androidx.appcompat.widget;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import androidx.appcompat.R;
import androidx.appcompat.widget.AppCompatProgressBarHelper;
import androidx.appcompat.widget.DrawableUtils;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;

class AppCompatSeekBarHelper
extends AppCompatProgressBarHelper {
    private boolean mHasTickMarkTint = false;
    private boolean mHasTickMarkTintMode = false;
    private Drawable mTickMark;
    private ColorStateList mTickMarkTintList = null;
    private PorterDuff.Mode mTickMarkTintMode = null;
    private final SeekBar mView;

    AppCompatSeekBarHelper(SeekBar seekBar) {
        super((ProgressBar)seekBar);
        this.mView = seekBar;
    }

    private void applyTickMarkTint() {
        Drawable drawable2 = this.mTickMark;
        if (drawable2 != null && (this.mHasTickMarkTint || this.mHasTickMarkTintMode)) {
            this.mTickMark = drawable2 = DrawableCompat.wrap(drawable2.mutate());
            if (this.mHasTickMarkTint) {
                DrawableCompat.setTintList(drawable2, this.mTickMarkTintList);
            }
            if (this.mHasTickMarkTintMode) {
                DrawableCompat.setTintMode(this.mTickMark, this.mTickMarkTintMode);
            }
            if (this.mTickMark.isStateful()) {
                this.mTickMark.setState(this.mView.getDrawableState());
            }
        }
    }

    void drawTickMarks(Canvas canvas) {
        if (this.mTickMark != null) {
            int n = this.mView.getMax();
            int n2 = 1;
            if (n > 1) {
                int n3 = this.mTickMark.getIntrinsicWidth();
                int n4 = this.mTickMark.getIntrinsicHeight();
                n3 = n3 >= 0 ? (n3 /= 2) : 1;
                if (n4 >= 0) {
                    n2 = n4 / 2;
                }
                this.mTickMark.setBounds(-n3, -n2, n3, n2);
                float f = (float)(this.mView.getWidth() - this.mView.getPaddingLeft() - this.mView.getPaddingRight()) / (float)n;
                n2 = canvas.save();
                canvas.translate((float)this.mView.getPaddingLeft(), (float)(this.mView.getHeight() / 2));
                for (n3 = 0; n3 <= n; ++n3) {
                    this.mTickMark.draw(canvas);
                    canvas.translate(f, 0.0f);
                }
                canvas.restoreToCount(n2);
            }
        }
    }

    void drawableStateChanged() {
        Drawable drawable2 = this.mTickMark;
        if (drawable2 != null && drawable2.isStateful() && drawable2.setState(this.mView.getDrawableState())) {
            this.mView.invalidateDrawable(drawable2);
        }
    }

    Drawable getTickMark() {
        return this.mTickMark;
    }

    ColorStateList getTickMarkTintList() {
        return this.mTickMarkTintList;
    }

    PorterDuff.Mode getTickMarkTintMode() {
        return this.mTickMarkTintMode;
    }

    void jumpDrawablesToCurrentState() {
        Drawable drawable2 = this.mTickMark;
        if (drawable2 != null) {
            drawable2.jumpToCurrentState();
        }
    }

    @Override
    void loadFromAttributes(AttributeSet attributeSet, int n) {
        super.loadFromAttributes(attributeSet, n);
        TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), attributeSet, R.styleable.AppCompatSeekBar, n, 0);
        SeekBar seekBar = this.mView;
        ViewCompat.saveAttributeDataForStyleable((View)seekBar, seekBar.getContext(), R.styleable.AppCompatSeekBar, attributeSet, tintTypedArray.getWrappedTypeArray(), n, 0);
        attributeSet = tintTypedArray.getDrawableIfKnown(R.styleable.AppCompatSeekBar_android_thumb);
        if (attributeSet != null) {
            this.mView.setThumb((Drawable)attributeSet);
        }
        this.setTickMark(tintTypedArray.getDrawable(R.styleable.AppCompatSeekBar_tickMark));
        if (tintTypedArray.hasValue(R.styleable.AppCompatSeekBar_tickMarkTintMode)) {
            this.mTickMarkTintMode = DrawableUtils.parseTintMode(tintTypedArray.getInt(R.styleable.AppCompatSeekBar_tickMarkTintMode, -1), this.mTickMarkTintMode);
            this.mHasTickMarkTintMode = true;
        }
        if (tintTypedArray.hasValue(R.styleable.AppCompatSeekBar_tickMarkTint)) {
            this.mTickMarkTintList = tintTypedArray.getColorStateList(R.styleable.AppCompatSeekBar_tickMarkTint);
            this.mHasTickMarkTint = true;
        }
        tintTypedArray.recycle();
        this.applyTickMarkTint();
    }

    void setTickMark(Drawable drawable2) {
        Drawable drawable3 = this.mTickMark;
        if (drawable3 != null) {
            drawable3.setCallback(null);
        }
        this.mTickMark = drawable2;
        if (drawable2 != null) {
            drawable2.setCallback((Drawable.Callback)this.mView);
            DrawableCompat.setLayoutDirection(drawable2, ViewCompat.getLayoutDirection((View)this.mView));
            if (drawable2.isStateful()) {
                drawable2.setState(this.mView.getDrawableState());
            }
            this.applyTickMarkTint();
        }
        this.mView.invalidate();
    }

    void setTickMarkTintList(ColorStateList colorStateList) {
        this.mTickMarkTintList = colorStateList;
        this.mHasTickMarkTint = true;
        this.applyTickMarkTint();
    }

    void setTickMarkTintMode(PorterDuff.Mode mode) {
        this.mTickMarkTintMode = mode;
        this.mHasTickMarkTintMode = true;
        this.applyTickMarkTint();
    }
}

