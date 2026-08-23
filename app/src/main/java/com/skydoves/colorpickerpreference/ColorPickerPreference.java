/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.DialogInterface$OnClickListener
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.GradientDrawable
 *  android.util.AttributeSet
 *  android.view.View
 */
package com.skydoves.colorpickerpreference;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceViewHolder;
import com.skydoves.colorpickerpreference.ColorPickerPreference;
import com.skydoves.colorpickerpreference.R;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.skydoves.colorpickerview.listeners.ColorListener;
import com.skydoves.colorpickerview.listeners.ColorPickerViewListener;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv={1, 0, 3}, d1={"\u0000t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007B\u001f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u0010\u0010#\u001a\u00020$2\u0006\u0010\u0005\u001a\u00020\u0006H\u0002J\u0018\u0010#\u001a\u00020$2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010%\u001a\u00020\tH\u0002J\u0006\u0010&\u001a\u00020\u001eJ\u0006\u0010'\u001a\u00020 J\u0010\u0010(\u001a\u00020$2\u0006\u0010)\u001a\u00020*H\u0002J\u0010\u0010+\u001a\u00020$2\u0006\u0010,\u001a\u00020-H\u0016J\b\u0010.\u001a\u00020$H\u0014J\b\u0010/\u001a\u00020$H\u0002J\u0010\u00100\u001a\u00020$2\u0006\u00101\u001a\u000202H\u0002R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u000e\u0010\u001d\u001a\u00020\u001eX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020 X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010!\u001a\u0004\u0018\u00010\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\"\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00063"}, d2={"Lcom/skydoves/colorpickerpreference/ColorPickerPreference;", "Landroidx/preference/Preference;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "colorBox", "Landroid/view/View;", "cornerRadius", "defaultColor", "isAttachAlphaSlideBar", "", "isAttachBrightnessSlideBar", "negative", "", "paletteDrawable", "Landroid/graphics/drawable/Drawable;", "positive", "preferenceColorListener", "Lcom/skydoves/colorpickerview/listeners/ColorPickerViewListener;", "getPreferenceColorListener", "()Lcom/skydoves/colorpickerview/listeners/ColorPickerViewListener;", "setPreferenceColorListener", "(Lcom/skydoves/colorpickerview/listeners/ColorPickerViewListener;)V", "preferenceColorPickerView", "Lcom/skydoves/colorpickerview/ColorPickerView;", "preferenceDialog", "Landroidx/appcompat/app/AlertDialog;", "selectorDrawable", "title", "getAttrs", "", "defStyle", "getColorPickerView", "getPreferenceDialog", "notifyColorChanged", "envelope", "Lcom/skydoves/colorpickerview/ColorEnvelope;", "onBindViewHolder", "holder", "Landroidx/preference/PreferenceViewHolder;", "onClick", "onInit", "setTypeArray", "typedArray", "Landroid/content/res/TypedArray;", "colorpickerpreference_release"}, k=1, mv={1, 4, 0})
public final class ColorPickerPreference
extends Preference {
    private View colorBox;
    private int cornerRadius;
    private int defaultColor;
    private boolean isAttachAlphaSlideBar;
    private boolean isAttachBrightnessSlideBar;
    private String negative;
    private Drawable paletteDrawable;
    private String positive;
    private ColorPickerViewListener preferenceColorListener;
    private ColorPickerView preferenceColorPickerView;
    private AlertDialog preferenceDialog;
    private Drawable selectorDrawable;
    private String title;

    public ColorPickerPreference(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        super(context);
        this.defaultColor = -16777216;
        this.isAttachAlphaSlideBar = true;
        this.isAttachBrightnessSlideBar = true;
    }

    public ColorPickerPreference(Context context, AttributeSet attributeSet) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(attributeSet, "attrs");
        super(context, attributeSet);
        this.defaultColor = -16777216;
        this.isAttachAlphaSlideBar = true;
        this.isAttachBrightnessSlideBar = true;
        this.getAttrs(attributeSet);
        this.onInit();
    }

    public ColorPickerPreference(Context context, AttributeSet attributeSet, int n) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(attributeSet, "attrs");
        super(context, attributeSet, n);
        this.defaultColor = -16777216;
        this.isAttachAlphaSlideBar = true;
        this.isAttachBrightnessSlideBar = true;
        this.getAttrs(attributeSet, n);
        this.onInit();
    }

    public static final /* synthetic */ View access$getColorBox$p(ColorPickerPreference colorPickerPreference) {
        colorPickerPreference = colorPickerPreference.colorBox;
        if (colorPickerPreference == null) {
            Intrinsics.throwUninitializedPropertyAccessException("colorBox");
        }
        return colorPickerPreference;
    }

    public static final /* synthetic */ void access$notifyColorChanged(ColorPickerPreference colorPickerPreference, ColorEnvelope colorEnvelope) {
        colorPickerPreference.notifyColorChanged(colorEnvelope);
    }

    public static final /* synthetic */ void access$setColorBox$p(ColorPickerPreference colorPickerPreference, View view) {
        colorPickerPreference.colorBox = view;
    }

    private final void getAttrs(AttributeSet attributeSet) {
        attributeSet = this.getContext().obtainStyledAttributes(attributeSet, R.styleable.ColorPickerPreference);
        Intrinsics.checkNotNullExpressionValue(attributeSet, "context.obtainStyledAttr\u2026le.ColorPickerPreference)");
        this.setTypeArray((TypedArray)attributeSet);
    }

    private final void getAttrs(AttributeSet attributeSet, int n) {
        attributeSet = this.getContext().obtainStyledAttributes(attributeSet, R.styleable.ColorPickerPreference, n, 0);
        Intrinsics.checkNotNullExpressionValue(attributeSet, "context.obtainStyledAttr\u2026rPreference, defStyle, 0)");
        this.setTypeArray((TypedArray)attributeSet);
    }

    private final void notifyColorChanged(ColorEnvelope colorEnvelope) {
        ColorPickerViewListener colorPickerViewListener = this.preferenceColorListener;
        if (colorPickerViewListener != null) {
            if (colorPickerViewListener instanceof ColorListener) {
                ((ColorListener)colorPickerViewListener).onColorSelected(colorEnvelope.getColor(), true);
            } else if (colorPickerViewListener instanceof ColorEnvelopeListener) {
                ((ColorEnvelopeListener)colorPickerViewListener).onColorSelected(colorEnvelope, true);
            }
        }
    }

    private final void onInit() {
        this.setWidgetLayoutResource(R.layout.layout_colorpicker_preference);
        Object object = new ColorPickerDialog.Builder(this.getContext());
        ((ColorPickerDialog.Builder)object).setTitle(this.title);
        ((ColorPickerDialog.Builder)object).setPositiveButton((CharSequence)this.positive, (ColorPickerViewListener)new ColorEnvelopeListener(this){
            final ColorPickerPreference this$0;
            {
                this.this$0 = colorPickerPreference;
            }

            public final void onColorSelected(ColorEnvelope colorEnvelope, boolean bl) {
                if (ColorPickerPreference.access$getColorBox$p(this.this$0).getBackground() instanceof GradientDrawable) {
                    Object object = ColorPickerPreference.access$getColorBox$p(this.this$0).getBackground();
                    if (object != null) {
                        object = (GradientDrawable)object;
                        Intrinsics.checkNotNullExpressionValue(colorEnvelope, "envelope");
                        object.setColor(colorEnvelope.getColor());
                        ColorPickerPreference.access$notifyColorChanged(this.this$0, colorEnvelope);
                        object = this.this$0.getPreferenceManager();
                        Intrinsics.checkNotNullExpressionValue(object, "preferenceManager");
                        ((PreferenceManager)object).getSharedPreferences().edit().putInt(this.this$0.getKey(), colorEnvelope.getColor()).apply();
                    } else {
                        throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.GradientDrawable");
                    }
                }
            }
        });
        ((ColorPickerDialog.Builder)object).setNegativeButton(this.negative, (DialogInterface.OnClickListener)onInit.1.2.INSTANCE);
        ((ColorPickerDialog.Builder)object).attachAlphaSlideBar(this.isAttachAlphaSlideBar);
        ((ColorPickerDialog.Builder)object).attachBrightnessSlideBar(this.isAttachBrightnessSlideBar);
        Object object2 = ((ColorPickerDialog.Builder)object).getColorPickerView();
        Object object3 = this.paletteDrawable;
        if (object3 != null) {
            ((ColorPickerView)object2).setPaletteDrawable((Drawable)object3);
        }
        if ((object3 = this.selectorDrawable) != null) {
            ((ColorPickerView)object2).setSelectorDrawable((Drawable)object3);
        }
        ((ColorPickerView)object2).setPreferenceName(this.getKey());
        ((ColorPickerView)object2).setInitialColor(this.defaultColor);
        object3 = Unit.INSTANCE;
        Intrinsics.checkNotNullExpressionValue(object2, "this.colorPickerView.app\u2026lor(defaultColor)\n      }");
        this.preferenceColorPickerView = object2;
        object2 = Unit.INSTANCE;
        object = ((ColorPickerDialog.Builder)object).create();
        Intrinsics.checkNotNullExpressionValue(object, "ColorPickerDialog.Builde\u2026r)\n      }\n    }.create()");
        this.preferenceDialog = object;
    }

    private final void setTypeArray(TypedArray typedArray) {
        this.defaultColor = typedArray.getColor(R.styleable.ColorPickerPreference_default_color, this.defaultColor);
        this.cornerRadius = typedArray.getDimensionPixelSize(R.styleable.ColorPickerPreference_preference_colorBox_radius, this.cornerRadius);
        this.paletteDrawable = typedArray.getDrawable(R.styleable.ColorPickerPreference_preference_palette);
        this.selectorDrawable = typedArray.getDrawable(R.styleable.ColorPickerPreference_preference_selector);
        this.title = typedArray.getString(R.styleable.ColorPickerPreference_preference_dialog_title);
        this.positive = typedArray.getString(R.styleable.ColorPickerPreference_preference_dialog_positive);
        this.negative = typedArray.getString(R.styleable.ColorPickerPreference_preference_dialog_negative);
        this.isAttachAlphaSlideBar = typedArray.getBoolean(R.styleable.ColorPickerPreference_preference_attachAlphaSlideBar, this.isAttachAlphaSlideBar);
        this.isAttachBrightnessSlideBar = typedArray.getBoolean(R.styleable.ColorPickerPreference_preference_attachBrightnessSlideBar, this.isAttachBrightnessSlideBar);
    }

    public final ColorPickerView getColorPickerView() {
        ColorPickerView colorPickerView = this.preferenceColorPickerView;
        if (colorPickerView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("preferenceColorPickerView");
        }
        return colorPickerView;
    }

    public final ColorPickerViewListener getPreferenceColorListener() {
        return this.preferenceColorListener;
    }

    public final AlertDialog getPreferenceDialog() {
        AlertDialog alertDialog = this.preferenceDialog;
        if (alertDialog == null) {
            Intrinsics.throwUninitializedPropertyAccessException("preferenceDialog");
        }
        return alertDialog;
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        Object object;
        int n;
        Intrinsics.checkNotNullParameter(preferenceViewHolder, "holder");
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder = preferenceViewHolder.findViewById(R.id.preference_colorBox);
        Intrinsics.checkNotNullExpressionValue(preferenceViewHolder, "holder.findViewById(R.id.preference_colorBox)");
        this.colorBox = preferenceViewHolder;
        if (preferenceViewHolder == null) {
            Intrinsics.throwUninitializedPropertyAccessException("colorBox");
        }
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius((float)this.cornerRadius);
        if (this.getKey() == null) {
            n = this.defaultColor;
        } else {
            object = this.getPreferenceManager();
            Intrinsics.checkNotNullExpressionValue(object, "preferenceManager");
            n = ((PreferenceManager)object).getSharedPreferences().getInt(this.getKey(), this.defaultColor);
        }
        gradientDrawable.setColor(n);
        object = Unit.INSTANCE;
        preferenceViewHolder.setBackground((Drawable)gradientDrawable);
    }

    @Override
    protected void onClick() {
        super.onClick();
        AlertDialog alertDialog = this.preferenceDialog;
        if (alertDialog == null) {
            Intrinsics.throwUninitializedPropertyAccessException("preferenceDialog");
        }
        alertDialog.show();
    }

    public final void setPreferenceColorListener(ColorPickerViewListener colorPickerViewListener) {
        this.preferenceColorListener = colorPickerViewListener;
    }
}

