/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.os.Parcelable$Creator
 *  android.util.AttributeSet
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 */
package com.google.android.material.bottomnavigation;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.R;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationPresenter;
import com.google.android.material.internal.ThemeEnforcement;

public class BottomNavigationView
extends FrameLayout {
    private static final int MENU_PRESENTER_ID = 1;
    private final MenuBuilder menu;
    private MenuInflater menuInflater;
    private final BottomNavigationMenuView menuView;
    private final BottomNavigationPresenter presenter;
    private OnNavigationItemReselectedListener reselectedListener;
    private OnNavigationItemSelectedListener selectedListener;

    public BottomNavigationView(Context context) {
        this(context, null);
    }

    public BottomNavigationView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.bottomNavigationStyle);
    }

    public BottomNavigationView(Context context, AttributeSet object, int n) {
        super(context, (AttributeSet)object, n);
        BottomNavigationMenuView bottomNavigationMenuView;
        BottomNavigationPresenter bottomNavigationPresenter;
        this.presenter = bottomNavigationPresenter = new BottomNavigationPresenter();
        BottomNavigationMenu bottomNavigationMenu = new BottomNavigationMenu(context);
        this.menu = bottomNavigationMenu;
        this.menuView = bottomNavigationMenuView = new BottomNavigationMenuView(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 17;
        bottomNavigationMenuView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        bottomNavigationPresenter.setBottomNavigationMenuView(bottomNavigationMenuView);
        bottomNavigationPresenter.setId(1);
        bottomNavigationMenuView.setPresenter(bottomNavigationPresenter);
        bottomNavigationMenu.addMenuPresenter(bottomNavigationPresenter);
        bottomNavigationPresenter.initForMenu(this.getContext(), bottomNavigationMenu);
        object = ThemeEnforcement.obtainTintedStyledAttributes(context, (AttributeSet)object, R.styleable.BottomNavigationView, n, R.style.Widget_Design_BottomNavigationView, R.styleable.BottomNavigationView_itemTextAppearanceInactive, R.styleable.BottomNavigationView_itemTextAppearanceActive);
        if (((TintTypedArray)object).hasValue(R.styleable.BottomNavigationView_itemIconTint)) {
            bottomNavigationMenuView.setIconTintList(((TintTypedArray)object).getColorStateList(R.styleable.BottomNavigationView_itemIconTint));
        } else {
            bottomNavigationMenuView.setIconTintList(bottomNavigationMenuView.createDefaultColorStateList(16842808));
        }
        this.setItemIconSize(((TintTypedArray)object).getDimensionPixelSize(R.styleable.BottomNavigationView_itemIconSize, this.getResources().getDimensionPixelSize(R.dimen.design_bottom_navigation_icon_size)));
        if (((TintTypedArray)object).hasValue(R.styleable.BottomNavigationView_itemTextAppearanceInactive)) {
            this.setItemTextAppearanceInactive(((TintTypedArray)object).getResourceId(R.styleable.BottomNavigationView_itemTextAppearanceInactive, 0));
        }
        if (((TintTypedArray)object).hasValue(R.styleable.BottomNavigationView_itemTextAppearanceActive)) {
            this.setItemTextAppearanceActive(((TintTypedArray)object).getResourceId(R.styleable.BottomNavigationView_itemTextAppearanceActive, 0));
        }
        if (((TintTypedArray)object).hasValue(R.styleable.BottomNavigationView_itemTextColor)) {
            this.setItemTextColor(((TintTypedArray)object).getColorStateList(R.styleable.BottomNavigationView_itemTextColor));
        }
        if (((TintTypedArray)object).hasValue(R.styleable.BottomNavigationView_elevation)) {
            ViewCompat.setElevation((View)this, ((TintTypedArray)object).getDimensionPixelSize(R.styleable.BottomNavigationView_elevation, 0));
        }
        this.setLabelVisibilityMode(((TintTypedArray)object).getInteger(R.styleable.BottomNavigationView_labelVisibilityMode, -1));
        this.setItemHorizontalTranslationEnabled(((TintTypedArray)object).getBoolean(R.styleable.BottomNavigationView_itemHorizontalTranslationEnabled, true));
        bottomNavigationMenuView.setItemBackgroundRes(((TintTypedArray)object).getResourceId(R.styleable.BottomNavigationView_itemBackground, 0));
        if (((TintTypedArray)object).hasValue(R.styleable.BottomNavigationView_menu)) {
            this.inflateMenu(((TintTypedArray)object).getResourceId(R.styleable.BottomNavigationView_menu, 0));
        }
        ((TintTypedArray)object).recycle();
        this.addView((View)bottomNavigationMenuView, (ViewGroup.LayoutParams)layoutParams);
        if (Build.VERSION.SDK_INT < 21) {
            this.addCompatibilityTopDivider(context);
        }
        bottomNavigationMenu.setCallback(new MenuBuilder.Callback(this){
            final BottomNavigationView this$0;
            {
                this.this$0 = bottomNavigationView;
            }

            @Override
            public boolean onMenuItemSelected(MenuBuilder object, MenuItem menuItem) {
                object = this.this$0.reselectedListener;
                boolean bl = true;
                if (object != null && menuItem.getItemId() == this.this$0.getSelectedItemId()) {
                    this.this$0.reselectedListener.onNavigationItemReselected(menuItem);
                    return true;
                }
                if (this.this$0.selectedListener == null || this.this$0.selectedListener.onNavigationItemSelected(menuItem)) {
                    bl = false;
                }
                return bl;
            }

            @Override
            public void onMenuModeChange(MenuBuilder menuBuilder) {
            }
        });
    }

    private void addCompatibilityTopDivider(Context context) {
        View view = new View(context);
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.design_bottom_navigation_shadow_color));
        view.setLayoutParams((ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-1, this.getResources().getDimensionPixelSize(R.dimen.design_bottom_navigation_shadow_height)));
        this.addView(view);
    }

    private MenuInflater getMenuInflater() {
        if (this.menuInflater == null) {
            this.menuInflater = new SupportMenuInflater(this.getContext());
        }
        return this.menuInflater;
    }

    public Drawable getItemBackground() {
        return this.menuView.getItemBackground();
    }

    @Deprecated
    public int getItemBackgroundResource() {
        return this.menuView.getItemBackgroundRes();
    }

    public int getItemIconSize() {
        return this.menuView.getItemIconSize();
    }

    public ColorStateList getItemIconTintList() {
        return this.menuView.getIconTintList();
    }

    public int getItemTextAppearanceActive() {
        return this.menuView.getItemTextAppearanceActive();
    }

    public int getItemTextAppearanceInactive() {
        return this.menuView.getItemTextAppearanceInactive();
    }

    public ColorStateList getItemTextColor() {
        return this.menuView.getItemTextColor();
    }

    public int getLabelVisibilityMode() {
        return this.menuView.getLabelVisibilityMode();
    }

    public int getMaxItemCount() {
        return 5;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public int getSelectedItemId() {
        return this.menuView.getSelectedItemId();
    }

    public void inflateMenu(int n) {
        this.presenter.setUpdateSuspended(true);
        this.getMenuInflater().inflate(n, (Menu)this.menu);
        this.presenter.setUpdateSuspended(false);
        this.presenter.updateMenuView(true);
    }

    public boolean isItemHorizontalTranslationEnabled() {
        return this.menuView.isItemHorizontalTranslationEnabled();
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        parcelable = (SavedState)parcelable;
        super.onRestoreInstanceState(parcelable.getSuperState());
        this.menu.restorePresenterStates(parcelable.menuPresenterState);
    }

    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.menuPresenterState = new Bundle();
        this.menu.savePresenterStates(savedState.menuPresenterState);
        return savedState;
    }

    public void setItemBackground(Drawable drawable2) {
        this.menuView.setItemBackground(drawable2);
    }

    public void setItemBackgroundResource(int n) {
        this.menuView.setItemBackgroundRes(n);
    }

    public void setItemHorizontalTranslationEnabled(boolean bl) {
        if (this.menuView.isItemHorizontalTranslationEnabled() != bl) {
            this.menuView.setItemHorizontalTranslationEnabled(bl);
            this.presenter.updateMenuView(false);
        }
    }

    public void setItemIconSize(int n) {
        this.menuView.setItemIconSize(n);
    }

    public void setItemIconSizeRes(int n) {
        this.setItemIconSize(this.getResources().getDimensionPixelSize(n));
    }

    public void setItemIconTintList(ColorStateList colorStateList) {
        this.menuView.setIconTintList(colorStateList);
    }

    public void setItemTextAppearanceActive(int n) {
        this.menuView.setItemTextAppearanceActive(n);
    }

    public void setItemTextAppearanceInactive(int n) {
        this.menuView.setItemTextAppearanceInactive(n);
    }

    public void setItemTextColor(ColorStateList colorStateList) {
        this.menuView.setItemTextColor(colorStateList);
    }

    public void setLabelVisibilityMode(int n) {
        if (this.menuView.getLabelVisibilityMode() != n) {
            this.menuView.setLabelVisibilityMode(n);
            this.presenter.updateMenuView(false);
        }
    }

    public void setOnNavigationItemReselectedListener(OnNavigationItemReselectedListener onNavigationItemReselectedListener) {
        this.reselectedListener = onNavigationItemReselectedListener;
    }

    public void setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener onNavigationItemSelectedListener) {
        this.selectedListener = onNavigationItemSelectedListener;
    }

    public void setSelectedItemId(int n) {
        MenuItem menuItem = this.menu.findItem(n);
        if (menuItem != null && !this.menu.performItemAction(menuItem, this.presenter, 0)) {
            menuItem.setChecked(true);
        }
    }

    public static interface OnNavigationItemReselectedListener {
        public void onNavigationItemReselected(MenuItem var1);
    }

    public static interface OnNavigationItemSelectedListener {
        public boolean onNavigationItemSelected(MenuItem var1);
    }

    static class SavedState
    extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        Bundle menuPresenterState;

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.readFromParcel(parcel, classLoader);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private void readFromParcel(Parcel parcel, ClassLoader classLoader) {
            this.menuPresenterState = parcel.readBundle(classLoader);
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeBundle(this.menuPresenterState);
        }
    }
}

