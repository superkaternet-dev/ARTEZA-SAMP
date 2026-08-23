/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  org.checkerframework.checker.nullness.qual.EnsuresNonNullIf
 */
package com.google.android.gms.common.util;

import android.text.TextUtils;
import java.util.regex.Pattern;
import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;

public class Strings {
    private static final Pattern zza = Pattern.compile("\\$\\{(.*?)\\}");

    private Strings() {
    }

    public static String emptyToNull(String string2) {
        String string3 = string2;
        if (TextUtils.isEmpty((CharSequence)string2)) {
            string3 = null;
        }
        return string3;
    }

    @EnsuresNonNullIf(expression={"#1"}, result=false)
    public static boolean isEmptyOrWhitespace(String string2) {
        return string2 == null || string2.trim().isEmpty();
        {
        }
    }
}

