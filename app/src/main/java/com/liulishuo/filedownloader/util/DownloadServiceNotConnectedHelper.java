/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 */
package com.liulishuo.filedownloader.util;

import android.app.Notification;
import com.liulishuo.filedownloader.util.FileDownloadLog;

public class DownloadServiceNotConnectedHelper {
    private static final String CAUSE = ", but the download service isn't connected yet.";
    private static final String TIPS = "\nYou can use FileDownloader#isServiceConnected() to check whether the service has been connected, \nbesides you can use following functions easier to control your code invoke after the service has been connected: \n1. FileDownloader#bindService(Runnable)\n2. FileDownloader#insureServiceBind()\n3. FileDownloader#insureServiceBindAsync()";

    public static boolean clearAllTaskData() {
        DownloadServiceNotConnectedHelper.log("request clear all tasks data in the database", new Object[0]);
        return false;
    }

    public static boolean clearTaskData(int n) {
        DownloadServiceNotConnectedHelper.log("request clear the task[%d] data in the database", n);
        return false;
    }

    public static long getSofar(int n) {
        DownloadServiceNotConnectedHelper.log("request get the downloaded so far byte for the task[%d] in the download service", n);
        return 0L;
    }

    public static byte getStatus(int n) {
        DownloadServiceNotConnectedHelper.log("request get the status for the task[%d] in the download service", n);
        return 0;
    }

    public static long getTotal(int n) {
        DownloadServiceNotConnectedHelper.log("request get the total byte for the task[%d] in the download service", n);
        return 0L;
    }

    public static boolean isDownloading(String string2, String string3) {
        DownloadServiceNotConnectedHelper.log("request check the task([%s], [%s]) is downloading in the download service", string2, string3);
        return false;
    }

    public static boolean isIdle() {
        DownloadServiceNotConnectedHelper.log("request check the download service is idle", new Object[0]);
        return true;
    }

    private static void log(String string2, Object ... objectArray) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append(CAUSE);
        stringBuilder.append(TIPS);
        FileDownloadLog.w(DownloadServiceNotConnectedHelper.class, stringBuilder.toString(), objectArray);
    }

    public static boolean pause(int n) {
        DownloadServiceNotConnectedHelper.log("request pause the task[%d] in the download service", n);
        return false;
    }

    public static void pauseAllTasks() {
        DownloadServiceNotConnectedHelper.log("request pause all tasks in the download service", new Object[0]);
    }

    public static boolean setMaxNetworkThreadCount(int n) {
        DownloadServiceNotConnectedHelper.log("request set the max network thread count[%d] in the download service", n);
        return false;
    }

    public static boolean start(String string2, String string3, boolean bl) {
        DownloadServiceNotConnectedHelper.log("request start the task([%s], [%s], [%B]) in the download service", string2, string3, bl);
        return false;
    }

    public static void startForeground(int n, Notification notification) {
        DownloadServiceNotConnectedHelper.log("request set the download service as the foreground service([%d],[%s]),", n, notification);
    }

    public static void stopForeground(boolean bl) {
        DownloadServiceNotConnectedHelper.log("request cancel the foreground status[%B] for the download service", bl);
    }
}

