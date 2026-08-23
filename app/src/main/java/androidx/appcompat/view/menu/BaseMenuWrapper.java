/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.MenuItem
 *  android.view.SubMenu
 */
package androidx.appcompat.view.menu;

import android.content.Context;
import android.view.MenuItem;
import android.view.SubMenu;
import androidx.appcompat.view.menu.MenuItemWrapperICS;
import androidx.appcompat.view.menu.SubMenuWrapperICS;
import androidx.collection.SimpleArrayMap;
import androidx.core.internal.view.SupportMenuItem;
import androidx.core.internal.view.SupportSubMenu;

abstract class BaseMenuWrapper {
    final Context mContext;
    private SimpleArrayMap<SupportMenuItem, MenuItem> mMenuItems;
    private SimpleArrayMap<SupportSubMenu, SubMenu> mSubMenus;

    BaseMenuWrapper(Context context) {
        this.mContext = context;
    }

    final MenuItem getMenuItemWrapper(MenuItem menuItem) {
        if (menuItem instanceof SupportMenuItem) {
            MenuItem menuItem2;
            SupportMenuItem supportMenuItem = (SupportMenuItem)menuItem;
            if (this.mMenuItems == null) {
                this.mMenuItems = new SimpleArrayMap();
            }
            menuItem = menuItem2 = this.mMenuItems.get(menuItem);
            if (menuItem2 == null) {
                menuItem = new MenuItemWrapperICS(this.mContext, supportMenuItem);
                this.mMenuItems.put(supportMenuItem, menuItem);
            }
            return menuItem;
        }
        return menuItem;
    }

    final SubMenu getSubMenuWrapper(SubMenu subMenu) {
        if (subMenu instanceof SupportSubMenu) {
            SubMenu subMenu2;
            SupportSubMenu supportSubMenu = (SupportSubMenu)subMenu;
            if (this.mSubMenus == null) {
                this.mSubMenus = new SimpleArrayMap();
            }
            subMenu = subMenu2 = this.mSubMenus.get(supportSubMenu);
            if (subMenu2 == null) {
                subMenu = new SubMenuWrapperICS(this.mContext, supportSubMenu);
                this.mSubMenus.put(supportSubMenu, subMenu);
            }
            return subMenu;
        }
        return subMenu;
    }

    final void internalClear() {
        SimpleArrayMap<Object, Object> simpleArrayMap = this.mMenuItems;
        if (simpleArrayMap != null) {
            simpleArrayMap.clear();
        }
        if ((simpleArrayMap = this.mSubMenus) != null) {
            simpleArrayMap.clear();
        }
    }

    final void internalRemoveGroup(int n) {
        if (this.mMenuItems == null) {
            return;
        }
        int n2 = 0;
        while (n2 < this.mMenuItems.size()) {
            int n3 = n2;
            if (this.mMenuItems.keyAt(n2).getGroupId() == n) {
                this.mMenuItems.removeAt(n2);
                n3 = n2 - 1;
            }
            n2 = n3 + 1;
        }
    }

    final void internalRemoveItem(int n) {
        if (this.mMenuItems == null) {
            return;
        }
        for (int i = 0; i < this.mMenuItems.size(); ++i) {
            if (this.mMenuItems.keyAt(i).getItemId() != n) continue;
            this.mMenuItems.removeAt(i);
            break;
        }
    }
}

