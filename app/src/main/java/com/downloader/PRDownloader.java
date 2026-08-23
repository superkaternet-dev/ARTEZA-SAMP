/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.downloader;

import android.content.Context;
import com.downloader.PRDownloaderConfig;
import com.downloader.Status;
import com.downloader.core.Core;
import com.downloader.internal.ComponentHolder;
import com.downloader.internal.DownloadRequestQueue;
import com.downloader.request.DownloadRequestBuilder;
import com.downloader.utils.Utils;

public class PRDownloader {
    private PRDownloader() {
    }

    public static void cancel(int n) {
        DownloadRequestQueue.getInstance().cancel(n);
    }

    public static void cancel(Object object) {
        DownloadRequestQueue.getInstance().cancel(object);
    }

    public static void cancelAll() {
        DownloadRequestQueue.getInstance().cancelAll();
    }

    public static void cleanUp(int n) {
        Utils.deleteUnwantedModelsAndTempFiles(n);
    }

    public static DownloadRequestBuilder download(String string2, String string3, String string4) {
        return new DownloadRequestBuilder(string2, string3, string4);
    }

    public static Status getStatus(int n) {
        return DownloadRequestQueue.getInstance().getStatus(n);
    }

    public static void initialize(Context context) {
        PRDownloader.initialize(context, PRDownloaderConfig.newBuilder().build());
    }

    public static void initialize(Context context, PRDownloaderConfig pRDownloaderConfig) {
        ComponentHolder.getInstance().init(context, pRDownloaderConfig);
        DownloadRequestQueue.initialize();
    }

    public static void pause(int n) {
        DownloadRequestQueue.getInstance().pause(n);
    }

    public static void resume(int n) {
        DownloadRequestQueue.getInstance().resume(n);
    }

    public static void shutDown() {
        Core.shutDown();
    }
}

