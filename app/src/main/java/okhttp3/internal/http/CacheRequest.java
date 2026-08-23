/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.http;

import java.io.IOException;
import okio.Sink;

public interface CacheRequest {
    public void abort();

    public Sink body() throws IOException;
}

