/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  android.view.View
 */
package androidx.transition;

import android.util.Log;
import android.view.View;
import androidx.transition.ViewUtilsBase;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ViewUtilsApi19
extends ViewUtilsBase {
    private static final String TAG = "ViewUtilsApi19";
    private static Method sGetTransitionAlphaMethod;
    private static boolean sGetTransitionAlphaMethodFetched;
    private static Method sSetTransitionAlphaMethod;
    private static boolean sSetTransitionAlphaMethodFetched;

    ViewUtilsApi19() {
    }

    private void fetchGetTransitionAlphaMethod() {
        if (!sGetTransitionAlphaMethodFetched) {
            try {
                Method method;
                sGetTransitionAlphaMethod = method = View.class.getDeclaredMethod("getTransitionAlpha", new Class[0]);
                method.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Log.i((String)TAG, (String)"Failed to retrieve getTransitionAlpha method", (Throwable)noSuchMethodException);
            }
            sGetTransitionAlphaMethodFetched = true;
        }
    }

    private void fetchSetTransitionAlphaMethod() {
        if (!sSetTransitionAlphaMethodFetched) {
            try {
                Method method;
                sSetTransitionAlphaMethod = method = View.class.getDeclaredMethod("setTransitionAlpha", Float.TYPE);
                method.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Log.i((String)TAG, (String)"Failed to retrieve setTransitionAlpha method", (Throwable)noSuchMethodException);
            }
            sSetTransitionAlphaMethodFetched = true;
        }
    }

    @Override
    public void clearNonTransitionAlpha(View view) {
    }

    @Override
    public float getTransitionAlpha(View view) {
        this.fetchGetTransitionAlphaMethod();
        Method method = sGetTransitionAlphaMethod;
        if (method != null) {
            try {
                float f = ((Float)method.invoke((Object)view, new Object[0])).floatValue();
                return f;
            }
            catch (InvocationTargetException invocationTargetException) {
                throw new RuntimeException(invocationTargetException.getCause());
            }
            catch (IllegalAccessException illegalAccessException) {
                // empty catch block
            }
        }
        return super.getTransitionAlpha(view);
    }

    @Override
    public void saveNonTransitionAlpha(View view) {
    }

    @Override
    public void setTransitionAlpha(View view, float f) {
        this.fetchSetTransitionAlphaMethod();
        Method method = sSetTransitionAlphaMethod;
        if (method != null) {
            try {
                method.invoke((Object)view, Float.valueOf(f));
            }
            catch (InvocationTargetException invocationTargetException) {
                throw new RuntimeException(invocationTargetException.getCause());
            }
            catch (IllegalAccessException illegalAccessException) {}
        } else {
            view.setAlpha(f);
        }
    }
}

