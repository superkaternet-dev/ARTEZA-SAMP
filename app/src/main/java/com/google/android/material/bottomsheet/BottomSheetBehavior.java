/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build$VERSION
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.os.Parcelable$Creator
 *  android.util.AttributeSet
 *  android.util.TypedValue
 *  android.view.MotionEvent
 *  android.view.VelocityTracker
 *  android.view.View
 *  android.view.ViewConfiguration
 *  android.view.ViewGroup
 *  android.view.ViewParent
 */
package com.google.android.material.bottomsheet;

import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;
import androidx.core.view.ViewCompat;
import androidx.customview.view.AbsSavedState;
import androidx.customview.widget.ViewDragHelper;
import com.google.android.material.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class BottomSheetBehavior<V extends View>
extends CoordinatorLayout.Behavior<V> {
    private static final float HIDE_FRICTION = 0.1f;
    private static final float HIDE_THRESHOLD = 0.5f;
    public static final int PEEK_HEIGHT_AUTO = -1;
    public static final int STATE_COLLAPSED = 4;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_EXPANDED = 3;
    public static final int STATE_HALF_EXPANDED = 6;
    public static final int STATE_HIDDEN = 5;
    public static final int STATE_SETTLING = 2;
    int activePointerId;
    private BottomSheetCallback callback;
    int collapsedOffset;
    private final ViewDragHelper.Callback dragCallback = new ViewDragHelper.Callback(this){
        final BottomSheetBehavior this$0;
        {
            this.this$0 = bottomSheetBehavior;
        }

        @Override
        public int clampViewPositionHorizontal(View view, int n, int n2) {
            return view.getLeft();
        }

        @Override
        public int clampViewPositionVertical(View view, int n, int n2) {
            int n3 = this.this$0.getExpandedOffset();
            n2 = this.this$0.hideable ? this.this$0.parentHeight : this.this$0.collapsedOffset;
            return MathUtils.clamp(n, n3, n2);
        }

        @Override
        public int getViewVerticalDragRange(View view) {
            if (this.this$0.hideable) {
                return this.this$0.parentHeight;
            }
            return this.this$0.collapsedOffset;
        }

        @Override
        public void onViewDragStateChanged(int n) {
            if (n == 1) {
                this.this$0.setStateInternal(1);
            }
        }

        @Override
        public void onViewPositionChanged(View view, int n, int n2, int n3, int n4) {
            this.this$0.dispatchOnSlide(n2);
        }

        @Override
        public void onViewReleased(View view, float f, float f2) {
            int n;
            int n2;
            if (f2 < 0.0f) {
                if (this.this$0.fitToContents) {
                    n2 = this.this$0.fitToContentsOffset;
                    n = 3;
                } else if (view.getTop() > this.this$0.halfExpandedOffset) {
                    n2 = this.this$0.halfExpandedOffset;
                    n = 6;
                } else {
                    n2 = 0;
                    n = 3;
                }
            } else if (this.this$0.hideable && this.this$0.shouldHide(view, f2) && (view.getTop() > this.this$0.collapsedOffset || Math.abs(f) < Math.abs(f2))) {
                n2 = this.this$0.parentHeight;
                n = 5;
            } else if (f2 != 0.0f && !(Math.abs(f) > Math.abs(f2))) {
                n2 = this.this$0.collapsedOffset;
                n = 4;
            } else {
                n2 = view.getTop();
                if (this.this$0.fitToContents) {
                    if (Math.abs(n2 - this.this$0.fitToContentsOffset) < Math.abs(n2 - this.this$0.collapsedOffset)) {
                        n2 = this.this$0.fitToContentsOffset;
                        n = 3;
                    } else {
                        n2 = this.this$0.collapsedOffset;
                        n = 4;
                    }
                } else if (n2 < this.this$0.halfExpandedOffset) {
                    if (n2 < Math.abs(n2 - this.this$0.collapsedOffset)) {
                        n2 = 0;
                        n = 3;
                    } else {
                        n2 = this.this$0.halfExpandedOffset;
                        n = 6;
                    }
                } else if (Math.abs(n2 - this.this$0.halfExpandedOffset) < Math.abs(n2 - this.this$0.collapsedOffset)) {
                    n2 = this.this$0.halfExpandedOffset;
                    n = 6;
                } else {
                    n2 = this.this$0.collapsedOffset;
                    n = 4;
                }
            }
            if (this.this$0.viewDragHelper.settleCapturedViewAt(view.getLeft(), n2)) {
                this.this$0.setStateInternal(2);
                ViewCompat.postOnAnimation(view, new SettleRunnable(this.this$0, view, n));
            } else {
                this.this$0.setStateInternal(n);
            }
        }

        @Override
        public boolean tryCaptureView(View view, int n) {
            View view2;
            int n2 = this.this$0.state;
            boolean bl = true;
            if (n2 == 1) {
                return false;
            }
            if (this.this$0.touchingScrollingChild) {
                return false;
            }
            if (this.this$0.state == 3 && this.this$0.activePointerId == n && (view2 = (View)this.this$0.nestedScrollingChildRef.get()) != null && view2.canScrollVertically(-1)) {
                return false;
            }
            if (this.this$0.viewRef == null || this.this$0.viewRef.get() != view) {
                bl = false;
            }
            return bl;
        }
    };
    private boolean fitToContents = true;
    int fitToContentsOffset;
    int halfExpandedOffset;
    boolean hideable;
    private boolean ignoreEvents;
    private Map<View, Integer> importantForAccessibilityMap;
    private int initialY;
    private int lastNestedScrollDy;
    private int lastPeekHeight;
    private float maximumVelocity;
    private boolean nestedScrolled;
    WeakReference<View> nestedScrollingChildRef;
    int parentHeight;
    private int peekHeight;
    private boolean peekHeightAuto;
    private int peekHeightMin;
    private boolean skipCollapsed;
    int state = 4;
    boolean touchingScrollingChild;
    private VelocityTracker velocityTracker;
    ViewDragHelper viewDragHelper;
    WeakReference<V> viewRef;

    public BottomSheetBehavior() {
    }

    public BottomSheetBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        attributeSet = context.obtainStyledAttributes(attributeSet, R.styleable.BottomSheetBehavior_Layout);
        TypedValue typedValue = attributeSet.peekValue(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight);
        if (typedValue != null && typedValue.data == -1) {
            this.setPeekHeight(typedValue.data);
        } else {
            this.setPeekHeight(attributeSet.getDimensionPixelSize(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight, -1));
        }
        this.setHideable(attributeSet.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_hideable, false));
        this.setFitToContents(attributeSet.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_fitToContents, true));
        this.setSkipCollapsed(attributeSet.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_skipCollapsed, false));
        attributeSet.recycle();
        this.maximumVelocity = ViewConfiguration.get((Context)context).getScaledMaximumFlingVelocity();
    }

    private void calculateCollapsedOffset() {
        this.collapsedOffset = this.fitToContents ? Math.max(this.parentHeight - this.lastPeekHeight, this.fitToContentsOffset) : this.parentHeight - this.lastPeekHeight;
    }

    public static <V extends View> BottomSheetBehavior<V> from(V object) {
        if ((object = object.getLayoutParams()) instanceof CoordinatorLayout.LayoutParams) {
            if ((object = ((CoordinatorLayout.LayoutParams)((Object)object)).getBehavior()) instanceof BottomSheetBehavior) {
                return (BottomSheetBehavior)object;
            }
            throw new IllegalArgumentException("The view is not associated with BottomSheetBehavior");
        }
        throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
    }

    private int getExpandedOffset() {
        int n = this.fitToContents ? this.fitToContentsOffset : 0;
        return n;
    }

    private float getYVelocity() {
        VelocityTracker velocityTracker = this.velocityTracker;
        if (velocityTracker == null) {
            return 0.0f;
        }
        velocityTracker.computeCurrentVelocity(1000, this.maximumVelocity);
        return this.velocityTracker.getYVelocity(this.activePointerId);
    }

    private void reset() {
        this.activePointerId = -1;
        VelocityTracker velocityTracker = this.velocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.velocityTracker = null;
        }
    }

    private void updateImportantForAccessibility(boolean bl) {
        Object object = this.viewRef;
        if (object == null) {
            return;
        }
        if (!((object = ((View)((Reference)object).get()).getParent()) instanceof CoordinatorLayout)) {
            return;
        }
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout)object;
        int n = coordinatorLayout.getChildCount();
        if (Build.VERSION.SDK_INT >= 16 && bl) {
            if (this.importantForAccessibilityMap == null) {
                this.importantForAccessibilityMap = new HashMap<View, Integer>(n);
            } else {
                return;
            }
        }
        for (int i = 0; i < n; ++i) {
            View view = coordinatorLayout.getChildAt(i);
            if (view == this.viewRef.get()) continue;
            if (!bl) {
                object = this.importantForAccessibilityMap;
                if (object == null || !object.containsKey(view)) continue;
                ViewCompat.setImportantForAccessibility(view, this.importantForAccessibilityMap.get(view));
                continue;
            }
            if (Build.VERSION.SDK_INT >= 16) {
                this.importantForAccessibilityMap.put(view, view.getImportantForAccessibility());
            }
            ViewCompat.setImportantForAccessibility(view, 4);
        }
        if (!bl) {
            this.importantForAccessibilityMap = null;
        }
    }

    void dispatchOnSlide(int n) {
        BottomSheetCallback bottomSheetCallback;
        View view = (View)this.viewRef.get();
        if (view != null && (bottomSheetCallback = this.callback) != null) {
            int n2 = this.collapsedOffset;
            if (n > n2) {
                bottomSheetCallback.onSlide(view, (float)(n2 - n) / (float)(this.parentHeight - n2));
            } else {
                bottomSheetCallback.onSlide(view, (float)(n2 - n) / (float)(n2 - this.getExpandedOffset()));
            }
        }
    }

    View findScrollingChild(View view) {
        if (ViewCompat.isNestedScrollingEnabled(view)) {
            return view;
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup)view;
            int n = viewGroup.getChildCount();
            for (int i = 0; i < n; ++i) {
                view = this.findScrollingChild(viewGroup.getChildAt(i));
                if (view == null) continue;
                return view;
            }
        }
        return null;
    }

    public final int getPeekHeight() {
        int n = this.peekHeightAuto ? -1 : this.peekHeight;
        return n;
    }

    int getPeekHeightMin() {
        return this.peekHeightMin;
    }

    public boolean getSkipCollapsed() {
        return this.skipCollapsed;
    }

    public final int getState() {
        return this.state;
    }

    public boolean isFitToContents() {
        return this.fitToContents;
    }

    public boolean isHideable() {
        return this.hideable;
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, V object, MotionEvent motionEvent) {
        Object object2;
        boolean bl = object.isShown();
        boolean bl2 = false;
        if (!bl) {
            this.ignoreEvents = true;
            return false;
        }
        int n = motionEvent.getActionMasked();
        if (n == 0) {
            this.reset();
        }
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
        this.velocityTracker.addMovement(motionEvent);
        Object var9_7 = null;
        switch (n) {
            default: {
                break;
            }
            case 1: 
            case 3: {
                this.touchingScrollingChild = false;
                this.activePointerId = -1;
                if (!this.ignoreEvents) break;
                this.ignoreEvents = false;
                return false;
            }
            case 0: {
                int n2 = (int)motionEvent.getX();
                this.initialY = (int)motionEvent.getY();
                object2 = this.nestedScrollingChildRef;
                object2 = object2 != null ? (View)object2.get() : null;
                if (object2 != null && coordinatorLayout.isPointInChildBounds((View)object2, n2, this.initialY)) {
                    this.activePointerId = motionEvent.getPointerId(motionEvent.getActionIndex());
                    this.touchingScrollingChild = true;
                }
                bl = this.activePointerId == -1 && !coordinatorLayout.isPointInChildBounds((View)object, n2, this.initialY);
                this.ignoreEvents = bl;
            }
        }
        if (!this.ignoreEvents && (object = this.viewDragHelper) != null && ((ViewDragHelper)object).shouldInterceptTouchEvent(motionEvent)) {
            return true;
        }
        object2 = this.nestedScrollingChildRef;
        object = var9_7;
        if (object2 != null) {
            object = (View)object2.get();
        }
        bl = n == 2 && object != null && !this.ignoreEvents && this.state != 1 && !coordinatorLayout.isPointInChildBounds((View)object, (int)motionEvent.getX(), (int)motionEvent.getY()) && this.viewDragHelper != null && Math.abs((float)this.initialY - motionEvent.getY()) > (float)this.viewDragHelper.getTouchSlop() ? true : bl2;
        return bl;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, V v, int n) {
        if (ViewCompat.getFitsSystemWindows((View)coordinatorLayout) && !ViewCompat.getFitsSystemWindows(v)) {
            v.setFitsSystemWindows(true);
        }
        int n2 = v.getTop();
        coordinatorLayout.onLayoutChild((View)v, n);
        this.parentHeight = coordinatorLayout.getHeight();
        if (this.peekHeightAuto) {
            if (this.peekHeightMin == 0) {
                this.peekHeightMin = coordinatorLayout.getResources().getDimensionPixelSize(R.dimen.design_bottom_sheet_peek_height_min);
            }
            this.lastPeekHeight = Math.max(this.peekHeightMin, this.parentHeight - coordinatorLayout.getWidth() * 9 / 16);
        } else {
            this.lastPeekHeight = this.peekHeight;
        }
        this.fitToContentsOffset = Math.max(0, this.parentHeight - v.getHeight());
        this.halfExpandedOffset = this.parentHeight / 2;
        this.calculateCollapsedOffset();
        n = this.state;
        if (n == 3) {
            ViewCompat.offsetTopAndBottom(v, this.getExpandedOffset());
        } else if (n == 6) {
            ViewCompat.offsetTopAndBottom(v, this.halfExpandedOffset);
        } else if (this.hideable && n == 5) {
            ViewCompat.offsetTopAndBottom(v, this.parentHeight);
        } else if (n == 4) {
            ViewCompat.offsetTopAndBottom(v, this.collapsedOffset);
        } else if (n == 1 || n == 2) {
            ViewCompat.offsetTopAndBottom(v, n2 - v.getTop());
        }
        if (this.viewDragHelper == null) {
            this.viewDragHelper = ViewDragHelper.create(coordinatorLayout, this.dragCallback);
        }
        this.viewRef = new WeakReference<V>(v);
        this.nestedScrollingChildRef = new WeakReference<View>(this.findScrollingChild((View)v));
        return true;
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, V v, View view, float f, float f2) {
        boolean bl = view == this.nestedScrollingChildRef.get() && (this.state != 3 || super.onNestedPreFling(coordinatorLayout, v, view, f, f2));
        return bl;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, V v, View view, int n, int n2, int[] nArray, int n3) {
        if (n3 == 1) {
            return;
        }
        if (view != (View)this.nestedScrollingChildRef.get()) {
            return;
        }
        n3 = v.getTop();
        n = n3 - n2;
        if (n2 > 0) {
            if (n < this.getExpandedOffset()) {
                nArray[1] = n3 - this.getExpandedOffset();
                ViewCompat.offsetTopAndBottom(v, -nArray[1]);
                this.setStateInternal(3);
            } else {
                nArray[1] = n2;
                ViewCompat.offsetTopAndBottom(v, -n2);
                this.setStateInternal(1);
            }
        } else if (n2 < 0 && !view.canScrollVertically(-1)) {
            int n4 = this.collapsedOffset;
            if (n > n4 && !this.hideable) {
                nArray[1] = n3 - n4;
                ViewCompat.offsetTopAndBottom(v, -nArray[1]);
                this.setStateInternal(4);
            } else {
                nArray[1] = n2;
                ViewCompat.offsetTopAndBottom(v, -n2);
                this.setStateInternal(1);
            }
        }
        this.dispatchOnSlide(v.getTop());
        this.lastNestedScrollDy = n2;
        this.nestedScrolled = true;
    }

    @Override
    public void onRestoreInstanceState(CoordinatorLayout coordinatorLayout, V v, Parcelable parcelable) {
        parcelable = (SavedState)parcelable;
        super.onRestoreInstanceState(coordinatorLayout, v, parcelable.getSuperState());
        this.state = parcelable.state != 1 && parcelable.state != 2 ? parcelable.state : 4;
    }

    @Override
    public Parcelable onSaveInstanceState(CoordinatorLayout coordinatorLayout, V v) {
        return new SavedState(super.onSaveInstanceState(coordinatorLayout, v), this.state);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V v, View view, View view2, int n, int n2) {
        boolean bl = false;
        this.lastNestedScrollDy = 0;
        this.nestedScrolled = false;
        if ((n & 2) != 0) {
            bl = true;
        }
        return bl;
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, V v, View view, int n) {
        if (v.getTop() == this.getExpandedOffset()) {
            this.setStateInternal(3);
            return;
        }
        if (view == this.nestedScrollingChildRef.get() && this.nestedScrolled) {
            int n2;
            if (this.lastNestedScrollDy > 0) {
                n = this.getExpandedOffset();
                n2 = 3;
            } else if (this.hideable && this.shouldHide((View)v, this.getYVelocity())) {
                n = this.parentHeight;
                n2 = 5;
            } else if (this.lastNestedScrollDy == 0) {
                n2 = v.getTop();
                if (this.fitToContents) {
                    if (Math.abs(n2 - this.fitToContentsOffset) < Math.abs(n2 - this.collapsedOffset)) {
                        n = this.fitToContentsOffset;
                        n2 = 3;
                    } else {
                        n = this.collapsedOffset;
                        n2 = 4;
                    }
                } else {
                    n = this.halfExpandedOffset;
                    if (n2 < n) {
                        if (n2 < Math.abs(n2 - this.collapsedOffset)) {
                            n = 0;
                            n2 = 3;
                        } else {
                            n = this.halfExpandedOffset;
                            n2 = 6;
                        }
                    } else if (Math.abs(n2 - n) < Math.abs(n2 - this.collapsedOffset)) {
                        n = this.halfExpandedOffset;
                        n2 = 6;
                    } else {
                        n = this.collapsedOffset;
                        n2 = 4;
                    }
                }
            } else {
                n = this.collapsedOffset;
                n2 = 4;
            }
            if (this.viewDragHelper.smoothSlideViewTo((View)v, v.getLeft(), n)) {
                this.setStateInternal(2);
                ViewCompat.postOnAnimation(v, new SettleRunnable(this, (View)v, n2));
            } else {
                this.setStateInternal(n2);
            }
            this.nestedScrolled = false;
            return;
        }
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout object, V v, MotionEvent motionEvent) {
        if (!v.isShown()) {
            return false;
        }
        int n = motionEvent.getActionMasked();
        if (this.state == 1 && n == 0) {
            return true;
        }
        object = this.viewDragHelper;
        if (object != null) {
            ((ViewDragHelper)object).processTouchEvent(motionEvent);
        }
        if (n == 0) {
            this.reset();
        }
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
        this.velocityTracker.addMovement(motionEvent);
        if (n == 2 && !this.ignoreEvents && Math.abs((float)this.initialY - motionEvent.getY()) > (float)this.viewDragHelper.getTouchSlop()) {
            this.viewDragHelper.captureChildView((View)v, motionEvent.getPointerId(motionEvent.getActionIndex()));
        }
        return this.ignoreEvents ^ true;
    }

    public void setBottomSheetCallback(BottomSheetCallback bottomSheetCallback) {
        this.callback = bottomSheetCallback;
    }

    public void setFitToContents(boolean bl) {
        if (this.fitToContents == bl) {
            return;
        }
        this.fitToContents = bl;
        if (this.viewRef != null) {
            this.calculateCollapsedOffset();
        }
        int n = this.fitToContents && this.state == 6 ? 3 : this.state;
        this.setStateInternal(n);
    }

    public void setHideable(boolean bl) {
        this.hideable = bl;
    }

    public final void setPeekHeight(int n) {
        View view;
        boolean bl = false;
        if (n == -1) {
            if (!this.peekHeightAuto) {
                this.peekHeightAuto = true;
                bl = true;
            }
        } else if (this.peekHeightAuto || this.peekHeight != n) {
            this.peekHeightAuto = false;
            this.peekHeight = Math.max(0, n);
            this.collapsedOffset = this.parentHeight - n;
            bl = true;
        }
        if (bl && this.state == 4 && (view = this.viewRef) != null && (view = (View)view.get()) != null) {
            view.requestLayout();
        }
    }

    public void setSkipCollapsed(boolean bl) {
        this.skipCollapsed = bl;
    }

    public final void setState(int n) {
        if (n == this.state) {
            return;
        }
        ViewParent viewParent = this.viewRef;
        if (viewParent == null) {
            if (n == 4 || n == 3 || n == 6 || this.hideable && n == 5) {
                this.state = n;
            }
            return;
        }
        View view = (View)viewParent.get();
        if (view == null) {
            return;
        }
        viewParent = view.getParent();
        if (viewParent != null && viewParent.isLayoutRequested() && ViewCompat.isAttachedToWindow(view)) {
            view.post(new Runnable(this, view, n){
                final BottomSheetBehavior this$0;
                final View val$child;
                final int val$finalState;
                {
                    this.this$0 = bottomSheetBehavior;
                    this.val$child = view;
                    this.val$finalState = n;
                }

                @Override
                public void run() {
                    this.this$0.startSettlingAnimation(this.val$child, this.val$finalState);
                }
            });
        } else {
            this.startSettlingAnimation(view, n);
        }
    }

    void setStateInternal(int n) {
        BottomSheetCallback bottomSheetCallback;
        View view;
        if (this.state == n) {
            return;
        }
        this.state = n;
        if (n != 6 && n != 3) {
            if (n == 5 || n == 4) {
                this.updateImportantForAccessibility(false);
            }
        } else {
            this.updateImportantForAccessibility(true);
        }
        if ((view = (View)this.viewRef.get()) != null && (bottomSheetCallback = this.callback) != null) {
            bottomSheetCallback.onStateChanged(view, n);
        }
    }

    boolean shouldHide(View view, float f) {
        boolean bl = this.skipCollapsed;
        boolean bl2 = true;
        if (bl) {
            return true;
        }
        if (view.getTop() < this.collapsedOffset) {
            return false;
        }
        if (!(Math.abs((float)view.getTop() + 0.1f * f - (float)this.collapsedOffset) / (float)this.peekHeight > 0.5f)) {
            bl2 = false;
        }
        return bl2;
    }

    void startSettlingAnimation(View object, int n) {
        block12: {
            int n2;
            int n3;
            block9: {
                block11: {
                    block10: {
                        int n4;
                        block8: {
                            if (n != 4) break block8;
                            n3 = this.collapsedOffset;
                            n2 = n;
                            break block9;
                        }
                        if (n != 6) break block10;
                        n3 = n4 = this.halfExpandedOffset;
                        n2 = n;
                        if (this.fitToContents) {
                            n3 = n4;
                            n2 = n;
                            if (n4 <= this.fitToContentsOffset) {
                                n2 = 3;
                                n3 = this.fitToContentsOffset;
                            }
                        }
                        break block9;
                    }
                    if (n != 3) break block11;
                    n3 = this.getExpandedOffset();
                    n2 = n;
                    break block9;
                }
                if (!this.hideable || n != 5) break block12;
                n3 = this.parentHeight;
                n2 = n;
            }
            if (this.viewDragHelper.smoothSlideViewTo((View)object, object.getLeft(), n3)) {
                this.setStateInternal(2);
                ViewCompat.postOnAnimation((View)object, new SettleRunnable(this, (View)object, n2));
            } else {
                this.setStateInternal(n2);
            }
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Illegal state argument: ");
        ((StringBuilder)object).append(n);
        throw new IllegalArgumentException(((StringBuilder)object).toString());
    }

    public static abstract class BottomSheetCallback {
        public abstract void onSlide(View var1, float var2);

        public abstract void onStateChanged(View var1, int var2);
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
        final int state;

        public SavedState(Parcel parcel) {
            this(parcel, null);
        }

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.state = parcel.readInt();
        }

        public SavedState(Parcelable parcelable, int n) {
            super(parcelable);
            this.state = n;
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.state);
        }
    }

    private class SettleRunnable
    implements Runnable {
        private final int targetState;
        final BottomSheetBehavior this$0;
        private final View view;

        SettleRunnable(BottomSheetBehavior bottomSheetBehavior, View view, int n) {
            this.this$0 = bottomSheetBehavior;
            this.view = view;
            this.targetState = n;
        }

        @Override
        public void run() {
            if (this.this$0.viewDragHelper != null && this.this$0.viewDragHelper.continueSettling(true)) {
                ViewCompat.postOnAnimation(this.view, this);
            } else {
                this.this$0.setStateInternal(this.targetState);
            }
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface State {
    }
}

