/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 */
package androidx.transition;

import android.view.View;
import androidx.transition.TransitionPropagation;
import androidx.transition.TransitionValues;

public abstract class VisibilityPropagation
extends TransitionPropagation {
    private static final String PROPNAME_VIEW_CENTER = "android:visibilityPropagation:center";
    private static final String PROPNAME_VISIBILITY = "android:visibilityPropagation:visibility";
    private static final String[] VISIBILITY_PROPAGATION_VALUES = new String[]{"android:visibilityPropagation:visibility", "android:visibilityPropagation:center"};

    private static int getViewCoordinate(TransitionValues object, int n) {
        if (object == null) {
            return -1;
        }
        object = (int[])((TransitionValues)object).values.get(PROPNAME_VIEW_CENTER);
        if (object == null) {
            return -1;
        }
        return (int)object[n];
    }

    @Override
    public void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        Integer n = (Integer)transitionValues.values.get("android:visibility:visibility");
        Object object = n;
        if (n == null) {
            object = view.getVisibility();
        }
        transitionValues.values.put(PROPNAME_VISIBILITY, object);
        object = new int[2];
        view.getLocationOnScreen((int[])object);
        object[0] = object[0] + Math.round(view.getTranslationX());
        object[0] = object[0] + view.getWidth() / 2;
        object[1] = object[1] + Math.round(view.getTranslationY());
        object[1] = object[1] + view.getHeight() / 2;
        transitionValues.values.put(PROPNAME_VIEW_CENTER, object);
    }

    @Override
    public String[] getPropagationProperties() {
        return VISIBILITY_PROPAGATION_VALUES;
    }

    public int getViewVisibility(TransitionValues object) {
        if (object == null) {
            return 8;
        }
        object = (Integer)((TransitionValues)object).values.get(PROPNAME_VISIBILITY);
        if (object == null) {
            return 8;
        }
        return (Integer)object;
    }

    public int getViewX(TransitionValues transitionValues) {
        return VisibilityPropagation.getViewCoordinate(transitionValues, 0);
    }

    public int getViewY(TransitionValues transitionValues) {
        return VisibilityPropagation.getViewCoordinate(transitionValues, 1);
    }
}

