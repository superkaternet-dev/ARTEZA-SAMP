/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.json.JSONArray
 *  org.json.JSONException
 *  org.json.JSONObject
 *  org.json.JSONStringer
 *  org.json.JSONTokener
 */
package com.google.firebase.database.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

public class JsonMapper {
    public static Map<String, Object> parseJson(String object) throws IOException {
        try {
            JSONObject jSONObject = new JSONObject((String)object);
            object = JsonMapper.unwrapJsonObject(jSONObject);
            return object;
        }
        catch (JSONException jSONException) {
            throw new IOException(jSONException);
        }
    }

    public static Object parseJsonValue(String object) throws IOException {
        try {
            JSONTokener jSONTokener = new JSONTokener((String)object);
            object = JsonMapper.unwrapJson(jSONTokener.nextValue());
            return object;
        }
        catch (JSONException jSONException) {
            throw new IOException(jSONException);
        }
    }

    public static String serializeJson(Map<String, Object> map) throws IOException {
        return JsonMapper.serializeJsonValue(map);
    }

    public static String serializeJsonValue(Object object) throws IOException {
        if (object == null) {
            return "null";
        }
        if (object instanceof String) {
            return JSONObject.quote((String)((String)object));
        }
        if (object instanceof Number) {
            try {
                object = JSONObject.numberToString((Number)((Number)object));
                return object;
            }
            catch (JSONException jSONException) {
                throw new IOException("Could not serialize number", jSONException);
            }
        }
        if (object instanceof Boolean) {
            object = (Boolean)object != false ? "true" : "false";
            return object;
        }
        try {
            JSONStringer jSONStringer = new JSONStringer();
            JsonMapper.serializeJsonValue(object, jSONStringer);
            object = jSONStringer.toString();
            return object;
        }
        catch (JSONException jSONException) {
            throw new IOException("Failed to serialize JSON", jSONException);
        }
    }

    private static void serializeJsonValue(Object iterator2, JSONStringer jSONStringer) throws IOException, JSONException {
        if (iterator2 instanceof Map) {
            jSONStringer.object();
            for (Map.Entry entry : ((Map)((Object)iterator2)).entrySet()) {
                jSONStringer.key((String)entry.getKey());
                JsonMapper.serializeJsonValue(entry.getValue(), jSONStringer);
            }
            jSONStringer.endObject();
        } else if (iterator2 instanceof Collection) {
            iterator2 = (Collection)((Object)iterator2);
            jSONStringer.array();
            iterator2 = iterator2.iterator();
            while (iterator2.hasNext()) {
                JsonMapper.serializeJsonValue(iterator2.next(), jSONStringer);
            }
            jSONStringer.endArray();
        } else {
            jSONStringer.value((Object)iterator2);
        }
    }

    private static Object unwrapJson(Object object) throws JSONException {
        if (object instanceof JSONObject) {
            return JsonMapper.unwrapJsonObject((JSONObject)object);
        }
        if (object instanceof JSONArray) {
            return JsonMapper.unwrapJsonArray((JSONArray)object);
        }
        if (object.equals(JSONObject.NULL)) {
            return null;
        }
        return object;
    }

    private static List<Object> unwrapJsonArray(JSONArray jSONArray) throws JSONException {
        ArrayList<Object> arrayList = new ArrayList<Object>(jSONArray.length());
        for (int i = 0; i < jSONArray.length(); ++i) {
            arrayList.add(JsonMapper.unwrapJson(jSONArray.get(i)));
        }
        return arrayList;
    }

    private static Map<String, Object> unwrapJsonObject(JSONObject jSONObject) throws JSONException {
        HashMap<String, Object> hashMap = new HashMap<String, Object>(jSONObject.length());
        Iterator iterator2 = jSONObject.keys();
        while (iterator2.hasNext()) {
            String string2 = (String)iterator2.next();
            hashMap.put(string2, JsonMapper.unwrapJson(jSONObject.get(string2)));
        }
        return hashMap;
    }
}

