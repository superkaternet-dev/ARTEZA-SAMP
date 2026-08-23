/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.AnimatorSet
 *  android.animation.ObjectAnimator
 *  android.animation.StateListAnimator
 *  android.content.res.ColorStateList
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.GradientDrawable
 *  android.graphics.drawable.InsetDrawable
 *  android.graphics.drawable.LayerDrawable
 *  android.graphics.drawable.RippleDrawable
 *  android.os.Build$VERSION
 *  android.util.Property
 *  android.view.View
 */
package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.util.Property;
import android.view.View;
import androidx.core.graphics.drawable.DrawableCompat;
import com.google.android.material.floatingactionbutton.FloatingActionButtonImpl;
import com.google.android.material.internal.CircularBorderDrawable;
import com.google.android.material.internal.CircularBorderDrawableLollipop;
import com.google.android.material.internal.VisibilityAwareImageButton;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shadow.ShadowDrawableWrapper;
import com.google.android.material.shadow.ShadowViewDelegate;
import java.util.ArrayList;

class FloatingActionButtonImplLollipop
extends FloatingActionButtonImpl {
    private InsetDrawable insetDrawable;

    FloatingActionButtonImplLollipop(VisibilityAwareImageButton visibilityAwareImageButton, ShadowViewDelegate shadowViewDelegate) {
        super(visibilityAwareImageButton, shadowViewDelegate);
    }

    private Animator createElevationAnimator(float f, float f2) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play((Animator)ObjectAnimator.ofFloat((Object)((Object)this.view), (String)"elevation", (float[])new float[]{f}).setDuration(0L)).with((Animator)ObjectAnimator.ofFloat((Object)((Object)this.view), (Property)View.TRANSLATION_Z, (float[])new float[]{f2}).setDuration(100L));
        animatorSet.setInterpolator(ELEVATION_ANIM_INTERPOLATOR);
        return animatorSet;
    }

    @Override
    public float getElevation() {
        return this.view.getElevation();
    }

    @Override
    void getPadding(Rect rect) {
        if (this.shadowViewDelegate.isCompatPaddingEnabled()) {
            float f = this.shadowViewDelegate.getRadius();
            float f2 = this.getElevation() + this.pressedTranslationZ;
            int n = (int)Math.ceil(ShadowDrawableWrapper.calculateHorizontalPadding(f2, f, false));
            int n2 = (int)Math.ceil(ShadowDrawableWrapper.calculateVerticalPadding(f2, f, false));
            rect.set(n, n2, n, n2);
        } else {
            rect.set(0, 0, 0, 0);
        }
    }

    @Override
    void jumpDrawableToCurrentState() {
    }

    @Override
    CircularBorderDrawable newCircularDrawable() {
        return new CircularBorderDrawableLollipop();
    }

    @Override
    GradientDrawable newGradientDrawableForShape() {
        return new AlwaysStatefulGradientDrawable();
    }

    @Override
    void onCompatShadowChanged() {
        this.updatePadding();
    }

    @Override
    void onDrawableStateChanged(int[] nArray) {
        if (Build.VERSION.SDK_INT == 21) {
            if (this.view.isEnabled()) {
                this.view.setElevation(this.elevation);
                if (this.view.isPressed()) {
                    this.view.setTranslationZ(this.pressedTranslationZ);
                } else if (!this.view.isFocused() && !this.view.isHovered()) {
                    this.view.setTranslationZ(0.0f);
                } else {
                    this.view.setTranslationZ(this.hoveredFocusedTranslationZ);
                }
            } else {
                this.view.setElevation(0.0f);
                this.view.setTranslationZ(0.0f);
            }
        }
    }

    @Override
    void onElevationsChanged(float f, float f2, float f3) {
        if (Build.VERSION.SDK_INT == 21) {
            this.view.refreshDrawableState();
        } else {
            StateListAnimator stateListAnimator = new StateListAnimator();
            stateListAnimator.addState(PRESSED_ENABLED_STATE_SET, this.createElevationAnimator(f, f3));
            stateListAnimator.addState(HOVERED_FOCUSED_ENABLED_STATE_SET, this.createElevationAnimator(f, f2));
            stateListAnimator.addState(FOCUSED_ENABLED_STATE_SET, this.createElevationAnimator(f, f2));
            stateListAnimator.addState(HOVERED_ENABLED_STATE_SET, this.createElevationAnimator(f, f2));
            AnimatorSet animatorSet = new AnimatorSet();
            ArrayList<ObjectAnimator> arrayList = new ArrayList<ObjectAnimator>();
            arrayList.add(ObjectAnimator.ofFloat((Object)((Object)this.view), (String)"elevation", (float[])new float[]{f}).setDuration(0L));
            if (Build.VERSION.SDK_INT >= 22 && Build.VERSION.SDK_INT <= 24) {
                arrayList.add(ObjectAnimator.ofFloat((Object)((Object)this.view), (Property)View.TRANSLATION_Z, (float[])new float[]{this.view.getTranslationZ()}).setDuration(100L));
            }
            arrayList.add(ObjectAnimator.ofFloat((Object)((Object)this.view), (Property)View.TRANSLATION_Z, (float[])new float[]{0.0f}).setDuration(100L));
            animatorSet.playSequentially(arrayList.toArray(new Animator[0]));
            animatorSet.setInterpolator(ELEVATION_ANIM_INTERPOLATOR);
            stateListAnimator.addState(ENABLED_STATE_SET, (Animator)animatorSet);
            stateListAnimator.addState(EMPTY_STATE_SET, this.createElevationAnimator(0.0f, 0.0f));
            this.view.setStateListAnimator(stateListAnimator);
        }
        if (this.shadowViewDelegate.isCompatPaddingEnabled()) {
            this.updatePadding();
        }
    }

    @Override
    void onPaddingUpdated(Rect rect) {
        if (this.shadowViewDelegate.isCompatPaddingEnabled()) {
            this.insetDrawable = new InsetDrawable(this.rippleDrawable, rect.left, rect.top, rect.right, rect.bottom);
            this.shadowViewDelegate.setBackgroundDrawable((Drawable)this.insetDrawable);
        } else {
            this.shadowViewDelegate.setBackgroundDrawable(this.rippleDrawable);
        }
    }

    @Override
    boolean requirePreDrawListener() {
        return false;
    }

    @Override
    void setBackgroundDrawable(ColorStateList colorStateList, PorterDuff.Mode mode, ColorStateList colorStateList2, int n) {
        this.shapeDrawable = DrawableCompat.wrap((Drawable)this.createShapeDrawable());
        DrawableCompat.setTintList(this.shapeDrawable, colorStateList);
        if (mode != null) {
            DrawableCompat.setTintMode(this.shapeDrawable, mode);
        }
        if (n > 0) {
            this.borderDrawable = this.createBorderDrawable(n, colorStateList);
            colorStateList = new LayerDrawable(new Drawable[]{this.borderDrawable, this.shapeDrawable});
        } else {
            this.borderDrawable = null;
            colorStateList = this.shapeDrawable;
        }
        this.contentBackground = this.rippleDrawable = new RippleDrawable(RippleUtils.convertToRippleDrawableColor(colorStateList2), (Drawable)colorStateList, null);
        this.shadowViewDelegate.setBackgroundDrawable(this.rippleDrawable);
    }

    @Override
    void setRippleColor(ColorStateList colorStateList) {
        if (this.rippleDrawable instanceof RippleDrawable) {
            ((RippleDrawable)this.rippleDrawable).setColor(RippleUtils.convertToRippleDrawableColor(colorStateList));
        } else {
            super.setRippleColor(colorStateList);
        }
    }

    static class AlwaysStatefulGradientDrawable
    extends GradientDrawable {
        AlwaysStatefulGradientDrawable() {
        }

        public boolean isStateful() {
            return true;
        }
    }
}

