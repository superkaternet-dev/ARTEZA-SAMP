/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Path
 *  android.graphics.PointF
 */
package androidx.core.graphics;

import android.graphics.Path;
import android.graphics.PointF;
import androidx.core.graphics.PathSegment;
import java.util.ArrayList;
import java.util.Collection;

public final class PathUtils {
    private PathUtils() {
    }

    public static Collection<PathSegment> flatten(Path path) {
        return PathUtils.flatten(path, 0.5f);
    }

    public static Collection<PathSegment> flatten(Path object, float f) {
        float[] fArray = object.approximate(f);
        int n = fArray.length / 3;
        object = new ArrayList(n);
        for (int i = 1; i < n; ++i) {
            int n2 = i * 3;
            int n3 = (i - 1) * 3;
            float f2 = fArray[n2];
            float f3 = fArray[n2 + 1];
            float f4 = fArray[n2 + 2];
            float f5 = fArray[n3];
            float f6 = fArray[n3 + 1];
            f = fArray[n3 + 2];
            if (f2 == f5 || f3 == f6 && f4 == f) continue;
            object.add(new PathSegment(new PointF(f6, f), f5, new PointF(f3, f4), f2));
        }
        return object;
    }
}

