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
import com.tuyenmonkey.mkloader.exception.InvalidNumberOfPulseException;
import com.tuyenmonkey.mkloader.model.Line;
import com.tuyenmonkey.mkloader.type.LoaderView;

public class Pulse
extends LoaderView {
    private float lineDistance;
    private float lineWidth;
    private Line[] lines;
    private int numberOfLines;
    private float[] scaleY;

    public Pulse(int n) throws InvalidNumberOfPulseException {
        if (n >= 3 && n <= 5) {
            this.numberOfLines = n;
            this.lines = new Line[n];
            this.scaleY = new float[n];
            return;
        }
        throw new InvalidNumberOfPulseException();
    }

    @Override
    public void draw(Canvas canvas) {
        for (int i = 0; i < this.numberOfLines; ++i) {
            canvas.save();
            canvas.translate((float)i * (this.lineWidth + this.lineDistance), 0.0f);
            canvas.scale(1.0f, this.scaleY[i], this.lines[i].getPoint1().x, this.center.y);
            this.lines[i].draw(canvas);
            canvas.restore();
        }
    }

    @Override
    public void initializeObjects() {
        float f;
        this.lineWidth = f = (float)(this.width / (this.numberOfLines * 2));
        this.lineDistance = f / 4.0f;
        float f2 = this.width;
        f = this.lineWidth;
        int n = this.numberOfLines;
        f = (f2 - ((float)n * f + this.lineDistance * (float)(n - 1))) / 2.0f + f / 2.0f;
        for (n = 0; n < this.numberOfLines; ++n) {
            this.lines[n] = new Line();
            this.lines[n].setColor(this.color);
            this.lines[n].setWidth(this.lineWidth);
            this.lines[n].setPoint1(new PointF(f, this.center.y - (float)this.height / 4.0f));
            this.lines[n].setPoint2(new PointF(f, this.center.y + (float)this.height / 4.0f));
        }
    }

    @Override
    public void setUpAnimation() {
        for (int i = 0; i < this.numberOfLines; ++i) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat((float[])new float[]{1.0f, 1.5f, 1.0f});
            valueAnimator.setDuration(1000L);
            valueAnimator.setStartDelay((long)(i * 120));
            valueAnimator.setRepeatCount(-1);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this, i){
                final Pulse this$0;
                final int val$index;
                {
                    this.this$0 = pulse;
                    this.val$index = n;
                }

                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ((Pulse)this.this$0).scaleY[this.val$index] = ((Float)valueAnimator.getAnimatedValue()).floatValue();
                    if (this.this$0.invalidateListener != null) {
                        this.this$0.invalidateListener.reDraw();
                    }
                }
            });
            valueAnimator.start();
        }
    }
}

