/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.SharedPreferences
 */
package androidx.core.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AppLaunchChecker {
    private static final String KEY_STARTED_FROM_LAUNCHER = "startedFromLauncher";
    private static final String SHARED_PREFS_NAME = "android.support.AppLaunchChecker";

    @Deprecated
    public AppLaunchChecker() {
    }

    public static boolean hasStartedFromLauncher(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, 0).getBoolean(KEY_STARTED_FROM_LAUNCHER, false);
    }

    public static void onActivityCreate(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREFS_NAME, 0);
        if (sharedPreferences.getBoolean(KEY_STARTED_FROM_LAUNCHER, false)) {
            return;
        }
        if ((activity = activity.getIntent()) == null) {
            return;
        }
        if ("android.intent.action.MAIN".equals(activity.getAction()) && (activity.hasCategory("android.intent.category.LAUNCHER") || activity.hasCategory("android.intent.category.LEANBACK_LAUNCHER"))) {
            sharedPreferences.edit().putBoolean(KEY_STARTED_FROM_LAUNCHER, true).apply();
        }
    }
}

