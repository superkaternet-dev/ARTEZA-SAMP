/*
 * Decompiled with CFR 0.152.
 */
package com.downloader;

import com.downloader.Error;

public interface OnDownloadListener {
    public void onDownloadComplete();

    public void onError(Error var1);
}

