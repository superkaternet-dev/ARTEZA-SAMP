/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.ViewGroup$LayoutParams
 */
package androidx.constraintlayout.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Guideline
extends View {
    public Guideline(Context context) {
        super(context);
        super.setVisibility(8);
    }

    public Guideline(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        super.setVisibility(8);
    }

    public Guideline(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        super.setVisibility(8);
    }

    public Guideline(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n);
        super.setVisibility(8);
    }

    public void draw(Canvas canvas) {
    }

    protected void onMeasure(int n, int n2) {
        this.setMeasuredDimension(0, 0);
    }

    public void setGuidelineBegin(int n) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)this.getLayoutParams();
        layoutParams.guideBegin = n;
        this.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    public void setGuidelineEnd(int n) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)this.getLayoutParams();
        layoutParams.guideEnd = n;
        this.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    public void setGuidelinePercent(float f) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)this.getLayoutParams();
        layoutParams.guidePercent = f;
        this.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    public void setVisibility(int n) {
    }
}

