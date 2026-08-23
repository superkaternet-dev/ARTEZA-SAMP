/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  android.view.ViewGroup
 */
package androidx.transition;

import android.util.Log;
import android.view.ViewGroup;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ViewGroupUtilsApi18 {
    private static final String TAG = "ViewUtilsApi18";
    private static Method sSuppressLayoutMethod;
    private static boolean sSuppressLayoutMethodFetched;

    private ViewGroupUtilsApi18() {
    }

    private static void fetchSuppressLayoutMethod() {
        if (!sSuppressLayoutMethodFetched) {
            try {
                Method method;
                sSuppressLayoutMethod = method = ViewGroup.class.getDeclaredMethod("suppressLayout", Boolean.TYPE);
                method.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Log.i((String)TAG, (String)"Failed to retrieve suppressLayout method", (Throwable)noSuchMethodException);
            }
            sSuppressLayoutMethodFetched = true;
        }
    }

    static void suppressLayout(ViewGroup viewGroup, boolean bl) {
        ViewGroupUtilsApi18.fetchSuppressLayoutMethod();
        Method method = sSuppressLayoutMethod;
        if (method != null) {
            try {
                method.invoke((Object)viewGroup, bl);
            }
            catch (InvocationTargetException invocationTargetException) {
                Log.i((String)TAG, (String)"Error invoking suppressLayout method", (Throwable)invocationTargetException);
            }
            catch (IllegalAccessException illegalAccessException) {
                Log.i((String)TAG, (String)"Failed to invoke suppressLayout method", (Throwable)illegalAccessException);
            }
        }
    }
}

