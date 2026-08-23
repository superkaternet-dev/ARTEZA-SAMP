/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.graphics.Canvas
 */
package com.tuyenmonkey.mkloader.type;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import com.tuyenmonkey.mkloader.model.Circle;
import com.tuyenmonkey.mkloader.type.LoaderView;

public class ClassicSpinner
extends LoaderView {
    private Circle[] circles;
    private int circlesSize = 8;

    @Override
    public void draw(Canvas canvas) {
        for (int i = 0; i < this.circlesSize; ++i) {
            canvas.save();
            canvas.rotate((float)(i * 45), this.center.x, this.center.y);
            this.circles[i].draw(canvas);
            canvas.restore();
        }
    }

    @Override
    public void initializeObjects() {
        float f = (float)Math.min(this.width, this.height) / 10.0f;
        this.circles = new Circle[this.circlesSize];
        for (int i = 0; i < this.circlesSize; ++i) {
            this.circles[i] = new Circle();
            this.circles[i].setCenter(this.center.x, f);
            this.circles[i].setColor(this.color);
            this.circles[i].setAlpha(126);
            this.circles[i].setRadius(f);
        }
    }

    @Override
    public void setUpAnimation() {
        for (int i = 0; i < this.circlesSize; ++i) {
            ValueAnimator valueAnimator = ValueAnimator.ofInt((int[])new int[]{126, 255, 126});
            valueAnimator.setRepeatCount(-1);
            valueAnimator.setDuration(1000L);
            valueAnimator.setStartDelay((long)(i * 120));
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this, i){
                final ClassicSpinner this$0;
                final int val$index;
                {
                    this.this$0 = classicSpinner;
                    this.val$index = n;
                }

                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    this.this$0.circles[this.val$index].setAlpha((Integer)valueAnimator.getAnimatedValue());
                    if (this.this$0.invalidateListener != null) {
                        this.this$0.invalidateListener.reDraw();
                    }
                }
            });
            valueAnimator.start();
        }
    }
}

