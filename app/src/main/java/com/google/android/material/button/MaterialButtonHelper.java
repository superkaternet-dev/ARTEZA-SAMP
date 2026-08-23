/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.content.res.TypedArray
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.Paint$Style
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.GradientDrawable
 *  android.graphics.drawable.InsetDrawable
 *  android.graphics.drawable.LayerDrawable
 *  android.graphics.drawable.RippleDrawable
 *  android.os.Build$VERSION
 *  android.view.View
 */
package com.google.android.material.button;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.view.View;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonBackgroundDrawable;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.ripple.RippleUtils;

class MaterialButtonHelper {
    private static final float CORNER_RADIUS_ADJUSTMENT = 1.0E-5f;
    private static final int DEFAULT_BACKGROUND_COLOR = -1;
    private static final boolean IS_LOLLIPOP;
    private GradientDrawable backgroundDrawableLollipop;
    private boolean backgroundOverwritten = false;
    private ColorStateList backgroundTint;
    private PorterDuff.Mode backgroundTintMode;
    private final Rect bounds;
    private final Paint buttonStrokePaint = new Paint(1);
    private GradientDrawable colorableBackgroundDrawableCompat;
    private int cornerRadius;
    private int insetBottom;
    private int insetLeft;
    private int insetRight;
    private int insetTop;
    private GradientDrawable maskDrawableLollipop;
    private final MaterialButton materialButton;
    private final RectF rectF;
    private ColorStateList rippleColor;
    private GradientDrawable rippleDrawableCompat;
    private ColorStateList strokeColor;
    private GradientDrawable strokeDrawableLollipop;
    private int strokeWidth;
    private Drawable tintableBackgroundDrawableCompat;
    private Drawable tintableRippleDrawableCompat;

    static {
        boolean bl = Build.VERSION.SDK_INT >= 21;
        IS_LOLLIPOP = bl;
    }

    public MaterialButtonHelper(MaterialButton materialButton) {
        this.bounds = new Rect();
        this.rectF = new RectF();
        this.materialButton = materialButton;
    }

    private Drawable createBackgroundCompat() {
        GradientDrawable gradientDrawable;
        this.colorableBackgroundDrawableCompat = gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius((float)this.cornerRadius + 1.0E-5f);
        this.colorableBackgroundDrawableCompat.setColor(-1);
        gradientDrawable = DrawableCompat.wrap((Drawable)this.colorableBackgroundDrawableCompat);
        this.tintableBackgroundDrawableCompat = gradientDrawable;
        DrawableCompat.setTintList((Drawable)gradientDrawable, this.backgroundTint);
        gradientDrawable = this.backgroundTintMode;
        if (gradientDrawable != null) {
            DrawableCompat.setTintMode(this.tintableBackgroundDrawableCompat, (PorterDuff.Mode)gradientDrawable);
        }
        this.rippleDrawableCompat = gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius((float)this.cornerRadius + 1.0E-5f);
        this.rippleDrawableCompat.setColor(-1);
        gradientDrawable = DrawableCompat.wrap((Drawable)this.rippleDrawableCompat);
        this.tintableRippleDrawableCompat = gradientDrawable;
        DrawableCompat.setTintList((Drawable)gradientDrawable, this.rippleColor);
        return this.wrapDrawableWithInset((Drawable)new LayerDrawable(new Drawable[]{this.tintableBackgroundDrawableCompat, this.tintableRippleDrawableCompat}));
    }

    private Drawable createBackgroundLollipop() {
        GradientDrawable gradientDrawable;
        GradientDrawable gradientDrawable2;
        this.backgroundDrawableLollipop = gradientDrawable2 = new GradientDrawable();
        gradientDrawable2.setCornerRadius((float)this.cornerRadius + 1.0E-5f);
        this.backgroundDrawableLollipop.setColor(-1);
        this.updateTintAndTintModeLollipop();
        this.strokeDrawableLollipop = gradientDrawable2 = new GradientDrawable();
        gradientDrawable2.setCornerRadius((float)this.cornerRadius + 1.0E-5f);
        this.strokeDrawableLollipop.setColor(0);
        this.strokeDrawableLollipop.setStroke(this.strokeWidth, this.strokeColor);
        gradientDrawable2 = this.wrapDrawableWithInset((Drawable)new LayerDrawable(new Drawable[]{this.backgroundDrawableLollipop, this.strokeDrawableLollipop}));
        this.maskDrawableLollipop = gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius((float)this.cornerRadius + 1.0E-5f);
        this.maskDrawableLollipop.setColor(-1);
        return new MaterialButtonBackgroundDrawable(RippleUtils.convertToRippleDrawableColor(this.rippleColor), (InsetDrawable)gradientDrawable2, (Drawable)this.maskDrawableLollipop);
    }

    private GradientDrawable unwrapBackgroundDrawable() {
        if (IS_LOLLIPOP && this.materialButton.getBackground() != null) {
            return (GradientDrawable)((LayerDrawable)((InsetDrawable)((RippleDrawable)this.materialButton.getBackground()).getDrawable(0)).getDrawable()).getDrawable(0);
        }
        return null;
    }

    private GradientDrawable unwrapStrokeDrawable() {
        if (IS_LOLLIPOP && this.materialButton.getBackground() != null) {
            return (GradientDrawable)((LayerDrawable)((InsetDrawable)((RippleDrawable)this.materialButton.getBackground()).getDrawable(0)).getDrawable()).getDrawable(1);
        }
        return null;
    }

    private void updateStroke() {
        boolean bl = IS_LOLLIPOP;
        if (bl && this.strokeDrawableLollipop != null) {
            this.materialButton.setInternalBackground(this.createBackgroundLollipop());
        } else if (!bl) {
            this.materialButton.invalidate();
        }
    }

    private void updateTintAndTintModeLollipop() {
        GradientDrawable gradientDrawable = this.backgroundDrawableLollipop;
        if (gradientDrawable != null) {
            DrawableCompat.setTintList((Drawable)gradientDrawable, this.backgroundTint);
            gradientDrawable = this.backgroundTintMode;
            if (gradientDrawable != null) {
                DrawableCompat.setTintMode((Drawable)this.backgroundDrawableLollipop, (PorterDuff.Mode)gradientDrawable);
            }
        }
    }

    private InsetDrawable wrapDrawableWithInset(Drawable drawable2) {
        return new InsetDrawable(drawable2, this.insetLeft, this.insetTop, this.insetRight, this.insetBottom);
    }

    void drawStroke(Canvas canvas) {
        if (canvas != null && this.strokeColor != null && this.strokeWidth > 0) {
            this.bounds.set(this.materialButton.getBackground().getBounds());
            this.rectF.set((float)this.bounds.left + (float)this.strokeWidth / 2.0f + (float)this.insetLeft, (float)this.bounds.top + (float)this.strokeWidth / 2.0f + (float)this.insetTop, (float)this.bounds.right - (float)this.strokeWidth / 2.0f - (float)this.insetRight, (float)this.bounds.bottom - (float)this.strokeWidth / 2.0f - (float)this.insetBottom);
            float f = (float)this.cornerRadius - (float)this.strokeWidth / 2.0f;
            canvas.drawRoundRect(this.rectF, f, f, this.buttonStrokePaint);
        }
    }

    int getCornerRadius() {
        return this.cornerRadius;
    }

    ColorStateList getRippleColor() {
        return this.rippleColor;
    }

    ColorStateList getStrokeColor() {
        return this.strokeColor;
    }

    int getStrokeWidth() {
        return this.strokeWidth;
    }

    ColorStateList getSupportBackgroundTintList() {
        return this.backgroundTint;
    }

    PorterDuff.Mode getSupportBackgroundTintMode() {
        return this.backgroundTintMode;
    }

    boolean isBackgroundOverwritten() {
        return this.backgroundOverwritten;
    }

    public void loadFromAttributes(TypedArray typedArray) {
        int n = R.styleable.MaterialButton_android_insetLeft;
        int n2 = 0;
        this.insetLeft = typedArray.getDimensionPixelOffset(n, 0);
        this.insetRight = typedArray.getDimensionPixelOffset(R.styleable.MaterialButton_android_insetRight, 0);
        this.insetTop = typedArray.getDimensionPixelOffset(R.styleable.MaterialButton_android_insetTop, 0);
        this.insetBottom = typedArray.getDimensionPixelOffset(R.styleable.MaterialButton_android_insetBottom, 0);
        this.cornerRadius = typedArray.getDimensionPixelSize(R.styleable.MaterialButton_cornerRadius, 0);
        this.strokeWidth = typedArray.getDimensionPixelSize(R.styleable.MaterialButton_strokeWidth, 0);
        this.backgroundTintMode = ViewUtils.parseTintMode(typedArray.getInt(R.styleable.MaterialButton_backgroundTintMode, -1), PorterDuff.Mode.SRC_IN);
        this.backgroundTint = MaterialResources.getColorStateList(this.materialButton.getContext(), typedArray, R.styleable.MaterialButton_backgroundTint);
        this.strokeColor = MaterialResources.getColorStateList(this.materialButton.getContext(), typedArray, R.styleable.MaterialButton_strokeColor);
        this.rippleColor = MaterialResources.getColorStateList(this.materialButton.getContext(), typedArray, R.styleable.MaterialButton_rippleColor);
        this.buttonStrokePaint.setStyle(Paint.Style.STROKE);
        this.buttonStrokePaint.setStrokeWidth((float)this.strokeWidth);
        Object object = this.buttonStrokePaint;
        typedArray = this.strokeColor;
        if (typedArray != null) {
            n2 = typedArray.getColorForState(this.materialButton.getDrawableState(), 0);
        }
        object.setColor(n2);
        n2 = ViewCompat.getPaddingStart((View)this.materialButton);
        int n3 = this.materialButton.getPaddingTop();
        int n4 = ViewCompat.getPaddingEnd((View)this.materialButton);
        n = this.materialButton.getPaddingBottom();
        object = this.materialButton;
        typedArray = IS_LOLLIPOP ? this.createBackgroundLollipop() : this.createBackgroundCompat();
        ((MaterialButton)object).setInternalBackground((Drawable)typedArray);
        ViewCompat.setPaddingRelative((View)this.materialButton, this.insetLeft + n2, this.insetTop + n3, this.insetRight + n4, this.insetBottom + n);
    }

    void setBackgroundColor(int n) {
        GradientDrawable gradientDrawable;
        boolean bl = IS_LOLLIPOP;
        if (bl && (gradientDrawable = this.backgroundDrawableLollipop) != null) {
            gradientDrawable.setColor(n);
        } else if (!bl && (gradientDrawable = this.colorableBackgroundDrawableCompat) != null) {
            gradientDrawable.setColor(n);
        }
    }

    void setBackgroundOverwritten() {
        this.backgroundOverwritten = true;
        this.materialButton.setSupportBackgroundTintList(this.backgroundTint);
        this.materialButton.setSupportBackgroundTintMode(this.backgroundTintMode);
    }

    void setCornerRadius(int n) {
        if (this.cornerRadius != n) {
            GradientDrawable gradientDrawable;
            this.cornerRadius = n;
            boolean bl = IS_LOLLIPOP;
            if (bl && this.backgroundDrawableLollipop != null && this.strokeDrawableLollipop != null && this.maskDrawableLollipop != null) {
                if (Build.VERSION.SDK_INT == 21) {
                    this.unwrapBackgroundDrawable().setCornerRadius((float)n + 1.0E-5f);
                    this.unwrapStrokeDrawable().setCornerRadius((float)n + 1.0E-5f);
                }
                this.backgroundDrawableLollipop.setCornerRadius((float)n + 1.0E-5f);
                this.strokeDrawableLollipop.setCornerRadius((float)n + 1.0E-5f);
                this.maskDrawableLollipop.setCornerRadius((float)n + 1.0E-5f);
            } else if (!bl && (gradientDrawable = this.colorableBackgroundDrawableCompat) != null && this.rippleDrawableCompat != null) {
                gradientDrawable.setCornerRadius((float)n + 1.0E-5f);
                this.rippleDrawableCompat.setCornerRadius((float)n + 1.0E-5f);
                this.materialButton.invalidate();
            }
        }
    }

    void setRippleColor(ColorStateList colorStateList) {
        if (this.rippleColor != colorStateList) {
            Drawable drawable2;
            this.rippleColor = colorStateList;
            boolean bl = IS_LOLLIPOP;
            if (bl && this.materialButton.getBackground() instanceof RippleDrawable) {
                ((RippleDrawable)this.materialButton.getBackground()).setColor(colorStateList);
            } else if (!bl && (drawable2 = this.tintableRippleDrawableCompat) != null) {
                DrawableCompat.setTintList(drawable2, colorStateList);
            }
        }
    }

    void setStrokeColor(ColorStateList colorStateList) {
        if (this.strokeColor != colorStateList) {
            this.strokeColor = colorStateList;
            Paint paint = this.buttonStrokePaint;
            int n = 0;
            if (colorStateList != null) {
                n = colorStateList.getColorForState(this.materialButton.getDrawableState(), 0);
            }
            paint.setColor(n);
            this.updateStroke();
        }
    }

    void setStrokeWidth(int n) {
        if (this.strokeWidth != n) {
            this.strokeWidth = n;
            this.buttonStrokePaint.setStrokeWidth((float)n);
            this.updateStroke();
        }
    }

    void setSupportBackgroundTintList(ColorStateList colorStateList) {
        if (this.backgroundTint != colorStateList) {
            this.backgroundTint = colorStateList;
            if (IS_LOLLIPOP) {
                this.updateTintAndTintModeLollipop();
            } else {
                Drawable drawable2 = this.tintableBackgroundDrawableCompat;
                if (drawable2 != null) {
                    DrawableCompat.setTintList(drawable2, colorStateList);
                }
            }
        }
    }

    void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        if (this.backgroundTintMode != mode) {
            this.backgroundTintMode = mode;
            if (IS_LOLLIPOP) {
                this.updateTintAndTintModeLollipop();
            } else {
                Drawable drawable2 = this.tintableBackgroundDrawableCompat;
                if (drawable2 != null && mode != null) {
                    DrawableCompat.setTintMode(drawable2, mode);
                }
            }
        }
    }

    void updateMaskBounds(int n, int n2) {
        GradientDrawable gradientDrawable = this.maskDrawableLollipop;
        if (gradientDrawable != null) {
            gradientDrawable.setBounds(this.insetLeft, this.insetTop, n2 - this.insetRight, n - this.insetBottom);
        }
    }
}

