/*
 * Decompiled with CFR 0.152.
 */
package com.blackrussia.launcher.activity;

public class UnZip {
    public static String getCompressCmd(String string2, String string3, String string4) {
        return String.format("7z a -t%s '%s' '%s'", string4, string3, string2);
    }

    public static String getExtractCmd(String string2, String string3) {
        return String.format("7z x '%s' '-o%s' -aoa", string2, string3);
    }
}

