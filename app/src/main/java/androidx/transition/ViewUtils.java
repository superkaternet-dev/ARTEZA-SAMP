/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Matrix
 *  android.graphics.Rect
 *  android.os.Build$VERSION
 *  android.util.Log
 *  android.util.Property
 *  android.view.View
 */
package androidx.transition;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.util.Property;
import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.transition.ViewOverlayApi14;
import androidx.transition.ViewOverlayApi18;
import androidx.transition.ViewOverlayImpl;
import androidx.transition.ViewUtilsApi19;
import androidx.transition.ViewUtilsApi21;
import androidx.transition.ViewUtilsApi22;
import androidx.transition.ViewUtilsBase;
import androidx.transition.WindowIdApi14;
import androidx.transition.WindowIdApi18;
import androidx.transition.WindowIdImpl;
import java.lang.reflect.Field;

class ViewUtils {
    static final Property<View, Rect> CLIP_BOUNDS;
    private static final ViewUtilsBase IMPL;
    private static final String TAG = "ViewUtils";
    static final Property<View, Float> TRANSITION_ALPHA;
    private static final int VISIBILITY_MASK = 12;
    private static Field sViewFlagsField;
    private static boolean sViewFlagsFieldFetched;

    static {
        IMPL = Build.VERSION.SDK_INT >= 22 ? new ViewUtilsApi22() : (Build.VERSION.SDK_INT >= 21 ? new ViewUtilsApi21() : (Build.VERSION.SDK_INT >= 19 ? new ViewUtilsApi19() : new ViewUtilsBase()));
        TRANSITION_ALPHA = new Property<View, Float>(Float.class, "translationAlpha"){

            public Float get(View view) {
                return Float.valueOf(ViewUtils.getTransitionAlpha(view));
            }

            public void set(View view, Float f) {
                ViewUtils.setTransitionAlpha(view, f.floatValue());
            }
        };
        CLIP_BOUNDS = new Property<View, Rect>(Rect.class, "clipBounds"){

            public Rect get(View view) {
                return ViewCompat.getClipBounds(view);
            }

            public void set(View view, Rect rect) {
                ViewCompat.setClipBounds(view, rect);
            }
        };
    }

    private ViewUtils() {
    }

    static void clearNonTransitionAlpha(View view) {
        IMPL.clearNonTransitionAlpha(view);
    }

    private static void fetchViewFlagsField() {
        if (!sViewFlagsFieldFetched) {
            try {
                Field field;
                sViewFlagsField = field = View.class.getDeclaredField("mViewFlags");
                field.setAccessible(true);
            }
            catch (NoSuchFieldException noSuchFieldException) {
                Log.i((String)TAG, (String)"fetchViewFlagsField: ");
            }
            sViewFlagsFieldFetched = true;
        }
    }

    static ViewOverlayImpl getOverlay(View view) {
        if (Build.VERSION.SDK_INT >= 18) {
            return new ViewOverlayApi18(view);
        }
        return ViewOverlayApi14.createFrom(view);
    }

    static float getTransitionAlpha(View view) {
        return IMPL.getTransitionAlpha(view);
    }

    static WindowIdImpl getWindowId(View view) {
        if (Build.VERSION.SDK_INT >= 18) {
            return new WindowIdApi18(view);
        }
        return new WindowIdApi14(view.getWindowToken());
    }

    static void saveNonTransitionAlpha(View view) {
        IMPL.saveNonTransitionAlpha(view);
    }

    static void setAnimationMatrix(View view, Matrix matrix) {
        IMPL.setAnimationMatrix(view, matrix);
    }

    static void setLeftTopRightBottom(View view, int n, int n2, int n3, int n4) {
        IMPL.setLeftTopRightBottom(view, n, n2, n3, n4);
    }

    static void setTransitionAlpha(View view, float f) {
        IMPL.setTransitionAlpha(view, f);
    }

    static void setTransitionVisibility(View view, int n) {
        ViewUtils.fetchViewFlagsField();
        Field field = sViewFlagsField;
        if (field != null) {
            try {
                int n2 = field.getInt(view);
                sViewFlagsField.setInt(view, n2 & 0xFFFFFFF3 | n);
            }
            catch (IllegalAccessException illegalAccessException) {
                // empty catch block
            }
        }
    }

    static void transformMatrixToGlobal(View view, Matrix matrix) {
        IMPL.transformMatrixToGlobal(view, matrix);
    }

    static void transformMatrixToLocal(View view, Matrix matrix) {
        IMPL.transformMatrixToLocal(view, matrix);
    }
}

