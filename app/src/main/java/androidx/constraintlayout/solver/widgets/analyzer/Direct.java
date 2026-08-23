/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.widgets.Barrier;
import androidx.constraintlayout.solver.widgets.ChainHead;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.solver.widgets.Guideline;
import androidx.constraintlayout.solver.widgets.WidgetContainer;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import java.util.ArrayList;
import java.util.Iterator;

public class Direct {
    private static final boolean APPLY_MATCH_PARENT = false;
    private static final boolean DEBUG = false;
    private static BasicMeasure.Measure measure = new BasicMeasure.Measure();

    private static boolean canMeasure(ConstraintWidget constraintWidget) {
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = constraintWidget.getHorizontalDimensionBehaviour();
        ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = constraintWidget.getVerticalDimensionBehaviour();
        ConstraintWidgetContainer constraintWidgetContainer = constraintWidget.getParent() != null ? (ConstraintWidgetContainer)constraintWidget.getParent() : null;
        boolean bl = false;
        if (constraintWidgetContainer == null || constraintWidgetContainer.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED) {
            // empty if block
        }
        if (constraintWidgetContainer == null || constraintWidgetContainer.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED) {
            // empty if block
        }
        boolean bl2 = dimensionBehaviour == ConstraintWidget.DimensionBehaviour.FIXED || dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mMatchConstraintDefaultWidth == 0 && constraintWidget.mDimensionRatio == 0.0f && constraintWidget.hasDanglingDimension(0) || constraintWidget.isResolvedHorizontally();
        boolean bl3 = dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.FIXED || dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mMatchConstraintDefaultHeight == 0 && constraintWidget.mDimensionRatio == 0.0f && constraintWidget.hasDanglingDimension(1) || constraintWidget.isResolvedVertically();
        if (constraintWidget.mDimensionRatio > 0.0f && (bl2 || bl3)) {
            return true;
        }
        boolean bl4 = bl;
        if (bl2) {
            bl4 = bl;
            if (bl3) {
                bl4 = true;
            }
        }
        return bl4;
    }

    private static void horizontalSolvingPass(ConstraintWidget constraintWidget, BasicMeasure.Measurer measurer, boolean bl) {
        int n;
        boolean bl2;
        if (!(constraintWidget instanceof ConstraintWidgetContainer) && constraintWidget.isMeasureRequested() && Direct.canMeasure(constraintWidget)) {
            ConstraintWidgetContainer.measure(constraintWidget, measurer, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS);
        }
        Iterator<ConstraintAnchor> iterator2 = constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT);
        Object object = constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT);
        int n2 = ((ConstraintAnchor)((Object)iterator2)).getFinalValue();
        int n3 = ((ConstraintAnchor)object).getFinalValue();
        if (((ConstraintAnchor)((Object)iterator2)).getDependents() != null && ((ConstraintAnchor)((Object)iterator2)).hasFinalValue()) {
            for (ConstraintAnchor constraintAnchor : ((ConstraintAnchor)((Object)iterator2)).getDependents()) {
                ConstraintWidget constraintWidget2 = constraintAnchor.mOwner;
                bl2 = Direct.canMeasure(constraintWidget2);
                if (constraintWidget2.isMeasureRequested() && bl2) {
                    ConstraintWidgetContainer.measure(constraintWidget2, measurer, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS);
                }
                if (constraintWidget2.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && !bl2) {
                    if (constraintWidget2.getHorizontalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || constraintWidget2.mMatchConstraintMaxWidth < 0 || constraintWidget2.mMatchConstraintMinWidth < 0 || constraintWidget2.getVisibility() != 8 && (constraintWidget2.mMatchConstraintDefaultWidth != 0 || constraintWidget2.getDimensionRatio() != 0.0f) || constraintWidget2.isInHorizontalChain() || constraintWidget2.isInVirtualLayout() || (n = constraintAnchor == constraintWidget2.mLeft && constraintWidget2.mRight.mTarget != null && constraintWidget2.mRight.mTarget.hasFinalValue() || constraintAnchor == constraintWidget2.mRight && constraintWidget2.mLeft.mTarget != null && constraintWidget2.mLeft.mTarget.hasFinalValue() ? 1 : 0) == 0 || constraintWidget2.isInHorizontalChain()) continue;
                    Direct.solveHorizontalMatchConstraint(constraintWidget, measurer, constraintWidget2, bl);
                    continue;
                }
                if (constraintWidget2.isMeasureRequested()) continue;
                if (constraintAnchor == constraintWidget2.mLeft && constraintWidget2.mRight.mTarget == null) {
                    n = constraintWidget2.mLeft.getMargin() + n2;
                    constraintWidget2.setFinalHorizontal(n, constraintWidget2.getWidth() + n);
                    Direct.horizontalSolvingPass(constraintWidget2, measurer, bl);
                    continue;
                }
                if (constraintAnchor == constraintWidget2.mRight && constraintWidget2.mLeft.mTarget == null) {
                    n = n2 - constraintWidget2.mRight.getMargin();
                    constraintWidget2.setFinalHorizontal(n - constraintWidget2.getWidth(), n);
                    Direct.horizontalSolvingPass(constraintWidget2, measurer, bl);
                    continue;
                }
                if (constraintAnchor != constraintWidget2.mLeft || constraintWidget2.mRight.mTarget == null || !constraintWidget2.mRight.mTarget.hasFinalValue() || constraintWidget2.isInHorizontalChain()) continue;
                Direct.solveHorizontalCenterConstraints(measurer, constraintWidget2, bl);
            }
        }
        if (constraintWidget instanceof Guideline) {
            return;
        }
        if (((ConstraintAnchor)object).getDependents() != null && ((ConstraintAnchor)object).hasFinalValue()) {
            for (ConstraintAnchor constraintAnchor : ((ConstraintAnchor)object).getDependents()) {
                object = constraintAnchor.mOwner;
                bl2 = Direct.canMeasure((ConstraintWidget)object);
                if (((ConstraintWidget)object).isMeasureRequested() && bl2) {
                    ConstraintWidgetContainer.measure((ConstraintWidget)object, measurer, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS);
                }
                n = constraintAnchor == ((ConstraintWidget)object).mLeft && ((ConstraintWidget)object).mRight.mTarget != null && ((ConstraintWidget)object).mRight.mTarget.hasFinalValue() || constraintAnchor == ((ConstraintWidget)object).mRight && ((ConstraintWidget)object).mLeft.mTarget != null && ((ConstraintWidget)object).mLeft.mTarget.hasFinalValue() ? 1 : 0;
                if (((ConstraintWidget)object).getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && !bl2) {
                    if (((ConstraintWidget)object).getHorizontalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || ((ConstraintWidget)object).mMatchConstraintMaxWidth < 0 || ((ConstraintWidget)object).mMatchConstraintMinWidth < 0 || ((ConstraintWidget)object).getVisibility() != 8 && (((ConstraintWidget)object).mMatchConstraintDefaultWidth != 0 || ((ConstraintWidget)object).getDimensionRatio() != 0.0f)) continue;
                    if (((ConstraintWidget)object).isInHorizontalChain() || ((ConstraintWidget)object).isInVirtualLayout() || n == 0 || ((ConstraintWidget)object).isInHorizontalChain()) continue;
                    Direct.solveHorizontalMatchConstraint(constraintWidget, measurer, (ConstraintWidget)object, bl);
                    continue;
                }
                if (((ConstraintWidget)object).isMeasureRequested()) continue;
                if (constraintAnchor == ((ConstraintWidget)object).mLeft && ((ConstraintWidget)object).mRight.mTarget == null) {
                    n = ((ConstraintWidget)object).mLeft.getMargin() + n3;
                    ((ConstraintWidget)object).setFinalHorizontal(n, ((ConstraintWidget)object).getWidth() + n);
                    Direct.horizontalSolvingPass((ConstraintWidget)object, measurer, bl);
                    continue;
                }
                if (constraintAnchor == ((ConstraintWidget)object).mRight && ((ConstraintWidget)object).mLeft.mTarget == null) {
                    n = n3 - ((ConstraintWidget)object).mRight.getMargin();
                    ((ConstraintWidget)object).setFinalHorizontal(n - ((ConstraintWidget)object).getWidth(), n);
                    Direct.horizontalSolvingPass((ConstraintWidget)object, measurer, bl);
                    continue;
                }
                if (n == 0 || ((ConstraintWidget)object).isInHorizontalChain()) continue;
                Direct.solveHorizontalCenterConstraints(measurer, (ConstraintWidget)object, bl);
            }
        }
    }

    private static void solveBarrier(Barrier barrier, BasicMeasure.Measurer measurer, int n, boolean bl) {
        if (barrier.allSolved()) {
            if (n == 0) {
                Direct.horizontalSolvingPass(barrier, measurer, bl);
            } else {
                Direct.verticalSolvingPass(barrier, measurer);
            }
        }
    }

    public static boolean solveChain(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, int n, int n2, ChainHead object, boolean bl, boolean bl2, boolean bl3) {
        if (bl3) {
            return false;
        }
        if (n == 0 ? !constraintWidgetContainer.isResolvedHorizontally() : !constraintWidgetContainer.isResolvedVertically()) {
            return false;
        }
        bl3 = constraintWidgetContainer.isRtl();
        ConstraintWidget constraintWidget = ((ChainHead)object).getFirst();
        ConstraintWidget constraintWidget2 = ((ChainHead)object).getLast();
        ConstraintWidget constraintWidget3 = ((ChainHead)object).getFirstVisibleWidget();
        ConstraintWidget constraintWidget4 = ((ChainHead)object).getLastVisibleWidget();
        ConstraintWidget constraintWidget5 = ((ChainHead)object).getHead();
        Object object2 = constraintWidget;
        object = null;
        int n3 = 0;
        Object object3 = constraintWidget.mListAnchors[n2];
        ConstraintAnchor constraintAnchor = constraintWidget2.mListAnchors[n2 + 1];
        if (((ConstraintAnchor)object3).mTarget != null && constraintAnchor.mTarget != null) {
            if (((ConstraintAnchor)object3).mTarget.hasFinalValue() && constraintAnchor.mTarget.hasFinalValue()) {
                if (constraintWidget3 != null && constraintWidget4 != null) {
                    int n4 = ((ConstraintAnchor)object3).mTarget.getFinalValue() + constraintWidget3.mListAnchors[n2].getMargin();
                    int n5 = constraintAnchor.mTarget.getFinalValue() - constraintWidget4.mListAnchors[n2 + 1].getMargin();
                    int n6 = n5 - n4;
                    if (n6 <= 0) {
                        return false;
                    }
                    int n7 = 0;
                    object3 = new BasicMeasure.Measure();
                    int n8 = 0;
                    int n9 = 0;
                    while (n3 == 0) {
                        object = ((ConstraintWidget)object2).mListAnchors[n2];
                        if (!Direct.canMeasure((ConstraintWidget)object2)) {
                            return false;
                        }
                        if (((ConstraintWidget)object2).mListDimensionBehaviors[n] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                            return false;
                        }
                        if (((ConstraintWidget)object2).isMeasureRequested()) {
                            ConstraintWidgetContainer.measure((ConstraintWidget)object2, constraintWidgetContainer.getMeasurer(), (BasicMeasure.Measure)object3, BasicMeasure.Measure.SELF_DIMENSIONS);
                        }
                        n7 += ((ConstraintWidget)object2).mListAnchors[n2].getMargin();
                        n7 = n == 0 ? (n7 += ((ConstraintWidget)object2).getWidth()) : (n7 += ((ConstraintWidget)object2).getHeight());
                        n7 += ((ConstraintWidget)object2).mListAnchors[n2 + 1].getMargin();
                        ++n8;
                        if (((ConstraintWidget)object2).getVisibility() != 8) {
                            ++n9;
                        }
                        if ((object = ((ConstraintWidget)object2).mListAnchors[n2 + 1].mTarget) != null) {
                            object = ((ConstraintAnchor)object).mOwner;
                            if (((ConstraintWidget)object).mListAnchors[n2].mTarget == null || ((ConstraintWidget)object).mListAnchors[n2].mTarget.mOwner != object2) {
                                object = null;
                            }
                        } else {
                            object = null;
                        }
                        if (object != null) {
                            object2 = object;
                            continue;
                        }
                        n3 = 1;
                    }
                    if (n9 == 0) {
                        return false;
                    }
                    if (n9 != n8) {
                        return false;
                    }
                    if (n6 < n7) {
                        return false;
                    }
                    n8 = n6 - n7;
                    if (bl) {
                        n8 /= n9 + 1;
                    } else if (bl2 && n9 > 2) {
                        n8 = n8 / n9 - 1;
                    }
                    if (n9 == 1) {
                        float f = n == 0 ? constraintWidget5.getHorizontalBiasPercent() : constraintWidget5.getVerticalBiasPercent();
                        n2 = (int)((float)n4 + 0.5f + (float)n8 * f);
                        if (n == 0) {
                            constraintWidget3.setFinalHorizontal(n2, constraintWidget3.getWidth() + n2);
                        } else {
                            constraintWidget3.setFinalVertical(n2, constraintWidget3.getHeight() + n2);
                        }
                        Direct.horizontalSolvingPass(constraintWidget3, constraintWidgetContainer.getMeasurer(), bl3);
                        return true;
                    }
                    if (bl) {
                        n3 = n4 + n8;
                        object3 = constraintWidget;
                        n9 = 0;
                        while (n9 == 0) {
                            object = ((ConstraintWidget)object3).mListAnchors[n2];
                            if (((ConstraintWidget)object3).getVisibility() == 8) {
                                if (n == 0) {
                                    ((ConstraintWidget)object3).setFinalHorizontal(n3, n3);
                                    Direct.horizontalSolvingPass((ConstraintWidget)object3, constraintWidgetContainer.getMeasurer(), bl3);
                                } else {
                                    ((ConstraintWidget)object3).setFinalVertical(n3, n3);
                                    Direct.verticalSolvingPass((ConstraintWidget)object3, constraintWidgetContainer.getMeasurer());
                                }
                            } else {
                                n3 += ((ConstraintWidget)object3).mListAnchors[n2].getMargin();
                                if (n == 0) {
                                    ((ConstraintWidget)object3).setFinalHorizontal(n3, ((ConstraintWidget)object3).getWidth() + n3);
                                    Direct.horizontalSolvingPass((ConstraintWidget)object3, constraintWidgetContainer.getMeasurer(), bl3);
                                    n3 += ((ConstraintWidget)object3).getWidth();
                                } else {
                                    ((ConstraintWidget)object3).setFinalVertical(n3, ((ConstraintWidget)object3).getHeight() + n3);
                                    Direct.verticalSolvingPass((ConstraintWidget)object3, constraintWidgetContainer.getMeasurer());
                                    n3 += ((ConstraintWidget)object3).getHeight();
                                }
                                n3 = n3 + ((ConstraintWidget)object3).mListAnchors[n2 + 1].getMargin() + n8;
                            }
                            ((ConstraintWidget)object3).addToSolver(linearSystem, false);
                            object = ((ConstraintWidget)object3).mListAnchors[n2 + 1].mTarget;
                            if (object != null) {
                                object = ((ConstraintAnchor)object).mOwner;
                                if (((ConstraintWidget)object).mListAnchors[n2].mTarget == null || ((ConstraintWidget)object).mListAnchors[n2].mTarget.mOwner != object3) {
                                    object = null;
                                }
                            } else {
                                object = null;
                            }
                            if (object != null) {
                                object3 = object;
                                continue;
                            }
                            n9 = 1;
                        }
                    } else if (bl2) {
                        if (n9 == 2) {
                            if (n == 0) {
                                constraintWidget3.setFinalHorizontal(n4, constraintWidget3.getWidth() + n4);
                                constraintWidget4.setFinalHorizontal(n5 - constraintWidget4.getWidth(), n5);
                                Direct.horizontalSolvingPass(constraintWidget3, constraintWidgetContainer.getMeasurer(), bl3);
                                Direct.horizontalSolvingPass(constraintWidget4, constraintWidgetContainer.getMeasurer(), bl3);
                            } else {
                                constraintWidget3.setFinalVertical(n4, constraintWidget3.getHeight() + n4);
                                constraintWidget4.setFinalVertical(n5 - constraintWidget4.getHeight(), n5);
                                Direct.verticalSolvingPass(constraintWidget3, constraintWidgetContainer.getMeasurer());
                                Direct.verticalSolvingPass(constraintWidget4, constraintWidgetContainer.getMeasurer());
                            }
                            return true;
                        }
                        return false;
                    }
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    private static void solveHorizontalCenterConstraints(BasicMeasure.Measurer measurer, ConstraintWidget constraintWidget, boolean bl) {
        float f = constraintWidget.getHorizontalBiasPercent();
        int n = constraintWidget.mLeft.mTarget.getFinalValue();
        int n2 = constraintWidget.mRight.mTarget.getFinalValue();
        int n3 = constraintWidget.mLeft.getMargin() + n;
        int n4 = n2 - constraintWidget.mRight.getMargin();
        if (n == n2) {
            f = 0.5f;
            n3 = n;
            n4 = n2;
        }
        int n5 = constraintWidget.getWidth();
        n2 = n4 - n3 - n5;
        if (n3 > n4) {
            n2 = n3 - n4 - n5;
        }
        int n6 = (int)((float)n2 * f + 0.5f);
        n2 = n3 + n6;
        n = n2 + n5;
        if (n3 > n4) {
            n2 = n3 + n6;
            n = n2 - n5;
        }
        constraintWidget.setFinalHorizontal(n2, n);
        Direct.horizontalSolvingPass(constraintWidget, measurer, bl);
    }

    private static void solveHorizontalMatchConstraint(ConstraintWidget constraintWidget, BasicMeasure.Measurer measurer, ConstraintWidget constraintWidget2, boolean bl) {
        float f = constraintWidget2.getHorizontalBiasPercent();
        int n = constraintWidget2.mLeft.mTarget.getFinalValue() + constraintWidget2.mLeft.getMargin();
        int n2 = constraintWidget2.mRight.mTarget.getFinalValue() - constraintWidget2.mRight.getMargin();
        if (n2 >= n) {
            int n3;
            int n4 = n3 = constraintWidget2.getWidth();
            if (constraintWidget2.getVisibility() != 8) {
                if (constraintWidget2.mMatchConstraintDefaultWidth == 2) {
                    n4 = constraintWidget instanceof ConstraintWidgetContainer ? constraintWidget.getWidth() : constraintWidget.getParent().getWidth();
                    n4 = (int)(constraintWidget2.getHorizontalBiasPercent() * 0.5f * (float)n4);
                } else {
                    n4 = n3;
                    if (constraintWidget2.mMatchConstraintDefaultWidth == 0) {
                        n4 = n2 - n;
                    }
                }
                n4 = n3 = Math.max(constraintWidget2.mMatchConstraintMinWidth, n4);
                if (constraintWidget2.mMatchConstraintMaxWidth > 0) {
                    n4 = Math.min(constraintWidget2.mMatchConstraintMaxWidth, n3);
                }
            }
            n3 = n + (int)((float)(n2 - n - n4) * f + 0.5f);
            constraintWidget2.setFinalHorizontal(n3, n3 + n4);
            Direct.horizontalSolvingPass(constraintWidget2, measurer, bl);
        }
    }

    private static void solveVerticalCenterConstraints(BasicMeasure.Measurer measurer, ConstraintWidget constraintWidget) {
        float f = constraintWidget.getVerticalBiasPercent();
        int n = constraintWidget.mTop.mTarget.getFinalValue();
        int n2 = constraintWidget.mBottom.mTarget.getFinalValue();
        int n3 = constraintWidget.mTop.getMargin() + n;
        int n4 = n2 - constraintWidget.mBottom.getMargin();
        if (n == n2) {
            f = 0.5f;
            n3 = n;
            n4 = n2;
        }
        int n5 = constraintWidget.getHeight();
        n2 = n4 - n3 - n5;
        if (n3 > n4) {
            n2 = n3 - n4 - n5;
        }
        int n6 = (int)((float)n2 * f + 0.5f);
        n2 = n3 + n6;
        n = n2 + n5;
        if (n3 > n4) {
            n2 = n3 - n6;
            n = n2 - n5;
        }
        constraintWidget.setFinalVertical(n2, n);
        Direct.verticalSolvingPass(constraintWidget, measurer);
    }

    private static void solveVerticalMatchConstraint(ConstraintWidget constraintWidget, BasicMeasure.Measurer measurer, ConstraintWidget constraintWidget2) {
        float f = constraintWidget2.getVerticalBiasPercent();
        int n = constraintWidget2.mTop.mTarget.getFinalValue() + constraintWidget2.mTop.getMargin();
        int n2 = constraintWidget2.mBottom.mTarget.getFinalValue() - constraintWidget2.mBottom.getMargin();
        if (n2 >= n) {
            int n3;
            int n4 = n3 = constraintWidget2.getHeight();
            if (constraintWidget2.getVisibility() != 8) {
                if (constraintWidget2.mMatchConstraintDefaultHeight == 2) {
                    n4 = constraintWidget instanceof ConstraintWidgetContainer ? constraintWidget.getHeight() : constraintWidget.getParent().getHeight();
                    n4 = (int)(f * 0.5f * (float)n4);
                } else {
                    n4 = n3;
                    if (constraintWidget2.mMatchConstraintDefaultHeight == 0) {
                        n4 = n2 - n;
                    }
                }
                n4 = n3 = Math.max(constraintWidget2.mMatchConstraintMinHeight, n4);
                if (constraintWidget2.mMatchConstraintMaxHeight > 0) {
                    n4 = Math.min(constraintWidget2.mMatchConstraintMaxHeight, n3);
                }
            }
            n3 = n + (int)((float)(n2 - n - n4) * f + 0.5f);
            constraintWidget2.setFinalVertical(n3, n3 + n4);
            Direct.verticalSolvingPass(constraintWidget2, measurer);
        }
    }

    public static void solvingPass(ConstraintWidgetContainer constraintWidget, BasicMeasure.Measurer measurer) {
        int n;
        int n2;
        int n3;
        Object object = constraintWidget.getHorizontalDimensionBehaviour();
        Object object2 = constraintWidget.getVerticalDimensionBehaviour();
        constraintWidget.resetFinalResolution();
        ArrayList<ConstraintWidget> arrayList = ((WidgetContainer)constraintWidget).getChildren();
        int n4 = arrayList.size();
        for (n3 = 0; n3 < n4; ++n3) {
            arrayList.get(n3).resetFinalResolution();
        }
        boolean bl = ((ConstraintWidgetContainer)constraintWidget).isRtl();
        if (object == ConstraintWidget.DimensionBehaviour.FIXED) {
            constraintWidget.setFinalHorizontal(0, constraintWidget.getWidth());
        } else {
            constraintWidget.setFinalLeft(0);
        }
        n3 = 0;
        boolean bl2 = false;
        for (n2 = 0; n2 < n4; ++n2) {
            object = arrayList.get(n2);
            if (object instanceof Guideline) {
                object = (Guideline)object;
                n = n3;
                if (((Guideline)object).getOrientation() == 1) {
                    if (((Guideline)object).getRelativeBegin() != -1) {
                        ((Guideline)object).setFinalValue(((Guideline)object).getRelativeBegin());
                    } else if (((Guideline)object).getRelativeEnd() != -1 && constraintWidget.isResolvedHorizontally()) {
                        ((Guideline)object).setFinalValue(constraintWidget.getWidth() - ((Guideline)object).getRelativeEnd());
                    } else if (constraintWidget.isResolvedHorizontally()) {
                        ((Guideline)object).setFinalValue((int)(((Guideline)object).getRelativePercent() * (float)constraintWidget.getWidth() + 0.5f));
                    }
                    n = 1;
                }
            } else {
                n = n3;
                if (object instanceof Barrier) {
                    n = n3;
                    if (((Barrier)object).getOrientation() == 0) {
                        bl2 = true;
                        n = n3;
                    }
                }
            }
            n3 = n;
        }
        if (n3 != 0) {
            for (n3 = 0; n3 < n4; ++n3) {
                object = arrayList.get(n3);
                if (!(object instanceof Guideline) || ((Guideline)(object = (Guideline)object)).getOrientation() != 1) continue;
                Direct.horizontalSolvingPass((ConstraintWidget)object, measurer, bl);
            }
        }
        Direct.horizontalSolvingPass(constraintWidget, measurer, bl);
        if (bl2) {
            for (n3 = 0; n3 < n4; ++n3) {
                object = arrayList.get(n3);
                if (!(object instanceof Barrier) || ((Barrier)(object = (Barrier)object)).getOrientation() != 0) continue;
                Direct.solveBarrier((Barrier)object, measurer, 0, bl);
            }
        }
        if (object2 == ConstraintWidget.DimensionBehaviour.FIXED) {
            constraintWidget.setFinalVertical(0, constraintWidget.getHeight());
        } else {
            constraintWidget.setFinalTop(0);
        }
        n3 = 0;
        bl2 = false;
        for (n2 = 0; n2 < n4; ++n2) {
            object2 = arrayList.get(n2);
            if (object2 instanceof Guideline) {
                object2 = (Guideline)object2;
                n = n3;
                if (((Guideline)object2).getOrientation() == 0) {
                    if (((Guideline)object2).getRelativeBegin() != -1) {
                        ((Guideline)object2).setFinalValue(((Guideline)object2).getRelativeBegin());
                    } else if (((Guideline)object2).getRelativeEnd() != -1 && constraintWidget.isResolvedVertically()) {
                        ((Guideline)object2).setFinalValue(constraintWidget.getHeight() - ((Guideline)object2).getRelativeEnd());
                    } else if (constraintWidget.isResolvedVertically()) {
                        ((Guideline)object2).setFinalValue((int)(((Guideline)object2).getRelativePercent() * (float)constraintWidget.getHeight() + 0.5f));
                    }
                    n = 1;
                }
            } else {
                n = n3;
                if (object2 instanceof Barrier) {
                    n = n3;
                    if (((Barrier)object2).getOrientation() == 1) {
                        bl2 = true;
                        n = n3;
                    }
                }
            }
            n3 = n;
        }
        if (n3 != 0) {
            for (n3 = 0; n3 < n4; ++n3) {
                object2 = arrayList.get(n3);
                if (!(object2 instanceof Guideline) || ((Guideline)(object2 = (Guideline)object2)).getOrientation() != 0) continue;
                Direct.verticalSolvingPass((ConstraintWidget)object2, measurer);
            }
        }
        Direct.verticalSolvingPass(constraintWidget, measurer);
        if (bl2) {
            for (n3 = 0; n3 < n4; ++n3) {
                constraintWidget = arrayList.get(n3);
                if (!(constraintWidget instanceof Barrier) || ((Barrier)(constraintWidget = (Barrier)constraintWidget)).getOrientation() != 1) continue;
                Direct.solveBarrier((Barrier)constraintWidget, measurer, 1, bl);
            }
        }
        for (n3 = 0; n3 < n4; ++n3) {
            constraintWidget = arrayList.get(n3);
            if (!constraintWidget.isMeasureRequested() || !Direct.canMeasure(constraintWidget)) continue;
            ConstraintWidgetContainer.measure(constraintWidget, measurer, measure, BasicMeasure.Measure.SELF_DIMENSIONS);
            Direct.horizontalSolvingPass(constraintWidget, measurer, bl);
            Direct.verticalSolvingPass(constraintWidget, measurer);
        }
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static void verticalSolvingPass(ConstraintWidget object, BasicMeasure.Measurer measurer) {
        ConstraintAnchor constraintAnchor;
        int n;
        boolean bl;
        void var1_5;
        if (!(object instanceof ConstraintWidgetContainer) && ((ConstraintWidget)object).isMeasureRequested() && Direct.canMeasure((ConstraintWidget)object)) {
            ConstraintWidgetContainer.measure((ConstraintWidget)object, (BasicMeasure.Measurer)var1_5, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS);
        }
        Object object2 = ((ConstraintWidget)object).getAnchor(ConstraintAnchor.Type.TOP);
        Iterator<ConstraintAnchor> iterator2 = ((ConstraintWidget)object).getAnchor(ConstraintAnchor.Type.BOTTOM);
        int n2 = ((ConstraintAnchor)object2).getFinalValue();
        int n3 = ((ConstraintAnchor)((Object)iterator2)).getFinalValue();
        if (((ConstraintAnchor)object2).getDependents() != null && ((ConstraintAnchor)object2).hasFinalValue()) {
            for (ConstraintAnchor constraintAnchor2 : ((ConstraintAnchor)object2).getDependents()) {
                object2 = constraintAnchor2.mOwner;
                bl = Direct.canMeasure((ConstraintWidget)object2);
                if (((ConstraintWidget)object2).isMeasureRequested() && bl) {
                    ConstraintWidgetContainer.measure((ConstraintWidget)object2, (BasicMeasure.Measurer)var1_5, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS);
                }
                if (((ConstraintWidget)object2).getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && !bl) {
                    if (((ConstraintWidget)object2).getVerticalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || ((ConstraintWidget)object2).mMatchConstraintMaxHeight < 0 || ((ConstraintWidget)object2).mMatchConstraintMinHeight < 0 || ((ConstraintWidget)object2).getVisibility() != 8 && (((ConstraintWidget)object2).mMatchConstraintDefaultHeight != 0 || ((ConstraintWidget)object2).getDimensionRatio() != 0.0f) || ((ConstraintWidget)object2).isInVerticalChain() || ((ConstraintWidget)object2).isInVirtualLayout() || (n = constraintAnchor2 == ((ConstraintWidget)object2).mTop && ((ConstraintWidget)object2).mBottom.mTarget != null && ((ConstraintWidget)object2).mBottom.mTarget.hasFinalValue() || constraintAnchor2 == ((ConstraintWidget)object2).mBottom && ((ConstraintWidget)object2).mTop.mTarget != null && ((ConstraintWidget)object2).mTop.mTarget.hasFinalValue() ? 1 : 0) == 0 || ((ConstraintWidget)object2).isInVerticalChain()) continue;
                    Direct.solveVerticalMatchConstraint((ConstraintWidget)object, (BasicMeasure.Measurer)var1_5, (ConstraintWidget)object2);
                    continue;
                }
                if (((ConstraintWidget)object2).isMeasureRequested()) continue;
                if (constraintAnchor2 == ((ConstraintWidget)object2).mTop && ((ConstraintWidget)object2).mBottom.mTarget == null) {
                    n = ((ConstraintWidget)object2).mTop.getMargin() + n2;
                    ((ConstraintWidget)object2).setFinalVertical(n, ((ConstraintWidget)object2).getHeight() + n);
                    Direct.verticalSolvingPass((ConstraintWidget)object2, (BasicMeasure.Measurer)var1_5);
                    continue;
                }
                if (constraintAnchor2 == ((ConstraintWidget)object2).mBottom && ((ConstraintWidget)object2).mBottom.mTarget == null) {
                    n = n2 - ((ConstraintWidget)object2).mBottom.getMargin();
                    ((ConstraintWidget)object2).setFinalVertical(n - ((ConstraintWidget)object2).getHeight(), n);
                    Direct.verticalSolvingPass((ConstraintWidget)object2, (BasicMeasure.Measurer)var1_5);
                    continue;
                }
                if (constraintAnchor2 != ((ConstraintWidget)object2).mTop || ((ConstraintWidget)object2).mBottom.mTarget == null || !((ConstraintWidget)object2).mBottom.mTarget.hasFinalValue()) continue;
                Direct.solveVerticalCenterConstraints((BasicMeasure.Measurer)var1_5, (ConstraintWidget)object2);
            }
        }
        if (object instanceof Guideline) {
            return;
        }
        if (((ConstraintAnchor)((Object)iterator2)).getDependents() != null && ((ConstraintAnchor)((Object)iterator2)).hasFinalValue()) {
            for (ConstraintAnchor constraintAnchor3 : ((ConstraintAnchor)((Object)iterator2)).getDependents()) {
                object2 = constraintAnchor3.mOwner;
                bl = Direct.canMeasure((ConstraintWidget)object2);
                if (((ConstraintWidget)object2).isMeasureRequested() && bl) {
                    ConstraintWidgetContainer.measure((ConstraintWidget)object2, (BasicMeasure.Measurer)var1_5, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS);
                }
                n = constraintAnchor3 == ((ConstraintWidget)object2).mTop && ((ConstraintWidget)object2).mBottom.mTarget != null && ((ConstraintWidget)object2).mBottom.mTarget.hasFinalValue() || constraintAnchor3 == ((ConstraintWidget)object2).mBottom && ((ConstraintWidget)object2).mTop.mTarget != null && ((ConstraintWidget)object2).mTop.mTarget.hasFinalValue() ? 1 : 0;
                if (((ConstraintWidget)object2).getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && !bl) {
                    if (((ConstraintWidget)object2).getVerticalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || ((ConstraintWidget)object2).mMatchConstraintMaxHeight < 0 || ((ConstraintWidget)object2).mMatchConstraintMinHeight < 0 || ((ConstraintWidget)object2).getVisibility() != 8 && (((ConstraintWidget)object2).mMatchConstraintDefaultHeight != 0 || ((ConstraintWidget)object2).getDimensionRatio() != 0.0f) || ((ConstraintWidget)object2).isInVerticalChain() || ((ConstraintWidget)object2).isInVirtualLayout() || n == 0 || ((ConstraintWidget)object2).isInVerticalChain()) continue;
                    Direct.solveVerticalMatchConstraint((ConstraintWidget)object, (BasicMeasure.Measurer)var1_5, (ConstraintWidget)object2);
                    continue;
                }
                if (((ConstraintWidget)object2).isMeasureRequested()) continue;
                if (constraintAnchor3 == ((ConstraintWidget)object2).mTop && ((ConstraintWidget)object2).mBottom.mTarget == null) {
                    n = ((ConstraintWidget)object2).mTop.getMargin() + n3;
                    ((ConstraintWidget)object2).setFinalVertical(n, ((ConstraintWidget)object2).getHeight() + n);
                    Direct.verticalSolvingPass((ConstraintWidget)object2, (BasicMeasure.Measurer)var1_5);
                    continue;
                }
                if (constraintAnchor3 == ((ConstraintWidget)object2).mBottom && ((ConstraintWidget)object2).mTop.mTarget == null) {
                    n = n3 - ((ConstraintWidget)object2).mBottom.getMargin();
                    ((ConstraintWidget)object2).setFinalVertical(n - ((ConstraintWidget)object2).getHeight(), n);
                    Direct.verticalSolvingPass((ConstraintWidget)object2, (BasicMeasure.Measurer)var1_5);
                    continue;
                }
                if (n == 0 || ((ConstraintWidget)object2).isInVerticalChain()) continue;
                Direct.solveVerticalCenterConstraints((BasicMeasure.Measurer)var1_5, (ConstraintWidget)object2);
            }
        }
        if ((constraintAnchor = ((ConstraintWidget)object).getAnchor(ConstraintAnchor.Type.BASELINE)).getDependents() == null) return;
        if (!constraintAnchor.hasFinalValue()) return;
        n = constraintAnchor.getFinalValue();
        iterator2 = constraintAnchor.getDependents().iterator();
        while (iterator2.hasNext()) {
            ConstraintAnchor constraintAnchor4 = iterator2.next();
            object2 = constraintAnchor4.mOwner;
            bl = Direct.canMeasure((ConstraintWidget)object2);
            if (((ConstraintWidget)object2).isMeasureRequested() && bl) {
                ConstraintWidgetContainer.measure((ConstraintWidget)object2, (BasicMeasure.Measurer)var1_5, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS);
            }
            if (((ConstraintWidget)object2).getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && !bl || ((ConstraintWidget)object2).isMeasureRequested() || constraintAnchor4 != ((ConstraintWidget)object2).mBaseline) continue;
            ((ConstraintWidget)object2).setFinalBaseline(n);
            Direct.verticalSolvingPass((ConstraintWidget)object2, (BasicMeasure.Measurer)var1_5);
        }
    }
}

