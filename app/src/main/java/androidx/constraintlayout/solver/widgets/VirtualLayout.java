/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.solver.widgets.Guideline;
import androidx.constraintlayout.solver.widgets.HelperWidget;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;

public class VirtualLayout
extends HelperWidget {
    protected BasicMeasure.Measure mMeasure = new BasicMeasure.Measure();
    private int mMeasuredHeight = 0;
    private int mMeasuredWidth = 0;
    BasicMeasure.Measurer mMeasurer = null;
    private boolean mNeedsCallFromSolver = false;
    private int mPaddingBottom = 0;
    private int mPaddingEnd = 0;
    private int mPaddingLeft = 0;
    private int mPaddingRight = 0;
    private int mPaddingStart = 0;
    private int mPaddingTop = 0;
    private int mResolvedPaddingLeft = 0;
    private int mResolvedPaddingRight = 0;

    public void applyRtl(boolean bl) {
        int n = this.mPaddingStart;
        if (n > 0 || this.mPaddingEnd > 0) {
            if (bl) {
                this.mResolvedPaddingLeft = this.mPaddingEnd;
                this.mResolvedPaddingRight = n;
            } else {
                this.mResolvedPaddingLeft = n;
                this.mResolvedPaddingRight = this.mPaddingEnd;
            }
        }
    }

    public void captureWidgets() {
        for (int i = 0; i < this.mWidgetsCount; ++i) {
            ConstraintWidget constraintWidget = this.mWidgets[i];
            if (constraintWidget == null) continue;
            constraintWidget.setInVirtualLayout(true);
        }
    }

    public int getMeasuredHeight() {
        return this.mMeasuredHeight;
    }

    public int getMeasuredWidth() {
        return this.mMeasuredWidth;
    }

    public int getPaddingBottom() {
        return this.mPaddingBottom;
    }

    public int getPaddingLeft() {
        return this.mResolvedPaddingLeft;
    }

    public int getPaddingRight() {
        return this.mResolvedPaddingRight;
    }

    public int getPaddingTop() {
        return this.mPaddingTop;
    }

    public void measure(int n, int n2, int n3, int n4) {
    }

    protected void measure(ConstraintWidget constraintWidget, ConstraintWidget.DimensionBehaviour dimensionBehaviour, int n, ConstraintWidget.DimensionBehaviour dimensionBehaviour2, int n2) {
        while (this.mMeasurer == null && this.getParent() != null) {
            this.mMeasurer = ((ConstraintWidgetContainer)this.getParent()).getMeasurer();
        }
        this.mMeasure.horizontalBehavior = dimensionBehaviour;
        this.mMeasure.verticalBehavior = dimensionBehaviour2;
        this.mMeasure.horizontalDimension = n;
        this.mMeasure.verticalDimension = n2;
        this.mMeasurer.measure(constraintWidget, this.mMeasure);
        constraintWidget.setWidth(this.mMeasure.measuredWidth);
        constraintWidget.setHeight(this.mMeasure.measuredHeight);
        constraintWidget.setHasBaseline(this.mMeasure.measuredHasBaseline);
        constraintWidget.setBaselineDistance(this.mMeasure.measuredBaseline);
    }

    protected boolean measureChildren() {
        BasicMeasure.Measurer measurer = null;
        if (this.mParent != null) {
            measurer = ((ConstraintWidgetContainer)this.mParent).getMeasurer();
        }
        if (measurer == null) {
            return false;
        }
        int n = 0;
        while (true) {
            int n2 = this.mWidgetsCount;
            boolean bl = true;
            if (n >= n2) break;
            ConstraintWidget constraintWidget = this.mWidgets[n];
            if (constraintWidget != null && !(constraintWidget instanceof Guideline)) {
                ConstraintWidget.DimensionBehaviour dimensionBehaviour = constraintWidget.getDimensionBehaviour(0);
                ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = constraintWidget.getDimensionBehaviour(1);
                if (dimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || constraintWidget.mMatchConstraintDefaultWidth == 1 || dimensionBehaviour2 != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || constraintWidget.mMatchConstraintDefaultHeight == 1) {
                    bl = false;
                }
                if (!bl) {
                    ConstraintWidget.DimensionBehaviour dimensionBehaviour3 = dimensionBehaviour;
                    if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                        dimensionBehaviour3 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                    }
                    dimensionBehaviour = dimensionBehaviour2;
                    if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                        dimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                    }
                    this.mMeasure.horizontalBehavior = dimensionBehaviour3;
                    this.mMeasure.verticalBehavior = dimensionBehaviour;
                    this.mMeasure.horizontalDimension = constraintWidget.getWidth();
                    this.mMeasure.verticalDimension = constraintWidget.getHeight();
                    measurer.measure(constraintWidget, this.mMeasure);
                    constraintWidget.setWidth(this.mMeasure.measuredWidth);
                    constraintWidget.setHeight(this.mMeasure.measuredHeight);
                    constraintWidget.setBaselineDistance(this.mMeasure.measuredBaseline);
                }
            }
            ++n;
        }
        return true;
    }

    public boolean needSolverPass() {
        return this.mNeedsCallFromSolver;
    }

    protected void needsCallbackFromSolver(boolean bl) {
        this.mNeedsCallFromSolver = bl;
    }

    public void setMeasure(int n, int n2) {
        this.mMeasuredWidth = n;
        this.mMeasuredHeight = n2;
    }

    public void setPadding(int n) {
        this.mPaddingLeft = n;
        this.mPaddingTop = n;
        this.mPaddingRight = n;
        this.mPaddingBottom = n;
        this.mPaddingStart = n;
        this.mPaddingEnd = n;
    }

    public void setPaddingBottom(int n) {
        this.mPaddingBottom = n;
    }

    public void setPaddingEnd(int n) {
        this.mPaddingEnd = n;
    }

    public void setPaddingLeft(int n) {
        this.mPaddingLeft = n;
        this.mResolvedPaddingLeft = n;
    }

    public void setPaddingRight(int n) {
        this.mPaddingRight = n;
        this.mResolvedPaddingRight = n;
    }

    public void setPaddingStart(int n) {
        this.mPaddingStart = n;
        this.mResolvedPaddingLeft = n;
        this.mResolvedPaddingRight = n;
    }

    public void setPaddingTop(int n) {
        this.mPaddingTop = n;
    }

    @Override
    public void updateConstraints(ConstraintWidgetContainer constraintWidgetContainer) {
        this.captureWidgets();
    }
}

