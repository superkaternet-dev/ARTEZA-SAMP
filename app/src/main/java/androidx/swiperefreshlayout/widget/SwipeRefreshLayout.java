/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.util.Log
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewConfiguration
 *  android.view.ViewGroup
 *  android.view.animation.Animation
 *  android.view.animation.Animation$AnimationListener
 *  android.view.animation.DecelerateInterpolator
 *  android.view.animation.Interpolator
 *  android.view.animation.Transformation
 *  android.widget.AbsListView
 *  android.widget.ListView
 */
package androidx.swiperefreshlayout.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.ListView;
import androidx.core.content.ContextCompat;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.core.widget.ListViewCompat;
import androidx.swiperefreshlayout.widget.CircleImageView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

public class SwipeRefreshLayout
extends ViewGroup
implements NestedScrollingParent,
NestedScrollingChild {
    private static final int ALPHA_ANIMATION_DURATION = 300;
    private static final int ANIMATE_TO_START_DURATION = 200;
    private static final int ANIMATE_TO_TRIGGER_DURATION = 200;
    private static final int CIRCLE_BG_LIGHT = -328966;
    static final int CIRCLE_DIAMETER = 40;
    static final int CIRCLE_DIAMETER_LARGE = 56;
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2.0f;
    public static final int DEFAULT = 1;
    private static final int DEFAULT_CIRCLE_TARGET = 64;
    public static final int DEFAULT_SLINGSHOT_DISTANCE = -1;
    private static final float DRAG_RATE = 0.5f;
    private static final int INVALID_POINTER = -1;
    public static final int LARGE = 0;
    private static final int[] LAYOUT_ATTRS;
    private static final String LOG_TAG;
    private static final int MAX_ALPHA = 255;
    private static final float MAX_PROGRESS_ANGLE = 0.8f;
    private static final int SCALE_DOWN_DURATION = 150;
    private static final int STARTING_PROGRESS_ALPHA = 76;
    private int mActivePointerId = -1;
    private Animation mAlphaMaxAnimation;
    private Animation mAlphaStartAnimation;
    private final Animation mAnimateToCorrectPosition;
    private final Animation mAnimateToStartPosition;
    private OnChildScrollUpCallback mChildScrollUpCallback;
    private int mCircleDiameter;
    CircleImageView mCircleView;
    private int mCircleViewIndex = -1;
    int mCurrentTargetOffsetTop;
    int mCustomSlingshotDistance;
    private final DecelerateInterpolator mDecelerateInterpolator;
    protected int mFrom;
    private float mInitialDownY;
    private float mInitialMotionY;
    private boolean mIsBeingDragged;
    OnRefreshListener mListener;
    private int mMediumAnimationDuration;
    private boolean mNestedScrollInProgress;
    private final NestedScrollingChildHelper mNestedScrollingChildHelper;
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    boolean mNotify;
    protected int mOriginalOffsetTop;
    private final int[] mParentOffsetInWindow;
    private final int[] mParentScrollConsumed = new int[2];
    CircularProgressDrawable mProgress;
    private Animation.AnimationListener mRefreshListener;
    boolean mRefreshing = false;
    private boolean mReturningToStart;
    boolean mScale;
    private Animation mScaleAnimation;
    private Animation mScaleDownAnimation;
    private Animation mScaleDownToStartAnimation;
    int mSpinnerOffsetEnd;
    float mStartingScale;
    private View mTarget;
    private float mTotalDragDistance = -1.0f;
    private float mTotalUnconsumed;
    private int mTouchSlop;
    boolean mUsingCustomStart;

    static {
        LOG_TAG = SwipeRefreshLayout.class.getSimpleName();
        LAYOUT_ATTRS = new int[]{0x101000E};
    }

    public SwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public SwipeRefreshLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        int n;
        this.mParentOffsetInWindow = new int[2];
        this.mRefreshListener = new Animation.AnimationListener(this){
            final SwipeRefreshLayout this$0;
            {
                this.this$0 = swipeRefreshLayout;
            }

            public void onAnimationEnd(Animation object) {
                if (this.this$0.mRefreshing) {
                    this.this$0.mProgress.setAlpha(255);
                    this.this$0.mProgress.start();
                    if (this.this$0.mNotify && this.this$0.mListener != null) {
                        this.this$0.mListener.onRefresh();
                    }
                    object = this.this$0;
                    object.mCurrentTargetOffsetTop = object.mCircleView.getTop();
                } else {
                    this.this$0.reset();
                }
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        };
        this.mAnimateToCorrectPosition = new Animation(this){
            final SwipeRefreshLayout this$0;
            {
                this.this$0 = swipeRefreshLayout;
            }

            public void applyTransformation(float f, Transformation transformation) {
                int n = !this.this$0.mUsingCustomStart ? this.this$0.mSpinnerOffsetEnd - Math.abs(this.this$0.mOriginalOffsetTop) : this.this$0.mSpinnerOffsetEnd;
                int n2 = this.this$0.mFrom;
                int n3 = (int)((float)(n - this.this$0.mFrom) * f);
                n = this.this$0.mCircleView.getTop();
                this.this$0.setTargetOffsetTopAndBottom(n2 + n3 - n);
                this.this$0.mProgress.setArrowScale(1.0f - f);
            }
        };
        this.mAnimateToStartPosition = new Animation(this){
            final SwipeRefreshLayout this$0;
            {
                this.this$0 = swipeRefreshLayout;
            }

            public void applyTransformation(float f, Transformation transformation) {
                this.this$0.moveToStart(f);
            }
        };
        this.mTouchSlop = ViewConfiguration.get((Context)context).getScaledTouchSlop();
        this.mMediumAnimationDuration = this.getResources().getInteger(0x10E0001);
        this.setWillNotDraw(false);
        this.mDecelerateInterpolator = new DecelerateInterpolator(2.0f);
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        this.mCircleDiameter = (int)(displayMetrics.density * 40.0f);
        this.createProgressView();
        this.setChildrenDrawingOrderEnabled(true);
        this.mSpinnerOffsetEnd = n = (int)(displayMetrics.density * 64.0f);
        this.mTotalDragDistance = n;
        this.mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        this.mNestedScrollingChildHelper = new NestedScrollingChildHelper((View)this);
        this.setNestedScrollingEnabled(true);
        this.mCurrentTargetOffsetTop = n = -this.mCircleDiameter;
        this.mOriginalOffsetTop = n;
        this.moveToStart(1.0f);
        context = context.obtainStyledAttributes(attributeSet, LAYOUT_ATTRS);
        this.setEnabled(context.getBoolean(0, true));
        context.recycle();
    }

    private void animateOffsetToCorrectPosition(int n, Animation.AnimationListener animationListener) {
        this.mFrom = n;
        this.mAnimateToCorrectPosition.reset();
        this.mAnimateToCorrectPosition.setDuration(200L);
        this.mAnimateToCorrectPosition.setInterpolator((Interpolator)this.mDecelerateInterpolator);
        if (animationListener != null) {
            this.mCircleView.setAnimationListener(animationListener);
        }
        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(this.mAnimateToCorrectPosition);
    }

    private void animateOffsetToStartPosition(int n, Animation.AnimationListener animationListener) {
        if (this.mScale) {
            this.startScaleDownReturnToStartAnimation(n, animationListener);
        } else {
            this.mFrom = n;
            this.mAnimateToStartPosition.reset();
            this.mAnimateToStartPosition.setDuration(200L);
            this.mAnimateToStartPosition.setInterpolator((Interpolator)this.mDecelerateInterpolator);
            if (animationListener != null) {
                this.mCircleView.setAnimationListener(animationListener);
            }
            this.mCircleView.clearAnimation();
            this.mCircleView.startAnimation(this.mAnimateToStartPosition);
        }
    }

    private void createProgressView() {
        CircularProgressDrawable circularProgressDrawable;
        this.mCircleView = new CircleImageView(this.getContext(), -328966);
        this.mProgress = circularProgressDrawable = new CircularProgressDrawable(this.getContext());
        circularProgressDrawable.setStyle(1);
        this.mCircleView.setImageDrawable(this.mProgress);
        this.mCircleView.setVisibility(8);
        this.addView((View)this.mCircleView);
    }

    private void ensureTarget() {
        if (this.mTarget == null) {
            for (int i = 0; i < this.getChildCount(); ++i) {
                View view = this.getChildAt(i);
                if (view.equals((Object)this.mCircleView)) continue;
                this.mTarget = view;
                break;
            }
        }
    }

    private void finishSpinner(float f) {
        if (f > this.mTotalDragDistance) {
            this.setRefreshing(true, true);
        } else {
            this.mRefreshing = false;
            this.mProgress.setStartEndTrim(0.0f, 0.0f);
            Animation.AnimationListener animationListener = null;
            if (!this.mScale) {
                animationListener = new Animation.AnimationListener(this){
                    final SwipeRefreshLayout this$0;
                    {
                        this.this$0 = swipeRefreshLayout;
                    }

                    public void onAnimationEnd(Animation animation) {
                        if (!this.this$0.mScale) {
                            this.this$0.startScaleDownAnimation(null);
                        }
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                    }
                };
            }
            this.animateOffsetToStartPosition(this.mCurrentTargetOffsetTop, animationListener);
            this.mProgress.setArrowEnabled(false);
        }
    }

    private boolean isAnimationRunning(Animation animation) {
        boolean bl = animation != null && animation.hasStarted() && !animation.hasEnded();
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void moveSpinner(float f) {
        this.mProgress.setArrowEnabled(true);
        float f2 = Math.min(1.0f, Math.abs(f / this.mTotalDragDistance));
        double d = f2;
        Double.isNaN(d);
        float f3 = (float)Math.max(d - 0.4, 0.0) * 5.0f / 3.0f;
        float f4 = Math.abs(f);
        float f5 = this.mTotalDragDistance;
        int n = this.mCustomSlingshotDistance;
        if (n <= 0) {
            n = this.mUsingCustomStart ? this.mSpinnerOffsetEnd - this.mOriginalOffsetTop : this.mSpinnerOffsetEnd;
        }
        float f6 = n;
        f4 = Math.max(0.0f, Math.min(f4 - f5, f6 * 2.0f) / f6);
        double d2 = f4 / 4.0f;
        d = Math.pow(f4 / 4.0f, 2.0);
        Double.isNaN(d2);
        f4 = (float)(d2 - d) * 2.0f;
        n = this.mOriginalOffsetTop;
        int n2 = (int)(f6 * f2 + f6 * f4 * 2.0f);
        if (this.mCircleView.getVisibility() != 0) {
            this.mCircleView.setVisibility(0);
        }
        if (!this.mScale) {
            this.mCircleView.setScaleX(1.0f);
            this.mCircleView.setScaleY(1.0f);
        }
        if (this.mScale) {
            this.setAnimationProgress(Math.min(1.0f, f / this.mTotalDragDistance));
        }
        if (f < this.mTotalDragDistance) {
            if (this.mProgress.getAlpha() > 76 && !this.isAnimationRunning(this.mAlphaStartAnimation)) {
                this.startProgressAlphaStartAnimation();
            }
        } else if (this.mProgress.getAlpha() < 255 && !this.isAnimationRunning(this.mAlphaMaxAnimation)) {
            this.startProgressAlphaMaxAnimation();
        }
        this.mProgress.setStartEndTrim(0.0f, Math.min(0.8f, f3 * 0.8f));
        this.mProgress.setArrowScale(Math.min(1.0f, f3));
        this.mProgress.setProgressRotation((0.4f * f3 - 0.25f + 2.0f * f4) * 0.5f);
        this.setTargetOffsetTopAndBottom(n + n2 - this.mCurrentTargetOffsetTop);
    }

    private void onSecondaryPointerUp(MotionEvent motionEvent) {
        int n = motionEvent.getActionIndex();
        if (motionEvent.getPointerId(n) == this.mActivePointerId) {
            n = n == 0 ? 1 : 0;
            this.mActivePointerId = motionEvent.getPointerId(n);
        }
    }

    private void setColorViewAlpha(int n) {
        this.mCircleView.getBackground().setAlpha(n);
        this.mProgress.setAlpha(n);
    }

    private void setRefreshing(boolean bl, boolean bl2) {
        if (this.mRefreshing != bl) {
            this.mNotify = bl2;
            this.ensureTarget();
            this.mRefreshing = bl;
            if (bl) {
                this.animateOffsetToCorrectPosition(this.mCurrentTargetOffsetTop, this.mRefreshListener);
            } else {
                this.startScaleDownAnimation(this.mRefreshListener);
            }
        }
    }

    private Animation startAlphaAnimation(int n, int n2) {
        Animation animation = new Animation(this, n, n2){
            final SwipeRefreshLayout this$0;
            final int val$endingAlpha;
            final int val$startingAlpha;
            {
                this.this$0 = swipeRefreshLayout;
                this.val$startingAlpha = n;
                this.val$endingAlpha = n2;
            }

            public void applyTransformation(float f, Transformation object) {
                object = this.this$0.mProgress;
                int n = this.val$startingAlpha;
                ((CircularProgressDrawable)((Object)object)).setAlpha((int)((float)n + (float)(this.val$endingAlpha - n) * f));
            }
        };
        animation.setDuration(300L);
        this.mCircleView.setAnimationListener(null);
        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(animation);
        return animation;
    }

    private void startDragging(float f) {
        float f2 = this.mInitialDownY;
        int n = this.mTouchSlop;
        if (f - f2 > (float)n && !this.mIsBeingDragged) {
            this.mInitialMotionY = f2 + (float)n;
            this.mIsBeingDragged = true;
            this.mProgress.setAlpha(76);
        }
    }

    private void startProgressAlphaMaxAnimation() {
        this.mAlphaMaxAnimation = this.startAlphaAnimation(this.mProgress.getAlpha(), 255);
    }

    private void startProgressAlphaStartAnimation() {
        this.mAlphaStartAnimation = this.startAlphaAnimation(this.mProgress.getAlpha(), 76);
    }

    private void startScaleDownReturnToStartAnimation(int n, Animation.AnimationListener animationListener) {
        Animation animation;
        this.mFrom = n;
        this.mStartingScale = this.mCircleView.getScaleX();
        this.mScaleDownToStartAnimation = animation = new Animation(this){
            final SwipeRefreshLayout this$0;
            {
                this.this$0 = swipeRefreshLayout;
            }

            public void applyTransformation(float f, Transformation transformation) {
                float f2 = this.this$0.mStartingScale;
                float f3 = -this.this$0.mStartingScale;
                this.this$0.setAnimationProgress(f2 + f3 * f);
                this.this$0.moveToStart(f);
            }
        };
        animation.setDuration(150L);
        if (animationListener != null) {
            this.mCircleView.setAnimationListener(animationListener);
        }
        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(this.mScaleDownToStartAnimation);
    }

    private void startScaleUpAnimation(Animation.AnimationListener animationListener) {
        Animation animation;
        this.mCircleView.setVisibility(0);
        this.mProgress.setAlpha(255);
        this.mScaleAnimation = animation = new Animation(this){
            final SwipeRefreshLayout this$0;
            {
                this.this$0 = swipeRefreshLayout;
            }

            public void applyTransformation(float f, Transformation transformation) {
                this.this$0.setAnimationProgress(f);
            }
        };
        animation.setDuration((long)this.mMediumAnimationDuration);
        if (animationListener != null) {
            this.mCircleView.setAnimationListener(animationListener);
        }
        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(this.mScaleAnimation);
    }

    public boolean canChildScrollUp() {
        OnChildScrollUpCallback onChildScrollUpCallback = this.mChildScrollUpCallback;
        if (onChildScrollUpCallback != null) {
            return onChildScrollUpCallback.canChildScrollUp(this, this.mTarget);
        }
        onChildScrollUpCallback = this.mTarget;
        if (onChildScrollUpCallback instanceof ListView) {
            return ListViewCompat.canScrollList((ListView)onChildScrollUpCallback, -1);
        }
        return onChildScrollUpCallback.canScrollVertically(-1);
    }

    @Override
    public boolean dispatchNestedFling(float f, float f2, boolean bl) {
        return this.mNestedScrollingChildHelper.dispatchNestedFling(f, f2, bl);
    }

    @Override
    public boolean dispatchNestedPreFling(float f, float f2) {
        return this.mNestedScrollingChildHelper.dispatchNestedPreFling(f, f2);
    }

    @Override
    public boolean dispatchNestedPreScroll(int n, int n2, int[] nArray, int[] nArray2) {
        return this.mNestedScrollingChildHelper.dispatchNestedPreScroll(n, n2, nArray, nArray2);
    }

    @Override
    public boolean dispatchNestedScroll(int n, int n2, int n3, int n4, int[] nArray) {
        return this.mNestedScrollingChildHelper.dispatchNestedScroll(n, n2, n3, n4, nArray);
    }

    protected int getChildDrawingOrder(int n, int n2) {
        int n3 = this.mCircleViewIndex;
        if (n3 < 0) {
            return n2;
        }
        if (n2 == n - 1) {
            return n3;
        }
        if (n2 >= n3) {
            return n2 + 1;
        }
        return n2;
    }

    @Override
    public int getNestedScrollAxes() {
        return this.mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    public int getProgressCircleDiameter() {
        return this.mCircleDiameter;
    }

    public int getProgressViewEndOffset() {
        return this.mSpinnerOffsetEnd;
    }

    public int getProgressViewStartOffset() {
        return this.mOriginalOffsetTop;
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return this.mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return this.mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    public boolean isRefreshing() {
        return this.mRefreshing;
    }

    void moveToStart(float f) {
        int n = this.mFrom;
        this.setTargetOffsetTopAndBottom(n + (int)((float)(this.mOriginalOffsetTop - n) * f) - this.mCircleView.getTop());
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.reset();
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        this.ensureTarget();
        int n = motionEvent.getActionMasked();
        if (this.mReturningToStart && n == 0) {
            this.mReturningToStart = false;
        }
        if (!(!this.isEnabled() || this.mReturningToStart || this.canChildScrollUp() || this.mRefreshing || this.mNestedScrollInProgress)) {
            switch (n) {
                default: {
                    break;
                }
                case 6: {
                    this.onSecondaryPointerUp(motionEvent);
                    break;
                }
                case 2: {
                    n = this.mActivePointerId;
                    if (n == -1) {
                        Log.e((String)LOG_TAG, (String)"Got ACTION_MOVE event but don't have an active pointer id.");
                        return false;
                    }
                    if ((n = motionEvent.findPointerIndex(n)) < 0) {
                        return false;
                    }
                    this.startDragging(motionEvent.getY(n));
                    break;
                }
                case 1: 
                case 3: {
                    this.mIsBeingDragged = false;
                    this.mActivePointerId = -1;
                    break;
                }
                case 0: {
                    this.setTargetOffsetTopAndBottom(this.mOriginalOffsetTop - this.mCircleView.getTop());
                    this.mActivePointerId = n = motionEvent.getPointerId(0);
                    this.mIsBeingDragged = false;
                    n = motionEvent.findPointerIndex(n);
                    if (n < 0) {
                        return false;
                    }
                    this.mInitialDownY = motionEvent.getY(n);
                }
            }
            return this.mIsBeingDragged;
        }
        return false;
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        n = this.getMeasuredWidth();
        n2 = this.getMeasuredHeight();
        if (this.getChildCount() == 0) {
            return;
        }
        if (this.mTarget == null) {
            this.ensureTarget();
        }
        if (this.mTarget == null) {
            return;
        }
        Object object = this.mTarget;
        n3 = this.getPaddingLeft();
        n4 = this.getPaddingTop();
        object.layout(n3, n4, n3 + (n - this.getPaddingLeft() - this.getPaddingRight()), n4 + (n2 - this.getPaddingTop() - this.getPaddingBottom()));
        int n5 = this.mCircleView.getMeasuredWidth();
        int n6 = this.mCircleView.getMeasuredHeight();
        object = this.mCircleView;
        n2 = n / 2;
        n3 = n5 / 2;
        n4 = this.mCurrentTargetOffsetTop;
        object.layout(n2 - n3, n4, n / 2 + n5 / 2, n4 + n6);
    }

    public void onMeasure(int n, int n2) {
        View view;
        super.onMeasure(n, n2);
        if (this.mTarget == null) {
            this.ensureTarget();
        }
        if ((view = this.mTarget) == null) {
            return;
        }
        view.measure(View.MeasureSpec.makeMeasureSpec((int)(this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight()), (int)0x40000000), View.MeasureSpec.makeMeasureSpec((int)(this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom()), (int)0x40000000));
        this.mCircleView.measure(View.MeasureSpec.makeMeasureSpec((int)this.mCircleDiameter, (int)0x40000000), View.MeasureSpec.makeMeasureSpec((int)this.mCircleDiameter, (int)0x40000000));
        this.mCircleViewIndex = -1;
        for (n = 0; n < this.getChildCount(); ++n) {
            if (this.getChildAt(n) != this.mCircleView) continue;
            this.mCircleViewIndex = n;
            break;
        }
    }

    @Override
    public boolean onNestedFling(View view, float f, float f2, boolean bl) {
        return this.dispatchNestedFling(f, f2, bl);
    }

    @Override
    public boolean onNestedPreFling(View view, float f, float f2) {
        return this.dispatchNestedPreFling(f, f2);
    }

    @Override
    public void onNestedPreScroll(View object, int n, int n2, int[] nArray) {
        float f;
        if (n2 > 0 && (f = this.mTotalUnconsumed) > 0.0f) {
            if ((float)n2 > f) {
                nArray[1] = n2 - (int)f;
                this.mTotalUnconsumed = 0.0f;
            } else {
                this.mTotalUnconsumed = f - (float)n2;
                nArray[1] = n2;
            }
            this.moveSpinner(this.mTotalUnconsumed);
        }
        if (this.mUsingCustomStart && n2 > 0 && this.mTotalUnconsumed == 0.0f && Math.abs(n2 - nArray[1]) > 0) {
            this.mCircleView.setVisibility(8);
        }
        if (this.dispatchNestedPreScroll(n - nArray[0], n2 - nArray[1], (int[])(object = (Object)this.mParentScrollConsumed), null)) {
            nArray[0] = nArray[0] + object[0];
            nArray[1] = nArray[1] + object[1];
        }
    }

    @Override
    public void onNestedScroll(View view, int n, int n2, int n3, int n4) {
        this.dispatchNestedScroll(n, n2, n3, n4, this.mParentOffsetInWindow);
        n = this.mParentOffsetInWindow[1] + n4;
        if (n < 0 && !this.canChildScrollUp()) {
            float f;
            this.mTotalUnconsumed = f = this.mTotalUnconsumed + (float)Math.abs(n);
            this.moveSpinner(f);
        }
    }

    @Override
    public void onNestedScrollAccepted(View view, View view2, int n) {
        this.mNestedScrollingParentHelper.onNestedScrollAccepted(view, view2, n);
        this.startNestedScroll(n & 2);
        this.mTotalUnconsumed = 0.0f;
        this.mNestedScrollInProgress = true;
    }

    @Override
    public boolean onStartNestedScroll(View view, View view2, int n) {
        boolean bl = this.isEnabled() && !this.mReturningToStart && !this.mRefreshing && (n & 2) != 0;
        return bl;
    }

    @Override
    public void onStopNestedScroll(View view) {
        this.mNestedScrollingParentHelper.onStopNestedScroll(view);
        this.mNestedScrollInProgress = false;
        float f = this.mTotalUnconsumed;
        if (f > 0.0f) {
            this.finishSpinner(f);
            this.mTotalUnconsumed = 0.0f;
        }
        this.stopNestedScroll();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int n = motionEvent.getActionMasked();
        if (this.mReturningToStart && n == 0) {
            this.mReturningToStart = false;
        }
        if (!(!this.isEnabled() || this.mReturningToStart || this.canChildScrollUp() || this.mRefreshing || this.mNestedScrollInProgress)) {
            switch (n) {
                default: {
                    break;
                }
                case 6: {
                    this.onSecondaryPointerUp(motionEvent);
                    break;
                }
                case 5: {
                    n = motionEvent.getActionIndex();
                    if (n < 0) {
                        Log.e((String)LOG_TAG, (String)"Got ACTION_POINTER_DOWN event but have an invalid action index.");
                        return false;
                    }
                    this.mActivePointerId = motionEvent.getPointerId(n);
                    break;
                }
                case 3: {
                    return false;
                }
                case 2: {
                    n = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (n < 0) {
                        Log.e((String)LOG_TAG, (String)"Got ACTION_MOVE event but have an invalid active pointer id.");
                        return false;
                    }
                    float f = motionEvent.getY(n);
                    this.startDragging(f);
                    if (!this.mIsBeingDragged) break;
                    if ((f = (f - this.mInitialMotionY) * 0.5f) > 0.0f) {
                        this.moveSpinner(f);
                        break;
                    }
                    return false;
                }
                case 1: {
                    n = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (n < 0) {
                        Log.e((String)LOG_TAG, (String)"Got ACTION_UP event but don't have an active pointer id.");
                        return false;
                    }
                    if (this.mIsBeingDragged) {
                        float f = motionEvent.getY(n);
                        float f2 = this.mInitialMotionY;
                        this.mIsBeingDragged = false;
                        this.finishSpinner((f - f2) * 0.5f);
                    }
                    this.mActivePointerId = -1;
                    return false;
                }
                case 0: {
                    this.mActivePointerId = motionEvent.getPointerId(0);
                    this.mIsBeingDragged = false;
                }
            }
            return true;
        }
        return false;
    }

    public void requestDisallowInterceptTouchEvent(boolean bl) {
        View view;
        if (!(Build.VERSION.SDK_INT < 21 && this.mTarget instanceof AbsListView || (view = this.mTarget) != null && !ViewCompat.isNestedScrollingEnabled(view))) {
            super.requestDisallowInterceptTouchEvent(bl);
        }
    }

    void reset() {
        this.mCircleView.clearAnimation();
        this.mProgress.stop();
        this.mCircleView.setVisibility(8);
        this.setColorViewAlpha(255);
        if (this.mScale) {
            this.setAnimationProgress(0.0f);
        } else {
            this.setTargetOffsetTopAndBottom(this.mOriginalOffsetTop - this.mCurrentTargetOffsetTop);
        }
        this.mCurrentTargetOffsetTop = this.mCircleView.getTop();
    }

    void setAnimationProgress(float f) {
        this.mCircleView.setScaleX(f);
        this.mCircleView.setScaleY(f);
    }

    @Deprecated
    public void setColorScheme(int ... nArray) {
        this.setColorSchemeResources(nArray);
    }

    public void setColorSchemeColors(int ... nArray) {
        this.ensureTarget();
        this.mProgress.setColorSchemeColors(nArray);
    }

    public void setColorSchemeResources(int ... nArray) {
        Context context = this.getContext();
        int[] nArray2 = new int[nArray.length];
        for (int i = 0; i < nArray.length; ++i) {
            nArray2[i] = ContextCompat.getColor(context, nArray[i]);
        }
        this.setColorSchemeColors(nArray2);
    }

    public void setDistanceToTriggerSync(int n) {
        this.mTotalDragDistance = n;
    }

    public void setEnabled(boolean bl) {
        super.setEnabled(bl);
        if (!bl) {
            this.reset();
        }
    }

    @Override
    public void setNestedScrollingEnabled(boolean bl) {
        this.mNestedScrollingChildHelper.setNestedScrollingEnabled(bl);
    }

    public void setOnChildScrollUpCallback(OnChildScrollUpCallback onChildScrollUpCallback) {
        this.mChildScrollUpCallback = onChildScrollUpCallback;
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.mListener = onRefreshListener;
    }

    @Deprecated
    public void setProgressBackgroundColor(int n) {
        this.setProgressBackgroundColorSchemeResource(n);
    }

    public void setProgressBackgroundColorSchemeColor(int n) {
        this.mCircleView.setBackgroundColor(n);
    }

    public void setProgressBackgroundColorSchemeResource(int n) {
        this.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this.getContext(), n));
    }

    public void setProgressViewEndTarget(boolean bl, int n) {
        this.mSpinnerOffsetEnd = n;
        this.mScale = bl;
        this.mCircleView.invalidate();
    }

    public void setProgressViewOffset(boolean bl, int n, int n2) {
        this.mScale = bl;
        this.mOriginalOffsetTop = n;
        this.mSpinnerOffsetEnd = n2;
        this.mUsingCustomStart = true;
        this.reset();
        this.mRefreshing = false;
    }

    public void setRefreshing(boolean bl) {
        if (bl && this.mRefreshing != bl) {
            this.mRefreshing = bl;
            int n = !this.mUsingCustomStart ? this.mSpinnerOffsetEnd + this.mOriginalOffsetTop : this.mSpinnerOffsetEnd;
            this.setTargetOffsetTopAndBottom(n - this.mCurrentTargetOffsetTop);
            this.mNotify = false;
            this.startScaleUpAnimation(this.mRefreshListener);
        } else {
            this.setRefreshing(bl, false);
        }
    }

    public void setSize(int n) {
        if (n != 0 && n != 1) {
            return;
        }
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        this.mCircleDiameter = n == 0 ? (int)(displayMetrics.density * 56.0f) : (int)(displayMetrics.density * 40.0f);
        this.mCircleView.setImageDrawable(null);
        this.mProgress.setStyle(n);
        this.mCircleView.setImageDrawable(this.mProgress);
    }

    public void setSlingshotDistance(int n) {
        this.mCustomSlingshotDistance = n;
    }

    void setTargetOffsetTopAndBottom(int n) {
        this.mCircleView.bringToFront();
        ViewCompat.offsetTopAndBottom((View)this.mCircleView, n);
        this.mCurrentTargetOffsetTop = this.mCircleView.getTop();
    }

    @Override
    public boolean startNestedScroll(int n) {
        return this.mNestedScrollingChildHelper.startNestedScroll(n);
    }

    void startScaleDownAnimation(Animation.AnimationListener animationListener) {
        Animation animation;
        this.mScaleDownAnimation = animation = new Animation(this){
            final SwipeRefreshLayout this$0;
            {
                this.this$0 = swipeRefreshLayout;
            }

            public void applyTransformation(float f, Transformation transformation) {
                this.this$0.setAnimationProgress(1.0f - f);
            }
        };
        animation.setDuration(150L);
        this.mCircleView.setAnimationListener(animationListener);
        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(this.mScaleDownAnimation);
    }

    @Override
    public void stopNestedScroll() {
        this.mNestedScrollingChildHelper.stopNestedScroll();
    }

    public static interface OnChildScrollUpCallback {
        public boolean canChildScrollUp(SwipeRefreshLayout var1, View var2);
    }

    public static interface OnRefreshListener {
        public void onRefresh();
    }
}

