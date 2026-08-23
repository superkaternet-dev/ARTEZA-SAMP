/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.util;

import com.google.firebase.database.util.JsonMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GAuthToken {
    private static final String AUTH_KEY = "auth";
    private static final String TOKEN_KEY = "token";
    private static final String TOKEN_PREFIX = "gauth|";
    private final Map<String, Object> auth;
    private final String token;

    public GAuthToken(String string2, Map<String, Object> map) {
        this.token = string2;
        this.auth = map;
    }

    public static GAuthToken tryParseFromString(String object) {
        if (!((String)object).startsWith(TOKEN_PREFIX)) {
            return null;
        }
        object = ((String)object).substring(TOKEN_PREFIX.length());
        try {
            object = JsonMapper.parseJson((String)object);
            object = new GAuthToken((String)object.get(TOKEN_KEY), (Map)object.get(AUTH_KEY));
            return object;
        }
        catch (IOException iOException) {
            throw new RuntimeException("Failed to parse gauth token", iOException);
        }
    }

    public Map<String, Object> getAuth() {
        return this.auth;
    }

    public String getToken() {
        return this.token;
    }

    public String serializeToString() {
        Object object = new HashMap<String, Object>();
        object.put(TOKEN_KEY, this.token);
        object.put(AUTH_KEY, this.auth);
        try {
            object = JsonMapper.serializeJson(object);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(TOKEN_PREFIX);
            stringBuilder.append((String)object);
            object = stringBuilder.toString();
            return object;
        }
        catch (IOException iOException) {
            throw new RuntimeException("Failed to serialize gauth token", iOException);
        }
    }
}

