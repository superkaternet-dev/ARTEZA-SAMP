/*
 * Decompiled with CFR 0.152.
 */
package com.downloader.database;

import com.downloader.database.DownloadModel;
import java.util.List;

public interface DbHelper {
    public void clear();

    public DownloadModel find(int var1);

    public List<DownloadModel> getUnwantedModels(int var1);

    public void insert(DownloadModel var1);

    public void remove(int var1);

    public void update(DownloadModel var1);

    public void updateProgress(int var1, long var2, long var4);
}

