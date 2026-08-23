/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.state.helpers;

import androidx.constraintlayout.solver.state.ConstraintReference;
import androidx.constraintlayout.solver.state.State;
import androidx.constraintlayout.solver.state.helpers.ChainReference;

public class VerticalChainReference
extends ChainReference {
    private Object mBottomToBottom;
    private Object mBottomToTop;
    private Object mTopToBottom;
    private Object mTopToTop;

    public VerticalChainReference(State state) {
        super(state, State.Helper.VERTICAL_CHAIN);
    }

    @Override
    public void apply() {
        Object object = null;
        Object object2 = null;
        for (Object object3 : this.mReferences) {
            this.mState.constraints(object3).clearVertical();
        }
        for (Object object3 : this.mReferences) {
            object3 = this.mState.constraints(object3);
            Object object4 = object;
            if (object == null) {
                object = object3;
                object4 = this.mTopToTop;
                if (object4 != null) {
                    ((ConstraintReference)object).topToTop(object4);
                    object4 = object;
                } else {
                    object4 = this.mTopToBottom;
                    if (object4 != null) {
                        ((ConstraintReference)object).topToBottom(object4);
                        object4 = object;
                    } else {
                        ((ConstraintReference)object).topToTop(State.PARENT);
                        object4 = object;
                    }
                }
            }
            if (object2 != null) {
                ((ConstraintReference)object2).bottomToTop(((ConstraintReference)object3).getKey());
                ((ConstraintReference)object3).topToBottom(((ConstraintReference)object2).getKey());
            }
            object2 = object3;
            object = object4;
        }
        if (object2 != null) {
            Object object3;
            object3 = this.mBottomToTop;
            if (object3 != null) {
                ((ConstraintReference)object2).bottomToTop(object3);
            } else {
                object3 = this.mBottomToBottom;
                if (object3 != null) {
                    ((ConstraintReference)object2).bottomToBottom(object3);
                } else {
                    ((ConstraintReference)object2).bottomToBottom(State.PARENT);
                }
            }
        }
        if (object != null && this.mBias != 0.5f) {
            ((ConstraintReference)object).verticalBias(this.mBias);
        }
        switch (1.$SwitchMap$androidx$constraintlayout$solver$state$State$Chain[this.mStyle.ordinal()]) {
            default: {
                break;
            }
            case 3: {
                ((ConstraintReference)object).setVerticalChainStyle(2);
                break;
            }
            case 2: {
                ((ConstraintReference)object).setVerticalChainStyle(1);
                break;
            }
            case 1: {
                ((ConstraintReference)object).setVerticalChainStyle(0);
            }
        }
    }

    public void bottomToBottom(Object object) {
        this.mBottomToBottom = object;
    }

    public void bottomToTop(Object object) {
        this.mBottomToTop = object;
    }

    public void topToBottom(Object object) {
        this.mTopToBottom = object;
    }

    public void topToTop(Object object) {
        this.mTopToTop = object;
    }
}

