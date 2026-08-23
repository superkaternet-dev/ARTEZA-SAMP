/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.Paint$Cap
 *  android.graphics.Paint$Style
 *  android.graphics.RectF
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.view.MotionEvent
 *  android.view.View
 */
package com.blackrussia.game.gui.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.blackrussia.game.R;

public class SeekArc
extends View {
    private static int INVALID_PROGRESS_VALUE;
    private static final String TAG;
    private final int mAngleOffset;
    private Paint mArcPaint;
    private int mArcRadius = 0;
    private RectF mArcRect = new RectF();
    private int mArcWidth = 2;
    private boolean mClockwise = true;
    private boolean mEnabled = true;
    private int mMax = 100;
    private OnSeekArcChangeListener mOnSeekArcChangeListener;
    private int mProgress = 0;
    private Paint mProgressPaint;
    private float mProgressSweep = 0.0f;
    private int mProgressWidth = 4;
    private int mRotation = 0;
    private boolean mRoundedEdges = false;
    private int mStartAngle = 0;
    private int mSweepAngle = 360;
    private Drawable mThumb;
    private int mThumbXPos;
    private int mThumbYPos;
    private double mTouchAngle;
    private float mTouchIgnoreRadius;
    private boolean mTouchInside = true;
    private int mTranslateX;
    private int mTranslateY;

    static {
        TAG = SeekArc.class.getSimpleName();
        INVALID_PROGRESS_VALUE = -1;
    }

    public SeekArc(Context context) {
        super(context);
        this.mAngleOffset = -90;
        this.init(context, null, 0);
    }

    public SeekArc(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mAngleOffset = -90;
        this.init(context, attributeSet, 2130969269);
    }

    public SeekArc(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.mAngleOffset = -90;
        this.init(context, attributeSet, n);
    }

    private int getProgressForAngle(double d) {
        int n;
        block1: {
            double d2 = this.valuePerDegree();
            Double.isNaN(d2);
            n = (int)Math.round(d2 * d);
            if (n < 0) {
                n = INVALID_PROGRESS_VALUE;
            }
            if (n <= this.mMax) break block1;
            n = INVALID_PROGRESS_VALUE;
        }
        return n;
    }

    private double getTouchDegrees(float f, float f2) {
        double d;
        f -= (float)this.mTranslateX;
        float f3 = this.mTranslateY;
        if (!this.mClockwise) {
            f = -f;
        }
        double d2 = d = Math.toDegrees(Math.atan2(f2 - f3, f) + 1.5707963267948966 - Math.toRadians(this.mRotation));
        if (d < 0.0) {
            d2 = d + 360.0;
        }
        d = this.mStartAngle;
        Double.isNaN(d);
        return d2 - d;
    }

    private boolean ignoreTouch(float f, float f2) {
        boolean bl = false;
        if ((float)Math.sqrt((f -= (float)this.mTranslateX) * f + (f2 -= (float)this.mTranslateY) * f2) < this.mTouchIgnoreRadius) {
            bl = true;
        }
        return bl;
    }

    private void init(Context context, AttributeSet attributeSet, int n) {
        int n2;
        int n3;
        Log.d((String)TAG, (String)"Initialising SeekArc");
        Resources resources = this.getResources();
        float f = context.getResources().getDisplayMetrics().density;
        int n4 = resources.getColor(2131099803);
        int n5 = resources.getColor(2131099724);
        this.mThumb = resources.getDrawable(2131231180);
        this.mProgressWidth = (int)((float)this.mProgressWidth * f);
        if (attributeSet != null) {
            if ((attributeSet = (context = context.obtainStyledAttributes(attributeSet, R.styleable.SeekArc, n, 0)).getDrawable(12)) != null) {
                this.mThumb = attributeSet;
            }
            n3 = this.mThumb.getIntrinsicHeight() / 2;
            n = this.mThumb.getIntrinsicWidth() / 2;
            this.mThumb.setBounds(-n, -n3, n, n3);
            this.mMax = context.getInteger(4, this.mMax);
            this.mProgress = context.getInteger(5, this.mProgress);
            this.mProgressWidth = (int)context.getDimension(7, (float)this.mProgressWidth);
            this.mArcWidth = (int)context.getDimension(1, (float)this.mArcWidth);
            this.mStartAngle = context.getInt(10, this.mStartAngle);
            this.mSweepAngle = context.getInt(11, this.mSweepAngle);
            this.mRotation = context.getInt(8, this.mRotation);
            this.mRoundedEdges = context.getBoolean(9, this.mRoundedEdges);
            this.mTouchInside = context.getBoolean(14, this.mTouchInside);
            this.mClockwise = context.getBoolean(2, this.mClockwise);
            this.mEnabled = context.getBoolean(3, this.mEnabled);
            n4 = context.getColor(0, n4);
            n5 = context.getColor(6, n5);
            context.recycle();
        }
        int n6 = 0;
        n3 = this.mProgress;
        int n7 = this.mMax;
        n = n3;
        if (n3 > n7) {
            n = n7;
        }
        this.mProgress = n;
        n3 = n;
        if (n < 0) {
            n3 = 0;
        }
        this.mProgress = n3;
        n = n2 = this.mSweepAngle;
        if (n2 > 360) {
            n = 360;
        }
        this.mSweepAngle = n;
        n2 = n;
        if (n < 0) {
            n2 = 0;
        }
        this.mSweepAngle = n2;
        this.mProgressSweep = (float)n3 / (float)n7 * (float)n2;
        n = n3 = this.mStartAngle;
        if (n3 > 360) {
            n = 0;
        }
        this.mStartAngle = n;
        if (n < 0) {
            n = n6;
        }
        this.mStartAngle = n;
        context = new Paint();
        this.mArcPaint = context;
        context.setColor(n4);
        this.mArcPaint.setAntiAlias(true);
        this.mArcPaint.setStyle(Paint.Style.STROKE);
        this.mArcPaint.setStrokeWidth((float)this.mArcWidth);
        context = new Paint();
        this.mProgressPaint = context;
        context.setColor(n5);
        this.mProgressPaint.setAntiAlias(true);
        this.mProgressPaint.setStyle(Paint.Style.STROKE);
        this.mProgressPaint.setStrokeWidth((float)this.mProgressWidth);
        if (this.mRoundedEdges) {
            this.mArcPaint.setStrokeCap(Paint.Cap.ROUND);
            this.mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        }
    }

    private void onProgressRefresh(int n, boolean bl) {
        this.updateProgress(n, bl);
    }

    private void onStartTrackingTouch() {
        OnSeekArcChangeListener onSeekArcChangeListener = this.mOnSeekArcChangeListener;
        if (onSeekArcChangeListener != null) {
            onSeekArcChangeListener.onStartTrackingTouch(this);
        }
    }

    private void onStopTrackingTouch() {
        OnSeekArcChangeListener onSeekArcChangeListener = this.mOnSeekArcChangeListener;
        if (onSeekArcChangeListener != null) {
            onSeekArcChangeListener.onStopTrackingTouch(this);
        }
    }

    private void updateOnTouch(MotionEvent motionEvent) {
        double d;
        if (this.ignoreTouch(motionEvent.getX(), motionEvent.getY())) {
            return;
        }
        this.setPressed(true);
        this.mTouchAngle = d = this.getTouchDegrees(motionEvent.getX(), motionEvent.getY());
        this.onProgressRefresh(this.getProgressForAngle(d), true);
    }

    private void updateProgress(int n, boolean bl) {
        if (n == INVALID_PROGRESS_VALUE) {
            return;
        }
        int n2 = this.mMax;
        if (n > n2) {
            n = n2;
        }
        if (n < 0) {
            n = 0;
        }
        this.mProgress = n;
        OnSeekArcChangeListener onSeekArcChangeListener = this.mOnSeekArcChangeListener;
        if (onSeekArcChangeListener != null) {
            onSeekArcChangeListener.onProgressChanged(this, n, bl);
        }
        this.mProgressSweep = (float)n / (float)this.mMax * (float)this.mSweepAngle;
        this.updateThumbPosition();
        this.invalidate();
    }

    private void updateThumbPosition() {
        int n = (int)((float)this.mStartAngle + this.mProgressSweep + (float)this.mRotation + 90.0f);
        double d = this.mArcRadius;
        double d2 = Math.cos(Math.toRadians(n));
        Double.isNaN(d);
        this.mThumbXPos = (int)(d * d2);
        d = this.mArcRadius;
        d2 = Math.sin(Math.toRadians(n));
        Double.isNaN(d);
        this.mThumbYPos = (int)(d * d2);
    }

    private float valuePerDegree() {
        return (float)this.mMax / (float)this.mSweepAngle;
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Object object = this.mThumb;
        if (object != null && object.isStateful()) {
            object = this.getDrawableState();
            this.mThumb.setState((int[])object);
        }
        this.invalidate();
    }

    public int getArcColor() {
        return this.mArcPaint.getColor();
    }

    public int getArcRotation() {
        return this.mRotation;
    }

    public int getArcWidth() {
        return this.mArcWidth;
    }

    public int getMax() {
        return this.mMax;
    }

    public int getProgress() {
        return this.mProgress;
    }

    public int getProgressColor() {
        return this.mProgressPaint.getColor();
    }

    public int getProgressWidth() {
        return this.mProgressWidth;
    }

    public int getStartAngle() {
        return this.mStartAngle;
    }

    public int getSweepAngle() {
        return this.mSweepAngle;
    }

    public boolean isClockwise() {
        return this.mClockwise;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    protected void onDraw(Canvas canvas) {
        if (!this.mClockwise) {
            canvas.scale(-1.0f, 1.0f, this.mArcRect.centerX(), this.mArcRect.centerY());
        }
        int n = this.mStartAngle - 90 + this.mRotation;
        int n2 = this.mSweepAngle;
        canvas.drawArc(this.mArcRect, (float)n, (float)n2, false, this.mArcPaint);
        canvas.drawArc(this.mArcRect, (float)n, this.mProgressSweep, false, this.mProgressPaint);
        if (this.mEnabled) {
            canvas.translate((float)(this.mTranslateX - this.mThumbXPos), (float)(this.mTranslateY - this.mThumbYPos));
            this.mThumb.draw(canvas);
        }
    }

    protected void onMeasure(int n, int n2) {
        int n3 = SeekArc.getDefaultSize((int)this.getSuggestedMinimumHeight(), (int)n2);
        int n4 = SeekArc.getDefaultSize((int)this.getSuggestedMinimumWidth(), (int)n);
        int n5 = Math.min(n4, n3);
        this.mTranslateX = (int)((float)n4 * 0.5f);
        this.mTranslateY = (int)((float)n3 * 0.5f);
        this.mArcRadius = (n5 -= this.getPaddingLeft()) / 2;
        float f = n3 / 2 - n5 / 2;
        float f2 = n4 / 2 - n5 / 2;
        this.mArcRect.set(f2, f, (float)n5 + f2, (float)n5 + f);
        n3 = (int)this.mProgressSweep + this.mStartAngle + this.mRotation + 90;
        double d = this.mArcRadius;
        double d2 = Math.cos(Math.toRadians(n3));
        Double.isNaN(d);
        this.mThumbXPos = (int)(d * d2);
        d = this.mArcRadius;
        d2 = Math.sin(Math.toRadians(n3));
        Double.isNaN(d);
        this.mThumbYPos = (int)(d * d2);
        this.setTouchInSide(this.mTouchInside);
        super.onMeasure(n, n2);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mEnabled) {
            this.getParent().requestDisallowInterceptTouchEvent(true);
            switch (motionEvent.getAction()) {
                default: {
                    break;
                }
                case 3: {
                    this.onStopTrackingTouch();
                    this.setPressed(false);
                    this.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
                }
                case 2: {
                    this.updateOnTouch(motionEvent);
                    break;
                }
                case 1: {
                    this.onStopTrackingTouch();
                    this.setPressed(false);
                    this.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
                }
                case 0: {
                    this.onStartTrackingTouch();
                    this.updateOnTouch(motionEvent);
                }
            }
            return true;
        }
        return false;
    }

    public void setArcColor(int n) {
        this.mArcPaint.setColor(n);
        this.invalidate();
    }

    public void setArcRotation(int n) {
        this.mRotation = n;
        this.updateThumbPosition();
    }

    public void setArcWidth(int n) {
        this.mArcWidth = n;
        this.mArcPaint.setStrokeWidth((float)n);
    }

    public void setClockwise(boolean bl) {
        this.mClockwise = bl;
    }

    public void setEnabled(boolean bl) {
        this.mEnabled = bl;
    }

    public void setMax(int n) {
        this.mMax = n;
    }

    public void setOnSeekArcChangeListener(OnSeekArcChangeListener onSeekArcChangeListener) {
        this.mOnSeekArcChangeListener = onSeekArcChangeListener;
    }

    public void setProgress(int n) {
        this.updateProgress(n, false);
    }

    public void setProgressColor(int n) {
        this.mProgressPaint.setColor(n);
        this.invalidate();
    }

    public void setProgressWidth(int n) {
        this.mProgressWidth = n;
        this.mProgressPaint.setStrokeWidth((float)n);
    }

    public void setRoundedEdges(boolean bl) {
        this.mRoundedEdges = bl;
        if (bl) {
            this.mArcPaint.setStrokeCap(Paint.Cap.ROUND);
            this.mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        } else {
            this.mArcPaint.setStrokeCap(Paint.Cap.SQUARE);
            this.mProgressPaint.setStrokeCap(Paint.Cap.SQUARE);
        }
    }

    public void setStartAngle(int n) {
        this.mStartAngle = n;
        this.updateThumbPosition();
    }

    public void setSweepAngle(int n) {
        this.mSweepAngle = n;
        this.updateThumbPosition();
    }

    public void setTouchInSide(boolean bl) {
        int n = this.mThumb.getIntrinsicHeight() / 2;
        int n2 = this.mThumb.getIntrinsicWidth() / 2;
        this.mTouchInside = bl;
        this.mTouchIgnoreRadius = bl ? (float)this.mArcRadius / 4.0f : (float)(this.mArcRadius - Math.min(n2, n));
    }

    public static interface OnSeekArcChangeListener {
        public void onProgressChanged(SeekArc var1, int var2, boolean var3);

        public void onStartTrackingTouch(SeekArc var1);

        public void onStopTrackingTouch(SeekArc var1);
    }
}

