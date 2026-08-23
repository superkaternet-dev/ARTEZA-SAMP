/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.state.helpers;

import androidx.constraintlayout.solver.state.ConstraintReference;
import androidx.constraintlayout.solver.state.State;
import androidx.constraintlayout.solver.state.helpers.ChainReference;

public class HorizontalChainReference
extends ChainReference {
    private Object mEndToEnd;
    private Object mEndToStart;
    private Object mStartToEnd;
    private Object mStartToStart;

    public HorizontalChainReference(State state) {
        super(state, State.Helper.HORIZONTAL_CHAIN);
    }

    @Override
    public void apply() {
        Object object = null;
        Object object2 = null;
        for (Object object3 : this.mReferences) {
            this.mState.constraints(object3).clearHorizontal();
        }
        for (Object object4 : this.mReferences) {
            Object object3;
            object4 = this.mState.constraints(object4);
            object3 = object;
            if (object == null) {
                object3 = object4;
                object = this.mStartToStart;
                if (object != null) {
                    ((ConstraintReference)object3).startToStart(object);
                } else {
                    object = this.mStartToEnd;
                    if (object != null) {
                        ((ConstraintReference)object3).startToEnd(object);
                    } else {
                        ((ConstraintReference)object3).startToStart(State.PARENT);
                    }
                }
            }
            if (object2 != null) {
                ((ConstraintReference)object2).endToStart(((ConstraintReference)object4).getKey());
                ((ConstraintReference)object4).startToEnd(((ConstraintReference)object2).getKey());
            }
            object2 = object4;
            object = object3;
        }
        if (object2 != null) {
            Object object4;
            object4 = this.mEndToStart;
            if (object4 != null) {
                ((ConstraintReference)object2).endToStart(object4);
            } else {
                object4 = this.mEndToEnd;
                if (object4 != null) {
                    ((ConstraintReference)object2).endToEnd(object4);
                } else {
                    ((ConstraintReference)object2).endToEnd(State.PARENT);
                }
            }
        }
        if (object != null && this.mBias != 0.5f) {
            ((ConstraintReference)object).horizontalBias(this.mBias);
        }
        switch (1.$SwitchMap$androidx$constraintlayout$solver$state$State$Chain[this.mStyle.ordinal()]) {
            default: {
                break;
            }
            case 3: {
                ((ConstraintReference)object).setHorizontalChainStyle(2);
                break;
            }
            case 2: {
                ((ConstraintReference)object).setHorizontalChainStyle(1);
                break;
            }
            case 1: {
                ((ConstraintReference)object).setHorizontalChainStyle(0);
            }
        }
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

