/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Rect
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$MeasureSpec
 */
package com.google.android.material.appbar;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.appbar.ViewOffsetBehavior;
import java.util.List;

abstract class HeaderScrollingViewBehavior
extends ViewOffsetBehavior<View> {
    private int overlayTop;
    final Rect tempRect1 = new Rect();
    final Rect tempRect2 = new Rect();
    private int verticalLayoutGap = 0;

    public HeaderScrollingViewBehavior() {
    }

    public HeaderScrollingViewBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private static int resolveGravity(int n) {
        block0: {
            if (n != 0) break block0;
            n = 0x800033;
        }
        return n;
    }

    abstract View findFirstDependency(List<View> var1);

    final int getOverlapPixelsForOffset(View view) {
        int n = this.overlayTop;
        int n2 = 0;
        if (n != 0) {
            float f = this.getOverlapRatioForOffset(view);
            n2 = this.overlayTop;
            n2 = MathUtils.clamp((int)(f * (float)n2), 0, n2);
        }
        return n2;
    }

    float getOverlapRatioForOffset(View view) {
        return 1.0f;
    }

    public final int getOverlayTop() {
        return this.overlayTop;
    }

    int getScrollRange(View view) {
        return view.getMeasuredHeight();
    }

    final int getVerticalLayoutGap() {
        return this.verticalLayoutGap;
    }

    @Override
    protected void layoutChild(CoordinatorLayout coordinatorLayout, View view, int n) {
        View view2 = this.findFirstDependency(coordinatorLayout.getDependencies(view));
        if (view2 != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)view.getLayoutParams();
            Rect rect = this.tempRect1;
            rect.set(coordinatorLayout.getPaddingLeft() + layoutParams.leftMargin, view2.getBottom() + layoutParams.topMargin, coordinatorLayout.getWidth() - coordinatorLayout.getPaddingRight() - layoutParams.rightMargin, coordinatorLayout.getHeight() + view2.getBottom() - coordinatorLayout.getPaddingBottom() - layoutParams.bottomMargin);
            WindowInsetsCompat windowInsetsCompat = coordinatorLayout.getLastWindowInsets();
            if (windowInsetsCompat != null && ViewCompat.getFitsSystemWindows((View)coordinatorLayout) && !ViewCompat.getFitsSystemWindows(view)) {
                rect.left += windowInsetsCompat.getSystemWindowInsetLeft();
                rect.right -= windowInsetsCompat.getSystemWindowInsetRight();
            }
            coordinatorLayout = this.tempRect2;
            GravityCompat.apply(HeaderScrollingViewBehavior.resolveGravity(layoutParams.gravity), view.getMeasuredWidth(), view.getMeasuredHeight(), rect, (Rect)coordinatorLayout, n);
            n = this.getOverlapPixelsForOffset(view2);
            view.layout(((Rect)coordinatorLayout).left, ((Rect)coordinatorLayout).top - n, ((Rect)coordinatorLayout).right, ((Rect)coordinatorLayout).bottom - n);
            this.verticalLayoutGap = ((Rect)coordinatorLayout).top - view2.getBottom();
        } else {
            super.layoutChild(coordinatorLayout, view, n);
            this.verticalLayoutGap = 0;
        }
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout coordinatorLayout, View view, int n, int n2, int n3, int n4) {
        int n5 = view.getLayoutParams().height;
        if (n5 == -1 || n5 == -2) {
            View view2 = this.findFirstDependency(coordinatorLayout.getDependencies(view));
            if (view2 != null) {
                if (ViewCompat.getFitsSystemWindows(view2) && !ViewCompat.getFitsSystemWindows(view)) {
                    ViewCompat.setFitsSystemWindows(view, true);
                    if (ViewCompat.getFitsSystemWindows(view)) {
                        view.requestLayout();
                        return true;
                    }
                }
                if ((n3 = View.MeasureSpec.getSize((int)n3)) == 0) {
                    n3 = coordinatorLayout.getHeight();
                }
                int n6 = view2.getMeasuredHeight();
                int n7 = this.getScrollRange(view2);
                n5 = n5 == -1 ? 0x40000000 : Integer.MIN_VALUE;
                coordinatorLayout.onMeasureChild(view, n, n2, View.MeasureSpec.makeMeasureSpec((int)(n3 - n6 + n7), (int)n5), n4);
                return true;
            }
        }
        return false;
    }

    public final void setOverlayTop(int n) {
        this.overlayTop = n;
    }
}

