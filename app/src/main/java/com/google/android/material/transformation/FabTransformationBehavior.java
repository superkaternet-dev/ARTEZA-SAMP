/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.AnimatorSet
 *  android.animation.ObjectAnimator
 *  android.animation.TypeEvaluator
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.Property
 *  android.view.View
 *  android.view.ViewAnimationUtils
 *  android.view.ViewGroup
 *  android.widget.ImageView
 */
package com.google.android.material.transformation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.animation.AnimatorSetCompat;
import com.google.android.material.animation.ArgbEvaluatorCompat;
import com.google.android.material.animation.ChildrenAlphaProperty;
import com.google.android.material.animation.DrawableAlphaProperty;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.animation.MotionTiming;
import com.google.android.material.animation.Positioning;
import com.google.android.material.circularreveal.CircularRevealCompat;
import com.google.android.material.circularreveal.CircularRevealHelper;
import com.google.android.material.circularreveal.CircularRevealWidget;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.math.MathUtils;
import com.google.android.material.transformation.ExpandableTransformationBehavior;
import com.google.android.material.transformation.TransformationChildCard;
import com.google.android.material.transformation.TransformationChildLayout;
import java.util.ArrayList;
import java.util.List;

public abstract class FabTransformationBehavior
extends ExpandableTransformationBehavior {
    private final int[] tmpArray;
    private final Rect tmpRect = new Rect();
    private final RectF tmpRectF1 = new RectF();
    private final RectF tmpRectF2 = new RectF();

    public FabTransformationBehavior() {
        this.tmpArray = new int[2];
    }

    public FabTransformationBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.tmpArray = new int[2];
    }

    private ViewGroup calculateChildContentContainer(View view) {
        View view2 = view.findViewById(R.id.mtrl_child_content_container);
        if (view2 != null) {
            return this.toViewGroupOrNull(view2);
        }
        if (!(view instanceof TransformationChildLayout) && !(view instanceof TransformationChildCard)) {
            return this.toViewGroupOrNull(view);
        }
        return this.toViewGroupOrNull(((ViewGroup)view).getChildAt(0));
    }

    private void calculateChildVisibleBoundsAtEndOfExpansion(View view, FabTransformationSpec fabTransformationSpec, MotionTiming motionTiming, MotionTiming motionTiming2, float f, float f2, float f3, float f4, RectF rectF) {
        f = this.calculateValueOfAnimationAtEndOfExpansion(fabTransformationSpec, motionTiming, f, f3);
        f2 = this.calculateValueOfAnimationAtEndOfExpansion(fabTransformationSpec, motionTiming2, f2, f4);
        motionTiming = this.tmpRect;
        view.getWindowVisibleDisplayFrame((Rect)motionTiming);
        fabTransformationSpec = this.tmpRectF1;
        fabTransformationSpec.set((Rect)motionTiming);
        motionTiming = this.tmpRectF2;
        this.calculateWindowBounds(view, (RectF)motionTiming);
        motionTiming.offset(f, f2);
        motionTiming.intersect((RectF)fabTransformationSpec);
        rectF.set((RectF)motionTiming);
    }

    private float calculateRevealCenterX(View view, View view2, Positioning positioning) {
        RectF rectF = this.tmpRectF1;
        RectF rectF2 = this.tmpRectF2;
        this.calculateWindowBounds(view, rectF);
        this.calculateWindowBounds(view2, rectF2);
        rectF2.offset(-this.calculateTranslationX(view, view2, positioning), 0.0f);
        return rectF.centerX() - rectF2.left;
    }

    private float calculateRevealCenterY(View view, View view2, Positioning positioning) {
        RectF rectF = this.tmpRectF1;
        RectF rectF2 = this.tmpRectF2;
        this.calculateWindowBounds(view, rectF);
        this.calculateWindowBounds(view2, rectF2);
        rectF2.offset(0.0f, -this.calculateTranslationY(view, view2, positioning));
        return rectF.centerY() - rectF2.top;
    }

    private float calculateTranslationX(View view, View view2, Positioning positioning) {
        RectF rectF = this.tmpRectF1;
        RectF rectF2 = this.tmpRectF2;
        this.calculateWindowBounds(view, rectF);
        this.calculateWindowBounds(view2, rectF2);
        float f = 0.0f;
        switch (positioning.gravity & 7) {
            default: {
                break;
            }
            case 5: {
                f = rectF2.right - rectF.right;
                break;
            }
            case 3: {
                f = rectF2.left - rectF.left;
                break;
            }
            case 1: {
                f = rectF2.centerX() - rectF.centerX();
            }
        }
        return f + positioning.xAdjustment;
    }

    private float calculateTranslationY(View view, View view2, Positioning positioning) {
        RectF rectF = this.tmpRectF1;
        RectF rectF2 = this.tmpRectF2;
        this.calculateWindowBounds(view, rectF);
        this.calculateWindowBounds(view2, rectF2);
        float f = 0.0f;
        switch (positioning.gravity & 0x70) {
            default: {
                break;
            }
            case 80: {
                f = rectF2.bottom - rectF.bottom;
                break;
            }
            case 48: {
                f = rectF2.top - rectF.top;
                break;
            }
            case 16: {
                f = rectF2.centerY() - rectF.centerY();
            }
        }
        return f + positioning.yAdjustment;
    }

    private float calculateValueOfAnimationAtEndOfExpansion(FabTransformationSpec object, MotionTiming motionTiming, float f, float f2) {
        long l = motionTiming.getDelay();
        long l2 = motionTiming.getDuration();
        object = ((FabTransformationSpec)object).timings.getTiming("expansion");
        float f3 = (float)(((MotionTiming)object).getDelay() + ((MotionTiming)object).getDuration() + 17L - l) / (float)l2;
        return AnimationUtils.lerp(f, f2, motionTiming.getInterpolator().getInterpolation(f3));
    }

    private void calculateWindowBounds(View view, RectF rectF) {
        rectF.set(0.0f, 0.0f, (float)view.getWidth(), (float)view.getHeight());
        int[] nArray = this.tmpArray;
        view.getLocationInWindow(nArray);
        rectF.offsetTo((float)nArray[0], (float)nArray[1]);
        rectF.offset((float)((int)(-view.getTranslationX())), (float)((int)(-view.getTranslationY())));
    }

    private void createChildrenFadeAnimation(View view, View view2, boolean bl, boolean bl2, FabTransformationSpec fabTransformationSpec, List<Animator> list, List<Animator.AnimatorListener> list2) {
        if (!(view2 instanceof ViewGroup)) {
            return;
        }
        if (view2 instanceof CircularRevealWidget && CircularRevealHelper.STRATEGY == 0) {
            return;
        }
        view = this.calculateChildContentContainer(view2);
        if (view == null) {
            return;
        }
        if (bl) {
            if (!bl2) {
                ChildrenAlphaProperty.CHILDREN_ALPHA.set((Object)view, (Object)Float.valueOf(0.0f));
            }
            view = ObjectAnimator.ofFloat((Object)view, ChildrenAlphaProperty.CHILDREN_ALPHA, (float[])new float[]{1.0f});
        } else {
            view = ObjectAnimator.ofFloat((Object)view, ChildrenAlphaProperty.CHILDREN_ALPHA, (float[])new float[]{0.0f});
        }
        fabTransformationSpec.timings.getTiming("contentFade").apply((Animator)view);
        list.add((Animator)view);
    }

    private void createColorAnimation(View view, View object, boolean bl, boolean bl2, FabTransformationSpec fabTransformationSpec, List<Animator> list, List<Animator.AnimatorListener> list2) {
        if (!(object instanceof CircularRevealWidget)) {
            return;
        }
        object = (CircularRevealWidget)object;
        int n = this.getBackgroundTint(view);
        if (bl) {
            if (!bl2) {
                object.setCircularRevealScrimColor(n);
            }
            view = ObjectAnimator.ofInt((Object)object, CircularRevealWidget.CircularRevealScrimColorProperty.CIRCULAR_REVEAL_SCRIM_COLOR, (int[])new int[]{0xFFFFFF & n});
        } else {
            view = ObjectAnimator.ofInt((Object)object, CircularRevealWidget.CircularRevealScrimColorProperty.CIRCULAR_REVEAL_SCRIM_COLOR, (int[])new int[]{n});
        }
        view.setEvaluator((TypeEvaluator)ArgbEvaluatorCompat.getInstance());
        fabTransformationSpec.timings.getTiming("color").apply((Animator)view);
        list.add((Animator)view);
    }

    private void createElevationAnimation(View view, View view2, boolean bl, boolean bl2, FabTransformationSpec fabTransformationSpec, List<Animator> list, List<Animator.AnimatorListener> list2) {
        float f = ViewCompat.getElevation(view2) - ViewCompat.getElevation(view);
        if (bl) {
            if (!bl2) {
                view2.setTranslationZ(-f);
            }
            view = ObjectAnimator.ofFloat((Object)view2, (Property)View.TRANSLATION_Z, (float[])new float[]{0.0f});
        } else {
            view = ObjectAnimator.ofFloat((Object)view2, (Property)View.TRANSLATION_Z, (float[])new float[]{-f});
        }
        fabTransformationSpec.timings.getTiming("elevation").apply((Animator)view);
        list.add((Animator)view);
    }

    private void createExpansionAnimation(View view, View view2, boolean bl, boolean bl2, FabTransformationSpec fabTransformationSpec, float f, float f2, List<Animator> list, List<Animator.AnimatorListener> list2) {
        if (!(view2 instanceof CircularRevealWidget)) {
            return;
        }
        CircularRevealWidget circularRevealWidget = (CircularRevealWidget)view2;
        float f3 = this.calculateRevealCenterX(view, view2, fabTransformationSpec.positioning);
        float f4 = this.calculateRevealCenterY(view, view2, fabTransformationSpec.positioning);
        ((FloatingActionButton)view).getContentRect(this.tmpRect);
        float f5 = (float)this.tmpRect.width() / 2.0f;
        MotionTiming motionTiming = fabTransformationSpec.timings.getTiming("expansion");
        if (bl) {
            if (!bl2) {
                circularRevealWidget.setRevealInfo(new CircularRevealWidget.RevealInfo(f3, f4, f5));
            }
            if (bl2) {
                f5 = circularRevealWidget.getRevealInfo().radius;
            }
            f = MathUtils.distanceToFurthestCorner(f3, f4, 0.0f, 0.0f, f, f2);
            view = CircularRevealCompat.createCircularReveal(circularRevealWidget, f3, f4, f);
            view.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this, circularRevealWidget){
                final FabTransformationBehavior this$0;
                final CircularRevealWidget val$circularRevealChild;
                {
                    this.this$0 = fabTransformationBehavior;
                    this.val$circularRevealChild = circularRevealWidget;
                }

                public void onAnimationEnd(Animator object) {
                    object = this.val$circularRevealChild.getRevealInfo();
                    object.radius = Float.MAX_VALUE;
                    this.val$circularRevealChild.setRevealInfo((CircularRevealWidget.RevealInfo)object);
                }
            });
            this.createPreFillRadialExpansion(view2, motionTiming.getDelay(), (int)f3, (int)f4, f5, list);
        } else {
            f = circularRevealWidget.getRevealInfo().radius;
            view = CircularRevealCompat.createCircularReveal(circularRevealWidget, f3, f4, f5);
            this.createPreFillRadialExpansion(view2, motionTiming.getDelay(), (int)f3, (int)f4, f, list);
            this.createPostFillRadialExpansion(view2, motionTiming.getDelay(), motionTiming.getDuration(), fabTransformationSpec.timings.getTotalDuration(), (int)f3, (int)f4, f5, list);
        }
        motionTiming.apply((Animator)view);
        list.add((Animator)view);
        list2.add(CircularRevealCompat.createCircularRevealListener(circularRevealWidget));
    }

    private void createIconFadeAnimation(View view, View view2, boolean bl, boolean bl2, FabTransformationSpec fabTransformationSpec, List<Animator> list, List<Animator.AnimatorListener> list2) {
        if (view2 instanceof CircularRevealWidget && view instanceof ImageView) {
            CircularRevealWidget circularRevealWidget = (CircularRevealWidget)view2;
            Drawable drawable2 = ((ImageView)view).getDrawable();
            if (drawable2 == null) {
                return;
            }
            drawable2.mutate();
            if (bl) {
                if (!bl2) {
                    drawable2.setAlpha(255);
                }
                view = ObjectAnimator.ofInt((Object)drawable2, DrawableAlphaProperty.DRAWABLE_ALPHA_COMPAT, (int[])new int[]{0});
            } else {
                view = ObjectAnimator.ofInt((Object)drawable2, DrawableAlphaProperty.DRAWABLE_ALPHA_COMPAT, (int[])new int[]{255});
            }
            view.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this, view2){
                final FabTransformationBehavior this$0;
                final View val$child;
                {
                    this.this$0 = fabTransformationBehavior;
                    this.val$child = view;
                }

                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    this.val$child.invalidate();
                }
            });
            fabTransformationSpec.timings.getTiming("iconFade").apply((Animator)view);
            list.add((Animator)view);
            list2.add((Animator.AnimatorListener)new AnimatorListenerAdapter(this, circularRevealWidget, drawable2){
                final FabTransformationBehavior this$0;
                final CircularRevealWidget val$circularRevealChild;
                final Drawable val$icon;
                {
                    this.this$0 = fabTransformationBehavior;
                    this.val$circularRevealChild = circularRevealWidget;
                    this.val$icon = drawable2;
                }

                public void onAnimationEnd(Animator animator2) {
                    this.val$circularRevealChild.setCircularRevealOverlayDrawable(null);
                }

                public void onAnimationStart(Animator animator2) {
                    this.val$circularRevealChild.setCircularRevealOverlayDrawable(this.val$icon);
                }
            });
            return;
        }
    }

    private void createPostFillRadialExpansion(View view, long l, long l2, long l3, int n, int n2, float f, List<Animator> list) {
        if (Build.VERSION.SDK_INT >= 21 && l + l2 < l3) {
            view = ViewAnimationUtils.createCircularReveal((View)view, (int)n, (int)n2, (float)f, (float)f);
            view.setStartDelay(l + l2);
            view.setDuration(l3 - (l + l2));
            list.add((Animator)view);
        }
    }

    private void createPreFillRadialExpansion(View view, long l, int n, int n2, float f, List<Animator> list) {
        if (Build.VERSION.SDK_INT >= 21 && l > 0L) {
            view = ViewAnimationUtils.createCircularReveal((View)view, (int)n, (int)n2, (float)f, (float)f);
            view.setStartDelay(0L);
            view.setDuration(l);
            list.add((Animator)view);
        }
    }

    private void createTranslationAnimation(View object, View view, boolean bl, boolean bl2, FabTransformationSpec fabTransformationSpec, List<Animator> list, List<Animator.AnimatorListener> object2, RectF rectF) {
        float f = this.calculateTranslationX((View)object, view, fabTransformationSpec.positioning);
        float f2 = this.calculateTranslationY((View)object, view, fabTransformationSpec.positioning);
        if (f != 0.0f && f2 != 0.0f) {
            if (bl && f2 < 0.0f || !bl && f2 > 0.0f) {
                object2 = fabTransformationSpec.timings.getTiming("translationXCurveUpwards");
                object = fabTransformationSpec.timings.getTiming("translationYCurveUpwards");
            } else {
                object2 = fabTransformationSpec.timings.getTiming("translationXCurveDownwards");
                object = fabTransformationSpec.timings.getTiming("translationYCurveDownwards");
            }
        } else {
            object2 = fabTransformationSpec.timings.getTiming("translationXLinear");
            object = fabTransformationSpec.timings.getTiming("translationYLinear");
        }
        if (bl) {
            if (!bl2) {
                view.setTranslationX(-f);
                view.setTranslationY(-f2);
            }
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat((Object)view, (Property)View.TRANSLATION_X, (float[])new float[]{0.0f});
            ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat((Object)view, (Property)View.TRANSLATION_Y, (float[])new float[]{0.0f});
            this.calculateChildVisibleBoundsAtEndOfExpansion(view, fabTransformationSpec, (MotionTiming)object2, (MotionTiming)object, -f, -f2, 0.0f, 0.0f, rectF);
            view = objectAnimator;
            fabTransformationSpec = objectAnimator2;
        } else {
            rectF = ObjectAnimator.ofFloat((Object)view, (Property)View.TRANSLATION_X, (float[])new float[]{-f});
            fabTransformationSpec = ObjectAnimator.ofFloat((Object)view, (Property)View.TRANSLATION_Y, (float[])new float[]{-f2});
            view = rectF;
        }
        ((MotionTiming)object2).apply((Animator)view);
        ((MotionTiming)object).apply((Animator)fabTransformationSpec);
        list.add((Animator)view);
        list.add((Animator)fabTransformationSpec);
    }

    private int getBackgroundTint(View view) {
        ColorStateList colorStateList = ViewCompat.getBackgroundTintList(view);
        if (colorStateList != null) {
            return colorStateList.getColorForState(view.getDrawableState(), colorStateList.getDefaultColor());
        }
        return 0;
    }

    private ViewGroup toViewGroupOrNull(View view) {
        if (view instanceof ViewGroup) {
            return (ViewGroup)view;
        }
        return null;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout coordinatorLayout, View view, View view2) {
        if (view.getVisibility() != 8) {
            boolean bl = view2 instanceof FloatingActionButton;
            boolean bl2 = false;
            if (bl) {
                int n = ((FloatingActionButton)view2).getExpandedComponentIdHint();
                if (n == 0 || n == view.getId()) {
                    bl2 = true;
                }
                return bl2;
            }
            return false;
        }
        throw new IllegalStateException("This behavior cannot be attached to a GONE view. Set the view to INVISIBLE instead.");
    }

    @Override
    public void onAttachedToLayoutParams(CoordinatorLayout.LayoutParams layoutParams) {
        if (layoutParams.dodgeInsetEdges == 0) {
            layoutParams.dodgeInsetEdges = 80;
        }
    }

    @Override
    protected AnimatorSet onCreateExpandedStateChangeAnimation(View view, View view2, boolean bl, boolean bl2) {
        FabTransformationSpec fabTransformationSpec = this.onCreateMotionSpec(view2.getContext(), bl);
        ArrayList<Animator> arrayList = new ArrayList<Animator>();
        ArrayList<Animator.AnimatorListener> arrayList2 = new ArrayList<Animator.AnimatorListener>();
        if (Build.VERSION.SDK_INT >= 21) {
            this.createElevationAnimation(view, view2, bl, bl2, fabTransformationSpec, arrayList, arrayList2);
        }
        RectF rectF = this.tmpRectF1;
        this.createTranslationAnimation(view, view2, bl, bl2, fabTransformationSpec, arrayList, arrayList2, rectF);
        float f = rectF.width();
        float f2 = rectF.height();
        this.createIconFadeAnimation(view, view2, bl, bl2, fabTransformationSpec, arrayList, arrayList2);
        this.createExpansionAnimation(view, view2, bl, bl2, fabTransformationSpec, f, f2, arrayList, arrayList2);
        this.createColorAnimation(view, view2, bl, bl2, fabTransformationSpec, arrayList, arrayList2);
        this.createChildrenFadeAnimation(view, view2, bl, bl2, fabTransformationSpec, arrayList, arrayList2);
        rectF = new AnimatorSet();
        AnimatorSetCompat.playTogether((AnimatorSet)rectF, arrayList);
        rectF.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this, bl, view2, view){
            final FabTransformationBehavior this$0;
            final View val$child;
            final View val$dependency;
            final boolean val$expanded;
            {
                this.this$0 = fabTransformationBehavior;
                this.val$expanded = bl;
                this.val$child = view;
                this.val$dependency = view2;
            }

            public void onAnimationEnd(Animator animator2) {
                if (!this.val$expanded) {
                    this.val$child.setVisibility(4);
                    this.val$dependency.setAlpha(1.0f);
                    this.val$dependency.setVisibility(0);
                }
            }

            public void onAnimationStart(Animator animator2) {
                if (this.val$expanded) {
                    this.val$child.setVisibility(0);
                    this.val$dependency.setAlpha(0.0f);
                    this.val$dependency.setVisibility(4);
                }
            }
        });
        int n = arrayList2.size();
        for (int i = 0; i < n; ++i) {
            rectF.addListener((Animator.AnimatorListener)arrayList2.get(i));
        }
        return rectF;
    }

    protected abstract FabTransformationSpec onCreateMotionSpec(Context var1, boolean var2);

    protected static class FabTransformationSpec {
        public Positioning positioning;
        public MotionSpec timings;

        protected FabTransformationSpec() {
        }
    }
}

