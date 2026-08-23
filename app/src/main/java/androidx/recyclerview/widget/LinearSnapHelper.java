/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 */
package androidx.recyclerview.widget;

import android.view.View;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

public class LinearSnapHelper
extends SnapHelper {
    private static final float INVALID_DISTANCE = 1.0f;
    private OrientationHelper mHorizontalHelper;
    private OrientationHelper mVerticalHelper;

    private float computeDistancePerChild(RecyclerView.LayoutManager layoutManager, OrientationHelper orientationHelper) {
        int n;
        View view = null;
        View view2 = null;
        int n2 = Integer.MAX_VALUE;
        int n3 = Integer.MIN_VALUE;
        int n4 = layoutManager.getChildCount();
        if (n4 == 0) {
            return 1.0f;
        }
        for (int i = 0; i < n4; ++i) {
            int n5;
            View view3;
            View view4 = layoutManager.getChildAt(i);
            int n6 = layoutManager.getPosition(view4);
            if (n6 == -1) {
                view3 = view;
                n5 = n3;
            } else {
                n = n2;
                if (n6 < n2) {
                    n = n6;
                    view = view4;
                }
                view3 = view;
                n2 = n;
                n5 = n3;
                if (n6 > n3) {
                    view2 = view4;
                    n5 = n6;
                    n2 = n;
                    view3 = view;
                }
            }
            view = view3;
            n3 = n5;
        }
        if (view != null && view2 != null) {
            n = Math.min(orientationHelper.getDecoratedStart(view), orientationHelper.getDecoratedStart(view2));
            n = Math.max(orientationHelper.getDecoratedEnd(view), orientationHelper.getDecoratedEnd(view2)) - n;
            if (n == 0) {
                return 1.0f;
            }
            return (float)n * 1.0f / (float)(n3 - n2 + 1);
        }
        return 1.0f;
    }

    private int distanceToCenter(RecyclerView.LayoutManager layoutManager, View view, OrientationHelper orientationHelper) {
        int n = orientationHelper.getDecoratedStart(view);
        int n2 = orientationHelper.getDecoratedMeasurement(view) / 2;
        int n3 = layoutManager.getClipToPadding() ? orientationHelper.getStartAfterPadding() + orientationHelper.getTotalSpace() / 2 : orientationHelper.getEnd() / 2;
        return n + n2 - n3;
    }

    private int estimateNextPositionDiffForFling(RecyclerView.LayoutManager layoutManager, OrientationHelper orientationHelper, int n, int n2) {
        int[] nArray = this.calculateScrollDistance(n, n2);
        float f = this.computeDistancePerChild(layoutManager, orientationHelper);
        if (f <= 0.0f) {
            return 0;
        }
        n = Math.abs(nArray[0]) > Math.abs(nArray[1]) ? nArray[0] : nArray[1];
        return Math.round((float)n / f);
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
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager.canScrollVertically()) {
            return this.findCenterView(layoutManager, this.getVerticalHelper(layoutManager));
        }
        if (layoutManager.canScrollHorizontally()) {
            return this.findCenterView(layoutManager, this.getHorizontalHelper(layoutManager));
        }
        return null;
    }

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int n, int n2) {
        int n3;
        if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
            return -1;
        }
        int n4 = layoutManager.getItemCount();
        if (n4 == 0) {
            return -1;
        }
        View view = this.findSnapView(layoutManager);
        if (view == null) {
            return -1;
        }
        int n5 = layoutManager.getPosition(view);
        if (n5 == -1) {
            return -1;
        }
        view = ((RecyclerView.SmoothScroller.ScrollVectorProvider)((Object)layoutManager)).computeScrollVectorForPosition(n4 - 1);
        if (view == null) {
            return -1;
        }
        if (layoutManager.canScrollHorizontally()) {
            n = n3 = this.estimateNextPositionDiffForFling(layoutManager, this.getHorizontalHelper(layoutManager), n, 0);
            if (view.x < 0.0f) {
                n = -n3;
            }
        } else {
            n = 0;
        }
        if (layoutManager.canScrollVertically()) {
            n2 = n3 = this.estimateNextPositionDiffForFling(layoutManager, this.getVerticalHelper(layoutManager), 0, n2);
            if (view.y < 0.0f) {
                n2 = -n3;
            }
        } else {
            n2 = 0;
        }
        if (!layoutManager.canScrollVertically()) {
            n2 = n;
        }
        if (n2 == 0) {
            return -1;
        }
        n = n2 = n5 + n2;
        if (n2 < 0) {
            n = 0;
        }
        n2 = n;
        if (n >= n4) {
            n2 = n4 - 1;
        }
        return n2;
    }
}

