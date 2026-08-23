/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.analyzer.Dependency;
import androidx.constraintlayout.solver.widgets.analyzer.DependencyNode;
import androidx.constraintlayout.solver.widgets.analyzer.DimensionDependency;
import androidx.constraintlayout.solver.widgets.analyzer.RunGroup;

public abstract class WidgetRun
implements Dependency {
    DimensionDependency dimension = new DimensionDependency(this);
    protected ConstraintWidget.DimensionBehaviour dimensionBehavior;
    public DependencyNode end;
    protected RunType mRunType;
    public int matchConstraintsType;
    public int orientation = 0;
    boolean resolved = false;
    RunGroup runGroup;
    public DependencyNode start = new DependencyNode(this);
    ConstraintWidget widget;

    public WidgetRun(ConstraintWidget constraintWidget) {
        this.end = new DependencyNode(this);
        this.mRunType = RunType.NONE;
        this.widget = constraintWidget;
    }

    private void resolveDimension(int n, int n2) {
        switch (this.matchConstraintsType) {
            default: {
                break;
            }
            case 3: {
                if (this.widget.horizontalRun.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.widget.horizontalRun.matchConstraintsType == 3 && this.widget.verticalRun.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.widget.verticalRun.matchConstraintsType == 3) break;
                Object object = this.widget;
                object = n == 0 ? ((ConstraintWidget)object).verticalRun : ((ConstraintWidget)object).horizontalRun;
                if (!((WidgetRun)object).dimension.resolved) break;
                float f = this.widget.getDimensionRatio();
                n = n == 1 ? (int)((float)((WidgetRun)object).dimension.value / f + 0.5f) : (int)((float)((WidgetRun)object).dimension.value * f + 0.5f);
                this.dimension.resolve(n);
                break;
            }
            case 2: {
                Object object = this.widget.getParent();
                if (object == null) break;
                object = n == 0 ? ((ConstraintWidget)object).horizontalRun : ((ConstraintWidget)object).verticalRun;
                if (!((WidgetRun)object).dimension.resolved) break;
                ConstraintWidget constraintWidget = this.widget;
                float f = n == 0 ? constraintWidget.mMatchConstraintPercentWidth : constraintWidget.mMatchConstraintPercentHeight;
                n2 = (int)((float)((WidgetRun)object).dimension.value * f + 0.5f);
                this.dimension.resolve(this.getLimitedDimension(n2, n));
                break;
            }
            case 1: {
                n = this.getLimitedDimension(this.dimension.wrapValue, n);
                this.dimension.resolve(Math.min(n, n2));
                break;
            }
            case 0: {
                this.dimension.resolve(this.getLimitedDimension(n2, n));
            }
        }
    }

    protected final void addTarget(DependencyNode dependencyNode, DependencyNode dependencyNode2, int n) {
        dependencyNode.targets.add(dependencyNode2);
        dependencyNode.margin = n;
        dependencyNode2.dependencies.add(dependencyNode);
    }

    protected final void addTarget(DependencyNode dependencyNode, DependencyNode dependencyNode2, int n, DimensionDependency dimensionDependency) {
        dependencyNode.targets.add(dependencyNode2);
        dependencyNode.targets.add(this.dimension);
        dependencyNode.marginFactor = n;
        dependencyNode.marginDependency = dimensionDependency;
        dependencyNode2.dependencies.add(dependencyNode);
        dimensionDependency.dependencies.add(dependencyNode);
    }

    abstract void apply();

    abstract void applyToWidget();

    abstract void clear();

    protected final int getLimitedDimension(int n, int n2) {
        int n3;
        if (n2 == 0) {
            n3 = this.widget.mMatchConstraintMaxWidth;
            n2 = Math.max(this.widget.mMatchConstraintMinWidth, n);
            if (n3 > 0) {
                n2 = Math.min(n3, n);
            }
            n3 = n;
            if (n2 != n) {
                n3 = n2;
            }
        } else {
            n3 = this.widget.mMatchConstraintMaxHeight;
            n2 = Math.max(this.widget.mMatchConstraintMinHeight, n);
            if (n3 > 0) {
                n2 = Math.min(n3, n);
            }
            n3 = n;
            if (n2 != n) {
                n3 = n2;
            }
        }
        return n3;
    }

    protected final DependencyNode getTarget(ConstraintAnchor object) {
        if (((ConstraintAnchor)object).mTarget == null) {
            return null;
        }
        Object var2_2 = null;
        ConstraintWidget constraintWidget = ((ConstraintAnchor)object).mTarget.mOwner;
        object = ((ConstraintAnchor)object).mTarget.mType;
        switch (1.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[((Enum)object).ordinal()]) {
            default: {
                object = var2_2;
                break;
            }
            case 5: {
                object = constraintWidget.verticalRun.end;
                break;
            }
            case 4: {
                object = constraintWidget.verticalRun.baseline;
                break;
            }
            case 3: {
                object = constraintWidget.verticalRun.start;
                break;
            }
            case 2: {
                object = constraintWidget.horizontalRun.end;
                break;
            }
            case 1: {
                object = constraintWidget.horizontalRun.start;
            }
        }
        return object;
    }

    protected final DependencyNode getTarget(ConstraintAnchor object, int n) {
        if (((ConstraintAnchor)object).mTarget == null) {
            return null;
        }
        Object var4_3 = null;
        Object object2 = ((ConstraintAnchor)object).mTarget.mOwner;
        object2 = n == 0 ? ((ConstraintWidget)object2).horizontalRun : ((ConstraintWidget)object2).verticalRun;
        object = ((ConstraintAnchor)object).mTarget.mType;
        switch (1.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[((Enum)object).ordinal()]) {
            default: {
                object = var4_3;
                break;
            }
            case 2: 
            case 5: {
                object = ((WidgetRun)object2).end;
                break;
            }
            case 1: 
            case 3: {
                object = ((WidgetRun)object2).start;
            }
        }
        return object;
    }

    public long getWrapDimension() {
        if (this.dimension.resolved) {
            return this.dimension.value;
        }
        return 0L;
    }

    public boolean isCenterConnection() {
        int n;
        int n2;
        int n3 = 0;
        int n4 = this.start.targets.size();
        for (n2 = 0; n2 < n4; ++n2) {
            n = n3;
            if (this.start.targets.get((int)n2).run != this) {
                n = n3 + 1;
            }
            n3 = n;
        }
        n4 = this.end.targets.size();
        for (n2 = 0; n2 < n4; ++n2) {
            n = n3;
            if (this.end.targets.get((int)n2).run != this) {
                n = n3 + 1;
            }
            n3 = n;
        }
        boolean bl = n3 >= 2;
        return bl;
    }

    public boolean isDimensionResolved() {
        return this.dimension.resolved;
    }

    public boolean isResolved() {
        return this.resolved;
    }

    abstract void reset();

    abstract boolean supportsWrapComputation();

    @Override
    public void update(Dependency dependency) {
    }

    protected void updateRunCenter(Dependency dependency, ConstraintAnchor object, ConstraintAnchor constraintAnchor, int n) {
        dependency = this.getTarget((ConstraintAnchor)object);
        DependencyNode dependencyNode = this.getTarget(constraintAnchor);
        if (((DependencyNode)dependency).resolved && dependencyNode.resolved) {
            int n2 = ((DependencyNode)dependency).value + ((ConstraintAnchor)object).getMargin();
            int n3 = dependencyNode.value - constraintAnchor.getMargin();
            int n4 = n3 - n2;
            if (!this.dimension.resolved && this.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                this.resolveDimension(n, n4);
            }
            if (!this.dimension.resolved) {
                return;
            }
            if (this.dimension.value == n4) {
                this.start.resolve(n2);
                this.end.resolve(n3);
                return;
            }
            object = this.widget;
            float f = n == 0 ? ((ConstraintWidget)object).getHorizontalBiasPercent() : ((ConstraintWidget)object).getVerticalBiasPercent();
            n = n3;
            if (dependency == dependencyNode) {
                n2 = ((DependencyNode)dependency).value;
                n = dependencyNode.value;
                f = 0.5f;
            }
            n3 = this.dimension.value;
            this.start.resolve((int)((float)n2 + 0.5f + (float)(n - n2 - n3) * f));
            this.end.resolve(this.start.value + this.dimension.value);
            return;
        }
    }

    protected void updateRunEnd(Dependency dependency) {
    }

    protected void updateRunStart(Dependency dependency) {
    }

    public long wrapSize(int n) {
        if (this.dimension.resolved) {
            long l = this.dimension.value;
            l = this.isCenterConnection() ? (l += (long)(this.start.margin - this.end.margin)) : (n == 0 ? (l += (long)this.start.margin) : (l -= (long)this.end.margin));
            return l;
        }
        return 0L;
    }

    static final class RunType
    extends Enum<RunType> {
        private static final RunType[] $VALUES;
        public static final /* enum */ RunType CENTER;
        public static final /* enum */ RunType END;
        public static final /* enum */ RunType NONE;
        public static final /* enum */ RunType START;

        static {
            RunType runType;
            RunType runType2;
            RunType runType3;
            RunType runType4;
            NONE = runType4 = new RunType();
            START = runType3 = new RunType();
            END = runType2 = new RunType();
            CENTER = runType = new RunType();
            $VALUES = new RunType[]{runType4, runType3, runType2, runType};
        }

        public static RunType valueOf(String string2) {
            return Enum.valueOf(RunType.class, string2);
        }

        public static RunType[] values() {
            return (RunType[])$VALUES.clone();
        }
    }
}

