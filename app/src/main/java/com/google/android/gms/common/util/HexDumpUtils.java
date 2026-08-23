/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.util;

public final class HexDumpUtils {
    public static String dump(byte[] byArray, int n, int n2, boolean bl) {
        int n3;
        if (byArray != null && (n3 = byArray.length) != 0 && n >= 0 && n2 > 0 && n + n2 <= n3) {
            n3 = bl ? 75 : 57;
            StringBuilder stringBuilder = new StringBuilder(n3 * ((n2 + 15) / 16));
            int n4 = n2;
            n3 = 0;
            int n5 = 0;
            while (n4 > 0) {
                int n6;
                if (n3 == 0) {
                    if (n2 < 65536) {
                        stringBuilder.append(String.format("%04X:", n));
                    } else {
                        stringBuilder.append(String.format("%08X:", n));
                    }
                    n6 = n;
                } else {
                    n6 = n5;
                    if (n3 == 8) {
                        stringBuilder.append(" -");
                        n6 = n5;
                    }
                }
                stringBuilder.append(String.format(" %02X", byArray[n] & 0xFF));
                n5 = n3 + 1;
                if (bl && (n5 == 16 || --n4 == 0)) {
                    int n7 = 16 - n5;
                    if (n7 > 0) {
                        for (n3 = 0; n3 < n7; ++n3) {
                            stringBuilder.append("   ");
                        }
                    }
                    if (n7 >= 8) {
                        stringBuilder.append("  ");
                    }
                    stringBuilder.append("  ");
                    for (n3 = 0; n3 < n5; ++n3) {
                        int n8;
                        int n9 = byArray[n6 + n3];
                        if (n9 >= 32) {
                            n8 = n9;
                            if (n9 > 126) {
                                n8 = 46;
                            }
                        } else {
                            n8 = 46;
                        }
                        stringBuilder.append((char)n8);
                    }
                }
                if (n5 != 16 && n4 != 0) {
                    n3 = n5;
                } else {
                    stringBuilder.append('\n');
                    n3 = 0;
                }
                ++n;
                n5 = n6;
            }
            return stringBuilder.toString();
        }
        return null;
    }
}

