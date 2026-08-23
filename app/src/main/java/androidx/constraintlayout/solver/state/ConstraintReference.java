/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.state;

import androidx.constraintlayout.solver.state.Dimension;
import androidx.constraintlayout.solver.state.Reference;
import androidx.constraintlayout.solver.state.State;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import java.util.ArrayList;

public class ConstraintReference
implements Reference {
    private Object key;
    Object mBaselineToBaseline = null;
    Object mBottomToBottom = null;
    Object mBottomToTop = null;
    private ConstraintWidget mConstraintWidget;
    Object mEndToEnd = null;
    Object mEndToStart = null;
    float mHorizontalBias = 0.5f;
    int mHorizontalChainStyle = 0;
    Dimension mHorizontalDimension = Dimension.Fixed(Dimension.WRAP_DIMENSION);
    State.Constraint mLast = null;
    Object mLeftToLeft = null;
    Object mLeftToRight = null;
    int mMarginBottom = 0;
    int mMarginBottomGone = 0;
    int mMarginEnd = 0;
    int mMarginEndGone = 0;
    int mMarginLeft = 0;
    int mMarginLeftGone = 0;
    int mMarginRight = 0;
    int mMarginRightGone = 0;
    int mMarginStart = 0;
    int mMarginStartGone = 0;
    int mMarginTop = 0;
    int mMarginTopGone = 0;
    Object mRightToLeft = null;
    Object mRightToRight = null;
    Object mStartToEnd = null;
    Object mStartToStart = null;
    final State mState;
    Object mTopToBottom = null;
    Object mTopToTop = null;
    float mVerticalBias = 0.5f;
    int mVerticalChainStyle = 0;
    Dimension mVerticalDimension = Dimension.Fixed(Dimension.WRAP_DIMENSION);
    private Object mView;

    public ConstraintReference(State state) {
        this.mState = state;
    }

    private void applyConnection(ConstraintWidget constraintWidget, Object object, State.Constraint constraint) {
        if ((object = this.getTarget(object)) == null) {
            return;
        }
        int n = 1.$SwitchMap$androidx$constraintlayout$solver$state$State$Constraint[constraint.ordinal()];
        switch (1.$SwitchMap$androidx$constraintlayout$solver$state$State$Constraint[constraint.ordinal()]) {
            default: {
                break;
            }
            case 13: {
                constraintWidget.immediateConnect(ConstraintAnchor.Type.BASELINE, (ConstraintWidget)object, ConstraintAnchor.Type.BASELINE, 0, 0);
                break;
            }
            case 12: {
                constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).connect(((ConstraintWidget)object).getAnchor(ConstraintAnchor.Type.BOTTOM), this.mMarginBottom, this.mMarginBottomGone, false);
                break;
            }
            case 11: {
                constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).connect(((ConstraintWidget)object).getAnchor(ConstraintAnchor.Type.TOP), this.mMarginBottom, this.mMarginBottomGone, false);
                break;
            }
            case 10: {
                constraintWidget.getAnchor(ConstraintAnchor.Type.TOP).connect(((ConstraintWidget)object).getAnchor(ConstraintAnchor.Type.BOTTOM), this.mMarginTop, this.mMarginTopGone, false);
                break;
            }
            case 9: {
                constraintWidget.getAnchor(ConstraintAnchor.Type.TOP).connect(((ConstraintWidget)object).getAnchor(ConstraintAnchor.Type.TOP), this.mMarginTop, this.mMarginTopGone, false);
                break;
            }
            case 8: {
                constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).connect(((ConstraintWidget)object).getAnchor(ConstraintAnchor.Type.RIGHT), this.mMarginEnd, this.mMarginEndGone, false);
                break;
            }
            case 7: {
                constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).connect(((ConstraintWidget)object).getAnchor(ConstraintAnchor.Type.LEFT), this.mMarginEnd, this.mMarginEndGone, false);
                break;
            }
            case 6: {
                constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT).connect(((ConstraintWidget)object).getAnchor(ConstraintAnchor.Type.RIGHT), this.mMarginStart, this.mMarginStartGone, false);
                break;
            }
            case 5: {
                constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT).connect(((ConstraintWidget)object).getAnchor(ConstraintAnchor.Type.LEFT), this.mMarginStart, this.mMarginStartGone, false);
                break;
            }
            case 4: {
                constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).connect(((ConstraintWidget)object).getAnchor(ConstraintAnchor.Type.RIGHT), this.mMarginRight, this.mMarginRightGone, false);
                break;
            }
            case 3: {
                constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).connect(((ConstraintWidget)object).getAnchor(ConstraintAnchor.Type.LEFT), this.mMarginRight, this.mMarginRightGone, false);
                break;
            }
            case 2: {
                constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT).connect(((ConstraintWidget)object).getAnchor(ConstraintAnchor.Type.RIGHT), this.mMarginLeft, this.mMarginLeftGone, false);
                break;
            }
            case 1: {
                constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT).connect(((ConstraintWidget)object).getAnchor(ConstraintAnchor.Type.LEFT), this.mMarginLeft, this.mMarginLeftGone, false);
            }
        }
    }

    private void dereference() {
        this.mLeftToLeft = this.get(this.mLeftToLeft);
        this.mLeftToRight = this.get(this.mLeftToRight);
        this.mRightToLeft = this.get(this.mRightToLeft);
        this.mRightToRight = this.get(this.mRightToRight);
        this.mStartToStart = this.get(this.mStartToStart);
        this.mStartToEnd = this.get(this.mStartToEnd);
        this.mEndToStart = this.get(this.mEndToStart);
        this.mEndToEnd = this.get(this.mEndToEnd);
        this.mTopToTop = this.get(this.mTopToTop);
        this.mTopToBottom = this.get(this.mTopToBottom);
        this.mBottomToTop = this.get(this.mBottomToTop);
        this.mBottomToBottom = this.get(this.mBottomToBottom);
        this.mBaselineToBaseline = this.get(this.mBaselineToBaseline);
    }

    private Object get(Object object) {
        if (object == null) {
            return null;
        }
        if (!(object instanceof ConstraintReference)) {
            return this.mState.reference(object);
        }
        return object;
    }

    private ConstraintWidget getTarget(Object object) {
        if (object instanceof Reference) {
            return ((Reference)object).getConstraintWidget();
        }
        return null;
    }

    @Override
    public void apply() {
        ConstraintWidget constraintWidget = this.mConstraintWidget;
        if (constraintWidget == null) {
            return;
        }
        this.mHorizontalDimension.apply(this.mState, constraintWidget, 0);
        this.mVerticalDimension.apply(this.mState, this.mConstraintWidget, 1);
        this.dereference();
        this.applyConnection(this.mConstraintWidget, this.mLeftToLeft, State.Constraint.LEFT_TO_LEFT);
        this.applyConnection(this.mConstraintWidget, this.mLeftToRight, State.Constraint.LEFT_TO_RIGHT);
        this.applyConnection(this.mConstraintWidget, this.mRightToLeft, State.Constraint.RIGHT_TO_LEFT);
        this.applyConnection(this.mConstraintWidget, this.mRightToRight, State.Constraint.RIGHT_TO_RIGHT);
        this.applyConnection(this.mConstraintWidget, this.mStartToStart, State.Constraint.START_TO_START);
        this.applyConnection(this.mConstraintWidget, this.mStartToEnd, State.Constraint.START_TO_END);
        this.applyConnection(this.mConstraintWidget, this.mEndToStart, State.Constraint.END_TO_START);
        this.applyConnection(this.mConstraintWidget, this.mEndToEnd, State.Constraint.END_TO_END);
        this.applyConnection(this.mConstraintWidget, this.mTopToTop, State.Constraint.TOP_TO_TOP);
        this.applyConnection(this.mConstraintWidget, this.mTopToBottom, State.Constraint.TOP_TO_BOTTOM);
        this.applyConnection(this.mConstraintWidget, this.mBottomToTop, State.Constraint.BOTTOM_TO_TOP);
        this.applyConnection(this.mConstraintWidget, this.mBottomToBottom, State.Constraint.BOTTOM_TO_BOTTOM);
        this.applyConnection(this.mConstraintWidget, this.mBaselineToBaseline, State.Constraint.BASELINE_TO_BASELINE);
        int n = this.mHorizontalChainStyle;
        if (n != 0) {
            this.mConstraintWidget.setHorizontalChainStyle(n);
        }
        if ((n = this.mVerticalChainStyle) != 0) {
            this.mConstraintWidget.setVerticalChainStyle(n);
        }
        this.mConstraintWidget.setHorizontalBiasPercent(this.mHorizontalBias);
        this.mConstraintWidget.setVerticalBiasPercent(this.mVerticalBias);
    }

    public ConstraintReference baseline() {
        this.mLast = State.Constraint.BASELINE_TO_BASELINE;
        return this;
    }

    public ConstraintReference baselineToBaseline(Object object) {
        this.mLast = State.Constraint.BASELINE_TO_BASELINE;
        this.mBaselineToBaseline = object;
        return this;
    }

    public ConstraintReference bias(float f) {
        if (this.mLast == null) {
            return this;
        }
        switch (1.$SwitchMap$androidx$constraintlayout$solver$state$State$Constraint[this.mLast.ordinal()]) {
            default: {
                break;
            }
            case 9: 
            case 10: 
            case 11: 
            case 12: 
            case 15: {
                this.mVerticalBias = f;
                break;
            }
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 14: {
                this.mHorizontalBias = f;
            }
        }
        return this;
    }

    public ConstraintReference bottom() {
        this.mLast = this.mBottomToTop != null ? State.Constraint.BOTTOM_TO_TOP : State.Constraint.BOTTOM_TO_BOTTOM;
        return this;
    }

    public ConstraintReference bottomToBottom(Object object) {
        this.mLast = State.Constraint.BOTTOM_TO_BOTTOM;
        this.mBottomToBottom = object;
        return this;
    }

    public ConstraintReference bottomToTop(Object object) {
        this.mLast = State.Constraint.BOTTOM_TO_TOP;
        this.mBottomToTop = object;
        return this;
    }

    public ConstraintReference centerHorizontally(Object object) {
        this.mStartToStart = object = this.get(object);
        this.mEndToEnd = object;
        this.mLast = State.Constraint.CENTER_HORIZONTALLY;
        this.mHorizontalBias = 0.5f;
        return this;
    }

    public ConstraintReference centerVertically(Object object) {
        this.mTopToTop = object = this.get(object);
        this.mBottomToBottom = object;
        this.mLast = State.Constraint.CENTER_VERTICALLY;
        this.mVerticalBias = 0.5f;
        return this;
    }

    public ConstraintReference clear() {
        if (this.mLast != null) {
            switch (1.$SwitchMap$androidx$constraintlayout$solver$state$State$Constraint[this.mLast.ordinal()]) {
                default: {
                    break;
                }
                case 13: {
                    this.mBaselineToBaseline = null;
                    break;
                }
                case 11: 
                case 12: {
                    this.mBottomToTop = null;
                    this.mBottomToBottom = null;
                    this.mMarginBottom = 0;
                    this.mMarginBottomGone = 0;
                    break;
                }
                case 9: 
                case 10: {
                    this.mTopToTop = null;
                    this.mTopToBottom = null;
                    this.mMarginTop = 0;
                    this.mMarginTopGone = 0;
                    break;
                }
                case 7: 
                case 8: {
                    this.mEndToStart = null;
                    this.mEndToEnd = null;
                    this.mMarginEnd = 0;
                    this.mMarginEndGone = 0;
                    break;
                }
                case 5: 
                case 6: {
                    this.mStartToStart = null;
                    this.mStartToEnd = null;
                    this.mMarginStart = 0;
                    this.mMarginStartGone = 0;
                    break;
                }
                case 3: 
                case 4: {
                    this.mRightToLeft = null;
                    this.mRightToRight = null;
                    this.mMarginRight = 0;
                    this.mMarginRightGone = 0;
                    break;
                }
                case 1: 
                case 2: {
                    this.mLeftToLeft = null;
                    this.mLeftToRight = null;
                    this.mMarginLeft = 0;
                    this.mMarginLeftGone = 0;
                    break;
                }
            }
        } else {
            this.mLeftToLeft = null;
            this.mLeftToRight = null;
            this.mMarginLeft = 0;
            this.mRightToLeft = null;
            this.mRightToRight = null;
            this.mMarginRight = 0;
            this.mStartToStart = null;
            this.mStartToEnd = null;
            this.mMarginStart = 0;
            this.mEndToStart = null;
            this.mEndToEnd = null;
            this.mMarginEnd = 0;
            this.mTopToTop = null;
            this.mTopToBottom = null;
            this.mMarginTop = 0;
            this.mBottomToTop = null;
            this.mBottomToBottom = null;
            this.mMarginBottom = 0;
            this.mBaselineToBaseline = null;
            this.mHorizontalBias = 0.5f;
            this.mVerticalBias = 0.5f;
            this.mMarginLeftGone = 0;
            this.mMarginRightGone = 0;
            this.mMarginStartGone = 0;
            this.mMarginEndGone = 0;
            this.mMarginTopGone = 0;
            this.mMarginBottomGone = 0;
        }
        return this;
    }

    public ConstraintReference clearHorizontal() {
        this.start().clear();
        this.end().clear();
        this.left().clear();
        this.right().clear();
        return this;
    }

    public ConstraintReference clearVertical() {
        this.top().clear();
        this.baseline().clear();
        this.bottom().clear();
        return this;
    }

    public ConstraintWidget createConstraintWidget() {
        return new ConstraintWidget(this.getWidth().getValue(), this.getHeight().getValue());
    }

    public ConstraintReference end() {
        this.mLast = this.mEndToStart != null ? State.Constraint.END_TO_START : State.Constraint.END_TO_END;
        return this;
    }

    public ConstraintReference endToEnd(Object object) {
        this.mLast = State.Constraint.END_TO_END;
        this.mEndToEnd = object;
        return this;
    }

    public ConstraintReference endToStart(Object object) {
        this.mLast = State.Constraint.END_TO_START;
        this.mEndToStart = object;
        return this;
    }

    @Override
    public ConstraintWidget getConstraintWidget() {
        if (this.mConstraintWidget == null) {
            ConstraintWidget constraintWidget;
            this.mConstraintWidget = constraintWidget = this.createConstraintWidget();
            constraintWidget.setCompanionWidget(this.mView);
        }
        return this.mConstraintWidget;
    }

    public Dimension getHeight() {
        return this.mVerticalDimension;
    }

    public int getHorizontalChainStyle() {
        return this.mHorizontalChainStyle;
    }

    @Override
    public Object getKey() {
        return this.key;
    }

    public int getVerticalChainStyle(int n) {
        return this.mVerticalChainStyle;
    }

    public Object getView() {
        return this.mView;
    }

    public Dimension getWidth() {
        return this.mHorizontalDimension;
    }

    public ConstraintReference height(Dimension dimension) {
        return this.setHeight(dimension);
    }

    public ConstraintReference horizontalBias(float f) {
        this.mHorizontalBias = f;
        return this;
    }

    public ConstraintReference left() {
        this.mLast = this.mLeftToLeft != null ? State.Constraint.LEFT_TO_LEFT : State.Constraint.LEFT_TO_RIGHT;
        return this;
    }

    public ConstraintReference leftToLeft(Object object) {
        this.mLast = State.Constraint.LEFT_TO_LEFT;
        this.mLeftToLeft = object;
        return this;
    }

    public ConstraintReference leftToRight(Object object) {
        this.mLast = State.Constraint.LEFT_TO_RIGHT;
        this.mLeftToRight = object;
        return this;
    }

    public ConstraintReference margin(int n) {
        if (this.mLast != null) {
            switch (1.$SwitchMap$androidx$constraintlayout$solver$state$State$Constraint[this.mLast.ordinal()]) {
                default: {
                    break;
                }
                case 11: 
                case 12: {
                    this.mMarginBottom = n;
                    break;
                }
                case 9: 
                case 10: {
                    this.mMarginTop = n;
                    break;
                }
                case 7: 
                case 8: {
                    this.mMarginEnd = n;
                    break;
                }
                case 5: 
                case 6: {
                    this.mMarginStart = n;
                    break;
                }
                case 3: 
                case 4: {
                    this.mMarginRight = n;
                    break;
                }
                case 1: 
                case 2: {
                    this.mMarginLeft = n;
                    break;
                }
            }
        } else {
            this.mMarginLeft = n;
            this.mMarginRight = n;
            this.mMarginStart = n;
            this.mMarginEnd = n;
            this.mMarginTop = n;
            this.mMarginBottom = n;
        }
        return this;
    }

    public ConstraintReference margin(Object object) {
        return this.margin(this.mState.convertDimension(object));
    }

    public ConstraintReference marginGone(int n) {
        if (this.mLast != null) {
            switch (1.$SwitchMap$androidx$constraintlayout$solver$state$State$Constraint[this.mLast.ordinal()]) {
                default: {
                    break;
                }
                case 11: 
                case 12: {
                    this.mMarginBottomGone = n;
                    break;
                }
                case 9: 
                case 10: {
                    this.mMarginTopGone = n;
                    break;
                }
                case 7: 
                case 8: {
                    this.mMarginEndGone = n;
                    break;
                }
                case 5: 
                case 6: {
                    this.mMarginStartGone = n;
                    break;
                }
                case 3: 
                case 4: {
                    this.mMarginRightGone = n;
                    break;
                }
                case 1: 
                case 2: {
                    this.mMarginLeftGone = n;
                    break;
                }
            }
        } else {
            this.mMarginLeftGone = n;
            this.mMarginRightGone = n;
            this.mMarginStartGone = n;
            this.mMarginEndGone = n;
            this.mMarginTopGone = n;
            this.mMarginBottomGone = n;
        }
        return this;
    }

    public ConstraintReference right() {
        this.mLast = this.mRightToLeft != null ? State.Constraint.RIGHT_TO_LEFT : State.Constraint.RIGHT_TO_RIGHT;
        return this;
    }

    public ConstraintReference rightToLeft(Object object) {
        this.mLast = State.Constraint.RIGHT_TO_LEFT;
        this.mRightToLeft = object;
        return this;
    }

    public ConstraintReference rightToRight(Object object) {
        this.mLast = State.Constraint.RIGHT_TO_RIGHT;
        this.mRightToRight = object;
        return this;
    }

    @Override
    public void setConstraintWidget(ConstraintWidget constraintWidget) {
        if (constraintWidget == null) {
            return;
        }
        this.mConstraintWidget = constraintWidget;
        constraintWidget.setCompanionWidget(this.mView);
    }

    public ConstraintReference setHeight(Dimension dimension) {
        this.mVerticalDimension = dimension;
        return this;
    }

    public void setHorizontalChainStyle(int n) {
        this.mHorizontalChainStyle = n;
    }

    @Override
    public void setKey(Object object) {
        this.key = object;
    }

    public void setVerticalChainStyle(int n) {
        this.mVerticalChainStyle = n;
    }

    public void setView(Object object) {
        this.mView = object;
        ConstraintWidget constraintWidget = this.mConstraintWidget;
        if (constraintWidget != null) {
            constraintWidget.setCompanionWidget(object);
        }
    }

    public ConstraintReference setWidth(Dimension dimension) {
        this.mHorizontalDimension = dimension;
        return this;
    }

    public ConstraintReference start() {
        this.mLast = this.mStartToStart != null ? State.Constraint.START_TO_START : State.Constraint.START_TO_END;
        return this;
    }

    public ConstraintReference startToEnd(Object object) {
        this.mLast = State.Constraint.START_TO_END;
        this.mStartToEnd = object;
        return this;
    }

    public ConstraintReference startToStart(Object object) {
        this.mLast = State.Constraint.START_TO_START;
        this.mStartToStart = object;
        return this;
    }

    public ConstraintReference top() {
        this.mLast = this.mTopToTop != null ? State.Constraint.TOP_TO_TOP : State.Constraint.TOP_TO_BOTTOM;
        return this;
    }

    public ConstraintReference topToBottom(Object object) {
        this.mLast = State.Constraint.TOP_TO_BOTTOM;
        this.mTopToBottom = object;
        return this;
    }

    public ConstraintReference topToTop(Object object) {
        this.mLast = State.Constraint.TOP_TO_TOP;
        this.mTopToTop = object;
        return this;
    }

    public void validate() throws IncorrectConstraintException {
        ArrayList<String> arrayList = new ArrayList<String>();
        if (this.mLeftToLeft != null && this.mLeftToRight != null) {
            arrayList.add("LeftToLeft and LeftToRight both defined");
        }
        if (this.mRightToLeft != null && this.mRightToRight != null) {
            arrayList.add("RightToLeft and RightToRight both defined");
        }
        if (this.mStartToStart != null && this.mStartToEnd != null) {
            arrayList.add("StartToStart and StartToEnd both defined");
        }
        if (this.mEndToStart != null && this.mEndToEnd != null) {
            arrayList.add("EndToStart and EndToEnd both defined");
        }
        if (!(this.mLeftToLeft == null && this.mLeftToRight == null && this.mRightToLeft == null && this.mRightToRight == null || this.mStartToStart == null && this.mStartToEnd == null && this.mEndToStart == null && this.mEndToEnd == null)) {
            arrayList.add("Both left/right and start/end constraints defined");
        }
        if (arrayList.size() <= 0) {
            return;
        }
        throw new IncorrectConstraintException(this, arrayList);
    }

    public ConstraintReference verticalBias(float f) {
        this.mVerticalBias = f;
        return this;
    }

    public ConstraintReference width(Dimension dimension) {
        return this.setWidth(dimension);
    }

    public static interface ConstraintReferenceFactory {
        public ConstraintReference create(State var1);
    }

    class IncorrectConstraintException
    extends Exception {
        private final ArrayList<String> mErrors;
        final ConstraintReference this$0;

        public IncorrectConstraintException(ConstraintReference constraintReference, ArrayList<String> arrayList) {
            this.this$0 = constraintReference;
            this.mErrors = arrayList;
        }

        public ArrayList<String> getErrors() {
            return this.mErrors;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("IncorrectConstraintException: ");
            stringBuilder.append(this.mErrors.toString());
            return stringBuilder.toString();
        }
    }
}

