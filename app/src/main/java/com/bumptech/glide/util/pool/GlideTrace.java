/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.util.pool;

import java.util.concurrent.atomic.AtomicInteger;

public final class GlideTrace {
    private static final AtomicInteger COOKIE_CREATOR = null;
    private static final int MAX_LENGTH = 127;
    private static final boolean TRACING_ENABLED = false;

    private GlideTrace() {
    }

    public static void beginSection(String string2) {
    }

    public static int beginSectionAsync(String string2) {
        return -1;
    }

    public static void beginSectionFormat(String string2, Object object) {
    }

    public static void beginSectionFormat(String string2, Object object, Object object2) {
    }

    public static void beginSectionFormat(String string2, Object object, Object object2, Object object3) {
    }

    public static void endSection() {
    }

    public static void endSectionAsync(String string2, int n) {
    }

    private static String truncateTag(String string2) {
        if (string2.length() > 127) {
            return string2.substring(0, 126);
        }
        return string2;
    }
}

