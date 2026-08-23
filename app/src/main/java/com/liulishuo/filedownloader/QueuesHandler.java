/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 *  android.os.Handler$Callback
 *  android.os.HandlerThread
 *  android.os.Message
 *  android.util.SparseArray
 */
package com.liulishuo.filedownloader;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.SparseArray;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadList;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadMonitor;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.IQueuesHandler;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;

class QueuesHandler
implements IQueuesHandler {
    static final int WHAT_FREEZE = 2;
    static final int WHAT_SERIAL_NEXT = 1;
    static final int WHAT_UNFREEZE = 3;
    private final SparseArray<Handler> mRunningSerialMap = new SparseArray();

    QueuesHandler() {
    }

    private void freezeSerialHandler(Handler handler) {
        handler.sendEmptyMessage(2);
    }

    private boolean onAssembledTasksToStart(int n, List<BaseDownloadTask.IRunningTask> list, FileDownloadListener fileDownloadListener, boolean bl) {
        if (FileDownloadMonitor.isValid()) {
            FileDownloadMonitor.getMonitor().onRequestStart(list.size(), true, fileDownloadListener);
        }
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.v(FileDownloader.class, "start list attachKey[%d] size[%d] listener[%s] isSerial[%B]", n, list.size(), fileDownloadListener, bl);
        }
        if (list != null && !list.isEmpty()) {
            return false;
        }
        FileDownloadLog.w(FileDownloader.class, "Tasks with the listener can't start, because can't find any task with the provided listener, maybe tasks instance has been started in the past, so they are all are inUsing, if in this case, you can use [BaseDownloadTask#reuse] to reuse theme first then start again: [%s, %B]", fileDownloadListener, bl);
        return true;
    }

    private void unFreezeSerialHandler(Handler handler) {
        handler.sendEmptyMessage(3);
    }

    @Override
    public boolean contain(int n) {
        boolean bl = this.mRunningSerialMap.get(n) != null;
        return bl;
    }

    @Override
    public void freezeAllSerialQueues() {
        for (int i = 0; i < this.mRunningSerialMap.size(); ++i) {
            int n = this.mRunningSerialMap.keyAt(i);
            this.freezeSerialHandler((Handler)this.mRunningSerialMap.get(n));
        }
    }

    @Override
    public int serialQueueSize() {
        return this.mRunningSerialMap.size();
    }

    @Override
    public boolean startQueueParallel(FileDownloadListener object) {
        List<BaseDownloadTask.IRunningTask> list;
        int n = object.hashCode();
        if (this.onAssembledTasksToStart(n, list = FileDownloadList.getImpl().assembleTasksToStart(n, (FileDownloadListener)object), (FileDownloadListener)object, false)) {
            return false;
        }
        object = list.iterator();
        while (object.hasNext()) {
            ((BaseDownloadTask.IRunningTask)object.next()).startTaskByQueue();
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean startQueueSerial(FileDownloadListener fileDownloadListener) {
        List<BaseDownloadTask.IRunningTask> list;
        SparseArray<Handler> sparseArray = new SparseArray<Handler>(this);
        int n = sparseArray.hashCode();
        if (this.onAssembledTasksToStart(n, list = FileDownloadList.getImpl().assembleTasksToStart(n, fileDownloadListener), fileDownloadListener, true)) {
            return false;
        }
        fileDownloadListener = new HandlerThread(FileDownloadUtils.formatString("filedownloader serial thread %s-%d", fileDownloadListener, n));
        fileDownloadListener.start();
        fileDownloadListener = new Handler(fileDownloadListener.getLooper(), (Handler.Callback)sparseArray);
        sparseArray.setHandler((Handler)fileDownloadListener);
        sparseArray.setList(list);
        ((SerialHandlerCallback)sparseArray).goNext(0);
        sparseArray = this.mRunningSerialMap;
        synchronized (sparseArray) {
            this.mRunningSerialMap.put(n, (Object)fileDownloadListener);
            return true;
        }
    }

    @Override
    public void unFreezeSerialQueues(List<Integer> object) {
        Iterator<Integer> iterator2 = object.iterator();
        while (iterator2.hasNext()) {
            object = iterator2.next();
            this.unFreezeSerialHandler((Handler)this.mRunningSerialMap.get(((Integer)object).intValue()));
        }
    }

    private static class SerialFinishListener
    implements BaseDownloadTask.FinishListener {
        private int nextIndex;
        private final WeakReference<SerialHandlerCallback> wSerialHandlerCallback;

        private SerialFinishListener(WeakReference<SerialHandlerCallback> weakReference) {
            this.wSerialHandlerCallback = weakReference;
        }

        @Override
        public void over(BaseDownloadTask weakReference) {
            weakReference = this.wSerialHandlerCallback;
            if (weakReference != null && weakReference.get() != null) {
                ((SerialHandlerCallback)this.wSerialHandlerCallback.get()).goNext(this.nextIndex);
            }
        }

        public BaseDownloadTask.FinishListener setNextIndex(int n) {
            this.nextIndex = n;
            return this;
        }
    }

    private class SerialHandlerCallback
    implements Handler.Callback {
        private Handler mHandler;
        private List<BaseDownloadTask.IRunningTask> mList;
        private int mRunningIndex;
        private SerialFinishListener mSerialFinishListener;
        final QueuesHandler this$0;

        SerialHandlerCallback(QueuesHandler queuesHandler) {
            this.this$0 = queuesHandler;
            this.mRunningIndex = 0;
            this.mSerialFinishListener = new SerialFinishListener(new WeakReference<SerialHandlerCallback>(this));
        }

        private void goNext(int n) {
            Object object = this.mHandler;
            if (object != null && this.mList != null) {
                Message message = object.obtainMessage();
                message.what = 1;
                message.arg1 = n;
                if (FileDownloadLog.NEED_LOG) {
                    List<BaseDownloadTask.IRunningTask> list = this.mList;
                    object = null;
                    if (list != null && list.get(0) != null) {
                        object = this.mList.get(0).getOrigin().getListener();
                    }
                    FileDownloadLog.d(SerialHandlerCallback.class, "start next %s %s", object, message.arg1);
                }
                this.mHandler.sendMessage(message);
                return;
            }
            FileDownloadLog.w(this, "need go next %d, but params is not ready %s %s", n, this.mHandler, this.mList);
        }

        public void freeze() {
            this.mList.get(this.mRunningIndex).getOrigin().removeFinishListener(this.mSerialFinishListener);
            this.mHandler.removeCallbacksAndMessages(null);
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public boolean handleMessage(Message message) {
            if (message.what == 1) {
                int n;
                if (message.arg1 >= this.mList.size()) {
                    Object object = this.this$0.mRunningSerialMap;
                    synchronized (object) {
                        this.this$0.mRunningSerialMap.remove(this.mList.get(0).getAttachKey());
                    }
                    Object object2 = this.mHandler;
                    object = null;
                    if (object2 != null && object2.getLooper() != null) {
                        this.mHandler.getLooper().quit();
                        this.mHandler = null;
                        this.mList = null;
                        this.mSerialFinishListener = null;
                    }
                    if (!FileDownloadLog.NEED_LOG) return true;
                    object2 = this.mList;
                    if (object2 != null && object2.get(0) != null) {
                        object = this.mList.get(0).getOrigin().getListener();
                    }
                    FileDownloadLog.d(SerialHandlerCallback.class, "final serial %s %d", object, message.arg1);
                    return true;
                }
                this.mRunningIndex = n = message.arg1;
                BaseDownloadTask.IRunningTask iRunningTask = this.mList.get(n);
                Object object = iRunningTask.getPauseLock();
                synchronized (object) {
                    if (iRunningTask.getOrigin().getStatus() == 0 && !FileDownloadList.getImpl().isNotContains(iRunningTask)) {
                        iRunningTask.getOrigin().addFinishListener(this.mSerialFinishListener.setNextIndex(this.mRunningIndex + 1));
                        iRunningTask.startTaskByQueue();
                        return true;
                    }
                    if (FileDownloadLog.NEED_LOG) {
                        FileDownloadLog.d(SerialHandlerCallback.class, "direct go next by not contains %s %d", iRunningTask, message.arg1);
                    }
                    this.goNext(message.arg1 + 1);
                    return true;
                }
            }
            if (message.what == 2) {
                this.freeze();
                return true;
            }
            if (message.what != 3) return true;
            this.unfreeze();
            return true;
        }

        public void setHandler(Handler handler) {
            this.mHandler = handler;
        }

        public void setList(List<BaseDownloadTask.IRunningTask> list) {
            this.mList = list;
        }

        public void unfreeze() {
            this.goNext(this.mRunningIndex);
        }
    }
}

