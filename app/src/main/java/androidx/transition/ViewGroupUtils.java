/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 *  android.view.ViewGroup
 */
package androidx.transition;

import android.os.Build;
import android.view.ViewGroup;
import androidx.transition.ViewGroupOverlayApi14;
import androidx.transition.ViewGroupOverlayApi18;
import androidx.transition.ViewGroupOverlayImpl;
import androidx.transition.ViewGroupUtilsApi14;
import androidx.transition.ViewGroupUtilsApi18;

class ViewGroupUtils {
    private ViewGroupUtils() {
    }

    static ViewGroupOverlayImpl getOverlay(ViewGroup viewGroup) {
        if (Build.VERSION.SDK_INT >= 18) {
            return new ViewGroupOverlayApi18(viewGroup);
        }
        return ViewGroupOverlayApi14.createFrom(viewGroup);
    }

    static void suppressLayout(ViewGroup viewGroup, boolean bl) {
        if (Build.VERSION.SDK_INT >= 18) {
            ViewGroupUtilsApi18.suppressLayout(viewGroup, bl);
        } else {
            ViewGroupUtilsApi14.suppressLayout(viewGroup, bl);
        }
    }
}

