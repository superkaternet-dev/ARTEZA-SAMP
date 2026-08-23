/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.TypedArray
 *  android.graphics.Canvas
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.view.View
 *  android.widget.TextView
 */
package com.google.android.material.button;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;
import com.google.android.material.R;
import com.google.android.material.button.MaterialButtonHelper;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MaterialButton
extends AppCompatButton {
    public static final int ICON_GRAVITY_START = 1;
    public static final int ICON_GRAVITY_TEXT_START = 2;
    private static final String LOG_TAG = "MaterialButton";
    private Drawable icon;
    private int iconGravity;
    private int iconLeft;
    private int iconPadding;
    private int iconSize;
    private ColorStateList iconTint;
    private PorterDuff.Mode iconTintMode;
    private final MaterialButtonHelper materialButtonHelper;

    public MaterialButton(Context context) {
        this(context, null);
    }

    public MaterialButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.materialButtonStyle);
    }

    public MaterialButton(Context context, AttributeSet object, int n) {
        super(context, (AttributeSet)object, n);
        context = ThemeEnforcement.obtainStyledAttributes(context, (AttributeSet)object, R.styleable.MaterialButton, n, R.style.Widget_MaterialComponents_Button, new int[0]);
        this.iconPadding = context.getDimensionPixelSize(R.styleable.MaterialButton_iconPadding, 0);
        this.iconTintMode = ViewUtils.parseTintMode(context.getInt(R.styleable.MaterialButton_iconTintMode, -1), PorterDuff.Mode.SRC_IN);
        this.iconTint = MaterialResources.getColorStateList(this.getContext(), (TypedArray)context, R.styleable.MaterialButton_iconTint);
        this.icon = MaterialResources.getDrawable(this.getContext(), (TypedArray)context, R.styleable.MaterialButton_icon);
        this.iconGravity = context.getInteger(R.styleable.MaterialButton_iconGravity, 1);
        this.iconSize = context.getDimensionPixelSize(R.styleable.MaterialButton_iconSize, 0);
        object = new MaterialButtonHelper(this);
        this.materialButtonHelper = object;
        ((MaterialButtonHelper)object).loadFromAttributes((TypedArray)context);
        context.recycle();
        this.setCompoundDrawablePadding(this.iconPadding);
        this.updateIcon();
    }

    private boolean isLayoutRTL() {
        int n = ViewCompat.getLayoutDirection((View)this);
        boolean bl = true;
        if (n != 1) {
            bl = false;
        }
        return bl;
    }

    private boolean isUsingOriginalBackground() {
        MaterialButtonHelper materialButtonHelper = this.materialButtonHelper;
        boolean bl = materialButtonHelper != null && !materialButtonHelper.isBackgroundOverwritten();
        return bl;
    }

    private void updateIcon() {
        Drawable drawable2 = this.icon;
        if (drawable2 != null) {
            int n;
            this.icon = drawable2 = drawable2.mutate();
            DrawableCompat.setTintList(drawable2, this.iconTint);
            drawable2 = this.iconTintMode;
            if (drawable2 != null) {
                DrawableCompat.setTintMode(this.icon, (PorterDuff.Mode)drawable2);
            }
            if ((n = this.iconSize) == 0) {
                n = this.icon.getIntrinsicWidth();
            }
            int n2 = this.iconSize;
            if (n2 == 0) {
                n2 = this.icon.getIntrinsicHeight();
            }
            drawable2 = this.icon;
            int n3 = this.iconLeft;
            drawable2.setBounds(n3, 0, n3 + n, n2);
        }
        TextViewCompat.setCompoundDrawablesRelative((TextView)this, this.icon, null, null, null);
    }

    public ColorStateList getBackgroundTintList() {
        return this.getSupportBackgroundTintList();
    }

    public PorterDuff.Mode getBackgroundTintMode() {
        return this.getSupportBackgroundTintMode();
    }

    public int getCornerRadius() {
        int n = this.isUsingOriginalBackground() ? this.materialButtonHelper.getCornerRadius() : 0;
        return n;
    }

    public Drawable getIcon() {
        return this.icon;
    }

    public int getIconGravity() {
        return this.iconGravity;
    }

    public int getIconPadding() {
        return this.iconPadding;
    }

    public int getIconSize() {
        return this.iconSize;
    }

    public ColorStateList getIconTint() {
        return this.iconTint;
    }

    public PorterDuff.Mode getIconTintMode() {
        return this.iconTintMode;
    }

    public ColorStateList getRippleColor() {
        ColorStateList colorStateList = this.isUsingOriginalBackground() ? this.materialButtonHelper.getRippleColor() : null;
        return colorStateList;
    }

    public ColorStateList getStrokeColor() {
        ColorStateList colorStateList = this.isUsingOriginalBackground() ? this.materialButtonHelper.getStrokeColor() : null;
        return colorStateList;
    }

    public int getStrokeWidth() {
        int n = this.isUsingOriginalBackground() ? this.materialButtonHelper.getStrokeWidth() : 0;
        return n;
    }

    @Override
    public ColorStateList getSupportBackgroundTintList() {
        if (this.isUsingOriginalBackground()) {
            return this.materialButtonHelper.getSupportBackgroundTintList();
        }
        return super.getSupportBackgroundTintList();
    }

    @Override
    public PorterDuff.Mode getSupportBackgroundTintMode() {
        if (this.isUsingOriginalBackground()) {
            return this.materialButtonHelper.getSupportBackgroundTintMode();
        }
        return super.getSupportBackgroundTintMode();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (Build.VERSION.SDK_INT < 21 && this.isUsingOriginalBackground()) {
            this.materialButtonHelper.drawStroke(canvas);
        }
    }

    @Override
    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        MaterialButtonHelper materialButtonHelper;
        super.onLayout(bl, n, n2, n3, n4);
        if (Build.VERSION.SDK_INT == 21 && (materialButtonHelper = this.materialButtonHelper) != null) {
            materialButtonHelper.updateMaskBounds(n4 - n2, n3 - n);
        }
    }

    protected void onMeasure(int n, int n2) {
        super.onMeasure(n, n2);
        if (this.icon != null && this.iconGravity == 2) {
            int n3 = (int)this.getPaint().measureText(this.getText().toString());
            n = n2 = this.iconSize;
            if (n2 == 0) {
                n = this.icon.getIntrinsicWidth();
            }
            n = n2 = (this.getMeasuredWidth() - n3 - ViewCompat.getPaddingEnd((View)this) - n - this.iconPadding - ViewCompat.getPaddingStart((View)this)) / 2;
            if (this.isLayoutRTL()) {
                n = -n2;
            }
            if (this.iconLeft != n) {
                this.iconLeft = n;
                this.updateIcon();
            }
            return;
        }
    }

    public void setBackground(Drawable drawable2) {
        this.setBackgroundDrawable(drawable2);
    }

    public void setBackgroundColor(int n) {
        if (this.isUsingOriginalBackground()) {
            this.materialButtonHelper.setBackgroundColor(n);
        } else {
            super.setBackgroundColor(n);
        }
    }

    @Override
    public void setBackgroundDrawable(Drawable drawable2) {
        if (this.isUsingOriginalBackground()) {
            if (drawable2 != this.getBackground()) {
                Log.i((String)LOG_TAG, (String)"Setting a custom background is not supported.");
                this.materialButtonHelper.setBackgroundOverwritten();
                super.setBackgroundDrawable(drawable2);
            } else {
                this.getBackground().setState(drawable2.getState());
            }
        } else {
            super.setBackgroundDrawable(drawable2);
        }
    }

    @Override
    public void setBackgroundResource(int n) {
        Drawable drawable2 = null;
        if (n != 0) {
            drawable2 = AppCompatResources.getDrawable(this.getContext(), n);
        }
        this.setBackgroundDrawable(drawable2);
    }

    public void setBackgroundTintList(ColorStateList colorStateList) {
        this.setSupportBackgroundTintList(colorStateList);
    }

    public void setBackgroundTintMode(PorterDuff.Mode mode) {
        this.setSupportBackgroundTintMode(mode);
    }

    public void setCornerRadius(int n) {
        if (this.isUsingOriginalBackground()) {
            this.materialButtonHelper.setCornerRadius(n);
        }
    }

    public void setCornerRadiusResource(int n) {
        if (this.isUsingOriginalBackground()) {
            this.setCornerRadius(this.getResources().getDimensionPixelSize(n));
        }
    }

    public void setIcon(Drawable drawable2) {
        if (this.icon != drawable2) {
            this.icon = drawable2;
            this.updateIcon();
        }
    }

    public void setIconGravity(int n) {
        this.iconGravity = n;
    }

    public void setIconPadding(int n) {
        if (this.iconPadding != n) {
            this.iconPadding = n;
            this.setCompoundDrawablePadding(n);
        }
    }

    public void setIconResource(int n) {
        Drawable drawable2 = null;
        if (n != 0) {
            drawable2 = AppCompatResources.getDrawable(this.getContext(), n);
        }
        this.setIcon(drawable2);
    }

    public void setIconSize(int n) {
        if (n >= 0) {
            if (this.iconSize != n) {
                this.iconSize = n;
                this.updateIcon();
            }
            return;
        }
        throw new IllegalArgumentException("iconSize cannot be less than 0");
    }

    public void setIconTint(ColorStateList colorStateList) {
        if (this.iconTint != colorStateList) {
            this.iconTint = colorStateList;
            this.updateIcon();
        }
    }

    public void setIconTintMode(PorterDuff.Mode mode) {
        if (this.iconTintMode != mode) {
            this.iconTintMode = mode;
            this.updateIcon();
        }
    }

    public void setIconTintResource(int n) {
        this.setIconTint(AppCompatResources.getColorStateList(this.getContext(), n));
    }

    void setInternalBackground(Drawable drawable2) {
        super.setBackgroundDrawable(drawable2);
    }

    public void setRippleColor(ColorStateList colorStateList) {
        if (this.isUsingOriginalBackground()) {
            this.materialButtonHelper.setRippleColor(colorStateList);
        }
    }

    public void setRippleColorResource(int n) {
        if (this.isUsingOriginalBackground()) {
            this.setRippleColor(AppCompatResources.getColorStateList(this.getContext(), n));
        }
    }

    public void setStrokeColor(ColorStateList colorStateList) {
        if (this.isUsingOriginalBackground()) {
            this.materialButtonHelper.setStrokeColor(colorStateList);
        }
    }

    public void setStrokeColorResource(int n) {
        if (this.isUsingOriginalBackground()) {
            this.setStrokeColor(AppCompatResources.getColorStateList(this.getContext(), n));
        }
    }

    public void setStrokeWidth(int n) {
        if (this.isUsingOriginalBackground()) {
            this.materialButtonHelper.setStrokeWidth(n);
        }
    }

    public void setStrokeWidthResource(int n) {
        if (this.isUsingOriginalBackground()) {
            this.setStrokeWidth(this.getResources().getDimensionPixelSize(n));
        }
    }

    @Override
    public void setSupportBackgroundTintList(ColorStateList colorStateList) {
        if (this.isUsingOriginalBackground()) {
            this.materialButtonHelper.setSupportBackgroundTintList(colorStateList);
        } else if (this.materialButtonHelper != null) {
            super.setSupportBackgroundTintList(colorStateList);
        }
    }

    @Override
    public void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        if (this.isUsingOriginalBackground()) {
            this.materialButtonHelper.setSupportBackgroundTintMode(mode);
        } else if (this.materialButtonHelper != null) {
            super.setSupportBackgroundTintMode(mode);
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface IconGravity {
    }
}

