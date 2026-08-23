/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.http;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import okhttp3.Challenge;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Platform;
import okhttp3.internal.Util;
import okhttp3.internal.http.HeaderParser;

public final class OkHeaders {
    static final String PREFIX;
    public static final String RECEIVED_MILLIS;
    public static final String RESPONSE_SOURCE;
    public static final String SELECTED_PROTOCOL;
    public static final String SENT_MILLIS;

    static {
        String string2;
        PREFIX = string2 = Platform.get().getPrefix();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append("-Sent-Millis");
        SENT_MILLIS = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append("-Received-Millis");
        RECEIVED_MILLIS = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append("-Selected-Protocol");
        SELECTED_PROTOCOL = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append("-Response-Source");
        RESPONSE_SOURCE = stringBuilder.toString();
    }

    private OkHeaders() {
    }

    public static long contentLength(Headers headers) {
        return OkHeaders.stringToLong(headers.get("Content-Length"));
    }

    public static long contentLength(Request request) {
        return OkHeaders.contentLength(request.headers());
    }

    public static long contentLength(Response response) {
        return OkHeaders.contentLength(response.headers());
    }

    public static boolean hasVaryAll(Headers headers) {
        return OkHeaders.varyFields(headers).contains("*");
    }

    public static boolean hasVaryAll(Response response) {
        return OkHeaders.hasVaryAll(response.headers());
    }

    static boolean isEndToEnd(String string2) {
        boolean bl = !"Connection".equalsIgnoreCase(string2) && !"Keep-Alive".equalsIgnoreCase(string2) && !"Proxy-Authenticate".equalsIgnoreCase(string2) && !"Proxy-Authorization".equalsIgnoreCase(string2) && !"TE".equalsIgnoreCase(string2) && !"Trailers".equalsIgnoreCase(string2) && !"Transfer-Encoding".equalsIgnoreCase(string2) && !"Upgrade".equalsIgnoreCase(string2);
        return bl;
    }

    public static List<Challenge> parseChallenges(Headers headers, String string2) {
        ArrayList<Challenge> arrayList = new ArrayList<Challenge>();
        int n = headers.size();
        block0: for (int i = 0; i < n; ++i) {
            if (!string2.equalsIgnoreCase(headers.name(i))) continue;
            String string3 = headers.value(i);
            int n2 = 0;
            while (n2 < string3.length()) {
                int n3 = HeaderParser.skipUntil(string3, n2, " ");
                String string4 = string3.substring(n2, n3).trim();
                n2 = HeaderParser.skipWhitespace(string3, n3);
                if (!string3.regionMatches(true, n2, "realm=\"", 0, "realm=\"".length())) continue block0;
                n3 = n2 + "realm=\"".length();
                n2 = HeaderParser.skipUntil(string3, n3, "\"");
                String string5 = string3.substring(n3, n2);
                n2 = HeaderParser.skipWhitespace(string3, HeaderParser.skipUntil(string3, n2 + 1, ",") + 1);
                arrayList.add(new Challenge(string4, string5));
            }
        }
        return arrayList;
    }

    private static long stringToLong(String string2) {
        if (string2 == null) {
            return -1L;
        }
        try {
            long l = Long.parseLong(string2);
            return l;
        }
        catch (NumberFormatException numberFormatException) {
            return -1L;
        }
    }

    public static Set<String> varyFields(Headers headers) {
        Set<String> set = Collections.emptySet();
        int n = headers.size();
        block0: for (int i = 0; i < n; ++i) {
            if (!"Vary".equalsIgnoreCase(headers.name(i))) continue;
            String[] stringArray = headers.value(i);
            Set<String> set2 = set;
            if (set.isEmpty()) {
                set2 = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
            }
            stringArray = stringArray.split(",");
            int n2 = stringArray.length;
            int n3 = 0;
            while (true) {
                set = set2;
                if (n3 >= n2) continue block0;
                set2.add(stringArray[n3].trim());
                ++n3;
            }
        }
        return set;
    }

    private static Set<String> varyFields(Response response) {
        return OkHeaders.varyFields(response.headers());
    }

    public static Headers varyHeaders(Headers headers, Headers object) {
        Set<String> set = OkHeaders.varyFields((Headers)object);
        if (set.isEmpty()) {
            return new Headers.Builder().build();
        }
        object = new Headers.Builder();
        int n = headers.size();
        for (int i = 0; i < n; ++i) {
            String string2 = headers.name(i);
            if (!set.contains(string2)) continue;
            ((Headers.Builder)object).add(string2, headers.value(i));
        }
        return ((Headers.Builder)object).build();
    }

    public static Headers varyHeaders(Response response) {
        return OkHeaders.varyHeaders(response.networkResponse().request().headers(), response.headers());
    }

    /*
     * WARNING - void declaration
     */
    public static boolean varyMatches(Response object2, Headers headers, Request request) {
        for (String string2 : OkHeaders.varyFields((Response)object2)) {
            void var2_4;
            void var1_3;
            if (Util.equal(var1_3.values(string2), var2_4.headers(string2))) continue;
            return false;
        }
        return true;
    }
}

