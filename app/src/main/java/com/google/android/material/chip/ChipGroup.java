/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.ViewGroup$OnHierarchyChangeListener
 *  android.widget.CompoundButton
 *  android.widget.CompoundButton$OnCheckedChangeListener
 */
package com.google.android.material.chip;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import com.google.android.material.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.internal.FlowLayout;
import com.google.android.material.internal.ThemeEnforcement;

public class ChipGroup
extends FlowLayout {
    private int checkedId = -1;
    private final CheckedStateTracker checkedStateTracker = new CheckedStateTracker(this);
    private int chipSpacingHorizontal;
    private int chipSpacingVertical;
    private OnCheckedChangeListener onCheckedChangeListener;
    private PassThroughHierarchyChangeListener passThroughListener = new PassThroughHierarchyChangeListener(this);
    private boolean protectFromCheckedChange = false;
    private boolean singleSelection;

    public ChipGroup(Context context) {
        this(context, null);
    }

    public ChipGroup(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.chipGroupStyle);
    }

    public ChipGroup(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        context = ThemeEnforcement.obtainStyledAttributes(context, attributeSet, R.styleable.ChipGroup, n, R.style.Widget_MaterialComponents_ChipGroup, new int[0]);
        n = context.getDimensionPixelOffset(R.styleable.ChipGroup_chipSpacing, 0);
        this.setChipSpacingHorizontal(context.getDimensionPixelOffset(R.styleable.ChipGroup_chipSpacingHorizontal, n));
        this.setChipSpacingVertical(context.getDimensionPixelOffset(R.styleable.ChipGroup_chipSpacingVertical, n));
        this.setSingleLine(context.getBoolean(R.styleable.ChipGroup_singleLine, false));
        this.setSingleSelection(context.getBoolean(R.styleable.ChipGroup_singleSelection, false));
        n = context.getResourceId(R.styleable.ChipGroup_checkedChip, -1);
        if (n != -1) {
            this.checkedId = n;
        }
        context.recycle();
        super.setOnHierarchyChangeListener((ViewGroup.OnHierarchyChangeListener)this.passThroughListener);
    }

    private void setCheckedId(int n) {
        this.checkedId = n;
        OnCheckedChangeListener onCheckedChangeListener = this.onCheckedChangeListener;
        if (onCheckedChangeListener != null && this.singleSelection) {
            onCheckedChangeListener.onCheckedChanged(this, n);
        }
    }

    private void setCheckedStateForView(int n, boolean bl) {
        View view = this.findViewById(n);
        if (view instanceof Chip) {
            this.protectFromCheckedChange = true;
            ((Chip)view).setChecked(bl);
            this.protectFromCheckedChange = false;
        }
    }

    public void addView(View view, int n, ViewGroup.LayoutParams layoutParams) {
        Chip chip;
        if (view instanceof Chip && (chip = (Chip)view).isChecked()) {
            int n2 = this.checkedId;
            if (n2 != -1 && this.singleSelection) {
                this.setCheckedStateForView(n2, false);
            }
            this.setCheckedId(chip.getId());
        }
        super.addView(view, n, layoutParams);
    }

    public void check(int n) {
        int n2 = this.checkedId;
        if (n == n2) {
            return;
        }
        if (n2 != -1 && this.singleSelection) {
            this.setCheckedStateForView(n2, false);
        }
        if (n != -1) {
            this.setCheckedStateForView(n, true);
        }
        this.setCheckedId(n);
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        boolean bl = super.checkLayoutParams(layoutParams) && layoutParams instanceof LayoutParams;
        return bl;
    }

    public void clearCheck() {
        this.protectFromCheckedChange = true;
        for (int i = 0; i < this.getChildCount(); ++i) {
            View view = this.getChildAt(i);
            if (!(view instanceof Chip)) continue;
            ((Chip)view).setChecked(false);
        }
        this.protectFromCheckedChange = false;
        this.setCheckedId(-1);
    }

    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(this.getContext(), attributeSet);
    }

    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    public int getCheckedChipId() {
        int n = this.singleSelection ? this.checkedId : -1;
        return n;
    }

    public int getChipSpacingHorizontal() {
        return this.chipSpacingHorizontal;
    }

    public int getChipSpacingVertical() {
        return this.chipSpacingVertical;
    }

    public boolean isSingleSelection() {
        return this.singleSelection;
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        int n = this.checkedId;
        if (n != -1) {
            this.setCheckedStateForView(n, true);
            this.setCheckedId(this.checkedId);
        }
    }

    public void setChipSpacing(int n) {
        this.setChipSpacingHorizontal(n);
        this.setChipSpacingVertical(n);
    }

    public void setChipSpacingHorizontal(int n) {
        if (this.chipSpacingHorizontal != n) {
            this.chipSpacingHorizontal = n;
            this.setItemSpacing(n);
            this.requestLayout();
        }
    }

    public void setChipSpacingHorizontalResource(int n) {
        this.setChipSpacingHorizontal(this.getResources().getDimensionPixelOffset(n));
    }

    public void setChipSpacingResource(int n) {
        this.setChipSpacing(this.getResources().getDimensionPixelOffset(n));
    }

    public void setChipSpacingVertical(int n) {
        if (this.chipSpacingVertical != n) {
            this.chipSpacingVertical = n;
            this.setLineSpacing(n);
            this.requestLayout();
        }
    }

    public void setChipSpacingVerticalResource(int n) {
        this.setChipSpacingVertical(this.getResources().getDimensionPixelOffset(n));
    }

    @Deprecated
    public void setDividerDrawableHorizontal(Drawable drawable2) {
        throw new UnsupportedOperationException("Changing divider drawables have no effect. ChipGroup do not use divider drawables as spacing.");
    }

    @Deprecated
    public void setDividerDrawableVertical(Drawable drawable2) {
        throw new UnsupportedOperationException("Changing divider drawables have no effect. ChipGroup do not use divider drawables as spacing.");
    }

    @Deprecated
    public void setFlexWrap(int n) {
        throw new UnsupportedOperationException("Changing flex wrap not allowed. ChipGroup exposes a singleLine attribute instead.");
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public void setOnHierarchyChangeListener(ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener) {
        PassThroughHierarchyChangeListener.access$202(this.passThroughListener, onHierarchyChangeListener);
    }

    @Deprecated
    public void setShowDividerHorizontal(int n) {
        throw new UnsupportedOperationException("Changing divider modes has no effect. ChipGroup do not use divider drawables as spacing.");
    }

    @Deprecated
    public void setShowDividerVertical(int n) {
        throw new UnsupportedOperationException("Changing divider modes has no effect. ChipGroup do not use divider drawables as spacing.");
    }

    public void setSingleLine(int n) {
        this.setSingleLine(this.getResources().getBoolean(n));
    }

    public void setSingleSelection(int n) {
        this.setSingleSelection(this.getResources().getBoolean(n));
    }

    public void setSingleSelection(boolean bl) {
        if (this.singleSelection != bl) {
            this.singleSelection = bl;
            this.clearCheck();
        }
    }

    private class CheckedStateTracker
    implements CompoundButton.OnCheckedChangeListener {
        final ChipGroup this$0;

        private CheckedStateTracker(ChipGroup chipGroup) {
            this.this$0 = chipGroup;
        }

        public void onCheckedChanged(CompoundButton object, boolean bl) {
            if (this.this$0.protectFromCheckedChange) {
                return;
            }
            int n = object.getId();
            if (bl) {
                if (this.this$0.checkedId != -1 && this.this$0.checkedId != n && this.this$0.singleSelection) {
                    object = this.this$0;
                    ((ChipGroup)((Object)object)).setCheckedStateForView(((ChipGroup)((Object)object)).checkedId, false);
                }
                this.this$0.setCheckedId(n);
            } else if (this.this$0.checkedId == n) {
                this.this$0.setCheckedId(-1);
            }
        }
    }

    public static class LayoutParams
    extends ViewGroup.MarginLayoutParams {
        public LayoutParams(int n, int n2) {
            super(n, n2);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }
    }

    public static interface OnCheckedChangeListener {
        public void onCheckedChanged(ChipGroup var1, int var2);
    }

    private class PassThroughHierarchyChangeListener
    implements ViewGroup.OnHierarchyChangeListener {
        private ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener;
        final ChipGroup this$0;

        private PassThroughHierarchyChangeListener(ChipGroup chipGroup) {
            this.this$0 = chipGroup;
        }

        static /* synthetic */ ViewGroup.OnHierarchyChangeListener access$202(PassThroughHierarchyChangeListener passThroughHierarchyChangeListener, ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener) {
            passThroughHierarchyChangeListener.onHierarchyChangeListener = onHierarchyChangeListener;
            return onHierarchyChangeListener;
        }

        public void onChildViewAdded(View view, View view2) {
            ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener;
            if (view == this.this$0 && view2 instanceof Chip) {
                if (view2.getId() == -1) {
                    int n = Build.VERSION.SDK_INT >= 17 ? View.generateViewId() : view2.hashCode();
                    view2.setId(n);
                }
                ((Chip)view2).setOnCheckedChangeListenerInternal(this.this$0.checkedStateTracker);
            }
            if ((onHierarchyChangeListener = this.onHierarchyChangeListener) != null) {
                onHierarchyChangeListener.onChildViewAdded(view, view2);
            }
        }

        public void onChildViewRemoved(View view, View view2) {
            ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener;
            if (view == this.this$0 && view2 instanceof Chip) {
                ((Chip)view2).setOnCheckedChangeListenerInternal(null);
            }
            if ((onHierarchyChangeListener = this.onHierarchyChangeListener) != null) {
                onHierarchyChangeListener.onChildViewRemoved(view, view2);
            }
        }
    }
}

