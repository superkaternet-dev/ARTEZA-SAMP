/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.SystemClock
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnAttachStateChangeListener
 *  android.view.View$OnTouchListener
 *  android.view.ViewConfiguration
 *  android.view.ViewParent
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import androidx.appcompat.view.menu.ShowableListMenu;
import androidx.appcompat.widget.DropDownListView;

public abstract class ForwardingListener
implements View.OnTouchListener,
View.OnAttachStateChangeListener {
    private int mActivePointerId;
    private Runnable mDisallowIntercept;
    private boolean mForwarding;
    private final int mLongPressTimeout;
    private final float mScaledTouchSlop;
    final View mSrc;
    private final int mTapTimeout;
    private final int[] mTmpLocation = new int[2];
    private Runnable mTriggerLongPress;

    public ForwardingListener(View view) {
        int n;
        this.mSrc = view;
        view.setLongClickable(true);
        view.addOnAttachStateChangeListener((View.OnAttachStateChangeListener)this);
        this.mScaledTouchSlop = ViewConfiguration.get((Context)view.getContext()).getScaledTouchSlop();
        this.mTapTimeout = n = ViewConfiguration.getTapTimeout();
        this.mLongPressTimeout = (n + ViewConfiguration.getLongPressTimeout()) / 2;
    }

    private void clearCallbacks() {
        Runnable runnable = this.mTriggerLongPress;
        if (runnable != null) {
            this.mSrc.removeCallbacks(runnable);
        }
        if ((runnable = this.mDisallowIntercept) != null) {
            this.mSrc.removeCallbacks(runnable);
        }
    }

    private boolean onTouchForwarded(MotionEvent motionEvent) {
        View view = this.mSrc;
        ShowableListMenu showableListMenu = this.getPopup();
        boolean bl = false;
        if (showableListMenu != null && showableListMenu.isShowing()) {
            DropDownListView dropDownListView = (DropDownListView)showableListMenu.getListView();
            if (dropDownListView != null && dropDownListView.isShown()) {
                showableListMenu = MotionEvent.obtainNoHistory((MotionEvent)motionEvent);
                this.toGlobalMotionEvent(view, (MotionEvent)showableListMenu);
                this.toLocalMotionEvent((View)dropDownListView, (MotionEvent)showableListMenu);
                boolean bl2 = dropDownListView.onForwardedEvent((MotionEvent)showableListMenu, this.mActivePointerId);
                showableListMenu.recycle();
                int n = motionEvent.getActionMasked();
                n = n != 1 && n != 3 ? 1 : 0;
                boolean bl3 = bl;
                if (bl2) {
                    bl3 = bl;
                    if (n != 0) {
                        bl3 = true;
                    }
                }
                return bl3;
            }
            return false;
        }
        return false;
    }

    private boolean onTouchObserved(MotionEvent motionEvent) {
        View view = this.mSrc;
        if (!view.isEnabled()) {
            return false;
        }
        switch (motionEvent.getActionMasked()) {
            default: {
                break;
            }
            case 2: {
                int n = motionEvent.findPointerIndex(this.mActivePointerId);
                if (n < 0 || ForwardingListener.pointInView(view, motionEvent.getX(n), motionEvent.getY(n), this.mScaledTouchSlop)) break;
                this.clearCallbacks();
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            }
            case 1: 
            case 3: {
                this.clearCallbacks();
                break;
            }
            case 0: {
                this.mActivePointerId = motionEvent.getPointerId(0);
                if (this.mDisallowIntercept == null) {
                    this.mDisallowIntercept = new DisallowIntercept(this);
                }
                view.postDelayed(this.mDisallowIntercept, (long)this.mTapTimeout);
                if (this.mTriggerLongPress == null) {
                    this.mTriggerLongPress = new TriggerLongPress(this);
                }
                view.postDelayed(this.mTriggerLongPress, (long)this.mLongPressTimeout);
            }
        }
        return false;
    }

    private static boolean pointInView(View view, float f, float f2, float f3) {
        boolean bl = f >= -f3 && f2 >= -f3 && f < (float)(view.getRight() - view.getLeft()) + f3 && f2 < (float)(view.getBottom() - view.getTop()) + f3;
        return bl;
    }

    private boolean toGlobalMotionEvent(View view, MotionEvent motionEvent) {
        int[] nArray = this.mTmpLocation;
        view.getLocationOnScreen(nArray);
        motionEvent.offsetLocation((float)nArray[0], (float)nArray[1]);
        return true;
    }

    private boolean toLocalMotionEvent(View view, MotionEvent motionEvent) {
        int[] nArray = this.mTmpLocation;
        view.getLocationOnScreen(nArray);
        motionEvent.offsetLocation((float)(-nArray[0]), (float)(-nArray[1]));
        return true;
    }

    public abstract ShowableListMenu getPopup();

    protected boolean onForwardingStarted() {
        ShowableListMenu showableListMenu = this.getPopup();
        if (showableListMenu != null && !showableListMenu.isShowing()) {
            showableListMenu.show();
        }
        return true;
    }

    protected boolean onForwardingStopped() {
        ShowableListMenu showableListMenu = this.getPopup();
        if (showableListMenu != null && showableListMenu.isShowing()) {
            showableListMenu.dismiss();
        }
        return true;
    }

    void onLongPress() {
        this.clearCallbacks();
        View view = this.mSrc;
        if (view.isEnabled() && !view.isLongClickable()) {
            if (!this.onForwardingStarted()) {
                return;
            }
            view.getParent().requestDisallowInterceptTouchEvent(true);
            long l = SystemClock.uptimeMillis();
            MotionEvent motionEvent = MotionEvent.obtain((long)l, (long)l, (int)3, (float)0.0f, (float)0.0f, (int)0);
            view.onTouchEvent(motionEvent);
            motionEvent.recycle();
            this.mForwarding = true;
            return;
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean bl;
        boolean bl2;
        boolean bl3 = this.mForwarding;
        boolean bl4 = true;
        if (bl3) {
            bl2 = this.onTouchForwarded(motionEvent) || !this.onForwardingStopped();
            bl = bl2;
        } else {
            bl2 = this.onTouchObserved(motionEvent) && this.onForwardingStarted();
            bl = bl2;
            if (bl2) {
                long l = SystemClock.uptimeMillis();
                view = MotionEvent.obtain((long)l, (long)l, (int)3, (float)0.0f, (float)0.0f, (int)0);
                this.mSrc.onTouchEvent((MotionEvent)view);
                view.recycle();
                bl = bl2;
            }
        }
        this.mForwarding = bl;
        bl2 = bl4;
        if (!bl) {
            bl2 = bl3 ? bl4 : false;
        }
        return bl2;
    }

    public void onViewAttachedToWindow(View view) {
    }

    public void onViewDetachedFromWindow(View object) {
        this.mForwarding = false;
        this.mActivePointerId = -1;
        object = this.mDisallowIntercept;
        if (object != null) {
            this.mSrc.removeCallbacks((Runnable)object);
        }
    }

    private class DisallowIntercept
    implements Runnable {
        final ForwardingListener this$0;

        DisallowIntercept(ForwardingListener forwardingListener) {
            this.this$0 = forwardingListener;
        }

        @Override
        public void run() {
            ViewParent viewParent = this.this$0.mSrc.getParent();
            if (viewParent != null) {
                viewParent.requestDisallowInterceptTouchEvent(true);
            }
        }
    }

    private class TriggerLongPress
    implements Runnable {
        final ForwardingListener this$0;

        TriggerLongPress(ForwardingListener forwardingListener) {
            this.this$0 = forwardingListener;
        }

        @Override
        public void run() {
            this.this$0.onLongPress();
        }
    }
}

