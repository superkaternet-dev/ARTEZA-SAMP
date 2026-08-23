/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver;

import androidx.constraintlayout.solver.ArrayRow;
import androidx.constraintlayout.solver.Cache;
import androidx.constraintlayout.solver.Metrics;
import androidx.constraintlayout.solver.PriorityGoalRow;
import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.SolverVariableValues;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;

public class LinearSystem {
    public static long ARRAY_ROW_CREATION;
    public static final boolean DEBUG = false;
    private static final boolean DEBUG_CONSTRAINTS = false;
    public static final boolean FULL_DEBUG = false;
    public static final boolean MEASURE = false;
    public static long OPTIMIZED_ARRAY_ROW_CREATION;
    public static boolean OPTIMIZED_ENGINE;
    private static int POOL_SIZE;
    public static boolean SIMPLIFY_SYNONYMS;
    public static boolean SKIP_COLUMNS;
    public static boolean USE_BASIC_SYNONYMS;
    public static boolean USE_DEPENDENCY_ORDERING;
    public static boolean USE_SYNONYMS;
    public static Metrics sMetrics;
    private int TABLE_SIZE = 32;
    public boolean graphOptimizer = false;
    public boolean hasSimpleDefinition = false;
    private boolean[] mAlreadyTestedCandidates = new boolean[32];
    final Cache mCache;
    private Row mGoal;
    private int mMaxColumns = 32;
    private int mMaxRows = 32;
    int mNumColumns = 1;
    int mNumRows = 0;
    private SolverVariable[] mPoolVariables = new SolverVariable[POOL_SIZE];
    private int mPoolVariablesCount = 0;
    ArrayRow[] mRows = new ArrayRow[32];
    private Row mTempGoal;
    private HashMap<String, SolverVariable> mVariables = null;
    int mVariablesID = 0;
    public boolean newgraphOptimizer = false;

    static {
        USE_DEPENDENCY_ORDERING = false;
        USE_BASIC_SYNONYMS = true;
        SIMPLIFY_SYNONYMS = true;
        USE_SYNONYMS = true;
        SKIP_COLUMNS = true;
        OPTIMIZED_ENGINE = false;
        POOL_SIZE = 1000;
        ARRAY_ROW_CREATION = 0L;
        OPTIMIZED_ARRAY_ROW_CREATION = 0L;
    }

    public LinearSystem() {
        Cache cache;
        this.releaseRows();
        this.mCache = cache = new Cache();
        this.mGoal = new PriorityGoalRow(cache);
        this.mTempGoal = OPTIMIZED_ENGINE ? new ValuesRow(this, cache) : new ArrayRow(cache);
    }

    private SolverVariable acquireSolverVariable(SolverVariable.Type object, String solverVariableArray) {
        SolverVariable solverVariable = this.mCache.solverVariablePool.acquire();
        if (solverVariable == null) {
            solverVariable = new SolverVariable((SolverVariable.Type)((Object)object), (String)solverVariableArray);
            solverVariable.setType((SolverVariable.Type)((Object)object), (String)solverVariableArray);
            object = solverVariable;
        } else {
            solverVariable.reset();
            solverVariable.setType((SolverVariable.Type)((Object)object), (String)solverVariableArray);
            object = solverVariable;
        }
        int n = this.mPoolVariablesCount;
        int n2 = POOL_SIZE;
        if (n >= n2) {
            POOL_SIZE = n2 *= 2;
            this.mPoolVariables = Arrays.copyOf(this.mPoolVariables, n2);
        }
        solverVariableArray = this.mPoolVariables;
        n2 = this.mPoolVariablesCount;
        this.mPoolVariablesCount = n2 + 1;
        solverVariableArray[n2] = object;
        return object;
    }

    private void addError(ArrayRow arrayRow) {
        arrayRow.addError(this, 0);
    }

    private final void addRow(ArrayRow object) {
        if (SIMPLIFY_SYNONYMS && object.isSimpleDefinition) {
            object.variable.setFinalValue(this, object.constantValue);
        } else {
            this.mRows[this.mNumRows] = object;
            object.variable.definitionId = this.mNumRows++;
            object.variable.updateReferencesWithNewDefinition(this, (ArrayRow)object);
        }
        if (SIMPLIFY_SYNONYMS && this.hasSimpleDefinition) {
            int n = 0;
            while (n < this.mNumRows) {
                if (this.mRows[n] == null) {
                    System.out.println("WTF");
                }
                object = this.mRows;
                int n2 = n;
                if (object[n] != null) {
                    n2 = n;
                    if (object[n].isSimpleDefinition) {
                        int n3;
                        object = this.mRows[n];
                        object.variable.setFinalValue(this, object.constantValue);
                        if (OPTIMIZED_ENGINE) {
                            this.mCache.optimizedArrayRowPool.release((ArrayRow)object);
                        } else {
                            this.mCache.arrayRowPool.release((ArrayRow)object);
                        }
                        this.mRows[n] = null;
                        int n4 = n + 1;
                        n2 = n + 1;
                        while (n2 < (n3 = this.mNumRows)) {
                            object = this.mRows;
                            object[n2 - 1] = object[n2];
                            if (object[n2 - 1].variable.definitionId == n2) {
                                this.mRows[n2 - 1].variable.definitionId = n2 - 1;
                            }
                            n4 = n2++;
                        }
                        if (n4 < n3) {
                            this.mRows[n4] = null;
                        }
                        this.mNumRows = n3 - 1;
                        n2 = n - 1;
                    }
                }
                n = n2 + 1;
            }
            this.hasSimpleDefinition = false;
        }
    }

    private void addSingleError(ArrayRow arrayRow, int n) {
        this.addSingleError(arrayRow, n, 0);
    }

    private void computeValues() {
        for (int i = 0; i < this.mNumRows; ++i) {
            ArrayRow arrayRow = this.mRows[i];
            arrayRow.variable.computedValue = arrayRow.constantValue;
        }
    }

    public static ArrayRow createRowDimensionPercent(LinearSystem linearSystem, SolverVariable solverVariable, SolverVariable solverVariable2, float f) {
        return linearSystem.createRow().createRowDimensionPercent(solverVariable, solverVariable2, f);
    }

    private SolverVariable createVariable(String string2, SolverVariable.Type object) {
        int n;
        Metrics metrics = sMetrics;
        if (metrics != null) {
            ++metrics.variables;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        object = this.acquireSolverVariable((SolverVariable.Type)((Object)object), null);
        ((SolverVariable)object).setName(string2);
        this.mVariablesID = n = this.mVariablesID + 1;
        ++this.mNumColumns;
        ((SolverVariable)object).id = n;
        if (this.mVariables == null) {
            this.mVariables = new HashMap();
        }
        this.mVariables.put(string2, (SolverVariable)object);
        this.mCache.mIndexedVariables[this.mVariablesID] = object;
        return object;
    }

    private void displayRows() {
        StringBuilder stringBuilder;
        this.displaySolverVariables();
        String string2 = "";
        for (int i = 0; i < this.mNumRows; ++i) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append(this.mRows[i]);
            string2 = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append("\n");
            string2 = stringBuilder.toString();
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append(this.mGoal);
        stringBuilder.append("\n");
        string2 = stringBuilder.toString();
        System.out.println(string2);
    }

    private void displaySolverVariables() {
        CharSequence charSequence = new StringBuilder();
        charSequence.append("Display Rows (");
        charSequence.append(this.mNumRows);
        charSequence.append("x");
        charSequence.append(this.mNumColumns);
        charSequence.append(")\n");
        charSequence = charSequence.toString();
        System.out.println((String)charSequence);
    }

    private int enforceBFS(Row object) throws Exception {
        int n;
        block33: {
            int n2;
            block20: {
                int n3;
                n2 = 0;
                int n4 = 0;
                n = 0;
                while (true) {
                    n3 = n4;
                    if (n >= this.mNumRows) break;
                    if (this.mRows[n].variable.mType != SolverVariable.Type.UNRESTRICTED && this.mRows[n].constantValue < 0.0f) {
                        n3 = 1;
                        break;
                    }
                    ++n;
                }
                if (n3 == 0) break block20;
                boolean bl = false;
                n = 0;
                while (!bl) {
                    Object object2;
                    object = sMetrics;
                    if (object != null) {
                        ++((Metrics)object).bfs;
                    }
                    int n5 = n + 1;
                    float f = Float.MAX_VALUE;
                    n2 = 0;
                    n4 = -1;
                    int n6 = -1;
                    for (n = 0; n < this.mNumRows; ++n) {
                        int n7;
                        int n8;
                        int n9;
                        float f2;
                        block19: {
                            block23: {
                                int n10;
                                int n11;
                                float f3;
                                block24: {
                                    block22: {
                                        block21: {
                                            object = this.mRows[n];
                                            if (((ArrayRow)object).variable.mType != SolverVariable.Type.UNRESTRICTED) break block21;
                                            f2 = f;
                                            n9 = n2;
                                            n8 = n4;
                                            n7 = n6;
                                            break block19;
                                        }
                                        if (!((ArrayRow)object).isSimpleDefinition) break block22;
                                        f2 = f;
                                        n9 = n2;
                                        n8 = n4;
                                        n7 = n6;
                                        break block19;
                                    }
                                    if (!(((ArrayRow)object).constantValue < 0.0f)) break block23;
                                    if (!SKIP_COLUMNS) break block24;
                                    int n12 = ((ArrayRow)object).variables.getCurrentSize();
                                    for (n8 = 0; n8 < n12; ++n8) {
                                        int n13;
                                        block26: {
                                            block25: {
                                                object2 = ((ArrayRow)object).variables.getVariable(n8);
                                                f3 = ((ArrayRow)object).variables.get((SolverVariable)object2);
                                                if (!(f3 <= 0.0f)) break block25;
                                                f2 = f;
                                                n13 = n2;
                                                n11 = n4;
                                                n7 = n6;
                                                n9 = n3;
                                                break block26;
                                            }
                                            n9 = 0;
                                            n10 = n4;
                                            n4 = n2;
                                            n2 = n9;
                                            while (true) {
                                                block28: {
                                                    block27: {
                                                        f2 = f;
                                                        n13 = n4;
                                                        n11 = n10;
                                                        n7 = n6;
                                                        n9 = n3;
                                                        if (n2 >= 9) break;
                                                        f2 = ((SolverVariable)object2).strengthVector[n2] / f3;
                                                        if (f2 < f && n2 == n4) break block27;
                                                        n9 = n4;
                                                        if (n2 <= n4) break block28;
                                                    }
                                                    f = f2;
                                                    n10 = n;
                                                    n6 = ((SolverVariable)object2).id;
                                                    n9 = n2;
                                                }
                                                ++n2;
                                                n4 = n9;
                                            }
                                        }
                                        n3 = n9;
                                        f = f2;
                                        n2 = n13;
                                        n4 = n11;
                                        n6 = n7;
                                    }
                                    f2 = f;
                                    n9 = n2;
                                    n8 = n4;
                                    n7 = n6;
                                    break block19;
                                }
                                n11 = n3;
                                n10 = 1;
                                while (true) {
                                    block30: {
                                        block29: {
                                            f2 = f;
                                            n9 = n2;
                                            n8 = n4;
                                            n7 = n6;
                                            n3 = n11;
                                            if (n10 >= this.mNumColumns) break block19;
                                            object2 = this.mCache.mIndexedVariables[n10];
                                            f3 = ((ArrayRow)object).variables.get((SolverVariable)object2);
                                            if (!(f3 <= 0.0f)) break block29;
                                            f2 = f;
                                            n9 = n2;
                                            n7 = n4;
                                            n8 = n6;
                                            break block30;
                                        }
                                        n3 = 0;
                                        while (true) {
                                            block32: {
                                                block31: {
                                                    f2 = f;
                                                    n9 = n2;
                                                    n7 = n4;
                                                    n8 = n6;
                                                    if (n3 >= 9) break;
                                                    f2 = ((SolverVariable)object2).strengthVector[n3] / f3;
                                                    if (f2 < f && n3 == n2) break block31;
                                                    n9 = n2;
                                                    if (n3 <= n2) break block32;
                                                }
                                                f = f2;
                                                n4 = n;
                                                n6 = n10;
                                                n9 = n3;
                                            }
                                            ++n3;
                                            n2 = n9;
                                        }
                                    }
                                    ++n10;
                                    f = f2;
                                    n2 = n9;
                                    n4 = n7;
                                    n6 = n8;
                                }
                            }
                            n7 = n6;
                            n8 = n4;
                            n9 = n2;
                            f2 = f;
                        }
                        f = f2;
                        n2 = n9;
                        n4 = n8;
                        n6 = n7;
                    }
                    if (n4 != -1) {
                        object2 = this.mRows[n4];
                        ((ArrayRow)object2).variable.definitionId = -1;
                        object = sMetrics;
                        if (object != null) {
                            ++((Metrics)object).pivots;
                        }
                        ((ArrayRow)object2).pivot(this.mCache.mIndexedVariables[n6]);
                        ((ArrayRow)object2).variable.definitionId = n4;
                        ((ArrayRow)object2).variable.updateReferencesWithNewDefinition(this, (ArrayRow)object2);
                    } else {
                        bl = true;
                    }
                    if (n5 > this.mNumColumns / 2) {
                        bl = true;
                    }
                    n = n5;
                }
                break block33;
            }
            n = n2;
        }
        return n;
    }

    private String getDisplaySize(int n) {
        int n2 = n * 4 / 1024 / 1024;
        if (n2 > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("");
            stringBuilder.append(n2);
            stringBuilder.append(" Mb");
            return stringBuilder.toString();
        }
        n2 = n * 4 / 1024;
        if (n2 > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("");
            stringBuilder.append(n2);
            stringBuilder.append(" Kb");
            return stringBuilder.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        stringBuilder.append(n * 4);
        stringBuilder.append(" bytes");
        return stringBuilder.toString();
    }

    private String getDisplayStrength(int n) {
        if (n == 1) {
            return "LOW";
        }
        if (n == 2) {
            return "MEDIUM";
        }
        if (n == 3) {
            return "HIGH";
        }
        if (n == 4) {
            return "HIGHEST";
        }
        if (n == 5) {
            return "EQUALITY";
        }
        if (n == 8) {
            return "FIXED";
        }
        if (n == 6) {
            return "BARRIER";
        }
        return "NONE";
    }

    public static Metrics getMetrics() {
        return sMetrics;
    }

    private void increaseTableSize() {
        int n;
        this.TABLE_SIZE = n = this.TABLE_SIZE * 2;
        this.mRows = Arrays.copyOf(this.mRows, n);
        Object object = this.mCache;
        ((Cache)object).mIndexedVariables = Arrays.copyOf(((Cache)object).mIndexedVariables, this.TABLE_SIZE);
        n = this.TABLE_SIZE;
        this.mAlreadyTestedCandidates = new boolean[n];
        this.mMaxColumns = n;
        this.mMaxRows = n;
        object = sMetrics;
        if (object != null) {
            ++((Metrics)object).tableSizeIncrease;
            object = sMetrics;
            ((Metrics)object).maxTableSize = Math.max(((Metrics)object).maxTableSize, (long)this.TABLE_SIZE);
            object = sMetrics;
            ((Metrics)object).lastTableSize = ((Metrics)object).maxTableSize;
        }
    }

    private final int optimize(Row row, boolean bl) {
        int n;
        int n2;
        Object object = sMetrics;
        if (object != null) {
            ++((Metrics)object).optimize;
        }
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        while (true) {
            n2 = n3;
            n = n4;
            if (n5 >= this.mNumColumns) break;
            this.mAlreadyTestedCandidates[n5] = false;
            ++n5;
        }
        while (n2 == 0) {
            object = sMetrics;
            if (object != null) {
                ++((Metrics)object).iterations;
            }
            if ((n4 = n + 1) >= this.mNumColumns * 2) {
                return n4;
            }
            if (row.getKey() != null) {
                this.mAlreadyTestedCandidates[row.getKey().id] = true;
            }
            if ((object = row.getPivotCandidate(this, this.mAlreadyTestedCandidates)) != null) {
                if (this.mAlreadyTestedCandidates[((SolverVariable)object).id]) {
                    return n4;
                }
                this.mAlreadyTestedCandidates[((SolverVariable)object).id] = true;
            }
            if (object != null) {
                ArrayRow arrayRow;
                float f = Float.MAX_VALUE;
                n3 = -1;
                for (n5 = 0; n5 < this.mNumRows; ++n5) {
                    float f2;
                    arrayRow = this.mRows[n5];
                    if (arrayRow.variable.mType == SolverVariable.Type.UNRESTRICTED) {
                        f2 = f;
                        n = n3;
                    } else if (arrayRow.isSimpleDefinition) {
                        f2 = f;
                        n = n3;
                    } else {
                        f2 = f;
                        n = n3;
                        if (arrayRow.hasVariable((SolverVariable)object)) {
                            float f3 = arrayRow.variables.get((SolverVariable)object);
                            f2 = f;
                            n = n3;
                            if (f3 < 0.0f) {
                                f3 = -arrayRow.constantValue / f3;
                                f2 = f;
                                n = n3;
                                if (f3 < f) {
                                    f2 = f3;
                                    n = n5;
                                }
                            }
                        }
                    }
                    f = f2;
                    n3 = n;
                }
                if (n3 > -1) {
                    arrayRow = this.mRows[n3];
                    arrayRow.variable.definitionId = -1;
                    Metrics metrics = sMetrics;
                    if (metrics != null) {
                        ++metrics.pivots;
                    }
                    arrayRow.pivot((SolverVariable)object);
                    arrayRow.variable.definitionId = n3;
                    arrayRow.variable.updateReferencesWithNewDefinition(this, arrayRow);
                }
            } else {
                n2 = 1;
            }
            n = n4;
        }
        return n;
    }

    private void releaseRows() {
        if (OPTIMIZED_ENGINE) {
            for (int i = 0; i < this.mNumRows; ++i) {
                ArrayRow arrayRow = this.mRows[i];
                if (arrayRow != null) {
                    this.mCache.optimizedArrayRowPool.release(arrayRow);
                }
                this.mRows[i] = null;
            }
        } else {
            for (int i = 0; i < this.mNumRows; ++i) {
                ArrayRow arrayRow = this.mRows[i];
                if (arrayRow != null) {
                    this.mCache.arrayRowPool.release(arrayRow);
                }
                this.mRows[i] = null;
            }
        }
    }

    public void addCenterPoint(ConstraintWidget object, ConstraintWidget object2, float f, int n) {
        SolverVariable solverVariable = this.createObjectVariable(((ConstraintWidget)object).getAnchor(ConstraintAnchor.Type.LEFT));
        SolverVariable solverVariable2 = this.createObjectVariable(((ConstraintWidget)object).getAnchor(ConstraintAnchor.Type.TOP));
        SolverVariable solverVariable3 = this.createObjectVariable(((ConstraintWidget)object).getAnchor(ConstraintAnchor.Type.RIGHT));
        SolverVariable solverVariable4 = this.createObjectVariable(((ConstraintWidget)object).getAnchor(ConstraintAnchor.Type.BOTTOM));
        SolverVariable solverVariable5 = this.createObjectVariable(((ConstraintWidget)object2).getAnchor(ConstraintAnchor.Type.LEFT));
        SolverVariable solverVariable6 = this.createObjectVariable(((ConstraintWidget)object2).getAnchor(ConstraintAnchor.Type.TOP));
        object = this.createObjectVariable(((ConstraintWidget)object2).getAnchor(ConstraintAnchor.Type.RIGHT));
        object2 = this.createObjectVariable(((ConstraintWidget)object2).getAnchor(ConstraintAnchor.Type.BOTTOM));
        ArrayRow arrayRow = this.createRow();
        double d = Math.sin(f);
        double d2 = n;
        Double.isNaN(d2);
        arrayRow.createRowWithAngle(solverVariable2, solverVariable4, solverVariable6, (SolverVariable)object2, (float)(d * d2));
        this.addConstraint(arrayRow);
        object2 = this.createRow();
        d = Math.cos(f);
        d2 = n;
        Double.isNaN(d2);
        ((ArrayRow)object2).createRowWithAngle(solverVariable, solverVariable3, solverVariable5, (SolverVariable)object, (float)(d * d2));
        this.addConstraint((ArrayRow)object2);
    }

    public void addCentering(SolverVariable solverVariable, SolverVariable solverVariable2, int n, float f, SolverVariable solverVariable3, SolverVariable solverVariable4, int n2, int n3) {
        ArrayRow arrayRow = this.createRow();
        arrayRow.createRowCentering(solverVariable, solverVariable2, n, f, solverVariable3, solverVariable4, n2);
        if (n3 != 8) {
            arrayRow.addError(this, n3);
        }
        this.addConstraint(arrayRow);
    }

    public void addConstraint(ArrayRow arrayRow) {
        if (arrayRow == null) {
            return;
        }
        Object object = sMetrics;
        if (object != null) {
            ++((Metrics)object).constraints;
            if (arrayRow.isSimpleDefinition) {
                object = sMetrics;
                ++((Metrics)object).simpleconstraints;
            }
        }
        if (this.mNumRows + 1 >= this.mMaxRows || this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        boolean bl = false;
        boolean bl2 = false;
        if (!arrayRow.isSimpleDefinition) {
            arrayRow.updateFromSystem(this);
            if (arrayRow.isEmpty()) {
                return;
            }
            arrayRow.ensurePositiveConstant();
            bl = bl2;
            if (arrayRow.chooseSubject(this)) {
                arrayRow.variable = object = this.createExtraVariable();
                int n = this.mNumRows;
                this.addRow(arrayRow);
                bl = bl2;
                if (this.mNumRows == n + 1) {
                    bl2 = true;
                    this.mTempGoal.initFromRow(arrayRow);
                    this.optimize(this.mTempGoal, true);
                    bl = bl2;
                    if (((SolverVariable)object).definitionId == -1) {
                        if (arrayRow.variable == object && (object = arrayRow.pickPivot((SolverVariable)object)) != null) {
                            Metrics metrics = sMetrics;
                            if (metrics != null) {
                                ++metrics.pivots;
                            }
                            arrayRow.pivot((SolverVariable)object);
                        }
                        if (!arrayRow.isSimpleDefinition) {
                            arrayRow.variable.updateReferencesWithNewDefinition(this, arrayRow);
                        }
                        if (OPTIMIZED_ENGINE) {
                            this.mCache.optimizedArrayRowPool.release(arrayRow);
                        } else {
                            this.mCache.arrayRowPool.release(arrayRow);
                        }
                        --this.mNumRows;
                        bl = bl2;
                    }
                }
            }
            if (!arrayRow.hasKeyVariable()) {
                return;
            }
        }
        if (!bl) {
            this.addRow(arrayRow);
        }
    }

    public ArrayRow addEquality(SolverVariable solverVariable, SolverVariable solverVariable2, int n, int n2) {
        if (USE_BASIC_SYNONYMS && n2 == 8 && solverVariable2.isFinalValue && solverVariable.definitionId == -1) {
            solverVariable.setFinalValue(this, solverVariable2.computedValue + (float)n);
            return null;
        }
        ArrayRow arrayRow = this.createRow();
        arrayRow.createRowEquals(solverVariable, solverVariable2, n);
        if (n2 != 8) {
            arrayRow.addError(this, n2);
        }
        this.addConstraint(arrayRow);
        return arrayRow;
    }

    public void addEquality(SolverVariable solverVariable, int n) {
        if (USE_BASIC_SYNONYMS && solverVariable.definitionId == -1) {
            solverVariable.setFinalValue(this, n);
            for (int i = 0; i < this.mVariablesID + 1; ++i) {
                SolverVariable solverVariable2 = this.mCache.mIndexedVariables[i];
                if (solverVariable2 == null || !solverVariable2.isSynonym || solverVariable2.synonym != solverVariable.id) continue;
                solverVariable2.setFinalValue(this, (float)n + solverVariable2.synonymDelta);
            }
            return;
        }
        int n2 = solverVariable.definitionId;
        if (solverVariable.definitionId != -1) {
            ArrayRow arrayRow = this.mRows[n2];
            if (arrayRow.isSimpleDefinition) {
                arrayRow.constantValue = n;
            } else if (arrayRow.variables.getCurrentSize() == 0) {
                arrayRow.isSimpleDefinition = true;
                arrayRow.constantValue = n;
            } else {
                arrayRow = this.createRow();
                arrayRow.createRowEquals(solverVariable, n);
                this.addConstraint(arrayRow);
            }
        } else {
            ArrayRow arrayRow = this.createRow();
            arrayRow.createRowDefinition(solverVariable, n);
            this.addConstraint(arrayRow);
        }
    }

    public void addGreaterBarrier(SolverVariable solverVariable, SolverVariable solverVariable2, int n, boolean bl) {
        ArrayRow arrayRow = this.createRow();
        SolverVariable solverVariable3 = this.createSlackVariable();
        solverVariable3.strength = 0;
        arrayRow.createRowGreaterThan(solverVariable, solverVariable2, solverVariable3, n);
        this.addConstraint(arrayRow);
    }

    public void addGreaterThan(SolverVariable solverVariable, SolverVariable solverVariable2, int n, int n2) {
        ArrayRow arrayRow = this.createRow();
        SolverVariable solverVariable3 = this.createSlackVariable();
        solverVariable3.strength = 0;
        arrayRow.createRowGreaterThan(solverVariable, solverVariable2, solverVariable3, n);
        if (n2 != 8) {
            this.addSingleError(arrayRow, (int)(-1.0f * arrayRow.variables.get(solverVariable3)), n2);
        }
        this.addConstraint(arrayRow);
    }

    public void addLowerBarrier(SolverVariable solverVariable, SolverVariable solverVariable2, int n, boolean bl) {
        ArrayRow arrayRow = this.createRow();
        SolverVariable solverVariable3 = this.createSlackVariable();
        solverVariable3.strength = 0;
        arrayRow.createRowLowerThan(solverVariable, solverVariable2, solverVariable3, n);
        this.addConstraint(arrayRow);
    }

    public void addLowerThan(SolverVariable solverVariable, SolverVariable solverVariable2, int n, int n2) {
        ArrayRow arrayRow = this.createRow();
        SolverVariable solverVariable3 = this.createSlackVariable();
        solverVariable3.strength = 0;
        arrayRow.createRowLowerThan(solverVariable, solverVariable2, solverVariable3, n);
        if (n2 != 8) {
            this.addSingleError(arrayRow, (int)(-1.0f * arrayRow.variables.get(solverVariable3)), n2);
        }
        this.addConstraint(arrayRow);
    }

    public void addRatio(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, SolverVariable solverVariable4, float f, int n) {
        ArrayRow arrayRow = this.createRow();
        arrayRow.createRowDimensionRatio(solverVariable, solverVariable2, solverVariable3, solverVariable4, f);
        if (n != 8) {
            arrayRow.addError(this, n);
        }
        this.addConstraint(arrayRow);
    }

    void addSingleError(ArrayRow arrayRow, int n, int n2) {
        arrayRow.addSingleError(this.createErrorVariable(n2, null), n);
    }

    public void addSynonym(SolverVariable solverVariable, SolverVariable solverVariable2, int n) {
        if (solverVariable.definitionId == -1 && n == 0) {
            SolverVariable solverVariable3 = solverVariable2;
            int n2 = n;
            if (solverVariable2.isSynonym) {
                n2 = (int)((float)n + solverVariable2.synonymDelta);
                solverVariable3 = this.mCache.mIndexedVariables[solverVariable2.synonym];
            }
            if (solverVariable.isSynonym) {
                n = (int)((float)n2 - solverVariable.synonymDelta);
                solverVariable = this.mCache.mIndexedVariables[solverVariable.synonym];
            } else {
                solverVariable.setSynonym(this, solverVariable3, 0.0f);
            }
        } else {
            this.addEquality(solverVariable, solverVariable2, n, 8);
        }
    }

    final void cleanupRows() {
        int n = 0;
        while (n < this.mNumRows) {
            ArrayRow arrayRow = this.mRows[n];
            if (arrayRow.variables.getCurrentSize() == 0) {
                arrayRow.isSimpleDefinition = true;
            }
            int n2 = n;
            if (arrayRow.isSimpleDefinition) {
                int n3;
                arrayRow.variable.computedValue = arrayRow.constantValue;
                arrayRow.variable.removeFromRow(arrayRow);
                for (n2 = n; n2 < (n3 = this.mNumRows) - 1; ++n2) {
                    ArrayRow[] arrayRowArray = this.mRows;
                    arrayRowArray[n2] = arrayRowArray[n2 + 1];
                }
                this.mRows[n3 - 1] = null;
                this.mNumRows = n3 - 1;
                n2 = n - 1;
                if (OPTIMIZED_ENGINE) {
                    this.mCache.optimizedArrayRowPool.release(arrayRow);
                } else {
                    this.mCache.arrayRowPool.release(arrayRow);
                }
            }
            n = n2 + 1;
        }
    }

    public SolverVariable createErrorVariable(int n, String object) {
        int n2;
        Metrics metrics = sMetrics;
        if (metrics != null) {
            ++metrics.errors;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        object = this.acquireSolverVariable(SolverVariable.Type.ERROR, (String)object);
        this.mVariablesID = n2 = this.mVariablesID + 1;
        ++this.mNumColumns;
        ((SolverVariable)object).id = n2;
        ((SolverVariable)object).strength = n;
        this.mCache.mIndexedVariables[this.mVariablesID] = object;
        this.mGoal.addError((SolverVariable)object);
        return object;
    }

    public SolverVariable createExtraVariable() {
        int n;
        Object object = sMetrics;
        if (object != null) {
            ++((Metrics)object).extravariables;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        object = this.acquireSolverVariable(SolverVariable.Type.SLACK, null);
        this.mVariablesID = n = this.mVariablesID + 1;
        ++this.mNumColumns;
        ((SolverVariable)object).id = n;
        this.mCache.mIndexedVariables[this.mVariablesID] = object;
        return object;
    }

    public SolverVariable createObjectVariable(Object object) {
        SolverVariable solverVariable;
        block7: {
            int n;
            SolverVariable solverVariable2;
            block8: {
                if (object == null) {
                    return null;
                }
                if (this.mNumColumns + 1 >= this.mMaxColumns) {
                    this.increaseTableSize();
                }
                solverVariable = null;
                if (!(object instanceof ConstraintAnchor)) break block7;
                solverVariable2 = solverVariable = ((ConstraintAnchor)object).getSolverVariable();
                if (solverVariable == null) {
                    ((ConstraintAnchor)object).resetSolverVariable(this.mCache);
                    solverVariable2 = ((ConstraintAnchor)object).getSolverVariable();
                }
                if (solverVariable2.id == -1 || solverVariable2.id > this.mVariablesID) break block8;
                solverVariable = solverVariable2;
                if (this.mCache.mIndexedVariables[solverVariable2.id] != null) break block7;
            }
            if (solverVariable2.id != -1) {
                solverVariable2.reset();
            }
            this.mVariablesID = n = this.mVariablesID + 1;
            ++this.mNumColumns;
            solverVariable2.id = n;
            solverVariable2.mType = SolverVariable.Type.UNRESTRICTED;
            this.mCache.mIndexedVariables[this.mVariablesID] = solverVariable2;
            solverVariable = solverVariable2;
        }
        return solverVariable;
    }

    public ArrayRow createRow() {
        ArrayRow arrayRow;
        if (OPTIMIZED_ENGINE) {
            arrayRow = this.mCache.optimizedArrayRowPool.acquire();
            if (arrayRow == null) {
                arrayRow = new ValuesRow(this, this.mCache);
                ++OPTIMIZED_ARRAY_ROW_CREATION;
            } else {
                arrayRow.reset();
            }
        } else {
            arrayRow = this.mCache.arrayRowPool.acquire();
            if (arrayRow == null) {
                arrayRow = new ArrayRow(this.mCache);
                ++ARRAY_ROW_CREATION;
            } else {
                arrayRow.reset();
            }
        }
        SolverVariable.increaseErrorId();
        return arrayRow;
    }

    public SolverVariable createSlackVariable() {
        int n;
        Object object = sMetrics;
        if (object != null) {
            ++((Metrics)object).slackvariables;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        object = this.acquireSolverVariable(SolverVariable.Type.SLACK, null);
        this.mVariablesID = n = this.mVariablesID + 1;
        ++this.mNumColumns;
        ((SolverVariable)object).id = n;
        this.mCache.mIndexedVariables[this.mVariablesID] = object;
        return object;
    }

    public void displayReadableRows() {
        CharSequence charSequence;
        SolverVariable solverVariable;
        int n;
        this.displaySolverVariables();
        CharSequence charSequence2 = new StringBuilder();
        ((StringBuilder)charSequence2).append(" num vars ");
        ((StringBuilder)charSequence2).append(this.mVariablesID);
        ((StringBuilder)charSequence2).append("\n");
        charSequence2 = ((StringBuilder)charSequence2).toString();
        for (n = 0; n < this.mVariablesID + 1; ++n) {
            solverVariable = this.mCache.mIndexedVariables[n];
            charSequence = charSequence2;
            if (solverVariable != null) {
                charSequence = charSequence2;
                if (solverVariable.isFinalValue) {
                    charSequence = new StringBuilder();
                    ((StringBuilder)charSequence).append((String)charSequence2);
                    ((StringBuilder)charSequence).append(" $[");
                    ((StringBuilder)charSequence).append(n);
                    ((StringBuilder)charSequence).append("] => ");
                    ((StringBuilder)charSequence).append(solverVariable);
                    ((StringBuilder)charSequence).append(" = ");
                    ((StringBuilder)charSequence).append(solverVariable.computedValue);
                    ((StringBuilder)charSequence).append("\n");
                    charSequence = ((StringBuilder)charSequence).toString();
                }
            }
            charSequence2 = charSequence;
        }
        charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append((String)charSequence2);
        ((StringBuilder)charSequence).append("\n");
        charSequence2 = ((StringBuilder)charSequence).toString();
        for (n = 0; n < this.mVariablesID + 1; ++n) {
            solverVariable = this.mCache.mIndexedVariables[n];
            charSequence = charSequence2;
            if (solverVariable != null) {
                charSequence = charSequence2;
                if (solverVariable.isSynonym) {
                    SolverVariable solverVariable2 = this.mCache.mIndexedVariables[solverVariable.synonym];
                    charSequence = new StringBuilder();
                    ((StringBuilder)charSequence).append((String)charSequence2);
                    ((StringBuilder)charSequence).append(" ~[");
                    ((StringBuilder)charSequence).append(n);
                    ((StringBuilder)charSequence).append("] => ");
                    ((StringBuilder)charSequence).append(solverVariable);
                    ((StringBuilder)charSequence).append(" = ");
                    ((StringBuilder)charSequence).append(solverVariable2);
                    ((StringBuilder)charSequence).append(" + ");
                    ((StringBuilder)charSequence).append(solverVariable.synonymDelta);
                    ((StringBuilder)charSequence).append("\n");
                    charSequence = ((StringBuilder)charSequence).toString();
                }
            }
            charSequence2 = charSequence;
        }
        charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append((String)charSequence2);
        ((StringBuilder)charSequence).append("\n\n #  ");
        charSequence2 = ((StringBuilder)charSequence).toString();
        for (n = 0; n < this.mNumRows; ++n) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append((String)charSequence2);
            ((StringBuilder)charSequence).append(this.mRows[n].toReadableString());
            charSequence = ((StringBuilder)charSequence).toString();
            charSequence2 = new StringBuilder();
            ((StringBuilder)charSequence2).append((String)charSequence);
            ((StringBuilder)charSequence2).append("\n #  ");
            charSequence2 = ((StringBuilder)charSequence2).toString();
        }
        charSequence = charSequence2;
        if (this.mGoal != null) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append((String)charSequence2);
            ((StringBuilder)charSequence).append("Goal: ");
            ((StringBuilder)charSequence).append(this.mGoal);
            ((StringBuilder)charSequence).append("\n");
            charSequence = ((StringBuilder)charSequence).toString();
        }
        System.out.println((String)charSequence);
    }

    void displaySystemInformations() {
        int n;
        Object object;
        int n2;
        int n3 = 0;
        for (n2 = 0; n2 < this.TABLE_SIZE; ++n2) {
            object = this.mRows;
            n = n3;
            if (object[n2] != null) {
                n = n3 + object[n2].sizeInBytes();
            }
            n3 = n;
        }
        n2 = 0;
        for (n = 0; n < this.mNumRows; ++n) {
            object = this.mRows;
            int n4 = n2;
            if (object[n] != null) {
                n4 = n2 + object[n].sizeInBytes();
            }
            n2 = n4;
        }
        object = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Linear System -> Table size: ");
        stringBuilder.append(this.TABLE_SIZE);
        stringBuilder.append(" (");
        n = this.TABLE_SIZE;
        stringBuilder.append(this.getDisplaySize(n * n));
        stringBuilder.append(") -- row sizes: ");
        stringBuilder.append(this.getDisplaySize(n3));
        stringBuilder.append(", actual size: ");
        stringBuilder.append(this.getDisplaySize(n2));
        stringBuilder.append(" rows: ");
        stringBuilder.append(this.mNumRows);
        stringBuilder.append("/");
        stringBuilder.append(this.mMaxRows);
        stringBuilder.append(" cols: ");
        stringBuilder.append(this.mNumColumns);
        stringBuilder.append("/");
        stringBuilder.append(this.mMaxColumns);
        stringBuilder.append(" ");
        stringBuilder.append(0);
        stringBuilder.append(" occupied cells, ");
        stringBuilder.append(this.getDisplaySize(0));
        ((PrintStream)object).println(stringBuilder.toString());
    }

    public void displayVariablesReadableRows() {
        CharSequence charSequence;
        this.displaySolverVariables();
        CharSequence charSequence2 = "";
        for (int i = 0; i < this.mNumRows; ++i) {
            charSequence = charSequence2;
            if (this.mRows[i].variable.mType == SolverVariable.Type.UNRESTRICTED) {
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append((String)charSequence2);
                ((StringBuilder)charSequence).append(this.mRows[i].toReadableString());
                charSequence = ((StringBuilder)charSequence).toString();
                charSequence2 = new StringBuilder();
                ((StringBuilder)charSequence2).append((String)charSequence);
                ((StringBuilder)charSequence2).append("\n");
                charSequence = ((StringBuilder)charSequence2).toString();
            }
            charSequence2 = charSequence;
        }
        charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append((String)charSequence2);
        ((StringBuilder)charSequence).append(this.mGoal);
        ((StringBuilder)charSequence).append("\n");
        charSequence2 = ((StringBuilder)charSequence).toString();
        System.out.println((String)charSequence2);
    }

    public void fillMetrics(Metrics metrics) {
        sMetrics = metrics;
    }

    public Cache getCache() {
        return this.mCache;
    }

    Row getGoal() {
        return this.mGoal;
    }

    public int getMemoryUsed() {
        int n = 0;
        for (int i = 0; i < this.mNumRows; ++i) {
            ArrayRow[] arrayRowArray = this.mRows;
            int n2 = n;
            if (arrayRowArray[i] != null) {
                n2 = n + arrayRowArray[i].sizeInBytes();
            }
            n = n2;
        }
        return n;
    }

    public int getNumEquations() {
        return this.mNumRows;
    }

    public int getNumVariables() {
        return this.mVariablesID;
    }

    public int getObjectVariableValue(Object object) {
        if ((object = ((ConstraintAnchor)object).getSolverVariable()) != null) {
            return (int)(((SolverVariable)object).computedValue + 0.5f);
        }
        return 0;
    }

    ArrayRow getRow(int n) {
        return this.mRows[n];
    }

    float getValueFor(String object) {
        if ((object = this.getVariable((String)object, SolverVariable.Type.UNRESTRICTED)) == null) {
            return 0.0f;
        }
        return ((SolverVariable)object).computedValue;
    }

    SolverVariable getVariable(String string2, SolverVariable.Type type) {
        SolverVariable solverVariable;
        if (this.mVariables == null) {
            this.mVariables = new HashMap();
        }
        SolverVariable solverVariable2 = solverVariable = this.mVariables.get(string2);
        if (solverVariable == null) {
            solverVariable2 = this.createVariable(string2, type);
        }
        return solverVariable2;
    }

    public void minimize() throws Exception {
        Metrics metrics = sMetrics;
        if (metrics != null) {
            ++metrics.minimize;
        }
        if (this.mGoal.isEmpty()) {
            this.computeValues();
            return;
        }
        if (!this.graphOptimizer && !this.newgraphOptimizer) {
            this.minimizeGoal(this.mGoal);
        } else {
            boolean bl;
            metrics = sMetrics;
            if (metrics != null) {
                ++metrics.graphOptimizer;
            }
            boolean bl2 = true;
            int n = 0;
            while (true) {
                bl = bl2;
                if (n >= this.mNumRows) break;
                if (!this.mRows[n].isSimpleDefinition) {
                    bl = false;
                    break;
                }
                ++n;
            }
            if (!bl) {
                this.minimizeGoal(this.mGoal);
            } else {
                metrics = sMetrics;
                if (metrics != null) {
                    ++metrics.fullySolved;
                }
                this.computeValues();
            }
        }
    }

    void minimizeGoal(Row row) throws Exception {
        Metrics metrics = sMetrics;
        if (metrics != null) {
            ++metrics.minimizeGoal;
            metrics = sMetrics;
            metrics.maxVariables = Math.max(metrics.maxVariables, (long)this.mNumColumns);
            metrics = sMetrics;
            metrics.maxRows = Math.max(metrics.maxRows, (long)this.mNumRows);
        }
        this.enforceBFS(row);
        this.optimize(row, false);
        this.computeValues();
    }

    public void removeRow(ArrayRow arrayRow) {
        if (arrayRow.isSimpleDefinition && arrayRow.variable != null) {
            if (arrayRow.variable.definitionId != -1) {
                int n;
                for (int i = arrayRow.variable.definitionId; i < (n = this.mNumRows) - 1; ++i) {
                    ArrayRow[] arrayRowArray = this.mRows[i + 1].variable;
                    if (arrayRowArray.definitionId == i + 1) {
                        arrayRowArray.definitionId = i;
                    }
                    arrayRowArray = this.mRows;
                    arrayRowArray[i] = arrayRowArray[i + 1];
                }
                this.mNumRows = n - 1;
            }
            if (!arrayRow.variable.isFinalValue) {
                arrayRow.variable.setFinalValue(this, arrayRow.constantValue);
            }
            if (OPTIMIZED_ENGINE) {
                this.mCache.optimizedArrayRowPool.release(arrayRow);
            } else {
                this.mCache.arrayRowPool.release(arrayRow);
            }
        }
    }

    public void reset() {
        Object object;
        int n;
        for (n = 0; n < this.mCache.mIndexedVariables.length; ++n) {
            object = this.mCache.mIndexedVariables[n];
            if (object == null) continue;
            object.reset();
        }
        this.mCache.solverVariablePool.releaseAll((SolverVariable[])this.mPoolVariables, this.mPoolVariablesCount);
        this.mPoolVariablesCount = 0;
        Arrays.fill(this.mCache.mIndexedVariables, null);
        object = this.mVariables;
        if (object != null) {
            object.clear();
        }
        this.mVariablesID = 0;
        this.mGoal.clear();
        this.mNumColumns = 1;
        for (n = 0; n < this.mNumRows; ++n) {
            object = this.mRows;
            if (object[n] == null) continue;
            object[n].used = false;
        }
        this.releaseRows();
        this.mNumRows = 0;
        this.mTempGoal = OPTIMIZED_ENGINE ? new ValuesRow(this, this.mCache) : new ArrayRow(this.mCache);
    }

    static interface Row {
        public void addError(SolverVariable var1);

        public void clear();

        public SolverVariable getKey();

        public SolverVariable getPivotCandidate(LinearSystem var1, boolean[] var2);

        public void initFromRow(Row var1);

        public boolean isEmpty();

        public void updateFromFinalVariable(LinearSystem var1, SolverVariable var2, boolean var3);

        public void updateFromRow(LinearSystem var1, ArrayRow var2, boolean var3);

        public void updateFromSystem(LinearSystem var1);
    }

    class ValuesRow
    extends ArrayRow {
        final LinearSystem this$0;

        public ValuesRow(LinearSystem linearSystem, Cache cache) {
            this.this$0 = linearSystem;
            this.variables = new SolverVariableValues(this, cache);
        }
    }
}

