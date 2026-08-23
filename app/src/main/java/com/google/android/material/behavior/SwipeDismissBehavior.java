/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.ViewGroup
 */
package com.google.android.material.behavior;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

public class SwipeDismissBehavior<V extends View>
extends CoordinatorLayout.Behavior<V> {
    private static final float DEFAULT_ALPHA_END_DISTANCE = 0.5f;
    private static final float DEFAULT_ALPHA_START_DISTANCE = 0.0f;
    private static final float DEFAULT_DRAG_DISMISS_THRESHOLD = 0.5f;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    public static final int SWIPE_DIRECTION_ANY = 2;
    public static final int SWIPE_DIRECTION_END_TO_START = 1;
    public static final int SWIPE_DIRECTION_START_TO_END = 0;
    float alphaEndSwipeDistance = 0.5f;
    float alphaStartSwipeDistance = 0.0f;
    private final ViewDragHelper.Callback dragCallback = new ViewDragHelper.Callback(this){
        private static final int INVALID_POINTER_ID = -1;
        private int activePointerId;
        private int originalCapturedViewLeft;
        final SwipeDismissBehavior this$0;
        {
            this.this$0 = swipeDismissBehavior;
            this.activePointerId = -1;
        }

        private boolean shouldDismiss(View view, float f) {
            boolean bl;
            boolean bl2;
            block7: {
                boolean bl3;
                block8: {
                    block10: {
                        block11: {
                            boolean bl4;
                            block9: {
                                bl2 = false;
                                bl = false;
                                bl4 = false;
                                if (f == 0.0f) break block7;
                                bl3 = ViewCompat.getLayoutDirection(view) == 1;
                                if (this.this$0.swipeDirection == 2) {
                                    return true;
                                }
                                if (this.this$0.swipeDirection != 0) break block8;
                                if (!bl3) break block9;
                                bl2 = bl4;
                                if (!(f < 0.0f)) break block10;
                                break block11;
                            }
                            bl2 = bl4;
                            if (!(f > 0.0f)) break block10;
                        }
                        bl2 = true;
                    }
                    return bl2;
                }
                if (this.this$0.swipeDirection == 1) {
                    if (bl3 ? f > 0.0f : f < 0.0f) {
                        bl2 = true;
                    }
                    return bl2;
                }
                return false;
            }
            int n = view.getLeft();
            int n2 = this.originalCapturedViewLeft;
            int n3 = Math.round((float)view.getWidth() * this.this$0.dragDismissThreshold);
            bl2 = bl;
            if (Math.abs(n - n2) >= n3) {
                bl2 = true;
            }
            return bl2;
        }

        @Override
        public int clampViewPositionHorizontal(View view, int n, int n2) {
            int n3;
            n2 = ViewCompat.getLayoutDirection(view) == 1 ? 1 : 0;
            if (this.this$0.swipeDirection == 0) {
                if (n2 != 0) {
                    n2 = this.originalCapturedViewLeft - view.getWidth();
                    n3 = this.originalCapturedViewLeft;
                } else {
                    n2 = this.originalCapturedViewLeft;
                    n3 = this.originalCapturedViewLeft + view.getWidth();
                }
            } else if (this.this$0.swipeDirection == 1) {
                if (n2 != 0) {
                    n2 = this.originalCapturedViewLeft;
                    n3 = this.originalCapturedViewLeft + view.getWidth();
                } else {
                    n2 = this.originalCapturedViewLeft - view.getWidth();
                    n3 = this.originalCapturedViewLeft;
                }
            } else {
                n2 = this.originalCapturedViewLeft - view.getWidth();
                n3 = this.originalCapturedViewLeft + view.getWidth();
            }
            return SwipeDismissBehavior.clamp(n2, n, n3);
        }

        @Override
        public int clampViewPositionVertical(View view, int n, int n2) {
            return view.getTop();
        }

        @Override
        public int getViewHorizontalDragRange(View view) {
            return view.getWidth();
        }

        @Override
        public void onViewCaptured(View view, int n) {
            this.activePointerId = n;
            this.originalCapturedViewLeft = view.getLeft();
            if ((view = view.getParent()) != null) {
                view.requestDisallowInterceptTouchEvent(true);
            }
        }

        @Override
        public void onViewDragStateChanged(int n) {
            if (this.this$0.listener != null) {
                this.this$0.listener.onDragStateChanged(n);
            }
        }

        @Override
        public void onViewPositionChanged(View view, int n, int n2, int n3, int n4) {
            float f = (float)this.originalCapturedViewLeft + (float)view.getWidth() * this.this$0.alphaStartSwipeDistance;
            float f2 = (float)this.originalCapturedViewLeft + (float)view.getWidth() * this.this$0.alphaEndSwipeDistance;
            if ((float)n <= f) {
                view.setAlpha(1.0f);
            } else if ((float)n >= f2) {
                view.setAlpha(0.0f);
            } else {
                view.setAlpha(SwipeDismissBehavior.clamp(0.0f, 1.0f - SwipeDismissBehavior.fraction(f, f2, n), 1.0f));
            }
        }

        @Override
        public void onViewReleased(View view, float f, float f2) {
            int n;
            this.activePointerId = -1;
            int n2 = view.getWidth();
            boolean bl = false;
            if (this.shouldDismiss(view, f)) {
                int n3 = view.getLeft();
                n = n3 < (n = this.originalCapturedViewLeft) ? (n -= n2) : (n += n2);
                bl = true;
            } else {
                n = this.originalCapturedViewLeft;
            }
            if (this.this$0.viewDragHelper.settleCapturedViewAt(n, view.getTop())) {
                ViewCompat.postOnAnimation(view, new SettleRunnable(this.this$0, view, bl));
            } else if (bl && this.this$0.listener != null) {
                this.this$0.listener.onDismiss(view);
            }
        }

        @Override
        public boolean tryCaptureView(View view, int n) {
            boolean bl = this.activePointerId == -1 && this.this$0.canSwipeDismissView(view);
            return bl;
        }
    };
    float dragDismissThreshold = 0.5f;
    private boolean interceptingEvents;
    OnDismissListener listener;
    private float sensitivity = 0.0f;
    private boolean sensitivitySet;
    int swipeDirection = 2;
    ViewDragHelper viewDragHelper;

    static float clamp(float f, float f2, float f3) {
        return Math.min(Math.max(f, f2), f3);
    }

    static int clamp(int n, int n2, int n3) {
        return Math.min(Math.max(n, n2), n3);
    }

    private void ensureViewDragHelper(ViewGroup object) {
        if (this.viewDragHelper == null) {
            object = this.sensitivitySet ? ViewDragHelper.create(object, this.sensitivity, this.dragCallback) : ViewDragHelper.create(object, this.dragCallback);
            this.viewDragHelper = object;
        }
    }

    static float fraction(float f, float f2, float f3) {
        return (f3 - f) / (f2 - f);
    }

    public boolean canSwipeDismissView(View view) {
        return true;
    }

    public int getDragState() {
        ViewDragHelper viewDragHelper = this.viewDragHelper;
        int n = viewDragHelper != null ? viewDragHelper.getViewDragState() : 0;
        return n;
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        boolean bl = this.interceptingEvents;
        switch (motionEvent.getActionMasked()) {
            default: {
                break;
            }
            case 1: 
            case 3: {
                this.interceptingEvents = false;
                break;
            }
            case 0: {
                bl = this.interceptingEvents = coordinatorLayout.isPointInChildBounds((View)v, (int)motionEvent.getX(), (int)motionEvent.getY());
            }
        }
        if (bl) {
            this.ensureViewDragHelper(coordinatorLayout);
            return this.viewDragHelper.shouldInterceptTouchEvent(motionEvent);
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout object, V v, MotionEvent motionEvent) {
        object = this.viewDragHelper;
        if (object != null) {
            ((ViewDragHelper)object).processTouchEvent(motionEvent);
            return true;
        }
        return false;
    }

    public void setDragDismissDistance(float f) {
        this.dragDismissThreshold = SwipeDismissBehavior.clamp(0.0f, f, 1.0f);
    }

    public void setEndAlphaSwipeDistance(float f) {
        this.alphaEndSwipeDistance = SwipeDismissBehavior.clamp(0.0f, f, 1.0f);
    }

    public void setListener(OnDismissListener onDismissListener) {
        this.listener = onDismissListener;
    }

    public void setSensitivity(float f) {
        this.sensitivity = f;
        this.sensitivitySet = true;
    }

    public void setStartAlphaSwipeDistance(float f) {
        this.alphaStartSwipeDistance = SwipeDismissBehavior.clamp(0.0f, f, 1.0f);
    }

    public void setSwipeDirection(int n) {
        this.swipeDirection = n;
    }

    public static interface OnDismissListener {
        public void onDismiss(View var1);

        public void onDragStateChanged(int var1);
    }

    private class SettleRunnable
    implements Runnable {
        private final boolean dismiss;
        final SwipeDismissBehavior this$0;
        private final View view;

        SettleRunnable(SwipeDismissBehavior swipeDismissBehavior, View view, boolean bl) {
            this.this$0 = swipeDismissBehavior;
            this.view = view;
            this.dismiss = bl;
        }

        @Override
        public void run() {
            if (this.this$0.viewDragHelper != null && this.this$0.viewDragHelper.continueSettling(true)) {
                ViewCompat.postOnAnimation(this.view, this);
            } else if (this.dismiss && this.this$0.listener != null) {
                this.this$0.listener.onDismiss(this.view);
            }
        }
    }
}

