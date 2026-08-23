/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.http;

public final class HttpMethod {
    private HttpMethod() {
    }

    public static boolean invalidatesCache(String string2) {
        boolean bl = string2.equals("POST") || string2.equals("PATCH") || string2.equals("PUT") || string2.equals("DELETE") || string2.equals("MOVE");
        return bl;
    }

    public static boolean permitsRequestBody(String string2) {
        boolean bl = HttpMethod.requiresRequestBody(string2) || string2.equals("OPTIONS") || string2.equals("DELETE") || string2.equals("PROPFIND") || string2.equals("MKCOL") || string2.equals("LOCK");
        return bl;
    }

    public static boolean redirectsToGet(String string2) {
        return string2.equals("PROPFIND") ^ true;
    }

    public static boolean requiresRequestBody(String string2) {
        boolean bl = string2.equals("POST") || string2.equals("PUT") || string2.equals("PATCH") || string2.equals("PROPPATCH") || string2.equals("REPORT");
        return bl;
    }
}

