/*
 * Decompiled with CFR 0.152.
 */
package com.downloader.database;

import com.downloader.database.DbHelper;
import com.downloader.database.DownloadModel;
import java.util.List;

public class NoOpsDbHelper
implements DbHelper {
    @Override
    public void clear() {
    }

    @Override
    public DownloadModel find(int n) {
        return null;
    }

    @Override
    public List<DownloadModel> getUnwantedModels(int n) {
        return null;
    }

    @Override
    public void insert(DownloadModel downloadModel) {
    }

    @Override
    public void remove(int n) {
    }

    @Override
    public void update(DownloadModel downloadModel) {
    }

    @Override
    public void updateProgress(int n, long l, long l2) {
    }
}

