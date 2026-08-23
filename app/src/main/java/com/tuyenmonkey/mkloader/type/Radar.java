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

public class Radar
extends LoaderView {
    private float degree;
    private Line line;

    static /* synthetic */ float access$002(Radar radar, float f) {
        radar.degree = f;
        return f;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.rotate(this.degree, this.center.x, this.center.y);
        this.line.draw(canvas);
        canvas.restore();
    }

    @Override
    public void initializeObjects() {
        Line line;
        float f = Math.min(this.width, this.height);
        this.line = line = new Line();
        line.setPoint1(this.center);
        this.line.setPoint2(new PointF(0.0f, f / 2.0f));
        this.line.setColor(this.color);
        this.line.setWidth(5.0f);
    }

    @Override
    public void setUpAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat((float[])new float[]{0.0f, 359.0f});
        valueAnimator.setDuration(1000L);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this){
            final Radar this$0;
            {
                this.this$0 = radar;
            }

            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Radar.access$002(this.this$0, ((Float)valueAnimator.getAnimatedValue()).floatValue());
                if (this.this$0.invalidateListener != null) {
                    this.this$0.invalidateListener.reDraw();
                }
            }
        });
        valueAnimator.start();
    }
}

