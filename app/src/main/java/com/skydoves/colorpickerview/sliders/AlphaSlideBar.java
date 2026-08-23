/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.Canvas
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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import androidx.appcompat.content.res.AppCompatResources;
import com.skydoves.colorpickerview.R;
import com.skydoves.colorpickerview.preference.ColorPickerPreferenceManager;
import com.skydoves.colorpickerview.sliders.AbstractSlider;
import com.skydoves.colorpickerview.sliders.AlphaTileDrawable;

public class AlphaSlideBar
extends AbstractSlider {
    private Bitmap backgroundBitmap;
    private AlphaTileDrawable drawable = new AlphaTileDrawable();

    public AlphaSlideBar(Context context) {
        super(context);
    }

    public AlphaSlideBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public AlphaSlideBar(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    public AlphaSlideBar(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
    }

    @Override
    public int assembleColor() {
        float[] fArray = new float[3];
        Color.colorToHSV((int)this.getColor(), (float[])fArray);
        return Color.HSVToColor((int)((int)(this.selectorPosition * 255.0f)), (float[])fArray);
    }

    @Override
    protected void getAttrs(AttributeSet attributeSet) {
        TypedArray typedArray = this.getContext().obtainStyledAttributes(attributeSet, R.styleable.AlphaSlideBar);
        try {
            int n;
            if (typedArray.hasValue(R.styleable.AlphaSlideBar_selector_AlphaSlideBar) && (n = typedArray.getResourceId(R.styleable.AlphaSlideBar_selector_AlphaSlideBar, -1)) != -1) {
                this.selectorDrawable = AppCompatResources.getDrawable(this.getContext(), n);
            }
            if (typedArray.hasValue(R.styleable.AlphaSlideBar_borderColor_AlphaSlideBar)) {
                this.borderColor = typedArray.getColor(R.styleable.AlphaSlideBar_borderColor_AlphaSlideBar, this.borderColor);
            }
            if (typedArray.hasValue(R.styleable.AlphaSlideBar_borderSize_AlphaSlideBar)) {
                this.borderSize = typedArray.getInt(R.styleable.AlphaSlideBar_borderSize_AlphaSlideBar, this.borderSize);
            }
            return;
        }
        finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(this.backgroundBitmap, 0.0f, 0.0f, null);
        super.onDraw(canvas);
    }

    @Override
    public void onInflateFinished() {
        int n = this.getMeasuredWidth();
        if (this.getPreferenceName() != null) {
            this.updateSelectorX(ColorPickerPreferenceManager.getInstance(this.getContext()).getAlphaSliderPosition(this.getPreferenceName(), n));
        } else {
            this.selector.setX((float)n);
        }
    }

    protected void onSizeChanged(int n, int n2, int n3, int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        if (n > 0 && n2 > 0) {
            this.backgroundBitmap = Bitmap.createBitmap((int)n, (int)n2, (Bitmap.Config)Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(this.backgroundBitmap);
            this.drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            this.drawable.draw(canvas);
        }
    }

    @Override
    public void updatePaint(Paint paint) {
        float[] fArray = new float[3];
        Color.colorToHSV((int)this.getColor(), (float[])fArray);
        int n = Color.HSVToColor((int)0, (float[])fArray);
        int n2 = Color.HSVToColor((int)255, (float[])fArray);
        paint.setShader((Shader)new LinearGradient(0.0f, 0.0f, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), n, n2, Shader.TileMode.CLAMP));
    }
}

