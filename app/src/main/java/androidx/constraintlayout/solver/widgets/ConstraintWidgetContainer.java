/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.Metrics;
import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.widgets.Barrier;
import androidx.constraintlayout.solver.widgets.Chain;
import androidx.constraintlayout.solver.widgets.ChainHead;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.Guideline;
import androidx.constraintlayout.solver.widgets.Optimizer;
import androidx.constraintlayout.solver.widgets.VirtualLayout;
import androidx.constraintlayout.solver.widgets.WidgetContainer;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import androidx.constraintlayout.solver.widgets.analyzer.DependencyGraph;
import androidx.constraintlayout.solver.widgets.analyzer.Direct;
import androidx.constraintlayout.solver.widgets.analyzer.Grouping;
import java.io.PrintStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class ConstraintWidgetContainer
extends WidgetContainer {
    private static final boolean DEBUG = false;
    static final boolean DEBUG_GRAPH = false;
    private static final boolean DEBUG_LAYOUT = false;
    private static final int MAX_ITERATIONS = 8;
    static int mycounter = 0;
    private WeakReference<ConstraintAnchor> horizontalWrapMax = null;
    private WeakReference<ConstraintAnchor> horizontalWrapMin = null;
    BasicMeasure mBasicMeasureSolver = new BasicMeasure(this);
    int mDebugSolverPassCount = 0;
    public DependencyGraph mDependencyGraph = new DependencyGraph(this);
    public boolean mGroupsWrapOptimized = false;
    private boolean mHeightMeasuredTooSmall = false;
    ChainHead[] mHorizontalChainsArray;
    public int mHorizontalChainsSize = 0;
    public boolean mHorizontalWrapOptimized = false;
    private boolean mIsRtl = false;
    public BasicMeasure.Measure mMeasure;
    protected BasicMeasure.Measurer mMeasurer = null;
    public Metrics mMetrics;
    private int mOptimizationLevel = 257;
    int mPaddingBottom;
    int mPaddingLeft;
    int mPaddingRight;
    int mPaddingTop;
    public boolean mSkipSolver = false;
    protected LinearSystem mSystem = new LinearSystem();
    ChainHead[] mVerticalChainsArray = new ChainHead[4];
    public int mVerticalChainsSize = 0;
    public boolean mVerticalWrapOptimized = false;
    private boolean mWidthMeasuredTooSmall = false;
    public int mWrapFixedHeight = 0;
    public int mWrapFixedWidth = 0;
    private WeakReference<ConstraintAnchor> verticalWrapMax = null;
    private WeakReference<ConstraintAnchor> verticalWrapMin = null;

    public ConstraintWidgetContainer() {
        this.mHorizontalChainsArray = new ChainHead[4];
        this.mMeasure = new BasicMeasure.Measure();
    }

    public ConstraintWidgetContainer(int n, int n2) {
        super(n, n2);
        this.mHorizontalChainsArray = new ChainHead[4];
        this.mMeasure = new BasicMeasure.Measure();
    }

    public ConstraintWidgetContainer(int n, int n2, int n3, int n4) {
        super(n, n2, n3, n4);
        this.mHorizontalChainsArray = new ChainHead[4];
        this.mMeasure = new BasicMeasure.Measure();
    }

    public ConstraintWidgetContainer(String string2, int n, int n2) {
        super(n, n2);
        this.mHorizontalChainsArray = new ChainHead[4];
        this.mMeasure = new BasicMeasure.Measure();
        this.setDebugName(string2);
    }

    private void addHorizontalChain(ConstraintWidget constraintWidget) {
        int n = this.mHorizontalChainsSize;
        ChainHead[] chainHeadArray = this.mHorizontalChainsArray;
        if (n + 1 >= chainHeadArray.length) {
            this.mHorizontalChainsArray = Arrays.copyOf(chainHeadArray, chainHeadArray.length * 2);
        }
        this.mHorizontalChainsArray[this.mHorizontalChainsSize] = new ChainHead(constraintWidget, 0, this.isRtl());
        ++this.mHorizontalChainsSize;
    }

    private void addMaxWrap(ConstraintAnchor object, SolverVariable solverVariable) {
        object = this.mSystem.createObjectVariable(object);
        this.mSystem.addGreaterThan(solverVariable, (SolverVariable)object, 0, 5);
    }

    private void addMinWrap(ConstraintAnchor object, SolverVariable solverVariable) {
        object = this.mSystem.createObjectVariable(object);
        this.mSystem.addGreaterThan((SolverVariable)object, solverVariable, 0, 5);
    }

    private void addVerticalChain(ConstraintWidget constraintWidget) {
        int n = this.mVerticalChainsSize;
        ChainHead[] chainHeadArray = this.mVerticalChainsArray;
        if (n + 1 >= chainHeadArray.length) {
            this.mVerticalChainsArray = Arrays.copyOf(chainHeadArray, chainHeadArray.length * 2);
        }
        this.mVerticalChainsArray[this.mVerticalChainsSize] = new ChainHead(constraintWidget, 1, this.isRtl());
        ++this.mVerticalChainsSize;
    }

    public static boolean measure(ConstraintWidget constraintWidget, BasicMeasure.Measurer measurer, BasicMeasure.Measure measure, int n) {
        if (measurer == null) {
            return false;
        }
        measure.horizontalBehavior = constraintWidget.getHorizontalDimensionBehaviour();
        measure.verticalBehavior = constraintWidget.getVerticalDimensionBehaviour();
        measure.horizontalDimension = constraintWidget.getWidth();
        measure.verticalDimension = constraintWidget.getHeight();
        measure.measuredNeedsSolverPass = false;
        measure.measureStrategy = n;
        int n2 = measure.horizontalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT ? 1 : 0;
        n = measure.verticalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT ? 1 : 0;
        boolean bl = n2 != 0 && constraintWidget.mDimensionRatio > 0.0f;
        boolean bl2 = n != 0 && constraintWidget.mDimensionRatio > 0.0f;
        int n3 = n2;
        if (n2 != 0) {
            n3 = n2;
            if (constraintWidget.hasDanglingDimension(0)) {
                n3 = n2;
                if (constraintWidget.mMatchConstraintDefaultWidth == 0) {
                    n3 = n2;
                    if (!bl) {
                        n2 = 0;
                        measure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                        n3 = n2;
                        if (n != 0) {
                            n3 = n2;
                            if (constraintWidget.mMatchConstraintDefaultHeight == 0) {
                                measure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
                                n3 = n2;
                            }
                        }
                    }
                }
            }
        }
        n2 = n;
        if (n != 0) {
            n2 = n;
            if (constraintWidget.hasDanglingDimension(1)) {
                n2 = n;
                if (constraintWidget.mMatchConstraintDefaultHeight == 0) {
                    n2 = n;
                    if (!bl2) {
                        n = 0;
                        measure.verticalBehavior = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                        n2 = n;
                        if (n3 != 0) {
                            n2 = n;
                            if (constraintWidget.mMatchConstraintDefaultWidth == 0) {
                                measure.verticalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
                                n2 = n;
                            }
                        }
                    }
                }
            }
        }
        if (constraintWidget.isResolvedHorizontally()) {
            n3 = 0;
            measure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
        }
        if (constraintWidget.isResolvedVertically()) {
            n2 = 0;
            measure.verticalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
        }
        if (bl) {
            if (constraintWidget.mResolvedMatchConstraintDefault[0] == 4) {
                measure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
            } else if (n2 == 0) {
                if (measure.verticalBehavior == ConstraintWidget.DimensionBehaviour.FIXED) {
                    n = measure.verticalDimension;
                } else {
                    measure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                    measurer.measure(constraintWidget, measure);
                    n = measure.measuredHeight;
                }
                measure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
                measure.horizontalDimension = constraintWidget.mDimensionRatioSide != 0 && constraintWidget.mDimensionRatioSide != -1 ? (int)(constraintWidget.getDimensionRatio() / (float)n) : (int)(constraintWidget.getDimensionRatio() * (float)n);
            }
        }
        if (bl2) {
            if (constraintWidget.mResolvedMatchConstraintDefault[1] == 4) {
                measure.verticalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
            } else if (n3 == 0) {
                if (measure.horizontalBehavior == ConstraintWidget.DimensionBehaviour.FIXED) {
                    n = measure.horizontalDimension;
                } else {
                    measure.verticalBehavior = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                    measurer.measure(constraintWidget, measure);
                    n = measure.measuredWidth;
                }
                measure.verticalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
                measure.verticalDimension = constraintWidget.mDimensionRatioSide != 0 && constraintWidget.mDimensionRatioSide != -1 ? (int)((float)n * constraintWidget.getDimensionRatio()) : (int)((float)n / constraintWidget.getDimensionRatio());
            }
        }
        measurer.measure(constraintWidget, measure);
        constraintWidget.setWidth(measure.measuredWidth);
        constraintWidget.setHeight(measure.measuredHeight);
        constraintWidget.setHasBaseline(measure.measuredHasBaseline);
        constraintWidget.setBaselineDistance(measure.measuredBaseline);
        measure.measureStrategy = BasicMeasure.Measure.SELF_DIMENSIONS;
        return measure.measuredNeedsSolverPass;
    }

    private void resetChains() {
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
    }

    void addChain(ConstraintWidget constraintWidget, int n) {
        if (n == 0) {
            this.addHorizontalChain(constraintWidget);
        } else if (n == 1) {
            this.addVerticalChain(constraintWidget);
        }
    }

    public boolean addChildrenToSolver(LinearSystem linearSystem) {
        Object object;
        int n;
        boolean bl = this.optimizeFor(64);
        this.addToSolver(linearSystem, bl);
        int n2 = this.mChildren.size();
        boolean bl2 = false;
        for (n = 0; n < n2; ++n) {
            object = (ConstraintWidget)this.mChildren.get(n);
            ((ConstraintWidget)object).setInBarrier(0, false);
            ((ConstraintWidget)object).setInBarrier(1, false);
            if (!(object instanceof Barrier)) continue;
            bl2 = true;
        }
        if (bl2) {
            for (n = 0; n < n2; ++n) {
                object = (ConstraintWidget)this.mChildren.get(n);
                if (!(object instanceof Barrier)) continue;
                ((Barrier)object).markWidgets();
            }
        }
        for (n = 0; n < n2; ++n) {
            object = (ConstraintWidget)this.mChildren.get(n);
            if (!((ConstraintWidget)object).addFirst()) continue;
            ((ConstraintWidget)object).addToSolver(linearSystem, bl);
        }
        if (LinearSystem.USE_DEPENDENCY_ORDERING) {
            Object object2;
            object = new HashSet();
            for (n = 0; n < n2; ++n) {
                object2 = (ConstraintWidget)this.mChildren.get(n);
                if (((ConstraintWidget)object2).addFirst()) continue;
                ((HashSet)object).add(object2);
            }
            n = this.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT ? 0 : 1;
            this.addChildrenToSolverByDependency(this, linearSystem, (HashSet<ConstraintWidget>)object, n, false);
            object2 = ((HashSet)object).iterator();
            while (object2.hasNext()) {
                object = (ConstraintWidget)object2.next();
                Optimizer.checkMatchParent(this, linearSystem, (ConstraintWidget)object);
                ((ConstraintWidget)object).addToSolver(linearSystem, bl);
            }
        } else {
            for (n = 0; n < n2; ++n) {
                object = (ConstraintWidget)this.mChildren.get(n);
                if (object instanceof ConstraintWidgetContainer) {
                    ConstraintWidget.DimensionBehaviour dimensionBehaviour = ((ConstraintWidget)object).mListDimensionBehaviors[0];
                    ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = ((ConstraintWidget)object).mListDimensionBehaviors[1];
                    if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                        ((ConstraintWidget)object).setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                    }
                    if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                        ((ConstraintWidget)object).setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                    }
                    ((ConstraintWidget)object).addToSolver(linearSystem, bl);
                    if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                        ((ConstraintWidget)object).setHorizontalDimensionBehaviour(dimensionBehaviour);
                    }
                    if (dimensionBehaviour2 != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) continue;
                    ((ConstraintWidget)object).setVerticalDimensionBehaviour(dimensionBehaviour2);
                    continue;
                }
                Optimizer.checkMatchParent(this, linearSystem, (ConstraintWidget)object);
                if (((ConstraintWidget)object).addFirst()) continue;
                ((ConstraintWidget)object).addToSolver(linearSystem, bl);
            }
        }
        if (this.mHorizontalChainsSize > 0) {
            Chain.applyChainConstraints(this, linearSystem, null, 0);
        }
        if (this.mVerticalChainsSize > 0) {
            Chain.applyChainConstraints(this, linearSystem, null, 1);
        }
        return true;
    }

    public void addHorizontalWrapMaxVariable(ConstraintAnchor constraintAnchor) {
        WeakReference<ConstraintAnchor> weakReference = this.horizontalWrapMax;
        if (weakReference == null || weakReference.get() == null || constraintAnchor.getFinalValue() > ((ConstraintAnchor)this.horizontalWrapMax.get()).getFinalValue()) {
            this.horizontalWrapMax = new WeakReference<ConstraintAnchor>(constraintAnchor);
        }
    }

    public void addHorizontalWrapMinVariable(ConstraintAnchor constraintAnchor) {
        WeakReference<ConstraintAnchor> weakReference = this.horizontalWrapMin;
        if (weakReference == null || weakReference.get() == null || constraintAnchor.getFinalValue() > ((ConstraintAnchor)this.horizontalWrapMin.get()).getFinalValue()) {
            this.horizontalWrapMin = new WeakReference<ConstraintAnchor>(constraintAnchor);
        }
    }

    void addVerticalWrapMaxVariable(ConstraintAnchor constraintAnchor) {
        WeakReference<ConstraintAnchor> weakReference = this.verticalWrapMax;
        if (weakReference == null || weakReference.get() == null || constraintAnchor.getFinalValue() > ((ConstraintAnchor)this.verticalWrapMax.get()).getFinalValue()) {
            this.verticalWrapMax = new WeakReference<ConstraintAnchor>(constraintAnchor);
        }
    }

    void addVerticalWrapMinVariable(ConstraintAnchor constraintAnchor) {
        WeakReference<ConstraintAnchor> weakReference = this.verticalWrapMin;
        if (weakReference == null || weakReference.get() == null || constraintAnchor.getFinalValue() > ((ConstraintAnchor)this.verticalWrapMin.get()).getFinalValue()) {
            this.verticalWrapMin = new WeakReference<ConstraintAnchor>(constraintAnchor);
        }
    }

    public void defineTerminalWidgets() {
        this.mDependencyGraph.defineTerminalWidgets(this.getHorizontalDimensionBehaviour(), this.getVerticalDimensionBehaviour());
    }

    public boolean directMeasure(boolean bl) {
        return this.mDependencyGraph.directMeasure(bl);
    }

    public boolean directMeasureSetup(boolean bl) {
        return this.mDependencyGraph.directMeasureSetup(bl);
    }

    public boolean directMeasureWithOrientation(boolean bl, int n) {
        return this.mDependencyGraph.directMeasureWithOrientation(bl, n);
    }

    public void fillMetrics(Metrics metrics) {
        this.mMetrics = metrics;
        this.mSystem.fillMetrics(metrics);
    }

    public ArrayList<Guideline> getHorizontalGuidelines() {
        ArrayList<Guideline> arrayList = new ArrayList<Guideline>();
        int n = this.mChildren.size();
        for (int i = 0; i < n; ++i) {
            ConstraintWidget constraintWidget = (ConstraintWidget)this.mChildren.get(i);
            if (!(constraintWidget instanceof Guideline) || ((Guideline)(constraintWidget = (Guideline)constraintWidget)).getOrientation() != 0) continue;
            arrayList.add((Guideline)constraintWidget);
        }
        return arrayList;
    }

    public BasicMeasure.Measurer getMeasurer() {
        return this.mMeasurer;
    }

    public int getOptimizationLevel() {
        return this.mOptimizationLevel;
    }

    public LinearSystem getSystem() {
        return this.mSystem;
    }

    @Override
    public String getType() {
        return "ConstraintLayout";
    }

    public ArrayList<Guideline> getVerticalGuidelines() {
        ArrayList<Guideline> arrayList = new ArrayList<Guideline>();
        int n = this.mChildren.size();
        for (int i = 0; i < n; ++i) {
            ConstraintWidget constraintWidget = (ConstraintWidget)this.mChildren.get(i);
            if (!(constraintWidget instanceof Guideline) || ((Guideline)(constraintWidget = (Guideline)constraintWidget)).getOrientation() != 1) continue;
            arrayList.add((Guideline)constraintWidget);
        }
        return arrayList;
    }

    public boolean handlesInternalConstraints() {
        return false;
    }

    public void invalidateGraph() {
        this.mDependencyGraph.invalidateGraph();
    }

    public void invalidateMeasures() {
        this.mDependencyGraph.invalidateMeasures();
    }

    public boolean isHeightMeasuredTooSmall() {
        return this.mHeightMeasuredTooSmall;
    }

    public boolean isRtl() {
        return this.mIsRtl;
    }

    public boolean isWidthMeasuredTooSmall() {
        return this.mWidthMeasuredTooSmall;
    }

    @Override
    public void layout() {
        int n;
        int n2;
        Object object;
        Object object2;
        int n3;
        this.mX = 0;
        this.mY = 0;
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
        int n4 = this.mChildren.size();
        int n5 = Math.max(0, this.getWidth());
        int n6 = Math.max(0, this.getHeight());
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = this.mListDimensionBehaviors[1];
        ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = this.mListDimensionBehaviors[0];
        Object object3 = this.mMetrics;
        if (object3 != null) {
            ++((Metrics)object3).layouts;
        }
        if (Optimizer.enabled(this.mOptimizationLevel, 1)) {
            Direct.solvingPass(this, this.getMeasurer());
            for (n3 = 0; n3 < n4; ++n3) {
                object3 = (ConstraintWidget)this.mChildren.get(n3);
                if (!((ConstraintWidget)object3).isMeasureRequested() || object3 instanceof Guideline || object3 instanceof Barrier || object3 instanceof VirtualLayout || ((ConstraintWidget)object3).isInVirtualLayout()) continue;
                object2 = ((ConstraintWidget)object3).getDimensionBehaviour(0);
                object = ((ConstraintWidget)object3).getDimensionBehaviour(1);
                n2 = object2 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && ((ConstraintWidget)object3).mMatchConstraintDefaultWidth != 1 && object == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && ((ConstraintWidget)object3).mMatchConstraintDefaultHeight != 1 ? 1 : 0;
                if (n2 != 0) continue;
                object2 = new BasicMeasure.Measure();
                ConstraintWidgetContainer.measure((ConstraintWidget)object3, this.mMeasurer, (BasicMeasure.Measure)object2, BasicMeasure.Measure.SELF_DIMENSIONS);
            }
        }
        if (n4 > 2 && (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) && Optimizer.enabled(this.mOptimizationLevel, 1024) && Grouping.simpleSolvingPass(this, this.getMeasurer())) {
            n3 = n5;
            if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                if (n5 < this.getWidth() && n5 > 0) {
                    this.setWidth(n5);
                    this.mWidthMeasuredTooSmall = true;
                    n3 = n5;
                } else {
                    n3 = this.getWidth();
                }
            }
            n5 = n6;
            if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                if (n6 < this.getHeight() && n6 > 0) {
                    this.setHeight(n6);
                    this.mHeightMeasuredTooSmall = true;
                    n5 = n6;
                } else {
                    n5 = this.getHeight();
                }
            }
            n2 = 1;
            n6 = n5;
            n5 = n2;
            n2 = n3;
        } else {
            n3 = 0;
            n2 = n5;
            n5 = n3;
        }
        n3 = !this.optimizeFor(64) && !this.optimizeFor(128) ? 0 : 1;
        this.mSystem.graphOptimizer = false;
        this.mSystem.newgraphOptimizer = false;
        if (this.mOptimizationLevel != 0 && n3 != 0) {
            this.mSystem.newgraphOptimizer = true;
        }
        object3 = this.mChildren;
        boolean bl = this.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || this.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        this.resetChains();
        for (n = 0; n < n4; ++n) {
            object2 = (ConstraintWidget)this.mChildren.get(n);
            if (!(object2 instanceof WidgetContainer)) continue;
            ((WidgetContainer)object2).layout();
        }
        boolean bl2 = this.optimizeFor(64);
        int n7 = 0;
        boolean bl3 = true;
        n = n3;
        n3 = n7;
        while (bl3) {
            boolean bl4;
            int n8;
            block61: {
                block60: {
                    block59: {
                        block57: {
                            block56: {
                                block55: {
                                    block54: {
                                        n8 = n3 + 1;
                                        bl4 = bl3;
                                        this.mSystem.reset();
                                        bl4 = bl3;
                                        this.resetChains();
                                        bl4 = bl3;
                                        this.createObjectVariables(this.mSystem);
                                        for (n3 = 0; n3 < n4; ++n3) {
                                            bl4 = bl3;
                                            ((ConstraintWidget)this.mChildren.get(n3)).createObjectVariables(this.mSystem);
                                            continue;
                                        }
                                        bl4 = bl3;
                                        bl4 = bl3 = this.addChildrenToSolver(this.mSystem);
                                        object2 = this.verticalWrapMin;
                                        if (object2 == null) break block54;
                                        bl4 = bl3;
                                        if (((Reference)object2).get() == null) break block54;
                                        bl4 = bl3;
                                        this.addMinWrap((ConstraintAnchor)this.verticalWrapMin.get(), this.mSystem.createObjectVariable(this.mTop));
                                        bl4 = bl3;
                                        this.verticalWrapMin = null;
                                    }
                                    bl4 = bl3;
                                    object2 = this.verticalWrapMax;
                                    if (object2 == null) break block55;
                                    bl4 = bl3;
                                    if (((Reference)object2).get() == null) break block55;
                                    bl4 = bl3;
                                    this.addMaxWrap((ConstraintAnchor)this.verticalWrapMax.get(), this.mSystem.createObjectVariable(this.mBottom));
                                    bl4 = bl3;
                                    this.verticalWrapMax = null;
                                }
                                bl4 = bl3;
                                object2 = this.horizontalWrapMin;
                                if (object2 == null) break block56;
                                bl4 = bl3;
                                if (((Reference)object2).get() == null) break block56;
                                bl4 = bl3;
                                this.addMinWrap((ConstraintAnchor)this.horizontalWrapMin.get(), this.mSystem.createObjectVariable(this.mLeft));
                                bl4 = bl3;
                                this.horizontalWrapMin = null;
                            }
                            bl4 = bl3;
                            object2 = this.horizontalWrapMax;
                            if (object2 == null) break block57;
                            bl4 = bl3;
                            if (((Reference)object2).get() == null) break block57;
                            bl4 = bl3;
                            this.addMaxWrap((ConstraintAnchor)this.horizontalWrapMax.get(), this.mSystem.createObjectVariable(this.mRight));
                            bl4 = bl3;
                            try {
                                this.horizontalWrapMax = null;
                            }
                            catch (Exception exception) {
                                exception.printStackTrace();
                                PrintStream printStream = System.out;
                                object = new StringBuilder();
                                ((StringBuilder)object).append("EXCEPTION : ");
                                ((StringBuilder)object).append(exception);
                                printStream.println(((StringBuilder)object).toString());
                            }
                        }
                        if (bl3) {
                            bl4 = bl3;
                            this.mSystem.minimize();
                        }
                        bl4 = bl3;
                        if (bl4) {
                            this.updateChildrenFromSolver(this.mSystem, Optimizer.flags);
                        } else {
                            this.updateFromSolver(this.mSystem, bl2);
                            for (n3 = 0; n3 < n4; ++n3) {
                                ((ConstraintWidget)this.mChildren.get(n3)).updateFromSolver(this.mSystem, bl2);
                            }
                        }
                        bl4 = false;
                        if (!bl || n8 >= 8 || !Optimizer.flags[2]) break block59;
                        int n9 = 0;
                        n7 = 0;
                        for (n3 = 0; n3 < n4; ++n3) {
                            object2 = (ConstraintWidget)this.mChildren.get(n3);
                            n9 = Math.max(n9, ((ConstraintWidget)object2).mX + ((ConstraintWidget)object2).getWidth());
                            n7 = Math.max(n7, ((ConstraintWidget)object2).mY + ((ConstraintWidget)object2).getHeight());
                        }
                        bl3 = bl4;
                        n9 = Math.max(this.mMinWidth, n9);
                        n7 = Math.max(this.mMinHeight, n7);
                        n3 = n5;
                        bl4 = bl3;
                        if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                            n3 = n5;
                            bl4 = bl3;
                            if (this.getWidth() < n9) {
                                this.setWidth(n9);
                                this.mListDimensionBehaviors[0] = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                                n3 = 1;
                                bl4 = true;
                            }
                        }
                        n5 = n3;
                        bl3 = bl4;
                        if (dimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) break block60;
                        n5 = n3;
                        bl3 = bl4;
                        if (this.getHeight() >= n7) break block60;
                        this.setHeight(n7);
                        this.mListDimensionBehaviors[1] = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                        n5 = 1;
                        bl4 = true;
                        break block61;
                    }
                    bl3 = false;
                }
                bl4 = bl3;
            }
            n3 = Math.max(this.mMinWidth, this.getWidth());
            if (n3 > this.getWidth()) {
                this.setWidth(n3);
                this.mListDimensionBehaviors[0] = ConstraintWidget.DimensionBehaviour.FIXED;
                n5 = 1;
                bl4 = true;
            }
            if ((n3 = Math.max(this.mMinHeight, this.getHeight())) > this.getHeight()) {
                this.setHeight(n3);
                this.mListDimensionBehaviors[1] = ConstraintWidget.DimensionBehaviour.FIXED;
                n5 = 1;
                bl4 = true;
            }
            if (n5 == 0) {
                bl3 = bl4;
                n3 = n5;
                if (this.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    bl3 = bl4;
                    n3 = n5;
                    if (n2 > 0) {
                        bl3 = bl4;
                        n3 = n5;
                        if (this.getWidth() > n2) {
                            this.mWidthMeasuredTooSmall = true;
                            n3 = 1;
                            this.mListDimensionBehaviors[0] = ConstraintWidget.DimensionBehaviour.FIXED;
                            this.setWidth(n2);
                            bl3 = true;
                        }
                    }
                }
                if (this.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && n6 > 0 && this.getHeight() > n6) {
                    this.mHeightMeasuredTooSmall = true;
                    n5 = 1;
                    this.mListDimensionBehaviors[1] = ConstraintWidget.DimensionBehaviour.FIXED;
                    this.setHeight(n6);
                    bl4 = true;
                } else {
                    bl4 = bl3;
                    n5 = n3;
                }
            }
            n3 = n8;
            bl3 = bl4;
        }
        this.mChildren = (ArrayList)object3;
        if (n5 != 0) {
            this.mListDimensionBehaviors[0] = dimensionBehaviour2;
            this.mListDimensionBehaviors[1] = dimensionBehaviour;
        }
        this.resetSolverVariables(this.mSystem.getCache());
    }

    public long measure(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9) {
        this.mPaddingLeft = n8;
        this.mPaddingTop = n9;
        return this.mBasicMeasureSolver.solverMeasure(this, n, n8, n9, n2, n3, n4, n5, n6, n7);
    }

    public boolean optimizeFor(int n) {
        boolean bl = (this.mOptimizationLevel & n) == n;
        return bl;
    }

    @Override
    public void reset() {
        this.mSystem.reset();
        this.mPaddingLeft = 0;
        this.mPaddingRight = 0;
        this.mPaddingTop = 0;
        this.mPaddingBottom = 0;
        this.mSkipSolver = false;
        super.reset();
    }

    public void setMeasurer(BasicMeasure.Measurer measurer) {
        this.mMeasurer = measurer;
        this.mDependencyGraph.setMeasurer(measurer);
    }

    public void setOptimizationLevel(int n) {
        this.mOptimizationLevel = n;
        LinearSystem.USE_DEPENDENCY_ORDERING = this.optimizeFor(512);
    }

    public void setPadding(int n, int n2, int n3, int n4) {
        this.mPaddingLeft = n;
        this.mPaddingTop = n2;
        this.mPaddingRight = n3;
        this.mPaddingBottom = n4;
    }

    public void setRtl(boolean bl) {
        this.mIsRtl = bl;
    }

    public void updateChildrenFromSolver(LinearSystem linearSystem, boolean[] blArray) {
        blArray[2] = false;
        boolean bl = this.optimizeFor(64);
        this.updateFromSolver(linearSystem, bl);
        int n = this.mChildren.size();
        for (int i = 0; i < n; ++i) {
            ((ConstraintWidget)this.mChildren.get(i)).updateFromSolver(linearSystem, bl);
        }
    }

    @Override
    public void updateFromRuns(boolean bl, boolean bl2) {
        super.updateFromRuns(bl, bl2);
        int n = this.mChildren.size();
        for (int i = 0; i < n; ++i) {
            ((ConstraintWidget)this.mChildren.get(i)).updateFromRuns(bl, bl2);
        }
    }

    public void updateHierarchy() {
        this.mBasicMeasureSolver.updateHierarchy(this);
    }
}

