/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.View$OnTouchListener
 *  android.widget.ListView
 *  android.widget.PopupWindow$OnDismissListener
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;
import androidx.appcompat.R;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.view.menu.ShowableListMenu;
import androidx.appcompat.widget.ForwardingListener;

public class PopupMenu {
    private final View mAnchor;
    private final Context mContext;
    private View.OnTouchListener mDragListener;
    private final MenuBuilder mMenu;
    OnMenuItemClickListener mMenuItemClickListener;
    OnDismissListener mOnDismissListener;
    final MenuPopupHelper mPopup;

    public PopupMenu(Context context, View view) {
        this(context, view, 0);
    }

    public PopupMenu(Context context, View view, int n) {
        this(context, view, n, R.attr.popupMenuStyle, 0);
    }

    public PopupMenu(Context object, View view, int n, int n2, int n3) {
        MenuBuilder menuBuilder;
        this.mContext = object;
        this.mAnchor = view;
        this.mMenu = menuBuilder = new MenuBuilder((Context)object);
        menuBuilder.setCallback(new MenuBuilder.Callback(this){
            final PopupMenu this$0;
            {
                this.this$0 = popupMenu;
            }

            @Override
            public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
                if (this.this$0.mMenuItemClickListener != null) {
                    return this.this$0.mMenuItemClickListener.onMenuItemClick(menuItem);
                }
                return false;
            }

            @Override
            public void onMenuModeChange(MenuBuilder menuBuilder) {
            }
        });
        object = new MenuPopupHelper((Context)object, menuBuilder, view, false, n2, n3);
        this.mPopup = object;
        ((MenuPopupHelper)object).setGravity(n);
        ((MenuPopupHelper)object).setOnDismissListener(new PopupWindow.OnDismissListener(this){
            final PopupMenu this$0;
            {
                this.this$0 = popupMenu;
            }

            public void onDismiss() {
                if (this.this$0.mOnDismissListener != null) {
                    this.this$0.mOnDismissListener.onDismiss(this.this$0);
                }
            }
        });
    }

    public void dismiss() {
        this.mPopup.dismiss();
    }

    public View.OnTouchListener getDragToOpenListener() {
        if (this.mDragListener == null) {
            this.mDragListener = new ForwardingListener(this, this.mAnchor){
                final PopupMenu this$0;
                {
                    this.this$0 = popupMenu;
                    super(view);
                }

                @Override
                public ShowableListMenu getPopup() {
                    return this.this$0.mPopup.getPopup();
                }

                @Override
                protected boolean onForwardingStarted() {
                    this.this$0.show();
                    return true;
                }

                @Override
                protected boolean onForwardingStopped() {
                    this.this$0.dismiss();
                    return true;
                }
            };
        }
        return this.mDragListener;
    }

    public int getGravity() {
        return this.mPopup.getGravity();
    }

    public Menu getMenu() {
        return this.mMenu;
    }

    public MenuInflater getMenuInflater() {
        return new SupportMenuInflater(this.mContext);
    }

    ListView getMenuListView() {
        if (!this.mPopup.isShowing()) {
            return null;
        }
        return this.mPopup.getListView();
    }

    public void inflate(int n) {
        this.getMenuInflater().inflate(n, (Menu)this.mMenu);
    }

    public void setGravity(int n) {
        this.mPopup.setGravity(n);
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.mMenuItemClickListener = onMenuItemClickListener;
    }

    public void show() {
        this.mPopup.show();
    }

    public static interface OnDismissListener {
        public void onDismiss(PopupMenu var1);
    }

    public static interface OnMenuItemClickListener {
        public boolean onMenuItemClick(MenuItem var1);
    }
}

