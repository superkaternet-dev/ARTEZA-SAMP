/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources$NotFoundException
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Typeface
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.os.LocaleList
 *  android.text.method.PasswordTransformationMethod
 *  android.util.AttributeSet
 *  android.view.View
 *  android.widget.TextView
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.LocaleList;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.appcompat.widget.AppCompatTextViewAutoSizeHelper;
import androidx.appcompat.widget.DrawableUtils;
import androidx.appcompat.widget.TintInfo;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.AutoSizeableTextView;
import androidx.core.widget.TextViewCompat;
import java.lang.ref.WeakReference;
import java.util.Locale;

class AppCompatTextHelper {
    private static final int MONOSPACE = 3;
    private static final int SANS = 1;
    private static final int SERIF = 2;
    private static final int TEXT_FONT_WEIGHT_UNSPECIFIED = -1;
    private boolean mAsyncFontPending;
    private final AppCompatTextViewAutoSizeHelper mAutoSizeTextHelper;
    private TintInfo mDrawableBottomTint;
    private TintInfo mDrawableEndTint;
    private TintInfo mDrawableLeftTint;
    private TintInfo mDrawableRightTint;
    private TintInfo mDrawableStartTint;
    private TintInfo mDrawableTint;
    private TintInfo mDrawableTopTint;
    private Typeface mFontTypeface;
    private int mFontWeight = -1;
    private int mStyle = 0;
    private final TextView mView;

    AppCompatTextHelper(TextView textView) {
        this.mView = textView;
        this.mAutoSizeTextHelper = new AppCompatTextViewAutoSizeHelper(textView);
    }

    private void applyCompoundDrawableTint(Drawable drawable2, TintInfo tintInfo) {
        if (drawable2 != null && tintInfo != null) {
            AppCompatDrawableManager.tintDrawable(drawable2, tintInfo, this.mView.getDrawableState());
        }
    }

    private static TintInfo createTintInfo(Context context, AppCompatDrawableManager object, int n) {
        if ((context = ((AppCompatDrawableManager)object).getTintList(context, n)) != null) {
            object = new TintInfo();
            ((TintInfo)object).mHasTintList = true;
            ((TintInfo)object).mTintList = context;
            return object;
        }
        return null;
    }

    private void setCompoundDrawables(Drawable drawable2, Drawable drawable3, Drawable drawable4, Drawable drawable5, Drawable drawable6, Drawable drawable7) {
        if (Build.VERSION.SDK_INT >= 17 && (drawable6 != null || drawable7 != null)) {
            Drawable[] drawableArray = this.mView.getCompoundDrawablesRelative();
            TextView textView = this.mView;
            drawable2 = drawable6 != null ? drawable6 : drawableArray[0];
            if (drawable3 == null) {
                drawable3 = drawableArray[1];
            }
            drawable4 = drawable7 != null ? drawable7 : drawableArray[2];
            if (drawable5 == null) {
                drawable5 = drawableArray[3];
            }
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable2, drawable3, drawable4, drawable5);
        } else if (drawable2 != null || drawable3 != null || drawable4 != null || drawable5 != null) {
            if (Build.VERSION.SDK_INT >= 17 && ((drawable7 = this.mView.getCompoundDrawablesRelative())[0] != null || drawable7[2] != null)) {
                drawable6 = this.mView;
                drawable4 = drawable7[0];
                drawable2 = drawable3 != null ? drawable3 : drawable7[1];
                drawable3 = drawable7[2];
                if (drawable5 == null) {
                    drawable5 = drawable7[3];
                }
                drawable6.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable4, drawable2, drawable3, drawable5);
                return;
            }
            drawable7 = this.mView.getCompoundDrawables();
            drawable6 = this.mView;
            if (drawable2 == null) {
                drawable2 = drawable7[0];
            }
            if (drawable3 == null) {
                drawable3 = drawable7[1];
            }
            if (drawable4 == null) {
                drawable4 = drawable7[2];
            }
            if (drawable5 == null) {
                drawable5 = drawable7[3];
            }
            drawable6.setCompoundDrawablesWithIntrinsicBounds(drawable2, drawable3, drawable4, drawable5);
        }
    }

    private void setCompoundTints() {
        TintInfo tintInfo;
        this.mDrawableLeftTint = tintInfo = this.mDrawableTint;
        this.mDrawableTopTint = tintInfo;
        this.mDrawableRightTint = tintInfo;
        this.mDrawableBottomTint = tintInfo;
        this.mDrawableStartTint = tintInfo;
        this.mDrawableEndTint = tintInfo;
    }

    private void setTextSizeInternal(int n, float f) {
        this.mAutoSizeTextHelper.setTextSizeInternal(n, f);
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void updateTypefaceAndStyle(Context object, TintTypedArray tintTypedArray) {
        boolean bl;
        void var2_11;
        this.mStyle = var2_11.getInt(R.styleable.TextAppearance_android_textStyle, this.mStyle);
        int n = Build.VERSION.SDK_INT;
        boolean bl2 = false;
        if (n >= 28) {
            this.mFontWeight = n = var2_11.getInt(R.styleable.TextAppearance_android_textFontWeight, -1);
            if (n != -1) {
                this.mStyle = this.mStyle & 2 | 0;
            }
        }
        if (!var2_11.hasValue(R.styleable.TextAppearance_android_fontFamily) && !var2_11.hasValue(R.styleable.TextAppearance_fontFamily)) {
            if (!var2_11.hasValue(R.styleable.TextAppearance_android_typeface)) return;
            this.mAsyncFontPending = false;
            switch (var2_11.getInt(R.styleable.TextAppearance_android_typeface, 1)) {
                default: {
                    return;
                }
                case 3: {
                    this.mFontTypeface = Typeface.MONOSPACE;
                    return;
                }
                case 2: {
                    this.mFontTypeface = Typeface.SERIF;
                    return;
                }
                case 1: 
            }
            this.mFontTypeface = Typeface.SANS_SERIF;
            return;
        }
        this.mFontTypeface = null;
        n = var2_11.hasValue(R.styleable.TextAppearance_fontFamily) ? R.styleable.TextAppearance_fontFamily : R.styleable.TextAppearance_android_fontFamily;
        int n2 = this.mFontWeight;
        int n3 = this.mStyle;
        if (!object.isRestricted()) {
            ResourcesCompat.FontCallback fontCallback = new ResourcesCompat.FontCallback(this, n2, n3, new WeakReference<TextView>(this.mView)){
                final AppCompatTextHelper this$0;
                final int val$fontWeight;
                final int val$style;
                final WeakReference val$textViewWeak;
                {
                    this.this$0 = appCompatTextHelper;
                    this.val$fontWeight = n;
                    this.val$style = n2;
                    this.val$textViewWeak = weakReference;
                }

                @Override
                public void onFontRetrievalFailed(int n) {
                }

                @Override
                public void onFontRetrieved(Typeface typeface) {
                    Typeface typeface2 = typeface;
                    if (Build.VERSION.SDK_INT >= 28) {
                        int n = this.val$fontWeight;
                        typeface2 = typeface;
                        if (n != -1) {
                            boolean bl = (this.val$style & 2) != 0;
                            typeface2 = Typeface.create((Typeface)typeface, (int)n, (boolean)bl);
                        }
                    }
                    this.this$0.onAsyncTypefaceReceived(this.val$textViewWeak, typeface2);
                }
            };
            try {
                Typeface typeface = var2_11.getFont(n, this.mStyle, fontCallback);
                if (typeface != null) {
                    if (Build.VERSION.SDK_INT >= 28 && this.mFontWeight != -1) {
                        Typeface typeface2 = Typeface.create((Typeface)typeface, (int)0);
                        n3 = this.mFontWeight;
                        bl = (this.mStyle & 2) != 0;
                        this.mFontTypeface = Typeface.create((Typeface)typeface2, (int)n3, (boolean)bl);
                    } else {
                        this.mFontTypeface = typeface;
                    }
                }
                bl = this.mFontTypeface == null;
                this.mAsyncFontPending = bl;
            }
            catch (Resources.NotFoundException notFoundException) {
            }
            catch (UnsupportedOperationException unsupportedOperationException) {
                // empty catch block
            }
        }
        if (this.mFontTypeface != null) return;
        String string2 = var2_11.getString(n);
        if (string2 == null) return;
        if (Build.VERSION.SDK_INT >= 28 && this.mFontWeight != -1) {
            Typeface typeface = Typeface.create((String)string2, (int)0);
            n = this.mFontWeight;
            bl = bl2;
            if ((this.mStyle & 2) != 0) {
                bl = true;
            }
            this.mFontTypeface = Typeface.create((Typeface)typeface, (int)n, (boolean)bl);
            return;
        }
        this.mFontTypeface = Typeface.create((String)string2, (int)this.mStyle);
    }

    void applyCompoundDrawablesTints() {
        Drawable[] drawableArray;
        if (this.mDrawableLeftTint != null || this.mDrawableTopTint != null || this.mDrawableRightTint != null || this.mDrawableBottomTint != null) {
            drawableArray = this.mView.getCompoundDrawables();
            this.applyCompoundDrawableTint(drawableArray[0], this.mDrawableLeftTint);
            this.applyCompoundDrawableTint(drawableArray[1], this.mDrawableTopTint);
            this.applyCompoundDrawableTint(drawableArray[2], this.mDrawableRightTint);
            this.applyCompoundDrawableTint(drawableArray[3], this.mDrawableBottomTint);
        }
        if (Build.VERSION.SDK_INT >= 17 && (this.mDrawableStartTint != null || this.mDrawableEndTint != null)) {
            drawableArray = this.mView.getCompoundDrawablesRelative();
            this.applyCompoundDrawableTint(drawableArray[0], this.mDrawableStartTint);
            this.applyCompoundDrawableTint(drawableArray[2], this.mDrawableEndTint);
        }
    }

    void autoSizeText() {
        this.mAutoSizeTextHelper.autoSizeText();
    }

    int getAutoSizeMaxTextSize() {
        return this.mAutoSizeTextHelper.getAutoSizeMaxTextSize();
    }

    int getAutoSizeMinTextSize() {
        return this.mAutoSizeTextHelper.getAutoSizeMinTextSize();
    }

    int getAutoSizeStepGranularity() {
        return this.mAutoSizeTextHelper.getAutoSizeStepGranularity();
    }

    int[] getAutoSizeTextAvailableSizes() {
        return this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
    }

    int getAutoSizeTextType() {
        return this.mAutoSizeTextHelper.getAutoSizeTextType();
    }

    ColorStateList getCompoundDrawableTintList() {
        TintInfo tintInfo = this.mDrawableTint;
        tintInfo = tintInfo != null ? tintInfo.mTintList : null;
        return tintInfo;
    }

    PorterDuff.Mode getCompoundDrawableTintMode() {
        TintInfo tintInfo = this.mDrawableTint;
        tintInfo = tintInfo != null ? tintInfo.mTintMode : null;
        return tintInfo;
    }

    boolean isAutoSizeEnabled() {
        return this.mAutoSizeTextHelper.isAutoSizeEnabled();
    }

    void loadFromAttributes(AttributeSet attributeSet, int n) {
        Context context = this.mView.getContext();
        AppCompatDrawableManager appCompatDrawableManager = AppCompatDrawableManager.get();
        Object object = TintTypedArray.obtainStyledAttributes(context, attributeSet, R.styleable.AppCompatTextHelper, n, 0);
        Object object2 = this.mView;
        ViewCompat.saveAttributeDataForStyleable((View)object2, object2.getContext(), R.styleable.AppCompatTextHelper, attributeSet, ((TintTypedArray)object).getWrappedTypeArray(), n, 0);
        int n2 = ((TintTypedArray)object).getResourceId(R.styleable.AppCompatTextHelper_android_textAppearance, -1);
        if (((TintTypedArray)object).hasValue(R.styleable.AppCompatTextHelper_android_drawableLeft)) {
            this.mDrawableLeftTint = AppCompatTextHelper.createTintInfo(context, appCompatDrawableManager, ((TintTypedArray)object).getResourceId(R.styleable.AppCompatTextHelper_android_drawableLeft, 0));
        }
        if (((TintTypedArray)object).hasValue(R.styleable.AppCompatTextHelper_android_drawableTop)) {
            this.mDrawableTopTint = AppCompatTextHelper.createTintInfo(context, appCompatDrawableManager, ((TintTypedArray)object).getResourceId(R.styleable.AppCompatTextHelper_android_drawableTop, 0));
        }
        if (((TintTypedArray)object).hasValue(R.styleable.AppCompatTextHelper_android_drawableRight)) {
            this.mDrawableRightTint = AppCompatTextHelper.createTintInfo(context, appCompatDrawableManager, ((TintTypedArray)object).getResourceId(R.styleable.AppCompatTextHelper_android_drawableRight, 0));
        }
        if (((TintTypedArray)object).hasValue(R.styleable.AppCompatTextHelper_android_drawableBottom)) {
            this.mDrawableBottomTint = AppCompatTextHelper.createTintInfo(context, appCompatDrawableManager, ((TintTypedArray)object).getResourceId(R.styleable.AppCompatTextHelper_android_drawableBottom, 0));
        }
        if (Build.VERSION.SDK_INT >= 17) {
            if (((TintTypedArray)object).hasValue(R.styleable.AppCompatTextHelper_android_drawableStart)) {
                this.mDrawableStartTint = AppCompatTextHelper.createTintInfo(context, appCompatDrawableManager, ((TintTypedArray)object).getResourceId(R.styleable.AppCompatTextHelper_android_drawableStart, 0));
            }
            if (((TintTypedArray)object).hasValue(R.styleable.AppCompatTextHelper_android_drawableEnd)) {
                this.mDrawableEndTint = AppCompatTextHelper.createTintInfo(context, appCompatDrawableManager, ((TintTypedArray)object).getResourceId(R.styleable.AppCompatTextHelper_android_drawableEnd, 0));
            }
        }
        ((TintTypedArray)object).recycle();
        boolean bl = this.mView.getTransformationMethod() instanceof PasswordTransformationMethod;
        boolean bl2 = false;
        boolean bl3 = false;
        int n3 = 0;
        int n4 = 0;
        ColorStateList colorStateList = null;
        Object object3 = null;
        String string2 = null;
        Object var19_16 = null;
        object = null;
        Object var17_17 = null;
        object2 = null;
        Object var16_18 = null;
        Object object4 = null;
        TintTypedArray tintTypedArray = null;
        String string3 = null;
        Object var15_22 = null;
        if (n2 != -1) {
            TintTypedArray tintTypedArray2 = TintTypedArray.obtainStyledAttributes(context, n2, R.styleable.TextAppearance);
            bl2 = bl3;
            n3 = n4;
            if (!bl) {
                bl2 = bl3;
                n3 = n4;
                if (tintTypedArray2.hasValue(R.styleable.TextAppearance_textAllCaps)) {
                    n3 = 1;
                    bl2 = tintTypedArray2.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
                }
            }
            this.updateTypefaceAndStyle(context, tintTypedArray2);
            object3 = colorStateList;
            object = var19_16;
            object2 = var16_18;
            if (Build.VERSION.SDK_INT < 23) {
                object4 = string2;
                if (tintTypedArray2.hasValue(R.styleable.TextAppearance_android_textColor)) {
                    object4 = tintTypedArray2.getColorStateList(R.styleable.TextAppearance_android_textColor);
                }
                string3 = var17_17;
                if (tintTypedArray2.hasValue(R.styleable.TextAppearance_android_textColorHint)) {
                    string3 = tintTypedArray2.getColorStateList(R.styleable.TextAppearance_android_textColorHint);
                }
                object3 = object4;
                object = string3;
                object2 = var16_18;
                if (tintTypedArray2.hasValue(R.styleable.TextAppearance_android_textColorLink)) {
                    object2 = tintTypedArray2.getColorStateList(R.styleable.TextAppearance_android_textColorLink);
                    object = string3;
                    object3 = object4;
                }
            }
            string3 = var15_22;
            if (tintTypedArray2.hasValue(R.styleable.TextAppearance_textLocale)) {
                string3 = tintTypedArray2.getString(R.styleable.TextAppearance_textLocale);
            }
            object4 = tintTypedArray;
            if (Build.VERSION.SDK_INT >= 26) {
                object4 = tintTypedArray;
                if (tintTypedArray2.hasValue(R.styleable.TextAppearance_fontVariationSettings)) {
                    object4 = tintTypedArray2.getString(R.styleable.TextAppearance_fontVariationSettings);
                }
            }
            tintTypedArray2.recycle();
        }
        tintTypedArray = TintTypedArray.obtainStyledAttributes(context, attributeSet, R.styleable.TextAppearance, n, 0);
        if (!bl && tintTypedArray.hasValue(R.styleable.TextAppearance_textAllCaps)) {
            bl2 = tintTypedArray.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
            n3 = 1;
        }
        if (Build.VERSION.SDK_INT < 23) {
            if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColor)) {
                object3 = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColor);
            }
            if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColorHint)) {
                object = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColorHint);
            }
            if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColorLink)) {
                object2 = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColorLink);
            }
        }
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_textLocale)) {
            string3 = tintTypedArray.getString(R.styleable.TextAppearance_textLocale);
        }
        if (Build.VERSION.SDK_INT >= 26 && tintTypedArray.hasValue(R.styleable.TextAppearance_fontVariationSettings)) {
            object4 = tintTypedArray.getString(R.styleable.TextAppearance_fontVariationSettings);
        }
        if (Build.VERSION.SDK_INT >= 28 && tintTypedArray.hasValue(R.styleable.TextAppearance_android_textSize) && tintTypedArray.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, -1) == 0) {
            this.mView.setTextSize(0, 0.0f);
        }
        this.updateTypefaceAndStyle(context, tintTypedArray);
        tintTypedArray.recycle();
        if (object3 != null) {
            this.mView.setTextColor(object3);
        }
        if (object != null) {
            this.mView.setHintTextColor((ColorStateList)object);
        }
        if (object2 != null) {
            this.mView.setLinkTextColor((ColorStateList)object2);
        }
        if (!bl && n3 != 0) {
            this.setAllCaps(bl2);
        }
        if ((object2 = this.mFontTypeface) != null) {
            if (this.mFontWeight == -1) {
                this.mView.setTypeface((Typeface)object2, this.mStyle);
            } else {
                this.mView.setTypeface((Typeface)object2);
            }
        }
        if (object4 != null) {
            this.mView.setFontVariationSettings((String)object4);
        }
        if (string3 != null) {
            if (Build.VERSION.SDK_INT >= 24) {
                this.mView.setTextLocales(LocaleList.forLanguageTags((String)string3));
            } else if (Build.VERSION.SDK_INT >= 21) {
                object2 = string3.substring(0, string3.indexOf(44));
                this.mView.setTextLocale(Locale.forLanguageTag((String)object2));
            }
        }
        this.mAutoSizeTextHelper.loadFromAttributes(attributeSet, n);
        if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE && this.mAutoSizeTextHelper.getAutoSizeTextType() != 0 && ((TextView)(object2 = (Object)this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes())).length > 0) {
            if ((float)this.mView.getAutoSizeStepGranularity() != -1.0f) {
                this.mView.setAutoSizeTextTypeUniformWithConfiguration(this.mAutoSizeTextHelper.getAutoSizeMinTextSize(), this.mAutoSizeTextHelper.getAutoSizeMaxTextSize(), this.mAutoSizeTextHelper.getAutoSizeStepGranularity(), 0);
            } else {
                this.mView.setAutoSizeTextTypeUniformWithPresetSizes((int[])object2, 0);
            }
        }
        tintTypedArray = TintTypedArray.obtainStyledAttributes(context, attributeSet, R.styleable.AppCompatTextView);
        object4 = null;
        string3 = null;
        attributeSet = null;
        n = R.styleable.AppCompatTextView_drawableLeftCompat;
        object2 = null;
        if ((n = tintTypedArray.getResourceId(n, -1)) != -1) {
            attributeSet = appCompatDrawableManager.getDrawable(context, n);
        }
        n = R.styleable.AppCompatTextView_drawableTopCompat;
        object = null;
        if ((n = tintTypedArray.getResourceId(n, -1)) != -1) {
            object2 = appCompatDrawableManager.getDrawable(context, n);
        }
        if ((n = tintTypedArray.getResourceId(R.styleable.AppCompatTextView_drawableRightCompat, -1)) != -1) {
            object = appCompatDrawableManager.getDrawable(context, n);
        }
        object3 = (n = tintTypedArray.getResourceId(R.styleable.AppCompatTextView_drawableBottomCompat, -1)) != -1 ? appCompatDrawableManager.getDrawable(context, n) : null;
        n = tintTypedArray.getResourceId(R.styleable.AppCompatTextView_drawableStartCompat, -1);
        if (n != -1) {
            object4 = appCompatDrawableManager.getDrawable(context, n);
        }
        if ((n = tintTypedArray.getResourceId(R.styleable.AppCompatTextView_drawableEndCompat, -1)) != -1) {
            string3 = appCompatDrawableManager.getDrawable(context, n);
        }
        this.setCompoundDrawables((Drawable)attributeSet, (Drawable)object2, (Drawable)object, (Drawable)object3, (Drawable)object4, (Drawable)string3);
        if (tintTypedArray.hasValue(R.styleable.AppCompatTextView_drawableTint)) {
            attributeSet = tintTypedArray.getColorStateList(R.styleable.AppCompatTextView_drawableTint);
            TextViewCompat.setCompoundDrawableTintList(this.mView, (ColorStateList)attributeSet);
        }
        if (tintTypedArray.hasValue(R.styleable.AppCompatTextView_drawableTintMode)) {
            attributeSet = DrawableUtils.parseTintMode(tintTypedArray.getInt(R.styleable.AppCompatTextView_drawableTintMode, -1), null);
            TextViewCompat.setCompoundDrawableTintMode(this.mView, (PorterDuff.Mode)attributeSet);
        }
        n3 = tintTypedArray.getDimensionPixelSize(R.styleable.AppCompatTextView_firstBaselineToTopHeight, -1);
        n = tintTypedArray.getDimensionPixelSize(R.styleable.AppCompatTextView_lastBaselineToBottomHeight, -1);
        n4 = tintTypedArray.getDimensionPixelSize(R.styleable.AppCompatTextView_lineHeight, -1);
        tintTypedArray.recycle();
        if (n3 != -1) {
            TextViewCompat.setFirstBaselineToTopHeight(this.mView, n3);
        }
        if (n != -1) {
            TextViewCompat.setLastBaselineToBottomHeight(this.mView, n);
        }
        if (n4 != -1) {
            TextViewCompat.setLineHeight(this.mView, n4);
        }
    }

    void onAsyncTypefaceReceived(WeakReference<TextView> textView, Typeface typeface) {
        if (this.mAsyncFontPending) {
            this.mFontTypeface = typeface;
            if ((textView = (TextView)textView.get()) != null) {
                textView.setTypeface(typeface, this.mStyle);
            }
        }
    }

    void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        if (!AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
            this.autoSizeText();
        }
    }

    void onSetCompoundDrawables() {
        this.applyCompoundDrawablesTints();
    }

    void onSetTextAppearance(Context object, int n) {
        ColorStateList colorStateList;
        TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(object, n, R.styleable.TextAppearance);
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_textAllCaps)) {
            this.setAllCaps(tintTypedArray.getBoolean(R.styleable.TextAppearance_textAllCaps, false));
        }
        if (Build.VERSION.SDK_INT < 23 && tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColor) && (colorStateList = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColor)) != null) {
            this.mView.setTextColor(colorStateList);
        }
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textSize) && tintTypedArray.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, -1) == 0) {
            this.mView.setTextSize(0, 0.0f);
        }
        this.updateTypefaceAndStyle((Context)object, tintTypedArray);
        if (Build.VERSION.SDK_INT >= 26 && tintTypedArray.hasValue(R.styleable.TextAppearance_fontVariationSettings) && (object = tintTypedArray.getString(R.styleable.TextAppearance_fontVariationSettings)) != null) {
            this.mView.setFontVariationSettings((String)object);
        }
        tintTypedArray.recycle();
        object = this.mFontTypeface;
        if (object != null) {
            this.mView.setTypeface((Typeface)object, this.mStyle);
        }
    }

    void setAllCaps(boolean bl) {
        this.mView.setAllCaps(bl);
    }

    void setAutoSizeTextTypeUniformWithConfiguration(int n, int n2, int n3, int n4) throws IllegalArgumentException {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithConfiguration(n, n2, n3, n4);
    }

    void setAutoSizeTextTypeUniformWithPresetSizes(int[] nArray, int n) throws IllegalArgumentException {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(nArray, n);
    }

    void setAutoSizeTextTypeWithDefaults(int n) {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeWithDefaults(n);
    }

    void setCompoundDrawableTintList(ColorStateList colorStateList) {
        if (this.mDrawableTint == null) {
            this.mDrawableTint = new TintInfo();
        }
        this.mDrawableTint.mTintList = colorStateList;
        TintInfo tintInfo = this.mDrawableTint;
        boolean bl = colorStateList != null;
        tintInfo.mHasTintList = bl;
        this.setCompoundTints();
    }

    void setCompoundDrawableTintMode(PorterDuff.Mode mode) {
        if (this.mDrawableTint == null) {
            this.mDrawableTint = new TintInfo();
        }
        this.mDrawableTint.mTintMode = mode;
        TintInfo tintInfo = this.mDrawableTint;
        boolean bl = mode != null;
        tintInfo.mHasTintMode = bl;
        this.setCompoundTints();
    }

    void setTextSize(int n, float f) {
        if (!AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE && !this.isAutoSizeEnabled()) {
            this.setTextSizeInternal(n, f);
        }
    }
}

