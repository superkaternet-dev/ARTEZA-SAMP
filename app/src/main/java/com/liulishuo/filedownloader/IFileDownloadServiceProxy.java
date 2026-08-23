/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.content.Context
 */
package com.liulishuo.filedownloader;

import android.app.Notification;
import android.content.Context;
import com.liulishuo.filedownloader.model.FileDownloadHeader;

public interface IFileDownloadServiceProxy {
    public void bindStartByContext(Context var1);

    public void bindStartByContext(Context var1, Runnable var2);

    public void clearAllTaskData();

    public boolean clearTaskData(int var1);

    public long getSofar(int var1);

    public byte getStatus(int var1);

    public long getTotal(int var1);

    public boolean isConnected();

    public boolean isDownloading(String var1, String var2);

    public boolean isIdle();

    public boolean isRunServiceForeground();

    public boolean pause(int var1);

    public void pauseAllTasks();

    public boolean setMaxNetworkThreadCount(int var1);

    public boolean start(String var1, String var2, boolean var3, int var4, int var5, int var6, boolean var7, FileDownloadHeader var8, boolean var9);

    public void startForeground(int var1, Notification var2);

    public void stopForeground(boolean var1);

    public void unbindByContext(Context var1);
}

