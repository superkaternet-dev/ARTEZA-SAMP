/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.Barrier;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.solver.widgets.Guideline;
import androidx.constraintlayout.solver.widgets.HelperWidget;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import androidx.constraintlayout.solver.widgets.analyzer.ChainRun;
import androidx.constraintlayout.solver.widgets.analyzer.Dependency;
import androidx.constraintlayout.solver.widgets.analyzer.DependencyNode;
import androidx.constraintlayout.solver.widgets.analyzer.GuidelineReference;
import androidx.constraintlayout.solver.widgets.analyzer.HelperReferences;
import androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun;
import androidx.constraintlayout.solver.widgets.analyzer.RunGroup;
import androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun;
import androidx.constraintlayout.solver.widgets.analyzer.WidgetRun;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class DependencyGraph {
    private static final boolean USE_GROUPS = true;
    private ConstraintWidgetContainer container;
    private ConstraintWidgetContainer mContainer;
    ArrayList<RunGroup> mGroups;
    private BasicMeasure.Measure mMeasure;
    private BasicMeasure.Measurer mMeasurer = null;
    private boolean mNeedBuildGraph = true;
    private boolean mNeedRedoMeasures = true;
    private ArrayList<WidgetRun> mRuns = new ArrayList();
    private ArrayList<RunGroup> runGroups = new ArrayList();

    public DependencyGraph(ConstraintWidgetContainer constraintWidgetContainer) {
        this.mMeasure = new BasicMeasure.Measure();
        this.mGroups = new ArrayList();
        this.container = constraintWidgetContainer;
        this.mContainer = constraintWidgetContainer;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void applyGroup(DependencyNode object, int n, int n2, DependencyNode dependencyNode, ArrayList<RunGroup> arrayList, RunGroup object2) {
        Object object3 = ((DependencyNode)object).run;
        if (((WidgetRun)object3).runGroup != null) return;
        if (object3 == this.container.horizontalRun) return;
        if (object3 == this.container.verticalRun) {
            return;
        }
        if (object2 == null) {
            object = new RunGroup((WidgetRun)object3, n2);
            arrayList.add((RunGroup)object);
        } else {
            object = object2;
        }
        ((WidgetRun)object3).runGroup = object;
        ((RunGroup)object).add((WidgetRun)object3);
        for (Dependency dependency : ((WidgetRun)object3).start.dependencies) {
            if (!(dependency instanceof DependencyNode)) continue;
            this.applyGroup((DependencyNode)dependency, n, 0, dependencyNode, arrayList, (RunGroup)object);
        }
        for (Dependency dependency : ((WidgetRun)object3).end.dependencies) {
            if (!(dependency instanceof DependencyNode)) continue;
            this.applyGroup((DependencyNode)dependency, n, 1, dependencyNode, arrayList, (RunGroup)object);
        }
        if (n == 1 && object3 instanceof VerticalWidgetRun) {
            for (Dependency dependency : ((VerticalWidgetRun)object3).baseline.dependencies) {
                if (!(dependency instanceof DependencyNode)) continue;
                this.applyGroup((DependencyNode)dependency, n, 2, dependencyNode, arrayList, (RunGroup)object);
            }
        }
        for (DependencyNode dependencyNode2 : ((WidgetRun)object3).start.targets) {
            if (dependencyNode2 == dependencyNode) {
                ((RunGroup)object).dual = true;
            }
            this.applyGroup(dependencyNode2, n, 0, dependencyNode, arrayList, (RunGroup)object);
        }
        for (DependencyNode dependencyNode3 : ((WidgetRun)object3).end.targets) {
            if (dependencyNode3 == dependencyNode) {
                ((RunGroup)object).dual = true;
            }
            this.applyGroup(dependencyNode3, n, 1, dependencyNode, arrayList, (RunGroup)object);
        }
        if (n != 1) return;
        if (!(object3 instanceof VerticalWidgetRun)) return;
        object3 = ((VerticalWidgetRun)object3).baseline.targets.iterator();
        while (object3.hasNext()) {
            DependencyNode dependencyNode4 = (DependencyNode)object3.next();
            this.applyGroup(dependencyNode4, n, 2, dependencyNode, arrayList, (RunGroup)object);
        }
    }

    private boolean basicMeasureWidgets(ConstraintWidgetContainer constraintWidgetContainer) {
        for (ConstraintWidget constraintWidget : constraintWidgetContainer.mChildren) {
            int n;
            int n2;
            ConstraintWidget.DimensionBehaviour dimensionBehaviour = constraintWidget.mListDimensionBehaviors[0];
            ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = constraintWidget.mListDimensionBehaviors[1];
            if (constraintWidget.getVisibility() == 8) {
                constraintWidget.measured = true;
                continue;
            }
            if (constraintWidget.mMatchConstraintPercentWidth < 1.0f && dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                constraintWidget.mMatchConstraintDefaultWidth = 2;
            }
            if (constraintWidget.mMatchConstraintPercentHeight < 1.0f && dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                constraintWidget.mMatchConstraintDefaultHeight = 2;
            }
            if (constraintWidget.getDimensionRatio() > 0.0f) {
                if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.FIXED)) {
                    constraintWidget.mMatchConstraintDefaultWidth = 3;
                } else if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || dimensionBehaviour == ConstraintWidget.DimensionBehaviour.FIXED)) {
                    constraintWidget.mMatchConstraintDefaultHeight = 3;
                } else if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    if (constraintWidget.mMatchConstraintDefaultWidth == 0) {
                        constraintWidget.mMatchConstraintDefaultWidth = 3;
                    }
                    if (constraintWidget.mMatchConstraintDefaultHeight == 0) {
                        constraintWidget.mMatchConstraintDefaultHeight = 3;
                    }
                }
            }
            if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mMatchConstraintDefaultWidth == 1 && (constraintWidget.mLeft.mTarget == null || constraintWidget.mRight.mTarget == null)) {
                dimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            }
            if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mMatchConstraintDefaultHeight == 1 && (constraintWidget.mTop.mTarget == null || constraintWidget.mBottom.mTarget == null)) {
                dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            }
            constraintWidget.horizontalRun.dimensionBehavior = dimensionBehaviour;
            constraintWidget.horizontalRun.matchConstraintsType = constraintWidget.mMatchConstraintDefaultWidth;
            constraintWidget.verticalRun.dimensionBehavior = dimensionBehaviour2;
            constraintWidget.verticalRun.matchConstraintsType = constraintWidget.mMatchConstraintDefaultHeight;
            if (dimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_PARENT && dimensionBehaviour != ConstraintWidget.DimensionBehaviour.FIXED && dimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || dimensionBehaviour2 != ConstraintWidget.DimensionBehaviour.MATCH_PARENT && dimensionBehaviour2 != ConstraintWidget.DimensionBehaviour.FIXED && dimensionBehaviour2 != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                float f;
                if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.FIXED)) {
                    if (constraintWidget.mMatchConstraintDefaultWidth == 3) {
                        if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                            this.measure(constraintWidget, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0);
                        }
                        n2 = constraintWidget.getHeight();
                        n = (int)((float)n2 * constraintWidget.mDimensionRatio + 0.5f);
                        this.measure(constraintWidget, ConstraintWidget.DimensionBehaviour.FIXED, n, ConstraintWidget.DimensionBehaviour.FIXED, n2);
                        constraintWidget.horizontalRun.dimension.resolve(constraintWidget.getWidth());
                        constraintWidget.verticalRun.dimension.resolve(constraintWidget.getHeight());
                        constraintWidget.measured = true;
                        continue;
                    }
                    if (constraintWidget.mMatchConstraintDefaultWidth == 1) {
                        this.measure(constraintWidget, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0, dimensionBehaviour2, 0);
                        constraintWidget.horizontalRun.dimension.wrapValue = constraintWidget.getWidth();
                        continue;
                    }
                    if (constraintWidget.mMatchConstraintDefaultWidth == 2) {
                        if (constraintWidgetContainer.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.FIXED || constraintWidgetContainer.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                            f = constraintWidget.mMatchConstraintPercentWidth;
                            n2 = (int)((float)constraintWidgetContainer.getWidth() * f + 0.5f);
                            n = constraintWidget.getHeight();
                            this.measure(constraintWidget, ConstraintWidget.DimensionBehaviour.FIXED, n2, dimensionBehaviour2, n);
                            constraintWidget.horizontalRun.dimension.resolve(constraintWidget.getWidth());
                            constraintWidget.verticalRun.dimension.resolve(constraintWidget.getHeight());
                            constraintWidget.measured = true;
                            continue;
                        }
                    } else if (constraintWidget.mListAnchors[0].mTarget == null || constraintWidget.mListAnchors[1].mTarget == null) {
                        this.measure(constraintWidget, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0, dimensionBehaviour2, 0);
                        constraintWidget.horizontalRun.dimension.resolve(constraintWidget.getWidth());
                        constraintWidget.verticalRun.dimension.resolve(constraintWidget.getHeight());
                        constraintWidget.measured = true;
                        continue;
                    }
                }
                if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || dimensionBehaviour == ConstraintWidget.DimensionBehaviour.FIXED)) {
                    if (constraintWidget.mMatchConstraintDefaultHeight == 3) {
                        if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                            this.measure(constraintWidget, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0);
                        }
                        n2 = constraintWidget.getWidth();
                        f = constraintWidget.mDimensionRatio;
                        if (constraintWidget.getDimensionRatioSide() == -1) {
                            f = 1.0f / f;
                        }
                        n = (int)((float)n2 * f + 0.5f);
                        this.measure(constraintWidget, ConstraintWidget.DimensionBehaviour.FIXED, n2, ConstraintWidget.DimensionBehaviour.FIXED, n);
                        constraintWidget.horizontalRun.dimension.resolve(constraintWidget.getWidth());
                        constraintWidget.verticalRun.dimension.resolve(constraintWidget.getHeight());
                        constraintWidget.measured = true;
                        continue;
                    }
                    if (constraintWidget.mMatchConstraintDefaultHeight == 1) {
                        this.measure(constraintWidget, dimensionBehaviour, 0, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0);
                        constraintWidget.verticalRun.dimension.wrapValue = constraintWidget.getHeight();
                        continue;
                    }
                    if (constraintWidget.mMatchConstraintDefaultHeight == 2) {
                        if (constraintWidgetContainer.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.FIXED || constraintWidgetContainer.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                            f = constraintWidget.mMatchConstraintPercentHeight;
                            n2 = constraintWidget.getWidth();
                            n = (int)((float)constraintWidgetContainer.getHeight() * f + 0.5f);
                            this.measure(constraintWidget, dimensionBehaviour, n2, ConstraintWidget.DimensionBehaviour.FIXED, n);
                            constraintWidget.horizontalRun.dimension.resolve(constraintWidget.getWidth());
                            constraintWidget.verticalRun.dimension.resolve(constraintWidget.getHeight());
                            constraintWidget.measured = true;
                            continue;
                        }
                    } else if (constraintWidget.mListAnchors[2].mTarget == null || constraintWidget.mListAnchors[3].mTarget == null) {
                        this.measure(constraintWidget, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0, dimensionBehaviour2, 0);
                        constraintWidget.horizontalRun.dimension.resolve(constraintWidget.getWidth());
                        constraintWidget.verticalRun.dimension.resolve(constraintWidget.getHeight());
                        constraintWidget.measured = true;
                        continue;
                    }
                }
                if (dimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || dimensionBehaviour2 != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) continue;
                if (constraintWidget.mMatchConstraintDefaultWidth != 1 && constraintWidget.mMatchConstraintDefaultHeight != 1) {
                    if (constraintWidget.mMatchConstraintDefaultHeight != 2 || constraintWidget.mMatchConstraintDefaultWidth != 2 || constraintWidgetContainer.mListDimensionBehaviors[0] != ConstraintWidget.DimensionBehaviour.FIXED && constraintWidgetContainer.mListDimensionBehaviors[0] != ConstraintWidget.DimensionBehaviour.FIXED || constraintWidgetContainer.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.FIXED && constraintWidgetContainer.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.FIXED) continue;
                    f = constraintWidget.mMatchConstraintPercentWidth;
                    float f2 = constraintWidget.mMatchConstraintPercentHeight;
                    n = (int)((float)constraintWidgetContainer.getWidth() * f + 0.5f);
                    n2 = (int)((float)constraintWidgetContainer.getHeight() * f2 + 0.5f);
                    this.measure(constraintWidget, ConstraintWidget.DimensionBehaviour.FIXED, n, ConstraintWidget.DimensionBehaviour.FIXED, n2);
                    constraintWidget.horizontalRun.dimension.resolve(constraintWidget.getWidth());
                    constraintWidget.verticalRun.dimension.resolve(constraintWidget.getHeight());
                    constraintWidget.measured = true;
                    continue;
                }
                this.measure(constraintWidget, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, 0);
                constraintWidget.horizontalRun.dimension.wrapValue = constraintWidget.getWidth();
                constraintWidget.verticalRun.dimension.wrapValue = constraintWidget.getHeight();
                continue;
            }
            n = constraintWidget.getWidth();
            ConstraintWidget.DimensionBehaviour dimensionBehaviour3 = dimensionBehaviour;
            if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                n = constraintWidgetContainer.getWidth() - constraintWidget.mLeft.mMargin - constraintWidget.mRight.mMargin;
                dimensionBehaviour3 = ConstraintWidget.DimensionBehaviour.FIXED;
            }
            n2 = constraintWidget.getHeight();
            if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                int n3 = constraintWidgetContainer.getHeight();
                int n4 = constraintWidget.mTop.mMargin;
                n2 = constraintWidget.mBottom.mMargin;
                dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.FIXED;
                n2 = n3 - n4 - n2;
            }
            this.measure(constraintWidget, dimensionBehaviour3, n, dimensionBehaviour2, n2);
            constraintWidget.horizontalRun.dimension.resolve(constraintWidget.getWidth());
            constraintWidget.verticalRun.dimension.resolve(constraintWidget.getHeight());
            constraintWidget.measured = true;
        }
        return false;
    }

    private int computeWrap(ConstraintWidgetContainer constraintWidgetContainer, int n) {
        int n2 = this.mGroups.size();
        long l = 0L;
        for (int i = 0; i < n2; ++i) {
            l = Math.max(l, this.mGroups.get(i).computeWrapSize(constraintWidgetContainer, n));
        }
        return (int)l;
    }

    private void displayGraph() {
        CharSequence charSequence = "digraph {\n";
        Object object = this.mRuns.iterator();
        while (object.hasNext()) {
            charSequence = this.generateDisplayGraph(object.next(), (String)charSequence);
        }
        object = new StringBuilder();
        ((StringBuilder)object).append((String)charSequence);
        ((StringBuilder)object).append("\n}\n");
        String string2 = ((StringBuilder)object).toString();
        object = System.out;
        charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append("content:<<\n");
        ((StringBuilder)charSequence).append(string2);
        ((StringBuilder)charSequence).append("\n>>");
        ((PrintStream)object).println(((StringBuilder)charSequence).toString());
    }

    /*
     * WARNING - void declaration
     */
    private void findGroup(WidgetRun dependency2, int n, ArrayList<RunGroup> arrayList) {
        void var3_5;
        void var2_4;
        for (Dependency dependency : ((WidgetRun)dependency2).start.dependencies) {
            if (dependency instanceof DependencyNode) {
                this.applyGroup((DependencyNode)dependency, (int)var2_4, 0, ((WidgetRun)dependency2).end, (ArrayList<RunGroup>)var3_5, null);
                continue;
            }
            if (!(dependency instanceof WidgetRun)) continue;
            this.applyGroup(((WidgetRun)dependency).start, (int)var2_4, 0, ((WidgetRun)dependency2).end, (ArrayList<RunGroup>)var3_5, null);
        }
        for (Dependency dependency : ((WidgetRun)dependency2).end.dependencies) {
            if (dependency instanceof DependencyNode) {
                this.applyGroup((DependencyNode)dependency, (int)var2_4, 1, ((WidgetRun)dependency2).start, (ArrayList<RunGroup>)var3_5, null);
                continue;
            }
            if (!(dependency instanceof WidgetRun)) continue;
            this.applyGroup(((WidgetRun)dependency).end, (int)var2_4, 1, ((WidgetRun)dependency2).start, (ArrayList<RunGroup>)var3_5, null);
        }
        if (var2_4 == true) {
            for (Dependency dependency : ((VerticalWidgetRun)dependency2).baseline.dependencies) {
                if (!(dependency instanceof DependencyNode)) continue;
                this.applyGroup((DependencyNode)dependency, (int)var2_4, 2, null, (ArrayList<RunGroup>)var3_5, null);
            }
        }
    }

    private String generateChainDisplayGraph(ChainRun object, String string2) {
        CharSequence charSequence;
        int n = ((ChainRun)object).orientation;
        CharSequence charSequence2 = new StringBuilder();
        ((StringBuilder)charSequence2).append("cluster_");
        ((StringBuilder)charSequence2).append(((ChainRun)object).widget.getDebugName());
        charSequence2 = ((StringBuilder)charSequence2).toString();
        if (n == 0) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append((String)charSequence2);
            ((StringBuilder)charSequence).append("_h");
            charSequence2 = ((StringBuilder)charSequence).toString();
        } else {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append((String)charSequence2);
            ((StringBuilder)charSequence).append("_v");
            charSequence2 = ((StringBuilder)charSequence).toString();
        }
        charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append("subgraph ");
        ((StringBuilder)charSequence).append((String)charSequence2);
        ((StringBuilder)charSequence).append(" {\n");
        charSequence2 = ((StringBuilder)charSequence).toString();
        charSequence = "";
        Iterator<WidgetRun> iterator2 = ((ChainRun)object).widgets.iterator();
        object = charSequence;
        while (iterator2.hasNext()) {
            StringBuilder stringBuilder;
            WidgetRun widgetRun = iterator2.next();
            charSequence = widgetRun.widget.getDebugName();
            if (n == 0) {
                stringBuilder = new StringBuilder();
                stringBuilder.append((String)charSequence);
                stringBuilder.append("_HORIZONTAL");
                charSequence = stringBuilder.toString();
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append((String)charSequence);
                stringBuilder.append("_VERTICAL");
                charSequence = stringBuilder.toString();
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append((String)charSequence2);
            stringBuilder.append((String)charSequence);
            stringBuilder.append(";\n");
            charSequence2 = stringBuilder.toString();
            object = this.generateDisplayGraph(widgetRun, (String)object);
        }
        charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append((String)charSequence2);
        ((StringBuilder)charSequence).append("}\n");
        charSequence = ((StringBuilder)charSequence).toString();
        charSequence2 = new StringBuilder();
        ((StringBuilder)charSequence2).append(string2);
        ((StringBuilder)charSequence2).append((String)object);
        ((StringBuilder)charSequence2).append((String)charSequence);
        return ((StringBuilder)charSequence2).toString();
    }

    private String generateDisplayGraph(WidgetRun widgetRun, String charSequence) {
        CharSequence charSequence2;
        block23: {
            Object object;
            DependencyNode dependencyNode;
            block21: {
                ConstraintWidget.DimensionBehaviour dimensionBehaviour;
                block22: {
                    dependencyNode = widgetRun.start;
                    object = widgetRun.end;
                    if (!(widgetRun instanceof HelperReferences) && dependencyNode.dependencies.isEmpty() && ((DependencyNode)object).dependencies.isEmpty() & dependencyNode.targets.isEmpty() && ((DependencyNode)object).targets.isEmpty()) {
                        return charSequence;
                    }
                    charSequence2 = new StringBuilder();
                    ((StringBuilder)charSequence2).append((String)charSequence);
                    ((StringBuilder)charSequence2).append(this.nodeDefinition(widgetRun));
                    charSequence = ((StringBuilder)charSequence2).toString();
                    boolean bl = this.isCenteredConnection(dependencyNode, (DependencyNode)object);
                    charSequence2 = this.generateDisplayNode((DependencyNode)object, bl, this.generateDisplayNode(dependencyNode, bl, (String)charSequence));
                    charSequence = charSequence2;
                    if (widgetRun instanceof VerticalWidgetRun) {
                        charSequence = this.generateDisplayNode(((VerticalWidgetRun)widgetRun).baseline, bl, (String)charSequence2);
                    }
                    if (widgetRun instanceof HorizontalWidgetRun || widgetRun instanceof ChainRun && ((ChainRun)widgetRun).orientation == 0) break block21;
                    if (widgetRun instanceof VerticalWidgetRun) break block22;
                    charSequence2 = charSequence;
                    if (!(widgetRun instanceof ChainRun)) break block23;
                    charSequence2 = charSequence;
                    if (((ChainRun)widgetRun).orientation != 1) break block23;
                }
                if ((dimensionBehaviour = widgetRun.widget.getVerticalDimensionBehaviour()) != ConstraintWidget.DimensionBehaviour.FIXED && dimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    charSequence2 = charSequence;
                    if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                        charSequence2 = charSequence;
                        if (widgetRun.widget.getDimensionRatio() > 0.0f) {
                            charSequence2 = widgetRun.widget.getDebugName();
                            object = new StringBuilder();
                            ((StringBuilder)object).append("\n");
                            ((StringBuilder)object).append((String)charSequence2);
                            ((StringBuilder)object).append("_VERTICAL -> ");
                            ((StringBuilder)object).append((String)charSequence2);
                            ((StringBuilder)object).append("_HORIZONTAL;\n");
                            ((StringBuilder)object).toString();
                            charSequence2 = charSequence;
                        }
                    }
                } else if (!dependencyNode.targets.isEmpty() && ((DependencyNode)object).targets.isEmpty()) {
                    charSequence2 = new StringBuilder();
                    ((StringBuilder)charSequence2).append("\n");
                    ((StringBuilder)charSequence2).append(((DependencyNode)object).name());
                    ((StringBuilder)charSequence2).append(" -> ");
                    ((StringBuilder)charSequence2).append(dependencyNode.name());
                    ((StringBuilder)charSequence2).append("\n");
                    charSequence2 = ((StringBuilder)charSequence2).toString();
                    object = new StringBuilder();
                    ((StringBuilder)object).append((String)charSequence);
                    ((StringBuilder)object).append((String)charSequence2);
                    charSequence2 = ((StringBuilder)object).toString();
                } else {
                    charSequence2 = charSequence;
                    if (dependencyNode.targets.isEmpty()) {
                        charSequence2 = charSequence;
                        if (!((DependencyNode)object).targets.isEmpty()) {
                            charSequence2 = new StringBuilder();
                            ((StringBuilder)charSequence2).append("\n");
                            ((StringBuilder)charSequence2).append(dependencyNode.name());
                            ((StringBuilder)charSequence2).append(" -> ");
                            ((StringBuilder)charSequence2).append(((DependencyNode)object).name());
                            ((StringBuilder)charSequence2).append("\n");
                            charSequence2 = ((StringBuilder)charSequence2).toString();
                            object = new StringBuilder();
                            ((StringBuilder)object).append((String)charSequence);
                            ((StringBuilder)object).append((String)charSequence2);
                            charSequence2 = ((StringBuilder)object).toString();
                        }
                    }
                }
                break block23;
            }
            ConstraintWidget.DimensionBehaviour dimensionBehaviour = widgetRun.widget.getHorizontalDimensionBehaviour();
            if (dimensionBehaviour != ConstraintWidget.DimensionBehaviour.FIXED && dimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                charSequence2 = charSequence;
                if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    charSequence2 = charSequence;
                    if (widgetRun.widget.getDimensionRatio() > 0.0f) {
                        object = widgetRun.widget.getDebugName();
                        charSequence2 = new StringBuilder();
                        ((StringBuilder)charSequence2).append("\n");
                        ((StringBuilder)charSequence2).append((String)object);
                        ((StringBuilder)charSequence2).append("_HORIZONTAL -> ");
                        ((StringBuilder)charSequence2).append((String)object);
                        ((StringBuilder)charSequence2).append("_VERTICAL;\n");
                        ((StringBuilder)charSequence2).toString();
                        charSequence2 = charSequence;
                    }
                }
            } else if (!dependencyNode.targets.isEmpty() && ((DependencyNode)object).targets.isEmpty()) {
                charSequence2 = new StringBuilder();
                ((StringBuilder)charSequence2).append("\n");
                ((StringBuilder)charSequence2).append(((DependencyNode)object).name());
                ((StringBuilder)charSequence2).append(" -> ");
                ((StringBuilder)charSequence2).append(dependencyNode.name());
                ((StringBuilder)charSequence2).append("\n");
                object = ((StringBuilder)charSequence2).toString();
                charSequence2 = new StringBuilder();
                ((StringBuilder)charSequence2).append((String)charSequence);
                ((StringBuilder)charSequence2).append((String)object);
                charSequence2 = ((StringBuilder)charSequence2).toString();
            } else {
                charSequence2 = charSequence;
                if (dependencyNode.targets.isEmpty()) {
                    charSequence2 = charSequence;
                    if (!((DependencyNode)object).targets.isEmpty()) {
                        charSequence2 = new StringBuilder();
                        ((StringBuilder)charSequence2).append("\n");
                        ((StringBuilder)charSequence2).append(dependencyNode.name());
                        ((StringBuilder)charSequence2).append(" -> ");
                        ((StringBuilder)charSequence2).append(((DependencyNode)object).name());
                        ((StringBuilder)charSequence2).append("\n");
                        object = ((StringBuilder)charSequence2).toString();
                        charSequence2 = new StringBuilder();
                        ((StringBuilder)charSequence2).append((String)charSequence);
                        ((StringBuilder)charSequence2).append((String)object);
                        charSequence2 = ((StringBuilder)charSequence2).toString();
                    }
                }
            }
        }
        if (widgetRun instanceof ChainRun) {
            return this.generateChainDisplayGraph((ChainRun)widgetRun, (String)charSequence2);
        }
        return charSequence2;
    }

    private String generateDisplayNode(DependencyNode dependencyNode, boolean bl, String object) {
        Iterator<DependencyNode> iterator2 = dependencyNode.targets.iterator();
        String string2 = object;
        while (iterator2.hasNext()) {
            Object object2;
            block9: {
                block8: {
                    object = iterator2.next();
                    object2 = new StringBuilder();
                    ((StringBuilder)object2).append("\n");
                    ((StringBuilder)object2).append(dependencyNode.name());
                    object2 = ((StringBuilder)object2).toString();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append((String)object2);
                    stringBuilder.append(" -> ");
                    stringBuilder.append(((DependencyNode)object).name());
                    object2 = stringBuilder.toString();
                    if (dependencyNode.margin > 0 || bl) break block8;
                    object = object2;
                    if (!(dependencyNode.run instanceof HelperReferences)) break block9;
                }
                object = new StringBuilder();
                ((StringBuilder)object).append((String)object2);
                ((StringBuilder)object).append("[");
                object = object2 = ((StringBuilder)object).toString();
                if (dependencyNode.margin > 0) {
                    object = new StringBuilder();
                    ((StringBuilder)object).append((String)object2);
                    ((StringBuilder)object).append("label=\"");
                    ((StringBuilder)object).append(dependencyNode.margin);
                    ((StringBuilder)object).append("\"");
                    object = object2 = ((StringBuilder)object).toString();
                    if (bl) {
                        object = new StringBuilder();
                        ((StringBuilder)object).append((String)object2);
                        ((StringBuilder)object).append(",");
                        object = ((StringBuilder)object).toString();
                    }
                }
                object2 = object;
                if (bl) {
                    object2 = new StringBuilder();
                    ((StringBuilder)object2).append((String)object);
                    ((StringBuilder)object2).append(" style=dashed ");
                    object2 = ((StringBuilder)object2).toString();
                }
                object = object2;
                if (dependencyNode.run instanceof HelperReferences) {
                    object = new StringBuilder();
                    ((StringBuilder)object).append((String)object2);
                    ((StringBuilder)object).append(" style=bold,color=gray ");
                    object = ((StringBuilder)object).toString();
                }
                object2 = new StringBuilder();
                ((StringBuilder)object2).append((String)object);
                ((StringBuilder)object2).append("]");
                object = ((StringBuilder)object2).toString();
            }
            object2 = new StringBuilder();
            ((StringBuilder)object2).append((String)object);
            ((StringBuilder)object2).append("\n");
            object = ((StringBuilder)object2).toString();
            object2 = new StringBuilder();
            ((StringBuilder)object2).append(string2);
            ((StringBuilder)object2).append((String)object);
            string2 = ((StringBuilder)object2).toString();
        }
        return string2;
    }

    private boolean isCenteredConnection(DependencyNode dependencyNode, DependencyNode object) {
        int n;
        int n2 = 0;
        int n3 = 0;
        Iterator<DependencyNode> iterator2 = dependencyNode.targets.iterator();
        while (iterator2.hasNext()) {
            n = n2;
            if (iterator2.next() != object) {
                n = n2 + 1;
            }
            n2 = n;
        }
        object = ((DependencyNode)object).targets.iterator();
        n = n3;
        while (object.hasNext()) {
            n3 = n;
            if ((DependencyNode)object.next() != dependencyNode) {
                n3 = n + 1;
            }
            n = n3;
        }
        boolean bl = n2 > 0 && n > 0;
        return bl;
    }

    private void measure(ConstraintWidget constraintWidget, ConstraintWidget.DimensionBehaviour dimensionBehaviour, int n, ConstraintWidget.DimensionBehaviour dimensionBehaviour2, int n2) {
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

    private String nodeDefinition(WidgetRun object) {
        boolean bl = object instanceof VerticalWidgetRun;
        String string2 = ((WidgetRun)object).widget.getDebugName();
        Object object2 = ((WidgetRun)object).widget;
        Object object3 = !bl ? ((ConstraintWidget)object2).getHorizontalDimensionBehaviour() : ((ConstraintWidget)object2).getVerticalDimensionBehaviour();
        RunGroup runGroup = ((WidgetRun)object).runGroup;
        if (!bl) {
            object2 = new StringBuilder();
            ((StringBuilder)object2).append(string2);
            ((StringBuilder)object2).append("_HORIZONTAL");
            object2 = ((StringBuilder)object2).toString();
        } else {
            object2 = new StringBuilder();
            ((StringBuilder)object2).append(string2);
            ((StringBuilder)object2).append("_VERTICAL");
            object2 = ((StringBuilder)object2).toString();
        }
        Object object4 = new StringBuilder();
        ((StringBuilder)object4).append((String)object2);
        ((StringBuilder)object4).append(" [shape=none, label=<");
        object2 = ((StringBuilder)object4).toString();
        object4 = new StringBuilder();
        ((StringBuilder)object4).append((String)object2);
        ((StringBuilder)object4).append("<TABLE BORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"2\">");
        object2 = ((StringBuilder)object4).toString();
        object4 = new StringBuilder();
        ((StringBuilder)object4).append((String)object2);
        ((StringBuilder)object4).append("  <TR>");
        object2 = ((StringBuilder)object4).toString();
        if (!bl) {
            object4 = new StringBuilder();
            ((StringBuilder)object4).append((String)object2);
            ((StringBuilder)object4).append("    <TD ");
            object2 = object4 = ((StringBuilder)object4).toString();
            if (((WidgetRun)object).start.resolved) {
                object2 = new StringBuilder();
                ((StringBuilder)object2).append((String)object4);
                ((StringBuilder)object2).append(" BGCOLOR=\"green\"");
                object2 = ((StringBuilder)object2).toString();
            }
            object4 = new StringBuilder();
            ((StringBuilder)object4).append((String)object2);
            ((StringBuilder)object4).append(" PORT=\"LEFT\" BORDER=\"1\">L</TD>");
            object2 = ((StringBuilder)object4).toString();
        } else {
            object4 = new StringBuilder();
            ((StringBuilder)object4).append((String)object2);
            ((StringBuilder)object4).append("    <TD ");
            object2 = object4 = ((StringBuilder)object4).toString();
            if (((WidgetRun)object).start.resolved) {
                object2 = new StringBuilder();
                ((StringBuilder)object2).append((String)object4);
                ((StringBuilder)object2).append(" BGCOLOR=\"green\"");
                object2 = ((StringBuilder)object2).toString();
            }
            object4 = new StringBuilder();
            ((StringBuilder)object4).append((String)object2);
            ((StringBuilder)object4).append(" PORT=\"TOP\" BORDER=\"1\">T</TD>");
            object2 = ((StringBuilder)object4).toString();
        }
        object4 = new StringBuilder();
        ((StringBuilder)object4).append((String)object2);
        ((StringBuilder)object4).append("    <TD BORDER=\"1\" ");
        object4 = ((StringBuilder)object4).toString();
        if (((WidgetRun)object).dimension.resolved && !((WidgetRun)object).widget.measured) {
            object2 = new StringBuilder();
            ((StringBuilder)object2).append((String)object4);
            ((StringBuilder)object2).append(" BGCOLOR=\"green\" ");
            object2 = ((StringBuilder)object2).toString();
        } else if (((WidgetRun)object).dimension.resolved && ((WidgetRun)object).widget.measured) {
            object2 = new StringBuilder();
            ((StringBuilder)object2).append((String)object4);
            ((StringBuilder)object2).append(" BGCOLOR=\"lightgray\" ");
            object2 = ((StringBuilder)object2).toString();
        } else {
            object2 = object4;
            if (!((WidgetRun)object).dimension.resolved) {
                object2 = object4;
                if (((WidgetRun)object).widget.measured) {
                    object2 = new StringBuilder();
                    ((StringBuilder)object2).append((String)object4);
                    ((StringBuilder)object2).append(" BGCOLOR=\"yellow\" ");
                    object2 = ((StringBuilder)object2).toString();
                }
            }
        }
        object4 = object2;
        if (object3 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            object3 = new StringBuilder();
            ((StringBuilder)object3).append((String)object2);
            ((StringBuilder)object3).append("style=\"dashed\"");
            object4 = ((StringBuilder)object3).toString();
        }
        object2 = "";
        if (runGroup != null) {
            object2 = new StringBuilder();
            ((StringBuilder)object2).append(" [");
            ((StringBuilder)object2).append(runGroup.groupIndex + 1);
            ((StringBuilder)object2).append("/");
            ((StringBuilder)object2).append(RunGroup.index);
            ((StringBuilder)object2).append("]");
            object2 = ((StringBuilder)object2).toString();
        }
        object3 = new StringBuilder();
        ((StringBuilder)object3).append((String)object4);
        ((StringBuilder)object3).append(">");
        ((StringBuilder)object3).append(string2);
        ((StringBuilder)object3).append((String)object2);
        ((StringBuilder)object3).append(" </TD>");
        object2 = ((StringBuilder)object3).toString();
        if (!bl) {
            object3 = new StringBuilder();
            ((StringBuilder)object3).append((String)object2);
            ((StringBuilder)object3).append("    <TD ");
            object2 = object3 = ((StringBuilder)object3).toString();
            if (((WidgetRun)object).end.resolved) {
                object = new StringBuilder();
                ((StringBuilder)object).append((String)object3);
                ((StringBuilder)object).append(" BGCOLOR=\"green\"");
                object2 = ((StringBuilder)object).toString();
            }
            object = new StringBuilder();
            ((StringBuilder)object).append((String)object2);
            ((StringBuilder)object).append(" PORT=\"RIGHT\" BORDER=\"1\">R</TD>");
            object = ((StringBuilder)object).toString();
        } else {
            object3 = new StringBuilder();
            ((StringBuilder)object3).append((String)object2);
            ((StringBuilder)object3).append("    <TD ");
            object2 = object3 = ((StringBuilder)object3).toString();
            if (object instanceof VerticalWidgetRun) {
                object2 = object3;
                if (((VerticalWidgetRun)object).baseline.resolved) {
                    object2 = new StringBuilder();
                    ((StringBuilder)object2).append((String)object3);
                    ((StringBuilder)object2).append(" BGCOLOR=\"green\"");
                    object2 = ((StringBuilder)object2).toString();
                }
            }
            object3 = new StringBuilder();
            ((StringBuilder)object3).append((String)object2);
            ((StringBuilder)object3).append(" PORT=\"BASELINE\" BORDER=\"1\">b</TD>");
            object2 = ((StringBuilder)object3).toString();
            object3 = new StringBuilder();
            ((StringBuilder)object3).append((String)object2);
            ((StringBuilder)object3).append("    <TD ");
            object2 = object3 = ((StringBuilder)object3).toString();
            if (((WidgetRun)object).end.resolved) {
                object = new StringBuilder();
                ((StringBuilder)object).append((String)object3);
                ((StringBuilder)object).append(" BGCOLOR=\"green\"");
                object2 = ((StringBuilder)object).toString();
            }
            object = new StringBuilder();
            ((StringBuilder)object).append((String)object2);
            ((StringBuilder)object).append(" PORT=\"BOTTOM\" BORDER=\"1\">B</TD>");
            object = ((StringBuilder)object).toString();
        }
        object2 = new StringBuilder();
        ((StringBuilder)object2).append((String)object);
        ((StringBuilder)object2).append("  </TR></TABLE>");
        object = ((StringBuilder)object2).toString();
        object2 = new StringBuilder();
        ((StringBuilder)object2).append((String)object);
        ((StringBuilder)object2).append(">];\n");
        return ((StringBuilder)object2).toString();
    }

    public void buildGraph() {
        this.buildGraph(this.mRuns);
        this.mGroups.clear();
        RunGroup.index = 0;
        this.findGroup(this.container.horizontalRun, 0, this.mGroups);
        this.findGroup(this.container.verticalRun, 1, this.mGroups);
        this.mNeedBuildGraph = false;
    }

    public void buildGraph(ArrayList<WidgetRun> object) {
        ((ArrayList)object).clear();
        this.mContainer.horizontalRun.clear();
        this.mContainer.verticalRun.clear();
        ((ArrayList)object).add((WidgetRun)this.mContainer.horizontalRun);
        ((ArrayList)object).add((WidgetRun)this.mContainer.verticalRun);
        HashSet<ChainRun> hashSet = null;
        for (ConstraintWidget constraintWidget : this.mContainer.mChildren) {
            HashSet<ChainRun> hashSet2;
            if (constraintWidget instanceof Guideline) {
                ((ArrayList)object).add((WidgetRun)new GuidelineReference(constraintWidget));
                continue;
            }
            if (constraintWidget.isInHorizontalChain()) {
                if (constraintWidget.horizontalChainRun == null) {
                    constraintWidget.horizontalChainRun = new ChainRun(constraintWidget, 0);
                }
                hashSet2 = hashSet;
                if (hashSet == null) {
                    hashSet2 = new HashSet();
                }
                hashSet2.add(constraintWidget.horizontalChainRun);
                hashSet = hashSet2;
            } else {
                ((ArrayList)object).add((WidgetRun)constraintWidget.horizontalRun);
            }
            if (constraintWidget.isInVerticalChain()) {
                if (constraintWidget.verticalChainRun == null) {
                    constraintWidget.verticalChainRun = new ChainRun(constraintWidget, 1);
                }
                hashSet2 = hashSet;
                if (hashSet == null) {
                    hashSet2 = new HashSet<ChainRun>();
                }
                hashSet2.add(constraintWidget.verticalChainRun);
                hashSet = hashSet2;
            } else {
                ((ArrayList)object).add(constraintWidget.verticalRun);
            }
            if (!(constraintWidget instanceof HelperWidget)) continue;
            ((ArrayList)object).add(new HelperReferences(constraintWidget));
        }
        if (hashSet != null) {
            ((ArrayList)object).addAll(hashSet);
        }
        hashSet = ((ArrayList)object).iterator();
        while (hashSet.hasNext()) {
            hashSet.next().clear();
        }
        object = ((ArrayList)object).iterator();
        while (object.hasNext()) {
            hashSet = (WidgetRun)object.next();
            if (((WidgetRun)((Object)hashSet)).widget == this.mContainer) continue;
            ((WidgetRun)((Object)hashSet)).apply();
        }
    }

    public void defineTerminalWidgets(ConstraintWidget.DimensionBehaviour dimensionBehaviour, ConstraintWidget.DimensionBehaviour dimensionBehaviour2) {
        if (this.mNeedBuildGraph) {
            this.buildGraph();
            boolean bl = false;
            for (Object object : this.container.mChildren) {
                ((ConstraintWidget)object).isTerminalWidget[0] = true;
                ((ConstraintWidget)object).isTerminalWidget[1] = true;
                if (!(object instanceof Barrier)) continue;
                bl = true;
            }
            if (!bl) {
                for (RunGroup runGroup : this.mGroups) {
                    boolean bl2 = dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                    boolean bl3 = dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                    runGroup.defineTerminalWidgets(bl2, bl3);
                }
            }
        }
    }

    public boolean directMeasure(boolean bl) {
        Object object;
        Object object222;
        block16: {
            int n;
            int n2 = bl & 1;
            if (this.mNeedBuildGraph || this.mNeedRedoMeasures) {
                for (Object object222 : this.container.mChildren) {
                    object222.ensureWidgetRuns();
                    object222.measured = false;
                    object222.horizontalRun.reset();
                    object222.verticalRun.reset();
                }
                this.container.ensureWidgetRuns();
                this.container.measured = false;
                this.container.horizontalRun.reset();
                this.container.verticalRun.reset();
                this.mNeedRedoMeasures = false;
            }
            if (this.basicMeasureWidgets(this.mContainer)) {
                return false;
            }
            this.container.setX(0);
            this.container.setY(0);
            object222 = this.container.getDimensionBehaviour(0);
            object = this.container.getDimensionBehaviour(1);
            if (this.mNeedBuildGraph) {
                this.buildGraph();
            }
            int n3 = this.container.getX();
            int n4 = this.container.getY();
            this.container.horizontalRun.start.resolve(n3);
            this.container.verticalRun.start.resolve(n4);
            this.measureWidgets();
            if (object222 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || object == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                block15: {
                    n = n2;
                    if (n2 != 0) {
                        Iterator<WidgetRun> iterator22 = this.mRuns.iterator();
                        do {
                            n = n2;
                            if (!iterator22.hasNext()) break block15;
                        } while (iterator22.next().supportsWrapComputation());
                        n = 0;
                    }
                }
                if (n != 0 && object222 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    this.container.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                    ConstraintWidgetContainer constraintWidgetContainer = this.container;
                    constraintWidgetContainer.setWidth(this.computeWrap(constraintWidgetContainer, 0));
                    this.container.horizontalRun.dimension.resolve(this.container.getWidth());
                }
                if (n != 0 && object == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    this.container.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                    ConstraintWidgetContainer constraintWidgetContainer = this.container;
                    constraintWidgetContainer.setHeight(this.computeWrap(constraintWidgetContainer, 1));
                    this.container.verticalRun.dimension.resolve(this.container.getHeight());
                }
            }
            n = 0;
            if (this.container.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.FIXED || this.container.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                n = this.container.getWidth() + n3;
                this.container.horizontalRun.end.resolve(n);
                this.container.horizontalRun.dimension.resolve(n - n3);
                this.measureWidgets();
                if (this.container.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.FIXED || this.container.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                    n = this.container.getHeight() + n4;
                    this.container.verticalRun.end.resolve(n);
                    this.container.verticalRun.dimension.resolve(n - n4);
                }
                this.measureWidgets();
                n = 1;
            }
            for (WidgetRun widgetRun : this.mRuns) {
                if (widgetRun.widget == this.container && !widgetRun.resolved) continue;
                widgetRun.applyToWidget();
            }
            boolean bl2 = true;
            Iterator<WidgetRun> iterator2 = this.mRuns.iterator();
            while (true) {
                bl = bl2;
                if (!iterator2.hasNext()) break block16;
                WidgetRun widgetRun = iterator2.next();
                if (n == 0 && widgetRun.widget == this.container) continue;
                if (!widgetRun.start.resolved) {
                    bl = false;
                    break block16;
                }
                if (!widgetRun.end.resolved && !(widgetRun instanceof GuidelineReference)) {
                    bl = false;
                    break block16;
                }
                if (!(widgetRun.dimension.resolved || widgetRun instanceof ChainRun || widgetRun instanceof GuidelineReference)) break;
            }
            bl = false;
        }
        this.container.setHorizontalDimensionBehaviour((ConstraintWidget.DimensionBehaviour)((Object)object222));
        this.container.setVerticalDimensionBehaviour((ConstraintWidget.DimensionBehaviour)((Object)object));
        return bl;
    }

    public boolean directMeasureSetup(boolean bl) {
        if (this.mNeedBuildGraph) {
            for (ConstraintWidget constraintWidget : this.container.mChildren) {
                constraintWidget.ensureWidgetRuns();
                constraintWidget.measured = false;
                constraintWidget.horizontalRun.dimension.resolved = false;
                constraintWidget.horizontalRun.resolved = false;
                constraintWidget.horizontalRun.reset();
                constraintWidget.verticalRun.dimension.resolved = false;
                constraintWidget.verticalRun.resolved = false;
                constraintWidget.verticalRun.reset();
            }
            this.container.ensureWidgetRuns();
            this.container.measured = false;
            this.container.horizontalRun.dimension.resolved = false;
            this.container.horizontalRun.resolved = false;
            this.container.horizontalRun.reset();
            this.container.verticalRun.dimension.resolved = false;
            this.container.verticalRun.resolved = false;
            this.container.verticalRun.reset();
            this.buildGraph();
        }
        if (this.basicMeasureWidgets(this.mContainer)) {
            return false;
        }
        this.container.setX(0);
        this.container.setY(0);
        this.container.horizontalRun.start.resolve(0);
        this.container.verticalRun.start.resolve(0);
        return true;
    }

    public boolean directMeasureWithOrientation(boolean bl, int n) {
        ConstraintWidget.DimensionBehaviour dimensionBehaviour;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour2;
        block15: {
            int n2;
            WidgetRun widgetRun2;
            Iterator<WidgetRun> iterator2;
            int n3 = bl & 1;
            dimensionBehaviour2 = this.container.getDimensionBehaviour(0);
            dimensionBehaviour = this.container.getDimensionBehaviour(1);
            int n4 = this.container.getX();
            int n5 = this.container.getY();
            if (n3 != 0 && (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)) {
                block14: {
                    iterator2 = this.mRuns.iterator();
                    do {
                        n2 = n3;
                        if (!iterator2.hasNext()) break block14;
                        widgetRun2 = iterator2.next();
                    } while (widgetRun2.orientation != n || widgetRun2.supportsWrapComputation());
                    n2 = 0;
                }
                if (n == 0) {
                    if (n2 != 0 && dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                        this.container.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                        iterator2 = this.container;
                        ((ConstraintWidget)((Object)iterator2)).setWidth(this.computeWrap((ConstraintWidgetContainer)((Object)iterator2), 0));
                        this.container.horizontalRun.dimension.resolve(this.container.getWidth());
                    }
                } else if (n2 != 0 && dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    this.container.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                    iterator2 = this.container;
                    ((ConstraintWidget)((Object)iterator2)).setHeight(this.computeWrap((ConstraintWidgetContainer)((Object)iterator2), 1));
                    this.container.verticalRun.dimension.resolve(this.container.getHeight());
                }
            }
            n2 = 0;
            if (n == 0) {
                if (this.container.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.FIXED || this.container.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                    n2 = this.container.getWidth() + n4;
                    this.container.horizontalRun.end.resolve(n2);
                    this.container.horizontalRun.dimension.resolve(n2 - n4);
                    n2 = 1;
                }
            } else if (this.container.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.FIXED || this.container.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                n2 = this.container.getHeight() + n5;
                this.container.verticalRun.end.resolve(n2);
                this.container.verticalRun.dimension.resolve(n2 - n5);
                n2 = 1;
            }
            this.measureWidgets();
            for (WidgetRun widgetRun2 : this.mRuns) {
                if (widgetRun2.orientation != n || widgetRun2.widget == this.container && !widgetRun2.resolved) continue;
                widgetRun2.applyToWidget();
            }
            boolean bl2 = true;
            iterator2 = this.mRuns.iterator();
            while (true) {
                bl = bl2;
                if (!iterator2.hasNext()) break block15;
                widgetRun2 = iterator2.next();
                if (widgetRun2.orientation != n || n2 == 0 && widgetRun2.widget == this.container) continue;
                if (!widgetRun2.start.resolved) {
                    bl = false;
                    break block15;
                }
                if (!widgetRun2.end.resolved) {
                    bl = false;
                    break block15;
                }
                if (!(widgetRun2 instanceof ChainRun) && !widgetRun2.dimension.resolved) break;
            }
            bl = false;
        }
        this.container.setHorizontalDimensionBehaviour(dimensionBehaviour2);
        this.container.setVerticalDimensionBehaviour(dimensionBehaviour);
        return bl;
    }

    public void invalidateGraph() {
        this.mNeedBuildGraph = true;
    }

    public void invalidateMeasures() {
        this.mNeedRedoMeasures = true;
    }

    public void measureWidgets() {
        for (ConstraintWidget constraintWidget : this.container.mChildren) {
            boolean bl;
            int n;
            ConstraintWidget.DimensionBehaviour dimensionBehaviour;
            Object object;
            block14: {
                block13: {
                    if (constraintWidget.measured) continue;
                    object = constraintWidget.mListDimensionBehaviors;
                    boolean bl2 = false;
                    dimensionBehaviour = object[0];
                    object = constraintWidget.mListDimensionBehaviors[1];
                    n = constraintWidget.mMatchConstraintDefaultWidth;
                    int n2 = constraintWidget.mMatchConstraintDefaultHeight;
                    n = dimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && (dimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || n != 1) ? 0 : 1;
                    if (object == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) break block13;
                    bl = bl2;
                    if (object != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) break block14;
                    bl = bl2;
                    if (n2 != 1) break block14;
                }
                bl = true;
            }
            boolean bl3 = constraintWidget.horizontalRun.dimension.resolved;
            boolean bl4 = constraintWidget.verticalRun.dimension.resolved;
            if (bl3 && bl4) {
                this.measure(constraintWidget, ConstraintWidget.DimensionBehaviour.FIXED, constraintWidget.horizontalRun.dimension.value, ConstraintWidget.DimensionBehaviour.FIXED, constraintWidget.verticalRun.dimension.value);
                constraintWidget.measured = true;
            } else if (bl3 && bl) {
                this.measure(constraintWidget, ConstraintWidget.DimensionBehaviour.FIXED, constraintWidget.horizontalRun.dimension.value, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, constraintWidget.verticalRun.dimension.value);
                if (object == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    constraintWidget.verticalRun.dimension.wrapValue = constraintWidget.getHeight();
                } else {
                    constraintWidget.verticalRun.dimension.resolve(constraintWidget.getHeight());
                    constraintWidget.measured = true;
                }
            } else if (bl4 && n != 0) {
                this.measure(constraintWidget, ConstraintWidget.DimensionBehaviour.WRAP_CONTENT, constraintWidget.horizontalRun.dimension.value, ConstraintWidget.DimensionBehaviour.FIXED, constraintWidget.verticalRun.dimension.value);
                if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    constraintWidget.horizontalRun.dimension.wrapValue = constraintWidget.getWidth();
                } else {
                    constraintWidget.horizontalRun.dimension.resolve(constraintWidget.getWidth());
                    constraintWidget.measured = true;
                }
            }
            if (!constraintWidget.measured || constraintWidget.verticalRun.baselineDimension == null) continue;
            constraintWidget.verticalRun.baselineDimension.resolve(constraintWidget.getBaselineDistance());
        }
    }

    public void setMeasurer(BasicMeasure.Measurer measurer) {
        this.mMeasurer = measurer;
    }
}

