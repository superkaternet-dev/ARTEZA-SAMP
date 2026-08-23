/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.connection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConnectionUtils {
    public static void hardAssert(boolean bl) {
        ConnectionUtils.hardAssert(bl, "", new Object[0]);
    }

    public static void hardAssert(boolean bl, String string2, Object ... objectArray) {
        if (bl) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("hardAssert failed: ");
        stringBuilder.append(String.format(string2, objectArray));
        throw new AssertionError((Object)stringBuilder.toString());
    }

    public static Long longFromObject(Object object) {
        if (object instanceof Integer) {
            return (long)((Integer)object);
        }
        if (object instanceof Long) {
            return (Long)object;
        }
        return null;
    }

    public static String pathToString(List<String> object) {
        if (object.isEmpty()) {
            return "/";
        }
        StringBuilder stringBuilder = new StringBuilder();
        boolean bl = true;
        Iterator<String> iterator2 = object.iterator();
        while (iterator2.hasNext()) {
            object = iterator2.next();
            if (!bl) {
                stringBuilder.append("/");
            }
            bl = false;
            stringBuilder.append((String)object);
        }
        return stringBuilder.toString();
    }

    public static List<String> stringToPath(String stringArray) {
        ArrayList<String> arrayList = new ArrayList<String>();
        stringArray = stringArray.split("/", -1);
        for (int i = 0; i < stringArray.length; ++i) {
            if (stringArray[i].isEmpty()) continue;
            arrayList.add(stringArray[i]);
        }
        return arrayList;
    }
}

