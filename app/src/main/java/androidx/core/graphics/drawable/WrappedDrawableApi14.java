/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Rect
 *  android.graphics.Region
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$Callback
 *  android.graphics.drawable.Drawable$ConstantState
 */
package androidx.core.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.drawable.TintAwareDrawable;
import androidx.core.graphics.drawable.WrappedDrawable;
import androidx.core.graphics.drawable.WrappedDrawableState;

class WrappedDrawableApi14
extends Drawable
implements Drawable.Callback,
WrappedDrawable,
TintAwareDrawable {
    static final PorterDuff.Mode DEFAULT_TINT_MODE = PorterDuff.Mode.SRC_IN;
    private boolean mColorFilterSet;
    private int mCurrentColor;
    private PorterDuff.Mode mCurrentMode;
    Drawable mDrawable;
    private boolean mMutated;
    WrappedDrawableState mState;

    WrappedDrawableApi14(Drawable drawable2) {
        this.mState = this.mutateConstantState();
        this.setWrappedDrawable(drawable2);
    }

    WrappedDrawableApi14(WrappedDrawableState wrappedDrawableState, Resources resources) {
        this.mState = wrappedDrawableState;
        this.updateLocalState(resources);
    }

    private WrappedDrawableState mutateConstantState() {
        return new WrappedDrawableState(this.mState);
    }

    private void updateLocalState(Resources resources) {
        WrappedDrawableState wrappedDrawableState = this.mState;
        if (wrappedDrawableState != null && wrappedDrawableState.mDrawableState != null) {
            this.setWrappedDrawable(this.mState.mDrawableState.newDrawable(resources));
        }
    }

    private boolean updateTint(int[] nArray) {
        if (!this.isCompatTintEnabled()) {
            return false;
        }
        ColorStateList colorStateList = this.mState.mTint;
        PorterDuff.Mode mode = this.mState.mTintMode;
        if (colorStateList != null && mode != null) {
            int n = colorStateList.getColorForState(nArray, colorStateList.getDefaultColor());
            if (!this.mColorFilterSet || n != this.mCurrentColor || mode != this.mCurrentMode) {
                this.setColorFilter(n, mode);
                this.mCurrentColor = n;
                this.mCurrentMode = mode;
                this.mColorFilterSet = true;
                return true;
            }
        } else {
            this.mColorFilterSet = false;
            this.clearColorFilter();
        }
        return false;
    }

    public void draw(Canvas canvas) {
        this.mDrawable.draw(canvas);
    }

    public int getChangingConfigurations() {
        int n = super.getChangingConfigurations();
        WrappedDrawableState wrappedDrawableState = this.mState;
        int n2 = wrappedDrawableState != null ? wrappedDrawableState.getChangingConfigurations() : 0;
        return n | n2 | this.mDrawable.getChangingConfigurations();
    }

    public Drawable.ConstantState getConstantState() {
        WrappedDrawableState wrappedDrawableState = this.mState;
        if (wrappedDrawableState != null && wrappedDrawableState.canConstantState()) {
            this.mState.mChangingConfigurations = this.getChangingConfigurations();
            return this.mState;
        }
        return null;
    }

    public Drawable getCurrent() {
        return this.mDrawable.getCurrent();
    }

    public int getIntrinsicHeight() {
        return this.mDrawable.getIntrinsicHeight();
    }

    public int getIntrinsicWidth() {
        return this.mDrawable.getIntrinsicWidth();
    }

    public int getMinimumHeight() {
        return this.mDrawable.getMinimumHeight();
    }

    public int getMinimumWidth() {
        return this.mDrawable.getMinimumWidth();
    }

    public int getOpacity() {
        return this.mDrawable.getOpacity();
    }

    public boolean getPadding(Rect rect) {
        return this.mDrawable.getPadding(rect);
    }

    public int[] getState() {
        return this.mDrawable.getState();
    }

    public Region getTransparentRegion() {
        return this.mDrawable.getTransparentRegion();
    }

    @Override
    public final Drawable getWrappedDrawable() {
        return this.mDrawable;
    }

    public void invalidateDrawable(Drawable drawable2) {
        this.invalidateSelf();
    }

    public boolean isAutoMirrored() {
        return this.mDrawable.isAutoMirrored();
    }

    protected boolean isCompatTintEnabled() {
        return true;
    }

    public boolean isStateful() {
        WrappedDrawableState wrappedDrawableState;
        wrappedDrawableState = this.isCompatTintEnabled() && (wrappedDrawableState = this.mState) != null ? wrappedDrawableState.mTint : null;
        boolean bl = wrappedDrawableState != null && wrappedDrawableState.isStateful() || this.mDrawable.isStateful();
        return bl;
    }

    public void jumpToCurrentState() {
        this.mDrawable.jumpToCurrentState();
    }

    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            WrappedDrawableState wrappedDrawableState;
            this.mState = this.mutateConstantState();
            Object object = this.mDrawable;
            if (object != null) {
                object.mutate();
            }
            if ((wrappedDrawableState = this.mState) != null) {
                object = this.mDrawable;
                object = object != null ? object.getConstantState() : null;
                wrappedDrawableState.mDrawableState = object;
            }
            this.mMutated = true;
        }
        return this;
    }

    protected void onBoundsChange(Rect rect) {
        Drawable drawable2 = this.mDrawable;
        if (drawable2 != null) {
            drawable2.setBounds(rect);
        }
    }

    protected boolean onLevelChange(int n) {
        return this.mDrawable.setLevel(n);
    }

    public void scheduleDrawable(Drawable drawable2, Runnable runnable, long l) {
        this.scheduleSelf(runnable, l);
    }

    public void setAlpha(int n) {
        this.mDrawable.setAlpha(n);
    }

    public void setAutoMirrored(boolean bl) {
        this.mDrawable.setAutoMirrored(bl);
    }

    public void setChangingConfigurations(int n) {
        this.mDrawable.setChangingConfigurations(n);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mDrawable.setColorFilter(colorFilter);
    }

    public void setDither(boolean bl) {
        this.mDrawable.setDither(bl);
    }

    public void setFilterBitmap(boolean bl) {
        this.mDrawable.setFilterBitmap(bl);
    }

    public boolean setState(int[] nArray) {
        boolean bl = this.mDrawable.setState(nArray);
        bl = this.updateTint(nArray) || bl;
        return bl;
    }

    @Override
    public void setTint(int n) {
        this.setTintList(ColorStateList.valueOf((int)n));
    }

    @Override
    public void setTintList(ColorStateList colorStateList) {
        this.mState.mTint = colorStateList;
        this.updateTint(this.getState());
    }

    @Override
    public void setTintMode(PorterDuff.Mode mode) {
        this.mState.mTintMode = mode;
        this.updateTint(this.getState());
    }

    public boolean setVisible(boolean bl, boolean bl2) {
        bl = super.setVisible(bl, bl2) || this.mDrawable.setVisible(bl, bl2);
        return bl;
    }

    @Override
    public final void setWrappedDrawable(Drawable drawable2) {
        Object object = this.mDrawable;
        if (object != null) {
            object.setCallback(null);
        }
        this.mDrawable = drawable2;
        if (drawable2 != null) {
            drawable2.setCallback((Drawable.Callback)this);
            this.setVisible(drawable2.isVisible(), true);
            this.setState(drawable2.getState());
            this.setLevel(drawable2.getLevel());
            this.setBounds(drawable2.getBounds());
            object = this.mState;
            if (object != null) {
                object.mDrawableState = drawable2.getConstantState();
            }
        }
        this.invalidateSelf();
    }

    public void unscheduleDrawable(Drawable drawable2, Runnable runnable) {
        this.unscheduleSelf(runnable);
    }
}

