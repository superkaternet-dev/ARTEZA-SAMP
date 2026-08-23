/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.view.ContextThemeWrapper
 *  android.view.Menu
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewDebug$ExportedProperty
 *  android.view.ViewGroup$LayoutParams
 *  android.view.accessibility.AccessibilityEvent
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.BaseMenuPresenter;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.ActionMenuPresenter;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.ViewUtils;

public class ActionMenuView
extends LinearLayoutCompat
implements MenuBuilder.ItemInvoker,
MenuView {
    static final int GENERATED_ITEM_PADDING = 4;
    static final int MIN_CELL_SIZE = 56;
    private static final String TAG = "ActionMenuView";
    private MenuPresenter.Callback mActionMenuPresenterCallback;
    private boolean mFormatItems;
    private int mFormatItemsWidth;
    private int mGeneratedItemPadding;
    private MenuBuilder mMenu;
    MenuBuilder.Callback mMenuBuilderCallback;
    private int mMinCellSize;
    OnMenuItemClickListener mOnMenuItemClickListener;
    private Context mPopupContext;
    private int mPopupTheme;
    private ActionMenuPresenter mPresenter;
    private boolean mReserveOverflow;

    public ActionMenuView(Context context) {
        this(context, null);
    }

    public ActionMenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.setBaselineAligned(false);
        float f = context.getResources().getDisplayMetrics().density;
        this.mMinCellSize = (int)(56.0f * f);
        this.mGeneratedItemPadding = (int)(4.0f * f);
        this.mPopupContext = context;
        this.mPopupTheme = 0;
    }

    static int measureChildForCells(View view, int n, int n2, int n3, int n4) {
        boolean bl;
        int n5;
        LayoutParams layoutParams;
        block8: {
            int n6;
            block9: {
                layoutParams = (LayoutParams)view.getLayoutParams();
                n5 = View.MeasureSpec.makeMeasureSpec((int)(View.MeasureSpec.getSize((int)n3) - n4), (int)View.MeasureSpec.getMode((int)n3));
                ActionMenuItemView actionMenuItemView = view instanceof ActionMenuItemView ? (ActionMenuItemView)view : null;
                bl = false;
                n4 = actionMenuItemView != null && actionMenuItemView.hasText() ? 1 : 0;
                n3 = n6 = 0;
                if (n2 <= 0) break block8;
                if (n4 == 0) break block9;
                n3 = n6;
                if (n2 < 2) break block8;
            }
            view.measure(View.MeasureSpec.makeMeasureSpec((int)(n * n2), (int)Integer.MIN_VALUE), n5);
            n6 = view.getMeasuredWidth();
            n2 = n3 = n6 / n;
            if (n6 % n != 0) {
                n2 = n3 + 1;
            }
            n3 = n2;
            if (n4 != 0) {
                n3 = n2;
                if (n2 < 2) {
                    n3 = 2;
                }
            }
        }
        boolean bl2 = bl;
        if (!layoutParams.isOverflowButton) {
            bl2 = bl;
            if (n4 != 0) {
                bl2 = true;
            }
        }
        layoutParams.expandable = bl2;
        layoutParams.cellsUsed = n3;
        view.measure(View.MeasureSpec.makeMeasureSpec((int)(n3 * n), (int)0x40000000), n5);
        return n3;
    }

    private void onMeasureExactFormat(int n, int n2) {
        long l;
        int n3;
        LayoutParams layoutParams;
        int n4;
        Object object;
        int n5;
        int n6 = View.MeasureSpec.getMode((int)n2);
        int n7 = View.MeasureSpec.getSize((int)n);
        int n8 = View.MeasureSpec.getSize((int)n2);
        int n9 = this.getPaddingLeft();
        n = this.getPaddingRight();
        int n10 = this.getPaddingTop() + this.getPaddingBottom();
        int n11 = ActionMenuView.getChildMeasureSpec((int)n2, (int)n10, (int)-2);
        int n12 = n7 - (n9 + n);
        n = this.mMinCellSize;
        int n13 = n12 / n;
        int n14 = n12 % n;
        if (n13 == 0) {
            this.setMeasuredDimension(n12, 0);
            return;
        }
        int n15 = n + n14 / n13;
        n = n13;
        n9 = 0;
        int n16 = 0;
        n2 = 0;
        int n17 = 0;
        long l2 = 0L;
        int n18 = this.getChildCount();
        n7 = 0;
        for (n5 = 0; n5 < n18; ++n5) {
            object = this.getChildAt(n5);
            if (object.getVisibility() == 8) {
                n4 = n7;
            } else {
                boolean bl = object instanceof ActionMenuItemView;
                n4 = n7 + 1;
                if (bl) {
                    n7 = this.mGeneratedItemPadding;
                    object.setPadding(n7, 0, n7, 0);
                }
                layoutParams = (LayoutParams)object.getLayoutParams();
                layoutParams.expanded = false;
                layoutParams.extraPixels = 0;
                layoutParams.cellsUsed = 0;
                layoutParams.expandable = false;
                layoutParams.leftMargin = 0;
                layoutParams.rightMargin = 0;
                bl = bl && ((ActionMenuItemView)object).hasText();
                layoutParams.preventEdgeOffset = bl;
                n7 = layoutParams.isOverflowButton ? 1 : n;
                n3 = ActionMenuView.measureChildForCells(object, n15, n7, n11, n10);
                n16 = Math.max(n16, n3);
                n7 = n2;
                if (layoutParams.expandable) {
                    n7 = n2 + 1;
                }
                if (layoutParams.isOverflowButton) {
                    n17 = 1;
                }
                n -= n3;
                n9 = Math.max(n9, object.getMeasuredHeight());
                if (n3 == 1) {
                    l = 1 << n5;
                    l2 |= l;
                    n2 = n7;
                } else {
                    n2 = n7;
                }
            }
            n7 = n4;
        }
        n3 = n17 != 0 && n7 == 2 ? 1 : 0;
        n4 = 0;
        int n19 = n;
        n = n4;
        n13 = n12;
        n5 = n6;
        while (n2 > 0 && n19 > 0) {
            n6 = Integer.MAX_VALUE;
            long l3 = 0L;
            n4 = 0;
            for (n12 = 0; n12 < n18; ++n12) {
                int n20;
                layoutParams = (LayoutParams)this.getChildAt(n12).getLayoutParams();
                if (!layoutParams.expandable) {
                    n14 = n4;
                    n20 = n6;
                    l = l3;
                } else if (layoutParams.cellsUsed < n6) {
                    n20 = layoutParams.cellsUsed;
                    l = 1L << n12;
                    n14 = 1;
                } else {
                    n14 = n4;
                    n20 = n6;
                    l = l3;
                    if (layoutParams.cellsUsed == n6) {
                        l = l3 | 1L << n12;
                        n14 = n4 + 1;
                        n20 = n6;
                    }
                }
                n4 = n14;
                n6 = n20;
                l3 = l;
            }
            l2 |= l3;
            if (n4 > n19) break;
            for (n = 0; n < n18; ++n) {
                layoutParams = this.getChildAt(n);
                object = (LayoutParams)layoutParams.getLayoutParams();
                if ((l3 & (long)(1 << n)) == 0L) {
                    n14 = n19;
                    l = l2;
                    if (object.cellsUsed == n6 + 1) {
                        l = l2 | (long)(1 << n);
                        n14 = n19;
                    }
                } else {
                    if (n3 != 0 && object.preventEdgeOffset && n19 == 1) {
                        n14 = this.mGeneratedItemPadding;
                        layoutParams.setPadding(n14 + n15, 0, n14, 0);
                    }
                    ++object.cellsUsed;
                    object.expanded = true;
                    n14 = n19 - 1;
                    l = l2;
                }
                n19 = n14;
                l2 = l;
            }
            n = 1;
        }
        n2 = n17 == 0 && n7 == 1 ? 1 : 0;
        if (n19 > 0 && l2 != 0L && (n19 < n7 - 1 || n2 != 0 || n16 > 1)) {
            float f;
            float f2 = f = (float)Long.bitCount(l2);
            if (n2 == 0) {
                float f3;
                if ((l2 & 1L) != 0L) {
                    f3 = f;
                    if (!((LayoutParams)this.getChildAt((int)0).getLayoutParams()).preventEdgeOffset) {
                        f3 = f - 0.5f;
                    }
                } else {
                    f3 = f;
                }
                f2 = f3;
                if ((l2 & (long)(1 << n18 - 1)) != 0L) {
                    f2 = f3;
                    if (!((LayoutParams)this.getChildAt((int)(n18 - 1)).getLayoutParams()).preventEdgeOffset) {
                        f2 = f3 - 0.5f;
                    }
                }
            }
            n4 = f2 > 0.0f ? (int)((float)(n19 * n15) / f2) : 0;
            n17 = n;
            for (n14 = 0; n14 < n18; ++n14) {
                if ((l2 & (long)(1 << n14)) == 0L) {
                    n = n17;
                } else {
                    object = this.getChildAt(n14);
                    layoutParams = (LayoutParams)object.getLayoutParams();
                    if (object instanceof ActionMenuItemView) {
                        layoutParams.extraPixels = n4;
                        layoutParams.expanded = true;
                        if (n14 == 0 && !layoutParams.preventEdgeOffset) {
                            layoutParams.leftMargin = -n4 / 2;
                        }
                        n = 1;
                    } else if (layoutParams.isOverflowButton) {
                        layoutParams.extraPixels = n4;
                        layoutParams.expanded = true;
                        layoutParams.rightMargin = -n4 / 2;
                        n = 1;
                    } else {
                        if (n14 != 0) {
                            layoutParams.leftMargin = n4 / 2;
                        }
                        n = n17;
                        if (n14 != n18 - 1) {
                            layoutParams.rightMargin = n4 / 2;
                            n = n17;
                        }
                    }
                }
                n17 = n;
            }
        } else {
            n17 = n;
        }
        if (n17 != 0) {
            for (n = 0; n < n18; ++n) {
                object = this.getChildAt(n);
                layoutParams = (LayoutParams)object.getLayoutParams();
                if (!layoutParams.expanded) continue;
                object.measure(View.MeasureSpec.makeMeasureSpec((int)(layoutParams.cellsUsed * n15 + layoutParams.extraPixels), (int)0x40000000), n11);
            }
        }
        n = n5 != 0x40000000 ? n9 : n8;
        this.setMeasuredDimension(n13, n);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    public void dismissPopupMenus() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.dismissPopupMenus();
        }
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        return false;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.gravity = 16;
        return layoutParams;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(this.getContext(), attributeSet);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams object) {
        if (object != null) {
            object = object instanceof LayoutParams ? new LayoutParams((LayoutParams)((Object)object)) : new LayoutParams((ViewGroup.LayoutParams)object);
            if (object.gravity <= 0) {
                object.gravity = 16;
            }
            return object;
        }
        return this.generateDefaultLayoutParams();
    }

    public LayoutParams generateOverflowButtonLayoutParams() {
        LayoutParams layoutParams = this.generateDefaultLayoutParams();
        layoutParams.isOverflowButton = true;
        return layoutParams;
    }

    public Menu getMenu() {
        if (this.mMenu == null) {
            Object object = this.getContext();
            Object object2 = new MenuBuilder((Context)object);
            this.mMenu = object2;
            ((MenuBuilder)object2).setCallback(new MenuBuilderCallback(this));
            this.mPresenter = object2 = new ActionMenuPresenter((Context)object);
            ((ActionMenuPresenter)object2).setReserveOverflow(true);
            object = this.mPresenter;
            object2 = this.mActionMenuPresenterCallback;
            if (object2 == null) {
                object2 = new ActionMenuPresenterCallback();
            }
            ((BaseMenuPresenter)object).setCallback((MenuPresenter.Callback)object2);
            this.mMenu.addMenuPresenter(this.mPresenter, this.mPopupContext);
            this.mPresenter.setMenuView(this);
        }
        return this.mMenu;
    }

    public Drawable getOverflowIcon() {
        this.getMenu();
        return this.mPresenter.getOverflowIcon();
    }

    public int getPopupTheme() {
        return this.mPopupTheme;
    }

    @Override
    public int getWindowAnimations() {
        return 0;
    }

    protected boolean hasSupportDividerBeforeChildAt(int n) {
        boolean bl;
        if (n == 0) {
            return false;
        }
        View view = this.getChildAt(n - 1);
        View view2 = this.getChildAt(n);
        boolean bl2 = bl = false;
        if (n < this.getChildCount()) {
            bl2 = bl;
            if (view instanceof ActionMenuChildView) {
                bl2 = false | ((ActionMenuChildView)view).needsDividerAfter();
            }
        }
        bl = bl2;
        if (n > 0) {
            bl = bl2;
            if (view2 instanceof ActionMenuChildView) {
                bl = bl2 | ((ActionMenuChildView)view2).needsDividerBefore();
            }
        }
        return bl;
    }

    public boolean hideOverflowMenu() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        boolean bl = actionMenuPresenter != null && actionMenuPresenter.hideOverflowMenu();
        return bl;
    }

    @Override
    public void initialize(MenuBuilder menuBuilder) {
        this.mMenu = menuBuilder;
    }

    @Override
    public boolean invokeItem(MenuItemImpl menuItemImpl) {
        return this.mMenu.performItemAction(menuItemImpl, 0);
    }

    public boolean isOverflowMenuShowPending() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        boolean bl = actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowPending();
        return bl;
    }

    public boolean isOverflowMenuShowing() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        boolean bl = actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowing();
        return bl;
    }

    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }

    public void onConfigurationChanged(Configuration object) {
        super.onConfigurationChanged((Configuration)object);
        object = this.mPresenter;
        if (object != null) {
            ((ActionMenuPresenter)object).updateMenuView(false);
            if (this.mPresenter.isOverflowMenuShowing()) {
                this.mPresenter.hideOverflowMenu();
                this.mPresenter.showOverflowMenu();
            }
        }
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.dismissPopupMenus();
    }

    @Override
    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        Object object;
        Object object2;
        int n5;
        if (!this.mFormatItems) {
            super.onLayout(bl, n, n2, n3, n4);
            return;
        }
        int n6 = this.getChildCount();
        int n7 = (n4 - n2) / 2;
        int n8 = this.getDividerWidth();
        n4 = 0;
        n2 = 0;
        int n9 = n3 - n - this.getPaddingRight() - this.getPaddingLeft();
        int n10 = 0;
        bl = ViewUtils.isLayoutRtl((View)this);
        for (n5 = 0; n5 < n6; ++n5) {
            object2 = this.getChildAt(n5);
            if (object2.getVisibility() == 8) continue;
            object = (LayoutParams)object2.getLayoutParams();
            if (object.isOverflowButton) {
                int n11;
                n4 = n10 = object2.getMeasuredWidth();
                if (this.hasSupportDividerBeforeChildAt(n5)) {
                    n4 = n10 + n8;
                }
                int n12 = object2.getMeasuredHeight();
                if (bl) {
                    n10 = this.getPaddingLeft() + object.leftMargin;
                    n11 = n10 + n4;
                } else {
                    n11 = this.getWidth() - this.getPaddingRight() - object.rightMargin;
                    n10 = n11 - n4;
                }
                int n13 = n7 - n12 / 2;
                object2.layout(n10, n13, n11, n13 + n12);
                n9 -= n4;
                n10 = 1;
                continue;
            }
            n9 -= object2.getMeasuredWidth() + object.leftMargin + object.rightMargin;
            this.hasSupportDividerBeforeChildAt(n5);
            ++n2;
        }
        if (n6 == 1 && n10 == 0) {
            object = this.getChildAt(0);
            n2 = object.getMeasuredWidth();
            n4 = object.getMeasuredHeight();
            n = (n3 - n) / 2 - n2 / 2;
            n3 = n7 - n4 / 2;
            object.layout(n, n3, n + n2, n3 + n4);
            return;
        }
        n = n2 - (n10 ^ 1);
        n = n > 0 ? n9 / n : 0;
        n9 = Math.max(0, n);
        if (bl) {
            n3 = this.getWidth() - this.getPaddingRight();
            for (n = 0; n < n6; ++n) {
                object = this.getChildAt(n);
                object2 = (LayoutParams)object.getLayoutParams();
                if (object.getVisibility() == 8 || object2.isOverflowButton) continue;
                n5 = object.getMeasuredWidth();
                n10 = object.getMeasuredHeight();
                n8 = n7 - n10 / 2;
                object.layout((n3 -= object2.rightMargin) - n5, n8, n3, n8 + n10);
                n3 -= object2.leftMargin + n5 + n9;
            }
        } else {
            n3 = this.getPaddingLeft();
            for (n = 0; n < n6; ++n) {
                object2 = this.getChildAt(n);
                object = (LayoutParams)object2.getLayoutParams();
                n2 = n3;
                if (object2.getVisibility() != 8) {
                    if (object.isOverflowButton) {
                        n2 = n3;
                    } else {
                        n2 = n3 + object.leftMargin;
                        n4 = object2.getMeasuredWidth();
                        n5 = object2.getMeasuredHeight();
                        n3 = n7 - n5 / 2;
                        object2.layout(n2, n3, n2 + n4, n3 + n5);
                        n2 += object.rightMargin + n4 + n9;
                    }
                }
                n3 = n2;
            }
        }
    }

    @Override
    protected void onMeasure(int n, int n2) {
        Object object;
        boolean bl = this.mFormatItems;
        boolean bl2 = View.MeasureSpec.getMode((int)n) == 0x40000000;
        this.mFormatItems = bl2;
        if (bl != bl2) {
            this.mFormatItemsWidth = 0;
        }
        int n3 = View.MeasureSpec.getSize((int)n);
        if (this.mFormatItems && (object = this.mMenu) != null && n3 != this.mFormatItemsWidth) {
            this.mFormatItemsWidth = n3;
            object.onItemsChanged(true);
        }
        int n4 = this.getChildCount();
        if (this.mFormatItems && n4 > 0) {
            this.onMeasureExactFormat(n, n2);
        } else {
            for (n3 = 0; n3 < n4; ++n3) {
                object = (LayoutParams)this.getChildAt(n3).getLayoutParams();
                ((LayoutParams)((Object)object)).rightMargin = 0;
                ((LayoutParams)((Object)object)).leftMargin = 0;
            }
            super.onMeasure(n, n2);
        }
    }

    public MenuBuilder peekMenu() {
        return this.mMenu;
    }

    public void setExpandedActionViewsExclusive(boolean bl) {
        this.mPresenter.setExpandedActionViewsExclusive(bl);
    }

    public void setMenuCallbacks(MenuPresenter.Callback callback, MenuBuilder.Callback callback2) {
        this.mActionMenuPresenterCallback = callback;
        this.mMenuBuilderCallback = callback2;
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.mOnMenuItemClickListener = onMenuItemClickListener;
    }

    public void setOverflowIcon(Drawable drawable2) {
        this.getMenu();
        this.mPresenter.setOverflowIcon(drawable2);
    }

    public void setOverflowReserved(boolean bl) {
        this.mReserveOverflow = bl;
    }

    public void setPopupTheme(int n) {
        if (this.mPopupTheme != n) {
            this.mPopupTheme = n;
            this.mPopupContext = n == 0 ? this.getContext() : new ContextThemeWrapper(this.getContext(), n);
        }
    }

    public void setPresenter(ActionMenuPresenter actionMenuPresenter) {
        this.mPresenter = actionMenuPresenter;
        actionMenuPresenter.setMenuView(this);
    }

    public boolean showOverflowMenu() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        boolean bl = actionMenuPresenter != null && actionMenuPresenter.showOverflowMenu();
        return bl;
    }

    public static interface ActionMenuChildView {
        public boolean needsDividerAfter();

        public boolean needsDividerBefore();
    }

    private static class ActionMenuPresenterCallback
    implements MenuPresenter.Callback {
        ActionMenuPresenterCallback() {
        }

        @Override
        public void onCloseMenu(MenuBuilder menuBuilder, boolean bl) {
        }

        @Override
        public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            return false;
        }
    }

    public static class LayoutParams
    extends LinearLayoutCompat.LayoutParams {
        @ViewDebug.ExportedProperty
        public int cellsUsed;
        @ViewDebug.ExportedProperty
        public boolean expandable;
        boolean expanded;
        @ViewDebug.ExportedProperty
        public int extraPixels;
        @ViewDebug.ExportedProperty
        public boolean isOverflowButton;
        @ViewDebug.ExportedProperty
        public boolean preventEdgeOffset;

        public LayoutParams(int n, int n2) {
            super(n, n2);
            this.isOverflowButton = false;
        }

        LayoutParams(int n, int n2, boolean bl) {
            super(n, n2);
            this.isOverflowButton = bl;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.LayoutParams)layoutParams);
            this.isOverflowButton = layoutParams.isOverflowButton;
        }
    }

    private class MenuBuilderCallback
    implements MenuBuilder.Callback {
        final ActionMenuView this$0;

        MenuBuilderCallback(ActionMenuView actionMenuView) {
            this.this$0 = actionMenuView;
        }

        @Override
        public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
            boolean bl = this.this$0.mOnMenuItemClickListener != null && this.this$0.mOnMenuItemClickListener.onMenuItemClick(menuItem);
            return bl;
        }

        @Override
        public void onMenuModeChange(MenuBuilder menuBuilder) {
            if (this.this$0.mMenuBuilderCallback != null) {
                this.this$0.mMenuBuilderCallback.onMenuModeChange(menuBuilder);
            }
        }
    }

    public static interface OnMenuItemClickListener {
        public boolean onMenuItemClick(MenuItem var1);
    }
}

