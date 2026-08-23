/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.solver.widgets.Helper;
import androidx.constraintlayout.solver.widgets.analyzer.Grouping;
import androidx.constraintlayout.solver.widgets.analyzer.WidgetGroup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class HelperWidget
extends ConstraintWidget
implements Helper {
    public ConstraintWidget[] mWidgets = new ConstraintWidget[4];
    public int mWidgetsCount = 0;

    @Override
    public void add(ConstraintWidget constraintWidget) {
        if (constraintWidget != this && constraintWidget != null) {
            int n = this.mWidgetsCount;
            ConstraintWidget[] constraintWidgetArray = this.mWidgets;
            if (n + 1 > constraintWidgetArray.length) {
                this.mWidgets = Arrays.copyOf(constraintWidgetArray, constraintWidgetArray.length * 2);
            }
            constraintWidgetArray = this.mWidgets;
            n = this.mWidgetsCount;
            constraintWidgetArray[n] = constraintWidget;
            this.mWidgetsCount = n + 1;
            return;
        }
    }

    public void addDependents(ArrayList<WidgetGroup> arrayList, int n, WidgetGroup widgetGroup) {
        int n2;
        for (n2 = 0; n2 < this.mWidgetsCount; ++n2) {
            widgetGroup.add(this.mWidgets[n2]);
        }
        for (n2 = 0; n2 < this.mWidgetsCount; ++n2) {
            Grouping.findDependents(this.mWidgets[n2], n, arrayList, widgetGroup);
        }
    }

    @Override
    public void copy(ConstraintWidget constraintWidget, HashMap<ConstraintWidget, ConstraintWidget> hashMap) {
        super.copy(constraintWidget, hashMap);
        constraintWidget = (HelperWidget)constraintWidget;
        this.mWidgetsCount = 0;
        int n = ((HelperWidget)constraintWidget).mWidgetsCount;
        for (int i = 0; i < n; ++i) {
            this.add(hashMap.get(((HelperWidget)constraintWidget).mWidgets[i]));
        }
    }

    public int findGroupInDependents(int n) {
        for (int i = 0; i < this.mWidgetsCount; ++i) {
            ConstraintWidget constraintWidget = this.mWidgets[i];
            if (n == 0 && constraintWidget.horizontalGroup != -1) {
                return constraintWidget.horizontalGroup;
            }
            if (n != 1 || constraintWidget.verticalGroup == -1) continue;
            return constraintWidget.verticalGroup;
        }
        return -1;
    }

    @Override
    public void removeAllIds() {
        this.mWidgetsCount = 0;
        Arrays.fill(this.mWidgets, null);
    }

    @Override
    public void updateConstraints(ConstraintWidgetContainer constraintWidgetContainer) {
    }
}

