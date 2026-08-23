/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.Barrier;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.analyzer.Dependency;
import androidx.constraintlayout.solver.widgets.analyzer.DependencyNode;
import androidx.constraintlayout.solver.widgets.analyzer.WidgetRun;

class HelperReferences
extends WidgetRun {
    public HelperReferences(ConstraintWidget constraintWidget) {
        super(constraintWidget);
    }

    private void addDependency(DependencyNode dependencyNode) {
        this.start.dependencies.add(dependencyNode);
        dependencyNode.targets.add(this.start);
    }

    @Override
    void apply() {
        if (this.widget instanceof Barrier) {
            this.start.delegateToWidgetRun = true;
            Barrier barrier = (Barrier)this.widget;
            int n = barrier.getBarrierType();
            boolean bl = barrier.allowsGoneWidget();
            switch (n) {
                default: {
                    break;
                }
                case 3: {
                    this.start.type = DependencyNode.Type.BOTTOM;
                    for (n = 0; n < barrier.mWidgetsCount; ++n) {
                        Object object = barrier.mWidgets[n];
                        if (!bl && ((ConstraintWidget)object).getVisibility() == 8) continue;
                        object = ((ConstraintWidget)object).verticalRun.end;
                        ((DependencyNode)object).dependencies.add(this.start);
                        this.start.targets.add((DependencyNode)object);
                    }
                    this.addDependency(this.widget.verticalRun.start);
                    this.addDependency(this.widget.verticalRun.end);
                    break;
                }
                case 2: {
                    this.start.type = DependencyNode.Type.TOP;
                    for (n = 0; n < barrier.mWidgetsCount; ++n) {
                        Object object = barrier.mWidgets[n];
                        if (!bl && ((ConstraintWidget)object).getVisibility() == 8) continue;
                        object = ((ConstraintWidget)object).verticalRun.start;
                        ((DependencyNode)object).dependencies.add(this.start);
                        this.start.targets.add((DependencyNode)object);
                    }
                    this.addDependency(this.widget.verticalRun.start);
                    this.addDependency(this.widget.verticalRun.end);
                    break;
                }
                case 1: {
                    this.start.type = DependencyNode.Type.RIGHT;
                    for (n = 0; n < barrier.mWidgetsCount; ++n) {
                        Object object = barrier.mWidgets[n];
                        if (!bl && ((ConstraintWidget)object).getVisibility() == 8) continue;
                        object = ((ConstraintWidget)object).horizontalRun.end;
                        ((DependencyNode)object).dependencies.add(this.start);
                        this.start.targets.add((DependencyNode)object);
                    }
                    this.addDependency(this.widget.horizontalRun.start);
                    this.addDependency(this.widget.horizontalRun.end);
                    break;
                }
                case 0: {
                    this.start.type = DependencyNode.Type.LEFT;
                    for (n = 0; n < barrier.mWidgetsCount; ++n) {
                        Object object = barrier.mWidgets[n];
                        if (!bl && ((ConstraintWidget)object).getVisibility() == 8) continue;
                        object = ((ConstraintWidget)object).horizontalRun.start;
                        ((DependencyNode)object).dependencies.add(this.start);
                        this.start.targets.add((DependencyNode)object);
                    }
                    this.addDependency(this.widget.horizontalRun.start);
                    this.addDependency(this.widget.horizontalRun.end);
                }
            }
        }
    }

    @Override
    public void applyToWidget() {
        if (this.widget instanceof Barrier) {
            int n = ((Barrier)this.widget).getBarrierType();
            if (n != 0 && n != 1) {
                this.widget.setY(this.start.value);
            } else {
                this.widget.setX(this.start.value);
            }
        }
    }

    @Override
    void clear() {
        this.runGroup = null;
        this.start.clear();
    }

    @Override
    void reset() {
        this.start.resolved = false;
    }

    @Override
    boolean supportsWrapComputation() {
        return false;
    }

    @Override
    public void update(Dependency object) {
        Barrier barrier = (Barrier)this.widget;
        int n = barrier.getBarrierType();
        int n2 = -1;
        int n3 = 0;
        object = this.start.targets.iterator();
        while (object.hasNext()) {
            int n4;
            int n5;
            block8: {
                block7: {
                    n5 = ((DependencyNode)object.next()).value;
                    if (n2 == -1) break block7;
                    n4 = n2;
                    if (n5 >= n2) break block8;
                }
                n4 = n5;
            }
            int n6 = n3;
            if (n3 < n5) {
                n6 = n5;
            }
            n2 = n4;
            n3 = n6;
        }
        if (n != 0 && n != 2) {
            this.start.resolve(barrier.getMargin() + n3);
        } else {
            this.start.resolve(barrier.getMargin() + n2);
        }
    }
}

