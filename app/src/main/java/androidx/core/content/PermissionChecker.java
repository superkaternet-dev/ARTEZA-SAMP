/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Binder
 *  android.os.Process
 */
package androidx.core.content;

import android.content.Context;
import android.os.Binder;
import android.os.Process;
import androidx.core.app.AppOpsManagerCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class PermissionChecker {
    public static final int PERMISSION_DENIED = -1;
    public static final int PERMISSION_DENIED_APP_OP = -2;
    public static final int PERMISSION_GRANTED = 0;

    private PermissionChecker() {
    }

    public static int checkCallingOrSelfPermission(Context context, String string2) {
        String string3 = Binder.getCallingPid() == Process.myPid() ? context.getPackageName() : null;
        return PermissionChecker.checkPermission(context, string2, Binder.getCallingPid(), Binder.getCallingUid(), string3);
    }

    public static int checkCallingPermission(Context context, String string2, String string3) {
        if (Binder.getCallingPid() == Process.myPid()) {
            return -1;
        }
        return PermissionChecker.checkPermission(context, string2, Binder.getCallingPid(), Binder.getCallingUid(), string3);
    }

    public static int checkPermission(Context context, String object, int n, int n2, String string2) {
        if (context.checkPermission((String)object, n, n2) == -1) {
            return -1;
        }
        String string3 = AppOpsManagerCompat.permissionToOp((String)object);
        if (string3 == null) {
            return 0;
        }
        object = string2;
        if (string2 == null) {
            object = context.getPackageManager().getPackagesForUid(n2);
            if (object != null && ((String[])object).length > 0) {
                object = object[0];
            } else {
                return -1;
            }
        }
        if (AppOpsManagerCompat.noteProxyOpNoThrow(context, string3, (String)object) != 0) {
            return -2;
        }
        return 0;
    }

    public static int checkSelfPermission(Context context, String string2) {
        return PermissionChecker.checkPermission(context, string2, Process.myPid(), Process.myUid(), context.getPackageName());
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface PermissionResult {
    }
}

