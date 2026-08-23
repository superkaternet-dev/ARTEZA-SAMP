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

class ScrollbarHelper {
    private ScrollbarHelper() {
    }

    static int computeScrollExtent(RecyclerView.State state, OrientationHelper orientationHelper, View view, View view2, RecyclerView.LayoutManager layoutManager, boolean bl) {
        if (layoutManager.getChildCount() != 0 && state.getItemCount() != 0 && view != null && view2 != null) {
            if (!bl) {
                return Math.abs(layoutManager.getPosition(view) - layoutManager.getPosition(view2)) + 1;
            }
            int n = orientationHelper.getDecoratedEnd(view2);
            int n2 = orientationHelper.getDecoratedStart(view);
            return Math.min(orientationHelper.getTotalSpace(), n - n2);
        }
        return 0;
    }

    static int computeScrollOffset(RecyclerView.State state, OrientationHelper orientationHelper, View view, View view2, RecyclerView.LayoutManager layoutManager, boolean bl, boolean bl2) {
        if (layoutManager.getChildCount() != 0 && state.getItemCount() != 0 && view != null && view2 != null) {
            int n = Math.min(layoutManager.getPosition(view), layoutManager.getPosition(view2));
            int n2 = Math.max(layoutManager.getPosition(view), layoutManager.getPosition(view2));
            n2 = bl2 ? Math.max(0, state.getItemCount() - n2 - 1) : Math.max(0, n);
            if (!bl) {
                return n2;
            }
            int n3 = Math.abs(orientationHelper.getDecoratedEnd(view2) - orientationHelper.getDecoratedStart(view));
            n = Math.abs(layoutManager.getPosition(view) - layoutManager.getPosition(view2));
            float f = (float)n3 / (float)(n + 1);
            return Math.round((float)n2 * f + (float)(orientationHelper.getStartAfterPadding() - orientationHelper.getDecoratedStart(view)));
        }
        return 0;
    }

    static int computeScrollRange(RecyclerView.State state, OrientationHelper orientationHelper, View view, View view2, RecyclerView.LayoutManager layoutManager, boolean bl) {
        if (layoutManager.getChildCount() != 0 && state.getItemCount() != 0 && view != null && view2 != null) {
            if (!bl) {
                return state.getItemCount();
            }
            int n = orientationHelper.getDecoratedEnd(view2);
            int n2 = orientationHelper.getDecoratedStart(view);
            int n3 = Math.abs(layoutManager.getPosition(view) - layoutManager.getPosition(view2));
            return (int)((float)(n - n2) / (float)(n3 + 1) * (float)state.getItemCount());
        }
        return 0;
    }
}

