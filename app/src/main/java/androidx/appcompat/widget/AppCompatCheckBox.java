/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.view.View
 *  android.widget.CheckBox
 *  android.widget.CompoundButton
 *  android.widget.TextView
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatBackgroundHelper;
import androidx.appcompat.widget.AppCompatCompoundButtonHelper;
import androidx.appcompat.widget.AppCompatTextHelper;
import androidx.appcompat.widget.ThemeUtils;
import androidx.appcompat.widget.TintContextWrapper;
import androidx.core.view.TintableBackgroundView;
import androidx.core.widget.TintableCompoundButton;

public class AppCompatCheckBox
extends CheckBox
implements TintableCompoundButton,
TintableBackgroundView {
    private final AppCompatBackgroundHelper mBackgroundTintHelper;
    private final AppCompatCompoundButtonHelper mCompoundButtonHelper;
    private final AppCompatTextHelper mTextHelper;

    public AppCompatCheckBox(Context context) {
        this(context, null);
    }

    public AppCompatCheckBox(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.checkboxStyle);
    }

    public AppCompatCheckBox(Context object, AttributeSet attributeSet, int n) {
        super(TintContextWrapper.wrap((Context)object), attributeSet, n);
        ThemeUtils.checkAppCompatTheme((View)this, this.getContext());
        object = new AppCompatCompoundButtonHelper((CompoundButton)this);
        this.mCompoundButtonHelper = object;
        ((AppCompatCompoundButtonHelper)object).loadFromAttributes(attributeSet, n);
        this.mBackgroundTintHelper = object = new AppCompatBackgroundHelper((View)this);
        ((AppCompatBackgroundHelper)object).loadFromAttributes(attributeSet, n);
        this.mTextHelper = object = new AppCompatTextHelper((TextView)this);
        ((AppCompatTextHelper)object).loadFromAttributes(attributeSet, n);
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Object object = this.mBackgroundTintHelper;
        if (object != null) {
            ((AppCompatBackgroundHelper)object).applySupportBackgroundTint();
        }
        if ((object = this.mTextHelper) != null) {
            ((AppCompatTextHelper)object).applyCompoundDrawablesTints();
        }
    }

    public int getCompoundPaddingLeft() {
        int n;
        block0: {
            n = super.getCompoundPaddingLeft();
            AppCompatCompoundButtonHelper appCompatCompoundButtonHelper = this.mCompoundButtonHelper;
            if (appCompatCompoundButtonHelper == null) break block0;
            n = appCompatCompoundButtonHelper.getCompoundPaddingLeft(n);
        }
        return n;
    }

    @Override
    public ColorStateList getSupportBackgroundTintList() {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        appCompatBackgroundHelper = appCompatBackgroundHelper != null ? appCompatBackgroundHelper.getSupportBackgroundTintList() : null;
        return appCompatBackgroundHelper;
    }

    @Override
    public PorterDuff.Mode getSupportBackgroundTintMode() {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        appCompatBackgroundHelper = appCompatBackgroundHelper != null ? appCompatBackgroundHelper.getSupportBackgroundTintMode() : null;
        return appCompatBackgroundHelper;
    }

    @Override
    public ColorStateList getSupportButtonTintList() {
        AppCompatCompoundButtonHelper appCompatCompoundButtonHelper = this.mCompoundButtonHelper;
        appCompatCompoundButtonHelper = appCompatCompoundButtonHelper != null ? appCompatCompoundButtonHelper.getSupportButtonTintList() : null;
        return appCompatCompoundButtonHelper;
    }

    @Override
    public PorterDuff.Mode getSupportButtonTintMode() {
        AppCompatCompoundButtonHelper appCompatCompoundButtonHelper = this.mCompoundButtonHelper;
        appCompatCompoundButtonHelper = appCompatCompoundButtonHelper != null ? appCompatCompoundButtonHelper.getSupportButtonTintMode() : null;
        return appCompatCompoundButtonHelper;
    }

    public void setBackgroundDrawable(Drawable drawable2) {
        super.setBackgroundDrawable(drawable2);
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.onSetBackgroundDrawable(drawable2);
        }
    }

    public void setBackgroundResource(int n) {
        super.setBackgroundResource(n);
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.onSetBackgroundResource(n);
        }
    }

    public void setButtonDrawable(int n) {
        this.setButtonDrawable(AppCompatResources.getDrawable(this.getContext(), n));
    }

    public void setButtonDrawable(Drawable object) {
        super.setButtonDrawable((Drawable)object);
        object = this.mCompoundButtonHelper;
        if (object != null) {
            ((AppCompatCompoundButtonHelper)object).onSetButtonDrawable();
        }
    }

    @Override
    public void setSupportBackgroundTintList(ColorStateList colorStateList) {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.setSupportBackgroundTintList(colorStateList);
        }
    }

    @Override
    public void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.setSupportBackgroundTintMode(mode);
        }
    }

    @Override
    public void setSupportButtonTintList(ColorStateList colorStateList) {
        AppCompatCompoundButtonHelper appCompatCompoundButtonHelper = this.mCompoundButtonHelper;
        if (appCompatCompoundButtonHelper != null) {
            appCompatCompoundButtonHelper.setSupportButtonTintList(colorStateList);
        }
    }

    @Override
    public void setSupportButtonTintMode(PorterDuff.Mode mode) {
        AppCompatCompoundButtonHelper appCompatCompoundButtonHelper = this.mCompoundButtonHelper;
        if (appCompatCompoundButtonHelper != null) {
            appCompatCompoundButtonHelper.setSupportButtonTintMode(mode);
        }
    }
}

