/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.util.SparseArray
 *  android.view.ContextThemeWrapper
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemClickListener
 *  android.widget.BaseAdapter
 *  android.widget.ListAdapter
 */
package androidx.appcompat.view.menu;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import androidx.appcompat.R;
import androidx.appcompat.view.menu.ExpandedMenuView;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuDialogHelper;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.view.menu.SubMenuBuilder;
import java.util.ArrayList;

public class ListMenuPresenter
implements MenuPresenter,
AdapterView.OnItemClickListener {
    private static final String TAG = "ListMenuPresenter";
    public static final String VIEWS_TAG = "android:menu:list";
    MenuAdapter mAdapter;
    private MenuPresenter.Callback mCallback;
    Context mContext;
    private int mId;
    LayoutInflater mInflater;
    int mItemIndexOffset;
    int mItemLayoutRes;
    MenuBuilder mMenu;
    ExpandedMenuView mMenuView;
    int mThemeRes;

    public ListMenuPresenter(int n, int n2) {
        this.mItemLayoutRes = n;
        this.mThemeRes = n2;
    }

    public ListMenuPresenter(Context context, int n) {
        this(n, 0);
        this.mContext = context;
        this.mInflater = LayoutInflater.from((Context)context);
    }

    @Override
    public boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    @Override
    public boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    @Override
    public boolean flagActionItems() {
        return false;
    }

    public ListAdapter getAdapter() {
        if (this.mAdapter == null) {
            this.mAdapter = new MenuAdapter(this);
        }
        return this.mAdapter;
    }

    @Override
    public int getId() {
        return this.mId;
    }

    int getItemIndexOffset() {
        return this.mItemIndexOffset;
    }

    @Override
    public MenuView getMenuView(ViewGroup viewGroup) {
        if (this.mMenuView == null) {
            this.mMenuView = (ExpandedMenuView)this.mInflater.inflate(R.layout.abc_expanded_menu_layout, viewGroup, false);
            if (this.mAdapter == null) {
                this.mAdapter = new MenuAdapter(this);
            }
            this.mMenuView.setAdapter((ListAdapter)this.mAdapter);
            this.mMenuView.setOnItemClickListener(this);
        }
        return this.mMenuView;
    }

    @Override
    public void initForMenu(Context object, MenuBuilder menuBuilder) {
        if (this.mThemeRes != 0) {
            object = new ContextThemeWrapper((Context)object, this.mThemeRes);
            this.mContext = object;
            this.mInflater = LayoutInflater.from((Context)object);
        } else if (this.mContext != null) {
            this.mContext = object;
            if (this.mInflater == null) {
                this.mInflater = LayoutInflater.from((Context)object);
            }
        }
        this.mMenu = menuBuilder;
        object = this.mAdapter;
        if (object != null) {
            ((MenuAdapter)((Object)object)).notifyDataSetChanged();
        }
    }

    @Override
    public void onCloseMenu(MenuBuilder menuBuilder, boolean bl) {
        MenuPresenter.Callback callback = this.mCallback;
        if (callback != null) {
            callback.onCloseMenu(menuBuilder, bl);
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int n, long l) {
        this.mMenu.performItemAction(this.mAdapter.getItem(n), this, 0);
    }

    @Override
    public void onRestoreInstanceState(Parcelable parcelable) {
        this.restoreHierarchyState((Bundle)parcelable);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        if (this.mMenuView == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        this.saveHierarchyState(bundle);
        return bundle;
    }

    @Override
    public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        if (!subMenuBuilder.hasVisibleItems()) {
            return false;
        }
        new MenuDialogHelper(subMenuBuilder).show(null);
        MenuPresenter.Callback callback = this.mCallback;
        if (callback != null) {
            callback.onOpenSubMenu(subMenuBuilder);
        }
        return true;
    }

    public void restoreHierarchyState(Bundle bundle) {
        if ((bundle = bundle.getSparseParcelableArray(VIEWS_TAG)) != null) {
            this.mMenuView.restoreHierarchyState((SparseArray)bundle);
        }
    }

    public void saveHierarchyState(Bundle bundle) {
        SparseArray sparseArray = new SparseArray();
        ExpandedMenuView expandedMenuView = this.mMenuView;
        if (expandedMenuView != null) {
            expandedMenuView.saveHierarchyState(sparseArray);
        }
        bundle.putSparseParcelableArray(VIEWS_TAG, sparseArray);
    }

    @Override
    public void setCallback(MenuPresenter.Callback callback) {
        this.mCallback = callback;
    }

    public void setId(int n) {
        this.mId = n;
    }

    public void setItemIndexOffset(int n) {
        this.mItemIndexOffset = n;
        if (this.mMenuView != null) {
            this.updateMenuView(false);
        }
    }

    @Override
    public void updateMenuView(boolean bl) {
        MenuAdapter menuAdapter = this.mAdapter;
        if (menuAdapter != null) {
            menuAdapter.notifyDataSetChanged();
        }
    }

    private class MenuAdapter
    extends BaseAdapter {
        private int mExpandedIndex;
        final ListMenuPresenter this$0;

        public MenuAdapter(ListMenuPresenter listMenuPresenter) {
            this.this$0 = listMenuPresenter;
            this.mExpandedIndex = -1;
            this.findExpandedIndex();
        }

        void findExpandedIndex() {
            MenuItemImpl menuItemImpl = this.this$0.mMenu.getExpandedItem();
            if (menuItemImpl != null) {
                ArrayList<MenuItemImpl> arrayList = this.this$0.mMenu.getNonActionItems();
                int n = arrayList.size();
                for (int i = 0; i < n; ++i) {
                    if (arrayList.get(i) != menuItemImpl) continue;
                    this.mExpandedIndex = i;
                    return;
                }
            }
            this.mExpandedIndex = -1;
        }

        public int getCount() {
            int n = this.this$0.mMenu.getNonActionItems().size() - this.this$0.mItemIndexOffset;
            if (this.mExpandedIndex < 0) {
                return n;
            }
            return n - 1;
        }

        public MenuItemImpl getItem(int n) {
            ArrayList<MenuItemImpl> arrayList = this.this$0.mMenu.getNonActionItems();
            int n2 = n + this.this$0.mItemIndexOffset;
            int n3 = this.mExpandedIndex;
            n = n2;
            if (n3 >= 0) {
                n = n2;
                if (n2 >= n3) {
                    n = n2 + 1;
                }
            }
            return arrayList.get(n);
        }

        public long getItemId(int n) {
            return n;
        }

        public View getView(int n, View view, ViewGroup viewGroup) {
            View view2 = view;
            if (view == null) {
                view2 = this.this$0.mInflater.inflate(this.this$0.mItemLayoutRes, viewGroup, false);
            }
            ((MenuView.ItemView)view2).initialize(this.getItem(n), 0);
            return view2;
        }

        public void notifyDataSetChanged() {
            this.findExpandedIndex();
            super.notifyDataSetChanged();
        }
    }
}

