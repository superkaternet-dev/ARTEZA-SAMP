/*
 * Decompiled with CFR 0.152.
 */
package com.hzy.libp7zip;

public class P7ZipApi {
    static {
        System.loadLibrary("p7zip");
    }

    public static native int executeCommand(String var0);

    public static native String get7zVersionInfo();
}

