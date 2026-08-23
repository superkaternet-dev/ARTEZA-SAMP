/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver;

import androidx.constraintlayout.solver.ArrayRow;
import androidx.constraintlayout.solver.Cache;
import androidx.constraintlayout.solver.SolverVariable;
import java.io.PrintStream;
import java.util.Arrays;

public class SolverVariableValues
implements ArrayRow.ArrayRowVariables {
    private static final boolean DEBUG = false;
    private static final boolean HASH = true;
    private static float epsilon = 0.001f;
    private int HASH_SIZE = 16;
    private final int NONE;
    private int SIZE = 16;
    int head = -1;
    int[] keys = new int[16];
    protected final Cache mCache;
    int mCount = 0;
    private final ArrayRow mRow;
    int[] next;
    int[] nextKeys = new int[16];
    int[] previous;
    float[] values;
    int[] variables = new int[16];

    SolverVariableValues(ArrayRow arrayRow, Cache cache) {
        this.NONE = -1;
        this.values = new float[16];
        this.previous = new int[16];
        this.next = new int[16];
        this.mRow = arrayRow;
        this.mCache = cache;
        this.clear();
    }

    private void addToHashMap(SolverVariable object, int n) {
        Object object2;
        int n2 = ((SolverVariable)object).id % this.HASH_SIZE;
        object = this.keys;
        Object object3 = object2 = object[n2];
        if (object2 == -1) {
            object[n2] = n;
        } else {
            while ((object = (Object)this.nextKeys)[object3] != -1) {
                object3 = object[object3];
            }
            object[object3] = n;
        }
        this.nextKeys[n] = -1;
    }

    private void addVariable(int n, SolverVariable solverVariable, float f) {
        this.variables[n] = solverVariable.id;
        this.values[n] = f;
        this.previous[n] = -1;
        this.next[n] = -1;
        solverVariable.addToRow(this.mRow);
        ++solverVariable.usageInRowCount;
        ++this.mCount;
    }

    private void displayHash() {
        for (int i = 0; i < this.HASH_SIZE; ++i) {
            if (this.keys[i] == -1) continue;
            CharSequence charSequence = new StringBuilder();
            charSequence.append(this.hashCode());
            charSequence.append(" hash [");
            charSequence.append(i);
            charSequence.append("] => ");
            charSequence = charSequence.toString();
            Object object = this.keys[i];
            boolean bl = false;
            while (!bl) {
                Object object2 = new StringBuilder();
                ((StringBuilder)object2).append((String)charSequence);
                ((StringBuilder)object2).append(" ");
                ((StringBuilder)object2).append(this.variables[object]);
                charSequence = ((StringBuilder)object2).toString();
                object2 = this.nextKeys;
                if (object2[object] != -1) {
                    object = object2[object];
                    continue;
                }
                bl = true;
            }
            System.out.println((String)charSequence);
        }
    }

    private int findEmptySlot() {
        for (int i = 0; i < this.SIZE; ++i) {
            if (this.variables[i] != -1) continue;
            return i;
        }
        return -1;
    }

    private void increaseSize() {
        int n = this.SIZE * 2;
        this.variables = Arrays.copyOf(this.variables, n);
        this.values = Arrays.copyOf(this.values, n);
        this.previous = Arrays.copyOf(this.previous, n);
        this.next = Arrays.copyOf(this.next, n);
        this.nextKeys = Arrays.copyOf(this.nextKeys, n);
        for (int i = this.SIZE; i < n; ++i) {
            this.variables[i] = -1;
            this.nextKeys[i] = -1;
        }
        this.SIZE = n;
    }

    private void insertVariable(int n, SolverVariable solverVariable, float f) {
        int[] nArray;
        int n2 = this.findEmptySlot();
        this.addVariable(n2, solverVariable, f);
        if (n != -1) {
            this.previous[n2] = n;
            nArray = this.next;
            nArray[n2] = nArray[n];
            nArray[n] = n2;
        } else {
            this.previous[n2] = -1;
            if (this.mCount > 0) {
                this.next[n2] = this.head;
                this.head = n2;
            } else {
                this.next[n2] = -1;
            }
        }
        nArray = this.next;
        if (nArray[n2] != -1) {
            this.previous[nArray[n2]] = n2;
        }
        this.addToHashMap(solverVariable, n2);
    }

    private void removeFromHashMap(SolverVariable object) {
        int n = ((SolverVariable)object).id % this.HASH_SIZE;
        Object object2 = this.keys[n];
        if (object2 == -1) {
            return;
        }
        int n2 = ((SolverVariable)object).id;
        Object object3 = object2;
        if (this.variables[object2] == n2) {
            object = this.keys;
            int[] nArray = this.nextKeys;
            object[n] = nArray[object2];
            nArray[object2] = -1;
        } else {
            while ((object = (Object)this.nextKeys)[object3] != -1 && this.variables[object[object3]] != n2) {
                object3 = object[object3];
            }
            object2 = object[object3];
            if (object2 != -1 && this.variables[object2] == n2) {
                object[object3] = object[object2];
                object[object2] = -1;
            }
        }
    }

    @Override
    public void add(SolverVariable solverVariable, float f, boolean bl) {
        float f2 = epsilon;
        if (f > -f2 && f < f2) {
            return;
        }
        int n = this.indexOf(solverVariable);
        if (n == -1) {
            this.put(solverVariable, f);
        } else {
            float[] fArray = this.values;
            fArray[n] = fArray[n] + f;
            f = fArray[n];
            f2 = epsilon;
            if (f > -f2 && fArray[n] < f2) {
                fArray[n] = 0.0f;
                this.remove(solverVariable, bl);
            }
        }
    }

    @Override
    public void clear() {
        int n;
        int n2 = this.mCount;
        for (n = 0; n < n2; ++n) {
            SolverVariable solverVariable = this.getVariable(n);
            if (solverVariable == null) continue;
            solverVariable.removeFromRow(this.mRow);
        }
        for (n = 0; n < this.SIZE; ++n) {
            this.variables[n] = -1;
            this.nextKeys[n] = -1;
        }
        for (n = 0; n < this.HASH_SIZE; ++n) {
            this.keys[n] = -1;
        }
        this.mCount = 0;
        this.head = -1;
    }

    @Override
    public boolean contains(SolverVariable solverVariable) {
        boolean bl = this.indexOf(solverVariable) != -1;
        return bl;
    }

    @Override
    public void display() {
        int n = this.mCount;
        System.out.print("{ ");
        for (int i = 0; i < n; ++i) {
            SolverVariable solverVariable = this.getVariable(i);
            if (solverVariable == null) continue;
            PrintStream printStream = System.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(solverVariable);
            stringBuilder.append(" = ");
            stringBuilder.append(this.getVariableValue(i));
            stringBuilder.append(" ");
            printStream.print(stringBuilder.toString());
        }
        System.out.println(" }");
    }

    @Override
    public void divideByAmount(float f) {
        int n = this.mCount;
        int n2 = this.head;
        for (int i = 0; i < n; ++i) {
            float[] fArray = this.values;
            fArray[n2] = fArray[n2] / f;
            if ((n2 = this.next[n2]) == -1) break;
        }
    }

    @Override
    public float get(SolverVariable solverVariable) {
        int n = this.indexOf(solverVariable);
        if (n != -1) {
            return this.values[n];
        }
        return 0.0f;
    }

    @Override
    public int getCurrentSize() {
        return this.mCount;
    }

    @Override
    public SolverVariable getVariable(int n) {
        int n2 = this.mCount;
        if (n2 == 0) {
            return null;
        }
        int n3 = this.head;
        for (int i = 0; i < n2; ++i) {
            if (i == n && n3 != -1) {
                return this.mCache.mIndexedVariables[this.variables[n3]];
            }
            if ((n3 = this.next[n3]) == -1) break;
        }
        return null;
    }

    @Override
    public float getVariableValue(int n) {
        int n2 = this.mCount;
        int n3 = this.head;
        for (int i = 0; i < n2; ++i) {
            if (i == n) {
                return this.values[n3];
            }
            if ((n3 = this.next[n3]) == -1) break;
        }
        return 0.0f;
    }

    @Override
    public int indexOf(SolverVariable object) {
        if (this.mCount != 0 && object != null) {
            int n = ((SolverVariable)object).id;
            Object object2 = this.HASH_SIZE;
            int n2 = this.keys[n % object2];
            if (n2 == -1) {
                return -1;
            }
            object2 = n2;
            if (this.variables[n2] == n) {
                return n2;
            }
            while ((object = (Object)this.nextKeys)[object2] != -1 && this.variables[object[object2]] != n) {
                object2 = object[object2];
            }
            if (object[object2] == -1) {
                return -1;
            }
            if (this.variables[object[object2]] == n) {
                return (int)object[object2];
            }
            return -1;
        }
        return -1;
    }

    @Override
    public void invert() {
        int n = this.mCount;
        int n2 = this.head;
        for (int i = 0; i < n; ++i) {
            float[] fArray = this.values;
            fArray[n2] = fArray[n2] * -1.0f;
            if ((n2 = this.next[n2]) == -1) break;
        }
    }

    @Override
    public void put(SolverVariable solverVariable, float f) {
        float f2 = epsilon;
        if (f > -f2 && f < f2) {
            this.remove(solverVariable, true);
            return;
        }
        if (this.mCount == 0) {
            this.addVariable(0, solverVariable, f);
            this.addToHashMap(solverVariable, 0);
            this.head = 0;
        } else {
            int n = this.indexOf(solverVariable);
            if (n != -1) {
                this.values[n] = f;
            } else {
                int n2;
                if (this.mCount + 1 >= this.SIZE) {
                    this.increaseSize();
                }
                int n3 = this.mCount;
                int n4 = -1;
                n = this.head;
                int n5 = 0;
                while (true) {
                    n2 = n4;
                    if (n5 >= n3) break;
                    if (this.variables[n] == solverVariable.id) {
                        this.values[n] = f;
                        return;
                    }
                    if (this.variables[n] < solverVariable.id) {
                        n4 = n;
                    }
                    if ((n = this.next[n]) == -1) {
                        n2 = n4;
                        break;
                    }
                    ++n5;
                }
                this.insertVariable(n2, solverVariable, f);
            }
        }
    }

    @Override
    public float remove(SolverVariable solverVariable, boolean bl) {
        int[] nArray;
        int n = this.indexOf(solverVariable);
        if (n == -1) {
            return 0.0f;
        }
        this.removeFromHashMap(solverVariable);
        float f = this.values[n];
        if (this.head == n) {
            this.head = this.next[n];
        }
        this.variables[n] = -1;
        int[] nArray2 = this.previous;
        if (nArray2[n] != -1) {
            nArray = this.next;
            nArray[nArray2[n]] = nArray[n];
        }
        if ((nArray = this.next)[n] != -1) {
            nArray2[nArray[n]] = nArray2[n];
        }
        --this.mCount;
        --solverVariable.usageInRowCount;
        if (bl) {
            solverVariable.removeFromRow(this.mRow);
        }
        return f;
    }

    @Override
    public int sizeInBytes() {
        return 0;
    }

    public String toString() {
        Object object;
        CharSequence charSequence = new StringBuilder();
        charSequence.append(this.hashCode());
        charSequence.append(" { ");
        charSequence = charSequence.toString();
        int n = this.mCount;
        for (int i = 0; i < n; ++i) {
            object = this.getVariable(i);
            if (object == null) continue;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((String)charSequence);
            stringBuilder.append(object);
            stringBuilder.append(" = ");
            stringBuilder.append(this.getVariableValue(i));
            stringBuilder.append(" ");
            charSequence = stringBuilder.toString();
            int n2 = this.indexOf((SolverVariable)object);
            object = new StringBuilder();
            ((StringBuilder)object).append((String)charSequence);
            ((StringBuilder)object).append("[p: ");
            charSequence = ((StringBuilder)object).toString();
            if (this.previous[n2] != -1) {
                object = new StringBuilder();
                ((StringBuilder)object).append((String)charSequence);
                ((StringBuilder)object).append(this.mCache.mIndexedVariables[this.variables[this.previous[n2]]]);
                charSequence = ((StringBuilder)object).toString();
            } else {
                object = new StringBuilder();
                ((StringBuilder)object).append((String)charSequence);
                ((StringBuilder)object).append("none");
                charSequence = ((StringBuilder)object).toString();
            }
            object = new StringBuilder();
            ((StringBuilder)object).append((String)charSequence);
            ((StringBuilder)object).append(", n: ");
            charSequence = ((StringBuilder)object).toString();
            if (this.next[n2] != -1) {
                object = new StringBuilder();
                ((StringBuilder)object).append((String)charSequence);
                ((StringBuilder)object).append(this.mCache.mIndexedVariables[this.variables[this.next[n2]]]);
                charSequence = ((StringBuilder)object).toString();
            } else {
                object = new StringBuilder();
                ((StringBuilder)object).append((String)charSequence);
                ((StringBuilder)object).append("none");
                charSequence = ((StringBuilder)object).toString();
            }
            object = new StringBuilder();
            ((StringBuilder)object).append((String)charSequence);
            ((StringBuilder)object).append("]");
            charSequence = ((StringBuilder)object).toString();
        }
        object = new StringBuilder();
        ((StringBuilder)object).append((String)charSequence);
        ((StringBuilder)object).append(" }");
        return ((StringBuilder)object).toString();
    }

    @Override
    public float use(ArrayRow object, boolean bl) {
        float f = this.get(((ArrayRow)object).variable);
        this.remove(((ArrayRow)object).variable, bl);
        object = (SolverVariableValues)((ArrayRow)object).variables;
        int n = ((SolverVariableValues)object).getCurrentSize();
        int n2 = ((SolverVariableValues)object).head;
        int n3 = 0;
        n2 = 0;
        while (n3 < n) {
            int n4 = n3;
            if (((SolverVariableValues)object).variables[n2] != -1) {
                float f2 = ((SolverVariableValues)object).values[n2];
                this.add(this.mCache.mIndexedVariables[((SolverVariableValues)object).variables[n2]], f2 * f, bl);
                n4 = n3 + 1;
            }
            ++n2;
            n3 = n4;
        }
        return f;
    }
}

