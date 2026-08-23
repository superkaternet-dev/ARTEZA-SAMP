/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.AbsListView$SelectionBoundsAdjuster
 *  android.widget.CheckBox
 *  android.widget.ImageView
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  android.widget.RadioButton
 *  android.widget.TextView
 */
package androidx.appcompat.view.menu;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.view.ViewCompat;

public class ListMenuItemView
extends LinearLayout
implements MenuView.ItemView,
AbsListView.SelectionBoundsAdjuster {
    private static final String TAG = "ListMenuItemView";
    private Drawable mBackground;
    private CheckBox mCheckBox;
    private LinearLayout mContent;
    private boolean mForceShowIcon;
    private ImageView mGroupDivider;
    private boolean mHasListDivider;
    private ImageView mIconView;
    private LayoutInflater mInflater;
    private MenuItemImpl mItemData;
    private boolean mPreserveIconSpacing;
    private RadioButton mRadioButton;
    private TextView mShortcutView;
    private Drawable mSubMenuArrow;
    private ImageView mSubMenuArrowView;
    private int mTextAppearance;
    private Context mTextAppearanceContext;
    private TextView mTitleView;

    public ListMenuItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.listMenuViewStyle);
    }

    public ListMenuItemView(Context context, AttributeSet object, int n) {
        super(context, (AttributeSet)object);
        object = TintTypedArray.obtainStyledAttributes(this.getContext(), (AttributeSet)object, R.styleable.MenuView, n, 0);
        this.mBackground = ((TintTypedArray)object).getDrawable(R.styleable.MenuView_android_itemBackground);
        this.mTextAppearance = ((TintTypedArray)object).getResourceId(R.styleable.MenuView_android_itemTextAppearance, -1);
        this.mPreserveIconSpacing = ((TintTypedArray)object).getBoolean(R.styleable.MenuView_preserveIconSpacing, false);
        this.mTextAppearanceContext = context;
        this.mSubMenuArrow = ((TintTypedArray)object).getDrawable(R.styleable.MenuView_subMenuArrow);
        context = context.getTheme();
        n = R.attr.dropDownListViewStyle;
        context = context.obtainStyledAttributes(null, new int[]{16843049}, n, 0);
        this.mHasListDivider = context.hasValue(0);
        ((TintTypedArray)object).recycle();
        context.recycle();
    }

    private void addContentView(View view) {
        this.addContentView(view, -1);
    }

    private void addContentView(View view, int n) {
        LinearLayout linearLayout = this.mContent;
        if (linearLayout != null) {
            linearLayout.addView(view, n);
        } else {
            this.addView(view, n);
        }
    }

    private LayoutInflater getInflater() {
        if (this.mInflater == null) {
            this.mInflater = LayoutInflater.from((Context)this.getContext());
        }
        return this.mInflater;
    }

    private void insertCheckBox() {
        CheckBox checkBox;
        this.mCheckBox = checkBox = (CheckBox)this.getInflater().inflate(R.layout.abc_list_menu_item_checkbox, (ViewGroup)this, false);
        this.addContentView((View)checkBox);
    }

    private void insertIconView() {
        ImageView imageView;
        this.mIconView = imageView = (ImageView)this.getInflater().inflate(R.layout.abc_list_menu_item_icon, (ViewGroup)this, false);
        this.addContentView((View)imageView, 0);
    }

    private void insertRadioButton() {
        RadioButton radioButton;
        this.mRadioButton = radioButton = (RadioButton)this.getInflater().inflate(R.layout.abc_list_menu_item_radio, (ViewGroup)this, false);
        this.addContentView((View)radioButton);
    }

    private void setSubMenuArrowVisible(boolean bl) {
        ImageView imageView = this.mSubMenuArrowView;
        if (imageView != null) {
            int n = bl ? 0 : 8;
            imageView.setVisibility(n);
        }
    }

    public void adjustListItemSelectionBounds(Rect rect) {
        ImageView imageView = this.mGroupDivider;
        if (imageView != null && imageView.getVisibility() == 0) {
            imageView = (LinearLayout.LayoutParams)this.mGroupDivider.getLayoutParams();
            rect.top += this.mGroupDivider.getHeight() + imageView.topMargin + imageView.bottomMargin;
        }
    }

    @Override
    public MenuItemImpl getItemData() {
        return this.mItemData;
    }

    @Override
    public void initialize(MenuItemImpl menuItemImpl, int n) {
        this.mItemData = menuItemImpl;
        n = menuItemImpl.isVisible() ? 0 : 8;
        this.setVisibility(n);
        this.setTitle(menuItemImpl.getTitleForItemView(this));
        this.setCheckable(menuItemImpl.isCheckable());
        this.setShortcut(menuItemImpl.shouldShowShortcut(), menuItemImpl.getShortcut());
        this.setIcon(menuItemImpl.getIcon());
        this.setEnabled(menuItemImpl.isEnabled());
        this.setSubMenuArrowVisible(menuItemImpl.hasSubMenu());
        this.setContentDescription(menuItemImpl.getContentDescription());
    }

    protected void onFinishInflate() {
        TextView textView;
        super.onFinishInflate();
        ViewCompat.setBackground((View)this, this.mBackground);
        this.mTitleView = textView = (TextView)this.findViewById(R.id.title);
        int n = this.mTextAppearance;
        if (n != -1) {
            textView.setTextAppearance(this.mTextAppearanceContext, n);
        }
        this.mShortcutView = (TextView)this.findViewById(R.id.shortcut);
        textView = (ImageView)this.findViewById(R.id.submenuarrow);
        this.mSubMenuArrowView = textView;
        if (textView != null) {
            textView.setImageDrawable(this.mSubMenuArrow);
        }
        this.mGroupDivider = (ImageView)this.findViewById(R.id.group_divider);
        this.mContent = (LinearLayout)this.findViewById(R.id.content);
    }

    protected void onMeasure(int n, int n2) {
        if (this.mIconView != null && this.mPreserveIconSpacing) {
            ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams)this.mIconView.getLayoutParams();
            if (layoutParams.height > 0 && layoutParams2.width <= 0) {
                layoutParams2.width = layoutParams.height;
            }
        }
        super.onMeasure(n, n2);
    }

    @Override
    public boolean prefersCondensedTitle() {
        return false;
    }

    @Override
    public void setCheckable(boolean bl) {
        CheckBox checkBox;
        RadioButton radioButton;
        if (!bl && this.mRadioButton == null && this.mCheckBox == null) {
            return;
        }
        if (this.mItemData.isExclusiveCheckable()) {
            if (this.mRadioButton == null) {
                this.insertRadioButton();
            }
            radioButton = this.mRadioButton;
            checkBox = this.mCheckBox;
        } else {
            if (this.mCheckBox == null) {
                this.insertCheckBox();
            }
            radioButton = this.mCheckBox;
            checkBox = this.mRadioButton;
        }
        if (bl) {
            radioButton.setChecked(this.mItemData.isChecked());
            if (radioButton.getVisibility() != 0) {
                radioButton.setVisibility(0);
            }
            if (checkBox != null && checkBox.getVisibility() != 8) {
                checkBox.setVisibility(8);
            }
        } else {
            radioButton = this.mCheckBox;
            if (radioButton != null) {
                radioButton.setVisibility(8);
            }
            if ((radioButton = this.mRadioButton) != null) {
                radioButton.setVisibility(8);
            }
        }
    }

    @Override
    public void setChecked(boolean bl) {
        RadioButton radioButton;
        if (this.mItemData.isExclusiveCheckable()) {
            if (this.mRadioButton == null) {
                this.insertRadioButton();
            }
            radioButton = this.mRadioButton;
        } else {
            if (this.mCheckBox == null) {
                this.insertCheckBox();
            }
            radioButton = this.mCheckBox;
        }
        radioButton.setChecked(bl);
    }

    public void setForceShowIcon(boolean bl) {
        this.mForceShowIcon = bl;
        this.mPreserveIconSpacing = bl;
    }

    public void setGroupDividerEnabled(boolean bl) {
        ImageView imageView = this.mGroupDivider;
        if (imageView != null) {
            int n = !this.mHasListDivider && bl ? 0 : 8;
            imageView.setVisibility(n);
        }
    }

    @Override
    public void setIcon(Drawable drawable2) {
        boolean bl = this.mItemData.shouldShowIcon() || this.mForceShowIcon;
        if (!bl && !this.mPreserveIconSpacing) {
            return;
        }
        ImageView imageView = this.mIconView;
        if (imageView == null && drawable2 == null && !this.mPreserveIconSpacing) {
            return;
        }
        if (imageView == null) {
            this.insertIconView();
        }
        if (drawable2 == null && !this.mPreserveIconSpacing) {
            this.mIconView.setVisibility(8);
        } else {
            imageView = this.mIconView;
            if (!bl) {
                drawable2 = null;
            }
            imageView.setImageDrawable(drawable2);
            if (this.mIconView.getVisibility() != 0) {
                this.mIconView.setVisibility(0);
            }
        }
    }

    @Override
    public void setShortcut(boolean bl, char c) {
        c = bl && this.mItemData.shouldShowShortcut() ? (char)'\u0000' : (char)8;
        if (c == '\u0000') {
            this.mShortcutView.setText((CharSequence)this.mItemData.getShortcutLabel());
        }
        if (this.mShortcutView.getVisibility() != c) {
            this.mShortcutView.setVisibility((int)c);
        }
    }

    @Override
    public void setTitle(CharSequence charSequence) {
        if (charSequence != null) {
            this.mTitleView.setText(charSequence);
            if (this.mTitleView.getVisibility() != 0) {
                this.mTitleView.setVisibility(0);
            }
        } else if (this.mTitleView.getVisibility() != 8) {
            this.mTitleView.setVisibility(8);
        }
    }

    @Override
    public boolean showsIcon() {
        return this.mForceShowIcon;
    }
}

