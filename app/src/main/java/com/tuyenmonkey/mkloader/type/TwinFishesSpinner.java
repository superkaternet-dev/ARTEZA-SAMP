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

public class TwinFishesSpinner
extends LoaderView {
    private Circle[] circles;
    private int numberOfCircle = 10;
    private float[] rotates = new float[10];

    @Override
    public void draw(Canvas canvas) {
        for (int i = 0; i < this.numberOfCircle; ++i) {
            canvas.save();
            canvas.rotate(this.rotates[i], this.center.x, this.center.y);
            this.circles[i].draw(canvas);
            canvas.restore();
        }
    }

    @Override
    public void initializeObjects() {
        int n;
        int n2;
        float f = Math.min(this.width, this.height);
        float f2 = f / 10.0f;
        this.circles = new Circle[this.numberOfCircle];
        for (n2 = 0; n2 < (n = this.numberOfCircle) / 2; ++n2) {
            this.circles[n2] = new Circle();
            this.circles[n2].setCenter(this.center.x, f2);
            this.circles[n2].setColor(this.color);
            this.circles[n2].setRadius(f2 - (float)n2 * f2 / 6.0f);
        }
        for (n2 = n / 2; n2 < this.numberOfCircle; ++n2) {
            this.circles[n2] = new Circle();
            this.circles[n2].setCenter(this.center.x, f - f2);
            this.circles[n2].setColor(this.color);
            this.circles[n2].setRadius(f2 - (float)(n2 - 5) * f2 / 6.0f);
        }
    }

    @Override
    public void setUpAnimation() {
        for (int i = 0; i < this.numberOfCircle; ++i) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat((float[])new float[]{0.0f, 360.0f});
            valueAnimator.setRepeatCount(-1);
            valueAnimator.setDuration(1700L);
            int n = i >= 5 ? i - 5 : i;
            valueAnimator.setStartDelay((long)(n * 100));
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this, i){
                final TwinFishesSpinner this$0;
                final int val$index;
                {
                    this.this$0 = twinFishesSpinner;
                    this.val$index = n;
                }

                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ((TwinFishesSpinner)this.this$0).rotates[this.val$index] = ((Float)valueAnimator.getAnimatedValue()).floatValue();
                    if (this.this$0.invalidateListener != null) {
                        this.this$0.invalidateListener.reDraw();
                    }
                }
            });
            valueAnimator.start();
        }
    }
}

