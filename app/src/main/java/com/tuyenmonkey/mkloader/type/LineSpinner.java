/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.graphics.Canvas
 *  android.graphics.PointF
 */
package com.tuyenmonkey.mkloader.type;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.PointF;
import com.tuyenmonkey.mkloader.model.Line;
import com.tuyenmonkey.mkloader.type.LoaderView;

public class LineSpinner
extends LoaderView {
    private Line[] lines;
    private int numberOfLine = 8;

    @Override
    public void draw(Canvas canvas) {
        for (int i = 0; i < this.numberOfLine; ++i) {
            canvas.save();
            canvas.rotate((float)(i * 45), this.center.x, this.center.y);
            this.lines[i].draw(canvas);
            canvas.restore();
        }
    }

    @Override
    public void initializeObjects() {
        int n = Math.min(this.width, this.height);
        float f = (float)n / 10.0f;
        this.lines = new Line[this.numberOfLine];
        for (int i = 0; i < this.numberOfLine; ++i) {
            this.lines[i] = new Line();
            this.lines[i].setColor(this.color);
            this.lines[i].setAlpha(126);
            this.lines[i].setWidth(f);
            this.lines[i].setPoint1(new PointF(this.center.x, this.center.y - (float)n / 2.0f + f));
            this.lines[i].setPoint2(new PointF(this.center.x, this.lines[i].getPoint1().y + 2.0f * f));
        }
    }

    @Override
    public void setUpAnimation() {
        for (int i = 0; i < this.numberOfLine; ++i) {
            ValueAnimator valueAnimator = ValueAnimator.ofInt((int[])new int[]{126, 255, 126});
            valueAnimator.setRepeatCount(-1);
            valueAnimator.setDuration(1000L);
            valueAnimator.setStartDelay((long)(i * 120));
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this, i){
                final LineSpinner this$0;
                final int val$index;
                {
                    this.this$0 = lineSpinner;
                    this.val$index = n;
                }

                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    this.this$0.lines[this.val$index].setAlpha((Integer)valueAnimator.getAnimatedValue());
                    if (this.this$0.invalidateListener != null) {
                        this.this$0.invalidateListener.reDraw();
                    }
                }
            });
            valueAnimator.start();
        }
    }
}

