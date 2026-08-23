/*
 * Decompiled with CFR 0.152.
 */
package com.downloader.request;

import com.downloader.Priority;

public interface RequestBuilder {
    public RequestBuilder setConnectTimeout(int var1);

    public RequestBuilder setHeader(String var1, String var2);

    public RequestBuilder setPriority(Priority var1);

    public RequestBuilder setReadTimeout(int var1);

    public RequestBuilder setTag(Object var1);

    public RequestBuilder setUserAgent(String var1);
}

