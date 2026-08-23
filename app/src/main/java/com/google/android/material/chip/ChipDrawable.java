/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.TypedArray
 *  android.content.res.XmlResourceParser
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.Outline
 *  android.graphics.Paint
 *  android.graphics.Paint$Align
 *  android.graphics.Paint$FontMetrics
 *  android.graphics.Paint$Style
 *  android.graphics.PointF
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.PorterDuffColorFilter
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.graphics.Typeface
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$Callback
 *  android.text.TextPaint
 *  android.text.TextUtils
 *  android.text.TextUtils$TruncateAt
 *  android.util.AttributeSet
 *  android.util.Xml
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package com.google.android.material.chip;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Xml;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.graphics.drawable.TintAwareDrawable;
import androidx.core.text.BidiFormatter;
import com.google.android.material.R;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.canvas.CanvasCompat;
import com.google.android.material.drawable.DrawableUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.ripple.RippleUtils;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ChipDrawable
extends Drawable
implements TintAwareDrawable,
Drawable.Callback {
    private static final boolean DEBUG = false;
    private static final int[] DEFAULT_STATE = new int[]{16842910};
    private static final String NAMESPACE_APP = "http://schemas.android.com/apk/res-auto";
    private int alpha;
    private boolean checkable;
    private Drawable checkedIcon;
    private boolean checkedIconVisible;
    private ColorStateList chipBackgroundColor;
    private float chipCornerRadius;
    private float chipEndPadding;
    private Drawable chipIcon;
    private float chipIconSize;
    private ColorStateList chipIconTint;
    private boolean chipIconVisible;
    private float chipMinHeight;
    private final Paint chipPaint;
    private float chipStartPadding;
    private ColorStateList chipStrokeColor;
    private float chipStrokeWidth;
    private Drawable closeIcon;
    private CharSequence closeIconContentDescription;
    private float closeIconEndPadding;
    private float closeIconSize;
    private float closeIconStartPadding;
    private int[] closeIconStateSet;
    private ColorStateList closeIconTint;
    private boolean closeIconVisible;
    private ColorFilter colorFilter;
    private ColorStateList compatRippleColor;
    private final Context context;
    private boolean currentChecked;
    private int currentChipBackgroundColor;
    private int currentChipStrokeColor;
    private int currentCompatRippleColor;
    private int currentTextColor;
    private int currentTint;
    private final Paint debugPaint;
    private WeakReference<Delegate> delegate;
    private final ResourcesCompat.FontCallback fontCallback = new ResourcesCompat.FontCallback(this){
        final ChipDrawable this$0;
        {
            this.this$0 = chipDrawable;
        }

        @Override
        public void onFontRetrievalFailed(int n) {
        }

        @Override
        public void onFontRetrieved(Typeface typeface) {
            ChipDrawable.access$002(this.this$0, true);
            this.this$0.onSizeChange();
            this.this$0.invalidateSelf();
        }
    };
    private final Paint.FontMetrics fontMetrics;
    private MotionSpec hideMotionSpec;
    private float iconEndPadding;
    private float iconStartPadding;
    private int maxWidth;
    private final PointF pointF;
    private CharSequence rawText;
    private final RectF rectF;
    private ColorStateList rippleColor;
    private boolean shouldDrawText;
    private MotionSpec showMotionSpec;
    private TextAppearance textAppearance;
    private float textEndPadding;
    private final TextPaint textPaint;
    private float textStartPadding;
    private float textWidth;
    private boolean textWidthDirty;
    private ColorStateList tint;
    private PorterDuffColorFilter tintFilter;
    private PorterDuff.Mode tintMode;
    private TextUtils.TruncateAt truncateAt;
    private CharSequence unicodeWrappedText;
    private boolean useCompatRipple;

    private ChipDrawable(Context object) {
        TextPaint textPaint;
        this.textPaint = textPaint = new TextPaint(1);
        this.chipPaint = new Paint(1);
        this.fontMetrics = new Paint.FontMetrics();
        this.rectF = new RectF();
        this.pointF = new PointF();
        this.alpha = 255;
        this.tintMode = PorterDuff.Mode.SRC_IN;
        this.delegate = new WeakReference<Object>(null);
        this.textWidthDirty = true;
        this.context = object;
        this.rawText = "";
        textPaint.density = object.getResources().getDisplayMetrics().density;
        this.debugPaint = null;
        object = DEFAULT_STATE;
        this.setState((int[])object);
        this.setCloseIconState((int[])object);
        this.shouldDrawText = true;
    }

    static /* synthetic */ boolean access$002(ChipDrawable chipDrawable, boolean bl) {
        chipDrawable.textWidthDirty = bl;
        return bl;
    }

    private void applyChildDrawable(Drawable drawable2) {
        if (drawable2 != null) {
            drawable2.setCallback((Drawable.Callback)this);
            DrawableCompat.setLayoutDirection(drawable2, DrawableCompat.getLayoutDirection(this));
            drawable2.setLevel(this.getLevel());
            drawable2.setVisible(this.isVisible(), false);
            if (drawable2 == this.closeIcon) {
                if (drawable2.isStateful()) {
                    drawable2.setState(this.getCloseIconState());
                }
                DrawableCompat.setTintList(drawable2, this.closeIconTint);
            } else if (drawable2.isStateful()) {
                drawable2.setState(this.getState());
            }
        }
    }

    private void calculateChipIconBounds(Rect rect, RectF rectF) {
        rectF.setEmpty();
        if (this.showsChipIcon() || this.showsCheckedIcon()) {
            float f = this.chipStartPadding + this.iconStartPadding;
            if (DrawableCompat.getLayoutDirection(this) == 0) {
                rectF.left = (float)rect.left + f;
                rectF.right = rectF.left + this.chipIconSize;
            } else {
                rectF.right = (float)rect.right - f;
                rectF.left = rectF.right - this.chipIconSize;
            }
            rectF.top = rect.exactCenterY() - this.chipIconSize / 2.0f;
            rectF.bottom = rectF.top + this.chipIconSize;
        }
    }

    private void calculateChipTouchBounds(Rect rect, RectF rectF) {
        rectF.set(rect);
        if (this.showsCloseIcon()) {
            float f = this.chipEndPadding + this.closeIconEndPadding + this.closeIconSize + this.closeIconStartPadding + this.textEndPadding;
            if (DrawableCompat.getLayoutDirection(this) == 0) {
                rectF.right = (float)rect.right - f;
            } else {
                rectF.left = (float)rect.left + f;
            }
        }
    }

    private void calculateCloseIconBounds(Rect rect, RectF rectF) {
        rectF.setEmpty();
        if (this.showsCloseIcon()) {
            float f = this.chipEndPadding + this.closeIconEndPadding;
            if (DrawableCompat.getLayoutDirection(this) == 0) {
                rectF.right = (float)rect.right - f;
                rectF.left = rectF.right - this.closeIconSize;
            } else {
                rectF.left = (float)rect.left + f;
                rectF.right = rectF.left + this.closeIconSize;
            }
            rectF.top = rect.exactCenterY() - this.closeIconSize / 2.0f;
            rectF.bottom = rectF.top + this.closeIconSize;
        }
    }

    private void calculateCloseIconTouchBounds(Rect rect, RectF rectF) {
        rectF.setEmpty();
        if (this.showsCloseIcon()) {
            float f = this.chipEndPadding + this.closeIconEndPadding + this.closeIconSize + this.closeIconStartPadding + this.textEndPadding;
            if (DrawableCompat.getLayoutDirection(this) == 0) {
                rectF.right = rect.right;
                rectF.left = rectF.right - f;
            } else {
                rectF.left = rect.left;
                rectF.right = (float)rect.left + f;
            }
            rectF.top = rect.top;
            rectF.bottom = rect.bottom;
        }
    }

    private float calculateCloseIconWidth() {
        if (this.showsCloseIcon()) {
            return this.closeIconStartPadding + this.closeIconSize + this.closeIconEndPadding;
        }
        return 0.0f;
    }

    private void calculateTextBounds(Rect rect, RectF rectF) {
        rectF.setEmpty();
        if (this.unicodeWrappedText != null) {
            float f = this.chipStartPadding + this.calculateChipIconWidth() + this.textStartPadding;
            float f2 = this.chipEndPadding + this.calculateCloseIconWidth() + this.textEndPadding;
            if (DrawableCompat.getLayoutDirection(this) == 0) {
                rectF.left = (float)rect.left + f;
                rectF.right = (float)rect.right - f2;
            } else {
                rectF.left = (float)rect.left + f2;
                rectF.right = (float)rect.right - f;
            }
            rectF.top = rect.top;
            rectF.bottom = rect.bottom;
        }
    }

    private float calculateTextCenterFromBaseline() {
        this.textPaint.getFontMetrics(this.fontMetrics);
        return (this.fontMetrics.descent + this.fontMetrics.ascent) / 2.0f;
    }

    private float calculateTextWidth(CharSequence charSequence) {
        if (charSequence == null) {
            return 0.0f;
        }
        return this.textPaint.measureText(charSequence, 0, charSequence.length());
    }

    private boolean canShowCheckedIcon() {
        boolean bl = this.checkedIconVisible && this.checkedIcon != null && this.checkable;
        return bl;
    }

    public static ChipDrawable createFromAttributes(Context object, AttributeSet attributeSet, int n, int n2) {
        object = new ChipDrawable((Context)object);
        super.loadFromAttributes(attributeSet, n, n2);
        return object;
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static ChipDrawable createFromResource(Context object, int n) {
        void var0_5;
        void var1_6;
        try {
            int n2;
            XmlResourceParser xmlResourceParser = object.getResources().getXml((int)var1_6);
            while ((n2 = xmlResourceParser.next()) != 2 && n2 != 1) {
            }
            if (n2 != 2) {
                XmlPullParserException xmlPullParserException = new XmlPullParserException("No start tag found");
                throw xmlPullParserException;
            }
            if (TextUtils.equals((CharSequence)xmlResourceParser.getName(), (CharSequence)"chip")) {
                int n3;
                AttributeSet attributeSet = Xml.asAttributeSet((XmlPullParser)xmlResourceParser);
                n2 = n3 = attributeSet.getStyleAttribute();
                if (n3 != 0) return ChipDrawable.createFromAttributes(object, attributeSet, R.attr.chipStandaloneStyle, n2);
                n2 = R.style.Widget_MaterialComponents_Chip_Entry;
                return ChipDrawable.createFromAttributes(object, attributeSet, R.attr.chipStandaloneStyle, n2);
            }
            XmlPullParserException xmlPullParserException = new XmlPullParserException("Must have a <chip> start tag");
            throw xmlPullParserException;
        }
        catch (IOException iOException) {
        }
        catch (XmlPullParserException xmlPullParserException) {
            // empty catch block
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Can't load chip resource ID #0x");
        stringBuilder.append(Integer.toHexString((int)var1_6));
        Resources.NotFoundException notFoundException = new Resources.NotFoundException(stringBuilder.toString());
        notFoundException.initCause((Throwable)var0_5);
        throw notFoundException;
    }

    private void drawCheckedIcon(Canvas canvas, Rect rect) {
        if (this.showsCheckedIcon()) {
            this.calculateChipIconBounds(rect, this.rectF);
            float f = this.rectF.left;
            float f2 = this.rectF.top;
            canvas.translate(f, f2);
            this.checkedIcon.setBounds(0, 0, (int)this.rectF.width(), (int)this.rectF.height());
            this.checkedIcon.draw(canvas);
            canvas.translate(-f, -f2);
        }
    }

    private void drawChipBackground(Canvas canvas, Rect rect) {
        this.chipPaint.setColor(this.currentChipBackgroundColor);
        this.chipPaint.setStyle(Paint.Style.FILL);
        this.chipPaint.setColorFilter(this.getTintColorFilter());
        this.rectF.set(rect);
        rect = this.rectF;
        float f = this.chipCornerRadius;
        canvas.drawRoundRect((RectF)rect, f, f, this.chipPaint);
    }

    private void drawChipIcon(Canvas canvas, Rect rect) {
        if (this.showsChipIcon()) {
            this.calculateChipIconBounds(rect, this.rectF);
            float f = this.rectF.left;
            float f2 = this.rectF.top;
            canvas.translate(f, f2);
            this.chipIcon.setBounds(0, 0, (int)this.rectF.width(), (int)this.rectF.height());
            this.chipIcon.draw(canvas);
            canvas.translate(-f, -f2);
        }
    }

    private void drawChipStroke(Canvas canvas, Rect rect) {
        if (this.chipStrokeWidth > 0.0f) {
            this.chipPaint.setColor(this.currentChipStrokeColor);
            this.chipPaint.setStyle(Paint.Style.STROKE);
            this.chipPaint.setColorFilter(this.getTintColorFilter());
            this.rectF.set((float)rect.left + this.chipStrokeWidth / 2.0f, (float)rect.top + this.chipStrokeWidth / 2.0f, (float)rect.right - this.chipStrokeWidth / 2.0f, (float)rect.bottom - this.chipStrokeWidth / 2.0f);
            float f = this.chipCornerRadius - this.chipStrokeWidth / 2.0f;
            canvas.drawRoundRect(this.rectF, f, f, this.chipPaint);
        }
    }

    private void drawCloseIcon(Canvas canvas, Rect rect) {
        if (this.showsCloseIcon()) {
            this.calculateCloseIconBounds(rect, this.rectF);
            float f = this.rectF.left;
            float f2 = this.rectF.top;
            canvas.translate(f, f2);
            this.closeIcon.setBounds(0, 0, (int)this.rectF.width(), (int)this.rectF.height());
            this.closeIcon.draw(canvas);
            canvas.translate(-f, -f2);
        }
    }

    private void drawCompatRipple(Canvas canvas, Rect rect) {
        this.chipPaint.setColor(this.currentCompatRippleColor);
        this.chipPaint.setStyle(Paint.Style.FILL);
        this.rectF.set(rect);
        rect = this.rectF;
        float f = this.chipCornerRadius;
        canvas.drawRoundRect((RectF)rect, f, f, this.chipPaint);
    }

    private void drawDebug(Canvas canvas, Rect rect) {
        Paint paint = this.debugPaint;
        if (paint != null) {
            paint.setColor(ColorUtils.setAlphaComponent(-16777216, 127));
            canvas.drawRect(rect, this.debugPaint);
            if (this.showsChipIcon() || this.showsCheckedIcon()) {
                this.calculateChipIconBounds(rect, this.rectF);
                canvas.drawRect(this.rectF, this.debugPaint);
            }
            if (this.unicodeWrappedText != null) {
                canvas.drawLine((float)rect.left, rect.exactCenterY(), (float)rect.right, rect.exactCenterY(), this.debugPaint);
            }
            if (this.showsCloseIcon()) {
                this.calculateCloseIconBounds(rect, this.rectF);
                canvas.drawRect(this.rectF, this.debugPaint);
            }
            this.debugPaint.setColor(ColorUtils.setAlphaComponent(-65536, 127));
            this.calculateChipTouchBounds(rect, this.rectF);
            canvas.drawRect(this.rectF, this.debugPaint);
            this.debugPaint.setColor(ColorUtils.setAlphaComponent(-16711936, 127));
            this.calculateCloseIconTouchBounds(rect, this.rectF);
            canvas.drawRect(this.rectF, this.debugPaint);
        }
    }

    private void drawText(Canvas canvas, Rect object) {
        if (this.unicodeWrappedText != null) {
            Object object2 = this.calculateTextOriginAndAlignment((Rect)object, this.pointF);
            this.calculateTextBounds((Rect)object, this.rectF);
            if (this.textAppearance != null) {
                this.textPaint.drawableState = this.getState();
                this.textAppearance.updateDrawState(this.context, this.textPaint, this.fontCallback);
            }
            this.textPaint.setTextAlign(object2);
            boolean bl = Math.round(this.getTextWidth()) > Math.round(this.rectF.width());
            int n = 0;
            if (bl) {
                n = canvas.save();
                canvas.clipRect(this.rectF);
            }
            object2 = this.unicodeWrappedText;
            object = object2;
            if (bl) {
                object = object2;
                if (this.truncateAt != null) {
                    object = TextUtils.ellipsize((CharSequence)this.unicodeWrappedText, (TextPaint)this.textPaint, (float)this.rectF.width(), (TextUtils.TruncateAt)this.truncateAt);
                }
            }
            canvas.drawText((CharSequence)object, 0, object.length(), this.pointF.x, this.pointF.y, (Paint)this.textPaint);
            if (bl) {
                canvas.restoreToCount(n);
            }
        }
    }

    private float getTextWidth() {
        float f;
        if (!this.textWidthDirty) {
            return this.textWidth;
        }
        this.textWidth = f = this.calculateTextWidth(this.unicodeWrappedText);
        this.textWidthDirty = false;
        return f;
    }

    private ColorFilter getTintColorFilter() {
        ColorFilter colorFilter = this.colorFilter;
        if (colorFilter == null) {
            colorFilter = this.tintFilter;
        }
        return colorFilter;
    }

    private static boolean hasState(int[] nArray, int n) {
        if (nArray == null) {
            return false;
        }
        int n2 = nArray.length;
        for (int i = 0; i < n2; ++i) {
            if (nArray[i] != n) continue;
            return true;
        }
        return false;
    }

    private static boolean isStateful(ColorStateList colorStateList) {
        boolean bl = colorStateList != null && colorStateList.isStateful();
        return bl;
    }

    private static boolean isStateful(Drawable drawable2) {
        boolean bl = drawable2 != null && drawable2.isStateful();
        return bl;
    }

    private static boolean isStateful(TextAppearance textAppearance) {
        boolean bl = textAppearance != null && textAppearance.textColor != null && textAppearance.textColor.isStateful();
        return bl;
    }

    private void loadFromAttributes(AttributeSet attributeSet, int n, int n2) {
        TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(this.context, attributeSet, R.styleable.Chip, n, n2, new int[0]);
        this.setChipBackgroundColor(MaterialResources.getColorStateList(this.context, typedArray, R.styleable.Chip_chipBackgroundColor));
        this.setChipMinHeight(typedArray.getDimension(R.styleable.Chip_chipMinHeight, 0.0f));
        this.setChipCornerRadius(typedArray.getDimension(R.styleable.Chip_chipCornerRadius, 0.0f));
        this.setChipStrokeColor(MaterialResources.getColorStateList(this.context, typedArray, R.styleable.Chip_chipStrokeColor));
        this.setChipStrokeWidth(typedArray.getDimension(R.styleable.Chip_chipStrokeWidth, 0.0f));
        this.setRippleColor(MaterialResources.getColorStateList(this.context, typedArray, R.styleable.Chip_rippleColor));
        this.setText(typedArray.getText(R.styleable.Chip_android_text));
        this.setTextAppearance(MaterialResources.getTextAppearance(this.context, typedArray, R.styleable.Chip_android_textAppearance));
        switch (typedArray.getInt(R.styleable.Chip_android_ellipsize, 0)) {
            default: {
                break;
            }
            case 3: {
                this.setEllipsize(TextUtils.TruncateAt.END);
                break;
            }
            case 2: {
                this.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                break;
            }
            case 1: {
                this.setEllipsize(TextUtils.TruncateAt.START);
            }
        }
        this.setChipIconVisible(typedArray.getBoolean(R.styleable.Chip_chipIconVisible, false));
        if (attributeSet != null && attributeSet.getAttributeValue(NAMESPACE_APP, "chipIconEnabled") != null && attributeSet.getAttributeValue(NAMESPACE_APP, "chipIconVisible") == null) {
            this.setChipIconVisible(typedArray.getBoolean(R.styleable.Chip_chipIconEnabled, false));
        }
        this.setChipIcon(MaterialResources.getDrawable(this.context, typedArray, R.styleable.Chip_chipIcon));
        this.setChipIconTint(MaterialResources.getColorStateList(this.context, typedArray, R.styleable.Chip_chipIconTint));
        this.setChipIconSize(typedArray.getDimension(R.styleable.Chip_chipIconSize, 0.0f));
        this.setCloseIconVisible(typedArray.getBoolean(R.styleable.Chip_closeIconVisible, false));
        if (attributeSet != null && attributeSet.getAttributeValue(NAMESPACE_APP, "closeIconEnabled") != null && attributeSet.getAttributeValue(NAMESPACE_APP, "closeIconVisible") == null) {
            this.setCloseIconVisible(typedArray.getBoolean(R.styleable.Chip_closeIconEnabled, false));
        }
        this.setCloseIcon(MaterialResources.getDrawable(this.context, typedArray, R.styleable.Chip_closeIcon));
        this.setCloseIconTint(MaterialResources.getColorStateList(this.context, typedArray, R.styleable.Chip_closeIconTint));
        this.setCloseIconSize(typedArray.getDimension(R.styleable.Chip_closeIconSize, 0.0f));
        this.setCheckable(typedArray.getBoolean(R.styleable.Chip_android_checkable, false));
        this.setCheckedIconVisible(typedArray.getBoolean(R.styleable.Chip_checkedIconVisible, false));
        if (attributeSet != null && attributeSet.getAttributeValue(NAMESPACE_APP, "checkedIconEnabled") != null && attributeSet.getAttributeValue(NAMESPACE_APP, "checkedIconVisible") == null) {
            this.setCheckedIconVisible(typedArray.getBoolean(R.styleable.Chip_checkedIconEnabled, false));
        }
        this.setCheckedIcon(MaterialResources.getDrawable(this.context, typedArray, R.styleable.Chip_checkedIcon));
        this.setShowMotionSpec(MotionSpec.createFromAttribute(this.context, typedArray, R.styleable.Chip_showMotionSpec));
        this.setHideMotionSpec(MotionSpec.createFromAttribute(this.context, typedArray, R.styleable.Chip_hideMotionSpec));
        this.setChipStartPadding(typedArray.getDimension(R.styleable.Chip_chipStartPadding, 0.0f));
        this.setIconStartPadding(typedArray.getDimension(R.styleable.Chip_iconStartPadding, 0.0f));
        this.setIconEndPadding(typedArray.getDimension(R.styleable.Chip_iconEndPadding, 0.0f));
        this.setTextStartPadding(typedArray.getDimension(R.styleable.Chip_textStartPadding, 0.0f));
        this.setTextEndPadding(typedArray.getDimension(R.styleable.Chip_textEndPadding, 0.0f));
        this.setCloseIconStartPadding(typedArray.getDimension(R.styleable.Chip_closeIconStartPadding, 0.0f));
        this.setCloseIconEndPadding(typedArray.getDimension(R.styleable.Chip_closeIconEndPadding, 0.0f));
        this.setChipEndPadding(typedArray.getDimension(R.styleable.Chip_chipEndPadding, 0.0f));
        this.setMaxWidth(typedArray.getDimensionPixelSize(R.styleable.Chip_android_maxWidth, Integer.MAX_VALUE));
        typedArray.recycle();
    }

    private boolean onStateChange(int[] nArray, int[] nArray2) {
        boolean bl = super.onStateChange(nArray);
        int n = 0;
        Object object = this.chipBackgroundColor;
        int n2 = 0;
        int n3 = object != null ? object.getColorForState(nArray, this.currentChipBackgroundColor) : 0;
        if (this.currentChipBackgroundColor != n3) {
            this.currentChipBackgroundColor = n3;
            bl = true;
        }
        if (this.currentChipStrokeColor != (n3 = (object = this.chipStrokeColor) != null ? object.getColorForState(nArray, this.currentChipStrokeColor) : 0)) {
            this.currentChipStrokeColor = n3;
            bl = true;
        }
        n3 = (object = this.compatRippleColor) != null ? object.getColorForState(nArray, this.currentCompatRippleColor) : 0;
        boolean bl2 = bl;
        if (this.currentCompatRippleColor != n3) {
            this.currentCompatRippleColor = n3;
            bl2 = bl;
            if (this.useCompatRipple) {
                bl2 = true;
            }
        }
        if (this.currentTextColor != (n3 = (object = this.textAppearance) != null && object.textColor != null ? this.textAppearance.textColor.getColorForState(nArray, this.currentTextColor) : 0)) {
            this.currentTextColor = n3;
            bl2 = true;
        }
        boolean bl3 = ChipDrawable.hasState(this.getState(), 0x10100A0) && this.checkable;
        bl = bl2;
        n3 = n;
        if (this.currentChecked != bl3) {
            bl = bl2;
            n3 = n;
            if (this.checkedIcon != null) {
                float f = this.calculateChipIconWidth();
                this.currentChecked = bl3;
                float f2 = this.calculateChipIconWidth();
                bl = bl2 = true;
                n3 = n;
                if (f != f2) {
                    n3 = 1;
                    bl = bl2;
                }
            }
        }
        if ((object = this.tint) != null) {
            n2 = object.getColorForState(nArray, this.currentTint);
        }
        if (this.currentTint != n2) {
            this.currentTint = n2;
            this.tintFilter = DrawableUtils.updateTintFilter(this, this.tint, this.tintMode);
            bl = true;
        }
        bl2 = bl;
        if (ChipDrawable.isStateful(this.chipIcon)) {
            bl2 = bl | this.chipIcon.setState(nArray);
        }
        bl = bl2;
        if (ChipDrawable.isStateful(this.checkedIcon)) {
            bl = bl2 | this.checkedIcon.setState(nArray);
        }
        bl2 = bl;
        if (ChipDrawable.isStateful(this.closeIcon)) {
            bl2 = bl | this.closeIcon.setState(nArray2);
        }
        if (bl2) {
            this.invalidateSelf();
        }
        if (n3 != 0) {
            this.onSizeChange();
        }
        return bl2;
    }

    private boolean showsCheckedIcon() {
        boolean bl = this.checkedIconVisible && this.checkedIcon != null && this.currentChecked;
        return bl;
    }

    private boolean showsChipIcon() {
        boolean bl = this.chipIconVisible && this.chipIcon != null;
        return bl;
    }

    private boolean showsCloseIcon() {
        boolean bl = this.closeIconVisible && this.closeIcon != null;
        return bl;
    }

    private void unapplyChildDrawable(Drawable drawable2) {
        if (drawable2 != null) {
            drawable2.setCallback(null);
        }
    }

    private void updateCompatRippleColor() {
        ColorStateList colorStateList = this.useCompatRipple ? RippleUtils.convertToRippleDrawableColor(this.rippleColor) : null;
        this.compatRippleColor = colorStateList;
    }

    float calculateChipIconWidth() {
        if (!this.showsChipIcon() && !this.showsCheckedIcon()) {
            return 0.0f;
        }
        return this.iconStartPadding + this.chipIconSize + this.iconEndPadding;
    }

    Paint.Align calculateTextOriginAndAlignment(Rect rect, PointF pointF) {
        pointF.set(0.0f, 0.0f);
        Paint.Align align = Paint.Align.LEFT;
        if (this.unicodeWrappedText != null) {
            float f = this.chipStartPadding + this.calculateChipIconWidth() + this.textStartPadding;
            if (DrawableCompat.getLayoutDirection(this) == 0) {
                pointF.x = (float)rect.left + f;
                align = Paint.Align.LEFT;
            } else {
                pointF.x = (float)rect.right - f;
                align = Paint.Align.RIGHT;
            }
            pointF.y = (float)rect.centerY() - this.calculateTextCenterFromBaseline();
        }
        return align;
    }

    public void draw(Canvas canvas) {
        Rect rect = this.getBounds();
        if (!rect.isEmpty() && this.getAlpha() != 0) {
            int n = 0;
            if (this.alpha < 255) {
                n = CanvasCompat.saveLayerAlpha(canvas, rect.left, rect.top, rect.right, rect.bottom, this.alpha);
            }
            this.drawChipBackground(canvas, rect);
            this.drawChipStroke(canvas, rect);
            this.drawCompatRipple(canvas, rect);
            this.drawChipIcon(canvas, rect);
            this.drawCheckedIcon(canvas, rect);
            if (this.shouldDrawText) {
                this.drawText(canvas, rect);
            }
            this.drawCloseIcon(canvas, rect);
            this.drawDebug(canvas, rect);
            if (this.alpha < 255) {
                canvas.restoreToCount(n);
            }
            return;
        }
    }

    public int getAlpha() {
        return this.alpha;
    }

    public Drawable getCheckedIcon() {
        return this.checkedIcon;
    }

    public ColorStateList getChipBackgroundColor() {
        return this.chipBackgroundColor;
    }

    public float getChipCornerRadius() {
        return this.chipCornerRadius;
    }

    public float getChipEndPadding() {
        return this.chipEndPadding;
    }

    public Drawable getChipIcon() {
        Object object = this.chipIcon;
        object = object != null ? DrawableCompat.unwrap(object) : null;
        return object;
    }

    public float getChipIconSize() {
        return this.chipIconSize;
    }

    public ColorStateList getChipIconTint() {
        return this.chipIconTint;
    }

    public float getChipMinHeight() {
        return this.chipMinHeight;
    }

    public float getChipStartPadding() {
        return this.chipStartPadding;
    }

    public ColorStateList getChipStrokeColor() {
        return this.chipStrokeColor;
    }

    public float getChipStrokeWidth() {
        return this.chipStrokeWidth;
    }

    public void getChipTouchBounds(RectF rectF) {
        this.calculateChipTouchBounds(this.getBounds(), rectF);
    }

    public Drawable getCloseIcon() {
        Object object = this.closeIcon;
        object = object != null ? DrawableCompat.unwrap(object) : null;
        return object;
    }

    public CharSequence getCloseIconContentDescription() {
        return this.closeIconContentDescription;
    }

    public float getCloseIconEndPadding() {
        return this.closeIconEndPadding;
    }

    public float getCloseIconSize() {
        return this.closeIconSize;
    }

    public float getCloseIconStartPadding() {
        return this.closeIconStartPadding;
    }

    public int[] getCloseIconState() {
        return this.closeIconStateSet;
    }

    public ColorStateList getCloseIconTint() {
        return this.closeIconTint;
    }

    public void getCloseIconTouchBounds(RectF rectF) {
        this.calculateCloseIconTouchBounds(this.getBounds(), rectF);
    }

    public ColorFilter getColorFilter() {
        return this.colorFilter;
    }

    public TextUtils.TruncateAt getEllipsize() {
        return this.truncateAt;
    }

    public MotionSpec getHideMotionSpec() {
        return this.hideMotionSpec;
    }

    public float getIconEndPadding() {
        return this.iconEndPadding;
    }

    public float getIconStartPadding() {
        return this.iconStartPadding;
    }

    public int getIntrinsicHeight() {
        return (int)this.chipMinHeight;
    }

    public int getIntrinsicWidth() {
        return Math.min(Math.round(this.chipStartPadding + this.calculateChipIconWidth() + this.textStartPadding + this.getTextWidth() + this.textEndPadding + this.calculateCloseIconWidth() + this.chipEndPadding), this.maxWidth);
    }

    public int getMaxWidth() {
        return this.maxWidth;
    }

    public int getOpacity() {
        return -3;
    }

    public void getOutline(Outline outline) {
        Rect rect = this.getBounds();
        if (!rect.isEmpty()) {
            outline.setRoundRect(rect, this.chipCornerRadius);
        } else {
            outline.setRoundRect(0, 0, this.getIntrinsicWidth(), this.getIntrinsicHeight(), this.chipCornerRadius);
        }
        outline.setAlpha((float)this.getAlpha() / 255.0f);
    }

    public ColorStateList getRippleColor() {
        return this.rippleColor;
    }

    public MotionSpec getShowMotionSpec() {
        return this.showMotionSpec;
    }

    public CharSequence getText() {
        return this.rawText;
    }

    public TextAppearance getTextAppearance() {
        return this.textAppearance;
    }

    public float getTextEndPadding() {
        return this.textEndPadding;
    }

    public float getTextStartPadding() {
        return this.textStartPadding;
    }

    public boolean getUseCompatRipple() {
        return this.useCompatRipple;
    }

    public void invalidateDrawable(Drawable drawable2) {
        drawable2 = this.getCallback();
        if (drawable2 != null) {
            drawable2.invalidateDrawable((Drawable)this);
        }
    }

    public boolean isCheckable() {
        return this.checkable;
    }

    @Deprecated
    public boolean isCheckedIconEnabled() {
        return this.isCheckedIconVisible();
    }

    public boolean isCheckedIconVisible() {
        return this.checkedIconVisible;
    }

    @Deprecated
    public boolean isChipIconEnabled() {
        return this.isChipIconVisible();
    }

    public boolean isChipIconVisible() {
        return this.chipIconVisible;
    }

    @Deprecated
    public boolean isCloseIconEnabled() {
        return this.isCloseIconVisible();
    }

    public boolean isCloseIconStateful() {
        return ChipDrawable.isStateful(this.closeIcon);
    }

    public boolean isCloseIconVisible() {
        return this.closeIconVisible;
    }

    public boolean isStateful() {
        boolean bl = ChipDrawable.isStateful(this.chipBackgroundColor) || ChipDrawable.isStateful(this.chipStrokeColor) || this.useCompatRipple && ChipDrawable.isStateful(this.compatRippleColor) || ChipDrawable.isStateful(this.textAppearance) || this.canShowCheckedIcon() || ChipDrawable.isStateful(this.chipIcon) || ChipDrawable.isStateful(this.checkedIcon) || ChipDrawable.isStateful(this.tint);
        return bl;
    }

    public boolean onLayoutDirectionChanged(int n) {
        boolean bl;
        boolean bl2 = bl = super.onLayoutDirectionChanged(n);
        if (this.showsChipIcon()) {
            bl2 = bl | this.chipIcon.setLayoutDirection(n);
        }
        bl = bl2;
        if (this.showsCheckedIcon()) {
            bl = bl2 | this.checkedIcon.setLayoutDirection(n);
        }
        bl2 = bl;
        if (this.showsCloseIcon()) {
            bl2 = bl | this.closeIcon.setLayoutDirection(n);
        }
        if (bl2) {
            this.invalidateSelf();
        }
        return true;
    }

    protected boolean onLevelChange(int n) {
        boolean bl;
        boolean bl2 = bl = super.onLevelChange(n);
        if (this.showsChipIcon()) {
            bl2 = bl | this.chipIcon.setLevel(n);
        }
        bl = bl2;
        if (this.showsCheckedIcon()) {
            bl = bl2 | this.checkedIcon.setLevel(n);
        }
        bl2 = bl;
        if (this.showsCloseIcon()) {
            bl2 = bl | this.closeIcon.setLevel(n);
        }
        if (bl2) {
            this.invalidateSelf();
        }
        return bl2;
    }

    protected void onSizeChange() {
        Delegate delegate = (Delegate)this.delegate.get();
        if (delegate != null) {
            delegate.onChipDrawableSizeChange();
        }
    }

    protected boolean onStateChange(int[] nArray) {
        return this.onStateChange(nArray, this.getCloseIconState());
    }

    public void scheduleDrawable(Drawable drawable2, Runnable runnable, long l) {
        drawable2 = this.getCallback();
        if (drawable2 != null) {
            drawable2.scheduleDrawable((Drawable)this, runnable, l);
        }
    }

    public void setAlpha(int n) {
        if (this.alpha != n) {
            this.alpha = n;
            this.invalidateSelf();
        }
    }

    public void setCheckable(boolean bl) {
        if (this.checkable != bl) {
            this.checkable = bl;
            float f = this.calculateChipIconWidth();
            if (!bl && this.currentChecked) {
                this.currentChecked = false;
            }
            float f2 = this.calculateChipIconWidth();
            this.invalidateSelf();
            if (f != f2) {
                this.onSizeChange();
            }
        }
    }

    public void setCheckableResource(int n) {
        this.setCheckable(this.context.getResources().getBoolean(n));
    }

    public void setCheckedIcon(Drawable drawable2) {
        if (this.checkedIcon != drawable2) {
            float f = this.calculateChipIconWidth();
            this.checkedIcon = drawable2;
            float f2 = this.calculateChipIconWidth();
            this.unapplyChildDrawable(this.checkedIcon);
            this.applyChildDrawable(this.checkedIcon);
            this.invalidateSelf();
            if (f != f2) {
                this.onSizeChange();
            }
        }
    }

    @Deprecated
    public void setCheckedIconEnabled(boolean bl) {
        this.setCheckedIconVisible(bl);
    }

    @Deprecated
    public void setCheckedIconEnabledResource(int n) {
        this.setCheckedIconVisible(this.context.getResources().getBoolean(n));
    }

    public void setCheckedIconResource(int n) {
        this.setCheckedIcon(AppCompatResources.getDrawable(this.context, n));
    }

    public void setCheckedIconVisible(int n) {
        this.setCheckedIconVisible(this.context.getResources().getBoolean(n));
    }

    public void setCheckedIconVisible(boolean bl) {
        if (this.checkedIconVisible != bl) {
            boolean bl2 = this.showsCheckedIcon();
            this.checkedIconVisible = bl;
            bl = this.showsCheckedIcon();
            boolean bl3 = bl2 != bl;
            if (bl3) {
                if (bl) {
                    this.applyChildDrawable(this.checkedIcon);
                } else {
                    this.unapplyChildDrawable(this.checkedIcon);
                }
                this.invalidateSelf();
                this.onSizeChange();
            }
        }
    }

    public void setChipBackgroundColor(ColorStateList colorStateList) {
        if (this.chipBackgroundColor != colorStateList) {
            this.chipBackgroundColor = colorStateList;
            this.onStateChange(this.getState());
        }
    }

    public void setChipBackgroundColorResource(int n) {
        this.setChipBackgroundColor(AppCompatResources.getColorStateList(this.context, n));
    }

    public void setChipCornerRadius(float f) {
        if (this.chipCornerRadius != f) {
            this.chipCornerRadius = f;
            this.invalidateSelf();
        }
    }

    public void setChipCornerRadiusResource(int n) {
        this.setChipCornerRadius(this.context.getResources().getDimension(n));
    }

    public void setChipEndPadding(float f) {
        if (this.chipEndPadding != f) {
            this.chipEndPadding = f;
            this.invalidateSelf();
            this.onSizeChange();
        }
    }

    public void setChipEndPaddingResource(int n) {
        this.setChipEndPadding(this.context.getResources().getDimension(n));
    }

    public void setChipIcon(Drawable object) {
        Drawable drawable2 = this.getChipIcon();
        if (drawable2 != object) {
            float f = this.calculateChipIconWidth();
            object = object != null ? DrawableCompat.wrap(object).mutate() : null;
            this.chipIcon = object;
            float f2 = this.calculateChipIconWidth();
            this.unapplyChildDrawable(drawable2);
            if (this.showsChipIcon()) {
                this.applyChildDrawable(this.chipIcon);
            }
            this.invalidateSelf();
            if (f != f2) {
                this.onSizeChange();
            }
        }
    }

    @Deprecated
    public void setChipIconEnabled(boolean bl) {
        this.setChipIconVisible(bl);
    }

    @Deprecated
    public void setChipIconEnabledResource(int n) {
        this.setChipIconVisible(n);
    }

    public void setChipIconResource(int n) {
        this.setChipIcon(AppCompatResources.getDrawable(this.context, n));
    }

    public void setChipIconSize(float f) {
        if (this.chipIconSize != f) {
            float f2 = this.calculateChipIconWidth();
            this.chipIconSize = f;
            f = this.calculateChipIconWidth();
            this.invalidateSelf();
            if (f2 != f) {
                this.onSizeChange();
            }
        }
    }

    public void setChipIconSizeResource(int n) {
        this.setChipIconSize(this.context.getResources().getDimension(n));
    }

    public void setChipIconTint(ColorStateList colorStateList) {
        if (this.chipIconTint != colorStateList) {
            this.chipIconTint = colorStateList;
            if (this.showsChipIcon()) {
                DrawableCompat.setTintList(this.chipIcon, colorStateList);
            }
            this.onStateChange(this.getState());
        }
    }

    public void setChipIconTintResource(int n) {
        this.setChipIconTint(AppCompatResources.getColorStateList(this.context, n));
    }

    public void setChipIconVisible(int n) {
        this.setChipIconVisible(this.context.getResources().getBoolean(n));
    }

    public void setChipIconVisible(boolean bl) {
        if (this.chipIconVisible != bl) {
            boolean bl2 = this.showsChipIcon();
            this.chipIconVisible = bl;
            bl = this.showsChipIcon();
            boolean bl3 = bl2 != bl;
            if (bl3) {
                if (bl) {
                    this.applyChildDrawable(this.chipIcon);
                } else {
                    this.unapplyChildDrawable(this.chipIcon);
                }
                this.invalidateSelf();
                this.onSizeChange();
            }
        }
    }

    public void setChipMinHeight(float f) {
        if (this.chipMinHeight != f) {
            this.chipMinHeight = f;
            this.invalidateSelf();
            this.onSizeChange();
        }
    }

    public void setChipMinHeightResource(int n) {
        this.setChipMinHeight(this.context.getResources().getDimension(n));
    }

    public void setChipStartPadding(float f) {
        if (this.chipStartPadding != f) {
            this.chipStartPadding = f;
            this.invalidateSelf();
            this.onSizeChange();
        }
    }

    public void setChipStartPaddingResource(int n) {
        this.setChipStartPadding(this.context.getResources().getDimension(n));
    }

    public void setChipStrokeColor(ColorStateList colorStateList) {
        if (this.chipStrokeColor != colorStateList) {
            this.chipStrokeColor = colorStateList;
            this.onStateChange(this.getState());
        }
    }

    public void setChipStrokeColorResource(int n) {
        this.setChipStrokeColor(AppCompatResources.getColorStateList(this.context, n));
    }

    public void setChipStrokeWidth(float f) {
        if (this.chipStrokeWidth != f) {
            this.chipStrokeWidth = f;
            this.chipPaint.setStrokeWidth(f);
            this.invalidateSelf();
        }
    }

    public void setChipStrokeWidthResource(int n) {
        this.setChipStrokeWidth(this.context.getResources().getDimension(n));
    }

    public void setCloseIcon(Drawable object) {
        Drawable drawable2 = this.getCloseIcon();
        if (drawable2 != object) {
            float f = this.calculateCloseIconWidth();
            object = object != null ? DrawableCompat.wrap(object).mutate() : null;
            this.closeIcon = object;
            float f2 = this.calculateCloseIconWidth();
            this.unapplyChildDrawable(drawable2);
            if (this.showsCloseIcon()) {
                this.applyChildDrawable(this.closeIcon);
            }
            this.invalidateSelf();
            if (f != f2) {
                this.onSizeChange();
            }
        }
    }

    public void setCloseIconContentDescription(CharSequence charSequence) {
        if (this.closeIconContentDescription != charSequence) {
            this.closeIconContentDescription = BidiFormatter.getInstance().unicodeWrap(charSequence);
            this.invalidateSelf();
        }
    }

    @Deprecated
    public void setCloseIconEnabled(boolean bl) {
        this.setCloseIconVisible(bl);
    }

    @Deprecated
    public void setCloseIconEnabledResource(int n) {
        this.setCloseIconVisible(n);
    }

    public void setCloseIconEndPadding(float f) {
        if (this.closeIconEndPadding != f) {
            this.closeIconEndPadding = f;
            this.invalidateSelf();
            if (this.showsCloseIcon()) {
                this.onSizeChange();
            }
        }
    }

    public void setCloseIconEndPaddingResource(int n) {
        this.setCloseIconEndPadding(this.context.getResources().getDimension(n));
    }

    public void setCloseIconResource(int n) {
        this.setCloseIcon(AppCompatResources.getDrawable(this.context, n));
    }

    public void setCloseIconSize(float f) {
        if (this.closeIconSize != f) {
            this.closeIconSize = f;
            this.invalidateSelf();
            if (this.showsCloseIcon()) {
                this.onSizeChange();
            }
        }
    }

    public void setCloseIconSizeResource(int n) {
        this.setCloseIconSize(this.context.getResources().getDimension(n));
    }

    public void setCloseIconStartPadding(float f) {
        if (this.closeIconStartPadding != f) {
            this.closeIconStartPadding = f;
            this.invalidateSelf();
            if (this.showsCloseIcon()) {
                this.onSizeChange();
            }
        }
    }

    public void setCloseIconStartPaddingResource(int n) {
        this.setCloseIconStartPadding(this.context.getResources().getDimension(n));
    }

    public boolean setCloseIconState(int[] nArray) {
        if (!Arrays.equals(this.closeIconStateSet, nArray)) {
            this.closeIconStateSet = nArray;
            if (this.showsCloseIcon()) {
                return this.onStateChange(this.getState(), nArray);
            }
        }
        return false;
    }

    public void setCloseIconTint(ColorStateList colorStateList) {
        if (this.closeIconTint != colorStateList) {
            this.closeIconTint = colorStateList;
            if (this.showsCloseIcon()) {
                DrawableCompat.setTintList(this.closeIcon, colorStateList);
            }
            this.onStateChange(this.getState());
        }
    }

    public void setCloseIconTintResource(int n) {
        this.setCloseIconTint(AppCompatResources.getColorStateList(this.context, n));
    }

    public void setCloseIconVisible(int n) {
        this.setCloseIconVisible(this.context.getResources().getBoolean(n));
    }

    public void setCloseIconVisible(boolean bl) {
        if (this.closeIconVisible != bl) {
            boolean bl2 = this.showsCloseIcon();
            this.closeIconVisible = bl;
            bl = this.showsCloseIcon();
            boolean bl3 = bl2 != bl;
            if (bl3) {
                if (bl) {
                    this.applyChildDrawable(this.closeIcon);
                } else {
                    this.unapplyChildDrawable(this.closeIcon);
                }
                this.invalidateSelf();
                this.onSizeChange();
            }
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        if (this.colorFilter != colorFilter) {
            this.colorFilter = colorFilter;
            this.invalidateSelf();
        }
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = new WeakReference<Delegate>(delegate);
    }

    public void setEllipsize(TextUtils.TruncateAt truncateAt) {
        this.truncateAt = truncateAt;
    }

    public void setHideMotionSpec(MotionSpec motionSpec) {
        this.hideMotionSpec = motionSpec;
    }

    public void setHideMotionSpecResource(int n) {
        this.setHideMotionSpec(MotionSpec.createFromResource(this.context, n));
    }

    public void setIconEndPadding(float f) {
        if (this.iconEndPadding != f) {
            float f2 = this.calculateChipIconWidth();
            this.iconEndPadding = f;
            f = this.calculateChipIconWidth();
            this.invalidateSelf();
            if (f2 != f) {
                this.onSizeChange();
            }
        }
    }

    public void setIconEndPaddingResource(int n) {
        this.setIconEndPadding(this.context.getResources().getDimension(n));
    }

    public void setIconStartPadding(float f) {
        if (this.iconStartPadding != f) {
            float f2 = this.calculateChipIconWidth();
            this.iconStartPadding = f;
            f = this.calculateChipIconWidth();
            this.invalidateSelf();
            if (f2 != f) {
                this.onSizeChange();
            }
        }
    }

    public void setIconStartPaddingResource(int n) {
        this.setIconStartPadding(this.context.getResources().getDimension(n));
    }

    public void setMaxWidth(int n) {
        this.maxWidth = n;
    }

    public void setRippleColor(ColorStateList colorStateList) {
        if (this.rippleColor != colorStateList) {
            this.rippleColor = colorStateList;
            this.updateCompatRippleColor();
            this.onStateChange(this.getState());
        }
    }

    public void setRippleColorResource(int n) {
        this.setRippleColor(AppCompatResources.getColorStateList(this.context, n));
    }

    void setShouldDrawText(boolean bl) {
        this.shouldDrawText = bl;
    }

    public void setShowMotionSpec(MotionSpec motionSpec) {
        this.showMotionSpec = motionSpec;
    }

    public void setShowMotionSpecResource(int n) {
        this.setShowMotionSpec(MotionSpec.createFromResource(this.context, n));
    }

    public void setText(CharSequence charSequence) {
        CharSequence charSequence2 = charSequence;
        if (charSequence == null) {
            charSequence2 = "";
        }
        if (this.rawText != charSequence2) {
            this.rawText = charSequence2;
            this.unicodeWrappedText = BidiFormatter.getInstance().unicodeWrap(charSequence2);
            this.textWidthDirty = true;
            this.invalidateSelf();
            this.onSizeChange();
        }
    }

    public void setTextAppearance(TextAppearance textAppearance) {
        if (this.textAppearance != textAppearance) {
            this.textAppearance = textAppearance;
            if (textAppearance != null) {
                textAppearance.updateMeasureState(this.context, this.textPaint, this.fontCallback);
                this.textWidthDirty = true;
            }
            this.onStateChange(this.getState());
            this.onSizeChange();
        }
    }

    public void setTextAppearanceResource(int n) {
        this.setTextAppearance(new TextAppearance(this.context, n));
    }

    public void setTextEndPadding(float f) {
        if (this.textEndPadding != f) {
            this.textEndPadding = f;
            this.invalidateSelf();
            this.onSizeChange();
        }
    }

    public void setTextEndPaddingResource(int n) {
        this.setTextEndPadding(this.context.getResources().getDimension(n));
    }

    public void setTextResource(int n) {
        this.setText(this.context.getResources().getString(n));
    }

    public void setTextStartPadding(float f) {
        if (this.textStartPadding != f) {
            this.textStartPadding = f;
            this.invalidateSelf();
            this.onSizeChange();
        }
    }

    public void setTextStartPaddingResource(int n) {
        this.setTextStartPadding(this.context.getResources().getDimension(n));
    }

    @Override
    public void setTintList(ColorStateList colorStateList) {
        if (this.tint != colorStateList) {
            this.tint = colorStateList;
            this.onStateChange(this.getState());
        }
    }

    @Override
    public void setTintMode(PorterDuff.Mode mode) {
        if (this.tintMode != mode) {
            this.tintMode = mode;
            this.tintFilter = DrawableUtils.updateTintFilter(this, this.tint, mode);
            this.invalidateSelf();
        }
    }

    public void setUseCompatRipple(boolean bl) {
        if (this.useCompatRipple != bl) {
            this.useCompatRipple = bl;
            this.updateCompatRippleColor();
            this.onStateChange(this.getState());
        }
    }

    public boolean setVisible(boolean bl, boolean bl2) {
        boolean bl3;
        boolean bl4 = bl3 = super.setVisible(bl, bl2);
        if (this.showsChipIcon()) {
            bl4 = bl3 | this.chipIcon.setVisible(bl, bl2);
        }
        bl3 = bl4;
        if (this.showsCheckedIcon()) {
            bl3 = bl4 | this.checkedIcon.setVisible(bl, bl2);
        }
        bl4 = bl3;
        if (this.showsCloseIcon()) {
            bl4 = bl3 | this.closeIcon.setVisible(bl, bl2);
        }
        if (bl4) {
            this.invalidateSelf();
        }
        return bl4;
    }

    boolean shouldDrawText() {
        return this.shouldDrawText;
    }

    public void unscheduleDrawable(Drawable drawable2, Runnable runnable) {
        drawable2 = this.getCallback();
        if (drawable2 != null) {
            drawable2.unscheduleDrawable((Drawable)this, runnable);
        }
    }

    public static interface Delegate {
        public void onChipDrawableSizeChange();
    }
}

