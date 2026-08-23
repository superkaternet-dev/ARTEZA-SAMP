/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.state.helpers;

import androidx.constraintlayout.solver.state.HelperReference;
import androidx.constraintlayout.solver.state.State;
import androidx.constraintlayout.solver.widgets.Barrier;
import androidx.constraintlayout.solver.widgets.HelperWidget;

public class BarrierReference
extends HelperReference {
    private Barrier mBarrierWidget;
    private State.Direction mDirection;
    private int mMargin;

    public BarrierReference(State state) {
        super(state, State.Helper.BARRIER);
    }

    @Override
    public void apply() {
        this.getHelperWidget();
        int n = 0;
        switch (1.$SwitchMap$androidx$constraintlayout$solver$state$State$Direction[this.mDirection.ordinal()]) {
            default: {
                break;
            }
            case 6: {
                n = 3;
                break;
            }
            case 5: {
                n = 2;
                break;
            }
            case 3: 
            case 4: {
                n = 1;
                break;
            }
            case 1: 
            case 2: {
                n = 0;
            }
        }
        this.mBarrierWidget.setBarrierType(n);
        this.mBarrierWidget.setMargin(this.mMargin);
    }

    @Override
    public HelperWidget getHelperWidget() {
        if (this.mBarrierWidget == null) {
            this.mBarrierWidget = new Barrier();
        }
        return this.mBarrierWidget;
    }

    public void margin(int n) {
        this.mMargin = n;
    }

    public void margin(Object object) {
        this.margin(this.mState.convertDimension(object));
    }

    public void setBarrierDirection(State.Direction direction) {
        this.mDirection = direction;
    }
}

