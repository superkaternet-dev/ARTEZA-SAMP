/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.graphics.Path
 *  android.graphics.PathMeasure
 *  android.util.AttributeSet
 *  android.view.InflateException
 *  android.view.animation.Interpolator
 *  org.xmlpull.v1.XmlPullParser
 */
package androidx.vectordrawable.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.animation.Interpolator;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.graphics.PathParser;
import androidx.vectordrawable.graphics.drawable.AndroidResources;
import org.xmlpull.v1.XmlPullParser;

public class PathInterpolatorCompat
implements Interpolator {
    public static final double EPSILON = 1.0E-5;
    public static final int MAX_NUM_POINTS = 3000;
    private static final float PRECISION = 0.002f;
    private float[] mX;
    private float[] mY;

    public PathInterpolatorCompat(Context context, AttributeSet attributeSet, XmlPullParser xmlPullParser) {
        this(context.getResources(), context.getTheme(), attributeSet, xmlPullParser);
    }

    public PathInterpolatorCompat(Resources resources, Resources.Theme theme, AttributeSet attributeSet, XmlPullParser xmlPullParser) {
        resources = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_PATH_INTERPOLATOR);
        this.parseInterpolatorFromTypeArray((TypedArray)resources, xmlPullParser);
        resources.recycle();
    }

    private void initCubic(float f, float f2, float f3, float f4) {
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.cubicTo(f, f2, f3, f4, 1.0f, 1.0f);
        this.initPath(path);
    }

    private void initPath(Path object) {
        float f = (object = new PathMeasure((Path)object, false)).getLength();
        int n = Math.min(3000, (int)(f / 0.002f) + 1);
        if (n > 0) {
            int n2;
            this.mX = new float[n];
            this.mY = new float[n];
            float[] fArray = new float[2];
            for (n2 = 0; n2 < n; ++n2) {
                object.getPosTan((float)n2 * f / (float)(n - 1), fArray, null);
                this.mX[n2] = fArray[0];
                this.mY[n2] = fArray[1];
            }
            if (!((double)Math.abs(this.mX[0]) > 1.0E-5 || (double)Math.abs(this.mY[0]) > 1.0E-5 || (double)Math.abs(this.mX[n - 1] - 1.0f) > 1.0E-5 || (double)Math.abs(this.mY[n - 1] - 1.0f) > 1.0E-5)) {
                f = 0.0f;
                n2 = 0;
                int n3 = 0;
                while (n3 < n) {
                    fArray = this.mX;
                    float f2 = fArray[n2];
                    if (!(f2 < f)) {
                        fArray[n3] = f2;
                        f = f2;
                        ++n3;
                        ++n2;
                        continue;
                    }
                    object = new StringBuilder();
                    ((StringBuilder)object).append("The Path cannot loop back on itself, x :");
                    ((StringBuilder)object).append(f2);
                    throw new IllegalArgumentException(((StringBuilder)object).toString());
                }
                if (!object.nextContour()) {
                    return;
                }
                throw new IllegalArgumentException("The Path should be continuous, can't have 2+ contours");
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("The Path must start at (0,0) and end at (1,1) start: ");
            ((StringBuilder)object).append(this.mX[0]);
            ((StringBuilder)object).append(",");
            ((StringBuilder)object).append(this.mY[0]);
            ((StringBuilder)object).append(" end:");
            ((StringBuilder)object).append(this.mX[n - 1]);
            ((StringBuilder)object).append(",");
            ((StringBuilder)object).append(this.mY[n - 1]);
            throw new IllegalArgumentException(((StringBuilder)object).toString());
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("The Path has a invalid length ");
        ((StringBuilder)object).append(f);
        object = new IllegalArgumentException(((StringBuilder)object).toString());
        throw object;
    }

    private void initQuad(float f, float f2) {
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.quadTo(f, f2, 1.0f, 1.0f);
        this.initPath(path);
    }

    private void parseInterpolatorFromTypeArray(TypedArray object, XmlPullParser object2) {
        block8: {
            block9: {
                block10: {
                    block7: {
                        block5: {
                            block6: {
                                if (!TypedArrayUtils.hasAttribute((XmlPullParser)object2, "pathData")) break block5;
                                if ((object2 = PathParser.createPathFromPathData((String)(object = TypedArrayUtils.getNamedString(object, (XmlPullParser)object2, "pathData", 4)))) == null) break block6;
                                this.initPath((Path)object2);
                                break block7;
                            }
                            object2 = new StringBuilder();
                            ((StringBuilder)object2).append("The path is null, which is created from ");
                            ((StringBuilder)object2).append((String)object);
                            throw new InflateException(((StringBuilder)object2).toString());
                        }
                        if (!TypedArrayUtils.hasAttribute((XmlPullParser)object2, "controlX1")) break block8;
                        if (!TypedArrayUtils.hasAttribute((XmlPullParser)object2, "controlY1")) break block9;
                        float f = TypedArrayUtils.getNamedFloat(object, (XmlPullParser)object2, "controlX1", 0, 0.0f);
                        float f2 = TypedArrayUtils.getNamedFloat(object, (XmlPullParser)object2, "controlY1", 1, 0.0f);
                        boolean bl = TypedArrayUtils.hasAttribute((XmlPullParser)object2, "controlX2");
                        if (bl != TypedArrayUtils.hasAttribute((XmlPullParser)object2, "controlY2")) break block10;
                        if (!bl) {
                            this.initQuad(f, f2);
                        } else {
                            this.initCubic(f, f2, TypedArrayUtils.getNamedFloat(object, (XmlPullParser)object2, "controlX2", 2, 0.0f), TypedArrayUtils.getNamedFloat(object, (XmlPullParser)object2, "controlY2", 3, 0.0f));
                        }
                    }
                    return;
                }
                throw new InflateException("pathInterpolator requires both controlX2 and controlY2 for cubic Beziers.");
            }
            throw new InflateException("pathInterpolator requires the controlY1 attribute");
        }
        throw new InflateException("pathInterpolator requires the controlX1 attribute");
    }

    public float getInterpolation(float f) {
        if (f <= 0.0f) {
            return 0.0f;
        }
        if (f >= 1.0f) {
            return 1.0f;
        }
        int n = 0;
        int n2 = this.mX.length - 1;
        while (n2 - n > 1) {
            int n3 = (n + n2) / 2;
            if (f < this.mX[n3]) {
                n2 = n3;
                continue;
            }
            n = n3;
        }
        float[] fArray = this.mX;
        float f2 = fArray[n2] - fArray[n];
        if (f2 == 0.0f) {
            return this.mY[n];
        }
        f2 = (f - fArray[n]) / f2;
        fArray = this.mY;
        f = fArray[n];
        return (fArray[n2] - f) * f2 + f;
    }
}

