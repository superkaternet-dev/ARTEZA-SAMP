/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.BlendMode
 *  android.graphics.BlendModeColorFilter
 *  android.graphics.ColorFilter
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.PorterDuffColorFilter
 *  android.os.Build$VERSION
 */
package androidx.core.graphics;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import androidx.core.graphics.BlendModeCompat;
import androidx.core.graphics.BlendModeUtils;

public class BlendModeColorFilterCompat {
    private BlendModeColorFilterCompat() {
    }

    public static ColorFilter createBlendModeColorFilterCompat(int n, BlendModeCompat blendModeCompat) {
        int n2 = Build.VERSION.SDK_INT;
        Object var3_3 = null;
        Object var4_4 = null;
        if (n2 >= 29) {
            blendModeCompat = (blendModeCompat = BlendModeUtils.obtainBlendModeFromCompat(blendModeCompat)) != null ? new BlendModeColorFilter(n, (BlendMode)blendModeCompat) : var4_4;
            return blendModeCompat;
        }
        blendModeCompat = (blendModeCompat = BlendModeUtils.obtainPorterDuffFromCompat(blendModeCompat)) != null ? new PorterDuffColorFilter(n, (PorterDuff.Mode)blendModeCompat) : var3_3;
        return blendModeCompat;
    }
}

