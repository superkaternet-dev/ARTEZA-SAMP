/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.view.ViewParent
 */
package androidx.constraintlayout.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewParent;
import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.R;

public abstract class VirtualLayout
extends ConstraintHelper {
    private boolean mApplyElevationOnAttach;
    private boolean mApplyVisibilityOnAttach;

    public VirtualLayout(Context context) {
        super(context);
    }

    public VirtualLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public VirtualLayout(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    @Override
    protected void init(AttributeSet attributeSet) {
        super.init(attributeSet);
        if (attributeSet != null) {
            attributeSet = this.getContext().obtainStyledAttributes(attributeSet, R.styleable.ConstraintLayout_Layout);
            int n = attributeSet.getIndexCount();
            for (int i = 0; i < n; ++i) {
                int n2 = attributeSet.getIndex(i);
                if (n2 == R.styleable.ConstraintLayout_Layout_android_visibility) {
                    this.mApplyVisibilityOnAttach = true;
                    continue;
                }
                if (n2 != R.styleable.ConstraintLayout_Layout_android_elevation) continue;
                this.mApplyElevationOnAttach = true;
            }
            attributeSet.recycle();
        }
    }

    @Override
    public void onAttachedToWindow() {
        ViewParent viewParent;
        super.onAttachedToWindow();
        if ((this.mApplyVisibilityOnAttach || this.mApplyElevationOnAttach) && (viewParent = this.getParent()) != null && viewParent instanceof ConstraintLayout) {
            ConstraintLayout constraintLayout = (ConstraintLayout)viewParent;
            int n = this.getVisibility();
            float f = 0.0f;
            if (Build.VERSION.SDK_INT >= 21) {
                f = this.getElevation();
            }
            for (int i = 0; i < this.mCount; ++i) {
                viewParent = constraintLayout.getViewById(this.mIds[i]);
                if (viewParent == null) continue;
                if (this.mApplyVisibilityOnAttach) {
                    viewParent.setVisibility(n);
                }
                if (!this.mApplyElevationOnAttach || !(f > 0.0f) || Build.VERSION.SDK_INT < 21) continue;
                viewParent.setTranslationZ(viewParent.getTranslationZ() + f);
            }
        }
    }

    public void onMeasure(androidx.constraintlayout.solver.widgets.VirtualLayout virtualLayout, int n, int n2) {
    }

    public void setElevation(float f) {
        super.setElevation(f);
        this.applyLayoutFeatures();
    }

    public void setVisibility(int n) {
        super.setVisibility(n);
        this.applyLayoutFeatures();
    }
}

