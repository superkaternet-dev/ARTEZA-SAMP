/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.Metrics;
import androidx.constraintlayout.solver.widgets.Barrier;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.solver.widgets.Guideline;
import androidx.constraintlayout.solver.widgets.Helper;
import androidx.constraintlayout.solver.widgets.Optimizer;
import androidx.constraintlayout.solver.widgets.VirtualLayout;
import java.util.ArrayList;

public class BasicMeasure {
    public static final int AT_MOST = Integer.MIN_VALUE;
    private static final boolean DEBUG = false;
    public static final int EXACTLY = 0x40000000;
    public static final int FIXED = -3;
    public static final int MATCH_PARENT = -1;
    private static final int MODE_SHIFT = 30;
    public static final int UNSPECIFIED = 0;
    public static final int WRAP_CONTENT = -2;
    private ConstraintWidgetContainer constraintWidgetContainer;
    private Measure mMeasure;
    private final ArrayList<ConstraintWidget> mVariableDimensionsWidgets = new ArrayList();

    public BasicMeasure(ConstraintWidgetContainer constraintWidgetContainer) {
        this.mMeasure = new Measure();
        this.constraintWidgetContainer = constraintWidgetContainer;
    }

    private boolean measure(Measurer measurer, ConstraintWidget constraintWidget, int n) {
        this.mMeasure.horizontalBehavior = constraintWidget.getHorizontalDimensionBehaviour();
        this.mMeasure.verticalBehavior = constraintWidget.getVerticalDimensionBehaviour();
        this.mMeasure.horizontalDimension = constraintWidget.getWidth();
        this.mMeasure.verticalDimension = constraintWidget.getHeight();
        this.mMeasure.measuredNeedsSolverPass = false;
        this.mMeasure.measureStrategy = n;
        n = this.mMeasure.horizontalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT ? 1 : 0;
        boolean bl = this.mMeasure.verticalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        n = n != 0 && constraintWidget.mDimensionRatio > 0.0f ? 1 : 0;
        bl = bl && constraintWidget.mDimensionRatio > 0.0f;
        if (n != 0 && constraintWidget.mResolvedMatchConstraintDefault[0] == 4) {
            this.mMeasure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
        }
        if (bl && constraintWidget.mResolvedMatchConstraintDefault[1] == 4) {
            this.mMeasure.verticalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
        }
        measurer.measure(constraintWidget, this.mMeasure);
        constraintWidget.setWidth(this.mMeasure.measuredWidth);
        constraintWidget.setHeight(this.mMeasure.measuredHeight);
        constraintWidget.setHasBaseline(this.mMeasure.measuredHasBaseline);
        constraintWidget.setBaselineDistance(this.mMeasure.measuredBaseline);
        this.mMeasure.measureStrategy = Measure.SELF_DIMENSIONS;
        return this.mMeasure.measuredNeedsSolverPass;
    }

    private void measureChildren(ConstraintWidgetContainer constraintWidgetContainer) {
        int n = constraintWidgetContainer.mChildren.size();
        boolean bl = constraintWidgetContainer.optimizeFor(64);
        Measurer measurer = constraintWidgetContainer.getMeasurer();
        for (int i = 0; i < n; ++i) {
            Object object;
            boolean bl2;
            ConstraintWidget constraintWidget;
            block17: {
                boolean bl3;
                block18: {
                    constraintWidget = (ConstraintWidget)constraintWidgetContainer.mChildren.get(i);
                    if (constraintWidget instanceof Guideline || constraintWidget instanceof Barrier || constraintWidget.isInVirtualLayout() || bl && constraintWidget.horizontalRun != null && constraintWidget.verticalRun != null && constraintWidget.horizontalRun.dimension.resolved && constraintWidget.verticalRun.dimension.resolved) continue;
                    bl2 = false;
                    ConstraintWidget.DimensionBehaviour dimensionBehaviour = constraintWidget.getDimensionBehaviour(0);
                    object = constraintWidget.getDimensionBehaviour(1);
                    bl3 = bl2;
                    if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                        bl3 = bl2;
                        if (constraintWidget.mMatchConstraintDefaultWidth != 1) {
                            bl3 = bl2;
                            if (object == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                                bl3 = bl2;
                                if (constraintWidget.mMatchConstraintDefaultHeight != 1) {
                                    bl3 = true;
                                }
                            }
                        }
                    }
                    bl2 = bl3;
                    if (bl3) break block17;
                    bl2 = bl3;
                    if (!constraintWidgetContainer.optimizeFor(1)) break block17;
                    bl2 = bl3;
                    if (constraintWidget instanceof VirtualLayout) break block17;
                    bl2 = bl3;
                    if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                        bl2 = bl3;
                        if (constraintWidget.mMatchConstraintDefaultWidth == 0) {
                            bl2 = bl3;
                            if (object != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                                bl2 = bl3;
                                if (!constraintWidget.isInHorizontalChain()) {
                                    bl2 = true;
                                }
                            }
                        }
                    }
                    bl3 = bl2;
                    if (object == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                        bl3 = bl2;
                        if (constraintWidget.mMatchConstraintDefaultHeight == 0) {
                            bl3 = bl2;
                            if (dimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                                bl3 = bl2;
                                if (!constraintWidget.isInHorizontalChain()) {
                                    bl3 = true;
                                }
                            }
                        }
                    }
                    if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) break block18;
                    bl2 = bl3;
                    if (object != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) break block17;
                }
                bl2 = bl3;
                if (constraintWidget.mDimensionRatio > 0.0f) {
                    bl2 = true;
                }
            }
            if (bl2) continue;
            this.measure(measurer, constraintWidget, Measure.SELF_DIMENSIONS);
            if (constraintWidgetContainer.mMetrics == null) continue;
            object = constraintWidgetContainer.mMetrics;
            ++((Metrics)object).measuredWidgets;
        }
        measurer.didMeasures();
    }

    private void solveLinearSystem(ConstraintWidgetContainer constraintWidgetContainer, String string2, int n, int n2) {
        int n3 = constraintWidgetContainer.getMinWidth();
        int n4 = constraintWidgetContainer.getMinHeight();
        constraintWidgetContainer.setMinWidth(0);
        constraintWidgetContainer.setMinHeight(0);
        constraintWidgetContainer.setWidth(n);
        constraintWidgetContainer.setHeight(n2);
        constraintWidgetContainer.setMinWidth(n3);
        constraintWidgetContainer.setMinHeight(n4);
        this.constraintWidgetContainer.layout();
    }

    public long solverMeasure(ConstraintWidgetContainer constraintWidgetContainer, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9) {
        Object object;
        Measurer measurer = constraintWidgetContainer.getMeasurer();
        n9 = constraintWidgetContainer.mChildren.size();
        int n10 = constraintWidgetContainer.getWidth();
        int n11 = constraintWidgetContainer.getHeight();
        boolean bl = Optimizer.enabled(n, 128);
        n = !bl && !Optimizer.enabled(n, 64) ? 0 : 1;
        if (n != 0) {
            for (n2 = 0; n2 < n9; ++n2) {
                object = (ConstraintWidget)constraintWidgetContainer.mChildren.get(n2);
                n3 = ((ConstraintWidget)object).getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT ? 1 : 0;
                n8 = ((ConstraintWidget)object).getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT ? 1 : 0;
                n3 = n3 != 0 && n8 != 0 && ((ConstraintWidget)object).getDimensionRatio() > 0.0f ? 1 : 0;
                if (((ConstraintWidget)object).isInHorizontalChain() && n3 != 0) {
                    n = 0;
                    break;
                }
                if (((ConstraintWidget)object).isInVerticalChain() && n3 != 0) {
                    n = 0;
                    break;
                }
                if (object instanceof VirtualLayout) {
                    n = 0;
                    break;
                }
                if (!((ConstraintWidget)object).isInHorizontalChain() && !((ConstraintWidget)object).isInVerticalChain()) {
                    continue;
                }
                n = 0;
                break;
            }
        }
        if (n != 0 && LinearSystem.sMetrics != null) {
            object = LinearSystem.sMetrics;
            ++((Metrics)object).measures;
        }
        boolean bl2 = false;
        n2 = n4 == 0x40000000 && n6 == 0x40000000 || bl ? 1 : 0;
        int n12 = n2 & n;
        n3 = 0;
        n = 0;
        if (n12 != 0) {
            n5 = Math.min(constraintWidgetContainer.getMaxWidth(), n5);
            n2 = Math.min(constraintWidgetContainer.getMaxHeight(), n7);
            if (n4 == 0x40000000 && constraintWidgetContainer.getWidth() != n5) {
                constraintWidgetContainer.setWidth(n5);
                constraintWidgetContainer.invalidateGraph();
            }
            if (n6 == 0x40000000 && constraintWidgetContainer.getHeight() != n2) {
                constraintWidgetContainer.setHeight(n2);
                constraintWidgetContainer.invalidateGraph();
            }
            if (n4 == 0x40000000 && n6 == 0x40000000) {
                bl2 = constraintWidgetContainer.directMeasure(bl);
                n = 2;
            } else {
                bl2 = constraintWidgetContainer.directMeasureSetup(bl);
                if (n4 == 0x40000000) {
                    bl2 &= constraintWidgetContainer.directMeasureWithOrientation(bl, 0);
                    n = 0 + 1;
                }
                if (n6 == 0x40000000) {
                    bl2 &= constraintWidgetContainer.directMeasureWithOrientation(bl, 1);
                    ++n;
                }
            }
            bl = true;
            if (bl2) {
                if (n4 != 0x40000000) {
                    bl = false;
                }
                boolean bl3 = n6 == 0x40000000;
                constraintWidgetContainer.updateFromRuns(bl, bl3);
            }
        } else {
            n2 = n7;
            n = n3;
        }
        if (!bl2 || n != 2) {
            n3 = constraintWidgetContainer.getOptimizationLevel();
            if (n9 > 0) {
                this.measureChildren(constraintWidgetContainer);
            }
            this.updateHierarchy(constraintWidgetContainer);
            int n13 = this.mVariableDimensionsWidgets.size();
            if (n9 > 0) {
                this.solveLinearSystem(constraintWidgetContainer, "First pass", n10, n11);
            }
            if (n13 > 0) {
                int n14;
                block45: {
                    int n15;
                    Metrics metrics;
                    int n16;
                    n4 = constraintWidgetContainer.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT ? 1 : 0;
                    boolean bl4 = constraintWidgetContainer.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                    n5 = Math.max(constraintWidgetContainer.getWidth(), this.constraintWidgetContainer.getMinWidth());
                    n9 = Math.max(constraintWidgetContainer.getHeight(), this.constraintWidgetContainer.getMinHeight());
                    n14 = 0;
                    n6 = n;
                    n7 = n2;
                    n = n9;
                    n2 = n5;
                    n5 = n14;
                    for (n8 = 0; n8 < n13; ++n8) {
                        object = this.mVariableDimensionsWidgets.get(n8);
                        if (!(object instanceof VirtualLayout)) continue;
                        n16 = ((ConstraintWidget)object).getWidth();
                        n9 = ((ConstraintWidget)object).getHeight();
                        n5 |= this.measure(measurer, (ConstraintWidget)object, Measure.TRY_GIVEN_DIMENSIONS);
                        if (constraintWidgetContainer.mMetrics != null) {
                            metrics = constraintWidgetContainer.mMetrics;
                            ++metrics.measuredMatchWidgets;
                        }
                        n15 = ((ConstraintWidget)object).getWidth();
                        n14 = ((ConstraintWidget)object).getHeight();
                        if (n15 != n16) {
                            ((ConstraintWidget)object).setWidth(n15);
                            if (n4 != 0 && ((ConstraintWidget)object).getRight() > n2) {
                                n2 = Math.max(n2, ((ConstraintWidget)object).getRight() + ((ConstraintWidget)object).getAnchor(ConstraintAnchor.Type.RIGHT).getMargin());
                            }
                            n5 = 1;
                        }
                        if (n14 != n9) {
                            ((ConstraintWidget)object).setHeight(n14);
                            if (bl4 && ((ConstraintWidget)object).getBottom() > n) {
                                n = Math.max(n, ((ConstraintWidget)object).getBottom() + ((ConstraintWidget)object).getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin());
                            }
                            n5 = 1;
                        }
                        n5 |= ((VirtualLayout)object).needSolverPass();
                    }
                    n14 = n3;
                    n7 = 2;
                    n6 = n12;
                    n3 = n5;
                    n8 = n4;
                    for (n9 = 0; n9 < n7; ++n9) {
                        n5 = 0;
                        n4 = n13;
                        n13 = n5;
                        n5 = n7;
                        n7 = n3;
                        n3 = n8;
                        while (n13 < n4) {
                            object = this.mVariableDimensionsWidgets.get(n13);
                            if (!(object instanceof Helper && !(object instanceof VirtualLayout) || object instanceof Guideline || ((ConstraintWidget)object).getVisibility() == 8 || n6 != 0 && ((ConstraintWidget)object).horizontalRun.dimension.resolved && ((ConstraintWidget)object).verticalRun.dimension.resolved || object instanceof VirtualLayout)) {
                                int n17 = ((ConstraintWidget)object).getWidth();
                                n16 = ((ConstraintWidget)object).getHeight();
                                n12 = ((ConstraintWidget)object).getBaselineDistance();
                                n8 = Measure.TRY_GIVEN_DIMENSIONS;
                                if (n9 == n5 - 1) {
                                    n8 = Measure.USE_GIVEN_DIMENSIONS;
                                }
                                n7 |= this.measure(measurer, (ConstraintWidget)object, n8);
                                if (constraintWidgetContainer.mMetrics != null) {
                                    metrics = constraintWidgetContainer.mMetrics;
                                    ++metrics.measuredMatchWidgets;
                                }
                                n8 = ((ConstraintWidget)object).getWidth();
                                n15 = ((ConstraintWidget)object).getHeight();
                                if (n8 != n17) {
                                    ((ConstraintWidget)object).setWidth(n8);
                                    if (n3 != 0 && ((ConstraintWidget)object).getRight() > n2) {
                                        n2 = Math.max(n2, ((ConstraintWidget)object).getRight() + ((ConstraintWidget)object).getAnchor(ConstraintAnchor.Type.RIGHT).getMargin());
                                    }
                                    n7 = 1;
                                }
                                n8 = n;
                                if (n15 != n16) {
                                    ((ConstraintWidget)object).setHeight(n15);
                                    n8 = n;
                                    if (bl4) {
                                        n8 = n;
                                        if (((ConstraintWidget)object).getBottom() > n) {
                                            n8 = Math.max(n, ((ConstraintWidget)object).getBottom() + ((ConstraintWidget)object).getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin());
                                        }
                                    }
                                    n7 = 1;
                                }
                                if (((ConstraintWidget)object).hasBaseline() && n12 != ((ConstraintWidget)object).getBaselineDistance()) {
                                    n7 = 1;
                                    n = n8;
                                } else {
                                    n = n8;
                                }
                            }
                            ++n13;
                        }
                        if (n7 != 0) {
                            this.solveLinearSystem(constraintWidgetContainer, "intermediate pass", n10, n11);
                            n7 = 0;
                            n8 = n3;
                            n3 = n7;
                            n7 = n5;
                            n13 = n4;
                            continue;
                        }
                        n3 = n7;
                        n4 = n;
                        break block45;
                    }
                    n4 = n;
                }
                n = n14;
                if (n3 != 0) {
                    this.solveLinearSystem(constraintWidgetContainer, "2nd pass", n10, n11);
                    n = 0;
                    if (constraintWidgetContainer.getWidth() < n2) {
                        constraintWidgetContainer.setWidth(n2);
                        n = 1;
                    }
                    n2 = n;
                    if (constraintWidgetContainer.getHeight() < n4) {
                        constraintWidgetContainer.setHeight(n4);
                        n2 = 1;
                    }
                    n = n14;
                    if (n2 != 0) {
                        this.solveLinearSystem(constraintWidgetContainer, "3rd pass", n10, n11);
                        n = n14;
                    }
                }
            } else {
                n = n3;
            }
            constraintWidgetContainer.setOptimizationLevel(n);
        }
        return 0L;
    }

    public void updateHierarchy(ConstraintWidgetContainer constraintWidgetContainer) {
        this.mVariableDimensionsWidgets.clear();
        int n = constraintWidgetContainer.mChildren.size();
        for (int i = 0; i < n; ++i) {
            ConstraintWidget constraintWidget = (ConstraintWidget)constraintWidgetContainer.mChildren.get(i);
            if (constraintWidget.getHorizontalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.getVerticalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) continue;
            this.mVariableDimensionsWidgets.add(constraintWidget);
        }
        constraintWidgetContainer.invalidateGraph();
    }

    public static class Measure {
        public static int SELF_DIMENSIONS = 0;
        public static int TRY_GIVEN_DIMENSIONS = 1;
        public static int USE_GIVEN_DIMENSIONS = 2;
        public ConstraintWidget.DimensionBehaviour horizontalBehavior;
        public int horizontalDimension;
        public int measureStrategy;
        public int measuredBaseline;
        public boolean measuredHasBaseline;
        public int measuredHeight;
        public boolean measuredNeedsSolverPass;
        public int measuredWidth;
        public ConstraintWidget.DimensionBehaviour verticalBehavior;
        public int verticalDimension;
    }

    public static interface Measurer {
        public void didMeasures();

        public void measure(ConstraintWidget var1, Measure var2);
    }
}

