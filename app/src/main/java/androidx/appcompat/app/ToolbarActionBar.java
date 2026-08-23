/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.graphics.drawable.Drawable
 *  android.view.KeyCharacterMap
 *  android.view.KeyEvent
 *  android.view.LayoutInflater
 *  android.view.Menu
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.Window$Callback
 *  android.widget.SpinnerAdapter
 */
package androidx.appcompat.app;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.SpinnerAdapter;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.NavItemSelectedListener;
import androidx.appcompat.view.WindowCallbackWrapper;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.widget.DecorToolbar;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.ToolbarWidgetWrapper;
import androidx.core.view.ViewCompat;
import java.util.ArrayList;

class ToolbarActionBar
extends ActionBar {
    DecorToolbar mDecorToolbar;
    private boolean mLastMenuVisibility;
    private boolean mMenuCallbackSet;
    private final Toolbar.OnMenuItemClickListener mMenuClicker;
    private final Runnable mMenuInvalidator;
    private ArrayList<ActionBar.OnMenuVisibilityListener> mMenuVisibilityListeners = new ArrayList();
    boolean mToolbarMenuPrepared;
    Window.Callback mWindowCallback;

    ToolbarActionBar(Toolbar toolbar, CharSequence charSequence, Window.Callback callback) {
        Toolbar.OnMenuItemClickListener onMenuItemClickListener;
        this.mMenuInvalidator = new Runnable(this){
            final ToolbarActionBar this$0;
            {
                this.this$0 = toolbarActionBar;
            }

            @Override
            public void run() {
                this.this$0.populateOptionsMenu();
            }
        };
        this.mMenuClicker = onMenuItemClickListener = new Toolbar.OnMenuItemClickListener(this){
            final ToolbarActionBar this$0;
            {
                this.this$0 = toolbarActionBar;
            }

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return this.this$0.mWindowCallback.onMenuItemSelected(0, menuItem);
            }
        };
        this.mDecorToolbar = new ToolbarWidgetWrapper(toolbar, false);
        this.mWindowCallback = callback = new ToolbarCallbackWrapper(this, callback);
        this.mDecorToolbar.setWindowCallback(callback);
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
        this.mDecorToolbar.setWindowTitle(charSequence);
    }

    private Menu getMenu() {
        if (!this.mMenuCallbackSet) {
            this.mDecorToolbar.setMenuCallbacks(new ActionMenuPresenterCallback(this), new MenuBuilderCallback(this));
            this.mMenuCallbackSet = true;
        }
        return this.mDecorToolbar.getMenu();
    }

    @Override
    public void addOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener onMenuVisibilityListener) {
        this.mMenuVisibilityListeners.add(onMenuVisibilityListener);
    }

    @Override
    public void addTab(ActionBar.Tab tab) {
        throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @Override
    public void addTab(ActionBar.Tab tab, int n) {
        throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @Override
    public void addTab(ActionBar.Tab tab, int n, boolean bl) {
        throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @Override
    public void addTab(ActionBar.Tab tab, boolean bl) {
        throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @Override
    public boolean closeOptionsMenu() {
        return this.mDecorToolbar.hideOverflowMenu();
    }

    @Override
    public boolean collapseActionView() {
        if (this.mDecorToolbar.hasExpandedActionView()) {
            this.mDecorToolbar.collapseActionView();
            return true;
        }
        return false;
    }

    @Override
    public void dispatchMenuVisibilityChanged(boolean bl) {
        if (bl == this.mLastMenuVisibility) {
            return;
        }
        this.mLastMenuVisibility = bl;
        int n = this.mMenuVisibilityListeners.size();
        for (int i = 0; i < n; ++i) {
            this.mMenuVisibilityListeners.get(i).onMenuVisibilityChanged(bl);
        }
    }

    @Override
    public View getCustomView() {
        return this.mDecorToolbar.getCustomView();
    }

    @Override
    public int getDisplayOptions() {
        return this.mDecorToolbar.getDisplayOptions();
    }

    @Override
    public float getElevation() {
        return ViewCompat.getElevation((View)this.mDecorToolbar.getViewGroup());
    }

    @Override
    public int getHeight() {
        return this.mDecorToolbar.getHeight();
    }

    @Override
    public int getNavigationItemCount() {
        return 0;
    }

    @Override
    public int getNavigationMode() {
        return 0;
    }

    @Override
    public int getSelectedNavigationIndex() {
        return -1;
    }

    @Override
    public ActionBar.Tab getSelectedTab() {
        throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @Override
    public CharSequence getSubtitle() {
        return this.mDecorToolbar.getSubtitle();
    }

    @Override
    public ActionBar.Tab getTabAt(int n) {
        throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @Override
    public int getTabCount() {
        return 0;
    }

    @Override
    public Context getThemedContext() {
        return this.mDecorToolbar.getContext();
    }

    @Override
    public CharSequence getTitle() {
        return this.mDecorToolbar.getTitle();
    }

    public Window.Callback getWrappedWindowCallback() {
        return this.mWindowCallback;
    }

    @Override
    public void hide() {
        this.mDecorToolbar.setVisibility(8);
    }

    @Override
    public boolean invalidateOptionsMenu() {
        this.mDecorToolbar.getViewGroup().removeCallbacks(this.mMenuInvalidator);
        ViewCompat.postOnAnimation((View)this.mDecorToolbar.getViewGroup(), this.mMenuInvalidator);
        return true;
    }

    @Override
    public boolean isShowing() {
        boolean bl = this.mDecorToolbar.getVisibility() == 0;
        return bl;
    }

    @Override
    public boolean isTitleTruncated() {
        return super.isTitleTruncated();
    }

    @Override
    public ActionBar.Tab newTab() {
        throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    @Override
    void onDestroy() {
        this.mDecorToolbar.getViewGroup().removeCallbacks(this.mMenuInvalidator);
    }

    @Override
    public boolean onKeyShortcut(int n, KeyEvent keyEvent) {
        Menu menu = this.getMenu();
        if (menu != null) {
            int n2 = keyEvent != null ? keyEvent.getDeviceId() : -1;
            n2 = KeyCharacterMap.load((int)n2).getKeyboardType();
            boolean bl = true;
            if (n2 == 1) {
                bl = false;
            }
            menu.setQwertyMode(bl);
            return menu.performShortcut(n, keyEvent, 0);
        }
        return false;
    }

    @Override
    public boolean onMenuKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getAction() == 1) {
            this.openOptionsMenu();
        }
        return true;
    }

    @Override
    public boolean openOptionsMenu() {
        return this.mDecorToolbar.showOverflowMenu();
    }

    void populateOptionsMenu() {
        Menu menu = this.getMenu();
        MenuBuilder menuBuilder = menu instanceof MenuBuilder ? (MenuBuilder)menu : null;
        if (menuBuilder != null) {
            menuBuilder.stopDispatchingItemsChanged();
        }
        try {
            menu.clear();
            if (!this.mWindowCallback.onCreatePanelMenu(0, menu) || !this.mWindowCallback.onPreparePanel(0, null, menu)) {
                menu.clear();
            }
            return;
        }
        finally {
            if (menuBuilder != null) {
                menuBuilder.startDispatchingItemsChanged();
            }
        }
    }

    @Override
    public void removeAllTabs() {
        throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @Override
    public void removeOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener onMenuVisibilityListener) {
        this.mMenuVisibilityListeners.remove(onMenuVisibilityListener);
    }

    @Override
    public void removeTab(ActionBar.Tab tab) {
        throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @Override
    public void removeTabAt(int n) {
        throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @Override
    public boolean requestFocus() {
        ViewGroup viewGroup = this.mDecorToolbar.getViewGroup();
        if (viewGroup != null && !viewGroup.hasFocus()) {
            viewGroup.requestFocus();
            return true;
        }
        return false;
    }

    @Override
    public void selectTab(ActionBar.Tab tab) {
        throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @Override
    public void setBackgroundDrawable(Drawable drawable2) {
        this.mDecorToolbar.setBackgroundDrawable(drawable2);
    }

    @Override
    public void setCustomView(int n) {
        this.setCustomView(LayoutInflater.from((Context)this.mDecorToolbar.getContext()).inflate(n, this.mDecorToolbar.getViewGroup(), false));
    }

    @Override
    public void setCustomView(View view) {
        this.setCustomView(view, new ActionBar.LayoutParams(-2, -2));
    }

    @Override
    public void setCustomView(View view, ActionBar.LayoutParams layoutParams) {
        if (view != null) {
            view.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        }
        this.mDecorToolbar.setCustomView(view);
    }

    @Override
    public void setDefaultDisplayHomeAsUpEnabled(boolean bl) {
    }

    @Override
    public void setDisplayHomeAsUpEnabled(boolean bl) {
        int n = bl ? 4 : 0;
        this.setDisplayOptions(n, 4);
    }

    @Override
    public void setDisplayOptions(int n) {
        this.setDisplayOptions(n, -1);
    }

    @Override
    public void setDisplayOptions(int n, int n2) {
        int n3 = this.mDecorToolbar.getDisplayOptions();
        this.mDecorToolbar.setDisplayOptions(n & n2 | ~n2 & n3);
    }

    @Override
    public void setDisplayShowCustomEnabled(boolean bl) {
        int n = bl ? 16 : 0;
        this.setDisplayOptions(n, 16);
    }

    @Override
    public void setDisplayShowHomeEnabled(boolean bl) {
        int n = bl ? 2 : 0;
        this.setDisplayOptions(n, 2);
    }

    @Override
    public void setDisplayShowTitleEnabled(boolean bl) {
        int n = bl ? 8 : 0;
        this.setDisplayOptions(n, 8);
    }

    @Override
    public void setDisplayUseLogoEnabled(boolean bl) {
        this.setDisplayOptions(bl ? 1 : 0, 1);
    }

    @Override
    public void setElevation(float f) {
        ViewCompat.setElevation((View)this.mDecorToolbar.getViewGroup(), f);
    }

    @Override
    public void setHomeActionContentDescription(int n) {
        this.mDecorToolbar.setNavigationContentDescription(n);
    }

    @Override
    public void setHomeActionContentDescription(CharSequence charSequence) {
        this.mDecorToolbar.setNavigationContentDescription(charSequence);
    }

    @Override
    public void setHomeAsUpIndicator(int n) {
        this.mDecorToolbar.setNavigationIcon(n);
    }

    @Override
    public void setHomeAsUpIndicator(Drawable drawable2) {
        this.mDecorToolbar.setNavigationIcon(drawable2);
    }

    @Override
    public void setHomeButtonEnabled(boolean bl) {
    }

    @Override
    public void setIcon(int n) {
        this.mDecorToolbar.setIcon(n);
    }

    @Override
    public void setIcon(Drawable drawable2) {
        this.mDecorToolbar.setIcon(drawable2);
    }

    @Override
    public void setListNavigationCallbacks(SpinnerAdapter spinnerAdapter, ActionBar.OnNavigationListener onNavigationListener) {
        this.mDecorToolbar.setDropdownParams(spinnerAdapter, new NavItemSelectedListener(onNavigationListener));
    }

    @Override
    public void setLogo(int n) {
        this.mDecorToolbar.setLogo(n);
    }

    @Override
    public void setLogo(Drawable drawable2) {
        this.mDecorToolbar.setLogo(drawable2);
    }

    @Override
    public void setNavigationMode(int n) {
        if (n != 2) {
            this.mDecorToolbar.setNavigationMode(n);
            return;
        }
        throw new IllegalArgumentException("Tabs not supported in this configuration");
    }

    @Override
    public void setSelectedNavigationItem(int n) {
        switch (this.mDecorToolbar.getNavigationMode()) {
            default: {
                throw new IllegalStateException("setSelectedNavigationIndex not valid for current navigation mode");
            }
            case 1: 
        }
        this.mDecorToolbar.setDropdownSelectedPosition(n);
    }

    @Override
    public void setShowHideAnimationEnabled(boolean bl) {
    }

    @Override
    public void setSplitBackgroundDrawable(Drawable drawable2) {
    }

    @Override
    public void setStackedBackgroundDrawable(Drawable drawable2) {
    }

    @Override
    public void setSubtitle(int n) {
        DecorToolbar decorToolbar = this.mDecorToolbar;
        CharSequence charSequence = n != 0 ? decorToolbar.getContext().getText(n) : null;
        decorToolbar.setSubtitle(charSequence);
    }

    @Override
    public void setSubtitle(CharSequence charSequence) {
        this.mDecorToolbar.setSubtitle(charSequence);
    }

    @Override
    public void setTitle(int n) {
        DecorToolbar decorToolbar = this.mDecorToolbar;
        CharSequence charSequence = n != 0 ? decorToolbar.getContext().getText(n) : null;
        decorToolbar.setTitle(charSequence);
    }

    @Override
    public void setTitle(CharSequence charSequence) {
        this.mDecorToolbar.setTitle(charSequence);
    }

    @Override
    public void setWindowTitle(CharSequence charSequence) {
        this.mDecorToolbar.setWindowTitle(charSequence);
    }

    @Override
    public void show() {
        this.mDecorToolbar.setVisibility(0);
    }

    private final class ActionMenuPresenterCallback
    implements MenuPresenter.Callback {
        private boolean mClosingActionMenu;
        final ToolbarActionBar this$0;

        ActionMenuPresenterCallback(ToolbarActionBar toolbarActionBar) {
            this.this$0 = toolbarActionBar;
        }

        @Override
        public void onCloseMenu(MenuBuilder menuBuilder, boolean bl) {
            if (this.mClosingActionMenu) {
                return;
            }
            this.mClosingActionMenu = true;
            this.this$0.mDecorToolbar.dismissPopupMenus();
            if (this.this$0.mWindowCallback != null) {
                this.this$0.mWindowCallback.onPanelClosed(108, (Menu)menuBuilder);
            }
            this.mClosingActionMenu = false;
        }

        @Override
        public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            if (this.this$0.mWindowCallback != null) {
                this.this$0.mWindowCallback.onMenuOpened(108, (Menu)menuBuilder);
                return true;
            }
            return false;
        }
    }

    private final class MenuBuilderCallback
    implements MenuBuilder.Callback {
        final ToolbarActionBar this$0;

        MenuBuilderCallback(ToolbarActionBar toolbarActionBar) {
            this.this$0 = toolbarActionBar;
        }

        @Override
        public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
            return false;
        }

        @Override
        public void onMenuModeChange(MenuBuilder menuBuilder) {
            if (this.this$0.mWindowCallback != null) {
                if (this.this$0.mDecorToolbar.isOverflowMenuShowing()) {
                    this.this$0.mWindowCallback.onPanelClosed(108, (Menu)menuBuilder);
                } else if (this.this$0.mWindowCallback.onPreparePanel(0, null, (Menu)menuBuilder)) {
                    this.this$0.mWindowCallback.onMenuOpened(108, (Menu)menuBuilder);
                }
            }
        }
    }

    private class ToolbarCallbackWrapper
    extends WindowCallbackWrapper {
        final ToolbarActionBar this$0;

        public ToolbarCallbackWrapper(ToolbarActionBar toolbarActionBar, Window.Callback callback) {
            this.this$0 = toolbarActionBar;
            super(callback);
        }

        @Override
        public View onCreatePanelView(int n) {
            if (n == 0) {
                return new View(this.this$0.mDecorToolbar.getContext());
            }
            return super.onCreatePanelView(n);
        }

        @Override
        public boolean onPreparePanel(int n, View view, Menu menu) {
            boolean bl = super.onPreparePanel(n, view, menu);
            if (bl && !this.this$0.mToolbarMenuPrepared) {
                this.this$0.mDecorToolbar.setMenuPrepared();
                this.this$0.mToolbarMenuPrepared = true;
            }
            return bl;
        }
    }
}

