/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.ActivityInfo
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.os.Build$VERSION
 *  android.util.Log
 */
package androidx.core.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

public final class NavUtils {
    public static final String PARENT_ACTIVITY = "android.support.PARENT_ACTIVITY";
    private static final String TAG = "NavUtils";

    private NavUtils() {
    }

    public static Intent getParentActivityIntent(Activity activity) {
        Object object;
        if (Build.VERSION.SDK_INT >= 16 && (object = activity.getParentActivityIntent()) != null) {
            return object;
        }
        object = NavUtils.getParentActivityName(activity);
        if (object == null) {
            return null;
        }
        ComponentName componentName = new ComponentName((Context)activity, (String)object);
        try {
            if (NavUtils.getParentActivityName((Context)activity, componentName) == null) {
                activity = Intent.makeMainActivity((ComponentName)componentName);
            } else {
                activity = new Intent();
                activity = activity.setComponent(componentName);
            }
            return activity;
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getParentActivityIntent: bad parentActivityName '");
            stringBuilder.append((String)object);
            stringBuilder.append("' in manifest");
            Log.e((String)TAG, (String)stringBuilder.toString());
            return null;
        }
    }

    public static Intent getParentActivityIntent(Context context, ComponentName componentName) throws PackageManager.NameNotFoundException {
        String string2 = NavUtils.getParentActivityName(context, componentName);
        if (string2 == null) {
            return null;
        }
        context = NavUtils.getParentActivityName(context, componentName = new ComponentName(componentName.getPackageName(), string2)) == null ? Intent.makeMainActivity((ComponentName)componentName) : new Intent().setComponent(componentName);
        return context;
    }

    public static Intent getParentActivityIntent(Context context, Class<?> object) throws PackageManager.NameNotFoundException {
        if ((object = NavUtils.getParentActivityName(context, new ComponentName(context, object))) == null) {
            return null;
        }
        context = NavUtils.getParentActivityName(context, (ComponentName)(object = new ComponentName(context, (String)object))) == null ? Intent.makeMainActivity((ComponentName)object) : new Intent().setComponent((ComponentName)object);
        return context;
    }

    public static String getParentActivityName(Activity object) {
        try {
            object = NavUtils.getParentActivityName((Context)object, object.getComponentName());
            return object;
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            throw new IllegalArgumentException(nameNotFoundException);
        }
    }

    public static String getParentActivityName(Context context, ComponentName object) throws PackageManager.NameNotFoundException {
        int n;
        Object object2 = context.getPackageManager();
        int n2 = Build.VERSION.SDK_INT >= 24 ? 0x80 | 0x200 : 0x80 | 0x200;
        if (Build.VERSION.SDK_INT >= 29) {
            n = n2 | 0x100C0000;
        } else {
            n = n2;
            if (Build.VERSION.SDK_INT >= 24) {
                n = n2 | 0xC0000;
            }
        }
        object = object2.getActivityInfo((ComponentName)object, n);
        if (Build.VERSION.SDK_INT >= 16 && (object2 = ((ActivityInfo)object).parentActivityName) != null) {
            return object2;
        }
        if (((ActivityInfo)object).metaData == null) {
            return null;
        }
        object2 = ((ActivityInfo)object).metaData.getString(PARENT_ACTIVITY);
        if (object2 == null) {
            return null;
        }
        object = object2;
        if (((String)object2).charAt(0) == '.') {
            object = new StringBuilder();
            ((StringBuilder)object).append(context.getPackageName());
            ((StringBuilder)object).append((String)object2);
            object = ((StringBuilder)object).toString();
        }
        return object;
    }

    public static void navigateUpFromSameTask(Activity activity) {
        Object object = NavUtils.getParentActivityIntent(activity);
        if (object != null) {
            NavUtils.navigateUpTo(activity, (Intent)object);
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Activity ");
        ((StringBuilder)object).append(activity.getClass().getSimpleName());
        ((StringBuilder)object).append(" does not have a parent activity name specified. (Did you forget to add the android.support.PARENT_ACTIVITY <meta-data>  element in your manifest?)");
        throw new IllegalArgumentException(((StringBuilder)object).toString());
    }

    public static void navigateUpTo(Activity activity, Intent intent) {
        if (Build.VERSION.SDK_INT >= 16) {
            activity.navigateUpTo(intent);
        } else {
            intent.addFlags(0x4000000);
            activity.startActivity(intent);
            activity.finish();
        }
    }

    public static boolean shouldUpRecreateTask(Activity object, Intent intent) {
        if (Build.VERSION.SDK_INT >= 16) {
            return object.shouldUpRecreateTask(intent);
        }
        boolean bl = (object = object.getIntent().getAction()) != null && !((String)object).equals("android.intent.action.MAIN");
        return bl;
    }
}

