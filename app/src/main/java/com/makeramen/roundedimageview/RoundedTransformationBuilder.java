/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.graphics.Bitmap
 *  android.util.DisplayMetrics
 *  android.util.TypedValue
 *  android.widget.ImageView$ScaleType
 *  com.squareup.picasso.Transformation
 */
package com.makeramen.roundedimageview;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.ImageView;
import com.makeramen.roundedimageview.RoundedDrawable;
import com.squareup.picasso.Transformation;
import java.util.Arrays;

public final class RoundedTransformationBuilder {
    private ColorStateList mBorderColor;
    private float mBorderWidth = 0.0f;
    private float[] mCornerRadii = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
    private final DisplayMetrics mDisplayMetrics;
    private boolean mOval = false;
    private ImageView.ScaleType mScaleType;

    public RoundedTransformationBuilder() {
        this.mBorderColor = ColorStateList.valueOf((int)-16777216);
        this.mScaleType = ImageView.ScaleType.FIT_CENTER;
        this.mDisplayMetrics = Resources.getSystem().getDisplayMetrics();
    }

    public RoundedTransformationBuilder borderColor(int n) {
        this.mBorderColor = ColorStateList.valueOf((int)n);
        return this;
    }

    public RoundedTransformationBuilder borderColor(ColorStateList colorStateList) {
        this.mBorderColor = colorStateList;
        return this;
    }

    public RoundedTransformationBuilder borderWidth(float f) {
        this.mBorderWidth = f;
        return this;
    }

    public RoundedTransformationBuilder borderWidthDp(float f) {
        this.mBorderWidth = TypedValue.applyDimension((int)1, (float)f, (DisplayMetrics)this.mDisplayMetrics);
        return this;
    }

    public Transformation build() {
        return new Transformation(this){
            final RoundedTransformationBuilder this$0;
            {
                this.this$0 = roundedTransformationBuilder;
            }

            public String key() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("r:");
                stringBuilder.append(Arrays.toString(this.this$0.mCornerRadii));
                stringBuilder.append("b:");
                stringBuilder.append(this.this$0.mBorderWidth);
                stringBuilder.append("c:");
                stringBuilder.append(this.this$0.mBorderColor);
                stringBuilder.append("o:");
                stringBuilder.append(this.this$0.mOval);
                return stringBuilder.toString();
            }

            public Bitmap transform(Bitmap bitmap) {
                Bitmap bitmap2 = RoundedDrawable.fromBitmap(bitmap).setScaleType(this.this$0.mScaleType).setCornerRadius(this.this$0.mCornerRadii[0], this.this$0.mCornerRadii[1], this.this$0.mCornerRadii[2], this.this$0.mCornerRadii[3]).setBorderWidth(this.this$0.mBorderWidth).setBorderColor(this.this$0.mBorderColor).setOval(this.this$0.mOval).toBitmap();
                if (!bitmap.equals(bitmap2)) {
                    bitmap.recycle();
                }
                return bitmap2;
            }
        };
    }

    public RoundedTransformationBuilder cornerRadius(float f) {
        float[] fArray = this.mCornerRadii;
        fArray[0] = f;
        fArray[1] = f;
        fArray[2] = f;
        fArray[3] = f;
        return this;
    }

    public RoundedTransformationBuilder cornerRadius(int n, float f) {
        this.mCornerRadii[n] = f;
        return this;
    }

    public RoundedTransformationBuilder cornerRadiusDp(float f) {
        return this.cornerRadius(TypedValue.applyDimension((int)1, (float)f, (DisplayMetrics)this.mDisplayMetrics));
    }

    public RoundedTransformationBuilder cornerRadiusDp(int n, float f) {
        return this.cornerRadius(n, TypedValue.applyDimension((int)1, (float)f, (DisplayMetrics)this.mDisplayMetrics));
    }

    public RoundedTransformationBuilder oval(boolean bl) {
        this.mOval = bl;
        return this;
    }

    public RoundedTransformationBuilder scaleType(ImageView.ScaleType scaleType) {
        this.mScaleType = scaleType;
        return this;
    }
}

