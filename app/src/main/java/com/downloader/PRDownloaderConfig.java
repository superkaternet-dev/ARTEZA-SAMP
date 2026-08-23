/*
 * Decompiled with CFR 0.152.
 */
package com.downloader;

import com.downloader.httpclient.DefaultHttpClient;
import com.downloader.httpclient.HttpClient;

public class PRDownloaderConfig {
    private int connectTimeout;
    private boolean databaseEnabled;
    private HttpClient httpClient;
    private int readTimeout;
    private String userAgent;

    private PRDownloaderConfig(Builder builder) {
        this.readTimeout = builder.readTimeout;
        this.connectTimeout = builder.connectTimeout;
        this.userAgent = builder.userAgent;
        this.httpClient = builder.httpClient;
        this.databaseEnabled = builder.databaseEnabled;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public HttpClient getHttpClient() {
        return this.httpClient;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public boolean isDatabaseEnabled() {
        return this.databaseEnabled;
    }

    public void setConnectTimeout(int n) {
        this.connectTimeout = n;
    }

    public void setDatabaseEnabled(boolean bl) {
        this.databaseEnabled = bl;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void setReadTimeout(int n) {
        this.readTimeout = n;
    }

    public void setUserAgent(String string2) {
        this.userAgent = string2;
    }

    public static class Builder {
        int connectTimeout = 20000;
        boolean databaseEnabled = false;
        HttpClient httpClient = new DefaultHttpClient();
        int readTimeout = 20000;
        String userAgent = "PRDownloader";

        public PRDownloaderConfig build() {
            return new PRDownloaderConfig(this);
        }

        public Builder setConnectTimeout(int n) {
            this.connectTimeout = n;
            return this;
        }

        public Builder setDatabaseEnabled(boolean bl) {
            this.databaseEnabled = bl;
            return this;
        }

        public Builder setHttpClient(HttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        public Builder setReadTimeout(int n) {
            this.readTimeout = n;
            return this;
        }

        public Builder setUserAgent(String string2) {
            this.userAgent = string2;
            return this;
        }
    }
}

