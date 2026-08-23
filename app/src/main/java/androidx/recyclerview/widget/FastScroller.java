/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.graphics.Canvas
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.StateListDrawable
 *  android.view.MotionEvent
 *  android.view.View
 */
package androidx.recyclerview.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

class FastScroller
extends RecyclerView.ItemDecoration
implements RecyclerView.OnItemTouchListener {
    private static final int ANIMATION_STATE_FADING_IN = 1;
    private static final int ANIMATION_STATE_FADING_OUT = 3;
    private static final int ANIMATION_STATE_IN = 2;
    private static final int ANIMATION_STATE_OUT = 0;
    private static final int DRAG_NONE = 0;
    private static final int DRAG_X = 1;
    private static final int DRAG_Y = 2;
    private static final int[] EMPTY_STATE_SET;
    private static final int HIDE_DELAY_AFTER_DRAGGING_MS = 1200;
    private static final int HIDE_DELAY_AFTER_VISIBLE_MS = 1500;
    private static final int HIDE_DURATION_MS = 500;
    private static final int[] PRESSED_STATE_SET;
    private static final int SCROLLBAR_FULL_OPAQUE = 255;
    private static final int SHOW_DURATION_MS = 500;
    private static final int STATE_DRAGGING = 2;
    private static final int STATE_HIDDEN = 0;
    private static final int STATE_VISIBLE = 1;
    int mAnimationState;
    private int mDragState = 0;
    private final Runnable mHideRunnable;
    float mHorizontalDragX;
    private final int[] mHorizontalRange;
    int mHorizontalThumbCenterX;
    private final StateListDrawable mHorizontalThumbDrawable;
    private final int mHorizontalThumbHeight;
    int mHorizontalThumbWidth;
    private final Drawable mHorizontalTrackDrawable;
    private final int mHorizontalTrackHeight;
    private final int mMargin;
    private boolean mNeedHorizontalScrollbar = false;
    private boolean mNeedVerticalScrollbar = false;
    private final RecyclerView.OnScrollListener mOnScrollListener;
    private RecyclerView mRecyclerView;
    private int mRecyclerViewHeight = 0;
    private int mRecyclerViewWidth = 0;
    private final int mScrollbarMinimumRange;
    final ValueAnimator mShowHideAnimator;
    private int mState = 0;
    float mVerticalDragY;
    private final int[] mVerticalRange = new int[2];
    int mVerticalThumbCenterY;
    final StateListDrawable mVerticalThumbDrawable;
    int mVerticalThumbHeight;
    private final int mVerticalThumbWidth;
    final Drawable mVerticalTrackDrawable;
    private final int mVerticalTrackWidth;

    static {
        PRESSED_STATE_SET = new int[]{16842919};
        EMPTY_STATE_SET = new int[0];
    }

    FastScroller(RecyclerView recyclerView, StateListDrawable stateListDrawable, Drawable drawable2, StateListDrawable stateListDrawable2, Drawable drawable3, int n, int n2, int n3) {
        ValueAnimator valueAnimator;
        this.mHorizontalRange = new int[2];
        this.mShowHideAnimator = valueAnimator = ValueAnimator.ofFloat((float[])new float[]{0.0f, 1.0f});
        this.mAnimationState = 0;
        this.mHideRunnable = new Runnable(this){
            final FastScroller this$0;
            {
                this.this$0 = fastScroller;
            }

            @Override
            public void run() {
                this.this$0.hide(500);
            }
        };
        this.mOnScrollListener = new RecyclerView.OnScrollListener(this){
            final FastScroller this$0;
            {
                this.this$0 = fastScroller;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int n, int n2) {
                this.this$0.updateScrollPosition(recyclerView.computeHorizontalScrollOffset(), recyclerView.computeVerticalScrollOffset());
            }
        };
        this.mVerticalThumbDrawable = stateListDrawable;
        this.mVerticalTrackDrawable = drawable2;
        this.mHorizontalThumbDrawable = stateListDrawable2;
        this.mHorizontalTrackDrawable = drawable3;
        this.mVerticalThumbWidth = Math.max(n, stateListDrawable.getIntrinsicWidth());
        this.mVerticalTrackWidth = Math.max(n, drawable2.getIntrinsicWidth());
        this.mHorizontalThumbHeight = Math.max(n, stateListDrawable2.getIntrinsicWidth());
        this.mHorizontalTrackHeight = Math.max(n, drawable3.getIntrinsicWidth());
        this.mScrollbarMinimumRange = n2;
        this.mMargin = n3;
        stateListDrawable.setAlpha(255);
        drawable2.setAlpha(255);
        valueAnimator.addListener((Animator.AnimatorListener)new AnimatorListener(this));
        valueAnimator.addUpdateListener((ValueAnimator.AnimatorUpdateListener)new AnimatorUpdater(this));
        this.attachToRecyclerView(recyclerView);
    }

    private void cancelHide() {
        this.mRecyclerView.removeCallbacks(this.mHideRunnable);
    }

    private void destroyCallbacks() {
        this.mRecyclerView.removeItemDecoration(this);
        this.mRecyclerView.removeOnItemTouchListener(this);
        this.mRecyclerView.removeOnScrollListener(this.mOnScrollListener);
        this.cancelHide();
    }

    private void drawHorizontalScrollbar(Canvas canvas) {
        int n = this.mRecyclerViewHeight;
        int n2 = this.mHorizontalThumbHeight;
        int n3 = this.mHorizontalThumbCenterX;
        int n4 = this.mHorizontalThumbWidth;
        this.mHorizontalThumbDrawable.setBounds(0, 0, n4, n2);
        this.mHorizontalTrackDrawable.setBounds(0, 0, this.mRecyclerViewWidth, this.mHorizontalTrackHeight);
        canvas.translate(0.0f, (float)(n -= n2));
        this.mHorizontalTrackDrawable.draw(canvas);
        canvas.translate((float)(n3 -= n4 / 2), 0.0f);
        this.mHorizontalThumbDrawable.draw(canvas);
        canvas.translate((float)(-n3), (float)(-n));
    }

    private void drawVerticalScrollbar(Canvas canvas) {
        int n = this.mRecyclerViewWidth;
        int n2 = this.mVerticalThumbWidth;
        n -= n2;
        int n3 = this.mVerticalThumbCenterY;
        int n4 = this.mVerticalThumbHeight;
        n3 -= n4 / 2;
        this.mVerticalThumbDrawable.setBounds(0, 0, n2, n4);
        this.mVerticalTrackDrawable.setBounds(0, 0, this.mVerticalTrackWidth, this.mRecyclerViewHeight);
        if (this.isLayoutRTL()) {
            this.mVerticalTrackDrawable.draw(canvas);
            canvas.translate((float)this.mVerticalThumbWidth, (float)n3);
            canvas.scale(-1.0f, 1.0f);
            this.mVerticalThumbDrawable.draw(canvas);
            canvas.scale(1.0f, 1.0f);
            canvas.translate((float)(-this.mVerticalThumbWidth), (float)(-n3));
        } else {
            canvas.translate((float)n, 0.0f);
            this.mVerticalTrackDrawable.draw(canvas);
            canvas.translate(0.0f, (float)n3);
            this.mVerticalThumbDrawable.draw(canvas);
            canvas.translate((float)(-n), (float)(-n3));
        }
    }

    private int[] getHorizontalRange() {
        int n;
        int[] nArray = this.mHorizontalRange;
        nArray[0] = n = this.mMargin;
        nArray[1] = this.mRecyclerViewWidth - n;
        return nArray;
    }

    private int[] getVerticalRange() {
        int n;
        int[] nArray = this.mVerticalRange;
        nArray[0] = n = this.mMargin;
        nArray[1] = this.mRecyclerViewHeight - n;
        return nArray;
    }

    private void horizontalScrollTo(float f) {
        int[] nArray = this.getHorizontalRange();
        f = Math.max((float)nArray[0], Math.min((float)nArray[1], f));
        if (Math.abs((float)this.mHorizontalThumbCenterX - f) < 2.0f) {
            return;
        }
        int n = this.scrollTo(this.mHorizontalDragX, f, nArray, this.mRecyclerView.computeHorizontalScrollRange(), this.mRecyclerView.computeHorizontalScrollOffset(), this.mRecyclerViewWidth);
        if (n != 0) {
            this.mRecyclerView.scrollBy(n, 0);
        }
        this.mHorizontalDragX = f;
    }

    private boolean isLayoutRTL() {
        int n = ViewCompat.getLayoutDirection((View)this.mRecyclerView);
        boolean bl = true;
        if (n != 1) {
            bl = false;
        }
        return bl;
    }

    private void resetHideDelay(int n) {
        this.cancelHide();
        this.mRecyclerView.postDelayed(this.mHideRunnable, n);
    }

    private int scrollTo(float f, float f2, int[] nArray, int n, int n2, int n3) {
        int n4 = nArray[1] - nArray[0];
        if (n4 == 0) {
            return 0;
        }
        if ((n2 += (n = (int)((float)(n3 = n - n3) * (f = (f2 - f) / (float)n4)))) < n3 && n2 >= 0) {
            return n;
        }
        return 0;
    }

    private void setupCallbacks() {
        this.mRecyclerView.addItemDecoration(this);
        this.mRecyclerView.addOnItemTouchListener(this);
        this.mRecyclerView.addOnScrollListener(this.mOnScrollListener);
    }

    private void verticalScrollTo(float f) {
        int[] nArray = this.getVerticalRange();
        f = Math.max((float)nArray[0], Math.min((float)nArray[1], f));
        if (Math.abs((float)this.mVerticalThumbCenterY - f) < 2.0f) {
            return;
        }
        int n = this.scrollTo(this.mVerticalDragY, f, nArray, this.mRecyclerView.computeVerticalScrollRange(), this.mRecyclerView.computeVerticalScrollOffset(), this.mRecyclerViewHeight);
        if (n != 0) {
            this.mRecyclerView.scrollBy(0, n);
        }
        this.mVerticalDragY = f;
    }

    public void attachToRecyclerView(RecyclerView recyclerView) {
        RecyclerView recyclerView2 = this.mRecyclerView;
        if (recyclerView2 == recyclerView) {
            return;
        }
        if (recyclerView2 != null) {
            this.destroyCallbacks();
        }
        this.mRecyclerView = recyclerView;
        if (recyclerView != null) {
            this.setupCallbacks();
        }
    }

    Drawable getHorizontalThumbDrawable() {
        return this.mHorizontalThumbDrawable;
    }

    Drawable getHorizontalTrackDrawable() {
        return this.mHorizontalTrackDrawable;
    }

    Drawable getVerticalThumbDrawable() {
        return this.mVerticalThumbDrawable;
    }

    Drawable getVerticalTrackDrawable() {
        return this.mVerticalTrackDrawable;
    }

    public void hide() {
        this.hide(0);
    }

    void hide(int n) {
        switch (this.mAnimationState) {
            default: {
                break;
            }
            case 1: {
                this.mShowHideAnimator.cancel();
            }
            case 2: {
                this.mAnimationState = 3;
                ValueAnimator valueAnimator = this.mShowHideAnimator;
                valueAnimator.setFloatValues(new float[]{((Float)valueAnimator.getAnimatedValue()).floatValue(), 0.0f});
                this.mShowHideAnimator.setDuration((long)n);
                this.mShowHideAnimator.start();
            }
        }
    }

    public boolean isDragging() {
        boolean bl = this.mState == 2;
        return bl;
    }

    boolean isHidden() {
        boolean bl = this.mState == 0;
        return bl;
    }

    boolean isPointInsideHorizontalThumb(float f, float f2) {
        int n;
        int n2;
        boolean bl = f2 >= (float)(this.mRecyclerViewHeight - this.mHorizontalThumbHeight) && f >= (float)((n2 = this.mHorizontalThumbCenterX) - (n = this.mHorizontalThumbWidth) / 2) && f <= (float)(n2 + n / 2);
        return bl;
    }

    boolean isPointInsideVerticalThumb(float f, float f2) {
        int n;
        int n2;
        boolean bl = (this.isLayoutRTL() ? f <= (float)(this.mVerticalThumbWidth / 2) : f >= (float)(this.mRecyclerViewWidth - this.mVerticalThumbWidth)) && f2 >= (float)((n2 = this.mVerticalThumbCenterY) - (n = this.mVerticalThumbHeight) / 2) && f2 <= (float)(n2 + n / 2);
        return bl;
    }

    boolean isVisible() {
        int n = this.mState;
        boolean bl = true;
        if (n != 1) {
            bl = false;
        }
        return bl;
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        if (this.mRecyclerViewWidth == this.mRecyclerView.getWidth() && this.mRecyclerViewHeight == this.mRecyclerView.getHeight()) {
            if (this.mAnimationState != 0) {
                if (this.mNeedVerticalScrollbar) {
                    this.drawVerticalScrollbar(canvas);
                }
                if (this.mNeedHorizontalScrollbar) {
                    this.drawHorizontalScrollbar(canvas);
                }
            }
            return;
        }
        this.mRecyclerViewWidth = this.mRecyclerView.getWidth();
        this.mRecyclerViewHeight = this.mRecyclerView.getHeight();
        this.setState(0);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        boolean bl;
        int n = this.mState;
        if (n == 1) {
            boolean bl2 = this.isPointInsideVerticalThumb(motionEvent.getX(), motionEvent.getY());
            bl = this.isPointInsideHorizontalThumb(motionEvent.getX(), motionEvent.getY());
            if (motionEvent.getAction() == 0 && (bl2 || bl)) {
                if (bl) {
                    this.mDragState = 1;
                    this.mHorizontalDragX = (int)motionEvent.getX();
                } else if (bl2) {
                    this.mDragState = 2;
                    this.mVerticalDragY = (int)motionEvent.getY();
                }
                this.setState(2);
                bl = true;
            } else {
                bl = false;
            }
        } else {
            bl = n == 2;
        }
        return bl;
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean bl) {
    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        if (this.mState == 0) {
            return;
        }
        if (motionEvent.getAction() == 0) {
            boolean bl = this.isPointInsideVerticalThumb(motionEvent.getX(), motionEvent.getY());
            boolean bl2 = this.isPointInsideHorizontalThumb(motionEvent.getX(), motionEvent.getY());
            if (bl || bl2) {
                if (bl2) {
                    this.mDragState = 1;
                    this.mHorizontalDragX = (int)motionEvent.getX();
                } else if (bl) {
                    this.mDragState = 2;
                    this.mVerticalDragY = (int)motionEvent.getY();
                }
                this.setState(2);
            }
        } else if (motionEvent.getAction() == 1 && this.mState == 2) {
            this.mVerticalDragY = 0.0f;
            this.mHorizontalDragX = 0.0f;
            this.setState(1);
            this.mDragState = 0;
        } else if (motionEvent.getAction() == 2 && this.mState == 2) {
            this.show();
            if (this.mDragState == 1) {
                this.horizontalScrollTo(motionEvent.getX());
            }
            if (this.mDragState == 2) {
                this.verticalScrollTo(motionEvent.getY());
            }
        }
    }

    void requestRedraw() {
        this.mRecyclerView.invalidate();
    }

    void setState(int n) {
        if (n == 2 && this.mState != 2) {
            this.mVerticalThumbDrawable.setState(PRESSED_STATE_SET);
            this.cancelHide();
        }
        if (n == 0) {
            this.requestRedraw();
        } else {
            this.show();
        }
        if (this.mState == 2 && n != 2) {
            this.mVerticalThumbDrawable.setState(EMPTY_STATE_SET);
            this.resetHideDelay(1200);
        } else if (n == 1) {
            this.resetHideDelay(1500);
        }
        this.mState = n;
    }

    public void show() {
        switch (this.mAnimationState) {
            default: {
                break;
            }
            case 3: {
                this.mShowHideAnimator.cancel();
            }
            case 0: {
                this.mAnimationState = 1;
                ValueAnimator valueAnimator = this.mShowHideAnimator;
                valueAnimator.setFloatValues(new float[]{((Float)valueAnimator.getAnimatedValue()).floatValue(), 1.0f});
                this.mShowHideAnimator.setDuration(500L);
                this.mShowHideAnimator.setStartDelay(0L);
                this.mShowHideAnimator.start();
            }
        }
    }

    void updateScrollPosition(int n, int n2) {
        float f;
        float f2;
        int n3;
        int n4 = this.mRecyclerView.computeVerticalScrollRange();
        boolean bl = n4 - (n3 = this.mRecyclerViewHeight) > 0 && this.mRecyclerViewHeight >= this.mScrollbarMinimumRange;
        this.mNeedVerticalScrollbar = bl;
        int n5 = this.mRecyclerView.computeHorizontalScrollRange();
        int n6 = this.mRecyclerViewWidth;
        bl = n5 - n6 > 0 && this.mRecyclerViewWidth >= this.mScrollbarMinimumRange;
        this.mNeedHorizontalScrollbar = bl;
        boolean bl2 = this.mNeedVerticalScrollbar;
        if (!bl2 && !bl) {
            if (this.mState != 0) {
                this.setState(0);
            }
            return;
        }
        if (bl2) {
            f2 = n2;
            f = (float)n3 / 2.0f;
            this.mVerticalThumbCenterY = (int)((float)n3 * (f2 + f) / (float)n4);
            this.mVerticalThumbHeight = Math.min(n3, n3 * n3 / n4);
        }
        if (this.mNeedHorizontalScrollbar) {
            f = n;
            f2 = (float)n6 / 2.0f;
            this.mHorizontalThumbCenterX = (int)((float)n6 * (f + f2) / (float)n5);
            this.mHorizontalThumbWidth = Math.min(n6, n6 * n6 / n5);
        }
        if ((n = this.mState) == 0 || n == 1) {
            this.setState(1);
        }
    }

    private class AnimatorListener
    extends AnimatorListenerAdapter {
        private boolean mCanceled;
        final FastScroller this$0;

        AnimatorListener(FastScroller fastScroller) {
            this.this$0 = fastScroller;
            this.mCanceled = false;
        }

        public void onAnimationCancel(Animator animator2) {
            this.mCanceled = true;
        }

        public void onAnimationEnd(Animator animator2) {
            if (this.mCanceled) {
                this.mCanceled = false;
                return;
            }
            if (((Float)this.this$0.mShowHideAnimator.getAnimatedValue()).floatValue() == 0.0f) {
                this.this$0.mAnimationState = 0;
                this.this$0.setState(0);
            } else {
                this.this$0.mAnimationState = 2;
                this.this$0.requestRedraw();
            }
        }
    }

    private class AnimatorUpdater
    implements ValueAnimator.AnimatorUpdateListener {
        final FastScroller this$0;

        AnimatorUpdater(FastScroller fastScroller) {
            this.this$0 = fastScroller;
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int n = (int)(((Float)valueAnimator.getAnimatedValue()).floatValue() * 255.0f);
            this.this$0.mVerticalThumbDrawable.setAlpha(n);
            this.this$0.mVerticalTrackDrawable.setAlpha(n);
            this.this$0.requestRedraw();
        }
    }
}

