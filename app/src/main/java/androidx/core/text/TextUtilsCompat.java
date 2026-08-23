/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 *  android.text.TextUtils
 */
package androidx.core.text;

import android.os.Build;
import android.text.TextUtils;
import androidx.core.text.ICUCompat;
import java.util.Locale;

public final class TextUtilsCompat {
    private static final String ARAB_SCRIPT_SUBTAG = "Arab";
    private static final String HEBR_SCRIPT_SUBTAG = "Hebr";
    private static final Locale ROOT = new Locale("", "");

    private TextUtilsCompat() {
    }

    private static int getLayoutDirectionFromFirstChar(Locale locale) {
        switch (Character.getDirectionality(locale.getDisplayName(locale).charAt(0))) {
            default: {
                return 0;
            }
            case 1: 
            case 2: 
        }
        return 1;
    }

    public static int getLayoutDirectionFromLocale(Locale locale) {
        if (Build.VERSION.SDK_INT >= 17) {
            return TextUtils.getLayoutDirectionFromLocale((Locale)locale);
        }
        if (locale != null && !locale.equals(ROOT)) {
            String string2 = ICUCompat.maximizeAndGetScript(locale);
            if (string2 == null) {
                return TextUtilsCompat.getLayoutDirectionFromFirstChar(locale);
            }
            if (string2.equalsIgnoreCase(ARAB_SCRIPT_SUBTAG) || string2.equalsIgnoreCase(HEBR_SCRIPT_SUBTAG)) {
                return 1;
            }
        }
        return 0;
    }

    public static String htmlEncode(String string2) {
        if (Build.VERSION.SDK_INT >= 17) {
            return TextUtils.htmlEncode((String)string2);
        }
        StringBuilder stringBuilder = new StringBuilder();
        block7: for (int i = 0; i < string2.length(); ++i) {
            char c = string2.charAt(i);
            switch (c) {
                default: {
                    stringBuilder.append(c);
                    continue block7;
                }
                case '>': {
                    stringBuilder.append("&gt;");
                    continue block7;
                }
                case '<': {
                    stringBuilder.append("&lt;");
                    continue block7;
                }
                case '\'': {
                    stringBuilder.append("&#39;");
                    continue block7;
                }
                case '&': {
                    stringBuilder.append("&amp;");
                    continue block7;
                }
                case '\"': {
                    stringBuilder.append("&quot;");
                }
            }
        }
        return stringBuilder.toString();
    }
}

