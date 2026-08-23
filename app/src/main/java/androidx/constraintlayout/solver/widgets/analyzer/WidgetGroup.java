/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.widgets.Chain;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class WidgetGroup {
    private static final boolean DEBUG = false;
    static int count = 0;
    boolean authoritative = false;
    int id = -1;
    private int moveTo = -1;
    int orientation = 0;
    ArrayList<MeasureResult> results = null;
    ArrayList<ConstraintWidget> widgets = new ArrayList();

    public WidgetGroup(int n) {
        int n2 = count;
        count = n2 + 1;
        this.id = n2;
        this.orientation = n;
    }

    private boolean contains(ConstraintWidget constraintWidget) {
        return this.widgets.contains(constraintWidget);
    }

    private String getOrientationString() {
        int n = this.orientation;
        if (n == 0) {
            return "Horizontal";
        }
        if (n == 1) {
            return "Vertical";
        }
        if (n == 2) {
            return "Both";
        }
        return "Unknown";
    }

    private int measureWrap(int n, ConstraintWidget constraintWidget) {
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = constraintWidget.getDimensionBehaviour(n);
        if (dimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && dimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_PARENT && dimensionBehaviour != ConstraintWidget.DimensionBehaviour.FIXED) {
            return -1;
        }
        n = n == 0 ? constraintWidget.getWidth() : constraintWidget.getHeight();
        return n;
    }

    private int solverMeasure(LinearSystem linearSystem, ArrayList<ConstraintWidget> arrayList, int n) {
        int n2;
        ConstraintWidgetContainer constraintWidgetContainer = (ConstraintWidgetContainer)arrayList.get(0).getParent();
        linearSystem.reset();
        constraintWidgetContainer.addToSolver(linearSystem, false);
        for (n2 = 0; n2 < arrayList.size(); ++n2) {
            arrayList.get(n2).addToSolver(linearSystem, false);
        }
        if (n == 0 && constraintWidgetContainer.mHorizontalChainsSize > 0) {
            Chain.applyChainConstraints(constraintWidgetContainer, linearSystem, arrayList, 0);
        }
        if (n == 1 && constraintWidgetContainer.mVerticalChainsSize > 0) {
            Chain.applyChainConstraints(constraintWidgetContainer, linearSystem, arrayList, 1);
        }
        try {
            linearSystem.minimize();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        this.results = new ArrayList();
        for (n2 = 0; n2 < arrayList.size(); ++n2) {
            MeasureResult measureResult = new MeasureResult(this, arrayList.get(n2), linearSystem, n);
            this.results.add(measureResult);
        }
        if (n == 0) {
            n2 = linearSystem.getObjectVariableValue(constraintWidgetContainer.mLeft);
            n = linearSystem.getObjectVariableValue(constraintWidgetContainer.mRight);
            linearSystem.reset();
            return n - n2;
        }
        n = linearSystem.getObjectVariableValue(constraintWidgetContainer.mTop);
        n2 = linearSystem.getObjectVariableValue(constraintWidgetContainer.mBottom);
        linearSystem.reset();
        return n2 - n;
    }

    public boolean add(ConstraintWidget constraintWidget) {
        if (this.widgets.contains(constraintWidget)) {
            return false;
        }
        this.widgets.add(constraintWidget);
        return true;
    }

    public void apply() {
        if (this.results == null) {
            return;
        }
        if (!this.authoritative) {
            return;
        }
        for (int i = 0; i < this.results.size(); ++i) {
            this.results.get(i).apply();
        }
    }

    public void cleanup(ArrayList<WidgetGroup> arrayList) {
        int n = this.widgets.size();
        if (this.moveTo != -1 && n > 0) {
            for (int i = 0; i < arrayList.size(); ++i) {
                WidgetGroup widgetGroup = arrayList.get(i);
                if (this.moveTo != widgetGroup.id) continue;
                this.moveTo(this.orientation, widgetGroup);
            }
        }
        if (n == 0) {
            arrayList.remove(this);
            return;
        }
    }

    public void clear() {
        this.widgets.clear();
    }

    public int getId() {
        return this.id;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public boolean intersectWith(WidgetGroup widgetGroup) {
        for (int i = 0; i < this.widgets.size(); ++i) {
            if (!widgetGroup.contains(this.widgets.get(i))) continue;
            return true;
        }
        return false;
    }

    public boolean isAuthoritative() {
        return this.authoritative;
    }

    public int measureWrap(LinearSystem linearSystem, int n) {
        if (this.widgets.size() == 0) {
            return 0;
        }
        return this.solverMeasure(linearSystem, this.widgets, n);
    }

    public void moveTo(int n, WidgetGroup widgetGroup) {
        for (ConstraintWidget constraintWidget : this.widgets) {
            widgetGroup.add(constraintWidget);
            if (n == 0) {
                constraintWidget.horizontalGroup = widgetGroup.getId();
                continue;
            }
            constraintWidget.verticalGroup = widgetGroup.getId();
        }
        this.moveTo = widgetGroup.id;
    }

    public void setAuthoritative(boolean bl) {
        this.authoritative = bl;
    }

    public void setOrientation(int n) {
        this.orientation = n;
    }

    public int size() {
        return this.widgets.size();
    }

    public String toString() {
        CharSequence charSequence = new StringBuilder();
        charSequence.append(this.getOrientationString());
        charSequence.append(" [");
        charSequence.append(this.id);
        charSequence.append("] <");
        charSequence = charSequence.toString();
        for (ConstraintWidget constraintWidget : this.widgets) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((String)charSequence);
            stringBuilder.append(" ");
            stringBuilder.append(constraintWidget.getDebugName());
            charSequence = stringBuilder.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append((String)charSequence);
        stringBuilder.append(" >");
        return stringBuilder.toString();
    }

    class MeasureResult {
        int baseline;
        int bottom;
        int left;
        int orientation;
        int right;
        final WidgetGroup this$0;
        int top;
        WeakReference<ConstraintWidget> widgetRef;

        public MeasureResult(WidgetGroup widgetGroup, ConstraintWidget constraintWidget, LinearSystem linearSystem, int n) {
            this.this$0 = widgetGroup;
            this.widgetRef = new WeakReference<ConstraintWidget>(constraintWidget);
            this.left = linearSystem.getObjectVariableValue(constraintWidget.mLeft);
            this.top = linearSystem.getObjectVariableValue(constraintWidget.mTop);
            this.right = linearSystem.getObjectVariableValue(constraintWidget.mRight);
            this.bottom = linearSystem.getObjectVariableValue(constraintWidget.mBottom);
            this.baseline = linearSystem.getObjectVariableValue(constraintWidget.mBaseline);
            this.orientation = n;
        }

        public void apply() {
            ConstraintWidget constraintWidget = (ConstraintWidget)this.widgetRef.get();
            if (constraintWidget != null) {
                constraintWidget.setFinalFrame(this.left, this.top, this.right, this.bottom, this.baseline, this.orientation);
            }
        }
    }
}

