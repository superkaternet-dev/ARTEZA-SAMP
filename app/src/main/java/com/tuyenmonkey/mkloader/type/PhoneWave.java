/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.graphics.Canvas
 *  android.graphics.Paint$Style
 *  android.graphics.RectF
 */
package com.tuyenmonkey.mkloader.type;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import com.tuyenmonkey.mkloader.model.Arc;
import com.tuyenmonkey.mkloader.type.LoaderView;

public class PhoneWave
extends LoaderView {
    private Arc[] arcs;
    private int numberOfArc = 3;

    @Override
    public void draw(Canvas canvas) {
        for (int i = 0; i < this.numberOfArc; ++i) {
            this.arcs[i].draw(canvas);
        }
    }

    @Override
    public void initializeObjects() {
        float f = (float)Math.min(this.width, this.height) / 2.0f;
        this.arcs = new Arc[this.numberOfArc];
        for (int i = 0; i < this.numberOfArc; ++i) {
            float f2 = f / 4.0f + (float)i * f / 4.0f;
            this.arcs[i] = new Arc();
            this.arcs[i].setColor(this.color);
            this.arcs[i].setAlpha(126);
            this.arcs[i].setOval(new RectF(this.center.x - f2, this.center.y - f2 + f / 3.0f, this.center.x + f2, this.center.y + f2 + f / 3.0f));
            this.arcs[i].setStartAngle(225.0f);
            this.arcs[i].setSweepAngle(90.0f);
            this.arcs[i].setStyle(Paint.Style.STROKE);
            this.arcs[i].setWidth(f / 10.0f);
        }
    }

    @Override
    public void setUpAnimation() {
        for (int i = 0; i < this.numberOfArc; ++i) {
            ValueAnimator valueAnimator = ValueAnimator.ofInt((int[])new int[]{126, 255, 126});
            valueAnimator.setRepeatCount(-1);
            valueAnimator.setDuration(1000L);
            valueAnimator.setStartDelay((long)(i * 120));
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this, i){
                final PhoneWave this$0;
                final int val$index;
                {
                    this.this$0 = phoneWave;
                    this.val$index = n;
                }

                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    this.this$0.arcs[this.val$index].setAlpha((Integer)valueAnimator.getAnimatedValue());
                    if (this.this$0.invalidateListener != null) {
                        this.this$0.invalidateListener.reDraw();
                    }
                }
            });
            valueAnimator.start();
        }
    }
}

