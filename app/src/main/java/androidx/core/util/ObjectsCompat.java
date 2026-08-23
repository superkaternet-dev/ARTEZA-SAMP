/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 */
package androidx.core.util;

import android.os.Build;
import androidx.core.graphics.ColorUtils$$ExternalSyntheticBackport0;
import java.util.Arrays;

public class ObjectsCompat {
    private ObjectsCompat() {
    }

    public static boolean equals(Object object, Object object2) {
        if (Build.VERSION.SDK_INT >= 19) {
            return ColorUtils$$ExternalSyntheticBackport0.m(object, object2);
        }
        boolean bl = object == object2 || object != null && object.equals(object2);
        return bl;
    }

    public static int hash(Object ... objectArray) {
        if (Build.VERSION.SDK_INT >= 19) {
            return Arrays.hashCode(objectArray);
        }
        return Arrays.hashCode(objectArray);
    }

    public static int hashCode(Object object) {
        int n = object != null ? object.hashCode() : 0;
        return n;
    }
}

