/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.ViewTreeObserver$OnGlobalLayoutListener
 */
package com.skydoves.colorpickerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import com.skydoves.colorpickerview.R;
import com.skydoves.colorpickerview.sliders.AlphaTileDrawable;

public class AlphaTileView
extends View {
    private Bitmap backgroundBitmap;
    private AlphaTileDrawable.Builder builder = new AlphaTileDrawable.Builder();
    private Paint colorPaint;

    public AlphaTileView(Context context) {
        super(context);
        this.onCreate();
        this.draw();
    }

    public AlphaTileView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.onCreate();
        this.getAttrs(attributeSet);
        this.draw();
    }

    public AlphaTileView(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.onCreate();
        this.getAttrs(attributeSet);
        this.draw();
    }

    public AlphaTileView(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
        this.onCreate();
        this.getAttrs(attributeSet);
        this.draw();
    }

    static /* synthetic */ Bitmap access$102(AlphaTileView alphaTileView, Bitmap bitmap) {
        alphaTileView.backgroundBitmap = bitmap;
        return bitmap;
    }

    private void draw() {
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(this){
            final AlphaTileView this$0;
            {
                this.this$0 = alphaTileView;
            }

            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    this.this$0.getViewTreeObserver().removeGlobalOnLayoutListener((ViewTreeObserver.OnGlobalLayoutListener)this);
                } else {
                    this.this$0.getViewTreeObserver().removeOnGlobalLayoutListener((ViewTreeObserver.OnGlobalLayoutListener)this);
                }
                AlphaTileDrawable alphaTileDrawable = this.this$0.builder.build();
                AlphaTileView alphaTileView = this.this$0;
                AlphaTileView.access$102(alphaTileView, Bitmap.createBitmap((int)alphaTileView.getMeasuredWidth(), (int)this.this$0.getMeasuredHeight(), (Bitmap.Config)Bitmap.Config.ARGB_8888));
                alphaTileView = new Canvas(this.this$0.backgroundBitmap);
                alphaTileDrawable.setBounds(0, 0, alphaTileView.getWidth(), alphaTileView.getHeight());
                alphaTileDrawable.draw((Canvas)alphaTileView);
            }
        });
    }

    private void getAttrs(AttributeSet attributeSet) {
        TypedArray typedArray = this.getContext().obtainStyledAttributes(attributeSet, R.styleable.AlphaTileView);
        try {
            if (typedArray.hasValue(R.styleable.AlphaTileView_tileSize)) {
                this.builder.setTileSize(typedArray.getInt(R.styleable.AlphaTileView_tileSize, this.builder.getTileSize()));
            }
            if (typedArray.hasValue(R.styleable.AlphaTileView_tileOddColor)) {
                this.builder.setTileOddColor(typedArray.getInt(R.styleable.AlphaTileView_tileOddColor, this.builder.getTileOddColor()));
            }
            if (typedArray.hasValue(R.styleable.AlphaTileView_tileEvenColor)) {
                this.builder.setTileEvenColor(typedArray.getInt(R.styleable.AlphaTileView_tileEvenColor, this.builder.getTileEvenColor()));
            }
            return;
        }
        finally {
            typedArray.recycle();
        }
    }

    private void onCreate() {
        this.colorPaint = new Paint(1);
        this.setBackgroundColor(-1);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(this.backgroundBitmap, 0.0f, 0.0f, null);
        canvas.drawRect(0.0f, 0.0f, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), this.colorPaint);
    }

    public void setBackgroundColor(int n) {
        this.setPaintColor(n);
    }

    public void setPaintColor(int n) {
        this.colorPaint.setColor(n);
        this.invalidate();
    }
}

