/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.Color
 *  android.graphics.LinearGradient
 *  android.graphics.Paint
 *  android.graphics.Shader
 *  android.graphics.Shader$TileMode
 *  android.util.AttributeSet
 */
package com.skydoves.colorpickerview.sliders;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import androidx.appcompat.content.res.AppCompatResources;
import com.skydoves.colorpickerview.R;
import com.skydoves.colorpickerview.preference.ColorPickerPreferenceManager;
import com.skydoves.colorpickerview.sliders.AbstractSlider;

public class BrightnessSlideBar
extends AbstractSlider {
    public BrightnessSlideBar(Context context) {
        super(context);
    }

    public BrightnessSlideBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public BrightnessSlideBar(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    public BrightnessSlideBar(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
    }

    @Override
    public int assembleColor() {
        float[] fArray = new float[3];
        Color.colorToHSV((int)this.getColor(), (float[])fArray);
        fArray[2] = this.selectorPosition;
        if (this.colorPickerView != null && this.colorPickerView.getAlphaSlideBar() != null) {
            return Color.HSVToColor((int)((int)(this.colorPickerView.getAlphaSlideBar().getSelectorPosition() * 255.0f)), (float[])fArray);
        }
        return Color.HSVToColor((float[])fArray);
    }

    @Override
    protected void getAttrs(AttributeSet attributeSet) {
        TypedArray typedArray = this.getContext().obtainStyledAttributes(attributeSet, R.styleable.BrightnessSlideBar);
        try {
            int n;
            if (typedArray.hasValue(R.styleable.BrightnessSlideBar_selector_BrightnessSlider) && (n = typedArray.getResourceId(R.styleable.BrightnessSlideBar_selector_BrightnessSlider, -1)) != -1) {
                this.selectorDrawable = AppCompatResources.getDrawable(this.getContext(), n);
            }
            if (typedArray.hasValue(R.styleable.BrightnessSlideBar_borderColor_BrightnessSlider)) {
                this.borderColor = typedArray.getColor(R.styleable.BrightnessSlideBar_borderColor_BrightnessSlider, this.borderColor);
            }
            if (typedArray.hasValue(R.styleable.BrightnessSlideBar_borderSize_BrightnessSlider)) {
                this.borderSize = typedArray.getInt(R.styleable.BrightnessSlideBar_borderSize_BrightnessSlider, this.borderSize);
            }
            return;
        }
        finally {
            typedArray.recycle();
        }
    }

    @Override
    public void onInflateFinished() {
        int n = this.getMeasuredWidth();
        if (this.getPreferenceName() != null) {
            this.updateSelectorX(ColorPickerPreferenceManager.getInstance(this.getContext()).getBrightnessSliderPosition(this.getPreferenceName(), n));
        } else {
            this.selector.setX((float)n);
        }
    }

    @Override
    protected void updatePaint(Paint paint) {
        float[] fArray = new float[3];
        Color.colorToHSV((int)this.getColor(), (float[])fArray);
        fArray[2] = 0.0f;
        int n = Color.HSVToColor((float[])fArray);
        fArray[2] = 1.0f;
        int n2 = Color.HSVToColor((float[])fArray);
        paint.setShader((Shader)new LinearGradient(0.0f, 0.0f, (float)this.getWidth(), (float)this.getHeight(), n, n2, Shader.TileMode.CLAMP));
    }
}

