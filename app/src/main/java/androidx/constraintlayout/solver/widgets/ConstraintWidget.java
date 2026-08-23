/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.ArrayRow;
import androidx.constraintlayout.solver.Cache;
import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.Metrics;
import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.widgets.Barrier;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.solver.widgets.Guideline;
import androidx.constraintlayout.solver.widgets.Optimizer;
import androidx.constraintlayout.solver.widgets.VirtualLayout;
import androidx.constraintlayout.solver.widgets.analyzer.ChainRun;
import androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun;
import androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun;
import androidx.constraintlayout.solver.widgets.analyzer.WidgetRun;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class ConstraintWidget {
    public static final int ANCHOR_BASELINE = 4;
    public static final int ANCHOR_BOTTOM = 3;
    public static final int ANCHOR_LEFT = 0;
    public static final int ANCHOR_RIGHT = 1;
    public static final int ANCHOR_TOP = 2;
    private static final boolean AUTOTAG_CENTER = false;
    public static final int BOTH = 2;
    public static final int CHAIN_PACKED = 2;
    public static final int CHAIN_SPREAD = 0;
    public static final int CHAIN_SPREAD_INSIDE = 1;
    public static float DEFAULT_BIAS = 0.0f;
    static final int DIMENSION_HORIZONTAL = 0;
    static final int DIMENSION_VERTICAL = 1;
    protected static final int DIRECT = 2;
    public static final int GONE = 8;
    public static final int HORIZONTAL = 0;
    public static final int INVISIBLE = 4;
    public static final int MATCH_CONSTRAINT_PERCENT = 2;
    public static final int MATCH_CONSTRAINT_RATIO = 3;
    public static final int MATCH_CONSTRAINT_RATIO_RESOLVED = 4;
    public static final int MATCH_CONSTRAINT_SPREAD = 0;
    public static final int MATCH_CONSTRAINT_WRAP = 1;
    protected static final int SOLVER = 1;
    public static final int UNKNOWN = -1;
    private static final boolean USE_WRAP_DIMENSION_FOR_SPREAD = false;
    public static final int VERTICAL = 1;
    public static final int VISIBLE = 0;
    private static final int WRAP = -2;
    private boolean OPTIMIZE_WRAP = false;
    private boolean OPTIMIZE_WRAP_ON_RESOLVED = true;
    private boolean hasBaseline = false;
    public ChainRun horizontalChainRun;
    public int horizontalGroup;
    public HorizontalWidgetRun horizontalRun = null;
    private boolean inPlaceholder;
    public boolean[] isTerminalWidget;
    protected ArrayList<ConstraintAnchor> mAnchors;
    public ConstraintAnchor mBaseline;
    int mBaselineDistance;
    public ConstraintAnchor mBottom;
    boolean mBottomHasCentered;
    public ConstraintAnchor mCenter;
    ConstraintAnchor mCenterX;
    ConstraintAnchor mCenterY;
    private float mCircleConstraintAngle = 0.0f;
    private Object mCompanionWidget;
    private int mContainerItemSkip;
    private String mDebugName;
    public float mDimensionRatio;
    protected int mDimensionRatioSide;
    int mDistToBottom;
    int mDistToLeft;
    int mDistToRight;
    int mDistToTop;
    boolean mGroupsToSolver;
    int mHeight;
    float mHorizontalBiasPercent;
    boolean mHorizontalChainFixedPosition;
    int mHorizontalChainStyle;
    ConstraintWidget mHorizontalNextWidget;
    public int mHorizontalResolution = -1;
    boolean mHorizontalWrapVisited;
    private boolean mInVirtuaLayout = false;
    public boolean mIsHeightWrapContent;
    private boolean[] mIsInBarrier;
    public boolean mIsWidthWrapContent;
    private int mLastHorizontalMeasureSpec = 0;
    private int mLastVerticalMeasureSpec = 0;
    public ConstraintAnchor mLeft;
    boolean mLeftHasCentered;
    public ConstraintAnchor[] mListAnchors;
    public DimensionBehaviour[] mListDimensionBehaviors;
    protected ConstraintWidget[] mListNextMatchConstraintsWidget;
    public int mMatchConstraintDefaultHeight = 0;
    public int mMatchConstraintDefaultWidth = 0;
    public int mMatchConstraintMaxHeight = 0;
    public int mMatchConstraintMaxWidth = 0;
    public int mMatchConstraintMinHeight = 0;
    public int mMatchConstraintMinWidth = 0;
    public float mMatchConstraintPercentHeight = 1.0f;
    public float mMatchConstraintPercentWidth = 1.0f;
    private int[] mMaxDimension;
    private boolean mMeasureRequested = true;
    protected int mMinHeight;
    protected int mMinWidth;
    protected ConstraintWidget[] mNextChainWidget;
    protected int mOffsetX;
    protected int mOffsetY;
    public ConstraintWidget mParent;
    int mRelX;
    int mRelY;
    float mResolvedDimensionRatio = 1.0f;
    int mResolvedDimensionRatioSide = -1;
    boolean mResolvedHasRatio = false;
    public int[] mResolvedMatchConstraintDefault;
    public ConstraintAnchor mRight;
    boolean mRightHasCentered;
    public ConstraintAnchor mTop;
    boolean mTopHasCentered;
    private String mType;
    float mVerticalBiasPercent;
    boolean mVerticalChainFixedPosition;
    int mVerticalChainStyle;
    ConstraintWidget mVerticalNextWidget;
    public int mVerticalResolution = -1;
    boolean mVerticalWrapVisited;
    private int mVisibility;
    public float[] mWeight;
    int mWidth;
    protected int mX;
    protected int mY;
    public boolean measured = false;
    private boolean resolvedHorizontal = false;
    private boolean resolvedVertical = false;
    public WidgetRun[] run = new WidgetRun[2];
    public ChainRun verticalChainRun;
    public int verticalGroup;
    public VerticalWidgetRun verticalRun = null;

    static {
        DEFAULT_BIAS = 0.5f;
    }

    public ConstraintWidget() {
        float f;
        ConstraintAnchor constraintAnchor;
        this.isTerminalWidget = new boolean[]{true, true};
        this.mResolvedMatchConstraintDefault = new int[2];
        this.mMaxDimension = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE};
        this.mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
        this.mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
        this.mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
        this.mCenter = constraintAnchor = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.mListAnchors = new ConstraintAnchor[]{this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, constraintAnchor};
        this.mAnchors = new ArrayList();
        this.mIsInBarrier = new boolean[2];
        this.mListDimensionBehaviors = new DimensionBehaviour[]{DimensionBehaviour.FIXED, DimensionBehaviour.FIXED};
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mRelX = 0;
        this.mRelY = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mHorizontalBiasPercent = f = DEFAULT_BIAS;
        this.mVerticalBiasPercent = f;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mGroupsToSolver = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mWeight = new float[]{-1.0f, -1.0f};
        this.mListNextMatchConstraintsWidget = new ConstraintWidget[]{null, null};
        this.mNextChainWidget = new ConstraintWidget[]{null, null};
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        this.horizontalGroup = -1;
        this.verticalGroup = -1;
        this.addAnchors();
    }

    public ConstraintWidget(int n, int n2) {
        this(0, 0, n, n2);
    }

    public ConstraintWidget(int n, int n2, int n3, int n4) {
        float f;
        ConstraintAnchor constraintAnchor;
        this.isTerminalWidget = new boolean[]{true, true};
        this.mResolvedMatchConstraintDefault = new int[2];
        this.mMaxDimension = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE};
        this.mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
        this.mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
        this.mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
        this.mCenter = constraintAnchor = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.mListAnchors = new ConstraintAnchor[]{this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, constraintAnchor};
        this.mAnchors = new ArrayList();
        this.mIsInBarrier = new boolean[2];
        this.mListDimensionBehaviors = new DimensionBehaviour[]{DimensionBehaviour.FIXED, DimensionBehaviour.FIXED};
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mRelX = 0;
        this.mRelY = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mHorizontalBiasPercent = f = DEFAULT_BIAS;
        this.mVerticalBiasPercent = f;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mGroupsToSolver = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mWeight = new float[]{-1.0f, -1.0f};
        this.mListNextMatchConstraintsWidget = new ConstraintWidget[]{null, null};
        this.mNextChainWidget = new ConstraintWidget[]{null, null};
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        this.horizontalGroup = -1;
        this.verticalGroup = -1;
        this.mX = n;
        this.mY = n2;
        this.mWidth = n3;
        this.mHeight = n4;
        this.addAnchors();
    }

    public ConstraintWidget(String string2) {
        float f;
        ConstraintAnchor constraintAnchor;
        this.isTerminalWidget = new boolean[]{true, true};
        this.mResolvedMatchConstraintDefault = new int[2];
        this.mMaxDimension = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE};
        this.mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
        this.mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
        this.mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
        this.mCenter = constraintAnchor = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.mListAnchors = new ConstraintAnchor[]{this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, constraintAnchor};
        this.mAnchors = new ArrayList();
        this.mIsInBarrier = new boolean[2];
        this.mListDimensionBehaviors = new DimensionBehaviour[]{DimensionBehaviour.FIXED, DimensionBehaviour.FIXED};
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mRelX = 0;
        this.mRelY = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mHorizontalBiasPercent = f = DEFAULT_BIAS;
        this.mVerticalBiasPercent = f;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mGroupsToSolver = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mWeight = new float[]{-1.0f, -1.0f};
        this.mListNextMatchConstraintsWidget = new ConstraintWidget[]{null, null};
        this.mNextChainWidget = new ConstraintWidget[]{null, null};
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        this.horizontalGroup = -1;
        this.verticalGroup = -1;
        this.addAnchors();
        this.setDebugName(string2);
    }

    public ConstraintWidget(String string2, int n, int n2) {
        this(n, n2);
        this.setDebugName(string2);
    }

    public ConstraintWidget(String string2, int n, int n2, int n3, int n4) {
        this(n, n2, n3, n4);
        this.setDebugName(string2);
    }

    private void addAnchors() {
        this.mAnchors.add(this.mLeft);
        this.mAnchors.add(this.mTop);
        this.mAnchors.add(this.mRight);
        this.mAnchors.add(this.mBottom);
        this.mAnchors.add(this.mCenterX);
        this.mAnchors.add(this.mCenterY);
        this.mAnchors.add(this.mCenter);
        this.mAnchors.add(this.mBaseline);
    }

    private void applyConstraints(LinearSystem object, boolean bl, boolean bl2, boolean bl3, boolean bl4, SolverVariable object2, SolverVariable solverVariable, DimensionBehaviour object3, boolean bl5, ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, int n, int n2, int n3, int n4, float f, boolean bl6, boolean bl7, boolean bl8, boolean bl9, boolean bl10, int n5, int n6, int n7, int n8, float f2, boolean bl11) {
        int n9;
        SolverVariable solverVariable2;
        SolverVariable solverVariable3;
        block117: {
            SolverVariable solverVariable4;
            block118: {
                int n10;
                SolverVariable solverVariable5;
                block120: {
                    block123: {
                        block124: {
                            Object object4;
                            Object object5;
                            block121: {
                                block122: {
                                    int n11;
                                    int n12;
                                    boolean bl12;
                                    boolean bl13;
                                    block119: {
                                        solverVariable3 = ((LinearSystem)object).createObjectVariable(constraintAnchor);
                                        solverVariable2 = ((LinearSystem)object).createObjectVariable(constraintAnchor2);
                                        solverVariable5 = ((LinearSystem)object).createObjectVariable(constraintAnchor.getTarget());
                                        solverVariable4 = ((LinearSystem)object).createObjectVariable(constraintAnchor2.getTarget());
                                        if (LinearSystem.getMetrics() != null) {
                                            object5 = LinearSystem.getMetrics();
                                            ++((Metrics)object5).nonresolvedWidgets;
                                        }
                                        bl13 = constraintAnchor.isConnected();
                                        bl12 = constraintAnchor2.isConnected();
                                        boolean bl14 = this.mCenter.isConnected();
                                        n12 = 0;
                                        n10 = 0;
                                        if (bl13) {
                                            n10 = 0 + 1;
                                        }
                                        n9 = n10;
                                        if (bl12) {
                                            n9 = n10 + 1;
                                        }
                                        if (bl14) {
                                            ++n9;
                                        }
                                        n10 = bl6 ? 3 : n5;
                                        switch (1.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour[object3.ordinal()]) {
                                            default: {
                                                n5 = n12;
                                                break;
                                            }
                                            case 4: {
                                                if (n10 != 4) {
                                                    n5 = 1;
                                                    break;
                                                }
                                                n5 = 0;
                                                break;
                                            }
                                            case 3: {
                                                n5 = 0;
                                                break;
                                            }
                                            case 2: {
                                                n5 = 0;
                                                break;
                                            }
                                            case 1: {
                                                n5 = 0;
                                            }
                                        }
                                        if (this.mVisibility == 8) {
                                            n5 = 0;
                                            n2 = 0;
                                        } else {
                                            n12 = n2;
                                            n2 = n5;
                                            n5 = n12;
                                        }
                                        if (bl11) {
                                            if (!(bl13 || bl12 || bl14)) {
                                                ((LinearSystem)object).addEquality(solverVariable3, n);
                                            } else if (bl13 && !bl12) {
                                                ((LinearSystem)object).addEquality(solverVariable3, solverVariable5, constraintAnchor.getMargin(), 8);
                                            }
                                        }
                                        if (n2 == 0) {
                                            if (bl5) {
                                                ((LinearSystem)object).addEquality(solverVariable2, solverVariable3, 0, 3);
                                                if (n3 > 0) {
                                                    ((LinearSystem)object).addGreaterThan(solverVariable2, solverVariable3, n3, 8);
                                                }
                                                if (n4 < Integer.MAX_VALUE) {
                                                    ((LinearSystem)object).addLowerThan(solverVariable2, solverVariable3, n4, 8);
                                                }
                                            } else {
                                                ((LinearSystem)object).addEquality(solverVariable2, solverVariable3, n5, 8);
                                            }
                                            n = n8;
                                            n8 = n2;
                                        } else if (!(n9 == 2 || bl6 || n10 != 1 && n10 != 0)) {
                                            n = n2 = Math.max(n7, n5);
                                            if (n8 > 0) {
                                                n = Math.min(n8, n2);
                                            }
                                            ((LinearSystem)object).addEquality(solverVariable2, solverVariable3, n, 8);
                                            n = n8;
                                            n8 = 0;
                                        } else {
                                            n = n7 == -2 ? n5 : n7;
                                            n4 = n8 == -2 ? n5 : n8;
                                            n7 = n5;
                                            if (n5 > 0) {
                                                n7 = n5;
                                                if (n10 != 1) {
                                                    n7 = 0;
                                                }
                                            }
                                            n5 = n7;
                                            if (n > 0) {
                                                ((LinearSystem)object).addGreaterThan(solverVariable2, solverVariable3, n, 8);
                                                n5 = Math.max(n7, n);
                                            }
                                            n7 = n5;
                                            if (n4 > 0) {
                                                n7 = n8 = 1;
                                                if (bl2) {
                                                    n7 = n8;
                                                    if (n10 == 1) {
                                                        n7 = 0;
                                                    }
                                                }
                                                if (n7 != 0) {
                                                    ((LinearSystem)object).addLowerThan(solverVariable2, solverVariable3, n4, 8);
                                                }
                                                n7 = Math.min(n5, n4);
                                            }
                                            if (n10 == 1) {
                                                if (bl2) {
                                                    ((LinearSystem)object).addEquality(solverVariable2, solverVariable3, n7, 8);
                                                } else if (bl8) {
                                                    ((LinearSystem)object).addEquality(solverVariable2, solverVariable3, n7, 5);
                                                    ((LinearSystem)object).addLowerThan(solverVariable2, solverVariable3, n7, 8);
                                                } else {
                                                    ((LinearSystem)object).addEquality(solverVariable2, solverVariable3, n7, 5);
                                                    ((LinearSystem)object).addLowerThan(solverVariable2, solverVariable3, n7, 8);
                                                }
                                                n7 = n;
                                                n8 = n2;
                                                n = n4;
                                            } else if (n10 == 2) {
                                                if (constraintAnchor.getType() != ConstraintAnchor.Type.TOP && constraintAnchor.getType() != ConstraintAnchor.Type.BOTTOM) {
                                                    object5 = ((LinearSystem)object).createObjectVariable(this.mParent.getAnchor(ConstraintAnchor.Type.LEFT));
                                                    object3 = ((LinearSystem)object).createObjectVariable(this.mParent.getAnchor(ConstraintAnchor.Type.RIGHT));
                                                } else {
                                                    object5 = ((LinearSystem)object).createObjectVariable(this.mParent.getAnchor(ConstraintAnchor.Type.TOP));
                                                    object3 = ((LinearSystem)object).createObjectVariable(this.mParent.getAnchor(ConstraintAnchor.Type.BOTTOM));
                                                }
                                                object4 = ((LinearSystem)object).createRow();
                                                ((LinearSystem)object).addConstraint(((ArrayRow)object4).createRowDimensionRatio(solverVariable2, solverVariable3, (SolverVariable)object3, (SolverVariable)object5, f2));
                                                n8 = 0;
                                                n7 = n;
                                                n = n4;
                                            } else {
                                                n7 = n;
                                                bl4 = true;
                                                n = n4;
                                                n8 = n2;
                                            }
                                        }
                                        if (!bl11 || bl8) break block117;
                                        if (!bl13 && !bl12 && !bl14 || bl13 && !bl12) break block118;
                                        if (bl13 || !bl12) break block119;
                                        ((LinearSystem)object).addEquality(solverVariable2, solverVariable4, -constraintAnchor2.getMargin(), 8);
                                        if (bl2) {
                                            if (this.OPTIMIZE_WRAP && solverVariable3.isFinalValue && (object3 = this.mParent) != null) {
                                                object2 = (ConstraintWidgetContainer)object3;
                                                if (bl) {
                                                    ((ConstraintWidgetContainer)object2).addHorizontalWrapMinVariable(constraintAnchor);
                                                } else {
                                                    ((ConstraintWidgetContainer)object2).addVerticalWrapMinVariable(constraintAnchor);
                                                }
                                            } else {
                                                ((LinearSystem)object).addGreaterThan(solverVariable3, (SolverVariable)object2, 0, 5);
                                            }
                                        }
                                        break block118;
                                    }
                                    if (!bl13 || !bl12) break block118;
                                    n9 = 0;
                                    n2 = 0;
                                    n4 = 4;
                                    n12 = 6;
                                    n5 = 5;
                                    object5 = constraintAnchor.mTarget.mOwner;
                                    object4 = constraintAnchor2.mTarget.mOwner;
                                    object3 = this.getParent();
                                    if (n8 != 0) {
                                        if (n10 == 0) {
                                            if (n == 0 && n7 == 0) {
                                                n9 = 1;
                                                if (solverVariable5.isFinalValue && solverVariable4.isFinalValue) {
                                                    ((LinearSystem)object).addEquality(solverVariable3, solverVariable5, constraintAnchor.getMargin(), 8);
                                                    ((LinearSystem)object).addEquality(solverVariable2, solverVariable4, -constraintAnchor2.getMargin(), 8);
                                                    return;
                                                }
                                                n2 = 0;
                                                n5 = 8;
                                                n4 = 8;
                                                n = 0;
                                            } else {
                                                n6 = 1;
                                                n = 1;
                                                n5 = 5;
                                                n4 = 5;
                                                n9 = n2;
                                                n2 = n6;
                                            }
                                            if (!(object5 instanceof Barrier) && !(object4 instanceof Barrier)) {
                                                n6 = n12;
                                            } else {
                                                n4 = 4;
                                                n6 = n12;
                                            }
                                        } else if (n10 == 1) {
                                            n5 = 8;
                                            n2 = 1;
                                            n = 1;
                                            n6 = n12;
                                        } else if (n10 == 3) {
                                            if (this.mResolvedDimensionRatioSide == -1) {
                                                n9 = 1;
                                                n5 = 8;
                                                n4 = 5;
                                                if (bl9) {
                                                    n4 = 5;
                                                    n6 = 4;
                                                    if (bl2) {
                                                        n6 = 5;
                                                        n2 = 1;
                                                        n = 1;
                                                    } else {
                                                        n2 = 1;
                                                        n = 1;
                                                    }
                                                } else {
                                                    n6 = 8;
                                                    n2 = 1;
                                                    n = 1;
                                                }
                                            } else {
                                                n9 = 1;
                                                if (bl6) {
                                                    n = n6 != 2 && n6 != 1 ? 0 : 1;
                                                    if (n == 0) {
                                                        n5 = 8;
                                                        n4 = 5;
                                                    }
                                                    n2 = 1;
                                                    n = 1;
                                                    n6 = n12;
                                                } else {
                                                    n5 = 5;
                                                    if (n > 0) {
                                                        n4 = 5;
                                                        n2 = 1;
                                                        n = 1;
                                                        n6 = n12;
                                                    } else if (n == 0 && n7 == 0) {
                                                        if (!bl9) {
                                                            n4 = 8;
                                                            n2 = 1;
                                                            n = 1;
                                                            n6 = n12;
                                                        } else {
                                                            n5 = object5 != object3 && object4 != object3 ? 4 : 5;
                                                            n4 = 4;
                                                            n2 = 1;
                                                            n = 1;
                                                            n6 = n12;
                                                        }
                                                    } else {
                                                        n2 = 1;
                                                        n = 1;
                                                        n6 = n12;
                                                    }
                                                }
                                            }
                                        } else {
                                            n2 = 0;
                                            n = 0;
                                            n6 = n12;
                                        }
                                    } else {
                                        n2 = 1;
                                        if (solverVariable5.isFinalValue && solverVariable4.isFinalValue) {
                                            ((LinearSystem)object).addCentering(solverVariable3, solverVariable5, constraintAnchor.getMargin(), f, solverVariable4, solverVariable2, constraintAnchor2.getMargin(), 8);
                                            if (bl2 && bl4) {
                                                n = 0;
                                                if (constraintAnchor2.mTarget != null) {
                                                    n = constraintAnchor2.getMargin();
                                                }
                                                if (solverVariable4 != solverVariable) {
                                                    ((LinearSystem)object).addGreaterThan(solverVariable, solverVariable2, n, 5);
                                                }
                                            }
                                            return;
                                        }
                                        n = 1;
                                        n6 = n12;
                                    }
                                    if (n != 0 && solverVariable5 == solverVariable4 && object5 != object3) {
                                        n = 0;
                                        n12 = 0;
                                    } else {
                                        n11 = 1;
                                        n12 = n;
                                        n = n11;
                                    }
                                    if (n2 != 0) {
                                        if (n8 == 0 && !bl7 && !bl9 && solverVariable5 == object2 && solverVariable4 == solverVariable) {
                                            n = 0;
                                            n6 = 8;
                                            n5 = 8;
                                            bl2 = false;
                                        }
                                        n2 = constraintAnchor.getMargin();
                                        n11 = constraintAnchor2.getMargin();
                                        ((LinearSystem)object).addCentering(solverVariable3, solverVariable5, n2, f, solverVariable4, solverVariable2, n11, n6);
                                        n2 = n;
                                    } else {
                                        n2 = n;
                                    }
                                    if (this.mVisibility == 8 && !constraintAnchor2.hasDependents()) {
                                        return;
                                    }
                                    if (n12 != 0) {
                                        n = bl2 && solverVariable5 != solverVariable4 && n8 == 0 && (object5 instanceof Barrier || object4 instanceof Barrier) ? 6 : n5;
                                        ((LinearSystem)object).addGreaterThan(solverVariable3, solverVariable5, constraintAnchor.getMargin(), n);
                                        ((LinearSystem)object).addLowerThan(solverVariable2, solverVariable4, -constraintAnchor2.getMargin(), n);
                                        n5 = n;
                                    }
                                    if (bl2 && bl10 && !(object5 instanceof Barrier) && !(object4 instanceof Barrier)) {
                                        n2 = 1;
                                        n4 = 6;
                                        n = 6;
                                    } else {
                                        n = n4;
                                        n4 = n5;
                                    }
                                    if (n2 == 0) break block120;
                                    n2 = n;
                                    if (n9 == 0) break block121;
                                    if (!bl9) break block122;
                                    n2 = n;
                                    if (!bl3) break block121;
                                }
                                n2 = n;
                                if (object5 == object3 || object4 == object3) {
                                    n2 = 6;
                                }
                                if (object5 instanceof Guideline || object4 instanceof Guideline) {
                                    n2 = 5;
                                }
                                if (object5 instanceof Barrier || object4 instanceof Barrier) {
                                    n2 = 5;
                                }
                                if (bl9) {
                                    n2 = 5;
                                }
                                n2 = Math.max(n2, n);
                            }
                            n = n2;
                            if (!bl2) break block123;
                            n = n2 = Math.min(n4, n2);
                            if (!bl6) break block123;
                            n = n2;
                            if (bl9) break block123;
                            if (object5 == object3) break block124;
                            n = n2;
                            if (object4 != object3) break block123;
                        }
                        n = 4;
                    }
                    ((LinearSystem)object).addEquality(solverVariable3, solverVariable5, constraintAnchor.getMargin(), n);
                    ((LinearSystem)object).addEquality(solverVariable2, solverVariable4, -constraintAnchor2.getMargin(), n);
                }
                if (bl2) {
                    n = 0;
                    if (object2 == solverVariable5) {
                        n = constraintAnchor.getMargin();
                    }
                    if (solverVariable5 != object2) {
                        ((LinearSystem)object).addGreaterThan(solverVariable3, (SolverVariable)object2, n, 5);
                    }
                }
                if (bl2 && n8 != 0 && n3 == 0 && n7 == 0) {
                    if (n8 != 0 && n10 == 3) {
                        ((LinearSystem)object).addGreaterThan(solverVariable2, solverVariable3, 0, 8);
                    } else {
                        ((LinearSystem)object).addGreaterThan(solverVariable2, solverVariable3, 0, 5);
                    }
                }
            }
            if (bl2 && bl4) {
                n = 0;
                if (constraintAnchor2.mTarget != null) {
                    n = constraintAnchor2.getMargin();
                }
                if (solverVariable4 != solverVariable) {
                    if (this.OPTIMIZE_WRAP && solverVariable2.isFinalValue && (object2 = this.mParent) != null) {
                        object = (ConstraintWidgetContainer)object2;
                        if (bl) {
                            ((ConstraintWidgetContainer)object).addHorizontalWrapMaxVariable(constraintAnchor2);
                        } else {
                            ((ConstraintWidgetContainer)object).addVerticalWrapMaxVariable(constraintAnchor2);
                        }
                        return;
                    }
                    ((LinearSystem)object).addGreaterThan(solverVariable, solverVariable2, n, 5);
                }
            }
            return;
        }
        if (n9 < 2 && bl2 && bl4) {
            ((LinearSystem)object).addGreaterThan(solverVariable3, (SolverVariable)object2, 0, 8);
            n2 = !bl && this.mBaseline.mTarget != null ? 0 : 1;
            n = n2;
            if (!bl) {
                n = n2;
                if (this.mBaseline.mTarget != null) {
                    object2 = this.mBaseline.mTarget.mOwner;
                    n = ((ConstraintWidget)object2).mDimensionRatio != 0.0f && ((ConstraintWidget)object2).mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT && ((ConstraintWidget)object2).mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT ? 1 : 0;
                }
            }
            if (n != 0) {
                ((LinearSystem)object).addGreaterThan(solverVariable, solverVariable2, 0, 8);
            }
        }
    }

    private boolean isChainHead(int n) {
        ConstraintAnchor[] constraintAnchorArray;
        ConstraintAnchor constraintAnchor;
        boolean bl = this.mListAnchors[n *= 2].mTarget != null && (constraintAnchor = this.mListAnchors[n].mTarget.mTarget) != (constraintAnchorArray = this.mListAnchors)[n] && constraintAnchorArray[n + 1].mTarget != null && this.mListAnchors[n + 1].mTarget.mTarget == this.mListAnchors[n + 1];
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void addChildrenToSolverByDependency(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, HashSet<ConstraintWidget> hashSet, int n, boolean bl) {
        if (bl) {
            if (!hashSet.contains(this)) {
                return;
            }
            Optimizer.checkMatchParent(constraintWidgetContainer, linearSystem, this);
            hashSet.remove(this);
            this.addToSolver(linearSystem, constraintWidgetContainer.optimizeFor(64));
        }
        if (n == 0) {
            HashSet<ConstraintAnchor> hashSet2 = this.mLeft.getDependents();
            if (hashSet2 != null) {
                hashSet2 = hashSet2.iterator();
                while (hashSet2.hasNext()) {
                    ((ConstraintAnchor)hashSet2.next()).mOwner.addChildrenToSolverByDependency(constraintWidgetContainer, linearSystem, hashSet, n, true);
                }
            }
            if ((hashSet2 = this.mRight.getDependents()) == null) return;
            hashSet2 = hashSet2.iterator();
            while (hashSet2.hasNext()) {
                ((ConstraintAnchor)hashSet2.next()).mOwner.addChildrenToSolverByDependency(constraintWidgetContainer, linearSystem, hashSet, n, true);
            }
            return;
        }
        HashSet<ConstraintAnchor> hashSet3 = this.mTop.getDependents();
        if (hashSet3 != null) {
            hashSet3 = hashSet3.iterator();
            while (hashSet3.hasNext()) {
                ((ConstraintAnchor)hashSet3.next()).mOwner.addChildrenToSolverByDependency(constraintWidgetContainer, linearSystem, hashSet, n, true);
            }
        }
        if ((hashSet3 = this.mBottom.getDependents()) != null) {
            hashSet3 = hashSet3.iterator();
            while (hashSet3.hasNext()) {
                ((ConstraintAnchor)hashSet3.next()).mOwner.addChildrenToSolverByDependency(constraintWidgetContainer, linearSystem, hashSet, n, true);
            }
        }
        if ((hashSet3 = this.mBaseline.getDependents()) == null) return;
        hashSet3 = hashSet3.iterator();
        while (hashSet3.hasNext()) {
            ConstraintWidget constraintWidget = ((ConstraintAnchor)hashSet3.next()).mOwner;
            constraintWidget.addChildrenToSolverByDependency(constraintWidgetContainer, linearSystem, hashSet, n, true);
        }
    }

    boolean addFirst() {
        boolean bl = this instanceof VirtualLayout || this instanceof Guideline;
        return bl;
    }

    public void addToSolver(LinearSystem linearSystem, boolean bl) {
        Object object;
        Object object2;
        Object object3;
        Object object4;
        int n;
        int n2;
        int n3;
        float f;
        boolean bl2;
        int n4;
        int n5;
        int n6;
        boolean bl3;
        boolean bl4;
        boolean bl5;
        boolean bl6;
        boolean bl7;
        boolean bl8;
        Object object5;
        DimensionBehaviour[] dimensionBehaviourArray;
        SolverVariable solverVariable;
        SolverVariable solverVariable2;
        SolverVariable solverVariable3;
        SolverVariable solverVariable4;
        block65: {
            block63: {
                block61: {
                    block64: {
                        block62: {
                            solverVariable4 = linearSystem.createObjectVariable(this.mLeft);
                            solverVariable3 = linearSystem.createObjectVariable(this.mRight);
                            solverVariable2 = linearSystem.createObjectVariable(this.mTop);
                            solverVariable = linearSystem.createObjectVariable(this.mBottom);
                            dimensionBehaviourArray = linearSystem.createObjectVariable(this.mBaseline);
                            object5 = this.mParent;
                            if (object5 != null) {
                                bl8 = object5 != null && ((ConstraintWidget)object5).mListDimensionBehaviors[0] == DimensionBehaviour.WRAP_CONTENT;
                                object5 = this.mParent;
                                bl7 = object5 != null && ((ConstraintWidget)object5).mListDimensionBehaviors[1] == DimensionBehaviour.WRAP_CONTENT;
                                bl6 = bl7;
                                bl5 = bl8;
                            } else {
                                bl5 = false;
                                bl6 = false;
                            }
                            if (this.mVisibility == 8 && !this.hasDependencies() && (object5 = (Object)this.mIsInBarrier)[0] == false && object5[1] == false) {
                                return;
                            }
                            bl8 = this.resolvedHorizontal;
                            if (bl8 || this.resolvedVertical) {
                                if (bl8) {
                                    linearSystem.addEquality(solverVariable4, this.mX);
                                    linearSystem.addEquality(solverVariable3, this.mX + this.mWidth);
                                    if (bl5 && (object5 = this.mParent) != null) {
                                        if (this.OPTIMIZE_WRAP_ON_RESOLVED) {
                                            object5 = (ConstraintWidgetContainer)object5;
                                            ((ConstraintWidgetContainer)object5).addVerticalWrapMinVariable(this.mLeft);
                                            ((ConstraintWidgetContainer)object5).addHorizontalWrapMaxVariable(this.mRight);
                                        } else {
                                            linearSystem.addGreaterThan(linearSystem.createObjectVariable(((ConstraintWidget)object5).mRight), solverVariable3, 0, 5);
                                        }
                                    }
                                }
                                if (this.resolvedVertical) {
                                    linearSystem.addEquality(solverVariable2, this.mY);
                                    linearSystem.addEquality(solverVariable, this.mY + this.mHeight);
                                    if (this.mBaseline.hasDependents()) {
                                        linearSystem.addEquality((SolverVariable)dimensionBehaviourArray, this.mY + this.mBaselineDistance);
                                    }
                                    if (bl6 && (object5 = this.mParent) != null) {
                                        if (this.OPTIMIZE_WRAP_ON_RESOLVED) {
                                            object5 = (ConstraintWidgetContainer)object5;
                                            ((ConstraintWidgetContainer)object5).addVerticalWrapMinVariable(this.mTop);
                                            ((ConstraintWidgetContainer)object5).addVerticalWrapMaxVariable(this.mBottom);
                                        } else {
                                            linearSystem.addGreaterThan(linearSystem.createObjectVariable(((ConstraintWidget)object5).mBottom), solverVariable, 0, 5);
                                        }
                                    }
                                }
                                if (this.resolvedHorizontal && this.resolvedVertical) {
                                    this.resolvedHorizontal = false;
                                    this.resolvedVertical = false;
                                    return;
                                }
                            }
                            if (LinearSystem.sMetrics != null) {
                                object5 = LinearSystem.sMetrics;
                                ++((Metrics)object5).widgets;
                            }
                            if (bl && (object5 = this.horizontalRun) != null && this.verticalRun != null && ((HorizontalWidgetRun)object5).start.resolved && this.horizontalRun.end.resolved && this.verticalRun.start.resolved && this.verticalRun.end.resolved) {
                                if (LinearSystem.sMetrics != null) {
                                    object5 = LinearSystem.sMetrics;
                                    ++((Metrics)object5).graphSolved;
                                }
                                linearSystem.addEquality(solverVariable4, this.horizontalRun.start.value);
                                linearSystem.addEquality(solverVariable3, this.horizontalRun.end.value);
                                linearSystem.addEquality(solverVariable2, this.verticalRun.start.value);
                                linearSystem.addEquality(solverVariable, this.verticalRun.end.value);
                                linearSystem.addEquality((SolverVariable)dimensionBehaviourArray, this.verticalRun.baseline.value);
                                if (this.mParent != null) {
                                    if (bl5 && this.isTerminalWidget[0] && !this.isInHorizontalChain()) {
                                        linearSystem.addGreaterThan(linearSystem.createObjectVariable(this.mParent.mRight), solverVariable3, 0, 8);
                                    }
                                    if (bl6 && this.isTerminalWidget[1] && !this.isInVerticalChain()) {
                                        linearSystem.addGreaterThan(linearSystem.createObjectVariable(this.mParent.mBottom), solverVariable, 0, 8);
                                    }
                                }
                                this.resolvedHorizontal = false;
                                this.resolvedVertical = false;
                                return;
                            }
                            if (LinearSystem.sMetrics != null) {
                                object5 = LinearSystem.sMetrics;
                                ++((Metrics)object5).linearSolved;
                            }
                            if (this.mParent != null) {
                                if (this.isChainHead(0)) {
                                    ((ConstraintWidgetContainer)this.mParent).addChain(this, 0);
                                    bl8 = true;
                                } else {
                                    bl8 = this.isInHorizontalChain();
                                }
                                if (this.isChainHead(1)) {
                                    ((ConstraintWidgetContainer)this.mParent).addChain(this, 1);
                                    bl7 = true;
                                } else {
                                    bl7 = this.isInVerticalChain();
                                }
                                if (!bl8 && bl5 && this.mVisibility != 8 && this.mLeft.mTarget == null && this.mRight.mTarget == null) {
                                    linearSystem.addGreaterThan(linearSystem.createObjectVariable(this.mParent.mRight), solverVariable3, 0, 1);
                                }
                                if (!bl7 && bl6 && this.mVisibility != 8 && this.mTop.mTarget == null && this.mBottom.mTarget == null && this.mBaseline == null) {
                                    linearSystem.addGreaterThan(linearSystem.createObjectVariable(this.mParent.mBottom), solverVariable, 0, 1);
                                }
                                bl4 = bl8;
                                bl3 = bl7;
                            } else {
                                bl4 = false;
                                bl3 = false;
                            }
                            n5 = n6 = this.mWidth;
                            if (n6 < this.mMinWidth) {
                                n5 = this.mMinWidth;
                            }
                            n4 = n6 = this.mHeight;
                            if (n6 < this.mMinHeight) {
                                n4 = this.mMinHeight;
                            }
                            bl8 = this.mListDimensionBehaviors[0] != DimensionBehaviour.MATCH_CONSTRAINT;
                            bl7 = this.mListDimensionBehaviors[1] != DimensionBehaviour.MATCH_CONSTRAINT;
                            bl2 = false;
                            this.mResolvedDimensionRatioSide = this.mDimensionRatioSide;
                            this.mResolvedDimensionRatio = f = this.mDimensionRatio;
                            n3 = this.mMatchConstraintDefaultWidth;
                            n2 = this.mMatchConstraintDefaultHeight;
                            if (!(f > 0.0f) || this.mVisibility == 8) break block61;
                            bl2 = true;
                            n6 = n3;
                            if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT) {
                                n6 = n3;
                                if (n3 == 0) {
                                    n6 = 3;
                                }
                            }
                            n = n2;
                            if (this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT) {
                                n = n2;
                                if (n2 == 0) {
                                    n = 3;
                                }
                            }
                            if ((object5 = this.mListDimensionBehaviors[0]) != (object4 = DimensionBehaviour.MATCH_CONSTRAINT) || this.mListDimensionBehaviors[1] != DimensionBehaviour.MATCH_CONSTRAINT || n6 != 3 || n != 3) break block62;
                            this.setupDimensionRatio(bl5, bl6, bl8, bl7);
                            n2 = n;
                            bl8 = bl2;
                            n3 = n6;
                            break block63;
                        }
                        if (this.mListDimensionBehaviors[0] != DimensionBehaviour.MATCH_CONSTRAINT || n6 != 3) break block64;
                        this.mResolvedDimensionRatioSide = 0;
                        n2 = (int)(this.mResolvedDimensionRatio * (float)this.mHeight);
                        if (this.mListDimensionBehaviors[1] != DimensionBehaviour.MATCH_CONSTRAINT) {
                            n6 = n2;
                            n5 = n;
                            n3 = 4;
                            bl8 = false;
                            n = n6;
                            n6 = n4;
                            n4 = n3;
                        } else {
                            n5 = n;
                            n3 = n6;
                            n6 = n4;
                            bl8 = true;
                            n = n2;
                            n4 = n3;
                        }
                        break block65;
                    }
                    n2 = n;
                    bl8 = bl2;
                    n3 = n6;
                    if (this.mListDimensionBehaviors[1] != DimensionBehaviour.MATCH_CONSTRAINT) break block63;
                    n2 = n;
                    bl8 = bl2;
                    n3 = n6;
                    if (n != 3) break block63;
                    this.mResolvedDimensionRatioSide = 1;
                    if (this.mDimensionRatioSide == -1) {
                        this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                    }
                    n4 = (int)(this.mResolvedDimensionRatio * (float)this.mWidth);
                    if (this.mListDimensionBehaviors[0] != DimensionBehaviour.MATCH_CONSTRAINT) {
                        n3 = n4;
                        n2 = 4;
                        n4 = n6;
                        n = n5;
                        bl8 = false;
                        n6 = n3;
                        n5 = n2;
                    } else {
                        n2 = n;
                        n3 = n6;
                        n = n5;
                        bl8 = true;
                        n6 = n4;
                        n5 = n2;
                        n4 = n3;
                    }
                    break block65;
                }
                bl8 = bl2;
            }
            n6 = n4;
            n = n5;
            n4 = n3;
            n5 = n2;
        }
        object5 = this.mResolvedMatchConstraintDefault;
        object5[0] = n4;
        object5[1] = n5;
        this.mResolvedHasRatio = bl8;
        boolean bl9 = bl8 && ((n3 = this.mResolvedDimensionRatioSide) == 0 || n3 == -1);
        bl2 = bl8 && ((n3 = this.mResolvedDimensionRatioSide) == 1 || n3 == -1);
        boolean bl10 = this.mListDimensionBehaviors[0] == DimensionBehaviour.WRAP_CONTENT && this instanceof ConstraintWidgetContainer;
        if (bl10) {
            n = 0;
        }
        bl7 = !this.mCenter.isConnected();
        object5 = this.mIsInBarrier;
        Object object6 = object5[0];
        Object object7 = object5[1];
        if (this.mHorizontalResolution != 2 && !this.resolvedHorizontal) {
            if (bl && (object5 = this.horizontalRun) != null && ((HorizontalWidgetRun)object5).start.resolved && this.horizontalRun.end.resolved) {
                if (bl) {
                    linearSystem.addEquality(solverVariable4, this.horizontalRun.start.value);
                    linearSystem.addEquality(solverVariable3, this.horizontalRun.end.value);
                    if (this.mParent != null && bl5 && this.isTerminalWidget[0] && !this.isInHorizontalChain()) {
                        linearSystem.addGreaterThan(linearSystem.createObjectVariable(this.mParent.mRight), solverVariable3, 0, 8);
                    }
                }
            } else {
                object5 = this.mParent;
                object5 = object5 != null ? linearSystem.createObjectVariable(((ConstraintWidget)object5).mRight) : null;
                object4 = this.mParent;
                object4 = object4 != null ? linearSystem.createObjectVariable(((ConstraintWidget)object4).mLeft) : null;
                boolean bl11 = this.isTerminalWidget[0];
                object3 = this.mListDimensionBehaviors;
                object2 = object3[0];
                ConstraintAnchor constraintAnchor = this.mLeft;
                object = this.mRight;
                int n7 = this.mX;
                n2 = this.mMinWidth;
                n3 = this.mMaxDimension[0];
                f = this.mHorizontalBiasPercent;
                boolean bl12 = object3[1] == DimensionBehaviour.MATCH_CONSTRAINT;
                this.applyConstraints(linearSystem, true, bl5, bl6, bl11, (SolverVariable)object4, (SolverVariable)object5, (DimensionBehaviour)((Object)object2), bl10, constraintAnchor, (ConstraintAnchor)object, n7, n, n2, n3, f, bl9, bl12, bl4, bl3, (boolean)object6, n4, n5, this.mMatchConstraintMinWidth, this.mMatchConstraintMaxWidth, this.mMatchConstraintPercentWidth, bl7);
            }
        }
        n = 1;
        if (bl && (object5 = this.verticalRun) != null && ((VerticalWidgetRun)object5).start.resolved && this.verticalRun.end.resolved) {
            n = this.verticalRun.start.value;
            linearSystem.addEquality(solverVariable2, n);
            n = this.verticalRun.end.value;
            linearSystem.addEquality(solverVariable, n);
            linearSystem.addEquality((SolverVariable)dimensionBehaviourArray, this.verticalRun.baseline.value);
            object5 = this.mParent;
            if (object5 != null && !bl3 && bl6 && this.isTerminalWidget[1]) {
                linearSystem.addGreaterThan(linearSystem.createObjectVariable(((ConstraintWidget)object5).mBottom), solverVariable, 0, 8);
            }
            n = 0;
        }
        if (this.mVerticalResolution == 2) {
            n = 0;
        }
        if (n != 0 && !this.resolvedVertical) {
            bl = this.mListDimensionBehaviors[1] == DimensionBehaviour.WRAP_CONTENT && this instanceof ConstraintWidgetContainer;
            if (bl) {
                n6 = 0;
            }
            object5 = (object5 = this.mParent) != null ? linearSystem.createObjectVariable(((ConstraintWidget)object5).mBottom) : null;
            object4 = this.mParent;
            object4 = object4 != null ? linearSystem.createObjectVariable(((ConstraintWidget)object4).mTop) : null;
            if (this.mBaselineDistance > 0 || this.mVisibility == 8) {
                if (this.mBaseline.mTarget != null) {
                    linearSystem.addEquality((SolverVariable)dimensionBehaviourArray, solverVariable2, this.getBaselineDistance(), 8);
                    linearSystem.addEquality((SolverVariable)dimensionBehaviourArray, linearSystem.createObjectVariable(this.mBaseline.mTarget), 0, 8);
                    if (bl6) {
                        linearSystem.addGreaterThan((SolverVariable)object5, linearSystem.createObjectVariable(this.mBottom), 0, 5);
                    }
                    bl7 = false;
                } else if (this.mVisibility == 8) {
                    linearSystem.addEquality((SolverVariable)dimensionBehaviourArray, solverVariable2, 0, 8);
                } else {
                    linearSystem.addEquality((SolverVariable)dimensionBehaviourArray, solverVariable2, this.getBaselineDistance(), 8);
                }
            }
            bl10 = this.isTerminalWidget[1];
            dimensionBehaviourArray = this.mListDimensionBehaviors;
            object = dimensionBehaviourArray[1];
            object2 = this.mTop;
            object3 = this.mBottom;
            n3 = this.mY;
            n = this.mMinHeight;
            n2 = this.mMaxDimension[1];
            f = this.mVerticalBiasPercent;
            bl9 = dimensionBehaviourArray[0] == DimensionBehaviour.MATCH_CONSTRAINT;
            this.applyConstraints(linearSystem, false, bl6, bl5, bl10, (SolverVariable)object4, (SolverVariable)object5, (DimensionBehaviour)((Object)object), bl, (ConstraintAnchor)object2, (ConstraintAnchor)object3, n3, n6, n, n2, f, bl2, bl9, bl3, bl4, (boolean)object7, n5, n4, this.mMatchConstraintMinHeight, this.mMatchConstraintMaxHeight, this.mMatchConstraintPercentHeight, bl7);
        }
        if (bl8) {
            if (this.mResolvedDimensionRatioSide == 1) {
                linearSystem.addRatio(solverVariable, solverVariable2, solverVariable3, solverVariable4, this.mResolvedDimensionRatio, 8);
            } else {
                linearSystem.addRatio(solverVariable3, solverVariable4, solverVariable, solverVariable2, this.mResolvedDimensionRatio, 8);
            }
        }
        if (this.mCenter.isConnected()) {
            linearSystem.addCenterPoint(this, this.mCenter.getTarget().getOwner(), (float)Math.toRadians(this.mCircleConstraintAngle + 90.0f), this.mCenter.getMargin());
        }
        this.resolvedHorizontal = false;
        this.resolvedVertical = false;
    }

    public boolean allowedInBarrier() {
        boolean bl = this.mVisibility != 8;
        return bl;
    }

    public void connect(ConstraintAnchor.Type type, ConstraintWidget constraintWidget, ConstraintAnchor.Type type2) {
        this.connect(type, constraintWidget, type2, 0);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void connect(ConstraintAnchor.Type object, ConstraintWidget object2, ConstraintAnchor.Type object3, int n) {
        int n2;
        ConstraintAnchor constraintAnchor;
        block36: {
            block38: {
                block37: {
                    block35: {
                        block29: {
                            block30: {
                                int n3;
                                block34: {
                                    int n4;
                                    block33: {
                                        ConstraintAnchor constraintAnchor2;
                                        block32: {
                                            ConstraintAnchor constraintAnchor3;
                                            block31: {
                                                if (object != ConstraintAnchor.Type.CENTER) break block29;
                                                if (object3 != ConstraintAnchor.Type.CENTER) break block30;
                                                object3 = this.getAnchor(ConstraintAnchor.Type.LEFT);
                                                constraintAnchor3 = this.getAnchor(ConstraintAnchor.Type.RIGHT);
                                                constraintAnchor2 = this.getAnchor(ConstraintAnchor.Type.TOP);
                                                object = this.getAnchor(ConstraintAnchor.Type.BOTTOM);
                                                n3 = 0;
                                                n4 = 0;
                                                if (object3 == null) break block31;
                                                n = n3;
                                                if (((ConstraintAnchor)object3).isConnected()) break block32;
                                            }
                                            if (constraintAnchor3 != null && constraintAnchor3.isConnected()) {
                                                n = n3;
                                            } else {
                                                this.connect(ConstraintAnchor.Type.LEFT, (ConstraintWidget)object2, ConstraintAnchor.Type.LEFT, 0);
                                                this.connect(ConstraintAnchor.Type.RIGHT, (ConstraintWidget)object2, ConstraintAnchor.Type.RIGHT, 0);
                                                n = 1;
                                            }
                                        }
                                        if (constraintAnchor2 == null) break block33;
                                        n3 = n4;
                                        if (constraintAnchor2.isConnected()) break block34;
                                    }
                                    if (object != null && ((ConstraintAnchor)object).isConnected()) {
                                        n3 = n4;
                                    } else {
                                        this.connect(ConstraintAnchor.Type.TOP, (ConstraintWidget)object2, ConstraintAnchor.Type.TOP, 0);
                                        this.connect(ConstraintAnchor.Type.BOTTOM, (ConstraintWidget)object2, ConstraintAnchor.Type.BOTTOM, 0);
                                        n3 = 1;
                                    }
                                }
                                if (n != 0 && n3 != 0) {
                                    this.getAnchor(ConstraintAnchor.Type.CENTER).connect(((ConstraintWidget)object2).getAnchor(ConstraintAnchor.Type.CENTER), 0);
                                    return;
                                }
                                if (n != 0) {
                                    this.getAnchor(ConstraintAnchor.Type.CENTER_X).connect(((ConstraintWidget)object2).getAnchor(ConstraintAnchor.Type.CENTER_X), 0);
                                    return;
                                }
                                if (n3 == 0) return;
                                this.getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(((ConstraintWidget)object2).getAnchor(ConstraintAnchor.Type.CENTER_Y), 0);
                                return;
                            }
                            if (object3 != ConstraintAnchor.Type.LEFT && object3 != ConstraintAnchor.Type.RIGHT) {
                                if (object3 != ConstraintAnchor.Type.TOP) {
                                    if (object3 != ConstraintAnchor.Type.BOTTOM) return;
                                }
                                this.connect(ConstraintAnchor.Type.TOP, (ConstraintWidget)object2, (ConstraintAnchor.Type)((Object)object3), 0);
                                this.connect(ConstraintAnchor.Type.BOTTOM, (ConstraintWidget)object2, (ConstraintAnchor.Type)((Object)object3), 0);
                                this.getAnchor(ConstraintAnchor.Type.CENTER).connect(((ConstraintWidget)object2).getAnchor((ConstraintAnchor.Type)((Object)object3)), 0);
                                return;
                            }
                            this.connect(ConstraintAnchor.Type.LEFT, (ConstraintWidget)object2, (ConstraintAnchor.Type)((Object)object3), 0);
                            object = ConstraintAnchor.Type.RIGHT;
                            this.connect((ConstraintAnchor.Type)((Object)object), (ConstraintWidget)object2, (ConstraintAnchor.Type)((Object)object3), 0);
                            this.getAnchor(ConstraintAnchor.Type.CENTER).connect(((ConstraintWidget)object2).getAnchor((ConstraintAnchor.Type)((Object)object3)), 0);
                            return;
                        }
                        if (object == ConstraintAnchor.Type.CENTER_X && (object3 == ConstraintAnchor.Type.LEFT || object3 == ConstraintAnchor.Type.RIGHT)) {
                            object = this.getAnchor(ConstraintAnchor.Type.LEFT);
                            object3 = ((ConstraintWidget)object2).getAnchor((ConstraintAnchor.Type)((Object)object3));
                            object2 = this.getAnchor(ConstraintAnchor.Type.RIGHT);
                            ((ConstraintAnchor)object).connect((ConstraintAnchor)object3, 0);
                            ((ConstraintAnchor)object2).connect((ConstraintAnchor)object3, 0);
                            this.getAnchor(ConstraintAnchor.Type.CENTER_X).connect((ConstraintAnchor)object3, 0);
                            return;
                        }
                        if (object == ConstraintAnchor.Type.CENTER_Y && (object3 == ConstraintAnchor.Type.TOP || object3 == ConstraintAnchor.Type.BOTTOM)) {
                            object = ((ConstraintWidget)object2).getAnchor((ConstraintAnchor.Type)((Object)object3));
                            this.getAnchor(ConstraintAnchor.Type.TOP).connect((ConstraintAnchor)object, 0);
                            this.getAnchor(ConstraintAnchor.Type.BOTTOM).connect((ConstraintAnchor)object, 0);
                            this.getAnchor(ConstraintAnchor.Type.CENTER_Y).connect((ConstraintAnchor)object, 0);
                            return;
                        }
                        if (object == ConstraintAnchor.Type.CENTER_X && object3 == ConstraintAnchor.Type.CENTER_X) {
                            this.getAnchor(ConstraintAnchor.Type.LEFT).connect(((ConstraintWidget)object2).getAnchor(ConstraintAnchor.Type.LEFT), 0);
                            this.getAnchor(ConstraintAnchor.Type.RIGHT).connect(((ConstraintWidget)object2).getAnchor(ConstraintAnchor.Type.RIGHT), 0);
                            this.getAnchor(ConstraintAnchor.Type.CENTER_X).connect(((ConstraintWidget)object2).getAnchor((ConstraintAnchor.Type)((Object)object3)), 0);
                            return;
                        }
                        if (object == ConstraintAnchor.Type.CENTER_Y && object3 == ConstraintAnchor.Type.CENTER_Y) {
                            this.getAnchor(ConstraintAnchor.Type.TOP).connect(((ConstraintWidget)object2).getAnchor(ConstraintAnchor.Type.TOP), 0);
                            this.getAnchor(ConstraintAnchor.Type.BOTTOM).connect(((ConstraintWidget)object2).getAnchor(ConstraintAnchor.Type.BOTTOM), 0);
                            this.getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(((ConstraintWidget)object2).getAnchor((ConstraintAnchor.Type)((Object)object3)), 0);
                            return;
                        }
                        constraintAnchor = this.getAnchor((ConstraintAnchor.Type)((Object)object));
                        if (!constraintAnchor.isValidConnection((ConstraintAnchor)(object2 = ((ConstraintWidget)object2).getAnchor((ConstraintAnchor.Type)((Object)object3))))) return;
                        if (object != ConstraintAnchor.Type.BASELINE) break block35;
                        object3 = this.getAnchor(ConstraintAnchor.Type.TOP);
                        object = this.getAnchor(ConstraintAnchor.Type.BOTTOM);
                        if (object3 != null) {
                            ((ConstraintAnchor)object3).reset();
                        }
                        if (object != null) {
                            ((ConstraintAnchor)object).reset();
                        }
                        n2 = 0;
                        break block36;
                    }
                    if (object == ConstraintAnchor.Type.TOP || object == ConstraintAnchor.Type.BOTTOM) break block37;
                    if (object != ConstraintAnchor.Type.LEFT && object != ConstraintAnchor.Type.RIGHT) break block38;
                    object3 = this.getAnchor(ConstraintAnchor.Type.CENTER);
                    if (((ConstraintAnchor)object3).getTarget() != object2) {
                        ((ConstraintAnchor)object3).reset();
                    }
                    object = this.getAnchor((ConstraintAnchor.Type)((Object)object)).getOpposite();
                    object3 = this.getAnchor(ConstraintAnchor.Type.CENTER_X);
                    n2 = n;
                    if (((ConstraintAnchor)object3).isConnected()) {
                        ((ConstraintAnchor)object).reset();
                        ((ConstraintAnchor)object3).reset();
                        n2 = n;
                    }
                    break block36;
                }
                object3 = this.getAnchor(ConstraintAnchor.Type.BASELINE);
                if (object3 != null) {
                    ((ConstraintAnchor)object3).reset();
                }
                if (((ConstraintAnchor)(object3 = this.getAnchor(ConstraintAnchor.Type.CENTER))).getTarget() != object2) {
                    ((ConstraintAnchor)object3).reset();
                }
                object = this.getAnchor((ConstraintAnchor.Type)((Object)object)).getOpposite();
                object3 = this.getAnchor(ConstraintAnchor.Type.CENTER_Y);
                if (((ConstraintAnchor)object3).isConnected()) {
                    ((ConstraintAnchor)object).reset();
                    ((ConstraintAnchor)object3).reset();
                }
            }
            n2 = n;
        }
        constraintAnchor.connect((ConstraintAnchor)object2, n2);
    }

    public void connect(ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, int n) {
        if (constraintAnchor.getOwner() == this) {
            this.connect(constraintAnchor.getType(), constraintAnchor2.getOwner(), constraintAnchor2.getType(), n);
        }
    }

    public void connectCircularConstraint(ConstraintWidget constraintWidget, float f, int n) {
        this.immediateConnect(ConstraintAnchor.Type.CENTER, constraintWidget, ConstraintAnchor.Type.CENTER, n, 0);
        this.mCircleConstraintAngle = f;
    }

    public void copy(ConstraintWidget object, HashMap<ConstraintWidget, ConstraintWidget> hashMap) {
        this.mHorizontalResolution = ((ConstraintWidget)object).mHorizontalResolution;
        this.mVerticalResolution = ((ConstraintWidget)object).mVerticalResolution;
        this.mMatchConstraintDefaultWidth = ((ConstraintWidget)object).mMatchConstraintDefaultWidth;
        this.mMatchConstraintDefaultHeight = ((ConstraintWidget)object).mMatchConstraintDefaultHeight;
        Object object2 = this.mResolvedMatchConstraintDefault;
        int[] nArray = ((ConstraintWidget)object).mResolvedMatchConstraintDefault;
        object2[0] = nArray[0];
        object2[1] = nArray[1];
        this.mMatchConstraintMinWidth = ((ConstraintWidget)object).mMatchConstraintMinWidth;
        this.mMatchConstraintMaxWidth = ((ConstraintWidget)object).mMatchConstraintMaxWidth;
        this.mMatchConstraintMinHeight = ((ConstraintWidget)object).mMatchConstraintMinHeight;
        this.mMatchConstraintMaxHeight = ((ConstraintWidget)object).mMatchConstraintMaxHeight;
        this.mMatchConstraintPercentHeight = ((ConstraintWidget)object).mMatchConstraintPercentHeight;
        this.mIsWidthWrapContent = ((ConstraintWidget)object).mIsWidthWrapContent;
        this.mIsHeightWrapContent = ((ConstraintWidget)object).mIsHeightWrapContent;
        this.mResolvedDimensionRatioSide = ((ConstraintWidget)object).mResolvedDimensionRatioSide;
        this.mResolvedDimensionRatio = ((ConstraintWidget)object).mResolvedDimensionRatio;
        object2 = ((ConstraintWidget)object).mMaxDimension;
        this.mMaxDimension = Arrays.copyOf(object2, ((int[])object2).length);
        this.mCircleConstraintAngle = ((ConstraintWidget)object).mCircleConstraintAngle;
        this.hasBaseline = ((ConstraintWidget)object).hasBaseline;
        this.inPlaceholder = ((ConstraintWidget)object).inPlaceholder;
        this.mLeft.reset();
        this.mTop.reset();
        this.mRight.reset();
        this.mBottom.reset();
        this.mBaseline.reset();
        this.mCenterX.reset();
        this.mCenterY.reset();
        this.mCenter.reset();
        this.mListDimensionBehaviors = Arrays.copyOf(this.mListDimensionBehaviors, 2);
        object2 = this.mParent;
        nArray = null;
        object2 = object2 == null ? null : (Object)hashMap.get(((ConstraintWidget)object).mParent);
        this.mParent = object2;
        this.mWidth = ((ConstraintWidget)object).mWidth;
        this.mHeight = ((ConstraintWidget)object).mHeight;
        this.mDimensionRatio = ((ConstraintWidget)object).mDimensionRatio;
        this.mDimensionRatioSide = ((ConstraintWidget)object).mDimensionRatioSide;
        this.mX = ((ConstraintWidget)object).mX;
        this.mY = ((ConstraintWidget)object).mY;
        this.mRelX = ((ConstraintWidget)object).mRelX;
        this.mRelY = ((ConstraintWidget)object).mRelY;
        this.mOffsetX = ((ConstraintWidget)object).mOffsetX;
        this.mOffsetY = ((ConstraintWidget)object).mOffsetY;
        this.mBaselineDistance = ((ConstraintWidget)object).mBaselineDistance;
        this.mMinWidth = ((ConstraintWidget)object).mMinWidth;
        this.mMinHeight = ((ConstraintWidget)object).mMinHeight;
        this.mHorizontalBiasPercent = ((ConstraintWidget)object).mHorizontalBiasPercent;
        this.mVerticalBiasPercent = ((ConstraintWidget)object).mVerticalBiasPercent;
        this.mCompanionWidget = ((ConstraintWidget)object).mCompanionWidget;
        this.mContainerItemSkip = ((ConstraintWidget)object).mContainerItemSkip;
        this.mVisibility = ((ConstraintWidget)object).mVisibility;
        this.mDebugName = ((ConstraintWidget)object).mDebugName;
        this.mType = ((ConstraintWidget)object).mType;
        this.mDistToTop = ((ConstraintWidget)object).mDistToTop;
        this.mDistToLeft = ((ConstraintWidget)object).mDistToLeft;
        this.mDistToRight = ((ConstraintWidget)object).mDistToRight;
        this.mDistToBottom = ((ConstraintWidget)object).mDistToBottom;
        this.mLeftHasCentered = ((ConstraintWidget)object).mLeftHasCentered;
        this.mRightHasCentered = ((ConstraintWidget)object).mRightHasCentered;
        this.mTopHasCentered = ((ConstraintWidget)object).mTopHasCentered;
        this.mBottomHasCentered = ((ConstraintWidget)object).mBottomHasCentered;
        this.mHorizontalWrapVisited = ((ConstraintWidget)object).mHorizontalWrapVisited;
        this.mVerticalWrapVisited = ((ConstraintWidget)object).mVerticalWrapVisited;
        this.mHorizontalChainStyle = ((ConstraintWidget)object).mHorizontalChainStyle;
        this.mVerticalChainStyle = ((ConstraintWidget)object).mVerticalChainStyle;
        this.mHorizontalChainFixedPosition = ((ConstraintWidget)object).mHorizontalChainFixedPosition;
        this.mVerticalChainFixedPosition = ((ConstraintWidget)object).mVerticalChainFixedPosition;
        Object[] objectArray = this.mWeight;
        object2 = ((ConstraintWidget)object).mWeight;
        objectArray[0] = object2[0];
        objectArray[1] = object2[1];
        object2 = this.mListNextMatchConstraintsWidget;
        objectArray = ((ConstraintWidget)object).mListNextMatchConstraintsWidget;
        object2[0] = (int)objectArray[0];
        object2[1] = (int)objectArray[1];
        object2 = this.mNextChainWidget;
        objectArray = ((ConstraintWidget)object).mNextChainWidget;
        object2[0] = (int)objectArray[0];
        object2[1] = (int)objectArray[1];
        object2 = ((ConstraintWidget)object).mHorizontalNextWidget;
        object2 = object2 == null ? null : (Object)hashMap.get(object2);
        this.mHorizontalNextWidget = object2;
        object = ((ConstraintWidget)object).mVerticalNextWidget;
        object = object == null ? (Object)nArray : hashMap.get(object);
        this.mVerticalNextWidget = object;
    }

    public void createObjectVariables(LinearSystem linearSystem) {
        linearSystem.createObjectVariable(this.mLeft);
        linearSystem.createObjectVariable(this.mTop);
        linearSystem.createObjectVariable(this.mRight);
        linearSystem.createObjectVariable(this.mBottom);
        if (this.mBaselineDistance > 0) {
            linearSystem.createObjectVariable(this.mBaseline);
        }
    }

    public void ensureMeasureRequested() {
        this.mMeasureRequested = true;
    }

    public void ensureWidgetRuns() {
        if (this.horizontalRun == null) {
            this.horizontalRun = new HorizontalWidgetRun(this);
        }
        if (this.verticalRun == null) {
            this.verticalRun = new VerticalWidgetRun(this);
        }
    }

    public ConstraintAnchor getAnchor(ConstraintAnchor.Type type) {
        switch (1.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[type.ordinal()]) {
            default: {
                throw new AssertionError((Object)type.name());
            }
            case 9: {
                return null;
            }
            case 8: {
                return this.mCenterY;
            }
            case 7: {
                return this.mCenterX;
            }
            case 6: {
                return this.mCenter;
            }
            case 5: {
                return this.mBaseline;
            }
            case 4: {
                return this.mBottom;
            }
            case 3: {
                return this.mRight;
            }
            case 2: {
                return this.mTop;
            }
            case 1: 
        }
        return this.mLeft;
    }

    public ArrayList<ConstraintAnchor> getAnchors() {
        return this.mAnchors;
    }

    public int getBaselineDistance() {
        return this.mBaselineDistance;
    }

    public float getBiasPercent(int n) {
        if (n == 0) {
            return this.mHorizontalBiasPercent;
        }
        if (n == 1) {
            return this.mVerticalBiasPercent;
        }
        return -1.0f;
    }

    public int getBottom() {
        return this.getY() + this.mHeight;
    }

    public Object getCompanionWidget() {
        return this.mCompanionWidget;
    }

    public int getContainerItemSkip() {
        return this.mContainerItemSkip;
    }

    public String getDebugName() {
        return this.mDebugName;
    }

    public DimensionBehaviour getDimensionBehaviour(int n) {
        if (n == 0) {
            return this.getHorizontalDimensionBehaviour();
        }
        if (n == 1) {
            return this.getVerticalDimensionBehaviour();
        }
        return null;
    }

    public float getDimensionRatio() {
        return this.mDimensionRatio;
    }

    public int getDimensionRatioSide() {
        return this.mDimensionRatioSide;
    }

    public boolean getHasBaseline() {
        return this.hasBaseline;
    }

    public int getHeight() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mHeight;
    }

    public float getHorizontalBiasPercent() {
        return this.mHorizontalBiasPercent;
    }

    public ConstraintWidget getHorizontalChainControlWidget() {
        Object object = null;
        Object object2 = null;
        if (this.isInHorizontalChain()) {
            Object object3 = this;
            while (true) {
                object = object2;
                if (object2 != null) break;
                object = object2;
                if (object3 == null) break;
                object = ((ConstraintWidget)object3).getAnchor(ConstraintAnchor.Type.LEFT);
                ConstraintAnchor constraintAnchor = null;
                object = object == null ? null : ((ConstraintAnchor)object).getTarget();
                if ((object = object == null ? null : ((ConstraintAnchor)object).getOwner()) == this.getParent()) {
                    object = object3;
                    break;
                }
                if (object != null) {
                    constraintAnchor = ((ConstraintWidget)object).getAnchor(ConstraintAnchor.Type.RIGHT).getTarget();
                }
                if (constraintAnchor != null && constraintAnchor.getOwner() != object3) {
                    object2 = object3;
                    continue;
                }
                object3 = object;
            }
        }
        return object;
    }

    public int getHorizontalChainStyle() {
        return this.mHorizontalChainStyle;
    }

    public DimensionBehaviour getHorizontalDimensionBehaviour() {
        return this.mListDimensionBehaviors[0];
    }

    public int getHorizontalMargin() {
        int n = 0;
        ConstraintAnchor constraintAnchor = this.mLeft;
        if (constraintAnchor != null) {
            n = 0 + constraintAnchor.mMargin;
        }
        constraintAnchor = this.mRight;
        int n2 = n;
        if (constraintAnchor != null) {
            n2 = n + constraintAnchor.mMargin;
        }
        return n2;
    }

    public int getLastHorizontalMeasureSpec() {
        return this.mLastHorizontalMeasureSpec;
    }

    public int getLastVerticalMeasureSpec() {
        return this.mLastVerticalMeasureSpec;
    }

    public int getLeft() {
        return this.getX();
    }

    public int getLength(int n) {
        if (n == 0) {
            return this.getWidth();
        }
        if (n == 1) {
            return this.getHeight();
        }
        return 0;
    }

    public int getMaxHeight() {
        return this.mMaxDimension[1];
    }

    public int getMaxWidth() {
        return this.mMaxDimension[0];
    }

    public int getMinHeight() {
        return this.mMinHeight;
    }

    public int getMinWidth() {
        return this.mMinWidth;
    }

    public ConstraintWidget getNextChainMember(int n) {
        ConstraintAnchor constraintAnchor;
        ConstraintAnchor constraintAnchor2;
        if (n == 0) {
            ConstraintAnchor constraintAnchor3;
            ConstraintAnchor constraintAnchor4;
            if (this.mRight.mTarget != null && (constraintAnchor4 = this.mRight.mTarget.mTarget) == (constraintAnchor3 = this.mRight)) {
                return constraintAnchor3.mTarget.mOwner;
            }
        } else if (n == 1 && this.mBottom.mTarget != null && (constraintAnchor2 = this.mBottom.mTarget.mTarget) == (constraintAnchor = this.mBottom)) {
            return constraintAnchor.mTarget.mOwner;
        }
        return null;
    }

    public int getOptimizerWrapHeight() {
        int n;
        int n2 = n = this.mHeight;
        if (this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT) {
            if (this.mMatchConstraintDefaultHeight == 1) {
                n = Math.max(this.mMatchConstraintMinHeight, n);
            } else if (this.mMatchConstraintMinHeight > 0) {
                this.mHeight = n = this.mMatchConstraintMinHeight;
            } else {
                n = 0;
            }
            int n3 = this.mMatchConstraintMaxHeight;
            n2 = n;
            if (n3 > 0) {
                n2 = n;
                if (n3 < n) {
                    n2 = this.mMatchConstraintMaxHeight;
                }
            }
        }
        return n2;
    }

    public int getOptimizerWrapWidth() {
        int n;
        int n2 = n = this.mWidth;
        if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT) {
            if (this.mMatchConstraintDefaultWidth == 1) {
                n = Math.max(this.mMatchConstraintMinWidth, n);
            } else if (this.mMatchConstraintMinWidth > 0) {
                this.mWidth = n = this.mMatchConstraintMinWidth;
            } else {
                n = 0;
            }
            int n3 = this.mMatchConstraintMaxWidth;
            n2 = n;
            if (n3 > 0) {
                n2 = n;
                if (n3 < n) {
                    n2 = this.mMatchConstraintMaxWidth;
                }
            }
        }
        return n2;
    }

    public ConstraintWidget getParent() {
        return this.mParent;
    }

    public ConstraintWidget getPreviousChainMember(int n) {
        ConstraintAnchor constraintAnchor;
        ConstraintAnchor constraintAnchor2;
        if (n == 0) {
            ConstraintAnchor constraintAnchor3;
            ConstraintAnchor constraintAnchor4;
            if (this.mLeft.mTarget != null && (constraintAnchor4 = this.mLeft.mTarget.mTarget) == (constraintAnchor3 = this.mLeft)) {
                return constraintAnchor3.mTarget.mOwner;
            }
        } else if (n == 1 && this.mTop.mTarget != null && (constraintAnchor2 = this.mTop.mTarget.mTarget) == (constraintAnchor = this.mTop)) {
            return constraintAnchor.mTarget.mOwner;
        }
        return null;
    }

    int getRelativePositioning(int n) {
        if (n == 0) {
            return this.mRelX;
        }
        if (n == 1) {
            return this.mRelY;
        }
        return 0;
    }

    public int getRight() {
        return this.getX() + this.mWidth;
    }

    protected int getRootX() {
        return this.mX + this.mOffsetX;
    }

    protected int getRootY() {
        return this.mY + this.mOffsetY;
    }

    public WidgetRun getRun(int n) {
        if (n == 0) {
            return this.horizontalRun;
        }
        if (n == 1) {
            return this.verticalRun;
        }
        return null;
    }

    public int getTop() {
        return this.getY();
    }

    public String getType() {
        return this.mType;
    }

    public float getVerticalBiasPercent() {
        return this.mVerticalBiasPercent;
    }

    public ConstraintWidget getVerticalChainControlWidget() {
        Object object = null;
        Object object2 = null;
        if (this.isInVerticalChain()) {
            Object object3 = this;
            while (true) {
                object = object2;
                if (object2 != null) break;
                object = object2;
                if (object3 == null) break;
                object = ((ConstraintWidget)object3).getAnchor(ConstraintAnchor.Type.TOP);
                ConstraintAnchor constraintAnchor = null;
                object = object == null ? null : ((ConstraintAnchor)object).getTarget();
                if ((object = object == null ? null : ((ConstraintAnchor)object).getOwner()) == this.getParent()) {
                    object = object3;
                    break;
                }
                if (object != null) {
                    constraintAnchor = ((ConstraintWidget)object).getAnchor(ConstraintAnchor.Type.BOTTOM).getTarget();
                }
                if (constraintAnchor != null && constraintAnchor.getOwner() != object3) {
                    object2 = object3;
                    continue;
                }
                object3 = object;
            }
        }
        return object;
    }

    public int getVerticalChainStyle() {
        return this.mVerticalChainStyle;
    }

    public DimensionBehaviour getVerticalDimensionBehaviour() {
        return this.mListDimensionBehaviors[1];
    }

    public int getVerticalMargin() {
        int n = 0;
        if (this.mLeft != null) {
            n = 0 + this.mTop.mMargin;
        }
        int n2 = n;
        if (this.mRight != null) {
            n2 = n + this.mBottom.mMargin;
        }
        return n2;
    }

    public int getVisibility() {
        return this.mVisibility;
    }

    public int getWidth() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mWidth;
    }

    public int getX() {
        ConstraintWidget constraintWidget = this.mParent;
        if (constraintWidget != null && constraintWidget instanceof ConstraintWidgetContainer) {
            return ((ConstraintWidgetContainer)constraintWidget).mPaddingLeft + this.mX;
        }
        return this.mX;
    }

    public int getY() {
        ConstraintWidget constraintWidget = this.mParent;
        if (constraintWidget != null && constraintWidget instanceof ConstraintWidgetContainer) {
            return ((ConstraintWidgetContainer)constraintWidget).mPaddingTop + this.mY;
        }
        return this.mY;
    }

    public boolean hasBaseline() {
        return this.hasBaseline;
    }

    public boolean hasDanglingDimension(int n) {
        boolean bl = true;
        boolean bl2 = true;
        if (n == 0) {
            int n2;
            n = this.mLeft.mTarget != null ? 1 : 0;
            if (n + (n2 = this.mRight.mTarget != null ? 1 : 0) >= 2) {
                bl2 = false;
            }
            return bl2;
        }
        n = this.mTop.mTarget != null ? 1 : 0;
        int n3 = this.mBottom.mTarget != null ? 1 : 0;
        int n4 = this.mBaseline.mTarget != null ? 1 : 0;
        bl2 = n + n3 + n4 < 2 ? bl : false;
        return bl2;
    }

    public boolean hasDependencies() {
        int n = this.mAnchors.size();
        for (int i = 0; i < n; ++i) {
            if (!this.mAnchors.get(i).hasDependents()) continue;
            return true;
        }
        return false;
    }

    public void immediateConnect(ConstraintAnchor.Type type, ConstraintWidget constraintWidget, ConstraintAnchor.Type type2, int n, int n2) {
        this.getAnchor(type).connect(constraintWidget.getAnchor(type2), n, n2, true);
    }

    public boolean isHeightWrapContent() {
        return this.mIsHeightWrapContent;
    }

    public boolean isInHorizontalChain() {
        return this.mLeft.mTarget != null && this.mLeft.mTarget.mTarget == this.mLeft || this.mRight.mTarget != null && this.mRight.mTarget.mTarget == this.mRight;
    }

    public boolean isInPlaceholder() {
        return this.inPlaceholder;
    }

    public boolean isInVerticalChain() {
        return this.mTop.mTarget != null && this.mTop.mTarget.mTarget == this.mTop || this.mBottom.mTarget != null && this.mBottom.mTarget.mTarget == this.mBottom;
    }

    public boolean isInVirtualLayout() {
        return this.mInVirtuaLayout;
    }

    public boolean isMeasureRequested() {
        boolean bl = this.mMeasureRequested && this.mVisibility != 8;
        return bl;
    }

    public boolean isResolvedHorizontally() {
        boolean bl = this.resolvedHorizontal || this.mLeft.hasFinalValue() && this.mRight.hasFinalValue();
        return bl;
    }

    public boolean isResolvedVertically() {
        boolean bl = this.resolvedVertical || this.mTop.hasFinalValue() && this.mBottom.hasFinalValue();
        return bl;
    }

    public boolean isRoot() {
        boolean bl = this.mParent == null;
        return bl;
    }

    public boolean isSpreadHeight() {
        int n = this.mMatchConstraintDefaultHeight;
        boolean bl = true;
        if (n != 0 || this.mDimensionRatio != 0.0f || this.mMatchConstraintMinHeight != 0 || this.mMatchConstraintMaxHeight != 0 || this.mListDimensionBehaviors[1] != DimensionBehaviour.MATCH_CONSTRAINT) {
            bl = false;
        }
        return bl;
    }

    public boolean isSpreadWidth() {
        boolean bl;
        int n = this.mMatchConstraintDefaultWidth;
        boolean bl2 = bl = false;
        if (n == 0) {
            bl2 = bl;
            if (this.mDimensionRatio == 0.0f) {
                bl2 = bl;
                if (this.mMatchConstraintMinWidth == 0) {
                    bl2 = bl;
                    if (this.mMatchConstraintMaxWidth == 0) {
                        bl2 = bl;
                        if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT) {
                            bl2 = true;
                        }
                    }
                }
            }
        }
        return bl2;
    }

    public boolean isWidthWrapContent() {
        return this.mIsWidthWrapContent;
    }

    public boolean oppositeDimensionDependsOn(int n) {
        boolean bl = true;
        int n2 = n == 0 ? 1 : 0;
        Object object = this.mListDimensionBehaviors;
        DimensionBehaviour dimensionBehaviour = object[n];
        object = object[n2];
        if (dimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT || object != DimensionBehaviour.MATCH_CONSTRAINT) {
            bl = false;
        }
        return bl;
    }

    public boolean oppositeDimensionsTied() {
        boolean bl;
        DimensionBehaviour[] dimensionBehaviourArray = this.mListDimensionBehaviors;
        boolean bl2 = bl = false;
        if (dimensionBehaviourArray[0] == DimensionBehaviour.MATCH_CONSTRAINT) {
            bl2 = bl;
            if (this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT) {
                bl2 = true;
            }
        }
        return bl2;
    }

    public void reset() {
        float f;
        this.mLeft.reset();
        this.mTop.reset();
        this.mRight.reset();
        this.mBottom.reset();
        this.mBaseline.reset();
        this.mCenterX.reset();
        this.mCenterY.reset();
        this.mCenter.reset();
        this.mParent = null;
        this.mCircleConstraintAngle = 0.0f;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        this.mHorizontalBiasPercent = f = DEFAULT_BIAS;
        this.mVerticalBiasPercent = f;
        this.mListDimensionBehaviors[0] = DimensionBehaviour.FIXED;
        this.mListDimensionBehaviors[1] = DimensionBehaviour.FIXED;
        this.mCompanionWidget = null;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mType = null;
        this.mHorizontalWrapVisited = false;
        this.mVerticalWrapVisited = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mHorizontalChainFixedPosition = false;
        this.mVerticalChainFixedPosition = false;
        Object[] objectArray = this.mWeight;
        objectArray[0] = -1.0f;
        objectArray[1] = -1.0f;
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        objectArray = this.mMaxDimension;
        objectArray[0] = Integer.MAX_VALUE;
        objectArray[1] = Integer.MAX_VALUE;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mMatchConstraintMaxWidth = Integer.MAX_VALUE;
        this.mMatchConstraintMaxHeight = Integer.MAX_VALUE;
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMinHeight = 0;
        this.mResolvedHasRatio = false;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        this.mGroupsToSolver = false;
        objectArray = this.isTerminalWidget;
        objectArray[0] = 1.0f;
        objectArray[1] = 1.0f;
        this.mInVirtuaLayout = false;
        objectArray = this.mIsInBarrier;
        objectArray[0] = 0.0f;
        objectArray[1] = 0.0f;
        this.mMeasureRequested = true;
    }

    public void resetAllConstraints() {
        this.resetAnchors();
        this.setVerticalBiasPercent(DEFAULT_BIAS);
        this.setHorizontalBiasPercent(DEFAULT_BIAS);
    }

    public void resetAnchor(ConstraintAnchor constraintAnchor) {
        if (this.getParent() != null && this.getParent() instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)this.getParent()).handlesInternalConstraints()) {
            return;
        }
        ConstraintAnchor constraintAnchor2 = this.getAnchor(ConstraintAnchor.Type.LEFT);
        ConstraintAnchor constraintAnchor3 = this.getAnchor(ConstraintAnchor.Type.RIGHT);
        ConstraintAnchor constraintAnchor4 = this.getAnchor(ConstraintAnchor.Type.TOP);
        ConstraintAnchor constraintAnchor5 = this.getAnchor(ConstraintAnchor.Type.BOTTOM);
        ConstraintAnchor constraintAnchor6 = this.getAnchor(ConstraintAnchor.Type.CENTER);
        ConstraintAnchor constraintAnchor7 = this.getAnchor(ConstraintAnchor.Type.CENTER_X);
        ConstraintAnchor constraintAnchor8 = this.getAnchor(ConstraintAnchor.Type.CENTER_Y);
        if (constraintAnchor == constraintAnchor6) {
            if (constraintAnchor2.isConnected() && constraintAnchor3.isConnected() && constraintAnchor2.getTarget() == constraintAnchor3.getTarget()) {
                constraintAnchor2.reset();
                constraintAnchor3.reset();
            }
            if (constraintAnchor4.isConnected() && constraintAnchor5.isConnected() && constraintAnchor4.getTarget() == constraintAnchor5.getTarget()) {
                constraintAnchor4.reset();
                constraintAnchor5.reset();
            }
            this.mHorizontalBiasPercent = 0.5f;
            this.mVerticalBiasPercent = 0.5f;
        } else if (constraintAnchor == constraintAnchor7) {
            if (constraintAnchor2.isConnected() && constraintAnchor3.isConnected() && constraintAnchor2.getTarget().getOwner() == constraintAnchor3.getTarget().getOwner()) {
                constraintAnchor2.reset();
                constraintAnchor3.reset();
            }
            this.mHorizontalBiasPercent = 0.5f;
        } else if (constraintAnchor == constraintAnchor8) {
            if (constraintAnchor4.isConnected() && constraintAnchor5.isConnected() && constraintAnchor4.getTarget().getOwner() == constraintAnchor5.getTarget().getOwner()) {
                constraintAnchor4.reset();
                constraintAnchor5.reset();
            }
            this.mVerticalBiasPercent = 0.5f;
        } else if (constraintAnchor != constraintAnchor2 && constraintAnchor != constraintAnchor3) {
            if ((constraintAnchor == constraintAnchor4 || constraintAnchor == constraintAnchor5) && constraintAnchor4.isConnected() && constraintAnchor4.getTarget() == constraintAnchor5.getTarget()) {
                constraintAnchor6.reset();
            }
        } else if (constraintAnchor2.isConnected() && constraintAnchor2.getTarget() == constraintAnchor3.getTarget()) {
            constraintAnchor6.reset();
        }
        constraintAnchor.reset();
    }

    public void resetAnchors() {
        ConstraintWidget constraintWidget = this.getParent();
        if (constraintWidget != null && constraintWidget instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)this.getParent()).handlesInternalConstraints()) {
            return;
        }
        int n = this.mAnchors.size();
        for (int i = 0; i < n; ++i) {
            this.mAnchors.get(i).reset();
        }
    }

    public void resetFinalResolution() {
        this.resolvedHorizontal = false;
        this.resolvedVertical = false;
        int n = this.mAnchors.size();
        for (int i = 0; i < n; ++i) {
            this.mAnchors.get(i).resetFinalResolution();
        }
    }

    public void resetSolverVariables(Cache cache) {
        this.mLeft.resetSolverVariable(cache);
        this.mTop.resetSolverVariable(cache);
        this.mRight.resetSolverVariable(cache);
        this.mBottom.resetSolverVariable(cache);
        this.mBaseline.resetSolverVariable(cache);
        this.mCenter.resetSolverVariable(cache);
        this.mCenterX.resetSolverVariable(cache);
        this.mCenterY.resetSolverVariable(cache);
    }

    public void setBaselineDistance(int n) {
        this.mBaselineDistance = n;
        boolean bl = n > 0;
        this.hasBaseline = bl;
    }

    public void setCompanionWidget(Object object) {
        this.mCompanionWidget = object;
    }

    public void setContainerItemSkip(int n) {
        this.mContainerItemSkip = n >= 0 ? n : 0;
    }

    public void setDebugName(String string2) {
        this.mDebugName = string2;
    }

    public void setDebugSolverName(LinearSystem object, String string2) {
        this.mDebugName = string2;
        SolverVariable solverVariable = ((LinearSystem)object).createObjectVariable(this.mLeft);
        Object object2 = ((LinearSystem)object).createObjectVariable(this.mTop);
        Object object3 = ((LinearSystem)object).createObjectVariable(this.mRight);
        Object object4 = ((LinearSystem)object).createObjectVariable(this.mBottom);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append(".left");
        solverVariable.setName(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append(".top");
        ((SolverVariable)object2).setName(stringBuilder.toString());
        object2 = new StringBuilder();
        ((StringBuilder)object2).append(string2);
        ((StringBuilder)object2).append(".right");
        ((SolverVariable)object3).setName(((StringBuilder)object2).toString());
        object3 = new StringBuilder();
        ((StringBuilder)object3).append(string2);
        ((StringBuilder)object3).append(".bottom");
        ((SolverVariable)object4).setName(((StringBuilder)object3).toString());
        object = ((LinearSystem)object).createObjectVariable(this.mBaseline);
        object4 = new StringBuilder();
        ((StringBuilder)object4).append(string2);
        ((StringBuilder)object4).append(".baseline");
        ((SolverVariable)object).setName(((StringBuilder)object4).toString());
    }

    public void setDimension(int n, int n2) {
        this.mWidth = n;
        int n3 = this.mMinWidth;
        if (n < n3) {
            this.mWidth = n3;
        }
        this.mHeight = n2;
        n = this.mMinHeight;
        if (n2 < n) {
            this.mHeight = n;
        }
    }

    public void setDimensionRatio(float f, int n) {
        this.mDimensionRatio = f;
        this.mDimensionRatioSide = n;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setDimensionRatio(String string2) {
        block13: {
            float f;
            int n;
            block15: {
                int n2;
                float f2;
                block14: {
                    float f3;
                    String string3;
                    if (string2 == null || string2.length() == 0) break block13;
                    n = -1;
                    float f4 = 0.0f;
                    f2 = 0.0f;
                    float f5 = 0.0f;
                    int n3 = string2.length();
                    int n4 = string2.indexOf(44);
                    if (n4 > 0 && n4 < n3 - 1) {
                        string3 = string2.substring(0, n4);
                        if (string3.equalsIgnoreCase("W")) {
                            n2 = 0;
                        } else {
                            n2 = n;
                            if (string3.equalsIgnoreCase("H")) {
                                n2 = 1;
                            }
                        }
                        n = n2;
                        n2 = ++n4;
                    } else {
                        n2 = 0;
                    }
                    n4 = string2.indexOf(58);
                    if (n4 < 0 || n4 >= n3 - 1) break block14;
                    string3 = string2.substring(n2, n4);
                    string2 = string2.substring(n4 + 1);
                    f = f4;
                    if (string3.length() <= 0) break block15;
                    f = f4;
                    if (string2.length() <= 0) break block15;
                    try {
                        f3 = Float.parseFloat(string3);
                        f2 = Float.parseFloat(string2);
                        f = f5;
                    }
                    catch (NumberFormatException numberFormatException) {
                        f = f4;
                    }
                    if (!(f3 > 0.0f)) break block15;
                    f = f5;
                    if (f2 > 0.0f) {
                        f = n == 1 ? Math.abs(f2 / f3) : Math.abs(f3 / f2);
                    }
                    break block15;
                }
                string2 = string2.substring(n2);
                f = f2;
                if (string2.length() > 0) {
                    try {
                        f = Float.parseFloat(string2);
                    }
                    catch (NumberFormatException numberFormatException) {
                        f = f2;
                    }
                }
            }
            if (f > 0.0f) {
                this.mDimensionRatio = f;
                this.mDimensionRatioSide = n;
            }
            return;
        }
        this.mDimensionRatio = 0.0f;
    }

    public void setFinalBaseline(int n) {
        if (!this.hasBaseline) {
            return;
        }
        int n2 = n - this.mBaselineDistance;
        int n3 = this.mHeight;
        this.mY = n2;
        this.mTop.setFinalValue(n2);
        this.mBottom.setFinalValue(n3 + n2);
        this.mBaseline.setFinalValue(n);
        this.resolvedVertical = true;
    }

    public void setFinalFrame(int n, int n2, int n3, int n4, int n5, int n6) {
        this.setFrame(n, n2, n3, n4);
        this.setBaselineDistance(n5);
        if (n6 == 0) {
            this.resolvedHorizontal = true;
            this.resolvedVertical = false;
        } else if (n6 == 1) {
            this.resolvedHorizontal = false;
            this.resolvedVertical = true;
        } else if (n6 == 2) {
            this.resolvedHorizontal = true;
            this.resolvedVertical = true;
        } else {
            this.resolvedHorizontal = false;
            this.resolvedVertical = false;
        }
    }

    public void setFinalHorizontal(int n, int n2) {
        this.mLeft.setFinalValue(n);
        this.mRight.setFinalValue(n2);
        this.mX = n;
        this.mWidth = n2 - n;
        this.resolvedHorizontal = true;
    }

    public void setFinalLeft(int n) {
        this.mLeft.setFinalValue(n);
        this.mX = n;
    }

    public void setFinalTop(int n) {
        this.mTop.setFinalValue(n);
        this.mY = n;
    }

    public void setFinalVertical(int n, int n2) {
        this.mTop.setFinalValue(n);
        this.mBottom.setFinalValue(n2);
        this.mY = n;
        this.mHeight = n2 - n;
        if (this.hasBaseline) {
            this.mBaseline.setFinalValue(this.mBaselineDistance + n);
        }
        this.resolvedVertical = true;
    }

    public void setFrame(int n, int n2, int n3) {
        if (n3 == 0) {
            this.setHorizontalDimension(n, n2);
        } else if (n3 == 1) {
            this.setVerticalDimension(n, n2);
        }
    }

    public void setFrame(int n, int n2, int n3, int n4) {
        int n5 = n3 - n;
        n3 = n4 - n2;
        this.mX = n;
        this.mY = n2;
        if (this.mVisibility == 8) {
            this.mWidth = 0;
            this.mHeight = 0;
            return;
        }
        n = n5;
        if (this.mListDimensionBehaviors[0] == DimensionBehaviour.FIXED) {
            n = n5;
            if (n5 < this.mWidth) {
                n = this.mWidth;
            }
        }
        n2 = n3;
        if (this.mListDimensionBehaviors[1] == DimensionBehaviour.FIXED) {
            n2 = n3;
            if (n3 < this.mHeight) {
                n2 = this.mHeight;
            }
        }
        this.mWidth = n;
        this.mHeight = n2;
        n3 = this.mMinHeight;
        if (n2 < n3) {
            this.mHeight = n3;
        }
        if (n < (n2 = this.mMinWidth)) {
            this.mWidth = n2;
        }
    }

    public void setGoneMargin(ConstraintAnchor.Type type, int n) {
        switch (1.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[type.ordinal()]) {
            default: {
                break;
            }
            case 4: {
                this.mBottom.mGoneMargin = n;
                break;
            }
            case 3: {
                this.mRight.mGoneMargin = n;
                break;
            }
            case 2: {
                this.mTop.mGoneMargin = n;
                break;
            }
            case 1: {
                this.mLeft.mGoneMargin = n;
            }
        }
    }

    public void setHasBaseline(boolean bl) {
        this.hasBaseline = bl;
    }

    public void setHeight(int n) {
        this.mHeight = n;
        int n2 = this.mMinHeight;
        if (n < n2) {
            this.mHeight = n2;
        }
    }

    public void setHeightWrapContent(boolean bl) {
        this.mIsHeightWrapContent = bl;
    }

    public void setHorizontalBiasPercent(float f) {
        this.mHorizontalBiasPercent = f;
    }

    public void setHorizontalChainStyle(int n) {
        this.mHorizontalChainStyle = n;
    }

    public void setHorizontalDimension(int n, int n2) {
        this.mX = n;
        this.mWidth = n = n2 - n;
        n2 = this.mMinWidth;
        if (n < n2) {
            this.mWidth = n2;
        }
    }

    public void setHorizontalDimensionBehaviour(DimensionBehaviour dimensionBehaviour) {
        this.mListDimensionBehaviors[0] = dimensionBehaviour;
    }

    public void setHorizontalMatchStyle(int n, int n2, int n3, float f) {
        this.mMatchConstraintDefaultWidth = n;
        this.mMatchConstraintMinWidth = n2;
        n2 = n3 == Integer.MAX_VALUE ? 0 : n3;
        this.mMatchConstraintMaxWidth = n2;
        this.mMatchConstraintPercentWidth = f;
        if (f > 0.0f && f < 1.0f && n == 0) {
            this.mMatchConstraintDefaultWidth = 2;
        }
    }

    public void setHorizontalWeight(float f) {
        this.mWeight[0] = f;
    }

    protected void setInBarrier(int n, boolean bl) {
        this.mIsInBarrier[n] = bl;
    }

    public void setInPlaceholder(boolean bl) {
        this.inPlaceholder = bl;
    }

    public void setInVirtualLayout(boolean bl) {
        this.mInVirtuaLayout = bl;
    }

    public void setLastMeasureSpec(int n, int n2) {
        this.mLastHorizontalMeasureSpec = n;
        this.mLastVerticalMeasureSpec = n2;
        this.setMeasureRequested(false);
    }

    public void setLength(int n, int n2) {
        if (n2 == 0) {
            this.setWidth(n);
        } else if (n2 == 1) {
            this.setHeight(n);
        }
    }

    public void setMaxHeight(int n) {
        this.mMaxDimension[1] = n;
    }

    public void setMaxWidth(int n) {
        this.mMaxDimension[0] = n;
    }

    public void setMeasureRequested(boolean bl) {
        this.mMeasureRequested = bl;
    }

    public void setMinHeight(int n) {
        this.mMinHeight = n < 0 ? 0 : n;
    }

    public void setMinWidth(int n) {
        this.mMinWidth = n < 0 ? 0 : n;
    }

    public void setOffset(int n, int n2) {
        this.mOffsetX = n;
        this.mOffsetY = n2;
    }

    public void setOrigin(int n, int n2) {
        this.mX = n;
        this.mY = n2;
    }

    public void setParent(ConstraintWidget constraintWidget) {
        this.mParent = constraintWidget;
    }

    void setRelativePositioning(int n, int n2) {
        if (n2 == 0) {
            this.mRelX = n;
        } else if (n2 == 1) {
            this.mRelY = n;
        }
    }

    public void setType(String string2) {
        this.mType = string2;
    }

    public void setVerticalBiasPercent(float f) {
        this.mVerticalBiasPercent = f;
    }

    public void setVerticalChainStyle(int n) {
        this.mVerticalChainStyle = n;
    }

    public void setVerticalDimension(int n, int n2) {
        this.mY = n;
        this.mHeight = n2 -= n;
        n = this.mMinHeight;
        if (n2 < n) {
            this.mHeight = n;
        }
    }

    public void setVerticalDimensionBehaviour(DimensionBehaviour dimensionBehaviour) {
        this.mListDimensionBehaviors[1] = dimensionBehaviour;
    }

    public void setVerticalMatchStyle(int n, int n2, int n3, float f) {
        this.mMatchConstraintDefaultHeight = n;
        this.mMatchConstraintMinHeight = n2;
        n2 = n3 == Integer.MAX_VALUE ? 0 : n3;
        this.mMatchConstraintMaxHeight = n2;
        this.mMatchConstraintPercentHeight = f;
        if (f > 0.0f && f < 1.0f && n == 0) {
            this.mMatchConstraintDefaultHeight = 2;
        }
    }

    public void setVerticalWeight(float f) {
        this.mWeight[1] = f;
    }

    public void setVisibility(int n) {
        this.mVisibility = n;
    }

    public void setWidth(int n) {
        this.mWidth = n;
        int n2 = this.mMinWidth;
        if (n < n2) {
            this.mWidth = n2;
        }
    }

    public void setWidthWrapContent(boolean bl) {
        this.mIsWidthWrapContent = bl;
    }

    public void setX(int n) {
        this.mX = n;
    }

    public void setY(int n) {
        this.mY = n;
    }

    public void setupDimensionRatio(boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        if (this.mResolvedDimensionRatioSide == -1) {
            if (bl3 && !bl4) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (!bl3 && bl4) {
                this.mResolvedDimensionRatioSide = 1;
                if (this.mDimensionRatioSide == -1) {
                    this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                }
            }
        }
        if (!(this.mResolvedDimensionRatioSide != 0 || this.mTop.isConnected() && this.mBottom.isConnected())) {
            this.mResolvedDimensionRatioSide = 1;
        } else if (!(this.mResolvedDimensionRatioSide != 1 || this.mLeft.isConnected() && this.mRight.isConnected())) {
            this.mResolvedDimensionRatioSide = 0;
        }
        if (!(this.mResolvedDimensionRatioSide != -1 || this.mTop.isConnected() && this.mBottom.isConnected() && this.mLeft.isConnected() && this.mRight.isConnected())) {
            if (this.mTop.isConnected() && this.mBottom.isConnected()) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (this.mLeft.isConnected() && this.mRight.isConnected()) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1) {
            int n = this.mMatchConstraintMinWidth;
            if (n > 0 && this.mMatchConstraintMinHeight == 0) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (n == 0 && this.mMatchConstraintMinHeight > 0) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        CharSequence charSequence = this.mType;
        String string2 = "";
        if (charSequence != null) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append("type: ");
            ((StringBuilder)charSequence).append(this.mType);
            ((StringBuilder)charSequence).append(" ");
            charSequence = ((StringBuilder)charSequence).toString();
        } else {
            charSequence = "";
        }
        stringBuilder.append((String)charSequence);
        charSequence = string2;
        if (this.mDebugName != null) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append("id: ");
            ((StringBuilder)charSequence).append(this.mDebugName);
            ((StringBuilder)charSequence).append(" ");
            charSequence = ((StringBuilder)charSequence).toString();
        }
        stringBuilder.append((String)charSequence);
        stringBuilder.append("(");
        stringBuilder.append(this.mX);
        stringBuilder.append(", ");
        stringBuilder.append(this.mY);
        stringBuilder.append(") - (");
        stringBuilder.append(this.mWidth);
        stringBuilder.append(" x ");
        stringBuilder.append(this.mHeight);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public void updateFromRuns(boolean bl, boolean bl2) {
        int n;
        int n2;
        int n3;
        int n4;
        boolean bl3;
        boolean bl4;
        block15: {
            block14: {
                bl4 = bl & this.horizontalRun.isResolved();
                bl3 = bl2 & this.verticalRun.isResolved();
                n4 = this.horizontalRun.start.value;
                n3 = this.verticalRun.start.value;
                n2 = this.horizontalRun.end.value;
                int n5 = this.verticalRun.end.value;
                if (n2 - n4 < 0 || n5 - n3 < 0 || n4 == Integer.MIN_VALUE || n4 == Integer.MAX_VALUE || n3 == Integer.MIN_VALUE || n3 == Integer.MAX_VALUE || n2 == Integer.MIN_VALUE || n2 == Integer.MAX_VALUE || n5 == Integer.MIN_VALUE) break block14;
                n = n5;
                if (n5 != Integer.MAX_VALUE) break block15;
            }
            n4 = 0;
            n3 = 0;
            n2 = 0;
            n = 0;
        }
        n2 -= n4;
        n -= n3;
        if (bl4) {
            this.mX = n4;
        }
        if (bl3) {
            this.mY = n3;
        }
        if (this.mVisibility == 8) {
            this.mWidth = 0;
            this.mHeight = 0;
            return;
        }
        if (bl4) {
            n3 = n2;
            if (this.mListDimensionBehaviors[0] == DimensionBehaviour.FIXED) {
                n3 = n2;
                if (n2 < this.mWidth) {
                    n3 = this.mWidth;
                }
            }
            this.mWidth = n3;
            n4 = this.mMinWidth;
            if (n3 < n4) {
                this.mWidth = n4;
            }
        }
        if (bl3) {
            n3 = n;
            if (this.mListDimensionBehaviors[1] == DimensionBehaviour.FIXED) {
                n3 = n;
                if (n < this.mHeight) {
                    n3 = this.mHeight;
                }
            }
            this.mHeight = n3;
            n4 = this.mMinHeight;
            if (n3 < n4) {
                this.mHeight = n4;
            }
        }
    }

    public void updateFromSolver(LinearSystem object, boolean bl) {
        int n;
        int n2;
        int n3;
        int n4;
        block12: {
            block11: {
                n4 = ((LinearSystem)object).getObjectVariableValue(this.mLeft);
                int n5 = ((LinearSystem)object).getObjectVariableValue(this.mTop);
                int n6 = ((LinearSystem)object).getObjectVariableValue(this.mRight);
                n3 = ((LinearSystem)object).getObjectVariableValue(this.mBottom);
                n2 = n4;
                n = n6;
                if (bl) {
                    object = this.horizontalRun;
                    n2 = n4;
                    n = n6;
                    if (object != null) {
                        n2 = n4;
                        n = n6;
                        if (((HorizontalWidgetRun)object).start.resolved) {
                            n2 = n4;
                            n = n6;
                            if (this.horizontalRun.end.resolved) {
                                n2 = this.horizontalRun.start.value;
                                n = this.horizontalRun.end.value;
                            }
                        }
                    }
                }
                n4 = n5;
                n6 = n3;
                if (bl) {
                    object = this.verticalRun;
                    n4 = n5;
                    n6 = n3;
                    if (object != null) {
                        n4 = n5;
                        n6 = n3;
                        if (((VerticalWidgetRun)object).start.resolved) {
                            n4 = n5;
                            n6 = n3;
                            if (this.verticalRun.end.resolved) {
                                n4 = this.verticalRun.start.value;
                                n6 = this.verticalRun.end.value;
                            }
                        }
                    }
                }
                if (n - n2 < 0 || n6 - n4 < 0 || n2 == Integer.MIN_VALUE || n2 == Integer.MAX_VALUE || n4 == Integer.MIN_VALUE || n4 == Integer.MAX_VALUE || n == Integer.MIN_VALUE || n == Integer.MAX_VALUE || n6 == Integer.MIN_VALUE) break block11;
                n3 = n;
                n = n6;
                if (n6 != Integer.MAX_VALUE) break block12;
            }
            n2 = 0;
            n4 = 0;
            n3 = 0;
            n = 0;
        }
        this.setFrame(n2, n4, n3, n);
    }

    public static final class DimensionBehaviour
    extends Enum<DimensionBehaviour> {
        private static final DimensionBehaviour[] $VALUES;
        public static final /* enum */ DimensionBehaviour FIXED;
        public static final /* enum */ DimensionBehaviour MATCH_CONSTRAINT;
        public static final /* enum */ DimensionBehaviour MATCH_PARENT;
        public static final /* enum */ DimensionBehaviour WRAP_CONTENT;

        static {
            DimensionBehaviour dimensionBehaviour;
            DimensionBehaviour dimensionBehaviour2;
            DimensionBehaviour dimensionBehaviour3;
            DimensionBehaviour dimensionBehaviour4;
            FIXED = dimensionBehaviour4 = new DimensionBehaviour();
            WRAP_CONTENT = dimensionBehaviour3 = new DimensionBehaviour();
            MATCH_CONSTRAINT = dimensionBehaviour2 = new DimensionBehaviour();
            MATCH_PARENT = dimensionBehaviour = new DimensionBehaviour();
            $VALUES = new DimensionBehaviour[]{dimensionBehaviour4, dimensionBehaviour3, dimensionBehaviour2, dimensionBehaviour};
        }

        public static DimensionBehaviour valueOf(String string2) {
            return Enum.valueOf(DimensionBehaviour.class, string2);
        }

        public static DimensionBehaviour[] values() {
            return (DimensionBehaviour[])$VALUES.clone();
        }
    }
}

