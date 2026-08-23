/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.Path
 *  android.util.AttributeSet
 *  org.xmlpull.v1.XmlPullParser
 */
package androidx.transition;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.util.AttributeSet;
import androidx.core.content.res.TypedArrayUtils;
import androidx.transition.PathMotion;
import androidx.transition.Styleable;
import org.xmlpull.v1.XmlPullParser;

public class ArcMotion
extends PathMotion {
    private static final float DEFAULT_MAX_ANGLE_DEGREES = 70.0f;
    private static final float DEFAULT_MAX_TANGENT = (float)Math.tan(Math.toRadians(35.0));
    private static final float DEFAULT_MIN_ANGLE_DEGREES = 0.0f;
    private float mMaximumAngle = 70.0f;
    private float mMaximumTangent = DEFAULT_MAX_TANGENT;
    private float mMinimumHorizontalAngle = 0.0f;
    private float mMinimumHorizontalTangent = 0.0f;
    private float mMinimumVerticalAngle = 0.0f;
    private float mMinimumVerticalTangent = 0.0f;

    public ArcMotion() {
    }

    public ArcMotion(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        context = context.obtainStyledAttributes(attributeSet, Styleable.ARC_MOTION);
        attributeSet = (XmlPullParser)attributeSet;
        this.setMinimumVerticalAngle(TypedArrayUtils.getNamedFloat((TypedArray)context, (XmlPullParser)attributeSet, "minimumVerticalAngle", 1, 0.0f));
        this.setMinimumHorizontalAngle(TypedArrayUtils.getNamedFloat((TypedArray)context, (XmlPullParser)attributeSet, "minimumHorizontalAngle", 0, 0.0f));
        this.setMaximumAngle(TypedArrayUtils.getNamedFloat((TypedArray)context, (XmlPullParser)attributeSet, "maximumAngle", 2, 70.0f));
        context.recycle();
    }

    private static float toTangent(float f) {
        if (!(f < 0.0f) && !(f > 90.0f)) {
            return (float)Math.tan(Math.toRadians(f / 2.0f));
        }
        throw new IllegalArgumentException("Arc must be between 0 and 90 degrees");
    }

    public float getMaximumAngle() {
        return this.mMaximumAngle;
    }

    public float getMinimumHorizontalAngle() {
        return this.mMinimumHorizontalAngle;
    }

    public float getMinimumVerticalAngle() {
        return this.mMinimumVerticalAngle;
    }

    @Override
    public Path getPath(float f, float f2, float f3, float f4) {
        Path path = new Path();
        path.moveTo(f, f2);
        float f5 = f3 - f;
        float f6 = f4 - f2;
        float f7 = f5 * f5 + f6 * f6;
        float f8 = (f + f3) / 2.0f;
        float f9 = (f2 + f4) / 2.0f;
        float f10 = f7 * 0.25f;
        boolean bl = f2 > f4;
        if (Math.abs(f5) < Math.abs(f6)) {
            f6 = Math.abs(f7 / (f6 * 2.0f));
            if (bl) {
                f7 = f4 + f6;
                f6 = f3;
            } else {
                f7 = f2 + f6;
                f6 = f;
            }
            f5 = this.mMinimumVerticalTangent;
            f5 = f10 * f5 * f5;
        } else {
            f6 = f7 / (f5 * 2.0f);
            if (bl) {
                f6 = f + f6;
                f7 = f2;
            } else {
                f6 = f3 - f6;
                f7 = f4;
            }
            f5 = this.mMinimumHorizontalTangent;
            f5 = f10 * f5 * f5;
        }
        float f11 = f8 - f6;
        float f12 = f9 - f7;
        f11 = f11 * f11 + f12 * f12;
        f12 = this.mMaximumTangent;
        f10 = f10 * f12 * f12;
        if (!(f11 < f5)) {
            f5 = f11 > f10 ? f10 : 0.0f;
        }
        if (f5 != 0.0f) {
            f5 = (float)Math.sqrt(f5 / f11);
            f7 = f9 + (f7 - f9) * f5;
            f6 = f8 + (f6 - f8) * f5;
        }
        path.cubicTo((f + f6) / 2.0f, (f2 + f7) / 2.0f, (f6 + f3) / 2.0f, (f7 + f4) / 2.0f, f3, f4);
        return path;
    }

    public void setMaximumAngle(float f) {
        this.mMaximumAngle = f;
        this.mMaximumTangent = ArcMotion.toTangent(f);
    }

    public void setMinimumHorizontalAngle(float f) {
        this.mMinimumHorizontalAngle = f;
        this.mMinimumHorizontalTangent = ArcMotion.toTangent(f);
    }

    public void setMinimumVerticalAngle(float f) {
        this.mMinimumVerticalAngle = f;
        this.mMinimumVerticalTangent = ArcMotion.toTangent(f);
    }
}

