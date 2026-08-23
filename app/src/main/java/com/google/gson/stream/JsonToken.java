/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.stream;

public final class JsonToken
extends Enum<JsonToken> {
    private static final JsonToken[] $VALUES;
    public static final /* enum */ JsonToken BEGIN_ARRAY;
    public static final /* enum */ JsonToken BEGIN_OBJECT;
    public static final /* enum */ JsonToken BOOLEAN;
    public static final /* enum */ JsonToken END_ARRAY;
    public static final /* enum */ JsonToken END_DOCUMENT;
    public static final /* enum */ JsonToken END_OBJECT;
    public static final /* enum */ JsonToken NAME;
    public static final /* enum */ JsonToken NULL;
    public static final /* enum */ JsonToken NUMBER;
    public static final /* enum */ JsonToken STRING;

    static {
        JsonToken jsonToken;
        JsonToken jsonToken2;
        JsonToken jsonToken3;
        JsonToken jsonToken4;
        JsonToken jsonToken5;
        JsonToken jsonToken6;
        JsonToken jsonToken7;
        JsonToken jsonToken8;
        JsonToken jsonToken9;
        JsonToken jsonToken10;
        BEGIN_ARRAY = jsonToken10 = new JsonToken();
        END_ARRAY = jsonToken9 = new JsonToken();
        BEGIN_OBJECT = jsonToken8 = new JsonToken();
        END_OBJECT = jsonToken7 = new JsonToken();
        NAME = jsonToken6 = new JsonToken();
        STRING = jsonToken5 = new JsonToken();
        NUMBER = jsonToken4 = new JsonToken();
        BOOLEAN = jsonToken3 = new JsonToken();
        NULL = jsonToken2 = new JsonToken();
        END_DOCUMENT = jsonToken = new JsonToken();
        $VALUES = new JsonToken[]{jsonToken10, jsonToken9, jsonToken8, jsonToken7, jsonToken6, jsonToken5, jsonToken4, jsonToken3, jsonToken2, jsonToken};
    }

    public static JsonToken valueOf(String string2) {
        return Enum.valueOf(JsonToken.class, string2);
    }

    public static JsonToken[] values() {
        return (JsonToken[])$VALUES.clone();
    }
}

