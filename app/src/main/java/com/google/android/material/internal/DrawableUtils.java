/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable$ConstantState
 *  android.graphics.drawable.DrawableContainer
 *  android.graphics.drawable.DrawableContainer$DrawableContainerState
 *  android.util.Log
 */
package com.google.android.material.internal;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.util.Log;
import java.lang.reflect.Method;

public class DrawableUtils {
    private static final String LOG_TAG = "DrawableUtils";
    private static Method setConstantStateMethod;
    private static boolean setConstantStateMethodFetched;

    private DrawableUtils() {
    }

    public static boolean setContainerConstantState(DrawableContainer drawableContainer, Drawable.ConstantState constantState) {
        return DrawableUtils.setContainerConstantStateV9(drawableContainer, constantState);
    }

    private static boolean setContainerConstantStateV9(DrawableContainer drawableContainer, Drawable.ConstantState constantState) {
        Method method;
        if (!setConstantStateMethodFetched) {
            try {
                setConstantStateMethod = method = DrawableContainer.class.getDeclaredMethod("setConstantState", DrawableContainer.DrawableContainerState.class);
                method.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Log.e((String)LOG_TAG, (String)"Could not fetch setConstantState(). Oh well.");
            }
            setConstantStateMethodFetched = true;
        }
        if ((method = setConstantStateMethod) != null) {
            try {
                method.invoke((Object)drawableContainer, constantState);
                return true;
            }
            catch (Exception exception) {
                Log.e((String)LOG_TAG, (String)"Could not invoke setConstantState(). Oh well.");
            }
        }
        return false;
    }
}

