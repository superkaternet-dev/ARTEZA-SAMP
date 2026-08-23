/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  org.json.JSONArray
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.google.android.gms.common.util;

import android.text.TextUtils;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.zzc;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class JsonUtils {
    private static final Pattern zza = Pattern.compile("\\\\.");
    private static final Pattern zzb = Pattern.compile("[\\\\\"/\b\f\n\r\t]");

    private JsonUtils() {
    }

    public static boolean areJsonValuesEquivalent(Object object, Object object2) {
        if (object == null && object2 == null) {
            return true;
        }
        if (object != null && object2 != null) {
            if (object instanceof JSONObject && object2 instanceof JSONObject) {
                object = (JSONObject)object;
                JSONObject jSONObject = (JSONObject)object2;
                if (object.length() != jSONObject.length()) {
                    return false;
                }
                object2 = object.keys();
                while (object2.hasNext()) {
                    String string2 = (String)object2.next();
                    if (!jSONObject.has(string2)) {
                        return false;
                    }
                    try {
                        Preconditions.checkNotNull(string2);
                        boolean bl = JsonUtils.areJsonValuesEquivalent(object.get(string2), jSONObject.get(string2));
                        if (bl) continue;
                        return false;
                    }
                    catch (JSONException jSONException) {
                        return false;
                    }
                }
                return true;
            }
            if (object instanceof JSONArray && object2 instanceof JSONArray) {
                object = (JSONArray)object;
                object2 = (JSONArray)object2;
                if (object.length() == object2.length()) {
                    for (int i = 0; i < object.length(); ++i) {
                        try {
                            boolean bl = JsonUtils.areJsonValuesEquivalent(object.get(i), object2.get(i));
                            if (bl) {
                                continue;
                            }
                            return false;
                        }
                        catch (JSONException jSONException) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            }
            return object.equals(object2);
        }
        return false;
    }

    public static String escapeString(String string2) {
        CharSequence charSequence = string2;
        if (!TextUtils.isEmpty((CharSequence)string2)) {
            Matcher matcher = zzb.matcher(string2);
            CharSequence charSequence2 = null;
            block10: while (matcher.find()) {
                charSequence = charSequence2;
                if (charSequence2 == null) {
                    charSequence = new StringBuffer();
                }
                switch (matcher.group().charAt(0)) {
                    default: {
                        charSequence2 = charSequence;
                        continue block10;
                    }
                    case '\\': {
                        matcher.appendReplacement((StringBuffer)charSequence, "\\\\\\\\");
                        charSequence2 = charSequence;
                        continue block10;
                    }
                    case '/': {
                        matcher.appendReplacement((StringBuffer)charSequence, "\\\\/");
                        charSequence2 = charSequence;
                        continue block10;
                    }
                    case '\"': {
                        matcher.appendReplacement((StringBuffer)charSequence, "\\\\\\\"");
                        charSequence2 = charSequence;
                        continue block10;
                    }
                    case '\r': {
                        matcher.appendReplacement((StringBuffer)charSequence, "\\\\r");
                        charSequence2 = charSequence;
                        continue block10;
                    }
                    case '\f': {
                        matcher.appendReplacement((StringBuffer)charSequence, "\\\\f");
                        charSequence2 = charSequence;
                        continue block10;
                    }
                    case '\n': {
                        matcher.appendReplacement((StringBuffer)charSequence, "\\\\n");
                        charSequence2 = charSequence;
                        continue block10;
                    }
                    case '\t': {
                        matcher.appendReplacement((StringBuffer)charSequence, "\\\\t");
                        charSequence2 = charSequence;
                        continue block10;
                    }
                    case '\b': 
                }
                matcher.appendReplacement((StringBuffer)charSequence, "\\\\b");
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

    public static String unescapeString(String charSequence) {
        if (!TextUtils.isEmpty((CharSequence)charSequence)) {
            String string2 = zzc.zza(charSequence);
            Matcher matcher = zza.matcher(string2);
            CharSequence charSequence2 = null;
            block10: while (matcher.find()) {
                charSequence = charSequence2;
                if (charSequence2 == null) {
                    charSequence = new StringBuffer();
                }
                switch (matcher.group().charAt(1)) {
                    default: {
                        throw new IllegalStateException("Found an escaped character that should never be.");
                    }
                    case 't': {
                        matcher.appendReplacement((StringBuffer)charSequence, "\t");
                        charSequence2 = charSequence;
                        continue block10;
                    }
                    case 'r': {
                        matcher.appendReplacement((StringBuffer)charSequence, "\r");
                        charSequence2 = charSequence;
                        continue block10;
                    }
                    case 'n': {
                        matcher.appendReplacement((StringBuffer)charSequence, "\n");
                        charSequence2 = charSequence;
                        continue block10;
                    }
                    case 'f': {
                        matcher.appendReplacement((StringBuffer)charSequence, "\f");
                        charSequence2 = charSequence;
                        continue block10;
                    }
                    case 'b': {
                        matcher.appendReplacement((StringBuffer)charSequence, "\b");
                        charSequence2 = charSequence;
                        continue block10;
                    }
                    case '\\': {
                        matcher.appendReplacement((StringBuffer)charSequence, "\\\\");
                        charSequence2 = charSequence;
                        continue block10;
                    }
                    case '/': {
                        matcher.appendReplacement((StringBuffer)charSequence, "/");
                        charSequence2 = charSequence;
                        continue block10;
                    }
                    case '\"': 
                }
                matcher.appendReplacement((StringBuffer)charSequence, "\"");
                charSequence2 = charSequence;
            }
            if (charSequence2 == null) {
                return string2;
            }
            matcher.appendTail((StringBuffer)charSequence2);
            return charSequence2.toString();
        }
        return charSequence;
    }
}

