/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.BlurMaskFilter
 *  android.graphics.BlurMaskFilter$Blur
 *  android.graphics.Canvas
 *  android.graphics.Color
 *  android.graphics.LinearGradient
 *  android.graphics.MaskFilter
 *  android.graphics.Matrix
 *  android.graphics.Paint
 *  android.graphics.Paint$Align
 *  android.graphics.Paint$Cap
 *  android.graphics.Paint$Style
 *  android.graphics.RadialGradient
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.graphics.Shader$TileMode
 *  android.graphics.SweepGradient
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.text.TextPaint
 *  android.text.TextUtils
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$BaseSavedState
 */
package com.dinuscxj.progressbar;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.MaskFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import com.dinuscxj.progressbar.R;

public class CircleProgressBar
extends View {
    private static final String COLOR_FFD3D3D5 = "#ffe3e3e5";
    private static final String COLOR_FFF2A670 = "#fff2a670";
    private static final int DEFAULT_LINE_COUNT = 45;
    private static final float DEFAULT_LINE_WIDTH = 4.0f;
    private static final int DEFAULT_MAX = 100;
    private static final float DEFAULT_PROGRESS_STROKE_WIDTH = 1.0f;
    private static final float DEFAULT_PROGRESS_TEXT_SIZE = 11.0f;
    private static final int DEFAULT_START_DEGREE = -90;
    public static final int LINE = 0;
    public static final int LINEAR = 0;
    private static final float LINEAR_START_DEGREE = 90.0f;
    private static final float MAX_DEGREE = 360.0f;
    public static final int RADIAL = 1;
    public static final int SOLID = 1;
    public static final int SOLID_LINE = 2;
    public static final int SWEEP = 2;
    private int mBlurRadius;
    private BlurMaskFilter.Blur mBlurStyle;
    private final RectF mBoundsRectF;
    private Paint.Cap mCap;
    private float mCenterX;
    private float mCenterY;
    private boolean mDrawBackgroundOutsideProgress;
    private int mLineCount;
    private float mLineWidth;
    private int mMax = 100;
    private int mProgress;
    private int mProgressBackgroundColor;
    private final Paint mProgressBackgroundPaint;
    private int mProgressEndColor;
    private ProgressFormatter mProgressFormatter;
    private final Paint mProgressPaint;
    private final RectF mProgressRectF = new RectF();
    private int mProgressStartColor;
    private float mProgressStrokeWidth;
    private int mProgressTextColor;
    private final Paint mProgressTextPaint;
    private final Rect mProgressTextRect;
    private float mProgressTextSize;
    private float mRadius;
    private int mShader;
    private int mStartDegree;
    private int mStyle;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mBoundsRectF = new RectF();
        this.mProgressTextRect = new Rect();
        this.mProgressPaint = new Paint(1);
        this.mProgressBackgroundPaint = new Paint(1);
        this.mProgressTextPaint = new TextPaint(1);
        this.mProgressFormatter = new DefaultProgressFormatter();
        this.initFromAttributes(context, attributeSet);
        this.initPaint();
    }

    private static int dip2px(Context context, float f) {
        return (int)(f * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    private void drawLineProgress(Canvas canvas) {
        int n = this.mLineCount;
        double d = n;
        Double.isNaN(d);
        float f = (float)(Math.PI * 2 / d);
        float f2 = this.mRadius;
        float f3 = this.mRadius - this.mLineWidth;
        int n2 = (int)((float)this.mProgress / (float)this.mMax * (float)n);
        for (n = 0; n < this.mLineCount; ++n) {
            float f4 = (float)n * -f;
            float f5 = this.mCenterX + (float)Math.cos(f4) * f3;
            float f6 = this.mCenterY - (float)Math.sin(f4) * f3;
            float f7 = this.mCenterX + (float)Math.cos(f4) * f2;
            f4 = this.mCenterY - (float)Math.sin(f4) * f2;
            if (this.mDrawBackgroundOutsideProgress) {
                if (n >= n2) {
                    canvas.drawLine(f5, f6, f7, f4, this.mProgressBackgroundPaint);
                }
            } else {
                canvas.drawLine(f5, f6, f7, f4, this.mProgressBackgroundPaint);
            }
            if (n >= n2) continue;
            canvas.drawLine(f5, f6, f7, f4, this.mProgressPaint);
        }
    }

    private void drawProgress(Canvas canvas) {
        switch (this.mStyle) {
            default: {
                this.drawLineProgress(canvas);
                break;
            }
            case 2: {
                this.drawSolidLineProgress(canvas);
                break;
            }
            case 1: {
                this.drawSolidProgress(canvas);
            }
        }
    }

    private void drawProgressText(Canvas canvas) {
        Object object = this.mProgressFormatter;
        if (object == null) {
            return;
        }
        if (TextUtils.isEmpty((CharSequence)(object = object.format(this.mProgress, this.mMax)))) {
            return;
        }
        this.mProgressTextPaint.setTextSize(this.mProgressTextSize);
        this.mProgressTextPaint.setColor(this.mProgressTextColor);
        this.mProgressTextPaint.getTextBounds(String.valueOf(object), 0, object.length(), this.mProgressTextRect);
        canvas.drawText((CharSequence)object, 0, object.length(), this.mCenterX, this.mCenterY + (float)(this.mProgressTextRect.height() / 2), this.mProgressTextPaint);
    }

    private void drawSolidLineProgress(Canvas canvas) {
        if (this.mDrawBackgroundOutsideProgress) {
            float f = (float)this.mProgress * 360.0f / (float)this.mMax;
            canvas.drawArc(this.mProgressRectF, f, 360.0f - f, false, this.mProgressBackgroundPaint);
        } else {
            canvas.drawArc(this.mProgressRectF, 0.0f, 360.0f, false, this.mProgressBackgroundPaint);
        }
        canvas.drawArc(this.mProgressRectF, 0.0f, (float)this.mProgress * 360.0f / (float)this.mMax, false, this.mProgressPaint);
    }

    private void drawSolidProgress(Canvas canvas) {
        if (this.mDrawBackgroundOutsideProgress) {
            float f = (float)this.mProgress * 360.0f / (float)this.mMax;
            canvas.drawArc(this.mProgressRectF, f, 360.0f - f, true, this.mProgressBackgroundPaint);
        } else {
            canvas.drawArc(this.mProgressRectF, 0.0f, 360.0f, true, this.mProgressBackgroundPaint);
        }
        canvas.drawArc(this.mProgressRectF, 0.0f, (float)this.mProgress * 360.0f / (float)this.mMax, true, this.mProgressPaint);
    }

    private void initFromAttributes(Context context, AttributeSet attributeSet) {
        attributeSet = context.obtainStyledAttributes(attributeSet, R.styleable.CircleProgressBar);
        this.mLineCount = attributeSet.getInt(R.styleable.CircleProgressBar_line_count, 45);
        this.mStyle = attributeSet.getInt(R.styleable.CircleProgressBar_progress_style, 0);
        this.mShader = attributeSet.getInt(R.styleable.CircleProgressBar_progress_shader, 0);
        context = attributeSet.hasValue(R.styleable.CircleProgressBar_progress_stroke_cap) ? Paint.Cap.values()[attributeSet.getInt(R.styleable.CircleProgressBar_progress_stroke_cap, 0)] : Paint.Cap.BUTT;
        this.mCap = context;
        this.mLineWidth = attributeSet.getDimensionPixelSize(R.styleable.CircleProgressBar_line_width, CircleProgressBar.dip2px(this.getContext(), 4.0f));
        this.mProgressTextSize = attributeSet.getDimensionPixelSize(R.styleable.CircleProgressBar_progress_text_size, CircleProgressBar.dip2px(this.getContext(), 11.0f));
        this.mProgressStrokeWidth = attributeSet.getDimensionPixelSize(R.styleable.CircleProgressBar_progress_stroke_width, CircleProgressBar.dip2px(this.getContext(), 1.0f));
        this.mProgressStartColor = attributeSet.getColor(R.styleable.CircleProgressBar_progress_start_color, Color.parseColor((String)COLOR_FFF2A670));
        this.mProgressEndColor = attributeSet.getColor(R.styleable.CircleProgressBar_progress_end_color, Color.parseColor((String)COLOR_FFF2A670));
        this.mProgressTextColor = attributeSet.getColor(R.styleable.CircleProgressBar_progress_text_color, Color.parseColor((String)COLOR_FFF2A670));
        this.mProgressBackgroundColor = attributeSet.getColor(R.styleable.CircleProgressBar_progress_background_color, Color.parseColor((String)COLOR_FFD3D3D5));
        this.mStartDegree = attributeSet.getInt(R.styleable.CircleProgressBar_progress_start_degree, -90);
        this.mDrawBackgroundOutsideProgress = attributeSet.getBoolean(R.styleable.CircleProgressBar_drawBackgroundOutsideProgress, false);
        this.mBlurRadius = attributeSet.getDimensionPixelSize(R.styleable.CircleProgressBar_progress_blur_radius, 0);
        switch (attributeSet.getInt(R.styleable.CircleProgressBar_progress_blur_style, 0)) {
            default: {
                this.mBlurStyle = BlurMaskFilter.Blur.NORMAL;
                break;
            }
            case 3: {
                this.mBlurStyle = BlurMaskFilter.Blur.INNER;
                break;
            }
            case 2: {
                this.mBlurStyle = BlurMaskFilter.Blur.OUTER;
                break;
            }
            case 1: {
                this.mBlurStyle = BlurMaskFilter.Blur.SOLID;
            }
        }
        attributeSet.recycle();
    }

    private void initPaint() {
        this.mProgressTextPaint.setTextAlign(Paint.Align.CENTER);
        this.mProgressTextPaint.setTextSize(this.mProgressTextSize);
        Paint paint = this.mProgressPaint;
        Paint.Style style2 = this.mStyle == 1 ? Paint.Style.FILL : Paint.Style.STROKE;
        paint.setStyle(style2);
        this.mProgressPaint.setStrokeWidth(this.mProgressStrokeWidth);
        this.mProgressPaint.setColor(this.mProgressStartColor);
        this.mProgressPaint.setStrokeCap(this.mCap);
        this.updateMaskBlurFilter();
        paint = this.mProgressBackgroundPaint;
        style2 = this.mStyle == 1 ? Paint.Style.FILL : Paint.Style.STROKE;
        paint.setStyle(style2);
        this.mProgressBackgroundPaint.setStrokeWidth(this.mProgressStrokeWidth);
        this.mProgressBackgroundPaint.setColor(this.mProgressBackgroundColor);
        this.mProgressBackgroundPaint.setStrokeCap(this.mCap);
    }

    private void updateMaskBlurFilter() {
        if (this.mBlurStyle != null && this.mBlurRadius > 0) {
            this.setLayerType(1, this.mProgressPaint);
            this.mProgressPaint.setMaskFilter((MaskFilter)new BlurMaskFilter((float)this.mBlurRadius, this.mBlurStyle));
        } else {
            this.mProgressPaint.setMaskFilter(null);
        }
    }

    private void updateProgressShader() {
        if (this.mProgressStartColor != this.mProgressEndColor) {
            RadialGradient radialGradient = null;
            switch (this.mShader) {
                default: {
                    break;
                }
                case 2: {
                    double d = this.mProgressStrokeWidth;
                    Double.isNaN(d);
                    double d2 = d / Math.PI;
                    d = this.mRadius;
                    Double.isNaN(d);
                    float f = (float)(d2 * 2.0 / d);
                    d = this.mCap == Paint.Cap.BUTT && this.mStyle == 2 ? 0.0 : Math.toDegrees(f);
                    f = (float)(-d);
                    radialGradient = new SweepGradient(this.mCenterX, this.mCenterY, new int[]{this.mProgressStartColor, this.mProgressEndColor}, new float[]{0.0f, 1.0f});
                    Matrix matrix = new Matrix();
                    matrix.setRotate(f, this.mCenterX, this.mCenterY);
                    radialGradient.setLocalMatrix(matrix);
                    break;
                }
                case 1: {
                    radialGradient = new RadialGradient(this.mCenterX, this.mCenterY, this.mRadius, this.mProgressStartColor, this.mProgressEndColor, Shader.TileMode.CLAMP);
                    break;
                }
                case 0: {
                    radialGradient = new LinearGradient(this.mProgressRectF.left, this.mProgressRectF.top, this.mProgressRectF.left, this.mProgressRectF.bottom, this.mProgressStartColor, this.mProgressEndColor, Shader.TileMode.CLAMP);
                    Matrix matrix = new Matrix();
                    matrix.setRotate(90.0f, this.mCenterX, this.mCenterY);
                    radialGradient.setLocalMatrix(matrix);
                }
            }
            this.mProgressPaint.setShader(radialGradient);
        } else {
            this.mProgressPaint.setShader(null);
            this.mProgressPaint.setColor(this.mProgressStartColor);
        }
    }

    public int getMax() {
        return this.mMax;
    }

    public int getProgress() {
        return this.mProgress;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.rotate((float)this.mStartDegree, this.mCenterX, this.mCenterY);
        this.drawProgress(canvas);
        canvas.restore();
        this.drawProgressText(canvas);
    }

    public void onRestoreInstanceState(Parcelable object) {
        object = (SavedState)((Object)object);
        super.onRestoreInstanceState(object.getSuperState());
        this.setProgress(object.progress);
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.progress = this.mProgress;
        return savedState;
    }

    protected void onSizeChanged(int n, int n2, int n3, int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        this.mBoundsRectF.left = this.getPaddingLeft();
        this.mBoundsRectF.top = this.getPaddingTop();
        this.mBoundsRectF.right = n - this.getPaddingRight();
        this.mBoundsRectF.bottom = n2 - this.getPaddingBottom();
        this.mCenterX = this.mBoundsRectF.centerX();
        this.mCenterY = this.mBoundsRectF.centerY();
        this.mRadius = Math.min(this.mBoundsRectF.width(), this.mBoundsRectF.height()) / 2.0f;
        this.mProgressRectF.set(this.mBoundsRectF);
        this.updateProgressShader();
        RectF rectF = this.mProgressRectF;
        float f = this.mProgressStrokeWidth;
        rectF.inset(f / 2.0f, f / 2.0f);
    }

    public void setBlurRadius(int n) {
        this.mBlurRadius = n;
        this.updateMaskBlurFilter();
        this.invalidate();
    }

    public void setBlurStyle(BlurMaskFilter.Blur blur) {
        this.mBlurStyle = blur;
        this.updateMaskBlurFilter();
        this.invalidate();
    }

    public void setCap(Paint.Cap cap) {
        this.mCap = cap;
        this.mProgressPaint.setStrokeCap(cap);
        this.mProgressBackgroundPaint.setStrokeCap(cap);
        this.invalidate();
    }

    public void setDrawBackgroundOutsideProgress(boolean bl) {
        this.mDrawBackgroundOutsideProgress = bl;
        this.invalidate();
    }

    public void setLineCount(int n) {
        this.mLineCount = n;
        this.invalidate();
    }

    public void setLineWidth(float f) {
        this.mLineWidth = f;
        this.invalidate();
    }

    public void setMax(int n) {
        this.mMax = n;
        this.invalidate();
    }

    public void setProgress(int n) {
        this.mProgress = n;
        this.invalidate();
    }

    public void setProgressBackgroundColor(int n) {
        this.mProgressBackgroundColor = n;
        this.mProgressBackgroundPaint.setColor(n);
        this.invalidate();
    }

    public void setProgressEndColor(int n) {
        this.mProgressEndColor = n;
        this.updateProgressShader();
        this.invalidate();
    }

    public void setProgressFormatter(ProgressFormatter progressFormatter) {
        this.mProgressFormatter = progressFormatter;
        this.invalidate();
    }

    public void setProgressStartColor(int n) {
        this.mProgressStartColor = n;
        this.updateProgressShader();
        this.invalidate();
    }

    public void setProgressStrokeWidth(float f) {
        this.mProgressStrokeWidth = f;
        this.mProgressRectF.set(this.mBoundsRectF);
        this.updateProgressShader();
        RectF rectF = this.mProgressRectF;
        f = this.mProgressStrokeWidth;
        rectF.inset(f / 2.0f, f / 2.0f);
        this.invalidate();
    }

    public void setProgressTextColor(int n) {
        this.mProgressTextColor = n;
        this.invalidate();
    }

    public void setProgressTextSize(float f) {
        this.mProgressTextSize = f;
        this.invalidate();
    }

    public void setShader(int n) {
        this.mShader = n;
        this.updateProgressShader();
        this.invalidate();
    }

    public void setStartDegree(int n) {
        this.mStartDegree = n;
        this.invalidate();
    }

    public void setStyle(int n) {
        this.mStyle = n;
        Paint paint = this.mProgressPaint;
        Paint.Style style2 = n == 1 ? Paint.Style.FILL : Paint.Style.STROKE;
        paint.setStyle(style2);
        paint = this.mProgressBackgroundPaint;
        style2 = this.mStyle == 1 ? Paint.Style.FILL : Paint.Style.STROKE;
        paint.setStyle(style2);
        this.invalidate();
    }

    private static final class DefaultProgressFormatter
    implements ProgressFormatter {
        private static final String DEFAULT_PATTERN = "%d%%";

        private DefaultProgressFormatter() {
        }

        @Override
        public CharSequence format(int n, int n2) {
            return String.format(DEFAULT_PATTERN, (int)((float)n / (float)n2 * 100.0f));
        }
    }

    public static interface ProgressFormatter {
        public CharSequence format(int var1, int var2);
    }

    private static final class SavedState
    extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        int progress;

        private SavedState(Parcel parcel) {
            super(parcel);
            this.progress = parcel.readInt();
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.progress);
        }
    }
}

