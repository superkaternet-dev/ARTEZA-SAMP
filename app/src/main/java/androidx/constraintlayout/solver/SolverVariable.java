/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver;

import androidx.constraintlayout.solver.ArrayRow;
import androidx.constraintlayout.solver.LinearSystem;
import java.util.Arrays;
import java.util.HashSet;

public class SolverVariable {
    private static final boolean INTERNAL_DEBUG = false;
    static final int MAX_STRENGTH = 9;
    public static final int STRENGTH_BARRIER = 6;
    public static final int STRENGTH_CENTERING = 7;
    public static final int STRENGTH_EQUALITY = 5;
    public static final int STRENGTH_FIXED = 8;
    public static final int STRENGTH_HIGH = 3;
    public static final int STRENGTH_HIGHEST = 4;
    public static final int STRENGTH_LOW = 1;
    public static final int STRENGTH_MEDIUM = 2;
    public static final int STRENGTH_NONE = 0;
    private static final boolean VAR_USE_HASH = false;
    private static int uniqueConstantId;
    private static int uniqueErrorId;
    private static int uniqueId;
    private static int uniqueSlackId;
    private static int uniqueUnrestrictedId;
    public float computedValue;
    int definitionId = -1;
    float[] goalStrengthVector;
    public int id = -1;
    public boolean inGoal;
    HashSet<ArrayRow> inRows = null;
    public boolean isFinalValue = false;
    boolean isSynonym = false;
    ArrayRow[] mClientEquations;
    int mClientEquationsCount = 0;
    private String mName;
    Type mType;
    public int strength = 0;
    float[] strengthVector = new float[9];
    int synonym = -1;
    float synonymDelta = 0.0f;
    public int usageInRowCount = 0;

    static {
        uniqueSlackId = 1;
        uniqueErrorId = 1;
        uniqueUnrestrictedId = 1;
        uniqueConstantId = 1;
        uniqueId = 1;
    }

    public SolverVariable(Type type, String string2) {
        this.goalStrengthVector = new float[9];
        this.mClientEquations = new ArrayRow[16];
        this.mType = type;
    }

    public SolverVariable(String string2, Type type) {
        this.goalStrengthVector = new float[9];
        this.mClientEquations = new ArrayRow[16];
        this.mName = string2;
        this.mType = type;
    }

    private static String getUniqueName(Type object, String string2) {
        int n;
        if (string2 != null) {
            object = new StringBuilder();
            ((StringBuilder)object).append(string2);
            ((StringBuilder)object).append(uniqueErrorId);
            return ((StringBuilder)object).toString();
        }
        switch (1.$SwitchMap$androidx$constraintlayout$solver$SolverVariable$Type[((Enum)object).ordinal()]) {
            default: {
                throw new AssertionError((Object)((Enum)object).name());
            }
            case 5: {
                int n2;
                object = new StringBuilder();
                ((StringBuilder)object).append("V");
                uniqueId = n2 = uniqueId + 1;
                ((StringBuilder)object).append(n2);
                return ((StringBuilder)object).toString();
            }
            case 4: {
                int n3;
                object = new StringBuilder();
                ((StringBuilder)object).append("e");
                uniqueErrorId = n3 = uniqueErrorId + 1;
                ((StringBuilder)object).append(n3);
                return ((StringBuilder)object).toString();
            }
            case 3: {
                int n4;
                object = new StringBuilder();
                ((StringBuilder)object).append("S");
                uniqueSlackId = n4 = uniqueSlackId + 1;
                ((StringBuilder)object).append(n4);
                return ((StringBuilder)object).toString();
            }
            case 2: {
                int n5;
                object = new StringBuilder();
                ((StringBuilder)object).append("C");
                uniqueConstantId = n5 = uniqueConstantId + 1;
                ((StringBuilder)object).append(n5);
                return ((StringBuilder)object).toString();
            }
            case 1: 
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("U");
        uniqueUnrestrictedId = n = uniqueUnrestrictedId + 1;
        ((StringBuilder)object).append(n);
        return ((StringBuilder)object).toString();
    }

    static void increaseErrorId() {
        ++uniqueErrorId;
    }

    public final void addToRow(ArrayRow arrayRow) {
        int n;
        int n2;
        for (n2 = 0; n2 < (n = this.mClientEquationsCount); ++n2) {
            if (this.mClientEquations[n2] != arrayRow) continue;
            return;
        }
        ArrayRow[] arrayRowArray = this.mClientEquations;
        if (n >= arrayRowArray.length) {
            this.mClientEquations = Arrays.copyOf(arrayRowArray, arrayRowArray.length * 2);
        }
        arrayRowArray = this.mClientEquations;
        n2 = this.mClientEquationsCount;
        arrayRowArray[n2] = arrayRow;
        this.mClientEquationsCount = n2 + 1;
    }

    void clearStrengths() {
        for (int i = 0; i < 9; ++i) {
            this.strengthVector[i] = 0.0f;
        }
    }

    public String getName() {
        return this.mName;
    }

    public final void removeFromRow(ArrayRow arrayRowArray) {
        int n = this.mClientEquationsCount;
        for (int i = 0; i < n; ++i) {
            if (this.mClientEquations[i] != arrayRowArray) continue;
            while (i < n - 1) {
                arrayRowArray = this.mClientEquations;
                arrayRowArray[i] = arrayRowArray[i + 1];
                ++i;
            }
            --this.mClientEquationsCount;
            return;
        }
    }

    public void reset() {
        this.mName = null;
        this.mType = Type.UNKNOWN;
        this.strength = 0;
        this.id = -1;
        this.definitionId = -1;
        this.computedValue = 0.0f;
        this.isFinalValue = false;
        this.isSynonym = false;
        this.synonym = -1;
        this.synonymDelta = 0.0f;
        int n = this.mClientEquationsCount;
        for (int i = 0; i < n; ++i) {
            this.mClientEquations[i] = null;
        }
        this.mClientEquationsCount = 0;
        this.usageInRowCount = 0;
        this.inGoal = false;
        Arrays.fill(this.goalStrengthVector, 0.0f);
    }

    public void setFinalValue(LinearSystem linearSystem, float f) {
        this.computedValue = f;
        this.isFinalValue = true;
        this.isSynonym = false;
        this.synonym = -1;
        this.synonymDelta = 0.0f;
        int n = this.mClientEquationsCount;
        this.definitionId = -1;
        for (int i = 0; i < n; ++i) {
            this.mClientEquations[i].updateFromFinalVariable(linearSystem, this, false);
        }
        this.mClientEquationsCount = 0;
    }

    public void setName(String string2) {
        this.mName = string2;
    }

    public void setSynonym(LinearSystem linearSystem, SolverVariable solverVariable, float f) {
        this.isSynonym = true;
        this.synonym = solverVariable.id;
        this.synonymDelta = f;
        int n = this.mClientEquationsCount;
        this.definitionId = -1;
        for (int i = 0; i < n; ++i) {
            this.mClientEquations[i].updateFromSynonymVariable(linearSystem, this, false);
        }
        this.mClientEquationsCount = 0;
        linearSystem.displayReadableRows();
    }

    public void setType(Type type, String string2) {
        this.mType = type;
    }

    String strengthsToString() {
        CharSequence charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append(this);
        ((StringBuilder)charSequence).append("[");
        Object object = ((StringBuilder)charSequence).toString();
        boolean bl = false;
        boolean bl2 = true;
        for (int i = 0; i < this.strengthVector.length; ++i) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append((String)object);
            ((StringBuilder)charSequence).append(this.strengthVector[i]);
            charSequence = ((StringBuilder)charSequence).toString();
            object = this.strengthVector;
            if (object[i] > 0.0f) {
                bl = false;
            } else if (object[i] < 0.0f) {
                bl = true;
            }
            if (object[i] != 0.0f) {
                bl2 = false;
            }
            if (i < ((Object)object).length - 1) {
                object = new StringBuilder();
                ((StringBuilder)object).append((String)charSequence);
                ((StringBuilder)object).append(", ");
                object = ((StringBuilder)object).toString();
                continue;
            }
            object = new StringBuilder();
            ((StringBuilder)object).append((String)charSequence);
            ((StringBuilder)object).append("] ");
            object = ((StringBuilder)object).toString();
        }
        charSequence = object;
        if (bl) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append((String)object);
            ((StringBuilder)charSequence).append(" (-)");
            charSequence = ((StringBuilder)charSequence).toString();
        }
        object = charSequence;
        if (bl2) {
            object = new StringBuilder();
            ((StringBuilder)object).append((String)charSequence);
            ((StringBuilder)object).append(" (*)");
            object = ((StringBuilder)object).toString();
        }
        return object;
    }

    public String toString() {
        CharSequence charSequence;
        if (this.mName != null) {
            charSequence = new StringBuilder();
            charSequence.append("");
            charSequence.append(this.mName);
            charSequence = charSequence.toString();
        } else {
            charSequence = new StringBuilder();
            charSequence.append("");
            charSequence.append(this.id);
            charSequence = charSequence.toString();
        }
        return charSequence;
    }

    public final void updateReferencesWithNewDefinition(LinearSystem linearSystem, ArrayRow arrayRow) {
        int n = this.mClientEquationsCount;
        for (int i = 0; i < n; ++i) {
            this.mClientEquations[i].updateFromRow(linearSystem, arrayRow, false);
        }
        this.mClientEquationsCount = 0;
    }

    public static final class Type
    extends Enum<Type> {
        private static final Type[] $VALUES;
        public static final /* enum */ Type CONSTANT;
        public static final /* enum */ Type ERROR;
        public static final /* enum */ Type SLACK;
        public static final /* enum */ Type UNKNOWN;
        public static final /* enum */ Type UNRESTRICTED;

        static {
            Type type;
            Type type2;
            Type type3;
            Type type4;
            Type type5;
            UNRESTRICTED = type5 = new Type();
            CONSTANT = type4 = new Type();
            SLACK = type3 = new Type();
            ERROR = type2 = new Type();
            UNKNOWN = type = new Type();
            $VALUES = new Type[]{type5, type4, type3, type2, type};
        }

        public static Type valueOf(String string2) {
            return Enum.valueOf(Type.class, string2);
        }

        public static Type[] values() {
            return (Type[])$VALUES.clone();
        }
    }
}

