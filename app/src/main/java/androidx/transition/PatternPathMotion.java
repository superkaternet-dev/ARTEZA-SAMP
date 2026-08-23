/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.Matrix
 *  android.graphics.Path
 *  android.graphics.PathMeasure
 *  android.util.AttributeSet
 *  org.xmlpull.v1.XmlPullParser
 */
package androidx.transition;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.graphics.PathParser;
import androidx.transition.PathMotion;
import androidx.transition.Styleable;
import org.xmlpull.v1.XmlPullParser;

public class PatternPathMotion
extends PathMotion {
    private Path mOriginalPatternPath;
    private final Path mPatternPath;
    private final Matrix mTempMatrix;

    public PatternPathMotion() {
        Path path;
        this.mPatternPath = path = new Path();
        this.mTempMatrix = new Matrix();
        path.lineTo(1.0f, 0.0f);
        this.mOriginalPatternPath = path;
    }

    public PatternPathMotion(Context context, AttributeSet object) {
        this.mPatternPath = new Path();
        this.mTempMatrix = new Matrix();
        context = context.obtainStyledAttributes(object, Styleable.PATTERN_PATH_MOTION);
        try {
            object = TypedArrayUtils.getNamedString((TypedArray)context, (XmlPullParser)object, "patternPathData", 0);
            if (object != null) {
                this.setPatternPath(PathParser.createPathFromPathData((String)object));
                return;
            }
            super("pathData must be supplied for patternPathMotion");
            throw object;
        }
        finally {
            context.recycle();
        }
    }

    public PatternPathMotion(Path path) {
        this.mPatternPath = new Path();
        this.mTempMatrix = new Matrix();
        this.setPatternPath(path);
    }

    private static float distance(float f, float f2) {
        return (float)Math.sqrt(f * f + f2 * f2);
    }

    @Override
    public Path getPath(float f, float f2, float f3, float f4) {
        float f5 = f4 - f2;
        f4 = PatternPathMotion.distance(f3 -= f, f5);
        double d = Math.atan2(f5, f3);
        this.mTempMatrix.setScale(f4, f4);
        this.mTempMatrix.postRotate((float)Math.toDegrees(d));
        this.mTempMatrix.postTranslate(f, f2);
        Path path = new Path();
        this.mPatternPath.transform(this.mTempMatrix, path);
        return path;
    }

    public Path getPatternPath() {
        return this.mOriginalPatternPath;
    }

    public void setPatternPath(Path path) {
        PathMeasure pathMeasure = new PathMeasure(path, false);
        float f = pathMeasure.getLength();
        float[] fArray = new float[2];
        pathMeasure.getPosTan(f, fArray, null);
        float f2 = fArray[0];
        float f3 = fArray[1];
        pathMeasure.getPosTan(0.0f, fArray, null);
        float f4 = fArray[0];
        f = fArray[1];
        if (f4 == f2 && f == f3) {
            throw new IllegalArgumentException("pattern must not end at the starting point");
        }
        this.mTempMatrix.setTranslate(-f4, -f);
        f3 -= f;
        f = 1.0f / PatternPathMotion.distance(f2 -= f4, f3);
        this.mTempMatrix.postScale(f, f);
        double d = Math.atan2(f3, f2);
        this.mTempMatrix.postRotate((float)Math.toDegrees(-d));
        path.transform(this.mTempMatrix, this.mPatternPath);
        this.mOriginalPatternPath = path;
    }
}

