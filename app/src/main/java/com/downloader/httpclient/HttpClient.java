/*
 * Decompiled with CFR 0.152.
 */
package com.downloader.httpclient;

import com.downloader.request.DownloadRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface HttpClient
extends Cloneable {
    public HttpClient clone();

    public void close();

    public void connect(DownloadRequest var1) throws IOException;

    public long getContentLength();

    public InputStream getErrorStream() throws IOException;

    public Map<String, List<String>> getHeaderFields();

    public InputStream getInputStream() throws IOException;

    public int getResponseCode() throws IOException;

    public String getResponseHeader(String var1);
}

