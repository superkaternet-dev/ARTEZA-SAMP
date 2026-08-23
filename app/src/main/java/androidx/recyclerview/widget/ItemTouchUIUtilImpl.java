/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Canvas
 *  android.os.Build$VERSION
 *  android.view.View
 */
package androidx.recyclerview.widget;

import android.graphics.Canvas;
import android.os.Build;
import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.R;
import androidx.recyclerview.widget.ItemTouchUIUtil;
import androidx.recyclerview.widget.RecyclerView;

class ItemTouchUIUtilImpl
implements ItemTouchUIUtil {
    static final ItemTouchUIUtil INSTANCE = new ItemTouchUIUtilImpl();

    ItemTouchUIUtilImpl() {
    }

    private static float findMaxElevation(RecyclerView recyclerView, View view) {
        int n = recyclerView.getChildCount();
        float f = 0.0f;
        for (int i = 0; i < n; ++i) {
            float f2;
            View view2 = recyclerView.getChildAt(i);
            if (view2 == view) {
                f2 = f;
            } else {
                float f3 = ViewCompat.getElevation(view2);
                f2 = f;
                if (f3 > f) {
                    f2 = f3;
                }
            }
            f = f2;
        }
        return f;
    }

    @Override
    public void clearView(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            Object object = view.getTag(R.id.item_touch_helper_previous_elevation);
            if (object != null && object instanceof Float) {
                ViewCompat.setElevation(view, ((Float)object).floatValue());
            }
            view.setTag(R.id.item_touch_helper_previous_elevation, null);
        }
        view.setTranslationX(0.0f);
        view.setTranslationY(0.0f);
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView recyclerView, View view, float f, float f2, int n, boolean bl) {
        if (Build.VERSION.SDK_INT >= 21 && bl && view.getTag(R.id.item_touch_helper_previous_elevation) == null) {
            float f3 = ViewCompat.getElevation(view);
            ViewCompat.setElevation(view, ItemTouchUIUtilImpl.findMaxElevation(recyclerView, view) + 1.0f);
            view.setTag(R.id.item_touch_helper_previous_elevation, (Object)Float.valueOf(f3));
        }
        view.setTranslationX(f);
        view.setTranslationY(f2);
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView recyclerView, View view, float f, float f2, int n, boolean bl) {
    }

    @Override
    public void onSelected(View view) {
    }
}

