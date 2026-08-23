/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.util;

import java.util.HashMap;
import java.util.Iterator;

public class MapUtils {
    public static void writeStringMapToJson(StringBuilder stringBuilder, HashMap<String, String> hashMap) {
        stringBuilder.append("{");
        Iterator<String> iterator2 = hashMap.keySet().iterator();
        boolean bl = true;
        while (iterator2.hasNext()) {
            String string2 = iterator2.next();
            if (!bl) {
                stringBuilder.append(",");
            }
            String string3 = hashMap.get(string2);
            stringBuilder.append("\"");
            stringBuilder.append(string2);
            stringBuilder.append("\":");
            if (string3 == null) {
                stringBuilder.append("null");
                bl = false;
                continue;
            }
            stringBuilder.append("\"");
            stringBuilder.append(string3);
            stringBuilder.append("\"");
            bl = false;
        }
        stringBuilder.append("}");
    }
}

