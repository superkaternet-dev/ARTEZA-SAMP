/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.ObjectAnimator
 *  android.graphics.Path
 *  android.graphics.PointF
 *  android.os.Build$VERSION
 *  android.util.Property
 */
package androidx.transition;

import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Build;
import android.util.Property;
import androidx.transition.PathProperty;

class ObjectAnimatorUtils {
    private ObjectAnimatorUtils() {
    }

    static <T> ObjectAnimator ofPointF(T t, Property<T, PointF> property, Path path) {
        if (Build.VERSION.SDK_INT >= 21) {
            return ObjectAnimator.ofObject(t, property, null, (Path)path);
        }
        return ObjectAnimator.ofFloat(t, new PathProperty<T>(property, path), (float[])new float[]{0.0f, 1.0f});
    }
}

