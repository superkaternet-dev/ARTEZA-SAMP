/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.http;

import java.net.Proxy;
import okhttp3.HttpUrl;
import okhttp3.Request;

public final class RequestLine {
    private RequestLine() {
    }

    static String get(Request request, Proxy.Type type) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(request.method());
        stringBuilder.append(' ');
        if (RequestLine.includeAuthorityInRequestLine(request, type)) {
            stringBuilder.append(request.url());
        } else {
            stringBuilder.append(RequestLine.requestPath(request.url()));
        }
        stringBuilder.append(" HTTP/1.1");
        return stringBuilder.toString();
    }

    private static boolean includeAuthorityInRequestLine(Request request, Proxy.Type type) {
        boolean bl = !request.isHttps() && type == Proxy.Type.HTTP;
        return bl;
    }

    public static String requestPath(HttpUrl object) {
        String string2 = ((HttpUrl)object).encodedPath();
        if ((object = ((HttpUrl)object).encodedQuery()) != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append('?');
            stringBuilder.append((String)object);
            object = stringBuilder.toString();
        } else {
            object = string2;
        }
        return object;
    }
}

