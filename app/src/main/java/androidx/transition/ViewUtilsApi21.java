/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Matrix
 *  android.util.Log
 *  android.view.View
 */
package androidx.transition;

import android.graphics.Matrix;
import android.util.Log;
import android.view.View;
import androidx.transition.ViewUtilsApi19;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ViewUtilsApi21
extends ViewUtilsApi19 {
    private static final String TAG = "ViewUtilsApi21";
    private static Method sSetAnimationMatrixMethod;
    private static boolean sSetAnimationMatrixMethodFetched;
    private static Method sTransformMatrixToGlobalMethod;
    private static boolean sTransformMatrixToGlobalMethodFetched;
    private static Method sTransformMatrixToLocalMethod;
    private static boolean sTransformMatrixToLocalMethodFetched;

    ViewUtilsApi21() {
    }

    private void fetchSetAnimationMatrix() {
        if (!sSetAnimationMatrixMethodFetched) {
            try {
                Method method;
                sSetAnimationMatrixMethod = method = View.class.getDeclaredMethod("setAnimationMatrix", Matrix.class);
                method.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Log.i((String)TAG, (String)"Failed to retrieve setAnimationMatrix method", (Throwable)noSuchMethodException);
            }
            sSetAnimationMatrixMethodFetched = true;
        }
    }

    private void fetchTransformMatrixToGlobalMethod() {
        if (!sTransformMatrixToGlobalMethodFetched) {
            try {
                Method method;
                sTransformMatrixToGlobalMethod = method = View.class.getDeclaredMethod("transformMatrixToGlobal", Matrix.class);
                method.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Log.i((String)TAG, (String)"Failed to retrieve transformMatrixToGlobal method", (Throwable)noSuchMethodException);
            }
            sTransformMatrixToGlobalMethodFetched = true;
        }
    }

    private void fetchTransformMatrixToLocalMethod() {
        if (!sTransformMatrixToLocalMethodFetched) {
            try {
                Method method;
                sTransformMatrixToLocalMethod = method = View.class.getDeclaredMethod("transformMatrixToLocal", Matrix.class);
                method.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Log.i((String)TAG, (String)"Failed to retrieve transformMatrixToLocal method", (Throwable)noSuchMethodException);
            }
            sTransformMatrixToLocalMethodFetched = true;
        }
    }

    @Override
    public void setAnimationMatrix(View view, Matrix matrix) {
        this.fetchSetAnimationMatrix();
        Method method = sSetAnimationMatrixMethod;
        if (method != null) {
            try {
                method.invoke((Object)view, matrix);
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new RuntimeException(illegalAccessException.getCause());
            }
            catch (InvocationTargetException invocationTargetException) {
                // empty catch block
            }
        }
    }

    @Override
    public void transformMatrixToGlobal(View view, Matrix matrix) {
        this.fetchTransformMatrixToGlobalMethod();
        Method method = sTransformMatrixToGlobalMethod;
        if (method != null) {
            try {
                method.invoke((Object)view, matrix);
            }
            catch (InvocationTargetException invocationTargetException) {
                throw new RuntimeException(invocationTargetException.getCause());
            }
            catch (IllegalAccessException illegalAccessException) {
                // empty catch block
            }
        }
    }

    @Override
    public void transformMatrixToLocal(View view, Matrix matrix) {
        this.fetchTransformMatrixToLocalMethod();
        Method method = sTransformMatrixToLocalMethod;
        if (method != null) {
            try {
                method.invoke((Object)view, matrix);
            }
            catch (InvocationTargetException invocationTargetException) {
                throw new RuntimeException(invocationTargetException.getCause());
            }
            catch (IllegalAccessException illegalAccessException) {
                // empty catch block
            }
        }
    }
}

