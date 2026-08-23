/*
 * Decompiled with CFR 0.152.
 */
package com.downloader.httpclient;

import com.downloader.httpclient.HttpClient;
import com.downloader.request.DownloadRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DefaultHttpClient
implements HttpClient {
    private URLConnection connection;

    private void addHeaders(DownloadRequest iterator2) {
        if ((iterator2 = ((DownloadRequest)((Object)iterator2)).getHeaders()) != null) {
            for (Map.Entry entry : ((HashMap)((Object)iterator2)).entrySet()) {
                String string2 = (String)entry.getKey();
                List list = (List)entry.getValue();
                if (list == null) continue;
                for (String string3 : list) {
                    this.connection.addRequestProperty(string2, string3);
                }
            }
        }
    }

    @Override
    public HttpClient clone() {
        return new DefaultHttpClient();
    }

    @Override
    public void close() {
    }

    @Override
    public void connect(DownloadRequest downloadRequest) throws IOException {
        Object object = new URL(downloadRequest.getUrl()).openConnection();
        this.connection = object;
        ((URLConnection)object).setReadTimeout(downloadRequest.getReadTimeout());
        this.connection.setConnectTimeout(downloadRequest.getConnectTimeout());
        object = String.format(Locale.ENGLISH, "bytes=%d-", downloadRequest.getDownloadedBytes());
        this.connection.addRequestProperty("Range", (String)object);
        this.connection.addRequestProperty("User-Agent", downloadRequest.getUserAgent());
        this.addHeaders(downloadRequest);
        this.connection.connect();
    }

    @Override
    public long getContentLength() {
        String string2 = this.connection.getHeaderField("Content-Length");
        try {
            long l = Long.parseLong(string2);
            return l;
        }
        catch (NumberFormatException numberFormatException) {
            return -1L;
        }
    }

    @Override
    public InputStream getErrorStream() {
        URLConnection uRLConnection = this.connection;
        if (uRLConnection instanceof HttpURLConnection) {
            return ((HttpURLConnection)uRLConnection).getErrorStream();
        }
        return null;
    }

    @Override
    public Map<String, List<String>> getHeaderFields() {
        return this.connection.getHeaderFields();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return this.connection.getInputStream();
    }

    @Override
    public int getResponseCode() throws IOException {
        int n = 0;
        URLConnection uRLConnection = this.connection;
        if (uRLConnection instanceof HttpURLConnection) {
            n = ((HttpURLConnection)uRLConnection).getResponseCode();
        }
        return n;
    }

    @Override
    public String getResponseHeader(String string2) {
        return this.connection.getHeaderField(string2);
    }
}

