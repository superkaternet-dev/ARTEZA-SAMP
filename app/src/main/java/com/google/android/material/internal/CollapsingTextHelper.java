/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.TimeInterpolator
 *  android.content.res.ColorStateList
 *  android.content.res.TypedArray
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.Canvas
 *  android.graphics.Color
 *  android.graphics.Paint
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.graphics.Typeface
 *  android.os.Build$VERSION
 *  android.text.TextPaint
 *  android.text.TextUtils
 *  android.text.TextUtils$TruncateAt
 *  android.view.View
 */
package com.google.android.material.internal;

import android.animation.TimeInterpolator;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import androidx.appcompat.R;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.math.MathUtils;
import androidx.core.text.TextDirectionHeuristicCompat;
import androidx.core.text.TextDirectionHeuristicsCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.animation.AnimationUtils;

public final class CollapsingTextHelper {
    private static final boolean DEBUG_DRAW = false;
    private static final Paint DEBUG_DRAW_PAINT;
    private static final boolean USE_SCALING_TEXTURE;
    private boolean boundsChanged;
    private final Rect collapsedBounds;
    private float collapsedDrawX;
    private float collapsedDrawY;
    private int collapsedShadowColor;
    private float collapsedShadowDx;
    private float collapsedShadowDy;
    private float collapsedShadowRadius;
    private ColorStateList collapsedTextColor;
    private int collapsedTextGravity = 16;
    private float collapsedTextSize = 15.0f;
    private Typeface collapsedTypeface;
    private final RectF currentBounds;
    private float currentDrawX;
    private float currentDrawY;
    private float currentTextSize;
    private Typeface currentTypeface;
    private boolean drawTitle;
    private final Rect expandedBounds;
    private float expandedDrawX;
    private float expandedDrawY;
    private float expandedFraction;
    private int expandedShadowColor;
    private float expandedShadowDx;
    private float expandedShadowDy;
    private float expandedShadowRadius;
    private ColorStateList expandedTextColor;
    private int expandedTextGravity = 16;
    private float expandedTextSize = 15.0f;
    private Bitmap expandedTitleTexture;
    private Typeface expandedTypeface;
    private boolean isRtl;
    private TimeInterpolator positionInterpolator;
    private float scale;
    private int[] state;
    private CharSequence text;
    private final TextPaint textPaint;
    private TimeInterpolator textSizeInterpolator;
    private CharSequence textToDraw;
    private float textureAscent;
    private float textureDescent;
    private Paint texturePaint;
    private final TextPaint tmpPaint;
    private boolean useTexture;
    private final View view;

    static {
        boolean bl = Build.VERSION.SDK_INT < 18;
        USE_SCALING_TEXTURE = bl;
        DEBUG_DRAW_PAINT = null;
    }

    public CollapsingTextHelper(View view) {
        this.view = view;
        view = new TextPaint(129);
        this.textPaint = view;
        this.tmpPaint = new TextPaint((Paint)view);
        this.collapsedBounds = new Rect();
        this.expandedBounds = new Rect();
        this.currentBounds = new RectF();
    }

    private static int blendColors(int n, int n2, float f) {
        float f2 = 1.0f - f;
        float f3 = Color.alpha((int)n);
        float f4 = Color.alpha((int)n2);
        float f5 = Color.red((int)n);
        float f6 = Color.red((int)n2);
        float f7 = Color.green((int)n);
        float f8 = Color.green((int)n2);
        float f9 = Color.blue((int)n);
        float f10 = Color.blue((int)n2);
        return Color.argb((int)((int)(f3 * f2 + f4 * f)), (int)((int)(f5 * f2 + f6 * f)), (int)((int)(f7 * f2 + f8 * f)), (int)((int)(f9 * f2 + f10 * f)));
    }

    private void calculateBaseOffsets() {
        float f;
        float f2 = this.currentTextSize;
        this.calculateUsingTextSize(this.collapsedTextSize);
        CharSequence charSequence = this.textToDraw;
        float f3 = 0.0f;
        float f4 = charSequence != null ? this.textPaint.measureText(charSequence, 0, charSequence.length()) : 0.0f;
        int n = GravityCompat.getAbsoluteGravity(this.collapsedTextGravity, this.isRtl ? 1 : 0);
        switch (n & 0x70) {
            default: {
                f = (this.textPaint.descent() - this.textPaint.ascent()) / 2.0f;
                float f5 = this.textPaint.descent();
                this.collapsedDrawY = (float)this.collapsedBounds.centerY() + (f - f5);
                break;
            }
            case 80: {
                this.collapsedDrawY = this.collapsedBounds.bottom;
                break;
            }
            case 48: {
                this.collapsedDrawY = (float)this.collapsedBounds.top - this.textPaint.ascent();
            }
        }
        switch (n & 0x800007) {
            default: {
                this.collapsedDrawX = this.collapsedBounds.left;
                break;
            }
            case 5: {
                this.collapsedDrawX = (float)this.collapsedBounds.right - f4;
                break;
            }
            case 1: {
                this.collapsedDrawX = (float)this.collapsedBounds.centerX() - f4 / 2.0f;
            }
        }
        this.calculateUsingTextSize(this.expandedTextSize);
        charSequence = this.textToDraw;
        f4 = f3;
        if (charSequence != null) {
            f4 = this.textPaint.measureText(charSequence, 0, charSequence.length());
        }
        n = GravityCompat.getAbsoluteGravity(this.expandedTextGravity, this.isRtl ? 1 : 0);
        switch (n & 0x70) {
            default: {
                f = (this.textPaint.descent() - this.textPaint.ascent()) / 2.0f;
                f3 = this.textPaint.descent();
                this.expandedDrawY = (float)this.expandedBounds.centerY() + (f - f3);
                break;
            }
            case 80: {
                this.expandedDrawY = this.expandedBounds.bottom;
                break;
            }
            case 48: {
                this.expandedDrawY = (float)this.expandedBounds.top - this.textPaint.ascent();
            }
        }
        switch (n & 0x800007) {
            default: {
                this.expandedDrawX = this.expandedBounds.left;
                break;
            }
            case 5: {
                this.expandedDrawX = (float)this.expandedBounds.right - f4;
                break;
            }
            case 1: {
                this.expandedDrawX = (float)this.expandedBounds.centerX() - f4 / 2.0f;
            }
        }
        this.clearTexture();
        this.setInterpolatedTextSize(f2);
    }

    private void calculateCurrentOffsets() {
        this.calculateOffsets(this.expandedFraction);
    }

    private boolean calculateIsRtl(CharSequence charSequence) {
        int n = ViewCompat.getLayoutDirection(this.view);
        boolean bl = true;
        if (n != 1) {
            bl = false;
        }
        TextDirectionHeuristicCompat textDirectionHeuristicCompat = bl ? TextDirectionHeuristicsCompat.FIRSTSTRONG_RTL : TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
        return textDirectionHeuristicCompat.isRtl(charSequence, 0, charSequence.length());
    }

    private void calculateOffsets(float f) {
        this.interpolateBounds(f);
        this.currentDrawX = CollapsingTextHelper.lerp(this.expandedDrawX, this.collapsedDrawX, f, this.positionInterpolator);
        this.currentDrawY = CollapsingTextHelper.lerp(this.expandedDrawY, this.collapsedDrawY, f, this.positionInterpolator);
        this.setInterpolatedTextSize(CollapsingTextHelper.lerp(this.expandedTextSize, this.collapsedTextSize, f, this.textSizeInterpolator));
        if (this.collapsedTextColor != this.expandedTextColor) {
            this.textPaint.setColor(CollapsingTextHelper.blendColors(this.getCurrentExpandedTextColor(), this.getCurrentCollapsedTextColor(), f));
        } else {
            this.textPaint.setColor(this.getCurrentCollapsedTextColor());
        }
        this.textPaint.setShadowLayer(CollapsingTextHelper.lerp(this.expandedShadowRadius, this.collapsedShadowRadius, f, null), CollapsingTextHelper.lerp(this.expandedShadowDx, this.collapsedShadowDx, f, null), CollapsingTextHelper.lerp(this.expandedShadowDy, this.collapsedShadowDy, f, null), CollapsingTextHelper.blendColors(this.expandedShadowColor, this.collapsedShadowColor, f));
        ViewCompat.postInvalidateOnAnimation(this.view);
    }

    private void calculateUsingTextSize(float f) {
        Object object;
        float f2;
        if (this.text == null) {
            return;
        }
        float f3 = this.collapsedBounds.width();
        float f4 = this.expandedBounds.width();
        boolean bl = false;
        boolean bl2 = false;
        if (CollapsingTextHelper.isClose(f, this.collapsedTextSize)) {
            f2 = this.collapsedTextSize;
            this.scale = 1.0f;
            Typeface typeface = this.currentTypeface;
            object = this.collapsedTypeface;
            bl = bl2;
            if (typeface != object) {
                this.currentTypeface = object;
                bl = true;
            }
            f = f3;
        } else {
            f2 = this.expandedTextSize;
            Typeface typeface = this.currentTypeface;
            object = this.expandedTypeface;
            if (typeface != object) {
                this.currentTypeface = object;
                bl = true;
            }
            this.scale = CollapsingTextHelper.isClose(f, this.expandedTextSize) ? 1.0f : f / this.expandedTextSize;
            f = this.collapsedTextSize / this.expandedTextSize;
            f = f4 * f > f3 ? Math.min(f3 / f, f4) : f4;
        }
        boolean bl3 = true;
        bl2 = bl;
        if (f > 0.0f) {
            bl = this.currentTextSize != f2 || this.boundsChanged || bl;
            this.currentTextSize = f2;
            this.boundsChanged = false;
            bl2 = bl;
        }
        if (this.textToDraw == null || bl2) {
            this.textPaint.setTextSize(this.currentTextSize);
            this.textPaint.setTypeface(this.currentTypeface);
            object = this.textPaint;
            if (this.scale == 1.0f) {
                bl3 = false;
            }
            object.setLinearText(bl3);
            object = TextUtils.ellipsize((CharSequence)this.text, (TextPaint)this.textPaint, (float)f, (TextUtils.TruncateAt)TextUtils.TruncateAt.END);
            if (!TextUtils.equals((CharSequence)object, (CharSequence)this.textToDraw)) {
                this.textToDraw = object;
                this.isRtl = this.calculateIsRtl((CharSequence)object);
            }
        }
    }

    private void clearTexture() {
        Bitmap bitmap = this.expandedTitleTexture;
        if (bitmap != null) {
            bitmap.recycle();
            this.expandedTitleTexture = null;
        }
    }

    private void ensureExpandedTexture() {
        if (this.expandedTitleTexture == null && !this.expandedBounds.isEmpty() && !TextUtils.isEmpty((CharSequence)this.textToDraw)) {
            this.calculateOffsets(0.0f);
            this.textureAscent = this.textPaint.ascent();
            this.textureDescent = this.textPaint.descent();
            TextPaint textPaint = this.textPaint;
            CharSequence charSequence = this.textToDraw;
            int n = Math.round(textPaint.measureText(charSequence, 0, charSequence.length()));
            int n2 = Math.round(this.textureDescent - this.textureAscent);
            if (n > 0 && n2 > 0) {
                this.expandedTitleTexture = Bitmap.createBitmap((int)n, (int)n2, (Bitmap.Config)Bitmap.Config.ARGB_8888);
                textPaint = new Canvas(this.expandedTitleTexture);
                charSequence = this.textToDraw;
                textPaint.drawText(charSequence, 0, charSequence.length(), 0.0f, (float)n2 - this.textPaint.descent(), (Paint)this.textPaint);
                if (this.texturePaint == null) {
                    this.texturePaint = new Paint(3);
                }
                return;
            }
            return;
        }
    }

    private int getCurrentExpandedTextColor() {
        int[] nArray = this.state;
        if (nArray != null) {
            return this.expandedTextColor.getColorForState(nArray, 0);
        }
        return this.expandedTextColor.getDefaultColor();
    }

    private void getTextPaintCollapsed(TextPaint textPaint) {
        textPaint.setTextSize(this.collapsedTextSize);
        textPaint.setTypeface(this.collapsedTypeface);
    }

    private void interpolateBounds(float f) {
        this.currentBounds.left = CollapsingTextHelper.lerp(this.expandedBounds.left, this.collapsedBounds.left, f, this.positionInterpolator);
        this.currentBounds.top = CollapsingTextHelper.lerp(this.expandedDrawY, this.collapsedDrawY, f, this.positionInterpolator);
        this.currentBounds.right = CollapsingTextHelper.lerp(this.expandedBounds.right, this.collapsedBounds.right, f, this.positionInterpolator);
        this.currentBounds.bottom = CollapsingTextHelper.lerp(this.expandedBounds.bottom, this.collapsedBounds.bottom, f, this.positionInterpolator);
    }

    private static boolean isClose(float f, float f2) {
        boolean bl = Math.abs(f - f2) < 0.001f;
        return bl;
    }

    private static float lerp(float f, float f2, float f3, TimeInterpolator timeInterpolator) {
        float f4 = f3;
        if (timeInterpolator != null) {
            f4 = timeInterpolator.getInterpolation(f3);
        }
        return AnimationUtils.lerp(f, f2, f4);
    }

    private Typeface readFontFamilyTypeface(int n) {
        TypedArray typedArray;
        block4: {
            typedArray = this.view.getContext().obtainStyledAttributes(n, new int[]{16843692});
            String string2 = typedArray.getString(0);
            if (string2 == null) break block4;
            string2 = Typeface.create((String)string2, (int)0);
            return string2;
        }
        typedArray.recycle();
        return null;
        finally {
            typedArray.recycle();
        }
    }

    private static boolean rectEquals(Rect rect, int n, int n2, int n3, int n4) {
        boolean bl = rect.left == n && rect.top == n2 && rect.right == n3 && rect.bottom == n4;
        return bl;
    }

    private void setInterpolatedTextSize(float f) {
        this.calculateUsingTextSize(f);
        boolean bl = USE_SCALING_TEXTURE && this.scale != 1.0f;
        this.useTexture = bl;
        if (bl) {
            this.ensureExpandedTexture();
        }
        ViewCompat.postInvalidateOnAnimation(this.view);
    }

    public float calculateCollapsedTextWidth() {
        if (this.text == null) {
            return 0.0f;
        }
        this.getTextPaintCollapsed(this.tmpPaint);
        TextPaint textPaint = this.tmpPaint;
        CharSequence charSequence = this.text;
        return textPaint.measureText(charSequence, 0, charSequence.length());
    }

    public void draw(Canvas canvas) {
        int n = canvas.save();
        if (this.textToDraw != null && this.drawTitle) {
            float f;
            float f2 = this.currentDrawX;
            float f3 = this.currentDrawY;
            boolean bl = this.useTexture && this.expandedTitleTexture != null;
            if (bl) {
                float f4 = this.textureAscent;
                float f5 = this.scale;
                f = this.textureDescent;
                f = f4 * f5;
            } else {
                float f6 = this.textPaint.ascent();
                float f7 = this.scale;
                this.textPaint.descent();
                f = this.scale;
                f = f6 * f7;
            }
            f = bl ? f3 + f : f3;
            f3 = this.scale;
            if (f3 != 1.0f) {
                canvas.scale(f3, f3, f2, f);
            }
            if (bl) {
                canvas.drawBitmap(this.expandedTitleTexture, f2, f, this.texturePaint);
            } else {
                CharSequence charSequence = this.textToDraw;
                canvas.drawText(charSequence, 0, charSequence.length(), f2, f, (Paint)this.textPaint);
            }
        }
        canvas.restoreToCount(n);
    }

    public void getCollapsedTextActualBounds(RectF rectF) {
        boolean bl = this.calculateIsRtl(this.text);
        Rect rect = this.collapsedBounds;
        float f = !bl ? (float)rect.left : (float)rect.right - this.calculateCollapsedTextWidth();
        rectF.left = f;
        rectF.top = this.collapsedBounds.top;
        f = !bl ? rectF.left + this.calculateCollapsedTextWidth() : (float)this.collapsedBounds.right;
        rectF.right = f;
        rectF.bottom = (float)this.collapsedBounds.top + this.getCollapsedTextHeight();
    }

    public ColorStateList getCollapsedTextColor() {
        return this.collapsedTextColor;
    }

    public int getCollapsedTextGravity() {
        return this.collapsedTextGravity;
    }

    public float getCollapsedTextHeight() {
        this.getTextPaintCollapsed(this.tmpPaint);
        return -this.tmpPaint.ascent();
    }

    public float getCollapsedTextSize() {
        return this.collapsedTextSize;
    }

    public Typeface getCollapsedTypeface() {
        Typeface typeface = this.collapsedTypeface;
        if (typeface == null) {
            typeface = Typeface.DEFAULT;
        }
        return typeface;
    }

    public int getCurrentCollapsedTextColor() {
        int[] nArray = this.state;
        if (nArray != null) {
            return this.collapsedTextColor.getColorForState(nArray, 0);
        }
        return this.collapsedTextColor.getDefaultColor();
    }

    public ColorStateList getExpandedTextColor() {
        return this.expandedTextColor;
    }

    public int getExpandedTextGravity() {
        return this.expandedTextGravity;
    }

    public float getExpandedTextSize() {
        return this.expandedTextSize;
    }

    public Typeface getExpandedTypeface() {
        Typeface typeface = this.expandedTypeface;
        if (typeface == null) {
            typeface = Typeface.DEFAULT;
        }
        return typeface;
    }

    public float getExpansionFraction() {
        return this.expandedFraction;
    }

    public CharSequence getText() {
        return this.text;
    }

    public final boolean isStateful() {
        ColorStateList colorStateList = this.collapsedTextColor;
        boolean bl = colorStateList != null && colorStateList.isStateful() || (colorStateList = this.expandedTextColor) != null && colorStateList.isStateful();
        return bl;
    }

    void onBoundsChanged() {
        boolean bl = this.collapsedBounds.width() > 0 && this.collapsedBounds.height() > 0 && this.expandedBounds.width() > 0 && this.expandedBounds.height() > 0;
        this.drawTitle = bl;
    }

    public void recalculate() {
        if (this.view.getHeight() > 0 && this.view.getWidth() > 0) {
            this.calculateBaseOffsets();
            this.calculateCurrentOffsets();
        }
    }

    public void setCollapsedBounds(int n, int n2, int n3, int n4) {
        if (!CollapsingTextHelper.rectEquals(this.collapsedBounds, n, n2, n3, n4)) {
            this.collapsedBounds.set(n, n2, n3, n4);
            this.boundsChanged = true;
            this.onBoundsChanged();
        }
    }

    public void setCollapsedTextAppearance(int n) {
        TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(this.view.getContext(), n, R.styleable.TextAppearance);
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColor)) {
            this.collapsedTextColor = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColor);
        }
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textSize)) {
            this.collapsedTextSize = tintTypedArray.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, (int)this.collapsedTextSize);
        }
        this.collapsedShadowColor = tintTypedArray.getInt(R.styleable.TextAppearance_android_shadowColor, 0);
        this.collapsedShadowDx = tintTypedArray.getFloat(R.styleable.TextAppearance_android_shadowDx, 0.0f);
        this.collapsedShadowDy = tintTypedArray.getFloat(R.styleable.TextAppearance_android_shadowDy, 0.0f);
        this.collapsedShadowRadius = tintTypedArray.getFloat(R.styleable.TextAppearance_android_shadowRadius, 0.0f);
        tintTypedArray.recycle();
        if (Build.VERSION.SDK_INT >= 16) {
            this.collapsedTypeface = this.readFontFamilyTypeface(n);
        }
        this.recalculate();
    }

    public void setCollapsedTextColor(ColorStateList colorStateList) {
        if (this.collapsedTextColor != colorStateList) {
            this.collapsedTextColor = colorStateList;
            this.recalculate();
        }
    }

    public void setCollapsedTextGravity(int n) {
        if (this.collapsedTextGravity != n) {
            this.collapsedTextGravity = n;
            this.recalculate();
        }
    }

    public void setCollapsedTextSize(float f) {
        if (this.collapsedTextSize != f) {
            this.collapsedTextSize = f;
            this.recalculate();
        }
    }

    public void setCollapsedTypeface(Typeface typeface) {
        if (this.collapsedTypeface != typeface) {
            this.collapsedTypeface = typeface;
            this.recalculate();
        }
    }

    public void setExpandedBounds(int n, int n2, int n3, int n4) {
        if (!CollapsingTextHelper.rectEquals(this.expandedBounds, n, n2, n3, n4)) {
            this.expandedBounds.set(n, n2, n3, n4);
            this.boundsChanged = true;
            this.onBoundsChanged();
        }
    }

    public void setExpandedTextAppearance(int n) {
        TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(this.view.getContext(), n, R.styleable.TextAppearance);
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColor)) {
            this.expandedTextColor = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColor);
        }
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textSize)) {
            this.expandedTextSize = tintTypedArray.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, (int)this.expandedTextSize);
        }
        this.expandedShadowColor = tintTypedArray.getInt(R.styleable.TextAppearance_android_shadowColor, 0);
        this.expandedShadowDx = tintTypedArray.getFloat(R.styleable.TextAppearance_android_shadowDx, 0.0f);
        this.expandedShadowDy = tintTypedArray.getFloat(R.styleable.TextAppearance_android_shadowDy, 0.0f);
        this.expandedShadowRadius = tintTypedArray.getFloat(R.styleable.TextAppearance_android_shadowRadius, 0.0f);
        tintTypedArray.recycle();
        if (Build.VERSION.SDK_INT >= 16) {
            this.expandedTypeface = this.readFontFamilyTypeface(n);
        }
        this.recalculate();
    }

    public void setExpandedTextColor(ColorStateList colorStateList) {
        if (this.expandedTextColor != colorStateList) {
            this.expandedTextColor = colorStateList;
            this.recalculate();
        }
    }

    public void setExpandedTextGravity(int n) {
        if (this.expandedTextGravity != n) {
            this.expandedTextGravity = n;
            this.recalculate();
        }
    }

    public void setExpandedTextSize(float f) {
        if (this.expandedTextSize != f) {
            this.expandedTextSize = f;
            this.recalculate();
        }
    }

    public void setExpandedTypeface(Typeface typeface) {
        if (this.expandedTypeface != typeface) {
            this.expandedTypeface = typeface;
            this.recalculate();
        }
    }

    public void setExpansionFraction(float f) {
        if ((f = MathUtils.clamp(f, 0.0f, 1.0f)) != this.expandedFraction) {
            this.expandedFraction = f;
            this.calculateCurrentOffsets();
        }
    }

    public void setPositionInterpolator(TimeInterpolator timeInterpolator) {
        this.positionInterpolator = timeInterpolator;
        this.recalculate();
    }

    public final boolean setState(int[] nArray) {
        this.state = nArray;
        if (this.isStateful()) {
            this.recalculate();
            return true;
        }
        return false;
    }

    public void setText(CharSequence charSequence) {
        if (charSequence == null || !charSequence.equals(this.text)) {
            this.text = charSequence;
            this.textToDraw = null;
            this.clearTexture();
            this.recalculate();
        }
    }

    public void setTextSizeInterpolator(TimeInterpolator timeInterpolator) {
        this.textSizeInterpolator = timeInterpolator;
        this.recalculate();
    }

    public void setTypefaces(Typeface typeface) {
        this.expandedTypeface = typeface;
        this.collapsedTypeface = typeface;
        this.recalculate();
    }
}

