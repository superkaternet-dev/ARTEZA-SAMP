/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build$VERSION
 *  android.os.Handler
 *  android.os.Message
 *  android.view.GestureDetector
 *  android.view.GestureDetector$OnDoubleTapListener
 *  android.view.GestureDetector$OnGestureListener
 *  android.view.MotionEvent
 *  android.view.VelocityTracker
 *  android.view.ViewConfiguration
 */
package androidx.core.view;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

public final class GestureDetectorCompat {
    private final GestureDetectorCompatImpl mImpl;

    public GestureDetectorCompat(Context context, GestureDetector.OnGestureListener onGestureListener) {
        this(context, onGestureListener, null);
    }

    public GestureDetectorCompat(Context context, GestureDetector.OnGestureListener onGestureListener, Handler handler) {
        this.mImpl = Build.VERSION.SDK_INT > 17 ? new GestureDetectorCompatImplJellybeanMr2(context, onGestureListener, handler) : new GestureDetectorCompatImplBase(context, onGestureListener, handler);
    }

    public boolean isLongpressEnabled() {
        return this.mImpl.isLongpressEnabled();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.mImpl.onTouchEvent(motionEvent);
    }

    public void setIsLongpressEnabled(boolean bl) {
        this.mImpl.setIsLongpressEnabled(bl);
    }

    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener onDoubleTapListener) {
        this.mImpl.setOnDoubleTapListener(onDoubleTapListener);
    }

    static interface GestureDetectorCompatImpl {
        public boolean isLongpressEnabled();

        public boolean onTouchEvent(MotionEvent var1);

        public void setIsLongpressEnabled(boolean var1);

        public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener var1);
    }

    static class GestureDetectorCompatImplBase
    implements GestureDetectorCompatImpl {
        private static final int DOUBLE_TAP_TIMEOUT;
        private static final int LONGPRESS_TIMEOUT;
        private static final int LONG_PRESS = 2;
        private static final int SHOW_PRESS = 1;
        private static final int TAP = 3;
        private static final int TAP_TIMEOUT;
        private boolean mAlwaysInBiggerTapRegion;
        private boolean mAlwaysInTapRegion;
        MotionEvent mCurrentDownEvent;
        boolean mDeferConfirmSingleTap;
        GestureDetector.OnDoubleTapListener mDoubleTapListener;
        private int mDoubleTapSlopSquare;
        private float mDownFocusX;
        private float mDownFocusY;
        private final Handler mHandler;
        private boolean mInLongPress;
        private boolean mIsDoubleTapping;
        private boolean mIsLongpressEnabled;
        private float mLastFocusX;
        private float mLastFocusY;
        final GestureDetector.OnGestureListener mListener;
        private int mMaximumFlingVelocity;
        private int mMinimumFlingVelocity;
        private MotionEvent mPreviousUpEvent;
        boolean mStillDown;
        private int mTouchSlopSquare;
        private VelocityTracker mVelocityTracker;

        static {
            LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
            TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
            DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
        }

        GestureDetectorCompatImplBase(Context context, GestureDetector.OnGestureListener onGestureListener, Handler handler) {
            this.mHandler = handler != null ? new GestureHandler(this, handler) : new GestureHandler(this);
            this.mListener = onGestureListener;
            if (onGestureListener instanceof GestureDetector.OnDoubleTapListener) {
                this.setOnDoubleTapListener((GestureDetector.OnDoubleTapListener)onGestureListener);
            }
            this.init(context);
        }

        private void cancel() {
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
            this.mIsDoubleTapping = false;
            this.mStillDown = false;
            this.mAlwaysInTapRegion = false;
            this.mAlwaysInBiggerTapRegion = false;
            this.mDeferConfirmSingleTap = false;
            if (this.mInLongPress) {
                this.mInLongPress = false;
            }
        }

        private void cancelTaps() {
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
            this.mIsDoubleTapping = false;
            this.mAlwaysInTapRegion = false;
            this.mAlwaysInBiggerTapRegion = false;
            this.mDeferConfirmSingleTap = false;
            if (this.mInLongPress) {
                this.mInLongPress = false;
            }
        }

        private void init(Context context) {
            if (context != null) {
                if (this.mListener != null) {
                    this.mIsLongpressEnabled = true;
                    context = ViewConfiguration.get((Context)context);
                    int n = context.getScaledTouchSlop();
                    int n2 = context.getScaledDoubleTapSlop();
                    this.mMinimumFlingVelocity = context.getScaledMinimumFlingVelocity();
                    this.mMaximumFlingVelocity = context.getScaledMaximumFlingVelocity();
                    this.mTouchSlopSquare = n * n;
                    this.mDoubleTapSlopSquare = n2 * n2;
                    return;
                }
                throw new IllegalArgumentException("OnGestureListener must not be null");
            }
            throw new IllegalArgumentException("Context must not be null");
        }

        private boolean isConsideredDoubleTap(MotionEvent motionEvent, MotionEvent motionEvent2, MotionEvent motionEvent3) {
            int n;
            boolean bl = this.mAlwaysInBiggerTapRegion;
            boolean bl2 = false;
            if (!bl) {
                return false;
            }
            if (motionEvent3.getEventTime() - motionEvent2.getEventTime() > (long)DOUBLE_TAP_TIMEOUT) {
                return false;
            }
            int n2 = (int)motionEvent.getX() - (int)motionEvent3.getX();
            if (n2 * n2 + (n = (int)motionEvent.getY() - (int)motionEvent3.getY()) * n < this.mDoubleTapSlopSquare) {
                bl2 = true;
            }
            return bl2;
        }

        void dispatchLongPress() {
            this.mHandler.removeMessages(3);
            this.mDeferConfirmSingleTap = false;
            this.mInLongPress = true;
            this.mListener.onLongPress(this.mCurrentDownEvent);
        }

        @Override
        public boolean isLongpressEnabled() {
            return this.mIsLongpressEnabled;
        }

        /*
         * Unable to fully structure code
         */
        @Override
        public boolean onTouchEvent(MotionEvent var1_1) {
            var9_2 = var1_1.getAction();
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(var1_1);
            var6_3 = (var9_2 & 255) == 6 ? 1 : 0;
            var7_4 = var6_3 != 0 ? var1_1.getActionIndex() : -1;
            var3_5 = 0.0f;
            var2_6 = 0.0f;
            var10_7 = var1_1.getPointerCount();
            for (var8_8 = 0; var8_8 < var10_7; ++var8_8) {
                if (var7_4 == var8_8) continue;
                var3_5 += var1_1.getX(var8_8);
                var2_6 += var1_1.getY(var8_8);
            }
            var8_8 = var6_3 != 0 ? var10_7 - 1 : var10_7;
            var3_5 /= (float)var8_8;
            var2_6 /= (float)var8_8;
            var13_9 = 0;
            var15_10 = 0;
            var8_8 = 0;
            var12_11 = 0;
            var14_12 = 0;
            block0 : switch (var9_2 & 255) {
                default: {
                    break;
                }
                case 6: {
                    this.mLastFocusX = var3_5;
                    this.mDownFocusX = var3_5;
                    this.mLastFocusY = var2_6;
                    this.mDownFocusY = var2_6;
                    this.mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
                    var8_8 = var1_1.getActionIndex();
                    var9_2 = var1_1.getPointerId(var8_8);
                    var3_5 = this.mVelocityTracker.getXVelocity(var9_2);
                    var2_6 = this.mVelocityTracker.getYVelocity(var9_2);
                    for (var9_2 = 0; var9_2 < var10_7; ++var9_2) {
                        if (var9_2 == var8_8 || !(this.mVelocityTracker.getXVelocity(var11_13 = var1_1.getPointerId(var9_2)) * var3_5 + this.mVelocityTracker.getYVelocity(var11_13) * var2_6 < 0.0f)) continue;
                        this.mVelocityTracker.clear();
                        break block0;
                    }
                    break;
                }
                case 5: {
                    this.mLastFocusX = var3_5;
                    this.mDownFocusX = var3_5;
                    this.mLastFocusY = var2_6;
                    this.mDownFocusY = var2_6;
                    this.cancelTaps();
                    break;
                }
                case 3: {
                    this.cancel();
                    break;
                }
                case 2: {
                    if (this.mInLongPress) break;
                    var5_14 = this.mLastFocusX - var3_5;
                    var4_15 = this.mLastFocusY - var2_6;
                    if (this.mIsDoubleTapping) {
                        var12_11 = 0 | this.mDoubleTapListener.onDoubleTapEvent(var1_1);
                        break;
                    }
                    if (this.mAlwaysInTapRegion) {
                        var7_4 = (int)(var3_5 - this.mDownFocusX);
                        var6_3 = (int)(var2_6 - this.mDownFocusY);
                        var6_3 = var7_4 * var7_4 + var6_3 * var6_3;
                        var13_9 = var14_12;
                        if (var6_3 > this.mTouchSlopSquare) {
                            var13_9 = this.mListener.onScroll(this.mCurrentDownEvent, var1_1, var5_14, var4_15);
                            this.mLastFocusX = var3_5;
                            this.mLastFocusY = var2_6;
                            this.mAlwaysInTapRegion = false;
                            this.mHandler.removeMessages(3);
                            this.mHandler.removeMessages(1);
                            this.mHandler.removeMessages(2);
                        }
                        var12_11 = var13_9;
                        if (var6_3 <= this.mTouchSlopSquare) break;
                        this.mAlwaysInBiggerTapRegion = false;
                        var12_11 = var13_9;
                        break;
                    }
                    if (!(Math.abs(var5_14) >= 1.0f)) {
                        var12_11 = var13_9;
                        if (!(Math.abs(var4_15) >= 1.0f)) break;
                    }
                    var12_11 = this.mListener.onScroll(this.mCurrentDownEvent, var1_1, var5_14, var4_15);
                    this.mLastFocusX = var3_5;
                    this.mLastFocusY = var2_6;
                    break;
                }
                case 1: {
                    this.mStillDown = false;
                    var16_16 = MotionEvent.obtain((MotionEvent)var1_1);
                    if (!this.mIsDoubleTapping) ** GOTO lbl89
                    var12_11 = 0 | this.mDoubleTapListener.onDoubleTapEvent(var1_1);
                    ** GOTO lbl113
lbl89:
                    // 1 sources

                    if (!this.mInLongPress) ** GOTO lbl94
                    this.mHandler.removeMessages(3);
                    this.mInLongPress = false;
                    var12_11 = var15_10;
                    ** GOTO lbl113
lbl94:
                    // 1 sources

                    if (!this.mAlwaysInTapRegion) ** GOTO lbl104
                    var12_11 = var13_9 = this.mListener.onSingleTapUp(var1_1);
                    if (this.mDeferConfirmSingleTap) {
                        var17_18 = this.mDoubleTapListener;
                        var12_11 = var13_9;
                        if (var17_18 != null) {
                            var17_18.onSingleTapConfirmed(var1_1);
                            var12_11 = var13_9;
                        }
                    }
                    ** GOTO lbl113
lbl104:
                    // 1 sources

                    var17_19 = this.mVelocityTracker;
                    var6_3 = var1_1.getPointerId(0);
                    var17_19.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
                    var3_5 = var17_19.getYVelocity(var6_3);
                    var2_6 = var17_19.getXVelocity(var6_3);
                    if (Math.abs(var3_5) > (float)this.mMinimumFlingVelocity) ** GOTO lbl112
                    var12_11 = var15_10;
                    if (!(Math.abs(var2_6) > (float)this.mMinimumFlingVelocity)) ** GOTO lbl113
lbl112:
                    // 2 sources

                    var12_11 = this.mListener.onFling(this.mCurrentDownEvent, var1_1, var2_6, var3_5);
lbl113:
                    // 5 sources

                    var1_1 = this.mPreviousUpEvent;
                    if (var1_1 != null) {
                        var1_1.recycle();
                    }
                    this.mPreviousUpEvent = var16_16;
                    var1_1 = this.mVelocityTracker;
                    if (var1_1 != null) {
                        var1_1.recycle();
                        this.mVelocityTracker = null;
                    }
                    this.mIsDoubleTapping = false;
                    this.mDeferConfirmSingleTap = false;
                    this.mHandler.removeMessages(1);
                    this.mHandler.removeMessages(2);
                    break;
                }
                case 0: {
                    var6_3 = var8_8;
                    if (this.mDoubleTapListener != null) {
                        var12_11 = this.mHandler.hasMessages(3);
                        if (var12_11 != 0) {
                            this.mHandler.removeMessages(3);
                        }
                        if ((var17_20 = this.mCurrentDownEvent) != null && (var16_17 = this.mPreviousUpEvent) != null && var12_11 != 0 && this.isConsideredDoubleTap(var17_20, var16_17, var1_1)) {
                            this.mIsDoubleTapping = true;
                            var6_3 = this.mDoubleTapListener.onDoubleTap(this.mCurrentDownEvent) | false | this.mDoubleTapListener.onDoubleTapEvent(var1_1);
                        } else {
                            this.mHandler.sendEmptyMessageDelayed(3, (long)GestureDetectorCompatImplBase.DOUBLE_TAP_TIMEOUT);
                            var6_3 = var8_8;
                        }
                    }
                    this.mLastFocusX = var3_5;
                    this.mDownFocusX = var3_5;
                    this.mLastFocusY = var2_6;
                    this.mDownFocusY = var2_6;
                    var16_17 = this.mCurrentDownEvent;
                    if (var16_17 != null) {
                        var16_17.recycle();
                    }
                    this.mCurrentDownEvent = MotionEvent.obtain((MotionEvent)var1_1);
                    this.mAlwaysInTapRegion = true;
                    this.mAlwaysInBiggerTapRegion = true;
                    this.mStillDown = true;
                    this.mInLongPress = false;
                    this.mDeferConfirmSingleTap = false;
                    if (this.mIsLongpressEnabled) {
                        this.mHandler.removeMessages(2);
                        this.mHandler.sendEmptyMessageAtTime(2, this.mCurrentDownEvent.getDownTime() + (long)GestureDetectorCompatImplBase.TAP_TIMEOUT + (long)GestureDetectorCompatImplBase.LONGPRESS_TIMEOUT);
                    }
                    this.mHandler.sendEmptyMessageAtTime(1, this.mCurrentDownEvent.getDownTime() + (long)GestureDetectorCompatImplBase.TAP_TIMEOUT);
                    var12_11 = var6_3 | this.mListener.onDown(var1_1);
                }
            }
            return (boolean)var12_11;
        }

        @Override
        public void setIsLongpressEnabled(boolean bl) {
            this.mIsLongpressEnabled = bl;
        }

        @Override
        public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener onDoubleTapListener) {
            this.mDoubleTapListener = onDoubleTapListener;
        }

        private class GestureHandler
        extends Handler {
            final GestureDetectorCompatImplBase this$0;

            GestureHandler(GestureDetectorCompatImplBase gestureDetectorCompatImplBase) {
                this.this$0 = gestureDetectorCompatImplBase;
            }

            GestureHandler(GestureDetectorCompatImplBase gestureDetectorCompatImplBase, Handler handler) {
                this.this$0 = gestureDetectorCompatImplBase;
                super(handler.getLooper());
            }

            public void handleMessage(Message message) {
                switch (message.what) {
                    default: {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unknown message ");
                        stringBuilder.append(message);
                        throw new RuntimeException(stringBuilder.toString());
                    }
                    case 3: {
                        if (this.this$0.mDoubleTapListener == null) break;
                        if (!this.this$0.mStillDown) {
                            this.this$0.mDoubleTapListener.onSingleTapConfirmed(this.this$0.mCurrentDownEvent);
                            break;
                        }
                        this.this$0.mDeferConfirmSingleTap = true;
                        break;
                    }
                    case 2: {
                        this.this$0.dispatchLongPress();
                        break;
                    }
                    case 1: {
                        this.this$0.mListener.onShowPress(this.this$0.mCurrentDownEvent);
                    }
                }
            }
        }
    }

    static class GestureDetectorCompatImplJellybeanMr2
    implements GestureDetectorCompatImpl {
        private final GestureDetector mDetector;

        GestureDetectorCompatImplJellybeanMr2(Context context, GestureDetector.OnGestureListener onGestureListener, Handler handler) {
            this.mDetector = new GestureDetector(context, onGestureListener, handler);
        }

        @Override
        public boolean isLongpressEnabled() {
            return this.mDetector.isLongpressEnabled();
        }

        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return this.mDetector.onTouchEvent(motionEvent);
        }

        @Override
        public void setIsLongpressEnabled(boolean bl) {
            this.mDetector.setIsLongpressEnabled(bl);
        }

        @Override
        public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener onDoubleTapListener) {
            this.mDetector.setOnDoubleTapListener(onDoubleTapListener);
        }
    }
}

