/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.TypedArray
 *  android.graphics.Typeface
 *  android.text.TextPaint
 *  android.util.Log
 */
package com.google.android.material.resources;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.Log;
import androidx.core.content.res.ResourcesCompat;
import com.google.android.material.R;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.resources.TextAppearanceConfig;

public class TextAppearance {
    private static final String TAG = "TextAppearance";
    private static final int TYPEFACE_MONOSPACE = 3;
    private static final int TYPEFACE_SANS = 1;
    private static final int TYPEFACE_SERIF = 2;
    private Typeface font;
    public final String fontFamily;
    private final int fontFamilyResourceId;
    private boolean fontResolved = false;
    public final ColorStateList shadowColor;
    public final float shadowDx;
    public final float shadowDy;
    public final float shadowRadius;
    public final boolean textAllCaps;
    public final ColorStateList textColor;
    public final ColorStateList textColorHint;
    public final ColorStateList textColorLink;
    public final float textSize;
    public final int textStyle;
    public final int typeface;

    public TextAppearance(Context context, int n) {
        TypedArray typedArray = context.obtainStyledAttributes(n, R.styleable.TextAppearance);
        this.textSize = typedArray.getDimension(R.styleable.TextAppearance_android_textSize, 0.0f);
        this.textColor = MaterialResources.getColorStateList(context, typedArray, R.styleable.TextAppearance_android_textColor);
        this.textColorHint = MaterialResources.getColorStateList(context, typedArray, R.styleable.TextAppearance_android_textColorHint);
        this.textColorLink = MaterialResources.getColorStateList(context, typedArray, R.styleable.TextAppearance_android_textColorLink);
        this.textStyle = typedArray.getInt(R.styleable.TextAppearance_android_textStyle, 0);
        this.typeface = typedArray.getInt(R.styleable.TextAppearance_android_typeface, 1);
        n = MaterialResources.getIndexWithValue(typedArray, R.styleable.TextAppearance_fontFamily, R.styleable.TextAppearance_android_fontFamily);
        this.fontFamilyResourceId = typedArray.getResourceId(n, 0);
        this.fontFamily = typedArray.getString(n);
        this.textAllCaps = typedArray.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
        this.shadowColor = MaterialResources.getColorStateList(context, typedArray, R.styleable.TextAppearance_android_shadowColor);
        this.shadowDx = typedArray.getFloat(R.styleable.TextAppearance_android_shadowDx, 0.0f);
        this.shadowDy = typedArray.getFloat(R.styleable.TextAppearance_android_shadowDy, 0.0f);
        this.shadowRadius = typedArray.getFloat(R.styleable.TextAppearance_android_shadowRadius, 0.0f);
        typedArray.recycle();
    }

    static /* synthetic */ Typeface access$002(TextAppearance textAppearance, Typeface typeface) {
        textAppearance.font = typeface;
        return typeface;
    }

    static /* synthetic */ boolean access$102(TextAppearance textAppearance, boolean bl) {
        textAppearance.fontResolved = bl;
        return bl;
    }

    private void createFallbackTypeface() {
        if (this.font == null) {
            this.font = Typeface.create((String)this.fontFamily, (int)this.textStyle);
        }
        if (this.font == null) {
            switch (this.typeface) {
                default: {
                    this.font = Typeface.DEFAULT;
                    break;
                }
                case 3: {
                    this.font = Typeface.MONOSPACE;
                    break;
                }
                case 2: {
                    this.font = Typeface.SERIF;
                    break;
                }
                case 1: {
                    this.font = Typeface.SANS_SERIF;
                }
            }
            Typeface typeface = this.font;
            if (typeface != null) {
                this.font = Typeface.create((Typeface)typeface, (int)this.textStyle);
            }
        }
    }

    public Typeface getFont(Context context) {
        block7: {
            if (this.fontResolved) {
                return this.font;
            }
            if (!context.isRestricted()) {
                context = ResourcesCompat.getFont(context, this.fontFamilyResourceId);
                this.font = context;
                if (context == null) break block7;
                try {
                    this.font = Typeface.create((Typeface)context, (int)this.textStyle);
                }
                catch (Exception exception) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Error loading font ");
                    stringBuilder.append(this.fontFamily);
                    Log.d((String)TAG, (String)stringBuilder.toString(), (Throwable)exception);
                }
                catch (Resources.NotFoundException notFoundException) {
                }
                catch (UnsupportedOperationException unsupportedOperationException) {
                    // empty catch block
                }
            }
        }
        this.createFallbackTypeface();
        this.fontResolved = true;
        return this.font;
    }

    public void getFontAsync(Context object, TextPaint textPaint, ResourcesCompat.FontCallback fontCallback) {
        if (this.fontResolved) {
            this.updateTextPaintMeasureState(textPaint, this.font);
            return;
        }
        this.createFallbackTypeface();
        if (object.isRestricted()) {
            this.fontResolved = true;
            this.updateTextPaintMeasureState(textPaint, this.font);
            return;
        }
        try {
            int n = this.fontFamilyResourceId;
            ResourcesCompat.FontCallback fontCallback2 = new ResourcesCompat.FontCallback(this, textPaint, fontCallback){
                final TextAppearance this$0;
                final ResourcesCompat.FontCallback val$callback;
                final TextPaint val$textPaint;
                {
                    this.this$0 = textAppearance;
                    this.val$textPaint = textPaint;
                    this.val$callback = fontCallback;
                }

                @Override
                public void onFontRetrievalFailed(int n) {
                    this.this$0.createFallbackTypeface();
                    TextAppearance.access$102(this.this$0, true);
                    this.val$callback.onFontRetrievalFailed(n);
                }

                @Override
                public void onFontRetrieved(Typeface typeface) {
                    TextAppearance textAppearance = this.this$0;
                    TextAppearance.access$002(textAppearance, Typeface.create((Typeface)typeface, (int)textAppearance.textStyle));
                    this.this$0.updateTextPaintMeasureState(this.val$textPaint, typeface);
                    TextAppearance.access$102(this.this$0, true);
                    this.val$callback.onFontRetrieved(typeface);
                }
            };
            ResourcesCompat.getFont((Context)object, n, fontCallback2, null);
        }
        catch (Exception exception) {
            object = new StringBuilder();
            ((StringBuilder)object).append("Error loading font ");
            ((StringBuilder)object).append(this.fontFamily);
            Log.d((String)TAG, (String)((StringBuilder)object).toString(), (Throwable)exception);
        }
        catch (Resources.NotFoundException notFoundException) {
        }
        catch (UnsupportedOperationException unsupportedOperationException) {
            // empty catch block
        }
    }

    public void updateDrawState(Context context, TextPaint textPaint, ResourcesCompat.FontCallback fontCallback) {
        this.updateMeasureState(context, textPaint, fontCallback);
        context = this.textColor;
        int n = context != null ? context.getColorForState(textPaint.drawableState, this.textColor.getDefaultColor()) : -16777216;
        textPaint.setColor(n);
        float f = this.shadowRadius;
        float f2 = this.shadowDx;
        float f3 = this.shadowDy;
        context = this.shadowColor;
        n = context != null ? context.getColorForState(textPaint.drawableState, this.shadowColor.getDefaultColor()) : 0;
        textPaint.setShadowLayer(f, f2, f3, n);
    }

    public void updateMeasureState(Context context, TextPaint textPaint, ResourcesCompat.FontCallback fontCallback) {
        if (TextAppearanceConfig.shouldLoadFontSynchronously()) {
            this.updateTextPaintMeasureState(textPaint, this.getFont(context));
        } else {
            this.getFontAsync(context, textPaint, fontCallback);
            if (!this.fontResolved) {
                this.updateTextPaintMeasureState(textPaint, this.font);
            }
        }
    }

    public void updateTextPaintMeasureState(TextPaint textPaint, Typeface typeface) {
        textPaint.setTypeface(typeface);
        int n = this.textStyle & ~typeface.getStyle();
        boolean bl = (n & 1) != 0;
        textPaint.setFakeBoldText(bl);
        float f = (n & 2) != 0 ? -0.25f : 0.0f;
        textPaint.setTextSkewX(f);
        textPaint.setTextSize(this.textSize);
    }
}

