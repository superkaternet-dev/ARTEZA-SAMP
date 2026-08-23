/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.AnimatorSet
 *  android.animation.ObjectAnimator
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.TypedArray
 *  android.graphics.Paint$Style
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.os.Parcelable$Creator
 *  android.util.AttributeSet
 *  android.view.View
 */
package com.google.android.material.bottomappbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.EdgeTreatment;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapePathModel;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BottomAppBar
extends Toolbar
implements CoordinatorLayout.AttachedBehavior {
    private static final long ANIMATION_DURATION = 300L;
    public static final int FAB_ALIGNMENT_MODE_CENTER = 0;
    public static final int FAB_ALIGNMENT_MODE_END = 1;
    private Animator attachAnimator;
    private int fabAlignmentMode;
    AnimatorListenerAdapter fabAnimationListener = new AnimatorListenerAdapter(this){
        final BottomAppBar this$0;
        {
            this.this$0 = bottomAppBar;
        }

        public void onAnimationStart(Animator object) {
            object = this.this$0;
            ((BottomAppBar)object).maybeAnimateAttachChange(((BottomAppBar)object).fabAttached);
            object = this.this$0;
            ((BottomAppBar)object).maybeAnimateMenuView(((BottomAppBar)object).fabAlignmentMode, this.this$0.fabAttached);
        }
    };
    private boolean fabAttached = true;
    private final int fabOffsetEndMode;
    private boolean hideOnScroll;
    private final MaterialShapeDrawable materialShapeDrawable;
    private Animator menuAnimator;
    private Animator modeAnimator;
    private final BottomAppBarTopEdgeTreatment topEdgeTreatment;

    public BottomAppBar(Context context) {
        this(context, null, 0);
    }

    public BottomAppBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.bottomAppBarStyle);
    }

    public BottomAppBar(Context context, AttributeSet object, int n) {
        super(context, (AttributeSet)object, n);
        object = ThemeEnforcement.obtainStyledAttributes(context, (AttributeSet)object, R.styleable.BottomAppBar, n, R.style.Widget_MaterialComponents_BottomAppBar, new int[0]);
        context = MaterialResources.getColorStateList(context, (TypedArray)object, R.styleable.BottomAppBar_backgroundTint);
        float f = object.getDimensionPixelOffset(R.styleable.BottomAppBar_fabCradleMargin, 0);
        float f2 = object.getDimensionPixelOffset(R.styleable.BottomAppBar_fabCradleRoundedCornerRadius, 0);
        float f3 = object.getDimensionPixelOffset(R.styleable.BottomAppBar_fabCradleVerticalOffset, 0);
        this.fabAlignmentMode = object.getInt(R.styleable.BottomAppBar_fabAlignmentMode, 0);
        this.hideOnScroll = object.getBoolean(R.styleable.BottomAppBar_hideOnScroll, false);
        object.recycle();
        this.fabOffsetEndMode = this.getResources().getDimensionPixelOffset(R.dimen.mtrl_bottomappbar_fabOffsetEndMode);
        object = new BottomAppBarTopEdgeTreatment(f, f2, f3);
        this.topEdgeTreatment = object;
        ShapePathModel shapePathModel = new ShapePathModel();
        shapePathModel.setTopEdge((EdgeTreatment)object);
        object = new MaterialShapeDrawable(shapePathModel);
        this.materialShapeDrawable = object;
        ((MaterialShapeDrawable)object).setShadowEnabled(true);
        ((MaterialShapeDrawable)object).setPaintStyle(Paint.Style.FILL);
        DrawableCompat.setTintList((Drawable)object, (ColorStateList)context);
        ViewCompat.setBackground((View)this, (Drawable)object);
    }

    static /* synthetic */ Animator access$002(BottomAppBar bottomAppBar, Animator animator2) {
        bottomAppBar.modeAnimator = animator2;
        return animator2;
    }

    static /* synthetic */ Animator access$302(BottomAppBar bottomAppBar, Animator animator2) {
        bottomAppBar.menuAnimator = animator2;
        return animator2;
    }

    static /* synthetic */ Animator access$502(BottomAppBar bottomAppBar, Animator animator2) {
        bottomAppBar.attachAnimator = animator2;
        return animator2;
    }

    private void addFabAnimationListeners(FloatingActionButton floatingActionButton) {
        this.removeFabAnimationListeners(floatingActionButton);
        floatingActionButton.addOnHideAnimationListener((Animator.AnimatorListener)this.fabAnimationListener);
        floatingActionButton.addOnShowAnimationListener((Animator.AnimatorListener)this.fabAnimationListener);
    }

    private void cancelAnimations() {
        Animator animator2 = this.attachAnimator;
        if (animator2 != null) {
            animator2.cancel();
        }
        if ((animator2 = this.menuAnimator) != null) {
            animator2.cancel();
        }
        if ((animator2 = this.modeAnimator) != null) {
            animator2.cancel();
        }
    }

    private void createCradleShapeAnimation(boolean bl, List<Animator> list) {
        if (bl) {
            this.topEdgeTreatment.setHorizontalOffset(this.getFabTranslationX());
        }
        float f = this.materialShapeDrawable.getInterpolation();
        float f2 = bl ? 1.0f : 0.0f;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat((float[])new float[]{f, f2});
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this){
            final BottomAppBar this$0;
            {
                this.this$0 = bottomAppBar;
            }

            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                this.this$0.materialShapeDrawable.setInterpolation(((Float)valueAnimator.getAnimatedValue()).floatValue());
            }
        });
        valueAnimator.setDuration(300L);
        list.add((Animator)valueAnimator);
    }

    private void createCradleTranslationAnimation(int n, List<Animator> list) {
        if (!this.fabAttached) {
            return;
        }
        ValueAnimator valueAnimator = ValueAnimator.ofFloat((float[])new float[]{this.topEdgeTreatment.getHorizontalOffset(), this.getFabTranslationX(n)});
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this){
            final BottomAppBar this$0;
            {
                this.this$0 = bottomAppBar;
            }

            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                this.this$0.topEdgeTreatment.setHorizontalOffset(((Float)valueAnimator.getAnimatedValue()).floatValue());
                this.this$0.materialShapeDrawable.invalidateSelf();
            }
        });
        valueAnimator.setDuration(300L);
        list.add((Animator)valueAnimator);
    }

    private void createFabTranslationXAnimation(int n, List<Animator> list) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat((Object)this.findDependentFab(), (String)"translationX", (float[])new float[]{this.getFabTranslationX(n)});
        objectAnimator.setDuration(300L);
        list.add((Animator)objectAnimator);
    }

    private void createFabTranslationYAnimation(boolean bl, List<Animator> list) {
        FloatingActionButton floatingActionButton = this.findDependentFab();
        if (floatingActionButton == null) {
            return;
        }
        floatingActionButton = ObjectAnimator.ofFloat((Object)floatingActionButton, (String)"translationY", (float[])new float[]{this.getFabTranslationY(bl)});
        floatingActionButton.setDuration(300L);
        list.add((Animator)floatingActionButton);
    }

    private void createMenuViewTranslationAnimation(int n, boolean bl, List<Animator> list) {
        ActionMenuView actionMenuView = this.getActionMenuView();
        if (actionMenuView == null) {
            return;
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat((Object)actionMenuView, (String)"alpha", (float[])new float[]{1.0f});
        if (!this.fabAttached && (!bl || !this.isVisibleFab()) || this.fabAlignmentMode != 1 && n != 1) {
            if (actionMenuView.getAlpha() < 1.0f) {
                list.add((Animator)objectAnimator);
            }
        } else {
            ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat((Object)actionMenuView, (String)"alpha", (float[])new float[]{0.0f});
            objectAnimator2.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this, actionMenuView, n, bl){
                public boolean cancelled;
                final BottomAppBar this$0;
                final ActionMenuView val$actionMenuView;
                final boolean val$targetAttached;
                final int val$targetMode;
                {
                    this.this$0 = bottomAppBar;
                    this.val$actionMenuView = actionMenuView;
                    this.val$targetMode = n;
                    this.val$targetAttached = bl;
                }

                public void onAnimationCancel(Animator animator2) {
                    this.cancelled = true;
                }

                public void onAnimationEnd(Animator animator2) {
                    if (!this.cancelled) {
                        this.this$0.translateActionMenuView(this.val$actionMenuView, this.val$targetMode, this.val$targetAttached);
                    }
                }
            });
            actionMenuView = new AnimatorSet();
            actionMenuView.setDuration(150L);
            actionMenuView.playSequentially(new Animator[]{objectAnimator2, objectAnimator});
            list.add((Animator)actionMenuView);
        }
    }

    private FloatingActionButton findDependentFab() {
        if (!(this.getParent() instanceof CoordinatorLayout)) {
            return null;
        }
        for (View view : ((CoordinatorLayout)this.getParent()).getDependents((View)this)) {
            if (!(view instanceof FloatingActionButton)) continue;
            return (FloatingActionButton)view;
        }
        return null;
    }

    private ActionMenuView getActionMenuView() {
        for (int i = 0; i < this.getChildCount(); ++i) {
            View view = this.getChildAt(i);
            if (!(view instanceof ActionMenuView)) continue;
            return (ActionMenuView)view;
        }
        return null;
    }

    private float getFabTranslationX() {
        return this.getFabTranslationX(this.fabAlignmentMode);
    }

    private int getFabTranslationX(int n) {
        int n2 = ViewCompat.getLayoutDirection((View)this);
        int n3 = 0;
        int n4 = 1;
        n2 = n2 == 1 ? 1 : 0;
        if (n == 1) {
            n3 = this.getMeasuredWidth() / 2;
            int n5 = this.fabOffsetEndMode;
            n = n4;
            if (n2 != 0) {
                n = -1;
            }
            n = (n3 - n5) * n;
        } else {
            n = n3;
        }
        return n;
    }

    private float getFabTranslationY() {
        return this.getFabTranslationY(this.fabAttached);
    }

    private float getFabTranslationY(boolean bl) {
        float f;
        FloatingActionButton floatingActionButton = this.findDependentFab();
        if (floatingActionButton == null) {
            return 0.0f;
        }
        Rect rect = new Rect();
        floatingActionButton.getContentRect(rect);
        float f2 = f = (float)rect.height();
        if (f == 0.0f) {
            f2 = floatingActionButton.getMeasuredHeight();
        }
        float f3 = floatingActionButton.getHeight() - rect.bottom;
        float f4 = floatingActionButton.getHeight() - rect.height();
        float f5 = -this.getCradleVerticalOffset();
        float f6 = f2 / 2.0f;
        f2 = floatingActionButton.getPaddingBottom();
        f = -this.getMeasuredHeight();
        f2 = bl ? f5 + f6 + f3 : f4 - f2;
        return f + f2;
    }

    private boolean isAnimationRunning() {
        Animator animator2 = this.attachAnimator;
        boolean bl = animator2 != null && animator2.isRunning() || (animator2 = this.menuAnimator) != null && animator2.isRunning() || (animator2 = this.modeAnimator) != null && animator2.isRunning();
        return bl;
    }

    private boolean isVisibleFab() {
        FloatingActionButton floatingActionButton = this.findDependentFab();
        boolean bl = floatingActionButton != null && floatingActionButton.isOrWillBeShown();
        return bl;
    }

    private void maybeAnimateAttachChange(boolean bl) {
        if (!ViewCompat.isLaidOut((View)this)) {
            return;
        }
        Animator animator2 = this.attachAnimator;
        if (animator2 != null) {
            animator2.cancel();
        }
        ArrayList<Animator> arrayList = new ArrayList<Animator>();
        boolean bl2 = bl && this.isVisibleFab();
        this.createCradleShapeAnimation(bl2, arrayList);
        this.createFabTranslationYAnimation(bl, arrayList);
        animator2 = new AnimatorSet();
        animator2.playTogether(arrayList);
        this.attachAnimator = animator2;
        animator2.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this){
            final BottomAppBar this$0;
            {
                this.this$0 = bottomAppBar;
            }

            public void onAnimationEnd(Animator animator2) {
                BottomAppBar.access$502(this.this$0, null);
            }
        });
        this.attachAnimator.start();
    }

    private void maybeAnimateMenuView(int n, boolean bl) {
        if (!ViewCompat.isLaidOut((View)this)) {
            return;
        }
        Object object = this.menuAnimator;
        if (object != null) {
            object.cancel();
        }
        object = new ArrayList();
        if (!this.isVisibleFab()) {
            n = 0;
            bl = false;
        }
        this.createMenuViewTranslationAnimation(n, bl, (List<Animator>)object);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether((Collection)object);
        this.menuAnimator = animatorSet;
        animatorSet.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this){
            final BottomAppBar this$0;
            {
                this.this$0 = bottomAppBar;
            }

            public void onAnimationEnd(Animator animator2) {
                BottomAppBar.access$302(this.this$0, null);
            }
        });
        this.menuAnimator.start();
    }

    private void maybeAnimateModeChange(int n) {
        if (this.fabAlignmentMode != n && ViewCompat.isLaidOut((View)this)) {
            Animator animator2 = this.modeAnimator;
            if (animator2 != null) {
                animator2.cancel();
            }
            ArrayList<Animator> arrayList = new ArrayList<Animator>();
            this.createCradleTranslationAnimation(n, arrayList);
            this.createFabTranslationXAnimation(n, arrayList);
            animator2 = new AnimatorSet();
            animator2.playTogether(arrayList);
            this.modeAnimator = animator2;
            animator2.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this){
                final BottomAppBar this$0;
                {
                    this.this$0 = bottomAppBar;
                }

                public void onAnimationEnd(Animator animator2) {
                    BottomAppBar.access$002(this.this$0, null);
                }
            });
            this.modeAnimator.start();
            return;
        }
    }

    private void removeFabAnimationListeners(FloatingActionButton floatingActionButton) {
        floatingActionButton.removeOnHideAnimationListener((Animator.AnimatorListener)this.fabAnimationListener);
        floatingActionButton.removeOnShowAnimationListener((Animator.AnimatorListener)this.fabAnimationListener);
    }

    private void setCutoutState() {
        this.topEdgeTreatment.setHorizontalOffset(this.getFabTranslationX());
        FloatingActionButton floatingActionButton = this.findDependentFab();
        Object object = this.materialShapeDrawable;
        float f = this.fabAttached && this.isVisibleFab() ? 1.0f : 0.0f;
        ((MaterialShapeDrawable)object).setInterpolation(f);
        if (floatingActionButton != null) {
            floatingActionButton.setTranslationY(this.getFabTranslationY());
            floatingActionButton.setTranslationX(this.getFabTranslationX());
        }
        if ((object = this.getActionMenuView()) != null) {
            object.setAlpha(1.0f);
            if (!this.isVisibleFab()) {
                this.translateActionMenuView((ActionMenuView)object, 0, false);
            } else {
                this.translateActionMenuView((ActionMenuView)object, this.fabAlignmentMode, this.fabAttached);
            }
        }
    }

    private void translateActionMenuView(ActionMenuView actionMenuView, int n, boolean bl) {
        int n2 = 0;
        int n3 = ViewCompat.getLayoutDirection((View)this) == 1 ? 1 : 0;
        for (int i = 0; i < this.getChildCount(); ++i) {
            View view = this.getChildAt(i);
            boolean bl2 = view.getLayoutParams() instanceof Toolbar.LayoutParams && (((Toolbar.LayoutParams)view.getLayoutParams()).gravity & 0x800007) == 0x800003;
            int n4 = n2;
            if (bl2) {
                n4 = n3 != 0 ? view.getLeft() : view.getRight();
                n4 = Math.max(n2, n4);
            }
            n2 = n4;
        }
        n3 = n3 != 0 ? actionMenuView.getRight() : actionMenuView.getLeft();
        float f = n == 1 && bl ? (float)(n2 - n3) : 0.0f;
        actionMenuView.setTranslationX(f);
    }

    public ColorStateList getBackgroundTint() {
        return this.materialShapeDrawable.getTintList();
    }

    @Override
    public CoordinatorLayout.Behavior<BottomAppBar> getBehavior() {
        return new Behavior();
    }

    public float getCradleVerticalOffset() {
        return this.topEdgeTreatment.getCradleVerticalOffset();
    }

    public int getFabAlignmentMode() {
        return this.fabAlignmentMode;
    }

    public float getFabCradleMargin() {
        return this.topEdgeTreatment.getFabCradleMargin();
    }

    public float getFabCradleRoundedCornerRadius() {
        return this.topEdgeTreatment.getFabCradleRoundedCornerRadius();
    }

    public boolean getHideOnScroll() {
        return this.hideOnScroll;
    }

    @Override
    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        super.onLayout(bl, n, n2, n3, n4);
        this.cancelAnimations();
        this.setCutoutState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        parcelable = (SavedState)parcelable;
        super.onRestoreInstanceState(parcelable.getSuperState());
        this.fabAlignmentMode = parcelable.fabAlignmentMode;
        this.fabAttached = parcelable.fabAttached;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.fabAlignmentMode = this.fabAlignmentMode;
        savedState.fabAttached = this.fabAttached;
        return savedState;
    }

    public void replaceMenu(int n) {
        this.getMenu().clear();
        this.inflateMenu(n);
    }

    public void setBackgroundTint(ColorStateList colorStateList) {
        DrawableCompat.setTintList(this.materialShapeDrawable, colorStateList);
    }

    public void setCradleVerticalOffset(float f) {
        if (f != this.getCradleVerticalOffset()) {
            this.topEdgeTreatment.setCradleVerticalOffset(f);
            this.materialShapeDrawable.invalidateSelf();
        }
    }

    public void setFabAlignmentMode(int n) {
        this.maybeAnimateModeChange(n);
        this.maybeAnimateMenuView(n, this.fabAttached);
        this.fabAlignmentMode = n;
    }

    public void setFabCradleMargin(float f) {
        if (f != this.getFabCradleMargin()) {
            this.topEdgeTreatment.setFabCradleMargin(f);
            this.materialShapeDrawable.invalidateSelf();
        }
    }

    public void setFabCradleRoundedCornerRadius(float f) {
        if (f != this.getFabCradleRoundedCornerRadius()) {
            this.topEdgeTreatment.setFabCradleRoundedCornerRadius(f);
            this.materialShapeDrawable.invalidateSelf();
        }
    }

    void setFabDiameter(int n) {
        if ((float)n != this.topEdgeTreatment.getFabDiameter()) {
            this.topEdgeTreatment.setFabDiameter(n);
            this.materialShapeDrawable.invalidateSelf();
        }
    }

    public void setHideOnScroll(boolean bl) {
        this.hideOnScroll = bl;
    }

    @Override
    public void setSubtitle(CharSequence charSequence) {
    }

    @Override
    public void setTitle(CharSequence charSequence) {
    }

    public static class Behavior
    extends HideBottomViewOnScrollBehavior<BottomAppBar> {
        private final Rect fabContentRect = new Rect();

        public Behavior() {
        }

        public Behavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        private boolean updateFabPositionAndVisibility(FloatingActionButton floatingActionButton, BottomAppBar bottomAppBar) {
            ((CoordinatorLayout.LayoutParams)floatingActionButton.getLayoutParams()).anchorGravity = 17;
            bottomAppBar.addFabAnimationListeners(floatingActionButton);
            return true;
        }

        @Override
        public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, BottomAppBar bottomAppBar, int n) {
            FloatingActionButton floatingActionButton = bottomAppBar.findDependentFab();
            if (floatingActionButton != null) {
                this.updateFabPositionAndVisibility(floatingActionButton, bottomAppBar);
                floatingActionButton.getMeasuredContentRect(this.fabContentRect);
                bottomAppBar.setFabDiameter(this.fabContentRect.height());
            }
            if (!bottomAppBar.isAnimationRunning()) {
                bottomAppBar.setCutoutState();
            }
            coordinatorLayout.onLayoutChild((View)bottomAppBar, n);
            return super.onLayoutChild(coordinatorLayout, bottomAppBar, n);
        }

        @Override
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, BottomAppBar bottomAppBar, View view, View view2, int n, int n2) {
            boolean bl = bottomAppBar.getHideOnScroll() && super.onStartNestedScroll(coordinatorLayout, bottomAppBar, view, view2, n, n2);
            return bl;
        }

        @Override
        protected void slideDown(BottomAppBar object) {
            super.slideDown(object);
            object = ((BottomAppBar)object).findDependentFab();
            if (object != null) {
                ((FloatingActionButton)object).getContentRect(this.fabContentRect);
                float f = object.getMeasuredHeight() - this.fabContentRect.height();
                object.clearAnimation();
                object.animate().translationY((float)(-object.getPaddingBottom()) + f).setInterpolator(AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR).setDuration(175L);
            }
        }

        @Override
        protected void slideUp(BottomAppBar bottomAppBar) {
            super.slideUp(bottomAppBar);
            FloatingActionButton floatingActionButton = bottomAppBar.findDependentFab();
            if (floatingActionButton != null) {
                floatingActionButton.clearAnimation();
                floatingActionButton.animate().translationY(bottomAppBar.getFabTranslationY()).setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR).setDuration(225L);
            }
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface FabAlignmentMode {
    }

    static class SavedState
    extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        int fabAlignmentMode;
        boolean fabAttached;

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.fabAlignmentMode = parcel.readInt();
            boolean bl = parcel.readInt() != 0;
            this.fabAttached = bl;
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.fabAlignmentMode);
            parcel.writeInt(this.fabAttached ? 1 : 0);
        }
    }
}

