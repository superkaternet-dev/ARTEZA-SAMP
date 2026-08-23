/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.platforminfo;

import kotlin.KotlinVersion;

public final class KotlinDetector {
    private KotlinDetector() {
    }

    public static String detectVersion() {
        try {
            String string2 = KotlinVersion.CURRENT.toString();
            return string2;
        }
        catch (NoClassDefFoundError noClassDefFoundError) {
            return null;
        }
    }
}

