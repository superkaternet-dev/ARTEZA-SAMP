/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 */
package androidx.core.database;

import android.text.TextUtils;

@Deprecated
public final class DatabaseUtilsCompat {
    private DatabaseUtilsCompat() {
    }

    @Deprecated
    public static String[] appendSelectionArgs(String[] stringArray, String[] stringArray2) {
        if (stringArray != null && stringArray.length != 0) {
            String[] stringArray3 = new String[stringArray.length + stringArray2.length];
            System.arraycopy(stringArray, 0, stringArray3, 0, stringArray.length);
            System.arraycopy(stringArray2, 0, stringArray3, stringArray.length, stringArray2.length);
            return stringArray3;
        }
        return stringArray2;
    }

    @Deprecated
    public static String concatenateWhere(String string2, String string3) {
        if (TextUtils.isEmpty((CharSequence)string2)) {
            return string3;
        }
        if (TextUtils.isEmpty((CharSequence)string3)) {
            return string2;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        stringBuilder.append(string2);
        stringBuilder.append(") AND (");
        stringBuilder.append(string3);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}

