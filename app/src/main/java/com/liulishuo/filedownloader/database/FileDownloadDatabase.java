/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.database;

import com.liulishuo.filedownloader.model.ConnectionModel;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import java.util.List;

public interface FileDownloadDatabase {
    public void clear();

    public FileDownloadModel find(int var1);

    public List<ConnectionModel> findConnectionModel(int var1);

    public void insert(FileDownloadModel var1);

    public void insertConnectionModel(ConnectionModel var1);

    public Maintainer maintainer();

    public void onTaskStart(int var1);

    public boolean remove(int var1);

    public void removeConnections(int var1);

    public void update(FileDownloadModel var1);

    public void updateCompleted(int var1, long var2);

    public void updateConnected(int var1, long var2, String var4, String var5);

    public void updateConnectionCount(int var1, int var2);

    public void updateConnectionModel(int var1, int var2, long var3);

    public void updateError(int var1, Throwable var2, long var3);

    public void updateOldEtagOverdue(int var1, String var2, long var3, long var5, int var7);

    public void updatePause(int var1, long var2);

    public void updatePending(int var1);

    public void updateProgress(int var1, long var2);

    public void updateRetry(int var1, Throwable var2);

    public static interface Maintainer
    extends Iterable<FileDownloadModel> {
        public void changeFileDownloadModelId(int var1, FileDownloadModel var2);

        public void onFinishMaintain();

        public void onRefreshedValidData(FileDownloadModel var1);

        public void onRemovedInvalidData(FileDownloadModel var1);
    }
}

