/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.Helper;
import androidx.constraintlayout.solver.widgets.analyzer.Dependency;
import androidx.constraintlayout.solver.widgets.analyzer.DependencyNode;
import androidx.constraintlayout.solver.widgets.analyzer.WidgetRun;

public class HorizontalWidgetRun
extends WidgetRun {
    private static int[] tempDimensions = new int[2];

    public HorizontalWidgetRun(ConstraintWidget constraintWidget) {
        super(constraintWidget);
        this.start.type = DependencyNode.Type.LEFT;
        this.end.type = DependencyNode.Type.RIGHT;
        this.orientation = 0;
    }

    private void computeInsetRatio(int[] nArray, int n, int n2, int n3, int n4, float f, int n5) {
        n = n2 - n;
        n2 = n4 - n3;
        switch (n5) {
            default: {
                break;
            }
            case 1: {
                n2 = (int)((float)n * f + 0.5f);
                nArray[0] = n;
                nArray[1] = n2;
                break;
            }
            case 0: {
                nArray[0] = (int)((float)n2 * f + 0.5f);
                nArray[1] = n2;
                break;
            }
            case -1: {
                n3 = (int)((float)n2 * f + 0.5f);
                n4 = (int)((float)n / f + 0.5f);
                if (n3 <= n && n2 <= n2) {
                    nArray[0] = n3;
                    nArray[1] = n2;
                    break;
                }
                if (n > n || n4 > n2) break;
                nArray[0] = n;
                nArray[1] = n4;
            }
        }
    }

    @Override
    void apply() {
        Object object;
        if (this.widget.measured) {
            this.dimension.resolve(this.widget.getWidth());
        }
        if (!this.dimension.resolved) {
            this.dimensionBehavior = this.widget.getHorizontalDimensionBehaviour();
            if (this.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_PARENT && ((object = this.widget.getParent()) != null && ((ConstraintWidget)object).getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED || ((ConstraintWidget)object).getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_PARENT)) {
                    int n = ((ConstraintWidget)object).getWidth();
                    int n2 = this.widget.mLeft.getMargin();
                    int n3 = this.widget.mRight.getMargin();
                    this.addTarget(this.start, ((ConstraintWidget)object).horizontalRun.start, this.widget.mLeft.getMargin());
                    this.addTarget(this.end, ((ConstraintWidget)object).horizontalRun.end, -this.widget.mRight.getMargin());
                    this.dimension.resolve(n - n2 - n3);
                    return;
                }
                if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.FIXED) {
                    this.dimension.resolve(this.widget.getWidth());
                }
            }
        } else if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_PARENT && ((object = this.widget.getParent()) != null && ((ConstraintWidget)object).getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED || ((ConstraintWidget)object).getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_PARENT)) {
            this.addTarget(this.start, ((ConstraintWidget)object).horizontalRun.start, this.widget.mLeft.getMargin());
            this.addTarget(this.end, ((ConstraintWidget)object).horizontalRun.end, -this.widget.mRight.getMargin());
            return;
        }
        if (this.dimension.resolved && this.widget.measured) {
            if (this.widget.mListAnchors[0].mTarget != null && this.widget.mListAnchors[1].mTarget != null) {
                if (this.widget.isInHorizontalChain()) {
                    this.start.margin = this.widget.mListAnchors[0].getMargin();
                    this.end.margin = -this.widget.mListAnchors[1].getMargin();
                } else {
                    object = this.getTarget(this.widget.mListAnchors[0]);
                    if (object != null) {
                        this.addTarget(this.start, (DependencyNode)object, this.widget.mListAnchors[0].getMargin());
                    }
                    if ((object = this.getTarget(this.widget.mListAnchors[1])) != null) {
                        this.addTarget(this.end, (DependencyNode)object, -this.widget.mListAnchors[1].getMargin());
                    }
                    this.start.delegateToWidgetRun = true;
                    this.end.delegateToWidgetRun = true;
                }
            } else if (this.widget.mListAnchors[0].mTarget != null) {
                object = this.getTarget(this.widget.mListAnchors[0]);
                if (object != null) {
                    this.addTarget(this.start, (DependencyNode)object, this.widget.mListAnchors[0].getMargin());
                    this.addTarget(this.end, this.start, this.dimension.value);
                }
            } else if (this.widget.mListAnchors[1].mTarget != null) {
                object = this.getTarget(this.widget.mListAnchors[1]);
                if (object != null) {
                    this.addTarget(this.end, (DependencyNode)object, -this.widget.mListAnchors[1].getMargin());
                    this.addTarget(this.start, this.end, -this.dimension.value);
                }
            } else if (!(this.widget instanceof Helper) && this.widget.getParent() != null && this.widget.getAnchor((ConstraintAnchor.Type)ConstraintAnchor.Type.CENTER).mTarget == null) {
                object = this.widget.getParent().horizontalRun.start;
                this.addTarget(this.start, (DependencyNode)object, this.widget.getX());
                this.addTarget(this.end, this.start, this.dimension.value);
            }
        } else {
            if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                switch (this.widget.mMatchConstraintDefaultWidth) {
                    default: {
                        break;
                    }
                    case 3: {
                        if (this.widget.mMatchConstraintDefaultHeight == 3) {
                            this.start.updateDelegate = this;
                            this.end.updateDelegate = this;
                            this.widget.verticalRun.start.updateDelegate = this;
                            this.widget.verticalRun.end.updateDelegate = this;
                            this.dimension.updateDelegate = this;
                            if (this.widget.isInVerticalChain()) {
                                this.dimension.targets.add(this.widget.verticalRun.dimension);
                                this.widget.verticalRun.dimension.dependencies.add(this.dimension);
                                this.widget.verticalRun.dimension.updateDelegate = this;
                                this.dimension.targets.add(this.widget.verticalRun.start);
                                this.dimension.targets.add(this.widget.verticalRun.end);
                                this.widget.verticalRun.start.dependencies.add(this.dimension);
                                this.widget.verticalRun.end.dependencies.add(this.dimension);
                                break;
                            }
                            if (this.widget.isInHorizontalChain()) {
                                this.widget.verticalRun.dimension.targets.add(this.dimension);
                                this.dimension.dependencies.add(this.widget.verticalRun.dimension);
                                break;
                            }
                            this.widget.verticalRun.dimension.targets.add(this.dimension);
                            break;
                        }
                        object = this.widget.verticalRun.dimension;
                        this.dimension.targets.add(object);
                        ((DependencyNode)object).dependencies.add(this.dimension);
                        this.widget.verticalRun.start.dependencies.add(this.dimension);
                        this.widget.verticalRun.end.dependencies.add(this.dimension);
                        this.dimension.delegateToWidgetRun = true;
                        this.dimension.dependencies.add(this.start);
                        this.dimension.dependencies.add(this.end);
                        this.start.targets.add(this.dimension);
                        this.end.targets.add(this.dimension);
                        break;
                    }
                    case 2: {
                        object = this.widget.getParent();
                        if (object == null) break;
                        object = ((ConstraintWidget)object).verticalRun.dimension;
                        this.dimension.targets.add(object);
                        ((DependencyNode)object).dependencies.add(this.dimension);
                        this.dimension.delegateToWidgetRun = true;
                        this.dimension.dependencies.add(this.start);
                        this.dimension.dependencies.add(this.end);
                    }
                }
            }
            if (this.widget.mListAnchors[0].mTarget != null && this.widget.mListAnchors[1].mTarget != null) {
                if (this.widget.isInHorizontalChain()) {
                    this.start.margin = this.widget.mListAnchors[0].getMargin();
                    this.end.margin = -this.widget.mListAnchors[1].getMargin();
                } else {
                    object = this.getTarget(this.widget.mListAnchors[0]);
                    DependencyNode dependencyNode = this.getTarget(this.widget.mListAnchors[1]);
                    ((DependencyNode)object).addDependency(this);
                    dependencyNode.addDependency(this);
                    this.mRunType = WidgetRun.RunType.CENTER;
                }
            } else if (this.widget.mListAnchors[0].mTarget != null) {
                object = this.getTarget(this.widget.mListAnchors[0]);
                if (object != null) {
                    this.addTarget(this.start, (DependencyNode)object, this.widget.mListAnchors[0].getMargin());
                    this.addTarget(this.end, this.start, 1, this.dimension);
                }
            } else if (this.widget.mListAnchors[1].mTarget != null) {
                object = this.getTarget(this.widget.mListAnchors[1]);
                if (object != null) {
                    this.addTarget(this.end, (DependencyNode)object, -this.widget.mListAnchors[1].getMargin());
                    this.addTarget(this.start, this.end, -1, this.dimension);
                }
            } else if (!(this.widget instanceof Helper) && this.widget.getParent() != null) {
                object = this.widget.getParent().horizontalRun.start;
                this.addTarget(this.start, (DependencyNode)object, this.widget.getX());
                this.addTarget(this.end, this.start, 1, this.dimension);
            }
        }
    }

    @Override
    public void applyToWidget() {
        if (this.start.resolved) {
            this.widget.setX(this.start.value);
        }
    }

    @Override
    void clear() {
        this.runGroup = null;
        this.start.clear();
        this.end.clear();
        this.dimension.clear();
        this.resolved = false;
    }

    @Override
    void reset() {
        this.resolved = false;
        this.start.clear();
        this.start.resolved = false;
        this.end.clear();
        this.end.resolved = false;
        this.dimension.resolved = false;
    }

    @Override
    boolean supportsWrapComputation() {
        if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            return this.widget.mMatchConstraintDefaultWidth == 0;
        }
        return true;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("HorizontalRun ");
        stringBuilder.append(this.widget.getDebugName());
        return stringBuilder.toString();
    }

    @Override
    public void update(Dependency object) {
        float f;
        int n;
        int n2;
        DependencyNode dependencyNode;
        int n3;
        switch (1.$SwitchMap$androidx$constraintlayout$solver$widgets$analyzer$WidgetRun$RunType[this.mRunType.ordinal()]) {
            default: {
                break;
            }
            case 3: {
                this.updateRunCenter((Dependency)object, this.widget.mLeft, this.widget.mRight, 0);
                return;
            }
            case 2: {
                this.updateRunEnd((Dependency)object);
                break;
            }
            case 1: {
                this.updateRunStart((Dependency)object);
            }
        }
        if (!this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            block5 : switch (this.widget.mMatchConstraintDefaultWidth) {
                default: {
                    break;
                }
                case 3: {
                    if (this.widget.mMatchConstraintDefaultHeight != 0 && this.widget.mMatchConstraintDefaultHeight != 3) {
                        n3 = 0;
                        switch (this.widget.getDimensionRatioSide()) {
                            default: {
                                break;
                            }
                            case 1: {
                                n3 = (int)((float)this.widget.verticalRun.dimension.value * this.widget.getDimensionRatio() + 0.5f);
                                break;
                            }
                            case 0: {
                                n3 = (int)((float)this.widget.verticalRun.dimension.value / this.widget.getDimensionRatio() + 0.5f);
                                break;
                            }
                            case -1: {
                                n3 = (int)((float)this.widget.verticalRun.dimension.value * this.widget.getDimensionRatio() + 0.5f);
                            }
                        }
                        this.dimension.resolve(n3);
                        break;
                    }
                    object = this.widget.verticalRun.start;
                    dependencyNode = this.widget.verticalRun.end;
                    n3 = this.widget.mLeft.mTarget != null ? 1 : 0;
                    n2 = this.widget.mTop.mTarget != null ? 1 : 0;
                    n = this.widget.mRight.mTarget != null ? 1 : 0;
                    int n4 = this.widget.mBottom.mTarget != null ? 1 : 0;
                    int n5 = this.widget.getDimensionRatioSide();
                    if (n3 != 0 && n2 != 0 && n != 0 && n4 != 0) {
                        int n6;
                        int n7;
                        int n8;
                        int n9;
                        f = this.widget.getDimensionRatio();
                        if (((DependencyNode)object).resolved && dependencyNode.resolved) {
                            if (this.start.readyToSolve && this.end.readyToSolve) {
                                n3 = this.start.targets.get((int)0).value;
                                int n10 = this.start.margin;
                                n2 = this.end.targets.get((int)0).value;
                                int n11 = this.end.margin;
                                n = ((DependencyNode)object).value;
                                int n12 = ((DependencyNode)object).margin;
                                n4 = dependencyNode.value;
                                int n13 = dependencyNode.margin;
                                this.computeInsetRatio(tempDimensions, n3 + n10, n2 - n11, n + n12, n4 - n13, f, n5);
                                this.dimension.resolve(tempDimensions[0]);
                                this.widget.verticalRun.dimension.resolve(tempDimensions[1]);
                                return;
                            }
                            return;
                        }
                        if (this.start.resolved && this.end.resolved) {
                            if (((DependencyNode)object).readyToSolve && dependencyNode.readyToSolve) {
                                n2 = this.start.value;
                                n9 = this.start.margin;
                                n3 = this.end.value;
                                n8 = this.end.margin;
                                n7 = ((DependencyNode)object).targets.get((int)0).value;
                                n = ((DependencyNode)object).margin;
                                n6 = dependencyNode.targets.get((int)0).value;
                                n4 = dependencyNode.margin;
                                this.computeInsetRatio(tempDimensions, n2 + n9, n3 - n8, n7 + n, n6 - n4, f, n5);
                                this.dimension.resolve(tempDimensions[0]);
                                this.widget.verticalRun.dimension.resolve(tempDimensions[1]);
                            } else {
                                return;
                            }
                        }
                        if (this.start.readyToSolve && this.end.readyToSolve && ((DependencyNode)object).readyToSolve && dependencyNode.readyToSolve) {
                            n = this.start.targets.get((int)0).value;
                            n2 = this.start.margin;
                            n6 = this.end.targets.get((int)0).value;
                            n3 = this.end.margin;
                            n8 = ((DependencyNode)object).targets.get((int)0).value;
                            n4 = ((DependencyNode)object).margin;
                            n9 = dependencyNode.targets.get((int)0).value;
                            n7 = dependencyNode.margin;
                            this.computeInsetRatio(tempDimensions, n + n2, n6 - n3, n8 + n4, n9 - n7, f, n5);
                            this.dimension.resolve(tempDimensions[0]);
                            this.widget.verticalRun.dimension.resolve(tempDimensions[1]);
                            break;
                        }
                        return;
                    }
                    if (n3 != 0 && n != 0) {
                        if (this.start.readyToSolve && this.end.readyToSolve) {
                            f = this.widget.getDimensionRatio();
                            n2 = this.start.targets.get((int)0).value + this.start.margin;
                            n3 = this.end.targets.get((int)0).value - this.end.margin;
                            switch (n5) {
                                default: {
                                    break block5;
                                }
                                case 1: {
                                    n3 = this.getLimitedDimension(n3 - n2, 0);
                                    n = (int)((float)n3 / f + 0.5f);
                                    n2 = this.getLimitedDimension(n, 1);
                                    if (n != n2) {
                                        n3 = (int)((float)n2 * f + 0.5f);
                                    }
                                    this.dimension.resolve(n3);
                                    this.widget.verticalRun.dimension.resolve(n2);
                                    break block5;
                                }
                                case -1: 
                                case 0: 
                            }
                            n3 = this.getLimitedDimension(n3 - n2, 0);
                            n = (int)((float)n3 * f + 0.5f);
                            n2 = this.getLimitedDimension(n, 1);
                            if (n != n2) {
                                n3 = (int)((float)n2 / f + 0.5f);
                            }
                            this.dimension.resolve(n3);
                            this.widget.verticalRun.dimension.resolve(n2);
                            break;
                        }
                        return;
                    }
                    if (n2 == 0 || n4 == 0) break;
                    if (((DependencyNode)object).readyToSolve && dependencyNode.readyToSolve) {
                        f = this.widget.getDimensionRatio();
                        n3 = ((DependencyNode)object).targets.get((int)0).value + ((DependencyNode)object).margin;
                        n2 = dependencyNode.targets.get((int)0).value - dependencyNode.margin;
                        switch (n5) {
                            default: {
                                break block5;
                            }
                            case 0: {
                                n3 = this.getLimitedDimension(n2 - n3, 1);
                                n = (int)((float)n3 * f + 0.5f);
                                n2 = this.getLimitedDimension(n, 0);
                                if (n != n2) {
                                    n3 = (int)((float)n2 / f + 0.5f);
                                }
                                this.dimension.resolve(n2);
                                this.widget.verticalRun.dimension.resolve(n3);
                                break block5;
                            }
                            case -1: 
                            case 1: 
                        }
                        n3 = this.getLimitedDimension(n2 - n3, 1);
                        n = (int)((float)n3 / f + 0.5f);
                        n2 = this.getLimitedDimension(n, 0);
                        if (n != n2) {
                            n3 = (int)((float)n2 * f + 0.5f);
                        }
                        this.dimension.resolve(n2);
                        this.widget.verticalRun.dimension.resolve(n3);
                        break;
                    }
                    return;
                }
                case 2: {
                    object = this.widget.getParent();
                    if (object == null || !((ConstraintWidget)object).horizontalRun.dimension.resolved) break;
                    f = this.widget.mMatchConstraintPercentWidth;
                    n3 = (int)((float)((ConstraintWidget)object).horizontalRun.dimension.value * f + 0.5f);
                    this.dimension.resolve(n3);
                }
            }
        }
        if (this.start.readyToSolve && this.end.readyToSolve) {
            if (this.start.resolved && this.end.resolved && this.dimension.resolved) {
                return;
            }
            if (!this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.widget.mMatchConstraintDefaultWidth == 0 && !this.widget.isInHorizontalChain()) {
                object = this.start.targets.get(0);
                dependencyNode = this.end.targets.get(0);
                n2 = ((DependencyNode)object).value + this.start.margin;
                n3 = dependencyNode.value + this.end.margin;
                this.start.resolve(n2);
                this.end.resolve(n3);
                this.dimension.resolve(n3 - n2);
                return;
            }
            if (!this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.matchConstraintsType == 1 && this.start.targets.size() > 0 && this.end.targets.size() > 0) {
                object = this.start.targets.get(0);
                dependencyNode = this.end.targets.get(0);
                n3 = ((DependencyNode)object).value;
                n2 = this.start.margin;
                n3 = Math.min(dependencyNode.value + this.end.margin - (n3 + n2), this.dimension.wrapValue);
                n = this.widget.mMatchConstraintMaxWidth;
                n3 = n2 = Math.max(this.widget.mMatchConstraintMinWidth, n3);
                if (n > 0) {
                    n3 = Math.min(n, n2);
                }
                this.dimension.resolve(n3);
            }
            if (!this.dimension.resolved) {
                return;
            }
            dependencyNode = this.start.targets.get(0);
            object = this.end.targets.get(0);
            n2 = dependencyNode.value + this.start.margin;
            n3 = ((DependencyNode)object).value + this.end.margin;
            f = this.widget.getHorizontalBiasPercent();
            if (dependencyNode == object) {
                n2 = dependencyNode.value;
                n3 = ((DependencyNode)object).value;
                f = 0.5f;
            }
            n = this.dimension.value;
            this.start.resolve((int)((float)n2 + 0.5f + (float)(n3 - n2 - n) * f));
            this.end.resolve(this.start.value + this.dimension.value);
            return;
        }
    }
}

