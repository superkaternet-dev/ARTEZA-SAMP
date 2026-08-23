/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.Resources
 *  android.os.SystemClock
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnTouchListener
 *  android.view.ViewConfiguration
 *  android.view.animation.AccelerateInterpolator
 *  android.view.animation.AnimationUtils
 *  android.view.animation.Interpolator
 */
package androidx.core.widget;

import android.content.res.Resources;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import androidx.core.view.ViewCompat;

public abstract class AutoScrollHelper
implements View.OnTouchListener {
    private static final int DEFAULT_ACTIVATION_DELAY = ViewConfiguration.getTapTimeout();
    private static final int DEFAULT_EDGE_TYPE = 1;
    private static final float DEFAULT_MAXIMUM_EDGE = Float.MAX_VALUE;
    private static final int DEFAULT_MAXIMUM_VELOCITY_DIPS = 1575;
    private static final int DEFAULT_MINIMUM_VELOCITY_DIPS = 315;
    private static final int DEFAULT_RAMP_DOWN_DURATION = 500;
    private static final int DEFAULT_RAMP_UP_DURATION = 500;
    private static final float DEFAULT_RELATIVE_EDGE = 0.2f;
    private static final float DEFAULT_RELATIVE_VELOCITY = 1.0f;
    public static final int EDGE_TYPE_INSIDE = 0;
    public static final int EDGE_TYPE_INSIDE_EXTEND = 1;
    public static final int EDGE_TYPE_OUTSIDE = 2;
    private static final int HORIZONTAL = 0;
    public static final float NO_MAX = Float.MAX_VALUE;
    public static final float NO_MIN = 0.0f;
    public static final float RELATIVE_UNSPECIFIED = 0.0f;
    private static final int VERTICAL = 1;
    private int mActivationDelay;
    private boolean mAlreadyDelayed;
    boolean mAnimating;
    private final Interpolator mEdgeInterpolator;
    private int mEdgeType;
    private boolean mEnabled;
    private boolean mExclusive;
    private float[] mMaximumEdges;
    private float[] mMaximumVelocity;
    private float[] mMinimumVelocity;
    boolean mNeedsCancel;
    boolean mNeedsReset;
    private float[] mRelativeEdges;
    private float[] mRelativeVelocity;
    private Runnable mRunnable;
    final ClampedScroller mScroller = new ClampedScroller();
    final View mTarget;

    public AutoScrollHelper(View view) {
        this.mEdgeInterpolator = new AccelerateInterpolator();
        this.mRelativeEdges = new float[]{0.0f, 0.0f};
        this.mMaximumEdges = new float[]{Float.MAX_VALUE, Float.MAX_VALUE};
        this.mRelativeVelocity = new float[]{0.0f, 0.0f};
        this.mMinimumVelocity = new float[]{0.0f, 0.0f};
        this.mMaximumVelocity = new float[]{Float.MAX_VALUE, Float.MAX_VALUE};
        this.mTarget = view;
        view = Resources.getSystem().getDisplayMetrics();
        int n = (int)(view.density * 1575.0f + 0.5f);
        int n2 = (int)(view.density * 315.0f + 0.5f);
        this.setMaximumVelocity(n, n);
        this.setMinimumVelocity(n2, n2);
        this.setEdgeType(1);
        this.setMaximumEdges(Float.MAX_VALUE, Float.MAX_VALUE);
        this.setRelativeEdges(0.2f, 0.2f);
        this.setRelativeVelocity(1.0f, 1.0f);
        this.setActivationDelay(DEFAULT_ACTIVATION_DELAY);
        this.setRampUpDuration(500);
        this.setRampDownDuration(500);
    }

    private float computeTargetVelocity(int n, float f, float f2, float f3) {
        if ((f2 = this.getEdgeValue(this.mRelativeEdges[n], f2, this.mMaximumEdges[n], f)) == 0.0f) {
            return 0.0f;
        }
        float f4 = this.mRelativeVelocity[n];
        float f5 = this.mMinimumVelocity[n];
        f = this.mMaximumVelocity[n];
        f3 = f4 * f3;
        if (f2 > 0.0f) {
            return AutoScrollHelper.constrain(f2 * f3, f5, f);
        }
        return -AutoScrollHelper.constrain(-f2 * f3, f5, f);
    }

    static float constrain(float f, float f2, float f3) {
        if (f > f3) {
            return f3;
        }
        if (f < f2) {
            return f2;
        }
        return f;
    }

    static int constrain(int n, int n2, int n3) {
        if (n > n3) {
            return n3;
        }
        if (n < n2) {
            return n2;
        }
        return n;
    }

    private float constrainEdgeValue(float f, float f2) {
        if (f2 == 0.0f) {
            return 0.0f;
        }
        int n = this.mEdgeType;
        switch (n) {
            default: {
                break;
            }
            case 2: {
                if (!(f < 0.0f)) break;
                return f / -f2;
            }
            case 0: 
            case 1: {
                if (!(f < f2)) break;
                if (f >= 0.0f) {
                    return 1.0f - f / f2;
                }
                if (!this.mAnimating || n != 1) break;
                return 1.0f;
            }
        }
        return 0.0f;
    }

    private float getEdgeValue(float f, float f2, float f3, float f4) {
        block4: {
            block3: {
                block2: {
                    f3 = AutoScrollHelper.constrain(f * f2, 0.0f, f3);
                    f = this.constrainEdgeValue(f4, f3);
                    f = this.constrainEdgeValue(f2 - f4, f3) - f;
                    if (!(f < 0.0f)) break block2;
                    f = -this.mEdgeInterpolator.getInterpolation(-f);
                    break block3;
                }
                if (!(f > 0.0f)) break block4;
                f = this.mEdgeInterpolator.getInterpolation(f);
            }
            return AutoScrollHelper.constrain(f, -1.0f, 1.0f);
        }
        return 0.0f;
    }

    private void requestStop() {
        if (this.mNeedsReset) {
            this.mAnimating = false;
        } else {
            this.mScroller.requestStop();
        }
    }

    private void startAnimating() {
        int n;
        if (this.mRunnable == null) {
            this.mRunnable = new ScrollAnimationRunnable(this);
        }
        this.mAnimating = true;
        this.mNeedsReset = true;
        if (!this.mAlreadyDelayed && (n = this.mActivationDelay) > 0) {
            ViewCompat.postOnAnimationDelayed(this.mTarget, this.mRunnable, n);
        } else {
            this.mRunnable.run();
        }
        this.mAlreadyDelayed = true;
    }

    public abstract boolean canTargetScrollHorizontally(int var1);

    public abstract boolean canTargetScrollVertically(int var1);

    void cancelTargetTouch() {
        long l = SystemClock.uptimeMillis();
        MotionEvent motionEvent = MotionEvent.obtain((long)l, (long)l, (int)3, (float)0.0f, (float)0.0f, (int)0);
        this.mTarget.onTouchEvent(motionEvent);
        motionEvent.recycle();
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public boolean isExclusive() {
        return this.mExclusive;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean bl = this.mEnabled;
        boolean bl2 = false;
        if (!bl) {
            return false;
        }
        switch (motionEvent.getActionMasked()) {
            default: {
                break;
            }
            case 1: 
            case 3: {
                this.requestStop();
                break;
            }
            case 0: {
                this.mNeedsCancel = true;
                this.mAlreadyDelayed = false;
            }
            case 2: {
                float f = this.computeTargetVelocity(0, motionEvent.getX(), view.getWidth(), this.mTarget.getWidth());
                float f2 = this.computeTargetVelocity(1, motionEvent.getY(), view.getHeight(), this.mTarget.getHeight());
                this.mScroller.setTargetVelocity(f, f2);
                if (this.mAnimating || !this.shouldAnimate()) break;
                this.startAnimating();
            }
        }
        bl = bl2;
        if (this.mExclusive) {
            bl = bl2;
            if (this.mAnimating) {
                bl = true;
            }
        }
        return bl;
    }

    public abstract void scrollTargetBy(int var1, int var2);

    public AutoScrollHelper setActivationDelay(int n) {
        this.mActivationDelay = n;
        return this;
    }

    public AutoScrollHelper setEdgeType(int n) {
        this.mEdgeType = n;
        return this;
    }

    public AutoScrollHelper setEnabled(boolean bl) {
        if (this.mEnabled && !bl) {
            this.requestStop();
        }
        this.mEnabled = bl;
        return this;
    }

    public AutoScrollHelper setExclusive(boolean bl) {
        this.mExclusive = bl;
        return this;
    }

    public AutoScrollHelper setMaximumEdges(float f, float f2) {
        float[] fArray = this.mMaximumEdges;
        fArray[0] = f;
        fArray[1] = f2;
        return this;
    }

    public AutoScrollHelper setMaximumVelocity(float f, float f2) {
        float[] fArray = this.mMaximumVelocity;
        fArray[0] = f / 1000.0f;
        fArray[1] = f2 / 1000.0f;
        return this;
    }

    public AutoScrollHelper setMinimumVelocity(float f, float f2) {
        float[] fArray = this.mMinimumVelocity;
        fArray[0] = f / 1000.0f;
        fArray[1] = f2 / 1000.0f;
        return this;
    }

    public AutoScrollHelper setRampDownDuration(int n) {
        this.mScroller.setRampDownDuration(n);
        return this;
    }

    public AutoScrollHelper setRampUpDuration(int n) {
        this.mScroller.setRampUpDuration(n);
        return this;
    }

    public AutoScrollHelper setRelativeEdges(float f, float f2) {
        float[] fArray = this.mRelativeEdges;
        fArray[0] = f;
        fArray[1] = f2;
        return this;
    }

    public AutoScrollHelper setRelativeVelocity(float f, float f2) {
        float[] fArray = this.mRelativeVelocity;
        fArray[0] = f / 1000.0f;
        fArray[1] = f2 / 1000.0f;
        return this;
    }

    boolean shouldAnimate() {
        ClampedScroller clampedScroller = this.mScroller;
        int n = clampedScroller.getVerticalDirection();
        int n2 = clampedScroller.getHorizontalDirection();
        boolean bl = n != 0 && this.canTargetScrollVertically(n) || n2 != 0 && this.canTargetScrollHorizontally(n2);
        return bl;
    }

    private static class ClampedScroller {
        private long mDeltaTime = 0L;
        private int mDeltaX = 0;
        private int mDeltaY = 0;
        private int mEffectiveRampDown;
        private int mRampDownDuration;
        private int mRampUpDuration;
        private long mStartTime = Long.MIN_VALUE;
        private long mStopTime = -1L;
        private float mStopValue;
        private float mTargetVelocityX;
        private float mTargetVelocityY;

        ClampedScroller() {
        }

        private float getValueAt(long l) {
            long l2 = this.mStartTime;
            if (l < l2) {
                return 0.0f;
            }
            long l3 = this.mStopTime;
            if (l3 >= 0L && l >= l3) {
                float f = this.mStopValue;
                return 1.0f - f + f * AutoScrollHelper.constrain((float)(l - l3) / (float)this.mEffectiveRampDown, 0.0f, 1.0f);
            }
            return AutoScrollHelper.constrain((float)(l - l2) / (float)this.mRampUpDuration, 0.0f, 1.0f) * 0.5f;
        }

        private float interpolateValue(float f) {
            return -4.0f * f * f + 4.0f * f;
        }

        public void computeScrollDelta() {
            if (this.mDeltaTime != 0L) {
                long l = AnimationUtils.currentAnimationTimeMillis();
                float f = this.interpolateValue(this.getValueAt(l));
                long l2 = l - this.mDeltaTime;
                this.mDeltaTime = l;
                this.mDeltaX = (int)((float)l2 * f * this.mTargetVelocityX);
                this.mDeltaY = (int)((float)l2 * f * this.mTargetVelocityY);
                return;
            }
            throw new RuntimeException("Cannot compute scroll delta before calling start()");
        }

        public int getDeltaX() {
            return this.mDeltaX;
        }

        public int getDeltaY() {
            return this.mDeltaY;
        }

        public int getHorizontalDirection() {
            float f = this.mTargetVelocityX;
            return (int)(f / Math.abs(f));
        }

        public int getVerticalDirection() {
            float f = this.mTargetVelocityY;
            return (int)(f / Math.abs(f));
        }

        public boolean isFinished() {
            boolean bl = this.mStopTime > 0L && AnimationUtils.currentAnimationTimeMillis() > this.mStopTime + (long)this.mEffectiveRampDown;
            return bl;
        }

        public void requestStop() {
            long l = AnimationUtils.currentAnimationTimeMillis();
            this.mEffectiveRampDown = AutoScrollHelper.constrain((int)(l - this.mStartTime), 0, this.mRampDownDuration);
            this.mStopValue = this.getValueAt(l);
            this.mStopTime = l;
        }

        public void setRampDownDuration(int n) {
            this.mRampDownDuration = n;
        }

        public void setRampUpDuration(int n) {
            this.mRampUpDuration = n;
        }

        public void setTargetVelocity(float f, float f2) {
            this.mTargetVelocityX = f;
            this.mTargetVelocityY = f2;
        }

        public void start() {
            long l;
            this.mStartTime = l = AnimationUtils.currentAnimationTimeMillis();
            this.mStopTime = -1L;
            this.mDeltaTime = l;
            this.mStopValue = 0.5f;
            this.mDeltaX = 0;
            this.mDeltaY = 0;
        }
    }

    private class ScrollAnimationRunnable
    implements Runnable {
        final AutoScrollHelper this$0;

        ScrollAnimationRunnable(AutoScrollHelper autoScrollHelper) {
            this.this$0 = autoScrollHelper;
        }

        @Override
        public void run() {
            ClampedScroller clampedScroller;
            if (!this.this$0.mAnimating) {
                return;
            }
            if (this.this$0.mNeedsReset) {
                this.this$0.mNeedsReset = false;
                this.this$0.mScroller.start();
            }
            if (!(clampedScroller = this.this$0.mScroller).isFinished() && this.this$0.shouldAnimate()) {
                if (this.this$0.mNeedsCancel) {
                    this.this$0.mNeedsCancel = false;
                    this.this$0.cancelTargetTouch();
                }
                clampedScroller.computeScrollDelta();
                int n = clampedScroller.getDeltaX();
                int n2 = clampedScroller.getDeltaY();
                this.this$0.scrollTargetBy(n, n2);
                ViewCompat.postOnAnimation(this.this$0.mTarget, this);
                return;
            }
            this.this$0.mAnimating = false;
        }
    }
}

