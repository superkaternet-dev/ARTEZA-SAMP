/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  android.os.Build$VERSION
 *  android.view.DisplayCutout
 */
package androidx.core.view;

import android.graphics.Rect;
import android.os.Build;
import android.view.DisplayCutout;
import java.util.List;

public final class DisplayCutoutCompat {
    private final Object mDisplayCutout;

    public DisplayCutoutCompat(Rect object, List<Rect> list) {
        object = Build.VERSION.SDK_INT >= 28 ? new DisplayCutout(object, list) : null;
        this(object);
    }

    private DisplayCutoutCompat(Object object) {
        this.mDisplayCutout = object;
    }

    static DisplayCutoutCompat wrap(Object object) {
        object = object == null ? null : new DisplayCutoutCompat(object);
        return object;
    }

    public boolean equals(Object object) {
        boolean bl = true;
        if (this == object) {
            return true;
        }
        if (object != null && this.getClass() == object.getClass()) {
            DisplayCutoutCompat displayCutoutCompat = (DisplayCutoutCompat)object;
            object = this.mDisplayCutout;
            if (object == null) {
                if (displayCutoutCompat.mDisplayCutout != null) {
                    bl = false;
                }
            } else {
                bl = object.equals(displayCutoutCompat.mDisplayCutout);
            }
            return bl;
        }
        return false;
    }

    public List<Rect> getBoundingRects() {
        if (Build.VERSION.SDK_INT >= 28) {
            return ((DisplayCutout)this.mDisplayCutout).getBoundingRects();
        }
        return null;
    }

    public int getSafeInsetBottom() {
        if (Build.VERSION.SDK_INT >= 28) {
            return ((DisplayCutout)this.mDisplayCutout).getSafeInsetBottom();
        }
        return 0;
    }

    public int getSafeInsetLeft() {
        if (Build.VERSION.SDK_INT >= 28) {
            return ((DisplayCutout)this.mDisplayCutout).getSafeInsetLeft();
        }
        return 0;
    }

    public int getSafeInsetRight() {
        if (Build.VERSION.SDK_INT >= 28) {
            return ((DisplayCutout)this.mDisplayCutout).getSafeInsetRight();
        }
        return 0;
    }

    public int getSafeInsetTop() {
        if (Build.VERSION.SDK_INT >= 28) {
            return ((DisplayCutout)this.mDisplayCutout).getSafeInsetTop();
        }
        return 0;
    }

    public int hashCode() {
        Object object = this.mDisplayCutout;
        int n = object == null ? 0 : object.hashCode();
        return n;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DisplayCutoutCompat{");
        stringBuilder.append(this.mDisplayCutout);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    DisplayCutout unwrap() {
        return (DisplayCutout)this.mDisplayCutout;
    }
}

