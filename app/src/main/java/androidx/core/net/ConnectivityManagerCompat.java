/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.net.ConnectivityManager
 *  android.net.NetworkInfo
 *  android.os.Build$VERSION
 */
package androidx.core.net;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class ConnectivityManagerCompat {
    public static final int RESTRICT_BACKGROUND_STATUS_DISABLED = 1;
    public static final int RESTRICT_BACKGROUND_STATUS_ENABLED = 3;
    public static final int RESTRICT_BACKGROUND_STATUS_WHITELISTED = 2;

    private ConnectivityManagerCompat() {
    }

    public static NetworkInfo getNetworkInfoFromBroadcast(ConnectivityManager connectivityManager, Intent intent) {
        if ((intent = (NetworkInfo)intent.getParcelableExtra("networkInfo")) != null) {
            return connectivityManager.getNetworkInfo(intent.getType());
        }
        return null;
    }

    public static int getRestrictBackgroundStatus(ConnectivityManager connectivityManager) {
        if (Build.VERSION.SDK_INT >= 24) {
            return connectivityManager.getRestrictBackgroundStatus();
        }
        return 3;
    }

    public static boolean isActiveNetworkMetered(ConnectivityManager connectivityManager) {
        if (Build.VERSION.SDK_INT >= 16) {
            return connectivityManager.isActiveNetworkMetered();
        }
        if ((connectivityManager = connectivityManager.getActiveNetworkInfo()) == null) {
            return true;
        }
        switch (connectivityManager.getType()) {
            default: {
                return true;
            }
            case 1: 
            case 7: 
            case 9: {
                return false;
            }
            case 0: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
        }
        return true;
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface RestrictBackgroundStatus {
    }
}

