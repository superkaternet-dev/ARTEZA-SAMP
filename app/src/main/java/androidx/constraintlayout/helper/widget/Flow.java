/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.SparseArray
 *  android.view.View$MeasureSpec
 */
package androidx.constraintlayout.helper.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.HelperWidget;
import androidx.constraintlayout.solver.widgets.VirtualLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.R;

public class Flow
extends androidx.constraintlayout.widget.VirtualLayout {
    public static final int CHAIN_PACKED = 2;
    public static final int CHAIN_SPREAD = 0;
    public static final int CHAIN_SPREAD_INSIDE = 1;
    public static final int HORIZONTAL = 0;
    public static final int HORIZONTAL_ALIGN_CENTER = 2;
    public static final int HORIZONTAL_ALIGN_END = 1;
    public static final int HORIZONTAL_ALIGN_START = 0;
    private static final String TAG = "Flow";
    public static final int VERTICAL = 1;
    public static final int VERTICAL_ALIGN_BASELINE = 3;
    public static final int VERTICAL_ALIGN_BOTTOM = 1;
    public static final int VERTICAL_ALIGN_CENTER = 2;
    public static final int VERTICAL_ALIGN_TOP = 0;
    public static final int WRAP_ALIGNED = 2;
    public static final int WRAP_CHAIN = 1;
    public static final int WRAP_NONE = 0;
    private androidx.constraintlayout.solver.widgets.Flow mFlow;

    public Flow(Context context) {
        super(context);
    }

    public Flow(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public Flow(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    @Override
    protected void init(AttributeSet attributeSet) {
        super.init(attributeSet);
        this.mFlow = new androidx.constraintlayout.solver.widgets.Flow();
        if (attributeSet != null) {
            attributeSet = this.getContext().obtainStyledAttributes(attributeSet, R.styleable.ConstraintLayout_Layout);
            int n = attributeSet.getIndexCount();
            for (int i = 0; i < n; ++i) {
                int n2 = attributeSet.getIndex(i);
                if (n2 == R.styleable.ConstraintLayout_Layout_android_orientation) {
                    this.mFlow.setOrientation(attributeSet.getInt(n2, 0));
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_android_padding) {
                    this.mFlow.setPadding(attributeSet.getDimensionPixelSize(n2, 0));
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_android_paddingStart) {
                    if (Build.VERSION.SDK_INT < 17) continue;
                    this.mFlow.setPaddingStart(attributeSet.getDimensionPixelSize(n2, 0));
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_android_paddingEnd) {
                    if (Build.VERSION.SDK_INT < 17) continue;
                    this.mFlow.setPaddingEnd(attributeSet.getDimensionPixelSize(n2, 0));
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_android_paddingLeft) {
                    this.mFlow.setPaddingLeft(attributeSet.getDimensionPixelSize(n2, 0));
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_android_paddingTop) {
                    this.mFlow.setPaddingTop(attributeSet.getDimensionPixelSize(n2, 0));
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_android_paddingRight) {
                    this.mFlow.setPaddingRight(attributeSet.getDimensionPixelSize(n2, 0));
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_android_paddingBottom) {
                    this.mFlow.setPaddingBottom(attributeSet.getDimensionPixelSize(n2, 0));
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_flow_wrapMode) {
                    this.mFlow.setWrapMode(attributeSet.getInt(n2, 0));
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_flow_horizontalStyle) {
                    this.mFlow.setHorizontalStyle(attributeSet.getInt(n2, 0));
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_flow_verticalStyle) {
                    this.mFlow.setVerticalStyle(attributeSet.getInt(n2, 0));
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_flow_firstHorizontalStyle) {
                    this.mFlow.setFirstHorizontalStyle(attributeSet.getInt(n2, 0));
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_flow_lastHorizontalStyle) {
                    this.mFlow.setLastHorizontalStyle(attributeSet.getInt(n2, 0));
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_flow_firstVerticalStyle) {
                    this.mFlow.setFirstVerticalStyle(attributeSet.getInt(n2, 0));
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_flow_lastVerticalStyle) {
                    this.mFlow.setLastVerticalStyle(attributeSet.getInt(n2, 0));
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_flow_horizontalBias) {
                    this.mFlow.setHorizontalBias(attributeSet.getFloat(n2, 0.5f));
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_flow_firstHorizontalBias) {
                    this.mFlow.setFirstHorizontalBias(attributeSet.getFloat(n2, 0.5f));
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_flow_lastHorizontalBias) {
                    this.mFlow.setLastHorizontalBias(attributeSet.getFloat(n2, 0.5f));
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_flow_firstVerticalBias) {
                    this.mFlow.setFirstVerticalBias(attributeSet.getFloat(n2, 0.5f));
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_flow_lastVerticalBias) {
                    this.mFlow.setLastVerticalBias(attributeSet.getFloat(n2, 0.5f));
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_flow_verticalBias) {
                    this.mFlow.setVerticalBias(attributeSet.getFloat(n2, 0.5f));
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_flow_horizontalAlign) {
                    this.mFlow.setHorizontalAlign(attributeSet.getInt(n2, 2));
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_flow_verticalAlign) {
                    this.mFlow.setVerticalAlign(attributeSet.getInt(n2, 2));
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_flow_horizontalGap) {
                    this.mFlow.setHorizontalGap(attributeSet.getDimensionPixelSize(n2, 0));
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_flow_verticalGap) {
                    this.mFlow.setVerticalGap(attributeSet.getDimensionPixelSize(n2, 0));
                    continue;
                }
                if (n2 != R.styleable.ConstraintLayout_Layout_flow_maxElementsWrap) continue;
                this.mFlow.setMaxElementsWrap(attributeSet.getInt(n2, -1));
            }
            attributeSet.recycle();
        }
        this.mHelperWidget = this.mFlow;
        this.validateParams();
    }

    @Override
    public void loadParameters(ConstraintSet.Constraint object, HelperWidget helperWidget, ConstraintLayout.LayoutParams layoutParams, SparseArray<ConstraintWidget> sparseArray) {
        super.loadParameters((ConstraintSet.Constraint)object, helperWidget, layoutParams, sparseArray);
        if (helperWidget instanceof androidx.constraintlayout.solver.widgets.Flow) {
            object = (androidx.constraintlayout.solver.widgets.Flow)helperWidget;
            if (layoutParams.orientation != -1) {
                ((androidx.constraintlayout.solver.widgets.Flow)object).setOrientation(layoutParams.orientation);
            }
        }
    }

    @Override
    protected void onMeasure(int n, int n2) {
        this.onMeasure(this.mFlow, n, n2);
    }

    @Override
    public void onMeasure(VirtualLayout virtualLayout, int n, int n2) {
        int n3 = View.MeasureSpec.getMode((int)n);
        int n4 = View.MeasureSpec.getSize((int)n);
        n = View.MeasureSpec.getMode((int)n2);
        n2 = View.MeasureSpec.getSize((int)n2);
        if (virtualLayout != null) {
            virtualLayout.measure(n3, n4, n, n2);
            this.setMeasuredDimension(virtualLayout.getMeasuredWidth(), virtualLayout.getMeasuredHeight());
        } else {
            this.setMeasuredDimension(0, 0);
        }
    }

    @Override
    public void resolveRtl(ConstraintWidget constraintWidget, boolean bl) {
        this.mFlow.applyRtl(bl);
    }

    public void setFirstHorizontalBias(float f) {
        this.mFlow.setFirstHorizontalBias(f);
        this.requestLayout();
    }

    public void setFirstHorizontalStyle(int n) {
        this.mFlow.setFirstHorizontalStyle(n);
        this.requestLayout();
    }

    public void setFirstVerticalBias(float f) {
        this.mFlow.setFirstVerticalBias(f);
        this.requestLayout();
    }

    public void setFirstVerticalStyle(int n) {
        this.mFlow.setFirstVerticalStyle(n);
        this.requestLayout();
    }

    public void setHorizontalAlign(int n) {
        this.mFlow.setHorizontalAlign(n);
        this.requestLayout();
    }

    public void setHorizontalBias(float f) {
        this.mFlow.setHorizontalBias(f);
        this.requestLayout();
    }

    public void setHorizontalGap(int n) {
        this.mFlow.setHorizontalGap(n);
        this.requestLayout();
    }

    public void setHorizontalStyle(int n) {
        this.mFlow.setHorizontalStyle(n);
        this.requestLayout();
    }

    public void setMaxElementsWrap(int n) {
        this.mFlow.setMaxElementsWrap(n);
        this.requestLayout();
    }

    public void setOrientation(int n) {
        this.mFlow.setOrientation(n);
        this.requestLayout();
    }

    public void setPadding(int n) {
        this.mFlow.setPadding(n);
        this.requestLayout();
    }

    public void setPaddingBottom(int n) {
        this.mFlow.setPaddingBottom(n);
        this.requestLayout();
    }

    public void setPaddingLeft(int n) {
        this.mFlow.setPaddingLeft(n);
        this.requestLayout();
    }

    public void setPaddingRight(int n) {
        this.mFlow.setPaddingRight(n);
        this.requestLayout();
    }

    public void setPaddingTop(int n) {
        this.mFlow.setPaddingTop(n);
        this.requestLayout();
    }

    public void setVerticalAlign(int n) {
        this.mFlow.setVerticalAlign(n);
        this.requestLayout();
    }

    public void setVerticalBias(float f) {
        this.mFlow.setVerticalBias(f);
        this.requestLayout();
    }

    public void setVerticalGap(int n) {
        this.mFlow.setVerticalGap(n);
        this.requestLayout();
    }

    public void setVerticalStyle(int n) {
        this.mFlow.setVerticalStyle(n);
        this.requestLayout();
    }

    public void setWrapMode(int n) {
        this.mFlow.setWrapMode(n);
        this.requestLayout();
    }
}

