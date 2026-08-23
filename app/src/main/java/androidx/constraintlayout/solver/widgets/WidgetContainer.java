/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.Cache;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import java.util.ArrayList;

public class WidgetContainer
extends ConstraintWidget {
    public ArrayList<ConstraintWidget> mChildren = new ArrayList();

    public WidgetContainer() {
    }

    public WidgetContainer(int n, int n2) {
        super(n, n2);
    }

    public WidgetContainer(int n, int n2, int n3, int n4) {
        super(n, n2, n3, n4);
    }

    public void add(ConstraintWidget constraintWidget) {
        this.mChildren.add(constraintWidget);
        if (constraintWidget.getParent() != null) {
            ((WidgetContainer)constraintWidget.getParent()).remove(constraintWidget);
        }
        constraintWidget.setParent(this);
    }

    public void add(ConstraintWidget ... constraintWidgetArray) {
        int n = constraintWidgetArray.length;
        for (int i = 0; i < n; ++i) {
            this.add(constraintWidgetArray[i]);
        }
    }

    public ArrayList<ConstraintWidget> getChildren() {
        return this.mChildren;
    }

    public ConstraintWidgetContainer getRootConstraintContainer() {
        ConstraintWidget constraintWidget;
        ConstraintWidget constraintWidget2 = this.getParent();
        ConstraintWidgetContainer constraintWidgetContainer = null;
        ConstraintWidget constraintWidget3 = constraintWidget2;
        if (this instanceof ConstraintWidgetContainer) {
            constraintWidgetContainer = (ConstraintWidgetContainer)this;
            constraintWidget3 = constraintWidget2;
        }
        while ((constraintWidget = constraintWidget3) != null) {
            constraintWidget3 = constraintWidget2 = constraintWidget.getParent();
            if (!(constraintWidget instanceof ConstraintWidgetContainer)) continue;
            constraintWidgetContainer = (ConstraintWidgetContainer)constraintWidget;
            constraintWidget3 = constraintWidget2;
        }
        return constraintWidgetContainer;
    }

    public void layout() {
        Object object = this.mChildren;
        if (object == null) {
            return;
        }
        int n = ((ArrayList)object).size();
        for (int i = 0; i < n; ++i) {
            object = this.mChildren.get(i);
            if (!(object instanceof WidgetContainer)) continue;
            ((WidgetContainer)object).layout();
        }
    }

    public void remove(ConstraintWidget constraintWidget) {
        this.mChildren.remove(constraintWidget);
        constraintWidget.reset();
    }

    public void removeAllChildren() {
        this.mChildren.clear();
    }

    @Override
    public void reset() {
        this.mChildren.clear();
        super.reset();
    }

    @Override
    public void resetSolverVariables(Cache cache) {
        super.resetSolverVariables(cache);
        int n = this.mChildren.size();
        for (int i = 0; i < n; ++i) {
            this.mChildren.get(i).resetSolverVariables(cache);
        }
    }

    @Override
    public void setOffset(int n, int n2) {
        super.setOffset(n, n2);
        n2 = this.mChildren.size();
        for (n = 0; n < n2; ++n) {
            this.mChildren.get(n).setOffset(this.getRootX(), this.getRootY());
        }
    }
}

