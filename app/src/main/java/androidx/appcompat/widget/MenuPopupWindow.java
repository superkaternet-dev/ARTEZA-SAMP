/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build$VERSION
 *  android.transition.Transition
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.view.KeyEvent
 *  android.view.MenuItem
 *  android.view.MotionEvent
 *  android.view.View
 *  android.widget.HeaderViewListAdapter
 *  android.widget.PopupWindow
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.os.Build;
import android.transition.Transition;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HeaderViewListAdapter;
import android.widget.PopupWindow;
import androidx.appcompat.view.menu.ListMenuItemView;
import androidx.appcompat.view.menu.MenuAdapter;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.DropDownListView;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.appcompat.widget.MenuItemHoverListener;
import java.lang.reflect.Method;

public class MenuPopupWindow
extends ListPopupWindow
implements MenuItemHoverListener {
    private static final String TAG = "MenuPopupWindow";
    private static Method sSetTouchModalMethod;
    private MenuItemHoverListener mHoverListener;

    static {
        try {
            if (Build.VERSION.SDK_INT <= 28) {
                sSetTouchModalMethod = PopupWindow.class.getDeclaredMethod("setTouchModal", Boolean.TYPE);
            }
        }
        catch (NoSuchMethodException noSuchMethodException) {
            Log.i((String)TAG, (String)"Could not find method setTouchModal() on PopupWindow. Oh well.");
        }
    }

    public MenuPopupWindow(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
    }

    @Override
    DropDownListView createDropDownListView(Context object, boolean bl) {
        object = new MenuDropDownListView((Context)object, bl);
        ((MenuDropDownListView)((Object)object)).setHoverListener(this);
        return object;
    }

    @Override
    public void onItemHoverEnter(MenuBuilder menuBuilder, MenuItem menuItem) {
        MenuItemHoverListener menuItemHoverListener = this.mHoverListener;
        if (menuItemHoverListener != null) {
            menuItemHoverListener.onItemHoverEnter(menuBuilder, menuItem);
        }
    }

    @Override
    public void onItemHoverExit(MenuBuilder menuBuilder, MenuItem menuItem) {
        MenuItemHoverListener menuItemHoverListener = this.mHoverListener;
        if (menuItemHoverListener != null) {
            menuItemHoverListener.onItemHoverExit(menuBuilder, menuItem);
        }
    }

    public void setEnterTransition(Object object) {
        if (Build.VERSION.SDK_INT >= 23) {
            this.mPopup.setEnterTransition((Transition)object);
        }
    }

    public void setExitTransition(Object object) {
        if (Build.VERSION.SDK_INT >= 23) {
            this.mPopup.setExitTransition((Transition)object);
        }
    }

    public void setHoverListener(MenuItemHoverListener menuItemHoverListener) {
        this.mHoverListener = menuItemHoverListener;
    }

    public void setTouchModal(boolean bl) {
        if (Build.VERSION.SDK_INT <= 28) {
            Method method = sSetTouchModalMethod;
            if (method != null) {
                try {
                    method.invoke((Object)this.mPopup, bl);
                }
                catch (Exception exception) {
                    Log.i((String)TAG, (String)"Could not invoke setTouchModal() on PopupWindow. Oh well.");
                }
            }
        } else {
            this.mPopup.setTouchModal(bl);
        }
    }

    public static class MenuDropDownListView
    extends DropDownListView {
        final int mAdvanceKey;
        private MenuItemHoverListener mHoverListener;
        private MenuItem mHoveredMenuItem;
        final int mRetreatKey;

        public MenuDropDownListView(Context context, boolean bl) {
            super(context, bl);
            context = context.getResources().getConfiguration();
            if (Build.VERSION.SDK_INT >= 17 && 1 == context.getLayoutDirection()) {
                this.mAdvanceKey = 21;
                this.mRetreatKey = 22;
            } else {
                this.mAdvanceKey = 22;
                this.mRetreatKey = 21;
            }
        }

        public void clearSelection() {
            this.setSelection(-1);
        }

        @Override
        public boolean onHoverEvent(MotionEvent motionEvent) {
            if (this.mHoverListener != null) {
                MenuItem menuItem;
                int n;
                Object object = this.getAdapter();
                if (object instanceof HeaderViewListAdapter) {
                    object = (HeaderViewListAdapter)object;
                    n = object.getHeadersCount();
                    object = (MenuAdapter)object.getWrappedAdapter();
                } else {
                    n = 0;
                    object = (MenuAdapter)((Object)object);
                }
                MenuItem menuItem2 = menuItem = null;
                if (motionEvent.getAction() != 10) {
                    int n2 = this.pointToPosition((int)motionEvent.getX(), (int)motionEvent.getY());
                    menuItem2 = menuItem;
                    if (n2 != -1) {
                        n = n2 - n;
                        menuItem2 = menuItem;
                        if (n >= 0) {
                            menuItem2 = menuItem;
                            if (n < ((MenuAdapter)((Object)object)).getCount()) {
                                menuItem2 = ((MenuAdapter)((Object)object)).getItem(n);
                            }
                        }
                    }
                }
                if ((menuItem = this.mHoveredMenuItem) != menuItem2) {
                    object = ((MenuAdapter)((Object)object)).getAdapterMenu();
                    if (menuItem != null) {
                        this.mHoverListener.onItemHoverExit((MenuBuilder)object, menuItem);
                    }
                    this.mHoveredMenuItem = menuItem2;
                    if (menuItem2 != null) {
                        this.mHoverListener.onItemHoverEnter((MenuBuilder)object, menuItem2);
                    }
                }
            }
            return super.onHoverEvent(motionEvent);
        }

        public boolean onKeyDown(int n, KeyEvent keyEvent) {
            ListMenuItemView listMenuItemView = (ListMenuItemView)this.getSelectedView();
            if (listMenuItemView != null && n == this.mAdvanceKey) {
                if (listMenuItemView.isEnabled() && listMenuItemView.getItemData().hasSubMenu()) {
                    this.performItemClick((View)listMenuItemView, this.getSelectedItemPosition(), this.getSelectedItemId());
                }
                return true;
            }
            if (listMenuItemView != null && n == this.mRetreatKey) {
                this.setSelection(-1);
                ((MenuAdapter)this.getAdapter()).getAdapterMenu().close(false);
                return true;
            }
            return super.onKeyDown(n, keyEvent);
        }

        public void setHoverListener(MenuItemHoverListener menuItemHoverListener) {
            this.mHoverListener = menuItemHoverListener;
        }
    }
}

