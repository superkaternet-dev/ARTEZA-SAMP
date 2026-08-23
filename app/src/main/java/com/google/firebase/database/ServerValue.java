/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ServerValue {
    public static final Map<String, String> TIMESTAMP = ServerValue.createScalarServerValuePlaceholder("timestamp");

    private static Map<String, Map<String, Object>> createParameterizedServerValuePlaceholder(String object, Object object2) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put((String)object, object2);
        object = new HashMap();
        object.put(".sv", Collections.unmodifiableMap(hashMap));
        return Collections.unmodifiableMap(object);
    }

    private static Map<String, String> createScalarServerValuePlaceholder(String string2) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put(".sv", string2);
        return Collections.unmodifiableMap(hashMap);
    }

    public static final Object increment(double d) {
        return ServerValue.createParameterizedServerValuePlaceholder("increment", d);
    }

    public static final Object increment(long l) {
        return ServerValue.createParameterizedServerValuePlaceholder("increment", l);
    }
}

