/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 *  android.os.Handler$Callback
 *  android.os.Looper
 *  android.os.Message
 */
package com.liulishuo.filedownloader;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.liulishuo.filedownloader.IFileDownloadMessenger;
import com.liulishuo.filedownloader.util.FileDownloadExecutors;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

public class FileDownloadMessageStation {
    private static final Executor BLOCK_COMPLETED_POOL = FileDownloadExecutors.newDefaultThreadPool(5, "BlockCompleted");
    public static final int DEFAULT_INTERVAL = 10;
    public static final int DEFAULT_SUB_PACKAGE_SIZE = 5;
    static final int DISPOSE_MESSENGER_LIST = 2;
    static final int HANDOVER_A_MESSENGER = 1;
    static int INTERVAL = 10;
    static int SUB_PACKAGE_SIZE = 5;
    private final ArrayList<IFileDownloadMessenger> disposingList;
    private final Handler handler;
    private final Object queueLock = new Object();
    private final LinkedBlockingQueue<IFileDownloadMessenger> waitingQueue;

    private FileDownloadMessageStation() {
        this.disposingList = new ArrayList();
        this.handler = new Handler(Looper.getMainLooper(), (Handler.Callback)new UIHandlerCallback());
        this.waitingQueue = new LinkedBlockingQueue();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void enqueue(IFileDownloadMessenger iFileDownloadMessenger) {
        Object object = this.queueLock;
        synchronized (object) {
            this.waitingQueue.offer(iFileDownloadMessenger);
        }
        this.push();
    }

    public static FileDownloadMessageStation getImpl() {
        return HolderClass.INSTANCE;
    }

    private void handoverInUIThread(IFileDownloadMessenger iFileDownloadMessenger) {
        Handler handler = this.handler;
        handler.sendMessage(handler.obtainMessage(1, (Object)iFileDownloadMessenger));
    }

    private static boolean interceptBlockCompleteMessage(IFileDownloadMessenger iFileDownloadMessenger) {
        if (iFileDownloadMessenger.isBlockingCompleted()) {
            BLOCK_COMPLETED_POOL.execute(new Runnable(iFileDownloadMessenger){
                final IFileDownloadMessenger val$messenger;
                {
                    this.val$messenger = iFileDownloadMessenger;
                }

                @Override
                public void run() {
                    this.val$messenger.handoverMessage();
                }
            });
            return true;
        }
        return false;
    }

    public static boolean isIntervalValid() {
        boolean bl = INTERVAL > 0;
        return bl;
    }

    /*
     * Loose catch block
     * WARNING - void declaration
     */
    private void push() {
        Handler handler;
        int n;
        Object object = this.queueLock;
        synchronized (object) {
            block14: {
                block13: {
                    if (!this.disposingList.isEmpty()) {
                        return;
                    }
                    if (this.waitingQueue.isEmpty()) {
                        return;
                    }
                    if (FileDownloadMessageStation.isIntervalValid()) break block13;
                    this.waitingQueue.drainTo(this.disposingList);
                    n = 0;
                    break block14;
                }
                int n2 = INTERVAL;
                int n3 = Math.min(this.waitingQueue.size(), SUB_PACKAGE_SIZE);
                int n4 = 0;
                while (true) {
                    n = n2;
                    if (n4 >= n3) break;
                    this.disposingList.add((IFileDownloadMessenger)this.waitingQueue.remove());
                    ++n4;
                    continue;
                    break;
                }
            }
            // MONITOREXIT @DISABLED, blocks:[4, 6] lbl29 : MonitorExitStatement: MONITOREXIT : var6_1
            handler = this.handler;
        }
        handler.sendMessageDelayed(handler.obtainMessage(2, this.disposingList), (long)n);
        return;
        {
            catch (Throwable throwable) {
                while (true) {
                    void var5_8;
                    try {}
                    catch (Throwable throwable2) {
                        continue;
                    }
                    throw var5_8;
                }
            }
        }
    }

    void requestEnqueue(IFileDownloadMessenger iFileDownloadMessenger) {
        this.requestEnqueue(iFileDownloadMessenger, false);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void requestEnqueue(IFileDownloadMessenger iFileDownloadMessenger, boolean bl) {
        if (iFileDownloadMessenger.handoverDirectly()) {
            iFileDownloadMessenger.handoverMessage();
            return;
        }
        if (FileDownloadMessageStation.interceptBlockCompleteMessage(iFileDownloadMessenger)) {
            return;
        }
        if (!FileDownloadMessageStation.isIntervalValid() && !this.waitingQueue.isEmpty()) {
            Object object = this.queueLock;
            synchronized (object) {
                if (!this.waitingQueue.isEmpty()) {
                    Iterator<IFileDownloadMessenger> iterator2 = this.waitingQueue.iterator();
                    while (iterator2.hasNext()) {
                        this.handoverInUIThread(iterator2.next());
                    }
                }
                this.waitingQueue.clear();
            }
        }
        if (FileDownloadMessageStation.isIntervalValid() && !bl) {
            this.enqueue(iFileDownloadMessenger);
            return;
        }
        this.handoverInUIThread(iFileDownloadMessenger);
    }

    private static final class HolderClass {
        private static final FileDownloadMessageStation INSTANCE = new FileDownloadMessageStation();

        private HolderClass() {
        }
    }

    private static class UIHandlerCallback
    implements Handler.Callback {
        private UIHandlerCallback() {
        }

        private void dispose(ArrayList<IFileDownloadMessenger> arrayList) {
            for (IFileDownloadMessenger iFileDownloadMessenger : arrayList) {
                if (FileDownloadMessageStation.interceptBlockCompleteMessage(iFileDownloadMessenger)) continue;
                iFileDownloadMessenger.handoverMessage();
            }
            arrayList.clear();
        }

        public boolean handleMessage(Message message) {
            if (message.what == 1) {
                ((IFileDownloadMessenger)message.obj).handoverMessage();
            } else if (message.what == 2) {
                this.dispose((ArrayList)message.obj);
                FileDownloadMessageStation.getImpl().push();
            }
            return true;
        }
    }
}

