/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.location.LocationManager
 *  android.os.Build$VERSION
 */
package androidx.core.location;

import android.location.LocationManager;
import android.os.Build;

public final class LocationManagerCompat {
    private LocationManagerCompat() {
    }

    public static boolean isLocationEnabled(LocationManager locationManager) {
        if (Build.VERSION.SDK_INT >= 28) {
            return locationManager.isLocationEnabled();
        }
        boolean bl = locationManager.isProviderEnabled("network") || locationManager.isProviderEnabled("gps");
        return bl;
    }
}

