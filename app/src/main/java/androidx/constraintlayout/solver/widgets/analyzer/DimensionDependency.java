/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.analyzer.Dependency;
import androidx.constraintlayout.solver.widgets.analyzer.DependencyNode;
import androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun;
import androidx.constraintlayout.solver.widgets.analyzer.WidgetRun;

class DimensionDependency
extends DependencyNode {
    public int wrapValue;

    public DimensionDependency(WidgetRun widgetRun) {
        super(widgetRun);
        this.type = widgetRun instanceof HorizontalWidgetRun ? DependencyNode.Type.HORIZONTAL_DIMENSION : DependencyNode.Type.VERTICAL_DIMENSION;
    }

    @Override
    public void resolve(int n) {
        if (this.resolved) {
            return;
        }
        this.resolved = true;
        this.value = n;
        for (Dependency dependency : this.dependencies) {
            dependency.update(dependency);
        }
    }
}

