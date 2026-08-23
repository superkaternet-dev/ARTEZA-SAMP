/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentSender
 *  android.content.IntentSender$SendIntentException
 *  android.content.pm.ResolveInfo
 *  android.content.pm.ShortcutInfo
 *  android.content.pm.ShortcutManager
 *  android.os.Build$VERSION
 *  android.text.TextUtils
 */
package androidx.core.content.pm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.os.Build;
import android.text.TextUtils;
import androidx.core.content.ContextCompat;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutInfoCompatSaver;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShortcutManagerCompat {
    static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    public static final String EXTRA_SHORTCUT_ID = "android.intent.extra.shortcut.ID";
    static final String INSTALL_SHORTCUT_PERMISSION = "com.android.launcher.permission.INSTALL_SHORTCUT";
    private static volatile ShortcutInfoCompatSaver<?> sShortcutInfoCompatSaver = null;

    private ShortcutManagerCompat() {
    }

    public static boolean addDynamicShortcuts(Context context, List<ShortcutInfoCompat> list) {
        if (Build.VERSION.SDK_INT >= 25) {
            ArrayList<ShortcutInfo> arrayList = new ArrayList<ShortcutInfo>();
            Iterator<ShortcutInfoCompat> iterator2 = list.iterator();
            while (iterator2.hasNext()) {
                arrayList.add(iterator2.next().toShortcutInfo());
            }
            if (!((ShortcutManager)context.getSystemService(ShortcutManager.class)).addDynamicShortcuts(arrayList)) {
                return false;
            }
        }
        ShortcutManagerCompat.getShortcutInfoSaverInstance(context).addShortcuts(list);
        return true;
    }

    public static Intent createShortcutResultIntent(Context context, ShortcutInfoCompat shortcutInfoCompat) {
        Intent intent = null;
        if (Build.VERSION.SDK_INT >= 26) {
            intent = ((ShortcutManager)context.getSystemService(ShortcutManager.class)).createShortcutResultIntent(shortcutInfoCompat.toShortcutInfo());
        }
        context = intent;
        if (intent == null) {
            context = new Intent();
        }
        return shortcutInfoCompat.addToIntent((Intent)context);
    }

    public static List<ShortcutInfoCompat> getDynamicShortcuts(Context object) {
        if (Build.VERSION.SDK_INT >= 25) {
            Object object2 = ((ShortcutManager)object.getSystemService(ShortcutManager.class)).getDynamicShortcuts();
            ArrayList<ShortcutInfoCompat> arrayList = new ArrayList<ShortcutInfoCompat>(object2.size());
            object2 = object2.iterator();
            while (object2.hasNext()) {
                arrayList.add(new ShortcutInfoCompat.Builder((Context)object, (ShortcutInfo)object2.next()).build());
            }
            return arrayList;
        }
        try {
            object = ShortcutManagerCompat.getShortcutInfoSaverInstance(object).getShortcuts();
            return object;
        }
        catch (Exception exception) {
            return new ArrayList<ShortcutInfoCompat>();
        }
    }

    public static int getMaxShortcutCountPerActivity(Context context) {
        if (Build.VERSION.SDK_INT >= 25) {
            return ((ShortcutManager)context.getSystemService(ShortcutManager.class)).getMaxShortcutCountPerActivity();
        }
        return 0;
    }

    private static ShortcutInfoCompatSaver<?> getShortcutInfoSaverInstance(Context context) {
        if (sShortcutInfoCompatSaver == null) {
            if (Build.VERSION.SDK_INT >= 23) {
                try {
                    sShortcutInfoCompatSaver = (ShortcutInfoCompatSaver)Class.forName("androidx.sharetarget.ShortcutInfoCompatSaverImpl", false, ShortcutManagerCompat.class.getClassLoader()).getMethod("getInstance", Context.class).invoke(null, context);
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            if (sShortcutInfoCompatSaver == null) {
                sShortcutInfoCompatSaver = new ShortcutInfoCompatSaver.NoopImpl();
            }
        }
        return sShortcutInfoCompatSaver;
    }

    public static boolean isRequestPinShortcutSupported(Context object) {
        if (Build.VERSION.SDK_INT >= 26) {
            return ((ShortcutManager)object.getSystemService(ShortcutManager.class)).isRequestPinShortcutSupported();
        }
        if (ContextCompat.checkSelfPermission(object, INSTALL_SHORTCUT_PERMISSION) != 0) {
            return false;
        }
        Iterator iterator2 = object.getPackageManager().queryBroadcastReceivers(new Intent(ACTION_INSTALL_SHORTCUT), 0).iterator();
        while (iterator2.hasNext()) {
            object = ((ResolveInfo)iterator2.next()).activityInfo.permission;
            if (!TextUtils.isEmpty((CharSequence)object) && !INSTALL_SHORTCUT_PERMISSION.equals(object)) continue;
            return true;
        }
        return false;
    }

    public static void removeAllDynamicShortcuts(Context context) {
        if (Build.VERSION.SDK_INT >= 25) {
            ((ShortcutManager)context.getSystemService(ShortcutManager.class)).removeAllDynamicShortcuts();
        }
        ShortcutManagerCompat.getShortcutInfoSaverInstance(context).removeAllShortcuts();
    }

    public static void removeDynamicShortcuts(Context context, List<String> list) {
        if (Build.VERSION.SDK_INT >= 25) {
            ((ShortcutManager)context.getSystemService(ShortcutManager.class)).removeDynamicShortcuts(list);
        }
        ShortcutManagerCompat.getShortcutInfoSaverInstance(context).removeShortcuts(list);
    }

    public static boolean requestPinShortcut(Context context, ShortcutInfoCompat shortcutInfoCompat, IntentSender intentSender) {
        if (Build.VERSION.SDK_INT >= 26) {
            return ((ShortcutManager)context.getSystemService(ShortcutManager.class)).requestPinShortcut(shortcutInfoCompat.toShortcutInfo(), intentSender);
        }
        if (!ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
            return false;
        }
        shortcutInfoCompat = shortcutInfoCompat.addToIntent(new Intent(ACTION_INSTALL_SHORTCUT));
        if (intentSender == null) {
            context.sendBroadcast((Intent)shortcutInfoCompat);
            return true;
        }
        context.sendOrderedBroadcast((Intent)shortcutInfoCompat, null, new BroadcastReceiver(intentSender){
            final IntentSender val$callback;
            {
                this.val$callback = intentSender;
            }

            public void onReceive(Context context, Intent intent) {
                try {
                    this.val$callback.sendIntent(context, 0, null, null, null);
                }
                catch (IntentSender.SendIntentException sendIntentException) {
                    // empty catch block
                }
            }
        }, null, -1, null, null);
        return true;
    }

    public static boolean updateShortcuts(Context context, List<ShortcutInfoCompat> list) {
        if (Build.VERSION.SDK_INT >= 25) {
            ArrayList<ShortcutInfo> arrayList = new ArrayList<ShortcutInfo>();
            Iterator<ShortcutInfoCompat> iterator2 = list.iterator();
            while (iterator2.hasNext()) {
                arrayList.add(iterator2.next().toShortcutInfo());
            }
            if (!((ShortcutManager)context.getSystemService(ShortcutManager.class)).updateShortcuts(arrayList)) {
                return false;
            }
        }
        ShortcutManagerCompat.getShortcutInfoSaverInstance(context).addShortcuts(list);
        return true;
    }
}

