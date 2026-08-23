/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.Helper;
import androidx.constraintlayout.solver.widgets.analyzer.BaselineDimensionDependency;
import androidx.constraintlayout.solver.widgets.analyzer.Dependency;
import androidx.constraintlayout.solver.widgets.analyzer.DependencyNode;
import androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency;
import androidx.constraintlayout.solver.widgets.analyzer.WidgetRun;

public class VerticalWidgetRun
extends WidgetRun {
    public DependencyNode baseline = new DependencyNode(this);
    DimensionDependency baselineDimension = null;

    public VerticalWidgetRun(ConstraintWidget constraintWidget) {
        super(constraintWidget);
        this.start.type = DependencyNode.Type.TOP;
        this.end.type = DependencyNode.Type.BOTTOM;
        this.baseline.type = DependencyNode.Type.BASELINE;
        this.orientation = 1;
    }

    @Override
    void apply() {
        block55: {
            Object object;
            block57: {
                block56: {
                    block54: {
                        if (this.widget.measured) {
                            this.dimension.resolve(this.widget.getHeight());
                        }
                        if (!this.dimension.resolved) {
                            this.dimensionBehavior = this.widget.getVerticalDimensionBehaviour();
                            if (this.widget.hasBaseline()) {
                                this.baselineDimension = new BaselineDimensionDependency(this);
                            }
                            if (this.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                                if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_PARENT && (object = this.widget.getParent()) != null && ((ConstraintWidget)object).getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED) {
                                    int n = ((ConstraintWidget)object).getHeight();
                                    int n2 = this.widget.mTop.getMargin();
                                    int n3 = this.widget.mBottom.getMargin();
                                    this.addTarget(this.start, ((ConstraintWidget)object).verticalRun.start, this.widget.mTop.getMargin());
                                    this.addTarget(this.end, ((ConstraintWidget)object).verticalRun.end, -this.widget.mBottom.getMargin());
                                    this.dimension.resolve(n - n2 - n3);
                                    return;
                                }
                                if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.FIXED) {
                                    this.dimension.resolve(this.widget.getHeight());
                                }
                            }
                        } else if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_PARENT && (object = this.widget.getParent()) != null && ((ConstraintWidget)object).getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED) {
                            this.addTarget(this.start, ((ConstraintWidget)object).verticalRun.start, this.widget.mTop.getMargin());
                            this.addTarget(this.end, ((ConstraintWidget)object).verticalRun.end, -this.widget.mBottom.getMargin());
                            return;
                        }
                        if (!this.dimension.resolved || !this.widget.measured) break block54;
                        if (this.widget.mListAnchors[2].mTarget != null && this.widget.mListAnchors[3].mTarget != null) {
                            if (this.widget.isInVerticalChain()) {
                                this.start.margin = this.widget.mListAnchors[2].getMargin();
                                this.end.margin = -this.widget.mListAnchors[3].getMargin();
                            } else {
                                object = this.getTarget(this.widget.mListAnchors[2]);
                                if (object != null) {
                                    this.addTarget(this.start, (DependencyNode)object, this.widget.mListAnchors[2].getMargin());
                                }
                                if ((object = this.getTarget(this.widget.mListAnchors[3])) != null) {
                                    this.addTarget(this.end, (DependencyNode)object, -this.widget.mListAnchors[3].getMargin());
                                }
                                this.start.delegateToWidgetRun = true;
                                this.end.delegateToWidgetRun = true;
                            }
                            if (this.widget.hasBaseline()) {
                                this.addTarget(this.baseline, this.start, this.widget.getBaselineDistance());
                            }
                        } else if (this.widget.mListAnchors[2].mTarget != null) {
                            object = this.getTarget(this.widget.mListAnchors[2]);
                            if (object != null) {
                                this.addTarget(this.start, (DependencyNode)object, this.widget.mListAnchors[2].getMargin());
                                this.addTarget(this.end, this.start, this.dimension.value);
                                if (this.widget.hasBaseline()) {
                                    this.addTarget(this.baseline, this.start, this.widget.getBaselineDistance());
                                }
                            }
                        } else if (this.widget.mListAnchors[3].mTarget != null) {
                            object = this.getTarget(this.widget.mListAnchors[3]);
                            if (object != null) {
                                this.addTarget(this.end, (DependencyNode)object, -this.widget.mListAnchors[3].getMargin());
                                this.addTarget(this.start, this.end, -this.dimension.value);
                            }
                            if (this.widget.hasBaseline()) {
                                this.addTarget(this.baseline, this.start, this.widget.getBaselineDistance());
                            }
                        } else if (this.widget.mListAnchors[4].mTarget != null) {
                            object = this.getTarget(this.widget.mListAnchors[4]);
                            if (object != null) {
                                this.addTarget(this.baseline, (DependencyNode)object, 0);
                                this.addTarget(this.start, this.baseline, -this.widget.getBaselineDistance());
                                this.addTarget(this.end, this.start, this.dimension.value);
                            }
                        } else if (!(this.widget instanceof Helper) && this.widget.getParent() != null && this.widget.getAnchor((ConstraintAnchor.Type)ConstraintAnchor.Type.CENTER).mTarget == null) {
                            object = this.widget.getParent().verticalRun.start;
                            this.addTarget(this.start, (DependencyNode)object, this.widget.getY());
                            this.addTarget(this.end, this.start, this.dimension.value);
                            if (this.widget.hasBaseline()) {
                                this.addTarget(this.baseline, this.start, this.widget.getBaselineDistance());
                            }
                        }
                        break block55;
                    }
                    if (this.dimension.resolved || this.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) break block56;
                    switch (this.widget.mMatchConstraintDefaultHeight) {
                        default: {
                            break;
                        }
                        case 3: {
                            if (!this.widget.isInVerticalChain()) {
                                if (this.widget.mMatchConstraintDefaultWidth == 3) break;
                                object = this.widget.horizontalRun.dimension;
                                this.dimension.targets.add(object);
                                ((DependencyNode)object).dependencies.add(this.dimension);
                                this.dimension.delegateToWidgetRun = true;
                                this.dimension.dependencies.add(this.start);
                                this.dimension.dependencies.add(this.end);
                                break;
                            }
                            break block57;
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
                    break block57;
                }
                this.dimension.addDependency(this);
            }
            if (this.widget.mListAnchors[2].mTarget != null && this.widget.mListAnchors[3].mTarget != null) {
                if (this.widget.isInVerticalChain()) {
                    this.start.margin = this.widget.mListAnchors[2].getMargin();
                    this.end.margin = -this.widget.mListAnchors[3].getMargin();
                } else {
                    object = this.getTarget(this.widget.mListAnchors[2]);
                    DependencyNode dependencyNode = this.getTarget(this.widget.mListAnchors[3]);
                    ((DependencyNode)object).addDependency(this);
                    dependencyNode.addDependency(this);
                    this.mRunType = WidgetRun.RunType.CENTER;
                }
                if (this.widget.hasBaseline()) {
                    this.addTarget(this.baseline, this.start, 1, this.baselineDimension);
                }
            } else if (this.widget.mListAnchors[2].mTarget != null) {
                object = this.getTarget(this.widget.mListAnchors[2]);
                if (object != null) {
                    this.addTarget(this.start, (DependencyNode)object, this.widget.mListAnchors[2].getMargin());
                    this.addTarget(this.end, this.start, 1, this.dimension);
                    if (this.widget.hasBaseline()) {
                        this.addTarget(this.baseline, this.start, 1, this.baselineDimension);
                    }
                    if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.widget.getDimensionRatio() > 0.0f && this.widget.horizontalRun.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                        this.widget.horizontalRun.dimension.dependencies.add(this.dimension);
                        this.dimension.targets.add(this.widget.horizontalRun.dimension);
                        this.dimension.updateDelegate = this;
                    }
                }
            } else if (this.widget.mListAnchors[3].mTarget != null) {
                object = this.getTarget(this.widget.mListAnchors[3]);
                if (object != null) {
                    this.addTarget(this.end, (DependencyNode)object, -this.widget.mListAnchors[3].getMargin());
                    this.addTarget(this.start, this.end, -1, this.dimension);
                    if (this.widget.hasBaseline()) {
                        this.addTarget(this.baseline, this.start, 1, this.baselineDimension);
                    }
                }
            } else if (this.widget.mListAnchors[4].mTarget != null) {
                object = this.getTarget(this.widget.mListAnchors[4]);
                if (object != null) {
                    this.addTarget(this.baseline, (DependencyNode)object, 0);
                    this.addTarget(this.start, this.baseline, -1, this.baselineDimension);
                    this.addTarget(this.end, this.start, 1, this.dimension);
                }
            } else if (!(this.widget instanceof Helper) && this.widget.getParent() != null) {
                object = this.widget.getParent().verticalRun.start;
                this.addTarget(this.start, (DependencyNode)object, this.widget.getY());
                this.addTarget(this.end, this.start, 1, this.dimension);
                if (this.widget.hasBaseline()) {
                    this.addTarget(this.baseline, this.start, 1, this.baselineDimension);
                }
                if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.widget.getDimensionRatio() > 0.0f && this.widget.horizontalRun.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    this.widget.horizontalRun.dimension.dependencies.add(this.dimension);
                    this.dimension.targets.add(this.widget.horizontalRun.dimension);
                    this.dimension.updateDelegate = this;
                }
            }
            if (this.dimension.targets.size() == 0) {
                this.dimension.readyToSolve = true;
            }
        }
    }

    @Override
    public void applyToWidget() {
        if (this.start.resolved) {
            this.widget.setY(this.start.value);
        }
    }

    @Override
    void clear() {
        this.runGroup = null;
        this.start.clear();
        this.end.clear();
        this.baseline.clear();
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
        this.baseline.clear();
        this.baseline.resolved = false;
        this.dimension.resolved = false;
    }

    @Override
    boolean supportsWrapComputation() {
        if (this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            return this.widget.mMatchConstraintDefaultHeight == 0;
        }
        return true;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("VerticalRun ");
        stringBuilder.append(this.widget.getDebugName());
        return stringBuilder.toString();
    }

    @Override
    public void update(Dependency object) {
        float f;
        int n;
        switch (1.$SwitchMap$androidx$constraintlayout$solver$widgets$analyzer$WidgetRun$RunType[this.mRunType.ordinal()]) {
            default: {
                break;
            }
            case 3: {
                this.updateRunCenter((Dependency)object, this.widget.mTop, this.widget.mBottom, 1);
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
        if (this.dimension.readyToSolve && !this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            switch (this.widget.mMatchConstraintDefaultHeight) {
                default: {
                    break;
                }
                case 3: {
                    if (!this.widget.horizontalRun.dimension.resolved) break;
                    n = 0;
                    switch (this.widget.getDimensionRatioSide()) {
                        default: {
                            break;
                        }
                        case 1: {
                            n = (int)((float)this.widget.horizontalRun.dimension.value / this.widget.getDimensionRatio() + 0.5f);
                            break;
                        }
                        case 0: {
                            n = (int)((float)this.widget.horizontalRun.dimension.value * this.widget.getDimensionRatio() + 0.5f);
                            break;
                        }
                        case -1: {
                            n = (int)((float)this.widget.horizontalRun.dimension.value / this.widget.getDimensionRatio() + 0.5f);
                        }
                    }
                    this.dimension.resolve(n);
                    break;
                }
                case 2: {
                    object = this.widget.getParent();
                    if (object == null || !((ConstraintWidget)object).verticalRun.dimension.resolved) break;
                    f = this.widget.mMatchConstraintPercentHeight;
                    n = (int)((float)((ConstraintWidget)object).verticalRun.dimension.value * f + 0.5f);
                    this.dimension.resolve(n);
                }
            }
        }
        if (this.start.readyToSolve && this.end.readyToSolve) {
            int n2;
            DependencyNode dependencyNode;
            if (this.start.resolved && this.end.resolved && this.dimension.resolved) {
                return;
            }
            if (!this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.widget.mMatchConstraintDefaultWidth == 0 && !this.widget.isInVerticalChain()) {
                object = this.start.targets.get(0);
                DependencyNode dependencyNode2 = this.end.targets.get(0);
                n = ((DependencyNode)object).value + this.start.margin;
                int n3 = dependencyNode2.value + this.end.margin;
                this.start.resolve(n);
                this.end.resolve(n3);
                this.dimension.resolve(n3 - n);
                return;
            }
            if (!this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.matchConstraintsType == 1 && this.start.targets.size() > 0 && this.end.targets.size() > 0) {
                dependencyNode = this.start.targets.get(0);
                object = this.end.targets.get(0);
                n = dependencyNode.value;
                n2 = this.start.margin;
                if ((n = ((DependencyNode)object).value + this.end.margin - (n + n2)) < this.dimension.wrapValue) {
                    this.dimension.resolve(n);
                } else {
                    this.dimension.resolve(this.dimension.wrapValue);
                }
            }
            if (!this.dimension.resolved) {
                return;
            }
            if (this.start.targets.size() > 0 && this.end.targets.size() > 0) {
                dependencyNode = this.start.targets.get(0);
                object = this.end.targets.get(0);
                n = dependencyNode.value + this.start.margin;
                n2 = ((DependencyNode)object).value + this.end.margin;
                f = this.widget.getVerticalBiasPercent();
                if (dependencyNode == object) {
                    n = dependencyNode.value;
                    n2 = ((DependencyNode)object).value;
                    f = 0.5f;
                }
                int n4 = this.dimension.value;
                this.start.resolve((int)((float)n + 0.5f + (float)(n2 - n - n4) * f));
                this.end.resolve(this.start.value + this.dimension.value);
            }
            return;
        }
    }
}

