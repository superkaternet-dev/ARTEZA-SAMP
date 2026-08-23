/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.Paint$Style
 *  android.graphics.Point
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewTreeObserver$OnGlobalLayoutListener
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.ImageView
 */
package com.skydoves.colorpickerview.sliders;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.skydoves.colorpickerview.ActionMode;
import com.skydoves.colorpickerview.ColorPickerView;

public abstract class AbstractSlider
extends FrameLayout {
    protected int borderColor = -16777216;
    protected Paint borderPaint;
    protected int borderSize = 2;
    protected int color = -1;
    protected Paint colorPaint;
    public ColorPickerView colorPickerView;
    protected String preferenceName;
    protected int selectedX = 0;
    protected ImageView selector;
    protected Drawable selectorDrawable;
    protected float selectorPosition = 1.0f;

    public AbstractSlider(Context context) {
        super(context);
        this.onCreate();
    }

    public AbstractSlider(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.getAttrs(attributeSet);
        this.onCreate();
    }

    public AbstractSlider(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.getAttrs(attributeSet);
        this.onCreate();
    }

    public AbstractSlider(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
        this.getAttrs(attributeSet);
        this.onCreate();
    }

    private float getBoundaryX(float f) {
        int n = this.getMeasuredWidth() - this.selector.getMeasuredWidth();
        if (f >= (float)n) {
            return n;
        }
        if (f <= 0.0f) {
            return 0.0f;
        }
        return f - (float)this.selector.getMeasuredWidth() * 0.5f;
    }

    private void initializeSelector() {
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(this){
            final AbstractSlider this$0;
            {
                this.this$0 = abstractSlider;
            }

            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    this.this$0.getViewTreeObserver().removeGlobalOnLayoutListener((ViewTreeObserver.OnGlobalLayoutListener)this);
                } else {
                    this.this$0.getViewTreeObserver().removeOnGlobalLayoutListener((ViewTreeObserver.OnGlobalLayoutListener)this);
                }
                this.this$0.onInflateFinished();
            }
        });
    }

    private void onCreate() {
        Paint paint;
        this.colorPaint = new Paint(1);
        this.borderPaint = paint = new Paint(1);
        paint.setStyle(Paint.Style.STROKE);
        this.borderPaint.setStrokeWidth((float)this.borderSize);
        this.borderPaint.setColor(this.borderColor);
        this.setBackgroundColor(-1);
        paint = new ImageView(this.getContext());
        this.selector = paint;
        Drawable drawable2 = this.selectorDrawable;
        if (drawable2 != null) {
            paint.setImageDrawable(drawable2);
            paint = new FrameLayout.LayoutParams(-2, -2);
            paint.gravity = 16;
            this.addView((View)this.selector, (ViewGroup.LayoutParams)paint);
        }
        this.initializeSelector();
    }

    private void onTouchReceived(MotionEvent motionEvent) {
        float f = motionEvent.getX();
        float f2 = this.selector.getMeasuredWidth();
        float f3 = this.getMeasuredWidth() - this.selector.getMeasuredWidth();
        float f4 = f;
        if (f < f2) {
            f4 = f2;
        }
        f = f4;
        if (f4 > f3) {
            f = f3;
        }
        this.selectorPosition = f4 = (f - f2) / (f3 - f2);
        if (f4 > 1.0f) {
            this.selectorPosition = 1.0f;
        }
        Point point = new Point((int)motionEvent.getX(), (int)motionEvent.getY());
        this.selectedX = point.x;
        this.selector.setX((float)point.x - (float)this.selector.getMeasuredWidth() * 0.5f);
        if (this.colorPickerView.getActionMode() == ActionMode.LAST) {
            if (motionEvent.getAction() == 1) {
                this.colorPickerView.fireColorListener(this.assembleColor(), true);
            }
        } else {
            this.colorPickerView.fireColorListener(this.assembleColor(), true);
        }
        if (this.colorPickerView.getFlagView() != null) {
            this.colorPickerView.getFlagView().receiveOnTouchEvent(motionEvent);
        }
        int n = this.getMeasuredWidth() - this.selector.getMeasuredWidth();
        if (this.selector.getX() >= (float)n) {
            this.selector.setX((float)n);
        }
        if (this.selector.getX() <= 0.0f) {
            this.selector.setX(0.0f);
        }
    }

    public abstract int assembleColor();

    public void attachColorPickerView(ColorPickerView colorPickerView) {
        this.colorPickerView = colorPickerView;
    }

    protected abstract void getAttrs(AttributeSet var1);

    public int getColor() {
        return this.color;
    }

    public String getPreferenceName() {
        return this.preferenceName;
    }

    public int getSelectedX() {
        return this.selectedX;
    }

    protected float getSelectorPosition() {
        return this.selectorPosition;
    }

    public void notifyColor() {
        this.color = this.colorPickerView.getPureColor();
        this.updatePaint(this.colorPaint);
        this.invalidate();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float f = this.getMeasuredWidth();
        float f2 = this.getMeasuredHeight();
        canvas.drawRect(0.0f, 0.0f, f, f2, this.colorPaint);
        canvas.drawRect(0.0f, 0.0f, f, f2, this.borderPaint);
    }

    public abstract void onInflateFinished();

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.colorPickerView != null) {
            switch (motionEvent.getActionMasked()) {
                default: {
                    this.selector.setPressed(false);
                    return false;
                }
                case 0: 
                case 1: 
                case 2: 
            }
            this.selector.setPressed(true);
            this.onTouchReceived(motionEvent);
            return true;
        }
        return false;
    }

    public void setPreferenceName(String string2) {
        this.preferenceName = string2;
    }

    public void setSelectorPosition(float f) {
        int n;
        this.selectorPosition = Math.min(f, 1.0f);
        this.selectedX = n = (int)this.getBoundaryX((float)this.getMeasuredWidth() * f - (float)this.selector.getMeasuredWidth() * 0.5f - (float)this.borderSize * 0.5f);
        this.selector.setX((float)n);
    }

    protected abstract void updatePaint(Paint var1);

    public void updateSelectorX(int n) {
        float f = this.selector.getMeasuredWidth();
        float f2 = this.getMeasuredWidth() - this.selector.getMeasuredWidth();
        this.selectorPosition = f = ((float)n - f) / (f2 - f);
        if (f > 1.0f) {
            this.selectorPosition = 1.0f;
        }
        this.selectedX = n = (int)this.getBoundaryX(n);
        this.selector.setX((float)n);
        this.colorPickerView.fireColorListener(this.assembleColor(), false);
    }
}

