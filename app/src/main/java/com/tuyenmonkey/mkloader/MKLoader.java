/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.graphics.Color
 *  android.util.AttributeSet
 *  android.view.View
 */
package com.tuyenmonkey.mkloader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import com.tuyenmonkey.mkloader.R;
import com.tuyenmonkey.mkloader.callback.InvalidateListener;
import com.tuyenmonkey.mkloader.type.LoaderView;
import com.tuyenmonkey.mkloader.util.LoaderGenerator;

public class MKLoader
extends View
implements InvalidateListener {
    private LoaderView loaderView;

    public MKLoader(Context context) {
        super(context);
        this.initialize(context, null, 0);
    }

    public MKLoader(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.initialize(context, attributeSet, 0);
    }

    public MKLoader(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.initialize(context, attributeSet, n);
    }

    private void initialize(Context object, AttributeSet attributeSet, int n) {
        attributeSet = object.obtainStyledAttributes(attributeSet, R.styleable.MKLoader);
        object = LoaderGenerator.generateLoaderView(attributeSet.getInt(R.styleable.MKLoader_mk_type, -1));
        this.loaderView = object;
        ((LoaderView)object).setColor(attributeSet.getColor(R.styleable.MKLoader_mk_color, Color.parseColor((String)"#ffffff")));
        attributeSet.recycle();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LoaderView loaderView = this.loaderView;
        if (loaderView != null && loaderView.isDetached()) {
            this.loaderView.setInvalidateListener(this);
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LoaderView loaderView = this.loaderView;
        if (loaderView != null) {
            loaderView.onDetach();
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.loaderView.draw(canvas);
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        super.onLayout(bl, n, n2, n3, n4);
        this.loaderView.setSize(this.getWidth(), this.getHeight());
        this.loaderView.initializeObjects();
        this.loaderView.setUpAnimation();
    }

    protected void onMeasure(int n, int n2) {
        this.setMeasuredDimension(MKLoader.resolveSize((int)this.loaderView.getDesiredWidth(), (int)n), MKLoader.resolveSize((int)this.loaderView.getDesiredHeight(), (int)n2));
    }

    @Override
    public void reDraw() {
        this.invalidate();
    }
}

