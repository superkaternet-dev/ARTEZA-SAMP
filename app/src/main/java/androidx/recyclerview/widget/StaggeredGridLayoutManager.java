/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.PointF
 *  android.graphics.Rect
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.accessibility.AccessibilityEvent
 */
package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.LayoutState;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ScrollbarHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

public class StaggeredGridLayoutManager
extends RecyclerView.LayoutManager
implements RecyclerView.SmoothScroller.ScrollVectorProvider {
    static final boolean DEBUG = false;
    @Deprecated
    public static final int GAP_HANDLING_LAZY = 1;
    public static final int GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS = 2;
    public static final int GAP_HANDLING_NONE = 0;
    public static final int HORIZONTAL = 0;
    static final int INVALID_OFFSET = Integer.MIN_VALUE;
    private static final float MAX_SCROLL_FACTOR = 0.33333334f;
    private static final String TAG = "StaggeredGridLManager";
    public static final int VERTICAL = 1;
    private final AnchorInfo mAnchorInfo;
    private final Runnable mCheckForGapsRunnable;
    private int mFullSizeSpec;
    private int mGapStrategy = 2;
    private boolean mLaidOutInvalidFullSpan = false;
    private boolean mLastLayoutFromEnd;
    private boolean mLastLayoutRTL;
    private final LayoutState mLayoutState;
    LazySpanLookup mLazySpanLookup;
    private int mOrientation;
    private SavedState mPendingSavedState;
    int mPendingScrollPosition = -1;
    int mPendingScrollPositionOffset = Integer.MIN_VALUE;
    private int[] mPrefetchDistances;
    OrientationHelper mPrimaryOrientation;
    private BitSet mRemainingSpans;
    boolean mReverseLayout = false;
    OrientationHelper mSecondaryOrientation;
    boolean mShouldReverseLayout = false;
    private int mSizePerSpan;
    private boolean mSmoothScrollbarEnabled = true;
    private int mSpanCount = -1;
    Span[] mSpans;
    private final Rect mTmpRect;

    public StaggeredGridLayoutManager(int n, int n2) {
        this.mLazySpanLookup = new LazySpanLookup();
        this.mTmpRect = new Rect();
        this.mAnchorInfo = new AnchorInfo(this);
        this.mCheckForGapsRunnable = new Runnable(this){
            final StaggeredGridLayoutManager this$0;
            {
                this.this$0 = staggeredGridLayoutManager;
            }

            @Override
            public void run() {
                this.this$0.checkForGaps();
            }
        };
        this.mOrientation = n2;
        this.setSpanCount(n);
        this.mLayoutState = new LayoutState();
        this.createOrientationHelpers();
    }

    public StaggeredGridLayoutManager(Context object, AttributeSet attributeSet, int n, int n2) {
        this.mLazySpanLookup = new LazySpanLookup();
        this.mTmpRect = new Rect();
        this.mAnchorInfo = new AnchorInfo(this);
        this.mCheckForGapsRunnable = new /* invalid duplicate definition of identical inner class */;
        object = StaggeredGridLayoutManager.getProperties(object, attributeSet, n, n2);
        this.setOrientation(object.orientation);
        this.setSpanCount(object.spanCount);
        this.setReverseLayout(object.reverseLayout);
        this.mLayoutState = new LayoutState();
        this.createOrientationHelpers();
    }

    private void appendViewToAllSpans(View view) {
        for (int i = this.mSpanCount - 1; i >= 0; --i) {
            this.mSpans[i].appendToSpan(view);
        }
    }

    private void applyPendingSavedState(AnchorInfo anchorInfo) {
        if (this.mPendingSavedState.mSpanOffsetsSize > 0) {
            if (this.mPendingSavedState.mSpanOffsetsSize == this.mSpanCount) {
                for (int i = 0; i < this.mSpanCount; ++i) {
                    int n;
                    this.mSpans[i].clear();
                    int n2 = n = this.mPendingSavedState.mSpanOffsets[i];
                    if (n != Integer.MIN_VALUE) {
                        n2 = this.mPendingSavedState.mAnchorLayoutFromEnd ? n + this.mPrimaryOrientation.getEndAfterPadding() : n + this.mPrimaryOrientation.getStartAfterPadding();
                    }
                    this.mSpans[i].setLine(n2);
                }
            } else {
                this.mPendingSavedState.invalidateSpanInfo();
                SavedState savedState = this.mPendingSavedState;
                savedState.mAnchorPosition = savedState.mVisibleAnchorPosition;
            }
        }
        this.mLastLayoutRTL = this.mPendingSavedState.mLastLayoutRTL;
        this.setReverseLayout(this.mPendingSavedState.mReverseLayout);
        this.resolveShouldLayoutReverse();
        if (this.mPendingSavedState.mAnchorPosition != -1) {
            this.mPendingScrollPosition = this.mPendingSavedState.mAnchorPosition;
            anchorInfo.mLayoutFromEnd = this.mPendingSavedState.mAnchorLayoutFromEnd;
        } else {
            anchorInfo.mLayoutFromEnd = this.mShouldReverseLayout;
        }
        if (this.mPendingSavedState.mSpanLookupSize > 1) {
            this.mLazySpanLookup.mData = this.mPendingSavedState.mSpanLookup;
            this.mLazySpanLookup.mFullSpanItems = this.mPendingSavedState.mFullSpanItems;
        }
    }

    private void attachViewToSpans(View view, LayoutParams layoutParams, LayoutState layoutState) {
        if (layoutState.mLayoutDirection == 1) {
            if (layoutParams.mFullSpan) {
                this.appendViewToAllSpans(view);
            } else {
                layoutParams.mSpan.appendToSpan(view);
            }
        } else if (layoutParams.mFullSpan) {
            this.prependViewToAllSpans(view);
        } else {
            layoutParams.mSpan.prependToSpan(view);
        }
    }

    private int calculateScrollDirectionForPosition(int n) {
        int n2 = this.getChildCount();
        int n3 = -1;
        if (n2 == 0) {
            if (this.mShouldReverseLayout) {
                n3 = 1;
            }
            return n3;
        }
        boolean bl = n < this.getFirstChildPosition();
        if (bl == this.mShouldReverseLayout) {
            n3 = 1;
        }
        return n3;
    }

    private boolean checkSpanForGap(Span span) {
        if (this.mShouldReverseLayout) {
            if (span.getEndLine() < this.mPrimaryOrientation.getEndAfterPadding()) {
                return span.getLayoutParams((View)span.mViews.get((int)(span.mViews.size() - 1))).mFullSpan ^ true;
            }
        } else if (span.getStartLine() > this.mPrimaryOrientation.getStartAfterPadding()) {
            return span.getLayoutParams((View)span.mViews.get((int)0)).mFullSpan ^ true;
        }
        return false;
    }

    private int computeScrollExtent(RecyclerView.State state) {
        if (this.getChildCount() == 0) {
            return 0;
        }
        return ScrollbarHelper.computeScrollExtent(state, this.mPrimaryOrientation, this.findFirstVisibleItemClosestToStart(this.mSmoothScrollbarEnabled ^ true), this.findFirstVisibleItemClosestToEnd(this.mSmoothScrollbarEnabled ^ true), this, this.mSmoothScrollbarEnabled);
    }

    private int computeScrollOffset(RecyclerView.State state) {
        if (this.getChildCount() == 0) {
            return 0;
        }
        return ScrollbarHelper.computeScrollOffset(state, this.mPrimaryOrientation, this.findFirstVisibleItemClosestToStart(this.mSmoothScrollbarEnabled ^ true), this.findFirstVisibleItemClosestToEnd(this.mSmoothScrollbarEnabled ^ true), this, this.mSmoothScrollbarEnabled, this.mShouldReverseLayout);
    }

    private int computeScrollRange(RecyclerView.State state) {
        if (this.getChildCount() == 0) {
            return 0;
        }
        return ScrollbarHelper.computeScrollRange(state, this.mPrimaryOrientation, this.findFirstVisibleItemClosestToStart(this.mSmoothScrollbarEnabled ^ true), this.findFirstVisibleItemClosestToEnd(this.mSmoothScrollbarEnabled ^ true), this, this.mSmoothScrollbarEnabled);
    }

    private int convertFocusDirectionToLayoutDirection(int n) {
        int n2 = -1;
        int n3 = Integer.MIN_VALUE;
        switch (n) {
            default: {
                return Integer.MIN_VALUE;
            }
            case 130: {
                if (this.mOrientation == 1) {
                    n3 = 1;
                }
                return n3;
            }
            case 66: {
                if (this.mOrientation == 0) {
                    n3 = 1;
                }
                return n3;
            }
            case 33: {
                if (this.mOrientation != 1) {
                    n2 = Integer.MIN_VALUE;
                }
                return n2;
            }
            case 17: {
                if (this.mOrientation != 0) {
                    n2 = Integer.MIN_VALUE;
                }
                return n2;
            }
            case 2: {
                if (this.mOrientation == 1) {
                    return 1;
                }
                if (this.isLayoutRTL()) {
                    return -1;
                }
                return 1;
            }
            case 1: 
        }
        if (this.mOrientation == 1) {
            return -1;
        }
        if (this.isLayoutRTL()) {
            return 1;
        }
        return -1;
    }

    private LazySpanLookup.FullSpanItem createFullSpanItemFromEnd(int n) {
        LazySpanLookup.FullSpanItem fullSpanItem = new LazySpanLookup.FullSpanItem();
        fullSpanItem.mGapPerSpan = new int[this.mSpanCount];
        for (int i = 0; i < this.mSpanCount; ++i) {
            fullSpanItem.mGapPerSpan[i] = n - this.mSpans[i].getEndLine(n);
        }
        return fullSpanItem;
    }

    private LazySpanLookup.FullSpanItem createFullSpanItemFromStart(int n) {
        LazySpanLookup.FullSpanItem fullSpanItem = new LazySpanLookup.FullSpanItem();
        fullSpanItem.mGapPerSpan = new int[this.mSpanCount];
        for (int i = 0; i < this.mSpanCount; ++i) {
            fullSpanItem.mGapPerSpan[i] = this.mSpans[i].getStartLine(n) - n;
        }
        return fullSpanItem;
    }

    private void createOrientationHelpers() {
        this.mPrimaryOrientation = OrientationHelper.createOrientationHelper(this, this.mOrientation);
        this.mSecondaryOrientation = OrientationHelper.createOrientationHelper(this, 1 - this.mOrientation);
    }

    private int fill(RecyclerView.Recycler recycler, LayoutState layoutState, RecyclerView.State state) {
        this.mRemainingSpans.set(0, this.mSpanCount, true);
        int n = this.mLayoutState.mInfinite ? (layoutState.mLayoutDirection == 1 ? Integer.MAX_VALUE : Integer.MIN_VALUE) : (layoutState.mLayoutDirection == 1 ? layoutState.mEndLine + layoutState.mAvailable : layoutState.mStartLine - layoutState.mAvailable);
        this.updateAllRemainingSpans(layoutState.mLayoutDirection, n);
        int n2 = this.mShouldReverseLayout ? this.mPrimaryOrientation.getEndAfterPadding() : this.mPrimaryOrientation.getStartAfterPadding();
        int n3 = 0;
        while (layoutState.hasMore(state) && (this.mLayoutState.mInfinite || !this.mRemainingSpans.isEmpty())) {
            LazySpanLookup.FullSpanItem fullSpanItem;
            int n4;
            int n5;
            int n6;
            Span span;
            View view = layoutState.next(recycler);
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            int n7 = layoutParams.getViewLayoutPosition();
            n3 = this.mLazySpanLookup.getSpan(n7);
            int n8 = n3 == -1 ? 1 : 0;
            if (n8 != 0) {
                span = layoutParams.mFullSpan ? this.mSpans[0] : this.getNextSpan(layoutState);
                this.mLazySpanLookup.setSpan(n7, span);
            } else {
                span = this.mSpans[n3];
            }
            layoutParams.mSpan = span;
            if (layoutState.mLayoutDirection == 1) {
                this.addView(view);
            } else {
                this.addView(view, 0);
            }
            this.measureChildWithDecorationsAndMargin(view, layoutParams, false);
            if (layoutState.mLayoutDirection == 1) {
                n3 = layoutParams.mFullSpan ? this.getMaxEnd(n2) : span.getEndLine(n2);
                n6 = this.mPrimaryOrientation.getDecoratedMeasurement(view) + n3;
                n5 = n3;
                n4 = n6;
                if (n8 != 0) {
                    n5 = n3;
                    n4 = n6;
                    if (layoutParams.mFullSpan) {
                        fullSpanItem = this.createFullSpanItemFromEnd(n3);
                        fullSpanItem.mGapDir = -1;
                        fullSpanItem.mPosition = n7;
                        this.mLazySpanLookup.addFullSpanItem(fullSpanItem);
                        n5 = n3;
                        n4 = n6;
                    }
                }
            } else {
                n3 = layoutParams.mFullSpan ? this.getMinStart(n2) : span.getStartLine(n2);
                n5 = n6 = n3 - this.mPrimaryOrientation.getDecoratedMeasurement(view);
                n4 = n3;
                if (n8 != 0) {
                    n5 = n6;
                    n4 = n3;
                    if (layoutParams.mFullSpan) {
                        fullSpanItem = this.createFullSpanItemFromStart(n3);
                        fullSpanItem.mGapDir = 1;
                        fullSpanItem.mPosition = n7;
                        this.mLazySpanLookup.addFullSpanItem(fullSpanItem);
                        n4 = n3;
                        n5 = n6;
                    }
                }
            }
            if (layoutParams.mFullSpan && layoutState.mItemDirection == -1) {
                if (n8 != 0) {
                    this.mLaidOutInvalidFullSpan = true;
                } else {
                    n3 = layoutState.mLayoutDirection == 1 ? this.areAllEndsEqual() ^ 1 : this.areAllStartsEqual() ^ 1;
                    if (n3 != 0) {
                        fullSpanItem = this.mLazySpanLookup.getFullSpanItem(n7);
                        if (fullSpanItem != null) {
                            fullSpanItem.mHasUnwantedGapAfter = true;
                        }
                        this.mLaidOutInvalidFullSpan = true;
                    }
                }
            }
            this.attachViewToSpans(view, layoutParams, layoutState);
            if (this.isLayoutRTL() && this.mOrientation == 1) {
                n3 = layoutParams.mFullSpan ? this.mSecondaryOrientation.getEndAfterPadding() : this.mSecondaryOrientation.getEndAfterPadding() - (this.mSpanCount - 1 - span.mIndex) * this.mSizePerSpan;
                n6 = this.mSecondaryOrientation.getDecoratedMeasurement(view);
                n8 = n3;
                n3 -= n6;
            } else {
                n3 = layoutParams.mFullSpan ? this.mSecondaryOrientation.getStartAfterPadding() : span.mIndex * this.mSizePerSpan + this.mSecondaryOrientation.getStartAfterPadding();
                n8 = this.mSecondaryOrientation.getDecoratedMeasurement(view) + n3;
            }
            if (this.mOrientation == 1) {
                this.layoutDecoratedWithMargins(view, n3, n5, n8, n4);
            } else {
                this.layoutDecoratedWithMargins(view, n5, n3, n4, n8);
            }
            if (layoutParams.mFullSpan) {
                this.updateAllRemainingSpans(this.mLayoutState.mLayoutDirection, n);
            } else {
                this.updateRemainingSpans(span, this.mLayoutState.mLayoutDirection, n);
            }
            this.recycle(recycler, this.mLayoutState);
            if (this.mLayoutState.mStopInFocusable && view.hasFocusable()) {
                if (layoutParams.mFullSpan) {
                    this.mRemainingSpans.clear();
                } else {
                    this.mRemainingSpans.set(span.mIndex, false);
                }
            }
            n3 = 1;
        }
        if (n3 == 0) {
            this.recycle(recycler, this.mLayoutState);
        }
        if (this.mLayoutState.mLayoutDirection == -1) {
            n = this.getMinStart(this.mPrimaryOrientation.getStartAfterPadding());
            n = this.mPrimaryOrientation.getStartAfterPadding() - n;
        } else {
            n = this.getMaxEnd(this.mPrimaryOrientation.getEndAfterPadding()) - this.mPrimaryOrientation.getEndAfterPadding();
        }
        n = n > 0 ? Math.min(layoutState.mAvailable, n) : 0;
        return n;
    }

    private int findFirstReferenceChildPosition(int n) {
        int n2 = this.getChildCount();
        for (int i = 0; i < n2; ++i) {
            int n3 = this.getPosition(this.getChildAt(i));
            if (n3 < 0 || n3 >= n) continue;
            return n3;
        }
        return 0;
    }

    private int findLastReferenceChildPosition(int n) {
        for (int i = this.getChildCount() - 1; i >= 0; --i) {
            int n2 = this.getPosition(this.getChildAt(i));
            if (n2 < 0 || n2 >= n) continue;
            return n2;
        }
        return 0;
    }

    private void fixEndGap(RecyclerView.Recycler recycler, RecyclerView.State state, boolean bl) {
        int n = this.getMaxEnd(Integer.MIN_VALUE);
        if (n == Integer.MIN_VALUE) {
            return;
        }
        n = this.mPrimaryOrientation.getEndAfterPadding() - n;
        if (n > 0) {
            n -= -this.scrollBy(-n, recycler, state);
            if (bl && n > 0) {
                this.mPrimaryOrientation.offsetChildren(n);
            }
            return;
        }
    }

    private void fixStartGap(RecyclerView.Recycler recycler, RecyclerView.State state, boolean bl) {
        int n = this.getMinStart(Integer.MAX_VALUE);
        if (n == Integer.MAX_VALUE) {
            return;
        }
        if ((n -= this.mPrimaryOrientation.getStartAfterPadding()) > 0) {
            n -= this.scrollBy(n, recycler, state);
            if (bl && n > 0) {
                this.mPrimaryOrientation.offsetChildren(-n);
            }
            return;
        }
    }

    private int getMaxEnd(int n) {
        int n2 = this.mSpans[0].getEndLine(n);
        for (int i = 1; i < this.mSpanCount; ++i) {
            int n3 = this.mSpans[i].getEndLine(n);
            int n4 = n2;
            if (n3 > n2) {
                n4 = n3;
            }
            n2 = n4;
        }
        return n2;
    }

    private int getMaxStart(int n) {
        int n2 = this.mSpans[0].getStartLine(n);
        for (int i = 1; i < this.mSpanCount; ++i) {
            int n3 = this.mSpans[i].getStartLine(n);
            int n4 = n2;
            if (n3 > n2) {
                n4 = n3;
            }
            n2 = n4;
        }
        return n2;
    }

    private int getMinEnd(int n) {
        int n2 = this.mSpans[0].getEndLine(n);
        for (int i = 1; i < this.mSpanCount; ++i) {
            int n3 = this.mSpans[i].getEndLine(n);
            int n4 = n2;
            if (n3 < n2) {
                n4 = n3;
            }
            n2 = n4;
        }
        return n2;
    }

    private int getMinStart(int n) {
        int n2 = this.mSpans[0].getStartLine(n);
        for (int i = 1; i < this.mSpanCount; ++i) {
            int n3 = this.mSpans[i].getStartLine(n);
            int n4 = n2;
            if (n3 < n2) {
                n4 = n3;
            }
            n2 = n4;
        }
        return n2;
    }

    private Span getNextSpan(LayoutState object) {
        int n;
        int n2;
        int n3;
        if (this.preferLastSpan(((LayoutState)object).mLayoutDirection)) {
            n3 = this.mSpanCount - 1;
            n2 = -1;
            n = -1;
        } else {
            n3 = 0;
            n2 = this.mSpanCount;
            n = 1;
        }
        if (((LayoutState)object).mLayoutDirection == 1) {
            object = null;
            int n4 = Integer.MAX_VALUE;
            int n5 = this.mPrimaryOrientation.getStartAfterPadding();
            while (n3 != n2) {
                Span span = this.mSpans[n3];
                int n6 = span.getEndLine(n5);
                int n7 = n4;
                if (n6 < n4) {
                    object = span;
                    n7 = n6;
                }
                n3 += n;
                n4 = n7;
            }
            return object;
        }
        object = null;
        int n8 = Integer.MIN_VALUE;
        int n9 = this.mPrimaryOrientation.getEndAfterPadding();
        while (n3 != n2) {
            Span span = this.mSpans[n3];
            int n10 = span.getStartLine(n9);
            int n11 = n8;
            if (n10 > n8) {
                object = span;
                n11 = n10;
            }
            n3 += n;
            n8 = n11;
        }
        return object;
    }

    private void handleUpdate(int n, int n2, int n3) {
        int n4;
        int n5;
        int n6 = this.mShouldReverseLayout ? this.getLastChildPosition() : this.getFirstChildPosition();
        if (n3 == 8) {
            if (n < n2) {
                n5 = n2 + 1;
                n4 = n;
            } else {
                n5 = n + 1;
                n4 = n2;
            }
        } else {
            n4 = n;
            n5 = n + n2;
        }
        this.mLazySpanLookup.invalidateAfter(n4);
        switch (n3) {
            default: {
                break;
            }
            case 8: {
                this.mLazySpanLookup.offsetForRemoval(n, 1);
                this.mLazySpanLookup.offsetForAddition(n2, 1);
                break;
            }
            case 2: {
                this.mLazySpanLookup.offsetForRemoval(n, n2);
                break;
            }
            case 1: {
                this.mLazySpanLookup.offsetForAddition(n, n2);
            }
        }
        if (n5 <= n6) {
            return;
        }
        n = this.mShouldReverseLayout ? this.getFirstChildPosition() : this.getLastChildPosition();
        if (n4 <= n) {
            this.requestLayout();
        }
    }

    private void measureChildWithDecorationsAndMargin(View view, int n, int n2, boolean bl) {
        this.calculateItemDecorationsForChild(view, this.mTmpRect);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        n = this.updateSpecWithExtra(n, layoutParams.leftMargin + this.mTmpRect.left, layoutParams.rightMargin + this.mTmpRect.right);
        n2 = this.updateSpecWithExtra(n2, layoutParams.topMargin + this.mTmpRect.top, layoutParams.bottomMargin + this.mTmpRect.bottom);
        bl = bl ? this.shouldReMeasureChild(view, n, n2, layoutParams) : this.shouldMeasureChild(view, n, n2, layoutParams);
        if (bl) {
            view.measure(n, n2);
        }
    }

    private void measureChildWithDecorationsAndMargin(View view, LayoutParams layoutParams, boolean bl) {
        if (layoutParams.mFullSpan) {
            if (this.mOrientation == 1) {
                this.measureChildWithDecorationsAndMargin(view, this.mFullSizeSpec, StaggeredGridLayoutManager.getChildMeasureSpec(this.getHeight(), this.getHeightMode(), this.getPaddingTop() + this.getPaddingBottom(), layoutParams.height, true), bl);
            } else {
                this.measureChildWithDecorationsAndMargin(view, StaggeredGridLayoutManager.getChildMeasureSpec(this.getWidth(), this.getWidthMode(), this.getPaddingLeft() + this.getPaddingRight(), layoutParams.width, true), this.mFullSizeSpec, bl);
            }
        } else if (this.mOrientation == 1) {
            this.measureChildWithDecorationsAndMargin(view, StaggeredGridLayoutManager.getChildMeasureSpec(this.mSizePerSpan, this.getWidthMode(), 0, layoutParams.width, false), StaggeredGridLayoutManager.getChildMeasureSpec(this.getHeight(), this.getHeightMode(), this.getPaddingTop() + this.getPaddingBottom(), layoutParams.height, true), bl);
        } else {
            this.measureChildWithDecorationsAndMargin(view, StaggeredGridLayoutManager.getChildMeasureSpec(this.getWidth(), this.getWidthMode(), this.getPaddingLeft() + this.getPaddingRight(), layoutParams.width, true), StaggeredGridLayoutManager.getChildMeasureSpec(this.mSizePerSpan, this.getHeightMode(), 0, layoutParams.height, false), bl);
        }
    }

    private void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state, boolean bl) {
        int n;
        Object object;
        AnchorInfo anchorInfo = this.mAnchorInfo;
        if ((this.mPendingSavedState != null || this.mPendingScrollPosition != -1) && state.getItemCount() == 0) {
            this.removeAndRecycleAllViews(recycler);
            anchorInfo.reset();
            return;
        }
        boolean bl2 = anchorInfo.mValid;
        boolean bl3 = true;
        int n2 = bl2 && this.mPendingScrollPosition == -1 && this.mPendingSavedState == null ? 0 : 1;
        if (n2 != 0) {
            anchorInfo.reset();
            if (this.mPendingSavedState != null) {
                this.applyPendingSavedState(anchorInfo);
            } else {
                this.resolveShouldLayoutReverse();
                anchorInfo.mLayoutFromEnd = this.mShouldReverseLayout;
            }
            this.updateAnchorInfoForLayout(state, anchorInfo);
            anchorInfo.mValid = true;
        }
        if (this.mPendingSavedState == null && this.mPendingScrollPosition == -1 && (anchorInfo.mLayoutFromEnd != this.mLastLayoutFromEnd || this.isLayoutRTL() != this.mLastLayoutRTL)) {
            this.mLazySpanLookup.clear();
            anchorInfo.mInvalidateOffsets = true;
        }
        if (this.getChildCount() > 0 && ((object = this.mPendingSavedState) == null || ((SavedState)object).mSpanOffsetsSize < 1)) {
            if (anchorInfo.mInvalidateOffsets) {
                for (n2 = 0; n2 < this.mSpanCount; ++n2) {
                    this.mSpans[n2].clear();
                    if (anchorInfo.mOffset == Integer.MIN_VALUE) continue;
                    this.mSpans[n2].setLine(anchorInfo.mOffset);
                }
            } else if (n2 == 0 && this.mAnchorInfo.mSpanReferenceLines != null) {
                for (n2 = 0; n2 < this.mSpanCount; ++n2) {
                    object = this.mSpans[n2];
                    ((Span)object).clear();
                    ((Span)object).setLine(this.mAnchorInfo.mSpanReferenceLines[n2]);
                }
            } else {
                for (n2 = 0; n2 < this.mSpanCount; ++n2) {
                    this.mSpans[n2].cacheReferenceLineAndClear(this.mShouldReverseLayout, anchorInfo.mOffset);
                }
                this.mAnchorInfo.saveSpanReferenceLines(this.mSpans);
            }
        }
        this.detachAndScrapAttachedViews(recycler);
        this.mLayoutState.mRecycle = false;
        this.mLaidOutInvalidFullSpan = false;
        this.updateMeasureSpecs(this.mSecondaryOrientation.getTotalSpace());
        this.updateLayoutState(anchorInfo.mPosition, state);
        if (anchorInfo.mLayoutFromEnd) {
            this.setLayoutStateDirection(-1);
            this.fill(recycler, this.mLayoutState, state);
            this.setLayoutStateDirection(1);
            this.mLayoutState.mCurrentPosition = anchorInfo.mPosition + this.mLayoutState.mItemDirection;
            this.fill(recycler, this.mLayoutState, state);
        } else {
            this.setLayoutStateDirection(1);
            this.fill(recycler, this.mLayoutState, state);
            this.setLayoutStateDirection(-1);
            this.mLayoutState.mCurrentPosition = anchorInfo.mPosition + this.mLayoutState.mItemDirection;
            this.fill(recycler, this.mLayoutState, state);
        }
        this.repositionToWrapContentIfNecessary();
        if (this.getChildCount() > 0) {
            if (this.mShouldReverseLayout) {
                this.fixEndGap(recycler, state, true);
                this.fixStartGap(recycler, state, false);
            } else {
                this.fixStartGap(recycler, state, true);
                this.fixEndGap(recycler, state, false);
            }
        }
        n2 = n = 0;
        if (bl) {
            n2 = n;
            if (!state.isPreLayout()) {
                if (this.mGapStrategy == 0 || this.getChildCount() <= 0 || !this.mLaidOutInvalidFullSpan && this.hasGapsToFix() == null) {
                    bl3 = false;
                }
                n2 = n;
                if (bl3) {
                    this.removeCallbacks(this.mCheckForGapsRunnable);
                    n2 = n;
                    if (this.checkForGaps()) {
                        n2 = 1;
                    }
                }
            }
        }
        if (state.isPreLayout()) {
            this.mAnchorInfo.reset();
        }
        this.mLastLayoutFromEnd = anchorInfo.mLayoutFromEnd;
        this.mLastLayoutRTL = this.isLayoutRTL();
        if (n2 != 0) {
            this.mAnchorInfo.reset();
            this.onLayoutChildren(recycler, state, false);
        }
    }

    private boolean preferLastSpan(int n) {
        int n2 = this.mOrientation;
        boolean bl = true;
        boolean bl2 = true;
        if (n2 == 0) {
            boolean bl3 = n == -1;
            bl3 = bl3 != this.mShouldReverseLayout ? bl2 : false;
            return bl3;
        }
        boolean bl4 = n == -1;
        bl4 = bl4 == this.mShouldReverseLayout;
        bl4 = bl4 == this.isLayoutRTL() ? bl : false;
        return bl4;
    }

    private void prependViewToAllSpans(View view) {
        for (int i = this.mSpanCount - 1; i >= 0; --i) {
            this.mSpans[i].prependToSpan(view);
        }
    }

    private void recycle(RecyclerView.Recycler recycler, LayoutState layoutState) {
        if (layoutState.mRecycle && !layoutState.mInfinite) {
            if (layoutState.mAvailable == 0) {
                if (layoutState.mLayoutDirection == -1) {
                    this.recycleFromEnd(recycler, layoutState.mEndLine);
                } else {
                    this.recycleFromStart(recycler, layoutState.mStartLine);
                }
            } else if (layoutState.mLayoutDirection == -1) {
                int n = layoutState.mStartLine - this.getMaxStart(layoutState.mStartLine);
                n = n < 0 ? layoutState.mEndLine : layoutState.mEndLine - Math.min(n, layoutState.mAvailable);
                this.recycleFromEnd(recycler, n);
            } else {
                int n = this.getMinEnd(layoutState.mEndLine) - layoutState.mEndLine;
                n = n < 0 ? layoutState.mStartLine : layoutState.mStartLine + Math.min(n, layoutState.mAvailable);
                this.recycleFromStart(recycler, n);
            }
            return;
        }
    }

    private void recycleFromEnd(RecyclerView.Recycler recycler, int n) {
        for (int i = this.getChildCount() - 1; i >= 0; --i) {
            View view = this.getChildAt(i);
            if (this.mPrimaryOrientation.getDecoratedStart(view) >= n && this.mPrimaryOrientation.getTransformedStartWithDecoration(view) >= n) {
                LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                if (layoutParams.mFullSpan) {
                    int n2;
                    for (n2 = 0; n2 < this.mSpanCount; ++n2) {
                        if (this.mSpans[n2].mViews.size() != 1) continue;
                        return;
                    }
                    for (n2 = 0; n2 < this.mSpanCount; ++n2) {
                        this.mSpans[n2].popEnd();
                    }
                } else {
                    if (layoutParams.mSpan.mViews.size() == 1) {
                        return;
                    }
                    layoutParams.mSpan.popEnd();
                }
                this.removeAndRecycleView(view, recycler);
                continue;
            }
            return;
        }
    }

    private void recycleFromStart(RecyclerView.Recycler recycler, int n) {
        while (this.getChildCount() > 0) {
            View view = this.getChildAt(0);
            if (this.mPrimaryOrientation.getDecoratedEnd(view) <= n && this.mPrimaryOrientation.getTransformedEndWithDecoration(view) <= n) {
                LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                if (layoutParams.mFullSpan) {
                    int n2;
                    for (n2 = 0; n2 < this.mSpanCount; ++n2) {
                        if (this.mSpans[n2].mViews.size() != 1) continue;
                        return;
                    }
                    for (n2 = 0; n2 < this.mSpanCount; ++n2) {
                        this.mSpans[n2].popStart();
                    }
                } else {
                    if (layoutParams.mSpan.mViews.size() == 1) {
                        return;
                    }
                    layoutParams.mSpan.popStart();
                }
                this.removeAndRecycleView(view, recycler);
                continue;
            }
            return;
        }
    }

    private void repositionToWrapContentIfNecessary() {
        int n;
        Object object;
        int n2;
        if (this.mSecondaryOrientation.getMode() == 0x40000000) {
            return;
        }
        float f = 0.0f;
        int n3 = this.getChildCount();
        for (n2 = 0; n2 < n3; ++n2) {
            object = this.getChildAt(n2);
            float f2 = this.mSecondaryOrientation.getDecoratedMeasurement((View)object);
            if (f2 < f) continue;
            float f3 = f2;
            if (((LayoutParams)object.getLayoutParams()).isFullSpan()) {
                f3 = 1.0f * f2 / (float)this.mSpanCount;
            }
            f = Math.max(f, f3);
        }
        int n4 = this.mSizePerSpan;
        n2 = n = Math.round((float)this.mSpanCount * f);
        if (this.mSecondaryOrientation.getMode() == Integer.MIN_VALUE) {
            n2 = Math.min(n, this.mSecondaryOrientation.getTotalSpace());
        }
        this.updateMeasureSpecs(n2);
        if (this.mSizePerSpan == n4) {
            return;
        }
        for (n2 = 0; n2 < n3; ++n2) {
            View view = this.getChildAt(n2);
            object = (LayoutParams)view.getLayoutParams();
            if (object.mFullSpan) continue;
            if (this.isLayoutRTL() && this.mOrientation == 1) {
                view.offsetLeftAndRight(-(this.mSpanCount - 1 - object.mSpan.mIndex) * this.mSizePerSpan - -(this.mSpanCount - 1 - object.mSpan.mIndex) * n4);
                continue;
            }
            n = object.mSpan.mIndex * this.mSizePerSpan;
            int n5 = object.mSpan.mIndex * n4;
            if (this.mOrientation == 1) {
                view.offsetLeftAndRight(n - n5);
                continue;
            }
            view.offsetTopAndBottom(n - n5);
        }
    }

    private void resolveShouldLayoutReverse() {
        this.mShouldReverseLayout = this.mOrientation != 1 && this.isLayoutRTL() ? this.mReverseLayout ^ true : this.mReverseLayout;
    }

    private void setLayoutStateDirection(int n) {
        this.mLayoutState.mLayoutDirection = n;
        LayoutState layoutState = this.mLayoutState;
        boolean bl = this.mShouldReverseLayout;
        int n2 = 1;
        boolean bl2 = n == -1;
        n = bl == bl2 ? n2 : -1;
        layoutState.mItemDirection = n;
    }

    private void updateAllRemainingSpans(int n, int n2) {
        for (int i = 0; i < this.mSpanCount; ++i) {
            if (this.mSpans[i].mViews.isEmpty()) continue;
            this.updateRemainingSpans(this.mSpans[i], n, n2);
        }
    }

    private boolean updateAnchorFromChildren(RecyclerView.State state, AnchorInfo anchorInfo) {
        int n = this.mLastLayoutFromEnd ? this.findLastReferenceChildPosition(state.getItemCount()) : this.findFirstReferenceChildPosition(state.getItemCount());
        anchorInfo.mPosition = n;
        anchorInfo.mOffset = Integer.MIN_VALUE;
        return true;
    }

    private void updateLayoutState(int n, RecyclerView.State object) {
        boolean bl;
        LayoutState layoutState = this.mLayoutState;
        boolean bl2 = false;
        layoutState.mAvailable = 0;
        this.mLayoutState.mCurrentPosition = n;
        int n2 = 0;
        int n3 = 0;
        int n4 = n2;
        int n5 = n3;
        if (this.isSmoothScrolling()) {
            int n6 = ((RecyclerView.State)object).getTargetScrollPosition();
            n4 = n2;
            n5 = n3;
            if (n6 != -1) {
                boolean bl3 = this.mShouldReverseLayout;
                bl = n6 < n;
                if (bl3 == bl) {
                    n5 = this.mPrimaryOrientation.getTotalSpace();
                    n4 = n2;
                } else {
                    n4 = this.mPrimaryOrientation.getTotalSpace();
                    n5 = n3;
                }
            }
        }
        if (this.getClipToPadding()) {
            this.mLayoutState.mStartLine = this.mPrimaryOrientation.getStartAfterPadding() - n4;
            this.mLayoutState.mEndLine = this.mPrimaryOrientation.getEndAfterPadding() + n5;
        } else {
            this.mLayoutState.mEndLine = this.mPrimaryOrientation.getEnd() + n5;
            this.mLayoutState.mStartLine = -n4;
        }
        this.mLayoutState.mStopInFocusable = false;
        this.mLayoutState.mRecycle = true;
        object = this.mLayoutState;
        bl = this.mPrimaryOrientation.getMode() == 0 && this.mPrimaryOrientation.getEnd() == 0 ? true : bl2;
        ((LayoutState)object).mInfinite = bl;
    }

    private void updateRemainingSpans(Span span, int n, int n2) {
        int n3 = span.getDeletedSize();
        if (n == -1) {
            if (span.getStartLine() + n3 <= n2) {
                this.mRemainingSpans.set(span.mIndex, false);
            }
        } else if (span.getEndLine() - n3 >= n2) {
            this.mRemainingSpans.set(span.mIndex, false);
        }
    }

    private int updateSpecWithExtra(int n, int n2, int n3) {
        if (n2 == 0 && n3 == 0) {
            return n;
        }
        int n4 = View.MeasureSpec.getMode((int)n);
        if (n4 != Integer.MIN_VALUE && n4 != 0x40000000) {
            return n;
        }
        return View.MeasureSpec.makeMeasureSpec((int)Math.max(0, View.MeasureSpec.getSize((int)n) - n2 - n3), (int)n4);
    }

    boolean areAllEndsEqual() {
        int n = this.mSpans[0].getEndLine(Integer.MIN_VALUE);
        for (int i = 1; i < this.mSpanCount; ++i) {
            if (this.mSpans[i].getEndLine(Integer.MIN_VALUE) == n) continue;
            return false;
        }
        return true;
    }

    boolean areAllStartsEqual() {
        int n = this.mSpans[0].getStartLine(Integer.MIN_VALUE);
        for (int i = 1; i < this.mSpanCount; ++i) {
            if (this.mSpans[i].getStartLine(Integer.MIN_VALUE) == n) continue;
            return false;
        }
        return true;
    }

    @Override
    public void assertNotInLayoutOrScroll(String string2) {
        if (this.mPendingSavedState == null) {
            super.assertNotInLayoutOrScroll(string2);
        }
    }

    @Override
    public boolean canScrollHorizontally() {
        boolean bl = this.mOrientation == 0;
        return bl;
    }

    @Override
    public boolean canScrollVertically() {
        int n = this.mOrientation;
        boolean bl = true;
        if (n != 1) {
            bl = false;
        }
        return bl;
    }

    boolean checkForGaps() {
        if (this.getChildCount() != 0 && this.mGapStrategy != 0 && this.isAttachedToWindow()) {
            int n;
            int n2;
            if (this.mShouldReverseLayout) {
                n2 = this.getLastChildPosition();
                n = this.getFirstChildPosition();
            } else {
                n2 = this.getFirstChildPosition();
                n = this.getLastChildPosition();
            }
            if (n2 == 0 && this.hasGapsToFix() != null) {
                this.mLazySpanLookup.clear();
                this.requestSimpleAnimationsInNextLayout();
                this.requestLayout();
                return true;
            }
            if (!this.mLaidOutInvalidFullSpan) {
                return false;
            }
            int n3 = this.mShouldReverseLayout ? -1 : 1;
            LazySpanLookup.FullSpanItem fullSpanItem = this.mLazySpanLookup.getFirstFullSpanItemInRange(n2, n + 1, n3, true);
            if (fullSpanItem == null) {
                this.mLaidOutInvalidFullSpan = false;
                this.mLazySpanLookup.forceInvalidateAfter(n + 1);
                return false;
            }
            LazySpanLookup.FullSpanItem fullSpanItem2 = this.mLazySpanLookup.getFirstFullSpanItemInRange(n2, fullSpanItem.mPosition, n3 * -1, true);
            if (fullSpanItem2 == null) {
                this.mLazySpanLookup.forceInvalidateAfter(fullSpanItem.mPosition);
            } else {
                this.mLazySpanLookup.forceInvalidateAfter(fullSpanItem2.mPosition + 1);
            }
            this.requestSimpleAnimationsInNextLayout();
            this.requestLayout();
            return true;
        }
        return false;
    }

    @Override
    public boolean checkLayoutParams(RecyclerView.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    @Override
    public void collectAdjacentPrefetchPositions(int n, int n2, RecyclerView.State state, RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry) {
        if (this.mOrientation != 0) {
            n = n2;
        }
        if (this.getChildCount() != 0 && n != 0) {
            this.prepareLayoutStateForDelta(n, state);
            Object object = this.mPrefetchDistances;
            if (object == null || ((int[])object).length < this.mSpanCount) {
                this.mPrefetchDistances = new int[this.mSpanCount];
            }
            n = 0;
            for (n2 = 0; n2 < this.mSpanCount; ++n2) {
                int n3 = this.mLayoutState.mItemDirection == -1 ? this.mLayoutState.mStartLine - this.mSpans[n2].getStartLine(this.mLayoutState.mStartLine) : this.mSpans[n2].getEndLine(this.mLayoutState.mEndLine) - this.mLayoutState.mEndLine;
                int n4 = n;
                if (n3 >= 0) {
                    this.mPrefetchDistances[n] = n3;
                    n4 = n + 1;
                }
                n = n4;
            }
            Arrays.sort(this.mPrefetchDistances, 0, n);
            for (n2 = 0; n2 < n && this.mLayoutState.hasMore(state); ++n2) {
                layoutPrefetchRegistry.addPosition(this.mLayoutState.mCurrentPosition, this.mPrefetchDistances[n2]);
                object = this.mLayoutState;
                object.mCurrentPosition += this.mLayoutState.mItemDirection;
            }
            return;
        }
    }

    @Override
    public int computeHorizontalScrollExtent(RecyclerView.State state) {
        return this.computeScrollExtent(state);
    }

    @Override
    public int computeHorizontalScrollOffset(RecyclerView.State state) {
        return this.computeScrollOffset(state);
    }

    @Override
    public int computeHorizontalScrollRange(RecyclerView.State state) {
        return this.computeScrollRange(state);
    }

    @Override
    public PointF computeScrollVectorForPosition(int n) {
        n = this.calculateScrollDirectionForPosition(n);
        PointF pointF = new PointF();
        if (n == 0) {
            return null;
        }
        if (this.mOrientation == 0) {
            pointF.x = n;
            pointF.y = 0.0f;
        } else {
            pointF.x = 0.0f;
            pointF.y = n;
        }
        return pointF;
    }

    @Override
    public int computeVerticalScrollExtent(RecyclerView.State state) {
        return this.computeScrollExtent(state);
    }

    @Override
    public int computeVerticalScrollOffset(RecyclerView.State state) {
        return this.computeScrollOffset(state);
    }

    @Override
    public int computeVerticalScrollRange(RecyclerView.State state) {
        return this.computeScrollRange(state);
    }

    public int[] findFirstCompletelyVisibleItemPositions(int[] object) {
        block5: {
            block4: {
                block3: {
                    if (object != null) break block3;
                    object = new int[this.mSpanCount];
                    break block4;
                }
                if (((int[])object).length < this.mSpanCount) break block5;
            }
            for (int i = 0; i < this.mSpanCount; ++i) {
                object[i] = this.mSpans[i].findFirstCompletelyVisibleItemPosition();
            }
            return object;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Provided int[]'s size must be more than or equal to span count. Expected:");
        stringBuilder.append(this.mSpanCount);
        stringBuilder.append(", array size:");
        stringBuilder.append(((int[])object).length);
        object = new IllegalArgumentException(stringBuilder.toString());
        throw object;
    }

    View findFirstVisibleItemClosestToEnd(boolean bl) {
        int n = this.mPrimaryOrientation.getStartAfterPadding();
        int n2 = this.mPrimaryOrientation.getEndAfterPadding();
        View view = null;
        for (int i = this.getChildCount() - 1; i >= 0; --i) {
            View view2 = this.getChildAt(i);
            int n3 = this.mPrimaryOrientation.getDecoratedStart(view2);
            int n4 = this.mPrimaryOrientation.getDecoratedEnd(view2);
            View view3 = view;
            if (n4 > n) {
                if (n3 >= n2) {
                    view3 = view;
                } else if (n4 > n2 && bl) {
                    view3 = view;
                    if (view == null) {
                        view3 = view2;
                    }
                } else {
                    return view2;
                }
            }
            view = view3;
        }
        return view;
    }

    View findFirstVisibleItemClosestToStart(boolean bl) {
        int n = this.mPrimaryOrientation.getStartAfterPadding();
        int n2 = this.mPrimaryOrientation.getEndAfterPadding();
        int n3 = this.getChildCount();
        View view = null;
        for (int i = 0; i < n3; ++i) {
            View view2 = this.getChildAt(i);
            int n4 = this.mPrimaryOrientation.getDecoratedStart(view2);
            View view3 = view;
            if (this.mPrimaryOrientation.getDecoratedEnd(view2) > n) {
                if (n4 >= n2) {
                    view3 = view;
                } else if (n4 < n && bl) {
                    view3 = view;
                    if (view == null) {
                        view3 = view2;
                    }
                } else {
                    return view2;
                }
            }
            view = view3;
        }
        return view;
    }

    int findFirstVisibleItemPositionInt() {
        View view = this.mShouldReverseLayout ? this.findFirstVisibleItemClosestToEnd(true) : this.findFirstVisibleItemClosestToStart(true);
        int n = view == null ? -1 : this.getPosition(view);
        return n;
    }

    public int[] findFirstVisibleItemPositions(int[] object) {
        block5: {
            block4: {
                block3: {
                    if (object != null) break block3;
                    object = new int[this.mSpanCount];
                    break block4;
                }
                if (((int[])object).length < this.mSpanCount) break block5;
            }
            for (int i = 0; i < this.mSpanCount; ++i) {
                object[i] = this.mSpans[i].findFirstVisibleItemPosition();
            }
            return object;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Provided int[]'s size must be more than or equal to span count. Expected:");
        stringBuilder.append(this.mSpanCount);
        stringBuilder.append(", array size:");
        stringBuilder.append(((int[])object).length);
        object = new IllegalArgumentException(stringBuilder.toString());
        throw object;
    }

    public int[] findLastCompletelyVisibleItemPositions(int[] object) {
        block5: {
            block4: {
                block3: {
                    if (object != null) break block3;
                    object = new int[this.mSpanCount];
                    break block4;
                }
                if (((int[])object).length < this.mSpanCount) break block5;
            }
            for (int i = 0; i < this.mSpanCount; ++i) {
                object[i] = this.mSpans[i].findLastCompletelyVisibleItemPosition();
            }
            return object;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Provided int[]'s size must be more than or equal to span count. Expected:");
        stringBuilder.append(this.mSpanCount);
        stringBuilder.append(", array size:");
        stringBuilder.append(((int[])object).length);
        object = new IllegalArgumentException(stringBuilder.toString());
        throw object;
    }

    public int[] findLastVisibleItemPositions(int[] object) {
        block5: {
            block4: {
                block3: {
                    if (object != null) break block3;
                    object = new int[this.mSpanCount];
                    break block4;
                }
                if (((int[])object).length < this.mSpanCount) break block5;
            }
            for (int i = 0; i < this.mSpanCount; ++i) {
                object[i] = this.mSpans[i].findLastVisibleItemPosition();
            }
            return object;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Provided int[]'s size must be more than or equal to span count. Expected:");
        stringBuilder.append(this.mSpanCount);
        stringBuilder.append(", array size:");
        stringBuilder.append(((int[])object).length);
        object = new IllegalArgumentException(stringBuilder.toString());
        throw object;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        if (this.mOrientation == 0) {
            return new LayoutParams(-2, -1);
        }
        return new LayoutParams(-1, -2);
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(Context context, AttributeSet attributeSet) {
        return new LayoutParams(context, attributeSet);
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams)layoutParams);
        }
        return new LayoutParams(layoutParams);
    }

    @Override
    public int getColumnCountForAccessibility(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mOrientation == 1) {
            return this.mSpanCount;
        }
        return super.getColumnCountForAccessibility(recycler, state);
    }

    int getFirstChildPosition() {
        int n = this.getChildCount();
        int n2 = 0;
        if (n != 0) {
            n2 = this.getPosition(this.getChildAt(0));
        }
        return n2;
    }

    public int getGapStrategy() {
        return this.mGapStrategy;
    }

    int getLastChildPosition() {
        int n = this.getChildCount();
        n = n == 0 ? 0 : this.getPosition(this.getChildAt(n - 1));
        return n;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public boolean getReverseLayout() {
        return this.mReverseLayout;
    }

    @Override
    public int getRowCountForAccessibility(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mOrientation == 0) {
            return this.mSpanCount;
        }
        return super.getRowCountForAccessibility(recycler, state);
    }

    public int getSpanCount() {
        return this.mSpanCount;
    }

    View hasGapsToFix() {
        int n;
        int n2;
        int n3 = this.getChildCount() - 1;
        BitSet bitSet = new BitSet(this.mSpanCount);
        bitSet.set(0, this.mSpanCount, true);
        int n4 = this.mOrientation;
        int n5 = -1;
        n4 = n4 == 1 && this.isLayoutRTL() ? 1 : -1;
        if (this.mShouldReverseLayout) {
            n2 = 0 - 1;
        } else {
            n = 0;
            n2 = n3 + 1;
            n3 = n;
        }
        if (n3 < n2) {
            n5 = 1;
        }
        for (n = n3; n != n2; n += n5) {
            int n6;
            View view = this.getChildAt(n);
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (bitSet.get(layoutParams.mSpan.mIndex)) {
                if (this.checkSpanForGap(layoutParams.mSpan)) {
                    return view;
                }
                bitSet.clear(layoutParams.mSpan.mIndex);
            }
            if (layoutParams.mFullSpan || n + n5 == n2) continue;
            Object object = this.getChildAt(n + n5);
            int n7 = 0;
            n3 = 0;
            if (this.mShouldReverseLayout) {
                n6 = this.mPrimaryOrientation.getDecoratedEnd(view);
                if (n6 < (n7 = this.mPrimaryOrientation.getDecoratedEnd((View)object))) {
                    return view;
                }
                if (n6 == n7) {
                    n3 = 1;
                }
            } else {
                int n8;
                n6 = this.mPrimaryOrientation.getDecoratedStart(view);
                if (n6 > (n8 = this.mPrimaryOrientation.getDecoratedStart((View)object))) {
                    return view;
                }
                n3 = n7;
                if (n6 == n8) {
                    n3 = 1;
                }
            }
            if (n3 == 0) continue;
            object = (LayoutParams)object.getLayoutParams();
            n3 = layoutParams.mSpan.mIndex - object.mSpan.mIndex < 0 ? 1 : 0;
            if (n3 == (n7 = n4 < 0 ? 1 : 0)) continue;
            return view;
        }
        return null;
    }

    public void invalidateSpanAssignments() {
        this.mLazySpanLookup.clear();
        this.requestLayout();
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        boolean bl = this.mGapStrategy != 0;
        return bl;
    }

    boolean isLayoutRTL() {
        int n = this.getLayoutDirection();
        boolean bl = true;
        if (n != 1) {
            bl = false;
        }
        return bl;
    }

    @Override
    public void offsetChildrenHorizontal(int n) {
        super.offsetChildrenHorizontal(n);
        for (int i = 0; i < this.mSpanCount; ++i) {
            this.mSpans[i].onOffset(n);
        }
    }

    @Override
    public void offsetChildrenVertical(int n) {
        super.offsetChildrenVertical(n);
        for (int i = 0; i < this.mSpanCount; ++i) {
            this.mSpans[i].onOffset(n);
        }
    }

    @Override
    public void onDetachedFromWindow(RecyclerView recyclerView, RecyclerView.Recycler recycler) {
        super.onDetachedFromWindow(recyclerView, recycler);
        this.removeCallbacks(this.mCheckForGapsRunnable);
        for (int i = 0; i < this.mSpanCount; ++i) {
            this.mSpans[i].clear();
        }
        recyclerView.requestLayout();
    }

    @Override
    public View onFocusSearchFailed(View view, int n, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int n2;
        if (this.getChildCount() == 0) {
            return null;
        }
        if ((view = this.findContainingItemView(view)) == null) {
            return null;
        }
        this.resolveShouldLayoutReverse();
        int n3 = this.convertFocusDirectionToLayoutDirection(n);
        if (n3 == Integer.MIN_VALUE) {
            return null;
        }
        Object object = (LayoutParams)view.getLayoutParams();
        boolean bl = ((LayoutParams)((Object)object)).mFullSpan;
        object = ((LayoutParams)((Object)object)).mSpan;
        n = n3 == 1 ? this.getLastChildPosition() : this.getFirstChildPosition();
        this.updateLayoutState(n, state);
        this.setLayoutStateDirection(n3);
        LayoutState layoutState = this.mLayoutState;
        layoutState.mCurrentPosition = layoutState.mItemDirection + n;
        this.mLayoutState.mAvailable = (int)((float)this.mPrimaryOrientation.getTotalSpace() * 0.33333334f);
        this.mLayoutState.mStopInFocusable = true;
        layoutState = this.mLayoutState;
        int n4 = 0;
        layoutState.mRecycle = false;
        this.fill(recycler, this.mLayoutState, state);
        this.mLastLayoutFromEnd = this.mShouldReverseLayout;
        if (!bl && (recycler = ((Span)object).getFocusableViewAfter(n, n3)) != null && recycler != view) {
            return recycler;
        }
        if (this.preferLastSpan(n3)) {
            for (n2 = this.mSpanCount - 1; n2 >= 0; --n2) {
                recycler = this.mSpans[n2].getFocusableViewAfter(n, n3);
                if (recycler == null || recycler == view) continue;
                return recycler;
            }
        } else {
            for (n2 = 0; n2 < this.mSpanCount; ++n2) {
                recycler = this.mSpans[n2].getFocusableViewAfter(n, n3);
                if (recycler == null || recycler == view) continue;
                return recycler;
            }
        }
        int n5 = this.mReverseLayout;
        n2 = n3 == -1 ? 1 : 0;
        n = n4;
        if ((n5 ^ 1) == n2) {
            n = 1;
        }
        if (!bl && (recycler = this.findViewByPosition(n2 = n != 0 ? ((Span)object).findFirstPartiallyVisibleItemPosition() : ((Span)object).findLastPartiallyVisibleItemPosition())) != null && recycler != view) {
            return recycler;
        }
        if (this.preferLastSpan(n3)) {
            for (n2 = this.mSpanCount - 1; n2 >= 0; --n2) {
                if (n2 == ((Span)object).mIndex || (recycler = this.findViewByPosition(n4 = n != 0 ? this.mSpans[n2].findFirstPartiallyVisibleItemPosition() : this.mSpans[n2].findLastPartiallyVisibleItemPosition())) == null || recycler == view) continue;
                return recycler;
            }
        } else {
            for (n2 = 0; n2 < this.mSpanCount; ++n2) {
                n4 = n != 0 ? this.mSpans[n2].findFirstPartiallyVisibleItemPosition() : this.mSpans[n2].findLastPartiallyVisibleItemPosition();
                recycler = this.findViewByPosition(n4);
                if (recycler == null || recycler == view) continue;
                return recycler;
            }
        }
        return null;
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        if (this.getChildCount() > 0) {
            View view = this.findFirstVisibleItemClosestToStart(false);
            View view2 = this.findFirstVisibleItemClosestToEnd(false);
            if (view != null && view2 != null) {
                int n;
                int n2 = this.getPosition(view);
                if (n2 < (n = this.getPosition(view2))) {
                    accessibilityEvent.setFromIndex(n2);
                    accessibilityEvent.setToIndex(n);
                } else {
                    accessibilityEvent.setFromIndex(n);
                    accessibilityEvent.setToIndex(n2);
                }
            } else {
                return;
            }
        }
    }

    @Override
    public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler object, RecyclerView.State state, View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        object = view.getLayoutParams();
        if (!(object instanceof LayoutParams)) {
            super.onInitializeAccessibilityNodeInfoForItem(view, accessibilityNodeInfoCompat);
            return;
        }
        object = (LayoutParams)((Object)object);
        if (this.mOrientation == 0) {
            int n = ((LayoutParams)((Object)object)).getSpanIndex();
            int n2 = ((LayoutParams)((Object)object)).mFullSpan ? this.mSpanCount : 1;
            accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(n, n2, -1, -1, ((LayoutParams)((Object)object)).mFullSpan, false));
        } else {
            int n = ((LayoutParams)((Object)object)).getSpanIndex();
            int n3 = ((LayoutParams)((Object)object)).mFullSpan ? this.mSpanCount : 1;
            accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(-1, -1, n, n3, ((LayoutParams)((Object)object)).mFullSpan, false));
        }
    }

    @Override
    public void onItemsAdded(RecyclerView recyclerView, int n, int n2) {
        this.handleUpdate(n, n2, 1);
    }

    @Override
    public void onItemsChanged(RecyclerView recyclerView) {
        this.mLazySpanLookup.clear();
        this.requestLayout();
    }

    @Override
    public void onItemsMoved(RecyclerView recyclerView, int n, int n2, int n3) {
        this.handleUpdate(n, n2, 8);
    }

    @Override
    public void onItemsRemoved(RecyclerView recyclerView, int n, int n2) {
        this.handleUpdate(n, n2, 2);
    }

    @Override
    public void onItemsUpdated(RecyclerView recyclerView, int n, int n2, Object object) {
        this.handleUpdate(n, n2, 4);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.onLayoutChildren(recycler, state, true);
    }

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mPendingSavedState = null;
        this.mAnchorInfo.reset();
    }

    @Override
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof SavedState) {
            this.mPendingSavedState = (SavedState)parcelable;
            this.requestLayout();
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        if (this.mPendingSavedState != null) {
            return new SavedState(this.mPendingSavedState);
        }
        SavedState savedState = new SavedState();
        savedState.mReverseLayout = this.mReverseLayout;
        savedState.mAnchorLayoutFromEnd = this.mLastLayoutFromEnd;
        savedState.mLastLayoutRTL = this.mLastLayoutRTL;
        LazySpanLookup lazySpanLookup = this.mLazySpanLookup;
        if (lazySpanLookup != null && lazySpanLookup.mData != null) {
            savedState.mSpanLookup = this.mLazySpanLookup.mData;
            savedState.mSpanLookupSize = savedState.mSpanLookup.length;
            savedState.mFullSpanItems = this.mLazySpanLookup.mFullSpanItems;
        } else {
            savedState.mSpanLookupSize = 0;
        }
        if (this.getChildCount() > 0) {
            int n = this.mLastLayoutFromEnd ? this.getLastChildPosition() : this.getFirstChildPosition();
            savedState.mAnchorPosition = n;
            savedState.mVisibleAnchorPosition = this.findFirstVisibleItemPositionInt();
            savedState.mSpanOffsetsSize = this.mSpanCount;
            savedState.mSpanOffsets = new int[this.mSpanCount];
            for (int i = 0; i < this.mSpanCount; ++i) {
                int n2;
                if (this.mLastLayoutFromEnd) {
                    n = n2 = this.mSpans[i].getEndLine(Integer.MIN_VALUE);
                    if (n2 != Integer.MIN_VALUE) {
                        n = n2 - this.mPrimaryOrientation.getEndAfterPadding();
                    }
                } else {
                    n = n2 = this.mSpans[i].getStartLine(Integer.MIN_VALUE);
                    if (n2 != Integer.MIN_VALUE) {
                        n = n2 - this.mPrimaryOrientation.getStartAfterPadding();
                    }
                }
                savedState.mSpanOffsets[i] = n;
            }
        } else {
            savedState.mAnchorPosition = -1;
            savedState.mVisibleAnchorPosition = -1;
            savedState.mSpanOffsetsSize = 0;
        }
        return savedState;
    }

    @Override
    public void onScrollStateChanged(int n) {
        if (n == 0) {
            this.checkForGaps();
        }
    }

    void prepareLayoutStateForDelta(int n, RecyclerView.State object) {
        int n2;
        int n3;
        if (n > 0) {
            n3 = 1;
            n2 = this.getLastChildPosition();
        } else {
            n3 = -1;
            n2 = this.getFirstChildPosition();
        }
        this.mLayoutState.mRecycle = true;
        this.updateLayoutState(n2, (RecyclerView.State)object);
        this.setLayoutStateDirection(n3);
        object = this.mLayoutState;
        ((LayoutState)object).mCurrentPosition = ((LayoutState)object).mItemDirection + n2;
        this.mLayoutState.mAvailable = Math.abs(n);
    }

    int scrollBy(int n, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.getChildCount() != 0 && n != 0) {
            this.prepareLayoutStateForDelta(n, state);
            int n2 = this.fill(recycler, this.mLayoutState, state);
            if (this.mLayoutState.mAvailable >= n2) {
                n = n < 0 ? -n2 : n2;
            }
            this.mPrimaryOrientation.offsetChildren(-n);
            this.mLastLayoutFromEnd = this.mShouldReverseLayout;
            this.mLayoutState.mAvailable = 0;
            this.recycle(recycler, this.mLayoutState);
            return n;
        }
        return 0;
    }

    @Override
    public int scrollHorizontallyBy(int n, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return this.scrollBy(n, recycler, state);
    }

    @Override
    public void scrollToPosition(int n) {
        SavedState savedState = this.mPendingSavedState;
        if (savedState != null && savedState.mAnchorPosition != n) {
            this.mPendingSavedState.invalidateAnchorPositionInfo();
        }
        this.mPendingScrollPosition = n;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.requestLayout();
    }

    public void scrollToPositionWithOffset(int n, int n2) {
        SavedState savedState = this.mPendingSavedState;
        if (savedState != null) {
            savedState.invalidateAnchorPositionInfo();
        }
        this.mPendingScrollPosition = n;
        this.mPendingScrollPositionOffset = n2;
        this.requestLayout();
    }

    @Override
    public int scrollVerticallyBy(int n, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return this.scrollBy(n, recycler, state);
    }

    public void setGapStrategy(int n) {
        this.assertNotInLayoutOrScroll(null);
        if (n == this.mGapStrategy) {
            return;
        }
        if (n != 0 && n != 2) {
            throw new IllegalArgumentException("invalid gap strategy. Must be GAP_HANDLING_NONE or GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS");
        }
        this.mGapStrategy = n;
        this.requestLayout();
    }

    @Override
    public void setMeasuredDimension(Rect rect, int n, int n2) {
        int n3 = this.getPaddingLeft() + this.getPaddingRight();
        int n4 = this.getPaddingTop() + this.getPaddingBottom();
        if (this.mOrientation == 1) {
            n2 = StaggeredGridLayoutManager.chooseSize(n2, rect.height() + n4, this.getMinimumHeight());
            n = StaggeredGridLayoutManager.chooseSize(n, this.mSizePerSpan * this.mSpanCount + n3, this.getMinimumWidth());
        } else {
            n = StaggeredGridLayoutManager.chooseSize(n, rect.width() + n3, this.getMinimumWidth());
            n2 = StaggeredGridLayoutManager.chooseSize(n2, this.mSizePerSpan * this.mSpanCount + n4, this.getMinimumHeight());
        }
        this.setMeasuredDimension(n, n2);
    }

    public void setOrientation(int n) {
        if (n != 0 && n != 1) {
            throw new IllegalArgumentException("invalid orientation.");
        }
        this.assertNotInLayoutOrScroll(null);
        if (n == this.mOrientation) {
            return;
        }
        this.mOrientation = n;
        OrientationHelper orientationHelper = this.mPrimaryOrientation;
        this.mPrimaryOrientation = this.mSecondaryOrientation;
        this.mSecondaryOrientation = orientationHelper;
        this.requestLayout();
    }

    public void setReverseLayout(boolean bl) {
        this.assertNotInLayoutOrScroll(null);
        SavedState savedState = this.mPendingSavedState;
        if (savedState != null && savedState.mReverseLayout != bl) {
            this.mPendingSavedState.mReverseLayout = bl;
        }
        this.mReverseLayout = bl;
        this.requestLayout();
    }

    public void setSpanCount(int n) {
        this.assertNotInLayoutOrScroll(null);
        if (n != this.mSpanCount) {
            this.invalidateSpanAssignments();
            this.mSpanCount = n;
            this.mRemainingSpans = new BitSet(this.mSpanCount);
            this.mSpans = new Span[this.mSpanCount];
            for (n = 0; n < this.mSpanCount; ++n) {
                this.mSpans[n] = new Span(this, n);
            }
            this.requestLayout();
        }
    }

    @Override
    public void smoothScrollToPosition(RecyclerView object, RecyclerView.State state, int n) {
        object = new LinearSmoothScroller(object.getContext());
        ((RecyclerView.SmoothScroller)object).setTargetPosition(n);
        this.startSmoothScroll((RecyclerView.SmoothScroller)object);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        boolean bl = this.mPendingSavedState == null;
        return bl;
    }

    boolean updateAnchorFromPendingData(RecyclerView.State object, AnchorInfo anchorInfo) {
        int n;
        boolean bl = ((RecyclerView.State)object).isPreLayout();
        boolean bl2 = false;
        if (!bl && (n = this.mPendingScrollPosition) != -1) {
            if (n >= 0 && n < ((RecyclerView.State)object).getItemCount()) {
                object = this.mPendingSavedState;
                if (object != null && ((SavedState)object).mAnchorPosition != -1 && this.mPendingSavedState.mSpanOffsetsSize >= 1) {
                    anchorInfo.mOffset = Integer.MIN_VALUE;
                    anchorInfo.mPosition = this.mPendingScrollPosition;
                } else {
                    object = this.findViewByPosition(this.mPendingScrollPosition);
                    if (object != null) {
                        n = this.mShouldReverseLayout ? this.getLastChildPosition() : this.getFirstChildPosition();
                        anchorInfo.mPosition = n;
                        if (this.mPendingScrollPositionOffset != Integer.MIN_VALUE) {
                            anchorInfo.mOffset = anchorInfo.mLayoutFromEnd ? this.mPrimaryOrientation.getEndAfterPadding() - this.mPendingScrollPositionOffset - this.mPrimaryOrientation.getDecoratedEnd((View)object) : this.mPrimaryOrientation.getStartAfterPadding() + this.mPendingScrollPositionOffset - this.mPrimaryOrientation.getDecoratedStart((View)object);
                            return true;
                        }
                        if (this.mPrimaryOrientation.getDecoratedMeasurement((View)object) > this.mPrimaryOrientation.getTotalSpace()) {
                            n = anchorInfo.mLayoutFromEnd ? this.mPrimaryOrientation.getEndAfterPadding() : this.mPrimaryOrientation.getStartAfterPadding();
                            anchorInfo.mOffset = n;
                            return true;
                        }
                        n = this.mPrimaryOrientation.getDecoratedStart((View)object) - this.mPrimaryOrientation.getStartAfterPadding();
                        if (n < 0) {
                            anchorInfo.mOffset = -n;
                            return true;
                        }
                        n = this.mPrimaryOrientation.getEndAfterPadding() - this.mPrimaryOrientation.getDecoratedEnd((View)object);
                        if (n < 0) {
                            anchorInfo.mOffset = n;
                            return true;
                        }
                        anchorInfo.mOffset = Integer.MIN_VALUE;
                    } else {
                        anchorInfo.mPosition = this.mPendingScrollPosition;
                        n = this.mPendingScrollPositionOffset;
                        if (n == Integer.MIN_VALUE) {
                            if (this.calculateScrollDirectionForPosition(anchorInfo.mPosition) == 1) {
                                bl2 = true;
                            }
                            anchorInfo.mLayoutFromEnd = bl2;
                            anchorInfo.assignCoordinateFromPadding();
                        } else {
                            anchorInfo.assignCoordinateFromPadding(n);
                        }
                        anchorInfo.mInvalidateOffsets = true;
                    }
                }
                return true;
            }
            this.mPendingScrollPosition = -1;
            this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
            return false;
        }
        return false;
    }

    void updateAnchorInfoForLayout(RecyclerView.State state, AnchorInfo anchorInfo) {
        if (this.updateAnchorFromPendingData(state, anchorInfo)) {
            return;
        }
        if (this.updateAnchorFromChildren(state, anchorInfo)) {
            return;
        }
        anchorInfo.assignCoordinateFromPadding();
        anchorInfo.mPosition = 0;
    }

    void updateMeasureSpecs(int n) {
        this.mSizePerSpan = n / this.mSpanCount;
        this.mFullSizeSpec = View.MeasureSpec.makeMeasureSpec((int)n, (int)this.mSecondaryOrientation.getMode());
    }

    class AnchorInfo {
        boolean mInvalidateOffsets;
        boolean mLayoutFromEnd;
        int mOffset;
        int mPosition;
        int[] mSpanReferenceLines;
        boolean mValid;
        final StaggeredGridLayoutManager this$0;

        AnchorInfo(StaggeredGridLayoutManager staggeredGridLayoutManager) {
            this.this$0 = staggeredGridLayoutManager;
            this.reset();
        }

        void assignCoordinateFromPadding() {
            int n = this.mLayoutFromEnd ? this.this$0.mPrimaryOrientation.getEndAfterPadding() : this.this$0.mPrimaryOrientation.getStartAfterPadding();
            this.mOffset = n;
        }

        void assignCoordinateFromPadding(int n) {
            this.mOffset = this.mLayoutFromEnd ? this.this$0.mPrimaryOrientation.getEndAfterPadding() - n : this.this$0.mPrimaryOrientation.getStartAfterPadding() + n;
        }

        void reset() {
            this.mPosition = -1;
            this.mOffset = Integer.MIN_VALUE;
            this.mLayoutFromEnd = false;
            this.mInvalidateOffsets = false;
            this.mValid = false;
            int[] nArray = this.mSpanReferenceLines;
            if (nArray != null) {
                Arrays.fill(nArray, -1);
            }
        }

        void saveSpanReferenceLines(Span[] spanArray) {
            int n = spanArray.length;
            int[] nArray = this.mSpanReferenceLines;
            if (nArray == null || nArray.length < n) {
                this.mSpanReferenceLines = new int[this.this$0.mSpans.length];
            }
            for (int i = 0; i < n; ++i) {
                this.mSpanReferenceLines[i] = spanArray[i].getStartLine(Integer.MIN_VALUE);
            }
        }
    }

    public static class LayoutParams
    extends RecyclerView.LayoutParams {
        public static final int INVALID_SPAN_ID = -1;
        boolean mFullSpan;
        Span mSpan;

        public LayoutParams(int n, int n2) {
            super(n, n2);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }

        public LayoutParams(RecyclerView.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public final int getSpanIndex() {
            Span span = this.mSpan;
            if (span == null) {
                return -1;
            }
            return span.mIndex;
        }

        public boolean isFullSpan() {
            return this.mFullSpan;
        }

        public void setFullSpan(boolean bl) {
            this.mFullSpan = bl;
        }
    }

    static class LazySpanLookup {
        private static final int MIN_SIZE = 10;
        int[] mData;
        List<FullSpanItem> mFullSpanItems;

        LazySpanLookup() {
        }

        private int invalidateFullSpansAfter(int n) {
            int n2;
            if (this.mFullSpanItems == null) {
                return -1;
            }
            FullSpanItem fullSpanItem = this.getFullSpanItem(n);
            if (fullSpanItem != null) {
                this.mFullSpanItems.remove(fullSpanItem);
            }
            int n3 = -1;
            int n4 = this.mFullSpanItems.size();
            int n5 = 0;
            while (true) {
                n2 = n3;
                if (n5 >= n4) break;
                if (this.mFullSpanItems.get((int)n5).mPosition >= n) {
                    n2 = n5;
                    break;
                }
                ++n5;
            }
            if (n2 != -1) {
                fullSpanItem = this.mFullSpanItems.get(n2);
                this.mFullSpanItems.remove(n2);
                return fullSpanItem.mPosition;
            }
            return -1;
        }

        private void offsetFullSpansForAddition(int n, int n2) {
            List<FullSpanItem> list = this.mFullSpanItems;
            if (list == null) {
                return;
            }
            for (int i = list.size() - 1; i >= 0; --i) {
                list = this.mFullSpanItems.get(i);
                if (((FullSpanItem)((Object)list)).mPosition < n) continue;
                ((FullSpanItem)((Object)list)).mPosition += n2;
            }
        }

        private void offsetFullSpansForRemoval(int n, int n2) {
            List<FullSpanItem> list = this.mFullSpanItems;
            if (list == null) {
                return;
            }
            for (int i = list.size() - 1; i >= 0; --i) {
                list = this.mFullSpanItems.get(i);
                if (((FullSpanItem)((Object)list)).mPosition < n) continue;
                if (((FullSpanItem)((Object)list)).mPosition < n + n2) {
                    this.mFullSpanItems.remove(i);
                    continue;
                }
                ((FullSpanItem)((Object)list)).mPosition -= n2;
            }
        }

        public void addFullSpanItem(FullSpanItem fullSpanItem) {
            if (this.mFullSpanItems == null) {
                this.mFullSpanItems = new ArrayList<FullSpanItem>();
            }
            int n = this.mFullSpanItems.size();
            for (int i = 0; i < n; ++i) {
                FullSpanItem fullSpanItem2 = this.mFullSpanItems.get(i);
                if (fullSpanItem2.mPosition == fullSpanItem.mPosition) {
                    this.mFullSpanItems.remove(i);
                }
                if (fullSpanItem2.mPosition < fullSpanItem.mPosition) continue;
                this.mFullSpanItems.add(i, fullSpanItem);
                return;
            }
            this.mFullSpanItems.add(fullSpanItem);
        }

        void clear() {
            int[] nArray = this.mData;
            if (nArray != null) {
                Arrays.fill(nArray, -1);
            }
            this.mFullSpanItems = null;
        }

        void ensureSize(int n) {
            int[] nArray = this.mData;
            if (nArray == null) {
                this.mData = nArray = new int[Math.max(n, 10) + 1];
                Arrays.fill(nArray, -1);
            } else if (n >= nArray.length) {
                nArray = this.mData;
                int[] nArray2 = new int[this.sizeForPosition(n)];
                this.mData = nArray2;
                System.arraycopy(nArray, 0, nArray2, 0, nArray.length);
                nArray2 = this.mData;
                Arrays.fill(nArray2, nArray.length, nArray2.length, -1);
            }
        }

        int forceInvalidateAfter(int n) {
            List<FullSpanItem> list = this.mFullSpanItems;
            if (list != null) {
                for (int i = list.size() - 1; i >= 0; --i) {
                    if (this.mFullSpanItems.get((int)i).mPosition < n) continue;
                    this.mFullSpanItems.remove(i);
                }
            }
            return this.invalidateAfter(n);
        }

        public FullSpanItem getFirstFullSpanItemInRange(int n, int n2, int n3, boolean bl) {
            List<FullSpanItem> list = this.mFullSpanItems;
            if (list == null) {
                return null;
            }
            int n4 = list.size();
            for (int i = 0; i < n4; ++i) {
                list = this.mFullSpanItems.get(i);
                if (((FullSpanItem)((Object)list)).mPosition >= n2) {
                    return null;
                }
                if (((FullSpanItem)((Object)list)).mPosition < n || n3 != 0 && ((FullSpanItem)((Object)list)).mGapDir != n3 && (!bl || !((FullSpanItem)((Object)list)).mHasUnwantedGapAfter)) continue;
                return list;
            }
            return null;
        }

        public FullSpanItem getFullSpanItem(int n) {
            List<FullSpanItem> list = this.mFullSpanItems;
            if (list == null) {
                return null;
            }
            for (int i = list.size() - 1; i >= 0; --i) {
                list = this.mFullSpanItems.get(i);
                if (((FullSpanItem)((Object)list)).mPosition != n) continue;
                return list;
            }
            return null;
        }

        int getSpan(int n) {
            int[] nArray = this.mData;
            if (nArray != null && n < nArray.length) {
                return nArray[n];
            }
            return -1;
        }

        int invalidateAfter(int n) {
            int[] nArray = this.mData;
            if (nArray == null) {
                return -1;
            }
            if (n >= nArray.length) {
                return -1;
            }
            int n2 = this.invalidateFullSpansAfter(n);
            if (n2 == -1) {
                nArray = this.mData;
                Arrays.fill(nArray, n, nArray.length, -1);
                return this.mData.length;
            }
            Arrays.fill(this.mData, n, n2 + 1, -1);
            return n2 + 1;
        }

        void offsetForAddition(int n, int n2) {
            int[] nArray = this.mData;
            if (nArray != null && n < nArray.length) {
                this.ensureSize(n + n2);
                nArray = this.mData;
                System.arraycopy(nArray, n, nArray, n + n2, nArray.length - n - n2);
                Arrays.fill(this.mData, n, n + n2, -1);
                this.offsetFullSpansForAddition(n, n2);
                return;
            }
        }

        void offsetForRemoval(int n, int n2) {
            int[] nArray = this.mData;
            if (nArray != null && n < nArray.length) {
                this.ensureSize(n + n2);
                nArray = this.mData;
                System.arraycopy(nArray, n + n2, nArray, n, nArray.length - n - n2);
                nArray = this.mData;
                Arrays.fill(nArray, nArray.length - n2, nArray.length, -1);
                this.offsetFullSpansForRemoval(n, n2);
                return;
            }
        }

        void setSpan(int n, Span span) {
            this.ensureSize(n);
            this.mData[n] = span.mIndex;
        }

        int sizeForPosition(int n) {
            int n2;
            for (n2 = this.mData.length; n2 <= n; n2 *= 2) {
            }
            return n2;
        }

        static class FullSpanItem
        implements Parcelable {
            public static final Parcelable.Creator<FullSpanItem> CREATOR = new Parcelable.Creator<FullSpanItem>(){

                public FullSpanItem createFromParcel(Parcel parcel) {
                    return new FullSpanItem(parcel);
                }

                public FullSpanItem[] newArray(int n) {
                    return new FullSpanItem[n];
                }
            };
            int mGapDir;
            int[] mGapPerSpan;
            boolean mHasUnwantedGapAfter;
            int mPosition;

            FullSpanItem() {
            }

            FullSpanItem(Parcel parcel) {
                this.mPosition = parcel.readInt();
                this.mGapDir = parcel.readInt();
                int n = parcel.readInt();
                boolean bl = true;
                if (n != 1) {
                    bl = false;
                }
                this.mHasUnwantedGapAfter = bl;
                n = parcel.readInt();
                if (n > 0) {
                    int[] nArray = new int[n];
                    this.mGapPerSpan = nArray;
                    parcel.readIntArray(nArray);
                }
            }

            public int describeContents() {
                return 0;
            }

            int getGapForSpan(int n) {
                int[] nArray = this.mGapPerSpan;
                n = nArray == null ? 0 : nArray[n];
                return n;
            }

            public String toString() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("FullSpanItem{mPosition=");
                stringBuilder.append(this.mPosition);
                stringBuilder.append(", mGapDir=");
                stringBuilder.append(this.mGapDir);
                stringBuilder.append(", mHasUnwantedGapAfter=");
                stringBuilder.append(this.mHasUnwantedGapAfter);
                stringBuilder.append(", mGapPerSpan=");
                stringBuilder.append(Arrays.toString(this.mGapPerSpan));
                stringBuilder.append('}');
                return stringBuilder.toString();
            }

            public void writeToParcel(Parcel parcel, int n) {
                parcel.writeInt(this.mPosition);
                parcel.writeInt(this.mGapDir);
                parcel.writeInt(this.mHasUnwantedGapAfter ? 1 : 0);
                int[] nArray = this.mGapPerSpan;
                if (nArray != null && nArray.length > 0) {
                    parcel.writeInt(nArray.length);
                    parcel.writeIntArray(this.mGapPerSpan);
                } else {
                    parcel.writeInt(0);
                }
            }
        }
    }

    public static class SavedState
    implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        boolean mAnchorLayoutFromEnd;
        int mAnchorPosition;
        List<LazySpanLookup.FullSpanItem> mFullSpanItems;
        boolean mLastLayoutRTL;
        boolean mReverseLayout;
        int[] mSpanLookup;
        int mSpanLookupSize;
        int[] mSpanOffsets;
        int mSpanOffsetsSize;
        int mVisibleAnchorPosition;

        public SavedState() {
        }

        SavedState(Parcel parcel) {
            int[] nArray;
            int n;
            this.mAnchorPosition = parcel.readInt();
            this.mVisibleAnchorPosition = parcel.readInt();
            this.mSpanOffsetsSize = n = parcel.readInt();
            if (n > 0) {
                nArray = new int[n];
                this.mSpanOffsets = nArray;
                parcel.readIntArray(nArray);
            }
            this.mSpanLookupSize = n = parcel.readInt();
            if (n > 0) {
                nArray = new int[n];
                this.mSpanLookup = nArray;
                parcel.readIntArray(nArray);
            }
            n = parcel.readInt();
            boolean bl = false;
            boolean bl2 = n == 1;
            this.mReverseLayout = bl2;
            bl2 = parcel.readInt() == 1;
            this.mAnchorLayoutFromEnd = bl2;
            bl2 = bl;
            if (parcel.readInt() == 1) {
                bl2 = true;
            }
            this.mLastLayoutRTL = bl2;
            this.mFullSpanItems = parcel.readArrayList(LazySpanLookup.FullSpanItem.class.getClassLoader());
        }

        public SavedState(SavedState savedState) {
            this.mSpanOffsetsSize = savedState.mSpanOffsetsSize;
            this.mAnchorPosition = savedState.mAnchorPosition;
            this.mVisibleAnchorPosition = savedState.mVisibleAnchorPosition;
            this.mSpanOffsets = savedState.mSpanOffsets;
            this.mSpanLookupSize = savedState.mSpanLookupSize;
            this.mSpanLookup = savedState.mSpanLookup;
            this.mReverseLayout = savedState.mReverseLayout;
            this.mAnchorLayoutFromEnd = savedState.mAnchorLayoutFromEnd;
            this.mLastLayoutRTL = savedState.mLastLayoutRTL;
            this.mFullSpanItems = savedState.mFullSpanItems;
        }

        public int describeContents() {
            return 0;
        }

        void invalidateAnchorPositionInfo() {
            this.mSpanOffsets = null;
            this.mSpanOffsetsSize = 0;
            this.mAnchorPosition = -1;
            this.mVisibleAnchorPosition = -1;
        }

        void invalidateSpanInfo() {
            this.mSpanOffsets = null;
            this.mSpanOffsetsSize = 0;
            this.mSpanLookupSize = 0;
            this.mSpanLookup = null;
            this.mFullSpanItems = null;
        }

        public void writeToParcel(Parcel parcel, int n) {
            parcel.writeInt(this.mAnchorPosition);
            parcel.writeInt(this.mVisibleAnchorPosition);
            parcel.writeInt(this.mSpanOffsetsSize);
            if (this.mSpanOffsetsSize > 0) {
                parcel.writeIntArray(this.mSpanOffsets);
            }
            parcel.writeInt(this.mSpanLookupSize);
            if (this.mSpanLookupSize > 0) {
                parcel.writeIntArray(this.mSpanLookup);
            }
            parcel.writeInt(this.mReverseLayout ? 1 : 0);
            parcel.writeInt(this.mAnchorLayoutFromEnd ? 1 : 0);
            parcel.writeInt(this.mLastLayoutRTL ? 1 : 0);
            parcel.writeList(this.mFullSpanItems);
        }
    }

    class Span {
        static final int INVALID_LINE = Integer.MIN_VALUE;
        int mCachedEnd;
        int mCachedStart;
        int mDeletedSize;
        final int mIndex;
        ArrayList<View> mViews;
        final StaggeredGridLayoutManager this$0;

        Span(StaggeredGridLayoutManager staggeredGridLayoutManager, int n) {
            this.this$0 = staggeredGridLayoutManager;
            this.mViews = new ArrayList();
            this.mCachedStart = Integer.MIN_VALUE;
            this.mCachedEnd = Integer.MIN_VALUE;
            this.mDeletedSize = 0;
            this.mIndex = n;
        }

        void appendToSpan(View view) {
            LayoutParams layoutParams = this.getLayoutParams(view);
            layoutParams.mSpan = this;
            this.mViews.add(view);
            this.mCachedEnd = Integer.MIN_VALUE;
            if (this.mViews.size() == 1) {
                this.mCachedStart = Integer.MIN_VALUE;
            }
            if (layoutParams.isItemRemoved() || layoutParams.isItemChanged()) {
                this.mDeletedSize += this.this$0.mPrimaryOrientation.getDecoratedMeasurement(view);
            }
        }

        void cacheReferenceLineAndClear(boolean bl, int n) {
            int n2 = bl ? this.getEndLine(Integer.MIN_VALUE) : this.getStartLine(Integer.MIN_VALUE);
            this.clear();
            if (n2 == Integer.MIN_VALUE) {
                return;
            }
            if (bl && n2 < this.this$0.mPrimaryOrientation.getEndAfterPadding() || !bl && n2 > this.this$0.mPrimaryOrientation.getStartAfterPadding()) {
                return;
            }
            int n3 = n2;
            if (n != Integer.MIN_VALUE) {
                n3 = n2 + n;
            }
            this.mCachedEnd = n3;
            this.mCachedStart = n3;
        }

        void calculateCachedEnd() {
            Object object = this.mViews;
            object = ((ArrayList)object).get(((ArrayList)object).size() - 1);
            LayoutParams layoutParams = this.getLayoutParams((View)object);
            this.mCachedEnd = this.this$0.mPrimaryOrientation.getDecoratedEnd((View)object);
            if (layoutParams.mFullSpan && (object = this.this$0.mLazySpanLookup.getFullSpanItem(layoutParams.getViewLayoutPosition())) != null && ((LazySpanLookup.FullSpanItem)object).mGapDir == 1) {
                this.mCachedEnd += ((LazySpanLookup.FullSpanItem)object).getGapForSpan(this.mIndex);
            }
        }

        void calculateCachedStart() {
            Object object = this.mViews.get(0);
            LayoutParams layoutParams = this.getLayoutParams((View)object);
            this.mCachedStart = this.this$0.mPrimaryOrientation.getDecoratedStart((View)object);
            if (layoutParams.mFullSpan && (object = this.this$0.mLazySpanLookup.getFullSpanItem(layoutParams.getViewLayoutPosition())) != null && ((LazySpanLookup.FullSpanItem)object).mGapDir == -1) {
                this.mCachedStart -= ((LazySpanLookup.FullSpanItem)object).getGapForSpan(this.mIndex);
            }
        }

        void clear() {
            this.mViews.clear();
            this.invalidateCache();
            this.mDeletedSize = 0;
        }

        public int findFirstCompletelyVisibleItemPosition() {
            int n = this.this$0.mReverseLayout ? this.findOneVisibleChild(this.mViews.size() - 1, -1, true) : this.findOneVisibleChild(0, this.mViews.size(), true);
            return n;
        }

        public int findFirstPartiallyVisibleItemPosition() {
            int n = this.this$0.mReverseLayout ? this.findOnePartiallyVisibleChild(this.mViews.size() - 1, -1, true) : this.findOnePartiallyVisibleChild(0, this.mViews.size(), true);
            return n;
        }

        public int findFirstVisibleItemPosition() {
            int n = this.this$0.mReverseLayout ? this.findOneVisibleChild(this.mViews.size() - 1, -1, false) : this.findOneVisibleChild(0, this.mViews.size(), false);
            return n;
        }

        public int findLastCompletelyVisibleItemPosition() {
            int n = this.this$0.mReverseLayout ? this.findOneVisibleChild(0, this.mViews.size(), true) : this.findOneVisibleChild(this.mViews.size() - 1, -1, true);
            return n;
        }

        public int findLastPartiallyVisibleItemPosition() {
            int n = this.this$0.mReverseLayout ? this.findOnePartiallyVisibleChild(0, this.mViews.size(), true) : this.findOnePartiallyVisibleChild(this.mViews.size() - 1, -1, true);
            return n;
        }

        public int findLastVisibleItemPosition() {
            int n = this.this$0.mReverseLayout ? this.findOneVisibleChild(0, this.mViews.size(), false) : this.findOneVisibleChild(this.mViews.size() - 1, -1, false);
            return n;
        }

        int findOnePartiallyOrCompletelyVisibleChild(int n, int n2, boolean bl, boolean bl2, boolean bl3) {
            int n3 = this.this$0.mPrimaryOrientation.getStartAfterPadding();
            int n4 = this.this$0.mPrimaryOrientation.getEndAfterPadding();
            int n5 = n2 > n ? 1 : -1;
            while (n != n2) {
                View view = this.mViews.get(n);
                int n6 = this.this$0.mPrimaryOrientation.getDecoratedStart(view);
                int n7 = this.this$0.mPrimaryOrientation.getDecoratedEnd(view);
                boolean bl4 = false;
                boolean bl5 = bl3 ? n6 <= n4 : n6 < n4;
                if (bl3 ? n7 >= n3 : n7 > n3) {
                    bl4 = true;
                }
                if (bl5 && bl4) {
                    if (bl && bl2) {
                        if (n6 >= n3 && n7 <= n4) {
                            return this.this$0.getPosition(view);
                        }
                    } else {
                        if (bl2) {
                            return this.this$0.getPosition(view);
                        }
                        if (n6 < n3 || n7 > n4) {
                            return this.this$0.getPosition(view);
                        }
                    }
                }
                n += n5;
            }
            return -1;
        }

        int findOnePartiallyVisibleChild(int n, int n2, boolean bl) {
            return this.findOnePartiallyOrCompletelyVisibleChild(n, n2, false, false, bl);
        }

        int findOneVisibleChild(int n, int n2, boolean bl) {
            return this.findOnePartiallyOrCompletelyVisibleChild(n, n2, bl, true, false);
        }

        public int getDeletedSize() {
            return this.mDeletedSize;
        }

        int getEndLine() {
            int n = this.mCachedEnd;
            if (n != Integer.MIN_VALUE) {
                return n;
            }
            this.calculateCachedEnd();
            return this.mCachedEnd;
        }

        int getEndLine(int n) {
            int n2 = this.mCachedEnd;
            if (n2 != Integer.MIN_VALUE) {
                return n2;
            }
            if (this.mViews.size() == 0) {
                return n;
            }
            this.calculateCachedEnd();
            return this.mCachedEnd;
        }

        public View getFocusableViewAfter(int n, int n2) {
            View view = null;
            View view2 = null;
            if (n2 == -1) {
                int n3 = this.mViews.size();
                for (n2 = 0; n2 < n3; ++n2) {
                    view = this.mViews.get(n2);
                    if (this.this$0.mReverseLayout && this.this$0.getPosition(view) <= n || !this.this$0.mReverseLayout && this.this$0.getPosition(view) >= n || !view.hasFocusable()) break;
                    view2 = view;
                }
                view = view2;
            } else {
                n2 = this.mViews.size() - 1;
                view2 = view;
                while (true) {
                    view = view2;
                    if (n2 < 0) break;
                    View view3 = this.mViews.get(n2);
                    if (this.this$0.mReverseLayout) {
                        view = view2;
                        if (this.this$0.getPosition(view3) >= n) break;
                    }
                    if (!this.this$0.mReverseLayout && this.this$0.getPosition(view3) <= n) {
                        view = view2;
                        break;
                    }
                    view = view2;
                    if (!view3.hasFocusable()) break;
                    view2 = view3;
                    --n2;
                }
            }
            return view;
        }

        LayoutParams getLayoutParams(View view) {
            return (LayoutParams)view.getLayoutParams();
        }

        int getStartLine() {
            int n = this.mCachedStart;
            if (n != Integer.MIN_VALUE) {
                return n;
            }
            this.calculateCachedStart();
            return this.mCachedStart;
        }

        int getStartLine(int n) {
            int n2 = this.mCachedStart;
            if (n2 != Integer.MIN_VALUE) {
                return n2;
            }
            if (this.mViews.size() == 0) {
                return n;
            }
            this.calculateCachedStart();
            return this.mCachedStart;
        }

        void invalidateCache() {
            this.mCachedStart = Integer.MIN_VALUE;
            this.mCachedEnd = Integer.MIN_VALUE;
        }

        void onOffset(int n) {
            int n2 = this.mCachedStart;
            if (n2 != Integer.MIN_VALUE) {
                this.mCachedStart = n2 + n;
            }
            if ((n2 = this.mCachedEnd) != Integer.MIN_VALUE) {
                this.mCachedEnd = n2 + n;
            }
        }

        void popEnd() {
            int n = this.mViews.size();
            View view = this.mViews.remove(n - 1);
            LayoutParams layoutParams = this.getLayoutParams(view);
            layoutParams.mSpan = null;
            if (layoutParams.isItemRemoved() || layoutParams.isItemChanged()) {
                this.mDeletedSize -= this.this$0.mPrimaryOrientation.getDecoratedMeasurement(view);
            }
            if (n == 1) {
                this.mCachedStart = Integer.MIN_VALUE;
            }
            this.mCachedEnd = Integer.MIN_VALUE;
        }

        void popStart() {
            View view = this.mViews.remove(0);
            LayoutParams layoutParams = this.getLayoutParams(view);
            layoutParams.mSpan = null;
            if (this.mViews.size() == 0) {
                this.mCachedEnd = Integer.MIN_VALUE;
            }
            if (layoutParams.isItemRemoved() || layoutParams.isItemChanged()) {
                this.mDeletedSize -= this.this$0.mPrimaryOrientation.getDecoratedMeasurement(view);
            }
            this.mCachedStart = Integer.MIN_VALUE;
        }

        void prependToSpan(View view) {
            LayoutParams layoutParams = this.getLayoutParams(view);
            layoutParams.mSpan = this;
            this.mViews.add(0, view);
            this.mCachedStart = Integer.MIN_VALUE;
            if (this.mViews.size() == 1) {
                this.mCachedEnd = Integer.MIN_VALUE;
            }
            if (layoutParams.isItemRemoved() || layoutParams.isItemChanged()) {
                this.mDeletedSize += this.this$0.mPrimaryOrientation.getDecoratedMeasurement(view);
            }
        }

        void setLine(int n) {
            this.mCachedStart = n;
            this.mCachedEnd = n;
        }
    }
}

