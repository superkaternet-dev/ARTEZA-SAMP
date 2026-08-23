/*
 * Decompiled with CFR 0.152.
 */
package com.blackrussia.launcher.other;

import java.io.File;
import java.text.DecimalFormat;

public class Utils {
    public static final long GB = 0x40000000L;
    public static final long KB = 1024L;
    public static final long MB = 0x100000L;

    public static String bytesIntoHumanReadable(long l) {
        long[] lArray;
        long[] lArray2 = lArray = new long[4];
        lArray[0] = 0x40000000L;
        lArray2[1] = 0x100000L;
        lArray2[2] = 1024L;
        lArray2[3] = 1L;
        long l2 = l;
        if (l < 1L) {
            l2 = 0L;
        }
        for (int i = 0; i < lArray.length; ++i) {
            l = lArray[i];
            if (l2 < l) continue;
            return Utils.format(l2, l, (new String[]{"GB", "MB", "KB", "B"})[i]);
        }
        return null;
    }

    public static void delete(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] fileArray = file.listFiles();
                int n = fileArray.length;
                for (int i = 0; i < n; ++i) {
                    Utils.delete(fileArray[i]);
                }
            }
            file.delete();
        }
    }

    private static String format(long l, long l2, String string2) {
        double d;
        double d2 = d = (double)l;
        if (l2 > 1L) {
            d2 = l2;
            Double.isNaN(d);
            Double.isNaN(d2);
            d2 = d / d2;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(new DecimalFormat("#,##0.#").format(d2));
        stringBuilder.append(" ");
        stringBuilder.append(string2);
        return stringBuilder.toString();
    }
}

