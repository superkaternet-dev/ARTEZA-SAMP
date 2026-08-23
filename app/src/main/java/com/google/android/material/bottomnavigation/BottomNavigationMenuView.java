/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.TimeInterpolator
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.util.TypedValue
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 */
package com.google.android.material.bottomnavigation;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.R;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.util.Pools;
import androidx.core.view.ViewCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;
import com.google.android.material.R;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationPresenter;
import com.google.android.material.internal.TextScale;

public class BottomNavigationMenuView
extends ViewGroup
implements MenuView {
    private static final long ACTIVE_ANIMATION_DURATION_MS = 115L;
    private static final int[] CHECKED_STATE_SET = new int[]{0x10100A0};
    private static final int[] DISABLED_STATE_SET = new int[]{-16842910};
    private final int activeItemMaxWidth;
    private final int activeItemMinWidth;
    private BottomNavigationItemView[] buttons;
    private final int inactiveItemMaxWidth;
    private final int inactiveItemMinWidth;
    private Drawable itemBackground;
    private int itemBackgroundRes;
    private final int itemHeight;
    private boolean itemHorizontalTranslationEnabled;
    private int itemIconSize;
    private ColorStateList itemIconTint;
    private final Pools.Pool<BottomNavigationItemView> itemPool = new Pools.SynchronizedPool<BottomNavigationItemView>(5);
    private int itemTextAppearanceActive;
    private int itemTextAppearanceInactive;
    private final ColorStateList itemTextColorDefault;
    private ColorStateList itemTextColorFromUser;
    private int labelVisibilityMode;
    private MenuBuilder menu;
    private final View.OnClickListener onClickListener;
    private BottomNavigationPresenter presenter;
    private int selectedItemId = 0;
    private int selectedItemPosition = 0;
    private final TransitionSet set;
    private int[] tempChildWidths;

    public BottomNavigationMenuView(Context context) {
        this(context, null);
    }

    public BottomNavigationMenuView(Context object, AttributeSet attributeSet) {
        super((Context)object, attributeSet);
        object = this.getResources();
        this.inactiveItemMaxWidth = object.getDimensionPixelSize(R.dimen.design_bottom_navigation_item_max_width);
        this.inactiveItemMinWidth = object.getDimensionPixelSize(R.dimen.design_bottom_navigation_item_min_width);
        this.activeItemMaxWidth = object.getDimensionPixelSize(R.dimen.design_bottom_navigation_active_item_max_width);
        this.activeItemMinWidth = object.getDimensionPixelSize(R.dimen.design_bottom_navigation_active_item_min_width);
        this.itemHeight = object.getDimensionPixelSize(R.dimen.design_bottom_navigation_height);
        this.itemTextColorDefault = this.createDefaultColorStateList(16842808);
        object = new AutoTransition();
        this.set = object;
        ((TransitionSet)object).setOrdering(0);
        ((TransitionSet)object).setDuration(115L);
        ((TransitionSet)object).setInterpolator((TimeInterpolator)new FastOutSlowInInterpolator());
        ((TransitionSet)object).addTransition(new TextScale());
        this.onClickListener = new View.OnClickListener(this){
            final BottomNavigationMenuView this$0;
            {
                this.this$0 = bottomNavigationMenuView;
            }

            public void onClick(View object) {
                object = ((BottomNavigationItemView)object).getItemData();
                if (!this.this$0.menu.performItemAction((MenuItem)object, this.this$0.presenter, 0)) {
                    object.setChecked(true);
                }
            }
        };
        this.tempChildWidths = new int[5];
    }

    private BottomNavigationItemView getNewItem() {
        BottomNavigationItemView bottomNavigationItemView;
        BottomNavigationItemView bottomNavigationItemView2 = bottomNavigationItemView = this.itemPool.acquire();
        if (bottomNavigationItemView == null) {
            bottomNavigationItemView2 = new BottomNavigationItemView(this.getContext());
        }
        return bottomNavigationItemView2;
    }

    private boolean isShifting(int n, int n2) {
        boolean bl = true;
        if (!(n == -1 ? n2 > 3 : n == 0)) {
            bl = false;
        }
        return bl;
    }

    public void buildMenuView() {
        BottomNavigationItemView bottomNavigationItemView;
        int n;
        this.removeAllViews();
        Drawable drawable2 = this.buttons;
        if (drawable2 != null) {
            int n2 = ((BottomNavigationItemView[])drawable2).length;
            for (n = 0; n < n2; ++n) {
                bottomNavigationItemView = drawable2[n];
                if (bottomNavigationItemView == null) continue;
                this.itemPool.release(bottomNavigationItemView);
            }
        }
        if (this.menu.size() == 0) {
            this.selectedItemId = 0;
            this.selectedItemPosition = 0;
            this.buttons = null;
            return;
        }
        this.buttons = new BottomNavigationItemView[this.menu.size()];
        boolean bl = this.isShifting(this.labelVisibilityMode, this.menu.getVisibleItems().size());
        for (n = 0; n < this.menu.size(); ++n) {
            this.presenter.setUpdateSuspended(true);
            this.menu.getItem(n).setCheckable(true);
            this.presenter.setUpdateSuspended(false);
            this.buttons[n] = bottomNavigationItemView = this.getNewItem();
            bottomNavigationItemView.setIconTintList(this.itemIconTint);
            bottomNavigationItemView.setIconSize(this.itemIconSize);
            bottomNavigationItemView.setTextColor(this.itemTextColorDefault);
            bottomNavigationItemView.setTextAppearanceInactive(this.itemTextAppearanceInactive);
            bottomNavigationItemView.setTextAppearanceActive(this.itemTextAppearanceActive);
            bottomNavigationItemView.setTextColor(this.itemTextColorFromUser);
            drawable2 = this.itemBackground;
            if (drawable2 != null) {
                bottomNavigationItemView.setItemBackground(drawable2);
            } else {
                bottomNavigationItemView.setItemBackground(this.itemBackgroundRes);
            }
            bottomNavigationItemView.setShifting(bl);
            bottomNavigationItemView.setLabelVisibilityMode(this.labelVisibilityMode);
            bottomNavigationItemView.initialize((MenuItemImpl)this.menu.getItem(n), 0);
            bottomNavigationItemView.setItemPosition(n);
            bottomNavigationItemView.setOnClickListener(this.onClickListener);
            this.addView((View)bottomNavigationItemView);
        }
        this.selectedItemPosition = n = Math.min(this.menu.size() - 1, this.selectedItemPosition);
        this.menu.getItem(n).setChecked(true);
    }

    public ColorStateList createDefaultColorStateList(int n) {
        Object object = new TypedValue();
        if (!this.getContext().getTheme().resolveAttribute(n, object, true)) {
            return null;
        }
        ColorStateList colorStateList = AppCompatResources.getColorStateList(this.getContext(), object.resourceId);
        if (!this.getContext().getTheme().resolveAttribute(R.attr.colorPrimary, object, true)) {
            return null;
        }
        int n2 = object.data;
        int n3 = colorStateList.getDefaultColor();
        int[] nArray = DISABLED_STATE_SET;
        object = CHECKED_STATE_SET;
        int[] nArray2 = EMPTY_STATE_SET;
        n = colorStateList.getColorForState(nArray, n3);
        return new ColorStateList((int[][])new int[][]{nArray, (int[])object, nArray2}, new int[]{n, n2, n3});
    }

    public ColorStateList getIconTintList() {
        return this.itemIconTint;
    }

    public Drawable getItemBackground() {
        BottomNavigationItemView[] bottomNavigationItemViewArray = this.buttons;
        if (bottomNavigationItemViewArray != null && bottomNavigationItemViewArray.length > 0) {
            return bottomNavigationItemViewArray[0].getBackground();
        }
        return this.itemBackground;
    }

    @Deprecated
    public int getItemBackgroundRes() {
        return this.itemBackgroundRes;
    }

    public int getItemIconSize() {
        return this.itemIconSize;
    }

    public int getItemTextAppearanceActive() {
        return this.itemTextAppearanceActive;
    }

    public int getItemTextAppearanceInactive() {
        return this.itemTextAppearanceInactive;
    }

    public ColorStateList getItemTextColor() {
        return this.itemTextColorFromUser;
    }

    public int getLabelVisibilityMode() {
        return this.labelVisibilityMode;
    }

    public int getSelectedItemId() {
        return this.selectedItemId;
    }

    @Override
    public int getWindowAnimations() {
        return 0;
    }

    @Override
    public void initialize(MenuBuilder menuBuilder) {
        this.menu = menuBuilder;
    }

    public boolean isItemHorizontalTranslationEnabled() {
        return this.itemHorizontalTranslationEnabled;
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        int n5 = this.getChildCount();
        n3 -= n;
        n4 -= n2;
        n2 = 0;
        for (n = 0; n < n5; ++n) {
            View view = this.getChildAt(n);
            if (view.getVisibility() == 8) continue;
            if (ViewCompat.getLayoutDirection((View)this) == 1) {
                view.layout(n3 - n2 - view.getMeasuredWidth(), 0, n3 - n2, n4);
            } else {
                view.layout(n2, 0, view.getMeasuredWidth() + n2, n4);
            }
            n2 += view.getMeasuredWidth();
        }
    }

    protected void onMeasure(int n, int n2) {
        int n3;
        Object object;
        int n4 = View.MeasureSpec.getSize((int)n);
        int n5 = this.menu.getVisibleItems().size();
        int n6 = this.getChildCount();
        int n7 = View.MeasureSpec.makeMeasureSpec((int)this.itemHeight, (int)0x40000000);
        if (this.isShifting(this.labelVisibilityMode, n5) && this.itemHorizontalTranslationEnabled) {
            object = this.getChildAt(this.selectedItemPosition);
            n = n2 = this.activeItemMinWidth;
            if (object.getVisibility() != 8) {
                object.measure(View.MeasureSpec.makeMeasureSpec((int)this.activeItemMaxWidth, (int)Integer.MIN_VALUE), n7);
                n = Math.max(n2, object.getMeasuredWidth());
            }
            n2 = object.getVisibility() != 8 ? 1 : 0;
            n2 = n5 - n2;
            n3 = Math.min(n4 - this.inactiveItemMinWidth * n2, Math.min(n, this.activeItemMaxWidth));
            n = n2 == 0 ? 1 : n2;
            int n8 = Math.min((n4 - n3) / n, this.inactiveItemMaxWidth);
            n = n4 - n3 - n8 * n2;
            for (n2 = 0; n2 < n6; ++n2) {
                if (this.getChildAt(n2).getVisibility() != 8) {
                    object = this.tempChildWidths;
                    n5 = n2 == this.selectedItemPosition ? n3 : n8;
                    object[n2] = (View)n5;
                    n5 = n;
                    if (n > 0) {
                        object[n2] = object[n2] + true;
                        n5 = n - 1;
                    }
                } else {
                    this.tempChildWidths[n2] = 0;
                    n5 = n;
                }
                n = n5;
            }
        } else {
            n = n5 == 0 ? 1 : n5;
            n3 = Math.min(n4 / n, this.activeItemMaxWidth);
            n2 = n4 - n3 * n5;
            for (n = 0; n < n6; ++n) {
                if (this.getChildAt(n).getVisibility() != 8) {
                    object = this.tempChildWidths;
                    object[n] = (View)n3;
                    n5 = n2;
                    if (n2 > 0) {
                        object[n] = object[n] + true;
                        n5 = n2 - 1;
                    }
                } else {
                    this.tempChildWidths[n] = 0;
                    n5 = n2;
                }
                n2 = n5;
            }
        }
        n2 = 0;
        for (n = 0; n < n6; ++n) {
            object = this.getChildAt(n);
            if (object.getVisibility() == 8) continue;
            object.measure(View.MeasureSpec.makeMeasureSpec((int)this.tempChildWidths[n], (int)0x40000000), n7);
            object.getLayoutParams().width = object.getMeasuredWidth();
            n2 += object.getMeasuredWidth();
        }
        this.setMeasuredDimension(View.resolveSizeAndState((int)n2, (int)View.MeasureSpec.makeMeasureSpec((int)n2, (int)0x40000000), (int)0), View.resolveSizeAndState((int)this.itemHeight, (int)n7, (int)0));
    }

    public void setIconTintList(ColorStateList colorStateList) {
        this.itemIconTint = colorStateList;
        BottomNavigationItemView[] bottomNavigationItemViewArray = this.buttons;
        if (bottomNavigationItemViewArray != null) {
            int n = bottomNavigationItemViewArray.length;
            for (int i = 0; i < n; ++i) {
                bottomNavigationItemViewArray[i].setIconTintList(colorStateList);
            }
        }
    }

    public void setItemBackground(Drawable drawable2) {
        this.itemBackground = drawable2;
        BottomNavigationItemView[] bottomNavigationItemViewArray = this.buttons;
        if (bottomNavigationItemViewArray != null) {
            int n = bottomNavigationItemViewArray.length;
            for (int i = 0; i < n; ++i) {
                bottomNavigationItemViewArray[i].setItemBackground(drawable2);
            }
        }
    }

    public void setItemBackgroundRes(int n) {
        this.itemBackgroundRes = n;
        BottomNavigationItemView[] bottomNavigationItemViewArray = this.buttons;
        if (bottomNavigationItemViewArray != null) {
            int n2 = bottomNavigationItemViewArray.length;
            for (int i = 0; i < n2; ++i) {
                bottomNavigationItemViewArray[i].setItemBackground(n);
            }
        }
    }

    public void setItemHorizontalTranslationEnabled(boolean bl) {
        this.itemHorizontalTranslationEnabled = bl;
    }

    public void setItemIconSize(int n) {
        this.itemIconSize = n;
        BottomNavigationItemView[] bottomNavigationItemViewArray = this.buttons;
        if (bottomNavigationItemViewArray != null) {
            int n2 = bottomNavigationItemViewArray.length;
            for (int i = 0; i < n2; ++i) {
                bottomNavigationItemViewArray[i].setIconSize(n);
            }
        }
    }

    public void setItemTextAppearanceActive(int n) {
        this.itemTextAppearanceActive = n;
        BottomNavigationItemView[] bottomNavigationItemViewArray = this.buttons;
        if (bottomNavigationItemViewArray != null) {
            for (BottomNavigationItemView bottomNavigationItemView : bottomNavigationItemViewArray) {
                bottomNavigationItemView.setTextAppearanceActive(n);
                ColorStateList colorStateList = this.itemTextColorFromUser;
                if (colorStateList == null) continue;
                bottomNavigationItemView.setTextColor(colorStateList);
            }
        }
    }

    public void setItemTextAppearanceInactive(int n) {
        this.itemTextAppearanceInactive = n;
        BottomNavigationItemView[] bottomNavigationItemViewArray = this.buttons;
        if (bottomNavigationItemViewArray != null) {
            for (BottomNavigationItemView bottomNavigationItemView : bottomNavigationItemViewArray) {
                bottomNavigationItemView.setTextAppearanceInactive(n);
                ColorStateList colorStateList = this.itemTextColorFromUser;
                if (colorStateList == null) continue;
                bottomNavigationItemView.setTextColor(colorStateList);
            }
        }
    }

    public void setItemTextColor(ColorStateList colorStateList) {
        this.itemTextColorFromUser = colorStateList;
        BottomNavigationItemView[] bottomNavigationItemViewArray = this.buttons;
        if (bottomNavigationItemViewArray != null) {
            int n = bottomNavigationItemViewArray.length;
            for (int i = 0; i < n; ++i) {
                bottomNavigationItemViewArray[i].setTextColor(colorStateList);
            }
        }
    }

    public void setLabelVisibilityMode(int n) {
        this.labelVisibilityMode = n;
    }

    public void setPresenter(BottomNavigationPresenter bottomNavigationPresenter) {
        this.presenter = bottomNavigationPresenter;
    }

    void tryRestoreSelectedItemId(int n) {
        int n2 = this.menu.size();
        for (int i = 0; i < n2; ++i) {
            MenuItem menuItem = this.menu.getItem(i);
            if (n != menuItem.getItemId()) continue;
            this.selectedItemId = n;
            this.selectedItemPosition = i;
            menuItem.setChecked(true);
            break;
        }
    }

    public void updateMenuView() {
        MenuBuilder menuBuilder = this.menu;
        if (menuBuilder != null && this.buttons != null) {
            int n;
            int n2 = menuBuilder.size();
            if (n2 != this.buttons.length) {
                this.buildMenuView();
                return;
            }
            int n3 = this.selectedItemId;
            for (n = 0; n < n2; ++n) {
                menuBuilder = this.menu.getItem(n);
                if (!menuBuilder.isChecked()) continue;
                this.selectedItemId = menuBuilder.getItemId();
                this.selectedItemPosition = n;
            }
            if (n3 != this.selectedItemId) {
                TransitionManager.beginDelayedTransition(this, this.set);
            }
            boolean bl = this.isShifting(this.labelVisibilityMode, this.menu.getVisibleItems().size());
            for (n = 0; n < n2; ++n) {
                this.presenter.setUpdateSuspended(true);
                this.buttons[n].setLabelVisibilityMode(this.labelVisibilityMode);
                this.buttons[n].setShifting(bl);
                this.buttons[n].initialize((MenuItemImpl)this.menu.getItem(n), 0);
                this.presenter.setUpdateSuspended(false);
            }
            return;
        }
    }
}

