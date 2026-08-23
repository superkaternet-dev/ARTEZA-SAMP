/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver;

import androidx.constraintlayout.solver.ArrayRow;
import androidx.constraintlayout.solver.Cache;
import androidx.constraintlayout.solver.SolverVariable;

public class GoalRow
extends ArrayRow {
    public GoalRow(Cache cache) {
        super(cache);
    }

    @Override
    public void addError(SolverVariable solverVariable) {
        super.addError(solverVariable);
        --solverVariable.usageInRowCount;
    }
}

