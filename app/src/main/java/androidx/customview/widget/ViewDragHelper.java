/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.Log
 *  android.view.MotionEvent
 *  android.view.VelocityTracker
 *  android.view.View
 *  android.view.ViewConfiguration
 *  android.view.ViewGroup
 *  android.view.animation.Interpolator
 *  android.widget.OverScroller
 */
package androidx.customview.widget;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.OverScroller;
import androidx.core.view.ViewCompat;
import java.util.Arrays;

public class ViewDragHelper {
    private static final int BASE_SETTLE_DURATION = 256;
    public static final int DIRECTION_ALL = 3;
    public static final int DIRECTION_HORIZONTAL = 1;
    public static final int DIRECTION_VERTICAL = 2;
    public static final int EDGE_ALL = 15;
    public static final int EDGE_BOTTOM = 8;
    public static final int EDGE_LEFT = 1;
    public static final int EDGE_RIGHT = 2;
    private static final int EDGE_SIZE = 20;
    public static final int EDGE_TOP = 4;
    public static final int INVALID_POINTER = -1;
    private static final int MAX_SETTLE_DURATION = 600;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private static final String TAG = "ViewDragHelper";
    private static final Interpolator sInterpolator = new Interpolator(){

        public float getInterpolation(float f) {
            return (f -= 1.0f) * f * f * f * f + 1.0f;
        }
    };
    private int mActivePointerId = -1;
    private final Callback mCallback;
    private View mCapturedView;
    private final int mDefaultEdgeSize;
    private int mDragState;
    private int[] mEdgeDragsInProgress;
    private int[] mEdgeDragsLocked;
    private int mEdgeSize;
    private int[] mInitialEdgesTouched;
    private float[] mInitialMotionX;
    private float[] mInitialMotionY;
    private float[] mLastMotionX;
    private float[] mLastMotionY;
    private float mMaxVelocity;
    private float mMinVelocity;
    private final ViewGroup mParentView;
    private int mPointersDown;
    private boolean mReleaseInProgress;
    private OverScroller mScroller;
    private final Runnable mSetIdleRunnable = new Runnable(this){
        final ViewDragHelper this$0;
        {
            this.this$0 = viewDragHelper;
        }

        @Override
        public void run() {
            this.this$0.setDragState(0);
        }
    };
    private int mTouchSlop;
    private int mTrackingEdges;
    private VelocityTracker mVelocityTracker;

    private ViewDragHelper(Context context, ViewGroup viewGroup, Callback callback) {
        if (viewGroup != null) {
            if (callback != null) {
                int n;
                this.mParentView = viewGroup;
                this.mCallback = callback;
                viewGroup = ViewConfiguration.get((Context)context);
                this.mDefaultEdgeSize = n = (int)(20.0f * context.getResources().getDisplayMetrics().density + 0.5f);
                this.mEdgeSize = n;
                this.mTouchSlop = viewGroup.getScaledTouchSlop();
                this.mMaxVelocity = viewGroup.getScaledMaximumFlingVelocity();
                this.mMinVelocity = viewGroup.getScaledMinimumFlingVelocity();
                this.mScroller = new OverScroller(context, sInterpolator);
                return;
            }
            throw new IllegalArgumentException("Callback may not be null");
        }
        throw new IllegalArgumentException("Parent view may not be null");
    }

    private boolean checkNewEdgeDrag(float f, float f2, int n, int n2) {
        f = Math.abs(f);
        f2 = Math.abs(f2);
        int n3 = this.mInitialEdgesTouched[n];
        boolean bl = false;
        if (!((n3 & n2) != n2 || (this.mTrackingEdges & n2) == 0 || (this.mEdgeDragsLocked[n] & n2) == n2 || (this.mEdgeDragsInProgress[n] & n2) == n2 || f <= (float)(n3 = this.mTouchSlop) && f2 <= (float)n3)) {
            if (f < 0.5f * f2 && this.mCallback.onEdgeLock(n2)) {
                int[] nArray = this.mEdgeDragsLocked;
                nArray[n] = nArray[n] | n2;
                return false;
            }
            boolean bl2 = bl;
            if ((this.mEdgeDragsInProgress[n] & n2) == 0) {
                bl2 = bl;
                if (f > (float)this.mTouchSlop) {
                    bl2 = true;
                }
            }
            return bl2;
        }
        return false;
    }

    private boolean checkTouchSlop(View view, float f, float f2) {
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        if (view == null) {
            return false;
        }
        int n = this.mCallback.getViewHorizontalDragRange(view) > 0 ? 1 : 0;
        boolean bl4 = this.mCallback.getViewVerticalDragRange(view) > 0;
        if (n != 0 && bl4) {
            n = this.mTouchSlop;
            bl = bl3;
            if (f * f + f2 * f2 > (float)(n * n)) {
                bl = true;
            }
            return bl;
        }
        if (n != 0) {
            if (Math.abs(f) > (float)this.mTouchSlop) {
                bl = true;
            }
            return bl;
        }
        if (bl4) {
            bl = bl2;
            if (Math.abs(f2) > (float)this.mTouchSlop) {
                bl = true;
            }
            return bl;
        }
        return false;
    }

    private float clampMag(float f, float f2, float f3) {
        float f4 = Math.abs(f);
        if (f4 < f2) {
            return 0.0f;
        }
        if (f4 > f3) {
            if (!(f > 0.0f)) {
                f3 = -f3;
            }
            return f3;
        }
        return f;
    }

    private int clampMag(int n, int n2, int n3) {
        int n4 = Math.abs(n);
        if (n4 < n2) {
            return 0;
        }
        if (n4 > n3) {
            n = n > 0 ? n3 : -n3;
            return n;
        }
        return n;
    }

    private void clearMotionHistory() {
        float[] fArray = this.mInitialMotionX;
        if (fArray == null) {
            return;
        }
        Arrays.fill(fArray, 0.0f);
        Arrays.fill(this.mInitialMotionY, 0.0f);
        Arrays.fill(this.mLastMotionX, 0.0f);
        Arrays.fill(this.mLastMotionY, 0.0f);
        Arrays.fill(this.mInitialEdgesTouched, 0);
        Arrays.fill(this.mEdgeDragsInProgress, 0);
        Arrays.fill(this.mEdgeDragsLocked, 0);
        this.mPointersDown = 0;
    }

    private void clearMotionHistory(int n) {
        if (this.mInitialMotionX != null && this.isPointerDown(n)) {
            this.mInitialMotionX[n] = 0.0f;
            this.mInitialMotionY[n] = 0.0f;
            this.mLastMotionX[n] = 0.0f;
            this.mLastMotionY[n] = 0.0f;
            this.mInitialEdgesTouched[n] = 0;
            this.mEdgeDragsInProgress[n] = 0;
            this.mEdgeDragsLocked[n] = 0;
            this.mPointersDown &= ~(1 << n);
            return;
        }
    }

    private int computeAxisDuration(int n, int n2, int n3) {
        if (n == 0) {
            return 0;
        }
        int n4 = this.mParentView.getWidth();
        int n5 = n4 / 2;
        float f = Math.min(1.0f, (float)Math.abs(n) / (float)n4);
        float f2 = n5;
        float f3 = n5;
        f = this.distanceInfluenceForSnapDuration(f);
        n = (n2 = Math.abs(n2)) > 0 ? Math.round(Math.abs((f2 + f3 * f) / (float)n2) * 1000.0f) * 4 : (int)((1.0f + (float)Math.abs(n) / (float)n3) * 256.0f);
        return Math.min(n, 600);
    }

    private int computeSettleDuration(View view, int n, int n2, int n3, int n4) {
        int n5 = this.clampMag(n3, (int)this.mMinVelocity, (int)this.mMaxVelocity);
        n3 = this.clampMag(n4, (int)this.mMinVelocity, (int)this.mMaxVelocity);
        int n6 = Math.abs(n);
        n4 = Math.abs(n2);
        int n7 = Math.abs(n5);
        int n8 = Math.abs(n3);
        int n9 = n7 + n8;
        int n10 = n6 + n4;
        float f = n5 != 0 ? (float)n7 / (float)n9 : (float)n6 / (float)n10;
        float f2 = n3 != 0 ? (float)n8 / (float)n9 : (float)n4 / (float)n10;
        n = this.computeAxisDuration(n, n5, this.mCallback.getViewHorizontalDragRange(view));
        n2 = this.computeAxisDuration(n2, n3, this.mCallback.getViewVerticalDragRange(view));
        return (int)((float)n * f + (float)n2 * f2);
    }

    public static ViewDragHelper create(ViewGroup object, float f, Callback callback) {
        object = ViewDragHelper.create(object, callback);
        object.mTouchSlop = (int)((float)object.mTouchSlop * (1.0f / f));
        return object;
    }

    public static ViewDragHelper create(ViewGroup viewGroup, Callback callback) {
        return new ViewDragHelper(viewGroup.getContext(), viewGroup, callback);
    }

    private void dispatchViewReleased(float f, float f2) {
        this.mReleaseInProgress = true;
        this.mCallback.onViewReleased(this.mCapturedView, f, f2);
        this.mReleaseInProgress = false;
        if (this.mDragState == 1) {
            this.setDragState(0);
        }
    }

    private float distanceInfluenceForSnapDuration(float f) {
        return (float)Math.sin((f - 0.5f) * 0.47123894f);
    }

    private void dragTo(int n, int n2, int n3, int n4) {
        int n5 = n;
        int n6 = n2;
        int n7 = this.mCapturedView.getLeft();
        int n8 = this.mCapturedView.getTop();
        if (n3 != 0) {
            n = this.mCallback.clampViewPositionHorizontal(this.mCapturedView, n, n3);
            ViewCompat.offsetLeftAndRight(this.mCapturedView, n - n7);
        } else {
            n = n5;
        }
        if (n4 != 0) {
            n6 = this.mCallback.clampViewPositionVertical(this.mCapturedView, n2, n4);
            ViewCompat.offsetTopAndBottom(this.mCapturedView, n6 - n8);
        }
        if (n3 != 0 || n4 != 0) {
            this.mCallback.onViewPositionChanged(this.mCapturedView, n, n6, n - n7, n6 - n8);
        }
    }

    private void ensureMotionHistorySizeForId(int n) {
        Object[] objectArray = this.mInitialMotionX;
        if (objectArray == null || objectArray.length <= n) {
            float[] fArray = new float[n + 1];
            float[] fArray2 = new float[n + 1];
            float[] fArray3 = new float[n + 1];
            float[] fArray4 = new float[n + 1];
            int[] nArray = new int[n + 1];
            int[] nArray2 = new int[n + 1];
            int[] nArray3 = new int[n + 1];
            if (objectArray != null) {
                System.arraycopy(objectArray, 0, fArray, 0, objectArray.length);
                objectArray = this.mInitialMotionY;
                System.arraycopy(objectArray, 0, fArray2, 0, objectArray.length);
                objectArray = this.mLastMotionX;
                System.arraycopy(objectArray, 0, fArray3, 0, objectArray.length);
                objectArray = this.mLastMotionY;
                System.arraycopy(objectArray, 0, fArray4, 0, objectArray.length);
                objectArray = this.mInitialEdgesTouched;
                System.arraycopy(objectArray, 0, nArray, 0, objectArray.length);
                objectArray = this.mEdgeDragsInProgress;
                System.arraycopy(objectArray, 0, nArray2, 0, objectArray.length);
                objectArray = this.mEdgeDragsLocked;
                System.arraycopy(objectArray, 0, nArray3, 0, objectArray.length);
            }
            this.mInitialMotionX = fArray;
            this.mInitialMotionY = fArray2;
            this.mLastMotionX = fArray3;
            this.mLastMotionY = fArray4;
            this.mInitialEdgesTouched = nArray;
            this.mEdgeDragsInProgress = nArray2;
            this.mEdgeDragsLocked = nArray3;
        }
    }

    private boolean forceSettleCapturedViewAt(int n, int n2, int n3, int n4) {
        int n5 = this.mCapturedView.getLeft();
        int n6 = this.mCapturedView.getTop();
        if ((n -= n5) == 0 && (n2 -= n6) == 0) {
            this.mScroller.abortAnimation();
            this.setDragState(0);
            return false;
        }
        n3 = this.computeSettleDuration(this.mCapturedView, n, n2, n3, n4);
        this.mScroller.startScroll(n5, n6, n, n2, n3);
        this.setDragState(2);
        return true;
    }

    private int getEdgesTouched(int n, int n2) {
        int n3 = 0;
        if (n < this.mParentView.getLeft() + this.mEdgeSize) {
            n3 = 0 | 1;
        }
        int n4 = n3;
        if (n2 < this.mParentView.getTop() + this.mEdgeSize) {
            n4 = n3 | 4;
        }
        n3 = n4;
        if (n > this.mParentView.getRight() - this.mEdgeSize) {
            n3 = n4 | 2;
        }
        n = n3;
        if (n2 > this.mParentView.getBottom() - this.mEdgeSize) {
            n = n3 | 8;
        }
        return n;
    }

    private boolean isValidPointerForActionMove(int n) {
        if (!this.isPointerDown(n)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Ignoring pointerId=");
            stringBuilder.append(n);
            stringBuilder.append(" because ACTION_DOWN was not received for this pointer before ACTION_MOVE. It likely happened because  ViewDragHelper did not receive all the events in the event stream.");
            Log.e((String)TAG, (String)stringBuilder.toString());
            return false;
        }
        return true;
    }

    private void releaseViewForPointerUp() {
        this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxVelocity);
        this.dispatchViewReleased(this.clampMag(this.mVelocityTracker.getXVelocity(this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity), this.clampMag(this.mVelocityTracker.getYVelocity(this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity));
    }

    private void reportNewEdgeDrags(float f, float f2, int n) {
        int n2 = 0;
        if (this.checkNewEdgeDrag(f, f2, n, 1)) {
            n2 = 0 | 1;
        }
        int n3 = n2;
        if (this.checkNewEdgeDrag(f2, f, n, 4)) {
            n3 = n2 | 4;
        }
        n2 = n3;
        if (this.checkNewEdgeDrag(f, f2, n, 2)) {
            n2 = n3 | 2;
        }
        n3 = n2;
        if (this.checkNewEdgeDrag(f2, f, n, 8)) {
            n3 = n2 | 8;
        }
        if (n3 != 0) {
            int[] nArray = this.mEdgeDragsInProgress;
            nArray[n] = nArray[n] | n3;
            this.mCallback.onEdgeDragStarted(n3, n);
        }
    }

    private void saveInitialMotion(float f, float f2, int n) {
        this.ensureMotionHistorySizeForId(n);
        float[] fArray = this.mInitialMotionX;
        this.mLastMotionX[n] = f;
        fArray[n] = f;
        fArray = this.mInitialMotionY;
        this.mLastMotionY[n] = f2;
        fArray[n] = f2;
        this.mInitialEdgesTouched[n] = this.getEdgesTouched((int)f, (int)f2);
        this.mPointersDown |= 1 << n;
    }

    private void saveLastMotion(MotionEvent motionEvent) {
        int n = motionEvent.getPointerCount();
        for (int i = 0; i < n; ++i) {
            int n2 = motionEvent.getPointerId(i);
            if (!this.isValidPointerForActionMove(n2)) continue;
            float f = motionEvent.getX(i);
            float f2 = motionEvent.getY(i);
            this.mLastMotionX[n2] = f;
            this.mLastMotionY[n2] = f2;
        }
    }

    public void abort() {
        this.cancel();
        if (this.mDragState == 2) {
            int n = this.mScroller.getCurrX();
            int n2 = this.mScroller.getCurrY();
            this.mScroller.abortAnimation();
            int n3 = this.mScroller.getCurrX();
            int n4 = this.mScroller.getCurrY();
            this.mCallback.onViewPositionChanged(this.mCapturedView, n3, n4, n3 - n, n4 - n2);
        }
        this.setDragState(0);
    }

    protected boolean canScroll(View view, boolean bl, int n, int n2, int n3, int n4) {
        block4: {
            block2: {
                boolean bl2;
                block3: {
                    boolean bl3 = view instanceof ViewGroup;
                    bl2 = true;
                    if (bl3) {
                        ViewGroup viewGroup = (ViewGroup)view;
                        int n5 = view.getScrollX();
                        int n6 = view.getScrollY();
                        for (int i = viewGroup.getChildCount() - 1; i >= 0; --i) {
                            View view2 = viewGroup.getChildAt(i);
                            if (n3 + n5 < view2.getLeft() || n3 + n5 >= view2.getRight() || n4 + n6 < view2.getTop() || n4 + n6 >= view2.getBottom() || !this.canScroll(view2, true, n, n2, n3 + n5 - view2.getLeft(), n4 + n6 - view2.getTop())) continue;
                            return true;
                        }
                    }
                    if (!bl) break block2;
                    if (view.canScrollHorizontally(-n)) break block3;
                    if (!view.canScrollVertically(-n2)) break block2;
                    bl = bl2;
                    break block4;
                }
                bl = bl2;
                break block4;
            }
            bl = false;
        }
        return bl;
    }

    public void cancel() {
        this.mActivePointerId = -1;
        this.clearMotionHistory();
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    public void captureChildView(View object, int n) {
        if (object.getParent() == this.mParentView) {
            this.mCapturedView = object;
            this.mActivePointerId = n;
            this.mCallback.onViewCaptured((View)object, n);
            this.setDragState(1);
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("captureChildView: parameter must be a descendant of the ViewDragHelper's tracked parent view (");
        ((StringBuilder)object).append(this.mParentView);
        ((StringBuilder)object).append(")");
        throw new IllegalArgumentException(((StringBuilder)object).toString());
    }

    public boolean checkTouchSlop(int n) {
        int n2 = this.mInitialMotionX.length;
        for (int i = 0; i < n2; ++i) {
            if (!this.checkTouchSlop(n, i)) continue;
            return true;
        }
        return false;
    }

    public boolean checkTouchSlop(int n, int n2) {
        boolean bl = this.isPointerDown(n2);
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        if (!bl) {
            return false;
        }
        boolean bl5 = (n & 1) == 1;
        n = (n & 2) == 2 ? 1 : 0;
        float f = this.mLastMotionX[n2] - this.mInitialMotionX[n2];
        float f2 = this.mLastMotionY[n2] - this.mInitialMotionY[n2];
        if (bl5 && n != 0) {
            n = this.mTouchSlop;
            bl3 = bl4;
            if (f * f + f2 * f2 > (float)(n * n)) {
                bl3 = true;
            }
            return bl3;
        }
        if (bl5) {
            bl3 = bl2;
            if (Math.abs(f) > (float)this.mTouchSlop) {
                bl3 = true;
            }
            return bl3;
        }
        if (n != 0) {
            if (Math.abs(f2) > (float)this.mTouchSlop) {
                bl3 = true;
            }
            return bl3;
        }
        return false;
    }

    public boolean continueSettling(boolean bl) {
        int n = this.mDragState;
        boolean bl2 = false;
        if (n == 2) {
            boolean bl3 = this.mScroller.computeScrollOffset();
            int n2 = this.mScroller.getCurrX();
            n = this.mScroller.getCurrY();
            int n3 = n2 - this.mCapturedView.getLeft();
            int n4 = n - this.mCapturedView.getTop();
            if (n3 != 0) {
                ViewCompat.offsetLeftAndRight(this.mCapturedView, n3);
            }
            if (n4 != 0) {
                ViewCompat.offsetTopAndBottom(this.mCapturedView, n4);
            }
            if (n3 != 0 || n4 != 0) {
                this.mCallback.onViewPositionChanged(this.mCapturedView, n2, n, n3, n4);
            }
            boolean bl4 = bl3;
            if (bl3) {
                bl4 = bl3;
                if (n2 == this.mScroller.getFinalX()) {
                    bl4 = bl3;
                    if (n == this.mScroller.getFinalY()) {
                        this.mScroller.abortAnimation();
                        bl4 = false;
                    }
                }
            }
            if (!bl4) {
                if (bl) {
                    this.mParentView.post(this.mSetIdleRunnable);
                } else {
                    this.setDragState(0);
                }
            }
        }
        bl = bl2;
        if (this.mDragState == 2) {
            bl = true;
        }
        return bl;
    }

    public View findTopChildUnder(int n, int n2) {
        for (int i = this.mParentView.getChildCount() - 1; i >= 0; --i) {
            View view = this.mParentView.getChildAt(this.mCallback.getOrderedChildIndex(i));
            if (n < view.getLeft() || n >= view.getRight() || n2 < view.getTop() || n2 >= view.getBottom()) continue;
            return view;
        }
        return null;
    }

    public void flingCapturedView(int n, int n2, int n3, int n4) {
        if (this.mReleaseInProgress) {
            this.mScroller.fling(this.mCapturedView.getLeft(), this.mCapturedView.getTop(), (int)this.mVelocityTracker.getXVelocity(this.mActivePointerId), (int)this.mVelocityTracker.getYVelocity(this.mActivePointerId), n, n3, n2, n4);
            this.setDragState(2);
            return;
        }
        throw new IllegalStateException("Cannot flingCapturedView outside of a call to Callback#onViewReleased");
    }

    public int getActivePointerId() {
        return this.mActivePointerId;
    }

    public View getCapturedView() {
        return this.mCapturedView;
    }

    public int getDefaultEdgeSize() {
        return this.mDefaultEdgeSize;
    }

    public int getEdgeSize() {
        return this.mEdgeSize;
    }

    public float getMinVelocity() {
        return this.mMinVelocity;
    }

    public int getTouchSlop() {
        return this.mTouchSlop;
    }

    public int getViewDragState() {
        return this.mDragState;
    }

    public boolean isCapturedViewUnder(int n, int n2) {
        return this.isViewUnder(this.mCapturedView, n, n2);
    }

    public boolean isEdgeTouched(int n) {
        int n2 = this.mInitialEdgesTouched.length;
        for (int i = 0; i < n2; ++i) {
            if (!this.isEdgeTouched(n, i)) continue;
            return true;
        }
        return false;
    }

    public boolean isEdgeTouched(int n, int n2) {
        boolean bl = this.isPointerDown(n2) && (this.mInitialEdgesTouched[n2] & n) != 0;
        return bl;
    }

    public boolean isPointerDown(int n) {
        int n2 = this.mPointersDown;
        boolean bl = true;
        if ((n2 & 1 << n) == 0) {
            bl = false;
        }
        return bl;
    }

    public boolean isViewUnder(View view, int n, int n2) {
        boolean bl;
        block1: {
            bl = false;
            if (view == null) {
                return false;
            }
            if (n < view.getLeft() || n >= view.getRight() || n2 < view.getTop() || n2 >= view.getBottom()) break block1;
            bl = true;
        }
        return bl;
    }

    public void processTouchEvent(MotionEvent motionEvent) {
        int n = motionEvent.getActionMasked();
        int n2 = motionEvent.getActionIndex();
        if (n == 0) {
            this.cancel();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        switch (n) {
            default: {
                break;
            }
            case 6: {
                int n3 = motionEvent.getPointerId(n2);
                if (this.mDragState == 1 && n3 == this.mActivePointerId) {
                    int n4 = -1;
                    int n5 = motionEvent.getPointerCount();
                    n = 0;
                    while (true) {
                        View view;
                        float f;
                        float f2;
                        View view2;
                        n2 = n4;
                        if (n >= n5) break;
                        n2 = motionEvent.getPointerId(n);
                        if (n2 != this.mActivePointerId && (view2 = this.findTopChildUnder((int)(f2 = motionEvent.getX(n)), (int)(f = motionEvent.getY(n)))) == (view = this.mCapturedView) && this.tryCaptureViewForDrag(view, n2)) {
                            n2 = this.mActivePointerId;
                            break;
                        }
                        ++n;
                    }
                    if (n2 == -1) {
                        this.releaseViewForPointerUp();
                    }
                }
                this.clearMotionHistory(n3);
                break;
            }
            case 5: {
                n = motionEvent.getPointerId(n2);
                float f = motionEvent.getX(n2);
                float f3 = motionEvent.getY(n2);
                this.saveInitialMotion(f, f3, n);
                if (this.mDragState == 0) {
                    this.tryCaptureViewForDrag(this.findTopChildUnder((int)f, (int)f3), n);
                    n2 = this.mInitialEdgesTouched[n];
                    int n6 = this.mTrackingEdges;
                    if ((n2 & n6) == 0) break;
                    this.mCallback.onEdgeTouched(n6 & n2, n);
                    break;
                }
                if (!this.isCapturedViewUnder((int)f, (int)f3)) break;
                this.tryCaptureViewForDrag(this.mCapturedView, n);
                break;
            }
            case 3: {
                if (this.mDragState == 1) {
                    this.dispatchViewReleased(0.0f, 0.0f);
                }
                this.cancel();
                break;
            }
            case 2: {
                if (this.mDragState == 1) {
                    if (!this.isValidPointerForActionMove(this.mActivePointerId)) break;
                    n = motionEvent.findPointerIndex(this.mActivePointerId);
                    float f = motionEvent.getX(n);
                    float f4 = motionEvent.getY(n);
                    float[] fArray = this.mLastMotionX;
                    n2 = this.mActivePointerId;
                    n = (int)(f - fArray[n2]);
                    n2 = (int)(f4 - this.mLastMotionY[n2]);
                    this.dragTo(this.mCapturedView.getLeft() + n, this.mCapturedView.getTop() + n2, n, n2);
                    this.saveLastMotion(motionEvent);
                    break;
                }
                n2 = motionEvent.getPointerCount();
                for (n = 0; n < n2; ++n) {
                    View view;
                    int n7 = motionEvent.getPointerId(n);
                    if (!this.isValidPointerForActionMove(n7)) continue;
                    float f = motionEvent.getX(n);
                    float f5 = motionEvent.getY(n);
                    float f6 = f - this.mInitialMotionX[n7];
                    float f7 = f5 - this.mInitialMotionY[n7];
                    this.reportNewEdgeDrags(f6, f7, n7);
                    if (this.mDragState == 1 || this.checkTouchSlop(view = this.findTopChildUnder((int)f, (int)f5), f6, f7) && this.tryCaptureViewForDrag(view, n7)) break;
                }
                this.saveLastMotion(motionEvent);
                break;
            }
            case 1: {
                if (this.mDragState == 1) {
                    this.releaseViewForPointerUp();
                }
                this.cancel();
                break;
            }
            case 0: {
                float f = motionEvent.getX();
                float f8 = motionEvent.getY();
                n2 = motionEvent.getPointerId(0);
                motionEvent = this.findTopChildUnder((int)f, (int)f8);
                this.saveInitialMotion(f, f8, n2);
                this.tryCaptureViewForDrag((View)motionEvent, n2);
                n = this.mInitialEdgesTouched[n2];
                int n8 = this.mTrackingEdges;
                if ((n & n8) == 0) break;
                this.mCallback.onEdgeTouched(n8 & n, n2);
            }
        }
    }

    void setDragState(int n) {
        this.mParentView.removeCallbacks(this.mSetIdleRunnable);
        if (this.mDragState != n) {
            this.mDragState = n;
            this.mCallback.onViewDragStateChanged(n);
            if (this.mDragState == 0) {
                this.mCapturedView = null;
            }
        }
    }

    public void setEdgeSize(int n) {
        this.mEdgeSize = n;
    }

    public void setEdgeTrackingEnabled(int n) {
        this.mTrackingEdges = n;
    }

    public void setMinVelocity(float f) {
        this.mMinVelocity = f;
    }

    public boolean settleCapturedViewAt(int n, int n2) {
        if (this.mReleaseInProgress) {
            return this.forceSettleCapturedViewAt(n, n2, (int)this.mVelocityTracker.getXVelocity(this.mActivePointerId), (int)this.mVelocityTracker.getYVelocity(this.mActivePointerId));
        }
        throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased");
    }

    public boolean shouldInterceptTouchEvent(MotionEvent motionEvent) {
        int n = motionEvent.getActionMasked();
        int n2 = motionEvent.getActionIndex();
        if (n == 0) {
            this.cancel();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        switch (n) {
            default: {
                break;
            }
            case 6: {
                this.clearMotionHistory(motionEvent.getPointerId(n2));
                break;
            }
            case 5: {
                int n3 = motionEvent.getPointerId(n2);
                float f = motionEvent.getX(n2);
                float f2 = motionEvent.getY(n2);
                this.saveInitialMotion(f, f2, n3);
                n2 = this.mDragState;
                if (n2 == 0) {
                    n2 = this.mInitialEdgesTouched[n3];
                    int n4 = this.mTrackingEdges;
                    if ((n2 & n4) == 0) break;
                    this.mCallback.onEdgeTouched(n4 & n2, n3);
                    break;
                }
                if (n2 != 2 || (motionEvent = this.findTopChildUnder((int)f, (int)f2)) != this.mCapturedView) break;
                this.tryCaptureViewForDrag((View)motionEvent, n3);
                break;
            }
            case 2: {
                if (this.mInitialMotionX == null || this.mInitialMotionY == null) break;
                int n5 = motionEvent.getPointerCount();
                for (int i = 0; i < n5; ++i) {
                    int n6 = motionEvent.getPointerId(i);
                    if (!this.isValidPointerForActionMove(n6)) continue;
                    float f = motionEvent.getX(i);
                    float f3 = motionEvent.getY(i);
                    float f4 = f - this.mInitialMotionX[n6];
                    float f5 = f3 - this.mInitialMotionY[n6];
                    View view = this.findTopChildUnder((int)f, (int)f3);
                    boolean bl = view != null && this.checkTouchSlop(view, f4, f5);
                    if (bl) {
                        int n7 = view.getLeft();
                        int n8 = (int)f4;
                        n8 = this.mCallback.clampViewPositionHorizontal(view, n8 + n7, (int)f4);
                        int n9 = view.getTop();
                        int n10 = (int)f5;
                        n10 = this.mCallback.clampViewPositionVertical(view, n10 + n9, (int)f5);
                        int n11 = this.mCallback.getViewHorizontalDragRange(view);
                        int n12 = this.mCallback.getViewVerticalDragRange(view);
                        if ((n11 == 0 || n11 > 0 && n8 == n7) && (n12 == 0 || n12 > 0 && n10 == n9)) break;
                    }
                    this.reportNewEdgeDrags(f4, f5, n6);
                    if (this.mDragState == 1 || bl && this.tryCaptureViewForDrag(view, n6)) break;
                }
                this.saveLastMotion(motionEvent);
                break;
            }
            case 1: 
            case 3: {
                this.cancel();
                break;
            }
            case 0: {
                int n13;
                float f = motionEvent.getX();
                float f6 = motionEvent.getY();
                int n14 = motionEvent.getPointerId(0);
                this.saveInitialMotion(f, f6, n14);
                motionEvent = this.findTopChildUnder((int)f, (int)f6);
                if (motionEvent == this.mCapturedView && this.mDragState == 2) {
                    this.tryCaptureViewForDrag((View)motionEvent, n14);
                }
                if (((n13 = this.mInitialEdgesTouched[n14]) & (n2 = this.mTrackingEdges)) == 0) break;
                this.mCallback.onEdgeTouched(n2 & n13, n14);
            }
        }
        boolean bl = false;
        if (this.mDragState == 1) {
            bl = true;
        }
        return bl;
    }

    public boolean smoothSlideViewTo(View view, int n, int n2) {
        this.mCapturedView = view;
        this.mActivePointerId = -1;
        boolean bl = this.forceSettleCapturedViewAt(n, n2, 0, 0);
        if (!bl && this.mDragState == 0 && this.mCapturedView != null) {
            this.mCapturedView = null;
        }
        return bl;
    }

    boolean tryCaptureViewForDrag(View view, int n) {
        if (view == this.mCapturedView && this.mActivePointerId == n) {
            return true;
        }
        if (view != null && this.mCallback.tryCaptureView(view, n)) {
            this.mActivePointerId = n;
            this.captureChildView(view, n);
            return true;
        }
        return false;
    }

    public static abstract class Callback {
        public int clampViewPositionHorizontal(View view, int n, int n2) {
            return 0;
        }

        public int clampViewPositionVertical(View view, int n, int n2) {
            return 0;
        }

        public int getOrderedChildIndex(int n) {
            return n;
        }

        public int getViewHorizontalDragRange(View view) {
            return 0;
        }

        public int getViewVerticalDragRange(View view) {
            return 0;
        }

        public void onEdgeDragStarted(int n, int n2) {
        }

        public boolean onEdgeLock(int n) {
            return false;
        }

        public void onEdgeTouched(int n, int n2) {
        }

        public void onViewCaptured(View view, int n) {
        }

        public void onViewDragStateChanged(int n) {
        }

        public void onViewPositionChanged(View view, int n, int n2, int n3, int n4) {
        }

        public void onViewReleased(View view, float f, float f2) {
        }

        public abstract boolean tryCaptureView(View var1, int var2);
    }
}

