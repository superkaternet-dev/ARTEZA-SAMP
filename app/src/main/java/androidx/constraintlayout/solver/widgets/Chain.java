/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.widgets.ChainHead;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import java.util.ArrayList;

public class Chain {
    private static final boolean DEBUG = false;
    public static final boolean USE_CHAIN_OPTIMIZATION = false;

    /*
     * Unable to fully structure code
     */
    static void applyChainConstraints(ConstraintWidgetContainer var0, LinearSystem var1_1, int var2_2, int var3_3, ChainHead var4_4) {
        block77: {
            block76: {
                block74: {
                    block75: {
                        var21_5 = var4_4.mFirst;
                        var25_6 = var4_4.mLast;
                        var18_7 = var4_4.mFirstVisibleWidget;
                        var26_8 = var4_4.mLastVisibleWidget;
                        var23_9 = var4_4.mHead;
                        var5_10 = var4_4.mTotalWeight;
                        var22_11 = var4_4.mFirstMatchConstraintWidget;
                        var20_12 = var4_4.mLastMatchConstraintWidget;
                        var13_13 = var0.mListDimensionBehaviors[var2_2] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT ? 1 : 0;
                        if (var2_2 == 0) {
                            var8_14 = var23_9.mHorizontalChainStyle == 0 ? 1 : 0;
                            var9_15 = var23_9.mHorizontalChainStyle;
                            var11_16 = var8_14;
                            var8_14 = var9_15 == 1 ? 1 : 0;
                            var9_15 = var23_9.mHorizontalChainStyle == 2 ? 1 : 0;
                            var10_17 = 0;
                            var17_18 = var21_5;
                            var12_19 = var8_14;
                            var8_14 = var10_17;
                            var14_20 = var9_15;
                        } else {
                            var8_14 = var23_9.mVerticalChainStyle == 0 ? 1 : 0;
                            var9_15 = var23_9.mVerticalChainStyle;
                            var11_16 = var8_14;
                            var8_14 = var9_15 == 1 ? 1 : 0;
                            var9_15 = var23_9.mVerticalChainStyle == 2 ? 1 : 0;
                            var10_17 = 0;
                            var17_18 = var21_5;
                            var12_19 = var8_14;
                            var14_20 = var9_15;
                            var8_14 = var10_17;
                        }
                        while (var8_14 == 0) {
                            var19_23 = var17_18.mListAnchors[var3_3];
                            var9_15 = 4;
                            if (var14_20 != 0) {
                                var9_15 = 1;
                            }
                            var16_22 = var19_23.getMargin();
                            var15_21 = var17_18.mListDimensionBehaviors[var2_2] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && var17_18.mResolvedMatchConstraintDefault[var2_2] == 0;
                            if (var19_23.mTarget != null && var17_18 != var21_5) {
                                var16_22 += var19_23.mTarget.getMargin();
                            }
                            var10_17 = var9_15;
                            if (var14_20 != 0) {
                                var10_17 = var9_15;
                                if (var17_18 != var21_5) {
                                    var10_17 = var9_15;
                                    if (var17_18 != var18_7) {
                                        var10_17 = 8;
                                    }
                                }
                            }
                            if (var19_23.mTarget != null) {
                                if (var17_18 == var18_7) {
                                    var1_1.addGreaterThan(var19_23.mSolverVariable, var19_23.mTarget.mSolverVariable, var16_22, 6);
                                } else {
                                    var1_1.addGreaterThan(var19_23.mSolverVariable, var19_23.mTarget.mSolverVariable, var16_22, 8);
                                }
                                if (var15_21 && var14_20 == 0) {
                                    var10_17 = 5;
                                }
                                var1_1.addEquality(var19_23.mSolverVariable, var19_23.mTarget.mSolverVariable, var16_22, var10_17);
                            }
                            if (var13_13 != 0) {
                                if (var17_18.getVisibility() != 8 && var17_18.mListDimensionBehaviors[var2_2] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                                    var1_1.addGreaterThan(var17_18.mListAnchors[var3_3 + 1].mSolverVariable, var17_18.mListAnchors[var3_3].mSolverVariable, 0, 5);
                                }
                                var1_1.addGreaterThan(var17_18.mListAnchors[var3_3].mSolverVariable, var0.mListAnchors[var3_3].mSolverVariable, 0, 8);
                            }
                            if ((var19_23 = var17_18.mListAnchors[var3_3 + 1].mTarget) != null) {
                                var19_23 = var19_23.mOwner;
                                if (var19_23.mListAnchors[var3_3].mTarget == null || var19_23.mListAnchors[var3_3].mTarget.mOwner != var17_18) {
                                    var19_23 = null;
                                }
                            } else {
                                var19_23 = null;
                            }
                            if (var19_23 != null) {
                                var17_18 = var19_23;
                                continue;
                            }
                            var8_14 = 1;
                        }
                        if (var26_8 != null && var25_6.mListAnchors[var3_3 + 1].mTarget != null) {
                            var19_23 = var26_8.mListAnchors[var3_3 + 1];
                            var8_14 = var26_8.mListDimensionBehaviors[var2_2] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && var26_8.mResolvedMatchConstraintDefault[var2_2] == 0 ? 1 : 0;
                            if (var8_14 != 0 && var14_20 == 0 && var19_23.mTarget.mOwner == var0) {
                                var1_1.addEquality(var19_23.mSolverVariable, var19_23.mTarget.mSolverVariable, -var19_23.getMargin(), 5);
                            } else if (var14_20 != 0 && var19_23.mTarget.mOwner == var0) {
                                var1_1.addEquality(var19_23.mSolverVariable, var19_23.mTarget.mSolverVariable, -var19_23.getMargin(), 4);
                            }
                            var1_1.addLowerThan(var19_23.mSolverVariable, var25_6.mListAnchors[var3_3 + 1].mTarget.mSolverVariable, -var19_23.getMargin(), 6);
                        }
                        if (var13_13 != 0) {
                            var1_1.addGreaterThan(var0.mListAnchors[var3_3 + 1].mSolverVariable, var25_6.mListAnchors[var3_3 + 1].mSolverVariable, var25_6.mListAnchors[var3_3 + 1].getMargin(), 8);
                        }
                        if ((var0 = var4_4.mWeightedMatchConstraintsWidgets) == null) break block74;
                        var8_14 = var0.size();
                        if (var8_14 <= 1) break block75;
                        var20_12 = null;
                        var7_24 = 0.0f;
                        var6_25 = var5_10;
                        if (var4_4.mHasUndefinedWeights) {
                            var6_25 = var5_10;
                            if (!var4_4.mHasComplexMatchWeights) {
                                var6_25 = var4_4.mWidgetsMatchCount;
                            }
                        }
                        var19_23 = var22_11;
                        for (var9_15 = 0; var9_15 < var8_14; ++var9_15) {
                            var22_11 = (ConstraintWidget)var0.get(var9_15);
                            var5_10 = var22_11.mWeight[var2_2];
                            if (!(var5_10 < 0.0f)) ** GOTO lbl106
                            if (var4_4.mHasComplexMatchWeights) {
                                var1_1.addEquality(var22_11.mListAnchors[var3_3 + 1].mSolverVariable, var22_11.mListAnchors[var3_3].mSolverVariable, 0, 4);
                                var5_10 = var7_24;
                            } else {
                                var5_10 = 1.0f;
lbl106:
                                // 2 sources

                                if (var5_10 == 0.0f) {
                                    var1_1.addEquality(var22_11.mListAnchors[var3_3 + 1].mSolverVariable, var22_11.mListAnchors[var3_3].mSolverVariable, 0, 8);
                                    var5_10 = var7_24;
                                } else {
                                    if (var20_12 != null) {
                                        var24_26 = var20_12.mListAnchors[var3_3].mSolverVariable;
                                        var27_27 = var20_12.mListAnchors[var3_3 + 1].mSolverVariable;
                                        var28_28 = var22_11.mListAnchors[var3_3].mSolverVariable;
                                        var20_12 = var22_11.mListAnchors[var3_3 + 1].mSolverVariable;
                                        var29_29 = var1_1.createRow();
                                        var29_29.createRowEqualMatchDimensions(var7_24, var6_25, var5_10, (SolverVariable)var24_26, var27_27, var28_28, (SolverVariable)var20_12);
                                        var1_1.addConstraint(var29_29);
                                    }
                                    var20_12 = var22_11;
                                }
                            }
                            var7_24 = var5_10;
                        }
                        var0 = var17_18;
                        break block76;
                    }
                    var0 = var17_18;
                    break block76;
                }
                var19_23 = var0;
                var0 = var17_18;
                var0 = var19_23;
            }
            if (var18_7 != null && (var18_7 == var26_8 || var14_20 != 0)) {
                var0 = var21_5.mListAnchors[var3_3];
                var4_4 = var25_6.mListAnchors[var3_3 + 1];
                var0 = var0.mTarget != null ? var0.mTarget.mSolverVariable : null;
                var4_4 = var4_4.mTarget != null ? var4_4.mTarget.mSolverVariable : null;
                var17_18 = var18_7.mListAnchors[var3_3];
                var19_23 = var26_8.mListAnchors[var3_3 + 1];
                if (var0 != null && var4_4 != null) {
                    var5_10 = var2_2 == 0 ? var23_9.mHorizontalBiasPercent : var23_9.mVerticalBiasPercent;
                    var8_14 = var17_18.getMargin();
                    var2_2 = var19_23.getMargin();
                    var1_1.addCentering(var17_18.mSolverVariable, (SolverVariable)var0, var8_14, var5_10, (SolverVariable)var4_4, var19_23.mSolverVariable, var2_2, 7);
                }
            } else if (var11_16 != 0 && var18_7 != null) {
                var10_17 = var4_4.mWidgetsMatchCount > 0 && var4_4.mWidgetsCount == var4_4.mWidgetsMatchCount ? 1 : 0;
                var17_18 = var18_7;
                var0 = var18_7;
                while (var17_18 != null) {
                    var4_4 = var17_18.mNextChainWidget[var2_2];
                    while (var4_4 != null && var4_4.getVisibility() == 8) {
                        var4_4 = var4_4.mNextChainWidget[var2_2];
                    }
                    if (var4_4 != null || var17_18 == var26_8) {
                        var22_11 = var17_18.mListAnchors[var3_3];
                        var27_27 = var22_11.mSolverVariable;
                        var19_23 = var22_11.mTarget != null ? var22_11.mTarget.mSolverVariable : null;
                        if (var0 != var17_18) {
                            var19_23 = var0.mListAnchors[var3_3 + 1].mSolverVariable;
                        } else if (var17_18 == var18_7 && var0 == var17_18) {
                            var19_23 = var21_5.mListAnchors[var3_3].mTarget != null ? var21_5.mListAnchors[var3_3].mTarget.mSolverVariable : null;
                        }
                        var20_12 = null;
                        var13_13 = var22_11.getMargin();
                        var9_15 = var17_18.mListAnchors[var3_3 + 1].getMargin();
                        if (var4_4 != null) {
                            var20_12 = var4_4.mListAnchors[var3_3];
                            var22_11 = var20_12.mSolverVariable;
                            var23_9 = var17_18.mListAnchors[var3_3 + 1].mSolverVariable;
                            var24_26 = var20_12;
                            var20_12 = var22_11;
                        } else {
                            var22_11 = var25_6.mListAnchors[var3_3 + 1].mTarget;
                            if (var22_11 != null) {
                                var20_12 = var22_11.mSolverVariable;
                            }
                            var23_9 = var17_18.mListAnchors[var3_3 + 1].mSolverVariable;
                            var24_26 = var22_11;
                        }
                        var8_14 = var9_15;
                        if (var24_26 != null) {
                            var8_14 = var9_15 + var24_26.getMargin();
                        }
                        var9_15 = var13_13;
                        if (var0 != null) {
                            var9_15 = var13_13 + var0.mListAnchors[var3_3 + 1].getMargin();
                        }
                        if (var27_27 != null && var19_23 != null && var20_12 != null && var23_9 != null) {
                            if (var17_18 == var18_7) {
                                var9_15 = var18_7.mListAnchors[var3_3].getMargin();
                            }
                            if (var17_18 == var26_8) {
                                var8_14 = var26_8.mListAnchors[var3_3 + 1].getMargin();
                            }
                            var13_13 = var10_17 != 0 ? 8 : 5;
                            var1_1.addCentering(var27_27, (SolverVariable)var19_23, var9_15, 0.5f, (SolverVariable)var20_12, (SolverVariable)var23_9, var8_14, var13_13);
                        }
                    }
                    if (var17_18.getVisibility() != 8) {
                        var0 = var17_18;
                    }
                    var17_18 = var4_4;
                }
            } else if (var12_19 != 0 && var18_7 != null) {
                var8_14 = var4_4.mWidgetsMatchCount > 0 && var4_4.mWidgetsCount == var4_4.mWidgetsMatchCount ? 1 : 0;
                var17_18 = var18_7;
                var4_4 = var18_7;
                while (var17_18 != null) {
                    var0 = var17_18.mNextChainWidget[var2_2];
                    while (var0 != null && var0.getVisibility() == 8) {
                        var0 = var0.mNextChainWidget[var2_2];
                    }
                    if (var17_18 != var18_7 && var17_18 != var26_8 && var0 != null) {
                        if (var0 == var26_8) {
                            var0 = null;
                        }
                        var20_12 = var17_18.mListAnchors[var3_3];
                        var24_26 = var20_12.mSolverVariable;
                        if (var20_12.mTarget != null) {
                            var19_23 = var20_12.mTarget.mSolverVariable;
                        }
                        var27_27 = var4_4.mListAnchors[var3_3 + 1].mSolverVariable;
                        var19_23 = null;
                        var13_13 = var20_12.getMargin();
                        var10_17 = var17_18.mListAnchors[var3_3 + 1].getMargin();
                        if (var0 != null) {
                            var22_11 = var0.mListAnchors[var3_3];
                            var20_12 = var22_11.mSolverVariable;
                            var19_23 = var22_11.mTarget != null ? var22_11.mTarget.mSolverVariable : null;
                            var23_9 = var20_12;
                        } else {
                            var20_12 = var26_8.mListAnchors[var3_3];
                            if (var20_12 != null) {
                                var19_23 = var20_12.mSolverVariable;
                            }
                            var22_11 = var17_18.mListAnchors[var3_3 + 1].mSolverVariable;
                            var23_9 = var19_23;
                            var19_23 = var22_11;
                            var22_11 = var20_12;
                        }
                        var9_15 = var10_17;
                        if (var22_11 != null) {
                            var9_15 = var10_17 + var22_11.getMargin();
                        }
                        var10_17 = var13_13;
                        if (var4_4 != null) {
                            var10_17 = var13_13 + var4_4.mListAnchors[var3_3 + 1].getMargin();
                        }
                        var13_13 = var8_14 != 0 ? 8 : 4;
                        if (var24_26 != null && var27_27 != null && var23_9 != null && var19_23 != null) {
                            var1_1.addCentering((SolverVariable)var24_26, var27_27, var10_17, 0.5f, (SolverVariable)var23_9, (SolverVariable)var19_23, var9_15, var13_13);
                        }
                    }
                    if (var17_18.getVisibility() != 8) {
                        var4_4 = var17_18;
                    }
                    var17_18 = var0;
                }
                var0 = var18_7.mListAnchors[var3_3];
                var4_4 = var21_5.mListAnchors[var3_3].mTarget;
                var19_23 = var26_8.mListAnchors[var3_3 + 1];
                var17_18 = var25_6.mListAnchors[var3_3 + 1].mTarget;
                if (var4_4 != null) {
                    if (var18_7 != var26_8) {
                        var1_1.addEquality(var0.mSolverVariable, var4_4.mSolverVariable, var0.getMargin(), 5);
                    } else if (var17_18 != null) {
                        var1_1.addCentering(var0.mSolverVariable, var4_4.mSolverVariable, var0.getMargin(), 0.5f, var19_23.mSolverVariable, var17_18.mSolverVariable, var19_23.getMargin(), 5);
                    }
                }
                if (var17_18 != null && var18_7 != var26_8) {
                    var1_1.addEquality(var19_23.mSolverVariable, var17_18.mSolverVariable, -var19_23.getMargin(), 5);
                }
            }
            if (var11_16 == 0 && var12_19 == 0 || var18_7 == null || var18_7 == var26_8) break block77;
            var17_18 = var18_7.mListAnchors[var3_3];
            var19_23 = var26_8.mListAnchors[var3_3 + 1];
            var4_4 = var17_18.mTarget != null ? var17_18.mTarget.mSolverVariable : null;
            var0 = var19_23.mTarget != null ? var19_23.mTarget.mSolverVariable : null;
            if (var25_6 != var26_8) {
                var0 = var25_6.mListAnchors[var3_3 + 1];
                var0 = var0.mTarget != null ? var0.mTarget.mSolverVariable : null;
            }
            if (var18_7 == var26_8) {
                var17_18 = var18_7.mListAnchors[var3_3];
                var18_7 = var18_7.mListAnchors[var3_3 + 1];
            } else {
                var18_7 = var19_23;
            }
            if (var4_4 != null && var0 != null) {
                var2_2 = var17_18.getMargin();
                var19_23 = var26_8;
                if (var26_8 == null) {
                    var19_23 = var25_6;
                }
                var3_3 = var19_23.mListAnchors[var3_3 + 1].getMargin();
                var1_1.addCentering(var17_18.mSolverVariable, (SolverVariable)var4_4, var2_2, 0.5f, (SolverVariable)var0, var18_7.mSolverVariable, var3_3, 5);
            }
        }
    }

    public static void applyChainConstraints(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, ArrayList<ConstraintWidget> arrayList, int n) {
        ChainHead[] chainHeadArray;
        int n2;
        int n3;
        if (n == 0) {
            n3 = 0;
            n2 = constraintWidgetContainer.mHorizontalChainsSize;
            chainHeadArray = constraintWidgetContainer.mHorizontalChainsArray;
        } else {
            n3 = 2;
            n2 = constraintWidgetContainer.mVerticalChainsSize;
            chainHeadArray = constraintWidgetContainer.mVerticalChainsArray;
        }
        for (int i = 0; i < n2; ++i) {
            ChainHead chainHead = chainHeadArray[i];
            chainHead.define();
            if (arrayList != null && (arrayList == null || !arrayList.contains(chainHead.mFirst))) continue;
            Chain.applyChainConstraints(constraintWidgetContainer, linearSystem, n, n3, chainHead);
        }
    }
}

