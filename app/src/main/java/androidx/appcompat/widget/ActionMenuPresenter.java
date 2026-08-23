/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.graphics.drawable.Drawable
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.SparseBooleanArray
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.R;
import androidx.appcompat.view.ActionBarPolicy;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.BaseMenuPresenter;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPopup;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.view.menu.ShowableListMenu;
import androidx.appcompat.view.menu.SubMenuBuilder;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.ForwardingListener;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ActionProvider;
import java.util.ArrayList;

class ActionMenuPresenter
extends BaseMenuPresenter
implements ActionProvider.SubUiVisibilityListener {
    private static final String TAG = "ActionMenuPresenter";
    private final SparseBooleanArray mActionButtonGroups = new SparseBooleanArray();
    ActionButtonSubmenu mActionButtonPopup;
    private int mActionItemWidthLimit;
    private boolean mExpandedActionViewsExclusive;
    private int mMaxItems;
    private boolean mMaxItemsSet;
    private int mMinCellSize;
    int mOpenSubMenuId;
    OverflowMenuButton mOverflowButton;
    OverflowPopup mOverflowPopup;
    private Drawable mPendingOverflowIcon;
    private boolean mPendingOverflowIconSet;
    private ActionMenuPopupCallback mPopupCallback;
    final PopupPresenterCallback mPopupPresenterCallback = new PopupPresenterCallback(this);
    OpenOverflowRunnable mPostedOpenRunnable;
    private boolean mReserveOverflow;
    private boolean mReserveOverflowSet;
    private boolean mStrictWidthLimit;
    private int mWidthLimit;
    private boolean mWidthLimitSet;

    public ActionMenuPresenter(Context context) {
        super(context, R.layout.abc_action_menu_layout, R.layout.abc_action_menu_item_layout);
    }

    private View findViewForItem(MenuItem menuItem) {
        ViewGroup viewGroup = (ViewGroup)this.mMenuView;
        if (viewGroup == null) {
            return null;
        }
        int n = viewGroup.getChildCount();
        for (int i = 0; i < n; ++i) {
            View view = viewGroup.getChildAt(i);
            if (!(view instanceof MenuView.ItemView) || ((MenuView.ItemView)view).getItemData() != menuItem) continue;
            return view;
        }
        return null;
    }

    @Override
    public void bindItemView(MenuItemImpl object, MenuView.ItemView itemView) {
        itemView.initialize((MenuItemImpl)object, 0);
        object = (ActionMenuView)this.mMenuView;
        itemView = (ActionMenuItemView)itemView;
        ((ActionMenuItemView)itemView).setItemInvoker((MenuBuilder.ItemInvoker)object);
        if (this.mPopupCallback == null) {
            this.mPopupCallback = new ActionMenuPopupCallback(this);
        }
        ((ActionMenuItemView)itemView).setPopupCallback(this.mPopupCallback);
    }

    public boolean dismissPopupMenus() {
        return this.hideOverflowMenu() | this.hideSubMenus();
    }

    @Override
    public boolean filterLeftoverView(ViewGroup viewGroup, int n) {
        if (viewGroup.getChildAt(n) == this.mOverflowButton) {
            return false;
        }
        return super.filterLeftoverView(viewGroup, n);
    }

    @Override
    public boolean flagActionItems() {
        int n;
        MenuItemImpl menuItemImpl;
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        ViewGroup viewGroup;
        int n7;
        int n8;
        int n9;
        int n10;
        ArrayList<MenuItemImpl> arrayList;
        block36: {
            block37: {
                if (this.mMenu != null) {
                    arrayList = this.mMenu.getVisibleItems();
                    n10 = arrayList.size();
                } else {
                    arrayList = null;
                    n10 = 0;
                }
                n9 = this.mMaxItems;
                n8 = this.mActionItemWidthLimit;
                n7 = View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
                viewGroup = (ViewGroup)this.mMenuView;
                n6 = 0;
                n5 = 0;
                n4 = 0;
                n3 = 0;
                for (n2 = 0; n2 < n10; ++n2) {
                    menuItemImpl = arrayList.get(n2);
                    if (menuItemImpl.requiresActionButton()) {
                        ++n6;
                    } else if (menuItemImpl.requestsActionButton()) {
                        ++n5;
                    } else {
                        n3 = 1;
                    }
                    n = n9;
                    if (this.mExpandedActionViewsExclusive) {
                        n = n9;
                        if (menuItemImpl.isActionViewExpanded()) {
                            n = 0;
                        }
                    }
                    n9 = n;
                }
                n2 = n9;
                if (!this.mReserveOverflow) break block36;
                if (n3 != 0) break block37;
                n2 = n9;
                if (n6 + n5 <= n9) break block36;
            }
            n2 = n9 - 1;
        }
        int n11 = n2 - n6;
        menuItemImpl = this.mActionButtonGroups;
        menuItemImpl.clear();
        n5 = 0;
        n2 = 0;
        if (this.mStrictWidthLimit) {
            n9 = this.mMinCellSize;
            n2 = n8 / n9;
            n5 = n9 + n8 % n9 / n2;
        }
        n9 = n4;
        n4 = n6;
        n3 = n8;
        n6 = n11;
        n8 = n10;
        for (n = 0; n < n8; ++n) {
            Object object;
            MenuItemImpl menuItemImpl2 = arrayList.get(n);
            if (menuItemImpl2.requiresActionButton()) {
                object = this.getItemView(menuItemImpl2, null, viewGroup);
                if (this.mStrictWidthLimit) {
                    n2 -= ActionMenuView.measureChildForCells((View)object, n5, n2, n7, 0);
                } else {
                    object.measure(n7, n7);
                }
                n11 = object.getMeasuredWidth();
                n3 -= n11;
                n10 = n9;
                if (n9 == 0) {
                    n10 = n11;
                }
                if ((n9 = menuItemImpl2.getGroupId()) != 0) {
                    menuItemImpl.put(n9, true);
                }
                menuItemImpl2.setIsActionButton(true);
                n9 = n10;
                continue;
            }
            if (menuItemImpl2.requestsActionButton()) {
                int n12 = menuItemImpl2.getGroupId();
                boolean bl = menuItemImpl.get(n12);
                int n13 = !(n6 <= 0 && !bl || n3 <= 0 || this.mStrictWidthLimit && n2 <= 0) ? 1 : 0;
                if (n13 != 0) {
                    object = this.getItemView(menuItemImpl2, null, viewGroup);
                    if (this.mStrictWidthLimit) {
                        n10 = ActionMenuView.measureChildForCells((View)object, n5, n2, n7, 0);
                        n2 -= n10;
                        if (n10 == 0) {
                            n13 = 0;
                        }
                    } else {
                        object.measure(n7, n7);
                    }
                    n11 = object.getMeasuredWidth();
                    n3 -= n11;
                    n10 = n9;
                    if (n9 == 0) {
                        n10 = n11;
                    }
                    if (this.mStrictWidthLimit) {
                        n9 = n3 >= 0 ? 1 : 0;
                        n13 = n9 & n13;
                    } else {
                        n9 = n3 + n10 > 0 ? 1 : 0;
                        n13 = n9 & n13;
                    }
                } else {
                    n10 = n9;
                }
                n9 = n6;
                if (n13 != 0 && n12 != 0) {
                    menuItemImpl.put(n12, true);
                } else if (bl) {
                    menuItemImpl.put(n12, false);
                    for (n11 = 0; n11 < n; ++n11) {
                        object = arrayList.get(n11);
                        n6 = n9;
                        if (((MenuItemImpl)object).getGroupId() == n12) {
                            n6 = n9;
                            if (((MenuItemImpl)object).isActionButton()) {
                                n6 = n9 + 1;
                            }
                            ((MenuItemImpl)object).setIsActionButton(false);
                        }
                        n9 = n6;
                    }
                }
                n6 = n9;
                if (n13 != 0) {
                    n6 = n9 - 1;
                }
                menuItemImpl2.setIsActionButton(n13 != 0);
                n9 = n10;
                continue;
            }
            menuItemImpl2.setIsActionButton(false);
        }
        return true;
    }

    @Override
    public View getItemView(MenuItemImpl object, View view, ViewGroup viewGroup) {
        View view2 = ((MenuItemImpl)object).getActionView();
        if (view2 == null || ((MenuItemImpl)object).hasCollapsibleActionView()) {
            view2 = super.getItemView((MenuItemImpl)object, view, viewGroup);
        }
        int n = ((MenuItemImpl)object).isActionViewExpanded() ? 8 : 0;
        view2.setVisibility(n);
        object = (ActionMenuView)viewGroup;
        view = view2.getLayoutParams();
        if (!((ActionMenuView)object).checkLayoutParams((ViewGroup.LayoutParams)view)) {
            view2.setLayoutParams((ViewGroup.LayoutParams)((ActionMenuView)object).generateLayoutParams((ViewGroup.LayoutParams)view));
        }
        return view2;
    }

    @Override
    public MenuView getMenuView(ViewGroup object) {
        MenuView menuView = this.mMenuView;
        if (menuView != (object = super.getMenuView((ViewGroup)object))) {
            ((ActionMenuView)object).setPresenter(this);
        }
        return object;
    }

    public Drawable getOverflowIcon() {
        OverflowMenuButton overflowMenuButton = this.mOverflowButton;
        if (overflowMenuButton != null) {
            return overflowMenuButton.getDrawable();
        }
        if (this.mPendingOverflowIconSet) {
            return this.mPendingOverflowIcon;
        }
        return null;
    }

    public boolean hideOverflowMenu() {
        if (this.mPostedOpenRunnable != null && this.mMenuView != null) {
            ((View)this.mMenuView).removeCallbacks((Runnable)this.mPostedOpenRunnable);
            this.mPostedOpenRunnable = null;
            return true;
        }
        OverflowPopup overflowPopup = this.mOverflowPopup;
        if (overflowPopup != null) {
            overflowPopup.dismiss();
            return true;
        }
        return false;
    }

    public boolean hideSubMenus() {
        ActionButtonSubmenu actionButtonSubmenu = this.mActionButtonPopup;
        if (actionButtonSubmenu != null) {
            actionButtonSubmenu.dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void initForMenu(Context object, MenuBuilder menuBuilder) {
        super.initForMenu((Context)object, menuBuilder);
        menuBuilder = object.getResources();
        object = ActionBarPolicy.get((Context)object);
        if (!this.mReserveOverflowSet) {
            this.mReserveOverflow = ((ActionBarPolicy)object).showsOverflowMenuButton();
        }
        if (!this.mWidthLimitSet) {
            this.mWidthLimit = ((ActionBarPolicy)object).getEmbeddedMenuWidthLimit();
        }
        if (!this.mMaxItemsSet) {
            this.mMaxItems = ((ActionBarPolicy)object).getMaxActionButtons();
        }
        int n = this.mWidthLimit;
        if (this.mReserveOverflow) {
            if (this.mOverflowButton == null) {
                this.mOverflowButton = object = new OverflowMenuButton(this, this.mSystemContext);
                if (this.mPendingOverflowIconSet) {
                    ((AppCompatImageView)object).setImageDrawable(this.mPendingOverflowIcon);
                    this.mPendingOverflowIcon = null;
                    this.mPendingOverflowIconSet = false;
                }
                int n2 = View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
                this.mOverflowButton.measure(n2, n2);
            }
            n -= this.mOverflowButton.getMeasuredWidth();
        } else {
            this.mOverflowButton = null;
        }
        this.mActionItemWidthLimit = n;
        this.mMinCellSize = (int)(menuBuilder.getDisplayMetrics().density * 56.0f);
    }

    public boolean isOverflowMenuShowPending() {
        boolean bl = this.mPostedOpenRunnable != null || this.isOverflowMenuShowing();
        return bl;
    }

    public boolean isOverflowMenuShowing() {
        OverflowPopup overflowPopup = this.mOverflowPopup;
        boolean bl = overflowPopup != null && overflowPopup.isShowing();
        return bl;
    }

    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }

    @Override
    public void onCloseMenu(MenuBuilder menuBuilder, boolean bl) {
        this.dismissPopupMenus();
        super.onCloseMenu(menuBuilder, bl);
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (!this.mMaxItemsSet) {
            this.mMaxItems = ActionBarPolicy.get(this.mContext).getMaxActionButtons();
        }
        if (this.mMenu != null) {
            this.mMenu.onItemsChanged(true);
        }
    }

    @Override
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            return;
        }
        parcelable = (SavedState)parcelable;
        if (parcelable.openSubMenuId > 0 && (parcelable = this.mMenu.findItem(parcelable.openSubMenuId)) != null) {
            this.onSubMenuSelected((SubMenuBuilder)parcelable.getSubMenu());
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState();
        savedState.openSubMenuId = this.mOpenSubMenuId;
        return savedState;
    }

    @Override
    public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        boolean bl;
        if (!subMenuBuilder.hasVisibleItems()) {
            return false;
        }
        Object object = subMenuBuilder;
        while (((SubMenuBuilder)object).getParentMenu() != this.mMenu) {
            object = (SubMenuBuilder)((SubMenuBuilder)object).getParentMenu();
        }
        if ((object = this.findViewForItem(((SubMenuBuilder)object).getItem())) == null) {
            return false;
        }
        this.mOpenSubMenuId = subMenuBuilder.getItem().getItemId();
        boolean bl2 = false;
        int n = subMenuBuilder.size();
        int n2 = 0;
        while (true) {
            bl = bl2;
            if (n2 >= n) break;
            MenuItem menuItem = subMenuBuilder.getItem(n2);
            if (menuItem.isVisible() && menuItem.getIcon() != null) {
                bl = true;
                break;
            }
            ++n2;
        }
        this.mActionButtonPopup = object = new ActionButtonSubmenu(this, this.mContext, subMenuBuilder, (View)object);
        ((MenuPopupHelper)object).setForceShowIcon(bl);
        this.mActionButtonPopup.show();
        super.onSubMenuSelected(subMenuBuilder);
        return true;
    }

    @Override
    public void onSubUiVisibilityChanged(boolean bl) {
        if (bl) {
            super.onSubMenuSelected(null);
        } else if (this.mMenu != null) {
            this.mMenu.close(false);
        }
    }

    public void setExpandedActionViewsExclusive(boolean bl) {
        this.mExpandedActionViewsExclusive = bl;
    }

    public void setItemLimit(int n) {
        this.mMaxItems = n;
        this.mMaxItemsSet = true;
    }

    public void setMenuView(ActionMenuView actionMenuView) {
        this.mMenuView = actionMenuView;
        actionMenuView.initialize(this.mMenu);
    }

    public void setOverflowIcon(Drawable drawable2) {
        OverflowMenuButton overflowMenuButton = this.mOverflowButton;
        if (overflowMenuButton != null) {
            overflowMenuButton.setImageDrawable(drawable2);
        } else {
            this.mPendingOverflowIconSet = true;
            this.mPendingOverflowIcon = drawable2;
        }
    }

    public void setReserveOverflow(boolean bl) {
        this.mReserveOverflow = bl;
        this.mReserveOverflowSet = true;
    }

    public void setWidthLimit(int n, boolean bl) {
        this.mWidthLimit = n;
        this.mStrictWidthLimit = bl;
        this.mWidthLimitSet = true;
    }

    @Override
    public boolean shouldIncludeItem(int n, MenuItemImpl menuItemImpl) {
        return menuItemImpl.isActionButton();
    }

    public boolean showOverflowMenu() {
        if (this.mReserveOverflow && !this.isOverflowMenuShowing() && this.mMenu != null && this.mMenuView != null && this.mPostedOpenRunnable == null && !this.mMenu.getNonActionItems().isEmpty()) {
            this.mPostedOpenRunnable = new OpenOverflowRunnable(this, new OverflowPopup(this, this.mContext, this.mMenu, (View)this.mOverflowButton, true));
            ((View)this.mMenuView).post((Runnable)this.mPostedOpenRunnable);
            return true;
        }
        return false;
    }

    @Override
    public void updateMenuView(boolean bl) {
        int n;
        int n2;
        Object object;
        super.updateMenuView(bl);
        ((View)this.mMenuView).requestLayout();
        if (this.mMenu != null) {
            object = this.mMenu.getActionItems();
            n2 = ((ArrayList)object).size();
            for (n = 0; n < n2; ++n) {
                ActionProvider actionProvider = ((ArrayList)object).get(n).getSupportActionProvider();
                if (actionProvider == null) continue;
                actionProvider.setSubUiVisibilityListener(this);
            }
        }
        object = this.mMenu != null ? this.mMenu.getNonActionItems() : null;
        n = n2 = 0;
        if (this.mReserveOverflow) {
            n = n2;
            if (object != null) {
                n2 = ((ArrayList)object).size();
                n = 0;
                if (n2 == 1) {
                    n = ((MenuItemImpl)((ArrayList)object).get(0)).isActionViewExpanded() ^ 1;
                } else if (n2 > 0) {
                    n = 1;
                }
            }
        }
        if (n != 0) {
            if (this.mOverflowButton == null) {
                this.mOverflowButton = new OverflowMenuButton(this, this.mSystemContext);
            }
            if ((object = (ViewGroup)this.mOverflowButton.getParent()) != this.mMenuView) {
                if (object != null) {
                    object.removeView((View)this.mOverflowButton);
                }
                object = (ActionMenuView)this.mMenuView;
                object.addView((View)this.mOverflowButton, (ViewGroup.LayoutParams)((ActionMenuView)object).generateOverflowButtonLayoutParams());
            }
        } else {
            object = this.mOverflowButton;
            if (object != null && object.getParent() == this.mMenuView) {
                ((ViewGroup)this.mMenuView).removeView((View)this.mOverflowButton);
            }
        }
        ((ActionMenuView)this.mMenuView).setOverflowReserved(this.mReserveOverflow);
    }

    private class ActionButtonSubmenu
    extends MenuPopupHelper {
        final ActionMenuPresenter this$0;

        public ActionButtonSubmenu(ActionMenuPresenter actionMenuPresenter, Context object, SubMenuBuilder subMenuBuilder, View view) {
            this.this$0 = actionMenuPresenter;
            super((Context)object, subMenuBuilder, view, false, R.attr.actionOverflowMenuStyle);
            if (!((MenuItemImpl)subMenuBuilder.getItem()).isActionButton()) {
                object = actionMenuPresenter.mOverflowButton == null ? (View)actionMenuPresenter.mMenuView : actionMenuPresenter.mOverflowButton;
                this.setAnchorView((View)object);
            }
            this.setPresenterCallback(actionMenuPresenter.mPopupPresenterCallback);
        }

        @Override
        protected void onDismiss() {
            this.this$0.mActionButtonPopup = null;
            this.this$0.mOpenSubMenuId = 0;
            super.onDismiss();
        }
    }

    private class ActionMenuPopupCallback
    extends ActionMenuItemView.PopupCallback {
        final ActionMenuPresenter this$0;

        ActionMenuPopupCallback(ActionMenuPresenter actionMenuPresenter) {
            this.this$0 = actionMenuPresenter;
        }

        @Override
        public ShowableListMenu getPopup() {
            MenuPopup menuPopup = this.this$0.mActionButtonPopup != null ? this.this$0.mActionButtonPopup.getPopup() : null;
            return menuPopup;
        }
    }

    private class OpenOverflowRunnable
    implements Runnable {
        private OverflowPopup mPopup;
        final ActionMenuPresenter this$0;

        public OpenOverflowRunnable(ActionMenuPresenter actionMenuPresenter, OverflowPopup overflowPopup) {
            this.this$0 = actionMenuPresenter;
            this.mPopup = overflowPopup;
        }

        @Override
        public void run() {
            View view;
            if (this.this$0.mMenu != null) {
                this.this$0.mMenu.changeMenuMode();
            }
            if ((view = (View)this.this$0.mMenuView) != null && view.getWindowToken() != null && this.mPopup.tryShow()) {
                this.this$0.mOverflowPopup = this.mPopup;
            }
            this.this$0.mPostedOpenRunnable = null;
        }
    }

    private class OverflowMenuButton
    extends AppCompatImageView
    implements ActionMenuView.ActionMenuChildView {
        final ActionMenuPresenter this$0;

        public OverflowMenuButton(ActionMenuPresenter actionMenuPresenter, Context context) {
            this.this$0 = actionMenuPresenter;
            super(context, null, R.attr.actionOverflowButtonStyle);
            this.setClickable(true);
            this.setFocusable(true);
            this.setVisibility(0);
            this.setEnabled(true);
            TooltipCompat.setTooltipText((View)this, this.getContentDescription());
            this.setOnTouchListener(new ForwardingListener(this, (View)this, actionMenuPresenter){
                final OverflowMenuButton this$1;
                final ActionMenuPresenter val$this$0;
                {
                    this.this$1 = overflowMenuButton;
                    this.val$this$0 = actionMenuPresenter;
                    super(view);
                }

                @Override
                public ShowableListMenu getPopup() {
                    if (this.this$1.this$0.mOverflowPopup == null) {
                        return null;
                    }
                    return this.this$1.this$0.mOverflowPopup.getPopup();
                }

                @Override
                public boolean onForwardingStarted() {
                    this.this$1.this$0.showOverflowMenu();
                    return true;
                }

                @Override
                public boolean onForwardingStopped() {
                    if (this.this$1.this$0.mPostedOpenRunnable != null) {
                        return false;
                    }
                    this.this$1.this$0.hideOverflowMenu();
                    return true;
                }
            });
        }

        @Override
        public boolean needsDividerAfter() {
            return false;
        }

        @Override
        public boolean needsDividerBefore() {
            return false;
        }

        public boolean performClick() {
            if (super.performClick()) {
                return true;
            }
            this.playSoundEffect(0);
            this.this$0.showOverflowMenu();
            return true;
        }

        protected boolean setFrame(int n, int n2, int n3, int n4) {
            boolean bl = super.setFrame(n, n2, n3, n4);
            Drawable drawable2 = this.getDrawable();
            Drawable drawable3 = this.getBackground();
            if (drawable2 != null && drawable3 != null) {
                int n5 = this.getWidth();
                n2 = this.getHeight();
                n = Math.max(n5, n2) / 2;
                int n6 = this.getPaddingLeft();
                int n7 = this.getPaddingRight();
                n4 = this.getPaddingTop();
                n3 = this.getPaddingBottom();
                n7 = (n5 + (n6 - n7)) / 2;
                n2 = (n2 + (n4 - n3)) / 2;
                DrawableCompat.setHotspotBounds(drawable3, n7 - n, n2 - n, n7 + n, n2 + n);
            }
            return bl;
        }
    }

    private class OverflowPopup
    extends MenuPopupHelper {
        final ActionMenuPresenter this$0;

        public OverflowPopup(ActionMenuPresenter actionMenuPresenter, Context context, MenuBuilder menuBuilder, View view, boolean bl) {
            this.this$0 = actionMenuPresenter;
            super(context, menuBuilder, view, bl, R.attr.actionOverflowMenuStyle);
            this.setGravity(0x800005);
            this.setPresenterCallback(actionMenuPresenter.mPopupPresenterCallback);
        }

        @Override
        protected void onDismiss() {
            if (this.this$0.mMenu != null) {
                this.this$0.mMenu.close();
            }
            this.this$0.mOverflowPopup = null;
            super.onDismiss();
        }
    }

    private class PopupPresenterCallback
    implements MenuPresenter.Callback {
        final ActionMenuPresenter this$0;

        PopupPresenterCallback(ActionMenuPresenter actionMenuPresenter) {
            this.this$0 = actionMenuPresenter;
        }

        @Override
        public void onCloseMenu(MenuBuilder menuBuilder, boolean bl) {
            MenuPresenter.Callback callback;
            if (menuBuilder instanceof SubMenuBuilder) {
                menuBuilder.getRootMenu().close(false);
            }
            if ((callback = this.this$0.getCallback()) != null) {
                callback.onCloseMenu(menuBuilder, bl);
            }
        }

        @Override
        public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            Object object = this.this$0.mMenu;
            boolean bl = false;
            if (menuBuilder == object) {
                return false;
            }
            this.this$0.mOpenSubMenuId = ((SubMenuBuilder)menuBuilder).getItem().getItemId();
            object = this.this$0.getCallback();
            if (object != null) {
                bl = object.onOpenSubMenu(menuBuilder);
            }
            return bl;
        }
    }

    private static class SavedState
    implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        public int openSubMenuId;

        SavedState() {
        }

        SavedState(Parcel parcel) {
            this.openSubMenuId = parcel.readInt();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int n) {
            parcel.writeInt(this.openSubMenuId);
        }
    }
}

