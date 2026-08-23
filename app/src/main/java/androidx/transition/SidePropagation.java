/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  android.view.View
 *  android.view.ViewGroup
 */
package androidx.transition;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;
import androidx.transition.Transition;
import androidx.transition.TransitionValues;
import androidx.transition.VisibilityPropagation;

public class SidePropagation
extends VisibilityPropagation {
    private float mPropagationSpeed = 3.0f;
    private int mSide = 80;

    private int distance(View view, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        int n9 = this.mSide;
        int n10 = 5;
        int n11 = 0;
        int n12 = 0;
        if (n9 == 0x800003) {
            n11 = n12;
            if (ViewCompat.getLayoutDirection(view) == 1) {
                n11 = 1;
            }
            if (n11 == 0) {
                n10 = 3;
            }
        } else if (n9 == 0x800005) {
            if (ViewCompat.getLayoutDirection(view) == 1) {
                n11 = 1;
            }
            if (n11 != 0) {
                n10 = 3;
            }
        } else {
            n10 = this.mSide;
        }
        n11 = 0;
        switch (n10) {
            default: {
                n = n11;
                break;
            }
            case 80: {
                n = n2 - n6 + Math.abs(n3 - n);
                break;
            }
            case 48: {
                n = n8 - n2 + Math.abs(n3 - n);
                break;
            }
            case 5: {
                n = n - n5 + Math.abs(n4 - n2);
                break;
            }
            case 3: {
                n = n7 - n + Math.abs(n4 - n2);
            }
        }
        return n;
    }

    private int getMaxDistance(ViewGroup viewGroup) {
        switch (this.mSide) {
            default: {
                return viewGroup.getHeight();
            }
            case 3: 
            case 5: 
            case 0x800003: 
            case 0x800005: 
        }
        return viewGroup.getWidth();
    }

    @Override
    public long getStartDelay(ViewGroup viewGroup, Transition transition, TransitionValues object, TransitionValues transitionValues) {
        long l;
        Object object2;
        Object object3;
        int n;
        if (object == null && transitionValues == null) {
            return 0L;
        }
        Rect rect = transition.getEpicenter();
        if (transitionValues != null && this.getViewVisibility((TransitionValues)object) != 0) {
            n = 1;
            object = transitionValues;
        } else {
            n = -1;
        }
        int n2 = this.getViewX((TransitionValues)object);
        int n3 = this.getViewY((TransitionValues)object);
        object = new int[2];
        viewGroup.getLocationOnScreen((int[])object);
        reference var14_9 = object[0] + Math.round(viewGroup.getTranslationX());
        reference var13_10 = object[1] + Math.round(viewGroup.getTranslationY());
        reference var9_11 = var14_9 + viewGroup.getWidth();
        reference var11_12 = var13_10 + viewGroup.getHeight();
        if (rect != null) {
            object3 = rect.centerX();
            object2 = rect.centerY();
        } else {
            object3 = (var14_9 + var9_11) / 2;
            object2 = (var13_10 + var11_12) / 2;
        }
        float f = (float)this.distance((View)viewGroup, n2, n3, (int)object3, (int)object2, (int)var14_9, (int)var13_10, (int)var9_11, (int)var11_12) / (float)this.getMaxDistance(viewGroup);
        long l2 = l = transition.getDuration();
        if (l < 0L) {
            l2 = 300L;
        }
        return Math.round((float)((long)n * l2) / this.mPropagationSpeed * f);
    }

    public void setPropagationSpeed(float f) {
        if (f != 0.0f) {
            this.mPropagationSpeed = f;
            return;
        }
        throw new IllegalArgumentException("propagationSpeed may not be 0");
    }

    public void setSide(int n) {
        this.mSide = n;
    }
}

