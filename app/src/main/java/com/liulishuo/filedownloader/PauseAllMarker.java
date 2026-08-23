/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Handler
 *  android.os.Handler$Callback
 *  android.os.HandlerThread
 *  android.os.Message
 *  android.os.RemoteException
 */
package com.liulishuo.filedownloader;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.RemoteException;
import com.liulishuo.filedownloader.i.IFileDownloadIPCService;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import java.io.File;
import java.io.IOException;

public class PauseAllMarker
implements Handler.Callback {
    private static final String MAKER_FILE_NAME = ".filedownloader_pause_all_marker.b";
    private static final Long PAUSE_ALL_CHECKER_PERIOD = 1000L;
    private static final int PAUSE_ALL_CHECKER_WHAT = 0;
    private static File markerFile;
    private HandlerThread pauseAllChecker;
    private Handler pauseAllHandler;
    private final IFileDownloadIPCService serviceHandler;

    public PauseAllMarker(IFileDownloadIPCService iFileDownloadIPCService) {
        this.serviceHandler = iFileDownloadIPCService;
    }

    public static void clearMarker() {
        File file = PauseAllMarker.markerFile();
        if (file.exists()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("delete marker file ");
            stringBuilder.append(file.delete());
            FileDownloadLog.d(PauseAllMarker.class, stringBuilder.toString(), new Object[0]);
        }
    }

    public static void createMarker() {
        File file = PauseAllMarker.markerFile();
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.exists()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("marker file ");
            stringBuilder.append(file.getAbsolutePath());
            stringBuilder.append(" exists");
            FileDownloadLog.w(PauseAllMarker.class, stringBuilder.toString(), new Object[0]);
            return;
        }
        try {
            boolean bl = file.createNewFile();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("create marker file");
            stringBuilder.append(file.getAbsolutePath());
            stringBuilder.append(" ");
            stringBuilder.append(bl);
            FileDownloadLog.d(PauseAllMarker.class, stringBuilder.toString(), new Object[0]);
        }
        catch (IOException iOException) {
            FileDownloadLog.e(PauseAllMarker.class, "create marker file failed", iOException);
        }
    }

    private static boolean isMarked() {
        return PauseAllMarker.markerFile().exists();
    }

    private static File markerFile() {
        if (markerFile == null) {
            Context context = FileDownloadHelper.getAppContext();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(context.getCacheDir());
            stringBuilder.append(File.separator);
            stringBuilder.append(MAKER_FILE_NAME);
            markerFile = new File(stringBuilder.toString());
        }
        return markerFile;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean handleMessage(Message message) {
        block6: {
            if (PauseAllMarker.isMarked()) {
                Throwable throwable2;
                block5: {
                    this.serviceHandler.pauseAllTasks();
                    {
                        catch (Throwable throwable2) {
                            break block5;
                        }
                        catch (RemoteException remoteException) {}
                        {
                            FileDownloadLog.e((Object)this, remoteException, "pause all failed", new Object[0]);
                        }
                    }
                    PauseAllMarker.clearMarker();
                    break block6;
                }
                PauseAllMarker.clearMarker();
                throw throwable2;
            }
        }
        this.pauseAllHandler.sendEmptyMessageDelayed(0, PAUSE_ALL_CHECKER_PERIOD.longValue());
        return true;
    }

    public void startPauseAllLooperCheck() {
        HandlerThread handlerThread;
        this.pauseAllChecker = handlerThread = new HandlerThread("PauseAllChecker");
        handlerThread.start();
        handlerThread = new Handler(this.pauseAllChecker.getLooper(), (Handler.Callback)this);
        this.pauseAllHandler = handlerThread;
        handlerThread.sendEmptyMessageDelayed(0, PAUSE_ALL_CHECKER_PERIOD.longValue());
    }

    public void stopPauseAllLooperCheck() {
        this.pauseAllHandler.removeMessages(0);
        this.pauseAllChecker.quit();
    }
}

