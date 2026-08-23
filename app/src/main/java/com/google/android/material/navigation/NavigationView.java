/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.graphics.drawable.Drawable
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.os.Parcelable$Creator
 *  android.util.AttributeSet
 *  android.util.TypedValue
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 */
package com.google.android.material.navigation;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.R;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.R;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.internal.NavigationMenuPresenter;
import com.google.android.material.internal.ScrimInsetsFrameLayout;
import com.google.android.material.internal.ThemeEnforcement;

public class NavigationView
extends ScrimInsetsFrameLayout {
    private static final int[] CHECKED_STATE_SET = new int[]{0x10100A0};
    private static final int[] DISABLED_STATE_SET = new int[]{-16842910};
    private static final int PRESENTER_NAVIGATION_VIEW_ID = 1;
    OnNavigationItemSelectedListener listener;
    private final int maxWidth;
    private final NavigationMenu menu;
    private MenuInflater menuInflater;
    private final NavigationMenuPresenter presenter;

    public NavigationView(Context context) {
        this(context, null);
    }

    public NavigationView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.navigationViewStyle);
    }

    public NavigationView(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        NavigationMenu navigationMenu;
        NavigationMenuPresenter navigationMenuPresenter;
        this.presenter = navigationMenuPresenter = new NavigationMenuPresenter();
        this.menu = navigationMenu = new NavigationMenu(context);
        TintTypedArray tintTypedArray = ThemeEnforcement.obtainTintedStyledAttributes(context, attributeSet, R.styleable.NavigationView, n, R.style.Widget_Design_NavigationView, new int[0]);
        ViewCompat.setBackground((View)this, tintTypedArray.getDrawable(R.styleable.NavigationView_android_background));
        if (tintTypedArray.hasValue(R.styleable.NavigationView_elevation)) {
            ViewCompat.setElevation((View)this, tintTypedArray.getDimensionPixelSize(R.styleable.NavigationView_elevation, 0));
        }
        ViewCompat.setFitsSystemWindows((View)this, tintTypedArray.getBoolean(R.styleable.NavigationView_android_fitsSystemWindows, false));
        this.maxWidth = tintTypedArray.getDimensionPixelSize(R.styleable.NavigationView_android_maxWidth, 0);
        ColorStateList colorStateList = tintTypedArray.hasValue(R.styleable.NavigationView_itemIconTint) ? tintTypedArray.getColorStateList(R.styleable.NavigationView_itemIconTint) : this.createDefaultColorStateList(16842808);
        n = 0;
        int n2 = 0;
        if (tintTypedArray.hasValue(R.styleable.NavigationView_itemTextAppearance)) {
            n2 = tintTypedArray.getResourceId(R.styleable.NavigationView_itemTextAppearance, 0);
            n = 1;
        }
        attributeSet = null;
        if (tintTypedArray.hasValue(R.styleable.NavigationView_itemTextColor)) {
            attributeSet = tintTypedArray.getColorStateList(R.styleable.NavigationView_itemTextColor);
        }
        AttributeSet attributeSet2 = attributeSet;
        if (n == 0) {
            attributeSet2 = attributeSet;
            if (attributeSet == null) {
                attributeSet2 = this.createDefaultColorStateList(16842806);
            }
        }
        attributeSet = tintTypedArray.getDrawable(R.styleable.NavigationView_itemBackground);
        if (tintTypedArray.hasValue(R.styleable.NavigationView_itemHorizontalPadding)) {
            navigationMenuPresenter.setItemHorizontalPadding(tintTypedArray.getDimensionPixelSize(R.styleable.NavigationView_itemHorizontalPadding, 0));
        }
        int n3 = tintTypedArray.getDimensionPixelSize(R.styleable.NavigationView_itemIconPadding, 0);
        navigationMenu.setCallback(new MenuBuilder.Callback(this){
            final NavigationView this$0;
            {
                this.this$0 = navigationView;
            }

            @Override
            public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
                boolean bl = this.this$0.listener != null && this.this$0.listener.onNavigationItemSelected(menuItem);
                return bl;
            }

            @Override
            public void onMenuModeChange(MenuBuilder menuBuilder) {
            }
        });
        navigationMenuPresenter.setId(1);
        navigationMenuPresenter.initForMenu(context, navigationMenu);
        navigationMenuPresenter.setItemIconTintList(colorStateList);
        if (n != 0) {
            navigationMenuPresenter.setItemTextAppearance(n2);
        }
        navigationMenuPresenter.setItemTextColor((ColorStateList)attributeSet2);
        navigationMenuPresenter.setItemBackground((Drawable)attributeSet);
        navigationMenuPresenter.setItemIconPadding(n3);
        navigationMenu.addMenuPresenter(navigationMenuPresenter);
        this.addView((View)navigationMenuPresenter.getMenuView((ViewGroup)this));
        if (tintTypedArray.hasValue(R.styleable.NavigationView_menu)) {
            this.inflateMenu(tintTypedArray.getResourceId(R.styleable.NavigationView_menu, 0));
        }
        if (tintTypedArray.hasValue(R.styleable.NavigationView_headerLayout)) {
            this.inflateHeaderView(tintTypedArray.getResourceId(R.styleable.NavigationView_headerLayout, 0));
        }
        tintTypedArray.recycle();
    }

    private ColorStateList createDefaultColorStateList(int n) {
        Object object = new TypedValue();
        if (!this.getContext().getTheme().resolveAttribute(n, object, true)) {
            return null;
        }
        ColorStateList colorStateList = AppCompatResources.getColorStateList(this.getContext(), object.resourceId);
        if (!this.getContext().getTheme().resolveAttribute(R.attr.colorPrimary, object, true)) {
            return null;
        }
        int n2 = object.data;
        n = colorStateList.getDefaultColor();
        int[] nArray = DISABLED_STATE_SET;
        int[] nArray2 = CHECKED_STATE_SET;
        object = EMPTY_STATE_SET;
        int n3 = colorStateList.getColorForState(nArray, n);
        return new ColorStateList((int[][])new int[][]{nArray, nArray2, (int[])object}, new int[]{n3, n2, n});
    }

    private MenuInflater getMenuInflater() {
        if (this.menuInflater == null) {
            this.menuInflater = new SupportMenuInflater(this.getContext());
        }
        return this.menuInflater;
    }

    public void addHeaderView(View view) {
        this.presenter.addHeaderView(view);
    }

    public MenuItem getCheckedItem() {
        return this.presenter.getCheckedItem();
    }

    public int getHeaderCount() {
        return this.presenter.getHeaderCount();
    }

    public View getHeaderView(int n) {
        return this.presenter.getHeaderView(n);
    }

    public Drawable getItemBackground() {
        return this.presenter.getItemBackground();
    }

    public int getItemHorizontalPadding() {
        return this.presenter.getItemHorizontalPadding();
    }

    public int getItemIconPadding() {
        return this.presenter.getItemIconPadding();
    }

    public ColorStateList getItemIconTintList() {
        return this.presenter.getItemTintList();
    }

    public ColorStateList getItemTextColor() {
        return this.presenter.getItemTextColor();
    }

    public Menu getMenu() {
        return this.menu;
    }

    public View inflateHeaderView(int n) {
        return this.presenter.inflateHeaderView(n);
    }

    public void inflateMenu(int n) {
        this.presenter.setUpdateSuspended(true);
        this.getMenuInflater().inflate(n, (Menu)this.menu);
        this.presenter.setUpdateSuspended(false);
        this.presenter.updateMenuView(false);
    }

    @Override
    protected void onInsetsChanged(WindowInsetsCompat windowInsetsCompat) {
        this.presenter.dispatchApplyWindowInsets(windowInsetsCompat);
    }

    protected void onMeasure(int n, int n2) {
        switch (View.MeasureSpec.getMode((int)n)) {
            default: {
                break;
            }
            case 0x40000000: {
                break;
            }
            case 0: {
                n = View.MeasureSpec.makeMeasureSpec((int)this.maxWidth, (int)0x40000000);
                break;
            }
            case -2147483648: {
                n = View.MeasureSpec.makeMeasureSpec((int)Math.min(View.MeasureSpec.getSize((int)n), this.maxWidth), (int)0x40000000);
            }
        }
        super.onMeasure(n, n2);
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        parcelable = (SavedState)parcelable;
        super.onRestoreInstanceState(parcelable.getSuperState());
        this.menu.restorePresenterStates(parcelable.menuState);
    }

    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.menuState = new Bundle();
        this.menu.savePresenterStates(savedState.menuState);
        return savedState;
    }

    public void removeHeaderView(View view) {
        this.presenter.removeHeaderView(view);
    }

    public void setCheckedItem(int n) {
        MenuItem menuItem = this.menu.findItem(n);
        if (menuItem != null) {
            this.presenter.setCheckedItem((MenuItemImpl)menuItem);
        }
    }

    public void setCheckedItem(MenuItem menuItem) {
        if ((menuItem = this.menu.findItem(menuItem.getItemId())) != null) {
            this.presenter.setCheckedItem((MenuItemImpl)menuItem);
            return;
        }
        throw new IllegalArgumentException("Called setCheckedItem(MenuItem) with an item that is not in the current menu.");
    }

    public void setItemBackground(Drawable drawable2) {
        this.presenter.setItemBackground(drawable2);
    }

    public void setItemBackgroundResource(int n) {
        this.setItemBackground(ContextCompat.getDrawable(this.getContext(), n));
    }

    public void setItemHorizontalPadding(int n) {
        this.presenter.setItemHorizontalPadding(n);
    }

    public void setItemHorizontalPaddingResource(int n) {
        this.presenter.setItemHorizontalPadding(this.getResources().getDimensionPixelSize(n));
    }

    public void setItemIconPadding(int n) {
        this.presenter.setItemIconPadding(n);
    }

    public void setItemIconPaddingResource(int n) {
        this.presenter.setItemIconPadding(this.getResources().getDimensionPixelSize(n));
    }

    public void setItemIconTintList(ColorStateList colorStateList) {
        this.presenter.setItemIconTintList(colorStateList);
    }

    public void setItemTextAppearance(int n) {
        this.presenter.setItemTextAppearance(n);
    }

    public void setItemTextColor(ColorStateList colorStateList) {
        this.presenter.setItemTextColor(colorStateList);
    }

    public void setNavigationItemSelectedListener(OnNavigationItemSelectedListener onNavigationItemSelectedListener) {
        this.listener = onNavigationItemSelectedListener;
    }

    public static interface OnNavigationItemSelectedListener {
        public boolean onNavigationItemSelected(MenuItem var1);
    }

    public static class SavedState
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
        public Bundle menuState;

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.menuState = parcel.readBundle(classLoader);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeBundle(this.menuState);
        }
    }
}

