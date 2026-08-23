/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.content.res.ColorStateList
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.util.Log
 *  android.view.ActionProvider
 *  android.view.ActionProvider$VisibilityListener
 *  android.view.CollapsibleActionView
 *  android.view.ContextMenu$ContextMenuInfo
 *  android.view.MenuItem
 *  android.view.MenuItem$OnActionExpandListener
 *  android.view.MenuItem$OnMenuItemClickListener
 *  android.view.SubMenu
 *  android.view.View
 *  android.widget.FrameLayout
 */
package androidx.appcompat.view.menu;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.view.CollapsibleActionView;
import androidx.appcompat.view.menu.BaseMenuWrapper;
import androidx.core.internal.view.SupportMenuItem;
import androidx.core.view.ActionProvider;
import java.lang.reflect.Method;

public class MenuItemWrapperICS
extends BaseMenuWrapper
implements MenuItem {
    static final String LOG_TAG = "MenuItemWrapper";
    private Method mSetExclusiveCheckableMethod;
    private final SupportMenuItem mWrappedObject;

    public MenuItemWrapperICS(Context context, SupportMenuItem supportMenuItem) {
        super(context);
        if (supportMenuItem != null) {
            this.mWrappedObject = supportMenuItem;
            return;
        }
        throw new IllegalArgumentException("Wrapped Object can not be null.");
    }

    public boolean collapseActionView() {
        return this.mWrappedObject.collapseActionView();
    }

    public boolean expandActionView() {
        return this.mWrappedObject.expandActionView();
    }

    public ActionProvider getActionProvider() {
        androidx.core.view.ActionProvider actionProvider = this.mWrappedObject.getSupportActionProvider();
        if (actionProvider instanceof ActionProviderWrapper) {
            return ((ActionProviderWrapper)actionProvider).mInner;
        }
        return null;
    }

    public View getActionView() {
        View view = this.mWrappedObject.getActionView();
        if (view instanceof CollapsibleActionViewWrapper) {
            return ((CollapsibleActionViewWrapper)view).getWrappedView();
        }
        return view;
    }

    public int getAlphabeticModifiers() {
        return this.mWrappedObject.getAlphabeticModifiers();
    }

    public char getAlphabeticShortcut() {
        return this.mWrappedObject.getAlphabeticShortcut();
    }

    public CharSequence getContentDescription() {
        return this.mWrappedObject.getContentDescription();
    }

    public int getGroupId() {
        return this.mWrappedObject.getGroupId();
    }

    public Drawable getIcon() {
        return this.mWrappedObject.getIcon();
    }

    public ColorStateList getIconTintList() {
        return this.mWrappedObject.getIconTintList();
    }

    public PorterDuff.Mode getIconTintMode() {
        return this.mWrappedObject.getIconTintMode();
    }

    public Intent getIntent() {
        return this.mWrappedObject.getIntent();
    }

    public int getItemId() {
        return this.mWrappedObject.getItemId();
    }

    public ContextMenu.ContextMenuInfo getMenuInfo() {
        return this.mWrappedObject.getMenuInfo();
    }

    public int getNumericModifiers() {
        return this.mWrappedObject.getNumericModifiers();
    }

    public char getNumericShortcut() {
        return this.mWrappedObject.getNumericShortcut();
    }

    public int getOrder() {
        return this.mWrappedObject.getOrder();
    }

    public SubMenu getSubMenu() {
        return this.getSubMenuWrapper(this.mWrappedObject.getSubMenu());
    }

    public CharSequence getTitle() {
        return this.mWrappedObject.getTitle();
    }

    public CharSequence getTitleCondensed() {
        return this.mWrappedObject.getTitleCondensed();
    }

    public CharSequence getTooltipText() {
        return this.mWrappedObject.getTooltipText();
    }

    public boolean hasSubMenu() {
        return this.mWrappedObject.hasSubMenu();
    }

    public boolean isActionViewExpanded() {
        return this.mWrappedObject.isActionViewExpanded();
    }

    public boolean isCheckable() {
        return this.mWrappedObject.isCheckable();
    }

    public boolean isChecked() {
        return this.mWrappedObject.isChecked();
    }

    public boolean isEnabled() {
        return this.mWrappedObject.isEnabled();
    }

    public boolean isVisible() {
        return this.mWrappedObject.isVisible();
    }

    public MenuItem setActionProvider(ActionProvider actionProvider) {
        ActionProviderWrapper actionProviderWrapper = Build.VERSION.SDK_INT >= 16 ? new ActionProviderWrapperJB(this, this.mContext, actionProvider) : new ActionProviderWrapper(this, this.mContext, actionProvider);
        SupportMenuItem supportMenuItem = this.mWrappedObject;
        if (actionProvider == null) {
            actionProviderWrapper = null;
        }
        supportMenuItem.setSupportActionProvider(actionProviderWrapper);
        return this;
    }

    public MenuItem setActionView(int n) {
        this.mWrappedObject.setActionView(n);
        View view = this.mWrappedObject.getActionView();
        if (view instanceof android.view.CollapsibleActionView) {
            this.mWrappedObject.setActionView((View)new CollapsibleActionViewWrapper(view));
        }
        return this;
    }

    public MenuItem setActionView(View view) {
        Object object = view;
        if (view instanceof android.view.CollapsibleActionView) {
            object = new CollapsibleActionViewWrapper(view);
        }
        this.mWrappedObject.setActionView((View)object);
        return this;
    }

    public MenuItem setAlphabeticShortcut(char c) {
        this.mWrappedObject.setAlphabeticShortcut(c);
        return this;
    }

    public MenuItem setAlphabeticShortcut(char c, int n) {
        this.mWrappedObject.setAlphabeticShortcut(c, n);
        return this;
    }

    public MenuItem setCheckable(boolean bl) {
        this.mWrappedObject.setCheckable(bl);
        return this;
    }

    public MenuItem setChecked(boolean bl) {
        this.mWrappedObject.setChecked(bl);
        return this;
    }

    public MenuItem setContentDescription(CharSequence charSequence) {
        this.mWrappedObject.setContentDescription(charSequence);
        return this;
    }

    public MenuItem setEnabled(boolean bl) {
        this.mWrappedObject.setEnabled(bl);
        return this;
    }

    public void setExclusiveCheckable(boolean bl) {
        try {
            if (this.mSetExclusiveCheckableMethod == null) {
                this.mSetExclusiveCheckableMethod = this.mWrappedObject.getClass().getDeclaredMethod("setExclusiveCheckable", Boolean.TYPE);
            }
            this.mSetExclusiveCheckableMethod.invoke((Object)this.mWrappedObject, bl);
        }
        catch (Exception exception) {
            Log.w((String)LOG_TAG, (String)"Error while calling setExclusiveCheckable", (Throwable)exception);
        }
    }

    public MenuItem setIcon(int n) {
        this.mWrappedObject.setIcon(n);
        return this;
    }

    public MenuItem setIcon(Drawable drawable2) {
        this.mWrappedObject.setIcon(drawable2);
        return this;
    }

    public MenuItem setIconTintList(ColorStateList colorStateList) {
        this.mWrappedObject.setIconTintList(colorStateList);
        return this;
    }

    public MenuItem setIconTintMode(PorterDuff.Mode mode) {
        this.mWrappedObject.setIconTintMode(mode);
        return this;
    }

    public MenuItem setIntent(Intent intent) {
        this.mWrappedObject.setIntent(intent);
        return this;
    }

    public MenuItem setNumericShortcut(char c) {
        this.mWrappedObject.setNumericShortcut(c);
        return this;
    }

    public MenuItem setNumericShortcut(char c, int n) {
        this.mWrappedObject.setNumericShortcut(c, n);
        return this;
    }

    public MenuItem setOnActionExpandListener(MenuItem.OnActionExpandListener onActionExpandListener) {
        SupportMenuItem supportMenuItem = this.mWrappedObject;
        onActionExpandListener = onActionExpandListener != null ? new OnActionExpandListenerWrapper(this, onActionExpandListener) : null;
        supportMenuItem.setOnActionExpandListener(onActionExpandListener);
        return this;
    }

    public MenuItem setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener onMenuItemClickListener) {
        SupportMenuItem supportMenuItem = this.mWrappedObject;
        onMenuItemClickListener = onMenuItemClickListener != null ? new OnMenuItemClickListenerWrapper(this, onMenuItemClickListener) : null;
        supportMenuItem.setOnMenuItemClickListener(onMenuItemClickListener);
        return this;
    }

    public MenuItem setShortcut(char c, char c2) {
        this.mWrappedObject.setShortcut(c, c2);
        return this;
    }

    public MenuItem setShortcut(char c, char c2, int n, int n2) {
        this.mWrappedObject.setShortcut(c, c2, n, n2);
        return this;
    }

    public void setShowAsAction(int n) {
        this.mWrappedObject.setShowAsAction(n);
    }

    public MenuItem setShowAsActionFlags(int n) {
        this.mWrappedObject.setShowAsActionFlags(n);
        return this;
    }

    public MenuItem setTitle(int n) {
        this.mWrappedObject.setTitle(n);
        return this;
    }

    public MenuItem setTitle(CharSequence charSequence) {
        this.mWrappedObject.setTitle(charSequence);
        return this;
    }

    public MenuItem setTitleCondensed(CharSequence charSequence) {
        this.mWrappedObject.setTitleCondensed(charSequence);
        return this;
    }

    public MenuItem setTooltipText(CharSequence charSequence) {
        this.mWrappedObject.setTooltipText(charSequence);
        return this;
    }

    public MenuItem setVisible(boolean bl) {
        return this.mWrappedObject.setVisible(bl);
    }

    private class ActionProviderWrapper
    extends androidx.core.view.ActionProvider {
        final ActionProvider mInner;
        final MenuItemWrapperICS this$0;

        ActionProviderWrapper(MenuItemWrapperICS menuItemWrapperICS, Context context, ActionProvider actionProvider) {
            this.this$0 = menuItemWrapperICS;
            super(context);
            this.mInner = actionProvider;
        }

        @Override
        public boolean hasSubMenu() {
            return this.mInner.hasSubMenu();
        }

        @Override
        public View onCreateActionView() {
            return this.mInner.onCreateActionView();
        }

        @Override
        public boolean onPerformDefaultAction() {
            return this.mInner.onPerformDefaultAction();
        }

        @Override
        public void onPrepareSubMenu(SubMenu subMenu) {
            this.mInner.onPrepareSubMenu(this.this$0.getSubMenuWrapper(subMenu));
        }
    }

    private class ActionProviderWrapperJB
    extends ActionProviderWrapper
    implements ActionProvider.VisibilityListener {
        private ActionProvider.VisibilityListener mListener;
        final MenuItemWrapperICS this$0;

        ActionProviderWrapperJB(MenuItemWrapperICS menuItemWrapperICS, Context context, ActionProvider actionProvider) {
            this.this$0 = menuItemWrapperICS;
            super(menuItemWrapperICS, context, actionProvider);
        }

        @Override
        public boolean isVisible() {
            return this.mInner.isVisible();
        }

        public void onActionProviderVisibilityChanged(boolean bl) {
            ActionProvider.VisibilityListener visibilityListener = this.mListener;
            if (visibilityListener != null) {
                visibilityListener.onActionProviderVisibilityChanged(bl);
            }
        }

        @Override
        public View onCreateActionView(MenuItem menuItem) {
            return this.mInner.onCreateActionView(menuItem);
        }

        @Override
        public boolean overridesItemVisibility() {
            return this.mInner.overridesItemVisibility();
        }

        @Override
        public void refreshVisibility() {
            this.mInner.refreshVisibility();
        }

        @Override
        public void setVisibilityListener(ActionProvider.VisibilityListener object) {
            this.mListener = object;
            ActionProvider actionProvider = this.mInner;
            object = object != null ? this : null;
            actionProvider.setVisibilityListener((ActionProvider.VisibilityListener)object);
        }
    }

    static class CollapsibleActionViewWrapper
    extends FrameLayout
    implements CollapsibleActionView {
        final android.view.CollapsibleActionView mWrappedView;

        CollapsibleActionViewWrapper(View view) {
            super(view.getContext());
            this.mWrappedView = (android.view.CollapsibleActionView)view;
            this.addView(view);
        }

        View getWrappedView() {
            return (View)this.mWrappedView;
        }

        @Override
        public void onActionViewCollapsed() {
            this.mWrappedView.onActionViewCollapsed();
        }

        @Override
        public void onActionViewExpanded() {
            this.mWrappedView.onActionViewExpanded();
        }
    }

    private class OnActionExpandListenerWrapper
    implements MenuItem.OnActionExpandListener {
        private final MenuItem.OnActionExpandListener mObject;
        final MenuItemWrapperICS this$0;

        OnActionExpandListenerWrapper(MenuItemWrapperICS menuItemWrapperICS, MenuItem.OnActionExpandListener onActionExpandListener) {
            this.this$0 = menuItemWrapperICS;
            this.mObject = onActionExpandListener;
        }

        public boolean onMenuItemActionCollapse(MenuItem menuItem) {
            return this.mObject.onMenuItemActionCollapse(this.this$0.getMenuItemWrapper(menuItem));
        }

        public boolean onMenuItemActionExpand(MenuItem menuItem) {
            return this.mObject.onMenuItemActionExpand(this.this$0.getMenuItemWrapper(menuItem));
        }
    }

    private class OnMenuItemClickListenerWrapper
    implements MenuItem.OnMenuItemClickListener {
        private final MenuItem.OnMenuItemClickListener mObject;
        final MenuItemWrapperICS this$0;

        OnMenuItemClickListenerWrapper(MenuItemWrapperICS menuItemWrapperICS, MenuItem.OnMenuItemClickListener onMenuItemClickListener) {
            this.this$0 = menuItemWrapperICS;
            this.mObject = onMenuItemClickListener;
        }

        public boolean onMenuItemClick(MenuItem menuItem) {
            return this.mObject.onMenuItemClick(this.this$0.getMenuItemWrapper(menuItem));
        }
    }
}

