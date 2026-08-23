/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.liulishuo.filedownloader.util;

import android.util.Log;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

public class FileDownloadLog {
    public static boolean NEED_LOG = false;
    private static final String TAG = "FileDownloader.";

    static {
        NEED_LOG = false;
    }

    public static void d(Object object, String string2, Object ... objectArray) {
        FileDownloadLog.log(3, object, string2, objectArray);
    }

    public static void e(Object object, String string2, Object ... objectArray) {
        FileDownloadLog.log(6, object, string2, objectArray);
    }

    public static void e(Object object, Throwable throwable, String string2, Object ... objectArray) {
        FileDownloadLog.log(6, object, throwable, string2, objectArray);
    }

    private static String getTag(Object clazz) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(TAG);
        clazz = clazz instanceof Class ? (Class)clazz : clazz.getClass();
        stringBuilder.append(clazz.getSimpleName());
        return stringBuilder.toString();
    }

    public static void i(Object object, String string2, Object ... objectArray) {
        FileDownloadLog.log(4, object, string2, objectArray);
    }

    private static void log(int n, Object object, String string2, Object ... objectArray) {
        FileDownloadLog.log(n, object, null, string2, objectArray);
    }

    private static void log(int n, Object object, Throwable throwable, String string2, Object ... objectArray) {
        boolean bl = n >= 5;
        if (!bl && !NEED_LOG) {
            return;
        }
        Log.println((int)n, (String)FileDownloadLog.getTag(object), (String)FileDownloadUtils.formatString(string2, objectArray));
        if (throwable != null) {
            throwable.printStackTrace();
        }
    }

    public static void v(Object object, String string2, Object ... objectArray) {
        FileDownloadLog.log(2, object, string2, objectArray);
    }

    public static void w(Object object, String string2, Object ... objectArray) {
        FileDownloadLog.log(5, object, string2, objectArray);
    }
}

