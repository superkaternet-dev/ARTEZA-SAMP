/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.Paint
 *  android.graphics.Paint$Cap
 *  android.graphics.Paint$Join
 *  android.graphics.Paint$Style
 *  android.graphics.Path
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 */
package androidx.appcompat.graphics.drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.appcompat.R;
import androidx.core.graphics.drawable.DrawableCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DrawerArrowDrawable
extends Drawable {
    public static final int ARROW_DIRECTION_END = 3;
    public static final int ARROW_DIRECTION_LEFT = 0;
    public static final int ARROW_DIRECTION_RIGHT = 1;
    public static final int ARROW_DIRECTION_START = 2;
    private static final float ARROW_HEAD_ANGLE = (float)Math.toRadians(45.0);
    private float mArrowHeadLength;
    private float mArrowShaftLength;
    private float mBarGap;
    private float mBarLength;
    private int mDirection;
    private float mMaxCutForBarSize;
    private final Paint mPaint;
    private final Path mPath;
    private float mProgress;
    private final int mSize;
    private boolean mSpin;
    private boolean mVerticalMirror;

    public DrawerArrowDrawable(Context context) {
        Paint paint;
        this.mPaint = paint = new Paint();
        this.mPath = new Path();
        this.mVerticalMirror = false;
        this.mDirection = 2;
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.MITER);
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setAntiAlias(true);
        context = context.getTheme().obtainStyledAttributes(null, R.styleable.DrawerArrowToggle, R.attr.drawerArrowStyle, R.style.Base_Widget_AppCompat_DrawerArrowToggle);
        this.setColor(context.getColor(R.styleable.DrawerArrowToggle_color, 0));
        this.setBarThickness(context.getDimension(R.styleable.DrawerArrowToggle_thickness, 0.0f));
        this.setSpinEnabled(context.getBoolean(R.styleable.DrawerArrowToggle_spinBars, true));
        this.setGapSize(Math.round(context.getDimension(R.styleable.DrawerArrowToggle_gapBetweenBars, 0.0f)));
        this.mSize = context.getDimensionPixelSize(R.styleable.DrawerArrowToggle_drawableSize, 0);
        this.mBarLength = Math.round(context.getDimension(R.styleable.DrawerArrowToggle_barLength, 0.0f));
        this.mArrowHeadLength = Math.round(context.getDimension(R.styleable.DrawerArrowToggle_arrowHeadLength, 0.0f));
        this.mArrowShaftLength = context.getDimension(R.styleable.DrawerArrowToggle_arrowShaftLength, 0.0f);
        context.recycle();
    }

    private static float lerp(float f, float f2, float f3) {
        return (f2 - f) * f3 + f;
    }

    public void draw(Canvas canvas) {
        Rect rect = this.getBounds();
        int n = this.mDirection;
        int n2 = 0;
        int n3 = 0;
        switch (n) {
            default: {
                n3 = n2;
                if (DrawableCompat.getLayoutDirection(this) != 1) break;
                n3 = 1;
                break;
            }
            case 3: {
                if (DrawableCompat.getLayoutDirection(this) != 0) break;
                n3 = 1;
                break;
            }
            case 1: {
                n3 = 1;
                break;
            }
            case 0: {
                n3 = 0;
                break;
            }
        }
        float f = this.mArrowHeadLength;
        f = (float)Math.sqrt(f * f * 2.0f);
        float f2 = DrawerArrowDrawable.lerp(this.mBarLength, f, this.mProgress);
        float f3 = DrawerArrowDrawable.lerp(this.mBarLength, this.mArrowShaftLength, this.mProgress);
        float f4 = Math.round(DrawerArrowDrawable.lerp(0.0f, this.mMaxCutForBarSize, this.mProgress));
        float f5 = DrawerArrowDrawable.lerp(0.0f, ARROW_HEAD_ANGLE, this.mProgress);
        f = n3 != 0 ? 0.0f : -180.0f;
        float f6 = n3 != 0 ? 180.0f : 0.0f;
        f = DrawerArrowDrawable.lerp(f, f6, this.mProgress);
        double d = f2;
        double d2 = Math.cos(f5);
        Double.isNaN(d);
        f6 = Math.round(d * d2);
        d2 = f2;
        d = Math.sin(f5);
        Double.isNaN(d2);
        f5 = Math.round(d2 * d);
        this.mPath.rewind();
        float f7 = DrawerArrowDrawable.lerp(this.mBarGap + this.mPaint.getStrokeWidth(), -this.mMaxCutForBarSize, this.mProgress);
        f2 = -f3 / 2.0f;
        this.mPath.moveTo(f2 + f4, 0.0f);
        this.mPath.rLineTo(f3 - f4 * 2.0f, 0.0f);
        this.mPath.moveTo(f2, f7);
        this.mPath.rLineTo(f6, f5);
        this.mPath.moveTo(f2, -f7);
        this.mPath.rLineTo(f6, -f5);
        this.mPath.close();
        canvas.save();
        f3 = this.mPaint.getStrokeWidth();
        f4 = rect.height();
        f6 = this.mBarGap;
        f4 = (int)(f4 - 3.0f * f3 - 2.0f * f6) / 4 * 2;
        canvas.translate((float)rect.centerX(), f4 + (1.5f * f3 + f6));
        if (this.mSpin) {
            n3 = this.mVerticalMirror ^ n3 ? -1 : 1;
            canvas.rotate((float)n3 * f);
        } else if (n3 != 0) {
            canvas.rotate(180.0f);
        }
        canvas.drawPath(this.mPath, this.mPaint);
        canvas.restore();
    }

    public float getArrowHeadLength() {
        return this.mArrowHeadLength;
    }

    public float getArrowShaftLength() {
        return this.mArrowShaftLength;
    }

    public float getBarLength() {
        return this.mBarLength;
    }

    public float getBarThickness() {
        return this.mPaint.getStrokeWidth();
    }

    public int getColor() {
        return this.mPaint.getColor();
    }

    public int getDirection() {
        return this.mDirection;
    }

    public float getGapSize() {
        return this.mBarGap;
    }

    public int getIntrinsicHeight() {
        return this.mSize;
    }

    public int getIntrinsicWidth() {
        return this.mSize;
    }

    public int getOpacity() {
        return -3;
    }

    public final Paint getPaint() {
        return this.mPaint;
    }

    public float getProgress() {
        return this.mProgress;
    }

    public boolean isSpinEnabled() {
        return this.mSpin;
    }

    public void setAlpha(int n) {
        if (n != this.mPaint.getAlpha()) {
            this.mPaint.setAlpha(n);
            this.invalidateSelf();
        }
    }

    public void setArrowHeadLength(float f) {
        if (this.mArrowHeadLength != f) {
            this.mArrowHeadLength = f;
            this.invalidateSelf();
        }
    }

    public void setArrowShaftLength(float f) {
        if (this.mArrowShaftLength != f) {
            this.mArrowShaftLength = f;
            this.invalidateSelf();
        }
    }

    public void setBarLength(float f) {
        if (this.mBarLength != f) {
            this.mBarLength = f;
            this.invalidateSelf();
        }
    }

    public void setBarThickness(float f) {
        if (this.mPaint.getStrokeWidth() != f) {
            this.mPaint.setStrokeWidth(f);
            double d = f / 2.0f;
            double d2 = Math.cos(ARROW_HEAD_ANGLE);
            Double.isNaN(d);
            this.mMaxCutForBarSize = (float)(d * d2);
            this.invalidateSelf();
        }
    }

    public void setColor(int n) {
        if (n != this.mPaint.getColor()) {
            this.mPaint.setColor(n);
            this.invalidateSelf();
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
        this.invalidateSelf();
    }

    public void setDirection(int n) {
        if (n != this.mDirection) {
            this.mDirection = n;
            this.invalidateSelf();
        }
    }

    public void setGapSize(float f) {
        if (f != this.mBarGap) {
            this.mBarGap = f;
            this.invalidateSelf();
        }
    }

    public void setProgress(float f) {
        if (this.mProgress != f) {
            this.mProgress = f;
            this.invalidateSelf();
        }
    }

    public void setSpinEnabled(boolean bl) {
        if (this.mSpin != bl) {
            this.mSpin = bl;
            this.invalidateSelf();
        }
    }

    public void setVerticalMirror(boolean bl) {
        if (this.mVerticalMirror != bl) {
            this.mVerticalMirror = bl;
            this.invalidateSelf();
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface ArrowDirection {
    }
}

