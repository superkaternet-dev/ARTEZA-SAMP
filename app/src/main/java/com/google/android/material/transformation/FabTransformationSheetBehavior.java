/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.view.View
 */
package com.google.android.material.transformation;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.animation.Positioning;
import com.google.android.material.transformation.FabTransformationBehavior;
import com.google.android.material.transformation.FabTransformationScrimBehavior;
import java.util.HashMap;
import java.util.Map;

public class FabTransformationSheetBehavior
extends FabTransformationBehavior {
    private Map<View, Integer> importantForAccessibilityMap;

    public FabTransformationSheetBehavior() {
    }

    public FabTransformationSheetBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private void updateImportantForAccessibility(View view, boolean bl) {
        Object object = view.getParent();
        if (!(object instanceof CoordinatorLayout)) {
            return;
        }
        object = (CoordinatorLayout)object;
        int n = object.getChildCount();
        if (Build.VERSION.SDK_INT >= 16 && bl) {
            this.importantForAccessibilityMap = new HashMap<View, Integer>(n);
        }
        for (int i = 0; i < n; ++i) {
            View view2 = object.getChildAt(i);
            boolean bl2 = view2.getLayoutParams() instanceof CoordinatorLayout.LayoutParams && ((CoordinatorLayout.LayoutParams)view2.getLayoutParams()).getBehavior() instanceof FabTransformationScrimBehavior;
            if (view2 == view || bl2) continue;
            if (!bl) {
                Map<View, Integer> map = this.importantForAccessibilityMap;
                if (map == null || !map.containsKey(view2)) continue;
                ViewCompat.setImportantForAccessibility(view2, this.importantForAccessibilityMap.get(view2));
                continue;
            }
            if (Build.VERSION.SDK_INT >= 16) {
                this.importantForAccessibilityMap.put(view2, view2.getImportantForAccessibility());
            }
            ViewCompat.setImportantForAccessibility(view2, 4);
        }
        if (!bl) {
            this.importantForAccessibilityMap = null;
        }
    }

    @Override
    protected FabTransformationBehavior.FabTransformationSpec onCreateMotionSpec(Context context, boolean bl) {
        int n = bl ? R.animator.mtrl_fab_transformation_sheet_expand_spec : R.animator.mtrl_fab_transformation_sheet_collapse_spec;
        FabTransformationBehavior.FabTransformationSpec fabTransformationSpec = new FabTransformationBehavior.FabTransformationSpec();
        fabTransformationSpec.timings = MotionSpec.createFromResource(context, n);
        fabTransformationSpec.positioning = new Positioning(17, 0.0f, 0.0f);
        return fabTransformationSpec;
    }

    @Override
    protected boolean onExpandedStateChange(View view, View view2, boolean bl, boolean bl2) {
        this.updateImportantForAccessibility(view2, bl);
        return super.onExpandedStateChange(view, view2, bl, bl2);
    }
}

