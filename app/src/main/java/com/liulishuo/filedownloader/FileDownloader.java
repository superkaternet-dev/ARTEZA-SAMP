/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Application
 *  android.app.Notification
 *  android.content.Context
 */
package com.liulishuo.filedownloader;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.DownloadTask;
import com.liulishuo.filedownloader.FileDownloadConnectListener;
import com.liulishuo.filedownloader.FileDownloadEventPool;
import com.liulishuo.filedownloader.FileDownloadLine;
import com.liulishuo.filedownloader.FileDownloadLineAsync;
import com.liulishuo.filedownloader.FileDownloadList;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadMessageStation;
import com.liulishuo.filedownloader.FileDownloadServiceProxy;
import com.liulishuo.filedownloader.FileDownloadTaskLauncher;
import com.liulishuo.filedownloader.ILostServiceConnectedHandler;
import com.liulishuo.filedownloader.IQueuesHandler;
import com.liulishuo.filedownloader.LostServiceConnectedHandler;
import com.liulishuo.filedownloader.PauseAllMarker;
import com.liulishuo.filedownloader.QueuesHandler;
import com.liulishuo.filedownloader.download.CustomComponentHolder;
import com.liulishuo.filedownloader.model.FileDownloadTaskAtom;
import com.liulishuo.filedownloader.services.DownloadMgrInitialParams;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.io.File;
import java.util.Iterator;
import java.util.List;

public class FileDownloader {
    private static final Object INIT_LOST_CONNECTED_HANDLER_LOCK;
    private static final Object INIT_QUEUES_HANDLER_LOCK;
    private ILostServiceConnectedHandler mLostConnectedHandler;
    private IQueuesHandler mQueuesHandler;

    static {
        INIT_QUEUES_HANDLER_LOCK = new Object();
        INIT_LOST_CONNECTED_HANDLER_LOCK = new Object();
    }

    public static void disableAvoidDropFrame() {
        FileDownloader.setGlobalPost2UIInterval(-1);
    }

    public static void enableAvoidDropFrame() {
        FileDownloader.setGlobalPost2UIInterval(10);
    }

    public static FileDownloader getImpl() {
        return HolderClass.INSTANCE;
    }

    public static void init(Context context) {
        if (context != null) {
            FileDownloader.setup(context);
            return;
        }
        throw new IllegalArgumentException("the provided context must not be null!");
    }

    public static void init(Context context, DownloadMgrInitialParams.InitCustomMaker initCustomMaker) {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(FileDownloader.class, "init Downloader with params: %s %s", context, initCustomMaker);
        }
        if (context != null) {
            FileDownloadHelper.holdContext(context.getApplicationContext());
            CustomComponentHolder.getImpl().setInitCustomMaker(initCustomMaker);
            return;
        }
        throw new IllegalArgumentException("the provided context must not be null!");
    }

    public static boolean isEnabledAvoidDropFrame() {
        return FileDownloadMessageStation.isIntervalValid();
    }

    public static void setGlobalHandleSubPackageSize(int n) {
        if (n > 0) {
            FileDownloadMessageStation.SUB_PACKAGE_SIZE = n;
            return;
        }
        throw new IllegalArgumentException("sub package size must more than 0");
    }

    public static void setGlobalPost2UIInterval(int n) {
        FileDownloadMessageStation.INTERVAL = n;
    }

    public static void setup(Context context) {
        FileDownloadHelper.holdContext(context.getApplicationContext());
    }

    public static DownloadMgrInitialParams.InitCustomMaker setupOnApplicationOnCreate(Application object) {
        FileDownloadHelper.holdContext(object.getApplicationContext());
        object = new DownloadMgrInitialParams.InitCustomMaker();
        CustomComponentHolder.getImpl().setInitCustomMaker((DownloadMgrInitialParams.InitCustomMaker)object);
        return object;
    }

    public void addServiceConnectListener(FileDownloadConnectListener fileDownloadConnectListener) {
        FileDownloadEventPool.getImpl().addListener("event.service.connect.changed", fileDownloadConnectListener);
    }

    public void bindService() {
        if (!this.isServiceConnected()) {
            FileDownloadServiceProxy.getImpl().bindStartByContext(FileDownloadHelper.getAppContext());
        }
    }

    public void bindService(Runnable runnable) {
        if (this.isServiceConnected()) {
            runnable.run();
        } else {
            FileDownloadServiceProxy.getImpl().bindStartByContext(FileDownloadHelper.getAppContext(), runnable);
        }
    }

    public boolean clear(int n, String object) {
        this.pause(n);
        if (FileDownloadServiceProxy.getImpl().clearTaskData(n)) {
            File file = new File(FileDownloadUtils.getTempPath((String)object));
            if (file.exists()) {
                file.delete();
            }
            if (((File)(object = new File((String)object))).exists()) {
                ((File)object).delete();
            }
            return true;
        }
        return false;
    }

    public void clearAllTaskData() {
        this.pauseAll();
        FileDownloadServiceProxy.getImpl().clearAllTaskData();
    }

    public BaseDownloadTask create(String string2) {
        return new DownloadTask(string2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    ILostServiceConnectedHandler getLostConnectedHandler() {
        if (this.mLostConnectedHandler != null) return this.mLostConnectedHandler;
        Object object = INIT_LOST_CONNECTED_HANDLER_LOCK;
        synchronized (object) {
            if (this.mLostConnectedHandler != null) return this.mLostConnectedHandler;
            LostServiceConnectedHandler lostServiceConnectedHandler = new LostServiceConnectedHandler();
            this.mLostConnectedHandler = lostServiceConnectedHandler;
            this.addServiceConnectListener(lostServiceConnectedHandler);
            return this.mLostConnectedHandler;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    IQueuesHandler getQueuesHandler() {
        if (this.mQueuesHandler != null) return this.mQueuesHandler;
        Object object = INIT_QUEUES_HANDLER_LOCK;
        synchronized (object) {
            if (this.mQueuesHandler != null) return this.mQueuesHandler;
            QueuesHandler queuesHandler = new QueuesHandler();
            this.mQueuesHandler = queuesHandler;
            return this.mQueuesHandler;
        }
    }

    public long getSoFar(int n) {
        BaseDownloadTask.IRunningTask iRunningTask = FileDownloadList.getImpl().get(n);
        if (iRunningTask == null) {
            return FileDownloadServiceProxy.getImpl().getSofar(n);
        }
        return iRunningTask.getOrigin().getLargeFileSoFarBytes();
    }

    public byte getStatus(int n, String string2) {
        BaseDownloadTask.IRunningTask iRunningTask = FileDownloadList.getImpl().get(n);
        int n2 = iRunningTask == null ? FileDownloadServiceProxy.getImpl().getStatus(n) : iRunningTask.getOrigin().getStatus();
        int n3 = n2;
        if (string2 != null) {
            n3 = n2;
            if (n2 == 0) {
                n3 = n2;
                if (FileDownloadUtils.isFilenameConverted(FileDownloadHelper.getAppContext())) {
                    n3 = n2;
                    if (new File(string2).exists()) {
                        n3 = -3;
                    }
                }
            }
        }
        return (byte)n3;
    }

    public byte getStatus(String string2, String string3) {
        return this.getStatus(FileDownloadUtils.generateId(string2, string3), string3);
    }

    public byte getStatusIgnoreCompleted(int n) {
        return this.getStatus(n, null);
    }

    public long getTotal(int n) {
        BaseDownloadTask.IRunningTask iRunningTask = FileDownloadList.getImpl().get(n);
        if (iRunningTask == null) {
            return FileDownloadServiceProxy.getImpl().getTotal(n);
        }
        return iRunningTask.getOrigin().getLargeFileTotalBytes();
    }

    public FileDownloadLine insureServiceBind() {
        return new FileDownloadLine();
    }

    public FileDownloadLineAsync insureServiceBindAsync() {
        return new FileDownloadLineAsync();
    }

    public boolean isServiceConnected() {
        return FileDownloadServiceProxy.getImpl().isConnected();
    }

    public int pause(int n) {
        List<BaseDownloadTask.IRunningTask> list = FileDownloadList.getImpl().getDownloadingList(n);
        if (list != null && !list.isEmpty()) {
            Iterator<BaseDownloadTask.IRunningTask> iterator2 = list.iterator();
            while (iterator2.hasNext()) {
                iterator2.next().getOrigin().pause();
            }
            return list.size();
        }
        FileDownloadLog.w(this, "request pause but not exist %d", n);
        return 0;
    }

    public void pause(FileDownloadListener object) {
        FileDownloadTaskLauncher.getImpl().expire((FileDownloadListener)object);
        object = FileDownloadList.getImpl().copy((FileDownloadListener)object).iterator();
        while (object.hasNext()) {
            ((BaseDownloadTask.IRunningTask)object.next()).getOrigin().pause();
        }
    }

    public void pauseAll() {
        FileDownloadTaskLauncher.getImpl().expireAll();
        BaseDownloadTask.IRunningTask[] iRunningTaskArray = FileDownloadList.getImpl().copy();
        int n = iRunningTaskArray.length;
        for (int i = 0; i < n; ++i) {
            iRunningTaskArray[i].getOrigin().pause();
        }
        if (FileDownloadServiceProxy.getImpl().isConnected()) {
            FileDownloadServiceProxy.getImpl().pauseAllTasks();
        } else {
            PauseAllMarker.createMarker();
        }
    }

    public void removeServiceConnectListener(FileDownloadConnectListener fileDownloadConnectListener) {
        FileDownloadEventPool.getImpl().removeListener("event.service.connect.changed", fileDownloadConnectListener);
    }

    public int replaceListener(int n, FileDownloadListener fileDownloadListener) {
        BaseDownloadTask.IRunningTask iRunningTask = FileDownloadList.getImpl().get(n);
        if (iRunningTask == null) {
            return 0;
        }
        iRunningTask.getOrigin().setListener(fileDownloadListener);
        return iRunningTask.getOrigin().getId();
    }

    public int replaceListener(String string2, FileDownloadListener fileDownloadListener) {
        return this.replaceListener(string2, FileDownloadUtils.getDefaultSaveFilePath(string2), fileDownloadListener);
    }

    public int replaceListener(String string2, String string3, FileDownloadListener fileDownloadListener) {
        return this.replaceListener(FileDownloadUtils.generateId(string2, string3), fileDownloadListener);
    }

    public boolean setMaxNetworkThreadCount(int n) {
        if (!FileDownloadList.getImpl().isEmpty()) {
            FileDownloadLog.w(this, "Can't change the max network thread count, because there are actively executing tasks in FileDownloader, please try again after all actively executing tasks are completed or invoking FileDownloader#pauseAll directly.", new Object[0]);
            return false;
        }
        return FileDownloadServiceProxy.getImpl().setMaxNetworkThreadCount(n);
    }

    public boolean setTaskCompleted(String string2, String string3, long l) {
        FileDownloadLog.w(this, "If you invoked this method, please remove it directly feel free, it doesn't need any longer", new Object[0]);
        return true;
    }

    public boolean setTaskCompleted(List<FileDownloadTaskAtom> list) {
        FileDownloadLog.w(this, "If you invoked this method, please remove it directly feel free, it doesn't need any longer", new Object[0]);
        return true;
    }

    public boolean start(FileDownloadListener fileDownloadListener, boolean bl) {
        if (fileDownloadListener == null) {
            FileDownloadLog.w(this, "Tasks with the listener can't start, because the listener provided is null: [null, %B]", bl);
            return false;
        }
        bl = bl ? this.getQueuesHandler().startQueueSerial(fileDownloadListener) : this.getQueuesHandler().startQueueParallel(fileDownloadListener);
        return bl;
    }

    public void startForeground(int n, Notification notification) {
        FileDownloadServiceProxy.getImpl().startForeground(n, notification);
    }

    public void stopForeground(boolean bl) {
        FileDownloadServiceProxy.getImpl().stopForeground(bl);
    }

    public void unBindService() {
        if (this.isServiceConnected()) {
            FileDownloadServiceProxy.getImpl().unbindByContext(FileDownloadHelper.getAppContext());
        }
    }

    public boolean unBindServiceIfIdle() {
        if (!this.isServiceConnected()) {
            return false;
        }
        if (FileDownloadList.getImpl().isEmpty() && FileDownloadServiceProxy.getImpl().isIdle()) {
            this.unBindService();
            return true;
        }
        return false;
    }

    private static final class HolderClass {
        private static final FileDownloader INSTANCE = new FileDownloader();

        private HolderClass() {
        }
    }
}

