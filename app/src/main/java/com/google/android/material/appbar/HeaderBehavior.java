/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.MotionEvent
 *  android.view.VelocityTracker
 *  android.view.View
 *  android.view.ViewConfiguration
 *  android.widget.OverScroller
 */
package com.google.android.material.appbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;
import androidx.core.view.ViewCompat;
import com.google.android.material.appbar.ViewOffsetBehavior;

abstract class HeaderBehavior<V extends View>
extends ViewOffsetBehavior<V> {
    private static final int INVALID_POINTER = -1;
    private int activePointerId = -1;
    private Runnable flingRunnable;
    private boolean isBeingDragged;
    private int lastMotionY;
    OverScroller scroller;
    private int touchSlop = -1;
    private VelocityTracker velocityTracker;

    public HeaderBehavior() {
    }

    public HeaderBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private void ensureVelocityTracker() {
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
    }

    boolean canDragView(V v) {
        return false;
    }

    final boolean fling(CoordinatorLayout object, V v, int n, int n2, float f) {
        Runnable runnable = this.flingRunnable;
        if (runnable != null) {
            v.removeCallbacks(runnable);
            this.flingRunnable = null;
        }
        if (this.scroller == null) {
            this.scroller = new OverScroller(v.getContext());
        }
        this.scroller.fling(0, this.getTopAndBottomOffset(), 0, Math.round(f), 0, 0, n, n2);
        if (this.scroller.computeScrollOffset()) {
            this.flingRunnable = object = new FlingRunnable(this, (CoordinatorLayout)object, v);
            ViewCompat.postOnAnimation(v, (Runnable)object);
            return true;
        }
        this.onFlingFinished((CoordinatorLayout)object, v);
        return false;
    }

    int getMaxDragOffset(V v) {
        return -v.getHeight();
    }

    int getScrollRangeForDragFling(V v) {
        return v.getHeight();
    }

    int getTopBottomOffsetForScrollingSibling() {
        return this.getTopAndBottomOffset();
    }

    void onFlingFinished(CoordinatorLayout coordinatorLayout, V v) {
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        if (this.touchSlop < 0) {
            this.touchSlop = ViewConfiguration.get((Context)coordinatorLayout.getContext()).getScaledTouchSlop();
        }
        if (motionEvent.getAction() == 2 && this.isBeingDragged) {
            return true;
        }
        switch (motionEvent.getActionMasked()) {
            default: {
                break;
            }
            case 2: {
                int n = this.activePointerId;
                if (n == -1 || (n = motionEvent.findPointerIndex(n)) == -1 || Math.abs((n = (int)motionEvent.getY(n)) - this.lastMotionY) <= this.touchSlop) break;
                this.isBeingDragged = true;
                this.lastMotionY = n;
                break;
            }
            case 1: 
            case 3: {
                this.isBeingDragged = false;
                this.activePointerId = -1;
                coordinatorLayout = this.velocityTracker;
                if (coordinatorLayout == null) break;
                coordinatorLayout.recycle();
                this.velocityTracker = null;
                break;
            }
            case 0: {
                this.isBeingDragged = false;
                int n = (int)motionEvent.getX();
                int n2 = (int)motionEvent.getY();
                if (!this.canDragView(v) || !coordinatorLayout.isPointInChildBounds((View)v, n, n2)) break;
                this.lastMotionY = n2;
                this.activePointerId = motionEvent.getPointerId(0);
                this.ensureVelocityTracker();
            }
        }
        coordinatorLayout = this.velocityTracker;
        if (coordinatorLayout != null) {
            coordinatorLayout.addMovement(motionEvent);
        }
        return this.isBeingDragged;
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        if (this.touchSlop < 0) {
            this.touchSlop = ViewConfiguration.get((Context)coordinatorLayout.getContext()).getScaledTouchSlop();
        }
        switch (motionEvent.getActionMasked()) {
            default: {
                break;
            }
            case 2: {
                int n;
                int n2 = motionEvent.findPointerIndex(this.activePointerId);
                if (n2 == -1) {
                    return false;
                }
                int n3 = (int)motionEvent.getY(n2);
                n2 = n = this.lastMotionY - n3;
                if (!this.isBeingDragged) {
                    int n4 = Math.abs(n);
                    int n5 = this.touchSlop;
                    n2 = n;
                    if (n4 > n5) {
                        this.isBeingDragged = true;
                        n2 = n > 0 ? n - n5 : n + n5;
                    }
                }
                if (!this.isBeingDragged) break;
                this.lastMotionY = n3;
                this.scroll(coordinatorLayout, v, n2, this.getMaxDragOffset(v), 0);
                break;
            }
            case 1: {
                VelocityTracker velocityTracker = this.velocityTracker;
                if (velocityTracker != null) {
                    velocityTracker.addMovement(motionEvent);
                    this.velocityTracker.computeCurrentVelocity(1000);
                    float f = this.velocityTracker.getYVelocity(this.activePointerId);
                    this.fling(coordinatorLayout, v, -this.getScrollRangeForDragFling(v), 0, f);
                }
            }
            case 3: {
                this.isBeingDragged = false;
                this.activePointerId = -1;
                coordinatorLayout = this.velocityTracker;
                if (coordinatorLayout == null) break;
                coordinatorLayout.recycle();
                this.velocityTracker = null;
                break;
            }
            case 0: {
                int n = (int)motionEvent.getX();
                int n6 = (int)motionEvent.getY();
                if (coordinatorLayout.isPointInChildBounds((View)v, n, n6) && this.canDragView(v)) {
                    this.lastMotionY = n6;
                    this.activePointerId = motionEvent.getPointerId(0);
                    this.ensureVelocityTracker();
                    break;
                }
                return false;
            }
        }
        coordinatorLayout = this.velocityTracker;
        if (coordinatorLayout != null) {
            coordinatorLayout.addMovement(motionEvent);
        }
        return true;
    }

    final int scroll(CoordinatorLayout coordinatorLayout, V v, int n, int n2, int n3) {
        return this.setHeaderTopBottomOffset(coordinatorLayout, v, this.getTopBottomOffsetForScrollingSibling() - n, n2, n3);
    }

    int setHeaderTopBottomOffset(CoordinatorLayout coordinatorLayout, V v, int n) {
        return this.setHeaderTopBottomOffset(coordinatorLayout, v, n, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    int setHeaderTopBottomOffset(CoordinatorLayout coordinatorLayout, V v, int n, int n2, int n3) {
        int n4;
        int n5 = this.getTopAndBottomOffset();
        int n6 = n4 = 0;
        if (n2 != 0) {
            n6 = n4;
            if (n5 >= n2) {
                n6 = n4;
                if (n5 <= n3) {
                    n = MathUtils.clamp(n, n2, n3);
                    n6 = n4;
                    if (n5 != n) {
                        this.setTopAndBottomOffset(n);
                        n6 = n5 - n;
                    }
                }
            }
        }
        return n6;
    }

    private class FlingRunnable
    implements Runnable {
        private final V layout;
        private final CoordinatorLayout parent;
        final HeaderBehavior this$0;

        FlingRunnable(CoordinatorLayout coordinatorLayout, V v) {
            this.this$0 = var1_1;
            this.parent = coordinatorLayout;
            this.layout = v;
        }

        @Override
        public void run() {
            if (this.layout != null && this.this$0.scroller != null) {
                if (this.this$0.scroller.computeScrollOffset()) {
                    HeaderBehavior headerBehavior = this.this$0;
                    headerBehavior.setHeaderTopBottomOffset(this.parent, this.layout, headerBehavior.scroller.getCurrY());
                    ViewCompat.postOnAnimation(this.layout, this);
                } else {
                    this.this$0.onFlingFinished(this.parent, this.layout);
                }
            }
        }
    }
}

