/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.state.helpers;

import androidx.constraintlayout.solver.state.ConstraintReference;
import androidx.constraintlayout.solver.state.HelperReference;
import androidx.constraintlayout.solver.state.State;

public class AlignHorizontallyReference
extends HelperReference {
    private float mBias = 0.5f;
    private Object mEndToEnd;
    private Object mEndToStart;
    private Object mStartToEnd;
    private Object mStartToStart;

    public AlignHorizontallyReference(State state) {
        super(state, State.Helper.ALIGN_VERTICALLY);
    }

    @Override
    public void apply() {
        for (Object object : this.mReferences) {
            float f;
            object = this.mState.constraints(object);
            ((ConstraintReference)object).clearHorizontal();
            Object object2 = this.mStartToStart;
            if (object2 != null) {
                ((ConstraintReference)object).startToStart(object2);
            } else {
                object2 = this.mStartToEnd;
                if (object2 != null) {
                    ((ConstraintReference)object).startToEnd(object2);
                } else {
                    ((ConstraintReference)object).startToStart(State.PARENT);
                }
            }
            object2 = this.mEndToStart;
            if (object2 != null) {
                ((ConstraintReference)object).endToStart(object2);
            } else {
                object2 = this.mEndToEnd;
                if (object2 != null) {
                    ((ConstraintReference)object).endToEnd(object2);
                } else {
                    ((ConstraintReference)object).endToEnd(State.PARENT);
                }
            }
            if ((f = this.mBias) == 0.5f) continue;
            ((ConstraintReference)object).horizontalBias(f);
        }
    }

    public void bias(float f) {
        this.mBias = f;
    }

    public void endToEnd(Object object) {
        this.mEndToEnd = object;
    }

    public void endToStart(Object object) {
        this.mEndToStart = object;
    }

    public void startToEnd(Object object) {
        this.mStartToEnd = object;
    }

    public void startToStart(Object object) {
        this.mStartToStart = object;
    }
}

