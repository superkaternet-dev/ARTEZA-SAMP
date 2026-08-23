/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.content.res.ColorStateList
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Drawable
 *  android.view.ActionProvider
 *  android.view.ContextMenu$ContextMenuInfo
 *  android.view.KeyEvent
 *  android.view.MenuItem
 *  android.view.MenuItem$OnActionExpandListener
 *  android.view.MenuItem$OnMenuItemClickListener
 *  android.view.SubMenu
 *  android.view.View
 */
package androidx.appcompat.view.menu;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.internal.view.SupportMenuItem;

public class ActionMenuItem
implements SupportMenuItem {
    private static final int CHECKABLE = 1;
    private static final int CHECKED = 2;
    private static final int ENABLED = 16;
    private static final int EXCLUSIVE = 4;
    private static final int HIDDEN = 8;
    private MenuItem.OnMenuItemClickListener mClickListener;
    private CharSequence mContentDescription;
    private Context mContext;
    private int mFlags = 16;
    private final int mGroup;
    private boolean mHasIconTint = false;
    private boolean mHasIconTintMode = false;
    private Drawable mIconDrawable;
    private ColorStateList mIconTintList = null;
    private PorterDuff.Mode mIconTintMode = null;
    private final int mId;
    private Intent mIntent;
    private final int mOrdering;
    private char mShortcutAlphabeticChar;
    private int mShortcutAlphabeticModifiers = 4096;
    private char mShortcutNumericChar;
    private int mShortcutNumericModifiers = 4096;
    private CharSequence mTitle;
    private CharSequence mTitleCondensed;
    private CharSequence mTooltipText;

    public ActionMenuItem(Context context, int n, int n2, int n3, int n4, CharSequence charSequence) {
        this.mContext = context;
        this.mId = n2;
        this.mGroup = n;
        this.mOrdering = n4;
        this.mTitle = charSequence;
    }

    private void applyIconTint() {
        Drawable drawable2 = this.mIconDrawable;
        if (drawable2 != null && (this.mHasIconTint || this.mHasIconTintMode)) {
            this.mIconDrawable = drawable2 = DrawableCompat.wrap(drawable2);
            this.mIconDrawable = drawable2 = drawable2.mutate();
            if (this.mHasIconTint) {
                DrawableCompat.setTintList(drawable2, this.mIconTintList);
            }
            if (this.mHasIconTintMode) {
                DrawableCompat.setTintMode(this.mIconDrawable, this.mIconTintMode);
            }
        }
    }

    @Override
    public boolean collapseActionView() {
        return false;
    }

    @Override
    public boolean expandActionView() {
        return false;
    }

    public ActionProvider getActionProvider() {
        throw new UnsupportedOperationException();
    }

    @Override
    public View getActionView() {
        return null;
    }

    @Override
    public int getAlphabeticModifiers() {
        return this.mShortcutAlphabeticModifiers;
    }

    public char getAlphabeticShortcut() {
        return this.mShortcutAlphabeticChar;
    }

    @Override
    public CharSequence getContentDescription() {
        return this.mContentDescription;
    }

    public int getGroupId() {
        return this.mGroup;
    }

    public Drawable getIcon() {
        return this.mIconDrawable;
    }

    @Override
    public ColorStateList getIconTintList() {
        return this.mIconTintList;
    }

    @Override
    public PorterDuff.Mode getIconTintMode() {
        return this.mIconTintMode;
    }

    public Intent getIntent() {
        return this.mIntent;
    }

    public int getItemId() {
        return this.mId;
    }

    public ContextMenu.ContextMenuInfo getMenuInfo() {
        return null;
    }

    @Override
    public int getNumericModifiers() {
        return this.mShortcutNumericModifiers;
    }

    public char getNumericShortcut() {
        return this.mShortcutNumericChar;
    }

    public int getOrder() {
        return this.mOrdering;
    }

    public SubMenu getSubMenu() {
        return null;
    }

    @Override
    public androidx.core.view.ActionProvider getSupportActionProvider() {
        return null;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public CharSequence getTitleCondensed() {
        CharSequence charSequence = this.mTitleCondensed;
        if (charSequence == null) {
            charSequence = this.mTitle;
        }
        return charSequence;
    }

    @Override
    public CharSequence getTooltipText() {
        return this.mTooltipText;
    }

    public boolean hasSubMenu() {
        return false;
    }

    public boolean invoke() {
        MenuItem.OnMenuItemClickListener onMenuItemClickListener = this.mClickListener;
        if (onMenuItemClickListener != null && onMenuItemClickListener.onMenuItemClick((MenuItem)this)) {
            return true;
        }
        onMenuItemClickListener = this.mIntent;
        if (onMenuItemClickListener != null) {
            this.mContext.startActivity((Intent)onMenuItemClickListener);
            return true;
        }
        return false;
    }

    @Override
    public boolean isActionViewExpanded() {
        return false;
    }

    public boolean isCheckable() {
        int n = this.mFlags;
        boolean bl = true;
        if ((n & 1) == 0) {
            bl = false;
        }
        return bl;
    }

    public boolean isChecked() {
        boolean bl = (this.mFlags & 2) != 0;
        return bl;
    }

    public boolean isEnabled() {
        boolean bl = (this.mFlags & 0x10) != 0;
        return bl;
    }

    public boolean isVisible() {
        boolean bl = (this.mFlags & 8) == 0;
        return bl;
    }

    @Override
    public boolean requiresActionButton() {
        return true;
    }

    @Override
    public boolean requiresOverflow() {
        return false;
    }

    public MenuItem setActionProvider(ActionProvider actionProvider) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SupportMenuItem setActionView(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SupportMenuItem setActionView(View view) {
        throw new UnsupportedOperationException();
    }

    public MenuItem setAlphabeticShortcut(char c) {
        this.mShortcutAlphabeticChar = Character.toLowerCase(c);
        return this;
    }

    @Override
    public MenuItem setAlphabeticShortcut(char c, int n) {
        this.mShortcutAlphabeticChar = Character.toLowerCase(c);
        this.mShortcutAlphabeticModifiers = KeyEvent.normalizeMetaState((int)n);
        return this;
    }

    public MenuItem setCheckable(boolean bl) {
        this.mFlags = this.mFlags & 0xFFFFFFFE | bl;
        return this;
    }

    public MenuItem setChecked(boolean bl) {
        int n = this.mFlags;
        int n2 = bl ? 2 : 0;
        this.mFlags = n & 0xFFFFFFFD | n2;
        return this;
    }

    @Override
    public SupportMenuItem setContentDescription(CharSequence charSequence) {
        this.mContentDescription = charSequence;
        return this;
    }

    public MenuItem setEnabled(boolean bl) {
        int n = this.mFlags;
        int n2 = bl ? 16 : 0;
        this.mFlags = n & 0xFFFFFFEF | n2;
        return this;
    }

    public ActionMenuItem setExclusiveCheckable(boolean bl) {
        int n = this.mFlags;
        int n2 = bl ? 4 : 0;
        this.mFlags = n & 0xFFFFFFFB | n2;
        return this;
    }

    public MenuItem setIcon(int n) {
        this.mIconDrawable = ContextCompat.getDrawable(this.mContext, n);
        this.applyIconTint();
        return this;
    }

    public MenuItem setIcon(Drawable drawable2) {
        this.mIconDrawable = drawable2;
        this.applyIconTint();
        return this;
    }

    @Override
    public MenuItem setIconTintList(ColorStateList colorStateList) {
        this.mIconTintList = colorStateList;
        this.mHasIconTint = true;
        this.applyIconTint();
        return this;
    }

    @Override
    public MenuItem setIconTintMode(PorterDuff.Mode mode) {
        this.mIconTintMode = mode;
        this.mHasIconTintMode = true;
        this.applyIconTint();
        return this;
    }

    public MenuItem setIntent(Intent intent) {
        this.mIntent = intent;
        return this;
    }

    public MenuItem setNumericShortcut(char c) {
        this.mShortcutNumericChar = c;
        return this;
    }

    @Override
    public MenuItem setNumericShortcut(char c, int n) {
        this.mShortcutNumericChar = c;
        this.mShortcutNumericModifiers = KeyEvent.normalizeMetaState((int)n);
        return this;
    }

    public MenuItem setOnActionExpandListener(MenuItem.OnActionExpandListener onActionExpandListener) {
        throw new UnsupportedOperationException();
    }

    public MenuItem setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener onMenuItemClickListener) {
        this.mClickListener = onMenuItemClickListener;
        return this;
    }

    public MenuItem setShortcut(char c, char c2) {
        this.mShortcutNumericChar = c;
        this.mShortcutAlphabeticChar = Character.toLowerCase(c2);
        return this;
    }

    @Override
    public MenuItem setShortcut(char c, char c2, int n, int n2) {
        this.mShortcutNumericChar = c;
        this.mShortcutNumericModifiers = KeyEvent.normalizeMetaState((int)n);
        this.mShortcutAlphabeticChar = Character.toLowerCase(c2);
        this.mShortcutAlphabeticModifiers = KeyEvent.normalizeMetaState((int)n2);
        return this;
    }

    @Override
    public void setShowAsAction(int n) {
    }

    @Override
    public SupportMenuItem setShowAsActionFlags(int n) {
        this.setShowAsAction(n);
        return this;
    }

    @Override
    public SupportMenuItem setSupportActionProvider(androidx.core.view.ActionProvider actionProvider) {
        throw new UnsupportedOperationException();
    }

    public MenuItem setTitle(int n) {
        this.mTitle = this.mContext.getResources().getString(n);
        return this;
    }

    public MenuItem setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        return this;
    }

    public MenuItem setTitleCondensed(CharSequence charSequence) {
        this.mTitleCondensed = charSequence;
        return this;
    }

    @Override
    public SupportMenuItem setTooltipText(CharSequence charSequence) {
        this.mTooltipText = charSequence;
        return this;
    }

    public MenuItem setVisible(boolean bl) {
        int n = this.mFlags;
        int n2 = 8;
        if (bl) {
            n2 = 0;
        }
        this.mFlags = n & 8 | n2;
        return this;
    }
}

