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

public class FishSpinner
extends LoaderView {
    private Circle[] circles;
    private int numberOfCircle = 5;
    private float[] rotates = new float[5];

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
        float f = (float)Math.min(this.width, this.height) / 10.0f;
        this.circles = new Circle[this.numberOfCircle];
        for (int i = 0; i < this.numberOfCircle; ++i) {
            this.circles[i] = new Circle();
            this.circles[i].setCenter(this.center.x, f);
            this.circles[i].setColor(this.color);
            this.circles[i].setRadius(f - (float)i * f / 6.0f);
        }
    }

    @Override
    public void setUpAnimation() {
        for (int i = 0; i < this.numberOfCircle; ++i) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat((float[])new float[]{0.0f, 360.0f});
            valueAnimator.setRepeatCount(-1);
            valueAnimator.setDuration(1700L);
            valueAnimator.setStartDelay((long)(i * 100));
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this, i){
                final FishSpinner this$0;
                final int val$index;
                {
                    this.this$0 = fishSpinner;
                    this.val$index = n;
                }

                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ((FishSpinner)this.this$0).rotates[this.val$index] = ((Float)valueAnimator.getAnimatedValue()).floatValue();
                    if (this.this$0.invalidateListener != null) {
                        this.this$0.invalidateListener.reDraw();
                    }
                }
            });
            valueAnimator.start();
        }
    }
}

