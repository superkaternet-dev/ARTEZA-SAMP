/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.PackageManager
 *  android.content.pm.ResolveInfo
 *  android.content.res.Resources
 *  android.graphics.drawable.Drawable
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.util.SparseArray
 *  android.view.ContextMenu$ContextMenuInfo
 *  android.view.KeyCharacterMap$KeyData
 *  android.view.KeyEvent
 *  android.view.MenuItem
 *  android.view.SubMenu
 *  android.view.View
 *  android.view.ViewConfiguration
 */
package androidx.appcompat.view.menu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewConfiguration;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.SubMenuBuilder;
import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ActionProvider;
import androidx.core.view.ViewConfigurationCompat;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MenuBuilder
implements SupportMenu {
    private static final String ACTION_VIEW_STATES_KEY = "android:menu:actionviewstates";
    private static final String EXPANDED_ACTION_VIEW_ID = "android:menu:expandedactionview";
    private static final String PRESENTER_KEY = "android:menu:presenters";
    private static final String TAG = "MenuBuilder";
    private static final int[] sCategoryToOrder = new int[]{1, 4, 5, 3, 2, 0};
    private ArrayList<MenuItemImpl> mActionItems;
    private Callback mCallback;
    private final Context mContext;
    private ContextMenu.ContextMenuInfo mCurrentMenuInfo;
    private int mDefaultShowAsAction = 0;
    private MenuItemImpl mExpandedItem;
    private boolean mGroupDividerEnabled = false;
    Drawable mHeaderIcon;
    CharSequence mHeaderTitle;
    View mHeaderView;
    private boolean mIsActionItemsStale;
    private boolean mIsClosing = false;
    private boolean mIsVisibleItemsStale;
    private ArrayList<MenuItemImpl> mItems;
    private boolean mItemsChangedWhileDispatchPrevented = false;
    private ArrayList<MenuItemImpl> mNonActionItems;
    private boolean mOptionalIconsVisible = false;
    private boolean mOverrideVisibleItems;
    private CopyOnWriteArrayList<WeakReference<MenuPresenter>> mPresenters;
    private boolean mPreventDispatchingItemsChanged = false;
    private boolean mQwertyMode;
    private final Resources mResources;
    private boolean mShortcutsVisible;
    private boolean mStructureChangedWhileDispatchPrevented = false;
    private ArrayList<MenuItemImpl> mTempShortcutItemList = new ArrayList();
    private ArrayList<MenuItemImpl> mVisibleItems;

    public MenuBuilder(Context context) {
        this.mPresenters = new CopyOnWriteArrayList();
        this.mContext = context;
        this.mResources = context.getResources();
        this.mItems = new ArrayList();
        this.mVisibleItems = new ArrayList();
        this.mIsVisibleItemsStale = true;
        this.mActionItems = new ArrayList();
        this.mNonActionItems = new ArrayList();
        this.mIsActionItemsStale = true;
        this.setShortcutsVisibleInner(true);
    }

    private MenuItemImpl createNewMenuItem(int n, int n2, int n3, int n4, CharSequence charSequence, int n5) {
        return new MenuItemImpl(this, n, n2, n3, n4, charSequence, n5);
    }

    private void dispatchPresenterUpdate(boolean bl) {
        if (this.mPresenters.isEmpty()) {
            return;
        }
        this.stopDispatchingItemsChanged();
        for (WeakReference<MenuPresenter> weakReference : this.mPresenters) {
            MenuPresenter menuPresenter = (MenuPresenter)weakReference.get();
            if (menuPresenter == null) {
                this.mPresenters.remove(weakReference);
                continue;
            }
            menuPresenter.updateMenuView(bl);
        }
        this.startDispatchingItemsChanged();
    }

    private void dispatchRestoreInstanceState(Bundle bundle) {
        if ((bundle = bundle.getSparseParcelableArray(PRESENTER_KEY)) != null && !this.mPresenters.isEmpty()) {
            for (WeakReference<MenuPresenter> weakReference : this.mPresenters) {
                Parcelable parcelable;
                MenuPresenter menuPresenter = (MenuPresenter)weakReference.get();
                if (menuPresenter == null) {
                    this.mPresenters.remove(weakReference);
                    continue;
                }
                int n = menuPresenter.getId();
                if (n <= 0 || (parcelable = (Parcelable)bundle.get(n)) == null) continue;
                menuPresenter.onRestoreInstanceState(parcelable);
            }
            return;
        }
    }

    private void dispatchSaveInstanceState(Bundle bundle) {
        if (this.mPresenters.isEmpty()) {
            return;
        }
        SparseArray sparseArray = new SparseArray();
        for (WeakReference<MenuPresenter> weakReference : this.mPresenters) {
            Parcelable parcelable;
            MenuPresenter menuPresenter = (MenuPresenter)weakReference.get();
            if (menuPresenter == null) {
                this.mPresenters.remove(weakReference);
                continue;
            }
            int n = menuPresenter.getId();
            if (n <= 0 || (parcelable = menuPresenter.onSaveInstanceState()) == null) continue;
            sparseArray.put(n, (Object)parcelable);
        }
        bundle.putSparseParcelableArray(PRESENTER_KEY, sparseArray);
    }

    private boolean dispatchSubMenuSelected(SubMenuBuilder subMenuBuilder, MenuPresenter object) {
        if (this.mPresenters.isEmpty()) {
            return false;
        }
        boolean bl = false;
        if (object != null) {
            bl = object.onSubMenuSelected(subMenuBuilder);
        }
        object = this.mPresenters.iterator();
        boolean bl2 = bl;
        while (object.hasNext()) {
            WeakReference weakReference = (WeakReference)object.next();
            MenuPresenter menuPresenter = (MenuPresenter)weakReference.get();
            if (menuPresenter == null) {
                this.mPresenters.remove(weakReference);
                bl = bl2;
            } else {
                bl = bl2;
                if (!bl2) {
                    bl = menuPresenter.onSubMenuSelected(subMenuBuilder);
                }
            }
            bl2 = bl;
        }
        return bl2;
    }

    private static int findInsertIndex(ArrayList<MenuItemImpl> arrayList, int n) {
        for (int i = arrayList.size() - 1; i >= 0; --i) {
            if (arrayList.get(i).getOrdering() > n) continue;
            return i + 1;
        }
        return 0;
    }

    private static int getOrdering(int n) {
        int[] nArray;
        int n2 = (0xFFFF0000 & n) >> 16;
        if (n2 >= 0 && n2 < (nArray = sCategoryToOrder).length) {
            return nArray[n2] << 16 | 0xFFFF & n;
        }
        throw new IllegalArgumentException("order does not contain a valid category.");
    }

    private void removeItemAtInt(int n, boolean bl) {
        if (n >= 0 && n < this.mItems.size()) {
            this.mItems.remove(n);
            if (bl) {
                this.onItemsChanged(true);
            }
            return;
        }
    }

    private void setHeaderInternal(int n, CharSequence charSequence, int n2, Drawable drawable2, View view) {
        Resources resources = this.getResources();
        if (view != null) {
            this.mHeaderView = view;
            this.mHeaderTitle = null;
            this.mHeaderIcon = null;
        } else {
            if (n > 0) {
                this.mHeaderTitle = resources.getText(n);
            } else if (charSequence != null) {
                this.mHeaderTitle = charSequence;
            }
            if (n2 > 0) {
                this.mHeaderIcon = ContextCompat.getDrawable(this.getContext(), n2);
            } else if (drawable2 != null) {
                this.mHeaderIcon = drawable2;
            }
            this.mHeaderView = null;
        }
        this.onItemsChanged(false);
    }

    private void setShortcutsVisibleInner(boolean bl) {
        boolean bl2 = true;
        bl = bl && this.mResources.getConfiguration().keyboard != 1 && ViewConfigurationCompat.shouldShowMenuShortcutsWhenKeyboardPresent(ViewConfiguration.get((Context)this.mContext), this.mContext) ? bl2 : false;
        this.mShortcutsVisible = bl;
    }

    public MenuItem add(int n) {
        return this.addInternal(0, 0, 0, this.mResources.getString(n));
    }

    public MenuItem add(int n, int n2, int n3, int n4) {
        return this.addInternal(n, n2, n3, this.mResources.getString(n4));
    }

    public MenuItem add(int n, int n2, int n3, CharSequence charSequence) {
        return this.addInternal(n, n2, n3, charSequence);
    }

    public MenuItem add(CharSequence charSequence) {
        return this.addInternal(0, 0, 0, charSequence);
    }

    public int addIntentOptions(int n, int n2, int n3, ComponentName componentName, Intent[] intentArray, Intent intent, int n4, MenuItem[] menuItemArray) {
        PackageManager packageManager = this.mContext.getPackageManager();
        int n5 = 0;
        List list = packageManager.queryIntentActivityOptions(componentName, intentArray, intent, 0);
        if (list != null) {
            n5 = list.size();
        }
        if ((n4 & 1) == 0) {
            this.removeGroup(n);
        }
        for (n4 = 0; n4 < n5; ++n4) {
            ResolveInfo resolveInfo = (ResolveInfo)list.get(n4);
            componentName = resolveInfo.specificIndex < 0 ? intent : intentArray[resolveInfo.specificIndex];
            componentName = new Intent((Intent)componentName);
            componentName.setComponent(new ComponentName(resolveInfo.activityInfo.applicationInfo.packageName, resolveInfo.activityInfo.name));
            componentName = this.add(n, n2, n3, resolveInfo.loadLabel(packageManager)).setIcon(resolveInfo.loadIcon(packageManager)).setIntent((Intent)componentName);
            if (menuItemArray == null || resolveInfo.specificIndex < 0) continue;
            menuItemArray[resolveInfo.specificIndex] = componentName;
        }
        return n5;
    }

    protected MenuItem addInternal(int n, int n2, int n3, CharSequence object) {
        int n4 = MenuBuilder.getOrdering(n3);
        object = this.createNewMenuItem(n, n2, n3, n4, (CharSequence)object, this.mDefaultShowAsAction);
        Object object2 = this.mCurrentMenuInfo;
        if (object2 != null) {
            ((MenuItemImpl)object).setMenuInfo((ContextMenu.ContextMenuInfo)object2);
        }
        object2 = this.mItems;
        ((ArrayList)object2).add(MenuBuilder.findInsertIndex((ArrayList<MenuItemImpl>)object2, n4), object);
        this.onItemsChanged(true);
        return object;
    }

    public void addMenuPresenter(MenuPresenter menuPresenter) {
        this.addMenuPresenter(menuPresenter, this.mContext);
    }

    public void addMenuPresenter(MenuPresenter menuPresenter, Context context) {
        this.mPresenters.add(new WeakReference<MenuPresenter>(menuPresenter));
        menuPresenter.initForMenu(context, this);
        this.mIsActionItemsStale = true;
    }

    public SubMenu addSubMenu(int n) {
        return this.addSubMenu(0, 0, 0, this.mResources.getString(n));
    }

    public SubMenu addSubMenu(int n, int n2, int n3, int n4) {
        return this.addSubMenu(n, n2, n3, this.mResources.getString(n4));
    }

    public SubMenu addSubMenu(int n, int n2, int n3, CharSequence object) {
        MenuItemImpl menuItemImpl = (MenuItemImpl)this.addInternal(n, n2, n3, (CharSequence)object);
        object = new SubMenuBuilder(this.mContext, this, menuItemImpl);
        menuItemImpl.setSubMenu((SubMenuBuilder)object);
        return object;
    }

    public SubMenu addSubMenu(CharSequence charSequence) {
        return this.addSubMenu(0, 0, 0, charSequence);
    }

    public void changeMenuMode() {
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onMenuModeChange(this);
        }
    }

    public void clear() {
        MenuItemImpl menuItemImpl = this.mExpandedItem;
        if (menuItemImpl != null) {
            this.collapseItemActionView(menuItemImpl);
        }
        this.mItems.clear();
        this.onItemsChanged(true);
    }

    public void clearAll() {
        this.mPreventDispatchingItemsChanged = true;
        this.clear();
        this.clearHeader();
        this.mPresenters.clear();
        this.mPreventDispatchingItemsChanged = false;
        this.mItemsChangedWhileDispatchPrevented = false;
        this.mStructureChangedWhileDispatchPrevented = false;
        this.onItemsChanged(true);
    }

    public void clearHeader() {
        this.mHeaderIcon = null;
        this.mHeaderTitle = null;
        this.mHeaderView = null;
        this.onItemsChanged(false);
    }

    public void close() {
        this.close(true);
    }

    public final void close(boolean bl) {
        if (this.mIsClosing) {
            return;
        }
        this.mIsClosing = true;
        for (WeakReference<MenuPresenter> weakReference : this.mPresenters) {
            MenuPresenter menuPresenter = (MenuPresenter)weakReference.get();
            if (menuPresenter == null) {
                this.mPresenters.remove(weakReference);
                continue;
            }
            menuPresenter.onCloseMenu(this, bl);
        }
        this.mIsClosing = false;
    }

    public boolean collapseItemActionView(MenuItemImpl menuItemImpl) {
        if (!this.mPresenters.isEmpty() && this.mExpandedItem == menuItemImpl) {
            boolean bl;
            boolean bl2 = false;
            this.stopDispatchingItemsChanged();
            Iterator<WeakReference<MenuPresenter>> iterator2 = this.mPresenters.iterator();
            while (true) {
                bl = bl2;
                if (!iterator2.hasNext()) break;
                WeakReference<MenuPresenter> weakReference = iterator2.next();
                MenuPresenter menuPresenter = (MenuPresenter)weakReference.get();
                if (menuPresenter == null) {
                    this.mPresenters.remove(weakReference);
                    bl = bl2;
                } else {
                    boolean bl3;
                    bl = bl2 = (bl3 = menuPresenter.collapseItemActionView(this, menuItemImpl));
                    if (bl3) {
                        bl = bl2;
                        break;
                    }
                }
                bl2 = bl;
            }
            this.startDispatchingItemsChanged();
            if (bl) {
                this.mExpandedItem = null;
            }
            return bl;
        }
        return false;
    }

    boolean dispatchMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
        Callback callback = this.mCallback;
        boolean bl = callback != null && callback.onMenuItemSelected(menuBuilder, menuItem);
        return bl;
    }

    public boolean expandItemActionView(MenuItemImpl menuItemImpl) {
        boolean bl;
        if (this.mPresenters.isEmpty()) {
            return false;
        }
        boolean bl2 = false;
        this.stopDispatchingItemsChanged();
        Iterator<WeakReference<MenuPresenter>> iterator2 = this.mPresenters.iterator();
        while (true) {
            bl = bl2;
            if (!iterator2.hasNext()) break;
            WeakReference<MenuPresenter> weakReference = iterator2.next();
            MenuPresenter menuPresenter = (MenuPresenter)weakReference.get();
            if (menuPresenter == null) {
                this.mPresenters.remove(weakReference);
                bl = bl2;
            } else {
                boolean bl3;
                bl = bl2 = (bl3 = menuPresenter.expandItemActionView(this, menuItemImpl));
                if (bl3) {
                    bl = bl2;
                    break;
                }
            }
            bl2 = bl;
        }
        this.startDispatchingItemsChanged();
        if (bl) {
            this.mExpandedItem = menuItemImpl;
        }
        return bl;
    }

    public int findGroupIndex(int n) {
        return this.findGroupIndex(n, 0);
    }

    public int findGroupIndex(int n, int n2) {
        int n3 = this.size();
        int n4 = n2;
        if (n2 < 0) {
            n4 = 0;
        }
        for (n2 = n4; n2 < n3; ++n2) {
            if (this.mItems.get(n2).getGroupId() != n) continue;
            return n2;
        }
        return -1;
    }

    public MenuItem findItem(int n) {
        int n2 = this.size();
        for (int i = 0; i < n2; ++i) {
            MenuItemImpl menuItemImpl = this.mItems.get(i);
            if (menuItemImpl.getItemId() == n) {
                return menuItemImpl;
            }
            if (!menuItemImpl.hasSubMenu() || (menuItemImpl = menuItemImpl.getSubMenu().findItem(n)) == null) continue;
            return menuItemImpl;
        }
        return null;
    }

    public int findItemIndex(int n) {
        int n2 = this.size();
        for (int i = 0; i < n2; ++i) {
            if (this.mItems.get(i).getItemId() != n) continue;
            return i;
        }
        return -1;
    }

    MenuItemImpl findItemWithShortcutForKey(int n, KeyEvent object) {
        ArrayList<MenuItemImpl> arrayList = this.mTempShortcutItemList;
        arrayList.clear();
        this.findItemsWithShortcutForKey(arrayList, n, (KeyEvent)object);
        if (arrayList.isEmpty()) {
            return null;
        }
        int n2 = object.getMetaState();
        KeyCharacterMap.KeyData keyData = new KeyCharacterMap.KeyData();
        object.getKeyData(keyData);
        int n3 = arrayList.size();
        if (n3 == 1) {
            return arrayList.get(0);
        }
        boolean bl = this.isQwertyMode();
        for (int i = 0; i < n3; ++i) {
            object = arrayList.get(i);
            char c = bl ? ((MenuItemImpl)object).getAlphabeticShortcut() : ((MenuItemImpl)object).getNumericShortcut();
            if (!(c == keyData.meta[0] && (n2 & 2) == 0 || c == keyData.meta[2] && (n2 & 2) != 0) && (!bl || c != '\b' || n != 67)) continue;
            return object;
        }
        return null;
    }

    void findItemsWithShortcutForKey(List<MenuItemImpl> list, int n, KeyEvent keyEvent) {
        boolean bl = this.isQwertyMode();
        int n2 = keyEvent.getModifiers();
        KeyCharacterMap.KeyData keyData = new KeyCharacterMap.KeyData();
        if (!keyEvent.getKeyData(keyData) && n != 67) {
            return;
        }
        int n3 = this.mItems.size();
        for (int i = 0; i < n3; ++i) {
            MenuItemImpl menuItemImpl = this.mItems.get(i);
            if (menuItemImpl.hasSubMenu()) {
                ((MenuBuilder)menuItemImpl.getSubMenu()).findItemsWithShortcutForKey(list, n, keyEvent);
            }
            char c = bl ? menuItemImpl.getAlphabeticShortcut() : menuItemImpl.getNumericShortcut();
            int n4 = bl ? menuItemImpl.getAlphabeticModifiers() : menuItemImpl.getNumericModifiers();
            if ((n4 = (n2 & 0x1100F) == (0x1100F & n4) ? 1 : 0) == 0 || c == '\u0000' || c != keyData.meta[0] && c != keyData.meta[2] && (!bl || c != '\b' || n != 67) || !menuItemImpl.isEnabled()) continue;
            list.add(menuItemImpl);
        }
    }

    public void flagActionItems() {
        ArrayList<MenuItemImpl> arrayList = this.getVisibleItems();
        if (!this.mIsActionItemsStale) {
            return;
        }
        int n = 0;
        for (WeakReference<MenuPresenter> object : this.mPresenters) {
            MenuPresenter menuPresenter = (MenuPresenter)object.get();
            if (menuPresenter == null) {
                this.mPresenters.remove(object);
                continue;
            }
            n |= menuPresenter.flagActionItems();
        }
        if (n != 0) {
            this.mActionItems.clear();
            this.mNonActionItems.clear();
            int n2 = arrayList.size();
            for (n = 0; n < n2; ++n) {
                MenuItemImpl menuItemImpl = arrayList.get(n);
                if (menuItemImpl.isActionButton()) {
                    this.mActionItems.add(menuItemImpl);
                    continue;
                }
                this.mNonActionItems.add(menuItemImpl);
            }
        } else {
            this.mActionItems.clear();
            this.mNonActionItems.clear();
            this.mNonActionItems.addAll(this.getVisibleItems());
        }
        this.mIsActionItemsStale = false;
    }

    public ArrayList<MenuItemImpl> getActionItems() {
        this.flagActionItems();
        return this.mActionItems;
    }

    protected String getActionViewStatesKey() {
        return ACTION_VIEW_STATES_KEY;
    }

    public Context getContext() {
        return this.mContext;
    }

    public MenuItemImpl getExpandedItem() {
        return this.mExpandedItem;
    }

    public Drawable getHeaderIcon() {
        return this.mHeaderIcon;
    }

    public CharSequence getHeaderTitle() {
        return this.mHeaderTitle;
    }

    public View getHeaderView() {
        return this.mHeaderView;
    }

    public MenuItem getItem(int n) {
        return this.mItems.get(n);
    }

    public ArrayList<MenuItemImpl> getNonActionItems() {
        this.flagActionItems();
        return this.mNonActionItems;
    }

    boolean getOptionalIconsVisible() {
        return this.mOptionalIconsVisible;
    }

    Resources getResources() {
        return this.mResources;
    }

    public MenuBuilder getRootMenu() {
        return this;
    }

    public ArrayList<MenuItemImpl> getVisibleItems() {
        if (!this.mIsVisibleItemsStale) {
            return this.mVisibleItems;
        }
        this.mVisibleItems.clear();
        int n = this.mItems.size();
        for (int i = 0; i < n; ++i) {
            MenuItemImpl menuItemImpl = this.mItems.get(i);
            if (!menuItemImpl.isVisible()) continue;
            this.mVisibleItems.add(menuItemImpl);
        }
        this.mIsVisibleItemsStale = false;
        this.mIsActionItemsStale = true;
        return this.mVisibleItems;
    }

    public boolean hasVisibleItems() {
        if (this.mOverrideVisibleItems) {
            return true;
        }
        int n = this.size();
        for (int i = 0; i < n; ++i) {
            if (!this.mItems.get(i).isVisible()) continue;
            return true;
        }
        return false;
    }

    public boolean isGroupDividerEnabled() {
        return this.mGroupDividerEnabled;
    }

    boolean isQwertyMode() {
        return this.mQwertyMode;
    }

    public boolean isShortcutKey(int n, KeyEvent keyEvent) {
        boolean bl = this.findItemWithShortcutForKey(n, keyEvent) != null;
        return bl;
    }

    public boolean isShortcutsVisible() {
        return this.mShortcutsVisible;
    }

    void onItemActionRequestChanged(MenuItemImpl menuItemImpl) {
        this.mIsActionItemsStale = true;
        this.onItemsChanged(true);
    }

    void onItemVisibleChanged(MenuItemImpl menuItemImpl) {
        this.mIsVisibleItemsStale = true;
        this.onItemsChanged(true);
    }

    public void onItemsChanged(boolean bl) {
        if (!this.mPreventDispatchingItemsChanged) {
            if (bl) {
                this.mIsVisibleItemsStale = true;
                this.mIsActionItemsStale = true;
            }
            this.dispatchPresenterUpdate(bl);
        } else {
            this.mItemsChangedWhileDispatchPrevented = true;
            if (bl) {
                this.mStructureChangedWhileDispatchPrevented = true;
            }
        }
    }

    public boolean performIdentifierAction(int n, int n2) {
        return this.performItemAction(this.findItem(n), n2);
    }

    public boolean performItemAction(MenuItem menuItem, int n) {
        return this.performItemAction(menuItem, null, n);
    }

    public boolean performItemAction(MenuItem object, MenuPresenter menuPresenter, int n) {
        Object object2 = (MenuItemImpl)object;
        if (object2 != null && ((MenuItemImpl)object2).isEnabled()) {
            boolean bl;
            boolean bl2 = ((MenuItemImpl)object2).invoke();
            object = ((MenuItemImpl)object2).getSupportActionProvider();
            boolean bl3 = object != null && ((ActionProvider)object).hasSubMenu();
            if (((MenuItemImpl)object2).hasCollapsibleActionView()) {
                bl = bl2 |= ((MenuItemImpl)object2).expandActionView();
                if (bl2) {
                    this.close(true);
                    bl = bl2;
                }
            } else if (!((MenuItemImpl)object2).hasSubMenu() && !bl3) {
                bl = bl2;
                if ((n & 1) == 0) {
                    this.close(true);
                    bl = bl2;
                }
            } else {
                if ((n & 4) == 0) {
                    this.close(false);
                }
                if (!((MenuItemImpl)object2).hasSubMenu()) {
                    ((MenuItemImpl)object2).setSubMenu(new SubMenuBuilder(this.getContext(), this, (MenuItemImpl)object2));
                }
                object2 = (SubMenuBuilder)((MenuItemImpl)object2).getSubMenu();
                if (bl3) {
                    ((ActionProvider)object).onPrepareSubMenu((SubMenu)object2);
                }
                if (!(bl = bl2 | this.dispatchSubMenuSelected((SubMenuBuilder)object2, menuPresenter))) {
                    this.close(true);
                }
            }
            return bl;
        }
        return false;
    }

    public boolean performShortcut(int n, KeyEvent object, int n2) {
        object = this.findItemWithShortcutForKey(n, (KeyEvent)object);
        boolean bl = false;
        if (object != null) {
            bl = this.performItemAction((MenuItem)object, n2);
        }
        if ((n2 & 2) != 0) {
            this.close(true);
        }
        return bl;
    }

    public void removeGroup(int n) {
        int n2 = this.findGroupIndex(n);
        if (n2 >= 0) {
            int n3 = this.mItems.size();
            for (int i = 0; i < n3 - n2 && this.mItems.get(n2).getGroupId() == n; ++i) {
                this.removeItemAtInt(n2, false);
            }
            this.onItemsChanged(true);
        }
    }

    public void removeItem(int n) {
        this.removeItemAtInt(this.findItemIndex(n), true);
    }

    public void removeItemAt(int n) {
        this.removeItemAtInt(n, true);
    }

    public void removeMenuPresenter(MenuPresenter menuPresenter) {
        for (WeakReference<MenuPresenter> weakReference : this.mPresenters) {
            MenuPresenter menuPresenter2 = (MenuPresenter)weakReference.get();
            if (menuPresenter2 != null && menuPresenter2 != menuPresenter) continue;
            this.mPresenters.remove(weakReference);
        }
    }

    public void restoreActionViewStates(Bundle bundle) {
        int n;
        if (bundle == null) {
            return;
        }
        SparseArray sparseArray = bundle.getSparseParcelableArray(this.getActionViewStatesKey());
        int n2 = this.size();
        for (n = 0; n < n2; ++n) {
            MenuItem menuItem = this.getItem(n);
            View view = menuItem.getActionView();
            if (view != null && view.getId() != -1) {
                view.restoreHierarchyState(sparseArray);
            }
            if (!menuItem.hasSubMenu()) continue;
            ((SubMenuBuilder)menuItem.getSubMenu()).restoreActionViewStates(bundle);
        }
        n = bundle.getInt(EXPANDED_ACTION_VIEW_ID);
        if (n > 0 && (bundle = this.findItem(n)) != null) {
            bundle.expandActionView();
        }
    }

    public void restorePresenterStates(Bundle bundle) {
        this.dispatchRestoreInstanceState(bundle);
    }

    public void saveActionViewStates(Bundle bundle) {
        SparseArray sparseArray = null;
        int n = this.size();
        for (int i = 0; i < n; ++i) {
            MenuItem menuItem = this.getItem(i);
            View view = menuItem.getActionView();
            SparseArray sparseArray2 = sparseArray;
            if (view != null) {
                sparseArray2 = sparseArray;
                if (view.getId() != -1) {
                    SparseArray sparseArray3 = sparseArray;
                    if (sparseArray == null) {
                        sparseArray3 = new SparseArray();
                    }
                    view.saveHierarchyState(sparseArray3);
                    sparseArray2 = sparseArray3;
                    if (menuItem.isActionViewExpanded()) {
                        bundle.putInt(EXPANDED_ACTION_VIEW_ID, menuItem.getItemId());
                        sparseArray2 = sparseArray3;
                    }
                }
            }
            if (menuItem.hasSubMenu()) {
                ((SubMenuBuilder)menuItem.getSubMenu()).saveActionViewStates(bundle);
            }
            sparseArray = sparseArray2;
        }
        if (sparseArray != null) {
            bundle.putSparseParcelableArray(this.getActionViewStatesKey(), sparseArray);
        }
    }

    public void savePresenterStates(Bundle bundle) {
        this.dispatchSaveInstanceState(bundle);
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public void setCurrentMenuInfo(ContextMenu.ContextMenuInfo contextMenuInfo) {
        this.mCurrentMenuInfo = contextMenuInfo;
    }

    public MenuBuilder setDefaultShowAsAction(int n) {
        this.mDefaultShowAsAction = n;
        return this;
    }

    void setExclusiveItemChecked(MenuItem menuItem) {
        int n = menuItem.getGroupId();
        int n2 = this.mItems.size();
        this.stopDispatchingItemsChanged();
        for (int i = 0; i < n2; ++i) {
            MenuItemImpl menuItemImpl = this.mItems.get(i);
            if (menuItemImpl.getGroupId() != n || !menuItemImpl.isExclusiveCheckable() || !menuItemImpl.isCheckable()) continue;
            boolean bl = menuItemImpl == menuItem;
            menuItemImpl.setCheckedInt(bl);
        }
        this.startDispatchingItemsChanged();
    }

    public void setGroupCheckable(int n, boolean bl, boolean bl2) {
        int n2 = this.mItems.size();
        for (int i = 0; i < n2; ++i) {
            MenuItemImpl menuItemImpl = this.mItems.get(i);
            if (menuItemImpl.getGroupId() != n) continue;
            menuItemImpl.setExclusiveCheckable(bl2);
            menuItemImpl.setCheckable(bl);
        }
    }

    @Override
    public void setGroupDividerEnabled(boolean bl) {
        this.mGroupDividerEnabled = bl;
    }

    public void setGroupEnabled(int n, boolean bl) {
        int n2 = this.mItems.size();
        for (int i = 0; i < n2; ++i) {
            MenuItemImpl menuItemImpl = this.mItems.get(i);
            if (menuItemImpl.getGroupId() != n) continue;
            menuItemImpl.setEnabled(bl);
        }
    }

    public void setGroupVisible(int n, boolean bl) {
        int n2 = this.mItems.size();
        boolean bl2 = false;
        for (int i = 0; i < n2; ++i) {
            MenuItemImpl menuItemImpl = this.mItems.get(i);
            boolean bl3 = bl2;
            if (menuItemImpl.getGroupId() == n) {
                bl3 = bl2;
                if (menuItemImpl.setVisibleInt(bl)) {
                    bl3 = true;
                }
            }
            bl2 = bl3;
        }
        if (bl2) {
            this.onItemsChanged(true);
        }
    }

    protected MenuBuilder setHeaderIconInt(int n) {
        this.setHeaderInternal(0, null, n, null, null);
        return this;
    }

    protected MenuBuilder setHeaderIconInt(Drawable drawable2) {
        this.setHeaderInternal(0, null, 0, drawable2, null);
        return this;
    }

    protected MenuBuilder setHeaderTitleInt(int n) {
        this.setHeaderInternal(n, null, 0, null, null);
        return this;
    }

    protected MenuBuilder setHeaderTitleInt(CharSequence charSequence) {
        this.setHeaderInternal(0, charSequence, 0, null, null);
        return this;
    }

    protected MenuBuilder setHeaderViewInt(View view) {
        this.setHeaderInternal(0, null, 0, null, view);
        return this;
    }

    public void setOptionalIconsVisible(boolean bl) {
        this.mOptionalIconsVisible = bl;
    }

    public void setOverrideVisibleItems(boolean bl) {
        this.mOverrideVisibleItems = bl;
    }

    public void setQwertyMode(boolean bl) {
        this.mQwertyMode = bl;
        this.onItemsChanged(false);
    }

    public void setShortcutsVisible(boolean bl) {
        if (this.mShortcutsVisible == bl) {
            return;
        }
        this.setShortcutsVisibleInner(bl);
        this.onItemsChanged(false);
    }

    public int size() {
        return this.mItems.size();
    }

    public void startDispatchingItemsChanged() {
        this.mPreventDispatchingItemsChanged = false;
        if (this.mItemsChangedWhileDispatchPrevented) {
            this.mItemsChangedWhileDispatchPrevented = false;
            this.onItemsChanged(this.mStructureChangedWhileDispatchPrevented);
        }
    }

    public void stopDispatchingItemsChanged() {
        if (!this.mPreventDispatchingItemsChanged) {
            this.mPreventDispatchingItemsChanged = true;
            this.mItemsChangedWhileDispatchPrevented = false;
            this.mStructureChangedWhileDispatchPrevented = false;
        }
    }

    public static interface Callback {
        public boolean onMenuItemSelected(MenuBuilder var1, MenuItem var2);

        public void onMenuModeChange(MenuBuilder var1);
    }

    public static interface ItemInvoker {
        public boolean invokeItem(MenuItemImpl var1);
    }
}

