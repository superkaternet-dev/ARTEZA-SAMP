/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader;

import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.ITaskHunter;
import com.liulishuo.filedownloader.MessageSnapshotGate;
import com.liulishuo.filedownloader.message.MessageSnapshotFlow;
import com.liulishuo.filedownloader.util.FileDownloadExecutors;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

class FileDownloadTaskLauncher {
    private final LaunchTaskPool mLaunchTaskPool = new LaunchTaskPool();

    FileDownloadTaskLauncher() {
    }

    public static FileDownloadTaskLauncher getImpl() {
        return HolderClass.INSTANCE;
    }

    void expire(FileDownloadListener fileDownloadListener) {
        synchronized (this) {
            this.mLaunchTaskPool.expire(fileDownloadListener);
            return;
        }
    }

    void expire(ITaskHunter.IStarter iStarter) {
        synchronized (this) {
            this.mLaunchTaskPool.expire(iStarter);
            return;
        }
    }

    void expireAll() {
        synchronized (this) {
            this.mLaunchTaskPool.expireAll();
            return;
        }
    }

    void launch(ITaskHunter.IStarter iStarter) {
        synchronized (this) {
            this.mLaunchTaskPool.asyncExecute(iStarter);
            return;
        }
    }

    private static class HolderClass {
        private static final FileDownloadTaskLauncher INSTANCE = new FileDownloadTaskLauncher();

        static {
            MessageSnapshotFlow.getImpl().setReceiver(new MessageSnapshotGate());
        }

        private HolderClass() {
        }
    }

    private static class LaunchTaskPool {
        private ThreadPoolExecutor mPool;
        private LinkedBlockingQueue<Runnable> mWorkQueue;

        LaunchTaskPool() {
            this.init();
        }

        private void init() {
            LinkedBlockingQueue<Runnable> linkedBlockingQueue = new LinkedBlockingQueue<Runnable>();
            this.mWorkQueue = linkedBlockingQueue;
            this.mPool = FileDownloadExecutors.newDefaultThreadPool(3, linkedBlockingQueue, "LauncherTask");
        }

        public void asyncExecute(ITaskHunter.IStarter iStarter) {
            this.mPool.execute(new LaunchTaskRunnable(iStarter));
        }

        public void expire(FileDownloadListener object2) {
            if (object2 == null) {
                FileDownloadLog.w(this, "want to expire by listener, but the listener provided is null", new Object[0]);
                return;
            }
            ArrayList<Runnable> arrayList = new ArrayList<Runnable>();
            for (Runnable runnable : this.mWorkQueue) {
                LaunchTaskRunnable launchTaskRunnable = (LaunchTaskRunnable)runnable;
                if (!launchTaskRunnable.isSameListener((FileDownloadListener)object2)) continue;
                launchTaskRunnable.expire();
                arrayList.add(runnable);
            }
            if (arrayList.isEmpty()) {
                return;
            }
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "expire %d tasks with listener[%s]", arrayList.size(), object2);
            }
            for (Runnable runnable : arrayList) {
                this.mPool.remove(runnable);
            }
        }

        public void expire(ITaskHunter.IStarter iStarter) {
            this.mWorkQueue.remove(iStarter);
        }

        public void expireAll() {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "expire %d tasks", this.mWorkQueue.size());
            }
            this.mPool.shutdownNow();
            this.init();
        }
    }

    private static class LaunchTaskRunnable
    implements Runnable {
        private boolean mExpired;
        private final ITaskHunter.IStarter mTaskStarter;

        LaunchTaskRunnable(ITaskHunter.IStarter iStarter) {
            this.mTaskStarter = iStarter;
            this.mExpired = false;
        }

        public boolean equals(Object object) {
            boolean bl = super.equals(object) || object == this.mTaskStarter;
            return bl;
        }

        public void expire() {
            this.mExpired = true;
        }

        public boolean isSameListener(FileDownloadListener fileDownloadListener) {
            ITaskHunter.IStarter iStarter = this.mTaskStarter;
            boolean bl = iStarter != null && iStarter.equalListener(fileDownloadListener);
            return bl;
        }

        @Override
        public void run() {
            if (this.mExpired) {
                return;
            }
            this.mTaskStarter.start();
        }
    }
}

