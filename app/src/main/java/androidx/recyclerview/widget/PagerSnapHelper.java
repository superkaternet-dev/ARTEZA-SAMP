/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.PointF
 *  android.util.DisplayMetrics
 *  android.view.View
 *  android.view.animation.Interpolator
 */
package androidx.recyclerview.widget;

import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Interpolator;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

public class PagerSnapHelper
extends SnapHelper {
    private static final int MAX_SCROLL_ON_FLING_DURATION = 100;
    private OrientationHelper mHorizontalHelper;
    private OrientationHelper mVerticalHelper;

    private int distanceToCenter(RecyclerView.LayoutManager layoutManager, View view, OrientationHelper orientationHelper) {
        int n = orientationHelper.getDecoratedStart(view);
        int n2 = orientationHelper.getDecoratedMeasurement(view) / 2;
        int n3 = layoutManager.getClipToPadding() ? orientationHelper.getStartAfterPadding() + orientationHelper.getTotalSpace() / 2 : orientationHelper.getEnd() / 2;
        return n + n2 - n3;
    }

    private View findCenterView(RecyclerView.LayoutManager layoutManager, OrientationHelper orientationHelper) {
        int n = layoutManager.getChildCount();
        if (n == 0) {
            return null;
        }
        View view = null;
        int n2 = layoutManager.getClipToPadding() ? orientationHelper.getStartAfterPadding() + orientationHelper.getTotalSpace() / 2 : orientationHelper.getEnd() / 2;
        int n3 = Integer.MAX_VALUE;
        for (int i = 0; i < n; ++i) {
            View view2 = layoutManager.getChildAt(i);
            int n4 = Math.abs(orientationHelper.getDecoratedStart(view2) + orientationHelper.getDecoratedMeasurement(view2) / 2 - n2);
            int n5 = n3;
            if (n4 < n3) {
                n5 = n4;
                view = view2;
            }
            n3 = n5;
        }
        return view;
    }

    private View findStartView(RecyclerView.LayoutManager layoutManager, OrientationHelper orientationHelper) {
        int n = layoutManager.getChildCount();
        if (n == 0) {
            return null;
        }
        View view = null;
        int n2 = Integer.MAX_VALUE;
        for (int i = 0; i < n; ++i) {
            View view2 = layoutManager.getChildAt(i);
            int n3 = orientationHelper.getDecoratedStart(view2);
            int n4 = n2;
            if (n3 < n2) {
                n4 = n3;
                view = view2;
            }
            n2 = n4;
        }
        return view;
    }

    private OrientationHelper getHorizontalHelper(RecyclerView.LayoutManager layoutManager) {
        OrientationHelper orientationHelper = this.mHorizontalHelper;
        if (orientationHelper == null || orientationHelper.mLayoutManager != layoutManager) {
            this.mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return this.mHorizontalHelper;
    }

    private OrientationHelper getVerticalHelper(RecyclerView.LayoutManager layoutManager) {
        OrientationHelper orientationHelper = this.mVerticalHelper;
        if (orientationHelper == null || orientationHelper.mLayoutManager != layoutManager) {
            this.mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
        }
        return this.mVerticalHelper;
    }

    @Override
    public int[] calculateDistanceToFinalSnap(RecyclerView.LayoutManager layoutManager, View view) {
        int[] nArray = new int[]{layoutManager.canScrollHorizontally() ? this.distanceToCenter(layoutManager, view, this.getHorizontalHelper(layoutManager)) : 0, layoutManager.canScrollVertically() ? this.distanceToCenter(layoutManager, view, this.getVerticalHelper(layoutManager)) : 0};
        return nArray;
    }

    @Override
    protected LinearSmoothScroller createSnapScroller(RecyclerView.LayoutManager layoutManager) {
        if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
            return null;
        }
        return new LinearSmoothScroller(this, this.mRecyclerView.getContext()){
            final PagerSnapHelper this$0;
            {
                this.this$0 = pagerSnapHelper;
                super(context);
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return 100.0f / (float)displayMetrics.densityDpi;
            }

            @Override
            protected int calculateTimeForScrolling(int n) {
                return Math.min(100, super.calculateTimeForScrolling(n));
            }

            @Override
            protected void onTargetFound(View object, RecyclerView.State object2, RecyclerView.SmoothScroller.Action action) {
                object2 = this.this$0;
                object = ((PagerSnapHelper)object2).calculateDistanceToFinalSnap(((PagerSnapHelper)object2).mRecyclerView.getLayoutManager(), (View)object);
                View view = object[0];
                View view2 = object[1];
                int n = this.calculateTimeForDeceleration(Math.max(Math.abs((int)view), Math.abs((int)view2)));
                if (n > 0) {
                    action.update((int)view, (int)view2, n, (Interpolator)this.mDecelerateInterpolator);
                }
            }
        };
    }

    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager.canScrollVertically()) {
            return this.findCenterView(layoutManager, this.getVerticalHelper(layoutManager));
        }
        if (layoutManager.canScrollHorizontally()) {
            return this.findCenterView(layoutManager, this.getHorizontalHelper(layoutManager));
        }
        return null;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int n, int n2) {
        int n3;
        block10: {
            block11: {
                int n4;
                int n5 = layoutManager.getItemCount();
                if (n5 == 0) {
                    return -1;
                }
                View view = null;
                if (layoutManager.canScrollVertically()) {
                    view = this.findStartView(layoutManager, this.getVerticalHelper(layoutManager));
                } else if (layoutManager.canScrollHorizontally()) {
                    view = this.findStartView(layoutManager, this.getHorizontalHelper(layoutManager));
                }
                if (view == null) {
                    return -1;
                }
                n3 = layoutManager.getPosition(view);
                if (n3 == -1) {
                    return -1;
                }
                boolean bl = layoutManager.canScrollHorizontally();
                int n6 = 0;
                n = bl ? (n > 0 ? 1 : 0) : (n2 > 0 ? 1 : 0);
                n2 = n4 = 0;
                if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) break block10;
                layoutManager = ((RecyclerView.SmoothScroller.ScrollVectorProvider)((Object)layoutManager)).computeScrollVectorForPosition(n5 - 1);
                n2 = n4;
                if (layoutManager == null) break block10;
                if (((PointF)layoutManager).x < 0.0f) break block11;
                n2 = n6;
                if (!(((PointF)layoutManager).y < 0.0f)) break block10;
            }
            n2 = 1;
        }
        if (n2 != 0) {
            if (n == 0) return n3;
            return n3 - 1;
        }
        if (n == 0) return n3;
        return n3 + 1;
    }
}

