/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import java.util.HashMap;

public class Guideline
extends ConstraintWidget {
    public static final int HORIZONTAL = 0;
    public static final int RELATIVE_BEGIN = 1;
    public static final int RELATIVE_END = 2;
    public static final int RELATIVE_PERCENT = 0;
    public static final int RELATIVE_UNKNWON = -1;
    public static final int VERTICAL = 1;
    private ConstraintAnchor mAnchor = this.mTop;
    private int mMinimumPosition = 0;
    private int mOrientation = 0;
    protected int mRelativeBegin = -1;
    protected int mRelativeEnd = -1;
    protected float mRelativePercent = -1.0f;
    private boolean resolved;

    public Guideline() {
        this.mAnchors.clear();
        this.mAnchors.add(this.mAnchor);
        int n = this.mListAnchors.length;
        for (int i = 0; i < n; ++i) {
            this.mListAnchors[i] = this.mAnchor;
        }
    }

    @Override
    public void addToSolver(LinearSystem linearSystem, boolean bl) {
        block8: {
            Object object;
            block9: {
                boolean bl2;
                ConstraintAnchor constraintAnchor;
                Object object2;
                block7: {
                    object2 = (ConstraintWidgetContainer)this.getParent();
                    if (object2 == null) {
                        return;
                    }
                    constraintAnchor = ((ConstraintWidget)object2).getAnchor(ConstraintAnchor.Type.LEFT);
                    object = ((ConstraintWidget)object2).getAnchor(ConstraintAnchor.Type.RIGHT);
                    ConstraintWidget constraintWidget = this.mParent;
                    boolean bl3 = true;
                    bl2 = constraintWidget != null && this.mParent.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                    if (this.mOrientation == 0) {
                        constraintAnchor = ((ConstraintWidget)object2).getAnchor(ConstraintAnchor.Type.TOP);
                        object = ((ConstraintWidget)object2).getAnchor(ConstraintAnchor.Type.BOTTOM);
                        bl2 = this.mParent != null && this.mParent.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT ? bl3 : false;
                    }
                    if (this.resolved && this.mAnchor.hasFinalValue()) {
                        object2 = linearSystem.createObjectVariable(this.mAnchor);
                        linearSystem.addEquality((SolverVariable)object2, this.mAnchor.getFinalValue());
                        if (this.mRelativeBegin != -1) {
                            if (bl2) {
                                linearSystem.addGreaterThan(linearSystem.createObjectVariable(object), (SolverVariable)object2, 0, 5);
                            }
                        } else if (this.mRelativeEnd != -1 && bl2) {
                            object = linearSystem.createObjectVariable(object);
                            linearSystem.addGreaterThan((SolverVariable)object2, linearSystem.createObjectVariable(constraintAnchor), 0, 5);
                            linearSystem.addGreaterThan((SolverVariable)object, (SolverVariable)object2, 0, 5);
                        }
                        this.resolved = false;
                        return;
                    }
                    if (this.mRelativeBegin == -1) break block7;
                    object2 = linearSystem.createObjectVariable(this.mAnchor);
                    linearSystem.addEquality((SolverVariable)object2, linearSystem.createObjectVariable(constraintAnchor), this.mRelativeBegin, 8);
                    if (!bl2) break block8;
                    linearSystem.addGreaterThan(linearSystem.createObjectVariable(object), (SolverVariable)object2, 0, 5);
                    break block8;
                }
                if (this.mRelativeEnd == -1) break block9;
                object2 = linearSystem.createObjectVariable(this.mAnchor);
                object = linearSystem.createObjectVariable(object);
                linearSystem.addEquality((SolverVariable)object2, (SolverVariable)object, -this.mRelativeEnd, 8);
                if (!bl2) break block8;
                linearSystem.addGreaterThan((SolverVariable)object2, linearSystem.createObjectVariable(constraintAnchor), 0, 5);
                linearSystem.addGreaterThan((SolverVariable)object, (SolverVariable)object2, 0, 5);
                break block8;
            }
            if (this.mRelativePercent == -1.0f) break block8;
            linearSystem.addConstraint(LinearSystem.createRowDimensionPercent(linearSystem, linearSystem.createObjectVariable(this.mAnchor), linearSystem.createObjectVariable(object), this.mRelativePercent));
        }
    }

    @Override
    public boolean allowedInBarrier() {
        return true;
    }

    @Override
    public void copy(ConstraintWidget constraintWidget, HashMap<ConstraintWidget, ConstraintWidget> hashMap) {
        super.copy(constraintWidget, hashMap);
        constraintWidget = (Guideline)constraintWidget;
        this.mRelativePercent = ((Guideline)constraintWidget).mRelativePercent;
        this.mRelativeBegin = ((Guideline)constraintWidget).mRelativeBegin;
        this.mRelativeEnd = ((Guideline)constraintWidget).mRelativeEnd;
        this.setOrientation(((Guideline)constraintWidget).mOrientation);
    }

    public void cyclePosition() {
        if (this.mRelativeBegin != -1) {
            this.inferRelativePercentPosition();
        } else if (this.mRelativePercent != -1.0f) {
            this.inferRelativeEndPosition();
        } else if (this.mRelativeEnd != -1) {
            this.inferRelativeBeginPosition();
        }
    }

    public ConstraintAnchor getAnchor() {
        return this.mAnchor;
    }

    @Override
    public ConstraintAnchor getAnchor(ConstraintAnchor.Type type) {
        switch (1.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[type.ordinal()]) {
            default: {
                break;
            }
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 9: {
                return null;
            }
            case 3: 
            case 4: {
                if (this.mOrientation != 0) break;
                return this.mAnchor;
            }
            case 1: 
            case 2: {
                if (this.mOrientation != 1) break;
                return this.mAnchor;
            }
        }
        throw new AssertionError((Object)type.name());
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public int getRelativeBegin() {
        return this.mRelativeBegin;
    }

    public int getRelativeBehaviour() {
        if (this.mRelativePercent != -1.0f) {
            return 0;
        }
        if (this.mRelativeBegin != -1) {
            return 1;
        }
        if (this.mRelativeEnd != -1) {
            return 2;
        }
        return -1;
    }

    public int getRelativeEnd() {
        return this.mRelativeEnd;
    }

    public float getRelativePercent() {
        return this.mRelativePercent;
    }

    @Override
    public String getType() {
        return "Guideline";
    }

    void inferRelativeBeginPosition() {
        int n = this.getX();
        if (this.mOrientation == 0) {
            n = this.getY();
        }
        this.setGuideBegin(n);
    }

    void inferRelativeEndPosition() {
        int n = this.getParent().getWidth() - this.getX();
        if (this.mOrientation == 0) {
            n = this.getParent().getHeight() - this.getY();
        }
        this.setGuideEnd(n);
    }

    void inferRelativePercentPosition() {
        float f = (float)this.getX() / (float)this.getParent().getWidth();
        if (this.mOrientation == 0) {
            f = (float)this.getY() / (float)this.getParent().getHeight();
        }
        this.setGuidePercent(f);
    }

    public boolean isPercent() {
        boolean bl = this.mRelativePercent != -1.0f && this.mRelativeBegin == -1 && this.mRelativeEnd == -1;
        return bl;
    }

    @Override
    public boolean isResolvedHorizontally() {
        return this.resolved;
    }

    @Override
    public boolean isResolvedVertically() {
        return this.resolved;
    }

    public void setFinalValue(int n) {
        this.mAnchor.setFinalValue(n);
        this.resolved = true;
    }

    public void setGuideBegin(int n) {
        if (n > -1) {
            this.mRelativePercent = -1.0f;
            this.mRelativeBegin = n;
            this.mRelativeEnd = -1;
        }
    }

    public void setGuideEnd(int n) {
        if (n > -1) {
            this.mRelativePercent = -1.0f;
            this.mRelativeBegin = -1;
            this.mRelativeEnd = n;
        }
    }

    public void setGuidePercent(float f) {
        if (f > -1.0f) {
            this.mRelativePercent = f;
            this.mRelativeBegin = -1;
            this.mRelativeEnd = -1;
        }
    }

    public void setGuidePercent(int n) {
        this.setGuidePercent((float)n / 100.0f);
    }

    public void setMinimumPosition(int n) {
        this.mMinimumPosition = n;
    }

    public void setOrientation(int n) {
        if (this.mOrientation == n) {
            return;
        }
        this.mOrientation = n;
        this.mAnchors.clear();
        this.mAnchor = this.mOrientation == 1 ? this.mLeft : this.mTop;
        this.mAnchors.add(this.mAnchor);
        int n2 = this.mListAnchors.length;
        for (n = 0; n < n2; ++n) {
            this.mListAnchors[n] = this.mAnchor;
        }
    }

    @Override
    public void updateFromSolver(LinearSystem linearSystem, boolean bl) {
        if (this.getParent() == null) {
            return;
        }
        int n = linearSystem.getObjectVariableValue(this.mAnchor);
        if (this.mOrientation == 1) {
            this.setX(n);
            this.setY(0);
            this.setHeight(this.getParent().getHeight());
            this.setWidth(0);
        } else {
            this.setX(0);
            this.setY(n);
            this.setWidth(this.getParent().getWidth());
            this.setHeight(0);
        }
    }
}

