/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.SparseArray
 */
package androidx.constraintlayout.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.solver.widgets.HelperWidget;
import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.R;

public class Barrier
extends ConstraintHelper {
    public static final int BOTTOM = 3;
    public static final int END = 6;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int START = 5;
    public static final int TOP = 2;
    private androidx.constraintlayout.solver.widgets.Barrier mBarrier;
    private int mIndicatedType;
    private int mResolvedType;

    public Barrier(Context context) {
        super(context);
        super.setVisibility(8);
    }

    public Barrier(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        super.setVisibility(8);
    }

    public Barrier(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        super.setVisibility(8);
    }

    private void updateType(ConstraintWidget constraintWidget, int n, boolean bl) {
        this.mResolvedType = n;
        if (Build.VERSION.SDK_INT < 17) {
            n = this.mIndicatedType;
            if (n == 5) {
                this.mResolvedType = 0;
            } else if (n == 6) {
                this.mResolvedType = 1;
            }
        } else if (bl) {
            n = this.mIndicatedType;
            if (n == 5) {
                this.mResolvedType = 1;
            } else if (n == 6) {
                this.mResolvedType = 0;
            }
        } else {
            n = this.mIndicatedType;
            if (n == 5) {
                this.mResolvedType = 0;
            } else if (n == 6) {
                this.mResolvedType = 1;
            }
        }
        if (constraintWidget instanceof androidx.constraintlayout.solver.widgets.Barrier) {
            ((androidx.constraintlayout.solver.widgets.Barrier)constraintWidget).setBarrierType(this.mResolvedType);
        }
    }

    public boolean allowsGoneWidget() {
        return this.mBarrier.allowsGoneWidget();
    }

    public int getMargin() {
        return this.mBarrier.getMargin();
    }

    public int getType() {
        return this.mIndicatedType;
    }

    @Override
    protected void init(AttributeSet attributeSet) {
        super.init(attributeSet);
        this.mBarrier = new androidx.constraintlayout.solver.widgets.Barrier();
        if (attributeSet != null) {
            attributeSet = this.getContext().obtainStyledAttributes(attributeSet, R.styleable.ConstraintLayout_Layout);
            int n = attributeSet.getIndexCount();
            for (int i = 0; i < n; ++i) {
                int n2 = attributeSet.getIndex(i);
                if (n2 == R.styleable.ConstraintLayout_Layout_barrierDirection) {
                    this.setType(attributeSet.getInt(n2, 0));
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_barrierAllowsGoneWidgets) {
                    this.mBarrier.setAllowsGoneWidget(attributeSet.getBoolean(n2, true));
                    continue;
                }
                if (n2 != R.styleable.ConstraintLayout_Layout_barrierMargin) continue;
                n2 = attributeSet.getDimensionPixelSize(n2, 0);
                this.mBarrier.setMargin(n2);
            }
            attributeSet.recycle();
        }
        this.mHelperWidget = this.mBarrier;
        this.validateParams();
    }

    @Override
    public void loadParameters(ConstraintSet.Constraint constraint, HelperWidget helperWidget, ConstraintLayout.LayoutParams object, SparseArray<ConstraintWidget> sparseArray) {
        super.loadParameters(constraint, helperWidget, (ConstraintLayout.LayoutParams)((Object)object), sparseArray);
        if (helperWidget instanceof androidx.constraintlayout.solver.widgets.Barrier) {
            object = (androidx.constraintlayout.solver.widgets.Barrier)helperWidget;
            boolean bl = ((ConstraintWidgetContainer)helperWidget.getParent()).isRtl();
            this.updateType((ConstraintWidget)object, constraint.layout.mBarrierDirection, bl);
            ((androidx.constraintlayout.solver.widgets.Barrier)object).setAllowsGoneWidget(constraint.layout.mBarrierAllowsGoneWidgets);
            ((androidx.constraintlayout.solver.widgets.Barrier)object).setMargin(constraint.layout.mBarrierMargin);
        }
    }

    @Override
    public void resolveRtl(ConstraintWidget constraintWidget, boolean bl) {
        this.updateType(constraintWidget, this.mIndicatedType, bl);
    }

    public void setAllowsGoneWidget(boolean bl) {
        this.mBarrier.setAllowsGoneWidget(bl);
    }

    public void setDpMargin(int n) {
        float f = this.getResources().getDisplayMetrics().density;
        n = (int)((float)n * f + 0.5f);
        this.mBarrier.setMargin(n);
    }

    public void setMargin(int n) {
        this.mBarrier.setMargin(n);
    }

    public void setType(int n) {
        this.mIndicatedType = n;
    }
}

