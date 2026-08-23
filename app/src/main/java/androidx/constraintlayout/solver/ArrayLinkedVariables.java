/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver;

import androidx.constraintlayout.solver.ArrayRow;
import androidx.constraintlayout.solver.Cache;
import androidx.constraintlayout.solver.SolverVariable;
import java.io.PrintStream;
import java.util.Arrays;

public class ArrayLinkedVariables
implements ArrayRow.ArrayRowVariables {
    private static final boolean DEBUG = false;
    private static final boolean FULL_NEW_CHECK = false;
    static final int NONE = -1;
    private static float epsilon = 0.001f;
    private int ROW_SIZE = 8;
    private SolverVariable candidate = null;
    int currentSize = 0;
    private int[] mArrayIndices = new int[8];
    private int[] mArrayNextIndices = new int[8];
    private float[] mArrayValues = new float[8];
    protected final Cache mCache;
    private boolean mDidFillOnce = false;
    private int mHead = -1;
    private int mLast = -1;
    private final ArrayRow mRow;

    ArrayLinkedVariables(ArrayRow arrayRow, Cache cache) {
        this.mRow = arrayRow;
        this.mCache = cache;
    }

    @Override
    public void add(SolverVariable object, float f, boolean bl) {
        int[] nArray;
        int n;
        float f2 = epsilon;
        if (f > -f2 && f < f2) {
            return;
        }
        if (this.mHead == -1) {
            this.mHead = 0;
            this.mArrayValues[0] = f;
            this.mArrayIndices[0] = ((SolverVariable)object).id;
            this.mArrayNextIndices[this.mHead] = -1;
            ++((SolverVariable)object).usageInRowCount;
            ((SolverVariable)object).addToRow(this.mRow);
            ++this.currentSize;
            if (!this.mDidFillOnce) {
                int n2;
                this.mLast = n2 = this.mLast + 1;
                object = this.mArrayIndices;
                if (n2 >= ((Object)object).length) {
                    this.mDidFillOnce = true;
                    this.mLast = ((Object)object).length - 1;
                }
            }
            return;
        }
        int n3 = this.mHead;
        int n4 = -1;
        for (n = 0; n3 != -1 && n < this.currentSize; ++n) {
            if (this.mArrayIndices[n3] == ((SolverVariable)object).id) {
                Object[] objectArray = this.mArrayValues;
                f2 = objectArray[n3] + f;
                float f3 = epsilon;
                f = f2;
                if (f2 > -f3) {
                    f = f2;
                    if (f2 < f3) {
                        f = 0.0f;
                    }
                }
                objectArray[n3] = f;
                if (f == 0.0f) {
                    if (n3 == this.mHead) {
                        this.mHead = this.mArrayNextIndices[n3];
                    } else {
                        objectArray = this.mArrayNextIndices;
                        objectArray[n4] = objectArray[n3];
                    }
                    if (bl) {
                        ((SolverVariable)object).removeFromRow(this.mRow);
                    }
                    if (this.mDidFillOnce) {
                        this.mLast = n3;
                    }
                    --((SolverVariable)object).usageInRowCount;
                    --this.currentSize;
                }
                return;
            }
            if (this.mArrayIndices[n3] < ((SolverVariable)object).id) {
                n4 = n3;
            }
            n3 = this.mArrayNextIndices[n3];
        }
        n = this.mLast;
        n3 = n + 1;
        if (this.mDidFillOnce) {
            nArray = this.mArrayIndices;
            n3 = nArray[n] == -1 ? this.mLast : nArray.length;
        }
        nArray = this.mArrayIndices;
        n = n3;
        if (n3 >= nArray.length) {
            n = n3;
            if (this.currentSize < nArray.length) {
                int n5 = 0;
                while (true) {
                    nArray = this.mArrayIndices;
                    n = n3;
                    if (n5 >= nArray.length) break;
                    if (nArray[n5] == -1) {
                        n = n5;
                        break;
                    }
                    ++n5;
                }
            }
        }
        nArray = this.mArrayIndices;
        n3 = n;
        if (n >= nArray.length) {
            n3 = nArray.length;
            this.ROW_SIZE = n = this.ROW_SIZE * 2;
            this.mDidFillOnce = false;
            this.mLast = n3 - 1;
            this.mArrayValues = Arrays.copyOf(this.mArrayValues, n);
            this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
            this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
        }
        this.mArrayIndices[n3] = ((SolverVariable)object).id;
        this.mArrayValues[n3] = f;
        if (n4 != -1) {
            nArray = this.mArrayNextIndices;
            nArray[n3] = nArray[n4];
            nArray[n4] = n3;
        } else {
            this.mArrayNextIndices[n3] = this.mHead;
            this.mHead = n3;
        }
        ++((SolverVariable)object).usageInRowCount;
        ((SolverVariable)object).addToRow(this.mRow);
        ++this.currentSize;
        if (!this.mDidFillOnce) {
            ++this.mLast;
        }
        if ((n3 = this.mLast) >= ((Object)(object = (Object)this.mArrayIndices)).length) {
            this.mDidFillOnce = true;
            this.mLast = ((Object)object).length - 1;
        }
    }

    @Override
    public final void clear() {
        int n = this.mHead;
        for (int i = 0; n != -1 && i < this.currentSize; ++i) {
            SolverVariable solverVariable = this.mCache.mIndexedVariables[this.mArrayIndices[n]];
            if (solverVariable != null) {
                solverVariable.removeFromRow(this.mRow);
            }
            n = this.mArrayNextIndices[n];
        }
        this.mHead = -1;
        this.mLast = -1;
        this.mDidFillOnce = false;
        this.currentSize = 0;
    }

    @Override
    public boolean contains(SolverVariable solverVariable) {
        if (this.mHead == -1) {
            return false;
        }
        int n = this.mHead;
        for (int i = 0; n != -1 && i < this.currentSize; ++i) {
            if (this.mArrayIndices[n] == solverVariable.id) {
                return true;
            }
            n = this.mArrayNextIndices[n];
        }
        return false;
    }

    @Override
    public void display() {
        int n = this.currentSize;
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
        int n = this.mHead;
        for (int i = 0; n != -1 && i < this.currentSize; ++i) {
            float[] fArray = this.mArrayValues;
            fArray[n] = fArray[n] / f;
            n = this.mArrayNextIndices[n];
        }
    }

    @Override
    public final float get(SolverVariable solverVariable) {
        int n = this.mHead;
        for (int i = 0; n != -1 && i < this.currentSize; ++i) {
            if (this.mArrayIndices[n] == solverVariable.id) {
                return this.mArrayValues[n];
            }
            n = this.mArrayNextIndices[n];
        }
        return 0.0f;
    }

    @Override
    public int getCurrentSize() {
        return this.currentSize;
    }

    public int getHead() {
        return this.mHead;
    }

    public final int getId(int n) {
        return this.mArrayIndices[n];
    }

    public final int getNextIndice(int n) {
        return this.mArrayNextIndices[n];
    }

    SolverVariable getPivotCandidate() {
        SolverVariable solverVariable;
        block3: {
            solverVariable = this.candidate;
            if (solverVariable != null) break block3;
            int n = this.mHead;
            solverVariable = null;
            for (int i = 0; n != -1 && i < this.currentSize; ++i) {
                SolverVariable solverVariable2;
                block4: {
                    SolverVariable solverVariable3;
                    block5: {
                        solverVariable2 = solverVariable;
                        if (!(this.mArrayValues[n] < 0.0f)) break block4;
                        solverVariable3 = this.mCache.mIndexedVariables[this.mArrayIndices[n]];
                        if (solverVariable == null) break block5;
                        solverVariable2 = solverVariable;
                        if (solverVariable.strength >= solverVariable3.strength) break block4;
                    }
                    solverVariable2 = solverVariable3;
                }
                n = this.mArrayNextIndices[n];
                solverVariable = solverVariable2;
            }
            return solverVariable;
        }
        return solverVariable;
    }

    public final float getValue(int n) {
        return this.mArrayValues[n];
    }

    @Override
    public SolverVariable getVariable(int n) {
        int n2 = this.mHead;
        for (int i = 0; n2 != -1 && i < this.currentSize; ++i) {
            if (i == n) {
                return this.mCache.mIndexedVariables[this.mArrayIndices[n2]];
            }
            n2 = this.mArrayNextIndices[n2];
        }
        return null;
    }

    @Override
    public float getVariableValue(int n) {
        int n2 = this.mHead;
        for (int i = 0; n2 != -1 && i < this.currentSize; ++i) {
            if (i == n) {
                return this.mArrayValues[n2];
            }
            n2 = this.mArrayNextIndices[n2];
        }
        return 0.0f;
    }

    boolean hasAtLeastOnePositiveVariable() {
        int n = this.mHead;
        for (int i = 0; n != -1 && i < this.currentSize; ++i) {
            if (this.mArrayValues[n] > 0.0f) {
                return true;
            }
            n = this.mArrayNextIndices[n];
        }
        return false;
    }

    @Override
    public int indexOf(SolverVariable solverVariable) {
        if (this.mHead == -1) {
            return -1;
        }
        int n = this.mHead;
        for (int i = 0; n != -1 && i < this.currentSize; ++i) {
            if (this.mArrayIndices[n] == solverVariable.id) {
                return n;
            }
            n = this.mArrayNextIndices[n];
        }
        return -1;
    }

    @Override
    public void invert() {
        int n = this.mHead;
        for (int i = 0; n != -1 && i < this.currentSize; ++i) {
            float[] fArray = this.mArrayValues;
            fArray[n] = fArray[n] * -1.0f;
            n = this.mArrayNextIndices[n];
        }
    }

    @Override
    public final void put(SolverVariable object, float f) {
        int[] nArray;
        int n;
        if (f == 0.0f) {
            this.remove((SolverVariable)object, true);
            return;
        }
        if (this.mHead == -1) {
            this.mHead = 0;
            this.mArrayValues[0] = f;
            this.mArrayIndices[0] = ((SolverVariable)object).id;
            this.mArrayNextIndices[this.mHead] = -1;
            ++((SolverVariable)object).usageInRowCount;
            ((SolverVariable)object).addToRow(this.mRow);
            ++this.currentSize;
            if (!this.mDidFillOnce) {
                int n2;
                this.mLast = n2 = this.mLast + 1;
                object = this.mArrayIndices;
                if (n2 >= ((Object)object).length) {
                    this.mDidFillOnce = true;
                    this.mLast = ((Object)object).length - 1;
                }
            }
            return;
        }
        int n3 = this.mHead;
        int n4 = -1;
        for (n = 0; n3 != -1 && n < this.currentSize; ++n) {
            if (this.mArrayIndices[n3] == ((SolverVariable)object).id) {
                this.mArrayValues[n3] = f;
                return;
            }
            if (this.mArrayIndices[n3] < ((SolverVariable)object).id) {
                n4 = n3;
            }
            n3 = this.mArrayNextIndices[n3];
        }
        n = this.mLast;
        n3 = n + 1;
        if (this.mDidFillOnce) {
            nArray = this.mArrayIndices;
            n3 = nArray[n] == -1 ? this.mLast : nArray.length;
        }
        nArray = this.mArrayIndices;
        n = n3;
        if (n3 >= nArray.length) {
            n = n3;
            if (this.currentSize < nArray.length) {
                int n5 = 0;
                while (true) {
                    nArray = this.mArrayIndices;
                    n = n3;
                    if (n5 >= nArray.length) break;
                    if (nArray[n5] == -1) {
                        n = n5;
                        break;
                    }
                    ++n5;
                }
            }
        }
        nArray = this.mArrayIndices;
        n3 = n;
        if (n >= nArray.length) {
            n3 = nArray.length;
            this.ROW_SIZE = n = this.ROW_SIZE * 2;
            this.mDidFillOnce = false;
            this.mLast = n3 - 1;
            this.mArrayValues = Arrays.copyOf(this.mArrayValues, n);
            this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
            this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
        }
        this.mArrayIndices[n3] = ((SolverVariable)object).id;
        this.mArrayValues[n3] = f;
        if (n4 != -1) {
            nArray = this.mArrayNextIndices;
            nArray[n3] = nArray[n4];
            nArray[n4] = n3;
        } else {
            this.mArrayNextIndices[n3] = this.mHead;
            this.mHead = n3;
        }
        ++((SolverVariable)object).usageInRowCount;
        ((SolverVariable)object).addToRow(this.mRow);
        this.currentSize = n3 = this.currentSize + 1;
        if (!this.mDidFillOnce) {
            ++this.mLast;
        }
        if (n3 >= ((Object)(object = (Object)this.mArrayIndices)).length) {
            this.mDidFillOnce = true;
        }
        if (this.mLast >= ((Object)object).length) {
            this.mDidFillOnce = true;
            this.mLast = ((Object)object).length - 1;
        }
    }

    @Override
    public final float remove(SolverVariable solverVariable, boolean bl) {
        if (this.candidate == solverVariable) {
            this.candidate = null;
        }
        if (this.mHead == -1) {
            return 0.0f;
        }
        int n = this.mHead;
        int n2 = -1;
        for (int i = 0; n != -1 && i < this.currentSize; ++i) {
            if (this.mArrayIndices[n] == solverVariable.id) {
                if (n == this.mHead) {
                    this.mHead = this.mArrayNextIndices[n];
                } else {
                    int[] nArray = this.mArrayNextIndices;
                    nArray[n2] = nArray[n];
                }
                if (bl) {
                    solverVariable.removeFromRow(this.mRow);
                }
                --solverVariable.usageInRowCount;
                --this.currentSize;
                this.mArrayIndices[n] = -1;
                if (this.mDidFillOnce) {
                    this.mLast = n;
                }
                return this.mArrayValues[n];
            }
            n2 = n;
            n = this.mArrayNextIndices[n];
        }
        return 0.0f;
    }

    @Override
    public int sizeInBytes() {
        return 0 + this.mArrayIndices.length * 4 * 3 + 36;
    }

    public String toString() {
        CharSequence charSequence = "";
        int n = this.mHead;
        for (int i = 0; n != -1 && i < this.currentSize; ++i) {
            CharSequence charSequence2 = new StringBuilder();
            charSequence2.append((String)charSequence);
            charSequence2.append(" -> ");
            charSequence2 = charSequence2.toString();
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append((String)charSequence2);
            ((StringBuilder)charSequence).append(this.mArrayValues[n]);
            ((StringBuilder)charSequence).append(" : ");
            charSequence2 = ((StringBuilder)charSequence).toString();
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append((String)charSequence2);
            ((StringBuilder)charSequence).append(this.mCache.mIndexedVariables[this.mArrayIndices[n]]);
            charSequence = ((StringBuilder)charSequence).toString();
            n = this.mArrayNextIndices[n];
        }
        return charSequence;
    }

    @Override
    public float use(ArrayRow object, boolean bl) {
        float f = this.get(((ArrayRow)object).variable);
        this.remove(((ArrayRow)object).variable, bl);
        ArrayRow.ArrayRowVariables arrayRowVariables = ((ArrayRow)object).variables;
        int n = arrayRowVariables.getCurrentSize();
        for (int i = 0; i < n; ++i) {
            object = arrayRowVariables.getVariable(i);
            this.add((SolverVariable)object, arrayRowVariables.get((SolverVariable)object) * f, bl);
        }
        return f;
    }
}

