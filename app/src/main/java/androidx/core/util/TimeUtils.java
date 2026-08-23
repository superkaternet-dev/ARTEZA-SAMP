/*
 * Decompiled with CFR 0.152.
 */
package androidx.core.util;

import java.io.PrintWriter;

public final class TimeUtils {
    public static final int HUNDRED_DAY_FIELD_LEN = 19;
    private static final int SECONDS_PER_DAY = 86400;
    private static final int SECONDS_PER_HOUR = 3600;
    private static final int SECONDS_PER_MINUTE = 60;
    private static char[] sFormatStr;
    private static final Object sFormatSync;

    static {
        sFormatSync = new Object();
        sFormatStr = new char[24];
    }

    private TimeUtils() {
    }

    private static int accumField(int n, int n2, boolean bl, int n3) {
        if (!(n > 99 || bl && n3 >= 3)) {
            if (!(n > 9 || bl && n3 >= 2)) {
                if (!bl && n <= 0) {
                    return 0;
                }
                return n2 + 1;
            }
            return n2 + 2;
        }
        return n2 + 3;
    }

    public static void formatDuration(long l, long l2, PrintWriter printWriter) {
        if (l == 0L) {
            printWriter.print("--");
            return;
        }
        TimeUtils.formatDuration(l - l2, printWriter, 0);
    }

    public static void formatDuration(long l, PrintWriter printWriter) {
        TimeUtils.formatDuration(l, printWriter, 0);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void formatDuration(long l, PrintWriter printWriter, int n) {
        Object object = sFormatSync;
        synchronized (object) {
            n = TimeUtils.formatDurationLocked(l, n);
            String string2 = new String(sFormatStr, 0, n);
            printWriter.print(string2);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void formatDuration(long l, StringBuilder stringBuilder) {
        Object object = sFormatSync;
        synchronized (object) {
            int n = TimeUtils.formatDurationLocked(l, 0);
            stringBuilder.append(sFormatStr, 0, n);
            return;
        }
    }

    private static int formatDurationLocked(long l, int n) {
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        if (sFormatStr.length < n) {
            sFormatStr = new char[n];
        }
        char[] cArray = sFormatStr;
        if (l == 0L) {
            while (n - 1 < 0) {
                cArray[0] = 32;
            }
            cArray[0] = 48;
            return 0 + 1;
        }
        if (l > 0L) {
            n7 = 43;
        } else {
            l = -l;
            n7 = 45;
        }
        int n8 = (int)(l % 1000L);
        int n9 = (int)Math.floor(l / 1000L);
        if (n9 > 86400) {
            n6 = n9 / 86400;
            n9 -= 86400 * n6;
        } else {
            n6 = 0;
        }
        if (n9 > 3600) {
            n5 = n9 / 3600;
            n9 -= n5 * 3600;
        } else {
            n5 = 0;
        }
        if (n9 > 60) {
            n4 = n9 / 60;
            n3 = n9 - n4 * 60;
        } else {
            n4 = 0;
            n3 = n9;
        }
        int n10 = 0;
        int n11 = 0;
        int n12 = 3;
        boolean bl = false;
        if (n != 0) {
            n9 = TimeUtils.accumField(n6, 1, false, 0);
            if (n9 > 0) {
                bl = true;
            }
            bl = (n9 += TimeUtils.accumField(n5, 1, bl, 2)) > 0;
            bl = (n9 += TimeUtils.accumField(n4, 1, bl, 2)) > 0;
            n2 = n9 + TimeUtils.accumField(n3, 1, bl, 2);
            n9 = n2 > 0 ? 3 : 0;
            n2 += TimeUtils.accumField(n8, 2, true, n9) + 1;
            n9 = n11;
            while (true) {
                n10 = ++n9;
                if (n2 >= n) break;
                cArray[n9] = 32;
                ++n2;
            }
        }
        cArray[n10] = n7;
        n = n != 0 ? 1 : 0;
        boolean bl2 = true;
        n2 = 2;
        n6 = TimeUtils.printField(cArray, n6, 'd', ++n10, false, 0);
        bl = n6 != n10;
        n9 = n != 0 ? 2 : 0;
        n6 = TimeUtils.printField(cArray, n5, 'h', n6, bl, n9);
        bl = n6 != n10;
        n9 = n != 0 ? 2 : 0;
        n6 = TimeUtils.printField(cArray, n4, 'm', n6, bl, n9);
        bl = n6 != n10 ? bl2 : false;
        n9 = n != 0 ? n2 : 0;
        n9 = TimeUtils.printField(cArray, n3, 's', n6, bl, n9);
        n = n != 0 && n9 != n10 ? n12 : 0;
        n = TimeUtils.printField(cArray, n8, 'm', n9, true, n);
        cArray[n] = 115;
        return n + 1;
    }

    private static int printField(char[] cArray, int n, char c, int n2, boolean bl, int n3) {
        int n4;
        block5: {
            block9: {
                int n5;
                block8: {
                    block7: {
                        block6: {
                            block4: {
                                if (bl) break block4;
                                n4 = n2;
                                if (n <= 0) break block5;
                            }
                            if (bl && n3 >= 3) break block6;
                            n4 = n;
                            n5 = n2;
                            if (n <= 99) break block7;
                        }
                        n4 = n / 100;
                        cArray[n2] = (char)(n4 + 48);
                        n5 = n2 + 1;
                        n4 = n - n4 * 100;
                    }
                    if (bl && n3 >= 2 || n4 > 9) break block8;
                    n3 = n4;
                    n = n5;
                    if (n2 == n5) break block9;
                }
                n2 = n4 / 10;
                cArray[n5] = (char)(n2 + 48);
                n = n5 + 1;
                n3 = n4 - n2 * 10;
            }
            cArray[n] = (char)(n3 + 48);
            cArray[++n] = c;
            n4 = n + 1;
        }
        return n4;
    }
}

