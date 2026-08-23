/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.AnimatorSet
 *  android.animation.ObjectAnimator
 *  android.animation.TimeInterpolator
 *  android.animation.TypeEvaluator
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.graphics.Matrix
 *  android.graphics.Matrix$ScaleToFit
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.GradientDrawable
 *  android.graphics.drawable.LayerDrawable
 *  android.os.Build$VERSION
 *  android.util.Property
 *  android.view.View
 *  android.view.ViewTreeObserver$OnPreDrawListener
 */
package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.Property;
import android.view.View;
import android.view.ViewTreeObserver;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.animation.AnimatorSetCompat;
import com.google.android.material.animation.ImageMatrixProperty;
import com.google.android.material.animation.MatrixEvaluator;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.internal.CircularBorderDrawable;
import com.google.android.material.internal.StateListAnimator;
import com.google.android.material.internal.VisibilityAwareImageButton;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shadow.ShadowDrawableWrapper;
import com.google.android.material.shadow.ShadowViewDelegate;
import java.util.ArrayList;

class FloatingActionButtonImpl {
    static final int ANIM_STATE_HIDING = 1;
    static final int ANIM_STATE_NONE = 0;
    static final int ANIM_STATE_SHOWING = 2;
    static final long ELEVATION_ANIM_DELAY = 100L;
    static final long ELEVATION_ANIM_DURATION = 100L;
    static final TimeInterpolator ELEVATION_ANIM_INTERPOLATOR = AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR;
    static final int[] EMPTY_STATE_SET;
    static final int[] ENABLED_STATE_SET;
    static final int[] FOCUSED_ENABLED_STATE_SET;
    private static final float HIDE_ICON_SCALE = 0.0f;
    private static final float HIDE_OPACITY = 0.0f;
    private static final float HIDE_SCALE = 0.0f;
    static final int[] HOVERED_ENABLED_STATE_SET;
    static final int[] HOVERED_FOCUSED_ENABLED_STATE_SET;
    static final int[] PRESSED_ENABLED_STATE_SET;
    private static final float SHOW_ICON_SCALE = 1.0f;
    private static final float SHOW_OPACITY = 1.0f;
    private static final float SHOW_SCALE = 1.0f;
    int animState = 0;
    CircularBorderDrawable borderDrawable;
    Drawable contentBackground;
    Animator currentAnimator;
    private MotionSpec defaultHideMotionSpec;
    private MotionSpec defaultShowMotionSpec;
    float elevation;
    private ArrayList<Animator.AnimatorListener> hideListeners;
    MotionSpec hideMotionSpec;
    float hoveredFocusedTranslationZ;
    float imageMatrixScale = 1.0f;
    int maxImageSize;
    private ViewTreeObserver.OnPreDrawListener preDrawListener;
    float pressedTranslationZ;
    Drawable rippleDrawable;
    private float rotation;
    ShadowDrawableWrapper shadowDrawable;
    final ShadowViewDelegate shadowViewDelegate;
    Drawable shapeDrawable;
    private ArrayList<Animator.AnimatorListener> showListeners;
    MotionSpec showMotionSpec;
    private final StateListAnimator stateListAnimator;
    private final Matrix tmpMatrix;
    private final Rect tmpRect = new Rect();
    private final RectF tmpRectF1 = new RectF();
    private final RectF tmpRectF2 = new RectF();
    final VisibilityAwareImageButton view;

    static {
        PRESSED_ENABLED_STATE_SET = new int[]{16842919, 16842910};
        HOVERED_FOCUSED_ENABLED_STATE_SET = new int[]{16843623, 16842908, 16842910};
        FOCUSED_ENABLED_STATE_SET = new int[]{16842908, 16842910};
        HOVERED_ENABLED_STATE_SET = new int[]{16843623, 16842910};
        ENABLED_STATE_SET = new int[]{16842910};
        EMPTY_STATE_SET = new int[0];
    }

    FloatingActionButtonImpl(VisibilityAwareImageButton visibilityAwareImageButton, ShadowViewDelegate object) {
        this.tmpMatrix = new Matrix();
        this.view = visibilityAwareImageButton;
        this.shadowViewDelegate = object;
        this.stateListAnimator = object = new StateListAnimator();
        ((StateListAnimator)object).addState(PRESSED_ENABLED_STATE_SET, this.createElevationAnimator(new ElevateToPressedTranslationZAnimation(this)));
        ((StateListAnimator)object).addState(HOVERED_FOCUSED_ENABLED_STATE_SET, this.createElevationAnimator(new ElevateToHoveredFocusedTranslationZAnimation(this)));
        ((StateListAnimator)object).addState(FOCUSED_ENABLED_STATE_SET, this.createElevationAnimator(new ElevateToHoveredFocusedTranslationZAnimation(this)));
        ((StateListAnimator)object).addState(HOVERED_ENABLED_STATE_SET, this.createElevationAnimator(new ElevateToHoveredFocusedTranslationZAnimation(this)));
        ((StateListAnimator)object).addState(ENABLED_STATE_SET, this.createElevationAnimator(new ResetElevationAnimation(this)));
        ((StateListAnimator)object).addState(EMPTY_STATE_SET, this.createElevationAnimator(new DisabledElevationAnimation(this)));
        this.rotation = visibilityAwareImageButton.getRotation();
    }

    private void calculateImageMatrixFromScale(float f, Matrix matrix) {
        matrix.reset();
        Drawable drawable2 = this.view.getDrawable();
        if (drawable2 != null && this.maxImageSize != 0) {
            RectF rectF = this.tmpRectF1;
            RectF rectF2 = this.tmpRectF2;
            rectF.set(0.0f, 0.0f, (float)drawable2.getIntrinsicWidth(), (float)drawable2.getIntrinsicHeight());
            int n = this.maxImageSize;
            rectF2.set(0.0f, 0.0f, (float)n, (float)n);
            matrix.setRectToRect(rectF, rectF2, Matrix.ScaleToFit.CENTER);
            n = this.maxImageSize;
            matrix.postScale(f, f, (float)n / 2.0f, (float)n / 2.0f);
        }
    }

    private AnimatorSet createAnimator(MotionSpec motionSpec, float f, float f2, float f3) {
        ArrayList<Animator> arrayList = new ArrayList<Animator>();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat((Object)((Object)this.view), (Property)View.ALPHA, (float[])new float[]{f});
        motionSpec.getTiming("opacity").apply((Animator)objectAnimator);
        arrayList.add((Animator)objectAnimator);
        objectAnimator = ObjectAnimator.ofFloat((Object)((Object)this.view), (Property)View.SCALE_X, (float[])new float[]{f2});
        motionSpec.getTiming("scale").apply((Animator)objectAnimator);
        arrayList.add((Animator)objectAnimator);
        objectAnimator = ObjectAnimator.ofFloat((Object)((Object)this.view), (Property)View.SCALE_Y, (float[])new float[]{f2});
        motionSpec.getTiming("scale").apply((Animator)objectAnimator);
        arrayList.add((Animator)objectAnimator);
        this.calculateImageMatrixFromScale(f3, this.tmpMatrix);
        objectAnimator = ObjectAnimator.ofObject((Object)((Object)this.view), (Property)new ImageMatrixProperty(), (TypeEvaluator)new MatrixEvaluator(), (Object[])new Matrix[]{new Matrix(this.tmpMatrix)});
        motionSpec.getTiming("iconScale").apply((Animator)objectAnimator);
        arrayList.add((Animator)objectAnimator);
        motionSpec = new AnimatorSet();
        AnimatorSetCompat.playTogether((AnimatorSet)motionSpec, arrayList);
        return motionSpec;
    }

    private ValueAnimator createElevationAnimator(ShadowAnimatorImpl shadowAnimatorImpl) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setInterpolator(ELEVATION_ANIM_INTERPOLATOR);
        valueAnimator.setDuration(100L);
        valueAnimator.addListener((Animator.AnimatorListener)shadowAnimatorImpl);
        valueAnimator.addUpdateListener((ValueAnimator.AnimatorUpdateListener)shadowAnimatorImpl);
        valueAnimator.setFloatValues(new float[]{0.0f, 1.0f});
        return valueAnimator;
    }

    private void ensurePreDrawListener() {
        if (this.preDrawListener == null) {
            this.preDrawListener = new ViewTreeObserver.OnPreDrawListener(this){
                final FloatingActionButtonImpl this$0;
                {
                    this.this$0 = floatingActionButtonImpl;
                }

                public boolean onPreDraw() {
                    this.this$0.onPreDraw();
                    return true;
                }
            };
        }
    }

    private MotionSpec getDefaultHideMotionSpec() {
        if (this.defaultHideMotionSpec == null) {
            this.defaultHideMotionSpec = MotionSpec.createFromResource(this.view.getContext(), R.animator.design_fab_hide_motion_spec);
        }
        return this.defaultHideMotionSpec;
    }

    private MotionSpec getDefaultShowMotionSpec() {
        if (this.defaultShowMotionSpec == null) {
            this.defaultShowMotionSpec = MotionSpec.createFromResource(this.view.getContext(), R.animator.design_fab_show_motion_spec);
        }
        return this.defaultShowMotionSpec;
    }

    private boolean shouldAnimateVisibilityChange() {
        boolean bl = ViewCompat.isLaidOut((View)this.view) && !this.view.isInEditMode();
        return bl;
    }

    private void updateFromViewRotation() {
        Drawable drawable2;
        if (Build.VERSION.SDK_INT == 19) {
            if (this.rotation % 90.0f != 0.0f) {
                if (this.view.getLayerType() != 1) {
                    this.view.setLayerType(1, null);
                }
            } else if (this.view.getLayerType() != 0) {
                this.view.setLayerType(0, null);
            }
        }
        if ((drawable2 = this.shadowDrawable) != null) {
            drawable2.setRotation(-this.rotation);
        }
        if ((drawable2 = this.borderDrawable) != null) {
            drawable2.setRotation(-this.rotation);
        }
    }

    public void addOnHideAnimationListener(Animator.AnimatorListener animatorListener) {
        if (this.hideListeners == null) {
            this.hideListeners = new ArrayList();
        }
        this.hideListeners.add(animatorListener);
    }

    void addOnShowAnimationListener(Animator.AnimatorListener animatorListener) {
        if (this.showListeners == null) {
            this.showListeners = new ArrayList();
        }
        this.showListeners.add(animatorListener);
    }

    CircularBorderDrawable createBorderDrawable(int n, ColorStateList colorStateList) {
        Context context = this.view.getContext();
        CircularBorderDrawable circularBorderDrawable = this.newCircularDrawable();
        circularBorderDrawable.setGradientColors(ContextCompat.getColor(context, R.color.design_fab_stroke_top_outer_color), ContextCompat.getColor(context, R.color.design_fab_stroke_top_inner_color), ContextCompat.getColor(context, R.color.design_fab_stroke_end_inner_color), ContextCompat.getColor(context, R.color.design_fab_stroke_end_outer_color));
        circularBorderDrawable.setBorderWidth(n);
        circularBorderDrawable.setBorderTint(colorStateList);
        return circularBorderDrawable;
    }

    GradientDrawable createShapeDrawable() {
        GradientDrawable gradientDrawable = this.newGradientDrawableForShape();
        gradientDrawable.setShape(1);
        gradientDrawable.setColor(-1);
        return gradientDrawable;
    }

    final Drawable getContentBackground() {
        return this.contentBackground;
    }

    float getElevation() {
        return this.elevation;
    }

    final MotionSpec getHideMotionSpec() {
        return this.hideMotionSpec;
    }

    float getHoveredFocusedTranslationZ() {
        return this.hoveredFocusedTranslationZ;
    }

    void getPadding(Rect rect) {
        this.shadowDrawable.getPadding(rect);
    }

    float getPressedTranslationZ() {
        return this.pressedTranslationZ;
    }

    final MotionSpec getShowMotionSpec() {
        return this.showMotionSpec;
    }

    void hide(InternalVisibilityChangedListener iterator2, boolean bl) {
        if (this.isOrWillBeHidden()) {
            return;
        }
        Object object = this.currentAnimator;
        if (object != null) {
            object.cancel();
        }
        if (this.shouldAnimateVisibilityChange()) {
            object = this.hideMotionSpec;
            if (object == null) {
                object = this.getDefaultHideMotionSpec();
            }
            object = this.createAnimator((MotionSpec)object, 0.0f, 0.0f, 0.0f);
            object.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this, bl, (InternalVisibilityChangedListener)((Object)iterator2)){
                private boolean cancelled;
                final FloatingActionButtonImpl this$0;
                final boolean val$fromUser;
                final InternalVisibilityChangedListener val$listener;
                {
                    this.this$0 = floatingActionButtonImpl;
                    this.val$fromUser = bl;
                    this.val$listener = internalVisibilityChangedListener;
                }

                public void onAnimationCancel(Animator animator2) {
                    this.cancelled = true;
                }

                public void onAnimationEnd(Animator object) {
                    this.this$0.animState = 0;
                    this.this$0.currentAnimator = null;
                    if (!this.cancelled) {
                        object = this.this$0.view;
                        boolean bl = this.val$fromUser;
                        int n = bl ? 8 : 4;
                        ((VisibilityAwareImageButton)((Object)object)).internalSetVisibility(n, bl);
                        object = this.val$listener;
                        if (object != null) {
                            object.onHidden();
                        }
                    }
                }

                public void onAnimationStart(Animator animator2) {
                    this.this$0.view.internalSetVisibility(0, this.val$fromUser);
                    this.this$0.animState = 1;
                    this.this$0.currentAnimator = animator2;
                    this.cancelled = false;
                }
            });
            iterator2 = this.hideListeners;
            if (iterator2 != null) {
                iterator2 = ((ArrayList)((Object)iterator2)).iterator();
                while (iterator2.hasNext()) {
                    object.addListener((Animator.AnimatorListener)iterator2.next());
                }
            }
            object.start();
        } else {
            object = this.view;
            int n = bl ? 8 : 4;
            ((VisibilityAwareImageButton)((Object)object)).internalSetVisibility(n, bl);
            if (iterator2 != null) {
                iterator2.onHidden();
            }
        }
    }

    boolean isOrWillBeHidden() {
        int n = this.view.getVisibility();
        boolean bl = false;
        boolean bl2 = false;
        if (n == 0) {
            if (this.animState == 1) {
                bl2 = true;
            }
            return bl2;
        }
        bl2 = bl;
        if (this.animState != 2) {
            bl2 = true;
        }
        return bl2;
    }

    boolean isOrWillBeShown() {
        int n = this.view.getVisibility();
        boolean bl = false;
        boolean bl2 = false;
        if (n != 0) {
            if (this.animState == 2) {
                bl2 = true;
            }
            return bl2;
        }
        bl2 = bl;
        if (this.animState != 1) {
            bl2 = true;
        }
        return bl2;
    }

    void jumpDrawableToCurrentState() {
        this.stateListAnimator.jumpToCurrentState();
    }

    CircularBorderDrawable newCircularDrawable() {
        return new CircularBorderDrawable();
    }

    GradientDrawable newGradientDrawableForShape() {
        return new GradientDrawable();
    }

    void onAttachedToWindow() {
        if (this.requirePreDrawListener()) {
            this.ensurePreDrawListener();
            this.view.getViewTreeObserver().addOnPreDrawListener(this.preDrawListener);
        }
    }

    void onCompatShadowChanged() {
    }

    void onDetachedFromWindow() {
        if (this.preDrawListener != null) {
            this.view.getViewTreeObserver().removeOnPreDrawListener(this.preDrawListener);
            this.preDrawListener = null;
        }
    }

    void onDrawableStateChanged(int[] nArray) {
        this.stateListAnimator.setState(nArray);
    }

    void onElevationsChanged(float f, float f2, float f3) {
        ShadowDrawableWrapper shadowDrawableWrapper = this.shadowDrawable;
        if (shadowDrawableWrapper != null) {
            shadowDrawableWrapper.setShadowSize(f, this.pressedTranslationZ + f);
            this.updatePadding();
        }
    }

    void onPaddingUpdated(Rect rect) {
    }

    void onPreDraw() {
        float f = this.view.getRotation();
        if (this.rotation != f) {
            this.rotation = f;
            this.updateFromViewRotation();
        }
    }

    public void removeOnHideAnimationListener(Animator.AnimatorListener animatorListener) {
        ArrayList<Animator.AnimatorListener> arrayList = this.hideListeners;
        if (arrayList == null) {
            return;
        }
        arrayList.remove(animatorListener);
    }

    void removeOnShowAnimationListener(Animator.AnimatorListener animatorListener) {
        ArrayList<Animator.AnimatorListener> arrayList = this.showListeners;
        if (arrayList == null) {
            return;
        }
        arrayList.remove(animatorListener);
    }

    boolean requirePreDrawListener() {
        return true;
    }

    void setBackgroundDrawable(ColorStateList object, PorterDuff.Mode mode, ColorStateList colorStateList, int n) {
        Drawable drawable2;
        this.shapeDrawable = drawable2 = DrawableCompat.wrap((Drawable)this.createShapeDrawable());
        DrawableCompat.setTintList(drawable2, (ColorStateList)object);
        if (mode != null) {
            DrawableCompat.setTintMode(this.shapeDrawable, mode);
        }
        mode = DrawableCompat.wrap((Drawable)this.createShapeDrawable());
        this.rippleDrawable = mode;
        DrawableCompat.setTintList((Drawable)mode, RippleUtils.convertToRippleDrawableColor(colorStateList));
        if (n > 0) {
            object = this.createBorderDrawable(n, (ColorStateList)object);
            this.borderDrawable = object;
            object = new Drawable[]{object, this.shapeDrawable, this.rippleDrawable};
        } else {
            this.borderDrawable = null;
            object = new Context[]{this.shapeDrawable, this.rippleDrawable};
        }
        this.contentBackground = new LayerDrawable((Drawable[])object);
        object = this.view.getContext();
        mode = this.contentBackground;
        float f = this.shadowViewDelegate.getRadius();
        float f2 = this.elevation;
        object = new ShadowDrawableWrapper((Context)object, (Drawable)mode, f, f2, f2 + this.pressedTranslationZ);
        this.shadowDrawable = object;
        ((ShadowDrawableWrapper)((Object)object)).setAddPaddingForCorners(false);
        this.shadowViewDelegate.setBackgroundDrawable(this.shadowDrawable);
    }

    void setBackgroundTintList(ColorStateList colorStateList) {
        Drawable drawable2 = this.shapeDrawable;
        if (drawable2 != null) {
            DrawableCompat.setTintList(drawable2, colorStateList);
        }
        if ((drawable2 = this.borderDrawable) != null) {
            drawable2.setBorderTint(colorStateList);
        }
    }

    void setBackgroundTintMode(PorterDuff.Mode mode) {
        Drawable drawable2 = this.shapeDrawable;
        if (drawable2 != null) {
            DrawableCompat.setTintMode(drawable2, mode);
        }
    }

    final void setElevation(float f) {
        if (this.elevation != f) {
            this.elevation = f;
            this.onElevationsChanged(f, this.hoveredFocusedTranslationZ, this.pressedTranslationZ);
        }
    }

    final void setHideMotionSpec(MotionSpec motionSpec) {
        this.hideMotionSpec = motionSpec;
    }

    final void setHoveredFocusedTranslationZ(float f) {
        if (this.hoveredFocusedTranslationZ != f) {
            this.hoveredFocusedTranslationZ = f;
            this.onElevationsChanged(this.elevation, f, this.pressedTranslationZ);
        }
    }

    final void setImageMatrixScale(float f) {
        this.imageMatrixScale = f;
        Matrix matrix = this.tmpMatrix;
        this.calculateImageMatrixFromScale(f, matrix);
        this.view.setImageMatrix(matrix);
    }

    final void setMaxImageSize(int n) {
        if (this.maxImageSize != n) {
            this.maxImageSize = n;
            this.updateImageMatrixScale();
        }
    }

    final void setPressedTranslationZ(float f) {
        if (this.pressedTranslationZ != f) {
            this.pressedTranslationZ = f;
            this.onElevationsChanged(this.elevation, this.hoveredFocusedTranslationZ, f);
        }
    }

    void setRippleColor(ColorStateList colorStateList) {
        Drawable drawable2 = this.rippleDrawable;
        if (drawable2 != null) {
            DrawableCompat.setTintList(drawable2, RippleUtils.convertToRippleDrawableColor(colorStateList));
        }
    }

    final void setShowMotionSpec(MotionSpec motionSpec) {
        this.showMotionSpec = motionSpec;
    }

    void show(InternalVisibilityChangedListener iterator2, boolean bl) {
        if (this.isOrWillBeShown()) {
            return;
        }
        Object object = this.currentAnimator;
        if (object != null) {
            object.cancel();
        }
        if (this.shouldAnimateVisibilityChange()) {
            if (this.view.getVisibility() != 0) {
                this.view.setAlpha(0.0f);
                this.view.setScaleY(0.0f);
                this.view.setScaleX(0.0f);
                this.setImageMatrixScale(0.0f);
            }
            if ((object = this.showMotionSpec) == null) {
                object = this.getDefaultShowMotionSpec();
            }
            object = this.createAnimator((MotionSpec)object, 1.0f, 1.0f, 1.0f);
            object.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this, bl, (InternalVisibilityChangedListener)((Object)iterator2)){
                final FloatingActionButtonImpl this$0;
                final boolean val$fromUser;
                final InternalVisibilityChangedListener val$listener;
                {
                    this.this$0 = floatingActionButtonImpl;
                    this.val$fromUser = bl;
                    this.val$listener = internalVisibilityChangedListener;
                }

                public void onAnimationEnd(Animator object) {
                    this.this$0.animState = 0;
                    this.this$0.currentAnimator = null;
                    object = this.val$listener;
                    if (object != null) {
                        object.onShown();
                    }
                }

                public void onAnimationStart(Animator animator2) {
                    this.this$0.view.internalSetVisibility(0, this.val$fromUser);
                    this.this$0.animState = 2;
                    this.this$0.currentAnimator = animator2;
                }
            });
            iterator2 = this.showListeners;
            if (iterator2 != null) {
                iterator2 = ((ArrayList)((Object)iterator2)).iterator();
                while (iterator2.hasNext()) {
                    object.addListener((Animator.AnimatorListener)iterator2.next());
                }
            }
            object.start();
        } else {
            this.view.internalSetVisibility(0, bl);
            this.view.setAlpha(1.0f);
            this.view.setScaleY(1.0f);
            this.view.setScaleX(1.0f);
            this.setImageMatrixScale(1.0f);
            if (iterator2 != null) {
                iterator2.onShown();
            }
        }
    }

    final void updateImageMatrixScale() {
        this.setImageMatrixScale(this.imageMatrixScale);
    }

    final void updatePadding() {
        Rect rect = this.tmpRect;
        this.getPadding(rect);
        this.onPaddingUpdated(rect);
        this.shadowViewDelegate.setShadowPadding(rect.left, rect.top, rect.right, rect.bottom);
    }

    private class DisabledElevationAnimation
    extends ShadowAnimatorImpl {
        final FloatingActionButtonImpl this$0;

        DisabledElevationAnimation(FloatingActionButtonImpl floatingActionButtonImpl) {
            this.this$0 = floatingActionButtonImpl;
            super(floatingActionButtonImpl);
        }

        @Override
        protected float getTargetShadowSize() {
            return 0.0f;
        }
    }

    private class ElevateToHoveredFocusedTranslationZAnimation
    extends ShadowAnimatorImpl {
        final FloatingActionButtonImpl this$0;

        ElevateToHoveredFocusedTranslationZAnimation(FloatingActionButtonImpl floatingActionButtonImpl) {
            this.this$0 = floatingActionButtonImpl;
            super(floatingActionButtonImpl);
        }

        @Override
        protected float getTargetShadowSize() {
            return this.this$0.elevation + this.this$0.hoveredFocusedTranslationZ;
        }
    }

    private class ElevateToPressedTranslationZAnimation
    extends ShadowAnimatorImpl {
        final FloatingActionButtonImpl this$0;

        ElevateToPressedTranslationZAnimation(FloatingActionButtonImpl floatingActionButtonImpl) {
            this.this$0 = floatingActionButtonImpl;
            super(floatingActionButtonImpl);
        }

        @Override
        protected float getTargetShadowSize() {
            return this.this$0.elevation + this.this$0.pressedTranslationZ;
        }
    }

    static interface InternalVisibilityChangedListener {
        public void onHidden();

        public void onShown();
    }

    private class ResetElevationAnimation
    extends ShadowAnimatorImpl {
        final FloatingActionButtonImpl this$0;

        ResetElevationAnimation(FloatingActionButtonImpl floatingActionButtonImpl) {
            this.this$0 = floatingActionButtonImpl;
            super(floatingActionButtonImpl);
        }

        @Override
        protected float getTargetShadowSize() {
            return this.this$0.elevation;
        }
    }

    private abstract class ShadowAnimatorImpl
    extends AnimatorListenerAdapter
    implements ValueAnimator.AnimatorUpdateListener {
        private float shadowSizeEnd;
        private float shadowSizeStart;
        final FloatingActionButtonImpl this$0;
        private boolean validValues;

        private ShadowAnimatorImpl(FloatingActionButtonImpl floatingActionButtonImpl) {
            this.this$0 = floatingActionButtonImpl;
        }

        protected abstract float getTargetShadowSize();

        public void onAnimationEnd(Animator animator2) {
            this.this$0.shadowDrawable.setShadowSize(this.shadowSizeEnd);
            this.validValues = false;
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if (!this.validValues) {
                this.shadowSizeStart = this.this$0.shadowDrawable.getShadowSize();
                this.shadowSizeEnd = this.getTargetShadowSize();
                this.validValues = true;
            }
            ShadowDrawableWrapper shadowDrawableWrapper = this.this$0.shadowDrawable;
            float f = this.shadowSizeStart;
            shadowDrawableWrapper.setShadowSize(f + (this.shadowSizeEnd - f) * valueAnimator.getAnimatedFraction());
        }
    }
}

