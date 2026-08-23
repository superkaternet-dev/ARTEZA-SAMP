/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.content.Context
 *  android.graphics.Rect
 *  android.os.Build$VERSION
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.os.Parcelable$Creator
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.animation.AnimationUtils
 *  android.view.animation.Interpolator
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 */
package com.google.android.material.appbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;
import androidx.core.util.ObjectsCompat;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.appbar.HeaderBehavior;
import com.google.android.material.appbar.HeaderScrollingViewBehavior;
import com.google.android.material.appbar.ViewUtilsLollipop;
import com.google.android.material.internal.ThemeEnforcement;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

@CoordinatorLayout.DefaultBehavior(value=Behavior.class)
public class AppBarLayout
extends LinearLayout {
    private static final int INVALID_SCROLL_RANGE = -1;
    static final int PENDING_ACTION_ANIMATE_ENABLED = 4;
    static final int PENDING_ACTION_COLLAPSED = 2;
    static final int PENDING_ACTION_EXPANDED = 1;
    static final int PENDING_ACTION_FORCE = 8;
    static final int PENDING_ACTION_NONE = 0;
    private int downPreScrollRange = -1;
    private int downScrollRange = -1;
    private boolean haveChildWithInterpolator;
    private WindowInsetsCompat lastInsets;
    private boolean liftOnScroll;
    private boolean liftable;
    private boolean liftableOverride;
    private boolean lifted;
    private List<BaseOnOffsetChangedListener> listeners;
    private int pendingAction = 0;
    private int[] tmpStatesArray;
    private int totalScrollRange = -1;

    public AppBarLayout(Context context) {
        this(context, null);
    }

    public AppBarLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.setOrientation(1);
        if (Build.VERSION.SDK_INT >= 21) {
            ViewUtilsLollipop.setBoundsViewOutlineProvider((View)this);
            ViewUtilsLollipop.setStateListAnimatorFromAttrs((View)this, attributeSet, 0, R.style.Widget_Design_AppBarLayout);
        }
        context = ThemeEnforcement.obtainStyledAttributes(context, attributeSet, R.styleable.AppBarLayout, 0, R.style.Widget_Design_AppBarLayout, new int[0]);
        ViewCompat.setBackground((View)this, context.getDrawable(R.styleable.AppBarLayout_android_background));
        if (context.hasValue(R.styleable.AppBarLayout_expanded)) {
            this.setExpanded(context.getBoolean(R.styleable.AppBarLayout_expanded, false), false, false);
        }
        if (Build.VERSION.SDK_INT >= 21 && context.hasValue(R.styleable.AppBarLayout_elevation)) {
            ViewUtilsLollipop.setDefaultAppBarLayoutStateListAnimator((View)this, context.getDimensionPixelSize(R.styleable.AppBarLayout_elevation, 0));
        }
        if (Build.VERSION.SDK_INT >= 26) {
            if (context.hasValue(R.styleable.AppBarLayout_android_keyboardNavigationCluster)) {
                this.setKeyboardNavigationCluster(context.getBoolean(R.styleable.AppBarLayout_android_keyboardNavigationCluster, false));
            }
            if (context.hasValue(R.styleable.AppBarLayout_android_touchscreenBlocksFocus)) {
                this.setTouchscreenBlocksFocus(context.getBoolean(R.styleable.AppBarLayout_android_touchscreenBlocksFocus, false));
            }
        }
        this.liftOnScroll = context.getBoolean(R.styleable.AppBarLayout_liftOnScroll, false);
        context.recycle();
        ViewCompat.setOnApplyWindowInsetsListener((View)this, new OnApplyWindowInsetsListener(this){
            final AppBarLayout this$0;
            {
                this.this$0 = appBarLayout;
            }

            @Override
            public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                return this.this$0.onWindowInsetChanged(windowInsetsCompat);
            }
        });
    }

    private boolean hasCollapsibleChild() {
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            if (!((LayoutParams)this.getChildAt(i).getLayoutParams()).isCollapsible()) continue;
            return true;
        }
        return false;
    }

    private void invalidateScrollRanges() {
        this.totalScrollRange = -1;
        this.downPreScrollRange = -1;
        this.downScrollRange = -1;
    }

    private void setExpanded(boolean bl, boolean bl2, boolean bl3) {
        int n = bl ? 1 : 2;
        int n2 = 0;
        int n3 = bl2 ? 4 : 0;
        if (bl3) {
            n2 = 8;
        }
        this.pendingAction = n | n3 | n2;
        this.requestLayout();
    }

    private boolean setLiftableState(boolean bl) {
        if (this.liftable != bl) {
            this.liftable = bl;
            this.refreshDrawableState();
            return true;
        }
        return false;
    }

    public void addOnOffsetChangedListener(BaseOnOffsetChangedListener baseOnOffsetChangedListener) {
        if (this.listeners == null) {
            this.listeners = new ArrayList<BaseOnOffsetChangedListener>();
        }
        if (baseOnOffsetChangedListener != null && !this.listeners.contains(baseOnOffsetChangedListener)) {
            this.listeners.add(baseOnOffsetChangedListener);
        }
    }

    public void addOnOffsetChangedListener(OnOffsetChangedListener onOffsetChangedListener) {
        this.addOnOffsetChangedListener((BaseOnOffsetChangedListener)onOffsetChangedListener);
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    void dispatchOffsetUpdates(int n) {
        Object object = this.listeners;
        if (object != null) {
            int n2 = object.size();
            for (int i = 0; i < n2; ++i) {
                object = this.listeners.get(i);
                if (object == null) continue;
                object.onOffsetChanged(this, n);
            }
        }
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -2);
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(this.getContext(), attributeSet);
    }

    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (Build.VERSION.SDK_INT >= 19 && layoutParams instanceof LinearLayout.LayoutParams) {
            return new LayoutParams((LinearLayout.LayoutParams)layoutParams);
        }
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams)layoutParams);
        }
        return new LayoutParams(layoutParams);
    }

    int getDownNestedPreScrollRange() {
        int n = this.downPreScrollRange;
        if (n != -1) {
            return n;
        }
        int n2 = 0;
        for (int i = this.getChildCount() - 1; i >= 0; --i) {
            View view = this.getChildAt(i);
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            int n3 = view.getMeasuredHeight();
            n = layoutParams.scrollFlags;
            if ((n & 5) == 5) {
                n = (n & 8) != 0 ? n2 + ViewCompat.getMinimumHeight(view) : ((n & 2) != 0 ? n2 + (n3 - ViewCompat.getMinimumHeight(view)) : (n2 += layoutParams.topMargin + layoutParams.bottomMargin) + (n3 - this.getTopInset()));
            } else {
                n = n2;
                if (n2 > 0) break;
            }
            n2 = n;
        }
        this.downPreScrollRange = n = Math.max(0, n2);
        return n;
    }

    int getDownNestedScrollRange() {
        int n;
        int n2 = this.downScrollRange;
        if (n2 != -1) {
            return n2;
        }
        n2 = 0;
        int n3 = 0;
        int n4 = this.getChildCount();
        while (true) {
            n = n2;
            if (n3 >= n4) break;
            View view = this.getChildAt(n3);
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            int n5 = view.getMeasuredHeight();
            int n6 = layoutParams.topMargin;
            int n7 = layoutParams.bottomMargin;
            int n8 = layoutParams.scrollFlags;
            n = n2;
            if ((n8 & 1) == 0) break;
            n2 += n5 + (n6 + n7);
            if ((n8 & 2) != 0) {
                n = n2 - (ViewCompat.getMinimumHeight(view) + this.getTopInset());
                break;
            }
            ++n3;
        }
        this.downScrollRange = n2 = Math.max(0, n);
        return n2;
    }

    public final int getMinimumHeightForVisibleOverlappingContent() {
        int n = this.getTopInset();
        int n2 = ViewCompat.getMinimumHeight((View)this);
        if (n2 != 0) {
            return n2 * 2 + n;
        }
        n2 = this.getChildCount();
        n2 = n2 >= 1 ? ViewCompat.getMinimumHeight(this.getChildAt(n2 - 1)) : 0;
        if (n2 != 0) {
            return n2 * 2 + n;
        }
        return this.getHeight() / 3;
    }

    int getPendingAction() {
        return this.pendingAction;
    }

    @Deprecated
    public float getTargetElevation() {
        return 0.0f;
    }

    final int getTopInset() {
        WindowInsetsCompat windowInsetsCompat = this.lastInsets;
        int n = windowInsetsCompat != null ? windowInsetsCompat.getSystemWindowInsetTop() : 0;
        return n;
    }

    public final int getTotalScrollRange() {
        int n;
        int n2 = this.totalScrollRange;
        if (n2 != -1) {
            return n2;
        }
        n2 = 0;
        int n3 = 0;
        int n4 = this.getChildCount();
        while (true) {
            n = n2;
            if (n3 >= n4) break;
            View view = this.getChildAt(n3);
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            int n5 = view.getMeasuredHeight();
            int n6 = layoutParams.scrollFlags;
            n = n2;
            if ((n6 & 1) == 0) break;
            n2 += layoutParams.topMargin + n5 + layoutParams.bottomMargin;
            if ((n6 & 2) != 0) {
                n = n2 - ViewCompat.getMinimumHeight(view);
                break;
            }
            ++n3;
        }
        this.totalScrollRange = n2 = Math.max(0, n - this.getTopInset());
        return n2;
    }

    int getUpNestedPreScrollRange() {
        return this.getTotalScrollRange();
    }

    boolean hasChildWithInterpolator() {
        return this.haveChildWithInterpolator;
    }

    boolean hasScrollableChildren() {
        boolean bl = this.getTotalScrollRange() != 0;
        return bl;
    }

    public boolean isLiftOnScroll() {
        return this.liftOnScroll;
    }

    protected int[] onCreateDrawableState(int n) {
        if (this.tmpStatesArray == null) {
            this.tmpStatesArray = new int[4];
        }
        int[] nArray = this.tmpStatesArray;
        int[] nArray2 = super.onCreateDrawableState(nArray.length + n);
        n = this.liftable ? R.attr.state_liftable : -R.attr.state_liftable;
        nArray[0] = n;
        n = this.liftable && this.lifted ? R.attr.state_lifted : -R.attr.state_lifted;
        nArray[1] = n;
        n = this.liftable ? R.attr.state_collapsible : -R.attr.state_collapsible;
        nArray[2] = n;
        n = this.liftable && this.lifted ? R.attr.state_collapsed : -R.attr.state_collapsed;
        nArray[3] = n;
        return AppBarLayout.mergeDrawableStates((int[])nArray2, (int[])nArray);
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        super.onLayout(bl, n, n2, n3, n4);
        this.invalidateScrollRanges();
        bl = false;
        this.haveChildWithInterpolator = false;
        n2 = this.getChildCount();
        for (n = 0; n < n2; ++n) {
            if (((LayoutParams)this.getChildAt(n).getLayoutParams()).getScrollInterpolator() == null) continue;
            this.haveChildWithInterpolator = true;
            break;
        }
        if (!this.liftableOverride) {
            if (this.liftOnScroll || this.hasCollapsibleChild()) {
                bl = true;
            }
            this.setLiftableState(bl);
        }
    }

    protected void onMeasure(int n, int n2) {
        super.onMeasure(n, n2);
        this.invalidateScrollRanges();
    }

    WindowInsetsCompat onWindowInsetChanged(WindowInsetsCompat windowInsetsCompat) {
        WindowInsetsCompat windowInsetsCompat2 = null;
        if (ViewCompat.getFitsSystemWindows((View)this)) {
            windowInsetsCompat2 = windowInsetsCompat;
        }
        if (!ObjectsCompat.equals(this.lastInsets, windowInsetsCompat2)) {
            this.lastInsets = windowInsetsCompat2;
            this.invalidateScrollRanges();
        }
        return windowInsetsCompat;
    }

    public void removeOnOffsetChangedListener(BaseOnOffsetChangedListener baseOnOffsetChangedListener) {
        List<BaseOnOffsetChangedListener> list = this.listeners;
        if (list != null && baseOnOffsetChangedListener != null) {
            list.remove(baseOnOffsetChangedListener);
        }
    }

    public void removeOnOffsetChangedListener(OnOffsetChangedListener onOffsetChangedListener) {
        this.removeOnOffsetChangedListener((BaseOnOffsetChangedListener)onOffsetChangedListener);
    }

    void resetPendingAction() {
        this.pendingAction = 0;
    }

    public void setExpanded(boolean bl) {
        this.setExpanded(bl, ViewCompat.isLaidOut((View)this));
    }

    public void setExpanded(boolean bl, boolean bl2) {
        this.setExpanded(bl, bl2, true);
    }

    public void setLiftOnScroll(boolean bl) {
        this.liftOnScroll = bl;
    }

    public boolean setLiftable(boolean bl) {
        this.liftableOverride = true;
        return this.setLiftableState(bl);
    }

    public boolean setLifted(boolean bl) {
        return this.setLiftedState(bl);
    }

    boolean setLiftedState(boolean bl) {
        if (this.lifted != bl) {
            this.lifted = bl;
            this.refreshDrawableState();
            return true;
        }
        return false;
    }

    public void setOrientation(int n) {
        if (n == 1) {
            super.setOrientation(n);
            return;
        }
        throw new IllegalArgumentException("AppBarLayout is always vertical and does not support horizontal orientation");
    }

    @Deprecated
    public void setTargetElevation(float f) {
        if (Build.VERSION.SDK_INT >= 21) {
            ViewUtilsLollipop.setDefaultAppBarLayoutStateListAnimator((View)this, f);
        }
    }

    protected static class BaseBehavior<T extends AppBarLayout>
    extends HeaderBehavior<T> {
        private static final int INVALID_POSITION = -1;
        private static final int MAX_OFFSET_ANIMATION_DURATION = 600;
        private WeakReference<View> lastNestedScrollingChildRef;
        private int lastStartedType;
        private ValueAnimator offsetAnimator;
        private int offsetDelta;
        private int offsetToChildIndexOnLayout = -1;
        private boolean offsetToChildIndexOnLayoutIsMinHeight;
        private float offsetToChildIndexOnLayoutPerc;
        private BaseDragCallback onDragCallback;

        public BaseBehavior() {
        }

        public BaseBehavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        private void animateOffsetTo(CoordinatorLayout coordinatorLayout, T t, int n, float f) {
            int n2 = Math.abs(this.getTopBottomOffsetForScrollingSibling() - n);
            n2 = (f = Math.abs(f)) > 0.0f ? Math.round((float)n2 / f * 1000.0f) * 3 : (int)((1.0f + (float)n2 / (float)t.getHeight()) * 150.0f);
            this.animateOffsetWithDuration(coordinatorLayout, t, n, n2);
        }

        private void animateOffsetWithDuration(CoordinatorLayout coordinatorLayout, T t, int n, int n2) {
            int n3 = this.getTopBottomOffsetForScrollingSibling();
            if (n3 == n) {
                coordinatorLayout = this.offsetAnimator;
                if (coordinatorLayout != null && coordinatorLayout.isRunning()) {
                    this.offsetAnimator.cancel();
                }
                return;
            }
            ValueAnimator valueAnimator = this.offsetAnimator;
            if (valueAnimator == null) {
                this.offsetAnimator = valueAnimator = new ValueAnimator();
                valueAnimator.setInterpolator(AnimationUtils.DECELERATE_INTERPOLATOR);
                this.offsetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this, coordinatorLayout, (AppBarLayout)((Object)t)){
                    final BaseBehavior this$0;
                    final AppBarLayout val$child;
                    final CoordinatorLayout val$coordinatorLayout;
                    {
                        this.this$0 = baseBehavior;
                        this.val$coordinatorLayout = coordinatorLayout;
                        this.val$child = appBarLayout;
                    }

                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        this.this$0.setHeaderTopBottomOffset(this.val$coordinatorLayout, this.val$child, (Integer)valueAnimator.getAnimatedValue());
                    }
                });
            } else {
                valueAnimator.cancel();
            }
            this.offsetAnimator.setDuration((long)Math.min(n2, 600));
            this.offsetAnimator.setIntValues(new int[]{n3, n});
            this.offsetAnimator.start();
        }

        private boolean canScrollChildren(CoordinatorLayout coordinatorLayout, T t, View view) {
            boolean bl = ((AppBarLayout)((Object)t)).hasScrollableChildren() && coordinatorLayout.getHeight() - view.getHeight() <= t.getHeight();
            return bl;
        }

        private static boolean checkFlag(int n, int n2) {
            boolean bl = (n & n2) == n2;
            return bl;
        }

        private View findFirstScrollingChild(CoordinatorLayout coordinatorLayout) {
            int n = coordinatorLayout.getChildCount();
            for (int i = 0; i < n; ++i) {
                View view = coordinatorLayout.getChildAt(i);
                if (!(view instanceof NestedScrollingChild)) continue;
                return view;
            }
            return null;
        }

        private static View getAppBarChildOnOffset(AppBarLayout appBarLayout, int n) {
            int n2 = Math.abs(n);
            int n3 = appBarLayout.getChildCount();
            for (n = 0; n < n3; ++n) {
                View view = appBarLayout.getChildAt(n);
                if (n2 < view.getTop() || n2 > view.getBottom()) continue;
                return view;
            }
            return null;
        }

        private int getChildIndexOnOffset(T t, int n) {
            int n2 = t.getChildCount();
            for (int i = 0; i < n2; ++i) {
                Object object = t.getChildAt(i);
                int n3 = object.getTop();
                int n4 = object.getBottom();
                object = (LayoutParams)object.getLayoutParams();
                int n5 = n3;
                int n6 = n4;
                if (BaseBehavior.checkFlag(((LayoutParams)((Object)object)).getScrollFlags(), 32)) {
                    n5 = n3 - ((LayoutParams)((Object)object)).topMargin;
                    n6 = n4 + ((LayoutParams)((Object)object)).bottomMargin;
                }
                if (n5 > -n || n6 < -n) continue;
                return i;
            }
            return -1;
        }

        private int interpolateOffset(T t, int n) {
            int n2 = Math.abs(n);
            int n3 = t.getChildCount();
            for (int i = 0; i < n3; ++i) {
                View view = t.getChildAt(i);
                LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                Interpolator interpolator2 = layoutParams.getScrollInterpolator();
                if (n2 < view.getTop() || n2 > view.getBottom()) continue;
                if (interpolator2 == null) break;
                i = 0;
                int n4 = layoutParams.getScrollFlags();
                if ((n4 & 1) != 0) {
                    i = n3 = 0 + (view.getHeight() + layoutParams.topMargin + layoutParams.bottomMargin);
                    if ((n4 & 2) != 0) {
                        i = n3 - ViewCompat.getMinimumHeight(view);
                    }
                }
                n3 = i;
                if (ViewCompat.getFitsSystemWindows(view)) {
                    n3 = i - ((AppBarLayout)((Object)t)).getTopInset();
                }
                if (n3 <= 0) break;
                i = view.getTop();
                i = Math.round((float)n3 * interpolator2.getInterpolation((float)(n2 - i) / (float)n3));
                return Integer.signum(n) * (view.getTop() + i);
            }
            return n;
        }

        private boolean shouldJumpElevationState(CoordinatorLayout object, T object2) {
            object = ((CoordinatorLayout)object).getDependents((View)object2);
            int n = 0;
            int n2 = object.size();
            while (true) {
                boolean bl = false;
                if (n >= n2) break;
                object2 = (View)object.get(n);
                if ((object2 = ((CoordinatorLayout.LayoutParams)object2.getLayoutParams()).getBehavior()) instanceof ScrollingViewBehavior) {
                    if (((ScrollingViewBehavior)object2).getOverlayTop() != 0) {
                        bl = true;
                    }
                    return bl;
                }
                ++n;
            }
            return false;
        }

        private void snapToChildIfNeeded(CoordinatorLayout coordinatorLayout, T t) {
            View view;
            LayoutParams layoutParams;
            int n;
            int n2 = this.getTopBottomOffsetForScrollingSibling();
            int n3 = this.getChildIndexOnOffset(t, n2);
            if (n3 >= 0 && ((n = (layoutParams = (LayoutParams)(view = t.getChildAt(n3)).getLayoutParams()).getScrollFlags()) & 0x11) == 17) {
                int n4;
                int n5 = -view.getTop();
                int n6 = n4 = -view.getBottom();
                if (n3 == t.getChildCount() - 1) {
                    n6 = n4 + ((AppBarLayout)((Object)t)).getTopInset();
                }
                if (BaseBehavior.checkFlag(n, 2)) {
                    n4 = n6 + ViewCompat.getMinimumHeight(view);
                    n3 = n5;
                } else {
                    n3 = n5;
                    n4 = n6;
                    if (BaseBehavior.checkFlag(n, 5)) {
                        n4 = ViewCompat.getMinimumHeight(view) + n6;
                        if (n2 < n4) {
                            n3 = n4;
                            n4 = n6;
                        } else {
                            n3 = n5;
                        }
                    }
                }
                n5 = n3;
                n6 = n4;
                if (BaseBehavior.checkFlag(n, 32)) {
                    n5 = n3 + layoutParams.topMargin;
                    n6 = n4 - layoutParams.bottomMargin;
                }
                n4 = n2 < (n6 + n5) / 2 ? n6 : n5;
                this.animateOffsetTo(coordinatorLayout, t, MathUtils.clamp(n4, -((AppBarLayout)((Object)t)).getTotalScrollRange(), 0), 0.0f);
            }
        }

        private void stopNestedScrollIfNeeded(int n, T t, View view, int n2) {
            if (n2 == 1) {
                n2 = this.getTopBottomOffsetForScrollingSibling();
                if (n < 0 && n2 == 0 || n > 0 && n2 == -((AppBarLayout)((Object)t)).getDownNestedScrollRange()) {
                    ViewCompat.stopNestedScroll(view, 1);
                }
            }
        }

        private void updateAppBarLayoutDrawableState(CoordinatorLayout coordinatorLayout, T t, int n, int n2, boolean bl) {
            View view = BaseBehavior.getAppBarChildOnOffset(t, n);
            if (view != null) {
                int n3 = ((LayoutParams)view.getLayoutParams()).getScrollFlags();
                boolean bl2 = false;
                boolean bl3 = false;
                boolean bl4 = bl2;
                if ((n3 & 1) != 0) {
                    int n4 = ViewCompat.getMinimumHeight(view);
                    if (n2 > 0 && (n3 & 0xC) != 0) {
                        bl4 = -n >= view.getBottom() - n4 - ((AppBarLayout)((Object)t)).getTopInset();
                    } else {
                        bl4 = bl2;
                        if ((n3 & 2) != 0) {
                            bl4 = -n >= view.getBottom() - n4 - ((AppBarLayout)((Object)t)).getTopInset();
                        }
                    }
                }
                bl2 = bl4;
                if (((AppBarLayout)((Object)t)).isLiftOnScroll()) {
                    view = this.findFirstScrollingChild(coordinatorLayout);
                    bl2 = bl4;
                    if (view != null) {
                        bl4 = bl3;
                        if (view.getScrollY() > 0) {
                            bl4 = true;
                        }
                        bl2 = bl4;
                    }
                }
                bl4 = ((AppBarLayout)((Object)t)).setLiftedState(bl2);
                if (Build.VERSION.SDK_INT >= 11 && (bl || bl4 && this.shouldJumpElevationState(coordinatorLayout, t))) {
                    t.jumpDrawablesToCurrentState();
                }
            }
        }

        @Override
        boolean canDragView(T object) {
            BaseDragCallback baseDragCallback = this.onDragCallback;
            if (baseDragCallback != null) {
                return baseDragCallback.canDrag(object);
            }
            object = this.lastNestedScrollingChildRef;
            boolean bl = true;
            if (object != null) {
                if ((object = (View)((Reference)object).get()) == null || !object.isShown() || object.canScrollVertically(-1)) {
                    bl = false;
                }
                return bl;
            }
            return true;
        }

        @Override
        int getMaxDragOffset(T t) {
            return -((AppBarLayout)((Object)t)).getDownNestedScrollRange();
        }

        @Override
        int getScrollRangeForDragFling(T t) {
            return ((AppBarLayout)((Object)t)).getTotalScrollRange();
        }

        @Override
        int getTopBottomOffsetForScrollingSibling() {
            return this.getTopAndBottomOffset() + this.offsetDelta;
        }

        boolean isOffsetAnimatorRunning() {
            ValueAnimator valueAnimator = this.offsetAnimator;
            boolean bl = valueAnimator != null && valueAnimator.isRunning();
            return bl;
        }

        @Override
        void onFlingFinished(CoordinatorLayout coordinatorLayout, T t) {
            this.snapToChildIfNeeded(coordinatorLayout, t);
        }

        @Override
        public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, T t, int n) {
            boolean bl = super.onLayoutChild(coordinatorLayout, t, n);
            int n2 = ((AppBarLayout)((Object)t)).getPendingAction();
            n = this.offsetToChildIndexOnLayout;
            if (n >= 0 && (n2 & 8) == 0) {
                View view = t.getChildAt(n);
                n = -view.getBottom();
                n = this.offsetToChildIndexOnLayoutIsMinHeight ? (n += ViewCompat.getMinimumHeight(view) + ((AppBarLayout)((Object)t)).getTopInset()) : (n += Math.round((float)view.getHeight() * this.offsetToChildIndexOnLayoutPerc));
                this.setHeaderTopBottomOffset(coordinatorLayout, t, n);
            } else if (n2 != 0) {
                n = (n2 & 4) != 0 ? 1 : 0;
                if ((n2 & 2) != 0) {
                    n2 = -((AppBarLayout)((Object)t)).getUpNestedPreScrollRange();
                    if (n != 0) {
                        this.animateOffsetTo(coordinatorLayout, t, n2, 0.0f);
                    } else {
                        this.setHeaderTopBottomOffset(coordinatorLayout, t, n2);
                    }
                } else if ((n2 & 1) != 0) {
                    if (n != 0) {
                        this.animateOffsetTo(coordinatorLayout, t, 0, 0.0f);
                    } else {
                        this.setHeaderTopBottomOffset(coordinatorLayout, t, 0);
                    }
                }
            }
            ((AppBarLayout)((Object)t)).resetPendingAction();
            this.offsetToChildIndexOnLayout = -1;
            this.setTopAndBottomOffset(MathUtils.clamp(this.getTopAndBottomOffset(), -((AppBarLayout)((Object)t)).getTotalScrollRange(), 0));
            this.updateAppBarLayoutDrawableState(coordinatorLayout, t, this.getTopAndBottomOffset(), 0, true);
            ((AppBarLayout)((Object)t)).dispatchOffsetUpdates(this.getTopAndBottomOffset());
            return bl;
        }

        @Override
        public boolean onMeasureChild(CoordinatorLayout coordinatorLayout, T t, int n, int n2, int n3, int n4) {
            if (((CoordinatorLayout.LayoutParams)t.getLayoutParams()).height == -2) {
                coordinatorLayout.onMeasureChild((View)t, n, n2, View.MeasureSpec.makeMeasureSpec((int)0, (int)0), n4);
                return true;
            }
            return super.onMeasureChild(coordinatorLayout, t, n, n2, n3, n4);
        }

        @Override
        public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, T t, View view, int n, int n2, int[] nArray, int n3) {
            block3: {
                int n4;
                if (n2 == 0) break block3;
                if (n2 < 0) {
                    n4 = -((AppBarLayout)((Object)t)).getTotalScrollRange();
                    int n5 = ((AppBarLayout)((Object)t)).getDownNestedPreScrollRange();
                    n = n4;
                    n5 += n4;
                    n4 = n;
                    n = n5;
                } else {
                    n4 = -((AppBarLayout)((Object)t)).getUpNestedPreScrollRange();
                    n = 0;
                }
                if (n4 != n) {
                    nArray[1] = this.scroll(coordinatorLayout, t, n2, n4, n);
                    this.stopNestedScrollIfNeeded(n2, t, view, n3);
                }
            }
        }

        @Override
        public void onNestedScroll(CoordinatorLayout coordinatorLayout, T t, View view, int n, int n2, int n3, int n4, int n5) {
            if (n4 < 0) {
                this.scroll(coordinatorLayout, t, n4, -((AppBarLayout)((Object)t)).getDownNestedScrollRange(), 0);
                this.stopNestedScrollIfNeeded(n4, t, view, n5);
            }
            if (((AppBarLayout)((Object)t)).isLiftOnScroll()) {
                boolean bl = view.getScrollY() > 0;
                ((AppBarLayout)((Object)t)).setLiftedState(bl);
            }
        }

        @Override
        public void onRestoreInstanceState(CoordinatorLayout coordinatorLayout, T t, Parcelable parcelable) {
            if (parcelable instanceof SavedState) {
                parcelable = (SavedState)parcelable;
                super.onRestoreInstanceState(coordinatorLayout, t, parcelable.getSuperState());
                this.offsetToChildIndexOnLayout = parcelable.firstVisibleChildIndex;
                this.offsetToChildIndexOnLayoutPerc = parcelable.firstVisibleChildPercentageShown;
                this.offsetToChildIndexOnLayoutIsMinHeight = parcelable.firstVisibleChildAtMinimumHeight;
            } else {
                super.onRestoreInstanceState(coordinatorLayout, t, parcelable);
                this.offsetToChildIndexOnLayout = -1;
            }
        }

        @Override
        public Parcelable onSaveInstanceState(CoordinatorLayout coordinatorLayout, T t) {
            Parcelable parcelable = super.onSaveInstanceState(coordinatorLayout, t);
            int n = this.getTopAndBottomOffset();
            int n2 = t.getChildCount();
            for (int i = 0; i < n2; ++i) {
                coordinatorLayout = t.getChildAt(i);
                int n3 = coordinatorLayout.getBottom() + n;
                if (coordinatorLayout.getTop() + n > 0 || n3 < 0) continue;
                parcelable = new SavedState(parcelable);
                parcelable.firstVisibleChildIndex = i;
                boolean bl = n3 == ViewCompat.getMinimumHeight((View)coordinatorLayout) + ((AppBarLayout)((Object)t)).getTopInset();
                parcelable.firstVisibleChildAtMinimumHeight = bl;
                parcelable.firstVisibleChildPercentageShown = (float)n3 / (float)coordinatorLayout.getHeight();
                return parcelable;
            }
            return parcelable;
        }

        @Override
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, T t, View view, View view2, int n, int n2) {
            boolean bl = (n & 2) != 0 && (((AppBarLayout)((Object)t)).isLiftOnScroll() || this.canScrollChildren(coordinatorLayout, t, view));
            if (bl && (coordinatorLayout = this.offsetAnimator) != null) {
                coordinatorLayout.cancel();
            }
            this.lastNestedScrollingChildRef = null;
            this.lastStartedType = n2;
            return bl;
        }

        @Override
        public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, T t, View view, int n) {
            if (this.lastStartedType == 0 || n == 1) {
                this.snapToChildIfNeeded(coordinatorLayout, t);
            }
            this.lastNestedScrollingChildRef = new WeakReference<View>(view);
        }

        public void setDragCallback(BaseDragCallback baseDragCallback) {
            this.onDragCallback = baseDragCallback;
        }

        @Override
        int setHeaderTopBottomOffset(CoordinatorLayout coordinatorLayout, T t, int n, int n2, int n3) {
            int n4 = this.getTopBottomOffsetForScrollingSibling();
            int n5 = 0;
            if (n2 != 0 && n4 >= n2 && n4 <= n3) {
                n2 = MathUtils.clamp(n, n2, n3);
                n = n5;
                if (n4 != n2) {
                    n = ((AppBarLayout)((Object)t)).hasChildWithInterpolator() ? this.interpolateOffset(t, n2) : n2;
                    boolean bl = this.setTopAndBottomOffset(n);
                    n3 = n4 - n2;
                    this.offsetDelta = n2 - n;
                    if (!bl && ((AppBarLayout)((Object)t)).hasChildWithInterpolator()) {
                        coordinatorLayout.dispatchDependentViewsChanged((View)t);
                    }
                    ((AppBarLayout)((Object)t)).dispatchOffsetUpdates(this.getTopAndBottomOffset());
                    n = n2 < n4 ? -1 : 1;
                    this.updateAppBarLayoutDrawableState(coordinatorLayout, t, n2, n, false);
                    n = n3;
                }
            } else {
                this.offsetDelta = 0;
                n = n5;
            }
            return n;
        }

        public static abstract class BaseDragCallback<T extends AppBarLayout> {
            public abstract boolean canDrag(T var1);
        }

        protected static class SavedState
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
            boolean firstVisibleChildAtMinimumHeight;
            int firstVisibleChildIndex;
            float firstVisibleChildPercentageShown;

            public SavedState(Parcel parcel, ClassLoader classLoader) {
                super(parcel, classLoader);
                this.firstVisibleChildIndex = parcel.readInt();
                this.firstVisibleChildPercentageShown = parcel.readFloat();
                boolean bl = parcel.readByte() != 0;
                this.firstVisibleChildAtMinimumHeight = bl;
            }

            public SavedState(Parcelable parcelable) {
                super(parcelable);
            }

            @Override
            public void writeToParcel(Parcel parcel, int n) {
                super.writeToParcel(parcel, n);
                parcel.writeInt(this.firstVisibleChildIndex);
                parcel.writeFloat(this.firstVisibleChildPercentageShown);
                parcel.writeByte((byte)(this.firstVisibleChildAtMinimumHeight ? 1 : 0));
            }
        }
    }

    public static interface BaseOnOffsetChangedListener<T extends AppBarLayout> {
        public void onOffsetChanged(T var1, int var2);
    }

    public static class Behavior
    extends BaseBehavior<AppBarLayout> {
        public Behavior() {
        }

        public Behavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public static abstract class DragCallback
        extends BaseBehavior.BaseDragCallback<AppBarLayout> {
        }
    }

    public static class LayoutParams
    extends LinearLayout.LayoutParams {
        static final int COLLAPSIBLE_FLAGS = 10;
        static final int FLAG_QUICK_RETURN = 5;
        static final int FLAG_SNAP = 17;
        public static final int SCROLL_FLAG_ENTER_ALWAYS = 4;
        public static final int SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED = 8;
        public static final int SCROLL_FLAG_EXIT_UNTIL_COLLAPSED = 2;
        public static final int SCROLL_FLAG_SCROLL = 1;
        public static final int SCROLL_FLAG_SNAP = 16;
        public static final int SCROLL_FLAG_SNAP_MARGINS = 32;
        int scrollFlags = 1;
        Interpolator scrollInterpolator;

        public LayoutParams(int n, int n2) {
            super(n, n2);
        }

        public LayoutParams(int n, int n2, float f) {
            super(n, n2, f);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            attributeSet = context.obtainStyledAttributes(attributeSet, R.styleable.AppBarLayout_Layout);
            this.scrollFlags = attributeSet.getInt(R.styleable.AppBarLayout_Layout_layout_scrollFlags, 0);
            if (attributeSet.hasValue(R.styleable.AppBarLayout_Layout_layout_scrollInterpolator)) {
                this.scrollInterpolator = android.view.animation.AnimationUtils.loadInterpolator((Context)context, (int)attributeSet.getResourceId(R.styleable.AppBarLayout_Layout_layout_scrollInterpolator, 0));
            }
            attributeSet.recycle();
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }

        public LayoutParams(LinearLayout.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((LinearLayout.LayoutParams)layoutParams);
            this.scrollFlags = layoutParams.scrollFlags;
            this.scrollInterpolator = layoutParams.scrollInterpolator;
        }

        public int getScrollFlags() {
            return this.scrollFlags;
        }

        public Interpolator getScrollInterpolator() {
            return this.scrollInterpolator;
        }

        boolean isCollapsible() {
            int n = this.scrollFlags;
            boolean bl = true;
            if ((n & 1) != 1 || (n & 0xA) == 0) {
                bl = false;
            }
            return bl;
        }

        public void setScrollFlags(int n) {
            this.scrollFlags = n;
        }

        public void setScrollInterpolator(Interpolator interpolator2) {
            this.scrollInterpolator = interpolator2;
        }

        @Retention(value=RetentionPolicy.SOURCE)
        public static @interface ScrollFlags {
        }
    }

    public static interface OnOffsetChangedListener
    extends BaseOnOffsetChangedListener<AppBarLayout> {
        @Override
        public void onOffsetChanged(AppBarLayout var1, int var2);
    }

    public static class ScrollingViewBehavior
    extends HeaderScrollingViewBehavior {
        public ScrollingViewBehavior() {
        }

        public ScrollingViewBehavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            context = context.obtainStyledAttributes(attributeSet, R.styleable.ScrollingViewBehavior_Layout);
            this.setOverlayTop(context.getDimensionPixelSize(R.styleable.ScrollingViewBehavior_Layout_behavior_overlapTop, 0));
            context.recycle();
        }

        private static int getAppBarLayoutOffset(AppBarLayout object) {
            if ((object = ((CoordinatorLayout.LayoutParams)object.getLayoutParams()).getBehavior()) instanceof BaseBehavior) {
                return ((BaseBehavior)object).getTopBottomOffsetForScrollingSibling();
            }
            return 0;
        }

        private void offsetChildAsNeeded(View view, View view2) {
            CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams)view2.getLayoutParams()).getBehavior();
            if (behavior instanceof BaseBehavior) {
                behavior = (BaseBehavior)behavior;
                ViewCompat.offsetTopAndBottom(view, view2.getBottom() - view.getTop() + ((BaseBehavior)behavior).offsetDelta + this.getVerticalLayoutGap() - this.getOverlapPixelsForOffset(view2));
            }
        }

        private void updateLiftedStateIfNeeded(View view, View object) {
            if (object instanceof AppBarLayout && ((AppBarLayout)((Object)(object = (AppBarLayout)((Object)object)))).isLiftOnScroll()) {
                boolean bl = view.getScrollY() > 0;
                ((AppBarLayout)((Object)object)).setLiftedState(bl);
            }
        }

        AppBarLayout findFirstDependency(List<View> list) {
            int n = list.size();
            for (int i = 0; i < n; ++i) {
                View view = list.get(i);
                if (!(view instanceof AppBarLayout)) continue;
                return (AppBarLayout)view;
            }
            return null;
        }

        @Override
        float getOverlapRatioForOffset(View object) {
            if (object instanceof AppBarLayout) {
                object = (AppBarLayout)((Object)object);
                int n = ((AppBarLayout)((Object)object)).getTotalScrollRange();
                int n2 = ((AppBarLayout)((Object)object)).getDownNestedPreScrollRange();
                int n3 = ScrollingViewBehavior.getAppBarLayoutOffset((AppBarLayout)((Object)object));
                if (n2 != 0 && n + n3 <= n2) {
                    return 0.0f;
                }
                if ((n2 = n - n2) != 0) {
                    return (float)n3 / (float)n2 + 1.0f;
                }
            }
            return 0.0f;
        }

        @Override
        int getScrollRange(View view) {
            if (view instanceof AppBarLayout) {
                return ((AppBarLayout)view).getTotalScrollRange();
            }
            return super.getScrollRange(view);
        }

        @Override
        public boolean layoutDependsOn(CoordinatorLayout coordinatorLayout, View view, View view2) {
            return view2 instanceof AppBarLayout;
        }

        @Override
        public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, View view, View view2) {
            this.offsetChildAsNeeded(view, view2);
            this.updateLiftedStateIfNeeded(view, view2);
            return false;
        }

        @Override
        public boolean onRequestChildRectangleOnScreen(CoordinatorLayout coordinatorLayout, View view, Rect rect, boolean bl) {
            AppBarLayout appBarLayout = this.findFirstDependency(coordinatorLayout.getDependencies(view));
            if (appBarLayout != null) {
                rect.offset(view.getLeft(), view.getTop());
                view = this.tempRect1;
                view.set(0, 0, coordinatorLayout.getWidth(), coordinatorLayout.getHeight());
                if (!view.contains(rect)) {
                    appBarLayout.setExpanded(false, bl ^ true);
                    return true;
                }
            }
            return false;
        }
    }
}

