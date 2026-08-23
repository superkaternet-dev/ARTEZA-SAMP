/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.View
 */
package androidx.appcompat.view;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.view.menu.SubMenuBuilder;
import androidx.appcompat.widget.ActionBarContextView;
import java.lang.ref.WeakReference;

public class StandaloneActionMode
extends ActionMode
implements MenuBuilder.Callback {
    private ActionMode.Callback mCallback;
    private Context mContext;
    private ActionBarContextView mContextView;
    private WeakReference<View> mCustomView;
    private boolean mFinished;
    private boolean mFocusable;
    private MenuBuilder mMenu;

    public StandaloneActionMode(Context object, ActionBarContextView actionBarContextView, ActionMode.Callback callback, boolean bl) {
        this.mContext = object;
        this.mContextView = actionBarContextView;
        this.mCallback = callback;
        object = new MenuBuilder(actionBarContextView.getContext()).setDefaultShowAsAction(1);
        this.mMenu = object;
        ((MenuBuilder)object).setCallback(this);
        this.mFocusable = bl;
    }

    @Override
    public void finish() {
        if (this.mFinished) {
            return;
        }
        this.mFinished = true;
        this.mContextView.sendAccessibilityEvent(32);
        this.mCallback.onDestroyActionMode(this);
    }

    @Override
    public View getCustomView() {
        Object object = this.mCustomView;
        object = object != null ? (View)object.get() : null;
        return object;
    }

    @Override
    public Menu getMenu() {
        return this.mMenu;
    }

    @Override
    public MenuInflater getMenuInflater() {
        return new SupportMenuInflater(this.mContextView.getContext());
    }

    @Override
    public CharSequence getSubtitle() {
        return this.mContextView.getSubtitle();
    }

    @Override
    public CharSequence getTitle() {
        return this.mContextView.getTitle();
    }

    @Override
    public void invalidate() {
        this.mCallback.onPrepareActionMode(this, this.mMenu);
    }

    @Override
    public boolean isTitleOptional() {
        return this.mContextView.isTitleOptional();
    }

    @Override
    public boolean isUiFocusable() {
        return this.mFocusable;
    }

    public void onCloseMenu(MenuBuilder menuBuilder, boolean bl) {
    }

    public void onCloseSubMenu(SubMenuBuilder subMenuBuilder) {
    }

    @Override
    public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
        return this.mCallback.onActionItemClicked(this, menuItem);
    }

    @Override
    public void onMenuModeChange(MenuBuilder menuBuilder) {
        this.invalidate();
        this.mContextView.showOverflowMenu();
    }

    public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        if (!subMenuBuilder.hasVisibleItems()) {
            return true;
        }
        new MenuPopupHelper(this.mContextView.getContext(), subMenuBuilder).show();
        return true;
    }

    @Override
    public void setCustomView(View object) {
        this.mContextView.setCustomView((View)object);
        object = object != null ? new WeakReference<View>((View)object) : null;
        this.mCustomView = object;
    }

    @Override
    public void setSubtitle(int n) {
        this.setSubtitle(this.mContext.getString(n));
    }

    @Override
    public void setSubtitle(CharSequence charSequence) {
        this.mContextView.setSubtitle(charSequence);
    }

    @Override
    public void setTitle(int n) {
        this.setTitle(this.mContext.getString(n));
    }

    @Override
    public void setTitle(CharSequence charSequence) {
        this.mContextView.setTitle(charSequence);
    }

    @Override
    public void setTitleOptionalHint(boolean bl) {
        super.setTitleOptionalHint(bl);
        this.mContextView.setTitleOptional(bl);
    }
}

