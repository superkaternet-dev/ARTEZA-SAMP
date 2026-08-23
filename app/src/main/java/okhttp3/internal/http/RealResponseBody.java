/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.http;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okhttp3.internal.http.OkHeaders;
import okio.BufferedSource;

public final class RealResponseBody
extends ResponseBody {
    private final Headers headers;
    private final BufferedSource source;

    public RealResponseBody(Headers headers, BufferedSource bufferedSource) {
        this.headers = headers;
        this.source = bufferedSource;
    }

    @Override
    public long contentLength() {
        return OkHeaders.contentLength(this.headers);
    }

    @Override
    public MediaType contentType() {
        Object object = this.headers.get("Content-Type");
        object = object != null ? MediaType.parse((String)object) : null;
        return object;
    }

    @Override
    public BufferedSource source() {
        return this.source;
    }
}

