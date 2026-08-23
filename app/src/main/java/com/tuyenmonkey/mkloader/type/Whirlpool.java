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

public class Whirlpool
extends LoaderView {
    private Arc[] arcs;
    private int numberOfArc = 3;
    private float[] rotates;

    @Override
    public void draw(Canvas canvas) {
        for (int i = 0; i < this.numberOfArc; ++i) {
            canvas.save();
            canvas.rotate(this.rotates[i], this.center.x, this.center.y);
            this.arcs[i].draw(canvas);
            canvas.restore();
        }
    }

    @Override
    public void initializeObjects() {
        float f = (float)Math.min(this.width, this.height) / 2.0f;
        int n = this.numberOfArc;
        this.arcs = new Arc[n];
        this.rotates = new float[n];
        for (n = 0; n < this.numberOfArc; ++n) {
            float f2 = f / 4.0f + (float)n * f / 4.0f;
            this.arcs[n] = new Arc();
            this.arcs[n].setColor(this.color);
            this.arcs[n].setOval(new RectF(this.center.x - f2, this.center.y - f2, this.center.x + f2, this.center.y + f2));
            this.arcs[n].setStartAngle(n * 45);
            this.arcs[n].setSweepAngle(n * 45 + 90);
            this.arcs[n].setStyle(Paint.Style.STROKE);
            this.arcs[n].setWidth(f / 10.0f);
        }
    }

    @Override
    public void setUpAnimation() {
        for (int i = this.numberOfArc - 1; i >= 0; --i) {
            float f = this.arcs[i].getStartAngle();
            float f2 = this.arcs[i].getStartAngle();
            int n = i % 2 == 0 ? -1 : 1;
            ValueAnimator valueAnimator = ValueAnimator.ofFloat((float[])new float[]{f, f2 + (float)(n * 360)});
            valueAnimator.setRepeatCount(-1);
            valueAnimator.setDuration((long)((i + 1) * 500));
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this, i){
                final Whirlpool this$0;
                final int val$index;
                {
                    this.this$0 = whirlpool;
                    this.val$index = n;
                }

                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ((Whirlpool)this.this$0).rotates[this.val$index] = ((Float)valueAnimator.getAnimatedValue()).floatValue();
                    if (this.this$0.invalidateListener != null) {
                        this.this$0.invalidateListener.reDraw();
                    }
                }
            });
            valueAnimator.start();
        }
    }
}

