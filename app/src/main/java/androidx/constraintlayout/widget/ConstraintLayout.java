/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources$NotFoundException
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.SparseArray
 *  android.util.SparseIntArray
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 */
package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.solver.Metrics;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.solver.widgets.Optimizer;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.constraintlayout.widget.ConstraintLayoutStates;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;
import androidx.constraintlayout.widget.ConstraintsChangedListener;
import androidx.constraintlayout.widget.Guideline;
import androidx.constraintlayout.widget.Placeholder;
import androidx.constraintlayout.widget.R;
import androidx.constraintlayout.widget.VirtualLayout;
import java.util.ArrayList;
import java.util.HashMap;

public class ConstraintLayout
extends ViewGroup {
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_DRAW_CONSTRAINTS = false;
    public static final int DESIGN_INFO_ID = 0;
    private static final boolean MEASURE = false;
    private static final String TAG = "ConstraintLayout";
    private static final boolean USE_CONSTRAINTS_HELPER = true;
    public static final String VERSION = "ConstraintLayout-2.0.4";
    SparseArray<View> mChildrenByIds = new SparseArray();
    private ArrayList<ConstraintHelper> mConstraintHelpers = new ArrayList(4);
    protected ConstraintLayoutStates mConstraintLayoutSpec = null;
    private ConstraintSet mConstraintSet = null;
    private int mConstraintSetId = -1;
    private ConstraintsChangedListener mConstraintsChangedListener;
    private HashMap<String, Integer> mDesignIds;
    protected boolean mDirtyHierarchy = true;
    private int mLastMeasureHeight = -1;
    int mLastMeasureHeightMode = 0;
    int mLastMeasureHeightSize = -1;
    private int mLastMeasureWidth = -1;
    int mLastMeasureWidthMode = 0;
    int mLastMeasureWidthSize = -1;
    protected ConstraintWidgetContainer mLayoutWidget = new ConstraintWidgetContainer();
    private int mMaxHeight;
    private int mMaxWidth = Integer.MAX_VALUE;
    Measurer mMeasurer;
    private Metrics mMetrics;
    private int mMinHeight = 0;
    private int mMinWidth = 0;
    private int mOnMeasureHeightMeasureSpec = 0;
    private int mOnMeasureWidthMeasureSpec = 0;
    private int mOptimizationLevel = 257;
    private SparseArray<ConstraintWidget> mTempMapIdToWidget;

    public ConstraintLayout(Context context) {
        super(context);
        this.mMaxHeight = Integer.MAX_VALUE;
        this.mDesignIds = new HashMap();
        this.mTempMapIdToWidget = new SparseArray();
        this.mMeasurer = new Measurer(this, this);
        this.init(null, 0, 0);
    }

    public ConstraintLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mMaxHeight = Integer.MAX_VALUE;
        this.mDesignIds = new HashMap();
        this.mTempMapIdToWidget = new SparseArray();
        this.mMeasurer = new Measurer(this, this);
        this.init(attributeSet, 0, 0);
    }

    public ConstraintLayout(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.mMaxHeight = Integer.MAX_VALUE;
        this.mDesignIds = new HashMap();
        this.mTempMapIdToWidget = new SparseArray();
        this.mMeasurer = new Measurer(this, this);
        this.init(attributeSet, n, 0);
    }

    public ConstraintLayout(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
        this.mMaxHeight = Integer.MAX_VALUE;
        this.mDesignIds = new HashMap();
        this.mTempMapIdToWidget = new SparseArray();
        this.mMeasurer = new Measurer(this, this);
        this.init(attributeSet, n, n2);
    }

    private int getPaddingWidth() {
        int n = Math.max(0, this.getPaddingLeft()) + Math.max(0, this.getPaddingRight());
        int n2 = 0;
        if (Build.VERSION.SDK_INT >= 17) {
            n2 = Math.max(0, this.getPaddingStart()) + Math.max(0, this.getPaddingEnd());
        }
        if (n2 > 0) {
            n = n2;
        }
        return n;
    }

    private final ConstraintWidget getTargetWidget(int n) {
        View view;
        if (n == 0) {
            return this.mLayoutWidget;
        }
        Object object = view = (View)this.mChildrenByIds.get(n);
        if (view == null) {
            object = view = this.findViewById(n);
            if (view != null) {
                object = view;
                if (view != this) {
                    object = view;
                    if (view.getParent() == this) {
                        this.onViewAdded(view);
                        object = view;
                    }
                }
            }
        }
        if (object == this) {
            return this.mLayoutWidget;
        }
        object = object == null ? null : ((LayoutParams)object.getLayoutParams()).widget;
        return object;
    }

    private void init(AttributeSet attributeSet, int n, int n2) {
        this.mLayoutWidget.setCompanionWidget((Object)this);
        this.mLayoutWidget.setMeasurer(this.mMeasurer);
        this.mChildrenByIds.put(this.getId(), (Object)this);
        this.mConstraintSet = null;
        if (attributeSet != null) {
            attributeSet = this.getContext().obtainStyledAttributes(attributeSet, R.styleable.ConstraintLayout_Layout, n, n2);
            n2 = attributeSet.getIndexCount();
            for (n = 0; n < n2; ++n) {
                int n3 = attributeSet.getIndex(n);
                if (n3 == R.styleable.ConstraintLayout_Layout_android_minWidth) {
                    this.mMinWidth = attributeSet.getDimensionPixelOffset(n3, this.mMinWidth);
                    continue;
                }
                if (n3 == R.styleable.ConstraintLayout_Layout_android_minHeight) {
                    this.mMinHeight = attributeSet.getDimensionPixelOffset(n3, this.mMinHeight);
                    continue;
                }
                if (n3 == R.styleable.ConstraintLayout_Layout_android_maxWidth) {
                    this.mMaxWidth = attributeSet.getDimensionPixelOffset(n3, this.mMaxWidth);
                    continue;
                }
                if (n3 == R.styleable.ConstraintLayout_Layout_android_maxHeight) {
                    this.mMaxHeight = attributeSet.getDimensionPixelOffset(n3, this.mMaxHeight);
                    continue;
                }
                if (n3 == R.styleable.ConstraintLayout_Layout_layout_optimizationLevel) {
                    this.mOptimizationLevel = attributeSet.getInt(n3, this.mOptimizationLevel);
                    continue;
                }
                if (n3 == R.styleable.ConstraintLayout_Layout_layoutDescription) {
                    if ((n3 = attributeSet.getResourceId(n3, 0)) == 0) continue;
                    try {
                        this.parseLayoutDescription(n3);
                    }
                    catch (Resources.NotFoundException notFoundException) {
                        this.mConstraintLayoutSpec = null;
                    }
                    continue;
                }
                if (n3 != R.styleable.ConstraintLayout_Layout_constraintSet) continue;
                n3 = attributeSet.getResourceId(n3, 0);
                try {
                    ConstraintSet constraintSet;
                    this.mConstraintSet = constraintSet = new ConstraintSet();
                    constraintSet.load(this.getContext(), n3);
                }
                catch (Resources.NotFoundException notFoundException) {
                    this.mConstraintSet = null;
                }
                this.mConstraintSetId = n3;
            }
            attributeSet.recycle();
        }
        this.mLayoutWidget.setOptimizationLevel(this.mOptimizationLevel);
    }

    private void markHierarchyDirty() {
        this.mDirtyHierarchy = true;
        this.mLastMeasureWidth = -1;
        this.mLastMeasureHeight = -1;
        this.mLastMeasureWidthSize = -1;
        this.mLastMeasureHeightSize = -1;
        this.mLastMeasureWidthMode = 0;
        this.mLastMeasureHeightMode = 0;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void setChildrenConstraints() {
        int n;
        Object object;
        int n2;
        boolean bl = this.isInEditMode();
        int n3 = this.getChildCount();
        for (n2 = 0; n2 < n3; ++n2) {
            object = this.getViewWidget(this.getChildAt(n2));
            if (object == null) continue;
            ((ConstraintWidget)object).reset();
        }
        if (bl) {
            for (n2 = 0; n2 < n3; ++n2) {
                View view = this.getChildAt(n2);
                try {
                    String string2 = this.getResources().getResourceName(view.getId());
                    this.setDesignInformation(0, string2, view.getId());
                    n = string2.indexOf(47);
                    object = string2;
                    if (n != -1) {
                        object = string2.substring(n + 1);
                    }
                    this.getTargetWidget(view.getId()).setDebugName((String)object);
                    continue;
                }
                catch (Resources.NotFoundException notFoundException) {
                    // empty catch block
                }
            }
        }
        if (this.mConstraintSetId != -1) {
            for (n2 = 0; n2 < n3; ++n2) {
                object = this.getChildAt(n2);
                if (object.getId() != this.mConstraintSetId || !(object instanceof Constraints)) continue;
                this.mConstraintSet = ((Constraints)((Object)object)).getConstraintSet();
            }
        }
        if ((object = this.mConstraintSet) != null) {
            ((ConstraintSet)object).applyToInternal(this, true);
        }
        this.mLayoutWidget.removeAllChildren();
        n = this.mConstraintHelpers.size();
        if (n > 0) {
            for (n2 = 0; n2 < n; ++n2) {
                this.mConstraintHelpers.get(n2).updatePreLayout(this);
            }
        }
        for (n2 = 0; n2 < n3; ++n2) {
            object = this.getChildAt(n2);
            if (!(object instanceof Placeholder)) continue;
            ((Placeholder)((Object)object)).updatePreLayout(this);
        }
        this.mTempMapIdToWidget.clear();
        this.mTempMapIdToWidget.put(0, (Object)this.mLayoutWidget);
        this.mTempMapIdToWidget.put(this.getId(), (Object)this.mLayoutWidget);
        for (n2 = 0; n2 < n3; ++n2) {
            object = this.getChildAt(n2);
            ConstraintWidget constraintWidget = this.getViewWidget((View)object);
            this.mTempMapIdToWidget.put(object.getId(), (Object)constraintWidget);
        }
        n2 = 0;
        while (n2 < n3) {
            object = this.getChildAt(n2);
            ConstraintWidget constraintWidget = this.getViewWidget((View)object);
            if (constraintWidget != null) {
                LayoutParams layoutParams = (LayoutParams)object.getLayoutParams();
                this.mLayoutWidget.add(constraintWidget);
                this.applyConstraintsFromLayoutParams(bl, (View)object, constraintWidget, layoutParams, this.mTempMapIdToWidget);
            }
            ++n2;
        }
        return;
    }

    private boolean updateHierarchy() {
        boolean bl;
        int n = this.getChildCount();
        boolean bl2 = false;
        int n2 = 0;
        while (true) {
            bl = bl2;
            if (n2 >= n) break;
            if (this.getChildAt(n2).isLayoutRequested()) {
                bl = true;
                break;
            }
            ++n2;
        }
        if (bl) {
            this.setChildrenConstraints();
        }
        return bl;
    }

    public void addView(View view, int n, ViewGroup.LayoutParams layoutParams) {
        super.addView(view, n, layoutParams);
        if (Build.VERSION.SDK_INT < 14) {
            this.onViewAdded(view);
        }
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    protected void applyConstraintsFromLayoutParams(boolean var1_1, View var2_2, ConstraintWidget var3_3, LayoutParams var4_4, SparseArray<ConstraintWidget> var5_5) {
        block55: {
            block56: {
                block54: {
                    var4_4.validate();
                    var4_4.helped = false;
                    var3_3.setVisibility(var2_2 /* !! */ .getVisibility());
                    if (var4_4.isInPlaceholder) {
                        var3_3.setInPlaceholder(true);
                        var3_3.setVisibility(8);
                    }
                    var3_3.setCompanionWidget(var2_2 /* !! */ );
                    if (var2_2 /* !! */  instanceof ConstraintHelper) {
                        ((ConstraintHelper)var2_2 /* !! */ ).resolveRtl(var3_3, this.mLayoutWidget.isRtl());
                    }
                    if (!var4_4.isGuideline) break block54;
                    var2_2 /* !! */  = (androidx.constraintlayout.solver.widgets.Guideline)var3_3;
                    var8_6 = var4_4.resolvedGuideBegin;
                    var7_8 = var4_4.resolvedGuideEnd;
                    var6_10 = var4_4.resolvedGuidePercent;
                    if (Build.VERSION.SDK_INT < 17) {
                        var8_6 = var4_4.guideBegin;
                        var7_8 = var4_4.guideEnd;
                        var6_10 = var4_4.guidePercent;
                    }
                    if (var6_10 != -1.0f) {
                        var2_2 /* !! */ .setGuidePercent(var6_10);
                    } else if (var8_6 != -1) {
                        var2_2 /* !! */ .setGuideBegin(var8_6);
                    } else if (var7_8 != -1) {
                        var2_2 /* !! */ .setGuideEnd(var7_8);
                    }
                    break block55;
                }
                var8_7 = var4_4.resolvedLeftToLeft;
                var9_12 = var4_4.resolvedLeftToRight;
                var10_13 = var4_4.resolvedRightToLeft;
                var7_9 = var4_4.resolvedRightToRight;
                var11_14 = var4_4.resolveGoneLeftMargin;
                var12_15 = var4_4.resolveGoneRightMargin;
                var6_11 = var4_4.resolvedHorizontalBias;
                if (Build.VERSION.SDK_INT >= 17) break block56;
                var8_7 = var4_4.leftToLeft;
                var7_9 = var4_4.leftToRight;
                var11_14 = var4_4.rightToLeft;
                var12_15 = var4_4.rightToRight;
                var10_13 = var4_4.goneLeftMargin;
                var9_12 = var4_4.goneRightMargin;
                var6_11 = var4_4.horizontalBias;
                if (var8_7 == -1 && var7_9 == -1) {
                    if (var4_4.startToStart != -1) {
                        var8_7 = var4_4.startToStart;
                    } else if (var4_4.startToEnd != -1) {
                        var7_9 = var4_4.startToEnd;
                    }
                }
                if (var11_14 != -1 || var12_15 != -1) ** GOTO lbl-1000
                if (var4_4.endToStart != -1) {
                    var14_16 = var4_4.endToStart;
                    var11_14 = var10_13;
                    var13_19 = var9_12;
                    var9_12 = var7_9;
                    var7_9 = var12_15;
                    var10_13 = var14_16;
                    var12_15 = var13_19;
                } else if (var4_4.endToEnd != -1) {
                    var14_17 = var4_4.endToEnd;
                    var13_20 = var10_13;
                    var12_15 = var9_12;
                    var9_12 = var7_9;
                    var7_9 = var14_17;
                    var10_13 = var11_14;
                    var11_14 = var13_20;
                } else lbl-1000:
                // 2 sources

                {
                    var13_21 = var10_13;
                    var14_18 = var9_12;
                    var9_12 = var7_9;
                    var7_9 = var12_15;
                    var10_13 = var11_14;
                    var11_14 = var13_21;
                    var12_15 = var14_18;
                }
            }
            if (var4_4.circleConstraint != -1) {
                var2_2 /* !! */  = (ConstraintWidget)var5_5.get(var4_4.circleConstraint);
                if (var2_2 /* !! */  != null) {
                    var3_3.connectCircularConstraint((ConstraintWidget)var2_2 /* !! */ , var4_4.circleAngle, var4_4.circleRadius);
                }
            } else {
                if (var8_7 != -1) {
                    var2_2 /* !! */  = (ConstraintWidget)var5_5.get(var8_7);
                    if (var2_2 /* !! */  != null) {
                        var3_3.immediateConnect(ConstraintAnchor.Type.LEFT, (ConstraintWidget)var2_2 /* !! */ , ConstraintAnchor.Type.LEFT, var4_4.leftMargin, var11_14);
                    }
                } else if (var9_12 != -1 && (var2_2 /* !! */  = (ConstraintWidget)var5_5.get(var9_12)) != null) {
                    var3_3.immediateConnect(ConstraintAnchor.Type.LEFT, (ConstraintWidget)var2_2 /* !! */ , ConstraintAnchor.Type.RIGHT, var4_4.leftMargin, var11_14);
                }
                if (var10_13 != -1) {
                    var2_2 /* !! */  = (ConstraintWidget)var5_5.get(var10_13);
                    if (var2_2 /* !! */  != null) {
                        var3_3.immediateConnect(ConstraintAnchor.Type.RIGHT, (ConstraintWidget)var2_2 /* !! */ , ConstraintAnchor.Type.LEFT, var4_4.rightMargin, var12_15);
                    }
                } else if (var7_9 != -1 && (var2_2 /* !! */  = (ConstraintWidget)var5_5.get(var7_9)) != null) {
                    var3_3.immediateConnect(ConstraintAnchor.Type.RIGHT, (ConstraintWidget)var2_2 /* !! */ , ConstraintAnchor.Type.RIGHT, var4_4.rightMargin, var12_15);
                }
                if (var4_4.topToTop != -1) {
                    var2_2 /* !! */  = (ConstraintWidget)var5_5.get(var4_4.topToTop);
                    if (var2_2 /* !! */  != null) {
                        var3_3.immediateConnect(ConstraintAnchor.Type.TOP, (ConstraintWidget)var2_2 /* !! */ , ConstraintAnchor.Type.TOP, var4_4.topMargin, var4_4.goneTopMargin);
                    }
                } else if (var4_4.topToBottom != -1 && (var2_2 /* !! */  = (ConstraintWidget)var5_5.get(var4_4.topToBottom)) != null) {
                    var3_3.immediateConnect(ConstraintAnchor.Type.TOP, (ConstraintWidget)var2_2 /* !! */ , ConstraintAnchor.Type.BOTTOM, var4_4.topMargin, var4_4.goneTopMargin);
                }
                if (var4_4.bottomToTop != -1) {
                    var2_2 /* !! */  = (ConstraintWidget)var5_5.get(var4_4.bottomToTop);
                    if (var2_2 /* !! */  != null) {
                        var3_3.immediateConnect(ConstraintAnchor.Type.BOTTOM, (ConstraintWidget)var2_2 /* !! */ , ConstraintAnchor.Type.TOP, var4_4.bottomMargin, var4_4.goneBottomMargin);
                    }
                } else if (var4_4.bottomToBottom != -1 && (var2_2 /* !! */  = (ConstraintWidget)var5_5.get(var4_4.bottomToBottom)) != null) {
                    var3_3.immediateConnect(ConstraintAnchor.Type.BOTTOM, (ConstraintWidget)var2_2 /* !! */ , ConstraintAnchor.Type.BOTTOM, var4_4.bottomMargin, var4_4.goneBottomMargin);
                }
                if (var4_4.baselineToBaseline != -1) {
                    var2_2 /* !! */  = (View)this.mChildrenByIds.get(var4_4.baselineToBaseline);
                    if ((var5_5 = (ConstraintWidget)var5_5.get(var4_4.baselineToBaseline)) != null && var2_2 /* !! */  != null && var2_2 /* !! */ .getLayoutParams() instanceof LayoutParams) {
                        var2_2 /* !! */  = (LayoutParams)var2_2 /* !! */ .getLayoutParams();
                        var4_4.needsBaseline = true;
                        var2_2 /* !! */ .needsBaseline = true;
                        var3_3.getAnchor(ConstraintAnchor.Type.BASELINE).connect(var5_5.getAnchor(ConstraintAnchor.Type.BASELINE), 0, -1, true);
                        var3_3.setHasBaseline(true);
                        var2_2 /* !! */ .widget.setHasBaseline(true);
                        var3_3.getAnchor(ConstraintAnchor.Type.TOP).reset();
                        var3_3.getAnchor(ConstraintAnchor.Type.BOTTOM).reset();
                    }
                }
                if (var6_11 >= 0.0f) {
                    var3_3.setHorizontalBiasPercent(var6_11);
                }
                if (var4_4.verticalBias >= 0.0f) {
                    var3_3.setVerticalBiasPercent(var4_4.verticalBias);
                }
            }
            if (var1_1 && (var4_4.editorAbsoluteX != -1 || var4_4.editorAbsoluteY != -1)) {
                var3_3.setOrigin(var4_4.editorAbsoluteX, var4_4.editorAbsoluteY);
            }
            if (!var4_4.horizontalDimensionFixed) {
                if (var4_4.width == -1) {
                    if (var4_4.constrainedWidth) {
                        var3_3.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                    } else {
                        var3_3.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
                    }
                    var3_3.getAnchor((ConstraintAnchor.Type)ConstraintAnchor.Type.LEFT).mMargin = var4_4.leftMargin;
                    var3_3.getAnchor((ConstraintAnchor.Type)ConstraintAnchor.Type.RIGHT).mMargin = var4_4.rightMargin;
                } else {
                    var3_3.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                    var3_3.setWidth(0);
                }
            } else {
                var3_3.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                var3_3.setWidth(var4_4.width);
                if (var4_4.width == -2) {
                    var3_3.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
                }
            }
            if (!var4_4.verticalDimensionFixed) {
                if (var4_4.height == -1) {
                    if (var4_4.constrainedHeight) {
                        var3_3.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                    } else {
                        var3_3.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
                    }
                    var3_3.getAnchor((ConstraintAnchor.Type)ConstraintAnchor.Type.TOP).mMargin = var4_4.topMargin;
                    var3_3.getAnchor((ConstraintAnchor.Type)ConstraintAnchor.Type.BOTTOM).mMargin = var4_4.bottomMargin;
                } else {
                    var3_3.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                    var3_3.setHeight(0);
                }
            } else {
                var3_3.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                var3_3.setHeight(var4_4.height);
                if (var4_4.height == -2) {
                    var3_3.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
                }
            }
            var3_3.setDimensionRatio(var4_4.dimensionRatio);
            var3_3.setHorizontalWeight(var4_4.horizontalWeight);
            var3_3.setVerticalWeight(var4_4.verticalWeight);
            var3_3.setHorizontalChainStyle(var4_4.horizontalChainStyle);
            var3_3.setVerticalChainStyle(var4_4.verticalChainStyle);
            var3_3.setHorizontalMatchStyle(var4_4.matchConstraintDefaultWidth, var4_4.matchConstraintMinWidth, var4_4.matchConstraintMaxWidth, var4_4.matchConstraintPercentWidth);
            var3_3.setVerticalMatchStyle(var4_4.matchConstraintDefaultHeight, var4_4.matchConstraintMinHeight, var4_4.matchConstraintMaxHeight, var4_4.matchConstraintPercentHeight);
        }
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    protected void dispatchDraw(Canvas canvas) {
        int n;
        int n2;
        Paint paint = this.mConstraintHelpers;
        if (paint != null && (n2 = paint.size()) > 0) {
            for (n = 0; n < n2; ++n) {
                this.mConstraintHelpers.get(n).updatePreDraw(this);
            }
        }
        super.dispatchDraw(canvas);
        if (this.isInEditMode()) {
            n2 = this.getChildCount();
            float f = this.getWidth();
            float f2 = this.getHeight();
            for (n = 0; n < n2; ++n) {
                paint = this.getChildAt(n);
                if (paint.getVisibility() == 8 || (paint = paint.getTag()) == null || !(paint instanceof String) || ((String[])(paint = ((String)paint).split(","))).length != 4) continue;
                int n3 = Integer.parseInt(paint[0]);
                int n4 = Integer.parseInt(paint[1]);
                int n5 = Integer.parseInt(paint[2]);
                int n6 = Integer.parseInt(paint[3]);
                n3 = (int)((float)n3 / 1080.0f * f);
                n4 = (int)((float)n4 / 1920.0f * f2);
                n5 = (int)((float)n5 / 1080.0f * f);
                n6 = (int)((float)n6 / 1920.0f * f2);
                paint = new Paint();
                paint.setColor(-65536);
                canvas.drawLine((float)n3, (float)n4, (float)(n3 + n5), (float)n4, paint);
                canvas.drawLine((float)(n3 + n5), (float)n4, (float)(n3 + n5), (float)(n4 + n6), paint);
                canvas.drawLine((float)(n3 + n5), (float)(n4 + n6), (float)n3, (float)(n4 + n6), paint);
                canvas.drawLine((float)n3, (float)(n4 + n6), (float)n3, (float)n4, paint);
                paint.setColor(-16711936);
                canvas.drawLine((float)n3, (float)n4, (float)(n3 + n5), (float)(n4 + n6), paint);
                canvas.drawLine((float)n3, (float)(n4 + n6), (float)(n3 + n5), (float)n4, paint);
            }
        }
    }

    public void fillMetrics(Metrics metrics) {
        this.mMetrics = metrics;
        this.mLayoutWidget.fillMetrics(metrics);
    }

    public void forceLayout() {
        this.markHierarchyDirty();
        super.forceLayout();
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(this.getContext(), attributeSet);
    }

    public Object getDesignInformation(int n, Object object) {
        if (n == 0 && object instanceof String) {
            object = (String)object;
            HashMap<String, Integer> hashMap = this.mDesignIds;
            if (hashMap != null && hashMap.containsKey(object)) {
                return this.mDesignIds.get(object);
            }
        }
        return null;
    }

    public int getMaxHeight() {
        return this.mMaxHeight;
    }

    public int getMaxWidth() {
        return this.mMaxWidth;
    }

    public int getMinHeight() {
        return this.mMinHeight;
    }

    public int getMinWidth() {
        return this.mMinWidth;
    }

    public int getOptimizationLevel() {
        return this.mLayoutWidget.getOptimizationLevel();
    }

    public View getViewById(int n) {
        return (View)this.mChildrenByIds.get(n);
    }

    public final ConstraintWidget getViewWidget(View object) {
        if (object == this) {
            return this.mLayoutWidget;
        }
        object = object == null ? null : ((LayoutParams)object.getLayoutParams()).widget;
        return object;
    }

    protected boolean isRtl() {
        int n = Build.VERSION.SDK_INT;
        boolean bl = false;
        if (n >= 17) {
            n = (this.getContext().getApplicationInfo().flags & 0x400000) != 0 ? 1 : 0;
            boolean bl2 = bl;
            if (n != 0) {
                bl2 = bl;
                if (1 == this.getLayoutDirection()) {
                    bl2 = true;
                }
            }
            return bl2;
        }
        return false;
    }

    public void loadLayoutDescription(int n) {
        if (n != 0) {
            try {
                ConstraintLayoutStates constraintLayoutStates;
                this.mConstraintLayoutSpec = constraintLayoutStates = new ConstraintLayoutStates(this.getContext(), this, n);
            }
            catch (Resources.NotFoundException notFoundException) {
                this.mConstraintLayoutSpec = null;
            }
        } else {
            this.mConstraintLayoutSpec = null;
        }
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        n2 = this.getChildCount();
        bl = this.isInEditMode();
        for (n = 0; n < n2; ++n) {
            View view = this.getChildAt(n);
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            ConstraintWidget constraintWidget = layoutParams.widget;
            if (view.getVisibility() == 8 && !layoutParams.isGuideline && !layoutParams.isHelper && !layoutParams.isVirtualGroup && !bl || layoutParams.isInPlaceholder) continue;
            n3 = constraintWidget.getX();
            int n5 = constraintWidget.getY();
            n4 = constraintWidget.getWidth() + n3;
            int n6 = constraintWidget.getHeight() + n5;
            view.layout(n3, n5, n4, n6);
            if (!(view instanceof Placeholder) || (view = ((Placeholder)view).getContent()) == null) continue;
            view.setVisibility(0);
            view.layout(n3, n5, n4, n6);
        }
        n2 = this.mConstraintHelpers.size();
        if (n2 > 0) {
            for (n = 0; n < n2; ++n) {
                this.mConstraintHelpers.get(n).updatePostLayout(this);
            }
        }
    }

    protected void onMeasure(int n, int n2) {
        int n3;
        if (!this.mDirtyHierarchy) {
            int n4 = this.getChildCount();
            for (n3 = 0; n3 < n4; ++n3) {
                if (!this.getChildAt(n3).isLayoutRequested()) continue;
                this.mDirtyHierarchy = true;
                break;
            }
        }
        if (!this.mDirtyHierarchy) {
            n3 = this.mOnMeasureWidthMeasureSpec;
            if (n3 == n && this.mOnMeasureHeightMeasureSpec == n2) {
                this.resolveMeasuredDimension(n, n2, this.mLayoutWidget.getWidth(), this.mLayoutWidget.getHeight(), this.mLayoutWidget.isWidthMeasuredTooSmall(), this.mLayoutWidget.isHeightMeasuredTooSmall());
                return;
            }
            if (n3 == n && View.MeasureSpec.getMode((int)n) == 0x40000000 && View.MeasureSpec.getMode((int)n2) == Integer.MIN_VALUE && View.MeasureSpec.getMode((int)this.mOnMeasureHeightMeasureSpec) == Integer.MIN_VALUE && View.MeasureSpec.getSize((int)n2) >= this.mLayoutWidget.getHeight()) {
                this.mOnMeasureWidthMeasureSpec = n;
                this.mOnMeasureHeightMeasureSpec = n2;
                this.resolveMeasuredDimension(n, n2, this.mLayoutWidget.getWidth(), this.mLayoutWidget.getHeight(), this.mLayoutWidget.isWidthMeasuredTooSmall(), this.mLayoutWidget.isHeightMeasuredTooSmall());
                return;
            }
        }
        this.mOnMeasureWidthMeasureSpec = n;
        this.mOnMeasureHeightMeasureSpec = n2;
        this.mLayoutWidget.setRtl(this.isRtl());
        if (this.mDirtyHierarchy) {
            this.mDirtyHierarchy = false;
            if (this.updateHierarchy()) {
                this.mLayoutWidget.updateHierarchy();
            }
        }
        this.resolveSystem(this.mLayoutWidget, this.mOptimizationLevel, n, n2);
        this.resolveMeasuredDimension(n, n2, this.mLayoutWidget.getWidth(), this.mLayoutWidget.getHeight(), this.mLayoutWidget.isWidthMeasuredTooSmall(), this.mLayoutWidget.isHeightMeasuredTooSmall());
    }

    public void onViewAdded(View view) {
        if (Build.VERSION.SDK_INT >= 14) {
            super.onViewAdded(view);
        }
        Object object = this.getViewWidget(view);
        if (view instanceof Guideline && !(object instanceof androidx.constraintlayout.solver.widgets.Guideline)) {
            object = (LayoutParams)view.getLayoutParams();
            ((LayoutParams)((Object)object)).widget = new androidx.constraintlayout.solver.widgets.Guideline();
            ((LayoutParams)((Object)object)).isGuideline = true;
            ((androidx.constraintlayout.solver.widgets.Guideline)((LayoutParams)((Object)object)).widget).setOrientation(((LayoutParams)((Object)object)).orientation);
        }
        if (view instanceof ConstraintHelper) {
            object = (ConstraintHelper)view;
            ((ConstraintHelper)((Object)object)).validateParams();
            ((LayoutParams)view.getLayoutParams()).isHelper = true;
            if (!this.mConstraintHelpers.contains(object)) {
                this.mConstraintHelpers.add((ConstraintHelper)((Object)object));
            }
        }
        this.mChildrenByIds.put(view.getId(), (Object)view);
        this.mDirtyHierarchy = true;
    }

    public void onViewRemoved(View view) {
        if (Build.VERSION.SDK_INT >= 14) {
            super.onViewRemoved(view);
        }
        this.mChildrenByIds.remove(view.getId());
        ConstraintWidget constraintWidget = this.getViewWidget(view);
        this.mLayoutWidget.remove(constraintWidget);
        this.mConstraintHelpers.remove(view);
        this.mDirtyHierarchy = true;
    }

    protected void parseLayoutDescription(int n) {
        this.mConstraintLayoutSpec = new ConstraintLayoutStates(this.getContext(), this, n);
    }

    public void removeView(View view) {
        super.removeView(view);
        if (Build.VERSION.SDK_INT < 14) {
            this.onViewRemoved(view);
        }
    }

    public void requestLayout() {
        this.markHierarchyDirty();
        super.requestLayout();
    }

    protected void resolveMeasuredDimension(int n, int n2, int n3, int n4, boolean bl, boolean bl2) {
        int n5 = this.mMeasurer.paddingHeight;
        n3 += this.mMeasurer.paddingWidth;
        n4 += n5;
        if (Build.VERSION.SDK_INT >= 11) {
            n = ConstraintLayout.resolveSizeAndState((int)n3, (int)n, (int)0);
            n3 = ConstraintLayout.resolveSizeAndState((int)n4, (int)n2, (int)(0 << 16));
            n2 = Math.min(this.mMaxWidth, n & 0xFFFFFF);
            n3 = Math.min(this.mMaxHeight, n3 & 0xFFFFFF);
            n = n2;
            if (bl) {
                n = n2 | 0x1000000;
            }
            n2 = n3;
            if (bl2) {
                n2 = n3 | 0x1000000;
            }
            this.setMeasuredDimension(n, n2);
            this.mLastMeasureWidth = n;
            this.mLastMeasureHeight = n2;
        } else {
            this.setMeasuredDimension(n3, n4);
            this.mLastMeasureWidth = n3;
            this.mLastMeasureHeight = n4;
        }
    }

    protected void resolveSystem(ConstraintWidgetContainer constraintWidgetContainer, int n, int n2, int n3) {
        int n4 = View.MeasureSpec.getMode((int)n2);
        int n5 = View.MeasureSpec.getSize((int)n2);
        int n6 = View.MeasureSpec.getMode((int)n3);
        int n7 = View.MeasureSpec.getSize((int)n3);
        int n8 = Math.max(0, this.getPaddingTop());
        int n9 = Math.max(0, this.getPaddingBottom());
        int n10 = n8 + n9;
        int n11 = this.getPaddingWidth();
        this.mMeasurer.captureLayoutInfos(n2, n3, n8, n9, n11, n10);
        if (Build.VERSION.SDK_INT >= 17) {
            n2 = Math.max(0, this.getPaddingStart());
            n3 = Math.max(0, this.getPaddingEnd());
            if (n2 <= 0 && n3 <= 0) {
                n2 = Math.max(0, this.getPaddingLeft());
            } else if (this.isRtl()) {
                n2 = n3;
            }
        } else {
            n2 = Math.max(0, this.getPaddingLeft());
        }
        n3 = n5 - n11;
        n10 = n7 - n10;
        this.setSelfDimensionBehaviour(constraintWidgetContainer, n4, n3, n6, n10);
        constraintWidgetContainer.measure(n, n4, n3, n6, n10, this.mLastMeasureWidth, this.mLastMeasureHeight, n2, n8);
    }

    public void setConstraintSet(ConstraintSet constraintSet) {
        this.mConstraintSet = constraintSet;
    }

    public void setDesignInformation(int n, Object object, Object object2) {
        if (n == 0 && object instanceof String && object2 instanceof Integer) {
            if (this.mDesignIds == null) {
                this.mDesignIds = new HashMap();
            }
            String string2 = (String)object;
            n = string2.indexOf("/");
            object = string2;
            if (n != -1) {
                object = string2.substring(n + 1);
            }
            n = (Integer)object2;
            this.mDesignIds.put((String)object, n);
        }
    }

    public void setId(int n) {
        this.mChildrenByIds.remove(this.getId());
        super.setId(n);
        this.mChildrenByIds.put(this.getId(), (Object)this);
    }

    public void setMaxHeight(int n) {
        if (n == this.mMaxHeight) {
            return;
        }
        this.mMaxHeight = n;
        this.requestLayout();
    }

    public void setMaxWidth(int n) {
        if (n == this.mMaxWidth) {
            return;
        }
        this.mMaxWidth = n;
        this.requestLayout();
    }

    public void setMinHeight(int n) {
        if (n == this.mMinHeight) {
            return;
        }
        this.mMinHeight = n;
        this.requestLayout();
    }

    public void setMinWidth(int n) {
        if (n == this.mMinWidth) {
            return;
        }
        this.mMinWidth = n;
        this.requestLayout();
    }

    public void setOnConstraintsChanged(ConstraintsChangedListener constraintsChangedListener) {
        this.mConstraintsChangedListener = constraintsChangedListener;
        ConstraintLayoutStates constraintLayoutStates = this.mConstraintLayoutSpec;
        if (constraintLayoutStates != null) {
            constraintLayoutStates.setOnConstraintsChanged(constraintsChangedListener);
        }
    }

    public void setOptimizationLevel(int n) {
        this.mOptimizationLevel = n;
        this.mLayoutWidget.setOptimizationLevel(n);
    }

    protected void setSelfDimensionBehaviour(ConstraintWidgetContainer constraintWidgetContainer, int n, int n2, int n3, int n4) {
        ConstraintWidget.DimensionBehaviour dimensionBehaviour;
        int n5 = this.mMeasurer.paddingHeight;
        int n6 = this.mMeasurer.paddingWidth;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.FIXED;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour3 = ConstraintWidget.DimensionBehaviour.FIXED;
        int n7 = 0;
        int n8 = 0;
        int n9 = this.getChildCount();
        switch (n) {
            default: {
                n = n7;
                break;
            }
            case 0x40000000: {
                n = Math.min(this.mMaxWidth - n6, n2);
                break;
            }
            case 0: {
                dimensionBehaviour2 = dimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                n = n7;
                if (n9 != 0) break;
                n = Math.max(0, this.mMinWidth);
                dimensionBehaviour2 = dimensionBehaviour;
                break;
            }
            case -2147483648: {
                dimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                n = n2;
                dimensionBehaviour2 = dimensionBehaviour;
                if (n9 != 0) break;
                n = Math.max(0, this.mMinWidth);
                dimensionBehaviour2 = dimensionBehaviour;
            }
        }
        switch (n3) {
            default: {
                n2 = n8;
                break;
            }
            case 0x40000000: {
                n2 = Math.min(this.mMaxHeight - n5, n4);
                break;
            }
            case 0: {
                dimensionBehaviour3 = dimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                n2 = n8;
                if (n9 != 0) break;
                n2 = Math.max(0, this.mMinHeight);
                dimensionBehaviour3 = dimensionBehaviour;
                break;
            }
            case -2147483648: {
                dimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                n2 = n4;
                dimensionBehaviour3 = dimensionBehaviour;
                if (n9 != 0) break;
                n2 = Math.max(0, this.mMinHeight);
                dimensionBehaviour3 = dimensionBehaviour;
            }
        }
        if (n != constraintWidgetContainer.getWidth() || n2 != constraintWidgetContainer.getHeight()) {
            constraintWidgetContainer.invalidateMeasures();
        }
        constraintWidgetContainer.setX(0);
        constraintWidgetContainer.setY(0);
        constraintWidgetContainer.setMaxWidth(this.mMaxWidth - n6);
        constraintWidgetContainer.setMaxHeight(this.mMaxHeight - n5);
        constraintWidgetContainer.setMinWidth(0);
        constraintWidgetContainer.setMinHeight(0);
        constraintWidgetContainer.setHorizontalDimensionBehaviour(dimensionBehaviour2);
        constraintWidgetContainer.setWidth(n);
        constraintWidgetContainer.setVerticalDimensionBehaviour(dimensionBehaviour3);
        constraintWidgetContainer.setHeight(n2);
        constraintWidgetContainer.setMinWidth(this.mMinWidth - n6);
        constraintWidgetContainer.setMinHeight(this.mMinHeight - n5);
    }

    public void setState(int n, int n2, int n3) {
        ConstraintLayoutStates constraintLayoutStates = this.mConstraintLayoutSpec;
        if (constraintLayoutStates != null) {
            constraintLayoutStates.updateConstraints(n, n2, n3);
        }
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public static class LayoutParams
    extends ViewGroup.MarginLayoutParams {
        public static final int BASELINE = 5;
        public static final int BOTTOM = 4;
        public static final int CHAIN_PACKED = 2;
        public static final int CHAIN_SPREAD = 0;
        public static final int CHAIN_SPREAD_INSIDE = 1;
        public static final int END = 7;
        public static final int HORIZONTAL = 0;
        public static final int LEFT = 1;
        public static final int MATCH_CONSTRAINT = 0;
        public static final int MATCH_CONSTRAINT_PERCENT = 2;
        public static final int MATCH_CONSTRAINT_SPREAD = 0;
        public static final int MATCH_CONSTRAINT_WRAP = 1;
        public static final int PARENT_ID = 0;
        public static final int RIGHT = 2;
        public static final int START = 6;
        public static final int TOP = 3;
        public static final int UNSET = -1;
        public static final int VERTICAL = 1;
        public int baselineToBaseline = -1;
        public int bottomToBottom = -1;
        public int bottomToTop = -1;
        public float circleAngle = 0.0f;
        public int circleConstraint = -1;
        public int circleRadius = 0;
        public boolean constrainedHeight = false;
        public boolean constrainedWidth = false;
        public String constraintTag = null;
        public String dimensionRatio = null;
        int dimensionRatioSide = 1;
        float dimensionRatioValue = 0.0f;
        public int editorAbsoluteX = -1;
        public int editorAbsoluteY = -1;
        public int endToEnd = -1;
        public int endToStart = -1;
        public int goneBottomMargin = -1;
        public int goneEndMargin = -1;
        public int goneLeftMargin = -1;
        public int goneRightMargin = -1;
        public int goneStartMargin = -1;
        public int goneTopMargin = -1;
        public int guideBegin = -1;
        public int guideEnd = -1;
        public float guidePercent = -1.0f;
        public boolean helped = false;
        public float horizontalBias = 0.5f;
        public int horizontalChainStyle = 0;
        boolean horizontalDimensionFixed = true;
        public float horizontalWeight = -1.0f;
        boolean isGuideline = false;
        boolean isHelper = false;
        boolean isInPlaceholder = false;
        boolean isVirtualGroup = false;
        public int leftToLeft = -1;
        public int leftToRight = -1;
        public int matchConstraintDefaultHeight = 0;
        public int matchConstraintDefaultWidth = 0;
        public int matchConstraintMaxHeight = 0;
        public int matchConstraintMaxWidth = 0;
        public int matchConstraintMinHeight = 0;
        public int matchConstraintMinWidth = 0;
        public float matchConstraintPercentHeight = 1.0f;
        public float matchConstraintPercentWidth = 1.0f;
        boolean needsBaseline = false;
        public int orientation = -1;
        int resolveGoneLeftMargin = -1;
        int resolveGoneRightMargin = -1;
        int resolvedGuideBegin;
        int resolvedGuideEnd;
        float resolvedGuidePercent;
        float resolvedHorizontalBias = 0.5f;
        int resolvedLeftToLeft = -1;
        int resolvedLeftToRight = -1;
        int resolvedRightToLeft = -1;
        int resolvedRightToRight = -1;
        public int rightToLeft = -1;
        public int rightToRight = -1;
        public int startToEnd = -1;
        public int startToStart = -1;
        public int topToBottom = -1;
        public int topToTop = -1;
        public float verticalBias = 0.5f;
        public int verticalChainStyle = 0;
        boolean verticalDimensionFixed = true;
        public float verticalWeight = -1.0f;
        ConstraintWidget widget = new ConstraintWidget();

        public LayoutParams(int n, int n2) {
            super(n, n2);
        }

        public LayoutParams(Context context, AttributeSet object) {
            super(context, (AttributeSet)object);
            context = context.obtainStyledAttributes((AttributeSet)object, R.styleable.ConstraintLayout_Layout);
            int n = context.getIndexCount();
            block66: for (int i = 0; i < n; ++i) {
                int n2 = context.getIndex(i);
                switch (Table.map.get(n2)) {
                    default: {
                        continue block66;
                    }
                    case 51: {
                        this.constraintTag = context.getString(n2);
                        continue block66;
                    }
                    case 50: {
                        this.editorAbsoluteY = context.getDimensionPixelOffset(n2, this.editorAbsoluteY);
                        continue block66;
                    }
                    case 49: {
                        this.editorAbsoluteX = context.getDimensionPixelOffset(n2, this.editorAbsoluteX);
                        continue block66;
                    }
                    case 48: {
                        this.verticalChainStyle = context.getInt(n2, 0);
                        continue block66;
                    }
                    case 47: {
                        this.horizontalChainStyle = context.getInt(n2, 0);
                        continue block66;
                    }
                    case 46: {
                        this.verticalWeight = context.getFloat(n2, this.verticalWeight);
                        continue block66;
                    }
                    case 45: {
                        this.horizontalWeight = context.getFloat(n2, this.horizontalWeight);
                        continue block66;
                    }
                    case 44: {
                        float f;
                        object = context.getString(n2);
                        this.dimensionRatio = object;
                        this.dimensionRatioValue = Float.NaN;
                        this.dimensionRatioSide = -1;
                        if (object == null) continue block66;
                        int n3 = ((String)object).length();
                        n2 = this.dimensionRatio.indexOf(44);
                        if (n2 > 0 && n2 < n3 - 1) {
                            object = this.dimensionRatio.substring(0, n2);
                            if (((String)object).equalsIgnoreCase("W")) {
                                this.dimensionRatioSide = 0;
                            } else if (((String)object).equalsIgnoreCase("H")) {
                                this.dimensionRatioSide = 1;
                            }
                            ++n2;
                        } else {
                            n2 = 0;
                        }
                        int n4 = this.dimensionRatio.indexOf(58);
                        if (n4 >= 0 && n4 < n3 - 1) {
                            float f2;
                            String string2 = this.dimensionRatio.substring(n2, n4);
                            object = this.dimensionRatio.substring(n4 + 1);
                            if (string2.length() <= 0 || ((String)object).length() <= 0) continue block66;
                            try {
                                f2 = Float.parseFloat(string2);
                                f = Float.parseFloat((String)object);
                            }
                            catch (NumberFormatException numberFormatException) {}
                            if (!(f2 > 0.0f) || !(f > 0.0f)) continue block66;
                            if (this.dimensionRatioSide == 1) {
                                this.dimensionRatioValue = Math.abs(f / f2);
                                continue block66;
                            }
                            this.dimensionRatioValue = Math.abs(f2 / f);
                            continue block66;
                        }
                        object = this.dimensionRatio.substring(n2);
                        if (((String)object).length() <= 0) continue block66;
                        try {
                            this.dimensionRatioValue = Float.parseFloat((String)object);
                        }
                        catch (NumberFormatException numberFormatException) {}
                        continue block66;
                    }
                    case 42: {
                        continue block66;
                    }
                    case 41: {
                        continue block66;
                    }
                    case 40: {
                        continue block66;
                    }
                    case 39: {
                        continue block66;
                    }
                    case 38: {
                        this.matchConstraintPercentHeight = Math.max(0.0f, context.getFloat(n2, this.matchConstraintPercentHeight));
                        this.matchConstraintDefaultHeight = 2;
                        continue block66;
                    }
                    case 37: {
                        try {
                            this.matchConstraintMaxHeight = context.getDimensionPixelSize(n2, this.matchConstraintMaxHeight);
                        }
                        catch (Exception exception) {
                            if (context.getInt(n2, this.matchConstraintMaxHeight) != -2) continue block66;
                            this.matchConstraintMaxHeight = -2;
                        }
                        continue block66;
                    }
                    case 36: {
                        try {
                            this.matchConstraintMinHeight = context.getDimensionPixelSize(n2, this.matchConstraintMinHeight);
                        }
                        catch (Exception exception) {
                            if (context.getInt(n2, this.matchConstraintMinHeight) != -2) continue block66;
                            this.matchConstraintMinHeight = -2;
                        }
                        continue block66;
                    }
                    case 35: {
                        this.matchConstraintPercentWidth = Math.max(0.0f, context.getFloat(n2, this.matchConstraintPercentWidth));
                        this.matchConstraintDefaultWidth = 2;
                        continue block66;
                    }
                    case 34: {
                        try {
                            this.matchConstraintMaxWidth = context.getDimensionPixelSize(n2, this.matchConstraintMaxWidth);
                        }
                        catch (Exception exception) {
                            if (context.getInt(n2, this.matchConstraintMaxWidth) != -2) continue block66;
                            this.matchConstraintMaxWidth = -2;
                        }
                        continue block66;
                    }
                    case 33: {
                        try {
                            this.matchConstraintMinWidth = context.getDimensionPixelSize(n2, this.matchConstraintMinWidth);
                        }
                        catch (Exception exception) {
                            if (context.getInt(n2, this.matchConstraintMinWidth) != -2) continue block66;
                            this.matchConstraintMinWidth = -2;
                        }
                        continue block66;
                    }
                    case 32: {
                        this.matchConstraintDefaultHeight = n2 = context.getInt(n2, 0);
                        if (n2 != 1) continue block66;
                        Log.e((String)ConstraintLayout.TAG, (String)"layout_constraintHeight_default=\"wrap\" is deprecated.\nUse layout_height=\"WRAP_CONTENT\" and layout_constrainedHeight=\"true\" instead.");
                        continue block66;
                    }
                    case 31: {
                        this.matchConstraintDefaultWidth = n2 = context.getInt(n2, 0);
                        if (n2 != 1) continue block66;
                        Log.e((String)ConstraintLayout.TAG, (String)"layout_constraintWidth_default=\"wrap\" is deprecated.\nUse layout_width=\"WRAP_CONTENT\" and layout_constrainedWidth=\"true\" instead.");
                        continue block66;
                    }
                    case 30: {
                        this.verticalBias = context.getFloat(n2, this.verticalBias);
                        continue block66;
                    }
                    case 29: {
                        this.horizontalBias = context.getFloat(n2, this.horizontalBias);
                        continue block66;
                    }
                    case 28: {
                        this.constrainedHeight = context.getBoolean(n2, this.constrainedHeight);
                        continue block66;
                    }
                    case 27: {
                        this.constrainedWidth = context.getBoolean(n2, this.constrainedWidth);
                        continue block66;
                    }
                    case 26: {
                        this.goneEndMargin = context.getDimensionPixelSize(n2, this.goneEndMargin);
                        continue block66;
                    }
                    case 25: {
                        this.goneStartMargin = context.getDimensionPixelSize(n2, this.goneStartMargin);
                        continue block66;
                    }
                    case 24: {
                        this.goneBottomMargin = context.getDimensionPixelSize(n2, this.goneBottomMargin);
                        continue block66;
                    }
                    case 23: {
                        this.goneRightMargin = context.getDimensionPixelSize(n2, this.goneRightMargin);
                        continue block66;
                    }
                    case 22: {
                        this.goneTopMargin = context.getDimensionPixelSize(n2, this.goneTopMargin);
                        continue block66;
                    }
                    case 21: {
                        this.goneLeftMargin = context.getDimensionPixelSize(n2, this.goneLeftMargin);
                        continue block66;
                    }
                    case 20: {
                        int n3;
                        this.endToEnd = n3 = context.getResourceId(n2, this.endToEnd);
                        if (n3 != -1) continue block66;
                        this.endToEnd = context.getInt(n2, -1);
                        continue block66;
                    }
                    case 19: {
                        int n3;
                        this.endToStart = n3 = context.getResourceId(n2, this.endToStart);
                        if (n3 != -1) continue block66;
                        this.endToStart = context.getInt(n2, -1);
                        continue block66;
                    }
                    case 18: {
                        int n3;
                        this.startToStart = n3 = context.getResourceId(n2, this.startToStart);
                        if (n3 != -1) continue block66;
                        this.startToStart = context.getInt(n2, -1);
                        continue block66;
                    }
                    case 17: {
                        int n3;
                        this.startToEnd = n3 = context.getResourceId(n2, this.startToEnd);
                        if (n3 != -1) continue block66;
                        this.startToEnd = context.getInt(n2, -1);
                        continue block66;
                    }
                    case 16: {
                        int n3;
                        this.baselineToBaseline = n3 = context.getResourceId(n2, this.baselineToBaseline);
                        if (n3 != -1) continue block66;
                        this.baselineToBaseline = context.getInt(n2, -1);
                        continue block66;
                    }
                    case 15: {
                        int n3;
                        this.bottomToBottom = n3 = context.getResourceId(n2, this.bottomToBottom);
                        if (n3 != -1) continue block66;
                        this.bottomToBottom = context.getInt(n2, -1);
                        continue block66;
                    }
                    case 14: {
                        int n3;
                        this.bottomToTop = n3 = context.getResourceId(n2, this.bottomToTop);
                        if (n3 != -1) continue block66;
                        this.bottomToTop = context.getInt(n2, -1);
                        continue block66;
                    }
                    case 13: {
                        int n3;
                        this.topToBottom = n3 = context.getResourceId(n2, this.topToBottom);
                        if (n3 != -1) continue block66;
                        this.topToBottom = context.getInt(n2, -1);
                        continue block66;
                    }
                    case 12: {
                        int n3;
                        this.topToTop = n3 = context.getResourceId(n2, this.topToTop);
                        if (n3 != -1) continue block66;
                        this.topToTop = context.getInt(n2, -1);
                        continue block66;
                    }
                    case 11: {
                        int n3;
                        this.rightToRight = n3 = context.getResourceId(n2, this.rightToRight);
                        if (n3 != -1) continue block66;
                        this.rightToRight = context.getInt(n2, -1);
                        continue block66;
                    }
                    case 10: {
                        int n3;
                        this.rightToLeft = n3 = context.getResourceId(n2, this.rightToLeft);
                        if (n3 != -1) continue block66;
                        this.rightToLeft = context.getInt(n2, -1);
                        continue block66;
                    }
                    case 9: {
                        int n3;
                        this.leftToRight = n3 = context.getResourceId(n2, this.leftToRight);
                        if (n3 != -1) continue block66;
                        this.leftToRight = context.getInt(n2, -1);
                        continue block66;
                    }
                    case 8: {
                        int n3;
                        this.leftToLeft = n3 = context.getResourceId(n2, this.leftToLeft);
                        if (n3 != -1) continue block66;
                        this.leftToLeft = context.getInt(n2, -1);
                        continue block66;
                    }
                    case 7: {
                        this.guidePercent = context.getFloat(n2, this.guidePercent);
                        continue block66;
                    }
                    case 6: {
                        this.guideEnd = context.getDimensionPixelOffset(n2, this.guideEnd);
                        continue block66;
                    }
                    case 5: {
                        this.guideBegin = context.getDimensionPixelOffset(n2, this.guideBegin);
                        continue block66;
                    }
                    case 4: {
                        float f;
                        this.circleAngle = f = context.getFloat(n2, this.circleAngle) % 360.0f;
                        if (!(f < 0.0f)) continue block66;
                        this.circleAngle = (360.0f - f) % 360.0f;
                        continue block66;
                    }
                    case 3: {
                        this.circleRadius = context.getDimensionPixelSize(n2, this.circleRadius);
                        continue block66;
                    }
                    case 2: {
                        int n3;
                        this.circleConstraint = n3 = context.getResourceId(n2, this.circleConstraint);
                        if (n3 != -1) continue block66;
                        this.circleConstraint = context.getInt(n2, -1);
                        continue block66;
                    }
                    case 1: {
                        this.orientation = context.getInt(n2, this.orientation);
                    }
                    case 0: 
                }
            }
            context.recycle();
            this.validate();
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.MarginLayoutParams)layoutParams);
            this.guideBegin = layoutParams.guideBegin;
            this.guideEnd = layoutParams.guideEnd;
            this.guidePercent = layoutParams.guidePercent;
            this.leftToLeft = layoutParams.leftToLeft;
            this.leftToRight = layoutParams.leftToRight;
            this.rightToLeft = layoutParams.rightToLeft;
            this.rightToRight = layoutParams.rightToRight;
            this.topToTop = layoutParams.topToTop;
            this.topToBottom = layoutParams.topToBottom;
            this.bottomToTop = layoutParams.bottomToTop;
            this.bottomToBottom = layoutParams.bottomToBottom;
            this.baselineToBaseline = layoutParams.baselineToBaseline;
            this.circleConstraint = layoutParams.circleConstraint;
            this.circleRadius = layoutParams.circleRadius;
            this.circleAngle = layoutParams.circleAngle;
            this.startToEnd = layoutParams.startToEnd;
            this.startToStart = layoutParams.startToStart;
            this.endToStart = layoutParams.endToStart;
            this.endToEnd = layoutParams.endToEnd;
            this.goneLeftMargin = layoutParams.goneLeftMargin;
            this.goneTopMargin = layoutParams.goneTopMargin;
            this.goneRightMargin = layoutParams.goneRightMargin;
            this.goneBottomMargin = layoutParams.goneBottomMargin;
            this.goneStartMargin = layoutParams.goneStartMargin;
            this.goneEndMargin = layoutParams.goneEndMargin;
            this.horizontalBias = layoutParams.horizontalBias;
            this.verticalBias = layoutParams.verticalBias;
            this.dimensionRatio = layoutParams.dimensionRatio;
            this.dimensionRatioValue = layoutParams.dimensionRatioValue;
            this.dimensionRatioSide = layoutParams.dimensionRatioSide;
            this.horizontalWeight = layoutParams.horizontalWeight;
            this.verticalWeight = layoutParams.verticalWeight;
            this.horizontalChainStyle = layoutParams.horizontalChainStyle;
            this.verticalChainStyle = layoutParams.verticalChainStyle;
            this.constrainedWidth = layoutParams.constrainedWidth;
            this.constrainedHeight = layoutParams.constrainedHeight;
            this.matchConstraintDefaultWidth = layoutParams.matchConstraintDefaultWidth;
            this.matchConstraintDefaultHeight = layoutParams.matchConstraintDefaultHeight;
            this.matchConstraintMinWidth = layoutParams.matchConstraintMinWidth;
            this.matchConstraintMaxWidth = layoutParams.matchConstraintMaxWidth;
            this.matchConstraintMinHeight = layoutParams.matchConstraintMinHeight;
            this.matchConstraintMaxHeight = layoutParams.matchConstraintMaxHeight;
            this.matchConstraintPercentWidth = layoutParams.matchConstraintPercentWidth;
            this.matchConstraintPercentHeight = layoutParams.matchConstraintPercentHeight;
            this.editorAbsoluteX = layoutParams.editorAbsoluteX;
            this.editorAbsoluteY = layoutParams.editorAbsoluteY;
            this.orientation = layoutParams.orientation;
            this.horizontalDimensionFixed = layoutParams.horizontalDimensionFixed;
            this.verticalDimensionFixed = layoutParams.verticalDimensionFixed;
            this.needsBaseline = layoutParams.needsBaseline;
            this.isGuideline = layoutParams.isGuideline;
            this.resolvedLeftToLeft = layoutParams.resolvedLeftToLeft;
            this.resolvedLeftToRight = layoutParams.resolvedLeftToRight;
            this.resolvedRightToLeft = layoutParams.resolvedRightToLeft;
            this.resolvedRightToRight = layoutParams.resolvedRightToRight;
            this.resolveGoneLeftMargin = layoutParams.resolveGoneLeftMargin;
            this.resolveGoneRightMargin = layoutParams.resolveGoneRightMargin;
            this.resolvedHorizontalBias = layoutParams.resolvedHorizontalBias;
            this.constraintTag = layoutParams.constraintTag;
            this.widget = layoutParams.widget;
        }

        public String getConstraintTag() {
            return this.constraintTag;
        }

        public ConstraintWidget getConstraintWidget() {
            return this.widget;
        }

        public void reset() {
            ConstraintWidget constraintWidget = this.widget;
            if (constraintWidget != null) {
                constraintWidget.reset();
            }
        }

        public void resolveLayoutDirection(int n) {
            float f;
            int n2;
            int n3;
            float f2;
            int n4 = this.leftMargin;
            int n5 = this.rightMargin;
            int n6 = 0;
            if (Build.VERSION.SDK_INT >= 17) {
                super.resolveLayoutDirection(n);
                n = 1 == this.getLayoutDirection() ? 1 : 0;
                n6 = n;
            }
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolveGoneLeftMargin = this.goneLeftMargin;
            this.resolveGoneRightMargin = this.goneRightMargin;
            this.resolvedHorizontalBias = f2 = this.horizontalBias;
            this.resolvedGuideBegin = n3 = this.guideBegin;
            this.resolvedGuideEnd = n2 = this.guideEnd;
            this.resolvedGuidePercent = f = this.guidePercent;
            if (n6 != 0) {
                n = 0;
                n6 = this.startToEnd;
                if (n6 != -1) {
                    this.resolvedRightToLeft = n6;
                    n = 1;
                } else {
                    n6 = this.startToStart;
                    if (n6 != -1) {
                        this.resolvedRightToRight = n6;
                        n = 1;
                    }
                }
                n6 = this.endToStart;
                if (n6 != -1) {
                    this.resolvedLeftToRight = n6;
                    n = 1;
                }
                if ((n6 = this.endToEnd) != -1) {
                    this.resolvedLeftToLeft = n6;
                    n = 1;
                }
                if ((n6 = this.goneStartMargin) != -1) {
                    this.resolveGoneRightMargin = n6;
                }
                if ((n6 = this.goneEndMargin) != -1) {
                    this.resolveGoneLeftMargin = n6;
                }
                if (n != 0) {
                    this.resolvedHorizontalBias = 1.0f - f2;
                }
                if (this.isGuideline && this.orientation == 1) {
                    if (f != -1.0f) {
                        this.resolvedGuidePercent = 1.0f - f;
                        this.resolvedGuideBegin = -1;
                        this.resolvedGuideEnd = -1;
                    } else if (n3 != -1) {
                        this.resolvedGuideEnd = n3;
                        this.resolvedGuideBegin = -1;
                        this.resolvedGuidePercent = -1.0f;
                    } else if (n2 != -1) {
                        this.resolvedGuideBegin = n2;
                        this.resolvedGuideEnd = -1;
                        this.resolvedGuidePercent = -1.0f;
                    }
                }
            } else {
                n = this.startToEnd;
                if (n != -1) {
                    this.resolvedLeftToRight = n;
                }
                if ((n = this.startToStart) != -1) {
                    this.resolvedLeftToLeft = n;
                }
                if ((n = this.endToStart) != -1) {
                    this.resolvedRightToLeft = n;
                }
                if ((n = this.endToEnd) != -1) {
                    this.resolvedRightToRight = n;
                }
                if ((n = this.goneStartMargin) != -1) {
                    this.resolveGoneLeftMargin = n;
                }
                if ((n = this.goneEndMargin) != -1) {
                    this.resolveGoneRightMargin = n;
                }
            }
            if (this.endToStart == -1 && this.endToEnd == -1 && this.startToStart == -1 && this.startToEnd == -1) {
                n = this.rightToLeft;
                if (n != -1) {
                    this.resolvedRightToLeft = n;
                    if (this.rightMargin <= 0 && n5 > 0) {
                        this.rightMargin = n5;
                    }
                } else {
                    n = this.rightToRight;
                    if (n != -1) {
                        this.resolvedRightToRight = n;
                        if (this.rightMargin <= 0 && n5 > 0) {
                            this.rightMargin = n5;
                        }
                    }
                }
                if ((n = this.leftToLeft) != -1) {
                    this.resolvedLeftToLeft = n;
                    if (this.leftMargin <= 0 && n4 > 0) {
                        this.leftMargin = n4;
                    }
                } else {
                    n = this.leftToRight;
                    if (n != -1) {
                        this.resolvedLeftToRight = n;
                        if (this.leftMargin <= 0 && n4 > 0) {
                            this.leftMargin = n4;
                        }
                    }
                }
            }
        }

        public void setWidgetDebugName(String string2) {
            this.widget.setDebugName(string2);
        }

        public void validate() {
            this.isGuideline = false;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            if (this.width == -2 && this.constrainedWidth) {
                this.horizontalDimensionFixed = false;
                if (this.matchConstraintDefaultWidth == 0) {
                    this.matchConstraintDefaultWidth = 1;
                }
            }
            if (this.height == -2 && this.constrainedHeight) {
                this.verticalDimensionFixed = false;
                if (this.matchConstraintDefaultHeight == 0) {
                    this.matchConstraintDefaultHeight = 1;
                }
            }
            if (this.width == 0 || this.width == -1) {
                this.horizontalDimensionFixed = false;
                if (this.width == 0 && this.matchConstraintDefaultWidth == 1) {
                    this.width = -2;
                    this.constrainedWidth = true;
                }
            }
            if (this.height == 0 || this.height == -1) {
                this.verticalDimensionFixed = false;
                if (this.height == 0 && this.matchConstraintDefaultHeight == 1) {
                    this.height = -2;
                    this.constrainedHeight = true;
                }
            }
            if (this.guidePercent != -1.0f || this.guideBegin != -1 || this.guideEnd != -1) {
                this.isGuideline = true;
                this.horizontalDimensionFixed = true;
                this.verticalDimensionFixed = true;
                if (!(this.widget instanceof androidx.constraintlayout.solver.widgets.Guideline)) {
                    this.widget = new androidx.constraintlayout.solver.widgets.Guideline();
                }
                ((androidx.constraintlayout.solver.widgets.Guideline)this.widget).setOrientation(this.orientation);
            }
        }

        private static class Table {
            public static final int ANDROID_ORIENTATION = 1;
            public static final int LAYOUT_CONSTRAINED_HEIGHT = 28;
            public static final int LAYOUT_CONSTRAINED_WIDTH = 27;
            public static final int LAYOUT_CONSTRAINT_BASELINE_CREATOR = 43;
            public static final int LAYOUT_CONSTRAINT_BASELINE_TO_BASELINE_OF = 16;
            public static final int LAYOUT_CONSTRAINT_BOTTOM_CREATOR = 42;
            public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_BOTTOM_OF = 15;
            public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_TOP_OF = 14;
            public static final int LAYOUT_CONSTRAINT_CIRCLE = 2;
            public static final int LAYOUT_CONSTRAINT_CIRCLE_ANGLE = 4;
            public static final int LAYOUT_CONSTRAINT_CIRCLE_RADIUS = 3;
            public static final int LAYOUT_CONSTRAINT_DIMENSION_RATIO = 44;
            public static final int LAYOUT_CONSTRAINT_END_TO_END_OF = 20;
            public static final int LAYOUT_CONSTRAINT_END_TO_START_OF = 19;
            public static final int LAYOUT_CONSTRAINT_GUIDE_BEGIN = 5;
            public static final int LAYOUT_CONSTRAINT_GUIDE_END = 6;
            public static final int LAYOUT_CONSTRAINT_GUIDE_PERCENT = 7;
            public static final int LAYOUT_CONSTRAINT_HEIGHT_DEFAULT = 32;
            public static final int LAYOUT_CONSTRAINT_HEIGHT_MAX = 37;
            public static final int LAYOUT_CONSTRAINT_HEIGHT_MIN = 36;
            public static final int LAYOUT_CONSTRAINT_HEIGHT_PERCENT = 38;
            public static final int LAYOUT_CONSTRAINT_HORIZONTAL_BIAS = 29;
            public static final int LAYOUT_CONSTRAINT_HORIZONTAL_CHAINSTYLE = 47;
            public static final int LAYOUT_CONSTRAINT_HORIZONTAL_WEIGHT = 45;
            public static final int LAYOUT_CONSTRAINT_LEFT_CREATOR = 39;
            public static final int LAYOUT_CONSTRAINT_LEFT_TO_LEFT_OF = 8;
            public static final int LAYOUT_CONSTRAINT_LEFT_TO_RIGHT_OF = 9;
            public static final int LAYOUT_CONSTRAINT_RIGHT_CREATOR = 41;
            public static final int LAYOUT_CONSTRAINT_RIGHT_TO_LEFT_OF = 10;
            public static final int LAYOUT_CONSTRAINT_RIGHT_TO_RIGHT_OF = 11;
            public static final int LAYOUT_CONSTRAINT_START_TO_END_OF = 17;
            public static final int LAYOUT_CONSTRAINT_START_TO_START_OF = 18;
            public static final int LAYOUT_CONSTRAINT_TAG = 51;
            public static final int LAYOUT_CONSTRAINT_TOP_CREATOR = 40;
            public static final int LAYOUT_CONSTRAINT_TOP_TO_BOTTOM_OF = 13;
            public static final int LAYOUT_CONSTRAINT_TOP_TO_TOP_OF = 12;
            public static final int LAYOUT_CONSTRAINT_VERTICAL_BIAS = 30;
            public static final int LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE = 48;
            public static final int LAYOUT_CONSTRAINT_VERTICAL_WEIGHT = 46;
            public static final int LAYOUT_CONSTRAINT_WIDTH_DEFAULT = 31;
            public static final int LAYOUT_CONSTRAINT_WIDTH_MAX = 34;
            public static final int LAYOUT_CONSTRAINT_WIDTH_MIN = 33;
            public static final int LAYOUT_CONSTRAINT_WIDTH_PERCENT = 35;
            public static final int LAYOUT_EDITOR_ABSOLUTEX = 49;
            public static final int LAYOUT_EDITOR_ABSOLUTEY = 50;
            public static final int LAYOUT_GONE_MARGIN_BOTTOM = 24;
            public static final int LAYOUT_GONE_MARGIN_END = 26;
            public static final int LAYOUT_GONE_MARGIN_LEFT = 21;
            public static final int LAYOUT_GONE_MARGIN_RIGHT = 23;
            public static final int LAYOUT_GONE_MARGIN_START = 25;
            public static final int LAYOUT_GONE_MARGIN_TOP = 22;
            public static final int UNUSED = 0;
            public static final SparseIntArray map;

            static {
                SparseIntArray sparseIntArray;
                map = sparseIntArray = new SparseIntArray();
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf, 8);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf, 9);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf, 10);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf, 11);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf, 12);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf, 13);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf, 14);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf, 15);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf, 16);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircle, 2);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleRadius, 3);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleAngle, 4);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteX, 49);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteY, 50);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_begin, 5);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_end, 6);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_percent, 7);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_android_orientation, 1);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf, 17);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf, 18);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf, 19);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf, 20);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginLeft, 21);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginTop, 22);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginRight, 23);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginBottom, 24);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginStart, 25);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginEnd, 26);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias, 29);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_bias, 30);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio, 44);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight, 45);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_weight, 46);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainStyle, 47);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_chainStyle, 48);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constrainedWidth, 27);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constrainedHeight, 28);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_default, 31);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_default, 32);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_min, 33);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_max, 34);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_percent, 35);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_min, 36);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_max, 37);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_percent, 38);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_creator, 39);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_creator, 40);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_creator, 41);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_creator, 42);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator, 43);
                sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintTag, 51);
            }

            private Table() {
            }
        }
    }

    class Measurer
    implements BasicMeasure.Measurer {
        ConstraintLayout layout;
        int layoutHeightSpec;
        int layoutWidthSpec;
        int paddingBottom;
        int paddingHeight;
        int paddingTop;
        int paddingWidth;
        final ConstraintLayout this$0;

        public Measurer(ConstraintLayout constraintLayout, ConstraintLayout constraintLayout2) {
            this.this$0 = constraintLayout;
            this.layout = constraintLayout2;
        }

        private boolean isSimilarSpec(int n, int n2, int n3) {
            if (n == n2) {
                return true;
            }
            int n4 = View.MeasureSpec.getMode((int)n);
            View.MeasureSpec.getSize((int)n);
            n = View.MeasureSpec.getMode((int)n2);
            n2 = View.MeasureSpec.getSize((int)n2);
            return n == 0x40000000 && (n4 == Integer.MIN_VALUE || n4 == 0) && n3 == n2;
        }

        public void captureLayoutInfos(int n, int n2, int n3, int n4, int n5, int n6) {
            this.paddingTop = n3;
            this.paddingBottom = n4;
            this.paddingWidth = n5;
            this.paddingHeight = n6;
            this.layoutWidthSpec = n;
            this.layoutHeightSpec = n2;
        }

        @Override
        public final void didMeasures() {
            int n;
            int n2 = this.layout.getChildCount();
            for (n = 0; n < n2; ++n) {
                View view = this.layout.getChildAt(n);
                if (!(view instanceof Placeholder)) continue;
                ((Placeholder)view).updatePostMeasure(this.layout);
            }
            n2 = this.layout.mConstraintHelpers.size();
            if (n2 > 0) {
                for (n = 0; n < n2; ++n) {
                    ((ConstraintHelper)((Object)this.layout.mConstraintHelpers.get(n))).updatePostMeasure(this.layout);
                }
            }
        }

        @Override
        public final void measure(ConstraintWidget constraintWidget, BasicMeasure.Measure measure) {
            if (constraintWidget == null) {
                return;
            }
            if (constraintWidget.getVisibility() == 8 && !constraintWidget.isInPlaceholder()) {
                measure.measuredWidth = 0;
                measure.measuredHeight = 0;
                measure.measuredBaseline = 0;
                return;
            }
            if (constraintWidget.getParent() == null) {
                return;
            }
            Object object = measure.horizontalBehavior;
            Object object2 = measure.verticalBehavior;
            int n = measure.horizontalDimension;
            int n2 = measure.verticalDimension;
            int n3 = 0;
            int n4 = 0;
            int n5 = this.paddingTop + this.paddingBottom;
            int n6 = this.paddingWidth;
            View view = (View)constraintWidget.getCompanionWidget();
            switch (1.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour[object.ordinal()]) {
                default: {
                    break;
                }
                case 4: {
                    n = ViewGroup.getChildMeasureSpec((int)this.layoutWidthSpec, (int)n6, (int)-2);
                    n6 = constraintWidget.mMatchConstraintDefaultWidth == 1 ? 1 : 0;
                    if (measure.measureStrategy != BasicMeasure.Measure.TRY_GIVEN_DIMENSIONS) {
                        n3 = n;
                        if (measure.measureStrategy != BasicMeasure.Measure.USE_GIVEN_DIMENSIONS) break;
                    }
                    n3 = view.getMeasuredHeight() == constraintWidget.getHeight() ? 1 : 0;
                    n3 = !(measure.measureStrategy == BasicMeasure.Measure.USE_GIVEN_DIMENSIONS || n6 == 0 || n6 != 0 && n3 != 0 || view instanceof Placeholder || constraintWidget.isResolvedHorizontally()) ? 0 : 1;
                    if (n3 != 0) {
                        n3 = View.MeasureSpec.makeMeasureSpec((int)constraintWidget.getWidth(), (int)0x40000000);
                        break;
                    }
                    n3 = n;
                    break;
                }
                case 3: {
                    n3 = ViewGroup.getChildMeasureSpec((int)this.layoutWidthSpec, (int)(constraintWidget.getHorizontalMargin() + n6), (int)-1);
                    break;
                }
                case 2: {
                    n3 = ViewGroup.getChildMeasureSpec((int)this.layoutWidthSpec, (int)n6, (int)-2);
                    break;
                }
                case 1: {
                    n3 = View.MeasureSpec.makeMeasureSpec((int)n, (int)0x40000000);
                }
            }
            switch (1.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour[object2.ordinal()]) {
                default: {
                    n6 = n4;
                    break;
                }
                case 4: {
                    n = ViewGroup.getChildMeasureSpec((int)this.layoutHeightSpec, (int)n5, (int)-2);
                    n4 = constraintWidget.mMatchConstraintDefaultHeight == 1 ? 1 : 0;
                    if (measure.measureStrategy != BasicMeasure.Measure.TRY_GIVEN_DIMENSIONS) {
                        n6 = n;
                        if (measure.measureStrategy != BasicMeasure.Measure.USE_GIVEN_DIMENSIONS) break;
                    }
                    n6 = view.getMeasuredWidth() == constraintWidget.getWidth() ? 1 : 0;
                    n6 = !(measure.measureStrategy == BasicMeasure.Measure.USE_GIVEN_DIMENSIONS || n4 == 0 || n4 != 0 && n6 != 0 || view instanceof Placeholder || constraintWidget.isResolvedVertically()) ? 0 : 1;
                    if (n6 != 0) {
                        n6 = View.MeasureSpec.makeMeasureSpec((int)constraintWidget.getHeight(), (int)0x40000000);
                        break;
                    }
                    n6 = n;
                    break;
                }
                case 3: {
                    n6 = ViewGroup.getChildMeasureSpec((int)this.layoutHeightSpec, (int)(constraintWidget.getVerticalMargin() + n5), (int)-1);
                    break;
                }
                case 2: {
                    n6 = ViewGroup.getChildMeasureSpec((int)this.layoutHeightSpec, (int)n5, (int)-2);
                    break;
                }
                case 1: {
                    n6 = View.MeasureSpec.makeMeasureSpec((int)n2, (int)0x40000000);
                }
            }
            ConstraintWidgetContainer constraintWidgetContainer = (ConstraintWidgetContainer)constraintWidget.getParent();
            if (constraintWidgetContainer != null && Optimizer.enabled(this.this$0.mOptimizationLevel, 256) && view.getMeasuredWidth() == constraintWidget.getWidth() && view.getMeasuredWidth() < constraintWidgetContainer.getWidth() && view.getMeasuredHeight() == constraintWidget.getHeight() && view.getMeasuredHeight() < constraintWidgetContainer.getHeight() && view.getBaseline() == constraintWidget.getBaselineDistance() && !constraintWidget.isMeasureRequested() && (n4 = this.isSimilarSpec(constraintWidget.getLastHorizontalMeasureSpec(), n3, constraintWidget.getWidth()) && this.isSimilarSpec(constraintWidget.getLastVerticalMeasureSpec(), n6, constraintWidget.getHeight()) ? 1 : 0) != 0) {
                measure.measuredWidth = constraintWidget.getWidth();
                measure.measuredHeight = constraintWidget.getHeight();
                measure.measuredBaseline = constraintWidget.getBaselineDistance();
                return;
            }
            n4 = object == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT ? 1 : 0;
            n = object2 == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT ? 1 : 0;
            n5 = object2 != ConstraintWidget.DimensionBehaviour.MATCH_PARENT && object2 != ConstraintWidget.DimensionBehaviour.FIXED ? 0 : 1;
            boolean bl = object == ConstraintWidget.DimensionBehaviour.MATCH_PARENT || object == ConstraintWidget.DimensionBehaviour.FIXED;
            boolean bl2 = n4 != 0 && constraintWidget.mDimensionRatio > 0.0f;
            boolean bl3 = n != 0 && constraintWidget.mDimensionRatio > 0.0f;
            if (view == null) {
                return;
            }
            object = (LayoutParams)view.getLayoutParams();
            if (measure.measureStrategy != BasicMeasure.Measure.TRY_GIVEN_DIMENSIONS && measure.measureStrategy != BasicMeasure.Measure.USE_GIVEN_DIMENSIONS && n4 != 0 && constraintWidget.mMatchConstraintDefaultWidth == 0 && n != 0 && constraintWidget.mMatchConstraintDefaultHeight == 0) {
                n3 = 0;
                n = 0;
                n6 = 0;
            } else {
                if (view instanceof VirtualLayout && constraintWidget instanceof androidx.constraintlayout.solver.widgets.VirtualLayout) {
                    object2 = (androidx.constraintlayout.solver.widgets.VirtualLayout)constraintWidget;
                    ((VirtualLayout)view).onMeasure((androidx.constraintlayout.solver.widgets.VirtualLayout)object2, n3, n6);
                } else {
                    view.measure(n3, n6);
                }
                constraintWidget.setLastMeasureSpec(n3, n6);
                int n7 = view.getMeasuredWidth();
                int n8 = view.getMeasuredHeight();
                int n9 = view.getBaseline();
                n = constraintWidget.mMatchConstraintMinWidth > 0 ? Math.max(constraintWidget.mMatchConstraintMinWidth, n7) : n7;
                n4 = n;
                if (constraintWidget.mMatchConstraintMaxWidth > 0) {
                    n4 = Math.min(constraintWidget.mMatchConstraintMaxWidth, n);
                }
                n = constraintWidget.mMatchConstraintMinHeight > 0 ? Math.max(constraintWidget.mMatchConstraintMinHeight, n8) : n8;
                n2 = n;
                if (constraintWidget.mMatchConstraintMaxHeight > 0) {
                    n2 = Math.min(constraintWidget.mMatchConstraintMaxHeight, n);
                }
                if (!Optimizer.enabled(this.this$0.mOptimizationLevel, 1)) {
                    if (bl2 && n5 != 0) {
                        float f = constraintWidget.mDimensionRatio;
                        n5 = (int)((float)n2 * f + 0.5f);
                        n = n2;
                    } else {
                        n5 = n4;
                        n = n2;
                        if (bl3) {
                            n5 = n4;
                            n = n2;
                            if (bl) {
                                float f = constraintWidget.mDimensionRatio;
                                n = (int)((float)n4 / f + 0.5f);
                                n5 = n4;
                            }
                        }
                    }
                } else {
                    n = n2;
                    n5 = n4;
                }
                if (n7 == n5 && n8 == n) {
                    n3 = n5;
                    n6 = n9;
                } else {
                    if (n7 != n5) {
                        n3 = View.MeasureSpec.makeMeasureSpec((int)n5, (int)0x40000000);
                    }
                    if (n8 != n) {
                        n6 = View.MeasureSpec.makeMeasureSpec((int)n, (int)0x40000000);
                    }
                    view.measure(n3, n6);
                    constraintWidget.setLastMeasureSpec(n3, n6);
                    n3 = view.getMeasuredWidth();
                    n = view.getMeasuredHeight();
                    n6 = view.getBaseline();
                }
            }
            boolean bl4 = n6 != -1;
            boolean bl5 = n3 != measure.horizontalDimension || n != measure.verticalDimension;
            measure.measuredNeedsSolverPass = bl5;
            if (((LayoutParams)((Object)object)).needsBaseline) {
                bl4 = true;
            }
            if (bl4 && n6 != -1 && constraintWidget.getBaselineDistance() != n6) {
                measure.measuredNeedsSolverPass = true;
            }
            measure.measuredWidth = n3;
            measure.measuredHeight = n;
            measure.measuredHasBaseline = bl4;
            measure.measuredBaseline = n6;
        }
    }
}

