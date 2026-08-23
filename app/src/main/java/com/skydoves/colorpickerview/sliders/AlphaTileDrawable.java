/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.BitmapShader
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.Paint
 *  android.graphics.Paint$Style
 *  android.graphics.Rect
 *  android.graphics.Shader
 *  android.graphics.Shader$TileMode
 *  android.graphics.drawable.Drawable
 */
package com.skydoves.colorpickerview.sliders;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

public class AlphaTileDrawable
extends Drawable {
    private Paint paint = new Paint(1);
    private int tileEvenColor;
    private int tileOddColor;
    private int tileSize;

    public AlphaTileDrawable() {
        Builder builder = new Builder();
        this.tileSize = builder.tileSize;
        this.tileOddColor = builder.tileOddColor;
        this.tileEvenColor = builder.tileEvenColor;
        this.drawTiles();
    }

    public AlphaTileDrawable(Builder builder) {
        this.tileSize = builder.tileSize;
        this.tileOddColor = builder.tileOddColor;
        this.tileEvenColor = builder.tileEvenColor;
        this.drawTiles();
    }

    private void drawTile(Canvas canvas, Rect rect, Paint paint, int n, int n2) {
        rect.offset(n, n2);
        canvas.drawRect(rect, paint);
    }

    private void drawTiles() {
        int n = this.tileSize;
        Bitmap bitmap = Bitmap.createBitmap((int)(n * 2), (int)(n * 2), (Bitmap.Config)Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        n = this.tileSize;
        Rect rect = new Rect(0, 0, n, n);
        Paint paint = new Paint(1);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(this.tileOddColor);
        this.drawTile(canvas, rect, paint, 0, 0);
        n = this.tileSize;
        this.drawTile(canvas, rect, paint, n, n);
        paint.setColor(this.tileEvenColor);
        this.drawTile(canvas, rect, paint, -this.tileSize, 0);
        n = this.tileSize;
        this.drawTile(canvas, rect, paint, n, -n);
        this.paint.setShader((Shader)new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
    }

    public void draw(Canvas canvas) {
        canvas.drawPaint(this.paint);
    }

    public int getOpacity() {
        return -1;
    }

    public void setAlpha(int n) {
        this.paint.setAlpha(n);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
    }

    public static class Builder {
        private int tileEvenColor = -3421237;
        private int tileOddColor = -1;
        private int tileSize = 25;

        public AlphaTileDrawable build() {
            return new AlphaTileDrawable(this);
        }

        public int getTileEvenColor() {
            return this.tileEvenColor;
        }

        public int getTileOddColor() {
            return this.tileOddColor;
        }

        public int getTileSize() {
            return this.tileSize;
        }

        public Builder setTileEvenColor(int n) {
            this.tileEvenColor = n;
            return this;
        }

        public Builder setTileOddColor(int n) {
            this.tileOddColor = n;
            return this;
        }

        public Builder setTileSize(int n) {
            this.tileSize = n;
            return this;
        }
    }
}

