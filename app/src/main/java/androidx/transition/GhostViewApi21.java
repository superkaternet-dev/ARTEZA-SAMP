/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Matrix
 *  android.util.Log
 *  android.view.View
 *  android.view.ViewGroup
 */
package androidx.transition;

import android.graphics.Matrix;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.transition.GhostViewImpl;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class GhostViewApi21
implements GhostViewImpl {
    private static final String TAG = "GhostViewApi21";
    private static Method sAddGhostMethod;
    private static boolean sAddGhostMethodFetched;
    private static Class<?> sGhostViewClass;
    private static boolean sGhostViewClassFetched;
    private static Method sRemoveGhostMethod;
    private static boolean sRemoveGhostMethodFetched;
    private final View mGhostView;

    private GhostViewApi21(View view) {
        this.mGhostView = view;
    }

    static GhostViewImpl addGhost(View object, ViewGroup viewGroup, Matrix matrix) {
        GhostViewApi21.fetchAddGhostMethod();
        Method method = sAddGhostMethod;
        if (method != null) {
            try {
                object = new GhostViewApi21((View)method.invoke(null, object, viewGroup, matrix));
                return object;
            }
            catch (InvocationTargetException invocationTargetException) {
                throw new RuntimeException(invocationTargetException.getCause());
            }
            catch (IllegalAccessException illegalAccessException) {
                // empty catch block
            }
        }
        return null;
    }

    private static void fetchAddGhostMethod() {
        if (!sAddGhostMethodFetched) {
            try {
                Method method;
                GhostViewApi21.fetchGhostViewClass();
                sAddGhostMethod = method = sGhostViewClass.getDeclaredMethod("addGhost", View.class, ViewGroup.class, Matrix.class);
                method.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Log.i((String)TAG, (String)"Failed to retrieve addGhost method", (Throwable)noSuchMethodException);
            }
            sAddGhostMethodFetched = true;
        }
    }

    private static void fetchGhostViewClass() {
        if (!sGhostViewClassFetched) {
            try {
                sGhostViewClass = Class.forName("android.view.GhostView");
            }
            catch (ClassNotFoundException classNotFoundException) {
                Log.i((String)TAG, (String)"Failed to retrieve GhostView class", (Throwable)classNotFoundException);
            }
            sGhostViewClassFetched = true;
        }
    }

    private static void fetchRemoveGhostMethod() {
        if (!sRemoveGhostMethodFetched) {
            try {
                Method method;
                GhostViewApi21.fetchGhostViewClass();
                sRemoveGhostMethod = method = sGhostViewClass.getDeclaredMethod("removeGhost", View.class);
                method.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Log.i((String)TAG, (String)"Failed to retrieve removeGhost method", (Throwable)noSuchMethodException);
            }
            sRemoveGhostMethodFetched = true;
        }
    }

    static void removeGhost(View view) {
        GhostViewApi21.fetchRemoveGhostMethod();
        Method method = sRemoveGhostMethod;
        if (method != null) {
            try {
                method.invoke(null, view);
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
    public void reserveEndViewTransition(ViewGroup viewGroup, View view) {
    }

    @Override
    public void setVisibility(int n) {
        this.mGhostView.setVisibility(n);
    }
}

