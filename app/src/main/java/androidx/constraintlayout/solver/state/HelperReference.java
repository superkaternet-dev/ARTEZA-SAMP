/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.state;

import androidx.constraintlayout.solver.state.State;
import androidx.constraintlayout.solver.widgets.HelperWidget;
import java.util.ArrayList;

public class HelperReference {
    private HelperWidget mHelperWidget;
    protected ArrayList<Object> mReferences = new ArrayList();
    protected final State mState;
    final State.Helper mType;

    public HelperReference(State state, State.Helper helper) {
        this.mState = state;
        this.mType = helper;
    }

    public HelperReference add(Object ... objectArray) {
        for (Object object : objectArray) {
            this.mReferences.add(object);
        }
        return this;
    }

    public void apply() {
    }

    public HelperWidget getHelperWidget() {
        return this.mHelperWidget;
    }

    public State.Helper getType() {
        return this.mType;
    }

    public void setHelperWidget(HelperWidget helperWidget) {
        this.mHelperWidget = helperWidget;
    }
}

