/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Rect
 *  android.os.Build$VERSION
 *  android.os.Handler
 *  android.os.Parcelable
 *  android.os.SystemClock
 *  android.view.KeyEvent
 *  android.view.LayoutInflater
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.View$OnAttachStateChangeListener
 *  android.view.View$OnKeyListener
 *  android.view.ViewGroup
 *  android.view.ViewTreeObserver
 *  android.view.ViewTreeObserver$OnGlobalLayoutListener
 *  android.widget.FrameLayout
 *  android.widget.HeaderViewListAdapter
 *  android.widget.ListAdapter
 *  android.widget.ListView
 *  android.widget.PopupWindow$OnDismissListener
 *  android.widget.TextView
 */
package androidx.appcompat.view.menu;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.appcompat.view.menu.MenuAdapter;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopup;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.SubMenuBuilder;
import androidx.appcompat.widget.MenuItemHoverListener;
import androidx.appcompat.widget.MenuPopupWindow;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class CascadingMenuPopup
extends MenuPopup
implements MenuPresenter,
View.OnKeyListener,
PopupWindow.OnDismissListener {
    static final int HORIZ_POSITION_LEFT = 0;
    static final int HORIZ_POSITION_RIGHT = 1;
    private static final int ITEM_LAYOUT = R.layout.abc_cascading_menu_item_layout;
    static final int SUBMENU_TIMEOUT_MS = 200;
    private View mAnchorView;
    private final View.OnAttachStateChangeListener mAttachStateChangeListener;
    private final Context mContext;
    private int mDropDownGravity = 0;
    private boolean mForceShowIcon;
    final ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener;
    private boolean mHasXOffset;
    private boolean mHasYOffset;
    private int mLastPosition;
    private final MenuItemHoverListener mMenuItemHoverListener;
    private final int mMenuMaxWidth;
    private PopupWindow.OnDismissListener mOnDismissListener;
    private final boolean mOverflowOnly;
    private final List<MenuBuilder> mPendingMenus = new ArrayList<MenuBuilder>();
    private final int mPopupStyleAttr;
    private final int mPopupStyleRes;
    private MenuPresenter.Callback mPresenterCallback;
    private int mRawDropDownGravity = 0;
    boolean mShouldCloseImmediately;
    private boolean mShowTitle;
    final List<CascadingMenuInfo> mShowingMenus = new ArrayList<CascadingMenuInfo>();
    View mShownAnchorView;
    final Handler mSubMenuHoverHandler;
    ViewTreeObserver mTreeObserver;
    private int mXOffset;
    private int mYOffset;

    public CascadingMenuPopup(Context context, View view, int n, int n2, boolean bl) {
        this.mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener(this){
            final CascadingMenuPopup this$0;
            {
                this.this$0 = cascadingMenuPopup;
            }

            public void onGlobalLayout() {
                if (this.this$0.isShowing() && this.this$0.mShowingMenus.size() > 0 && !this.this$0.mShowingMenus.get((int)0).window.isModal()) {
                    Object object = this.this$0.mShownAnchorView;
                    if (object != null && object.isShown()) {
                        object = this.this$0.mShowingMenus.iterator();
                        while (object.hasNext()) {
                            ((CascadingMenuInfo)object.next()).window.show();
                        }
                    } else {
                        this.this$0.dismiss();
                    }
                }
            }
        };
        this.mAttachStateChangeListener = new View.OnAttachStateChangeListener(this){
            final CascadingMenuPopup this$0;
            {
                this.this$0 = cascadingMenuPopup;
            }

            public void onViewAttachedToWindow(View view) {
            }

            public void onViewDetachedFromWindow(View view) {
                if (this.this$0.mTreeObserver != null) {
                    if (!this.this$0.mTreeObserver.isAlive()) {
                        this.this$0.mTreeObserver = view.getViewTreeObserver();
                    }
                    this.this$0.mTreeObserver.removeGlobalOnLayoutListener(this.this$0.mGlobalLayoutListener);
                }
                view.removeOnAttachStateChangeListener((View.OnAttachStateChangeListener)this);
            }
        };
        this.mMenuItemHoverListener = new MenuItemHoverListener(this){
            final CascadingMenuPopup this$0;
            {
                this.this$0 = cascadingMenuPopup;
            }

            @Override
            public void onItemHoverEnter(MenuBuilder menuBuilder, MenuItem object) {
                int n;
                this.this$0.mSubMenuHoverHandler.removeCallbacksAndMessages(null);
                int n2 = -1;
                int n3 = 0;
                int n4 = this.this$0.mShowingMenus.size();
                while (true) {
                    n = n2;
                    if (n3 >= n4) break;
                    if (menuBuilder == this.this$0.mShowingMenus.get((int)n3).menu) {
                        n = n3;
                        break;
                    }
                    ++n3;
                }
                if (n == -1) {
                    return;
                }
                n3 = n + 1;
                CascadingMenuInfo cascadingMenuInfo = n3 < this.this$0.mShowingMenus.size() ? this.this$0.mShowingMenus.get(n3) : null;
                object = new Runnable(this, cascadingMenuInfo, (MenuItem)object, menuBuilder){
                    final 3 this$1;
                    final MenuItem val$item;
                    final MenuBuilder val$menu;
                    final CascadingMenuInfo val$nextInfo;
                    {
                        this.this$1 = var1_1;
                        this.val$nextInfo = cascadingMenuInfo;
                        this.val$item = menuItem;
                        this.val$menu = menuBuilder;
                    }

                    @Override
                    public void run() {
                        if (this.val$nextInfo != null) {
                            this.this$1.this$0.mShouldCloseImmediately = true;
                            this.val$nextInfo.menu.close(false);
                            this.this$1.this$0.mShouldCloseImmediately = false;
                        }
                        if (this.val$item.isEnabled() && this.val$item.hasSubMenu()) {
                            this.val$menu.performItemAction(this.val$item, 4);
                        }
                    }
                };
                long l = SystemClock.uptimeMillis();
                this.this$0.mSubMenuHoverHandler.postAtTime((Runnable)object, (Object)menuBuilder, l + 200L);
            }

            @Override
            public void onItemHoverExit(MenuBuilder menuBuilder, MenuItem menuItem) {
                this.this$0.mSubMenuHoverHandler.removeCallbacksAndMessages((Object)menuBuilder);
            }
        };
        this.mContext = context;
        this.mAnchorView = view;
        this.mPopupStyleAttr = n;
        this.mPopupStyleRes = n2;
        this.mOverflowOnly = bl;
        this.mForceShowIcon = false;
        this.mLastPosition = this.getInitialMenuPosition();
        context = context.getResources();
        this.mMenuMaxWidth = Math.max(context.getDisplayMetrics().widthPixels / 2, context.getDimensionPixelSize(R.dimen.abc_config_prefDialogWidth));
        this.mSubMenuHoverHandler = new Handler();
    }

    private MenuPopupWindow createPopupWindow() {
        MenuPopupWindow menuPopupWindow = new MenuPopupWindow(this.mContext, null, this.mPopupStyleAttr, this.mPopupStyleRes);
        menuPopupWindow.setHoverListener(this.mMenuItemHoverListener);
        menuPopupWindow.setOnItemClickListener(this);
        menuPopupWindow.setOnDismissListener(this);
        menuPopupWindow.setAnchorView(this.mAnchorView);
        menuPopupWindow.setDropDownGravity(this.mDropDownGravity);
        menuPopupWindow.setModal(true);
        menuPopupWindow.setInputMethodMode(2);
        return menuPopupWindow;
    }

    private int findIndexOfAddedMenu(MenuBuilder menuBuilder) {
        int n = this.mShowingMenus.size();
        for (int i = 0; i < n; ++i) {
            if (menuBuilder != this.mShowingMenus.get((int)i).menu) continue;
            return i;
        }
        return -1;
    }

    private MenuItem findMenuItemForSubmenu(MenuBuilder menuBuilder, MenuBuilder menuBuilder2) {
        int n = menuBuilder.size();
        for (int i = 0; i < n; ++i) {
            MenuItem menuItem = menuBuilder.getItem(i);
            if (!menuItem.hasSubMenu() || menuBuilder2 != menuItem.getSubMenu()) continue;
            return menuItem;
        }
        return null;
    }

    private View findParentViewForSubmenu(CascadingMenuInfo object, MenuBuilder menuBuilder) {
        int n;
        int n2;
        if ((menuBuilder = this.findMenuItemForSubmenu(((CascadingMenuInfo)object).menu, menuBuilder)) == null) {
            return null;
        }
        ListView listView = ((CascadingMenuInfo)object).getListView();
        if ((object = listView.getAdapter()) instanceof HeaderViewListAdapter) {
            object = (HeaderViewListAdapter)object;
            n2 = object.getHeadersCount();
            object = (MenuAdapter)object.getWrappedAdapter();
        } else {
            n2 = 0;
            object = (MenuAdapter)((Object)object);
        }
        int n3 = -1;
        int n4 = 0;
        int n5 = ((MenuAdapter)((Object)object)).getCount();
        while (true) {
            n = n3;
            if (n4 >= n5) break;
            if (menuBuilder == ((MenuAdapter)((Object)object)).getItem(n4)) {
                n = n4;
                break;
            }
            ++n4;
        }
        if (n == -1) {
            return null;
        }
        n4 = n + n2 - listView.getFirstVisiblePosition();
        if (n4 >= 0 && n4 < listView.getChildCount()) {
            return listView.getChildAt(n4);
        }
        return null;
    }

    private int getInitialMenuPosition() {
        int n;
        block0: {
            int n2 = ViewCompat.getLayoutDirection(this.mAnchorView);
            n = 1;
            if (n2 != 1) break block0;
            n = 0;
        }
        return n;
    }

    private int getNextMenuPosition(int n) {
        Object object = this.mShowingMenus;
        ListView listView = object.get(object.size() - 1).getListView();
        object = new int[2];
        listView.getLocationOnScreen((int[])object);
        Rect rect = new Rect();
        this.mShownAnchorView.getWindowVisibleDisplayFrame(rect);
        if (this.mLastPosition == 1) {
            if (object[0] + listView.getWidth() + n > rect.right) {
                return 0;
            }
            return 1;
        }
        if (object[0] - n < 0) {
            return 1;
        }
        return 0;
    }

    private void showMenu(MenuBuilder menuBuilder) {
        Object object;
        LayoutInflater layoutInflater = LayoutInflater.from((Context)this.mContext);
        Object object2 = new MenuAdapter(menuBuilder, layoutInflater, this.mOverflowOnly, ITEM_LAYOUT);
        if (!this.isShowing() && this.mForceShowIcon) {
            ((MenuAdapter)((Object)object2)).setForceShowIcon(true);
        } else if (this.isShowing()) {
            ((MenuAdapter)((Object)object2)).setForceShowIcon(MenuPopup.shouldPreserveIconSpacing(menuBuilder));
        }
        int n = CascadingMenuPopup.measureIndividualMenuWidth((ListAdapter)object2, null, this.mContext, this.mMenuMaxWidth);
        MenuPopupWindow menuPopupWindow = this.createPopupWindow();
        menuPopupWindow.setAdapter((ListAdapter)object2);
        menuPopupWindow.setContentWidth(n);
        menuPopupWindow.setDropDownGravity(this.mDropDownGravity);
        if (this.mShowingMenus.size() > 0) {
            object2 = this.mShowingMenus;
            object2 = (CascadingMenuInfo)object2.get(object2.size() - 1);
            object = this.findParentViewForSubmenu((CascadingMenuInfo)object2, menuBuilder);
        } else {
            object2 = null;
            object = null;
        }
        if (object != null) {
            int n2;
            menuPopupWindow.setTouchModal(false);
            menuPopupWindow.setEnterTransition(null);
            int n3 = this.getNextMenuPosition(n);
            int n4 = n3 == 1 ? 1 : 0;
            this.mLastPosition = n3;
            if (Build.VERSION.SDK_INT >= 26) {
                menuPopupWindow.setAnchorView((View)object);
                n2 = 0;
                n3 = 0;
            } else {
                int[] nArray = new int[2];
                this.mAnchorView.getLocationOnScreen(nArray);
                int[] nArray2 = new int[2];
                object.getLocationOnScreen(nArray2);
                if ((this.mDropDownGravity & 7) == 5) {
                    nArray[0] = nArray[0] + this.mAnchorView.getWidth();
                    nArray2[0] = nArray2[0] + object.getWidth();
                }
                n2 = nArray2[0] - nArray[0];
                n3 = nArray2[1] - nArray[1];
            }
            n4 = (this.mDropDownGravity & 5) == 5 ? (n4 != 0 ? n2 + n : n2 - object.getWidth()) : (n4 != 0 ? object.getWidth() + n2 : n2 - n);
            menuPopupWindow.setHorizontalOffset(n4);
            menuPopupWindow.setOverlapAnchor(true);
            menuPopupWindow.setVerticalOffset(n3);
        } else {
            if (this.mHasXOffset) {
                menuPopupWindow.setHorizontalOffset(this.mXOffset);
            }
            if (this.mHasYOffset) {
                menuPopupWindow.setVerticalOffset(this.mYOffset);
            }
            menuPopupWindow.setEpicenterBounds(this.getEpicenterBounds());
        }
        object = new CascadingMenuInfo(menuPopupWindow, menuBuilder, this.mLastPosition);
        this.mShowingMenus.add((CascadingMenuInfo)object);
        menuPopupWindow.show();
        object = menuPopupWindow.getListView();
        object.setOnKeyListener((View.OnKeyListener)this);
        if (object2 == null && this.mShowTitle && menuBuilder.getHeaderTitle() != null) {
            layoutInflater = (FrameLayout)layoutInflater.inflate(R.layout.abc_popup_menu_header_item_layout, (ViewGroup)object, false);
            object2 = (TextView)layoutInflater.findViewById(16908310);
            layoutInflater.setEnabled(false);
            object2.setText(menuBuilder.getHeaderTitle());
            object.addHeaderView((View)layoutInflater, null, false);
            menuPopupWindow.show();
        }
    }

    @Override
    public void addMenu(MenuBuilder menuBuilder) {
        menuBuilder.addMenuPresenter(this, this.mContext);
        if (this.isShowing()) {
            this.showMenu(menuBuilder);
        } else {
            this.mPendingMenus.add(menuBuilder);
        }
    }

    @Override
    protected boolean closeMenuOnSubMenuOpened() {
        return false;
    }

    @Override
    public void dismiss() {
        int n = this.mShowingMenus.size();
        if (n > 0) {
            CascadingMenuInfo[] cascadingMenuInfoArray = this.mShowingMenus.toArray(new CascadingMenuInfo[n]);
            --n;
            while (n >= 0) {
                CascadingMenuInfo cascadingMenuInfo = cascadingMenuInfoArray[n];
                if (cascadingMenuInfo.window.isShowing()) {
                    cascadingMenuInfo.window.dismiss();
                }
                --n;
            }
        }
    }

    @Override
    public boolean flagActionItems() {
        return false;
    }

    @Override
    public ListView getListView() {
        ListView listView;
        if (this.mShowingMenus.isEmpty()) {
            listView = null;
        } else {
            listView = this.mShowingMenus;
            listView = listView.get(listView.size() - 1).getListView();
        }
        return listView;
    }

    @Override
    public boolean isShowing() {
        boolean bl;
        int n = this.mShowingMenus.size();
        boolean bl2 = bl = false;
        if (n > 0) {
            bl2 = bl;
            if (this.mShowingMenus.get((int)0).window.isShowing()) {
                bl2 = true;
            }
        }
        return bl2;
    }

    @Override
    public void onCloseMenu(MenuBuilder menuBuilder, boolean bl) {
        int n = this.findIndexOfAddedMenu(menuBuilder);
        if (n < 0) {
            return;
        }
        int n2 = n + 1;
        if (n2 < this.mShowingMenus.size()) {
            this.mShowingMenus.get((int)n2).menu.close(false);
        }
        Object object = this.mShowingMenus.remove(n);
        ((CascadingMenuInfo)object).menu.removeMenuPresenter(this);
        if (this.mShouldCloseImmediately) {
            ((CascadingMenuInfo)object).window.setExitTransition(null);
            ((CascadingMenuInfo)object).window.setAnimationStyle(0);
        }
        ((CascadingMenuInfo)object).window.dismiss();
        n = this.mShowingMenus.size();
        this.mLastPosition = n > 0 ? this.mShowingMenus.get((int)(n - 1)).position : this.getInitialMenuPosition();
        if (n == 0) {
            this.dismiss();
            object = this.mPresenterCallback;
            if (object != null) {
                object.onCloseMenu(menuBuilder, true);
            }
            if ((menuBuilder = this.mTreeObserver) != null) {
                if (menuBuilder.isAlive()) {
                    this.mTreeObserver.removeGlobalOnLayoutListener(this.mGlobalLayoutListener);
                }
                this.mTreeObserver = null;
            }
            this.mShownAnchorView.removeOnAttachStateChangeListener(this.mAttachStateChangeListener);
            this.mOnDismissListener.onDismiss();
        } else if (bl) {
            this.mShowingMenus.get((int)0).menu.close(false);
        }
    }

    public void onDismiss() {
        CascadingMenuInfo cascadingMenuInfo;
        CascadingMenuInfo cascadingMenuInfo2 = null;
        int n = 0;
        int n2 = this.mShowingMenus.size();
        while (true) {
            cascadingMenuInfo = cascadingMenuInfo2;
            if (n >= n2) break;
            cascadingMenuInfo = this.mShowingMenus.get(n);
            if (!cascadingMenuInfo.window.isShowing()) break;
            ++n;
        }
        if (cascadingMenuInfo != null) {
            cascadingMenuInfo.menu.close(false);
        }
    }

    public boolean onKey(View view, int n, KeyEvent keyEvent) {
        if (keyEvent.getAction() == 1 && n == 82) {
            this.dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void onRestoreInstanceState(Parcelable parcelable) {
    }

    @Override
    public Parcelable onSaveInstanceState() {
        return null;
    }

    @Override
    public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        for (CascadingMenuInfo cascadingMenuInfo : this.mShowingMenus) {
            if (subMenuBuilder != cascadingMenuInfo.menu) continue;
            cascadingMenuInfo.getListView().requestFocus();
            return true;
        }
        if (subMenuBuilder.hasVisibleItems()) {
            this.addMenu(subMenuBuilder);
            MenuPresenter.Callback callback = this.mPresenterCallback;
            if (callback != null) {
                callback.onOpenSubMenu(subMenuBuilder);
            }
            return true;
        }
        return false;
    }

    @Override
    public void setAnchorView(View view) {
        if (this.mAnchorView != view) {
            this.mAnchorView = view;
            this.mDropDownGravity = GravityCompat.getAbsoluteGravity(this.mRawDropDownGravity, ViewCompat.getLayoutDirection(view));
        }
    }

    @Override
    public void setCallback(MenuPresenter.Callback callback) {
        this.mPresenterCallback = callback;
    }

    @Override
    public void setForceShowIcon(boolean bl) {
        this.mForceShowIcon = bl;
    }

    @Override
    public void setGravity(int n) {
        if (this.mRawDropDownGravity != n) {
            this.mRawDropDownGravity = n;
            this.mDropDownGravity = GravityCompat.getAbsoluteGravity(n, ViewCompat.getLayoutDirection(this.mAnchorView));
        }
    }

    @Override
    public void setHorizontalOffset(int n) {
        this.mHasXOffset = true;
        this.mXOffset = n;
    }

    @Override
    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    @Override
    public void setShowTitle(boolean bl) {
        this.mShowTitle = bl;
    }

    @Override
    public void setVerticalOffset(int n) {
        this.mHasYOffset = true;
        this.mYOffset = n;
    }

    @Override
    public void show() {
        if (this.isShowing()) {
            return;
        }
        View view = this.mPendingMenus.iterator();
        while (view.hasNext()) {
            this.showMenu(view.next());
        }
        this.mPendingMenus.clear();
        this.mShownAnchorView = view = this.mAnchorView;
        if (view != null) {
            boolean bl = this.mTreeObserver == null;
            view = view.getViewTreeObserver();
            this.mTreeObserver = view;
            if (bl) {
                view.addOnGlobalLayoutListener(this.mGlobalLayoutListener);
            }
            this.mShownAnchorView.addOnAttachStateChangeListener(this.mAttachStateChangeListener);
        }
    }

    @Override
    public void updateMenuView(boolean bl) {
        Iterator<CascadingMenuInfo> iterator2 = this.mShowingMenus.iterator();
        while (iterator2.hasNext()) {
            CascadingMenuPopup.toMenuAdapter(iterator2.next().getListView().getAdapter()).notifyDataSetChanged();
        }
    }

    private static class CascadingMenuInfo {
        public final MenuBuilder menu;
        public final int position;
        public final MenuPopupWindow window;

        public CascadingMenuInfo(MenuPopupWindow menuPopupWindow, MenuBuilder menuBuilder, int n) {
            this.window = menuPopupWindow;
            this.menu = menuBuilder;
            this.position = n;
        }

        public ListView getListView() {
            return this.window.getListView();
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface HorizPosition {
    }
}

