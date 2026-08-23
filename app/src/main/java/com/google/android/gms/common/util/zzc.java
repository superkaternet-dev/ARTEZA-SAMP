/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 */
package com.google.android.gms.common.util;

import android.text.TextUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class zzc {
    private static final Pattern zza = Pattern.compile("\\\\u[0-9a-fA-F]{4}");

    public static String zza(String string2) {
        CharSequence charSequence = string2;
        if (!TextUtils.isEmpty((CharSequence)string2)) {
            Matcher matcher = zza.matcher(string2);
            CharSequence charSequence2 = null;
            while (matcher.find()) {
                charSequence = charSequence2;
                if (charSequence2 == null) {
                    charSequence = new StringBuffer();
                }
                matcher.appendReplacement((StringBuffer)charSequence, new String(Character.toChars(Integer.parseInt(matcher.group().substring(2), 16))));
                charSequence2 = charSequence;
            }
            if (charSequence2 == null) {
                return string2;
            }
            matcher.appendTail((StringBuffer)charSequence2);
            charSequence = charSequence2.toString();
        }
        return charSequence;
    }
}

