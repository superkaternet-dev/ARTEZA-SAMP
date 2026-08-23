/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.BitmapShader
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.Matrix
 *  android.graphics.Matrix$ScaleToFit
 *  android.graphics.Paint
 *  android.graphics.Paint$Style
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.graphics.Shader
 *  android.graphics.Shader$TileMode
 *  android.graphics.drawable.BitmapDrawable
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.LayerDrawable
 *  android.util.Log
 *  android.widget.ImageView$ScaleType
 */
package com.makeramen.roundedimageview;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.widget.ImageView;
import java.util.HashSet;

public class RoundedDrawable
extends Drawable {
    public static final int DEFAULT_BORDER_COLOR = -16777216;
    public static final String TAG = "RoundedDrawable";
    private final Bitmap mBitmap;
    private final int mBitmapHeight;
    private final Paint mBitmapPaint;
    private final RectF mBitmapRect;
    private final int mBitmapWidth;
    private ColorStateList mBorderColor;
    private final Paint mBorderPaint;
    private final RectF mBorderRect;
    private float mBorderWidth;
    private final RectF mBounds = new RectF();
    private float mCornerRadius;
    private final boolean[] mCornersRounded;
    private final RectF mDrawableRect = new RectF();
    private boolean mOval;
    private boolean mRebuildShader;
    private ImageView.ScaleType mScaleType;
    private final Matrix mShaderMatrix;
    private final RectF mSquareCornersRect;
    private Shader.TileMode mTileModeX;
    private Shader.TileMode mTileModeY;

    public RoundedDrawable(Bitmap bitmap) {
        int n;
        int n2;
        RectF rectF;
        this.mBitmapRect = rectF = new RectF();
        this.mBorderRect = new RectF();
        this.mShaderMatrix = new Matrix();
        this.mSquareCornersRect = new RectF();
        this.mTileModeX = Shader.TileMode.CLAMP;
        this.mTileModeY = Shader.TileMode.CLAMP;
        this.mRebuildShader = true;
        this.mCornerRadius = 0.0f;
        this.mCornersRounded = new boolean[]{true, true, true, true};
        this.mOval = false;
        this.mBorderWidth = 0.0f;
        this.mBorderColor = ColorStateList.valueOf((int)-16777216);
        this.mScaleType = ImageView.ScaleType.FIT_CENTER;
        this.mBitmap = bitmap;
        this.mBitmapWidth = n2 = bitmap.getWidth();
        this.mBitmapHeight = n = bitmap.getHeight();
        rectF.set(0.0f, 0.0f, (float)n2, (float)n);
        bitmap = new Paint();
        this.mBitmapPaint = bitmap;
        bitmap.setStyle(Paint.Style.FILL);
        bitmap.setAntiAlias(true);
        bitmap = new Paint();
        this.mBorderPaint = bitmap;
        bitmap.setStyle(Paint.Style.STROKE);
        bitmap.setAntiAlias(true);
        bitmap.setColor(this.mBorderColor.getColorForState(this.getState(), -16777216));
        bitmap.setStrokeWidth(this.mBorderWidth);
    }

    private static boolean all(boolean[] blArray) {
        int n = blArray.length;
        for (int i = 0; i < n; ++i) {
            if (!blArray[i]) continue;
            return false;
        }
        return true;
    }

    private static boolean any(boolean[] blArray) {
        int n = blArray.length;
        for (int i = 0; i < n; ++i) {
            if (!blArray[i]) continue;
            return true;
        }
        return false;
    }

    public static Bitmap drawableToBitmap(Drawable drawable2) {
        if (drawable2 instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable2).getBitmap();
        }
        int n = Math.max(drawable2.getIntrinsicWidth(), 2);
        int n2 = Math.max(drawable2.getIntrinsicHeight(), 2);
        try {
            Bitmap bitmap = Bitmap.createBitmap((int)n, (int)n2, (Bitmap.Config)Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable2.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable2.draw(canvas);
            drawable2 = bitmap;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            Log.w((String)TAG, (String)"Failed to create bitmap from drawable!");
            drawable2 = null;
        }
        return drawable2;
    }

    public static RoundedDrawable fromBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            return new RoundedDrawable(bitmap);
        }
        return null;
    }

    public static Drawable fromDrawable(Drawable drawable2) {
        if (drawable2 != null) {
            if (drawable2 instanceof RoundedDrawable) {
                return drawable2;
            }
            if (drawable2 instanceof LayerDrawable) {
                LayerDrawable layerDrawable = (LayerDrawable)drawable2;
                int n = layerDrawable.getNumberOfLayers();
                for (int i = 0; i < n; ++i) {
                    drawable2 = layerDrawable.getDrawable(i);
                    layerDrawable.setDrawableByLayerId(layerDrawable.getId(i), RoundedDrawable.fromDrawable(drawable2));
                }
                return layerDrawable;
            }
            Bitmap bitmap = RoundedDrawable.drawableToBitmap(drawable2);
            if (bitmap != null) {
                return new RoundedDrawable(bitmap);
            }
        }
        return drawable2;
    }

    private static boolean only(int n, boolean[] blArray) {
        int n2 = 0;
        int n3 = blArray.length;
        while (true) {
            boolean bl = true;
            if (n2 >= n3) break;
            boolean bl2 = blArray[n2];
            if (n2 != n) {
                bl = false;
            }
            if (bl2 != bl) {
                return false;
            }
            ++n2;
        }
        return true;
    }

    private void redrawBitmapForSquareCorners(Canvas canvas) {
        if (RoundedDrawable.all(this.mCornersRounded)) {
            return;
        }
        if (this.mCornerRadius == 0.0f) {
            return;
        }
        float f = this.mDrawableRect.left;
        float f2 = this.mDrawableRect.top;
        float f3 = this.mDrawableRect.width() + f;
        float f4 = this.mDrawableRect.height() + f2;
        float f5 = this.mCornerRadius;
        if (!this.mCornersRounded[0]) {
            this.mSquareCornersRect.set(f, f2, f + f5, f2 + f5);
            canvas.drawRect(this.mSquareCornersRect, this.mBitmapPaint);
        }
        if (!this.mCornersRounded[1]) {
            this.mSquareCornersRect.set(f3 - f5, f2, f3, f5);
            canvas.drawRect(this.mSquareCornersRect, this.mBitmapPaint);
        }
        if (!this.mCornersRounded[2]) {
            this.mSquareCornersRect.set(f3 - f5, f4 - f5, f3, f4);
            canvas.drawRect(this.mSquareCornersRect, this.mBitmapPaint);
        }
        if (!this.mCornersRounded[3]) {
            this.mSquareCornersRect.set(f, f4 - f5, f + f5, f4);
            canvas.drawRect(this.mSquareCornersRect, this.mBitmapPaint);
        }
    }

    private void redrawBorderForSquareCorners(Canvas canvas) {
        if (RoundedDrawable.all(this.mCornersRounded)) {
            return;
        }
        if (this.mCornerRadius == 0.0f) {
            return;
        }
        float f = this.mDrawableRect.left;
        float f2 = this.mDrawableRect.top;
        float f3 = f + this.mDrawableRect.width();
        float f4 = f2 + this.mDrawableRect.height();
        float f5 = this.mCornerRadius;
        float f6 = this.mBorderWidth / 2.0f;
        if (!this.mCornersRounded[0]) {
            canvas.drawLine(f - f6, f2, f + f5, f2, this.mBorderPaint);
            canvas.drawLine(f, f2 - f6, f, f2 + f5, this.mBorderPaint);
        }
        if (!this.mCornersRounded[1]) {
            canvas.drawLine(f3 - f5 - f6, f2, f3, f2, this.mBorderPaint);
            canvas.drawLine(f3, f2 - f6, f3, f2 + f5, this.mBorderPaint);
        }
        if (!this.mCornersRounded[2]) {
            canvas.drawLine(f3 - f5 - f6, f4, f3 + f6, f4, this.mBorderPaint);
            canvas.drawLine(f3, f4 - f5, f3, f4, this.mBorderPaint);
        }
        if (!this.mCornersRounded[3]) {
            canvas.drawLine(f - f6, f4, f + f5, f4, this.mBorderPaint);
            canvas.drawLine(f, f4 - f5, f, f4, this.mBorderPaint);
        }
    }

    private void updateShaderMatrix() {
        switch (1.$SwitchMap$android$widget$ImageView$ScaleType[this.mScaleType.ordinal()]) {
            default: {
                this.mBorderRect.set(this.mBitmapRect);
                this.mShaderMatrix.setRectToRect(this.mBitmapRect, this.mBounds, Matrix.ScaleToFit.CENTER);
                this.mShaderMatrix.mapRect(this.mBorderRect);
                RectF rectF = this.mBorderRect;
                float f = this.mBorderWidth;
                rectF.inset(f / 2.0f, f / 2.0f);
                this.mShaderMatrix.setRectToRect(this.mBitmapRect, this.mBorderRect, Matrix.ScaleToFit.FILL);
                break;
            }
            case 7: {
                this.mBorderRect.set(this.mBounds);
                RectF rectF = this.mBorderRect;
                float f = this.mBorderWidth;
                rectF.inset(f / 2.0f, f / 2.0f);
                this.mShaderMatrix.reset();
                this.mShaderMatrix.setRectToRect(this.mBitmapRect, this.mBorderRect, Matrix.ScaleToFit.FILL);
                break;
            }
            case 6: {
                this.mBorderRect.set(this.mBitmapRect);
                this.mShaderMatrix.setRectToRect(this.mBitmapRect, this.mBounds, Matrix.ScaleToFit.START);
                this.mShaderMatrix.mapRect(this.mBorderRect);
                RectF rectF = this.mBorderRect;
                float f = this.mBorderWidth;
                rectF.inset(f / 2.0f, f / 2.0f);
                this.mShaderMatrix.setRectToRect(this.mBitmapRect, this.mBorderRect, Matrix.ScaleToFit.FILL);
                break;
            }
            case 5: {
                this.mBorderRect.set(this.mBitmapRect);
                this.mShaderMatrix.setRectToRect(this.mBitmapRect, this.mBounds, Matrix.ScaleToFit.END);
                this.mShaderMatrix.mapRect(this.mBorderRect);
                RectF rectF = this.mBorderRect;
                float f = this.mBorderWidth;
                rectF.inset(f / 2.0f, f / 2.0f);
                this.mShaderMatrix.setRectToRect(this.mBitmapRect, this.mBorderRect, Matrix.ScaleToFit.FILL);
                break;
            }
            case 3: {
                this.mShaderMatrix.reset();
                float f = (float)this.mBitmapWidth <= this.mBounds.width() && (float)this.mBitmapHeight <= this.mBounds.height() ? 1.0f : Math.min(this.mBounds.width() / (float)this.mBitmapWidth, this.mBounds.height() / (float)this.mBitmapHeight);
                float f2 = (int)((this.mBounds.width() - (float)this.mBitmapWidth * f) * 0.5f + 0.5f);
                float f3 = (int)((this.mBounds.height() - (float)this.mBitmapHeight * f) * 0.5f + 0.5f);
                this.mShaderMatrix.setScale(f, f);
                this.mShaderMatrix.postTranslate(f2, f3);
                this.mBorderRect.set(this.mBitmapRect);
                this.mShaderMatrix.mapRect(this.mBorderRect);
                RectF rectF = this.mBorderRect;
                f = this.mBorderWidth;
                rectF.inset(f / 2.0f, f / 2.0f);
                this.mShaderMatrix.setRectToRect(this.mBitmapRect, this.mBorderRect, Matrix.ScaleToFit.FILL);
                break;
            }
            case 2: {
                float f;
                this.mBorderRect.set(this.mBounds);
                RectF rectF = this.mBorderRect;
                float f4 = this.mBorderWidth;
                rectF.inset(f4 / 2.0f, f4 / 2.0f);
                this.mShaderMatrix.reset();
                float f5 = 0.0f;
                f4 = 0.0f;
                if ((float)this.mBitmapWidth * this.mBorderRect.height() > this.mBorderRect.width() * (float)this.mBitmapHeight) {
                    f = this.mBorderRect.height() / (float)this.mBitmapHeight;
                    f5 = (this.mBorderRect.width() - (float)this.mBitmapWidth * f) * 0.5f;
                } else {
                    f = this.mBorderRect.width() / (float)this.mBitmapWidth;
                    f4 = (this.mBorderRect.height() - (float)this.mBitmapHeight * f) * 0.5f;
                }
                this.mShaderMatrix.setScale(f, f);
                rectF = this.mShaderMatrix;
                f = (int)(f5 + 0.5f);
                f5 = this.mBorderWidth;
                rectF.postTranslate(f + f5 / 2.0f, (float)((int)(0.5f + f4)) + f5 / 2.0f);
                break;
            }
            case 1: {
                this.mBorderRect.set(this.mBounds);
                RectF rectF = this.mBorderRect;
                float f = this.mBorderWidth;
                rectF.inset(f / 2.0f, f / 2.0f);
                this.mShaderMatrix.reset();
                this.mShaderMatrix.setTranslate((float)((int)((this.mBorderRect.width() - (float)this.mBitmapWidth) * 0.5f + 0.5f)), (float)((int)((this.mBorderRect.height() - (float)this.mBitmapHeight) * 0.5f + 0.5f)));
            }
        }
        this.mDrawableRect.set(this.mBorderRect);
    }

    public void draw(Canvas canvas) {
        if (this.mRebuildShader) {
            BitmapShader bitmapShader = new BitmapShader(this.mBitmap, this.mTileModeX, this.mTileModeY);
            if (this.mTileModeX == Shader.TileMode.CLAMP && this.mTileModeY == Shader.TileMode.CLAMP) {
                bitmapShader.setLocalMatrix(this.mShaderMatrix);
            }
            this.mBitmapPaint.setShader((Shader)bitmapShader);
            this.mRebuildShader = false;
        }
        if (this.mOval) {
            if (this.mBorderWidth > 0.0f) {
                canvas.drawOval(this.mDrawableRect, this.mBitmapPaint);
                canvas.drawOval(this.mBorderRect, this.mBorderPaint);
            } else {
                canvas.drawOval(this.mDrawableRect, this.mBitmapPaint);
            }
        } else if (RoundedDrawable.any(this.mCornersRounded)) {
            float f = this.mCornerRadius;
            if (this.mBorderWidth > 0.0f) {
                canvas.drawRoundRect(this.mDrawableRect, f, f, this.mBitmapPaint);
                canvas.drawRoundRect(this.mBorderRect, f, f, this.mBorderPaint);
                this.redrawBitmapForSquareCorners(canvas);
                this.redrawBorderForSquareCorners(canvas);
            } else {
                canvas.drawRoundRect(this.mDrawableRect, f, f, this.mBitmapPaint);
                this.redrawBitmapForSquareCorners(canvas);
            }
        } else {
            canvas.drawRect(this.mDrawableRect, this.mBitmapPaint);
            if (this.mBorderWidth > 0.0f) {
                canvas.drawRect(this.mBorderRect, this.mBorderPaint);
            }
        }
    }

    public int getAlpha() {
        return this.mBitmapPaint.getAlpha();
    }

    public int getBorderColor() {
        return this.mBorderColor.getDefaultColor();
    }

    public ColorStateList getBorderColors() {
        return this.mBorderColor;
    }

    public float getBorderWidth() {
        return this.mBorderWidth;
    }

    public ColorFilter getColorFilter() {
        return this.mBitmapPaint.getColorFilter();
    }

    public float getCornerRadius() {
        return this.mCornerRadius;
    }

    public float getCornerRadius(int n) {
        float f = this.mCornersRounded[n] ? this.mCornerRadius : 0.0f;
        return f;
    }

    public int getIntrinsicHeight() {
        return this.mBitmapHeight;
    }

    public int getIntrinsicWidth() {
        return this.mBitmapWidth;
    }

    public int getOpacity() {
        return -3;
    }

    public ImageView.ScaleType getScaleType() {
        return this.mScaleType;
    }

    public Bitmap getSourceBitmap() {
        return this.mBitmap;
    }

    public Shader.TileMode getTileModeX() {
        return this.mTileModeX;
    }

    public Shader.TileMode getTileModeY() {
        return this.mTileModeY;
    }

    public boolean isOval() {
        return this.mOval;
    }

    public boolean isStateful() {
        return this.mBorderColor.isStateful();
    }

    protected void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        this.mBounds.set(rect);
        this.updateShaderMatrix();
    }

    protected boolean onStateChange(int[] nArray) {
        int n = this.mBorderColor.getColorForState(nArray, 0);
        if (this.mBorderPaint.getColor() != n) {
            this.mBorderPaint.setColor(n);
            return true;
        }
        return super.onStateChange(nArray);
    }

    public void setAlpha(int n) {
        this.mBitmapPaint.setAlpha(n);
        this.invalidateSelf();
    }

    public RoundedDrawable setBorderColor(int n) {
        return this.setBorderColor(ColorStateList.valueOf((int)n));
    }

    public RoundedDrawable setBorderColor(ColorStateList colorStateList) {
        if (colorStateList == null) {
            colorStateList = ColorStateList.valueOf((int)0);
        }
        this.mBorderColor = colorStateList;
        this.mBorderPaint.setColor(colorStateList.getColorForState(this.getState(), -16777216));
        return this;
    }

    public RoundedDrawable setBorderWidth(float f) {
        this.mBorderWidth = f;
        this.mBorderPaint.setStrokeWidth(f);
        return this;
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mBitmapPaint.setColorFilter(colorFilter);
        this.invalidateSelf();
    }

    public RoundedDrawable setCornerRadius(float f) {
        this.setCornerRadius(f, f, f, f);
        return this;
    }

    /*
     * Enabled aggressive block sorting
     */
    public RoundedDrawable setCornerRadius(float f, float f2, float f3, float f4) {
        boolean bl;
        Object object;
        block4: {
            object = new HashSet<Float>(4);
            object.add(Float.valueOf(f));
            object.add(Float.valueOf(f2));
            object.add(Float.valueOf(f3));
            object.add(Float.valueOf(f4));
            object.remove(Float.valueOf(0.0f));
            int n = object.size();
            bl = true;
            if (n > 1) {
                throw new IllegalArgumentException("Multiple nonzero corner radii not yet supported.");
            }
            if (!object.isEmpty()) {
                float f5 = ((Float)object.iterator().next()).floatValue();
                if (!(Float.isInfinite(f5) || Float.isNaN(f5) || f5 < 0.0f)) {
                    this.mCornerRadius = f5;
                    break block4;
                } else {
                    object = new StringBuilder();
                    ((StringBuilder)object).append("Invalid radius value: ");
                    ((StringBuilder)object).append(f5);
                    throw new IllegalArgumentException(((StringBuilder)object).toString());
                }
            }
            this.mCornerRadius = 0.0f;
        }
        object = this.mCornersRounded;
        boolean bl2 = f > 0.0f;
        object[0] = bl2;
        bl2 = f2 > 0.0f;
        object[1] = bl2;
        bl2 = f3 > 0.0f;
        object[2] = bl2;
        bl2 = f4 > 0.0f ? bl : false;
        object[3] = bl2;
        return this;
    }

    public RoundedDrawable setCornerRadius(int n, float f) {
        float f2;
        if (f != 0.0f && (f2 = this.mCornerRadius) != 0.0f && f2 != f) {
            throw new IllegalArgumentException("Multiple nonzero corner radii not yet supported.");
        }
        if (f == 0.0f) {
            if (RoundedDrawable.only(n, this.mCornersRounded)) {
                this.mCornerRadius = 0.0f;
            }
            this.mCornersRounded[n] = false;
        } else {
            if (this.mCornerRadius == 0.0f) {
                this.mCornerRadius = f;
            }
            this.mCornersRounded[n] = true;
        }
        return this;
    }

    public void setDither(boolean bl) {
        this.mBitmapPaint.setDither(bl);
        this.invalidateSelf();
    }

    public void setFilterBitmap(boolean bl) {
        this.mBitmapPaint.setFilterBitmap(bl);
        this.invalidateSelf();
    }

    public RoundedDrawable setOval(boolean bl) {
        this.mOval = bl;
        return this;
    }

    public RoundedDrawable setScaleType(ImageView.ScaleType scaleType) {
        ImageView.ScaleType scaleType2 = scaleType;
        if (scaleType == null) {
            scaleType2 = ImageView.ScaleType.FIT_CENTER;
        }
        if (this.mScaleType != scaleType2) {
            this.mScaleType = scaleType2;
            this.updateShaderMatrix();
        }
        return this;
    }

    public RoundedDrawable setTileModeX(Shader.TileMode tileMode) {
        if (this.mTileModeX != tileMode) {
            this.mTileModeX = tileMode;
            this.mRebuildShader = true;
            this.invalidateSelf();
        }
        return this;
    }

    public RoundedDrawable setTileModeY(Shader.TileMode tileMode) {
        if (this.mTileModeY != tileMode) {
            this.mTileModeY = tileMode;
            this.mRebuildShader = true;
            this.invalidateSelf();
        }
        return this;
    }

    public Bitmap toBitmap() {
        return RoundedDrawable.drawableToBitmap(this);
    }
}

