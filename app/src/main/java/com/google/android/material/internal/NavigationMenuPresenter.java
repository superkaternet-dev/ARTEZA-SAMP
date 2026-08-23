/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.graphics.drawable.Drawable
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.util.SparseArray
 *  android.view.LayoutInflater
 *  android.view.MenuItem
 *  android.view.SubMenu
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.widget.LinearLayout
 *  android.widget.TextView
 */
package com.google.android.material.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.view.menu.SubMenuBuilder;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.R;
import com.google.android.material.internal.NavigationMenuItemView;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.internal.ParcelableSparseArray;
import java.util.ArrayList;

public class NavigationMenuPresenter
implements MenuPresenter {
    private static final String STATE_ADAPTER = "android:menu:adapter";
    private static final String STATE_HEADER = "android:menu:header";
    private static final String STATE_HIERARCHY = "android:menu:list";
    NavigationMenuAdapter adapter;
    private MenuPresenter.Callback callback;
    LinearLayout headerLayout;
    ColorStateList iconTintList;
    private int id;
    Drawable itemBackground;
    int itemHorizontalPadding;
    int itemIconPadding;
    LayoutInflater layoutInflater;
    MenuBuilder menu;
    private NavigationMenuView menuView;
    final View.OnClickListener onClickListener = new View.OnClickListener(this){
        final NavigationMenuPresenter this$0;
        {
            this.this$0 = navigationMenuPresenter;
        }

        public void onClick(View object) {
            object = (NavigationMenuItemView)object;
            this.this$0.setUpdateSuspended(true);
            object = ((NavigationMenuItemView)object).getItemData();
            boolean bl = this.this$0.menu.performItemAction((MenuItem)object, this.this$0, 0);
            if (object != null && ((MenuItemImpl)object).isCheckable() && bl) {
                this.this$0.adapter.setCheckedItem((MenuItemImpl)object);
            }
            this.this$0.setUpdateSuspended(false);
            this.this$0.updateMenuView(false);
        }
    };
    int paddingSeparator;
    private int paddingTopDefault;
    int textAppearance;
    boolean textAppearanceSet;
    ColorStateList textColor;

    public void addHeaderView(View object) {
        this.headerLayout.addView(object);
        object = this.menuView;
        object.setPadding(0, 0, 0, object.getPaddingBottom());
    }

    @Override
    public boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    public void dispatchApplyWindowInsets(WindowInsetsCompat windowInsetsCompat) {
        int n = windowInsetsCompat.getSystemWindowInsetTop();
        if (this.paddingTopDefault != n) {
            this.paddingTopDefault = n;
            if (this.headerLayout.getChildCount() == 0) {
                NavigationMenuView navigationMenuView = this.menuView;
                navigationMenuView.setPadding(0, this.paddingTopDefault, 0, navigationMenuView.getPaddingBottom());
            }
        }
        ViewCompat.dispatchApplyWindowInsets((View)this.headerLayout, windowInsetsCompat);
    }

    @Override
    public boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    @Override
    public boolean flagActionItems() {
        return false;
    }

    public MenuItemImpl getCheckedItem() {
        return this.adapter.getCheckedItem();
    }

    public int getHeaderCount() {
        return this.headerLayout.getChildCount();
    }

    public View getHeaderView(int n) {
        return this.headerLayout.getChildAt(n);
    }

    @Override
    public int getId() {
        return this.id;
    }

    public Drawable getItemBackground() {
        return this.itemBackground;
    }

    public int getItemHorizontalPadding() {
        return this.itemHorizontalPadding;
    }

    public int getItemIconPadding() {
        return this.itemIconPadding;
    }

    public ColorStateList getItemTextColor() {
        return this.textColor;
    }

    public ColorStateList getItemTintList() {
        return this.iconTintList;
    }

    @Override
    public MenuView getMenuView(ViewGroup viewGroup) {
        if (this.menuView == null) {
            this.menuView = (NavigationMenuView)this.layoutInflater.inflate(R.layout.design_navigation_menu, viewGroup, false);
            if (this.adapter == null) {
                this.adapter = new NavigationMenuAdapter(this);
            }
            this.headerLayout = (LinearLayout)this.layoutInflater.inflate(R.layout.design_navigation_item_header, (ViewGroup)this.menuView, false);
            this.menuView.setAdapter(this.adapter);
        }
        return this.menuView;
    }

    public View inflateHeaderView(int n) {
        View view = this.layoutInflater.inflate(n, (ViewGroup)this.headerLayout, false);
        this.addHeaderView(view);
        return view;
    }

    @Override
    public void initForMenu(Context context, MenuBuilder menuBuilder) {
        this.layoutInflater = LayoutInflater.from((Context)context);
        this.menu = menuBuilder;
        this.paddingSeparator = context.getResources().getDimensionPixelOffset(R.dimen.design_navigation_separator_vertical_padding);
    }

    @Override
    public void onCloseMenu(MenuBuilder menuBuilder, boolean bl) {
        MenuPresenter.Callback callback = this.callback;
        if (callback != null) {
            callback.onCloseMenu(menuBuilder, bl);
        }
    }

    @Override
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            SparseArray sparseArray = (parcelable = (Bundle)parcelable).getSparseParcelableArray(STATE_HIERARCHY);
            if (sparseArray != null) {
                this.menuView.restoreHierarchyState(sparseArray);
            }
            if ((sparseArray = parcelable.getBundle(STATE_ADAPTER)) != null) {
                this.adapter.restoreInstanceState((Bundle)sparseArray);
            }
            if ((parcelable = parcelable.getSparseParcelableArray(STATE_HEADER)) != null) {
                this.headerLayout.restoreHierarchyState((SparseArray)parcelable);
            }
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        NavigationMenuAdapter navigationMenuAdapter;
        Bundle bundle = new Bundle();
        if (this.menuView != null) {
            navigationMenuAdapter = new SparseArray();
            this.menuView.saveHierarchyState((SparseArray)navigationMenuAdapter);
            bundle.putSparseParcelableArray(STATE_HIERARCHY, (SparseArray)navigationMenuAdapter);
        }
        if ((navigationMenuAdapter = this.adapter) != null) {
            bundle.putBundle(STATE_ADAPTER, navigationMenuAdapter.createInstanceState());
        }
        if (this.headerLayout != null) {
            navigationMenuAdapter = new SparseArray();
            this.headerLayout.saveHierarchyState((SparseArray)navigationMenuAdapter);
            bundle.putSparseParcelableArray(STATE_HEADER, (SparseArray)navigationMenuAdapter);
        }
        return bundle;
    }

    @Override
    public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        return false;
    }

    public void removeHeaderView(View object) {
        this.headerLayout.removeView(object);
        if (this.headerLayout.getChildCount() == 0) {
            object = this.menuView;
            object.setPadding(0, this.paddingTopDefault, 0, object.getPaddingBottom());
        }
    }

    @Override
    public void setCallback(MenuPresenter.Callback callback) {
        this.callback = callback;
    }

    public void setCheckedItem(MenuItemImpl menuItemImpl) {
        this.adapter.setCheckedItem(menuItemImpl);
    }

    public void setId(int n) {
        this.id = n;
    }

    public void setItemBackground(Drawable drawable2) {
        this.itemBackground = drawable2;
        this.updateMenuView(false);
    }

    public void setItemHorizontalPadding(int n) {
        this.itemHorizontalPadding = n;
        this.updateMenuView(false);
    }

    public void setItemIconPadding(int n) {
        this.itemIconPadding = n;
        this.updateMenuView(false);
    }

    public void setItemIconTintList(ColorStateList colorStateList) {
        this.iconTintList = colorStateList;
        this.updateMenuView(false);
    }

    public void setItemTextAppearance(int n) {
        this.textAppearance = n;
        this.textAppearanceSet = true;
        this.updateMenuView(false);
    }

    public void setItemTextColor(ColorStateList colorStateList) {
        this.textColor = colorStateList;
        this.updateMenuView(false);
    }

    public void setUpdateSuspended(boolean bl) {
        NavigationMenuAdapter navigationMenuAdapter = this.adapter;
        if (navigationMenuAdapter != null) {
            navigationMenuAdapter.setUpdateSuspended(bl);
        }
    }

    @Override
    public void updateMenuView(boolean bl) {
        NavigationMenuAdapter navigationMenuAdapter = this.adapter;
        if (navigationMenuAdapter != null) {
            navigationMenuAdapter.update();
        }
    }

    private static class HeaderViewHolder
    extends ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    private class NavigationMenuAdapter
    extends RecyclerView.Adapter<ViewHolder> {
        private static final String STATE_ACTION_VIEWS = "android:menu:action_views";
        private static final String STATE_CHECKED_ITEM = "android:menu:checked";
        private static final int VIEW_TYPE_HEADER = 3;
        private static final int VIEW_TYPE_NORMAL = 0;
        private static final int VIEW_TYPE_SEPARATOR = 2;
        private static final int VIEW_TYPE_SUBHEADER = 1;
        private MenuItemImpl checkedItem;
        private final ArrayList<NavigationMenuItem> items;
        final NavigationMenuPresenter this$0;
        private boolean updateSuspended;

        NavigationMenuAdapter(NavigationMenuPresenter navigationMenuPresenter) {
            this.this$0 = navigationMenuPresenter;
            this.items = new ArrayList();
            this.prepareMenuItems();
        }

        private void appendTransparentIconIfMissing(int n, int n2) {
            while (n < n2) {
                ((NavigationMenuTextItem)this.items.get((int)n)).needsEmptyIcon = true;
                ++n;
            }
        }

        private void prepareMenuItems() {
            if (this.updateSuspended) {
                return;
            }
            this.updateSuspended = true;
            this.items.clear();
            this.items.add(new NavigationMenuHeaderItem());
            int n = -1;
            int n2 = 0;
            boolean bl = false;
            int n3 = 0;
            int n4 = this.this$0.menu.getVisibleItems().size();
            while (true) {
                int n5;
                int n6;
                boolean bl2 = false;
                if (n3 >= n4) break;
                Object object = this.this$0.menu.getVisibleItems().get(n3);
                if (((MenuItemImpl)object).isChecked()) {
                    this.setCheckedItem((MenuItemImpl)object);
                }
                if (((MenuItemImpl)object).isCheckable()) {
                    ((MenuItemImpl)object).setExclusiveCheckable(false);
                }
                if (((MenuItemImpl)object).hasSubMenu()) {
                    SubMenu subMenu = ((MenuItemImpl)object).getSubMenu();
                    if (subMenu.hasVisibleItems()) {
                        if (n3 != 0) {
                            this.items.add(new NavigationMenuSeparatorItem(this.this$0.paddingSeparator, 0));
                        }
                        this.items.add(new NavigationMenuTextItem((MenuItemImpl)object));
                        n6 = 0;
                        int n7 = this.items.size();
                        int n8 = subMenu.size();
                        for (int i = 0; i < n8; ++i) {
                            MenuItemImpl menuItemImpl = (MenuItemImpl)subMenu.getItem(i);
                            n5 = n6;
                            if (menuItemImpl.isVisible()) {
                                n5 = n6;
                                if (n6 == 0) {
                                    n5 = n6;
                                    if (menuItemImpl.getIcon() != null) {
                                        n5 = 1;
                                    }
                                }
                                if (menuItemImpl.isCheckable()) {
                                    menuItemImpl.setExclusiveCheckable(false);
                                }
                                if (((MenuItemImpl)object).isChecked()) {
                                    this.setCheckedItem((MenuItemImpl)object);
                                }
                                this.items.add(new NavigationMenuTextItem(menuItemImpl));
                            }
                            n6 = n5;
                        }
                        if (n6 != 0) {
                            this.appendTransparentIconIfMissing(n7, this.items.size());
                        }
                    }
                    n6 = n2;
                    bl2 = bl;
                } else {
                    n5 = ((MenuItemImpl)object).getGroupId();
                    if (n5 != n) {
                        n2 = this.items.size();
                        if (((MenuItemImpl)object).getIcon() != null) {
                            bl2 = true;
                        }
                        bl = bl2;
                        n6 = n2;
                        bl2 = bl;
                        if (n3 != 0) {
                            n6 = n2 + 1;
                            this.items.add(new NavigationMenuSeparatorItem(this.this$0.paddingSeparator, this.this$0.paddingSeparator));
                            bl2 = bl;
                        }
                    } else {
                        n6 = n2;
                        bl2 = bl;
                        if (!bl) {
                            n6 = n2;
                            bl2 = bl;
                            if (((MenuItemImpl)object).getIcon() != null) {
                                bl2 = true;
                                this.appendTransparentIconIfMissing(n2, this.items.size());
                                n6 = n2;
                            }
                        }
                    }
                    object = new NavigationMenuTextItem((MenuItemImpl)object);
                    ((NavigationMenuTextItem)object).needsEmptyIcon = bl2;
                    this.items.add((NavigationMenuItem)object);
                    n = n5;
                }
                ++n3;
                n2 = n6;
                bl = bl2;
            }
            this.updateSuspended = false;
        }

        public Bundle createInstanceState() {
            Bundle bundle = new Bundle();
            Object object = this.checkedItem;
            if (object != null) {
                bundle.putInt(STATE_CHECKED_ITEM, ((MenuItemImpl)object).getItemId());
            }
            SparseArray sparseArray = new SparseArray();
            int n = this.items.size();
            for (int i = 0; i < n; ++i) {
                MenuItemImpl menuItemImpl;
                object = this.items.get(i);
                if (!(object instanceof NavigationMenuTextItem) || (object = (menuItemImpl = ((NavigationMenuTextItem)object).getMenuItem()) != null ? menuItemImpl.getActionView() : null) == null) continue;
                ParcelableSparseArray parcelableSparseArray = new ParcelableSparseArray();
                object.saveHierarchyState((SparseArray)parcelableSparseArray);
                sparseArray.put(menuItemImpl.getItemId(), (Object)parcelableSparseArray);
            }
            bundle.putSparseParcelableArray(STATE_ACTION_VIEWS, sparseArray);
            return bundle;
        }

        public MenuItemImpl getCheckedItem() {
            return this.checkedItem;
        }

        @Override
        public int getItemCount() {
            return this.items.size();
        }

        @Override
        public long getItemId(int n) {
            return n;
        }

        @Override
        public int getItemViewType(int n) {
            NavigationMenuItem navigationMenuItem = this.items.get(n);
            if (navigationMenuItem instanceof NavigationMenuSeparatorItem) {
                return 2;
            }
            if (navigationMenuItem instanceof NavigationMenuHeaderItem) {
                return 3;
            }
            if (navigationMenuItem instanceof NavigationMenuTextItem) {
                if (((NavigationMenuTextItem)navigationMenuItem).getMenuItem().hasSubMenu()) {
                    return 1;
                }
                return 0;
            }
            throw new RuntimeException("Unknown item type.");
        }

        @Override
        public void onBindViewHolder(ViewHolder object, int n) {
            switch (this.getItemViewType(n)) {
                default: {
                    break;
                }
                case 2: {
                    NavigationMenuSeparatorItem navigationMenuSeparatorItem = (NavigationMenuSeparatorItem)this.items.get(n);
                    ((ViewHolder)object).itemView.setPadding(0, navigationMenuSeparatorItem.getPaddingTop(), 0, navigationMenuSeparatorItem.getPaddingBottom());
                    break;
                }
                case 1: {
                    ((TextView)((ViewHolder)object).itemView).setText(((NavigationMenuTextItem)this.items.get(n)).getMenuItem().getTitle());
                    break;
                }
                case 0: {
                    NavigationMenuItemView navigationMenuItemView = (NavigationMenuItemView)((ViewHolder)object).itemView;
                    navigationMenuItemView.setIconTintList(this.this$0.iconTintList);
                    if (this.this$0.textAppearanceSet) {
                        navigationMenuItemView.setTextAppearance(this.this$0.textAppearance);
                    }
                    if (this.this$0.textColor != null) {
                        navigationMenuItemView.setTextColor(this.this$0.textColor);
                    }
                    object = this.this$0.itemBackground != null ? this.this$0.itemBackground.getConstantState().newDrawable() : null;
                    ViewCompat.setBackground((View)navigationMenuItemView, (Drawable)object);
                    object = (NavigationMenuTextItem)this.items.get(n);
                    navigationMenuItemView.setNeedsEmptyIcon(((NavigationMenuTextItem)object).needsEmptyIcon);
                    navigationMenuItemView.setHorizontalPadding(this.this$0.itemHorizontalPadding);
                    navigationMenuItemView.setIconPadding(this.this$0.itemIconPadding);
                    navigationMenuItemView.initialize(((NavigationMenuTextItem)object).getMenuItem(), 0);
                }
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int n) {
            switch (n) {
                default: {
                    return null;
                }
                case 3: {
                    return new HeaderViewHolder((View)this.this$0.headerLayout);
                }
                case 2: {
                    return new SeparatorViewHolder(this.this$0.layoutInflater, viewGroup);
                }
                case 1: {
                    return new SubheaderViewHolder(this.this$0.layoutInflater, viewGroup);
                }
                case 0: 
            }
            return new NormalViewHolder(this.this$0.layoutInflater, viewGroup, this.this$0.onClickListener);
        }

        @Override
        public void onViewRecycled(ViewHolder viewHolder) {
            if (viewHolder instanceof NormalViewHolder) {
                ((NavigationMenuItemView)viewHolder.itemView).recycle();
            }
        }

        public void restoreInstanceState(Bundle bundle) {
            Object object;
            int n;
            int n2;
            int n3 = bundle.getInt(STATE_CHECKED_ITEM, 0);
            if (n3 != 0) {
                this.updateSuspended = true;
                n2 = this.items.size();
                for (n = 0; n < n2; ++n) {
                    object = this.items.get(n);
                    if (!(object instanceof NavigationMenuTextItem) || (object = ((NavigationMenuTextItem)object).getMenuItem()) == null || ((MenuItemImpl)object).getItemId() != n3) continue;
                    this.setCheckedItem((MenuItemImpl)object);
                    break;
                }
                this.updateSuspended = false;
                this.prepareMenuItems();
            }
            if ((bundle = bundle.getSparseParcelableArray(STATE_ACTION_VIEWS)) != null) {
                n2 = this.items.size();
                for (n = 0; n < n2; ++n) {
                    Object object2;
                    object = this.items.get(n);
                    if (!(object instanceof NavigationMenuTextItem) || (object2 = ((NavigationMenuTextItem)object).getMenuItem()) == null || (object = object2.getActionView()) == null || (object2 = (ParcelableSparseArray)((Object)bundle.get(object2.getItemId()))) == null) continue;
                    object.restoreHierarchyState((SparseArray)object2);
                }
            }
        }

        public void setCheckedItem(MenuItemImpl menuItemImpl) {
            if (this.checkedItem != menuItemImpl && menuItemImpl.isCheckable()) {
                MenuItemImpl menuItemImpl2 = this.checkedItem;
                if (menuItemImpl2 != null) {
                    menuItemImpl2.setChecked(false);
                }
                this.checkedItem = menuItemImpl;
                menuItemImpl.setChecked(true);
                return;
            }
        }

        public void setUpdateSuspended(boolean bl) {
            this.updateSuspended = bl;
        }

        public void update() {
            this.prepareMenuItems();
            this.notifyDataSetChanged();
        }
    }

    private static class NavigationMenuHeaderItem
    implements NavigationMenuItem {
        NavigationMenuHeaderItem() {
        }
    }

    private static interface NavigationMenuItem {
    }

    private static class NavigationMenuSeparatorItem
    implements NavigationMenuItem {
        private final int paddingBottom;
        private final int paddingTop;

        public NavigationMenuSeparatorItem(int n, int n2) {
            this.paddingTop = n;
            this.paddingBottom = n2;
        }

        public int getPaddingBottom() {
            return this.paddingBottom;
        }

        public int getPaddingTop() {
            return this.paddingTop;
        }
    }

    private static class NavigationMenuTextItem
    implements NavigationMenuItem {
        private final MenuItemImpl menuItem;
        boolean needsEmptyIcon;

        NavigationMenuTextItem(MenuItemImpl menuItemImpl) {
            this.menuItem = menuItemImpl;
        }

        public MenuItemImpl getMenuItem() {
            return this.menuItem;
        }
    }

    private static class NormalViewHolder
    extends ViewHolder {
        public NormalViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, View.OnClickListener onClickListener) {
            super(layoutInflater.inflate(R.layout.design_navigation_item, viewGroup, false));
            this.itemView.setOnClickListener(onClickListener);
        }
    }

    private static class SeparatorViewHolder
    extends ViewHolder {
        public SeparatorViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.design_navigation_item_separator, viewGroup, false));
        }
    }

    private static class SubheaderViewHolder
    extends ViewHolder {
        public SubheaderViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.design_navigation_item_subheader, viewGroup, false));
        }
    }

    private static abstract class ViewHolder
    extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }
}

