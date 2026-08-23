/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.accessibility.AccessibilityEvent
 *  android.view.accessibility.AccessibilityNodeInfo
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.appcompat.R;
import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.widget.ViewUtils;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LinearLayoutCompat
extends ViewGroup {
    private static final String ACCESSIBILITY_CLASS_NAME = "androidx.appcompat.widget.LinearLayoutCompat";
    public static final int HORIZONTAL = 0;
    private static final int INDEX_BOTTOM = 2;
    private static final int INDEX_CENTER_VERTICAL = 0;
    private static final int INDEX_FILL = 3;
    private static final int INDEX_TOP = 1;
    public static final int SHOW_DIVIDER_BEGINNING = 1;
    public static final int SHOW_DIVIDER_END = 4;
    public static final int SHOW_DIVIDER_MIDDLE = 2;
    public static final int SHOW_DIVIDER_NONE = 0;
    public static final int VERTICAL = 1;
    private static final int VERTICAL_GRAVITY_COUNT = 4;
    private boolean mBaselineAligned = true;
    private int mBaselineAlignedChildIndex = -1;
    private int mBaselineChildTop = 0;
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerPadding;
    private int mDividerWidth;
    private int mGravity = 0x800033;
    private int[] mMaxAscent;
    private int[] mMaxDescent;
    private int mOrientation;
    private int mShowDividers;
    private int mTotalLength;
    private boolean mUseLargestChild;
    private float mWeightSum;

    public LinearLayoutCompat(Context context) {
        this(context, null);
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        boolean bl;
        TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(context, attributeSet, R.styleable.LinearLayoutCompat, n, 0);
        ViewCompat.saveAttributeDataForStyleable((View)this, context, R.styleable.LinearLayoutCompat, attributeSet, tintTypedArray.getWrappedTypeArray(), n, 0);
        n = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_android_orientation, -1);
        if (n >= 0) {
            this.setOrientation(n);
        }
        if ((n = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_android_gravity, -1)) >= 0) {
            this.setGravity(n);
        }
        if (!(bl = tintTypedArray.getBoolean(R.styleable.LinearLayoutCompat_android_baselineAligned, true))) {
            this.setBaselineAligned(bl);
        }
        this.mWeightSum = tintTypedArray.getFloat(R.styleable.LinearLayoutCompat_android_weightSum, -1.0f);
        this.mBaselineAlignedChildIndex = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
        this.mUseLargestChild = tintTypedArray.getBoolean(R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
        this.setDividerDrawable(tintTypedArray.getDrawable(R.styleable.LinearLayoutCompat_divider));
        this.mShowDividers = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_showDividers, 0);
        this.mDividerPadding = tintTypedArray.getDimensionPixelSize(R.styleable.LinearLayoutCompat_dividerPadding, 0);
        tintTypedArray.recycle();
    }

    private void forceUniformHeight(int n, int n2) {
        int n3 = View.MeasureSpec.makeMeasureSpec((int)this.getMeasuredHeight(), (int)0x40000000);
        for (int i = 0; i < n; ++i) {
            View view = this.getVirtualChildAt(i);
            if (view.getVisibility() == 8) continue;
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (layoutParams.height != -1) continue;
            int n4 = layoutParams.width;
            layoutParams.width = view.getMeasuredWidth();
            this.measureChildWithMargins(view, n2, 0, n3, 0);
            layoutParams.width = n4;
        }
    }

    private void forceUniformWidth(int n, int n2) {
        int n3 = View.MeasureSpec.makeMeasureSpec((int)this.getMeasuredWidth(), (int)0x40000000);
        for (int i = 0; i < n; ++i) {
            View view = this.getVirtualChildAt(i);
            if (view.getVisibility() == 8) continue;
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (layoutParams.width != -1) continue;
            int n4 = layoutParams.height;
            layoutParams.height = view.getMeasuredHeight();
            this.measureChildWithMargins(view, n3, 0, n2, 0);
            layoutParams.height = n4;
        }
    }

    private void setChildFrame(View view, int n, int n2, int n3, int n4) {
        view.layout(n, n2, n + n3, n2 + n4);
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    void drawDividersHorizontal(Canvas canvas) {
        LayoutParams layoutParams;
        View view;
        int n;
        int n2 = this.getVirtualChildCount();
        boolean bl = ViewUtils.isLayoutRtl((View)this);
        for (n = 0; n < n2; ++n) {
            view = this.getVirtualChildAt(n);
            if (view == null || view.getVisibility() == 8 || !this.hasDividerBeforeChildAt(n)) continue;
            layoutParams = (LayoutParams)view.getLayoutParams();
            int n3 = bl ? view.getRight() + layoutParams.rightMargin : view.getLeft() - layoutParams.leftMargin - this.mDividerWidth;
            this.drawVerticalDivider(canvas, n3);
        }
        if (this.hasDividerBeforeChildAt(n2)) {
            view = this.getVirtualChildAt(n2 - 1);
            if (view == null) {
                n = bl ? this.getPaddingLeft() : this.getWidth() - this.getPaddingRight() - this.mDividerWidth;
            } else {
                layoutParams = (LayoutParams)view.getLayoutParams();
                n = bl ? view.getLeft() - layoutParams.leftMargin - this.mDividerWidth : view.getRight() + layoutParams.rightMargin;
            }
            this.drawVerticalDivider(canvas, n);
        }
    }

    void drawDividersVertical(Canvas canvas) {
        LayoutParams layoutParams;
        View view;
        int n;
        int n2 = this.getVirtualChildCount();
        for (n = 0; n < n2; ++n) {
            view = this.getVirtualChildAt(n);
            if (view == null || view.getVisibility() == 8 || !this.hasDividerBeforeChildAt(n)) continue;
            layoutParams = (LayoutParams)view.getLayoutParams();
            this.drawHorizontalDivider(canvas, view.getTop() - layoutParams.topMargin - this.mDividerHeight);
        }
        if (this.hasDividerBeforeChildAt(n2)) {
            view = this.getVirtualChildAt(n2 - 1);
            if (view == null) {
                n = this.getHeight() - this.getPaddingBottom() - this.mDividerHeight;
            } else {
                layoutParams = (LayoutParams)view.getLayoutParams();
                n = view.getBottom() + layoutParams.bottomMargin;
            }
            this.drawHorizontalDivider(canvas, n);
        }
    }

    void drawHorizontalDivider(Canvas canvas, int n) {
        this.mDivider.setBounds(this.getPaddingLeft() + this.mDividerPadding, n, this.getWidth() - this.getPaddingRight() - this.mDividerPadding, this.mDividerHeight + n);
        this.mDivider.draw(canvas);
    }

    void drawVerticalDivider(Canvas canvas, int n) {
        this.mDivider.setBounds(n, this.getPaddingTop() + this.mDividerPadding, this.mDividerWidth + n, this.getHeight() - this.getPaddingBottom() - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }

    protected LayoutParams generateDefaultLayoutParams() {
        int n = this.mOrientation;
        if (n == 0) {
            return new LayoutParams(-2, -2);
        }
        if (n == 1) {
            return new LayoutParams(-1, -2);
        }
        return null;
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(this.getContext(), attributeSet);
    }

    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    public int getBaseline() {
        int n;
        if (this.mBaselineAlignedChildIndex < 0) {
            return super.getBaseline();
        }
        int n2 = this.getChildCount();
        if (n2 > (n = this.mBaselineAlignedChildIndex)) {
            View view = this.getChildAt(n);
            int n3 = view.getBaseline();
            if (n3 == -1) {
                if (this.mBaselineAlignedChildIndex == 0) {
                    return -1;
                }
                throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
            }
            n2 = n = this.mBaselineChildTop;
            if (this.mOrientation == 1) {
                int n4 = this.mGravity & 0x70;
                n2 = n;
                if (n4 != 48) {
                    switch (n4) {
                        default: {
                            n2 = n;
                            break;
                        }
                        case 80: {
                            n2 = this.getBottom() - this.getTop() - this.getPaddingBottom() - this.mTotalLength;
                            break;
                        }
                        case 16: {
                            n2 = n + (this.getBottom() - this.getTop() - this.getPaddingTop() - this.getPaddingBottom() - this.mTotalLength) / 2;
                        }
                    }
                }
            }
            return ((LayoutParams)view.getLayoutParams()).topMargin + n2 + n3;
        }
        throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
    }

    public int getBaselineAlignedChildIndex() {
        return this.mBaselineAlignedChildIndex;
    }

    int getChildrenSkipCount(View view, int n) {
        return 0;
    }

    public Drawable getDividerDrawable() {
        return this.mDivider;
    }

    public int getDividerPadding() {
        return this.mDividerPadding;
    }

    public int getDividerWidth() {
        return this.mDividerWidth;
    }

    public int getGravity() {
        return this.mGravity;
    }

    int getLocationOffset(View view) {
        return 0;
    }

    int getNextLocationOffset(View view) {
        return 0;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public int getShowDividers() {
        return this.mShowDividers;
    }

    View getVirtualChildAt(int n) {
        return this.getChildAt(n);
    }

    int getVirtualChildCount() {
        return this.getChildCount();
    }

    public float getWeightSum() {
        return this.mWeightSum;
    }

    protected boolean hasDividerBeforeChildAt(int n) {
        boolean bl = false;
        boolean bl2 = false;
        if (n == 0) {
            bl = bl2;
            if ((this.mShowDividers & 1) != 0) {
                bl = true;
            }
            return bl;
        }
        if (n == this.getChildCount()) {
            if ((this.mShowDividers & 4) != 0) {
                bl = true;
            }
            return bl;
        }
        if ((this.mShowDividers & 2) != 0) {
            bl2 = false;
            --n;
            while (true) {
                bl = bl2;
                if (n < 0) break;
                if (this.getChildAt(n).getVisibility() != 8) {
                    bl = true;
                    break;
                }
                --n;
            }
            return bl;
        }
        return false;
    }

    public boolean isBaselineAligned() {
        return this.mBaselineAligned;
    }

    public boolean isMeasureWithLargestChildEnabled() {
        return this.mUseLargestChild;
    }

    void layoutHorizontal(int n, int n2, int n3, int n4) {
        int n5;
        int n6;
        boolean bl = ViewUtils.isLayoutRtl((View)this);
        int n7 = this.getPaddingTop();
        int n8 = n4 - n2;
        int n9 = n8 - this.getPaddingBottom();
        int n10 = this.getPaddingBottom();
        int n11 = this.getVirtualChildCount();
        int n12 = this.mGravity;
        boolean bl2 = this.mBaselineAligned;
        int[] nArray = this.mMaxAscent;
        int[] nArray2 = this.mMaxDescent;
        int n13 = ViewCompat.getLayoutDirection((View)this);
        switch (GravityCompat.getAbsoluteGravity(n12 & 0x800007, n13)) {
            default: {
                n = this.getPaddingLeft();
                break;
            }
            case 5: {
                n = this.getPaddingLeft() + n3 - n - this.mTotalLength;
                break;
            }
            case 1: {
                n = this.getPaddingLeft() + (n3 - n - this.mTotalLength) / 2;
            }
        }
        if (bl) {
            n6 = n11 - 1;
            n5 = -1;
        } else {
            n6 = 0;
            n5 = 1;
        }
        int n14 = n8;
        n3 = n7;
        n4 = n;
        for (n2 = 0; n2 < n11; ++n2) {
            int n15 = n6 + n5 * n2;
            View view = this.getVirtualChildAt(n15);
            if (view == null) {
                n4 += this.measureNullChild(n15);
                continue;
            }
            if (view.getVisibility() == 8) continue;
            int n16 = view.getMeasuredWidth();
            int n17 = view.getMeasuredHeight();
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            n = bl2 && layoutParams.height != -1 ? view.getBaseline() : -1;
            int n18 = layoutParams.gravity;
            if (n18 < 0) {
                n18 = n12 & 0x70;
            }
            switch (n18 & 0x70) {
                default: {
                    n = n3;
                    break;
                }
                case 80: {
                    n18 = n9 - n17 - layoutParams.bottomMargin;
                    if (n != -1) {
                        int n19 = view.getMeasuredHeight();
                        n = n18 - (nArray2[2] - (n19 - n));
                        break;
                    }
                    n = n18;
                    break;
                }
                case 48: {
                    n18 = layoutParams.topMargin + n3;
                    if (n != -1) {
                        n = n18 + (nArray[1] - n);
                        break;
                    }
                    n = n18;
                    break;
                }
                case 16: {
                    n = (n8 - n7 - n10 - n17) / 2 + n3 + layoutParams.topMargin - layoutParams.bottomMargin;
                }
            }
            n18 = n4;
            if (this.hasDividerBeforeChildAt(n15)) {
                n18 = n4 + this.mDividerWidth;
            }
            n4 = n18 + layoutParams.leftMargin;
            this.setChildFrame(view, n4 + this.getLocationOffset(view), n, n16, n17);
            n = layoutParams.rightMargin;
            n18 = this.getNextLocationOffset(view);
            n2 += this.getChildrenSkipCount(view, n15);
            n4 += n16 + n + n18;
        }
    }

    void layoutVertical(int n, int n2, int n3, int n4) {
        int n5 = this.getPaddingLeft();
        int n6 = n3 - n;
        int n7 = this.getPaddingRight();
        int n8 = this.getPaddingRight();
        int n9 = this.getVirtualChildCount();
        int n10 = this.mGravity;
        switch (n10 & 0x70) {
            default: {
                n = this.getPaddingTop();
                break;
            }
            case 80: {
                n = this.getPaddingTop() + n4 - n2 - this.mTotalLength;
                break;
            }
            case 16: {
                n = this.getPaddingTop() + (n4 - n2 - this.mTotalLength) / 2;
            }
        }
        n2 = 0;
        n3 = n5;
        while (true) {
            n4 = n3;
            if (n2 >= n9) break;
            View view = this.getVirtualChildAt(n2);
            if (view == null) {
                n += this.measureNullChild(n2);
            } else if (view.getVisibility() != 8) {
                int n11 = view.getMeasuredWidth();
                int n12 = view.getMeasuredHeight();
                LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                n3 = layoutParams.gravity;
                if (n3 < 0) {
                    n3 = n10 & 0x800007;
                }
                switch (GravityCompat.getAbsoluteGravity(n3, ViewCompat.getLayoutDirection((View)this)) & 7) {
                    default: {
                        n3 = layoutParams.leftMargin + n4;
                        break;
                    }
                    case 5: {
                        n3 = n6 - n7 - n11 - layoutParams.rightMargin;
                        break;
                    }
                    case 1: {
                        n3 = (n6 - n5 - n8 - n11) / 2 + n4 + layoutParams.leftMargin - layoutParams.rightMargin;
                    }
                }
                int n13 = n;
                if (this.hasDividerBeforeChildAt(n2)) {
                    n13 = n + this.mDividerHeight;
                }
                n = n13 + layoutParams.topMargin;
                this.setChildFrame(view, n3, n + this.getLocationOffset(view), n11, n12);
                n13 = layoutParams.bottomMargin;
                n3 = this.getNextLocationOffset(view);
                n2 += this.getChildrenSkipCount(view, n2);
                n += n12 + n13 + n3;
            }
            ++n2;
            n3 = n4;
        }
    }

    void measureChildBeforeLayout(View view, int n, int n2, int n3, int n4, int n5) {
        this.measureChildWithMargins(view, n2, n3, n4, n5);
    }

    void measureHorizontal(int n, int n2) {
        block44: {
            int n3;
            int n4;
            int n5;
            int n6;
            LayoutParams layoutParams;
            Object object;
            int n7;
            this.mTotalLength = 0;
            int n8 = this.getVirtualChildCount();
            int n9 = View.MeasureSpec.getMode((int)n);
            int n10 = View.MeasureSpec.getMode((int)n2);
            if (this.mMaxAscent == null || this.mMaxDescent == null) {
                this.mMaxAscent = new int[4];
                this.mMaxDescent = new int[4];
            }
            int[] nArray = this.mMaxAscent;
            Object object2 = this.mMaxDescent;
            nArray[3] = -1;
            nArray[2] = -1;
            nArray[1] = -1;
            nArray[0] = -1;
            object2[3] = -1;
            object2[2] = -1;
            object2[1] = -1;
            object2[0] = -1;
            boolean bl = this.mBaselineAligned;
            boolean bl2 = this.mUseLargestChild;
            boolean bl3 = n9 == 0x40000000;
            int n11 = 0;
            float f = 0.0f;
            int n12 = 0;
            int n13 = 0;
            int n14 = 0;
            int n15 = 0;
            boolean bl4 = true;
            int n16 = 0;
            int n17 = 0;
            for (n7 = 0; n7 < n8; ++n7) {
                int n18;
                object = this.getVirtualChildAt(n7);
                if (object == null) {
                    this.mTotalLength += this.measureNullChild(n7);
                    continue;
                }
                if (object.getVisibility() == 8) {
                    n7 += this.getChildrenSkipCount((View)object, n7);
                    continue;
                }
                if (this.hasDividerBeforeChildAt(n7)) {
                    this.mTotalLength += this.mDividerWidth;
                }
                layoutParams = (LayoutParams)object.getLayoutParams();
                f += layoutParams.weight;
                if (n9 == 0x40000000 && layoutParams.width == 0 && layoutParams.weight > 0.0f) {
                    if (bl3) {
                        this.mTotalLength += layoutParams.leftMargin + layoutParams.rightMargin;
                    } else {
                        n6 = this.mTotalLength;
                        this.mTotalLength = Math.max(n6, layoutParams.leftMargin + n6 + layoutParams.rightMargin);
                    }
                    if (bl) {
                        n6 = View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
                        object.measure(n6, n6);
                        n6 = n16;
                    } else {
                        n14 = 1;
                        n6 = n16;
                    }
                } else {
                    if (layoutParams.width == 0 && layoutParams.weight > 0.0f) {
                        layoutParams.width = -2;
                        n6 = 0;
                    } else {
                        n6 = Integer.MIN_VALUE;
                    }
                    n5 = f == 0.0f ? this.mTotalLength : 0;
                    n4 = n16;
                    this.measureChildBeforeLayout((View)object, n7, n, n5, n2, 0);
                    if (n6 != Integer.MIN_VALUE) {
                        layoutParams.width = n6;
                    }
                    n5 = object.getMeasuredWidth();
                    if (bl3) {
                        this.mTotalLength += layoutParams.leftMargin + n5 + layoutParams.rightMargin + this.getNextLocationOffset((View)object);
                    } else {
                        n6 = this.mTotalLength;
                        this.mTotalLength = Math.max(n6, n6 + n5 + layoutParams.leftMargin + layoutParams.rightMargin + this.getNextLocationOffset((View)object));
                    }
                    if (bl2) {
                        n13 = Math.max(n5, n13);
                    }
                }
                n6 = n16;
                n5 = n4 = 0;
                n16 = n15;
                if (n10 != 0x40000000) {
                    n5 = n4;
                    n16 = n15;
                    if (layoutParams.height == -1) {
                        n16 = 1;
                        n5 = 1;
                    }
                }
                n15 = layoutParams.topMargin + layoutParams.bottomMargin;
                n4 = object.getMeasuredHeight() + n15;
                n3 = View.combineMeasuredStates((int)n12, (int)object.getMeasuredState());
                if (bl && (n18 = object.getBaseline()) != -1) {
                    n12 = layoutParams.gravity < 0 ? this.mGravity : layoutParams.gravity;
                    n12 = ((n12 & 0x70) >> 4 & 0xFFFFFFFE) >> 1;
                    nArray[n12] = Math.max(nArray[n12], n18);
                    object2[n12] = Math.max(object2[n12], n4 - n18);
                }
                n11 = Math.max(n11, n4);
                bl4 = bl4 && layoutParams.height == -1;
                if (layoutParams.weight > 0.0f) {
                    if (n5 == 0) {
                        n15 = n4;
                    }
                    n17 = Math.max(n17, n15);
                    n12 = n6;
                } else {
                    n12 = n5 != 0 ? n15 : n4;
                    n12 = Math.max(n6, n12);
                }
                n7 += this.getChildrenSkipCount((View)object, n7);
                n6 = n3;
                n5 = n12;
                n15 = n16;
                n12 = n6;
                n16 = n5;
            }
            n7 = n17;
            n6 = n13;
            if (this.mTotalLength > 0 && this.hasDividerBeforeChildAt(n8)) {
                this.mTotalLength += this.mDividerWidth;
            }
            n13 = nArray[1] == -1 && nArray[0] == -1 && nArray[2] == -1 && nArray[3] == -1 ? n11 : Math.max(n11, Math.max(nArray[3], Math.max(nArray[0], Math.max(nArray[1], nArray[2]))) + Math.max(object2[3], Math.max(object2[0], Math.max(object2[1], object2[2]))));
            if (bl2 && (n9 == Integer.MIN_VALUE || n9 == 0)) {
                this.mTotalLength = 0;
                for (n17 = 0; n17 < n8; ++n17) {
                    layoutParams = this.getVirtualChildAt(n17);
                    if (layoutParams == null) {
                        this.mTotalLength += this.measureNullChild(n17);
                        continue;
                    }
                    if (layoutParams.getVisibility() == 8) {
                        n17 += this.getChildrenSkipCount((View)layoutParams, n17);
                        continue;
                    }
                    object = (LayoutParams)layoutParams.getLayoutParams();
                    if (bl3) {
                        this.mTotalLength += object.leftMargin + n6 + object.rightMargin + this.getNextLocationOffset((View)layoutParams);
                        continue;
                    }
                    n11 = this.mTotalLength;
                    this.mTotalLength = Math.max(n11, n11 + n6 + object.leftMargin + object.rightMargin + this.getNextLocationOffset((View)layoutParams));
                }
            }
            this.mTotalLength += this.getPaddingLeft() + this.getPaddingRight();
            n17 = View.resolveSizeAndState((int)Math.max(this.mTotalLength, this.getSuggestedMinimumWidth()), (int)n, (int)0);
            n11 = n17 & 0xFFFFFF;
            n4 = n11 - this.mTotalLength;
            if (!(n14 != 0 || n4 != 0 && f > 0.0f)) {
                n16 = Math.max(n16, n7);
                if (bl2 && n9 != 0x40000000) {
                    n9 = n11;
                    for (n7 = 0; n7 < n8; ++n7) {
                        object2 = this.getVirtualChildAt(n7);
                        if (object2 == null || object2.getVisibility() == 8 || !(((LayoutParams)object2.getLayoutParams()).weight > 0.0f)) continue;
                        object2.measure(View.MeasureSpec.makeMeasureSpec((int)n6, (int)0x40000000), View.MeasureSpec.makeMeasureSpec((int)object2.getMeasuredHeight(), (int)0x40000000));
                    }
                }
            } else {
                n6 = n16;
                float f2 = this.mWeightSum;
                if (f2 > 0.0f) {
                    f = f2;
                }
                nArray[3] = -1;
                nArray[2] = -1;
                nArray[1] = -1;
                nArray[0] = -1;
                object2[3] = -1;
                object2[2] = -1;
                object2[1] = -1;
                object2[0] = -1;
                this.mTotalLength = 0;
                n5 = 0;
                n13 = n4;
                n11 = -1;
                n16 = n12;
                n14 = n7;
                n7 = n11;
                n11 = n9;
                n9 = n6;
                n12 = n8;
                for (n6 = n5; n6 < n12; ++n6) {
                    object = this.getVirtualChildAt(n6);
                    if (object == null || object.getVisibility() == 8) continue;
                    layoutParams = (LayoutParams)object.getLayoutParams();
                    f2 = layoutParams.weight;
                    if (f2 > 0.0f) {
                        n5 = (int)((float)n13 * f2 / f);
                        n3 = LinearLayoutCompat.getChildMeasureSpec((int)n2, (int)(this.getPaddingTop() + this.getPaddingBottom() + layoutParams.topMargin + layoutParams.bottomMargin), (int)layoutParams.height);
                        if (layoutParams.width == 0 && n11 == 0x40000000) {
                            n8 = n5 > 0 ? n5 : 0;
                            object.measure(View.MeasureSpec.makeMeasureSpec((int)n8, (int)0x40000000), n3);
                        } else {
                            n8 = n4 = object.getMeasuredWidth() + n5;
                            if (n4 < 0) {
                                n8 = 0;
                            }
                            object.measure(View.MeasureSpec.makeMeasureSpec((int)n8, (int)0x40000000), n3);
                        }
                        n16 = View.combineMeasuredStates((int)n16, (int)(object.getMeasuredState() & 0xFF000000));
                        f -= f2;
                        n13 -= n5;
                    }
                    if (bl3) {
                        this.mTotalLength += object.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin + this.getNextLocationOffset((View)object);
                    } else {
                        n8 = this.mTotalLength;
                        this.mTotalLength = Math.max(n8, object.getMeasuredWidth() + n8 + layoutParams.leftMargin + layoutParams.rightMargin + this.getNextLocationOffset((View)object));
                    }
                    n8 = n10 != 0x40000000 && layoutParams.height == -1 ? 1 : 0;
                    n4 = layoutParams.topMargin + layoutParams.bottomMargin;
                    n5 = object.getMeasuredHeight() + n4;
                    n7 = Math.max(n7, n5);
                    n8 = n8 != 0 ? n4 : n5;
                    n8 = Math.max(n9, n8);
                    bl4 = bl4 && layoutParams.height == -1;
                    if (bl && (n4 = object.getBaseline()) != -1) {
                        n9 = layoutParams.gravity < 0 ? this.mGravity : layoutParams.gravity;
                        n9 = ((n9 & 0x70) >> 4 & 0xFFFFFFFE) >> 1;
                        nArray[n9] = Math.max(nArray[n9], n4);
                        object2[n9] = Math.max(object2[n9], n5 - n4);
                    }
                    n9 = n8;
                }
                this.mTotalLength += this.getPaddingLeft() + this.getPaddingRight();
                n13 = nArray[1] == -1 && nArray[0] == -1 && nArray[2] == -1 && nArray[3] == -1 ? n7 : Math.max(n7, Math.max(nArray[3], Math.max(nArray[0], Math.max(nArray[1], nArray[2]))) + Math.max(object2[3], Math.max(object2[0], Math.max(object2[1], object2[2]))));
                n8 = n12;
                n12 = n16;
                n16 = n9;
            }
            n9 = n13;
            if (!bl4) {
                n9 = n13;
                if (n10 != 0x40000000) {
                    n9 = n16;
                }
            }
            this.setMeasuredDimension(n17 | 0xFF000000 & n12, View.resolveSizeAndState((int)Math.max(n9 + (this.getPaddingTop() + this.getPaddingBottom()), this.getSuggestedMinimumHeight()), (int)n2, (int)(n12 << 16)));
            if (n15 == 0) break block44;
            this.forceUniformHeight(n8, n);
        }
    }

    int measureNullChild(int n) {
        return 0;
    }

    void measureVertical(int n, int n2) {
        int n3;
        int n4;
        int n5;
        int n6;
        LayoutParams layoutParams;
        Object object;
        int n7;
        this.mTotalLength = 0;
        int n8 = this.getVirtualChildCount();
        int n9 = View.MeasureSpec.getMode((int)n);
        int n10 = View.MeasureSpec.getMode((int)n2);
        int n11 = this.mBaselineAlignedChildIndex;
        boolean bl = this.mUseLargestChild;
        int n12 = 0;
        int n13 = 0;
        float f = 0.0f;
        int n14 = 0;
        int n15 = 0;
        int n16 = 0;
        int n17 = 0;
        int n18 = 0;
        int n19 = 1;
        for (n7 = 0; n7 < n8; ++n7) {
            object = this.getVirtualChildAt(n7);
            if (object == null) {
                this.mTotalLength += this.measureNullChild(n7);
                continue;
            }
            if (object.getVisibility() == 8) {
                n7 += this.getChildrenSkipCount((View)object, n7);
                continue;
            }
            if (this.hasDividerBeforeChildAt(n7)) {
                this.mTotalLength += this.mDividerHeight;
            }
            layoutParams = (LayoutParams)object.getLayoutParams();
            f += layoutParams.weight;
            if (n10 == 0x40000000 && layoutParams.height == 0 && layoutParams.weight > 0.0f) {
                n12 = this.mTotalLength;
                this.mTotalLength = Math.max(n12, layoutParams.topMargin + n12 + layoutParams.bottomMargin);
                n12 = 1;
            } else {
                if (layoutParams.height == 0 && layoutParams.weight > 0.0f) {
                    layoutParams.height = -2;
                    n6 = 0;
                } else {
                    n6 = Integer.MIN_VALUE;
                }
                n5 = f == 0.0f ? this.mTotalLength : 0;
                this.measureChildBeforeLayout((View)object, n7, n, 0, n2, n5);
                if (n6 != Integer.MIN_VALUE) {
                    layoutParams.height = n6;
                }
                n5 = object.getMeasuredHeight();
                n6 = this.mTotalLength;
                this.mTotalLength = Math.max(n6, n6 + n5 + layoutParams.topMargin + layoutParams.bottomMargin + this.getNextLocationOffset((View)object));
                if (bl) {
                    n18 = Math.max(n5, n18);
                }
            }
            n6 = n14;
            if (n11 >= 0 && n11 == n7 + 1) {
                this.mBaselineChildTop = this.mTotalLength;
            }
            if (n7 < n11 && layoutParams.weight > 0.0f) {
                throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
            }
            n5 = n4 = 0;
            n14 = n15;
            if (n9 != 0x40000000) {
                n5 = n4;
                n14 = n15;
                if (layoutParams.width == -1) {
                    n14 = 1;
                    n5 = 1;
                }
            }
            n4 = layoutParams.leftMargin + layoutParams.rightMargin;
            n15 = object.getMeasuredWidth() + n4;
            n13 = Math.max(n13, n15);
            n3 = View.combineMeasuredStates((int)n16, (int)object.getMeasuredState());
            n16 = n19 != 0 && layoutParams.width == -1 ? 1 : 0;
            if (layoutParams.weight > 0.0f) {
                n19 = n5 != 0 ? n4 : n15;
                n19 = Math.max(n17, n19);
                n17 = n6;
            } else {
                n19 = n17;
                if (n5 != 0) {
                    n15 = n4;
                }
                n17 = Math.max(n6, n15);
            }
            n15 = this.getChildrenSkipCount((View)object, n7);
            n6 = n19;
            n5 = n15 + n7;
            n7 = n3;
            n15 = n14;
            n19 = n16;
            n16 = n7;
            n14 = n17;
            n17 = n6;
            n7 = n5;
        }
        n6 = n14;
        n14 = n17;
        if (this.mTotalLength > 0 && this.hasDividerBeforeChildAt(n8)) {
            this.mTotalLength += this.mDividerHeight;
        }
        if (bl) {
            if (n10 != Integer.MIN_VALUE && n10 != 0) {
                n17 = n16;
            } else {
                this.mTotalLength = 0;
                for (n17 = 0; n17 < n8; ++n17) {
                    object = this.getVirtualChildAt(n17);
                    if (object == null) {
                        this.mTotalLength += this.measureNullChild(n17);
                        continue;
                    }
                    if (object.getVisibility() == 8) {
                        n17 += this.getChildrenSkipCount((View)object, n17);
                        continue;
                    }
                    layoutParams = (LayoutParams)object.getLayoutParams();
                    n7 = this.mTotalLength;
                    this.mTotalLength = Math.max(n7, n7 + n18 + layoutParams.topMargin + layoutParams.bottomMargin + this.getNextLocationOffset((View)object));
                }
                n17 = n16;
            }
        } else {
            n17 = n16;
        }
        n7 = n10;
        this.mTotalLength += this.getPaddingTop() + this.getPaddingBottom();
        n16 = Math.max(this.mTotalLength, this.getSuggestedMinimumHeight());
        n10 = n18;
        n3 = View.resolveSizeAndState((int)n16, (int)n2, (int)0);
        n16 = n3 & 0xFFFFFF;
        n18 = n16 - this.mTotalLength;
        if (!(n12 != 0 || n18 != 0 && f > 0.0f)) {
            n18 = Math.max(n6, n14);
            if (bl && n7 != 0x40000000) {
                for (n7 = 0; n7 < n8; ++n7) {
                    object = this.getVirtualChildAt(n7);
                    if (object == null || object.getVisibility() == 8 || !(((LayoutParams)object.getLayoutParams()).weight > 0.0f)) continue;
                    object.measure(View.MeasureSpec.makeMeasureSpec((int)object.getMeasuredWidth(), (int)0x40000000), View.MeasureSpec.makeMeasureSpec((int)n10, (int)0x40000000));
                }
            }
            n16 = n18;
        } else {
            float f2 = this.mWeightSum;
            if (f2 > 0.0f) {
                f = f2;
            }
            this.mTotalLength = 0;
            n14 = n11;
            n16 = n6;
            for (n12 = 0; n12 < n8; ++n12) {
                layoutParams = this.getVirtualChildAt(n12);
                if (layoutParams.getVisibility() == 8) continue;
                object = (LayoutParams)layoutParams.getLayoutParams();
                f2 = object.weight;
                if (f2 > 0.0f) {
                    n5 = (int)((float)n18 * f2 / f);
                    n11 = this.getPaddingLeft();
                    int n20 = this.getPaddingRight();
                    n4 = object.leftMargin;
                    int n21 = object.rightMargin;
                    int n22 = object.width;
                    n6 = n18 - n5;
                    n4 = LinearLayoutCompat.getChildMeasureSpec((int)n, (int)(n11 + n20 + n4 + n21), (int)n22);
                    if (object.height == 0 && n7 == 0x40000000) {
                        n18 = n5 > 0 ? n5 : 0;
                        layoutParams.measure(n4, View.MeasureSpec.makeMeasureSpec((int)n18, (int)0x40000000));
                    } else {
                        n18 = n5 = layoutParams.getMeasuredHeight() + n5;
                        if (n5 < 0) {
                            n18 = 0;
                        }
                        layoutParams.measure(n4, View.MeasureSpec.makeMeasureSpec((int)n18, (int)0x40000000));
                    }
                    n17 = View.combineMeasuredStates((int)n17, (int)(layoutParams.getMeasuredState() & 0xFFFFFF00));
                    f -= f2;
                    n18 = n6;
                }
                n4 = object.leftMargin + object.rightMargin;
                n5 = layoutParams.getMeasuredWidth() + n4;
                n6 = Math.max(n13, n5);
                n13 = n9 != 0x40000000 && object.width == -1 ? 1 : 0;
                n13 = n13 != 0 ? n4 : n5;
                n13 = Math.max(n16, n13);
                n16 = n19 != 0 && object.width == -1 ? 1 : 0;
                n19 = this.mTotalLength;
                this.mTotalLength = Math.max(n19, n19 + layoutParams.getMeasuredHeight() + object.topMargin + object.bottomMargin + this.getNextLocationOffset((View)layoutParams));
                n19 = n16;
                n16 = n13;
                n13 = n6;
            }
            this.mTotalLength += this.getPaddingTop() + this.getPaddingBottom();
        }
        n18 = n13;
        if (n19 == 0) {
            n18 = n13;
            if (n9 != 0x40000000) {
                n18 = n16;
            }
        }
        this.setMeasuredDimension(View.resolveSizeAndState((int)Math.max(n18 + (this.getPaddingLeft() + this.getPaddingRight()), this.getSuggestedMinimumWidth()), (int)n, (int)n17), n3);
        if (n15 != 0) {
            this.forceUniformWidth(n8, n2);
        }
    }

    protected void onDraw(Canvas canvas) {
        if (this.mDivider == null) {
            return;
        }
        if (this.mOrientation == 1) {
            this.drawDividersVertical(canvas);
        } else {
            this.drawDividersHorizontal(canvas);
        }
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName((CharSequence)ACCESSIBILITY_CLASS_NAME);
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName((CharSequence)ACCESSIBILITY_CLASS_NAME);
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        if (this.mOrientation == 1) {
            this.layoutVertical(n, n2, n3, n4);
        } else {
            this.layoutHorizontal(n, n2, n3, n4);
        }
    }

    protected void onMeasure(int n, int n2) {
        if (this.mOrientation == 1) {
            this.measureVertical(n, n2);
        } else {
            this.measureHorizontal(n, n2);
        }
    }

    public void setBaselineAligned(boolean bl) {
        this.mBaselineAligned = bl;
    }

    public void setBaselineAlignedChildIndex(int n) {
        if (n >= 0 && n < this.getChildCount()) {
            this.mBaselineAlignedChildIndex = n;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("base aligned child index out of range (0, ");
        stringBuilder.append(this.getChildCount());
        stringBuilder.append(")");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public void setDividerDrawable(Drawable drawable2) {
        if (drawable2 == this.mDivider) {
            return;
        }
        this.mDivider = drawable2;
        boolean bl = false;
        if (drawable2 != null) {
            this.mDividerWidth = drawable2.getIntrinsicWidth();
            this.mDividerHeight = drawable2.getIntrinsicHeight();
        } else {
            this.mDividerWidth = 0;
            this.mDividerHeight = 0;
        }
        if (drawable2 == null) {
            bl = true;
        }
        this.setWillNotDraw(bl);
        this.requestLayout();
    }

    public void setDividerPadding(int n) {
        this.mDividerPadding = n;
    }

    public void setGravity(int n) {
        if (this.mGravity != n) {
            int n2 = n;
            if ((0x800007 & n) == 0) {
                n2 = n | 0x800003;
            }
            n = n2;
            if ((n2 & 0x70) == 0) {
                n = n2 | 0x30;
            }
            this.mGravity = n;
            this.requestLayout();
        }
    }

    public void setHorizontalGravity(int n) {
        n = this.mGravity;
        int n2 = n & 0x800007;
        if ((0x800007 & n) != n2) {
            this.mGravity = 0xFF7FFFF8 & n | n2;
            this.requestLayout();
        }
    }

    public void setMeasureWithLargestChildEnabled(boolean bl) {
        this.mUseLargestChild = bl;
    }

    public void setOrientation(int n) {
        if (this.mOrientation != n) {
            this.mOrientation = n;
            this.requestLayout();
        }
    }

    public void setShowDividers(int n) {
        if (n != this.mShowDividers) {
            this.requestLayout();
        }
        this.mShowDividers = n;
    }

    public void setVerticalGravity(int n) {
        int n2 = this.mGravity;
        if ((n2 & 0x70) != (n &= 0x70)) {
            this.mGravity = n2 & 0xFFFFFF8F | n;
            this.requestLayout();
        }
    }

    public void setWeightSum(float f) {
        this.mWeightSum = Math.max(0.0f, f);
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface DividerMode {
    }

    public static class LayoutParams
    extends ViewGroup.MarginLayoutParams {
        public int gravity = -1;
        public float weight;

        public LayoutParams(int n, int n2) {
            super(n, n2);
            this.weight = 0.0f;
        }

        public LayoutParams(int n, int n2, float f) {
            super(n, n2);
            this.weight = f;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            context = context.obtainStyledAttributes(attributeSet, R.styleable.LinearLayoutCompat_Layout);
            this.weight = context.getFloat(R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0f);
            this.gravity = context.getInt(R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
            context.recycle();
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.MarginLayoutParams)layoutParams);
            this.weight = layoutParams.weight;
            this.gravity = layoutParams.gravity;
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface OrientationMode {
    }
}

