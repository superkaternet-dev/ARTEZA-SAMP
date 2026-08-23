/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.GradientDrawable
 */
package com.google.android.material.card;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import com.google.android.material.R;
import com.google.android.material.card.MaterialCardView;

class MaterialCardViewHelper {
    private static final int DEFAULT_STROKE_VALUE = -1;
    private final MaterialCardView materialCardView;
    private int strokeColor;
    private int strokeWidth;

    public MaterialCardViewHelper(MaterialCardView materialCardView) {
        this.materialCardView = materialCardView;
    }

    private void adjustContentPadding() {
        int n = this.materialCardView.getContentPaddingLeft();
        int n2 = this.strokeWidth;
        int n3 = this.materialCardView.getContentPaddingTop();
        int n4 = this.strokeWidth;
        int n5 = this.materialCardView.getContentPaddingRight();
        int n6 = this.strokeWidth;
        int n7 = this.materialCardView.getContentPaddingBottom();
        int n8 = this.strokeWidth;
        this.materialCardView.setContentPadding(n + n2, n3 + n4, n5 + n6, n7 + n8);
    }

    private Drawable createForegroundDrawable() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(this.materialCardView.getRadius());
        int n = this.strokeColor;
        if (n != -1) {
            gradientDrawable.setStroke(this.strokeWidth, n);
        }
        return gradientDrawable;
    }

    int getStrokeColor() {
        return this.strokeColor;
    }

    int getStrokeWidth() {
        return this.strokeWidth;
    }

    public void loadFromAttributes(TypedArray typedArray) {
        this.strokeColor = typedArray.getColor(R.styleable.MaterialCardView_strokeColor, -1);
        this.strokeWidth = typedArray.getDimensionPixelSize(R.styleable.MaterialCardView_strokeWidth, 0);
        this.updateForeground();
        this.adjustContentPadding();
    }

    void setStrokeColor(int n) {
        this.strokeColor = n;
        this.updateForeground();
    }

    void setStrokeWidth(int n) {
        this.strokeWidth = n;
        this.updateForeground();
        this.adjustContentPadding();
    }

    void updateForeground() {
        this.materialCardView.setForeground(this.createForegroundDrawable());
    }
}

