/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.AnimatorSet
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.graphics.Canvas
 *  android.graphics.Paint$Style
 */
package com.tuyenmonkey.mkloader.type;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.tuyenmonkey.mkloader.model.Circle;
import com.tuyenmonkey.mkloader.type.LoaderView;

public class Sharingan
extends LoaderView {
    private Circle eye;
    private Circle eyeBound;
    private float eyeBoundRadius;
    private float eyeBoundRadiusScale;
    private int numberOfSharingan = 3;
    private float rotate;
    private float scale;
    private Circle[] sharingans;

    static /* synthetic */ float access$002(Sharingan sharingan, float f) {
        sharingan.rotate = f;
        return f;
    }

    static /* synthetic */ float access$102(Sharingan sharingan, float f) {
        sharingan.scale = f;
        return f;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        float f = this.scale;
        canvas.scale(f, f, this.center.x, this.center.y);
        canvas.rotate(this.rotate, this.center.x, this.center.y);
        this.eye.draw(canvas);
        this.eyeBound.draw(canvas);
        for (int i = 0; i < this.numberOfSharingan; ++i) {
            canvas.save();
            canvas.rotate((float)(i * 120), this.center.x, this.center.y);
            this.sharingans[i].draw(canvas);
            canvas.restore();
        }
        canvas.restore();
    }

    @Override
    public void initializeObjects() {
        Circle circle;
        float f = (float)Math.min(this.width, this.height) / 2.0f;
        this.eyeBoundRadius = f / 1.5f;
        this.eye = circle = new Circle();
        circle.setCenter(this.center.x, this.center.y);
        this.eye.setColor(this.color);
        this.eye.setRadius(f / 4.0f);
        this.eyeBound = circle = new Circle();
        circle.setCenter(this.center.x, this.center.y);
        this.eyeBound.setColor(this.color);
        this.eyeBound.setRadius(this.eyeBoundRadius);
        this.eyeBound.setStyle(Paint.Style.STROKE);
        this.eyeBound.setWidth(f / 20.0f);
        this.sharingans = new Circle[this.numberOfSharingan];
        for (int i = 0; i < this.numberOfSharingan; ++i) {
            this.sharingans[i] = new Circle();
            this.sharingans[i].setCenter(this.center.x, this.center.y - this.eyeBoundRadius);
            this.sharingans[i].setColor(this.color);
            this.sharingans[i].setRadius(f / 6.0f);
        }
    }

    @Override
    public void setUpAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat((float[])new float[]{0.0f, 360.0f});
        valueAnimator.setDuration(1500L);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this){
            final Sharingan this$0;
            {
                this.this$0 = sharingan;
            }

            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Sharingan.access$002(this.this$0, ((Float)valueAnimator.getAnimatedValue()).floatValue());
                if (this.this$0.invalidateListener != null) {
                    this.this$0.invalidateListener.reDraw();
                }
            }
        });
        ValueAnimator valueAnimator2 = ValueAnimator.ofFloat((float[])new float[]{1.0f, 0.8f, 1.0f});
        valueAnimator2.setDuration(1000L);
        valueAnimator2.setRepeatCount(-1);
        valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this){
            final Sharingan this$0;
            {
                this.this$0 = sharingan;
            }

            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Sharingan.access$102(this.this$0, ((Float)valueAnimator.getAnimatedValue()).floatValue());
                if (this.this$0.invalidateListener != null) {
                    this.this$0.invalidateListener.reDraw();
                }
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play((Animator)valueAnimator).with((Animator)valueAnimator2);
        animatorSet.start();
    }
}

