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

public class Worm
extends LoaderView {
    private Circle[] circles;
    private int circlesSize = 5;
    private float radius;
    private int[] transformations = new int[]{-2, -1, 0, 1, 2};

    @Override
    public void draw(Canvas canvas) {
        for (int i = 0; i < this.circlesSize; ++i) {
            canvas.save();
            canvas.translate(this.radius * 2.0f * (float)this.transformations[i], 0.0f);
            this.circles[i].draw(canvas);
            canvas.restore();
        }
    }

    @Override
    public void initializeObjects() {
        this.circles = new Circle[this.circlesSize];
        this.radius = (float)this.width / 10.0f - (float)this.width / 100.0f;
        for (int i = 0; i < this.circlesSize; ++i) {
            this.circles[i] = new Circle();
            this.circles[i].setColor(this.color);
            this.circles[i].setRadius(this.radius);
            this.circles[i].setCenter(this.center.x, this.center.y);
        }
    }

    @Override
    public void setUpAnimation() {
        for (int i = 0; i < this.circlesSize; ++i) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat((float[])new float[]{this.center.y, (float)this.height / 4.0f, (float)(this.height * 3) / 4.0f, this.center.y});
            valueAnimator.setDuration(1000L);
            valueAnimator.setStartDelay((long)(i * 120));
            valueAnimator.setRepeatCount(-1);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this, i){
                final Worm this$0;
                final int val$index;
                {
                    this.this$0 = worm;
                    this.val$index = n;
                }

                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    this.this$0.circles[this.val$index].setCenter(this.this$0.center.x, ((Float)valueAnimator.getAnimatedValue()).floatValue());
                    if (this.this$0.invalidateListener != null) {
                        this.this$0.invalidateListener.reDraw();
                    }
                }
            });
            valueAnimator.start();
        }
    }
}

