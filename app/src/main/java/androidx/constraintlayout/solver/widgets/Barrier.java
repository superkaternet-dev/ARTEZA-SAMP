/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.HelperWidget;
import java.util.HashMap;

public class Barrier
extends HelperWidget {
    public static final int BOTTOM = 3;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int TOP = 2;
    private static final boolean USE_RELAX_GONE = false;
    private static final boolean USE_RESOLUTION = true;
    private boolean mAllowsGoneWidget = true;
    private int mBarrierType = 0;
    private int mMargin = 0;
    boolean resolved = false;

    public Barrier() {
    }

    public Barrier(String string2) {
        this.setDebugName(string2);
    }

    @Override
    public void addToSolver(LinearSystem linearSystem, boolean bl) {
        int n;
        this.mListAnchors[0] = this.mLeft;
        this.mListAnchors[2] = this.mTop;
        this.mListAnchors[1] = this.mRight;
        this.mListAnchors[3] = this.mBottom;
        for (n = 0; n < this.mListAnchors.length; ++n) {
            this.mListAnchors[n].mSolverVariable = linearSystem.createObjectVariable(this.mListAnchors[n]);
        }
        n = this.mBarrierType;
        if (n >= 0 && n < 4) {
            int n2;
            int n3;
            ConstraintWidget constraintWidget;
            ConstraintAnchor constraintAnchor = this.mListAnchors[this.mBarrierType];
            if (!this.resolved) {
                this.allSolved();
            }
            if (this.resolved) {
                this.resolved = false;
                n = this.mBarrierType;
                if (n != 0 && n != 1) {
                    if (n == 2 || n == 3) {
                        linearSystem.addEquality(this.mTop.mSolverVariable, this.mY);
                        linearSystem.addEquality(this.mBottom.mSolverVariable, this.mY);
                    }
                } else {
                    linearSystem.addEquality(this.mLeft.mSolverVariable, this.mX);
                    linearSystem.addEquality(this.mRight.mSolverVariable, this.mX);
                }
                return;
            }
            boolean bl2 = false;
            n = 0;
            while (true) {
                bl = bl2;
                if (n >= this.mWidgetsCount) break;
                constraintWidget = this.mWidgets[n];
                if (this.mAllowsGoneWidget || constraintWidget.allowedInBarrier()) {
                    n3 = this.mBarrierType;
                    if ((n3 == 0 || n3 == 1) && constraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mLeft.mTarget != null && constraintWidget.mRight.mTarget != null) {
                        bl = true;
                        break;
                    }
                    n3 = this.mBarrierType;
                    if ((n3 == 2 || n3 == 3) && constraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mTop.mTarget != null && constraintWidget.mBottom.mTarget != null) {
                        bl = true;
                        break;
                    }
                }
                ++n;
            }
            n = !this.mLeft.hasCenteredDependents() && !this.mRight.hasCenteredDependents() ? 0 : 1;
            n3 = !this.mTop.hasCenteredDependents() && !this.mBottom.hasCenteredDependents() ? 0 : 1;
            n3 = !bl && ((n2 = this.mBarrierType) == 0 && n != 0 || n2 == 2 && n3 != 0 || n2 == 1 && n != 0 || n2 == 3 && n3 != 0) ? 1 : 0;
            n = 5;
            if (n3 == 0) {
                n = 4;
            }
            for (n3 = 0; n3 < this.mWidgetsCount; ++n3) {
                int n4;
                SolverVariable solverVariable;
                constraintWidget = this.mWidgets[n3];
                if (!this.mAllowsGoneWidget && !constraintWidget.allowedInBarrier()) continue;
                constraintWidget.mListAnchors[this.mBarrierType].mSolverVariable = solverVariable = linearSystem.createObjectVariable(constraintWidget.mListAnchors[this.mBarrierType]);
                n2 = n4 = 0;
                if (constraintWidget.mListAnchors[this.mBarrierType].mTarget != null) {
                    n2 = n4;
                    if (constraintWidget.mListAnchors[this.mBarrierType].mTarget.mOwner == this) {
                        n2 = 0 + constraintWidget.mListAnchors[this.mBarrierType].mMargin;
                    }
                }
                if ((n4 = this.mBarrierType) != 0 && n4 != 2) {
                    linearSystem.addGreaterBarrier(constraintAnchor.mSolverVariable, solverVariable, this.mMargin + n2, bl);
                } else {
                    linearSystem.addLowerBarrier(constraintAnchor.mSolverVariable, solverVariable, this.mMargin - n2, bl);
                }
                linearSystem.addEquality(constraintAnchor.mSolverVariable, solverVariable, this.mMargin + n2, n);
            }
            n = this.mBarrierType;
            if (n == 0) {
                linearSystem.addEquality(this.mRight.mSolverVariable, this.mLeft.mSolverVariable, 0, 8);
                linearSystem.addEquality(this.mLeft.mSolverVariable, this.mParent.mRight.mSolverVariable, 0, 4);
                linearSystem.addEquality(this.mLeft.mSolverVariable, this.mParent.mLeft.mSolverVariable, 0, 0);
            } else if (n == 1) {
                linearSystem.addEquality(this.mLeft.mSolverVariable, this.mRight.mSolverVariable, 0, 8);
                linearSystem.addEquality(this.mLeft.mSolverVariable, this.mParent.mLeft.mSolverVariable, 0, 4);
                linearSystem.addEquality(this.mLeft.mSolverVariable, this.mParent.mRight.mSolverVariable, 0, 0);
            } else if (n == 2) {
                linearSystem.addEquality(this.mBottom.mSolverVariable, this.mTop.mSolverVariable, 0, 8);
                linearSystem.addEquality(this.mTop.mSolverVariable, this.mParent.mBottom.mSolverVariable, 0, 4);
                linearSystem.addEquality(this.mTop.mSolverVariable, this.mParent.mTop.mSolverVariable, 0, 0);
            } else if (n == 3) {
                linearSystem.addEquality(this.mTop.mSolverVariable, this.mBottom.mSolverVariable, 0, 8);
                linearSystem.addEquality(this.mTop.mSolverVariable, this.mParent.mTop.mSolverVariable, 0, 4);
                linearSystem.addEquality(this.mTop.mSolverVariable, this.mParent.mBottom.mSolverVariable, 0, 0);
            }
            return;
        }
    }

    public boolean allSolved() {
        int n;
        int n2;
        ConstraintWidget constraintWidget;
        int n3;
        int n4 = 1;
        for (n3 = 0; n3 < this.mWidgetsCount; ++n3) {
            block21: {
                block23: {
                    block22: {
                        block20: {
                            constraintWidget = this.mWidgets[n3];
                            if (this.mAllowsGoneWidget || constraintWidget.allowedInBarrier()) break block20;
                            n2 = n4;
                            break block21;
                        }
                        n2 = this.mBarrierType;
                        if (n2 != 0 && n2 != 1 || constraintWidget.isResolvedHorizontally()) break block22;
                        n2 = 0;
                        break block21;
                    }
                    n = this.mBarrierType;
                    if (n == 2) break block23;
                    n2 = n4;
                    if (n != 3) break block21;
                }
                n2 = n4;
                if (!constraintWidget.isResolvedVertically()) {
                    n2 = 0;
                }
            }
            n4 = n2;
        }
        if (n4 != 0 && this.mWidgetsCount > 0) {
            n2 = 0;
            n = 0;
            for (int i = 0; i < this.mWidgetsCount; ++i) {
                int n5;
                constraintWidget = this.mWidgets[i];
                if (!this.mAllowsGoneWidget && !constraintWidget.allowedInBarrier()) continue;
                n4 = n2;
                n3 = n;
                if (n == 0) {
                    n3 = this.mBarrierType;
                    if (n3 == 0) {
                        n2 = constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT).getFinalValue();
                    } else if (n3 == 1) {
                        n2 = constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).getFinalValue();
                    } else if (n3 == 2) {
                        n2 = constraintWidget.getAnchor(ConstraintAnchor.Type.TOP).getFinalValue();
                    } else if (n3 == 3) {
                        n2 = constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).getFinalValue();
                    }
                    n3 = 1;
                    n4 = n2;
                }
                if ((n5 = this.mBarrierType) == 0) {
                    n2 = Math.min(n4, constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT).getFinalValue());
                    n = n3;
                    continue;
                }
                if (n5 == 1) {
                    n2 = Math.max(n4, constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).getFinalValue());
                    n = n3;
                    continue;
                }
                if (n5 == 2) {
                    n2 = Math.min(n4, constraintWidget.getAnchor(ConstraintAnchor.Type.TOP).getFinalValue());
                    n = n3;
                    continue;
                }
                n2 = n4;
                n = n3;
                if (n5 != 3) continue;
                n2 = Math.max(n4, constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).getFinalValue());
                n = n3;
            }
            n3 = n2 + this.mMargin;
            n2 = this.mBarrierType;
            if (n2 != 0 && n2 != 1) {
                this.setFinalVertical(n3, n3);
            } else {
                this.setFinalHorizontal(n3, n3);
            }
            this.resolved = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean allowedInBarrier() {
        return true;
    }

    public boolean allowsGoneWidget() {
        return this.mAllowsGoneWidget;
    }

    @Override
    public void copy(ConstraintWidget constraintWidget, HashMap<ConstraintWidget, ConstraintWidget> hashMap) {
        super.copy(constraintWidget, hashMap);
        constraintWidget = (Barrier)constraintWidget;
        this.mBarrierType = ((Barrier)constraintWidget).mBarrierType;
        this.mAllowsGoneWidget = ((Barrier)constraintWidget).mAllowsGoneWidget;
        this.mMargin = ((Barrier)constraintWidget).mMargin;
    }

    public int getBarrierType() {
        return this.mBarrierType;
    }

    public int getMargin() {
        return this.mMargin;
    }

    public int getOrientation() {
        switch (this.mBarrierType) {
            default: {
                return -1;
            }
            case 2: 
            case 3: {
                return 1;
            }
            case 0: 
            case 1: 
        }
        return 0;
    }

    @Override
    public boolean isResolvedHorizontally() {
        return this.resolved;
    }

    @Override
    public boolean isResolvedVertically() {
        return this.resolved;
    }

    protected void markWidgets() {
        for (int i = 0; i < this.mWidgetsCount; ++i) {
            ConstraintWidget constraintWidget = this.mWidgets[i];
            int n = this.mBarrierType;
            if (n != 0 && n != 1) {
                if (n != 2 && n != 3) continue;
                constraintWidget.setInBarrier(1, true);
                continue;
            }
            constraintWidget.setInBarrier(0, true);
        }
    }

    public void setAllowsGoneWidget(boolean bl) {
        this.mAllowsGoneWidget = bl;
    }

    public void setBarrierType(int n) {
        this.mBarrierType = n;
    }

    public void setMargin(int n) {
        this.mMargin = n;
    }

    @Override
    public String toString() {
        CharSequence charSequence;
        CharSequence charSequence2 = new StringBuilder();
        ((StringBuilder)charSequence2).append("[Barrier] ");
        ((StringBuilder)charSequence2).append(this.getDebugName());
        ((StringBuilder)charSequence2).append(" {");
        charSequence2 = ((StringBuilder)charSequence2).toString();
        for (int i = 0; i < this.mWidgetsCount; ++i) {
            ConstraintWidget constraintWidget = this.mWidgets[i];
            charSequence = charSequence2;
            if (i > 0) {
                charSequence = new StringBuilder();
                charSequence.append((String)charSequence2);
                charSequence.append(", ");
                charSequence = charSequence.toString();
            }
            charSequence2 = new StringBuilder();
            ((StringBuilder)charSequence2).append((String)charSequence);
            ((StringBuilder)charSequence2).append(constraintWidget.getDebugName());
            charSequence2 = ((StringBuilder)charSequence2).toString();
        }
        charSequence = new StringBuilder();
        charSequence.append((String)charSequence2);
        charSequence.append("}");
        return charSequence.toString();
    }
}

