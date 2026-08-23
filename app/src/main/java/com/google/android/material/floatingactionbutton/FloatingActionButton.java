/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator$AnimatorListener
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.graphics.ColorFilter
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.os.Parcelable
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.widget.ImageView
 *  android.widget.ImageView$ScaleType
 */
package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.appcompat.widget.AppCompatImageHelper;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.TintableBackgroundView;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TintableImageSourceView;
import com.google.android.material.R;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.expandable.ExpandableTransformationWidget;
import com.google.android.material.expandable.ExpandableWidgetHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButtonImpl;
import com.google.android.material.floatingactionbutton.FloatingActionButtonImplLollipop;
import com.google.android.material.internal.DescendantOffsetUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.internal.VisibilityAwareImageButton;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shadow.ShadowViewDelegate;
import com.google.android.material.stateful.ExtendableSavedState;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

@CoordinatorLayout.DefaultBehavior(value=Behavior.class)
public class FloatingActionButton
extends VisibilityAwareImageButton
implements TintableBackgroundView,
TintableImageSourceView,
ExpandableTransformationWidget {
    private static final int AUTO_MINI_LARGEST_SCREEN_WIDTH = 470;
    private static final String EXPANDABLE_WIDGET_HELPER_KEY = "expandableWidgetHelper";
    private static final String LOG_TAG = "FloatingActionButton";
    public static final int NO_CUSTOM_SIZE = 0;
    public static final int SIZE_AUTO = -1;
    public static final int SIZE_MINI = 1;
    public static final int SIZE_NORMAL = 0;
    private ColorStateList backgroundTint;
    private PorterDuff.Mode backgroundTintMode;
    private int borderWidth;
    boolean compatPadding;
    private int customSize;
    private final ExpandableWidgetHelper expandableWidgetHelper;
    private final AppCompatImageHelper imageHelper;
    private PorterDuff.Mode imageMode;
    private int imagePadding;
    private ColorStateList imageTint;
    private FloatingActionButtonImpl impl;
    private int maxImageSize;
    private ColorStateList rippleColor;
    final Rect shadowPadding = new Rect();
    private int size;
    private final Rect touchArea = new Rect();

    public FloatingActionButton(Context context) {
        this(context, null);
    }

    public FloatingActionButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.floatingActionButtonStyle);
    }

    public FloatingActionButton(Context object, AttributeSet attributeSet, int n) {
        super((Context)object, attributeSet, n);
        Object object2 = ThemeEnforcement.obtainStyledAttributes(object, attributeSet, R.styleable.FloatingActionButton, n, R.style.Widget_Design_FloatingActionButton, new int[0]);
        this.backgroundTint = MaterialResources.getColorStateList(object, (TypedArray)object2, R.styleable.FloatingActionButton_backgroundTint);
        this.backgroundTintMode = ViewUtils.parseTintMode(object2.getInt(R.styleable.FloatingActionButton_backgroundTintMode, -1), null);
        this.rippleColor = MaterialResources.getColorStateList(object, (TypedArray)object2, R.styleable.FloatingActionButton_rippleColor);
        this.size = object2.getInt(R.styleable.FloatingActionButton_fabSize, -1);
        this.customSize = object2.getDimensionPixelSize(R.styleable.FloatingActionButton_fabCustomSize, 0);
        this.borderWidth = object2.getDimensionPixelSize(R.styleable.FloatingActionButton_borderWidth, 0);
        float f = object2.getDimension(R.styleable.FloatingActionButton_elevation, 0.0f);
        float f2 = object2.getDimension(R.styleable.FloatingActionButton_hoveredFocusedTranslationZ, 0.0f);
        float f3 = object2.getDimension(R.styleable.FloatingActionButton_pressedTranslationZ, 0.0f);
        this.compatPadding = object2.getBoolean(R.styleable.FloatingActionButton_useCompatPadding, false);
        this.maxImageSize = object2.getDimensionPixelSize(R.styleable.FloatingActionButton_maxImageSize, 0);
        MotionSpec motionSpec = MotionSpec.createFromAttribute(object, (TypedArray)object2, R.styleable.FloatingActionButton_showMotionSpec);
        object = MotionSpec.createFromAttribute(object, (TypedArray)object2, R.styleable.FloatingActionButton_hideMotionSpec);
        object2.recycle();
        object2 = new AppCompatImageHelper((ImageView)this);
        this.imageHelper = object2;
        ((AppCompatImageHelper)object2).loadFromAttributes(attributeSet, n);
        this.expandableWidgetHelper = new ExpandableWidgetHelper(this);
        this.getImpl().setBackgroundDrawable(this.backgroundTint, this.backgroundTintMode, this.rippleColor, this.borderWidth);
        this.getImpl().setElevation(f);
        this.getImpl().setHoveredFocusedTranslationZ(f2);
        this.getImpl().setPressedTranslationZ(f3);
        this.getImpl().setMaxImageSize(this.maxImageSize);
        this.getImpl().setShowMotionSpec(motionSpec);
        this.getImpl().setHideMotionSpec((MotionSpec)object);
        this.setScaleType(ImageView.ScaleType.MATRIX);
    }

    private FloatingActionButtonImpl createImpl() {
        if (Build.VERSION.SDK_INT >= 21) {
            return new FloatingActionButtonImplLollipop(this, new ShadowDelegateImpl(this));
        }
        return new FloatingActionButtonImpl(this, new ShadowDelegateImpl(this));
    }

    private FloatingActionButtonImpl getImpl() {
        if (this.impl == null) {
            this.impl = this.createImpl();
        }
        return this.impl;
    }

    private int getSizeDimension(int n) {
        int n2 = this.customSize;
        if (n2 != 0) {
            return n2;
        }
        Resources resources = this.getResources();
        switch (n) {
            default: {
                return resources.getDimensionPixelSize(R.dimen.design_fab_size_normal);
            }
            case 1: {
                return resources.getDimensionPixelSize(R.dimen.design_fab_size_mini);
            }
            case -1: 
        }
        n = Math.max(resources.getConfiguration().screenWidthDp, resources.getConfiguration().screenHeightDp) < 470 ? this.getSizeDimension(1) : this.getSizeDimension(0);
        return n;
    }

    private void offsetRectWithShadow(Rect rect) {
        rect.left += this.shadowPadding.left;
        rect.top += this.shadowPadding.top;
        rect.right -= this.shadowPadding.right;
        rect.bottom -= this.shadowPadding.bottom;
    }

    private void onApplySupportImageTint() {
        Drawable drawable2 = this.getDrawable();
        if (drawable2 == null) {
            return;
        }
        ColorStateList colorStateList = this.imageTint;
        if (colorStateList == null) {
            DrawableCompat.clearColorFilter(drawable2);
            return;
        }
        int n = colorStateList.getColorForState(this.getDrawableState(), 0);
        PorterDuff.Mode mode = this.imageMode;
        colorStateList = mode;
        if (mode == null) {
            colorStateList = PorterDuff.Mode.SRC_IN;
        }
        drawable2.mutate().setColorFilter((ColorFilter)AppCompatDrawableManager.getPorterDuffColorFilter(n, (PorterDuff.Mode)colorStateList));
    }

    private static int resolveAdjustedSize(int n, int n2) {
        int n3 = View.MeasureSpec.getMode((int)n2);
        n2 = View.MeasureSpec.getSize((int)n2);
        switch (n3) {
            default: {
                throw new IllegalArgumentException();
            }
            case 0x40000000: {
                n = n2;
                break;
            }
            case 0: {
                break;
            }
            case -2147483648: {
                n = Math.min(n, n2);
            }
        }
        return n;
    }

    private FloatingActionButtonImpl.InternalVisibilityChangedListener wrapOnVisibilityChangedListener(OnVisibilityChangedListener onVisibilityChangedListener) {
        if (onVisibilityChangedListener == null) {
            return null;
        }
        return new FloatingActionButtonImpl.InternalVisibilityChangedListener(this, onVisibilityChangedListener){
            final FloatingActionButton this$0;
            final OnVisibilityChangedListener val$listener;
            {
                this.this$0 = floatingActionButton;
                this.val$listener = onVisibilityChangedListener;
            }

            @Override
            public void onHidden() {
                this.val$listener.onHidden(this.this$0);
            }

            @Override
            public void onShown() {
                this.val$listener.onShown(this.this$0);
            }
        };
    }

    public void addOnHideAnimationListener(Animator.AnimatorListener animatorListener) {
        this.getImpl().addOnHideAnimationListener(animatorListener);
    }

    public void addOnShowAnimationListener(Animator.AnimatorListener animatorListener) {
        this.getImpl().addOnShowAnimationListener(animatorListener);
    }

    public void clearCustomSize() {
        this.setCustomSize(0);
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        this.getImpl().onDrawableStateChanged(this.getDrawableState());
    }

    public ColorStateList getBackgroundTintList() {
        return this.backgroundTint;
    }

    public PorterDuff.Mode getBackgroundTintMode() {
        return this.backgroundTintMode;
    }

    public float getCompatElevation() {
        return this.getImpl().getElevation();
    }

    public float getCompatHoveredFocusedTranslationZ() {
        return this.getImpl().getHoveredFocusedTranslationZ();
    }

    public float getCompatPressedTranslationZ() {
        return this.getImpl().getPressedTranslationZ();
    }

    public Drawable getContentBackground() {
        return this.getImpl().getContentBackground();
    }

    @Deprecated
    public boolean getContentRect(Rect rect) {
        if (ViewCompat.isLaidOut((View)this)) {
            rect.set(0, 0, this.getWidth(), this.getHeight());
            this.offsetRectWithShadow(rect);
            return true;
        }
        return false;
    }

    public int getCustomSize() {
        return this.customSize;
    }

    @Override
    public int getExpandedComponentIdHint() {
        return this.expandableWidgetHelper.getExpandedComponentIdHint();
    }

    public MotionSpec getHideMotionSpec() {
        return this.getImpl().getHideMotionSpec();
    }

    public void getMeasuredContentRect(Rect rect) {
        rect.set(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
        this.offsetRectWithShadow(rect);
    }

    @Deprecated
    public int getRippleColor() {
        ColorStateList colorStateList = this.rippleColor;
        int n = colorStateList != null ? colorStateList.getDefaultColor() : 0;
        return n;
    }

    public ColorStateList getRippleColorStateList() {
        return this.rippleColor;
    }

    public MotionSpec getShowMotionSpec() {
        return this.getImpl().getShowMotionSpec();
    }

    public int getSize() {
        return this.size;
    }

    int getSizeDimension() {
        return this.getSizeDimension(this.size);
    }

    @Override
    public ColorStateList getSupportBackgroundTintList() {
        return this.getBackgroundTintList();
    }

    @Override
    public PorterDuff.Mode getSupportBackgroundTintMode() {
        return this.getBackgroundTintMode();
    }

    @Override
    public ColorStateList getSupportImageTintList() {
        return this.imageTint;
    }

    @Override
    public PorterDuff.Mode getSupportImageTintMode() {
        return this.imageMode;
    }

    public boolean getUseCompatPadding() {
        return this.compatPadding;
    }

    public void hide() {
        this.hide(null);
    }

    public void hide(OnVisibilityChangedListener onVisibilityChangedListener) {
        this.hide(onVisibilityChangedListener, true);
    }

    void hide(OnVisibilityChangedListener onVisibilityChangedListener, boolean bl) {
        this.getImpl().hide(this.wrapOnVisibilityChangedListener(onVisibilityChangedListener), bl);
    }

    @Override
    public boolean isExpanded() {
        return this.expandableWidgetHelper.isExpanded();
    }

    public boolean isOrWillBeHidden() {
        return this.getImpl().isOrWillBeHidden();
    }

    public boolean isOrWillBeShown() {
        return this.getImpl().isOrWillBeShown();
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        this.getImpl().jumpDrawableToCurrentState();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.getImpl().onAttachedToWindow();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.getImpl().onDetachedFromWindow();
    }

    protected void onMeasure(int n, int n2) {
        int n3 = this.getSizeDimension();
        this.imagePadding = (n3 - this.maxImageSize) / 2;
        this.getImpl().updatePadding();
        n = Math.min(FloatingActionButton.resolveAdjustedSize(n3, n), FloatingActionButton.resolveAdjustedSize(n3, n2));
        this.setMeasuredDimension(this.shadowPadding.left + n + this.shadowPadding.right, this.shadowPadding.top + n + this.shadowPadding.bottom);
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof ExtendableSavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        parcelable = (ExtendableSavedState)parcelable;
        super.onRestoreInstanceState(parcelable.getSuperState());
        this.expandableWidgetHelper.onRestoreInstanceState(parcelable.extendableStates.get(EXPANDABLE_WIDGET_HELPER_KEY));
    }

    protected Parcelable onSaveInstanceState() {
        ExtendableSavedState extendableSavedState = new ExtendableSavedState(super.onSaveInstanceState());
        extendableSavedState.extendableStates.put(EXPANDABLE_WIDGET_HELPER_KEY, this.expandableWidgetHelper.onSaveInstanceState());
        return extendableSavedState;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0 && this.getContentRect(this.touchArea) && !this.touchArea.contains((int)motionEvent.getX(), (int)motionEvent.getY())) {
            return false;
        }
        return super.onTouchEvent(motionEvent);
    }

    public void removeOnHideAnimationListener(Animator.AnimatorListener animatorListener) {
        this.getImpl().removeOnHideAnimationListener(animatorListener);
    }

    public void removeOnShowAnimationListener(Animator.AnimatorListener animatorListener) {
        this.getImpl().removeOnShowAnimationListener(animatorListener);
    }

    public void setBackgroundColor(int n) {
        Log.i((String)LOG_TAG, (String)"Setting a custom background is not supported.");
    }

    public void setBackgroundDrawable(Drawable drawable2) {
        Log.i((String)LOG_TAG, (String)"Setting a custom background is not supported.");
    }

    public void setBackgroundResource(int n) {
        Log.i((String)LOG_TAG, (String)"Setting a custom background is not supported.");
    }

    public void setBackgroundTintList(ColorStateList colorStateList) {
        if (this.backgroundTint != colorStateList) {
            this.backgroundTint = colorStateList;
            this.getImpl().setBackgroundTintList(colorStateList);
        }
    }

    public void setBackgroundTintMode(PorterDuff.Mode mode) {
        if (this.backgroundTintMode != mode) {
            this.backgroundTintMode = mode;
            this.getImpl().setBackgroundTintMode(mode);
        }
    }

    public void setCompatElevation(float f) {
        this.getImpl().setElevation(f);
    }

    public void setCompatElevationResource(int n) {
        this.setCompatElevation(this.getResources().getDimension(n));
    }

    public void setCompatHoveredFocusedTranslationZ(float f) {
        this.getImpl().setHoveredFocusedTranslationZ(f);
    }

    public void setCompatHoveredFocusedTranslationZResource(int n) {
        this.setCompatHoveredFocusedTranslationZ(this.getResources().getDimension(n));
    }

    public void setCompatPressedTranslationZ(float f) {
        this.getImpl().setPressedTranslationZ(f);
    }

    public void setCompatPressedTranslationZResource(int n) {
        this.setCompatPressedTranslationZ(this.getResources().getDimension(n));
    }

    public void setCustomSize(int n) {
        if (n >= 0) {
            this.customSize = n;
            return;
        }
        throw new IllegalArgumentException("Custom size must be non-negative");
    }

    @Override
    public boolean setExpanded(boolean bl) {
        return this.expandableWidgetHelper.setExpanded(bl);
    }

    @Override
    public void setExpandedComponentIdHint(int n) {
        this.expandableWidgetHelper.setExpandedComponentIdHint(n);
    }

    public void setHideMotionSpec(MotionSpec motionSpec) {
        this.getImpl().setHideMotionSpec(motionSpec);
    }

    public void setHideMotionSpecResource(int n) {
        this.setHideMotionSpec(MotionSpec.createFromResource(this.getContext(), n));
    }

    public void setImageDrawable(Drawable drawable2) {
        super.setImageDrawable(drawable2);
        this.getImpl().updateImageMatrixScale();
    }

    public void setImageResource(int n) {
        this.imageHelper.setImageResource(n);
    }

    public void setRippleColor(int n) {
        this.setRippleColor(ColorStateList.valueOf((int)n));
    }

    public void setRippleColor(ColorStateList colorStateList) {
        if (this.rippleColor != colorStateList) {
            this.rippleColor = colorStateList;
            this.getImpl().setRippleColor(this.rippleColor);
        }
    }

    public void setShowMotionSpec(MotionSpec motionSpec) {
        this.getImpl().setShowMotionSpec(motionSpec);
    }

    public void setShowMotionSpecResource(int n) {
        this.setShowMotionSpec(MotionSpec.createFromResource(this.getContext(), n));
    }

    public void setSize(int n) {
        this.customSize = 0;
        if (n != this.size) {
            this.size = n;
            this.requestLayout();
        }
    }

    @Override
    public void setSupportBackgroundTintList(ColorStateList colorStateList) {
        this.setBackgroundTintList(colorStateList);
    }

    @Override
    public void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        this.setBackgroundTintMode(mode);
    }

    @Override
    public void setSupportImageTintList(ColorStateList colorStateList) {
        if (this.imageTint != colorStateList) {
            this.imageTint = colorStateList;
            this.onApplySupportImageTint();
        }
    }

    @Override
    public void setSupportImageTintMode(PorterDuff.Mode mode) {
        if (this.imageMode != mode) {
            this.imageMode = mode;
            this.onApplySupportImageTint();
        }
    }

    public void setUseCompatPadding(boolean bl) {
        if (this.compatPadding != bl) {
            this.compatPadding = bl;
            this.getImpl().onCompatShadowChanged();
        }
    }

    public void show() {
        this.show(null);
    }

    public void show(OnVisibilityChangedListener onVisibilityChangedListener) {
        this.show(onVisibilityChangedListener, true);
    }

    void show(OnVisibilityChangedListener onVisibilityChangedListener, boolean bl) {
        this.getImpl().show(this.wrapOnVisibilityChangedListener(onVisibilityChangedListener), bl);
    }

    protected static class BaseBehavior<T extends FloatingActionButton>
    extends CoordinatorLayout.Behavior<T> {
        private static final boolean AUTO_HIDE_DEFAULT = true;
        private boolean autoHideEnabled;
        private OnVisibilityChangedListener internalAutoHideListener;
        private Rect tmpRect;

        public BaseBehavior() {
            this.autoHideEnabled = true;
        }

        public BaseBehavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            context = context.obtainStyledAttributes(attributeSet, R.styleable.FloatingActionButton_Behavior_Layout);
            this.autoHideEnabled = context.getBoolean(R.styleable.FloatingActionButton_Behavior_Layout_behavior_autoHide, true);
            context.recycle();
        }

        private static boolean isBottomSheet(View view) {
            if ((view = view.getLayoutParams()) instanceof CoordinatorLayout.LayoutParams) {
                return ((CoordinatorLayout.LayoutParams)view).getBehavior() instanceof BottomSheetBehavior;
            }
            return false;
        }

        private void offsetIfNeeded(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton) {
            Rect rect = floatingActionButton.shadowPadding;
            if (rect != null && rect.centerX() > 0 && rect.centerY() > 0) {
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)floatingActionButton.getLayoutParams();
                int n = 0;
                int n2 = 0;
                if (floatingActionButton.getRight() >= coordinatorLayout.getWidth() - layoutParams.rightMargin) {
                    n2 = rect.right;
                } else if (floatingActionButton.getLeft() <= layoutParams.leftMargin) {
                    n2 = -rect.left;
                }
                if (floatingActionButton.getBottom() >= coordinatorLayout.getHeight() - layoutParams.bottomMargin) {
                    n = rect.bottom;
                } else if (floatingActionButton.getTop() <= layoutParams.topMargin) {
                    n = -rect.top;
                }
                if (n != 0) {
                    ViewCompat.offsetTopAndBottom((View)floatingActionButton, n);
                }
                if (n2 != 0) {
                    ViewCompat.offsetLeftAndRight((View)floatingActionButton, n2);
                }
            }
        }

        private boolean shouldUpdateVisibility(View view, FloatingActionButton floatingActionButton) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)floatingActionButton.getLayoutParams();
            if (!this.autoHideEnabled) {
                return false;
            }
            if (layoutParams.getAnchorId() != view.getId()) {
                return false;
            }
            return floatingActionButton.getUserSetVisibility() == 0;
        }

        private boolean updateFabVisibilityForAppBarLayout(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, FloatingActionButton floatingActionButton) {
            if (!this.shouldUpdateVisibility((View)appBarLayout, floatingActionButton)) {
                return false;
            }
            if (this.tmpRect == null) {
                this.tmpRect = new Rect();
            }
            Rect rect = this.tmpRect;
            DescendantOffsetUtils.getDescendantRect(coordinatorLayout, (View)appBarLayout, rect);
            if (rect.bottom <= appBarLayout.getMinimumHeightForVisibleOverlappingContent()) {
                floatingActionButton.hide(this.internalAutoHideListener, false);
            } else {
                floatingActionButton.show(this.internalAutoHideListener, false);
            }
            return true;
        }

        private boolean updateFabVisibilityForBottomSheet(View view, FloatingActionButton floatingActionButton) {
            if (!this.shouldUpdateVisibility(view, floatingActionButton)) {
                return false;
            }
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)floatingActionButton.getLayoutParams();
            if (view.getTop() < floatingActionButton.getHeight() / 2 + layoutParams.topMargin) {
                floatingActionButton.hide(this.internalAutoHideListener, false);
            } else {
                floatingActionButton.show(this.internalAutoHideListener, false);
            }
            return true;
        }

        @Override
        public boolean getInsetDodgeRect(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, Rect rect) {
            coordinatorLayout = floatingActionButton.shadowPadding;
            rect.set(floatingActionButton.getLeft() + ((Rect)coordinatorLayout).left, floatingActionButton.getTop() + ((Rect)coordinatorLayout).top, floatingActionButton.getRight() - ((Rect)coordinatorLayout).right, floatingActionButton.getBottom() - ((Rect)coordinatorLayout).bottom);
            return true;
        }

        public boolean isAutoHideEnabled() {
            return this.autoHideEnabled;
        }

        @Override
        public void onAttachedToLayoutParams(CoordinatorLayout.LayoutParams layoutParams) {
            if (layoutParams.dodgeInsetEdges == 0) {
                layoutParams.dodgeInsetEdges = 80;
            }
        }

        @Override
        public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, View view) {
            if (view instanceof AppBarLayout) {
                this.updateFabVisibilityForAppBarLayout(coordinatorLayout, (AppBarLayout)view, floatingActionButton);
            } else if (BaseBehavior.isBottomSheet(view)) {
                this.updateFabVisibilityForBottomSheet(view, floatingActionButton);
            }
            return false;
        }

        @Override
        public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, int n) {
            View view;
            List<View> list = coordinatorLayout.getDependencies((View)floatingActionButton);
            int n2 = list.size();
            for (int i = 0; i < n2 && !((view = list.get(i)) instanceof AppBarLayout ? this.updateFabVisibilityForAppBarLayout(coordinatorLayout, (AppBarLayout)view, floatingActionButton) : BaseBehavior.isBottomSheet(view) && this.updateFabVisibilityForBottomSheet(view, floatingActionButton)); ++i) {
            }
            coordinatorLayout.onLayoutChild((View)floatingActionButton, n);
            this.offsetIfNeeded(coordinatorLayout, floatingActionButton);
            return true;
        }

        public void setAutoHideEnabled(boolean bl) {
            this.autoHideEnabled = bl;
        }

        public void setInternalAutoHideListener(OnVisibilityChangedListener onVisibilityChangedListener) {
            this.internalAutoHideListener = onVisibilityChangedListener;
        }
    }

    public static class Behavior
    extends BaseBehavior<FloatingActionButton> {
        public Behavior() {
        }

        public Behavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }
    }

    public static abstract class OnVisibilityChangedListener {
        public void onHidden(FloatingActionButton floatingActionButton) {
        }

        public void onShown(FloatingActionButton floatingActionButton) {
        }
    }

    private class ShadowDelegateImpl
    implements ShadowViewDelegate {
        final FloatingActionButton this$0;

        ShadowDelegateImpl(FloatingActionButton floatingActionButton) {
            this.this$0 = floatingActionButton;
        }

        @Override
        public float getRadius() {
            return (float)this.this$0.getSizeDimension() / 2.0f;
        }

        @Override
        public boolean isCompatPaddingEnabled() {
            return this.this$0.compatPadding;
        }

        @Override
        public void setBackgroundDrawable(Drawable drawable2) {
            FloatingActionButton.super.setBackgroundDrawable(drawable2);
        }

        @Override
        public void setShadowPadding(int n, int n2, int n3, int n4) {
            this.this$0.shadowPadding.set(n, n2, n3, n4);
            FloatingActionButton floatingActionButton = this.this$0;
            floatingActionButton.setPadding(floatingActionButton.imagePadding + n, this.this$0.imagePadding + n2, this.this$0.imagePadding + n3, this.this$0.imagePadding + n4);
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface Size {
    }
}

