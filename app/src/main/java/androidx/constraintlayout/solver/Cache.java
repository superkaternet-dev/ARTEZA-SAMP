/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver;

import androidx.constraintlayout.solver.ArrayRow;
import androidx.constraintlayout.solver.Pools;
import androidx.constraintlayout.solver.SolverVariable;

public class Cache {
    Pools.Pool<ArrayRow> arrayRowPool;
    SolverVariable[] mIndexedVariables;
    Pools.Pool<ArrayRow> optimizedArrayRowPool = new Pools.SimplePool<ArrayRow>(256);
    Pools.Pool<SolverVariable> solverVariablePool;

    public Cache() {
        this.arrayRowPool = new Pools.SimplePool<ArrayRow>(256);
        this.solverVariablePool = new Pools.SimplePool<SolverVariable>(256);
        this.mIndexedVariables = new SolverVariable[32];
    }
}

