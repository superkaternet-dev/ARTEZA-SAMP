/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.NotificationChannel
 *  android.app.NotificationManager
 *  android.app.Service
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Build$VERSION
 *  android.os.IBinder
 */
package com.liulishuo.filedownloader.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import com.liulishuo.filedownloader.PauseAllMarker;
import com.liulishuo.filedownloader.download.CustomComponentHolder;
import com.liulishuo.filedownloader.i.IFileDownloadIPCService;
import com.liulishuo.filedownloader.services.FDServiceSeparateHandler;
import com.liulishuo.filedownloader.services.FDServiceSharedHandler;
import com.liulishuo.filedownloader.services.FileDownloadManager;
import com.liulishuo.filedownloader.services.ForegroundServiceConfig;
import com.liulishuo.filedownloader.services.IFileDownloadServiceHandler;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadProperties;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.lang.ref.WeakReference;

public class FileDownloadService
extends Service {
    private IFileDownloadServiceHandler handler;
    private PauseAllMarker pauseAllMarker;

    private void inspectRunServiceForeground(Intent object) {
        if (object == null) {
            return;
        }
        if (object.getBooleanExtra("is_foreground", false)) {
            object = CustomComponentHolder.getImpl().getForegroundConfigInstance();
            if (((ForegroundServiceConfig)object).isNeedRecreateChannelId() && Build.VERSION.SDK_INT >= 26) {
                NotificationChannel notificationChannel = new NotificationChannel(((ForegroundServiceConfig)object).getNotificationChannelId(), (CharSequence)((ForegroundServiceConfig)object).getNotificationChannelName(), 2);
                NotificationManager notificationManager = (NotificationManager)this.getSystemService("notification");
                if (notificationManager == null) {
                    return;
                }
                notificationManager.createNotificationChannel(notificationChannel);
            }
            this.startForeground(((ForegroundServiceConfig)object).getNotificationId(), ((ForegroundServiceConfig)object).getNotification((Context)this));
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d((Object)this, "run service foreground with config: %s", object);
            }
        }
    }

    public IBinder onBind(Intent intent) {
        return this.handler.onBind(intent);
    }

    public void onCreate() {
        super.onCreate();
        FileDownloadHelper.holdContext((Context)this);
        try {
            FileDownloadUtils.setMinProgressStep(FileDownloadProperties.getImpl().downloadMinProgressStep);
            FileDownloadUtils.setMinProgressTime(FileDownloadProperties.getImpl().downloadMinProgressTime);
        }
        catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
        }
        Object object = new FileDownloadManager();
        this.handler = FileDownloadProperties.getImpl().processNonSeparate ? new FDServiceSharedHandler(new WeakReference<FileDownloadService>(this), (FileDownloadManager)object) : new FDServiceSeparateHandler(new WeakReference<FileDownloadService>(this), (FileDownloadManager)object);
        PauseAllMarker.clearMarker();
        this.pauseAllMarker = object = new PauseAllMarker((IFileDownloadIPCService)((Object)this.handler));
        ((PauseAllMarker)object).startPauseAllLooperCheck();
    }

    public void onDestroy() {
        this.pauseAllMarker.stopPauseAllLooperCheck();
        this.stopForeground(true);
        super.onDestroy();
    }

    public int onStartCommand(Intent intent, int n, int n2) {
        this.handler.onStartCommand(intent, n, n2);
        this.inspectRunServiceForeground(intent);
        return 1;
    }

    public static class SeparateProcessService
    extends FileDownloadService {
    }

    public static class SharedMainProcessService
    extends FileDownloadService {
    }
}

