/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Rect
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.SparseIntArray
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 */
package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;

public class GridLayoutManager
extends LinearLayoutManager {
    private static final boolean DEBUG = false;
    public static final int DEFAULT_SPAN_COUNT = -1;
    private static final String TAG = "GridLayoutManager";
    int[] mCachedBorders;
    final Rect mDecorInsets;
    boolean mPendingSpanCountChange = false;
    final SparseIntArray mPreLayoutSpanIndexCache;
    final SparseIntArray mPreLayoutSpanSizeCache = new SparseIntArray();
    View[] mSet;
    int mSpanCount = -1;
    SpanSizeLookup mSpanSizeLookup;

    public GridLayoutManager(Context context, int n) {
        super(context);
        this.mPreLayoutSpanIndexCache = new SparseIntArray();
        this.mSpanSizeLookup = new DefaultSpanSizeLookup();
        this.mDecorInsets = new Rect();
        this.setSpanCount(n);
    }

    public GridLayoutManager(Context context, int n, int n2, boolean bl) {
        super(context, n2, bl);
        this.mPreLayoutSpanIndexCache = new SparseIntArray();
        this.mSpanSizeLookup = new DefaultSpanSizeLookup();
        this.mDecorInsets = new Rect();
        this.setSpanCount(n);
    }

    public GridLayoutManager(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
        this.mPreLayoutSpanIndexCache = new SparseIntArray();
        this.mSpanSizeLookup = new DefaultSpanSizeLookup();
        this.mDecorInsets = new Rect();
        this.setSpanCount(GridLayoutManager.getProperties((Context)context, (AttributeSet)attributeSet, (int)n, (int)n2).spanCount);
    }

    private void assignSpans(RecyclerView.Recycler recycler, RecyclerView.State state, int n, int n2, boolean bl) {
        int n3;
        int n4;
        if (bl) {
            n4 = 0;
            n2 = n;
            n3 = 1;
            n = n4;
        } else {
            --n;
            n2 = -1;
            n3 = -1;
        }
        n4 = 0;
        while (n != n2) {
            View view = this.mSet[n];
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            layoutParams.mSpanSize = this.getSpanSize(recycler, state, this.getPosition(view));
            layoutParams.mSpanIndex = n4;
            n4 += layoutParams.mSpanSize;
            n += n3;
        }
    }

    private void cachePreLayoutSpanMapping() {
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            LayoutParams layoutParams = (LayoutParams)this.getChildAt(i).getLayoutParams();
            int n2 = layoutParams.getViewLayoutPosition();
            this.mPreLayoutSpanSizeCache.put(n2, layoutParams.getSpanSize());
            this.mPreLayoutSpanIndexCache.put(n2, layoutParams.getSpanIndex());
        }
    }

    private void calculateItemBorders(int n) {
        this.mCachedBorders = GridLayoutManager.calculateItemBorders(this.mCachedBorders, this.mSpanCount, n);
    }

    static int[] calculateItemBorders(int[] nArray, int n, int n2) {
        int[] nArray2;
        block7: {
            block6: {
                if (nArray == null || nArray.length != n + 1) break block6;
                nArray2 = nArray;
                if (nArray[nArray.length - 1] == n2) break block7;
            }
            nArray2 = new int[n + 1];
        }
        nArray2[0] = 0;
        int n3 = n2 / n;
        int n4 = n2 % n;
        int n5 = 0;
        n2 = 0;
        for (int i = 1; i <= n; ++i) {
            int n6;
            int n7 = n3;
            n2 = n6 = n2 + n4;
            int n8 = n7;
            if (n6 > 0) {
                n2 = n6;
                n8 = n7;
                if (n - n6 < n4) {
                    n8 = n7 + 1;
                    n2 = n6 - n;
                }
            }
            nArray2[i] = n5 += n8;
        }
        return nArray2;
    }

    private void clearPreLayoutSpanMappingCache() {
        this.mPreLayoutSpanSizeCache.clear();
        this.mPreLayoutSpanIndexCache.clear();
    }

    private void ensureAnchorIsInCorrectSpan(RecyclerView.Recycler recycler, RecyclerView.State state, LinearLayoutManager.AnchorInfo anchorInfo, int n) {
        int n2 = n == 1 ? 1 : 0;
        n = this.getSpanIndex(recycler, state, anchorInfo.mPosition);
        if (n2 != 0) {
            while (n > 0 && anchorInfo.mPosition > 0) {
                --anchorInfo.mPosition;
                n = this.getSpanIndex(recycler, state, anchorInfo.mPosition);
            }
        } else {
            int n3 = state.getItemCount();
            int n4 = anchorInfo.mPosition;
            n2 = n;
            for (n = n4; n < n3 - 1 && (n4 = this.getSpanIndex(recycler, state, n + 1)) > n2; ++n) {
                n2 = n4;
            }
            anchorInfo.mPosition = n;
        }
    }

    private void ensureViewSet() {
        View[] viewArray = this.mSet;
        if (viewArray == null || viewArray.length != this.mSpanCount) {
            this.mSet = new View[this.mSpanCount];
        }
    }

    private int getSpanGroupIndex(RecyclerView.Recycler object, RecyclerView.State state, int n) {
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getSpanGroupIndex(n, this.mSpanCount);
        }
        int n2 = ((RecyclerView.Recycler)object).convertPreLayoutPositionToPostLayout(n);
        if (n2 == -1) {
            object = new StringBuilder();
            ((StringBuilder)object).append("Cannot find span size for pre layout position. ");
            ((StringBuilder)object).append(n);
            Log.w((String)TAG, (String)((StringBuilder)object).toString());
            return 0;
        }
        return this.mSpanSizeLookup.getSpanGroupIndex(n2, this.mSpanCount);
    }

    private int getSpanIndex(RecyclerView.Recycler object, RecyclerView.State state, int n) {
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getCachedSpanIndex(n, this.mSpanCount);
        }
        int n2 = this.mPreLayoutSpanIndexCache.get(n, -1);
        if (n2 != -1) {
            return n2;
        }
        n2 = ((RecyclerView.Recycler)object).convertPreLayoutPositionToPostLayout(n);
        if (n2 == -1) {
            object = new StringBuilder();
            ((StringBuilder)object).append("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:");
            ((StringBuilder)object).append(n);
            Log.w((String)TAG, (String)((StringBuilder)object).toString());
            return 0;
        }
        return this.mSpanSizeLookup.getCachedSpanIndex(n2, this.mSpanCount);
    }

    private int getSpanSize(RecyclerView.Recycler object, RecyclerView.State state, int n) {
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getSpanSize(n);
        }
        int n2 = this.mPreLayoutSpanSizeCache.get(n, -1);
        if (n2 != -1) {
            return n2;
        }
        n2 = ((RecyclerView.Recycler)object).convertPreLayoutPositionToPostLayout(n);
        if (n2 == -1) {
            object = new StringBuilder();
            ((StringBuilder)object).append("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:");
            ((StringBuilder)object).append(n);
            Log.w((String)TAG, (String)((StringBuilder)object).toString());
            return 1;
        }
        return this.mSpanSizeLookup.getSpanSize(n2);
    }

    private void guessMeasurement(float f, int n) {
        this.calculateItemBorders(Math.max(Math.round((float)this.mSpanCount * f), n));
    }

    private void measureChild(View view, int n, boolean bl) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        Rect rect = layoutParams.mDecorInsets;
        int n2 = rect.top + rect.bottom + layoutParams.topMargin + layoutParams.bottomMargin;
        int n3 = rect.left + rect.right + layoutParams.leftMargin + layoutParams.rightMargin;
        int n4 = this.getSpaceForSpanRange(layoutParams.mSpanIndex, layoutParams.mSpanSize);
        if (this.mOrientation == 1) {
            n = GridLayoutManager.getChildMeasureSpec(n4, n, n3, layoutParams.width, false);
            n2 = GridLayoutManager.getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), this.getHeightMode(), n2, layoutParams.height, true);
        } else {
            n2 = GridLayoutManager.getChildMeasureSpec(n4, n, n2, layoutParams.height, false);
            n = GridLayoutManager.getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), this.getWidthMode(), n3, layoutParams.width, true);
        }
        this.measureChildWithDecorationsAndMargin(view, n, n2, bl);
    }

    private void measureChildWithDecorationsAndMargin(View view, int n, int n2, boolean bl) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)view.getLayoutParams();
        if (bl = bl ? this.shouldReMeasureChild(view, n, n2, layoutParams) : this.shouldMeasureChild(view, n, n2, layoutParams)) {
            view.measure(n, n2);
        }
    }

    private void updateMeasurements() {
        int n = this.getOrientation() == 1 ? this.getWidth() - this.getPaddingRight() - this.getPaddingLeft() : this.getHeight() - this.getPaddingBottom() - this.getPaddingTop();
        this.calculateItemBorders(n);
    }

    @Override
    public boolean checkLayoutParams(RecyclerView.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    @Override
    void collectPrefetchPositionsForLayoutState(RecyclerView.State state, LinearLayoutManager.LayoutState layoutState, RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry) {
        int n = this.mSpanCount;
        for (int i = 0; i < this.mSpanCount && layoutState.hasMore(state) && n > 0; ++i) {
            int n2 = layoutState.mCurrentPosition;
            layoutPrefetchRegistry.addPosition(n2, Math.max(0, layoutState.mScrollingOffset));
            n -= this.mSpanSizeLookup.getSpanSize(n2);
            layoutState.mCurrentPosition += layoutState.mItemDirection;
        }
    }

    @Override
    View findReferenceChild(RecyclerView.Recycler recycler, RecyclerView.State state, int n, int n2, int n3) {
        this.ensureLayoutState();
        View view = null;
        View view2 = null;
        int n4 = this.mOrientationHelper.getStartAfterPadding();
        int n5 = this.mOrientationHelper.getEndAfterPadding();
        int n6 = n2 > n ? 1 : -1;
        while (n != n2) {
            View view3 = this.getChildAt(n);
            int n7 = this.getPosition(view3);
            View view4 = view;
            View view5 = view2;
            if (n7 >= 0) {
                view4 = view;
                view5 = view2;
                if (n7 < n3) {
                    if (this.getSpanIndex(recycler, state, n7) != 0) {
                        view4 = view;
                        view5 = view2;
                    } else if (((RecyclerView.LayoutParams)view3.getLayoutParams()).isItemRemoved()) {
                        view4 = view;
                        view5 = view2;
                        if (view == null) {
                            view4 = view3;
                            view5 = view2;
                        }
                    } else {
                        if (this.mOrientationHelper.getDecoratedStart(view3) < n5 && this.mOrientationHelper.getDecoratedEnd(view3) >= n4) {
                            return view3;
                        }
                        view4 = view;
                        view5 = view2;
                        if (view2 == null) {
                            view5 = view3;
                            view4 = view;
                        }
                    }
                }
            }
            n += n6;
            view = view4;
            view2 = view5;
        }
        if (view2 == null) {
            view2 = view;
        }
        return view2;
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
        if (state.getItemCount() < 1) {
            return 0;
        }
        return this.getSpanGroupIndex(recycler, state, state.getItemCount() - 1) + 1;
    }

    @Override
    public int getRowCountForAccessibility(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mOrientation == 0) {
            return this.mSpanCount;
        }
        if (state.getItemCount() < 1) {
            return 0;
        }
        return this.getSpanGroupIndex(recycler, state, state.getItemCount() - 1) + 1;
    }

    int getSpaceForSpanRange(int n, int n2) {
        if (this.mOrientation == 1 && this.isLayoutRTL()) {
            int[] nArray = this.mCachedBorders;
            int n3 = this.mSpanCount;
            return nArray[n3 - n] - nArray[n3 - n - n2];
        }
        int[] nArray = this.mCachedBorders;
        return nArray[n + n2] - nArray[n];
    }

    public int getSpanCount() {
        return this.mSpanCount;
    }

    public SpanSizeLookup getSpanSizeLookup() {
        return this.mSpanSizeLookup;
    }

    @Override
    void layoutChunk(RecyclerView.Recycler object, RecyclerView.State object2, LinearLayoutManager.LayoutState layoutState, LinearLayoutManager.LayoutChunkResult layoutChunkResult) {
        View view;
        int n;
        int n2;
        int n3;
        int n4;
        int n5;
        boolean bl;
        int n6;
        int n7;
        int n8;
        block35: {
            n8 = this.mOrientationHelper.getModeInOther();
            n7 = n8 != 0x40000000 ? 1 : 0;
            n6 = this.getChildCount() > 0 ? this.mCachedBorders[this.mSpanCount] : 0;
            if (n7 != 0) {
                this.updateMeasurements();
            }
            bl = layoutState.mItemDirection == 1;
            n5 = this.mSpanCount;
            if (!bl) {
                n5 = this.getSpanIndex((RecyclerView.Recycler)object, (RecyclerView.State)object2, layoutState.mCurrentPosition) + this.getSpanSize((RecyclerView.Recycler)object, (RecyclerView.State)object2, layoutState.mCurrentPosition);
                n4 = 0;
                n3 = 0;
            } else {
                n4 = 0;
                n3 = 0;
            }
            while (true) {
                n2 = n5;
                if (n4 >= this.mSpanCount) break block35;
                n2 = n5;
                if (!layoutState.hasMore((RecyclerView.State)object2)) break block35;
                n2 = n5;
                if (n5 <= 0) break block35;
                n = layoutState.mCurrentPosition;
                n2 = this.getSpanSize((RecyclerView.Recycler)object, (RecyclerView.State)object2, n);
                if (n2 > this.mSpanCount) break;
                if ((n5 -= n2) < 0) {
                    n2 = n5;
                    break block35;
                }
                view = layoutState.next((RecyclerView.Recycler)object);
                if (view == null) {
                    n2 = n5;
                    break block35;
                }
                n3 += n2;
                this.mSet[n4] = view;
                ++n4;
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Item at position ");
            ((StringBuilder)object).append(n);
            ((StringBuilder)object).append(" requires ");
            ((StringBuilder)object).append(n2);
            ((StringBuilder)object).append(" spans but GridLayoutManager has only ");
            ((StringBuilder)object).append(this.mSpanCount);
            ((StringBuilder)object).append(" spans.");
            throw new IllegalArgumentException(((StringBuilder)object).toString());
        }
        if (n4 == 0) {
            layoutChunkResult.mFinished = true;
            return;
        }
        this.assignSpans((RecyclerView.Recycler)object, (RecyclerView.State)object2, n4, n3, bl);
        n5 = 0;
        float f = 0.0f;
        for (n = 0; n < n4; ++n) {
            object2 = this.mSet[n];
            if (layoutState.mScrapList == null) {
                if (bl) {
                    this.addView((View)object2);
                } else {
                    this.addView((View)object2, 0);
                }
            } else if (bl) {
                this.addDisappearingView((View)object2);
            } else {
                this.addDisappearingView((View)object2, 0);
            }
            this.calculateItemDecorationsForChild((View)object2, this.mDecorInsets);
            this.measureChild((View)object2, n8, false);
            int n9 = this.mOrientationHelper.getDecoratedMeasurement((View)object2);
            n3 = n5;
            if (n9 > n5) {
                n3 = n9;
            }
            object = (LayoutParams)object2.getLayoutParams();
            float f2 = (float)this.mOrientationHelper.getDecoratedMeasurementInOther((View)object2) * 1.0f / (float)((LayoutParams)((Object)object)).mSpanSize;
            float f3 = f;
            if (f2 > f) {
                f3 = f2;
            }
            n5 = n3;
            f = f3;
        }
        if (n7 != 0) {
            this.guessMeasurement(f, n6);
            n5 = 0;
            for (n3 = 0; n3 < n4; ++n3) {
                object = this.mSet[n3];
                this.measureChild((View)object, 0x40000000, true);
                n6 = this.mOrientationHelper.getDecoratedMeasurement((View)object);
                n7 = n5;
                if (n6 > n5) {
                    n7 = n6;
                }
                n5 = n7;
            }
            n6 = n5;
        } else {
            n6 = n5;
        }
        n5 = n8;
        for (n3 = 0; n3 < n4; ++n3) {
            object = this.mSet[n3];
            if (this.mOrientationHelper.getDecoratedMeasurement((View)object) == n6) continue;
            object2 = (LayoutParams)object.getLayoutParams();
            view = ((LayoutParams)((Object)object2)).mDecorInsets;
            n = view.top + view.bottom + ((LayoutParams)((Object)object2)).topMargin + ((LayoutParams)((Object)object2)).bottomMargin;
            n7 = view.left + view.right + ((LayoutParams)((Object)object2)).leftMargin + ((LayoutParams)((Object)object2)).rightMargin;
            n8 = this.getSpaceForSpanRange(((LayoutParams)((Object)object2)).mSpanIndex, ((LayoutParams)((Object)object2)).mSpanSize);
            if (this.mOrientation == 1) {
                n7 = GridLayoutManager.getChildMeasureSpec(n8, 0x40000000, n7, ((LayoutParams)((Object)object2)).width, false);
                n = View.MeasureSpec.makeMeasureSpec((int)(n6 - n), (int)0x40000000);
            } else {
                n7 = View.MeasureSpec.makeMeasureSpec((int)(n6 - n7), (int)0x40000000);
                n = GridLayoutManager.getChildMeasureSpec(n8, 0x40000000, n, ((LayoutParams)((Object)object2)).height, false);
            }
            this.measureChildWithDecorationsAndMargin((View)object, n7, n, true);
        }
        layoutChunkResult.mConsumed = n6;
        n3 = 0;
        n2 = 0;
        n7 = 0;
        n5 = 0;
        if (this.mOrientation == 1) {
            if (layoutState.mLayoutDirection == -1) {
                n5 = layoutState.mOffset;
                n7 = n5 - n6;
            } else {
                n7 = layoutState.mOffset;
                n5 = n7 + n6;
            }
        } else if (layoutState.mLayoutDirection == -1) {
            n2 = layoutState.mOffset;
            n3 = n2 - n6;
        } else {
            n3 = layoutState.mOffset;
            n2 = n3 + n6;
        }
        n = 0;
        while (n < n4) {
            object2 = this.mSet[n];
            object = (LayoutParams)object2.getLayoutParams();
            if (this.mOrientation == 1) {
                if (this.isLayoutRTL()) {
                    n8 = this.getPaddingLeft() + this.mCachedBorders[this.mSpanCount - ((LayoutParams)((Object)object)).mSpanIndex];
                    n2 = n8 - this.mOrientationHelper.getDecoratedMeasurementInOther((View)object2);
                    n3 = n7;
                    n7 = n8;
                } else {
                    n3 = this.getPaddingLeft() + this.mCachedBorders[((LayoutParams)((Object)object)).mSpanIndex];
                    n8 = this.mOrientationHelper.getDecoratedMeasurementInOther((View)object2);
                    n2 = n3;
                    n8 += n3;
                    n3 = n7;
                    n7 = n8;
                }
            } else {
                n5 = this.getPaddingTop() + this.mCachedBorders[((LayoutParams)((Object)object)).mSpanIndex];
                n7 = this.mOrientationHelper.getDecoratedMeasurementInOther((View)object2);
                n8 = n5;
                n5 = n7 + n5;
                n7 = n2;
                n2 = n3;
                n3 = n8;
            }
            this.layoutDecoratedWithMargins((View)object2, n2, n3, n7, n5);
            if (((RecyclerView.LayoutParams)((Object)object)).isItemRemoved() || ((RecyclerView.LayoutParams)((Object)object)).isItemChanged()) {
                layoutChunkResult.mIgnoreConsumed = true;
            }
            layoutChunkResult.mFocusable |= object2.hasFocusable();
            n8 = n + 1;
            n = n3;
            n3 = n2;
            n2 = n7;
            n7 = n;
            n = n8;
        }
        Arrays.fill(this.mSet, null);
    }

    @Override
    void onAnchorReady(RecyclerView.Recycler recycler, RecyclerView.State state, LinearLayoutManager.AnchorInfo anchorInfo, int n) {
        super.onAnchorReady(recycler, state, anchorInfo, n);
        this.updateMeasurements();
        if (state.getItemCount() > 0 && !state.isPreLayout()) {
            this.ensureAnchorIsInCorrectSpan(recycler, state, anchorInfo, n);
        }
        this.ensureViewSet();
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    @Override
    public View onFocusSearchFailed(View var1_1, int var2_2, RecyclerView.Recycler var3_3, RecyclerView.State var4_4) {
        block16: {
            var22_5 = this.findContainingItemView(var1_1 /* !! */ );
            if (var22_5 == null) {
                return null;
            }
            var21_6 = (LayoutParams)var22_5.getLayoutParams();
            var15_7 = var21_6.mSpanIndex;
            var16_8 = var21_6.mSpanIndex + var21_6.mSpanSize;
            if (super.onFocusSearchFailed(var1_1 /* !! */ , var2_2, var3_3, var4_4) == null) {
                return null;
            }
            var20_9 = this.convertFocusDirectionToLayoutDirection(var2_2) == 1;
            if ((var2_2 = var20_9 != this.mShouldReverseLayout ? 1 : 0) != 0) {
                var2_2 = this.getChildCount() - 1;
                var8_10 = -1;
                var7_11 = -1;
            } else {
                var2_2 = 0;
                var8_10 = 1;
                var7_11 = this.getChildCount();
            }
            var9_12 = this.mOrientation == 1 && this.isLayoutRTL() != false ? 1 : 0;
            var21_6 = null;
            var1_1 /* !! */  = null;
            var12_13 = this.getSpanGroupIndex(var3_3, var4_4, var2_2);
            var6_14 = -1;
            var5_15 = 0;
            var14_16 = -1;
            var13_17 = 0;
            var10_19 = var2_2;
            for (var11_18 = var2_2; var11_18 != var7_11; var11_18 += var8_10) {
                block12: {
                    block15: {
                        block13: {
                            block14: {
                                block11: {
                                    var2_2 = this.getSpanGroupIndex(var3_3, var4_4, var11_18);
                                    var23_23 = this.getChildAt(var11_18);
                                    if (var23_23 == var22_5) break;
                                    if (var23_23.hasFocusable() && var2_2 != var12_13) {
                                        if (var21_6 == null) continue;
                                        break;
                                    }
                                    var24_24 = (LayoutParams)var23_23.getLayoutParams();
                                    var17_20 = var24_24.mSpanIndex;
                                    var18_21 = var24_24.mSpanIndex + var24_24.mSpanSize;
                                    if (var23_23.hasFocusable() && var17_20 == var15_7 && var18_21 == var16_8) {
                                        return var23_23;
                                    }
                                    if ((!var23_23.hasFocusable() || var21_6 != null) && (var23_23.hasFocusable() || var1_1 /* !! */  != null)) break block11;
                                    var2_2 = 1;
                                    break block12;
                                }
                                var2_2 = Math.max(var17_20, var15_7);
                                var19_22 = Math.min(var18_21, var16_8) - var2_2;
                                if (!var23_23.hasFocusable()) break block13;
                                if (var19_22 <= var5_15) break block14;
                                var2_2 = 1;
                                break block12;
                            }
                            if (var19_22 != var5_15 || var9_12 != (var2_2 = var17_20 > var6_14 ? 1 : 0)) ** GOTO lbl-1000
                            var2_2 = 1;
                            break block12;
                        }
                        if (var21_6 != null) ** GOTO lbl-1000
                        var2_2 = 0;
                        if (!this.isViewPartiallyVisible(var23_23, false, true)) ** GOTO lbl-1000
                        if (var19_22 <= var13_17) break block15;
                        var2_2 = 1;
                        break block12;
                    }
                    if (var19_22 != var13_17) ** GOTO lbl-1000
                    if (var17_20 > var14_16) {
                        var2_2 = 1;
                    }
                    if (var9_12 == var2_2) {
                        var2_2 = 1;
                    } else lbl-1000:
                    // 5 sources

                    {
                        var2_2 = 0;
                    }
                }
                if (var2_2 == 0) continue;
                if (var23_23.hasFocusable()) {
                    var6_14 = var24_24.mSpanIndex;
                    var2_2 = Math.min(var18_21, var16_8);
                    var5_15 = Math.max(var17_20, var15_7);
                    var21_6 = var23_23;
                    var5_15 = var2_2 - var5_15;
                    continue;
                }
                var14_16 = var24_24.mSpanIndex;
                var2_2 = Math.min(var18_21, var16_8);
                var13_17 = Math.max(var17_20, var15_7);
                var1_1 /* !! */  = var23_23;
                var13_17 = var2_2 - var13_17;
            }
            if (var21_6 == null) break block16;
            var1_1 /* !! */  = var21_6;
        }
        return var1_1 /* !! */ ;
    }

    @Override
    public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler recycler, RecyclerView.State state, View object, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        ViewGroup.LayoutParams layoutParams = object.getLayoutParams();
        if (!(layoutParams instanceof LayoutParams)) {
            super.onInitializeAccessibilityNodeInfoForItem((View)object, accessibilityNodeInfoCompat);
            return;
        }
        object = (LayoutParams)layoutParams;
        int n = this.getSpanGroupIndex(recycler, state, ((RecyclerView.LayoutParams)((Object)object)).getViewLayoutPosition());
        if (this.mOrientation == 0) {
            int n2 = ((LayoutParams)((Object)object)).getSpanIndex();
            int n3 = ((LayoutParams)((Object)object)).getSpanSize();
            boolean bl = this.mSpanCount > 1 && ((LayoutParams)((Object)object)).getSpanSize() == this.mSpanCount;
            accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(n2, n3, n, 1, bl, false));
        } else {
            int n4 = ((LayoutParams)((Object)object)).getSpanIndex();
            int n5 = ((LayoutParams)((Object)object)).getSpanSize();
            boolean bl = this.mSpanCount > 1 && ((LayoutParams)((Object)object)).getSpanSize() == this.mSpanCount;
            accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(n, 1, n4, n5, bl, false));
        }
    }

    @Override
    public void onItemsAdded(RecyclerView recyclerView, int n, int n2) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }

    @Override
    public void onItemsChanged(RecyclerView recyclerView) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }

    @Override
    public void onItemsMoved(RecyclerView recyclerView, int n, int n2, int n3) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }

    @Override
    public void onItemsRemoved(RecyclerView recyclerView, int n, int n2) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }

    @Override
    public void onItemsUpdated(RecyclerView recyclerView, int n, int n2, Object object) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.isPreLayout()) {
            this.cachePreLayoutSpanMapping();
        }
        super.onLayoutChildren(recycler, state);
        this.clearPreLayoutSpanMappingCache();
    }

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        this.mPendingSpanCountChange = false;
    }

    @Override
    public int scrollHorizontallyBy(int n, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.updateMeasurements();
        this.ensureViewSet();
        return super.scrollHorizontallyBy(n, recycler, state);
    }

    @Override
    public int scrollVerticallyBy(int n, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.updateMeasurements();
        this.ensureViewSet();
        return super.scrollVerticallyBy(n, recycler, state);
    }

    @Override
    public void setMeasuredDimension(Rect object, int n, int n2) {
        if (this.mCachedBorders == null) {
            super.setMeasuredDimension((Rect)object, n, n2);
        }
        int n3 = this.getPaddingLeft() + this.getPaddingRight();
        int n4 = this.getPaddingTop() + this.getPaddingBottom();
        if (this.mOrientation == 1) {
            n2 = GridLayoutManager.chooseSize(n2, object.height() + n4, this.getMinimumHeight());
            object = this.mCachedBorders;
            n = GridLayoutManager.chooseSize(n, (int)(object[((Rect)object).length - 1] + n3), this.getMinimumWidth());
        } else {
            n = GridLayoutManager.chooseSize(n, object.width() + n3, this.getMinimumWidth());
            object = this.mCachedBorders;
            n2 = GridLayoutManager.chooseSize(n2, (int)(object[((Rect)object).length - 1] + n4), this.getMinimumHeight());
        }
        this.setMeasuredDimension(n, n2);
    }

    public void setSpanCount(int n) {
        if (n == this.mSpanCount) {
            return;
        }
        this.mPendingSpanCountChange = true;
        if (n >= 1) {
            this.mSpanCount = n;
            this.mSpanSizeLookup.invalidateSpanIndexCache();
            this.requestLayout();
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Span count should be at least 1. Provided ");
        stringBuilder.append(n);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        this.mSpanSizeLookup = spanSizeLookup;
    }

    @Override
    public void setStackFromEnd(boolean bl) {
        if (!bl) {
            super.setStackFromEnd(false);
            return;
        }
        throw new UnsupportedOperationException("GridLayoutManager does not support stack from end. Consider using reverse layout");
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        boolean bl = this.mPendingSavedState == null && !this.mPendingSpanCountChange;
        return bl;
    }

    public static final class DefaultSpanSizeLookup
    extends SpanSizeLookup {
        @Override
        public int getSpanIndex(int n, int n2) {
            return n % n2;
        }

        @Override
        public int getSpanSize(int n) {
            return 1;
        }
    }

    public static class LayoutParams
    extends RecyclerView.LayoutParams {
        public static final int INVALID_SPAN_ID = -1;
        int mSpanIndex = -1;
        int mSpanSize = 0;

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

        public int getSpanIndex() {
            return this.mSpanIndex;
        }

        public int getSpanSize() {
            return this.mSpanSize;
        }
    }

    public static abstract class SpanSizeLookup {
        private boolean mCacheSpanIndices = false;
        final SparseIntArray mSpanIndexCache = new SparseIntArray();

        int findReferenceIndexFromCache(int n) {
            int n2 = 0;
            int n3 = this.mSpanIndexCache.size() - 1;
            while (n2 <= n3) {
                int n4 = n2 + n3 >>> 1;
                if (this.mSpanIndexCache.keyAt(n4) < n) {
                    n2 = n4 + 1;
                    continue;
                }
                n3 = n4 - 1;
            }
            n = n2 - 1;
            if (n >= 0 && n < this.mSpanIndexCache.size()) {
                return this.mSpanIndexCache.keyAt(n);
            }
            return -1;
        }

        int getCachedSpanIndex(int n, int n2) {
            if (!this.mCacheSpanIndices) {
                return this.getSpanIndex(n, n2);
            }
            int n3 = this.mSpanIndexCache.get(n, -1);
            if (n3 != -1) {
                return n3;
            }
            n2 = this.getSpanIndex(n, n2);
            this.mSpanIndexCache.put(n, n2);
            return n2;
        }

        public int getSpanGroupIndex(int n, int n2) {
            int n3 = 0;
            int n4 = 0;
            int n5 = this.getSpanSize(n);
            for (int i = 0; i < n; ++i) {
                int n6;
                int n7 = this.getSpanSize(i);
                int n8 = n3 + n7;
                if (n8 == n2) {
                    n3 = 0;
                    n6 = n4 + 1;
                } else {
                    n3 = n8;
                    n6 = n4;
                    if (n8 > n2) {
                        n3 = n7;
                        n6 = n4 + 1;
                    }
                }
                n4 = n6;
            }
            n = n4;
            if (n3 + n5 > n2) {
                n = n4 + 1;
            }
            return n;
        }

        public int getSpanIndex(int n, int n2) {
            int n3 = this.getSpanSize(n);
            if (n3 == n2) {
                return 0;
            }
            int n4 = 0;
            int n5 = 0;
            int n6 = n4;
            int n7 = n5;
            if (this.mCacheSpanIndices) {
                n6 = n4;
                n7 = n5;
                if (this.mSpanIndexCache.size() > 0) {
                    int n8 = this.findReferenceIndexFromCache(n);
                    n6 = n4;
                    n7 = n5;
                    if (n8 >= 0) {
                        n6 = this.mSpanIndexCache.get(n8) + this.getSpanSize(n8);
                        n7 = n8 + 1;
                    }
                }
            }
            while (n7 < n) {
                n5 = this.getSpanSize(n7);
                n4 = n6 + n5;
                if (n4 == n2) {
                    n6 = 0;
                } else {
                    n6 = n4;
                    if (n4 > n2) {
                        n6 = n5;
                    }
                }
                ++n7;
            }
            if (n6 + n3 <= n2) {
                return n6;
            }
            return 0;
        }

        public abstract int getSpanSize(int var1);

        public void invalidateSpanIndexCache() {
            this.mSpanIndexCache.clear();
        }

        public boolean isSpanIndexCacheEnabled() {
            return this.mCacheSpanIndices;
        }

        public void setSpanIndexCacheEnabled(boolean bl) {
            this.mCacheSpanIndices = bl;
        }
    }
}

