/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 */
package com.liulishuo.filedownloader.services;

import android.text.TextUtils;
import com.liulishuo.filedownloader.IThreadPoolMonitor;
import com.liulishuo.filedownloader.PauseAllMarker;
import com.liulishuo.filedownloader.database.FileDownloadDatabase;
import com.liulishuo.filedownloader.download.CustomComponentHolder;
import com.liulishuo.filedownloader.download.DownloadLaunchRunnable;
import com.liulishuo.filedownloader.model.ConnectionModel;
import com.liulishuo.filedownloader.model.FileDownloadHeader;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.services.FileDownloadThreadPool;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.util.List;

class FileDownloadManager
implements IThreadPoolMonitor {
    private final FileDownloadDatabase mDatabase;
    private final FileDownloadThreadPool mThreadPool;

    FileDownloadManager() {
        CustomComponentHolder customComponentHolder = CustomComponentHolder.getImpl();
        this.mDatabase = customComponentHolder.getDatabaseInstance();
        this.mThreadPool = new FileDownloadThreadPool(customComponentHolder.getMaxNetworkThreadCount());
    }

    public void clearAllTaskData() {
        this.mDatabase.clear();
    }

    public boolean clearTaskData(int n) {
        if (n == 0) {
            FileDownloadLog.w(this, "The task[%d] id is invalid, can't clear it.", n);
            return false;
        }
        if (this.isDownloading(n)) {
            FileDownloadLog.w(this, "The task[%d] is downloading, can't clear it.", n);
            return false;
        }
        this.mDatabase.remove(n);
        this.mDatabase.removeConnections(n);
        return true;
    }

    @Override
    public int findRunningTaskIdBySameTempPath(String string2, int n) {
        return this.mThreadPool.findRunningTaskIdBySameTempPath(string2, n);
    }

    public long getSoFar(int n) {
        List<ConnectionModel> list = this.mDatabase.find(n);
        if (list == null) {
            return 0L;
        }
        int n2 = ((FileDownloadModel)((Object)list)).getConnectionCount();
        if (n2 <= 1) {
            return ((FileDownloadModel)((Object)list)).getSoFar();
        }
        list = this.mDatabase.findConnectionModel(n);
        if (list != null && list.size() == n2) {
            return ConnectionModel.getTotalOffset(list);
        }
        return 0L;
    }

    public byte getStatus(int n) {
        FileDownloadModel fileDownloadModel = this.mDatabase.find(n);
        if (fileDownloadModel == null) {
            return 0;
        }
        return fileDownloadModel.getStatus();
    }

    public long getTotal(int n) {
        FileDownloadModel fileDownloadModel = this.mDatabase.find(n);
        if (fileDownloadModel == null) {
            return 0L;
        }
        return fileDownloadModel.getTotal();
    }

    public boolean isDownloading(int n) {
        return this.isDownloading(this.mDatabase.find(n));
    }

    @Override
    public boolean isDownloading(FileDownloadModel fileDownloadModel) {
        if (fileDownloadModel == null) {
            return false;
        }
        boolean bl = this.mThreadPool.isInThreadPool(fileDownloadModel.getId());
        if (FileDownloadStatus.isOver(fileDownloadModel.getStatus())) {
            bl = bl;
        } else if (bl) {
            bl = true;
        } else {
            FileDownloadLog.e((Object)this, "%d status is[%s](not finish) & but not in the pool", fileDownloadModel.getId(), fileDownloadModel.getStatus());
            bl = false;
        }
        return bl;
    }

    public boolean isDownloading(String string2, String string3) {
        return this.isDownloading(FileDownloadUtils.generateId(string2, string3));
    }

    public boolean isIdle() {
        boolean bl = this.mThreadPool.exactSize() <= 0;
        return bl;
    }

    public boolean pause(int n) {
        FileDownloadModel fileDownloadModel;
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "request pause the task %d", n);
        }
        if ((fileDownloadModel = this.mDatabase.find(n)) == null) {
            return false;
        }
        fileDownloadModel.setStatus((byte)-2);
        this.mThreadPool.cancel(n);
        return true;
    }

    public void pauseAll() {
        Object object = this.mThreadPool.getAllExactRunningDownloadIds();
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "pause all tasks %d", object.size());
        }
        object = object.iterator();
        while (object.hasNext()) {
            this.pause((Integer)object.next());
        }
    }

    public boolean setMaxNetworkThreadCount(int n) {
        synchronized (this) {
            boolean bl = this.mThreadPool.setMaxNetworkThreadCount(n);
            return bl;
        }
    }

    /*
     * Unable to fully structure code
     */
    public void start(String var1_1, String var2_3, boolean var3_4, int var4_5, int var5_6, int var6_7, boolean var7_8, FileDownloadHeader var8_9, boolean var9_10) {
        synchronized (this) {
            block33: {
                block30: {
                    block31: {
                        block32: {
                            block29: {
                                block35: {
                                    block28: {
                                        block27: {
                                            block34: {
                                                block26: {
                                                    block25: {
                                                        if (FileDownloadLog.NEED_LOG) {
                                                            FileDownloadLog.d(this, "request start the task with url(%s) path(%s) isDirectory(%B)", new Object[]{var1_1, var2_3, var3_4});
                                                        }
                                                        PauseAllMarker.clearMarker();
                                                        var10_11 = FileDownloadUtils.generateId((String)var1_1, (String)var2_3, var3_4);
                                                        var14_13 = var15_12 = this.mDatabase.find(var10_11);
                                                        if (var3_4) break block25;
                                                        var14_13 = var15_12;
                                                        if (var15_12 != null) break block25;
                                                        var11_14 = FileDownloadUtils.generateId((String)var1_1, FileDownloadUtils.getParent((String)var2_3), true);
                                                        var14_13 = var15_12 = this.mDatabase.find(var11_14);
                                                        if (var15_12 == null) break block25;
                                                        var14_13 = var15_12;
                                                        if (!var2_3.equals(var15_12.getTargetFilePath())) break block25;
                                                        if (FileDownloadLog.NEED_LOG) {
                                                            FileDownloadLog.d(this, "task[%d] find model by dirCaseId[%d]", new Object[]{var10_11, var11_14});
                                                        }
                                                        var16_15 = this.mDatabase.findConnectionModel(var11_14);
                                                        var14_13 = var15_12;
                                                        var15_12 = var16_15;
                                                        break block26;
                                                    }
                                                    var15_12 = null;
                                                }
                                                if (FileDownloadHelper.inspectAndInflowDownloading(var10_11, (FileDownloadModel)var14_13, this, true)) {
                                                    if (FileDownloadLog.NEED_LOG) {
                                                        FileDownloadLog.d(this, "has already started download %d", new Object[]{var10_11});
                                                    }
                                                    return;
                                                }
                                                if (var14_13 == null) ** GOTO lbl36
                                                var16_15 = var14_13.getTargetFilePath();
                                                break block34;
lbl36:
                                                // 1 sources

                                                var16_15 = FileDownloadUtils.getTargetFilePath((String)var2_3, var3_4, null);
                                            }
                                            if (FileDownloadHelper.inspectAndInflowDownloaded(var10_11, (String)var16_15, var7_8, true)) {
                                                if (FileDownloadLog.NEED_LOG) {
                                                    FileDownloadLog.d(this, "has already completed downloading %d", new Object[]{var10_11});
                                                }
                                                return;
                                            }
                                            if (var14_13 == null) break block27;
                                            var12_16 = var14_13.getSoFar();
                                            break block28;
                                        }
                                        var12_16 = 0L;
                                    }
                                    if (var14_13 == null) ** GOTO lbl53
                                    var17_17 = var14_13.getTempFilePath();
                                    break block35;
lbl53:
                                    // 1 sources

                                    var17_17 = FileDownloadUtils.getTempPath((String)var16_15);
                                }
                                if (!FileDownloadHelper.inspectAndInflowConflictPath(var10_11, var12_16, var17_17, (String)var16_15, this)) break block29;
                                if (FileDownloadLog.NEED_LOG) {
                                    FileDownloadLog.d(this, "there is an another task with the same target-file-path %d %s", new Object[]{var10_11, var16_15});
                                }
                                if (var14_13 == null) ** GOTO lbl63
                                this.mDatabase.remove(var10_11);
                                this.mDatabase.removeConnections(var10_11);
lbl63:
                                // 2 sources

                                return;
                            }
                            if (var14_13 == null) break block30;
                            if (var14_13.getStatus() != -2 && var14_13.getStatus() != -1 && var14_13.getStatus() != 1 && var14_13.getStatus() != 6 && var14_13.getStatus() != 2) break block30;
                            if (var14_13.getId() == var10_11) break block31;
                            this.mDatabase.remove(var14_13.getId());
                            this.mDatabase.removeConnections(var14_13.getId());
                            var14_13.setId(var10_11);
                            var14_13.setPath((String)var2_3, var3_4);
                            if (var15_12 == null) break block32;
                            try {
                                var2_3 = var15_12.iterator();
                                while (var2_3.hasNext()) {
                                    var1_1 = (ConnectionModel)var2_3.next();
                                    var1_1.setId(var10_11);
                                    this.mDatabase.insertConnectionModel((ConnectionModel)var1_1);
                                }
                            }
                            catch (Throwable var1_2) {}
                            {
                                throw var1_2;
                            }
                        }
                        var10_11 = 1;
                        break block33;
                    }
                    if (!TextUtils.equals((CharSequence)var1_1, (CharSequence)var14_13.getUrl())) {
                        var14_13.setUrl((String)var1_1);
                        var10_11 = 1;
                    } else {
                        var10_11 = 0;
                    }
                    break block33;
                }
                var15_12 = var14_13;
                if (var14_13 != null) ** GOTO lbl101
                var15_12 = new FileDownloadModel();
lbl101:
                // 2 sources

                var15_12.setUrl((String)var1_1);
                var15_12.setPath((String)var2_3, var3_4);
                var15_12.setId(var10_11);
                var15_12.setSoFar(0L);
                var15_12.setTotal(0L);
                var15_12.setStatus((byte)1);
                var15_12.setConnectionCount(1);
                var10_11 = 1;
                var14_13 = var15_12;
            }
            if (var10_11 == 0) ** GOTO lbl114
            this.mDatabase.update((FileDownloadModel)var14_13);
lbl114:
            // 2 sources

            var1_1 = new DownloadLaunchRunnable.Builder();
            var1_1 = var1_1.setModel((FileDownloadModel)var14_13).setHeader(var8_9).setThreadPoolMonitor(this).setMinIntervalMillis(var5_6).setCallbackProgressMaxCount(var4_5).setForceReDownload(var7_8).setWifiRequired(var9_10).setMaxRetryTimes(var6_7).build();
            this.mThreadPool.execute((DownloadLaunchRunnable)var1_1);
            return;
        }
    }
}

