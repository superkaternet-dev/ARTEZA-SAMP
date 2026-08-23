/*
 * Decompiled with CFR 0.152.
 */
package androidx.core.content;

import java.util.ArrayList;

public final class MimeTypeFilter {
    private MimeTypeFilter() {
    }

    public static String matches(String string22, String[] stringArray) {
        if (string22 == null) {
            return null;
        }
        String[] stringArray2 = string22.split("/");
        for (String string22 : stringArray) {
            if (!MimeTypeFilter.mimeTypeAgainstFilter(stringArray2, string22.split("/"))) continue;
            return string22;
        }
        return null;
    }

    public static String matches(String[] stringArray, String stringArray2) {
        if (stringArray == null) {
            return null;
        }
        stringArray2 = stringArray2.split("/");
        for (String string2 : stringArray) {
            if (!MimeTypeFilter.mimeTypeAgainstFilter(string2.split("/"), stringArray2)) continue;
            return string2;
        }
        return null;
    }

    public static boolean matches(String string2, String string3) {
        if (string2 == null) {
            return false;
        }
        return MimeTypeFilter.mimeTypeAgainstFilter(string2.split("/"), string3.split("/"));
    }

    public static String[] matchesMany(String[] stringArray, String stringArray2) {
        if (stringArray == null) {
            return new String[0];
        }
        ArrayList<String> arrayList = new ArrayList<String>();
        stringArray2 = stringArray2.split("/");
        for (String string2 : stringArray) {
            if (!MimeTypeFilter.mimeTypeAgainstFilter(string2.split("/"), stringArray2)) continue;
            arrayList.add(string2);
        }
        return arrayList.toArray(new String[arrayList.size()]);
    }

    private static boolean mimeTypeAgainstFilter(String[] stringArray, String[] stringArray2) {
        if (stringArray2.length == 2) {
            if (!stringArray2[0].isEmpty() && !stringArray2[1].isEmpty()) {
                if (stringArray.length != 2) {
                    return false;
                }
                if (!"*".equals(stringArray2[0]) && !stringArray2[0].equals(stringArray[0])) {
                    return false;
                }
                return "*".equals(stringArray2[1]) || stringArray2[1].equals(stringArray[1]);
            }
            throw new IllegalArgumentException("Ill-formatted MIME type filter. Type or subtype empty.");
        }
        throw new IllegalArgumentException("Ill-formatted MIME type filter. Must be type/subtype.");
    }
}

