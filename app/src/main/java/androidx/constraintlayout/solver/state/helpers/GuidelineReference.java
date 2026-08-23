/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.state.helpers;

import androidx.constraintlayout.solver.state.Reference;
import androidx.constraintlayout.solver.state.State;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.Guideline;

public class GuidelineReference
implements Reference {
    private Object key;
    private int mEnd = -1;
    private Guideline mGuidelineWidget;
    private int mOrientation;
    private float mPercent = 0.0f;
    private int mStart = -1;
    final State mState;

    public GuidelineReference(State state) {
        this.mState = state;
    }

    @Override
    public void apply() {
        this.mGuidelineWidget.setOrientation(this.mOrientation);
        int n = this.mStart;
        if (n != -1) {
            this.mGuidelineWidget.setGuideBegin(n);
        } else {
            n = this.mEnd;
            if (n != -1) {
                this.mGuidelineWidget.setGuideEnd(n);
            } else {
                this.mGuidelineWidget.setGuidePercent(this.mPercent);
            }
        }
    }

    public void end(Object object) {
        this.mStart = -1;
        this.mEnd = this.mState.convertDimension(object);
        this.mPercent = 0.0f;
    }

    @Override
    public ConstraintWidget getConstraintWidget() {
        if (this.mGuidelineWidget == null) {
            this.mGuidelineWidget = new Guideline();
        }
        return this.mGuidelineWidget;
    }

    @Override
    public Object getKey() {
        return this.key;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public void percent(float f) {
        this.mStart = -1;
        this.mEnd = -1;
        this.mPercent = f;
    }

    @Override
    public void setConstraintWidget(ConstraintWidget constraintWidget) {
        this.mGuidelineWidget = constraintWidget instanceof Guideline ? (Guideline)constraintWidget : null;
    }

    @Override
    public void setKey(Object object) {
        this.key = object;
    }

    public void setOrientation(int n) {
        this.mOrientation = n;
    }

    public void start(Object object) {
        this.mStart = this.mState.convertDimension(object);
        this.mEnd = -1;
        this.mPercent = 0.0f;
    }
}

