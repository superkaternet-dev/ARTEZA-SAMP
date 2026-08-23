/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.RectF
 */
package androidx.cardview.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.cardview.widget.CardViewBaseImpl;
import androidx.cardview.widget.RoundRectDrawableWithShadow;

class CardViewApi17Impl
extends CardViewBaseImpl {
    CardViewApi17Impl() {
    }

    @Override
    public void initStatic() {
        RoundRectDrawableWithShadow.sRoundRectHelper = new RoundRectDrawableWithShadow.RoundRectHelper(this){
            final CardViewApi17Impl this$0;
            {
                this.this$0 = cardViewApi17Impl;
            }

            @Override
            public void drawRoundRect(Canvas canvas, RectF rectF, float f, Paint paint) {
                canvas.drawRoundRect(rectF, f, f, paint);
            }
        };
    }
}

