/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.SparseArray
 */
package com.liulishuo.filedownloader.services;

import android.util.SparseArray;
import com.liulishuo.filedownloader.download.DownloadLaunchRunnable;
import com.liulishuo.filedownloader.util.FileDownloadExecutors;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

class FileDownloadThreadPool {
    private int mIgnoreCheckTimes = 0;
    private int mMaxThreadCount;
    private ThreadPoolExecutor mThreadPool;
    private SparseArray<DownloadLaunchRunnable> runnablePool = new SparseArray();
    private final String threadPrefix;

    FileDownloadThreadPool(int n) {
        this.threadPrefix = "Network";
        this.mThreadPool = FileDownloadExecutors.newDefaultThreadPool(n, "Network");
        this.mMaxThreadCount = n;
    }

    private void filterOutNoExist() {
        synchronized (this) {
            SparseArray sparseArray = new SparseArray();
            int n = this.runnablePool.size();
            for (int i = 0; i < n; ++i) {
                int n2 = this.runnablePool.keyAt(i);
                DownloadLaunchRunnable downloadLaunchRunnable = (DownloadLaunchRunnable)this.runnablePool.get(n2);
                if (downloadLaunchRunnable == null) continue;
                if (!downloadLaunchRunnable.isAlive()) continue;
                sparseArray.put(n2, (Object)downloadLaunchRunnable);
            }
            try {
                this.runnablePool = sparseArray;
                return;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void cancel(int n) {
        this.filterOutNoExist();
        synchronized (this) {
            DownloadLaunchRunnable downloadLaunchRunnable = (DownloadLaunchRunnable)this.runnablePool.get(n);
            if (downloadLaunchRunnable != null) {
                downloadLaunchRunnable.pause();
                boolean bl = this.mThreadPool.remove(downloadLaunchRunnable);
                if (FileDownloadLog.NEED_LOG) {
                    FileDownloadLog.d(this, "successful cancel %d %B", n, bl);
                }
            }
            this.runnablePool.remove(n);
            return;
        }
    }

    public int exactSize() {
        synchronized (this) {
            this.filterOutNoExist();
            int n = this.runnablePool.size();
            return n;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void execute(DownloadLaunchRunnable downloadLaunchRunnable) {
        downloadLaunchRunnable.pending();
        synchronized (this) {
            this.runnablePool.put(downloadLaunchRunnable.getId(), (Object)downloadLaunchRunnable);
        }
        this.mThreadPool.execute(downloadLaunchRunnable);
        int n = this.mIgnoreCheckTimes;
        if (n >= 600) {
            this.filterOutNoExist();
            this.mIgnoreCheckTimes = 0;
            return;
        }
        this.mIgnoreCheckTimes = n + 1;
    }

    public int findRunningTaskIdBySameTempPath(String string2, int n) {
        synchronized (this) {
            int n2;
            if (string2 == null) {
                return 0;
            }
            try {
                n2 = this.runnablePool.size();
            }
            catch (Throwable throwable) {
                throw throwable;
            }
            for (int i = 0; i < n2; ++i) {
                DownloadLaunchRunnable downloadLaunchRunnable = (DownloadLaunchRunnable)this.runnablePool.valueAt(i);
                if (downloadLaunchRunnable == null) continue;
                if (!downloadLaunchRunnable.isAlive() || downloadLaunchRunnable.getId() == n || !string2.equals(downloadLaunchRunnable.getTempFilePath())) continue;
                n = downloadLaunchRunnable.getId();
                return n;
            }
            return 0;
        }
    }

    public List<Integer> getAllExactRunningDownloadIds() {
        synchronized (this) {
            int n;
            ArrayList<Integer> arrayList;
            try {
                this.filterOutNoExist();
                arrayList = new ArrayList<Integer>();
                n = 0;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
            while (true) {
                if (n >= this.runnablePool.size()) break;
                SparseArray<DownloadLaunchRunnable> sparseArray = this.runnablePool;
                arrayList.add(((DownloadLaunchRunnable)sparseArray.get(sparseArray.keyAt(n))).getId());
                ++n;
                continue;
                break;
            }
            return arrayList;
        }
    }

    public boolean isInThreadPool(int n) {
        synchronized (this) {
            boolean bl;
            block5: {
                block4: {
                    DownloadLaunchRunnable downloadLaunchRunnable = (DownloadLaunchRunnable)this.runnablePool.get(n);
                    if (downloadLaunchRunnable == null) break block4;
                    bl = downloadLaunchRunnable.isAlive();
                    if (!bl) break block4;
                    bl = true;
                    break block5;
                }
                bl = false;
            }
            return bl;
        }
    }

    public boolean setMaxNetworkThreadCount(int n) {
        synchronized (this) {
            block6: {
                if (this.exactSize() <= 0) break block6;
                FileDownloadLog.w(this, "Can't change the max network thread count, because the  network thread pool isn't in IDLE, please try again after all running tasks are completed or invoking FileDownloader#pauseAll directly.", new Object[0]);
                return false;
            }
            n = FileDownloadProperties.getValidNetworkThreadCount(n);
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "change the max network thread count, from %d to %d", this.mMaxThreadCount, n);
            }
            List<Runnable> list = this.mThreadPool.shutdownNow();
            this.mThreadPool = FileDownloadExecutors.newDefaultThreadPool(n, "Network");
            if (list.size() > 0) {
                FileDownloadLog.w(this, "recreate the network thread pool and discard %d tasks", list.size());
            }
            this.mMaxThreadCount = n;
            return true;
        }
    }
}

