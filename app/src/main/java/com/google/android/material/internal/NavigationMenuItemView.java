/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.graphics.drawable.ColorDrawable
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.StateListDrawable
 *  android.util.AttributeSet
 *  android.util.TypedValue
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewStub
 *  android.widget.CheckedTextView
 *  android.widget.FrameLayout
 *  android.widget.TextView
 */
package com.google.android.material.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.widget.TextViewCompat;
import com.google.android.material.R;
import com.google.android.material.internal.ForegroundLinearLayout;

public class NavigationMenuItemView
extends ForegroundLinearLayout
implements MenuView.ItemView {
    private static final int[] CHECKED_STATE_SET = new int[]{0x10100A0};
    private final AccessibilityDelegateCompat accessibilityDelegate;
    private FrameLayout actionArea;
    boolean checkable;
    private Drawable emptyDrawable;
    private boolean hasIconTintList;
    private final int iconSize;
    private ColorStateList iconTintList;
    private MenuItemImpl itemData;
    private boolean needsEmptyIcon;
    private final CheckedTextView textView;

    public NavigationMenuItemView(Context context) {
        this(context, null);
    }

    public NavigationMenuItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NavigationMenuItemView(Context context, AttributeSet object, int n) {
        super(context, (AttributeSet)object, n);
        object = new AccessibilityDelegateCompat(this){
            final NavigationMenuItemView this$0;
            {
                this.this$0 = navigationMenuItemView;
            }

            @Override
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                accessibilityNodeInfoCompat.setCheckable(this.this$0.checkable);
            }
        };
        this.accessibilityDelegate = object;
        this.setOrientation(0);
        LayoutInflater.from((Context)context).inflate(R.layout.design_navigation_menu_item, (ViewGroup)this, true);
        this.iconSize = context.getResources().getDimensionPixelSize(R.dimen.design_navigation_icon_size);
        context = (CheckedTextView)this.findViewById(R.id.design_menu_item_text);
        this.textView = context;
        context.setDuplicateParentStateEnabled(true);
        ViewCompat.setAccessibilityDelegate((View)context, (AccessibilityDelegateCompat)object);
    }

    private void adjustAppearance() {
        if (this.shouldExpandActionArea()) {
            this.textView.setVisibility(8);
            Object object = this.actionArea;
            if (object != null) {
                object = (LinearLayoutCompat.LayoutParams)object.getLayoutParams();
                object.width = -1;
                this.actionArea.setLayoutParams((ViewGroup.LayoutParams)object);
            }
        } else {
            this.textView.setVisibility(0);
            Object object = this.actionArea;
            if (object != null) {
                object = (LinearLayoutCompat.LayoutParams)object.getLayoutParams();
                object.width = -2;
                this.actionArea.setLayoutParams((ViewGroup.LayoutParams)object);
            }
        }
    }

    private StateListDrawable createDefaultBackground() {
        TypedValue typedValue = new TypedValue();
        if (this.getContext().getTheme().resolveAttribute(R.attr.colorControlHighlight, typedValue, true)) {
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(CHECKED_STATE_SET, (Drawable)new ColorDrawable(typedValue.data));
            stateListDrawable.addState(EMPTY_STATE_SET, (Drawable)new ColorDrawable(0));
            return stateListDrawable;
        }
        return null;
    }

    private void setActionView(View view) {
        if (view != null) {
            if (this.actionArea == null) {
                this.actionArea = (FrameLayout)((ViewStub)this.findViewById(R.id.design_menu_item_action_area_stub)).inflate();
            }
            this.actionArea.removeAllViews();
            this.actionArea.addView(view);
        }
    }

    private boolean shouldExpandActionArea() {
        boolean bl = this.itemData.getTitle() == null && this.itemData.getIcon() == null && this.itemData.getActionView() != null;
        return bl;
    }

    @Override
    public MenuItemImpl getItemData() {
        return this.itemData;
    }

    @Override
    public void initialize(MenuItemImpl menuItemImpl, int n) {
        this.itemData = menuItemImpl;
        n = menuItemImpl.isVisible() ? 0 : 8;
        this.setVisibility(n);
        if (this.getBackground() == null) {
            ViewCompat.setBackground((View)this, (Drawable)this.createDefaultBackground());
        }
        this.setCheckable(menuItemImpl.isCheckable());
        this.setChecked(menuItemImpl.isChecked());
        this.setEnabled(menuItemImpl.isEnabled());
        this.setTitle(menuItemImpl.getTitle());
        this.setIcon(menuItemImpl.getIcon());
        this.setActionView(menuItemImpl.getActionView());
        this.setContentDescription(menuItemImpl.getContentDescription());
        TooltipCompat.setTooltipText((View)this, menuItemImpl.getTooltipText());
        this.adjustAppearance();
    }

    protected int[] onCreateDrawableState(int n) {
        int[] nArray = super.onCreateDrawableState(n + 1);
        MenuItemImpl menuItemImpl = this.itemData;
        if (menuItemImpl != null && menuItemImpl.isCheckable() && this.itemData.isChecked()) {
            NavigationMenuItemView.mergeDrawableStates((int[])nArray, (int[])CHECKED_STATE_SET);
        }
        return nArray;
    }

    @Override
    public boolean prefersCondensedTitle() {
        return false;
    }

    public void recycle() {
        FrameLayout frameLayout = this.actionArea;
        if (frameLayout != null) {
            frameLayout.removeAllViews();
        }
        this.textView.setCompoundDrawables(null, null, null, null);
    }

    @Override
    public void setCheckable(boolean bl) {
        this.refreshDrawableState();
        if (this.checkable != bl) {
            this.checkable = bl;
            this.accessibilityDelegate.sendAccessibilityEvent((View)this.textView, 2048);
        }
    }

    @Override
    public void setChecked(boolean bl) {
        this.refreshDrawableState();
        this.textView.setChecked(bl);
    }

    public void setHorizontalPadding(int n) {
        this.setPadding(n, 0, n, 0);
    }

    @Override
    public void setIcon(Drawable drawable2) {
        if (drawable2 != null) {
            Drawable drawable3 = drawable2;
            if (this.hasIconTintList) {
                drawable3 = drawable2.getConstantState();
                if (drawable3 != null) {
                    drawable2 = drawable3.newDrawable();
                }
                drawable3 = DrawableCompat.wrap(drawable2).mutate();
                DrawableCompat.setTintList(drawable3, this.iconTintList);
            }
            int n = this.iconSize;
            drawable3.setBounds(0, 0, n, n);
            drawable2 = drawable3;
        } else if (this.needsEmptyIcon) {
            if (this.emptyDrawable == null) {
                this.emptyDrawable = drawable2 = ResourcesCompat.getDrawable(this.getResources(), R.drawable.navigation_empty_icon, this.getContext().getTheme());
                if (drawable2 != null) {
                    int n = this.iconSize;
                    drawable2.setBounds(0, 0, n, n);
                }
            }
            drawable2 = this.emptyDrawable;
        }
        TextViewCompat.setCompoundDrawablesRelative((TextView)this.textView, drawable2, null, null, null);
    }

    public void setIconPadding(int n) {
        this.textView.setCompoundDrawablePadding(n);
    }

    void setIconTintList(ColorStateList object) {
        this.iconTintList = object;
        boolean bl = object != null;
        this.hasIconTintList = bl;
        object = this.itemData;
        if (object != null) {
            this.setIcon(((MenuItemImpl)object).getIcon());
        }
    }

    public void setNeedsEmptyIcon(boolean bl) {
        this.needsEmptyIcon = bl;
    }

    @Override
    public void setShortcut(boolean bl, char c) {
    }

    public void setTextAppearance(int n) {
        TextViewCompat.setTextAppearance((TextView)this.textView, n);
    }

    public void setTextColor(ColorStateList colorStateList) {
        this.textView.setTextColor(colorStateList);
    }

    @Override
    public void setTitle(CharSequence charSequence) {
        this.textView.setText(charSequence);
    }

    @Override
    public boolean showsIcon() {
        return true;
    }
}

