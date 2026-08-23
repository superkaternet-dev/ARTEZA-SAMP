/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.downloader.internal;

import android.content.Context;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.database.AppDbHelper;
import com.downloader.database.DbHelper;
import com.downloader.database.NoOpsDbHelper;
import com.downloader.httpclient.DefaultHttpClient;
import com.downloader.httpclient.HttpClient;

public class ComponentHolder {
    private static final ComponentHolder INSTANCE = new ComponentHolder();
    private int connectTimeout;
    private DbHelper dbHelper;
    private HttpClient httpClient;
    private int readTimeout;
    private String userAgent;

    public static ComponentHolder getInstance() {
        return INSTANCE;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getConnectTimeout() {
        if (this.connectTimeout != 0) return this.connectTimeout;
        synchronized (ComponentHolder.class) {
            if (this.connectTimeout != 0) return this.connectTimeout;
            this.connectTimeout = 20000;
            return this.connectTimeout;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public DbHelper getDbHelper() {
        if (this.dbHelper != null) return this.dbHelper;
        synchronized (ComponentHolder.class) {
            if (this.dbHelper != null) return this.dbHelper;
            NoOpsDbHelper noOpsDbHelper = new NoOpsDbHelper();
            this.dbHelper = noOpsDbHelper;
            return this.dbHelper;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public HttpClient getHttpClient() {
        if (this.httpClient != null) return this.httpClient.clone();
        synchronized (ComponentHolder.class) {
            if (this.httpClient != null) return this.httpClient.clone();
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            this.httpClient = defaultHttpClient;
            return this.httpClient.clone();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getReadTimeout() {
        if (this.readTimeout != 0) return this.readTimeout;
        synchronized (ComponentHolder.class) {
            if (this.readTimeout != 0) return this.readTimeout;
            this.readTimeout = 20000;
            return this.readTimeout;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String getUserAgent() {
        if (this.userAgent != null) return this.userAgent;
        synchronized (ComponentHolder.class) {
            if (this.userAgent != null) return this.userAgent;
            this.userAgent = "PRDownloader";
            return this.userAgent;
        }
    }

    public void init(Context object, PRDownloaderConfig pRDownloaderConfig) {
        this.readTimeout = pRDownloaderConfig.getReadTimeout();
        this.connectTimeout = pRDownloaderConfig.getConnectTimeout();
        this.userAgent = pRDownloaderConfig.getUserAgent();
        this.httpClient = pRDownloaderConfig.getHttpClient();
        object = pRDownloaderConfig.isDatabaseEnabled() ? new AppDbHelper((Context)object) : new NoOpsDbHelper();
        this.dbHelper = object;
        if (pRDownloaderConfig.isDatabaseEnabled()) {
            PRDownloader.cleanUp(30);
        }
    }
}

